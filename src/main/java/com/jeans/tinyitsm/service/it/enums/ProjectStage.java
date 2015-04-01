package com.jeans.tinyitsm.service.it.enums;

public enum ProjectStage implements EnumTitle {
	Analyzing, OutlineDesigning, Purchasing, DetailDesigning, Developing, Implementing, Trying, Deploying, Constructed;

	@Override
	public String getTitle() {
		String title = null;
		switch (this) {
		case Analyzing:
			title = "系统分析";
			break;
		case OutlineDesigning:
			title = "概要设计";
			break;
		case Purchasing:
			title = "项目采购";
			break;
		case DetailDesigning:
			title = "详细设计";
			break;
		case Developing:
			title = "系统开发";
			break;
		case Implementing:
			title = "安装实施";
			break;
		case Trying:
			title = "项目试点";
			break;
		case Deploying:
			title = "项目推广";
			break;
		case Constructed:
			title = "验收竣工";
		}
		return title;
	}

}
