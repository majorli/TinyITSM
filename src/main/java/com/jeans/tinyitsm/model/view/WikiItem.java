package com.jeans.tinyitsm.model.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.jeans.tinyitsm.model.it.ITSystem;
import com.jeans.tinyitsm.model.it.Wiki;

public class WikiItem implements Serializable {

	private Wiki wiki;

	public WikiItem() {
		this.wiki = null;
	}

	public WikiItem(Wiki wiki) {
		this.wiki = wiki;
	}

	public Long getId() {
		return null == wiki ? null : wiki.getId();
	}

	public String getName() {
		return null == wiki ? null : wiki.getName();
	}

	public String getSystems() {
		if (null == wiki) {
			return null;
		} else {
			List<String> names = new ArrayList<String>();
			Set<ITSystem> systems = wiki.getSystems();
			for (ITSystem system : systems) {
				names.add(system.getAlias());
			}
			return StringUtils.join(names, ",");
		}
	}

	public Integer getPages() {
		return null == wiki ? null : wiki.getPages().size();
	}
}
