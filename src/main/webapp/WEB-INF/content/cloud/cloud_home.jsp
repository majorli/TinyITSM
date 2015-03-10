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
<script type="text/javascript" charset="UTF-8" src="${pageContext.request.contextPath}/js/cloud_home.js"></script>
<title>TinyCloud - 资料库首页</title>
</head>
<body>
	<%@ include file="../html/header.jsp"%>
	<div class="Container" id="cc">
		<%@ include file="../html/navi.jsp" %>
		<div id="wpad">
			<div class="easyui-layout" data-options="height:'100%'">
				<div data-options="region:'east',title:'资料库TOP10',split:true" style="width:700px">
					<div class="easyui-layout" data-options="height:'100%'">
						<div data-options="region:'north',height:36" style="overflow:hidden;padding:4px;">
							<a id="refresh" class="easyui-linkbutton" data-options="iconCls:'icon-refresh',plain:true">刷新排名</a>
							<a id="download" class="easyui-linkbutton" data-options="iconCls:'icon-tlb-download',plain:true">下载</a>
							<a id="subscribe" class="easyui-linkbutton" data-options="iconCls:'icon-tlb-rss',plain:true">订阅</a>
							<a id="favorite" class="easyui-linkbutton" data-options="iconCls:'icon-tlb-favor',plain:true">收藏</a>
						</div>
						<div data-options="region:'center'" style="padding:4px;">
							<ul id="tops" class="easyui-tree"></ul>
						</div>
					</div>
				</div>
				<div data-options="region:'center',title:'我的订阅消息'">
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
	<div id="fav" class="easyui-dialog" data-options="closed:true,width:480,height:160,top:220,modal:true,title:'资料收藏',iconCls:'icon-root-favorites'" style="padding:1em; overflow:hidden">
		<h5>选择收藏夹：</h5>
		<input id="fl" class="easyui-combobox" data-options="width:'100%',height:24,valueField:'nodeId',textField:'text',editable:false,required:true" />
	</div>
	<iframe name="dlerr" style="display:none"></iframe>
	<form id="dl_file" action="cloud/download" method="post" target="dlerr"><input type="hidden" id="dl_file_id" name="id"/></form>
	<form id="dl_list" action="cloud/download-list" method="post" target="dlerr"><input type="hidden" id="dl_list_id" name="id"/><input type="hidden" id="dl_list_type" name="type"/></form>
	<%@ include file="../html/footer.jsp"%>
</body>
</html>