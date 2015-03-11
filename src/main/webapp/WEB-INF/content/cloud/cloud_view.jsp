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
<script type="text/javascript" charset="UTF-8" src="${pageContext.request.contextPath}/js/cloud_view.js"></script>
<title>TinyCloud - IT资料库</title>
</head>
<body>
	<%@ include file="../html/header.jsp"%>
	<div class="Container" id="cc">
		<%@ include file="../html/navi.jsp" %>
		<div id="wpad">
			<div class="easyui-layout" data-options="width:'100%',height:'100%'">
				<div data-options="region:'west',title:'资料类别',width:200,split:true" style="padding:5px;">
					<!-- 目录树 -->
					<ul id="cata" class="easyui-tree" data-options="url:''"></ul>
				</div>
				<div data-options="region:'center',title:'资料分类视图'">
					<!-- 资料列表 -->
					<table id="files" class="easyui-datagrid"></table>
				</div>
			</div>
		</div>
	</div>
	<iframe name="dlerr" style="display:none"></iframe>
	<form id="dl_file" action="cloud/download" method="post" target="dlerr"><input type="hidden" id="dl_file_id" name="id"/></form>
	<form id="dl_multi" action="cloud/download-multi" method="post" target="dlerr"><input type="hidden" id="dl_multi_ids" name="ids"/></form>
	<%@ include file="../html/footer.jsp"%>
</body>
</html>