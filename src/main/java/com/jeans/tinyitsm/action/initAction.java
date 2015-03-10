package com.jeans.tinyitsm.action;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.jeans.tinyitsm.Environment;
import com.jeans.tinyitsm.HREventObserver;
import com.jeans.tinyitsm.event.HREventListener;
import com.jeans.tinyitsm.model.view.HRUnit;
import com.jeans.tinyitsm.service.hr.HRConstants;
import com.jeans.tinyitsm.service.hr.HRService;
import com.jeans.tinyitsm.service.hr.OrgTreeType;
import com.jeans.tinyitsm.util.TreeUtil;

public class initAction extends TinyAction {

	private HRService service;
	
	@Autowired
	public void setService(HRService service) {
		this.service = service;
	}
	
	private HREventListener hrListener;
	
	@Autowired
	public void setHREventHandler(HREventListener hrListener) {
		this.hrListener = hrListener;
	}
	
	private long emplId;
	private long deptId;
	private long compId;

	public long getEmplId() {
		return emplId;
	}

	public void setEmplId(long emplId) {
		this.emplId = emplId;
	}

	public long getDeptId() {
		return deptId;
	}

	public void setDeptId(long deptId) {
		this.deptId = deptId;
	}

	public long getCompId() {
		return compId;
	}

	public void setCompId(long compId) {
		this.compId = compId;
	}

	private boolean initSuccess;
	private List<HRUnit> companies;
	private List<HRUnit> departments;
	private List<HRUnit> employees;

	public List<HRUnit> getCompanies() {
		return companies;
	}

	public void setCompanies(List<HRUnit> companies) {
		this.companies = companies;
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

	public boolean isInitSuccess() {
		return initSuccess;
	}

	public void setInitSuccess(boolean initSuccess) {
		this.initSuccess = initSuccess;
	}

	/**
	 * init system environment. create an Environment instance and init it
	 * 
	 * @return json: boolean initResult, true for success, false for fail.
	 * 
	 * @throws Exception
	 */
	@Action(results = { @Result(type = "json") })
	public String execute() throws Exception {
		Environment e = Environment.getInstance();
		e.init();
		initSuccess = (e.getErrorMessages() != null && hrListener != null);

		// TODO: 事件处理器都需要在这里进行注入
		HREventObserver.set(hrListener);

		if (initSuccess) {
			getSession().put("extraHR", e.isExtraHR());
			getSession().put("extraCI", e.isExtraCI());
			
			companies = new ArrayList<HRUnit>();
			companies.add(new HRUnit(0, "请选择所在公司", "请选择所在公司", HRConstants.COMPANY, (short) 0, null));
			List<HRUnit> comps = service.getOrgTree(1L, OrgTreeType.BranchesTree, true);
			TreeUtil.expandOrgTree(comps, 0);
			companies.addAll(comps);

			departments = new ArrayList<HRUnit>();
			departments.add(new HRUnit(0, "请选择所在部门", "请选择所在部门", HRConstants.DEPARTMENT, (short) 0, null));
			if (compId > 0) {
				List<HRUnit> depts = service.getOrgTree(compId, OrgTreeType.DepartmentsTree, false);
				departments.addAll(depts);
			}

			employees = new ArrayList<HRUnit>();
			if (compId > 0 && deptId == 0)
				employees.add(new HRUnit(0, "管理员", "管理员", HRConstants.EMPLOYEE, (short) 0, null));
			if (deptId > 0) {
				List<HRUnit> empls = service.getEmployees(deptId);
				employees.addAll(empls);
			}

			if (TreeUtil.seek(deptId, departments) == null)
				deptId = 0;
			if (TreeUtil.seek(emplId, employees) == null)
				emplId = 0;
		}
		return SUCCESS;
	}
}
