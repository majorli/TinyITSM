package com.jeans.tinyitsm.service.it.enums;

public enum TeamType implements EnumTitle {
	ProjectTeam, MaintainTeam, OtherTeam;

	@Override
	public String getTitle() {
		String title = null;
		switch (this) {
		case ProjectTeam:
			title = "项目组";
			break;
		case MaintainTeam:
			title = "运维组";
			break;
		case OtherTeam:
			title = "其他类型小组";
		}
		return title;
	}

}
