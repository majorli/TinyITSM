package com.jeans.tinyitsm.model.view;

import java.io.Serializable;
import java.text.Collator;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.json.annotations.JSON;

import com.jeans.tinyitsm.model.TreeNode;
import com.jeans.tinyitsm.service.cloud.CloudConstants;

/**
 * 资料树的节点
 * 
 * ============资料树===========
 * 资料
 * 		栏目
 * 			文件
 * 			...
 * 		...
 * 收藏
 * 		收藏夹
 * 			链接文件
 * 			...
 * 		...
 * 订阅
 * 		订阅栏目
 * 			链接文件
 * 			...
 * 		...
 * 推送
 * 		推送栏目
 * 			链接文件
 * 			...
 * 		...
 * 		链接文件
 * 		...
 * 
 * @author Majorli
 *
 */
public class CloudTreeNode implements TreeNode, Comparable<CloudTreeNode>, Serializable {

	private long id;
	private String name;
	private List<CloudTreeNode> children = new ArrayList<CloudTreeNode>();

	private String state;
	private String iconCls;

	private long nodeId;
	private byte nodeType;
	private boolean loaded;

	/**
	 * 生成一个根节点，如果给定的类型不是四种根节点类型之一，则返回null
	 * 
	 * @param type
	 *            CloudConstants中定义的四种根节点
	 * @return
	 */
	public static CloudTreeNode getRootNodeInstance(byte type) {
		CloudTreeNode node = new CloudTreeNode();
		node.setId(type);
		node.setNodeId(type);
		node.setNodeType(type);
		node.setLoaded(false);
		node.collapse();
		switch (type) {
		case CloudConstants.FILES_ROOT:
			node.setText("资料");
			node.setIconCls("icon-root-file");
			break;
		case CloudConstants.FAVORITES_ROOT:
			node.setText("收藏");
			node.setIconCls("icon-root-favorites");
			break;
		case CloudConstants.SUBSCRIPTIONS_ROOT:
			node.setText("订阅");
			node.setIconCls("icon-root-rss");
			break;
		case CloudConstants.PUSHES_ROOT:
			node.setText("推送");
			node.setIconCls("icon-root-push");
			break;
		default:
			node = null;
		}
		return node;
	}

	/**
	 * 生成一个节点，如果给定的类型不符合规范，则返回null<br>
	 *
	 * 为了确保CloudTreeNode树的id不重复，在加载不同类型的节点时要给其id加上一个系数作为返回到前台的节点id<br>
	 * LIST:		id = id + 10,000,000<br>
	 * FAVOR_LIST:	id = id + 20,000,000<br>
	 * RSS_LIST:	id = id + 30,000,000<br>
	 * PUSH_LIST:	id = id + 40,000,000<br>
	 * FILE:			id = id<br>
	 * FAVOR_LIST/LINK:	id = id + 50,000,000<br>
	 * RSS_LIST/LINK:	id = id + 60,000,000<br>
	 * PUSH_ROOT/LINK:	id = id + 70,000,000<br>
	 * PUSH_LIST/LINK:	id = id + 80,000,000<br>
	 * 
	 * @param type
	 *            CloudConstants中定义的节点类型
	 * @param id
	 *            节点id
	 * @param nodeId
	 *            节点对应实际资源的id
	 * @param text
	 *            标题
	 * @param iconCls
	 *            图标，根节点和栏目节点图标由系统自动设置
	 * @return
	 */
	public static CloudTreeNode getInstance(long id, byte type, long nodeId, String text, String iconCls) {
		CloudTreeNode node = new CloudTreeNode();

		node.setNodeId(nodeId);
		node.setNodeType(type);
		node.setLoaded(false);
		node.setText(text);
		switch (type) {
		case CloudConstants.LIST:
			node.setId(id + 10000000);
			node.collapse();
			node.setIconCls("icon-folder-documents");
			break;
		case CloudConstants.FAVOR_LIST:
			node.setId(id + 20000000);
			node.collapse();
			node.setIconCls("icon-folder-favorites");
			break;
		case CloudConstants.RSS_LIST:
			node.setId(id + 30000000);
			node.collapse();
			node.setIconCls("icon-folder-rss");
			break;
		case CloudConstants.PUSH_LIST:
			node.setId(id + 40000000);
			node.collapse();
			node.setIconCls("icon-folder-push");
			break;
		case CloudConstants.FILE:
			node.setId(id);
			node.setState(null);
			node.setIconCls(iconCls);
			break;
		case CloudConstants.FAVOR_LIST_LINK:
			node.setId(id + 50000000);
			node.setState(null);
			node.setIconCls(iconCls);
			break;
		case CloudConstants.RSS_LIST_LINK:
			node.setId(id + 60000000);
			node.setState(null);
			node.setIconCls(iconCls);
			break;
		case CloudConstants.PUSHES_ROOT_LINK:
			node.setId(id + 70000000);
			node.setState(null);
			node.setIconCls(iconCls);
			break;
		case CloudConstants.PUSH_LIST_LINK:
			node.setId(id + 80000000);
			node.setState(null);
			node.setIconCls(iconCls);
			break;
		default:
			node = getRootNodeInstance(type);
		}
		return node;
	}

	@Override
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	@JSON(serialize = false)
	public String getName() {
		return name;
	}

	@Override
	@JSON(serialize = false)
	public String getAlias() {
		return name;
	}

	@Override
	public String getText() {
		return name;
	}

	public void setText(String text) {
		this.name = text;
	}

	@Override
	public List<CloudTreeNode> getChildren() {
		return children;
	}

	public void setChildren(List<CloudTreeNode> children) {
		this.children = children;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

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

	public boolean isLoaded() {
		return loaded;
	}

	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}

	@Override
	public void expand() {
		state = "open";
	}

	@Override
	public void collapse() {
		state = "closed";
	}

	public void addChild(CloudTreeNode child) {
		children.add(child);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((iconCls == null) ? 0 : iconCls.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + (loaded ? 1231 : 1237);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + (int) (nodeId ^ (nodeId >>> 32));
		result = prime * result + nodeType;
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CloudTreeNode other = (CloudTreeNode) obj;
		if (iconCls == null) {
			if (other.iconCls != null)
				return false;
		} else if (!iconCls.equals(other.iconCls))
			return false;
		if (id != other.id)
			return false;
		if (loaded != other.loaded)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (nodeId != other.nodeId)
			return false;
		if (nodeType != other.nodeType)
			return false;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CloudTreeNode [id=").append(id).append(", name=").append(name).append(", state=").append(state).append(", iconCls=").append(iconCls)
				.append(", nodeId=").append(nodeId).append(", nodeType=").append(nodeType).append(", loaded=").append(loaded).append("]");
		return builder.toString();
	}

	@Override
	public int compareTo(CloudTreeNode o) {
		String s1 = this.getName();
		String s2 = o.getName();
		
		if (null == s1) {
			if (null == s2) {
				return 0;
			} else {
				return -1;
			}
		} else {
			if (null == s2) {
				return 1;
			} else {
				return Collator.getInstance(java.util.Locale.CHINA).compare(s1, s2);
			}
		}
	}
}
