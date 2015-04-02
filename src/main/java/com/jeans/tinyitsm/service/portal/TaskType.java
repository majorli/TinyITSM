package com.jeans.tinyitsm.service.portal;

import com.jeans.tinyitsm.service.EnumTitle;

public enum TaskType implements EnumTitle {
	ProjectTask, MaintainTask, CustomTask;

	@Override
	public String getTitle() {
		String title = null;
		switch (this) {
		case ProjectTask:
			title = "项目建设任务";
			break;
		case MaintainTask:
			title = "系统运维任务";
			break;
		case CustomTask:
			title = "用户自定义任务";
		}
		return title;
	}

}
