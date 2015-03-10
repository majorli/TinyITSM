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
<style type="text/css">
	dl#filesList {height: 282px; overflow: auto;}
	#fileUpl dd {margin: 4px 0; padding: 2px;}
	#fileUpl a.easyui-linkbutton {padding: 0 2px;}
	div.fnc {display: inline-block; width:398px; border-bottom: 1px dotted #C0C0C0; padding: 4px 0; text-overflow: ellipsis; white-space: nowrap; overflow:hidden; vertical-align: -8px; cursor: default;}
	_::-moz-svg-foreign-content, :root div.fnc {vertical-align: 0;}
	div.fcc, div.frc {display: inline-block; margin: 0 3px;}
	/* div.fic {display: none;} */
	div.fic {opacity: 0; width: 0; height: 0; display: inline-block; position: absolute; left: 0;}
</style>
<script type="text/javascript" charset="UTF-8" src="${pageContext.request.contextPath}/js/cloud_private.js"></script>
<title>TinyCloud - IT资料库</title>
</head>
<body>
	<%@ include file="../html/header.jsp"%>
	<div class="Container" id="cc">
		<%@ include file="../html/navi.jsp" %>
		<div id="wpad">
			<div class="easyui-layout" data-options="height:'100%'">
				<div id="toolbar" data-options="region:'north',collapsible:false,title:'我的资料库'" style="padding:5px;height:65px;">
					<!-- 工具栏 -->
					<a id="refresh" class="easyui-linkbutton" data-options="iconCls:'icon-refresh',plain:true">刷新</a>
					<a id="createList" class="easyui-linkbutton act" data-options="iconCls:'icon-tlb-new-list',plain:true">创建栏目</a>
					<a id="upload" class="easyui-linkbutton act" data-options="iconCls:'icon-tlb-upload',plain:true">上传</a>
					<a id="download" class="easyui-linkbutton act" data-options="iconCls:'icon-tlb-download',plain:true">下载</a>
					<a id="delete" class="easyui-linkbutton act" data-options="iconCls:'icon-tlb-delete',plain:true">删除</a>
					<a id="move" class="easyui-linkbutton act" data-options="iconCls:'icon-tlb-move',plain:true">移动</a>
					<a id="rename" class="easyui-linkbutton act" data-options="iconCls:'icon-tlb-rename',plain:true">重命名</a>
					<a id="brief" class="easyui-linkbutton act" data-options="iconCls:'icon-tlb-info',plain:true">版本摘要</a>
					<a id="tag" class="easyui-linkbutton act" data-options="iconCls:'icon-tlb-tag',plain:true">标签</a>
					<a id="permission" class="easyui-linkbutton act" data-options="iconCls:'icon-tlb-permission',plain:true">权限</a>
					<a id="push" class="easyui-linkbutton act" data-options="iconCls:'icon-tlb-push',plain:true">推送</a>
					<a id="refuse" class="easyui-linkbutton act" data-options="iconCls:'icon-tlb-refuse',plain:true">拒绝</a>
					<a id="favor" class="easyui-linkbutton act" data-options="iconCls:'icon-tlb-favor',plain:true">收藏</a>
					<a id="rss" class="easyui-linkbutton act" data-options="iconCls:'icon-tlb-rss',plain:true">订阅</a>
				</div>
				<div id="props" data-options="region:'east',split:true,width:300,title:'资料属性'">
					<!-- 属性网格 -->
					<table id="properties" class="easyui-propertygrid" style="width:100%;height:100%;" data-options="showGroup:true,scrollbarSize:0,border:false"></table>
				</div>
				<div id="kb" data-options="region:'center',title:'资料目录'" style="padding:.5em;">
					<!-- 资料树 -->
					<ul id="doctree" class="easyui-tree"></ul>
				</div>
			</div>
		</div>
	</div>
	<div id="opt" class="easyui-dialog" data-options="closed:true,width:580,height:405,top:220,modal:true,title:'无标题'" style="padding:1em;overflow:hidden;">
		<h5 id="opt_ttl"></h5>
		<div id="opt_name_div" class="TinyLine">
			<span>名称：<input id="opt_name" class="easyui-textbox" data-options="height:24,width:506,required:true,missingMessage:'名称不能为空'" /></span>
		</div>
		<div id="opt_tags_div" class="TinyLine">
			<span>标签：<input id="opt_tags" class="easyui-textbox" data-options="height:24,width:506" /></span>
		</div>
		<div id="opt_perm_div" class="TinyLine">
			<span>权限：<input id="opt_perm" class="easyui-combobox" data-options="height:24,width:506,editable:false" /></span>
		</div>
		<div id="opt_user_div" class="easyui-panel" data-options="width:542,height:198,title:'有访问权限的用户'" style="padding:.5em;">
			<ul id="opt_user" class="easyui-tree" data-options="fit:true"></ul>
		</div>
	</div>
	<div id="upl" class="easyui-dialog" data-options="closed:true,width:580,height:405,top:220,modal:true,title:'文件上传',iconCls:'icon-cloud'" style="padding:1em; overflow:hidden">
		<h5>目标栏目：[<span id="listName"></span>]</h5>
		<form id="fileUpl" action="cloud/upload" method="post" enctype="multipart/form-data">
			<input type="hidden" id="lid" name="listId" />
			<dl id="filesList"></dl>
		</form>
	</div>
	<div id="fav" class="easyui-dialog" data-options="closed:true,width:480,height:160,top:220,modal:true,title:'资料收藏',iconCls:'icon-root-favorites'" style="padding:1em; overflow:hidden">
		<h5>选择收藏夹：</h5>
		<input id="fl" class="easyui-combobox" data-options="width:'100%',height:24,valueField:'nodeId',textField:'text',editable:false,required:true" />
	</div>
	<div id="ver" class="easyui-dialog" data-options="closed:true,width:580,height:300,top:220,modal:true,title:'资料版本与摘要',iconCls:'icon-information'" style="padding:1em; overflow:hidden">
		<div id="ver_ver_div" class="TinyLine">
			<span>版本：
				<input id="ver_major" class="easyui-numberspinner" data-options="height:24,width:100,min:0,max:127" />
				.
				<input id="ver_minor" class="easyui-numberbox" data-options="height:24,width:100,min:0.00,max:99.99,precision:2" />
				-
				<input id="ver_type" class="easyui-combobox" data-options="height:24,width:100" />
			</span>
		</div>
		<div class="TinyLine">
			<span>摘要：</span>
		</div>
		<div class="ver_brf_div" id="TinyLine">
			<input id="ver_brf" class="easyui-textbox" data-options="width:'100%',height:132,multiline:true,prompt:'输入资料摘要，不超过512个字'" />
		</div>
		<div class="TinyLine">
			<p style="margin:.5em;">剩余字数：<span id="brf_rest">512</span></p>
		</div>
	</div>
	<iframe name="dlerr" style="display:none"></iframe>
	<form id="dl_file" action="cloud/download" method="post" target="dlerr"><input type="hidden" id="dl_file_id" name="id"/></form>
	<form id="dl_list" action="cloud/download-list" method="post" target="dlerr"><input type="hidden" id="dl_list_id" name="id"/><input type="hidden" id="dl_list_type" name="type"/></form>
	<%@ include file="../html/footer.jsp"%>
</body>
</html>