package com.jeans.tinyitsm.service.portal;

public class MessageConstants {
	public static final String[] NOTIFICATION_SOURCE_NAMES = new String[] { "管理员公告", "信息化服务管理", "组织机构与人员管理", "外部HR系统接口", "信息化物资管理", "外部配置管理系统接口", "信息化项目管理",
			"信息化运维管理", "信息化知识库" };
	public static final byte ADMIN = 0;
	public static final byte TINY_SERVICE = 1;
	public static final byte TINY_HR = 2;
	public static final byte EXTRA_HR_CONNECTOR = 3;
	public static final byte TINY_ASSET = 4;
	public static final byte EXTRA_CI_CONNECTOR = 5;
	public static final byte TINY_PROJECT = 6;
	public static final byte TINY_MAINTAIN = 7;
	public static final byte TINY_CLOUD = 8;

	public static final byte RSS_SUBSCRIPTION = 0;
	public static final byte RSS_LIST_PUSH = 1;
	public static final byte RSS_FILE_PUSH = 2;
}
