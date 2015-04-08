package com.jeans.tinyitsm.action.project;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.jeans.tinyitsm.action.TinyAction;
import com.jeans.tinyitsm.model.hr.Organization;
import com.jeans.tinyitsm.model.view.HRUnit;
import com.jeans.tinyitsm.model.view.MenuItem;
import com.jeans.tinyitsm.service.hr.HRConstants;
import com.jeans.tinyitsm.service.hr.HRService;
import com.jeans.tinyitsm.service.it.enums.SystemScope;
import com.jeans.tinyitsm.util.HrUtil;

public class ParamsAction extends TinyAction {

	private HRService hrService;

	@Autowired
	public void setHrService(HRService hrService) {
		this.hrService = hrService;
	}

	private List<MenuItem> items;

	public List<MenuItem> getItems() {
		return items;
	}

	public void setItems(List<MenuItem> items) {
		this.items = items;
	}

	@Action(value = "system-scopes", results = { @Result(type = "json", params = { "root", "items" }) })
	public String scope() throws Exception {
		items = new ArrayList<MenuItem>();
		Organization company = hrService.getCompany(getCurrentCompanyId());
		if (null != company) {
			byte level = HrUtil.getCompanyLevel(company);
			if (level == HRConstants.PROVINCE) {
				// Private, PrivateAndInferiors, PrivateAndDirectInferiors, DirectInferiors, Inferiors, Custom;
				items.add(new MenuItem(Integer.toString(SystemScope.Private.ordinal()), SystemScope.Private.getTitle(level)));
				items.add(new MenuItem(Integer.toString(SystemScope.PrivateAndInferiors.ordinal()), SystemScope.PrivateAndInferiors.getTitle(level)));
				items.add(new MenuItem(Integer.toString(SystemScope.PrivateAndDirectInferiors.ordinal()), SystemScope.PrivateAndDirectInferiors.getTitle(level)));
				items.add(new MenuItem(Integer.toString(SystemScope.DirectInferiors.ordinal()), SystemScope.DirectInferiors.getTitle(level)));
				items.add(new MenuItem(Integer.toString(SystemScope.Inferiors.ordinal()), SystemScope.Inferiors.getTitle(level)));
				items.add(new MenuItem(Integer.toString(SystemScope.Custom.ordinal()), SystemScope.Custom.getTitle(level)));
			} else if (level == HRConstants.CITY) {
				// Private, PrivateAndInferiors, Inferiors, Custom;
				items.add(new MenuItem(Integer.toString(SystemScope.Private.ordinal()), SystemScope.Private.getTitle(level)));
				items.add(new MenuItem(Integer.toString(SystemScope.PrivateAndInferiors.ordinal()), SystemScope.PrivateAndInferiors.getTitle(level)));
				items.add(new MenuItem(Integer.toString(SystemScope.Inferiors.ordinal()), SystemScope.Inferiors.getTitle(level)));
				items.add(new MenuItem(Integer.toString(SystemScope.Custom.ordinal()), SystemScope.Custom.getTitle(level)));
			} else if (level == HRConstants.BRANCH) {
				items.add(new MenuItem(Integer.toString(SystemScope.Private.ordinal()), SystemScope.Private.getTitle(level)));
			}
		}
		return SUCCESS;
	}

	@Action(value = "potential-branches", results = { @Result(type = "json", params = { "root", "items" }) })
	public String potentialBranches() throws Exception {
		items = new ArrayList<MenuItem>();
		if (1 == getCurrentCompanyId()) {
			List<HRUnit> branches = hrService.getCompChildren(getCurrentCompanyId());
			for (HRUnit branch : branches) {
				items.add(new MenuItem(Long.toString(branch.getId()), branch.getAlias()));
			}
		}
		return SUCCESS;
	}
}
