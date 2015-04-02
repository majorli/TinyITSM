package com.jeans.tinyitsm.service.it.enums;

import com.jeans.tinyitsm.service.EnumTitle;

public enum ProjectType implements EnumTitle {
	Infrastructure, Hardware, Software, Integration;

	@Override
	public String getTitle() {
		String title = null;
		switch (this) {
		case Infrastructure:
			title = "基础设施";
			break;
		case Hardware:
			title = "硬件安装";
			break;
		case Software:
			title = "软件开发";
			break;
		case Integration:
			title = "系统集成";
		}
		return title;
	}

}
