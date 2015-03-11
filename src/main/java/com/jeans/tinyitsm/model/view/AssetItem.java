package com.jeans.tinyitsm.model.view;

import java.io.Serializable;

public interface AssetItem extends Serializable {
	public long getId();
	public void setId(long id);
	public String getFullName();
	public void setFullName(String fullName);
}
