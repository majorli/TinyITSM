package com.jeans.tinyitsm.action;

import java.util.Map;

import com.jeans.tinyitsm.Environment;
import com.jeans.tinyitsm.model.portal.User;
import com.jeans.tinyitsm.model.view.HRUnit;
import com.jeans.tinyitsm.util.LoggerUtil;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class TinyAction extends ActionSupport {
	
	/**
	 * 获取当前Session
	 * 
	 * @return
	 */
	protected Map<String, Object> getSession() {
		return ActionContext.getContext().getSession();
	}
	
	/**
	 * 获取当前运行环境
	 * 
	 * @return
	 */
	protected Environment getEnvironment() {
		return Environment.getInstance();
	}

	/**
	 * 获取当前登录的用户
	 * 
	 * @return
	 */
	protected User getCurrentUser() {
		return getEnvironment().getCurrentUser(getSession());
	}
	
	/**
	 * 获取当前登录的用户Id
	 * 
	 * @return
	 */
	protected long getCurrentUserId() {
		return getCurrentUser().getId();
	}
	
	/**
	 * 获取当前登录的用户对应的员工，管理员用户对应的员工为null
	 * 
	 * @return
	 */
	protected HRUnit getCurrentEmployee() {
		return getEnvironment().getCurrentEmployee(getSession());
	}
	
	/**
	 * 获取当前登录的用户对应的员工Id，管理员用户没有对应的员工，返回值为0
	 * 
	 * @return
	 */
	protected long getCurrentEmployeeId() {
		return getCurrentUser().getEmployeeId();
	}
	
	/**
	 * 获取当前登录的用户对应的员工所在的公司
	 * 
	 * @return
	 */
	protected HRUnit getCurrentCompany() {
		return getEnvironment().getCurrentCompany(getSession());
	}
	
	/**
	 * 获取当前登录的用户对应的员工所在的公司Id
	 * 
	 * @return
	 */
	protected long getCurrentCompanyId() {
		return getCurrentUser().getCompanyId();
	}
	
	/**
	 * 记录日志的快捷方法
	 * 
	 * @param message
	 */
	protected void log(Object message) {
		LoggerUtil.info(message);
	}
	
	/**
	 * 记录异常日志的快捷方法
	 * 
	 * @param e
	 */
	protected void log(Throwable e) {
		LoggerUtil.error(e);
	}
}
