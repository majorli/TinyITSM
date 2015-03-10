package com.jeans.tinyitsm.action;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.jeans.tinyitsm.model.view.FtsResultItem;
import com.jeans.tinyitsm.service.portal.FtsService;

public class FTSAction extends TinyAction {

	private FtsService ftsService;

	@Autowired
	public void setFtsService(FtsService ftsService) {
		this.ftsService = ftsService;
	}
	
	private Map<String, List<FtsResultItem>> results;

	public Map<String, List<FtsResultItem>> getResults() {
		return results;
	}

	public void setResults(Map<String, List<FtsResultItem>> results) {
		this.results = results;
	}

	private String catalog;
	private String keyword;

	public String getCatalog() {
		return catalog;
	}

	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	private int count;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	@Action(value = "fts", results = { @Result(name = SUCCESS, location = "portal/fts.jsp") })
	public String fts() throws Exception {
		if (StringUtils.isBlank(keyword) || StringUtils.isBlank(catalog)) {
			count = 0;
		} else {
			results = ftsService.search(catalog, keyword, getCurrentUser());
			count = 0;
			for (String t : results.keySet()) {
				count += results.get(t).size();
			}
		}
		return SUCCESS;
	}

	private boolean success;
	
	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	@Action(value = "fts-reindex", results = { @Result(type = "json", params = { "root", "success" }) })
	public String reIndex() throws Exception {
		try {
			ftsService.reIndex();
			success = true;
		} catch (InterruptedException e) {
			log(e);
			success = false;
		}
		return SUCCESS;
	}
}
