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

	var page = {
		"reset" : function() {
			$("#v_type").combobox("setValue", 0);
			$("#v_stage").combobox("setValue", 0);
			$("#v_name").textbox("clear");
			$("#v_alias").textbox("clear");
			$("#v_modelOrVersion").textbox("clear");
			$("#v_brief").textbox("clear");
			$("#v_securityLevel").combobox("setValue", 0);
			$("#v_userBrief").textbox("clear");
			$("#v_provider").textbox("clear");
			$("#v_scope").combobox("setValue", 0);
			$("#v_companiesInScope").combotree("clear");
			$("#v_deploy").combobox("setValue", 3);
			$("#v_branches").combobox("clear");
			$("#v_constructedTime").datebox("clear");
			$("#v_freeMaintainMonths").numberspinner("clear");
			$("#v_prjOptions").combobox("setValue", 0);
			$("#v_mtnOptions").combobox("setValue", 0);
			$("#v_wikiOptions").combobox("setValue", 0);
			$("#v_oldPrjs").combobox("clear");
			$("#v_oldMtns").combobox("clear");
			$("#v_wikies").combobox("clear");
			$("#v_newPrjName").textbox("clear");
			$("#v_newMtnName").textbox("clear");
			$("#v_newWikiName").textbox("clear");
		}
	};
	/**
	 * initialize wpad
	 */
	$("#d_companiesInScope,#d_newPrj,#d_oldPrj,#d_newMtn,#d_oldMtn,#d_wikies").hide();
	$("#v_scope").combobox({
		"onChange" : function(newValue, oldValue) {
			if (newValue == 5) {
				$("#d_companiesInScope").show();
			} else {
				$("#v_companiesInScope").combotree("clear");
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
	$("#v_securityLevel").combobox({
		"onChange" : function(newValue, oldValue) {
			if (newValue == 0) {
				$("#v_securityCode").textbox("clear");
				$("#v_securityCode").textbox("disable");
			} else {
				$("#v_securityCode").textbox("enable");
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
				$("#d_newWiki").show();
				$("#d_wikies").hide();
			} else {
				$("#d_newWiki").hide();
				$("#d_wikies").show();
			}
		}
	});
	$("#f_reset").on("click", page.reset);
});