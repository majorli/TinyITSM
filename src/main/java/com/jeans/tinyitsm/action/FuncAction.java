package com.jeans.tinyitsm.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.jeans.tinyitsm.Environment;
import com.jeans.tinyitsm.model.portal.Function;
import com.jeans.tinyitsm.service.FuncService;
import com.opensymphony.xwork2.ActionContext;

public class FuncAction extends TinyAction {
	
	private FuncService service;
	
	@Autowired
	public void setService(FuncService service) {
		this.service = service;
	}

	private String code;
	private String target;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	@Action(results = { @Result(name = LOGIN, location = "index.jsp"), @Result(name = SUCCESS, location = "${target}"),
			@Result(name = ERROR, type = "redirectAction", location = "error-handler", params = { "c", "1", "p", "${code}" }) })
	public String execute() throws Exception {
		if (Environment.getInstance().isLoggedIn(ActionContext.getContext().getSession())) {
			if (null == code || "LOGOUT".equals(code)) {
				getEnvironment().clearSession(getSession());
				return LOGIN;
			}
			Function f = service.getFunction(code);
			if (null == f) {
				return ERROR;
			} else {
				target = f.getTarget();
				return SUCCESS;
			}
		} else {
			return LOGIN;
		}
	}
}
