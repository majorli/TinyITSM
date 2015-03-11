package com.jeans.tinyitsm.action;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

public class BaseAction<T> extends TinyAction {

	protected long id;				// 主键
	protected String ids;			// 多个主键的列表，用逗号分隔
	protected int page = 1;			// 请求Datagrid数据：当前页，0表示不分页
	protected int rows = 20;		// 请求Datagrid数据：每页显示记录数，0表示不分页
	protected String sort;			// 请求Datagrid数据：排序字段
	protected String order = "asc";	// 请求Datagrid数据：asc, desc, null或者空字符串表示asc
	protected int expandLevel = 0;	// 请求Tree数据：展开的层次，0表示全部展开
	protected T data;				// 前后端传递数据的泛型字段

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public int getExpandLevel() {
		return expandLevel;
	}

	public void setExpandLevel(int expandLevel) {
		this.expandLevel = expandLevel;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
	
	protected Set<Long> splitIds() {
		Set<Long> idSet = new HashSet<Long>();
		if (!StringUtils.isBlank(ids)) {
			String[] stringIds = ids.trim().split(",");
			for (String id : stringIds) {
				try {
					idSet.add(Long.parseLong(id.trim()));
				} catch (NumberFormatException e) {
				}
			}
		}
		return idSet;
	}
}
