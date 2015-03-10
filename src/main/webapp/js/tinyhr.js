$(function() {
	/**
	 * initialize page parameters and functions
	 */
	$("#breadcrumbs").html("<a href=\"javascript:window.location.replace('func?code=HOME_PAGE')\">首页</a>&nbsp;&#187;&nbsp;TinyHR人力资源");
	var grid = {
		"grids" : [ "#dept", "#empl", "#hisDept", "#fmrEmpl" ],
		"editIndex" : [ undefined, undefined ],
		"endEditing" : function() {
			var tab = $("#nodeDetailTabs").tabs("getTabIndex", $("#nodeDetailTabs").tabs("getSelected"));
			if (tab > 1)
				return;
			if (grid.editIndex[tab] == undefined)
				return true;
			if ($(grid.grids[tab]).datagrid("validateRow", grid.editIndex[tab])) {
				$(grid.grids[tab]).datagrid("endEdit", grid.editIndex[tab]);
				grid.editIndex[tab] = undefined;
				return true;
			} else {
				return false;
			}
		},
		"clickRow" : function(index) {
			if ($("#extraHR").val() == "true")
				return;
			var tab = $("#nodeDetailTabs").tabs("getTabIndex", $("#nodeDetailTabs").tabs("getSelected"));
			if (tab > 1)
				return;
			if (grid.editIndex[tab] != index) {
				if (grid.endEditing()) {
					$(grid.grids[tab]).datagrid("selectRow", index).datagrid("beginEdit", index);
					grid.editIndex[tab] = index;
				} else {
					$(grid.grids[tab]).datagrid("selectRow", grid.editIndex[tab]);
				}
			}
		},
		"init" : function() {
			if ($("#extraHR").val() == "true") {
				$("#nodeDetailSave,#nodeDetailCancel").linkbutton("disable");
				$("#nodeDetailEdit").splitbutton("disable");
				$("#nodeDetailCancel").after("<span style='color:red;padding-left:1em;'>使用外部HR系统，此处不再提供编辑功能</span>");
			}
			$("#dept,#hisDept").datagrid({
				"singleSelect" : true,
				"idField" : "id",
				"fit" : true,
				"columns" : [ [ {
					"field" : "id",
					"title" : "UUID",
					"width" : 50,
					"align" : "center"
				}, {
					"field" : "name",
					"title" : "部门名称",
					"width" : 200,
					"editor" : {
						"type" : "textbox",
						"options" : {
							"required" : true,
							"missingMessage" : "部门名称不能为空"
						}
					}
				}, {
					"field" : "alias",
					"title" : "简称",
					"width" : 160,
					"editor" : "textbox"
				}, {
					"field" : "listOrder",
					"title" : "排列序号",
					"width" : 80,
					"editor" : {
						"type" : "numberbox",
						"options" : {
							"min" : 1,
							"max" : 127
						}
					}
				} ] ],
				"onDblClickRow" : grid.clickRow,
				"rownumbers" : true
			});
			$("#empl,#fmrEmpl").datagrid({
				"singleSelect" : true,
				"idField" : "id",
				"fit" : true,
				"columns" : [ [ {
					"field" : "id",
					"title" : "UUID",
					"width" : 50,
					"align" : "center"
				}, {
					"field" : "name",
					"title" : "员工姓名",
					"width" : 160,
					"editor" : {
						"type" : "textbox",
						"options" : {
							"required" : true,
							"missingMessage" : "员工姓名不能为空"
						}
					}
				}, {
					"field" : "alias",
					"title" : "所在部门",
					"width" : 160
				}, {
					"field" : "listOrder",
					"title" : "排列序号",
					"width" : 80,
					"editor" : {
						"type" : "numberbox",
						"options" : {
							"min" : 1,
							"max" : 127
						}
					}
				} ] ],
				"onDblClickRow" : grid.clickRow,
				"rownumbers" : true
			});
		},
		"load" : function(id, type) {
			grid.editIndex[type] = undefined;
			$(grid.grids[type]).datagrid("unselectAll").datagrid({
				"url" : "hr/get-children",
				"queryParams" : {
					"id" : id,
					"type" : type
				}
			});
		},
		"butFunc" : {
			"nodeDetailEdit" : function() {
				if ($("#extraHR").val() == "true")
					return;
				var tabIndex = $("#nodeDetailTabs").tabs("getTabIndex", $("#nodeDetailTabs").tabs("getSelected"));
				if (tabIndex > 1)
					return;
				$.each($(grid.grids[tabIndex]).datagrid("getRows"), function(index) {
					$(grid.grids[tabIndex]).datagrid("beginEdit", index);
				});
			},
			"nodeDetailSave" : function() {
				if ($("#extraHR").val() == "true")
					return;
				var tab = $("#nodeDetailTabs").tabs("getTabIndex", $("#nodeDetailTabs").tabs("getSelected"));
				if (tab > 1)
					return;
				var gd = $(grid.grids[tab]);
				var rs = gd.datagrid("getRows");
				for (var i = 0, il = rs.length; i < il; i++) {
					if (!gd.datagrid("validateRow", i)) {
						$.messager.alert("错误", "校验未通过，请输入" + (tab == 0 ? "部门名称" : "员工姓名"), "warning").window({
							"top" : "280px"
						});
						return;
					}
				}
				for (var i = 0, il = rs.length; i < il; i++) {
					gd.datagrid("endEdit", i);
				}
				grid.editIndex[tab] = undefined;
				var chgs = gd.datagrid("getChanges");
				if (chgs.length > 0) {
					var u = (tab == 0) ? "hr/update-depts" : "hr/update-empls";
					for (var i = 0, il = chgs.length; i < il; i++) {
						if (chgs[i].listOrder == "")
							chgs[i].listOrder = 127;
						if (tab == 0) {
							if (chgs[i].alias == "")
								chgs[i].alias = chgs[i].name;
						}
					}
					$.ajax({
						"url" : u,
						"contentType" : "application/json; charset=UTF-8",
						"data" : JSON.stringify({
							"updates" : chgs
						}),
						"success" : function(count) {
							if (count > 0) {
								gd.datagrid("acceptChanges");
								// refresh tree, only when editing departments
								if (tab == 0) {
									for (var i = 0, il = chgs.length; i < il; i++) {
										var node = $("#tree").tree("find", chgs[i].id);
										$("#tree").tree("update", {
											"target" : node.target,
											"alias" : chgs[i].alias,
											"name" : chgs[i].name,
											"listOrder" : chgs[i].listOrder
										});
									}
								}
								$.messager.alert("消息", "保存成功，共" + count + "条记录已被修改。", "info").window({
									"top" : "280px"
								});
							} else {
								gd.datagrid("rejectChanges");
								$.messager.alert("错误", "后台保存失败，请联系系统管理员。", "warning").window({
									"top" : "280px"
								});
							}
						}
					});
				}
			},
			"nodeDetailCancel" : function() {
				if ($("#extraHR").val() == "true")
					return;
				var tabIndex = $("#nodeDetailTabs").tabs("getTabIndex", $("#nodeDetailTabs").tabs("getSelected"));
				if (tabIndex > 1)
					return;
				$(grid.grids[tabIndex]).datagrid("rejectChanges");
				grid.editIndex[tabIndex] = undefined;
			},
			"newDept" : function() {
				var tabIndex = $("#nodeDetailTabs").tabs("getTabIndex", $("#nodeDetailTabs").tabs("getSelected"));
				if (tabIndex != 0)
					return;
				var org = $("#tree").tree("getSelected");
				if (org == null)
					return;
				$.ajax({
					"url" : "hr/add-dept",
					"data" : {
						"id" : org.id
					},
					"success" : function(data) {
						if (data.length > 0) {
							$("#dept").datagrid("insertRow", {
								"row" : data[0]
							});
							$("#dept").datagrid("scrollTo", $("#dept").datagrid("getRows").length - 1);
							var par = $("#tree").tree("getSelected");
							var sib = $("#tree").tree("getChildren", par.target);
							if (sib.length == 0) {
								$("#tree").tree("append", {
									"parent" : par.target,
									"data" : [ data[0] ]
								});
							} else {
								var i = sib.length - 1;
								while (i >= 0 && (sib[i].iconCls == "icon-company" || $("#tree").tree("getParent", sib[i].target) != par))
									i--;
								if (i >= 0) {
									$("#tree").tree("insert", {
										"after" : sib[i].target,
										"data" : [ data[0] ]
									});
								} else {
									$("#tree").tree("insert", {
										"before" : sib[0].target,
										"data" : [ data[0] ]
									});
								}
							}
						} else {
							$.messager.alert("错误", "新建部门失败，可能的原因为上级部门不存在或已经被停用。", "warning").window({
								"top" : "280px"
							});
						}
					}
				});
			},
			"abandonDept" : function() {
				var tabIndex = $("#nodeDetailTabs").tabs("getTabIndex", $("#nodeDetailTabs").tabs("getSelected"));
				if (tabIndex != 0)
					return;
				var org = $("#dept").datagrid("getSelected");
				if (org == null)
					return;
				$.messager.confirm("确认", "确定要将部门" + org.alias + "设为停用吗？下属部门将同时停用且操作不能撤销。", function(conf) {
					if (conf) {
						$.ajax({
							"url" : "hr/abandon-depts",
							"data" : {
								"id" : org.id
							},
							"success" : function(count) {
								switch (count) {
								case -1:
									$.messager.alert("错误", "部门停用失败，可能的原因有：<br/>1)" + org.name + "或其下级部门中仍有员工；<br/>2)部门不存在或已经被停用。", "warning").window({
										"top" : "280px"
									});
									break;
								case 0:
									$.messager.alert("错误", "发生未知原因的错误，停用部门失败。", "warning").window({
										"top" : "280px"
									});
									break;
								default:
									$.messager.alert("消息", org.name + "及其所有下属共" + count + "个部门已被停用。", "info").window({
										"top" : "280px"
									});
									$("#dept").datagrid("deleteRow", $("#dept").datagrid("getRowIndex", org));
									grid.editIndex[0] = undefined;
									$("#tree").tree("remove", $("#tree").tree("find", org.id).target);
								}
							}
						});
					}
				});
			},
			"newEmpl" : function() {
				var tab = $("#nodeDetailTabs").tabs("getTabIndex", $("#nodeDetailTabs").tabs("getSelected"));
				if (tab != 1)
					return;
				var org = $("#tree").tree("getSelected");
				if (org == null)
					return;
				$.ajax({
					"url" : "hr/add-empl",
					"data" : {
						"id" : org.id
					},
					"success" : function(data) {
						if (data.length > 0) {
							$("#empl").datagrid("insertRow", {
								"row" : data[0]
							});
							$("#empl").datagrid("scrollTo", $("#empl").datagrid("getRows").length - 1);
						} else {
							$.messager.alert("错误", "新建员工失败，可能的原因有：<br/>1)不能直接在公司下新增员工；<br/>2)指定部门不存在或已经停用。", "warning").window({
								"top" : "280px"
							});
						}
					}
				});
			},
			"moveEmpl" : function() {
				var tab = $("#nodeDetailTabs").tabs("getTabIndex", $("#nodeDetailTabs").tabs("getSelected"));
				if (tab != 1)
					return;
				if ($("#empl").datagrid("getSelected") == null)
					return;
				$("#move").dialog("open");
			},
			"leaveEmpl" : function() {
				var tab = $("#nodeDetailTabs").tabs("getTabIndex", $("#nodeDetailTabs").tabs("getSelected"));
				if (tab != 1)
					return;
				var emp = $("#empl").datagrid("getSelected");
				if (emp == null)
					return;
				$.messager.confirm("确认", "确定要将员工" + emp.name + "设为离职吗？本操作无法撤销。", function(conf) {
					if (conf) {
						$.ajax({
							"url" : "hr/leave-empl",
							"data" : {
								"id" : emp.id
							},
							"success" : function(succ) {
								if (succ) {
									$.messager.alert("消息", emp.name + "已被设为离职。", "info").window({
										"top" : "280px"
									});
									$("#empl").datagrid("deleteRow", $("#empl").datagrid("getRowIndex", emp));
									grid.editIndex[1] = undefined;
								} else {
									$.messager.alert("错误", "设置员工离职失败，可能的原因有：<br/>1)该员工不存在；<br/>2)该员工已经设为离职。", "warning").window({
										"top" : "280px"
									});
								}
							}
						});
					}
				});
			}
		}
	};
	$("#move").css("padding", "10px").dialog({
		"width" : "300px",
		"top" : "280px",
		"iconCls" : "icon-man",
		"title" : "员工调动",
		"modal" : true,
		"minimizable" : false,
		"maximizable" : false,
		"resizable" : false,
		"collapsible" : false,
		"closable" : false,
		"shadow" : true,
		"buttons" : [ {
			"text" : "确定",
			"iconCls" : "icon-saved",
			"handler" : function() {
				if ($("#targetDept").combotree("isValid")) {
					var emp = $("#empl").datagrid("getSelected");
					$.ajax({
						"url" : "hr/move-empl",
						"data" : {
							"id" : emp.id,
							"newDeptId" : $("#targetDept").combotree("getValue")
						},
						"success" : function(succ) {
							if (succ) {
								$("#empl").datagrid("deleteRow", $("#empl").datagrid("getRowIndex", emp));
								grid.editIndex[1] = undefined;
							} else {
								$.messager.alert("错误", "员工调动失败，可能的原因有：<br/>1)目标部门不存在或已停用；<br/>2)员工不存在或已经离职。", "warning").window({
									"top" : "280px"
								});
							}
							$("#move").dialog("close");
						}
					});
				}
			}
		}, {
			"text" : "取消",
			"iconCls" : "icon-multiple",
			"handler" : function() {
				$("#move").dialog("close");
			}
		} ],
		"closed" : true,
		"onOpen" : function() {
			var cmp = $.getCookie("usr").split("&")[0].split("=")[1];
			if (cmp && !isNaN(cmp)) {
				$("#targetComp").combotree("setValue", cmp);
			}
		}
	});
	$("#targetComp").combotree({
		"width" : "200px",
		"editable" : false,
		"formatter" : function(node) {
			node.text = node.alias;
			return node.text;
		},
		"url" : "hr/get-targets",
		"queryParams" : {
			"id" : 0
		},
		"onChange" : function(newValue, oldValue) {
			$("#targetDept").combotree({
				"url" : "hr/get-targets",
				"queryParams" : {
					"id" : newValue
				}
			});
		}
	});
	$("#targetDept").combotree({
		"width" : "200px",
		"editable" : false,
		"formatter" : function(node) {
			node.text = node.name;
			return node.text;
		},
		"required" : true,
		"missingMessage" : "目标部门为必填项"
	});
	/**
	 * initialize navi
	 */
	$.initNavi("tinyhr");
	/**
	 * initialize wpad
	 */
	$("#orgTreeReload").on("click", function() {
		$("#tree").tree("reload");
	});
	$("#mmOrgTreeView").menu({
		"onClick" : function(item) {
			switch (item.id) {
			case "showName":
				$("#tree").tree({
					"formatter" : function(node) {
						return node.name;
					}
				});
				break;
			case "showAlias":
				$("#tree").tree({
					"formatter" : function(node) {
						return node.alias;
					}
				});
				break;
			default:
				$("#tree").tree(item.id);
			}
		}
	});
	grid.init();
	$("#nodeDetailTools a, #mmNodeDetailEdit div.menu-item").on("click", function() {
		grid.butFunc[this.id]();
	});
	$("#tree").tree({
		"url" : "hr/get-tree",
		"formatter" : function(node) {
			return node.alias;
		},
		"onLoadSuccess" : function(node, data) {
			if (node == null)
				$(this).tree("select", $(this).tree("getRoot").target);
		},
		"onSelect" : function(node) {
			var type = $("#nodeDetailTabs").tabs("getTabIndex", $("#nodeDetailTabs").tabs("getSelected"));
			grid.load(node.id, type);
		}
	});
	$("#nodeDetailTabs").tabs({
		"onSelect" : function(tab, index) {
			var org = $("#tree").tree("getSelected");
			if (org == null)
				return;
			grid.load(org.id, index);
		}
	});
});