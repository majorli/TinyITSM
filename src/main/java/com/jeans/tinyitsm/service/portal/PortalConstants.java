package com.jeans.tinyitsm.service.portal;

public class PortalConstants {
	/**
	 * 用户登录的返回码
	 */
	public static final int LOGIN_SUCCESS = 0;
	public static final int LOGIN_USER_INACTIVE = 1;
	public static final int LOGIN_FAILED = 2;
	public static final int LOGIN_USER_UNAVAILABLE = 3;
	
	/**
	 * 全文检索结果类型
	 */
	public static final int FTS_ASSET_HARDWARE = 201;
	public static final int FTS_ASSET_SOFTWARE = 202;
	public static final int FTS_PROJECT_PROJECT = 301;
	public static final int FTS_PROJECT_ISSUES = 302;
	public static final int FTS_PROJECT_WIKI = 303;
	public static final int FTS_CLOUD_FILE = 501;
	public static final int FTS_CLOUD_LIST = 502;
}
