package com.jeans.tinyitsm.service.it;

import java.util.List;

import com.jeans.tinyitsm.model.it.Wiki;

public interface WikiService {

	/**
	 * 获取指定id的讨论组
	 * 
	 * @param id
	 *            讨论组id
	 * @return
	 */
	public Wiki getWiki(long id);

	/**
	 * 获取所有讨论组的列表
	 * 
	 * @return
	 */
	public List<Wiki> getWikies();
}
