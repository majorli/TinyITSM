package com.jeans.tinyitsm.model;

import java.util.List;

public interface TreeNode {
	public long getId();
	public String getName();
	public String getAlias();
	public String getText();
	public List<? extends TreeNode> getChildren();
	public void expand();
	public void collapse();
}
