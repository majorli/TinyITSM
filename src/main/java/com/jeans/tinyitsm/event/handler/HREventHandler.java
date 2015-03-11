package com.jeans.tinyitsm.event.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jeans.tinyitsm.event.HREvent;
import com.jeans.tinyitsm.event.HREventListener;
import com.jeans.tinyitsm.event.HREventType;
import com.jeans.tinyitsm.service.portal.NotificationService;
import com.jeans.tinyitsm.service.portal.UserService;
import com.jeans.tinyitsm.util.LoggerUtil;

@Component
public class HREventHandler implements HREventListener {

	private UserService service;
	private NotificationService notiService;

	@Autowired
	public void setService(UserService service) {
		this.service = service;
	}
	
	@Autowired
	public void setNotiService(NotificationService notiService) {
		this.notiService = notiService;
	}

	@Override
	public void changed(HREvent event) {
		StringBuilder sb = new StringBuilder();
		sb.append(event.getMessage());
		// 任务一：完善日志消息，更新用户账户
		if (event.getType() == HREventType.EmployeeLeft) {
			// 任务二：更新用户账号（1）
			sb.append("，对应的").append(service.setAvailable(event.getTargets().keySet(), false)).append("个用户账号已经同时失效");
		} else if (event.getType() == HREventType.EmployeeMoved) {
			// 任务二：更新用户账号（2）
			for (long id : event.getTargets().keySet())
				service.updateRoles(id, false, false);
			sb.append("，对应用户账户的特殊权限同时清空");
			if (event.getNewCompId() != event.getCompId())
				sb.append("，其中跨公司调动的").append(service.changeCompany(event.getTargets().keySet(), event.getNewCompId())).append("个用户账户已经转移至新公司");
		}
		// 任务二：生成日志记录
		LoggerUtil.info(sb.toString());
		// 任务三：生成系统公告
		notiService.publish(event);
	}

}
