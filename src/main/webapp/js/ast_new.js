$(function() {
	/**
	 * initialize page parameters and functions
	 */
	$("#breadcrumbs")
			.html(
					"<a href=\"javascript:window.location.replace('func?code=HOME_PAGE')\">首页</a>&nbsp;&#187;&nbsp;<a href=\"javascript:window.location.replace('func?code=AST_HOME')\">资产管理</a>&nbsp;&#187;&nbsp;新增资产");

	/**
	 * initialize navi
	 */
	$.initNavi("ast_new");

	/**
	 * initialize wpad
	 */
	var page = {
		"_catalog" : [ [ {
			"id" : 11,
			"text" : "网络设备"
		}, {
			"id" : 12,
			"text" : "信息安全设备"
		}, {
			"id" : 13,
			"text" : "服务器"
		}, {
			"id" : 14,
			"text" : "存储备份设备"
		}, {
			"id" : 15,
			"text" : "基础设施"
		}, {
			"id" : 16,
			"text" : "桌面终端"
		}, {
			"id" : 17,
			"text" : "移动终端"
		}, {
			"id" : 18,
			"text" : "打印设备"
		}, {
			"id" : 29,
			"text" : "其他设备"
		} ], [ {
			"id" : 31,
			"text" : "操作系统"
		}, {
			"id" : 32,
			"text" : "数据库系统"
		}, {
			"id" : 33,
			"text" : "中间件"
		}, {
			"id" : 34,
			"text" : "存储备份软件"
		}, {
			"id" : 35,
			"text" : "信息安全软件"
		}, {
			"id" : 36,
			"text" : "办公软件"
		}, {
			"id" : 37,
			"text" : "应用软件"
		}, {
			"id" : 49,
			"text" : "其他软件"
		} ] ]
	};
	$("form input.easyui-combobox").combobox({
		"width" : 300,
		"height" : 30
	});
	$("form input.easyui-textbox").textbox({
		"width" : 300,
		"height" : 30
	});
	$("#v_type").combobox({
		"onChange" : function() {
			if ($(this).combobox("getValue") == 1) {
				$("#v_catalog").combobox({
					"data" : page._catalog[0],
					"value" : 11
				});
			} else {
				$("#v_catalog").combobox({
					"data" : page._catalog[1],
					"value" : 31
				});
			}
		}
	}).combobox("setValue", 1);
});