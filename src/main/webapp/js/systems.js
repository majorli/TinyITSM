$(function() {
	/**
	 * initialize page parameters and functions
	 */
	$("#breadcrumbs").html("<a href=\"javascript:window.location.replace('func?code=HOME_PAGE')\">首页</a>&nbsp;&#187;&nbsp;信息系统管理");

	/**
	 * initialize navi
	 */
	$.initNavi("systems");
	var page = {
		"init" : function() {
			$("#systems").datagrid({
				"fit" : true,
				"rownumbers" : true,
				"columns" : [ [ {
					"field" : "type",
					"title" : "类别",
					"sortable" : true,
					"width" : 72
				}, {
					"field" : "name",
					"title" : "名称",
					"sortable" : true,
					"width" : 120
				}, {
					"field" : "alias",
					"title" : "简称",
					"sortable" : true,
					"width" : 72
				}, {
					"field" : "modelOrVersion",
					"title" : "型号/版本",
					"sortable" : false,
					"width" : 108
				}, {
					"field" : "brief",
					"title" : "功能与用途",
					"sortable" : false,
					"width" : 180
				}, {
					"field" : "securityLevel",
					"title" : "等级保护",
					"sortable" : true,
					"width" : 96
				}, {
					"field" : "userBrief",
					"title" : "使用者",
					"sortable" : false,
					"width" : 120
				}, {
					"field" : "provider",
					"title" : "供应商",
					"sortable" : true,
					"width" : 72
				}, {
					"field" : "owner",
					"title" : "主管公司",
					"sortable" : true,
					"width" : 72
				}, {
					"field" : "scope",
					"title" : "使用范围",
					"sortable" : true,
					"width" : 108
				}, {
					"field" : "deploy",
					"title" : "部署范围",
					"sortable" : true,
					"width" : 108
				}, {
					"field" : "branches",
					"title" : "分支数",
					"sortable" : false,
					"width" : 72
				}, {
					"field" : "stage",
					"title" : "当前状态",
					"sortable" : true,
					"width" : 72
				}, {
					"field" : "constructedTime",
					"title" : "建成时间",
					"sortable" : true,
					"width" : 84
				}, {
					"field" : "freeMaintainMonths",
					"title" : "免费服务期（月）",
					"sortable" : true,
					"width" : 96
				}, {
					"field" : "abandonedTime",
					"title" : "停用时间",
					"sortable" : true,
					"width" : 84
				} ] ]
			});
		}
	};

	/**
	 * initialize wpad
	 */

	page.init();
	$("#append").on("click", function() {
		window.location.replace("func?code=PRJ_NEW_SYS");
	});
});