package com.jeans.tinyitsm.action;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.json.annotations.JSON;
import org.springframework.beans.factory.annotation.Autowired;

import com.jeans.tinyitsm.model.view.HRUnit;
import com.jeans.tinyitsm.service.hr.HRConstants;
import com.jeans.tinyitsm.service.hr.HRService;
import com.jeans.tinyitsm.service.hr.OrgTreeType;

public class LoginFormAction extends TinyAction {

	private HRService service;
	
	@Autowired
	public void setService(HRService service) {
		this.service = service;
	}

	private long id;
	private boolean companyChanged;
	
	private List<HRUnit> departments;
	private List<HRUnit> employees;

	@JSON(serialize=false)
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	@JSON(serialize=false)
	public boolean isCompanyChanged() {
		return companyChanged;
	}
	public void setCompanyChanged(boolean companyChanged) {
		this.companyChanged = companyChanged;
	}

	public List<HRUnit> getDepartments() {
		return departments;
	}
	public void setDepartments(List<HRUnit> departments) {
		this.departments = departments;
	}

	public List<HRUnit> getEmployees() {
		return employees;
	}
	public void setEmployees(List<HRUnit> employees) {
		this.employees = employees;
	}

	@Action(results={@Result(type="json")})
	public String execute() throws Exception {
		// retrieve new departments(optional, if companyChanged==false, departments=null) and employees and return SUCCESS
		long deptId, compId;
		if (companyChanged) {
			compId = id;
			deptId = 0;
			departments = new ArrayList<HRUnit>();
			departments.add(new HRUnit(0, "请选择所在部门", "请选择所在部门", HRConstants.DEPARTMENT, (short) 0, null));
			if (compId > 0) {
				List<HRUnit> depts = service.getOrgTree(compId, OrgTreeType.DepartmentsTree, false);
				departments.addAll(depts);
			}
		} else {
			compId = 0;
			deptId = id;
			departments = null;
		}
		
		employees = new ArrayList<HRUnit>();
		if ((companyChanged && compId > 0) || (!companyChanged && deptId == 0))
			employees.add(new HRUnit(0, "管理员", "管理员", HRConstants.EMPLOYEE, (short) 0, null));
		if (deptId > 0) {
			List<HRUnit> empls = service.getEmployees(deptId);
			employees.addAll(empls);
		}
		return SUCCESS;
	}
}
