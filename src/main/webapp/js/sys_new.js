$(function() {
	/**
	 * initialize page parameters and functions
	 */
	$("#breadcrumbs")
			.html(
					"<a href=\"javascript:window.location.replace('func?code=HOME_PAGE')\">首页</a>&nbsp;&#187;&nbsp;<a href=\"javascript:window.location.replace('func?code=PRJ_SYSTEM')\">信息系统管理</a>&nbsp;&#187;&nbsp;新增信息系统");

	/**
	 * initialize navi
	 */
	$.initNavi("sys_new");

	/**
	 * initialize wpad
	 */
	$("#d_companiesInScope").hide();
	$("#v_scope").combobox({
		"onChange" : function(newValue, oldValue) {
			if (newValue == 5) {
				$("#d_companiesInScope").show();
			} else {
				$("#d_companiesInScope").hide();
			}
		}
	});
});