package com.jeans.tinyitsm.service.it.enums;

public enum IssueType implements EnumTitle {
	Bug,Requirement,Problem,Other;

	@Override
	public String getTitle() {
		String title = null;
		switch(this) {
		case Bug:
			title = "错误";
			break;
		case Requirement:
			title = "需求";
			break;
		case Problem:
			title = "问题";
			break;
		case Other:
			title = "其他";
		}
		return title;
	}

}
