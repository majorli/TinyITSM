package com.jeans.tinyitsm.action;

import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

public class ErrorHandlerAction extends TinyAction {

	private int c;
	private String p;

	private Map<String, Object> errorMsg;

	public int getC() {
		return c;
	}

	public void setC(int c) {
		this.c = c;
	}

	public String getP() {
		return p;
	}

	public void setP(String p) {
		this.p = p;
	}

	public Map<String, Object> getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(Map<String, Object> errorMsg) {
		this.errorMsg = errorMsg;
	}

	@Action(results = { @Result(name = "success", location = "error.jsp") })
	public String execute() throws Exception {
		errorMsg = new LinkedHashMap<String, Object>();
		if (null == p)
			p = "";
		else
			p = p.trim();

		Properties em = getEnvironment().getErrorMessages();

		String errorCode = "err" + c;
		if (!em.containsKey(errorCode))
			errorCode = "err0";
		String errorText = em.getProperty(errorCode);
		errorMsg.put(errorCode, errorText);

		int tCount = Integer.parseInt(em.getProperty(errorCode + ".tCount", "0"));
		int pCount = Integer.parseInt(em.getProperty(errorCode + ".pCount", "0"));

		String[] params = new String[pCount];
		String[] oParams = p.split(",");
		if (pCount != 0)
			System.arraycopy(oParams, 0, params, 0, oParams.length);
		int pIndex = 0;

		for (int i = 0; i < tCount; i++) {
			String tipCode = "tip" + c + "." + i;
			String tipText = em.getProperty(tipCode, "");
			int tipPCount = "".equals(tipText) ? 0 : Integer.parseInt(em.getProperty(tipCode + ".pCount", "0"));
			if (tipPCount != 0) {
				String[] ps = new String[tipPCount];
				System.arraycopy(params, pIndex, ps, 0, tipPCount);
				pIndex += tipPCount;
				tipText = MessageFormat.format(tipText, (Object[]) ps);
			}
			errorMsg.put(tipCode, tipText);
		}
		return SUCCESS;
	}
}
