package com.jeans.tinyitsm.action.portal;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.jeans.tinyitsm.action.TinyAction;
import com.jeans.tinyitsm.model.portal.User;
import com.jeans.tinyitsm.model.view.HRUnit;
import com.jeans.tinyitsm.service.hr.HRConstants;
import com.jeans.tinyitsm.service.hr.HRService;
import com.jeans.tinyitsm.service.portal.UserService;

public class UserAction extends TinyAction {

	private UserService userService;
	private HRService hrService;

	@Autowired
	public void setuserService(UserService userService) {
		this.userService = userService;
	}

	@Autowired
	public void setHrService(HRService hrService) {
		this.hrService = hrService;
	}

	private String keyword;
	private Map<String, Object> tuser;

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Map<String, Object> getTuser() {
		return tuser;
	}

	public void setTuser(Map<String, Object> tuser) {
		this.tuser = tuser;
	}

	@Action(value = "seek-user", results = { @Result(type = "json", params = { "root", "tuser" }) })
	public String seekUser() throws Exception {
		tuser = new HashMap<String, Object>();
		tuser.put("user", null);
		tuser.put("employee", "");
		tuser.put("department", "");
		User user = userService.findUser(getCurrentCompanyId(), keyword);
		if (null != user) {
			tuser.put("user", user);
			HRUnit empl = hrService.getUnit(user.getEmployeeId(), HRConstants.EMPLOYEE);
			if (null != empl) {
				tuser.put("employee", empl.getName());
				tuser.put("department", empl.getAlias());
			}
		}
		return SUCCESS;
	}

	private String roles;
	private boolean success;

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	@Action(value = "set-roles", results = { @Result(type = "json", params = { "root", "success" }) })
	public String setRoles() throws Exception {
		String[] role = roles.split(",");
		long id = Long.parseLong(role[0]);
		boolean iter = Boolean.parseBoolean(role[1]);
		boolean admin = Boolean.parseBoolean(role[2]);
		boolean leader = Boolean.parseBoolean(role[3]);
		boolean supervisor = Boolean.parseBoolean(role[4]);
		boolean auditor = Boolean.parseBoolean(role[5]);
		success = userService.updateRoles(id, iter, admin, leader, supervisor, auditor);
		return SUCCESS;
	}

	private long userId;
	private String oldPassword;
	private String newPassword;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	@Action(value = "change-pwd", results = { @Result(type = "json", params = { "root", "success" }) })
	public String changePassword() throws Exception {
		if (userId != 0) {
			success = userService.changePassword(userId, oldPassword, newPassword);
		} else {
			success = userService.changePassword(0, getCurrentCompanyId(), oldPassword, newPassword);
		}
		return SUCCESS;
	}

	private String newUsn;

	public String getNewUsn() {
		return newUsn;
	}

	public void setNewUsn(String newUsn) {
		this.newUsn = newUsn;
	}

	@Action(value = "rename", results = { @Result(name = SUCCESS, type = "redirectAction", location = "../func", params = { "code", "USR_SET" }),
			@Result(name = ERROR, type = "redirectAction", location = "../error-handler", params = { "c", "3" }) })
	public String rename() throws Exception {
		if (StringUtils.isBlank(newUsn))
			newUsn = getCurrentEmployee().getName();
		if (userService.rename(getCurrentUserId(), newUsn)) {
			getCurrentUser().setUsername(newUsn);
			return SUCCESS;
		} else {
			return ERROR;
		}
	}
}
