package com.jeans.tinyitsm.action.maintain;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.jeans.tinyitsm.action.BaseAction;
import com.jeans.tinyitsm.model.it.Maintenance;
import com.jeans.tinyitsm.model.view.MaintenanceItem;
import com.jeans.tinyitsm.model.view.MenuItem;
import com.jeans.tinyitsm.service.it.MaintenanceService;

public class MaintenanceAction extends BaseAction<MaintenanceItem> {

	private MaintenanceService mtnService;

	@Autowired
	public void setMtnService(MaintenanceService mtnService) {
		this.mtnService = mtnService;
	}

	private List<MenuItem> items;

	public List<MenuItem> getItems() {
		return items;
	}

	public void setItems(List<MenuItem> items) {
		this.items = items;
	}

	@Action(value = "mtn-list", results = { @Result(type = "json", params = { "root", "items" }) })
	public String loadMaintenance() throws Exception {
		List<Maintenance> mtns = mtnService.getMaintenances(getCurrentCompanyId());
		items = new ArrayList<MenuItem>();
		for (Maintenance mtn : mtns) {
			items.add(new MenuItem(Long.toString(mtn.getId()),mtn.getName()));
		}
		return SUCCESS;
	}
}
