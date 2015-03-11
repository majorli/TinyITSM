$(function() {
	/**
	 * initialize page parameters and functions
	 */
	$("#breadcrumbs").html("<a href=\"javascript:window.location.replace('func?code=HOME_PAGE')\">首页</a>&nbsp;&#187;&nbsp;资产管理");
	var grid = {
		"init" : function() {
			$("#hardware").datagrid({
				"fit" : true,
				"rownumbers" : true,
				"columns" : [ [ {
					"checkbox" : true,
					"field" : "ck",
					"styler" : function(value, row, index) {
						return "height:30px;";
					}
				}, {
					"field" : "code",
					"title" : "资产编号",
					"sortable" : true,
					"width" : 96
				}, {
					"field" : "financialCode",
					"title" : "财务资产编号",
					"sortable" : true,
					"width" : 96
				}, {
					"field" : "company",
					"title" : "所属公司",
					"width" : 72
				}, {
					"field" : "catalog",
					"title" : "资产类别",
					"width" : 84
				}, {
					"field" : "name",
					"title" : "名称",
					"sortable" : true,
					"width" : 96
				}, {
					"field" : "vendor",
					"title" : "制造商/品牌",
					"sortable" : true,
					"width" : 96
				}, {
					"field" : "modelOrVersion",
					"title" : "型号",
					"sortable" : true,
					"width" : 96
				}, {
					"field" : "assetUsage",
					"title" : "用途及基本功能",
					"width" : 180
				}, {
					"field" : "sn",
					"title" : "序列号",
					"sortable" : true,
					"width" : 108
				}, {
					"field" : "configuration",
					"title" : "主要配置",
					"width" : 144
				}, {
					"field" : "purchaseTime",
					"title" : "采购时间",
					"sortable" : true,
					"width" : 84
				}, {
					"field" : "quantity",
					"title" : "数量",
					"align" : "center",
					"sortable" : true,
					"width" : 60
				}, {
					"field" : "cost",
					"title" : "原值",
					"align" : "right",
					"sortable" : true,
					"formatter" : function(value, row, index) {
						if (isNaN(value)) {
							return "";
						} else {
							return value.toFixed(2);
						}
					},
					"width" : 96
				}, {
					"field" : "state",
					"title" : "使用情况",
					"sortable" : true,
					"width" : 72
				}, {
					"field" : "warranty",
					"title" : "保修状态",
					"sortable" : true,
					"width" : 72
				}, {
					"field" : "location",
					"title" : "物理位置",
					"sortable" : true,
					"width" : 120
				}, {
					"field" : "ip",
					"title" : "网络地址",
					"sortable" : true,
					"width" : 96
				}, {
					"field" : "importance",
					"title" : "重要程度",
					"sortable" : true,
					"width" : 72
				}, {
					"field" : "owner",
					"title" : "责任人",
					"sortable" : true,
					"width" : 60
				}, {
					"field" : "comment",
					"title" : "备注",
					"width" : 120
				} ] ],
				"pagination" : true,
				"pageSize" : 20
			});
			$("#software").datagrid({
				"fit" : true,
				"rownumbers" : true,
				"columns" : [ [ {
					"checkbox" : true,
					"field" : "ck",
					"styler" : function(value, row, index) {
						return "height:30px;";
					}
				}, {
					"field" : "company",
					"title" : "所属公司",
					"width" : 72
				}, {
					"field" : "catalog",
					"title" : "资产类别",
					"width" : 84
				}, {
					"field" : "name",
					"title" : "名称",
					"sortable" : true,
					"width" : 120
				}, {
					"field" : "vendor",
					"title" : "软件厂商",
					"sortable" : true,
					"width" : 72
				}, {
					"field" : "modelOrVersion",
					"title" : "软件版本",
					"sortable" : true,
					"width" : 120
				}, {
					"field" : "assetUsage",
					"title" : "用途及基本功能",
					"width" : 180
				}, {
					"field" : "purchaseTime",
					"title" : "采购时间",
					"sortable" : true,
					"width" : 84
				}, {
					"field" : "quantity",
					"title" : "数量",
					"align" : "center",
					"sortable" : true,
					"width" : 60
				}, {
					"field" : "cost",
					"title" : "原值",
					"align" : "right",
					"formatter" : function(value, row, index) {
						if (isNaN(value)) {
							return "";
						} else {
							return value.toFixed(2);
						}
					},
					"sortable" : true,
					"width" : 96
				}, {
					"field" : "state",
					"title" : "使用情况",
					"sortable" : true,
					"width" : 72
				}, {
					"field" : "softwareType",
					"title" : "软件类型",
					"sortable" : true,
					"width" : 72
				}, {
					"field" : "license",
					"title" : "许可证",
					"sortable" : true,
					"width" : 72
				}, {
					"field" : "expiredTime",
					"title" : "许可期限",
					"sortable" : true,
					"width" : 84
				}, {
					"field" : "comment",
					"title" : "备注",
					"width" : 120
				} ] ],
				"pagination" : true,
				"pageSize" : 20
			});
		},
		"loadHardware" : function() {
			var c = $("#hwCatalog").combobox("getValue");
			var t = $("#tabs").tabs("getTabIndex", $("#tabs").tabs("getSelected"));
			if (c > 0) {
				if (t != 0) {
					$("#tabs").tabs("select", 0);
				}
				var params = {
					"type" : c
				};
				if (params.type <= 0) {
					return;
				}
				if ($("#hardware").datagrid("options").url == null) {
					$("#hardware").datagrid({
						"url" : "asset/load-assets",
						"queryParams" : params
					});
				} else {
					$("#hardware").datagrid("load", params);
				}
			}
		},
		"loadSoftware" : function() {
			var c = $("#swCatalog").combobox("getValue");
			var t = $("#tabs").tabs("getTabIndex", $("#tabs").tabs("getSelected"));
			if (c > 0) {
				if (t != 1) {
					$("#tabs").tabs("select", 1);
				}
				var params = {
					"type" : c
				};
				if ($("#software").datagrid("options").url == null) {
					$("#software").datagrid({
						"url" : "asset/load-assets",
						"queryParams" : params
					});
				} else {
					$("#software").datagrid("load", params);
				}
			}
		}
	};

	/**
	 * initialize navi
	 */
	$.initNavi("ast_home");

	/**
	 * initialize wpad
	 */
	grid.init();
	$("#hwCatalog").combobox({
		"valueField" : "id",
		"textField" : "text",
		"data" : [ {
			"id" : 0,
			"text" : "==请选择类别=="
		}, {
			"id" : 1,
			"text" : "全部硬件类资产"
		}, {
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
			"text" : "其他硬件类资产"
		} ],
		"onSelect" : grid.loadHardware
	});
	$("#swCatalog").combobox({
		"valueField" : "id",
		"textField" : "text",
		"data" : [ {
			"id" : 0,
			"text" : "==请选择类别=="
		}, {
			"id" : 3,
			"text" : "全部软件类资产"
		}, {
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
			"text" : "其他软件类资产"
		} ],
		"onSelect" : grid.loadSoftware
	});
});