<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Cache-Control" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<link rel="shortcut icon" href="${pageContext.request.contextPath}/favicon.ico" />
<script type="text/javascript" charset="UTF-8" src="${pageContext.request.contextPath}/jquery/jquery.min.js"></script>
<script type="text/javascript" charset="UTF-8" src="${pageContext.request.contextPath}/jquery/jquery.easyui.min.js"></script>
<script type="text/javascript" charset="UTF-8" src="${pageContext.request.contextPath}/jquery/locale/easyui-lang-zh_CN.js"></script>
<link id="easyuiTheme" rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/jquery/themes/<c:out value="${cookie.theme.value}" default="default"/>/easyui.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/jquery/themes/icon.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/ext-icon.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/itsm.css" />
<script type="text/javascript" charset="UTF-8" src="${pageContext.request.contextPath}/js/user-setting.js"></script>
<title>TinyITSM - 用户设置</title>
</head>
<body>
	<%@ include file="../html/header.jsp"%>
	<div class="Container" id="cc">
		<%@ include file="../html/navi.jsp"%>
		<div id="wpad">
			<div id="props">
				<input type="hidden" id="rname" value="${sessionScope.userInfo.employee.name}" />
				<input type="hidden" id="uid" value="${sessionScope.userInfo.user.id}" />
				<h4>修改用户名</h4>
				<div class="TinyLine">
					<form action="portal/rename" method="post" id="renameForm">
						用户名：<input id="username" name="newUsn" type="text" value="${sessionScope.userInfo.user.username}" />
					</form>
				</div>
				<div class="Line" style="padding-left: 4em;">
					<a href="javascript:void(0)" id="rename">确定</a>
					<a href="javascript:void(0)" id="realname">实名</a>
				</div>
				<h4>修改密码</h4>
				<div class="TinyLine">
					旧密码：<input id="oldPassword" type="password" />
				</div>
				<div class="TinyLine">
					新密码：<input id="newPassword" type="password" />
				</div>
				<div class="TinyLine">
					确&emsp;认：<input id="rePassword" type="password" />
				</div>
				<div class="Line" style="padding-left: 4em;">
					<a href="javascript:void(0)" id="changePassword">确定</a>
					<a href="javascript:void(0)" id="clear">重填</a>
				</div>
			</div>
		</div>
	</div>
	<%@ include file="../html/footer.jsp"%>
</body>
</html>