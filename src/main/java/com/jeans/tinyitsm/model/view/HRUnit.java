package com.jeans.tinyitsm.model.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.json.annotations.JSON;

import com.jeans.tinyitsm.model.TreeNode;

public class HRUnit implements Serializable, TreeNode {

	private long id;
	private String name;
	private String alias;
	private byte type;
	private String iconCls;
	private short listOrder;
	private String state;
	private List<HRUnit> children = new ArrayList<HRUnit>();

	private static final String[] iconClsNames = { "icon-company", "icon-department", "icon-employee" };

	public HRUnit() {
	}

	public HRUnit(long id, String name, String alias, byte type, short listOrder, List<HRUnit> children) {
		super();
		this.id = id;
		this.name = name;
		this.alias = alias;
		this.type = type;
		this.iconCls = iconClsNames[this.type];
		this.listOrder = listOrder;
		this.state = "open";
		this.children = (children == null) ? new ArrayList<HRUnit>() : children;
	}

	@Override
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	@Override
	public List<HRUnit> getChildren() {
		return children;
	}

	public void setChildren(List<HRUnit> children) {
		this.children = (children == null) ? new ArrayList<HRUnit>() : children;
	}

	@JSON(serialize=false)
	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	public short getListOrder() {
		return listOrder;
	}

	public void setListOrder(short listOrder) {
		this.listOrder = listOrder;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	@Override
	public String getText() {
		return alias;
	}
	
	public void setText(String text) {}

	@Override
	public void expand() {
		this.state = "open";
	}

	@Override
	public void collapse() {
		this.state = "closed";
	}

	@Override
	public String toString() {
		final int maxLen = 10;
		StringBuilder builder = new StringBuilder();
		builder.append("HRUnit [id=").append(id).append(", name=").append(name).append(", alias=").append(alias).append(", type=").append(type)
				.append(", iconCls=").append(iconCls).append(", listOrder=").append(listOrder).append(", state=").append(state).append(", children=")
				.append(children != null ? children.subList(0, Math.min(children.size(), maxLen)) : null).append("]");
		return builder.toString();
	}
}
