$(function() {
	/**
	 * initialize page parameters and functions
	 */
	$("#breadcrumbs").html("<a href=\"javascript:window.location.replace('func?code=HOME_PAGE')\">首页</a>&nbsp;&#187;&nbsp;系统控制台");
	$.extend($.fn.validatebox.defaults.rules, {
		"equals" : {
			validator : function(value, param) {
				return value == $(param[0]).val();
			},
			message : "两次输入的密码不一致"
		},
		"minLength" : {
			validator : function(value, param) {
				return value.length >= param[0];
			},
			message : '密码长度不得少于 {0}个字符'
		}
	});

	/**
	 * initialize navi
	 */
	$.initNavi("settings");

	/**
	 * initialize wpad
	 */
	$("#syscon").css({
		"padding" : "1em 2em"
	}).panel({
		"height" : "100%",
		"title" : "系统控制台 [修改用户特殊属性，设置外部系统接口，批量导入初始化数据]"
	});

	/**
	 * 用户账户设置特殊权限部分
	 */
	$("#user").searchbox({
		"prompt" : "关键字：用户名、员工姓名或用户ID",
		"searcher" : function(value, name) {
			if (value.trim() == "")
				return;
			$.ajax({
				"url" : "portal/seek-user",
				"data" : {
					"keyword" : value
				},
				"success" : function(data) {
					if (data.user) {
						$("#userid").val(data.user.id);
						$("#username").html(data.user.username);
						$("#employee").html(data.employee);
						$("#department").html(data.department);
						$("#roles input[type=checkbox]").prop("disabled", false);
						$("#iter").prop("checked", data.user.iter);
						$("#admin").prop("checked", data.user.admin);
						$("#leader").prop("checked", data.user.leader);
						$("#supervisor").prop("checked", data.user.supervisor);
						$("#auditor").prop("checked", data.user.auditor);
						if (!data.user.iter)
							$("#admin").prop("disabled", true);
					} else {
						$("#userid").val("");
						$("#username").html("");
						$("#employee").html("");
						$("#department").html("");
						$("#roles input[type=checkbox]").prop("disabled", true).prop("checked", false);
					}
					$("#setRoles").linkbutton("disable");
				}
			});
		}
	});
	$("#iter").on("change", function() {
		if ($(this).prop("checked"))
			$("#admin").prop("disabled", false);
		else
			$("#admin").prop("disabled", true).prop("checked", false);
	});
	$("#roles input[type=checkbox]").prop("disabled", true).on("change", function() {
		$("#setRoles").linkbutton("enable");
	});
	$("#cancelRoles").on("click", function() {
		$("#user").searchbox("setValue", "");
		$("#userid").val("");
		$("#username").html("");
		$("#employee").html("");
		$("#department").html("");
		$("#roles input[type=checkbox]").prop("disabled", true).prop("checked", false);
		$("#setRoles").linkbutton("disable");
	});
	$("#setRoles").linkbutton("disable").on(
			"click",
			function() {
				if (!$("#userid").val() || $(this).linkbutton("options").disabled)
					return;
				var tuser = [ $("#userid").val(), $("#iter").prop("checked"), $("#admin").prop("checked"), $("#leader").prop("checked"),
						$("#supervisor").prop("checked"), $("#auditor").prop("checked") ];
				$.ajax({
					"url" : "portal/set-roles",
					"data" : {
						"roles" : tuser.join(",")
					},
					"success" : function(data) {
						if (data == true) {
							$.messager.alert("消息", "用户角色保存成功", "info").window({
								"top" : "280px"
							});
							$("#setRoles").linkbutton("disable");
						} else {
							$.messager.alert("错误", "用户角色保存失败", "warning").window({
								"top" : "280px"
							});
						}
					}
				});
			});

	/**
	 * 修改管理员密码的部分
	 */
	$("#pwd,#pswd,#repswd").textbox({
		"prompt" : "Password",
		"iconCls" : "icon-lock",
		"iconWidth" : 38,
		"required" : true,
		"missingMessage" : "密码不能为空",
		"onChange" : function(newValue, oldValue) {
			$("#resetAdminPassword").linkbutton("enable");
		}
	});
	$("#pwd,#pswd").textbox({
		"validType" : "minLength[6]"
	});
	$("#repswd").textbox({
		"validType" : "equals['#pswd']"
	});
	$("#resetAdminPassword").linkbutton("disable").on("click", function() {
		if ($(this).linkbutton("options").disabled)
			return;
		if (!$("#pwd").textbox("isValid")) {
			$.messager.alert("错误", "请输入至少6位的旧密码", "warning").window({
				"top" : "280px"
			});
			return;
		}
		if (!$("#pswd").textbox("isValid")) {
			$.messager.alert("错误", "请输入至少6位的新密码", "warning").window({
				"top" : "280px"
			});
			return;
		}
		if (!$("#repswd").textbox("isValid")) {
			$.messager.alert("错误", "两次输入的新密码必须一致", "warning").window({
				"top" : "280px"
			});
			return;
		}
		$.ajax({
			"url" : "portal/change-pwd",
			"data" : {
				"oldPassword" : $("#pwd").textbox("getText"),
				"newPassword" : $("#pswd").textbox("getText")
			},
			"success" : function(data) {
				if (data == true) {
					$.messager.alert("消息", "管理员密码变更成功", "info").window({
						"top" : "280px"
					});
					$("#pwd,#pswd,#repswd").textbox({
						"value" : ""
					});
					$("#resetAdminPassword").linkbutton("disable");
				} else {
					$.messager.alert("错误", "管理员密码变更失败，请输入正确的旧密码", "warning").window({
						"top" : "280px"
					});
				}
			}
		});
	});
	$("#cancelAdminPwd").on("click", function() {
		$("#pwd,#pswd,#repswd").textbox({
			"value" : ""
		});
		$("#resetAdminPassword").linkbutton("disable");
	});
	/**
	 * 导入数据的部分
	 */
	var imp = {
		"progress" : function() {
			$.messager.progress({
				title : "请稍候",
				msg : "批量导入数据处理中......"
			}).window({
				top : "280px"
			});
		},
		"result" : function(data) {
			$.messager.progress("close");
			if (data.code == 0)
				$.messager.alert("消息", data.tip, "info").window({
					"top" : "280px"
				});
			else
				$.messager.alert("错误", data.tip, "warning").window({
					"top" : "280px"
				});
		}
	};
	// $("#hr");
	$("#hr").filebox({
		"onChange" : function(newValue, oldValue) {
			if (newValue) {
				var fn = newValue.split("\\");
				$(this).filebox("textbox").val(fn[fn.length - 1]);
			}
		}
	}).filebox("textbox").prop("disabled", true);
	$("#ci").filebox({
		"onChange" : function(newValue, oldValue) {
			if (newValue) {
				var fn = newValue.split("\\");
				$(this).filebox("textbox").val(fn[fn.length - 1]);
			}
		}
	}).filebox("textbox").prop("disabled", true);
	$("#hrsubmit").on("click", function() {
		var f = $("#hrfile :file")[0].files;
		if (f.length == 0) {
			$.messager.alert("错误", "请选择一个文件(100M以内的符合数据导入格式规范的Excel文档)", "warning").window({
				"top" : "280px"
			});
			return;
		}
		if (f[0].size > 104857600) {
			$.messager.alert("错误", "文件长度不能超过100M", "warning").window({
				"top" : "280px"
			});
			$("#hr").filebox("clear");
			return;
		}
		if (f[0].type != "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" && f[0].type != "application/vnd.ms-excel") {
			$.messager.alert("错误", "批量导入数据只能采用Excel文档", "warning").window({
				"top" : "280px"
			});
			$("#hr").filebox("clear");
			return;
		}

		$.messager.confirm("确认", "批量导入可能导致数据错误、重复、不一致等问题，你确定要导入吗？", function(r) {
			if (!r) {
				$("#hr").filebox("clear");
				return;
			}
			imp.progress();
			$("#hrfile").form("submit", {
				"success" : function(data) {
					imp.result(JSON.parse(data));
					$("#hr").filebox("clear");
				}
			});
		});
	});
	$("#cisubmit").on("click", function() {
		var f = $("#cifile :file")[0].files;
		if (f.length == 0) {
			$.messager.alert("错误", "请选择一个文件(100M以内的符合数据导入格式规范的Excel文档)", "warning").window({
				"top" : "280px"
			});
			return;
		}
		if (f[0].size > 104857600) {
			$.messager.alert("错误", "文件长度不能超过100M", "warning").window({
				"top" : "280px"
			});
			$("#ci").filebox("clear");
			return;
		}
		if (f[0].type != "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" && f[0].type != "application/vnd.ms-excel") {
			$.messager.alert("错误", "批量导入数据只能采用Excel文档", "warning").window({
				"top" : "280px"
			});
			$("#ci").filebox("clear");
			return;
		}

		$.messager.confirm("确认", "批量导入可能导致数据错误、重复、不一致等问题，你确定要导入吗？", function(r) {
			if (!r) {
				$("#ci").filebox("clear");
				return;
			}
			imp.progress();
			$("#cifile").form("submit", {
				"success" : function(data) {
					imp.result(JSON.parse(data));
					$("#ci").filebox("clear");
				}
			});
		});
	});
	$("#hrreset").on("click", function() {
		$("#hr").filebox("clear");
	});
	$("#cireset").on("click", function() {
		$("#ci").filebox("clear");
	});
	/**
	 * 重建全文检索索引
	 */
	$("#reIndex").on("click", function() {
		$.waitbox("正在初始化");
		$.ajax({
			"url" : "fts-reindex",
			"success" : function(success) {
				$.waitbox();
				if (success) {
					$.msgbox("消息", "全文检索初始化完成", "info");
				} else {
					$.msgbox("错误", "全文检索初始化失败", "warning");
				}
			}
		});
	});
});