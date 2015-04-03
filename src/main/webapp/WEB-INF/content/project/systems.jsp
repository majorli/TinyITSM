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
<script type="text/javascript" charset="UTF-8" src="${pageContext.request.contextPath}/js/systems.js"></script>
<title>TinyProject - 信息系统管理</title>
</head>
<body>
	<%@ include file="../html/header.jsp"%>
	<div class="Container" id="cc">
		<%@ include file="../html/navi.jsp" %>
		<div id="wpad">
			<div class="easyui-layout" data-options="width:'100%',height:'100%'">
				<div data-options="region:'north',title:'信息系统',height:64,collapsible:false" style="padding:6px;overflow:hidden;">
					<a id="refresh" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新</a>
					<a id="append" class="easyui-linkbutton" data-options="iconCls:'icon-vcard-add',plain:true">新增系统</a>
				</div>
				<div data-options="region:'center'"><table id="systems" class="easyui-datagrid"></table></div>
			</div>
		</div>
	</div>
	<%@ include file="../html/footer.jsp"%>
</body>
</html>