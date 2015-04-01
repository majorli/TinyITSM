package com.jeans.tinyitsm.service.it.enums;

public enum SystemStage implements EnumTitle {
	Constructing, Operating, Abandoned;

	@Override
	public String getTitle() {
		String title = null;
		switch (this) {
		case Constructing:
			title = "建设阶段";
			break;
		case Operating:
			title = "正常使用";
			break;
		case Abandoned:
			title = "停止使用";
		}
		return title;
	}

}
