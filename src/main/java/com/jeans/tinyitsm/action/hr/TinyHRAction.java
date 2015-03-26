package com.jeans.tinyitsm.action.hr;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.jeans.tinyitsm.action.BaseAction;
import com.jeans.tinyitsm.model.view.HRUnit;
import com.jeans.tinyitsm.service.hr.ChildrenType;
import com.jeans.tinyitsm.service.hr.HRConstants;
import com.jeans.tinyitsm.service.hr.HRService;
import com.jeans.tinyitsm.service.hr.OrgTreeType;

public class TinyHRAction extends BaseAction<List<HRUnit>> {

	private HRService hrService;

	@Autowired
	public void setHrService(HRService hrService) {
		this.hrService = hrService;
	}

	@Action(value = "get-tree", results = { @Result(type = "json", params = { "root", "data" }) })
	public String execute() throws Exception {
		long compId = getEnvironment().getCurrentUser(getSession()).getCompanyId();
		data = hrService.getOrgTree(compId, OrgTreeType.FullTree, true);
		return SUCCESS;
	}

	private int type;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Action(value = "get-children", results = { @Result(type = "json", params = { "root", "data" }) })
	public String getChildren() throws Exception {
		data = hrService.getChildren(id, ChildrenType.values()[type]);
		return SUCCESS;
	}

	@Action(value = "add-dept", results = { @Result(type = "json", params = { "root", "data" }) })
	public String newDepartment() throws Exception {
		data = new ArrayList<HRUnit>();
		HRUnit n = hrService.create(id, HRConstants.DEPARTMENT);
		if (null != n)
			data.add(n);
		return SUCCESS;
	}

	@Action(value = "add-empl", results = { @Result(type = "json", params = { "root", "data" }) })
	public String newEmployee() throws Exception {
		data = new ArrayList<HRUnit>();
		HRUnit e = hrService.create(id, HRConstants.EMPLOYEE);
		if (null != e)
			data.add(e);
		return SUCCESS;
	}

	private int count;
	private List<HRUnit> updates;

	public int isCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<HRUnit> getUpdates() {
		return updates;
	}

	public void setUpdates(List<HRUnit> updates) {
		this.updates = updates;
	}

	@Action(value = "update-depts", results = { @Result(type = "json", params = { "root", "count" }) }, interceptorRefs = { @InterceptorRef("json"),
			@InterceptorRef("defaultStack") })
	public String updateDepartments() throws Exception {
		count = hrService.update(updates, HRConstants.DEPARTMENT);
		return SUCCESS;
	}

	@Action(value = "update-empls", results = { @Result(type = "json", params = { "root", "count" }) }, interceptorRefs = { @InterceptorRef("json"),
			@InterceptorRef("defaultStack") })
	public String updateEmployees() throws Exception {
		count = hrService.update(updates, HRConstants.EMPLOYEE);
		return SUCCESS;
	}

	/**
	 * count: numbers of departments have been abandoned. or 0 if any exceptions ocurred, or -1 if the department is not empty.
	 */
	@Action(value = "abandon-depts", results = { @Result(type = "json", params = { "root", "count" }) })
	public String abandonDepartment() throws Exception {
		count = hrService.departmentAbandon(id);
		return SUCCESS;
	}

	private long newDeptId;
	private boolean succ;

	public boolean isSucc() {
		return succ;
	}

	public void setSucc(boolean succ) {
		this.succ = succ;
	}

	public long getNewDeptId() {
		return newDeptId;
	}

	public void setNewDeptId(long newDeptId) {
		this.newDeptId = newDeptId;
	}

	@Action(value = "move-empl", results = { @Result(type = "json", params = { "root", "succ" }) })
	public String move() throws Exception {
		succ = hrService.employeeMove(id, newDeptId);
		return SUCCESS;
	}

	@Action(value = "leave-empl", results = { @Result(type = "json", params = { "root", "succ" }) })
	public String leave() throws Exception {
		succ = hrService.employeeLeave(id);
		return SUCCESS;
	}

	@Action(value = "get-targets", results = { @Result(type = "json", params = { "root", "data" }) })
	public String moveTarget() throws Exception {
		if (id == 0) {
			/* 获取浙江烟草商业专卖系统完整的省市县三级公司树 */
			data = hrService.getOrgTree(1L, OrgTreeType.BranchesTree, true);
		} else {
			/* 获取公司id下面的部门树 */
			data = hrService.getOrgTree(id, OrgTreeType.DepartmentsTree, false);
		}
		return SUCCESS;
	}

	@Action(value = "get-curr-depts", results = { @Result(type = "json", params = { "root", "data" }) })
	public String getDepartments() throws Exception {
		data = hrService.getOrgTree(getCurrentCompanyId(), OrgTreeType.DepartmentsTree, false);
		return SUCCESS;
	}

	@Action(value = "get-companies", results = { @Result(type = "json", params = { "root", "data" }) })
	public String getCompanies() throws Exception {
		data = hrService.getOrgTree(getCurrentCompanyId(), OrgTreeType.BranchesTree, true);
		return SUCCESS;
	}
}
