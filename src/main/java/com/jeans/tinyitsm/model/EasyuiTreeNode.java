package com.jeans.tinyitsm.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EasyuiTreeNode implements Serializable {

	private long id;
	private String text;
	private String iconCls;
	private List<EasyuiTreeNode> children;
	
	private String state = null;
	
	public static EasyuiTreeNode createInstance(long id, String text, String iconCls) {
		EasyuiTreeNode node = new EasyuiTreeNode();
		node.setId(id);
		node.setText(text);
		node.setIconCls(iconCls);
		return node;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	public List<EasyuiTreeNode> getChildren() {
		return children;
	}

	public void setChildren(List<EasyuiTreeNode> children) {
		this.children = children;
	}

	public void addChild(EasyuiTreeNode child) {
		if (null == children) {
			children = new ArrayList<EasyuiTreeNode>();
		}
		children.add(child);
	}

	public void removeChild(EasyuiTreeNode child) {
		if (null != children && children.size() > 0) {
			children.remove(child);
		}
	}
	
	public String getState() {
		return state;
	}
	
	public void expand() {
		if (null != children && children.size() > 0) {
			state = "open";
		}
	}
	
	public void collapse() {
		if (null != children && children.size() > 0) {
			state = "closed";
		}
	}
}
