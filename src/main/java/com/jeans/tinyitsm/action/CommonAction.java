package com.jeans.tinyitsm.action;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.jeans.tinyitsm.model.portal.User;
import com.jeans.tinyitsm.model.view.HRUnit;
import com.jeans.tinyitsm.service.hr.HRConstants;
import com.jeans.tinyitsm.service.hr.HRService;
import com.jeans.tinyitsm.service.hr.OrgTreeType;
import com.jeans.tinyitsm.service.portal.UserService;

@Namespace("/common")
public class CommonAction extends TinyAction {

	private HRService hrService;
	private UserService userService;

	@Autowired
	public void setHrService(HRService hrService) {
		this.hrService = hrService;
	}

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	private long id;
	private List<HRUnit> hrData;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public List<HRUnit> getHrData() {
		return hrData;
	}

	public void setHrData(List<HRUnit> hrData) {
		this.hrData = hrData;
	}

	@Action(value = "companies", results = { @Result(type = "json", params = { "root", "hrData" }) })
	public String getFullCompanyTree() throws Exception {
		hrData = hrService.getOrgTree(id, OrgTreeType.BranchesTree, true);
		return SUCCESS;
	}

	@Action(value = "departments", results = { @Result(type = "json", params = { "root", "hrData" }) })
	public String getDepartmentTree() throws Exception {
		hrData = hrService.getOrgTree(id, OrgTreeType.DepartmentsTree, false);
		return SUCCESS;
	}

	@Action(value = "employees", results = { @Result(type = "json", params = { "root", "hrData" }) })
	public String getEmployees() throws Exception {
		hrData = hrService.getEmployees(id);
		return SUCCESS;
	}

	@Action(value = "iters", results = { @Result(type = "json", params = { "root", "hrData" }) })
	public String getIters() throws Exception {
		List<User> iters = userService.getITers(id);
		hrData = new ArrayList<HRUnit>();
		for (User u : iters) {
			hrData.add(new HRUnit(u.getId(), u.getUsername(), u.getUsername(), HRConstants.EMPLOYEE, (short) 1, null));
		}
		return SUCCESS;
	}

	@Action(value = "all-iters", results = { @Result(type = "json", params = { "root", "hrData" }) })
	public String getAllIters() throws Exception {
		hrData = hrService.getOrgTree(id, OrgTreeType.BranchesTree, true);
		loadIters(hrData);
		return SUCCESS;
	}
	
	private void loadIters(List<HRUnit> list) {
		for (HRUnit node : list) {
			List<HRUnit> children = node.getChildren();
			if (null != children && children.size() > 0) {
				loadIters(children);
			}
			List<User> iters = userService.getITers(node.getId());
			List<HRUnit> iterNodes = new ArrayList<HRUnit>();
			for (User u : iters) {
				iterNodes.add(new HRUnit(u.getId(), u.getUsername(), u.getUsername(), HRConstants.EMPLOYEE, (short) 1, null));
			}
			children.addAll(0, iterNodes);
		}
	}
}
