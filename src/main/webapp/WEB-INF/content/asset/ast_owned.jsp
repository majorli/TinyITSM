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
<script type="text/javascript" charset="UTF-8" src="${pageContext.request.contextPath}/js/ast_owned.js"></script>
<title>TinyAsset - 我的设备</title>
</head>
<body>
	<%@ include file="../html/header.jsp"%>
	<div class="Container" id="cc">
		<%@ include file="../html/navi.jsp" %>
		<div id="wpad">
			<div class="easyui-layout" data-options="width:'100%',height:'100%'">
				<div id="cmd" data-options="region:'north',title:'我的信息化设备',height:64,collapsible:false" style="padding:6px;overflow:hidden;">
					<a id="refresh" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true">刷新列表</a>
					<a id="1" class="easyui-linkbutton Apply" data-options="iconCls:'icon-wrench',plain:true">维修服务</a>
					<a id="2" class="easyui-linkbutton Apply" data-options="iconCls:'icon-wrench-orange',plain:true">升级服务</a>
					<a id="3" class="easyui-linkbutton Apply" data-options="iconCls:'icon-printer',plain:true">配件服务</a>
					<a id="0" class="easyui-linkbutton Apply" data-options="iconCls:'icon-phone-sound',plain:true">其他服务...</a>
				</div>
				<div data-options="region:'center'"><table id="myEquipments"></table></div>
			</div>
		</div>
	</div>
	<%@ include file="../html/footer.jsp"%>
</body>
</html>