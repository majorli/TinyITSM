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
	$("#d_companiesInScope,#d_newPrj,#d_oldPrj,#d_newMtn,#d_oldMtn,#d_wikies").hide();
	$("#v_scope").combobox({
		"onChange" : function(newValue, oldValue) {
			if (newValue == 5) {
				$("#d_companiesInScope").show();
			} else {
				$("#d_companiesInScope").hide();
			}
		}
	});
	$("#v_stage").combobox({
		"onChange" : function(newValue, oldValue) {
			if (newValue == 0) {
				$("#v_constructedTime").datebox("enable");
			} else {
				$("#v_constructedTime").datebox("clear");
				$("#v_constructedTime").datebox("disable");
			}
		}
	});
	$("#v_prjOptions").combobox({
		"onChange" : function(newValue, oldValue) {
			$("#d_newPrj,#d_oldPrj").hide();
			if (newValue == 1) {
				$("#d_newPrj").show();
			} else if (newValue == 2) {
				$("#d_oldPrj").show();
			}
		}
	});
	$("#v_mtnOptions").combobox({
		"onChange" : function(newValue, oldValue) {
			$("#d_newMtn,#d_oldMtn").hide();
			if (newValue == 1) {
				$("#d_newMtn").show();
			} else if (newValue == 2) {
				$("#d_oldMtn").show();
			}
		}
	});
	$("#v_wikiOptions").combobox({
		"onChange" : function(newValue, oldValue) {
			if (newValue == 0) {
				$("#d_wikies").hide();
			} else {
				$("#d_wikies").show();
			}
		}
	});
});