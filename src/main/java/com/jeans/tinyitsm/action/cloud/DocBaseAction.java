package com.jeans.tinyitsm.action.cloud;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.jeans.tinyitsm.action.BaseAction;
import com.jeans.tinyitsm.model.view.CloudTreeNode;
import com.jeans.tinyitsm.model.view.Grid;
import com.jeans.tinyitsm.model.view.PropertyGridRow;
import com.jeans.tinyitsm.service.cloud.CloudConstants;
import com.jeans.tinyitsm.service.cloud.CloudService;

public class DocBaseAction extends BaseAction<List<CloudTreeNode>> {

	private CloudService cloudService;

	@Autowired
	public void setCloudService(CloudService cloudService) {
		this.cloudService = cloudService;
	}

	@Action(value = "outline", results = { @Result(type = "json", params = { "root", "data" }) })
	public String getTreeOutline() throws Exception {
		data = cloudService.getTreeOutline();
		return SUCCESS;
	}

	private long nodeId;
	private byte nodeType;

	public long getNodeId() {
		return nodeId;
	}

	public void setNodeId(long nodeId) {
		this.nodeId = nodeId;
	}

	public byte getNodeType() {
		return nodeType;
	}

	public void setNodeType(byte nodeType) {
		this.nodeType = nodeType;
	}

	@Action(value = "load-children", results = { @Result(type = "json", params = { "root", "data" }) })
	public String retrieveChildren() throws Exception {
		data = cloudService.getChildren(nodeId, nodeType, getCurrentUser());
		return SUCCESS;
	}

	private Grid<PropertyGridRow> props;

	public Grid<PropertyGridRow> getProps() {
		return props;
	}

	public void setProps(Grid<PropertyGridRow> props) {
		this.props = props;
	}

	@Action(value = "properties", results = { @Result(type = "json", params = { "root", "props" }) })
	public String getProperties() throws Exception {
		props = cloudService.getProperties(nodeId, nodeType);
		return SUCCESS;
	}

	private String name;
	private String tags;
	private boolean priv;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public boolean isPriv() {
		return priv;
	}

	public void setPriv(boolean priv) {
		this.priv = priv;
	}

	/**
	 * 创建新栏目/新收藏夹，必须传入的参数有nodeType和name，nodeType用来确定是新建栏目还是收藏夹
	 * 如果是新建栏目，还需要传入tags, priv和ids(有权访问的用户ids)
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "create-list", results = { @Result(type = "json", params = { "root", "data" }) })
	public String createList() throws Exception {
		data = new ArrayList<CloudTreeNode>();
		if (nodeType == CloudConstants.FILE || nodeType == CloudConstants.LIST || nodeType == CloudConstants.FILES_ROOT) {
			data.add(cloudService.createList(getCurrentUser(), name, tags, priv, splitIds()));
		} else {
			data.add(cloudService.createFavorList(getCurrentUser(), name));
		}
		return SUCCESS;
	}

	private int result;

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	/**
	 * 推送一个栏目或者一个文件，前端需提供nodeType,nodeId和ids(目标用户id)
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "push", results = { @Result(type = "json", params = { "root", "result" }) })
	public String push() throws Exception {
		result = cloudService.push(nodeType, nodeId, splitIds(), getCurrentUserId());
		return SUCCESS;
	}

	private String brief;

	public String getBrief() {
		return brief;
	}

	public void setBrief(String brief) {
		this.brief = brief;
	}

	/**
	 * 获取文件简介，参数：id，文件id
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "brief", results = { @Result(type = "json", params = { "root", "brief" }) })
	public String loadBrief() throws Exception {
		brief = cloudService.getBrief(id);
		if (null == brief) {
			brief = "(资料已被删除)";
		} else if (StringUtils.isBlank(brief)) {
			brief = "(暂无摘要)";
		}
		return SUCCESS;
	}

	/**
	 * 栏目订阅， 参数：id，要订阅的栏目id
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "subscribe", results = { @Result(type = "json", params = { "root", "data" }) })
	public String subscribe() throws Exception {
		data = new ArrayList<CloudTreeNode>();
		CloudTreeNode node = cloudService.subscribe(id, getCurrentUser());
		if (null != node) {
			data.add(node);
		}
		return SUCCESS;
	}

	/**
	 * 文件收藏，参数：id，要收藏的文件id；nodeId：目标收藏夹的id
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "favor", results = { @Result(type = "json", params = { "root", "data" }) })
	public String favor() throws Exception {
		data = new ArrayList<CloudTreeNode>();
		CloudTreeNode node = cloudService.favor(id, nodeId, getCurrentUser());
		if (null != node) {
			data.add(node);
		}
		return SUCCESS;
	}

	/**
	 * 删除文件，返回result = 1表示删除成功; 0表示文件本身已经删除; -1表示删除失败
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "del-file", results = { @Result(type = "json", params = { "root", "result" }) })
	public String deleteFile() throws Exception {
		try {
			if (cloudService.deleteFile(id, ServletActionContext.getServletContext().getRealPath(getEnvironment().getCloudRoot()), false)) {
				result = 1;
			} else {
				result = 0;
			}
		} catch (IOException e) {
			log(e);
			result = -1;
		}
		return SUCCESS;
	}

	/**
	 * 删除栏目及其中所有文件，返回result表示实际删除的CloudUnit数量，即如果是空栏目，则返回1，如果有n个文件，则返回n+1<br>
	 * 如果栏目删除出错，则返回一个负数，其绝对值是实际删除的CloudUnit数量加1<br>
	 * 如果返回值为0，表示该栏目实际上已经不存在
	 * 
	 * @return
	 */
	@Action(value = "del-list", results = { @Result(type = "json", params = { "root", "result" }) })
	public String deleteList() throws Exception {
		String rootPath = ServletActionContext.getServletContext().getRealPath(getEnvironment().getCloudRoot());
		result = 0;
		List<CloudTreeNode> files = cloudService.getChildren(id, CloudConstants.LIST, getCurrentUser());
		for (CloudTreeNode file : files) {
			try {
				if (cloudService.deleteFile(file.getNodeId(), rootPath, true)) {
					result++;
				}
			} catch (IOException e) {
				log(e);
				result = -result - 1;
				break;
			}
		}
		if (result >= 0 && cloudService.deleteList(id)) {
			result++;
		}
		return SUCCESS;
	}

	/**
	 * 取消一条收藏，返回result = 1表示取消成功，0表示这条收藏不存在
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "del-favor", results = { @Result(type = "json", params = { "root", "result" }) })
	public String deleteFavorite() throws Exception {
		if (cloudService.deleteFavorite(id - 50000000)) {
			result = 1;
		} else {
			result = 0;
		}
		return SUCCESS;
	}

	/**
	 * 删除一个收藏夹，同时取消其中所有收藏，返回result = 1表示取消成功，0表示收藏夹不存在
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "del-favorlist", results = { @Result(type = "json", params = { "root", "result" }) })
	public String deleteFavorList() throws Exception {
		if (cloudService.deleteFavorList(id)) {
			result = 1;
		} else {
			result = 0;
		}
		return SUCCESS;
	}

	/**
	 * 取消一个订阅，返回result = 1表示取消成功，0表示这条订阅不存在
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "del-rss", results = { @Result(type = "json", params = { "root", "result" }) })
	public String deleteRssList() throws Exception {
		if (cloudService.deleteRssList(id - 30000000)) {
			result = 1;
		} else {
			result = 0;
		}
		return SUCCESS;
	}

	/**
	 * 拒绝一次推送，返回result = 1表示取消成功，0表示这条订阅不存在
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "refuse-push", results = { @Result(type = "json", params = { "root", "result" }) })
	public String refusePush() throws Exception {
		long realId = 0;
		if (nodeType == CloudConstants.PUSH_LIST) {
			realId = id - 40000000;
		} else {
			realId = id - 70000000;
		}
		if (cloudService.refusePush(realId, getCurrentUser())) {
			result = 1;
		} else {
			result = 0;
		}
		return SUCCESS;
	}

	/**
	 * 移动一个文件或一个收藏的文件，返回新的CloudTreeNode节点，如果目标栏目/收藏夹不存在，或文件/收藏的文件不存在，或目标栏目/收藏夹与源相同，返回空List
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "move", results = { @Result(type = "json", params = { "root", "data" }) })
	public String move() throws Exception {
		long realId = id;
		if (nodeType == CloudConstants.FAVOR_LIST_LINK) {
			realId -= 50000000;
		}
		CloudTreeNode node = cloudService.move(realId, nodeId, nodeType);
		data = new ArrayList<CloudTreeNode>();
		if (null != node) {
			data.add(node);
		}
		return SUCCESS;
	}

	/**
	 * 修改一个文件或栏目的访问权限，传入参数：id, nodeType, priv, ids，返回result = 1表示修改成功，0表示修改失败
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "set-perm", results = { @Result(type = "json", params = { "root", "result" }) })
	public String changePermissions() throws Exception {
		cloudService.changePermissions(getCurrentUser(), id, nodeType, priv, splitIds());
		result = 1;
		return SUCCESS;
	}

	/**
	 * 获取一个文件或栏目的标签，多个标签用空格分隔
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "load-tags", results = { @Result(type = "json", params = { "root", "tags" }) })
	public String loadTags() throws Exception {
		tags = cloudService.getTags(id, nodeType);
		return SUCCESS;
	}

	/**
	 * 保存一个文件或栏目的标签，多个标签用空格分隔
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "save-tags", results = { @Result(type = "json", params = { "root", "result" }) })
	public String saveTags() throws Exception {
		cloudService.saveTags(id, nodeType, tags);
		result = 1;
		return SUCCESS;
	}

	/**
	 * 文件、栏目或收藏夹重命名，名称最多256个字，特殊字符\/:*?"<>|会被一一替换为空格<br>
	 * 如果文件名合法则返回最终的新名称，如果不合法则返回null
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "rename", results = { @Result(type = "json", params = { "root", "data" }) })
	public String rename() throws Exception {
		data = new ArrayList<CloudTreeNode>();
		CloudTreeNode node = cloudService.rename(id, nodeType, name);
		if (null != node) {
			data.add(node);
		}
		return SUCCESS;
	}

	private Map<String, Object> info = new HashMap<String, Object>();
	
	public Map<String, Object> getInfo() {
		return info;
	}

	public void setInfo(Map<String, Object> info) {
		this.info = info;
	}

	/**
	 * 一次性获取资料的版本信息和摘要，返回在Map info中，关键字为majorVersion, minorVersion, versionType, version, brief
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "info", results = { @Result(type = "json", params = { "root", "info" }) })
	public String loadInfo() throws Exception {
		String b = cloudService.getBrief(id);
		if (null != b) {
			info.put("brief", b);
			Map<String, Object> v = cloudService.getVersion(id);
			if (null != v) {
				info.putAll(v);
			}
		}
		return SUCCESS;
	}

	private byte majorVersion;
	private double minorVersion;
	private String versionType;
	
	public byte getMajorVersion() {
		return majorVersion;
	}

	public void setMajorVersion(byte majorVersion) {
		this.majorVersion = majorVersion;
	}

	public double getMinorVersion() {
		return minorVersion;
	}

	public void setMinorVersion(double minorVersion) {
		this.minorVersion = minorVersion;
	}

	public String getVersionType() {
		return versionType;
	}

	public void setVersionType(String versionType) {
		this.versionType = versionType;
	}

	/**
	 * 修改资料的版本和摘要
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "save-info", results = { @Result(type = "json", params = { "root", "result" }) })
	public String saveInfo() throws Exception {
		cloudService.saveVersion(id, majorVersion, minorVersion, versionType);
		cloudService.saveBrief(id, brief);
		result = 1;
		return SUCCESS;
	}
}
