package com.jeans.tinyitsm.service;

import java.util.List;

import com.jeans.tinyitsm.model.portal.Function;
import com.jeans.tinyitsm.model.portal.User;
import com.jeans.tinyitsm.model.view.MenuItem;


public interface FuncService {
	
	public List<MenuItem> getMenu(User user);
	
	public Function getFunction(String code);
}
