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
<script type="text/javascript" charset="UTF-8" src="${pageContext.request.contextPath}/js/home.js"></script>
<title>TinyITSM - 首页</title>
</head>
<body>
	<%@ include file="../html/header.jsp"%>
	<div class="Container" id="cc">
		<%@ include file="../html/navi.jsp" %>
		<div id="wpad">
			<div class="easyui-layout" data-options="height:'100%'">
				<div id="memo" data-options="region:'east',title:'备忘录'" style="width:250px;padding:4px;">
					<div class="easyui-layout" data-options="height:'100%'">
						<div data-options="region:'north',border:false,collapse:false,height:272">
							<div id="memoCalendar" class="easyui-calendar" style="width:240px;height:240px;"></div>
							<h4><span id="dt"></span>备忘：</h4>
						</div>
						<div data-options="region:'center',border:false">
							<input type="hidden" id="mmid" />
							<input id="mm" class="easyui-textbox" />
						</div>
						<div data-options="region:'south',border:false,collapse:false,height:30">
							<p style="margin:.5em;">剩余字数：<span id="mmrest">255</span>，Ctrl-Enter提交保存</p>
						</div>
					</div>
				</div>
				<div id="notifications" data-options="region:'center',title:'系统公告'">
					<div class="easyui-layout" data-options="height:'100%'">
						<div data-options="region:'north',border:false,collapse:false,height:40" style="overflow:hidden;">
							<h5 style="margin:7px;"><a id="refrNoti" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-rotate'">加载最新</a></h5>
						</div>
						<div data-options="region:'center',border:false,collapse:false" style="overflow:auto;">
							<dl id="noti" class="Notes"></dl>
						</div>
						<div data-options="region:'south',border:false,collapse:false,height:40" style="overflow:hidden;">
							<h5 style="margin:7px;"><a id="moreNoti" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-go-down'">加载更多</a></h5>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<%@ include file="../html/footer.jsp"%>
</body>
</html>