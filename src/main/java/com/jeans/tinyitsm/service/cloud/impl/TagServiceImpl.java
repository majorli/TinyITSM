package com.jeans.tinyitsm.service.cloud.impl;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeans.tinyitsm.dao.BaseDao;
import com.jeans.tinyitsm.model.cloud.Tag;
import com.jeans.tinyitsm.service.cloud.TagService;

@Service
public class TagServiceImpl implements TagService {

	private BaseDao<Tag> tagDao;

	@Autowired
	public void setTagDao(BaseDao<Tag> tagDao) {
		this.tagDao = tagDao;
	}
	
	@Override
	@Transactional
	public Set<Tag> loadAndUpdate(String tags) {
		Set<Tag> results = new HashSet<Tag>();
		
		if (StringUtils.isBlank(tags))
			return results;
		
		String[] ts = tags.trim().split("\\s+");
		for (String t : ts) {
			String hql = "from Tag where title = '" + t + "'";
			Tag tag = tagDao.getByHql(hql);
			if (null == tag) {
				tag = new Tag();
				tag.setTitle(t);
				tagDao.save(tag);
			}
			results.add(tag);
		}
		return results;
	}
}
