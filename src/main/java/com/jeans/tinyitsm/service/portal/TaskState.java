package com.jeans.tinyitsm.service.portal;

import com.jeans.tinyitsm.service.EnumTitle;

public enum TaskState implements EnumTitle {
	Normal, Reminding, Overdue, Completed;

	@Override
	public String getTitle() {
		String title = null;
		switch (this) {
		case Normal:
			title = "正常";
			break;
		case Reminding:
			title = "提醒";
			break;
		case Overdue:
			title = "延期";
			break;
		case Completed:
			title = "完成";
		}
		return title;
	}

}
