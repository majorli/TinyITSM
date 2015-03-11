package com.jeans.tinyitsm.action.cloud;

import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.jeans.tinyitsm.action.BaseAction;
import com.jeans.tinyitsm.model.view.CloudGridRow;
import com.jeans.tinyitsm.model.view.Grid;
import com.jeans.tinyitsm.service.cloud.CloudViewService;

public class CloudViewAction extends BaseAction<Grid<CloudGridRow>> {

	private CloudViewService service;

	@Autowired
	public void setService(CloudViewService service) {
		this.service = service;
	}

	private int docType;

	public int getDocType() {
		return docType;
	}

	public void setDocType(int docType) {
		this.docType = docType;
	}

	/**
	 * 获取特定类型的文件资料，分页，每页20条
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "view-files", results = { @Result(type = "json", params = { "root", "data" }) })
	public String loadFiles() throws Exception {
		data = service.loadFiles(docType, page, rows, getCurrentUser());
		if (null != sort && !("name".equals(sort) && "asc".equals(order))) {
			Collections.sort(data.getRows(), new Comparator<CloudGridRow>() {

				@Override
				public int compare(CloudGridRow o1, CloudGridRow o2) {
					int ret = 0;
					boolean asc = "asc".equals(order);
					switch (sort) {
					case "name":
						ret = compString(o1.getName(), o2.getName(), asc);
						break;
					case "list":
						ret = compString(o1.getList(), o2.getList(), asc);
						break;
					case "size":
						ret = asc ? Long.compare(o1.getSize(), o2.getSize()) : Long.compare(o2.getSize(), o1.getSize());
						break;
					case "createTime":
						ret = asc ? o1.getCreateTime().compareTo(o2.getCreateTime()) : o2.getCreateTime().compareTo(o1.getCreateTime());
					}
					return ret;
				}

				private int compString(String s1, String s2, boolean asc) {
					if (null == s1) {
						if (null == s2) {
							return 0;
						} else {
							return asc ? -1 : 1;
						}
					} else {
						if (null == s2) {
							return asc ? 1 : -1;
						} else {
							return asc ? Collator.getInstance(java.util.Locale.CHINA).compare(s1, s2) : Collator.getInstance(java.util.Locale.CHINA).compare(
									s2, s1);
						}
					}
				}

			});
		}
		return SUCCESS;
	}
}
