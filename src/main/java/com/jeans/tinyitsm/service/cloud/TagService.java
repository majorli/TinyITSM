package com.jeans.tinyitsm.service.cloud;

import java.util.Set;

import com.jeans.tinyitsm.model.cloud.Tag;

public interface TagService {

	/**
	 * 根据字符串更新标签数据库并返回字符串中所表示的所有标签
	 * 
	 * @param tags
	 *            标签字符串，多个标签用空格
	 * @return
	 */
	public Set<Tag> loadAndUpdate(String tags);
}
