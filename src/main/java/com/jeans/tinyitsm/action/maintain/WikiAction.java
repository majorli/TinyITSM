package com.jeans.tinyitsm.action.maintain;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.jeans.tinyitsm.action.BaseAction;
import com.jeans.tinyitsm.model.it.Wiki;
import com.jeans.tinyitsm.model.view.MenuItem;
import com.jeans.tinyitsm.service.it.WikiService;

public class WikiAction extends BaseAction<Wiki> {

	private WikiService wikiService;

	@Autowired
	public void setWikiService(WikiService wikiService) {
		this.wikiService = wikiService;
	}

	private List<MenuItem> items;

	public List<MenuItem> getItems() {
		return items;
	}

	public void setItems(List<MenuItem> items) {
		this.items = items;
	}

	@Action(value = "wiki-list", results = { @Result(type = "json", params = { "root", "items" }) })
	public String wikiList() throws Exception {
		items = new ArrayList<MenuItem>();
		List<Wiki> wikies = wikiService.getWikies();
		for (Wiki wiki : wikies) {
			items.add(new MenuItem(Long.toString(wiki.getId()), wiki.getName()));
		}
		return SUCCESS;
	}
}
