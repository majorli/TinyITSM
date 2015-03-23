package com.jeans.tinyitsm.service.asset;

public class AssetConstants {

	/**
	 * 资产类型，Asset.type
	 */
	public static final byte HARDWARE_ASSET = 1;
	public static final byte SOFTWARE_ASSET = 3;
	public static final byte GENERIC_ASSET = 7;

	/**
	 * 资产类别，Asset.catalog
	 */
	public static final byte NETWORK_EQUIPMENT = 11;
	public static final byte SECURITY_EQUIPMENT = 12;
	public static final byte SERVER_EQUIPMENT = 13;
	public static final byte STORAGE_EQUIPMENT = 14;
	public static final byte INFRASTRUCTURE_EQUIPMENT = 15;
	public static final byte TERMINATOR_EQUIPMENT = 16;
	public static final byte MOBILE_EQUIPMENT = 17;
	public static final byte PRINTER_EQUIPMENT = 18;
	public static final byte OTHER_EQUIPMENT = 29;
	public static final byte OPERATING_SYSTEM_SOFTWARE = 31;
	public static final byte DATABASE_SYSTEM_SOFTWARE = 32;
	public static final byte MIDDLEWARE_SOFTWARE = 33;
	public static final byte STORAGE_SYSTEM_SOFTWARE = 34;
	public static final byte SECURITY_SOFTWARE = 35;
	public static final byte OFFICE_SOFTWARE = 36;
	public static final byte APPLICATION_SOFTWARE = 37;
	public static final byte OTHER_SOFTWARE = 49;
	public static final byte GENERIC_IT_ASSET = 51;
	private static final String[] EQUIPMENT_NAMES = new String[] { "网络设备", "信息安全设备", "服务器", "存储备份设备", "基础设施", "桌面终端", "移动终端", "打印设备", "其他设备", "未知类别设备" };
	private static final String[] SOFTWARE_NAMES = new String[] { "操作系统", "数据库系统", "中间件", "存储备份软件", "信息安全软件", "办公软件", "应用软件", "其他软件", "未知类别软件" };

	/**
	 * 资产状态，Asset.state，其中软件类资产只适用IN_USE, IDLE, DISUSE三种状态
	 */
	public static final byte IN_USE = 0;		// 在用
	public static final byte DISUSE = 1;		// 淘汰
	public static final byte FIXING = 2;		// 维修
	public static final byte IDLE = 3;			// 备用
	public static final byte ELIMINATED = -1;	// 报损

	/**
	 * 硬件类资产保修状态，Hardware.warranty
	 */
	public static final byte IMPLIED_WARRANTY = 0;
	public static final byte RENEWAL_WARRANTY = 1;
	public static final byte EXPIRED_WARRANTY = -1;
	private static final String[] WARRANTY_NAMES = new String[] { "过保", "在保", "续保" };

	/**
	 * 软件类型，Software.softwareType
	 */
	public static final byte COMMERCIAL_SOFTWARE = 0;
	public static final byte OPEN_SOURCE_SOFTWARE = 1;
	public static final byte FREE_SOFTWARE = 2;
	public static final byte TRIAL_SOFTWARE = 3;
	public static final byte CUSTOM_DEVELOPED_SOFTWARE = 4;
	public static final byte SELF_DEVELOPED_SOFTWARE = 5;
	public static final byte OTHER_TYPE_SOFTWARE = 6;
	private static final String[] SOFTWARE_TYPE_NAMES = new String[] { "商品软件", "自由/开源软件", "免费软件", "试用软件", "定制开发软件", "自主研发软件", "其他类型软件" };

	/**
	 * 硬件类资产重要程度的中文名称，Hardware.importance
	 */
	public static final byte GENERAL_DEGREE = 0;
	public static final byte IMPORTANT_DEGREE = 1;
	public static final byte KEY_DEGREE = 2;
	private static final String[] IMPORTANCE_NAMES = new String[] { "普通", "重要", "关键" };

	/**
	 * 硬件类资产数据一致性检查错误码，负数为错误，正数为警告
	 */
	public static final byte INVALID_OWNER = -3;									// 责任人不存在
	public static final byte IN_USE_ASSET_OWNED_BY_FORMER_EMPLOYEE = -2;			// 在用资产的责任人已经离职
	public static final byte INVALID_OWNER_COMPANY = -1;							// 责任人和资产所属公司不一致
	public static final byte VALIDATE_OK = 0;										// 检验无误
	public static final byte IN_USE_ASSET_WITHOUT_OWNER = 1;						// 在用资产没有责任人（疑问）

	/**
	 * 获取资产类型对应的中文名称
	 * 
	 * @param type
	 * @return
	 */
	public static String getAssetTypeName(byte type) {
		if (type == HARDWARE_ASSET) {
			return "硬件设备";
		} else if (type == SOFTWARE_ASSET) {
			return "计算机软件";
		} else if (type == GENERIC_ASSET) {
			return "无类型资产";
		} else {
			return "未知类型";
		}
	}

	/**
	 * 获取资产类别对应的中文名称
	 * 
	 * @param catalog
	 * @return
	 */
	public static String getAssetCatalogName(byte catalog) {
		if (catalog >= NETWORK_EQUIPMENT && catalog <= PRINTER_EQUIPMENT) {
			return EQUIPMENT_NAMES[catalog - NETWORK_EQUIPMENT];
		} else if (catalog > PRINTER_EQUIPMENT && catalog < OTHER_EQUIPMENT) {
			return EQUIPMENT_NAMES[9];
		} else if (catalog == OTHER_EQUIPMENT) {
			return EQUIPMENT_NAMES[8];
		} else if (catalog >= OPERATING_SYSTEM_SOFTWARE && catalog <= APPLICATION_SOFTWARE) {
			return SOFTWARE_NAMES[catalog - OPERATING_SYSTEM_SOFTWARE];
		} else if (catalog > APPLICATION_SOFTWARE && catalog < OTHER_SOFTWARE) {
			return SOFTWARE_NAMES[8];
		} else if (catalog == OTHER_SOFTWARE) {
			return SOFTWARE_NAMES[7];
		} else if (catalog == GENERIC_IT_ASSET) {
			return "其他IT资产";
		} else {
			return "未知类别";
		}
	}

	/**
	 * 获取资产使用状态的中文名称
	 * 
	 * @param state
	 * @return
	 */
	public static String getAssetStateName(byte state) {
		String r = null;
		switch (state) {
		case IN_USE:
			r = "在用";
			break;
		case DISUSE:
			r = "淘汰";
			break;
		case FIXING:
			r = "维修";
			break;
		case IDLE:
			r = "闲置";
			break;
		case ELIMINATED:
			r = "报损";
			break;
		default:
			r = "未知";
		}
		return r;
	}

	/**
	 * 获取硬件设备保修状态的中文名称
	 * 
	 * @param warranty
	 * @return
	 */
	public static String getHardwareWarrantyName(byte warranty) {
		return (warranty < -1 || warranty > 1) ? "未知" : WARRANTY_NAMES[warranty + 1];
	}

	/**
	 * 获取硬件设备重要程度的中文名称
	 * 
	 * @param importance
	 * @return
	 */
	public static String getHardwareImportanceName(byte importance) {
		return (importance < 0 || importance > 2) ? "未知" : IMPORTANCE_NAMES[importance];
	}

	/**
	 * 获取软件类型的中文名称
	 * 
	 * @param softwareType
	 * @return
	 */
	public static String getSoftwareTypeName(byte softwareType) {
		return (softwareType < 0 || softwareType > 6) ? "未知类型" : SOFTWARE_TYPE_NAMES[softwareType];
	}
}
