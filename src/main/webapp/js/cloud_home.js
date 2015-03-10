$(function() {
	/**
	 * initialize page parameters and functions
	 */
	$("#breadcrumbs").html("<a href=\"javascript:window.location.replace('func?code=HOME_PAGE')\">首页</a>&nbsp;&#187;&nbsp;资料库首页");
	var func = {
		"fId" : 0,
		"lId" : 0,
		"refreshSubscriptions" : function() {
			$.ajax({
				"url" : "cloud/refr-subs",
				"data" : {
					"id" : func.fId
				},
				"success" : function(subs) {
					if (subs.length > 0) {
						func.fId = subs[0].id;
						if (func.lId == 0) {
							func.lId = subs[subs.length - 1].id;
						}
						$.each(subs.reverse(), function(i, n) {
							var d = [];
							d.push("<dt>" + n.publishTime + "</dt><dd>" + n.text);
							if (n.newRss) {
								d.push("<span class='Red'> [新消息]</span></dd>");
							} else {
								d.push("</dd>");
							}
							$("#noti").prepend(d.join(""));
						});
						$("#noti dt:first").get(0).scrollIntoView(true);
					}
				}
			});
		},
		"moreSubscriptions" : function() {
			$.ajax({
				"url" : "cloud/more-subs",
				"data" : {
					"id" : func.lId
				},
				"success" : function(subs) {
					if (subs.length > 0) {
						if (func.fId == 0) {
							func.fId = subs[0].id;
						}
						func.lId = subs[subs.length - 1].id;
						$.each(subs, function(i, n) {
							var d = [];
							d.push("<dt>" + n.publishTime + "</dt><dd>" + n.text);
							if (n.newRss) {
								d.push("<span class='Red'> [新消息]</span></dd>");
							} else {
								d.push("</dd>");
							}
							$("#noti").append(d.join(""));
						});
						$("#noti dt:last").get(0).scrollIntoView(false);
					}
				}
			});
		},
		"download" : function() {
			var node = $("#tops").tree("getSelected");
			var i = node.id % 10000000;
			if (node.id > 40000000) {
				$("#dl_list_id").val(i);
				$("#dl_list_type").val(-11);
				$("#dl_list").form("submit");
			} else {
				$("#dl_file_id").val(i);
				$("#dl_file").form("submit");
			}
		}
	};

	/**
	 * initialize navi
	 */
	$.initNavi("cloud_home");

	/**
	 * initialize wpad
	 */
	$("#download,#subscribe,#favorite").hide();
	$("#tops").tree({
		"url" : "cloud/top-tens",
		"onSelect" : function(node) {
			if (node.id < 10) {
				$("#download,#subscribe,#favorite").hide();
			} else if (node.id > 40000000) {
				$("#download,#subscribe").show();
				$("#favorite").hide();
			} else {
				$("#download,#favorite").show();
				$("#subscribe").hide();
			}
		},
		"onDblClick" : function(node) {
			if (node.id > 40000000) {
				// 栏目
				$.ajax({
					"url" : "cloud/load-children",
					"data" : {
						"nodeId" : node.id % 10000000,
						"nodeType" : -11
					},
					"success" : function(data) {
						var files = [];
						files.push("<h5 style='text-overflow:ellipsis;white-space:nowrap;overflow:hidden;' title='" + node.text + "'>" + node.text + "</h5><div style='height:300px;overflow:auto'>");
						if (data.length > 0) {
							$.each(data, function(i, n) {
								files.push("<p>" + (i + 1) + ". " + n.text + "</p>");
							});
						} else {
							files.push("<p>无资料</p>");
						}
						files.push("</div>");
						$.messager.show({
							"title" : "栏目资料目录",
							"msg" : files.join(""),
							"showType" : "fade",
							"draggable" : true,
							"width" : "500px",
							"height" : "380px",
							"style" : {
								"top" : 280,
								"right" : "",
								"bottom" : ""
							}							
						});
					}
				});
			} else if (node.id > 10000000) {
				$.ajax({
					"url" : "cloud/brief",
					"data" : {
						"id" : node.id % 10000000
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
			}
		}
	});
	$("#fav").dialog({
		"buttons" : [ {
			"text" : "确定",
			"iconCls" : "icon-saved",
			"handler" : function() {
				if ($("#fl").combobox("isValid")) {
					var flId = $("#fl").combobox("getValue");
					var me = $("#tops").tree("getSelected");
					$.waitbox("正在收藏");
					$.ajax({
						"url" : "cloud/topten-favor",
						"data" : {
							"id" : me.id % 10000000,
							"flId" : flId
						},
						"success" : function(code) {
							$.waitbox();
							switch (code) {
							case -2:
								$.msgbox("错误", "收藏失败，资料源文件不存在，或没有访问权限", "warning");
								break;
							case -1:
								$.msgbox("错误", "你已收藏过此文件，不能重复收藏", "warning");
								break;
							case 0:
								$.msgbox("错误", "这是你自己的文件，无需收藏", "warning");
								break;
							case 1:
								$.msgbox("消息", "收藏成功", "info");
								break;
							default:
								$.msgbox("错误", "未知错误", "warning");
							}
							$("#fav").dialog("close");
						}
					});
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
		"url" : "cloud/load-children?nodeId=-2&nodeType=-2",
		"missingMessage" : "必须选择一个收藏夹",
	});
	$("#refresh").on("click", function() {
		$("#tops").tree("reload");
	});
	$("#download").on("click", func.download);
	$("#favorite").on("click", function() {
		$("#fav h5").html("选择收藏夹：");
		var d = $("#fl").combobox("getData");
		if (d.length > 0) {
			$("#fl").combobox("select", d[0].nodeId);
		}
		$("#fav").dialog({
			"title" : "资料收藏"
		}).dialog("open");
	});
	$("#subscribe").on("click", function() {
		var me = $("#tops").tree("getSelected");
		$.waitbox("正在订阅");
		$.ajax({
			"url" : "cloud/topten-subscribe",
			"data" : {
				"id" : me.id % 10000000
			},
			"success" : function(code) {
				$.waitbox();
				switch (code) {
				case -2:
					$.msgbox("错误", "订阅失败，源栏目不存在或没有访问权限", "warning");
					break;
				case -1:
					$.msgbox("错误", "你已订阅过此栏目，不能重复订阅", "warning");
					break;
				case 0:
					$.msgbox("错误", "这是你自己的栏目，无需订阅", "warning");
					break;
				case 1:
					$.msgbox("消息", "订阅成功", "info");
					break;
				default:
					$.msgbox("错误", "未知错误", "warning");
				}
			}
		});
	});
	$("#refrNoti").on("click", func.refreshSubscriptions);
	$("#moreNoti").on("click", func.moreSubscriptions);
	$.ajax({
		"url" : "cloud/refr-subs",
		"data" : {
			"id" : func.fId
		},
		"success" : function(subs) {
			if (subs.length > 0) {
				func.fId = subs[0].id;
				if (func.lId == 0) {
					func.lId = subs[subs.length - 1].id;
				}
				$.each(subs.reverse(), function(i, n) {
					var d = [];
					d.push("<dt>" + n.publishTime + "</dt><dd>" + n.text);
					if (n.newRss) {
						d.push("<span class='Red'> [新消息]</span></dd>");
					} else {
						d.push("</dd>");
					}
					$("#noti").prepend(d.join(""));
				});
			}
		}
	});
});