package com.jeans.tinyitsm.interceptor;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.StrutsStatics;

import com.jeans.tinyitsm.model.portal.User;
import com.jeans.tinyitsm.util.LoggerUtil;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class LoggingInterceptor extends AbstractInterceptor {

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		Action action = (Action) invocation.getAction();
		Class<? extends Action> clazz = action.getClass();
		String actionName = clazz.getSimpleName();

		ActionContext ac = invocation.getInvocationContext();
		Map<String, Object> actionContext = ac.getContextMap();
		HttpServletRequest request = (HttpServletRequest) actionContext.get(StrutsStatics.HTTP_REQUEST);
		String requestURI = request.getRequestURI();
		
		StringBuilder builder = new StringBuilder("来源:[");
		builder.append(request.getRemoteHost()).append("(").append(request.getRemoteAddr()).append(")] 请求:[").append(requestURI).append("] 主要参数:[ ");
		
		Map<String, Object> parameters = ac.getParameters();
		Set<String> keys = parameters.keySet();
		Iterator<String> it = keys.iterator();
		while (it.hasNext()) {
			String key = it.next();
			if (key.endsWith("Id") || "id".equals(key)) {
				builder.append(key).append("=").append(((String[]) parameters.get(key))[0]).append(" ");
			}
		}
		builder.append("] ");

		String result = invocation.invoke();

		Map<String, Object> session = ac.getSession();
		if (session.containsKey("userInfo")) {
			@SuppressWarnings("unchecked")
			User user = (User) ((Map<String, Object>) session.get("userInfo")).get("user");
			long id = user.getId();
			long comp = user.getCompanyId();
			
			builder.append("用户Id:").append(id).append(" 公司Id:").append(comp).append(" Action:").append(actionName).append(" 返回值:").append(result);
			LoggerUtil.info(builder.toString());
		}

		return result;
	}

}
