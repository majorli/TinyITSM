package com.jeans.tinyitsm.service.it.enums;

import com.jeans.tinyitsm.service.EnumTitle;

public enum SystemType implements EnumTitle {
	Infrastructure, Network, Security, DataCenter, Cloud, HardwarePlatform, SoftwarePlatform, DataMining, MobileApplication, BusinessApplication, Manufacturing, Tools, Desktop, Others;

	private String[] titles = new String[] { "基础设施", "信息网络系统", "信息安全系统", "核心平台", "云系统", "硬件平台", "软件平台", "数据分析系统", "移动应用", "业务应用系统", "作业类系统", "信息化工具", "桌面办公系统",
			"其他信息系统" };

	@Override
	public String getTitle() {
		return titles[this.ordinal()];
	}

}
