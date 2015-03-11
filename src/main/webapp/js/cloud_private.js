$(function() {
	/**
	 * initialize page parameters and functions
	 */
	$("#breadcrumbs").html("<a href=\"javascript:window.location.replace('func?code=HOME_PAGE')\">首页</a>&nbsp;&#187;&nbsp;我的资料库");
	var func = {
		"fc" : 0,
		"initUploadDialog" : function() {
			var node = $("#doctree").tree("getSelected");
			if (node.nodeType == -11) {
				$("#listName").html(node.text);
			} else {
				$("#listName").html($("#doctree").tree("getParent", node.target).text);
			}
			$("#filesList").html("");
		},
		"addFile" : function() {
			var dd = $("<dd></dd>");
			var divFilenameCol = $("<div class=\"fnc\"></div>");
			var divFileChangeCol = $("<div class=\"fcc\"></div>");
			var fileChange = $("<a>变更</a>");
			fileChange.linkbutton({
				"iconCls" : "icon-file-refresh"
			});
			divFileChangeCol.append(fileChange);
			var divFileRemoveCol = $("<div class=\"frc\"></div>");
			var fileRemove = $("<a>删除</a>");
			fileRemove.linkbutton({
				"iconCls" : "icon-minus"
			});
			divFileRemoveCol.append(fileRemove);
			var divFileInputCol = $("<div class=\"fic\"></div>");
			var fileInput = $("<input type=\"file\" name=\"upload\" />");
			divFileInputCol.append(fileInput);
			$("#filesList").append(dd.append(divFilenameCol).append(divFileChangeCol).append(divFileRemoveCol).append(divFileInputCol));
			dd.get(0).scrollIntoView(false);
			fileInput.trigger("click");
		},
		"init" : function() {
			$.ajax({
				"url" : "cloud/outline",
				"success" : function(data) {
					$("#doctree").tree("loadData", data);
					func.setToolbar();
					$("#properties").propertygrid("loadData", {
						"total" : 0,
						"rows" : []
					});
				}
			});
		},
		"setToolbar" : function(node) {
			$("#toolbar a.act").hide();
			if (node) {
				switch (node.nodeType) {
				case -1: // 资料: #createList
					$("#createList").linkbutton({
						"text" : "新建栏目"
					}).show();
					break;
				case -2: // 收藏: #createList
					$("#createList").linkbutton({
						"text" : "新建收藏夹"
					}).show();
					break;
				case -11: // 资料 - 栏目:
					// #createList,#upload,#download,#delete,#rename,#tag,#permission,#push
					$("#createList").linkbutton({
						"text" : "新建栏目"
					}).show();
					$("#delete").linkbutton({
						"text" : "删除栏目"
					}).show();
					$("#upload,#download,#rename,#tag,#permission,#push").show();
					break;
				case -21: // 收藏 - 收藏夹:
					// #createList,#download,#delete,#rename
					$("#createList").linkbutton({
						"text" : "新建收藏夹"
					}).show();
					$("#delete").linkbutton({
						"text" : "删除收藏夹"
					}).show();
					$("#download,#rename").show();
					break;
				case -31: // 订阅 - 栏目: #download,#delete
					$("#delete").linkbutton({
						"text" : "取消订阅"
					}).show();
					$("#download").show();
					break;
				case -41: // 推送 - 栏目: #download,#refuse,#rss
					$("#download,#refuse,#rss").show();
					break;
				case -101: // 资料 - 栏目 - 文件:
					// #createList,#upload,#download,#delete,#move,#rename,#brief,#tag,#permission,#push
					$("#createList").linkbutton({
						"text" : "新建栏目"
					}).show();
					$("#delete").linkbutton({
						"text" : "删除"
					}).show();
					$("#upload,#download,#move,#rename,#brief,#tag,#permission,#push").show();
					break;
				case -111: // 收藏 - 收藏夹 - 链接文件: 收藏夹里的文件，可以#download,#createList,#delete,#move
					$("#createList").linkbutton({
						"text" : "新建收藏夹"
					}).show();
					$("#delete").linkbutton({
						"text" : "取消收藏"
					}).show();
					$("#download,#move").show();
					break;
				case -113: // 推送 - 链接文件: 直接推送来的文件，除了下载可以拒绝和收藏
					$("#download,#refuse,#favor").show();
					break;
				case -114: // 推送 - 栏目 - 链接文件:
				case -112: // 订阅 - 栏目 - 链接文件: 推送来的栏目和订阅的栏目里的文件，只能收藏和下载
					$("#download,#favor").show();
					break;
				case -3: // 订阅: 无，除了刷新其他什么都不能操作
				case -4: // 推送: 无，除了刷新其他什么都不能操作
				default:
				}
			}
		},
		"loadChildren" : function(node) {
			if (node.loaded)
				return;
			$.ajax({
				"async" : false,
				"url" : "cloud/load-children",
				"data" : {
					"nodeId" : node.nodeId,
					"nodeType" : node.nodeType
				},
				"success" : function(data) {
					$("#doctree").tree("append", {
						"parent" : node.target,
						"data" : data
					});
					node.loaded = true;
				}
			});
		},
		"initOptDlg" : function(type) {
			var node = $("#doctree").tree("getSelected");
			switch (type) {
			case "createList":
				$("#opt_tags").textbox({
					"prompt" : "输入标签，多个标签用空格分隔"
				}).textbox("clear");
				$("#opt_perm").combobox("setValue", "private");
				$("#opt_user").tree("uncheck", $("#opt_user").tree("getRoot").target);
				$("#opt").dialog({
					"buttons" : [ {
						"text" : "确定",
						"iconCls" : "icon-saved",
						"handler" : func.createList
					}, {
						"text" : "取消",
						"iconCls" : "icon-multiple",
						"handler" : function() {
							$("#opt").dialog("close");
						}
					} ]
				});
				switch (node.nodeType) {
				case -1:
				case -11:
				case -101:
					$("#opt").dialog({
						"title" : "创建新栏目",
						"iconCls" : "icon-folder-documents"
					});
					$("#opt_ttl").html("创建新栏目");
					$("#opt_name").textbox({
						"prompt" : "输入新栏目名称",
						"editable" : true,
						"disabled" : false
					}).textbox("clear");
					$("#opt_tags_div,#opt_perm_div").show();
					$("#opt_user_div").panel({
						"title" : "有访问权限的用户",
						"height" : 198
					}).panel("open");
					break;
				case -2:
				case -21:
				case -111:
					$("#opt").dialog({
						"title" : "创建新收藏夹",
						"iconCls" : "icon-folder-favorites"
					});
					$("#opt_ttl").html("创建新收藏夹");
					$("#opt_name").textbox({
						"prompt" : "输入新收藏夹名称",
						"editable" : true,
						"disabled" : false
					}).textbox("clear");
					$("#opt_tags_div,#opt_perm_div").hide();
					$("#opt_user_div").panel("close");
					break;
				default:
				}
				break;
			case "push":
				$("#opt_tags_div,#opt_perm_div").hide();
				$("#opt_user").tree("uncheck", $("#opt_user").tree("getRoot").target);
				$("#opt_user_div").panel({
					"title" : "推送给以下用户",
					"height" : 258
				// 198 + 30 + 30
				}).panel("open");
				$("#opt_name").textbox({
					"value" : node.text,
					"editable" : false,
					"disabled" : true
				});
				$("#opt").dialog({
					"iconCls" : "icon-cart",
					"title" : "推送",
					"buttons" : [ {
						"text" : "确定",
						"iconCls" : "icon-saved",
						"handler" : func.push
					}, {
						"text" : "取消",
						"iconCls" : "icon-multiple",
						"handler" : function() {
							$("#opt").dialog("close");
						}
					} ]
				});
				switch (node.nodeType) {
				case -11:
					$("#opt_ttl").html("推送我的栏目");
					break;
				case -101:
					$("#opt_ttl").html("推送我的文件");
					break;
				default:
				}
				break;
			case "permission":
				$("#opt").dialog({
					"title" : "修改访问权限",
					"iconCls" : "icon-key",
					"buttons" : [ {
						"text" : "确定",
						"iconCls" : "icon-saved",
						"handler" : func.setPerm
					}, {
						"text" : "取消",
						"iconCls" : "icon-multiple",
						"handler" : function() {
							$("#opt").dialog("close");
						}
					} ]
				});
				$("#opt_ttl").html("修改访问权限");
				$("#opt_name").textbox({
					"value" : node.text,
					"editable" : false,
					"disabled" : true
				});
				$("#opt_tags_div").hide();
				$("#opt_perm").combobox("setValue", "private");
				$("#opt_perm_div").show();
				$("#opt_user").tree("uncheck", $("#opt_user").tree("getRoot").target);
				$("#opt_user_div").panel({
					"title" : "有访问权限的用户",
					"height" : 228
				}).panel("open");
				break;
			case "tags":
				$("#opt").dialog({
					"title" : "设置标签",
					"iconCls" : "icon-tickets",
					"buttons" : [ {
						"text" : "确定",
						"iconCls" : "icon-saved",
						"handler" : func.tags
					}, {
						"text" : "取消",
						"iconCls" : "icon-multiple",
						"handler" : function() {
							$("#opt").dialog("close");
						}
					} ]
				});
				$("#opt_ttl").html("设置标签");
				$("#opt_name").textbox({
					"value" : node.text,
					"editable" : false,
					"disabled" : true
				});
				$("#opt_tags_div").show();
				$("#opt_perm_div").hide();
				$("#opt_user_div").panel("close");
				break;
			case "rename":
				$("#opt").dialog({
					"title" : "重命名",
					"iconCls" : "icon-file-log",
					"buttons" : [ {
						"text" : "确定",
						"iconCls" : "icon-saved",
						"handler" : func.rename
					}, {
						"text" : "取消",
						"iconCls" : "icon-multiple",
						"handler" : function() {
							$("#opt").dialog("close");
						}
					} ]
				});
				$("#opt_ttl").html("重命名");
				$("#opt_name").textbox({
					"prompt" : "输入新名称",
					"value" : node.text,
					"editable" : true,
					"disabled" : false
				});
				$("#opt_tags_div, #opt_perm_div").hide();
				$("#opt_user_div").panel("close");
				break;
			default:
			}
		},
		"collectUserIds" : function() {
			var a = [];
			var c = $("#opt_user").tree("getChecked");
			$.each(c, function(i, n) {
				if (n.iconCls == "icon-employee") {
					a.push(n.id);
				}
			});
			if (a.length == 0) {
				return "";
			} else {
				return a.join(",");
			}
		},
		"createList" : function() {
			if (!$("#opt_name").textbox("isValid")) {
				$.msgbox("错误", "名称不能为空", "warning");
				return;
			}
			$.waitbox("正在创建");
			var d = {
				"nodeType" : $("#doctree").tree("getSelected").nodeType,
				"name" : $("#opt_name").textbox("getText")
			};
			if (d.nodeType == -1 || d.nodeType == -11 || d.nodeType == -101) {
				d.tags = $("#opt_tags").textbox("getText");
				var p = $("#opt_perm").combobox("getValue");
				d.priv = (p == "private");
				if (p == "permitted") {
					d.ids = func.collectUserIds();
				} else {
					d.ids = "";
				}
			}
			$.ajax({
				"url" : "cloud/create-list",
				"data" : d,
				"success" : function(data) {
					$.waitbox();
					var pa = null;
					if (d.nodeType == -1 || d.nodeType == -11 || d.nodeType == -101) {
						pa = $("#doctree").tree("find", -1);
					} else {
						pa = $("#doctree").tree("find", -2);
					}
					if (pa.loaded) {
						$("#doctree").tree("append", {
							"parent" : pa.target,
							"data" : data
						});
					}
					$("#opt").dialog("close");
					$("#doctree").tree("collapse", pa.target);
					$("#doctree").tree("expand", pa.target);
				}
			});
		},
		"push" : function() {
			$.waitbox("正在推送");
			var node = $("#doctree").tree("getSelected");
			$.ajax({
				"url" : "cloud/push",
				"data" : {
					"nodeType" : node.nodeType == -11 ? 0 : 1,
					"nodeId" : node.nodeId,
					"ids" : func.collectUserIds()
				},
				"success" : function(result) {
					$.waitbox();
					if (result >= 0) {
						$.msgbox("消息", "推送成功，" + result + "个用户收到本次推送", "info");
					} else {
						$.msgbox("错误", "推送失败", "warning");
					}
					$("#opt").dialog("close");
				}
			});
		},
		"upload" : function() {
			var node = $("#doctree").tree("getSelected");
			var pa = null;
			if (node.nodeType == -11) {
				pa = node;
			} else {
				pa = $("#doctree").tree("getParent", node.target);
			}
			$("#lid").val(pa.nodeId);
			$.waitbox("正在上传");
			$("#fileUpl").form("submit", {
				"success" : function(data) {
					$.waitbox();
					var d = JSON.parse(data);
					if (d.f.length == 0) {
						$.msgbox("消息", "文件上传成功", "info");
					} else {
						var em = [ d.f.length + "个文件上传失败：" ];
						$.each(d.f, function(i, n) {
							em.push(n);
						});
						$.msgbox("错误", em.join("<br/>"), "warning");
					}
					if (pa.loaded) {
						$("#doctree").tree("append", {
							"parent" : pa.target,
							"data" : d.s
						});
					}
					$("#upl").dialog("close");
					$("#doctree").tree("collapse", pa.target);
					$("#doctree").tree("expand", pa.target);
				}
			});
		},
		"setPerm" : function() {
			var node = $("#doctree").tree("getSelected");
			$.waitbox("正在修改权限");
			var d = {
				"nodeType" : node.nodeType,
				"id" : node.nodeId,
			};
			var p = $("#opt_perm").combobox("getValue");
			d.priv = (p == "private");
			if (p == "permitted") {
				d.ids = func.collectUserIds();
			} else {
				d.ids = "";
			}
			$.ajax({
				"url" : "cloud/set-perm",
				"data" : d,
				"success" : function(data) {
					$.waitbox();
					$("#opt").dialog("close");
					$("#properties").propertygrid({
						"url" : "cloud/properties",
						"queryParams" : {
							"nodeId" : node.nodeId,
							"nodeType" : node.nodeType
						}
					});
				}
			});
		},
		"rename" : function() {
			if (!$("#opt_name").textbox("isValid")) {
				$.msgbox("错误", "名称不能为空", "warning");
				return;
			}
			var node = $("#doctree").tree("getSelected");
			$.waitbox("正在重命名");
			$.ajax({
				"url" : "cloud/rename",
				"data" : {
					"id" : node.nodeId,
					"nodeType" : node.nodeType,
					"name" : $("#opt_name").textbox("getText")
				},
				"success" : function(data) {
					$.waitbox();
					if (data.length > 0) {
						var d = data[0];
						$("#doctree").tree("update", {
							"target" : node.target,
							"text" : d.text,
							"iconCls" : d.iconCls
						});
					} else {
						$.msgbox("错误", "重命名失败，目标不存在或名称不合法", "warning");
					}
					$("#opt").dialog("close");
				}
			});
		},
		"tags" : function() {
			var node = $("#doctree").tree("getSelected");
			$.waitbox("正在保存标签");
			$.ajax({
				"url" : "cloud/save-tags",
				"data" : {
					"id" : node.nodeId,
					"nodeType" : node.nodeType,
					"tags" : $("#opt_tags").textbox("getText")
				},
				"success" : function(result) {
					$.waitbox();
					$("#opt").dialog("close");
					$("#properties").propertygrid({
						"url" : "cloud/properties",
						"queryParams" : {
							"nodeId" : node.nodeId,
							"nodeType" : node.nodeType
						}
					});
				}
			});
		},
		"brief" : function() {
			$.waitbox("正在保存");
			var node = $("#doctree").tree("getSelected");
			var d = {
				"id" : node.nodeId,
				"majorVersion" : $("#ver_major").numberspinner("getValue"),
				"minorVersion" : $("#ver_minor").numberbox("getValue"),
				"versionType" : $("#ver_type").combobox("getText").trim(),
				"brief" : $("#ver_brf").textbox("getText")
			};
			if (d.majorVersion == "") {
				d.majorVersion = "0";
			}
			if (d.minorVersion == "") {
				d.minorVersion = "0.0"
			}
			$.ajax({
				"url" : "cloud/save-info",
				"data" : d,
				"success" : function(result) {
					$.waitbox();
					$("#ver").dialog("close");
					$("#properties").propertygrid({
						"url" : "cloud/properties",
						"queryParams" : {
							"nodeId" : node.nodeId,
							"nodeType" : node.nodeType
						}
					});
				}
			});
		}
	};

	/**
	 * initialize navi
	 */
	$.initNavi("cloud_private");

	/**
	 * initialize wpad
	 */
	func.init();
	$("#doctree").tree({
		"onClick" : function(node) {
			func.setToolbar(node);
			$("#properties").propertygrid({
				"url" : "cloud/properties",
				"queryParams" : {
					"nodeId" : node.nodeId,
					"nodeType" : node.nodeType
				}
			});
		},
		"onDblClick" : function(node) {
			if (node.nodeType < -100) {
				$.ajax({
					"url" : "cloud/brief",
					"data" : {
						"id" : node.nodeId
					},
					"success" : function(data) {
						$.messager.show({
							"title" : "资料摘要",
							"msg" : "<h5 style='text-overflow:ellipsis;white-space:nowrap;overflow:hidden;' title='" + node.text + "'>" + node.text + "</h5><div style=\"height:200px;overflow:auto;\"><pre>" + data + "</pre></div>",
							"showType" : "fade",
							"draggable" : true,
							"width" : "500px",
							"height" : "280px",
							"style" : {
								"top" : 280,
								"right" : "",
								"bottom" : ""
							}
						});
					}
				});
			} else {
				if (node.state == "closed") {
					$("#doctree").tree("expand", node.target);
					node.state = "open";
				} else if (node.state == "open") {
					$("#doctree").tree("collapse", node.target);
					node.state = "closed";
				}
			}
		},
		"onBeforeExpand" : function(node) {
			func.loadChildren(node);
		}
	});
	$("#opt_perm").combobox({
		"valueField" : "value",
		"textField" : "label",
		"data" : [ {
			"value" : "private",
			"label" : "私有（仅本人可见）"
		}, {
			"value" : "public",
			"label" : "公开（全部信息化人员可见）"
		}, {
			"value" : "permitted",
			"label" : "有限（以下选中的人员可见）"
		} ]
	});
	$("#opt_user").tree({
		"url" : "common/all-iters",
		"checkbox" : true
	});
	$("#refresh").on("click", function() {
		func.init();
	});
	$("#createList").on("click", function() {
		func.initOptDlg("createList");
		$("#opt").dialog("open");
	});
	$("dl#filesList").delegate("div.fcc a", "click", function() {
		$(this).parent().next().next().children(":file").trigger("click");
	});
	$("dl#filesList").delegate("div.frc a", "click", function() {
		$(this).parent().parent().remove();
	});
	$("dl#filesList").delegate(":file", "change", function() {
		var fn = this.files[0].name;
		var fs = this.files[0].size;
		// var ft = this.files[0].type;
		if (fs > 104857600) {
			$.msgbox("错误", "单个文件长度不能大于100M，请重新添加", "warning");
			$(this).parent().parent().remove();
		} else {
			$(this).parent().parent().children(".fnc").prop("title", fn).html(fn);
		}
	});
	$("#upl").dialog({
		"buttons" : [ {
			"text" : "添加",
			"iconCls" : "icon-plus",
			"handler" : function() {
				func.addFile();
			}
		}, {
			"text" : "清除",
			"iconCls" : "icon-forbid",
			"handler" : function() {
				$("#filesList").html("");
			}
		}, {
			"text" : "上传",
			"iconCls" : "icon-saved",
			"handler" : function() {
				var fs = $("#filesList :file");
				var sz = 0;
				$.each(fs, function(i, n) {
					sz += n.files[0].size;
				});
				if (sz == 0) {
					$.msgbox("错误", "当前没有选择任何文件", "warning");
				} else if (sz >= 2147483648) {
					$.msgbox("错误", "选择的文件总长度超过2GB，超过一次性上传的长度上限", "warning");
				} else {
					func.upload();
				}
			}
		}, {
			"text" : "取消",
			"iconCls" : "icon-multiple",
			"handler" : function() {
				$("#upl").dialog("close");
			}
		} ]
	});
	$("#fav").dialog({
		"buttons" : [ {
			"text" : "确定",
			"iconCls" : "icon-saved",
			"handler" : function() {
				if ($("#fl").combobox("isValid")) {
					var flId = $("#fl").combobox("getValue");
					var me = $("#doctree").tree("getSelected");
					if (me.nodeType >= -111) {
						// 移动
						$.waitbox("正在移动");
						$.ajax({
							"url" : "cloud/move",
							"data" : {
								"id" : me.id,
								"nodeId" : flId,
								"nodeType" : me.nodeType
							},
							"success" : function(data) {
								$.waitbox();
								if (data.length > 0) {
									var myp = $("#doctree").tree("getParent", me.target);
									var myr = $("#doctree").tree("getParent", myp.target);
									var mys = $("#doctree").tree("getChildren", myr.target);
									$("#doctree").tree("remove", me.target);
									$.each(mys, function(i, n) {
										if (n.nodeId == flId && n.loaded) {
											$("#doctree").tree("append", {
												"parent" : n.target,
												"data" : data
											});
											$("#doctree").tree("collapse", n.target);
											$("#doctree").tree("expand", n.target);
										}
									});
								}
								$("#fav").dialog("close");
							}
						});
					} else {
						// 收藏
						$.waitbox("正在收藏");
						$.ajax({
							"url" : "cloud/favor",
							"data" : {
								"id" : me.nodeId,
								"nodeId" : flId
							},
							"success" : function(data) {
								$.waitbox();
								if (data.length == 0) {
									$.msgbox("错误", "收藏失败，此文件已经收藏，或资料源文件不存在，或没有访问权限", "warning");
								} else {
									var fvRoot = $("#doctree").tree("find", -2);
									if (me.nodeType == -113) {
										// 直接推送来的文件，收藏之后删除推送
										$("#doctree").tree("remove", me.target);
									}
									// fvRoot.loaded = false;
									var v = $("#doctree").tree("getChildren", fvRoot.target);
									$.each(v, function(i, n) {
										if (n.nodeId == flId && n.loaded) {
											$("#doctree").tree("append", {
												"parent" : n.target,
												"data" : data
											});
											$("#doctree").tree("collapse", n.target);
											$("#doctree").tree("expand", n.target);
										}
									});
									$.msgbox("消息", "收藏成功", "info");
								}
								$("#fav").dialog("close");
							}
						});
					}
				}
			}
		}, {
			"text" : "取消",
			"iconCls" : "icon-multiple",
			"handler" : function() {
				$("#fav").dialog("close");
			}
		} ]
	});
	$("#fl").combobox({
		"onLoadSuccess" : function(node, data) {
			if (node.length > 0) {
				$(this).combobox("setValue", node[0].nodeId);
			}
		}
	});
	$("#ver").dialog({
		"buttons" : [ {
			"text" : "确定",
			"iconCls" : "icon-saved",
			"handler" : func.brief
		}, {
			"text" : "取消",
			"iconCls" : "icon-multiple",
			"handler" : function() {
				$("#ver").dialog("close");
			}
		} ]
	});
	$("#ver_type").combobox({
		"valueField" : "id",
		"textField" : "text",
		"data" : [ {
			"id" : 0,
			"text" : "大纲"
		}, {
			"id" : 1,
			"text" : "草稿"
		}, {
			"id" : 2,
			"text" : "初稿"
		}, {
			"id" : 3,
			"text" : "审核稿"
		}, {
			"id" : 4,
			"text" : "审查稿"
		}, {
			"id" : 5,
			"text" : "批阅稿"
		}, {
			"id" : 6,
			"text" : "讨论稿"
		}, {
			"id" : 7,
			"text" : "定稿"
		}, {
			"id" : 8,
			"text" : "正式稿"
		}, {
			"id" : 9,
			"text" : "最终稿"
		}, {
			"id" : 10,
			"text" : "节选版"
		}, {
			"id" : 11,
			"text" : "精校版"
		}, {
			"id" : 12,
			"text" : "停用版"
		}, {
			"id" : 13,
			"text" : "无效版"
		} ]
	});
	$("#ver_brf").textbox('textbox').on("keyup", function(e) {
		var r = 512 - $("#ver_brf").textbox("getText").length
		$("#brf_rest").html(r);
		if (r < 0)
			$("#brf_rest").addClass("Red");
		else
			$("#brf_rest").removeClass("Red");
	}).on("contextmenu", function(e) {
		return false;
	}).css({
		"white-space" : "pre",
		"overflow" : "auto"
	});
	/*
	 * 下面是具体操作功能
	 */
	// 文件上传
	$("#upload").on("click", function() {
		func.initUploadDialog();
		$("#upl").dialog("open");
	});
	// 文件、栏目、收藏夹下载
	$("#download").on("click", function() {
		var node = $("#doctree").tree("getSelected");
		if (node.nodeType > -100) {
			// 栏目、收藏夹、订阅的栏目、推送的栏目
			$("#dl_list_id").val(node.nodeId);
			$("#dl_list_type").val(node.nodeType);
			$("#dl_list").form("submit");
		} else {
			// 文件
			$("#dl_file_id").val(node.nodeId);
			$("#dl_file").form("submit");
		}
	});
	// 五种删除：删除栏目、删除收藏夹、删除文件、取消订阅、取消收藏
	$("#delete").on("click", function() {
		var node = $("#doctree").tree("getSelected");
		switch (node.nodeType) {
		case -11: // 删除栏目
			$.messager.confirm("确认", "删除栏目将同时删除其中的文件，无法恢复。你确定要删除\"" + node.text + "\"吗？", function(r) {
				if (r) {
					$.waitbox("正在删除");
					$.ajax({
						"url" : "cloud/del-list",
						"data" : {
							"id" : node.nodeId
						},
						"success" : function(result) {
							if (result < 0) {
								$.msgbox("错误", "删除栏目失败，但已有" + (-result - 1) + "个文件被永久删除", "warning");
							} else {
								$("#doctree").tree("remove", node.target);
							}
							$.waitbox();
						}
					});
				}
			});
			break;
		case -21: // 删除收藏夹
			$.messager.confirm("确认", "删除收藏夹将同时取消其中所有的文件收藏，确实要删除\"" + node.text + "\"吗？", function(r) {
				if (r) {
					$.waitbox("正在删除");
					$.ajax({
						"url" : "cloud/del-favorlist",
						"data" : {
							"id" : node.nodeId
						},
						"success" : function(result) {
							$("#doctree").tree("remove", node.target);
							$.waitbox();
						}
					});
				}
			});
			break;
		case -31: // 取消订阅
			$.messager.confirm("确认", "确实要取消订阅\"" + node.text + "\"吗？", function(r) {
				if (r) {
					$.waitbox("正在取消");
					$.ajax({
						"url" : "cloud/del-rss",
						"data" : {
							"id" : node.id
						},
						"success" : function(result) {
							$("#doctree").tree("remove", node.target);
							$.waitbox();
						}
					});
				}
			});
			break;
		case -101: // 删除文件
			$.messager.confirm("确认", "目前系统还没有回收站功能，文件删除后无法恢复。你确定要删除\"" + node.text + "\"吗？", function(r) {
				if (r) {
					$.waitbox("正在删除");
					$.ajax({
						"url" : "cloud/del-file",
						"data" : {
							"id" : node.nodeId
						},
						"success" : function(result) {
							if (result < 0) {
								$.msgbox("错误", "删除文件失败", "warning");
							} else {
								$("#doctree").tree("remove", node.target);
							}
							$.waitbox();
						}
					});
				}
			});
			break;
		case -111: // 取消收藏
			$.messager.confirm("确认", "确实要取消收藏\"" + node.text + "\"吗？", function(r) {
				if (r) {
					$.waitbox("正在取消");
					$.ajax({
						"url" : "cloud/del-favor",
						"data" : {
							"id" : node.id
						},
						"success" : function(result) {
							$("#doctree").tree("remove", node.target);
							$.waitbox();
						}
					});
				}
			});
			break;
		default:
		}
	});
	// 移动文件到其他栏目，或移动收藏的文件到其他收藏夹
	$("#move").on("click", function() {
		var node = $("#doctree").tree("getSelected");
		if (node.nodeType == -101) {
			$("#fav h5").html("选择栏目：");
			$("#fl").combobox({
				"url" : "cloud/load-children?nodeId=-1&nodeType=-1",
				"missingMessage" : "必须选择一个栏目"
			});
			$("#fav").dialog({
				"title" : "移动我的资料"
			}).dialog("open");
		} else {
			$("#fav h5").html("选择收藏夹：");
			$("#fl").combobox({
				"url" : "cloud/load-children?nodeId=-2&nodeType=-2",
				"missingMessage" : "必须选择一个收藏夹"
			});
			$("#fav").dialog({
				"title" : "移动收藏的资料"
			}).dialog("open");
		}
	});
	// 栏目、文件、收藏夹重命名
	$("#rename").on("click", function() {
		func.initOptDlg("rename");
		$("#opt").dialog("open");
	});
	// 文件版本及摘要
	$("#brief").on("click", function() {
		$.ajax({
			"url" : "cloud/info",
			"data" : {
				"id" : $("#doctree").tree("getSelected").nodeId
			},
			"success" : function(info) {
				if (info.length == 0) {
					$.msgbox("错误", "资料文件不存在", "warning");
					return;
				}
				$("#ver_major").numberspinner("setValue", info.majorVersion);
				$("#ver_minor").numberbox("setValue", info.minorVersion);
				$("#ver_type").combobox("setText", info.versionType);
				$("#ver_brf").textbox("setText", info.brief);
				$("#ver").dialog({
					"title" : "资料版本与摘要 - " + $("#doctree").tree("getSelected").text
				}).dialog("open");
			}
		});
	});
	// 栏目或文件标签设置
	$("#tag").on("click", function() {
		var node = $("#doctree").tree("getSelected");
		func.initOptDlg("tags");
		$.ajax({
			"url" : "cloud/load-tags",
			"data" : {
				"id" : node.nodeId,
				"nodeType" : node.nodeType
			},
			"success" : function(tags) {
				$("#opt_tags").textbox({
					"prompt" : "输入标签，多个标签用空格分隔",
					"value" : tags
				});
				$("#opt").dialog("open");
			}
		});
	});
	// 修改文件或栏目的访问权限
	$("#permission").on("click", function() {
		func.initOptDlg("permission");
		$("#opt").dialog("open");
	});
	// 推送栏目或文件
	$("#push").on("click", function() {
		func.initOptDlg("push");
		$("#opt").dialog("open");
	});
	// 拒绝推送类的栏目或文件
	$("#refuse").on("click", function() {
		node = $("#doctree").tree("getSelected");
		$.messager.confirm("确认", "确实要拒绝推送来的\"" + node.text + "\"吗？", function(r) {
			if (r) {
				$.waitbox("正在拒绝推送");
				$.ajax({
					"url" : "cloud/refuse-push",
					"data" : {
						"id" : node.id,
						"nodeType" : node.nodeType
					},
					"success" : function(result) {
						$("#doctree").tree("remove", node.target);
						$.waitbox();
					}
				});
			}
		});
	});
	// 收藏文件
	$("#favor").on("click", function() {
		$("#fav h5").html("选择收藏夹：");
		$("#fl").combobox({
			"url" : "cloud/load-children?nodeId=-2&nodeType=-2",
			"missingMessage" : "必须选择一个收藏夹"
		});
		$("#fav").dialog({
			"title" : "资料收藏"
		}).dialog("open");
	});
	// 订阅栏目
	$("#rss").on("click", function() {
		$.waitbox("正在订阅");
		var me = $("#doctree").tree("getSelected");
		$.ajax({
			"url" : "cloud/subscribe",
			"data" : {
				"id" : me.nodeId
			},
			"success" : function(data) {
				$.waitbox();
				if (data.length == 0) {
					$.msgbox("错误", "订阅失败，栏目已经订阅，或栏目不存在，或没有访问权限", "warning");
				} else {
					var subsRoot = $("#doctree").tree("find", -3);
					$("#doctree").tree("remove", me.target);
					if (subsRoot.loaded) {
						$("#doctree").tree("append", {
							"parent" : subsRoot.target,
							"data" : data
						});
					}
					$("#doctree").tree("collapse", subsRoot.target);
					$("#doctree").tree("expand", subsRoot.target);
					$.msgbox("消息", "订阅成功", "info");
				}
			}
		});
	});
});