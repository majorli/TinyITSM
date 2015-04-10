package com.jeans.tinyitsm.service.it.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeans.tinyitsm.dao.BaseDao;
import com.jeans.tinyitsm.model.it.Wiki;
import com.jeans.tinyitsm.service.it.WikiService;

@Service
public class WikiServiceImpl implements WikiService {

	private BaseDao<Wiki> wikiDao;

	@Autowired
	public void setWikiDao(BaseDao<Wiki> wikiDao) {
		this.wikiDao = wikiDao;
	}

	@Override
	@Transactional(readOnly = true)
	public Wiki getWiki(long id) {
		return wikiDao.getById(Wiki.class, id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Wiki> getWikies() {
		return wikiDao.find("from Wiki");
	}

}
