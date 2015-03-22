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
		} ] ],
		"_getValue" : function(key) {
			var ele = "#v_" + key;
			if (key == "purchaseTime" || key == "expiredTime") {
				var dt = $(ele).datebox("getValue");
				if (isNaN(Date.parse(dt))) {
					return null;
				} else {
					return dt;
				}
			} else if (key == "warranty" || key == "importance") {
				if ($(ele).combobox("getValue") == null || $(ele).combobox("getText") == "") {
					return 0;	// 取默认值"在保"和"普通"
				} else {
					return +$(ele).combobox("getValue");
				}
			} else if (key == "softwareType") {
				if ($(ele).combobox("getValue") == null || $(ele).combobox("getText") == "") {
					return 6;	// 取默认值"其他类型软件"
				} else {
					return +$(ele).combobox("getValue");
				}
			} else if (key == "empl") {
				if ($(ele).combobox("getValue") == null || $(ele).combobox("getText") == "") {
					return 0;	// 无责任人
				} else {
					return +$(ele).combobox("getValue");
				}
			} else if (key == "cost" || key == "quantity" || key == "number") {
				if ($(ele).numberspinner("getValue")) {
					return +$(ele).numberspinner("getValue")
				} else {
					return -1;
				}
			} else {
				return $(ele).textbox("getValue");
			}
		},
		"_serializeForm" : function() {
			var data = {};
			data["props.type"] = page._getValue("type");
			data["props.catalog"] = page._getValue("catalog");
			data["props.name"] = page._getValue("name");
			data["props.vendor"] = page._getValue("vendor");
			data["props.modelOrVersion"] = page._getValue("modelOrVersion");
			data["props.assetUsage"] = page._getValue("assetUsage");
			data["props.purchaseTime"] = page._getValue("purchaseTime");
			data["props.quantity"] = page._getValue("quantity");
			data["props.cost"] = page._getValue("cost");
			data["props.state"] = page._getValue("state");
			data["props.comment"] = page._getValue("comment");
			data["props.number"] = page._getValue("number");
			if (data["props.type"] == 1) {
				data["props.code"] = page._getValue("code");
				data["props.financialCode"] = page._getValue("financialCode");
				data["props.sn"] = page._getValue("sn");
				data["props.configuration"] = page._getValue("configuration");
				data["props.warranty"] = page._getValue("warranty");
				data["props.location"] = page._getValue("location");
				data["props.ip"] = page._getValue("ip");
				data["props.importance"] = page._getValue("importance");
				data["props.ownerId"] = page._getValue("empl");
			} else {
				data["props.softwareType"] = page._getValue("softwareType");
				data["props.license"] = page._getValue("license");
				data["props.expiredTime"] = page._getValue("expiredTime");
			}
			return data;
		},
		"_clearForm" : function() {
			$("#v_catalog,#v_state,#v_warranty,#v_importance,#v_empl,#v_softwareType").combobox("clear");
			$("#v_dept").combotree("clear");
			$("form input.easyui-textbox").textbox("clear");
			$("form input.easyui-datebox").datebox("clear");
			$("form input.easyui-numberspinner").numberspinner("clear");
			$("#v_quantity,#v_number").numberspinner("setValue", 1);
			$("#v_cost").numberspinner("setValue", 0);
		},
		"_submit" : function() {
			if ($("#newAssets").form("validate")) {	// 校验：超长字段，必填字段（资产类别，目前使用情况）
				var data = page._serializeForm();
				if (data["props.type"] == 1 && data["props.state"] == 0 && data["props.ownerId"] == 0) {
					$.msgbox("错误", "使用情况为在用的硬件类资产必须选择一个责任人！", "warning");
				} else {
					$.waitbox("正在保存");
					$.ajax({
						"url" : "asset/new-assets",
						"data" : data,
						"success" : function(result) {
							$.waitbox();
							if (result <= 0) {
								$.msgbox("错误", "保存新增资产失败，请联系维护人员，稍后重试。", "warning");
							} else {
								$.msgbox("消息", result + "条新增资产记录保存完成。", "info");
								page._clearForm();
							}
						}
					});
				}
			}
		}
	};
	$("form input.easyui-combobox").combobox({
		"height" : 30
	});
	$("form input.easyui-textbox").textbox({
		"height" : 30
	});
	$("form input.easyui-datebox").datebox({
		"height" : 30
	});
	$("form input.easyui-numberspinner").numberspinner({
		"height" : 30
	});
	$("#v_dept").combotree({
		"height" : 30
	});
	$("#v_type").combobox({
		"onChange" : function(newValue, oldValue) {
			if ($(this).combobox("getValue") == 1) {
				$("#newAssets div.HW").show();
				$("#newAssets div.SW").hide();
				$("#v_catalog").combobox({
					"data" : page._catalog[0]
				});
			} else {
				$("#newAssets div.HW").hide();
				$("#newAssets div.SW").show();
				$("#v_catalog").combobox({
					"data" : page._catalog[1]
				});
			}
			page._clearForm();
		}
	}).combobox("setValue", 1); // 页面初始化为硬件类资产录入的初始状态
	$("#v_dept").combotree({
		"onChange" : function(newValue, oldValue) {
			$("#v_empl").combobox({
				"url" : "hr/get-children?type=1&id=" + newValue,
			});
		}
	});
	$("#f_reset").on("click", page._clearForm);
	$("#f_submit").on("click", page._submit);
});