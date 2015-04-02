package com.jeans.tinyitsm.service.it.enums;

import com.jeans.tinyitsm.service.EnumTitle;

public enum PageType implements EnumTitle {
	Generic, Bug, Requirement, Issue, Discussion, Tutorial;

	@Override
	public String getTitle() {
		String title = null;
		switch (this) {
		case Generic:
			title = "普通";
			break;
		case Bug:
			title = "错误";
			break;
		case Requirement:
			title = "需求";
			break;
		case Issue:
			title = "问题";
			break;
		case Discussion:
			title = "讨论";
			break;
		case Tutorial:
			title = "教程";
		}
		return title;
	}

}
