$(function() {
	/**
	 * initialize page parameters and functions
	 */
	$("#breadcrumbs").html("<a href=\"javascript:window.location.replace('func?code=HOME_PAGE')\">首页</a>&nbsp;&#187;&nbsp;全文检索");

	/**
	 * initialize navi
	 */
	$.initNavi("fts");
	$("#naviPanel").accordion("select", 1);
	$("#fullTextSearch").searchbox("selectName", $("#fts_c").val());
	$("#fullTextSearch").searchbox("setValue", $("#fts_k").val());
	/**
	 * initialize wpad
	 */
	$("#fav").dialog({
		"buttons" : [ {
			"text" : "确定",
			"iconCls" : "icon-saved",
			"handler" : function() {
				if ($("#fl").combobox("isValid")) {
					var flId = $("#fl").combobox("getValue");
					var id = $("#temp").val();
					$.waitbox("正在收藏");
					$.ajax({
						"url" : "cloud/topten-favor",
						"data" : {
							"id" : id,
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

	$("div#results").delegate(
			"a.ShowHardware",
			"click",
			function() {
				var id = $(this).siblings("input").val();
				$.ajax({
					"url" : "asset/load-asset",
					"data" : {
						"id" : id,
						"type" : 1
					},
					"success" : function(data) {
						if (data != null) {
							var a = data;
							var m = [];
							m.push("<h5 style='text-overflow:ellipsis;white-space:nowrap;overflow:hidden;' title='" + a.fullName + "'>" + a.fullName
									+ "</h5><div style=\"height:470px;overflow:auto;\">");
							m.push("<p><span style='font-weight:bold'>资产编号：</span><span>" + a.code + "</span></p>");
							m.push("<p><span style='font-weight:bold'>财务资产编号：</span><span>" + a.financialCode + "</span></p>");
							m.push("<p><span style='font-weight:bold'>所属公司：</span><span>" + a.company + "</span></p>");
							m.push("<p><span style='font-weight:bold'>资产类型：</span><span>" + a.type + "</span></p>");
							m.push("<p><span style='font-weight:bold'>资产类别：</span><span>" + a.catalog + "</span></p>");
							m.push("<p><span style='font-weight:bold'>名称：</span><span>" + a.name + "</span></p>");
							m.push("<p><span style='font-weight:bold'>制造商/品牌：</span><span>" + a.vendor + "</span></p>");
							m.push("<p><span style='font-weight:bold'>型号：</span><span>" + a.modelOrVersion + "</span></p>");
							m.push("<p><span style='font-weight:bold'>用途及基本功能：</span><span>" + a.assetUsage + "</span></p>");
							m.push("<p><span style='font-weight:bold'>序列号：</span><span>" + a.sn + "</span></p>");
							m.push("<p><span style='font-weight:bold'>主要配置：</span><span>" + a.configuration + "</span></p>");
							m.push("<p><span style='font-weight:bold'>采购时间：</span><span>" + (a.purchaseTime == null ? "不明" : a.purchaseTime) + "</span></p>");
							m.push("<p><span style='font-weight:bold'>数量：</span><span>" + a.quantity + "</span></p>");
							m.push("<p><span style='font-weight:bold'>原值：</span><span>" + a.cost + "</span></p>");
							m.push("<p><span style='font-weight:bold'>使用情况：</span><span>" + a.state + "</span></p>");
							m.push("<p><span style='font-weight:bold'>保修状态：</span><span>" + a.warranty + "</span></p>");
							m.push("<p><span style='font-weight:bold'>物理位置：</span><span>" + a.location + "</span></p>");
							m.push("<p><span style='font-weight:bold'>网络地址：</span><span>" + a.ip + "</span></p>");
							m.push("<p><span style='font-weight:bold'>重要程度：</span><span>" + a.importance + "</span></p>");
							m.push("<p><span style='font-weight:bold'>责任人：</span><span>" + (a.owner == null ? "无" : a.owner) + "</span></p>");
							m.push("<p><span style='font-weight:bold'>备注：</span><span>" + a.comment + "</span></p>");
							m.push("</div>");
							$.messager.show({
								"title" : "资产详情",
								"msg" : m.join(""),
								"showType" : "fade",
								"draggable" : true,
								"width" : "650px",
								"height" : "550px",
								"style" : {
									"top" : 180,
									"right" : "",
									"bottom" : ""
								}
							});
						}
					}
				});
			});
	$("div#results").delegate("a.ShowSoftware", "click", function() {
		var id = $(this).siblings("input").val();
		$.ajax({
			"url" : "asset/load-asset",
			"data" : {
				"id" : id,
				"type" : 3
			},
			"success" : function(data) {
				if (data != null) {
					var a = data;
					var m = [];
					m.push("<h5 style='text-overflow:ellipsis;white-space:nowrap;overflow:hidden;' title='" + a.fullName + "'>" + a.fullName
							+ "</h5><div style=\"height:470px;overflow:auto;\">");
					m.push("<p><span style='font-weight:bold'>所属公司：</span><span>" + a.company + "</span></p>");
					m.push("<p><span style='font-weight:bold'>资产类型：</span><span>" + a.type + "</span></p>");
					m.push("<p><span style='font-weight:bold'>资产类别：</span><span>" + a.catalog + "</span></p>");
					m.push("<p><span style='font-weight:bold'>名称：</span><span>" + a.name + "</span></p>");
					m.push("<p><span style='font-weight:bold'>软件厂商：</span><span>" + a.vendor + "</span></p>");
					m.push("<p><span style='font-weight:bold'>软件版本：</span><span>" + a.modelOrVersion + "</span></p>");
					m.push("<p><span style='font-weight:bold'>用途及基本功能：</span><span>" + a.assetUsage + "</span></p>");
					m.push("<p><span style='font-weight:bold'>采购时间：</span><span>" + (a.purchaseTime == null ? "不明" : a.purchaseTime) + "</span></p>");
					m.push("<p><span style='font-weight:bold'>数量：</span><span>" + a.quantity + "</span></p>");
					m.push("<p><span style='font-weight:bold'>原值：</span><span>" + a.cost + "</span></p>");
					m.push("<p><span style='font-weight:bold'>使用情况：</span><span>" + a.state + "</span></p>");
					m.push("<p><span style='font-weight:bold'>软件类型：</span><span>" + a.softwareType + "</span></p>");
					m.push("<p><span style='font-weight:bold'>许可证：</span><span>" + a.license + "</span></p>");
					m.push("<p><span style='font-weight:bold'>许可期限：</span><span>" + (a.expiredTime == null ? "永久" : a.expiredTime) + "</span></p>");
					m.push("<p><span style='font-weight:bold'>备注：</span><span>" + a.comment + "</span></p>");
					m.push("</div>");
					$.messager.show({
						"title" : "资产详情",
						"msg" : m.join(""),
						"showType" : "fade",
						"draggable" : true,
						"width" : "650px",
						"height" : "550px",
						"style" : {
							"top" : 180,
							"right" : "",
							"bottom" : ""
						}
					});
				}
			}
		});
	});
	$("div#results").delegate(
			"a.FileBrief",
			"click",
			function() {
				var id = $(this).siblings("input").val();
				var name = $(this).siblings("span").html();
				$.ajax({
					"url" : "cloud/brief",
					"data" : {
						"id" : id
					},
					"success" : function(data) {
						$.messager.show({
							"title" : "资料摘要",
							"msg" : "<h5 style='text-overflow:ellipsis;white-space:nowrap;overflow:hidden;' title='" + name + "'>" + name
									+ "</h5><div style=\"height:200px;overflow:auto;\"><pre>" + data + "</pre></div>",
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
			});
	$("div#results").delegate("a.Favorite", "click", function() {
		$("#temp").val($(this).siblings('input').val());
		$("#fav h5").html("选择收藏夹：");
		var d = $("#fl").combobox("getData");
		if (d.length > 0) {
			$("#fl").combobox("select", d[0].nodeId);
		}
		$("#fav").dialog({
			"title" : "资料收藏"
		}).dialog("open");
	});
	$("div#results").delegate("a.FileDownload", "click", function() {
		$("#dl_file_id").val($(this).siblings("input").val());
		$("#dl_file").form("submit");
	});
	$("div#results").delegate(
			"a.ListFiles",
			"click",
			function() {
				var id = $(this).siblings("input").val();
				var name = $(this).siblings("span").html();
				$.ajax({
					"url" : "cloud/load-children",
					"data" : {
						"nodeId" : id,
						"nodeType" : -11
					},
					"success" : function(data) {
						var files = [];
						files.push("<h5 style='text-overflow:ellipsis;white-space:nowrap;overflow:hidden;' title='" + name + "'>" + name
								+ "</h5><div style='height:300px;overflow:auto'>");
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
			});
	$("div#results").delegate("a.ListSubscribe", "click", function() {
		$.waitbox("正在订阅");
		$.ajax({
			"url" : "cloud/topten-subscribe",
			"data" : {
				"id" : $(this).siblings("input").val()
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
	$("div#results").delegate("a.ListDownload", "click", function() {
		$("#dl_list_id").val($(this).siblings("input").val());
		$("#dl_list_type").val(-11);
		$("#dl_list").form("submit");
	});
});