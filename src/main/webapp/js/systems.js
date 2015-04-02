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
					"width" : 108
				}, {
					"field" : "alias",
					"title" : "简称",
					"sortable" : true,
					"width" : 72
				} ] ]
			});
		}
	};

	/**
	 * initialize wpad
	 */

	page.init();
});