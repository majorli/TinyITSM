package com.jeans.tinyitsm.model.view;

import java.io.Serializable;

import com.jeans.tinyitsm.model.it.ITSystem;

public class ITSystemItem implements Serializable {

	private ITSystem system;

	public ITSystemItem() {
		this.system = null;
	}

	public ITSystemItem(ITSystem system) {
		this.system = system;
	}

	public Long getId() {
		return null == system ? null : system.getId();
	}

	public String getName() {
		return null == system ? null : system.getName();
	}

	public String getAlias() {
		return null == system ? null : system.getAlias();
	}
	// TODO properties of ITSystemItem ...
}
