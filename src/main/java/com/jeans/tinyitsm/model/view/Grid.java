package com.jeans.tinyitsm.model.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Grid<T> implements Serializable {

	private long total = 0;
	private List<T> rows = new ArrayList<T>();

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public List<T> getRows() {
		return rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}
}
