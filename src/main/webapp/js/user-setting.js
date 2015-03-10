$(function() {
	/**
	 * initialize page parameters and functions
	 */
	$("#breadcrumbs").html("<a href=\"javascript:window.location.replace('func?code=HOME_PAGE')\">首页</a>&nbsp;&#187;&nbsp;用户设置");
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
	$.initNavi("user-setting");

	/**
	 * initialize wpad
	 */
	$("#props").css({
		"padding" : "1em 2em"
	}).panel({
		"height" : "100%",
		"title" : "用户设置 [修改用户属性，变更登录密码，设置用户标签]"
	});
	$("#username,#oldPassword,#newPassword,#rePassword").css({
		"width" : "300px",
		"height" : "30px",
		"padding" : "12px"
	});
	$("#oldPassword,#newPassword,#rePassword").textbox({
		"required" : true,
		"prompt" : "Password",
		"iconCls" : "icon-lock",
		"iconWidth" : 38,
		"missingMessage" : "密码不能为空",
		"onChange" : function(newValue, oldValue) {
			$("#resetAdminPassword").linkbutton("enable");
		}
	});
	$("#username").textbox({
		"required" : true,
		"missingMessage" : "用户名不能为空",
		"prompt" : "用户名",
		"iconCls" : "icon-man",
		"iconWidth" : 38,
	});
	$("#rename, #changePassword").css("padding", "2px 8px").linkbutton({
		"iconCls" : "icon-saved"
	});
	$("#clear").css("padding", "2px 8px").linkbutton({
		"iconCls" : "icon-multiple"
	});
	$("#realname").css("padding", "2px 8px").linkbutton({
		"iconCls" : "icon-employee"
	});
	$("#realname").on("click", function() {
		$("#username").textbox("setValue", $("#rname").val());
		$("#renameForm").submit();
	});
	$("#rename").on("click", function() {
		if (!$("#username").textbox("isValid")) {
			$.msgbox("错误", "用户名不能为空", "warning");
			return;
		}
		$("#renameForm").submit();
	});
	$("#clear").on("click", function() {
		$("#oldPassword,#newPassword,#rePassword").textbox({
			"value" : ""
		});
	});
	$("#changePassword").on("click", function() {
		if (!$("#oldPassword").textbox("isValid")) {
			$.msgbox("错误", "请输入至少6位的旧密码", "warning");
			return;
		}
		if (!$("#newPassword").textbox("isValid")) {
			$.msgbox("错误", "请输入至少6位的新密码", "warning");
			return;
		}
		if (!$("#rePassword").textbox("isValid")) {
			$.msgbox("错误", "两次输入的新密码必须一致", "warning");
			return;
		}
		$.ajax({
			"url" : "portal/change-pwd",
			"data" : {
				"userId" : $("#uid").val(),
				"oldPassword" : $("#oldPassword").textbox("getText"),
				"newPassword" : $("#newPassword").textbox("getText")
			},
			"success" : function(data) {
				if (data == true) {
					$.msgbox("消息", "密码变更成功", "info");
					$("#oldPassword,#newPassword,#rePassword").textbox({
						"value" : ""
					});
				} else {
					$.msgbox("错误", "密码变更失败，请输入正确的旧密码", "warning");
				}
			}
		});

	});
});