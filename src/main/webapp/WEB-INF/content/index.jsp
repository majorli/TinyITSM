<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>

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
<script type="text/javascript" charset="UTF-8" src="${pageContext.request.contextPath}/js/index.js"></script>
<title>欢迎</title>
</head>
<body>
	<%@ include file="html/header.jsp"%>
	<div class="Container" id="cc" style="text-align: center;">
		<h4>欢迎使用TinyITSM。建议使用较新版本的Firefox，Chrome，Safari浏览器或IE9+，本系统恕不支持IE8及以下版本。</h4>
		<div id="lp">
			<h5>首次登录无需密码，直接点击登录以激活用户。</h5>
			<div class="Line">
				<div>公司：</div>
				<input id="comp" />
			</div>
			<div class="Line">
				<div>部门：</div>
				<input id="dept" />
			</div>
			<div class="Line">
				<div>人员：</div>
				<input id="empl" />
			</div>
			<div class="Line">
				<div>密码：</div>
				<input id="pwd" type="password" />
			</div>
			<div>
				<a id="login" href="javascript:void(0);"><span>登录</span></a>
			</div>
		</div>
		<div id="act_win">
			<h5>用户首次登录，请设置密码和类型以激活用户。</h5>
			<div class="Line">
				<div>请设置密码：</div>
				<input id="pswd" type="password" validType="minLength[6]" />
			</div>
			<div class="Line">
				<div>请确认密码：</div>
				<input id="repswd" type="password" validType="equals['#pswd']" />
			</div>
			<div class="Line">
				<label id="rit"><input id="iter" type="checkbox" />信息化人员</label><label id="rad"><input id="admin" type="checkbox" />管理员权限</label>
			</div>
			<div>
				<a id="act" href="javascript:void(0);"><span>提交</span></a>
			</div>
		</div>
	</div>
	<%@ include file="html/footer.jsp"%>
</body>
</html>