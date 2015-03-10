package com.jeans.tinyitsm.action;

import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.jeans.tinyitsm.model.portal.User;
import com.jeans.tinyitsm.model.view.HRUnit;
import com.jeans.tinyitsm.service.FuncService;
import com.jeans.tinyitsm.service.hr.HRConstants;
import com.jeans.tinyitsm.service.hr.HRService;
import com.jeans.tinyitsm.service.portal.PortalConstants;
import com.jeans.tinyitsm.service.portal.UserService;

public class LoginAction extends TinyAction {

	private FuncService funcService;
	private UserService userService;
	private HRService hrService;

	@Autowired
	public void setFuncService(FuncService funcService) {
		this.funcService = funcService;
	}

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Autowired
	public void setHrService(HRService hrService) {
		this.hrService = hrService;
	}

	/*
	 * login.action
	 */
	private int companyId;
	private int departmentId;
	private int employeeId;
	private String password;

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public int getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(int departmentId) {
		this.departmentId = departmentId;
	}

	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	private int result;

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	@Action(value = "login", results = { @Result(type = "json", params = { "root", "result" }) }, interceptorRefs = { @InterceptorRef("loggingInterceptor"),
			@InterceptorRef("defaultStack") })
	public String login() throws Exception {
		User user = userService.getUser(companyId, employeeId);
		result = userService.userLogin(user, password);
		if (result == PortalConstants.LOGIN_SUCCESS) {
			Map<String, Object> userInfo = new HashMap<String, Object>();
			userInfo.put("user", user);
			HRUnit comp = hrService.getUnit(user.getCompanyId(), HRConstants.COMPANY);
			userInfo.put("company", comp);
			HRUnit empl = hrService.getUnit(user.getEmployeeId(), HRConstants.EMPLOYEE);
			userInfo.put("employee", empl);
			userInfo.put("menu", funcService.getMenu(user));
			getEnvironment().initSession(userInfo, getSession());
		}
		return SUCCESS;
	}

	/*
	 * user-active.action
	 */
	private User user;
	private boolean success;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	@Action(value = "user-active", results = { @Result(type = "json", params = { "root", "success" }) }, interceptorRefs = {
			@InterceptorRef("loggingInterceptor"), @InterceptorRef("defaultStack") })
	public String execute() throws Exception {
		success = userService.activeUser(user);
		if (success) {
			Map<String, Object> userInfo = new HashMap<String, Object>();
			userInfo.put("user", user);
			HRUnit comp = hrService.getUnit(user.getCompanyId(), HRConstants.COMPANY);
			userInfo.put("company", comp);
			HRUnit empl = hrService.getUnit(user.getEmployeeId(), HRConstants.EMPLOYEE);
			userInfo.put("employee", empl);
			userInfo.put("menu", funcService.getMenu(user));
			getEnvironment().initSession(userInfo, getSession());
		}
		return SUCCESS;
	}
}
