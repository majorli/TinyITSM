package com.jeans.tinyitsm.dao;

import java.util.List;

public interface FtsDao<T> {

	/**
	 * 初始化搜索引擎索引库
	 * @throws InterruptedException 
	 */
	public void reIndex() throws InterruptedException;

	/**
	 * 按关键字检索
	 * 
	 * @param c
	 * @param fields
	 * @param keyword
	 * @return
	 */
	public List<T> query(Class<T> c, String[] fields, String keyword);

	/**
	 * 限定范围内搜索
	 * 
	 * @param c
	 * @param fields
	 * @param keyword
	 * @param scopeField
	 * @param scopeId
	 * @return
	 */
	public List<T> query(Class<T> c, String[] fields, String keyword, String scopeField, long scopeId);

	/**
	 * 模糊、精确和限定范围混合检索
	 * 
	 * @param c
	 * @param fuzzyFields
	 * @param exactFields
	 * @param keyword
	 * @param scopeField
	 * @param scopeId
	 * @return
	 */
	public List<T> query(Class<T> c, String[] fuzzyFields, String[] exactFields, String keyword, String scopeField, long scopeId);
}
