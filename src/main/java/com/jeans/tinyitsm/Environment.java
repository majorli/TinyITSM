package com.jeans.tinyitsm;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import com.jeans.tinyitsm.model.portal.User;
import com.jeans.tinyitsm.model.view.HRUnit;
import com.jeans.tinyitsm.util.LoggerUtil;

public enum Environment {
	INSTANCE;
	
	public static Environment getInstance() {
		return INSTANCE;
	}
	
	private Properties em;
	private boolean extraHR, extraCI;
	private String cloudRoot;

	public void init() {
		em = new Properties();
		try (InputStream fi = getClass().getResourceAsStream("/errorMessages.xml")) {
			em.loadFromXML(fi);
		} catch (IOException e) {
			em = null;
			LoggerUtil.error(e);
			e.printStackTrace();
		}
		
		Properties conf = new Properties();
		try (InputStream fi = getClass().getResourceAsStream("/tinyitsmConf.xml")) {
			conf.loadFromXML(fi);
			extraHR = StringUtils.equalsIgnoreCase(conf.getProperty("extraHR", "false"), "true");
			extraCI = StringUtils.equalsIgnoreCase(conf.getProperty("extraCI", "false"), "true");
			cloudRoot = conf.getProperty("cloudRoot");
		} catch (IOException e) {
			conf = null;
			extraHR = false;
			extraCI = false;
			cloudRoot = "/CloudRoot";
			LoggerUtil.error(e);
			e.printStackTrace();
		}
	}
	
	public Properties getErrorMessages() {
		return em;
	}
	
	public boolean isExtraHR() {
		return extraHR;
	}

	public boolean isExtraCI() {
		return extraCI;
	}
	
	public String getCloudRoot() {
		return StringUtils.trimToEmpty(cloudRoot);
	}

	public void initSession(Map<String, Object> userInfo, Map<String, Object> session) {
		if (session.containsKey("userInfo"))
			session.remove("userInfo");
		session.put("userInfo", userInfo);
	}
	
	public void clearSession(Map<String, Object> session) {
		if (session.containsKey("userInfo"))
			session.remove("userInfo");
	}
	
	public boolean isLoggedIn(Map<String, Object> session) {
		return session.containsKey("userInfo");
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> getUserInfo(Map<String, Object> session) {
		return (Map<String, Object>) session.get("userInfo");
	}
	
	public User getCurrentUser(Map<String, Object> session) {
		return (User) getUserInfo(session).get("user");
	}
	
	public HRUnit getCurrentCompany(Map<String, Object> session) {
		return (HRUnit) getUserInfo(session).get("company");
	}
	
	public HRUnit getCurrentEmployee(Map<String, Object> session) {
		return (HRUnit) getUserInfo(session).get("employee");
	}
}
