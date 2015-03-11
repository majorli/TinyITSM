package com.jeans.tinyitsm.service.cloud.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeans.tinyitsm.dao.BaseDao;
import com.jeans.tinyitsm.event.CloudEvent;
import com.jeans.tinyitsm.event.CloudEventType;
import com.jeans.tinyitsm.event.itsm.EventListener;
import com.jeans.tinyitsm.model.CloudUnit;
import com.jeans.tinyitsm.model.cloud.CloudFile;
import com.jeans.tinyitsm.model.cloud.CloudList;
import com.jeans.tinyitsm.model.cloud.FavorList;
import com.jeans.tinyitsm.model.cloud.Favorite;
import com.jeans.tinyitsm.model.cloud.Push;
import com.jeans.tinyitsm.model.cloud.Subscribe;
import com.jeans.tinyitsm.model.cloud.Tag;
import com.jeans.tinyitsm.model.portal.User;
import com.jeans.tinyitsm.model.view.CloudTreeNode;
import com.jeans.tinyitsm.model.view.Grid;
import com.jeans.tinyitsm.model.view.PropertyGridRow;
import com.jeans.tinyitsm.service.cloud.CloudConstants;
import com.jeans.tinyitsm.service.cloud.CloudService;
import com.jeans.tinyitsm.service.cloud.TagService;
import com.jeans.tinyitsm.util.FileUtil;

@Service
public class CloudServiceImpl implements CloudService {

	private TagService tagService;

	private BaseDao<User> userDao;
	private BaseDao<CloudList> cloudListDao;
	private BaseDao<FavorList> favorListDao;
	private BaseDao<Favorite> favoriteDao;
	private BaseDao<Subscribe> subscribeDao;
	private BaseDao<Push> pushDao;
	private BaseDao<CloudFile> cloudFileDao;

	private EventListener<CloudEvent> eventHandler;

	@Autowired
	public void setTagService(TagService tagService) {
		this.tagService = tagService;
	}

	@Autowired
	public void setUserDao(BaseDao<User> userDao) {
		this.userDao = userDao;
	}

	@Autowired
	public void setCloudListDao(BaseDao<CloudList> cloudListDao) {
		this.cloudListDao = cloudListDao;
	}

	@Autowired
	public void setFavorListDao(BaseDao<FavorList> favorListDao) {
		this.favorListDao = favorListDao;
	}

	@Autowired
	public void setFavoriteDao(BaseDao<Favorite> favoriteDao) {
		this.favoriteDao = favoriteDao;
	}

	@Autowired
	public void setSubscribeDao(BaseDao<Subscribe> subscribeDao) {
		this.subscribeDao = subscribeDao;
	}

	@Autowired
	public void setPushDao(BaseDao<Push> pushDao) {
		this.pushDao = pushDao;
	}

	@Autowired
	public void setCloudFileDao(BaseDao<CloudFile> cloudFileDao) {
		this.cloudFileDao = cloudFileDao;
	}

	@Autowired
	public void setEventHandler(EventListener<CloudEvent> eventHandler) {
		this.eventHandler = eventHandler;
	}

	@Override
	public List<CloudTreeNode> getTreeOutline() {
		List<CloudTreeNode> tree = new ArrayList<CloudTreeNode>();
		tree.add(CloudTreeNode.getRootNodeInstance(CloudConstants.FILES_ROOT));
		tree.add(CloudTreeNode.getRootNodeInstance(CloudConstants.FAVORITES_ROOT));
		tree.add(CloudTreeNode.getRootNodeInstance(CloudConstants.SUBSCRIPTIONS_ROOT));
		tree.add(CloudTreeNode.getRootNodeInstance(CloudConstants.PUSHES_ROOT));
		return tree;
	}

	/**
	 * <b>父子节点对应关系及子节点id和nodeId的取值</b> <code>
	 * <li>FILES_ROOT -> LIST, id = CloudList.id + 10000000, nodeId = CloudList.id
	 * <li>FAVORITES_ROOT -> FAVOR_LIST, id = FavorList.id + 20000000, nodeId = FavorList.id
	 * <li>SUBSCRIPTIONS_ROOT -> RSS_LIST, id = Subscribe.id + 30000000, nodeId = Subscribe.list.id
	 * <li>PUSHES_ROOT -> PUSH_LIST, id = Push.id + 40000000, nodeId = Push.unitId
	 * <li>PUSHES_ROOT -> PUSH_ROOT_LINK, id = Push.id + 70000000, nodeId = Push.unitId
	 * <li>LIST -> FILE, id = CloudFile.id, nodeId = CloudFile.id
	 * <li>FAVOR_LIST -> FAVOR_LIST_LINK, id = Favorite.id + 50000000, nodeId = Favorite.file.id
	 * <li>RSS_LIST -> RSS_LIST_LINK, id = Subscribe.list.files[x].id + 60000000, nodeId = Subscribe.list.files[x].id
	 * <li>PUSH_LIST -> PUSH_LIST_LINK, id = CloudList(Push.unitId).files[x].id + 80000000, nodeId = CloudList(Push.unitId).files[x].id
	 * </code> <br>
	 * 检索指向别的用户的CloudList或者CloudList下的CloudFile时要注意权限过滤<br>
	 * 过滤条件为<code>privateUnit = false and (permittedReaders is empty or user member of permittedReaders)</code>
	 */
	@Override
	@Transactional(readOnly = true)
	public List<CloudTreeNode> getChildren(long id, byte type, User user) {
		List<CloudTreeNode> children = new ArrayList<CloudTreeNode>();
		StringBuilder hqlBuilder = new StringBuilder();
		Map<String, Object> params = new HashMap<String, Object>();

		switch (type) {
		case CloudConstants.FILES_ROOT:
			hqlBuilder.append("from CloudList where owner = :p_owner");
			params.put("p_owner", user);
			List<CloudList> cl = cloudListDao.find(hqlBuilder.toString(), params);
			for (CloudList l : cl) {
				children.add(CloudTreeNode.getInstance(l.getId(), CloudConstants.LIST, l.getId(), l.getName(), null));
			}
			break;
		case CloudConstants.FAVORITES_ROOT:
			hqlBuilder.append("from FavorList where owner = :p_owner");
			params.put("p_owner", user);
			List<FavorList> fl = favorListDao.find(hqlBuilder.toString(), params);
			for (FavorList l : fl) {
				children.add(CloudTreeNode.getInstance(l.getId(), CloudConstants.FAVOR_LIST, l.getId(), l.getName(), null));
			}
			break;
		case CloudConstants.SUBSCRIPTIONS_ROOT: // 要检查权限
			hqlBuilder.append("from Subscribe where subscriber = :p_subscriber");
			params.put("p_subscriber", user);
			List<Subscribe> sl = subscribeDao.find(hqlBuilder.toString(), params);
			for (Subscribe l : sl) {
				CloudList list = l.getList();
				if (FileUtil.checkPermission(list, user)) {
					children.add(CloudTreeNode.getInstance(l.getId(), CloudConstants.RSS_LIST, list.getId(), list.getName(), null));
				}
			}
			break;
		case CloudConstants.PUSHES_ROOT: // 要检查权限
			hqlBuilder.append("from Push where :p_user member of users order by type, id");
			params.put("p_user", user);
			List<Push> pl = pushDao.find(hqlBuilder.toString(), params);
			List<CloudTreeNode> pushedLists = new ArrayList<CloudTreeNode>();
			List<CloudTreeNode> pushedFiles = new ArrayList<CloudTreeNode>();
			for (Push p : pl) {
				if (p.getType() == CloudConstants.CLOUD_LIST) {
					CloudList pushedList = cloudListDao.getById(CloudList.class, p.getUnitId());
					if (FileUtil.checkPermission(pushedList, user)) {
						pushedLists.add(CloudTreeNode.getInstance(p.getId(), CloudConstants.PUSH_LIST, pushedList.getId(), pushedList.getName(), null));
					}
				} else if (p.getType() == CloudConstants.CLOUD_FILE) {
					CloudFile pushedFile = cloudFileDao.getById(CloudFile.class, p.getUnitId());
					if (FileUtil.checkPermission(pushedFile, user)) {
						pushedFiles.add(CloudTreeNode.getInstance(p.getId(), CloudConstants.PUSHES_ROOT_LINK, pushedFile.getId(), pushedFile.getName(),
								pushedFile.getIconCls()));
					}
				}
			}
			Collections.sort(pushedLists);
			Collections.sort(pushedFiles);
			children.addAll(pushedLists);
			children.addAll(pushedFiles);
			break;
		case CloudConstants.LIST:
			CloudList parentList = cloudListDao.getById(CloudList.class, id);
			if (null == parentList)
				break;
			for (CloudFile f : parentList.getFiles()) {
				children.add(CloudTreeNode.getInstance(f.getId(), CloudConstants.FILE, f.getId(), f.getName(), f.getIconCls()));
			}
			break;
		case CloudConstants.FAVOR_LIST: // 要检查权限
			FavorList parentFavorList = favorListDao.getById(FavorList.class, id);
			if (null == parentFavorList)
				break;
			for (Favorite fa : parentFavorList.getFavors()) {
				CloudFile f = fa.getFile();
				if (FileUtil.checkPermission(f, user)) {
					children.add(CloudTreeNode.getInstance(fa.getId(), CloudConstants.FAVOR_LIST_LINK, f.getId(), f.getName(), f.getIconCls()));
				}
			}
			break;
		case CloudConstants.RSS_LIST: // 要检查权限
			CloudList parentSubList = cloudListDao.getById(CloudList.class, id);
			if (null == parentSubList)
				break;
			for (CloudFile f : parentSubList.getFiles()) {
				if (FileUtil.checkPermission(f, user)) {
					children.add(CloudTreeNode.getInstance(f.getId(), CloudConstants.RSS_LIST_LINK, f.getId(), f.getName(), f.getIconCls()));
				}
			}
			break;
		case CloudConstants.PUSH_LIST: // 要检查权限
			CloudList parentPushList = cloudListDao.getById(CloudList.class, id);
			if (null == parentPushList)
				break;
			for (CloudFile f : parentPushList.getFiles()) {
				if (FileUtil.checkPermission(f, user)) {
					children.add(CloudTreeNode.getInstance(f.getId(), CloudConstants.PUSH_LIST_LINK, f.getId(), f.getName(), f.getIconCls()));
				}
			}
		default:
		}
		if (type != CloudConstants.PUSHES_ROOT) {
			Collections.sort(children);
		}
		return children;
	}

	@Override
	@Transactional
	public CloudTreeNode createList(User owner, String name, String tags, boolean isPrivate, Set<Long> permittedReaderIds) {
		CloudList list = new CloudList();
		String listName = null;
		if (StringUtils.isBlank(name)) {
			listName = "新栏目";
		} else {
			listName = name.trim();
		}
		list.setName(listName);
		list.setOwner(owner);
		list.setPrivateUnit(isPrivate);
		if (!isPrivate) {
			// 是可公开的项目，从User表中获取有权限人员集合
			if (null == permittedReaderIds || permittedReaderIds.size() == 0) {
				// 公开权限
				list.setPermittedReaders(new HashSet<User>());
			} else {
				if (permittedReaderIds.contains(owner.getId())) {
					permittedReaderIds.remove(owner.getId());
					if (permittedReaderIds.isEmpty()) {
						// 有限访问权限但是只指定了栏目所有人本人为有权访问的，转为私有权限
						list.setPrivateUnit(true);
					} else {
						Map<String, Object> params = new HashMap<String, Object>();
						params.put("p_ids", permittedReaderIds);
						List<User> readers = userDao.find("from User where id in (:p_ids)", params);
						list.setPermittedReaders(new HashSet<User>(readers));
					}
				}
			}
		}
		list.setTags(tagService.loadAndUpdate(tags));
		list.setCreateTime(new Date());
		list.setLastUpdateTime(new Date());
		cloudListDao.save(list);

		CloudTreeNode node = CloudTreeNode.getInstance(list.getId(), CloudConstants.LIST, list.getId(), list.getName(), null);
		return node;
	}

	@Override
	@Transactional
	public CloudTreeNode createFavorList(User owner, String name) {
		FavorList list = new FavorList();
		String listName = null;
		if (StringUtils.isBlank(name)) {
			listName = "新收藏夹";
		} else {
			listName = name.trim();
		}
		list.setName(listName);
		list.setOwner(owner);
		favorListDao.save(list);

		CloudTreeNode node = CloudTreeNode.getInstance(list.getId(), CloudConstants.FAVOR_LIST, list.getId(), list.getName(), null);
		return node;
	}

	@Override
	@Transactional(readOnly = true)
	public Grid<PropertyGridRow> getProperties(long id, byte type) {
		Grid<PropertyGridRow> props = null;
		switch (type) {
		case CloudConstants.LIST:
		case CloudConstants.RSS_LIST:
		case CloudConstants.PUSH_LIST:
			props = getListProperties(cloudListDao.getById(CloudList.class, id));
			break;
		case CloudConstants.FILE:
		case CloudConstants.FAVOR_LIST_LINK:
		case CloudConstants.RSS_LIST_LINK:
		case CloudConstants.PUSHES_ROOT_LINK:
		case CloudConstants.PUSH_LIST_LINK:
			props = getFileProperties(cloudFileDao.getById(CloudFile.class, id));
			break;
		default:
			props = new Grid<PropertyGridRow>();
		}
		return props;
	}

	private Grid<PropertyGridRow> getListProperties(CloudList list) {
		Grid<PropertyGridRow> props = new Grid<PropertyGridRow>();
		if (null != list) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			List<PropertyGridRow> ps = new ArrayList<PropertyGridRow>();
			ps.add(new PropertyGridRow("创建人", list.getOwner().getUsername(), "基本属性", null));
			ps.add(new PropertyGridRow("创建时间", df.format(list.getCreateTime()), "基本属性", null));
			ps.add(new PropertyGridRow("修改时间", df.format(list.getLastUpdateTime()), "基本属性", null));
			ps.add(new PropertyGridRow("订阅次数", Long.toString(list.getSubscribeCount()), "基本属性", null));
			if (list.isPrivateUnit()) {
				ps.add(new PropertyGridRow("访问权限", "私有", "权限属性", null));
			} else {
				Set<User> permittedReaders = list.getPermittedReaders();
				if (permittedReaders.size() == 0) {
					ps.add(new PropertyGridRow("访问权限", "公开", "权限属性", null));
				} else {
					ps.add(new PropertyGridRow("访问权限", "有限", "权限属性", null));
					for (User reader : permittedReaders) {
						ps.add(new PropertyGridRow("可访问用户", reader.getUsername(), "权限属性", null));
					}
				}
			}
			Set<Tag> tags = list.getTags();
			ps.add(new PropertyGridRow("标签数", Integer.toString(tags.size()), "分类属性", null));
			for (Tag tag : tags) {
				ps.add(new PropertyGridRow("标签", tag.getTitle(), "分类属性", null));
			}
			props.setTotal(ps.size());
			props.setRows(ps);
		}
		return props;
	}

	private Grid<PropertyGridRow> getFileProperties(CloudFile file) {
		Grid<PropertyGridRow> props = new Grid<PropertyGridRow>();
		if (null != file) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			List<PropertyGridRow> ps = new ArrayList<PropertyGridRow>();
			ps.add(new PropertyGridRow("上传人", file.getOwner().getUsername(), "基本属性", null));
			ps.add(new PropertyGridRow("文件类型", FileUtil.getContentTypeName(file), "基本属性", null));
			ps.add(new PropertyGridRow("文件长度", Long.toString(file.getSize()), "基本属性", null));
			ps.add(new PropertyGridRow("上传时间", df.format(file.getCreateTime()), "基本属性", null));
			ps.add(new PropertyGridRow("修改时间", df.format(file.getLastUpdateTime()), "基本属性", null));
			ps.add(new PropertyGridRow("当前版本", file.getVersion(), "基本属性", null));
			ps.add(new PropertyGridRow("下载次数", Long.toString(file.getDlCount()), "基本属性", null));
			ps.add(new PropertyGridRow("收藏次数", Long.toString(file.getFvCount()), "基本属性", null));
			if (file.isPrivateUnit()) {
				ps.add(new PropertyGridRow("访问权限", "私有", "权限属性", null));
			} else {
				Set<User> permittedReaders = file.getPermittedReaders();
				if (permittedReaders.size() == 0) {
					ps.add(new PropertyGridRow("访问权限", "公开", "权限属性", null));
				} else {
					ps.add(new PropertyGridRow("访问权限", "有限", "权限属性", null));
					for (User reader : permittedReaders) {
						ps.add(new PropertyGridRow("可访问用户", reader.getUsername(), "权限属性", null));
					}
				}
			}
			ps.add(new PropertyGridRow("栏目", file.getList().getName(), "分类属性", null));
			Set<Tag> tags = file.getTags();
			ps.add(new PropertyGridRow("标签数", Integer.toString(tags.size()), "分类属性", null));
			for (Tag tag : tags) {
				ps.add(new PropertyGridRow("标签", tag.getTitle(), "分类属性", null));
			}
			props.setTotal(ps.size());
			props.setRows(ps);
		}
		return props;
	}

	@Override
	@Transactional(readOnly = true)
	public String getBrief(long id) {
		CloudFile file = cloudFileDao.getById(CloudFile.class, id);
		if (null == file) {
			return null;
		} else {
			return file.getBrief();
		}
	}

	@Override
	@Transactional(readOnly = true)
	public String getTags(long id, byte type) {
		CloudUnit unit = null;
		if (type == CloudConstants.FILE) {
			unit = cloudFileDao.getById(CloudFile.class, id);
		} else if (type == CloudConstants.LIST) {
			unit = cloudListDao.getById(CloudList.class, id);
		}
		if (null == unit) {
			return "";
		}
		StringBuilder builder = new StringBuilder();
		Set<Tag> tags = unit.getTags();
		for (Tag tag : tags) {
			builder.append(tag.getTitle()).append(" ");
		}
		return builder.toString().trim();
	}

	@Override
	@Transactional
	public boolean deleteList(long id) {
		// 1. 检查数据库中是否有该栏目的记录存在
		CloudList list = cloudListDao.getById(CloudList.class, id);
		if (null == list) {
			// 这个栏目已经不存在了，返回false
			return false;
		} else {
			// 2. 检查该栏目是否有订阅，如有则触发事件SubscribedListRemoved发出订阅消息，同时删除所有订阅
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("p_list", list);
			List<Subscribe> subs = subscribeDao.find("from Subscribe where list = :p_list", params);
			Set<User> subscribers = new HashSet<User>();
			for (Subscribe sub : subs) {
				subscribers.add(sub.getSubscriber());
				subscribeDao.delete(sub);
			}
			eventHandler.fired(new CloudEvent(CloudEventType.SubscribedListRemoved, list, subscribers));
			// 3. 删除该栏目的所有推送
			List<Push> pushes = pushDao.find("from Push where type = 0 and unitId = " + id);
			for (Push push : pushes) {
				push.getUsers().clear();
				pushDao.delete(push);
			}
			// 4. 删除数据库中的栏目记录
			cloudListDao.delete(list);
			return true;
		}
	}

	@Override
	@Transactional
	public boolean deleteFavorList(long id) {
		FavorList fl = favorListDao.getById(FavorList.class, id);
		if (null == fl) {
			return false;
		} else {
			List<Favorite> favors = fl.getFavors();
			for (Favorite favor : favors) {
				CloudFile file = favor.getFile();
				file.setFvCount(file.getFvCount() - 1);
				cloudFileDao.save(file);
				favoriteDao.delete(favor);
			}
			favorListDao.delete(fl);
			return true;
		}
	}

	@Override
	@Transactional
	public boolean deleteRssList(long id) {
		Subscribe rss = subscribeDao.getById(Subscribe.class, id);
		if (null == rss) {
			return false;
		} else {
			CloudList list = rss.getList();
			list.setSubscribeCount(list.getSubscribeCount() - 1);
			cloudListDao.update(list);
			subscribeDao.delete(rss);
			return true;
		}
	}

	@Override
	@Transactional
	public boolean deleteFavorite(long id) {
		Favorite favor = favoriteDao.getById(Favorite.class, id);
		if (null == favor) {
			return false;
		} else {
			CloudFile file = favor.getFile();
			file.setFvCount(file.getFvCount() - 1);
			cloudFileDao.save(file);
			favoriteDao.delete(favor);
			return true;
		}
	}

	@Override
	@Transactional(rollbackFor = java.io.IOException.class)
	public boolean deleteFile(long id, String rootPath, boolean silent) throws IOException {
		// 1. 检查数据库中的文件记录是否存在
		CloudFile file = cloudFileDao.getById(CloudFile.class, id);
		if (null != file) {
			// 2. 删除所有收藏记录
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("p_file", file);
			List<Favorite> favorites = favoriteDao.find("from Favorite where file = :p_file", params);
			for (Favorite favorite : favorites) {
				favoriteDao.delete(favorite);
			}
			// 3. 删除所有推送记录
			List<Push> pushes = pushDao.find("from Push where type = 1 and unitId = " + id);
			for (Push push : pushes) {
				push.getUsers().clear();
				pushDao.delete(push);
			}
			// 4. 如果不是静默删除，那么获取栏目订阅列表，触发FileRemovedInSubscribedList事件
			if (!silent) {
				CloudList list = file.getList();
				params.clear();
				params.put("p_list", list);
				List<Subscribe> subs = subscribeDao.find("from Subscribe where list = :p_list", params);
				Set<User> subscribers = new HashSet<User>();
				for (Subscribe sub : subs) {
					subscribers.add(sub.getSubscriber());
				}
				eventHandler.fired(new CloudEvent(CloudEventType.FileRemovedInSubscribedList, file, subscribers));
			}
			// 5. 删除数据库中的文件记录
			cloudFileDao.delete(file);
			// 6. 检查物理文件是否存在，如存在则删除
			String path = rootPath + file.getFullPath();
			File dir = new File(path);
			if (dir.exists()) {
				String newFilename = file.getId() + ".cloudfile";
				File f = new File(dir, newFilename);
				if (f.exists()) {
					f.delete();
				}
			}
			return true;
		} else {
			// 数据库中文件已经不存在，删除失败，返回false
			return false;
		}
	}

	@Override
	@Transactional
	public int push(byte type, long id, Set<Long> userIds, long ownerId) {
		userIds.remove(ownerId);
		CloudUnit target = null;
		if (type == CloudConstants.CLOUD_FILE) {
			target = cloudFileDao.getById(CloudFile.class, id);
		} else if (type == CloudConstants.CLOUD_LIST) {
			target = cloudListDao.getById(CloudList.class, id);
		}
		if (null == target || userIds.size() == 0 || target.isPrivateUnit()) {
			// 要推送的资源不存在，或没有指定潜在受众，或要推送的资源是私有权限，则不会执行推送
			return 0;
		} else {
			Set<User> users = new HashSet<User>();
			for (long userId : userIds) {
				User user = userDao.getById(User.class, userId);
				if (null == user || !FileUtil.checkPermission(target, user)) {
					continue;
				}
				users.add(user);
			}
			if (users.size() > 0) {
				// 有最终的实际受众才会被保存
				Push push = new Push();
				push.setType(type);
				push.setUnitId(id);
				push.setUsers(users);
				pushDao.save(push);
				eventHandler.fired(new CloudEvent(type == CloudConstants.CLOUD_LIST ? CloudEventType.ListPushed : CloudEventType.FilePushed, target, users));
			}
			return users.size();
		}
	}

	@Override
	@Transactional
	public CloudTreeNode subscribe(long id, User subscriber) {
		CloudTreeNode node = null;
		CloudList list = cloudListDao.getById(CloudList.class, id);
		if (null != list && FileUtil.checkPermission(list, subscriber)) {
			// 栏目存在而且有访问权限，可以订阅
			// 1. 检查是不是已经订阅过了
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("p_subscriber", subscriber);
			params.put("p_list", list);
			long count = subscribeDao.count("from Subscribe where subscriber = :p_subscriber and list = :p_list", params);
			if (count == 0) {
				// 2. 订阅
				Subscribe subs = new Subscribe();
				subs.setList(list);
				subs.setSubscriber(subscriber);
				subscribeDao.save(subs);
				list.setSubscribeCount(list.getSubscribeCount() + 1);
				cloudListDao.update(list);
				// 3. 订阅完成之后查看是否有相应的推送记录，如果有的话删除推送记录
				List<Push> pushes = pushDao.find("from Push where type = 0 and unitId = " + id);
				for (Push push : pushes) {
					if (push.getUsers().remove(subscriber)) {
						pushDao.update(push);
						if (push.getUsers().size() == 0) {
							pushDao.delete(push);
						}
					}
				}
				node = CloudTreeNode.getInstance(subs.getId(), CloudConstants.RSS_LIST, list.getId(), list.getName(), null);
			}
		}
		return node;
	}

	@Override
	@Transactional(rollbackFor = java.io.IOException.class)
	public CloudTreeNode upload(File file, long listId, String filename, String contentType, String rootPath) throws IOException {
		CloudTreeNode node = null;
		CloudList list = cloudListDao.getById(CloudList.class, listId);
		if (list == null) {
			return null;
		} else {
			/*
			 * 先检查同栏目下有没有同名文件，有的话要自动分配版本
			 * 如果已经有一个同名文件，则原同名文件设版本号为1.0.0
			 * 自动版本号按minorVersion+1.0递增
			 */
			byte majorVersion = 0;
			double minorVersion = 0.0;
			String versionType = "";
			String hql = "from CloudFile where name = '" + filename + "' and list.id = " + listId + " order by majorVersion, minorVersion";
			List<CloudFile> sames = cloudFileDao.find(hql);
			if (sames.size() > 0) {
				CloudFile of = sames.get(sames.size() - 1);
				if ("".equals(of.getVersion())) {
					of.upgradeTo((byte) 1, 0.0);
					cloudFileDao.update(of);
				}
				majorVersion = of.getMajorVersion();
				minorVersion = of.getMinorVersion();
				versionType = of.getVersionType();
			}
			CloudFile f = new CloudFile();
			Date date = new Date();
			f.setName(filename);
			f.setContentType(contentType);
			f.setBrief("");
			f.setCreateTime(date);
			f.setLastUpdateTime(date);
			f.setMajorVersion(majorVersion);
			f.setMinorVersion(minorVersion);
			f.setVersionType(versionType);
			if (sames.size() > 0) {
				f.upgrade(0.1);
			}
			f.setDlCount(0);
			f.setFvCount(0);
			f.setList(list);
			f.setOwner(list.getOwner());
			Set<User> pr = new HashSet<User>();
			pr.addAll(list.getPermittedReaders());
			f.setPermittedReaders(pr);
			f.setPrivateUnit(list.isPrivateUnit());
			f.setSize(file.length());
			f.setTags(new HashSet<Tag>());
			cloudFileDao.save(f);

			String path = rootPath + f.getFullPath();
			File dir = new File(path);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			String newFilename = f.getId() + ".cloudfile";
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(dir, newFilename)));
			byte[] buffer = new byte[4096];
			int len = -1;
			while ((len = bis.read(buffer)) != -1) {
				bos.write(buffer, 0, len);
			}
			bis.close();
			bos.close();
			node = CloudTreeNode.getInstance(f.getId(), CloudConstants.FILE, f.getId(), f.getName(), f.getIconCls());
		}
		return node;
	}

	@Override
	public void uploadRss(List<CloudTreeNode> files, long listId) {
		String hql = "from Subscribe where list.id = " + listId;
		List<Subscribe> subs = subscribeDao.find(hql);
		if (subs.size() > 0) {
			Set<User> users = new HashSet<User>();
			for (Subscribe sub : subs) {
				users.add(sub.getSubscriber());
			}
			CloudFile f = cloudFileDao.getById(CloudFile.class, files.get(0).getNodeId());
			CloudEvent e = new CloudEvent(CloudEventType.NewFileUploaded, f, users);
			e.setCount(files.size());
			eventHandler.fired(e);
		}
	}

	@Override
	@Transactional
	public CloudTreeNode favor(long id, long favorListId, User user) {
		CloudTreeNode node = null;
		FavorList fl = favorListDao.getById(FavorList.class, favorListId);
		CloudFile f = cloudFileDao.getById(CloudFile.class, id);
		if (null != fl && null != f && FileUtil.checkPermission(f, user)) {
			// 收藏夹存在、文件存在且具有访问权限，可以收藏
			// 1. 检查是否已经收藏过了
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("p_user", user);
			params.put("p_file", f);
			long count = favoriteDao.count("from Favorite where user = :p_user and file = :p_file", params);
			if (count == 0) {
				// 2. 收藏
				Favorite fv = new Favorite();
				fv.setFile(f);
				fv.setList(fl);
				fv.setUser(user);
				favoriteDao.save(fv);
				f.setFvCount(f.getFvCount() + 1);
				cloudFileDao.update(f);
				// 3. 检查是否有相应的推送记录，如果有则删除推送记录
				List<Push> pushes = pushDao.find("from Push where type = 1 and unitId = " + id);
				for (Push push : pushes) {
					if (push.getUsers().remove(user)) {
						pushDao.update(push);
						if (push.getUsers().size() == 0) {
							pushDao.delete(push);
						}
					}
				}
				node = CloudTreeNode.getInstance(fv.getId(), CloudConstants.FAVOR_LIST_LINK, f.getId(), f.getName(), f.getIconCls());
			}
		}
		return node;
	}

	@Override
	@Transactional
	public boolean refusePush(long id, User user) {
		Push push = pushDao.getById(Push.class, id);
		if (null == push) {
			return false;
		} else {
			push.getUsers().remove(user);
			pushDao.update(push);
			if (push.getUsers().size() == 0) {
				pushDao.delete(push);
			}
			return true;
		}
	}

	@Override
	@Transactional
	public CloudTreeNode move(long id, long targetId, byte type) {
		CloudTreeNode node = null;
		if (type == CloudConstants.FILE) {
			CloudFile file = cloudFileDao.getById(CloudFile.class, id);
			CloudList list = cloudListDao.getById(CloudList.class, targetId);
			if (null != file && null != list && targetId != file.getList().getId()) {
				file.setList(list);
				if (list.isPrivateUnit()) {
					file.setPrivateUnit(true);
					file.setPermittedReaders(new HashSet<User>());
				} else {
					if (!file.isPrivateUnit() && list.getPermittedReaders().size() != 0) {
						Set<User> newPerm = new HashSet<User>();
						newPerm.addAll(list.getPermittedReaders());
						if (file.getPermittedReaders().size() != 0) {
							newPerm.retainAll(file.getPermittedReaders());
						}
						if (newPerm.size() == 0) {
							file.setPrivateUnit(true);
						}
						file.setPermittedReaders(newPerm);
					}
				}
				cloudFileDao.update(file);
				node = CloudTreeNode.getInstance(file.getId(), CloudConstants.FILE, file.getId(), file.getName(), file.getIconCls());
			}
		} else {
			Favorite favor = favoriteDao.getById(Favorite.class, id);
			FavorList favorList = favorListDao.getById(FavorList.class, targetId);
			if (null != favor && null != favorList && targetId != favor.getList().getId()) {
				favor.setList(favorList);
				favoriteDao.update(favor);
				CloudFile file = favor.getFile();
				node = CloudTreeNode.getInstance(favor.getId(), CloudConstants.FAVOR_LIST_LINK, file.getId(), file.getName(), file.getIconCls());
			}
		}
		return node;
	}

	@Override
	@Transactional
	public void changePermissions(User owner, long id, byte type, boolean isPrivate, Set<Long> permittedReaderIds) {
		// 对传入的权限需求要先进行一次去除所有者自己的预处理，即如果传入的新权限是有限访问权限，那么要扣除所有者自己，如果扣除后没有其他用户了则要改为私有
		if (!isPrivate && permittedReaderIds.size() != 0) {
			permittedReaderIds.remove(owner.getId());
			if (permittedReaderIds.size() == 0) {
				isPrivate = true;
			}
		}
		Set<User> permittedReaders = new HashSet<User>();
		if (null != permittedReaderIds && permittedReaderIds.size() > 0) {
			for (long permittedReaderId : permittedReaderIds) {
				User user = userDao.getById(User.class, permittedReaderId);
				if (null != user) {
					permittedReaders.add(user);
				}
			}
		}
		if (type == CloudConstants.FILE) {
			changeFilePermissions(cloudFileDao.getById(CloudFile.class, id), isPrivate, permittedReaders);
		} else if (type == CloudConstants.LIST) {
			changeListPermissions(cloudListDao.getById(CloudList.class, id), isPrivate, permittedReaders);
		}
	}

	/**
	 * 改变文件的权限时，要根据所在栏目的权限取交集，具体规则与move方法中相同
	 * 
	 * @param file
	 *            要改变权限的文件
	 * @param isPrivate
	 *            是否改为私有文件
	 * @param permittedReaders
	 *            文件要设为可访问的用户集合
	 */
	private void changeFilePermissions(CloudFile file, boolean isPrivate, Set<User> permittedReaders) {
		if (null == file) {
			return;
		}
		CloudList list = file.getList();
		if (!list.isPrivateUnit()) {
			// 如果所在栏目为私有栏目，那么文件本身必定也是私有文件，不可以更改访问权限，因此过滤掉此种可能
			if (list.getPermittedReaders().size() == 0) {
				// 如果所在栏目为公开访问权限，则只需照抄需要变更为的权限即可
				file.setPrivateUnit(isPrivate);
				file.setPermittedReaders(permittedReaders);
			} else {
				file.setPrivateUnit(isPrivate);
				if (isPrivate) {
					// 如果所在栏目为部分访问权限，而文件要更改为私有，那么直接更改即可
					file.setPermittedReaders(new HashSet<User>());
				} else {
					// 如果所在栏目为部分访问权限，而文件要更改为公开或部分访问，那么取交集
					Set<User> newPerm = new HashSet<User>();
					newPerm.addAll(list.getPermittedReaders());
					if (permittedReaders.size() != 0) {
						// 如果要更改为部分访问的那么取交集，否则直接复制List的访问列表即可
						newPerm.retainAll(permittedReaders);
					}
					if (newPerm.size() == 0) {
						// 如果交集为空，强行更改为私有
						file.setPrivateUnit(true);
					}
					file.setPermittedReaders(newPerm);
				}
			}
		}
		cloudFileDao.update(file);
	}

	/**
	 * 改变栏目的权限后，要按取交集的原则改变栏目中所有文件的访问权限
	 * 
	 * @param list
	 *            要改变权限的栏目
	 * @param isPrivate
	 *            是否改为私有栏目
	 * @param permittedReaders
	 *            栏目要设为可访问的用户集合
	 */
	private void changeListPermissions(CloudList list, boolean isPrivate, Set<User> permittedReaders) {
		if (null == list) {
			return;
		}
		// 先设置好List的权限
		list.setPrivateUnit(isPrivate);
		Set<User> newListPerm = new HashSet<User>();
		newListPerm.addAll(permittedReaders);
		list.setPermittedReaders(newListPerm);
		cloudListDao.update(list);
		// 检查是否有订阅受到影响，如有则触发事件发布订阅失效消息
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("p_list", list);
		List<Subscribe> subs = subscribeDao.find("from Subscribe where list = :p_list", params);
		Set<User> subscribers = new HashSet<User>();
		for (Subscribe sub : subs) {
			User subscriber = sub.getSubscriber();
			if (list.isPrivateUnit() || (newListPerm.size() != 0 && !newListPerm.contains(subscriber))) {
				subscribers.add(subscriber);
			}
		}
		eventHandler.fired(new CloudEvent(CloudEventType.SubscribedListPermissionRemoved, list, subscribers));

		// 设置所有栏目内文件的权限
		if (!list.isPrivateUnit() && newListPerm.size() == 0) {
			// 如果栏目变为公开权限，那么栏目内文件不需要改变原有权限，直接返回
			return;
		}
		List<CloudFile> files = list.getFiles();
		for (CloudFile file : files) {
			if (list.isPrivateUnit()) {
				// 如果栏目变为私有，则所有文件统一设为私有
				file.setPrivateUnit(true);
				file.setPermittedReaders(new HashSet<User>());
			} else if (newListPerm.size() != 0) {
				// 如果栏目变为受限访问，而文件本身为私有权限，那么不需要做任何更改
				if (file.isPrivateUnit()) {
					continue;
				}
				// 如果栏目变为受限访问，而文件本身为公开权限，那么复制栏目的权限
				// 如果栏目变为受限访问，而文件本身也是受限访问，那么取二者的交集
				Set<User> newFilePerm = new HashSet<User>();
				newFilePerm.addAll(newListPerm);
				if (file.getPermittedReaders().size() != 0) {
					newFilePerm.retainAll(file.getPermittedReaders());
				}
				if (newFilePerm.size() == 0) {
					// 如果交集为空集，那么改为私有权限
					file.setPrivateUnit(true);
				}
				file.setPermittedReaders(newFilePerm);
			}
			cloudFileDao.update(file);
		}
	}

	@Override
	@Transactional
	public void saveBrief(long id, String brief) {
		StringBuilder builder = new StringBuilder();
		if (!StringUtils.isBlank(brief)) {
			builder.append(brief);
			if (builder.length() > 512) {
				builder.delete(512, builder.length());
			}
		}
		if (builder.length() > 0) {
			CloudFile file = cloudFileDao.getById(CloudFile.class, id);
			if (null != file) {
				file.setBrief(builder.toString());
				cloudFileDao.update(file);
			}
		}
	}

	@Override
	@Transactional
	public void saveTags(long id, byte type, String tags) {
		CloudUnit unit = null;
		if (type == CloudConstants.FILE) {
			unit = cloudFileDao.getById(CloudFile.class, id);
		} else if (type == CloudConstants.LIST) {
			unit = cloudListDao.getById(CloudList.class, id);
		}
		if (null == unit) {
			return;
		}
		Set<Tag> t = tagService.loadAndUpdate(tags);
		if (unit instanceof CloudFile) {
			((CloudFile) unit).setTags(t);
			cloudFileDao.save((CloudFile) unit);
		} else if (unit instanceof CloudList) {
			((CloudList) unit).setTags(t);
			cloudListDao.save((CloudList) unit);
		}
	}

	@Override
	@Transactional
	public CloudTreeNode rename(long id, byte type, String name) {
		CloudTreeNode node = null;
		if (null != name) {
			StringBuilder builder = new StringBuilder();
			String newName = name.replaceAll("[\\\\/:\\*\\?\\\"<>\\|]", " ");
			if (!StringUtils.isBlank(newName)) {
				builder.append(newName.trim());
				if (builder.length() > 256) {
					builder.delete(256, builder.length());
				}
			}
			if (builder.length() > 0) {
				switch (type) {
				case CloudConstants.FILE:
					CloudFile file = cloudFileDao.getById(CloudFile.class, id);
					if (null != file) {
						file.setName(builder.toString());
						cloudFileDao.update(file);
						node = CloudTreeNode.getInstance(file.getId(), CloudConstants.FILE, file.getId(), file.getName(), file.getIconCls());
					}
					break;
				case CloudConstants.LIST:
					CloudList list = cloudListDao.getById(CloudList.class, id);
					if (null != list) {
						list.setName(builder.toString());
						cloudListDao.update(list);
						node = CloudTreeNode.getInstance(list.getId(), CloudConstants.LIST, list.getId(), list.getName(), null);
					}
					break;
				case CloudConstants.FAVOR_LIST:
					FavorList fl = favorListDao.getById(FavorList.class, id);
					if (null != fl) {
						fl.setName(builder.toString());
						favorListDao.update(fl);
						node = CloudTreeNode.getInstance(fl.getId(), CloudConstants.FAVOR_LIST, fl.getId(), fl.getName(), null);
					}
					break;
				default:
				}
			}
		}
		return node;
	}

	@Override
	@Transactional(readOnly = true)
	public Map<String, Object> getVersion(long id) {
		Map<String, Object> ver = new HashMap<String, Object>();
		CloudFile file = cloudFileDao.getById(CloudFile.class, id);
		if (null != file) {
			ver.put("majorVersion", file.getMajorVersion());
			ver.put("minorVersion", file.getMinorVersion());
			ver.put("versionType", file.getVersionType());
			ver.put("version", file.getVersion());
		}
		return ver;
	}

	@Override
	@Transactional
	public void saveVersion(long id, byte majorVersion, double minorVersion, String versionType) {
		CloudFile file = cloudFileDao.getById(CloudFile.class, id);
		if (null != file) {
			file.setVersionType(versionType.trim());
			file.upgradeTo(majorVersion, minorVersion);
			cloudFileDao.update(file);
		}
	}

	@Override
	@Transactional
	public String prepareDownload(long id, String rootPath, boolean isIE, List<File> files, User downloader) throws UnsupportedEncodingException {
		String filename = null;
		CloudFile file = cloudFileDao.getById(CloudFile.class, id);
		if (null != file && FileUtil.checkPermission(file, downloader)) {
			String path = rootPath + file.getFullPath();
			File dir = new File(path);
			if (null != dir || dir.exists()) {
				if (isIE) {
					filename = URLEncoder.encode(file.getName(), "UTF-8").replaceAll("\\+", "%20");
				} else {
					filename = new String(file.getName().getBytes("UTF-8"), "iso8859-1");
				}
				String realFilename = file.getId() + ".cloudfile";
				File f = new File(dir, realFilename);
				if (null != f && f.exists()) {
					files.add(f);
					file.setDlCount(file.getDlCount() + 1);
					cloudFileDao.update(file);
				}
			}
		}
		return filename;
	}

	@Override
	@Transactional
	public String prepareListDownload(long id, byte type, String rootPath, boolean isIE, Map<String, File> entries, User downloader)
			throws UnsupportedEncodingException {
		String filename = null;
		if (type == CloudConstants.FAVOR_LIST) {
			// 下载收藏夹
			FavorList fl = favorListDao.getById(FavorList.class, id);
			if (null != fl && fl.getFavors().size() > 0) {
				List<Favorite> favors = fl.getFavors();
				for (Favorite favor : favors) {
					CloudFile file = favor.getFile();
					zipFile(file, entries, rootPath, downloader);
				}
				if (entries.size() > 0) {
					// 有文件可下载，生成文件名=收藏夹名称.zip
					filename = encodeZipFilename(fl.getName(), isIE);
				}
			}
		} else {
			// 下载栏目
			CloudList list = cloudListDao.getById(CloudList.class, id);
			if (null != list && FileUtil.checkPermission(list, downloader) && list.getFiles().size() > 0) {
				List<CloudFile> files = list.getFiles();
				for (CloudFile file : files) {
					zipFile(file, entries, rootPath, downloader);
				}
				if (entries.size() > 0) {
					// 有文件可下载，生成文件名=栏目名称.zip
					filename = encodeZipFilename(list.getName(), isIE);
				}
			}
		}
		return filename;
	}

	@Override
	@Transactional
	public String prepareMultiDownload(List<Long> ids, String rootPath, boolean isIE, Map<String, File> entries, User downloader)
			throws UnsupportedEncodingException {
		String filename = null;
		if (null != ids && ids.size() > 0) {
			for (long id : ids) {
				CloudFile file = cloudFileDao.getById(CloudFile.class, id);
				zipFile(file, entries, rootPath, downloader);
			}
			if (entries.size() > 0) {
				filename = encodeZipFilename("多个文件打包下载", isIE);
			}
		}
		return filename;
	}

	/**
	 * 文件打包预处理，由于多个文件下载可能会遇到同名文件，所以要用含有版本号的文件名
	 * 
	 * @param file
	 * @param entries
	 * @param rootPath
	 * @param downloader
	 */
	private void zipFile(CloudFile file, Map<String, File> entries, String rootPath, User downloader) {
		if (null != file && FileUtil.checkPermission(file, downloader)) {
			String path = rootPath + file.getFullPath();
			String fn = file.getVersionFilename();
			File dir = new File(path);
			if (null != dir || dir.exists()) {
				String realFilename = file.getId() + ".cloudfile";
				File f = new File(dir, realFilename);
				if (null != f && f.exists()) {
					entries.put(fn, f);
					file.setDlCount(file.getDlCount() + 1);
					cloudFileDao.update(file);
				}
			}
		}
	}

	/**
	 * 文件名处理，由于多个文件打包下载直接使用Servlet的输出流，所以非IE浏览器要在文件名上加双引号
	 * 
	 * @param filename
	 * @param isIE
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private String encodeZipFilename(String filename, boolean isIE) throws UnsupportedEncodingException {
		String fn = filename + ".zip";
		if (isIE) {
			return URLEncoder.encode(fn, "UTF-8").replaceAll("\\+", "%20");
		}	else {
			return "\"" + new String(fn.getBytes("UTF-8"), "iso8859-1") + "\"";
		}
	}
}
