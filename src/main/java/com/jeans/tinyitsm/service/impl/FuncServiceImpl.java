package com.jeans.tinyitsm.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeans.tinyitsm.dao.BaseDao;
import com.jeans.tinyitsm.model.portal.Function;
import com.jeans.tinyitsm.model.portal.User;
import com.jeans.tinyitsm.model.view.MenuItem;
import com.jeans.tinyitsm.service.FuncService;

@Service
public class FuncServiceImpl implements FuncService {

	private BaseDao<Function> funcDao;

	@Autowired
	public void setFuncDao(BaseDao<Function> funcDao) {
		this.funcDao = funcDao;
	}

	@Override
	@Transactional(readOnly = true)
	public List<MenuItem> getMenu(User user) {
		List<MenuItem> menu = new ArrayList<MenuItem>();
		/**
		 *	user.admin		user.iter		criteria
		 *		T				T			ALL
		 *		T				F			admin = true
		 *		F				T			admin = false
		 *		F				F			admin = false and iter = false
		 */
		StringBuilder sb = new StringBuilder("from Function where folder = :p_folder");
		if (user.isAdmin()) {
			if (user.isIter()) {
				sb.append(" ");
			} else {
				sb.append(" and adminFunction = true ");
			}
		} else {
			if (user.isIter()) {
				sb.append(" and adminFunction = false ");
			} else {
				sb.append(" and adminFunction = false and iterFunction = false ");
			}
		}
		sb.append("order by listOrder");
		String hqlItems = sb.toString();
		
		List<Function> menus = funcDao.find("from Function where folder = '' order by listOrder");
		Iterator<Function> it = menus.iterator();
		while (it.hasNext()) {
			Function f = it.next();
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("p_folder", f.getCode());
			List<Function> items = funcDao.find(hqlItems, params);
			if (items.size() == 0)
				continue;
			MenuItem i = new MenuItem(f.getCode(), f.getTitle());
			List<MenuItem> children = new ArrayList<MenuItem>();
			Iterator<Function> it_items = items.iterator();
			while (it_items.hasNext()) {
				Function f_item = it_items.next();
				MenuItem i_item = new MenuItem(f_item.getCode(), f_item.getTitle());
				children.add(i_item);
			}
			i.setChildren(children);
			menu.add(i);
		}

		return menu;
	}

	@Override
	@Transactional(readOnly = true)
	public Function getFunction(String code) {
		Map<String, Object> naturalIds = new HashMap<String, Object>();
		naturalIds.put("code", code);
		return funcDao.getByNaturalId(Function.class, naturalIds);
	}
}
