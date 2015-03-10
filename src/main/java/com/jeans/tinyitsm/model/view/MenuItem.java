package com.jeans.tinyitsm.model.view;

import java.io.Serializable;
import java.util.List;

public class MenuItem implements Serializable {

	private String id;
	private String text;
	private List<MenuItem> children;

	public MenuItem() {}
	
	public MenuItem(String id, String text) {
		this.id = id;
		this.text = text;
		this.children = null;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<MenuItem> getChildren() {
		return children;
	}

	public void setChildren(List<MenuItem> children) {
		this.children = children;
	}
}
