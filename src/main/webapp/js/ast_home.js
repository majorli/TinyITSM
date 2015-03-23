$(function() {
	/**
	 * initialize page parameters and functions
	 */
	$("#breadcrumbs").html("<a href=\"javascript:window.location.replace('func?code=HOME_PAGE')\">首页</a>&nbsp;&#187;&nbsp;资产管理");
	var grid = {
		"init" : function() {
			$("div#validation").delegate("a._Chown", "click", grid._chown);
			$("div#validation").delegate("a._SetIdle", "click", grid._setIdle);
			$("div#validation").delegate("a._Chcmp", "click", grid._chcmp);
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
			$("#aProps").dialog({
				"buttons" : [ {
					"text" : "确定",
					"iconCls" : "icon-saved",
					"handler" : grid.sendProps
				}, {
					"text" : "取消",
					"iconCls" : "icon-multiple",
					"handler" : function() {
						$("#aProps").dialog("close");
					}
				} ]
			});
			$("#aState").dialog({
				"buttons" : [ {
					"text" : "确定",
					"iconCls" : "icon-saved",
					"handler" : function() {
						var tab = $("#tabs").tabs("getTabIndex", $("#tabs").tabs("getSelected"));
						if (tab == 2) {
							grid.changeOwner();
						} else {
							grid.changeState();
						}
					}
				}, {
					"text" : "取消",
					"iconCls" : "icon-multiple",
					"handler" : function() {
						$("#aState").dialog("close");
					}
				} ]
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
		},
		"openProps" : function() {
			var data = {
				"ids" : "",
				"type" : 0
			};
			var tab = $("#tabs").tabs("getTabIndex", $("#tabs").tabs("getSelected"));
			var sel = [];
			if (tab == 0) {
				data.type = 1;
				sel = $("#hardware").datagrid("getSelections");
				$("#aProps div.SW").hide();
				$("#aProps div.HW").show();
				if (sel.length > 1) {
					$("#p_code,#p_financialCode").hide();
				} else {
					$("#p_code,#p_financialCode").show();
				}
			} else if (tab == 1) {
				data.type = 3;
				sel = $("#software").datagrid("getSelections");
				$("#aProps div.SW").show();
				$("#aProps div.HW").hide();
			} else {
				return;
			}
			if (sel.length == 0) {
				return;
			}
			var id = [];
			$.each(sel, function(i, n) {
				id.push(n.id);
			});
			data.ids = id.join();
			$("#v_ids").val(data.ids);
			$("#v_type").val(data.type);
			$("#aProps").dialog({
				"title" : "资产属性[已选" + id.length + "项资产]"
			});
			$.ajax({
				"url" : "asset/load-props",
				"data" : data,
				"success" : function(props) {
					for ( var key in props) {
						if (props[key] == null) {
							if (key == "purchaseTime" || key == "expiredTime") {
								$("#v_" + key).datebox("clear");
							} else if (key == "warranty" || key == "importance" || key == "softwareType") {
								$("#v_" + key).combobox("clear");
							} else if (key == "cost" || key == "quantity") {
								$("#v_" + key).numberspinner("clear");
							} else {
								$("#v_" + key).textbox("clear");
							}
						} else {
							if (key == "purchaseTime" || key == "expiredTime") {
								$("#v_" + key).datebox("setValue", props[key]);
							} else if (key == "warranty" || key == "importance" || key == "softwareType") {
								$("#v_" + key).combobox("setValue", props[key]);
							} else if (key == "cost" || key == "quantity") {
								$("#v_" + key).numberspinner("setValue", props[key]);
							} else {
								$("#v_" + key).textbox("setValue", props[key]);
							}
						}
					}
					$("#aProps").dialog("open");
					$("#aProps").scrollTop(0);
				}
			});
		},
		"sendProps" : function() {
			var data = {
				"ids" : $("#v_ids").val(),
				"type" : +$("#v_type").val()
			};
			data["props.name"] = grid._getValue("name");
			data["props.vendor"] = grid._getValue("vendor");
			data["props.modelOrVersion"] = grid._getValue("modelOrVersion");
			data["props.assetUsage"] = grid._getValue("assetUsage");
			data["props.purchaseTime"] = grid._getValue("purchaseTime");
			data["props.quantity"] = grid._getValue("quantity");
			data["props.cost"] = grid._getValue("cost");
			data["props.comment"] = grid._getValue("comment");
			if (data.type == 1) {
				data["props.code"] = grid._getValue("code");
				data["props.financialCode"] = grid._getValue("financialCode");
				data["props.sn"] = grid._getValue("sn");
				data["props.configuration"] = grid._getValue("configuration");
				data["props.warranty"] = grid._getValue("warranty");
				data["props.location"] = grid._getValue("location");
				data["props.ip"] = grid._getValue("ip");
				data["props.importance"] = grid._getValue("importance");
			} else {
				data["props.softwareType"] = grid._getValue("softwareType");
				data["props.license"] = grid._getValue("license");
				data["props.expiredTime"] = grid._getValue("expiredTime");
			}
			$.waitbox("正在保存");
			$.ajax({
				"url" : "asset/save-props",
				"data" : data,
				"success" : function(result) {
					$.waitbox();
					$.msgbox("消息", result + "项资产属性修改成功。", "info");
					if (data.type == 1) {
						$("#hardware").datagrid("reload");
					} else {
						$("#software").datagrid("reload");
					}
					$("#aProps").dialog("close");
				}
			});
		},
		"_getValue" : function(key) {
			var ele = "#v_" + key;
			if (key == "purchaseTime" || key == "expiredTime") {
				var dt = $(ele).datebox("getValue");
				if (isNaN(Date.parse(dt))) {
					return null;
				} else {
					return dt;
				}
			} else if (key == "warranty" || key == "importance" || key == "softwareType") {
				if ($(ele).combobox("getValue") == null || $(ele).combobox("getText") == "") {
					return -99;
				} else {
					return +$(ele).combobox("getValue");
				}
			} else if (key == "cost" || key == "quantity") {
				if ($(ele).numberspinner("getValue")) {
					return +$(ele).numberspinner("getValue")
				} else {
					return -1;
				}
			} else {
				return $(ele).textbox("getValue");
			}
		},
		"exp" : function(type) {
			// 导出资产为excel
			$("#exportType").val(type);
			$("#exportForm").form("submit");
		},
		"validate" : function() {
			$("#tabs").tabs("select", 2);
			var v = $("<ol id='vali'></ol>");
			$.waitbox("正在校验");
			$.ajax({
				"url" : "asset/asset-vali",
				"success" : function(validation) {
					var count = 0;
					for ( var id in validation) {
						var l = $(validation[id]);
						l.prop("id", id);
						v.append(l);
						count++;
					}
					$("#validation").html("").append("<h4>资产数据校验：共发现" + count + "项资产数据可能存在错误：</h4>").append(v);
					$.waitbox();
				}
			});
		},
		"_chown" : function() {
			// 设置责任人
			$("#p_state,#p_keep").hide();
			$("#p_dept,#p_empl").show();
			$("#v_ids").val($(this).parent().prop("id"));
			$("#v_dept").combotree("clear");
			$("#v_empl").combobox("clear");
			$("#aState").dialog("open");
		},
		"changeOwner" : function() {
			// 调整责任人
			var data = {
				"id" : +$("#v_ids").val(),
				"owner" : +$("#v_empl").combobox("getValue"),
				"adjustType" : 0
			};
			if (data.owner == 0) {
				$.msgbox("错误", "必须选择一个责任人。", "warning");
			} else {
				$.waitbox("正在调整");
				$.ajax({
					"url" : "asset/adjust",
					"data" : data,
					"success" : function(result) {
						$.waitbox();
						if (result == 0) {
							$.msgbox("错误", "调整责任人失败。", "warning");
						} else {
							$.msgbox("消息", "调整责任人完成。", "info");
						}
						$("#aState").dialog("close");
					}
				});
			}
		},
		"_setIdle" : function() {
			// 回收设备（状态设置为IDLE，责任人id设置为0）
			var id = +$(this).parent().prop("id");
			$.messager.confirm("确认", "确定要将该资产回收为闲置/备用状态吗？", function(r) {
				if (r) {
					grid._adjust(id, 1, "资产回收");
				}
			});
		},
		"_chcmp" : function() {
			// 调整设备所属公司为责任人所在公司
			var id = +$(this).parent().prop("id");
			$.messager.confirm("确认", "确定要将该资产所属公司调整为其责任人所在公司吗？", function(r) {
				if (r) {
					grid._adjust(id, 2, "调整所属公司");
				}
			});
		},
		"_adjust" : function(id, type, msg) {
			var data = {
					"id" : id,
					"adjustType" : type
				};
				$.waitbox("正在调整");
				$.ajax({
					"url" : "asset/adjust",
					"data" : data,
					"success" : function(result) {
						$.waitbox();
						if (result == 0) {
							$.msgbox("错误", msg + "失败。", "warning");
						} else {
							$.msgbox("消息", msg + "完成。", "info");
						}
					}
				});
		},
		"beginChangeState" : function() {
			var data = {
				"ids" : "",
				"type" : 0
			};
			var tab = $("#tabs").tabs("getTabIndex", $("#tabs").tabs("getSelected"));
			var sel = [];
			if (tab == 0) {
				data.type = 1;
				sel = $("#hardware").datagrid("getSelections");
				$("#p_state,#p_dept,#p_empl,#p_keep").show();
			} else if (tab == 1) {
				data.type = 3;
				sel = $("#software").datagrid("getSelections");
				$("#p_state").show();
				$("#p_dept,#p_empl,#p_keep").hide();
			} else {
				return;
			}
			if (sel.length == 0) {
				return;
			}
			var id = [];
			$.each(sel, function(i, n) {
				id.push(n.id);
			});
			data.ids = id.join();
			$("#v_ids").val(data.ids);
			$("#v_type").val(data.type);
			$.ajax({
				"url" : "asset/check-state",
				"data" : data,
				"success" : function(nextStates) {
					if (nextStates.length == 0) {
						$.msgbox("错误", "所选的资产没有统一的状态变更方式或已进入最终状态（硬件已报损/软件已淘汰）", "warning");
					} else {
						// 生成状态转移对话框
						var st = [];
						var stNames = [ "报损", "在用", "淘汰", "维修", "闲置/备用" ];
						$.each(nextStates, function(i, n) {
							var t = {};
							t.id = +n;
							t.text = stNames[t.id + 1];
							st.push(t);
						});
						$("#v_state").combobox({
							"data" : st,
							"value" : +nextStates[0]
						});
						$("#v_dept").combotree("clear");
						$("#v_empl").combobox("clear");
						$("#v_keep").prop("checked", false);
						// 打开对话框
						$("#aState").dialog("open");
					}
				}
			});
		},
		"changeState" : function() {
			var data = {
				"ids" : $("#v_ids").val(),
				"type" : +$("#v_type").val(),
				"state" : +$("#v_state").combobox("getValue"),
				"owner" : +$("#v_empl").combobox("getValue"),
				"keep" : $("#v_keep").prop("checked")
			};
			if (data.type == 1 && data.state == 0 && data.owner == 0) {
				// 如果硬件设备状态值是在用，则必须有责任人
				$.msgbox("错误", "硬件类资产状态为在用时必须选择一个责任人。", "warning");
			} else {
				$.waitbox("正在调整");
				$.ajax({
					"url" : "asset/change-state",
					"data" : data,
					"success" : function(result) {
						$.waitbox();
						$.msgbox("消息", result + "项资产状态调整成功。", "info");
						if (data.type == 1) {
							$("#hardware").datagrid("reload");
						} else {
							$("#software").datagrid("reload");
						}
						$("#aState").dialog("close");
					}
				});
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
	$("#appendAsset").on("click", function() {
		window.location.replace("func?code=AST_NEW");
	});
	$("#editProperties").on("click", grid.openProps);
	$($("#exportAssets").menubutton("options").menu).menu({
		"onClick" : function(item) {
			grid.exp(item.id);
		}
	});
	$("#validateAssets").on("click", grid.validate);
	$("#changeState").on("click", grid.beginChangeState);
	$("#v_dept").combotree({
		"onChange" : function(newValue, oldValue) {
			$("#v_empl").combobox({
				"url" : "hr/get-children?type=1&id=" + newValue,
			});
		}
	});
});