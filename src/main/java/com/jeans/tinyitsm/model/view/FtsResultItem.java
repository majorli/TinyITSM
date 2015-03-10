package com.jeans.tinyitsm.model.view;

import java.io.Serializable;

public class FtsResultItem implements Serializable {

	private long id;
	private long type;
	private String title;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getType() {
		return type;
	}

	public void setType(long type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
