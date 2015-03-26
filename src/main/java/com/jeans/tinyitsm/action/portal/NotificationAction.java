package com.jeans.tinyitsm.action.portal;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.jeans.tinyitsm.action.BaseAction;
import com.jeans.tinyitsm.model.portal.Notification;
import com.jeans.tinyitsm.service.portal.MessageConstants;
import com.jeans.tinyitsm.service.portal.NotificationService;
import com.jeans.tinyitsm.service.portal.SubscriptionService;
import com.jeans.tinyitsm.service.portal.TaskService;

public class NotificationAction extends BaseAction<List<Notification>> {

	private NotificationService notiService;
	private SubscriptionService subsService;
	private TaskService taskService;

	@Autowired
	public void setNotiService(NotificationService notiService) {
		this.notiService = notiService;
	}

	@Autowired
	public void setSubsService(SubscriptionService subsService) {
		this.subsService = subsService;
	}

	@Autowired
	public void setTaskService(TaskService taskService) {
		this.taskService = taskService;
	}

	@Action(value = "refr-noti", results = { @Result(type = "json", params = { "root", "data" }) })
	public String refreshNotification() throws Exception {
		data = notiService.loadNew(id, rows, getCurrentCompanyId());
		return SUCCESS;
	}

	@Action(value = "more-noti", results = { @Result(type = "json", params = { "root", "data" }) })
	public String moreNotification() throws Exception {
		data = notiService.loadMore(id, rows, getCurrentCompanyId());
		return SUCCESS;
	}

	private String tip;

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	@Action(value = "title-tip", results = { @Result(type = "json", params = { "root", "tip" }) })
	public String getTitleTips() throws Exception {
		long newRss = subsService.checkNews(getCurrentUserId());
		long newTasks = taskService.checkNews(getCurrentUserId());
		tip = "系统公告 [你有：" + newTasks + "项待办任务，" + newRss + "条新订阅动态]";
		return SUCCESS;
	}

	private int result;
	private String text;

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Action(value = "pub-admin-noti", results = { @Result(type = "json", params = { "root", "result" }) })
	public String adminNoti() throws Exception {
		result = notiService.publish(MessageConstants.ADMIN, text, splitIds()).size();
		return SUCCESS;
	}
}
