package com.jeans.tinyitsm.service.portal;

import java.util.List;
import java.util.Map;

import com.jeans.tinyitsm.model.portal.User;
import com.jeans.tinyitsm.model.view.FtsResultItem;

public interface FtsService {

	/**
	 * 初始化Hibernate Search索引
	 * 
	 * @throws InterruptedException 
	 * 
	 */
	public void reIndex() throws InterruptedException;

	/**
	 * 执行搜索，搜索结果要根据当前用户进行可访问范围判断
	 * 
	 * @param catalog
	 * @param keyword
	 * @param user
	 * @return
	 */
	public Map<String, List<FtsResultItem>> search(String catalog, String keyword, User user);
}
