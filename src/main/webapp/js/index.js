$(function() {
	/**
	 * initialize page parameters
	 */
	$("#breadcrumbs").html("登录页");
	if (!$.getCookie("usr"))
		$.setCookie("usr", "compId=0&deptId=0&emplId=0", 3650);
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
	 * initialize easyui components and event handlers
	 */
	$("#lp").css("padding", "30px").panel({
		"width" : "400px",
		"title" : "登录"
	}).parent().css("margin", "20px auto");

	$("#comp,#dept,#empl,#pwd,#pswd,#repswd").css({
		"width" : "100%",
		"height" : "30px",
		"padding" : "12px"
	});
	$("#comp").combotree({
		"onChange" : function(newValue, oldValue) {
			$.ajax({
				url : "login-form",
				data : {
					"companyChanged" : true,
					"id" : newValue
				},
				success : function(data) {
					$("#dept").combotree("loadData", data.departments).combotree({
						"value" : 0
					});
					$("#empl").combobox("loadData", data.employees);
					if (data.employees.length > 0)
						$("#empl").combobox("setValue", data.employees[0].id);
					else
						$("#empl").combobox("clear");
					$("#pwd").textbox("clear");
				}
			});
		}
	});
	$("#dept").combotree({
		"onChange" : function(newValue, oldValue) {
			$.ajax({
				url : "login-form",
				data : {
					"companyChanged" : false,
					"id" : newValue
				},
				success : function(data) {
					$("#empl").combobox("loadData", data.employees);
					if (data.employees.length > 0)
						$("#empl").combobox("setValue", data.employees[0].id);
					else
						$("#empl").combobox("clear");
					$("#pwd").textbox("clear");
				}
			});
		}
	});
	$("#empl").combobox({
		"valueField" : "id",
		"textField" : "name",
		"editable" : false,
		"required" : true,
		"missingMessage" : "必须选择一个用户"
	});
	$("input[type=password]").textbox({
		"prompt" : "Password",
		"iconCls" : "icon-lock",
		"iconWidth" : 38,
	});
	$("#pswd,#repswd").textbox({
		"required" : true,
		"missingMessage" : "密码不能为空"
	});
	$("#pwd").next().children("input:first").on("keyup", function(event) {
		if (event.keyCode == 13)
			$("#login").trigger("click");
	});
	$("#login,#act").css({
		"padding" : "5px 0",
		"width" : "100%"
	}).linkbutton({
		"iconCls" : "icon-ok"
	});
	$("#rit").tooltip({
		"content" : "省市信息中心人员、县区分公司系统管理员"
	});
	$("#rad").css("margin-left", "2em").tooltip({
		"position" : "top",
		"content" : "具有TinyITSM系统管理员权限，必须为信息化人员"
	});
	$("#act_win").css("padding", "20px").window({
		"width" : "400px",
		"height" : "320px",
		"iconCls" : "icon-man",
		"title" : "激活用户",
		"modal" : true,
		"minimizable" : false,
		"maximizable" : false,
		"resizable" : false,
		"collapsible" : false,
		"shadow" : true,
		"closed" : true
	});

	$("#login").on(
			"click",
			function() {
				if (!$("#empl").combobox("isValid")) {
					$.messager.alert("错误", "必须选择一个用户", "warning").window({
						"top" : "280px"
					});
					return;
				}
				$.ajax({
					url : "login",
					data : {
						"companyId" : $("#comp").combotree("getValue"),
						"departmentId" : $("#dept").combotree("getValue"),
						"employeeId" : $("#empl").combobox("getValue"),
						"password" : $("#pwd").textbox("getText")
					},
					success : function(result) {
						switch (result) {
						case 0:
							$.setCookie("usr", "compId=" + $("#comp").combotree("getValue") + "&deptId=" + $("#dept").combotree("getValue") + "&emplId="
									+ $("#empl").combobox("getValue"), 3650);
							window.location.replace("func?code=HOME_PAGE");
							break;
						case 1:
							$("#act_win").window({
								"top" : "180px"
							}).window("hcenter").window("open");
							$("#pswd").textbox("clear");
							$("#repswd").textbox("clear");
							if ($("#empl").combobox("getValue") == 0) {
								$("input[type=checkbox]").prop("disabled", true).prop("checked", false);
								$("#admin").prop("checked", true);
							} else {
								$("input[type=checkbox]").prop("checked", false);
								$("#admin").prop("disabled", true);
							}
							break;
						case 2:
							$.messager.alert('错误', '密码不正确，登录失败！', 'warning').window({
								top : "280px"
							});
							break;
						case 3:
							$.messager.alert('错误', '该员工对应的用户已经被停用，登录失败！', 'warning').window({
								top : "280px"
							});
							break;
						default:
							$.messager.alert("错误", "未知的错误，请联系系统管理员！", "warning").window({
								top : "280px"
							});
						}
					}
				});
			});
	$("#act").on(
			"click",
			function() {
				if (!$("#pswd").textbox("isValid")) {
					$.messager.alert("错误", "请输入至少6位的登录密码", "warning").window({
						"top" : "280px"
					});
					return;
				}
				if (!$("#repswd").textbox("isValid")) {
					$.messager.alert("错误", "两次输入的密码必须一致", "warning").window({
						"top" : "280px"
					});
					return;
				}
				var d = {
					"user.companyId" : $("#comp").combotree("getValue"),
					"user.employeeId" : $("#empl").combobox("getValue"),
					"user.password" : $("#pswd").textbox("getText"),
					"user.iter" : $("#iter").prop("checked"),
					"user.admin" : $("#admin").prop("checked"),
					"user.username" : $("#empl").combobox("getText")
				};
				$.ajax({
					"url" : "user-active",
					"data" : d,
					"success" : function(success) {
						if (success) {
							$.messager.alert(
									"消息",
									"用户激活成功，点击确定直接登录系统。",
									"info",
									function() {
										$.setCookie("usr", "compId=" + $("#comp").combotree("getValue") + "&deptId=" + $("#dept").combotree("getValue")
												+ "&emplId=" + $("#empl").combobox("getValue"), 3650);
										window.location.replace("func?code=HOME_PAGE");
									}).window({
								"top" : "280px",
								"closable" : false
							});
						} else {
							$.messager.alert("错误", "该用户已经激活。", "warn").window({
								"top" : "280px"
							});
						}
					}
				});
			});
	$("#iter").on("change", function() {
		if ($(this).prop("checked"))
			$("#admin").prop("disabled", false);
		else
			$("#admin").prop("disabled", true).prop("checked", false);
	});

	/**
	 * action
	 */
	$.messager.progress({
		title : "请稍候",
		msg : "初始化系统组件......"
	}).window({
		top : "280px"
	});
	$.ajax({
		url : "init",
		data : $.getCookie("usr"),
		success : function(data) {
			$.messager.progress("close");
			if (data.initSuccess) {
				$("#comp").combotree("loadData", data.companies).combotree({
					"value" : data.compId
				});
				$("#dept").combotree("loadData", data.departments).combotree({
					"value" : data.deptId
				});
				$("#empl").combobox("loadData", data.employees);
				if (data.employees.length > 0)
					$("#empl").combobox("setValue", data.emplId);
				$("#pwd").textbox("clear");
				$.messager.alert("提示", "初始化完成，请登录系统。").window({
					"top" : "280px"
				});
			} else {
				$.messager.alert("提示", "初始化失败，系统无法使用，请联系维护人员。").window({
					"top" : "280px"
				});
			}
		}
	});
});