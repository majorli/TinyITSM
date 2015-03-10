package com.jeans.tinyitsm.service.cloud.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeans.tinyitsm.dao.BaseDao;
import com.jeans.tinyitsm.model.EasyuiTreeNode;
import com.jeans.tinyitsm.model.cloud.CloudFile;
import com.jeans.tinyitsm.model.cloud.CloudList;
import com.jeans.tinyitsm.model.cloud.Favorite;
import com.jeans.tinyitsm.model.cloud.Subscribe;
import com.jeans.tinyitsm.model.portal.User;
import com.jeans.tinyitsm.model.view.CloudGridRow;
import com.jeans.tinyitsm.model.view.Grid;
import com.jeans.tinyitsm.service.cloud.CloudConstants;
import com.jeans.tinyitsm.service.cloud.CloudViewService;

@Service
public class CloudViewServiceImpl implements CloudViewService {

	private BaseDao<CloudFile> fileDao;
	private BaseDao<CloudList> listDao;
	private BaseDao<Favorite> favorDao;
	private BaseDao<Subscribe> subsDao;

	@Autowired
	public void setFileDao(BaseDao<CloudFile> fileDao) {
		this.fileDao = fileDao;
	}

	@Autowired
	public void setListDao(BaseDao<CloudList> listDao) {
		this.listDao = listDao;
	}

	@Autowired
	public void setFavorDao(BaseDao<Favorite> favorDao) {
		this.favorDao = favorDao;
	}

	@Autowired
	public void setSubsDao(BaseDao<Subscribe> subsDao) {
		this.subsDao = subsDao;
	}

	@Override
	@Transactional(readOnly = true)
	public Grid<CloudGridRow> loadFiles(int docType, int page, int rows, User user) {
		Grid<CloudGridRow> g = new Grid<CloudGridRow>();
		List<CloudGridRow> r = new ArrayList<CloudGridRow>();
		long t = 0;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("p_owner", user);
		StringBuilder builder = new StringBuilder("from CloudFile where owner = :p_owner");
		if (docType != CloudConstants.UNKNOWN_TYPE) {
			switch (docType) {
			case CloudConstants.ALL:
				break;
			case CloudConstants.DOCUMENTS:
				builder.append(" and contentType in :p_type");
				params.put("p_type", documentTypeList());
				break;
			case CloudConstants.WORD:
				builder.append(" and contentType = 'application/msword' or contentType = 'application/vnd.openxmlformats-officedocument.wordprocessingml.document'");
				break;
			case CloudConstants.EXCEL:
				builder.append(" and contentType = 'application/vnd.ms-excel' or contentType = 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'");
				break;
			case CloudConstants.PPT:
				builder.append(" and contentType = 'application/vnd.ms-powerpoint' or contentType = 'application/vnd.openxmlformats-officedocument.presentationml.presentation'");
				break;
			case CloudConstants.VISIO:
				builder.append(" and contentType = 'application/vnd.visio'");
				break;
			case CloudConstants.PDF:
				builder.append(" and contentType = 'application/pdf'");
				break;
			case CloudConstants.MULTIMEDIA:
				builder.append(" and substring(contentType, 1, 5) in ('image', 'audio', 'video')");
				break;
			case CloudConstants.IMAGES:
				builder.append(" and substring(contentType, 1, 5) = 'image'");
				break;
			case CloudConstants.MUSIC:
				builder.append(" and substring(contentType, 1, 5) = 'audio'");
				break;
			case CloudConstants.VIDEO:
				builder.append(" and substring(contentType, 1, 5) = 'video'");
				break;
			case CloudConstants.OTHERS:
				builder.append(" and substring(contentType, 1, 5) not in ('image', 'audio', 'video') and contentType not in :p_type");
				params.put("p_type", documentTypeList());
				break;
			default:
			}
			List<CloudFile> files = fileDao.find(builder.toString(), params, page, rows);
			for (CloudFile file : files) {
				r.add(CloudGridRow.createRow(file));
			}
			Collections.sort(r);
			t = fileDao.count(builder.toString(), params);
		}
		g.setRows(r);
		g.setTotal(t);
		return g;
	}

	private List<String> documentTypeList() {
		List<String> doc = new ArrayList<String>();
		doc.add("application/msword");
		doc.add("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
		doc.add("application/vnd.ms-excel");
		doc.add("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		doc.add("application/vnd.ms-powerpoint");
		doc.add("application/vnd.openxmlformats-officedocument.presentationml.presentation");
		doc.add("application/vnd.visio");
		doc.add("application/pdf");
		return doc;
	}

	@Override
	@Transactional(readOnly = true)
	public List<EasyuiTreeNode> loadTopTens(User user) {
		List<EasyuiTreeNode> tree = new ArrayList<EasyuiTreeNode>();
		EasyuiTreeNode topNewFiles = EasyuiTreeNode.createInstance(1L, "最新资料TOP10", "icon-top10-new-files");
		EasyuiTreeNode topDownloads = EasyuiTreeNode.createInstance(2L, "最热下载TOP10", "icon-top10-downloads");
		EasyuiTreeNode topFavorites = EasyuiTreeNode.createInstance(3L, "最热收藏TOP10", "icon-root-favorites");
		EasyuiTreeNode topNewLists = EasyuiTreeNode.createInstance(4L, "最新栏目TOP10", "icon-top10-new-lists");
		EasyuiTreeNode topSubscribes = EasyuiTreeNode.createInstance(5L, "最热订阅TOP10", "icon-folder-rss");

		/*
		 * 树节点id和实际的文件/栏目id对应关系：
		 * 最新资料：TreeNode.id = CloudFile.id + 10000000
		 * 最热下载：TreeNode.id = CloudFile.id + 20000000
		 * 最热收藏：TreeNode.id = CloudFile.id + 30000000
		 * 最新栏目：TreeNode.id = CloudList.id + 40000000
		 * 最热订阅：TreeNode.id = CloudList.id + 50000000
		 */
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("p_user", user);
		String fileHql = "from CloudFile where owner = :p_user or (privateUnit = false and size(permittedReaders) = 0 or :p_user in elements(permittedReaders))";
		String listHql = "from CloudList where owner = :p_user or (privateUnit = false and size(permittedReaders) = 0 or :p_user in elements(permittedReaders))";
		// 最新资料
		String hql = fileHql + " order by createTime desc";
		List<CloudFile> files = fileDao.find(hql, params, 1, 10);
		for (CloudFile file : files) {
			topNewFiles.addChild(EasyuiTreeNode.createInstance(10000000 + file.getId(), file.getOwner().getUsername() + " - " + file.getVersionFilename()
					+ " [" + file.getCreateTime() + "]", file.getIconCls()));
		}
		tree.add(topNewFiles);

		// 最热下载
		hql = fileHql + " order by dlCount desc";
		files = fileDao.find(hql, params, 1, 10);
		for (CloudFile file : files) {
			if (file.getDlCount() <= 0) {
				break;
			}
			topDownloads.addChild(EasyuiTreeNode.createInstance(20000000 + file.getId(), file.getOwner().getUsername() + " - " + file.getVersionFilename()
					+ " [" + file.getDlCount() + "次下载]", file.getIconCls()));
		}
		tree.add(topDownloads);

		// 最热收藏
		hql = fileHql + " order by fvCount desc";
		files = fileDao.find(hql, params, 1, 10);
		for (CloudFile file : files) {
			if (file.getFvCount() <= 0) {
				break;
			}
			topFavorites.addChild(EasyuiTreeNode.createInstance(30000000 + file.getId(), file.getOwner().getUsername() + " - " + file.getVersionFilename()
					+ " [" + file.getFvCount() + "次收藏]", file.getIconCls()));
		}
		tree.add(topFavorites);

		// 最新栏目
		hql = listHql + " order by createTime desc";
		List<CloudList> lists = listDao.find(hql, params, 1, 10);
		for (CloudList list : lists) {
			topNewLists.addChild(EasyuiTreeNode.createInstance(40000000 + list.getId(),
					list.getOwner().getUsername() + " - " + list.getName() + " [" + list.getCreateTime() + "]", "icon-folder-documents"));
		}
		tree.add(topNewLists);

		// 最热订阅
		hql = listHql + " order by subscribeCount desc";
		lists = listDao.find(hql, params, 1, 10);
		for (CloudList list : lists) {
			if (list.getSubscribeCount() <= 0) {
				break;
			}
			topSubscribes.addChild(EasyuiTreeNode.createInstance(50000000 + list.getId(),
					list.getOwner().getUsername() + " - " + list.getName() + " [" + list.getSubscribeCount() + "次订阅]", "icon-folder-documents"));
		}
		tree.add(topSubscribes);
		return tree;
	}

	@Override
	@Transactional(readOnly = true)
	public int topTenFavoriteCheck(long id, User user) {
		CloudFile file = fileDao.getById(CloudFile.class, id);
		if (null == file || file.getOwner().equals(user)) {
			return 0;
		}
		String hql = "from Favorite where user = :p_user and file = :p_file";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("p_user", user);
		params.put("p_file", file);
		if (favorDao.count(hql, params) > 0) {
			return -1;
		} else {
			return 1;
		}
	}

	@Override
	@Transactional(readOnly = true)
	public int topTenSubscribeCheck(long id, User user) {
		CloudList list = listDao.getById(CloudList.class, id);
		if (null == list || list.getOwner().equals(user)) {
			return 0;
		}
		String hql = "from Subscribe where subscriber = :p_user and list = :p_list";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("p_user", user);
		params.put("p_list", list);
		if (subsDao.count(hql, params) > 0) {
			return -1;
		} else {
			return 1;
		}
	}
}
