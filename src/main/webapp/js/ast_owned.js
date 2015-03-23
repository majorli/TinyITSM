$(function() {
	/**
	 * initialize page parameters and functions
	 */
	$("#breadcrumbs").html("<a href=\"javascript:window.location.replace('func?code=HOME_PAGE')\">首页</a>&nbsp;&#187;&nbsp;我的设备");

	/**
	 * initialize navi
	 */
	$.initNavi("ast_owned");

	/**
	 * initialize wpad
	 */
	$("#myEquipments").datagrid({
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
		"title" : "设备列表",
		"border" : false,
		"url" : "asset/owned-equipments"
	});
	$("#refresh").on("click", function() {
		$("#myEquipments").datagrid("reload");
	});
	$("div#cmd a.Apply").on("click", function() {
		var sel = $("#myEquipments").datagrid("getSelections");
		if (sel.length == 0) {
			return;
		}
		var code = +$(this).get(0).id;
		var ids = [];
		$.each(sel, function(i, n) {
			ids.push(n.id);
		});
		// TODO 生成服务单或进入服务单录入界面，服务单录入界面应该能接受服务对象和服务编号的可能的预设值
		console.info("申请IT服务，类型编号=" + $(this).get(0).id + ", 服务对象编号：" + ids.join());
	});
});