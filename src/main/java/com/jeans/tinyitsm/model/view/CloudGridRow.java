package com.jeans.tinyitsm.model.view;

import java.io.Serializable;
import java.text.Collator;
import java.util.Date;

import org.apache.struts2.json.annotations.JSON;

import com.jeans.tinyitsm.model.cloud.CloudFile;

public class CloudGridRow implements Comparable<CloudGridRow>, Serializable {

	private long id;
	private String name;
	private String list;
	private long size;
	private Date createTime;
	private String iconCls;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getList() {
		return list;
	}

	public void setList(String list) {
		this.list = list;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	@JSON(format = "yyyy-MM-dd HH:mm:ss")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	public static CloudGridRow createRow(CloudFile file) {
		CloudGridRow row = new CloudGridRow();
		row.setId(file.getId());
		row.setName(file.getVersionFilename());
		row.setList(file.getList().getName());
		row.setSize(file.getSize());
		row.setIconCls(file.getIconCls());
		row.setCreateTime(file.getCreateTime());
		return row;
	}

	@Override
	public int compareTo(CloudGridRow o) {
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
