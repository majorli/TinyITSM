package com.jeans.tinyitsm.service.cloud;

public class CloudConstants {

	public static final byte CLOUD_LIST = 0;
	public static final byte CLOUD_FILE = 1;
	
	public static final byte FILES_ROOT = -1;			// 资料
	public static final byte FAVORITES_ROOT = -2;		// 收藏
	public static final byte SUBSCRIPTIONS_ROOT = -3;	// 订阅
	public static final byte PUSHES_ROOT = -4;			// 推送
	public static final byte LIST = -11;				// 资料 - 栏目
	public static final byte FAVOR_LIST = -21;			// 收藏 - 收藏夹
	public static final byte RSS_LIST = -31;			// 订阅 - 栏目
	public static final byte PUSH_LIST = -41;			// 推送 - 栏目
	public static final byte FILE = -101;				// 资料 - 栏目 - 文件
	public static final byte FAVOR_LIST_LINK = -111;	// 收藏 - 收藏夹 - 链接文件
	public static final byte RSS_LIST_LINK = -112;		// 订阅 - 栏目 - 链接文件
	public static final byte PUSHES_ROOT_LINK = -113;	// 推送 - 链接文件
	public static final byte PUSH_LIST_LINK = -114;		// 推送 - 栏目 - 链接文件
	
	public static final String[] VERSION_TYPES = { "大纲", "草稿", "初稿", "审核稿", "审查稿", "批阅稿", "讨论稿", "定稿", "正式稿", "最终稿", "节选版", "精校版", "停用版", "无效版" };

	public static final int UNKNOWN_TYPE = 0;
	public static final int ALL = 1;
	public static final int DOCUMENTS = 10;
	public static final int WORD = 101;
	public static final int EXCEL = 102;
	public static final int PPT = 103;
	public static final int VISIO = 104;
	public static final int PDF = 105;
	public static final int MULTIMEDIA = 20;
	public static final int IMAGES = 201;
	public static final int MUSIC = 202;
	public static final int VIDEO = 203;
	public static final int OTHERS = 30;
}
