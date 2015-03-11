<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

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
<script type="text/javascript" charset="UTF-8" src="${pageContext.request.contextPath}/js/fts.js"></script>
<title>TinyITSM - 全文检索</title>
</head>
<body>
	<%@ include file="../html/header.jsp"%>
	<div class="Container" id="cc">
		<%@ include file="../html/navi.jsp" %>
		<div id="wpad">
			<input type="hidden" id="fts_c" value='<s:property value="catalog" />' /> /><input type="hidden" id="fts_k" value='<s:property value="keyword" />' />
			<div id="results" class="easyui-panel" data-options="title:'全文检索结果: 关键字=\'<s:property value="keyword" />\', 共检索到<s:property value="count" />条记录',height:'100%',iconCls:'icon-search'" style="padding:1em 2em;">
				<s:if test="count==0">
					<h4>抱歉：没有搜索到任何结果</h4>
				</s:if>
				<s:else>
					<s:iterator value="results" id="r">
						<h4>在<s:property value="#r.key" />中搜索到<s:property value="#r.value.size" />条结果：</h4>
						<s:iterator value="#r.value" id="t">
							<p style="padding-left:.5em;">
								<input type="hidden" value="<s:property value="#t.id" />" />
								<s:if test="#t.type==201">[硬件]&nbsp;<span><s:property value="#t.title"/></span>&emsp;<a class="ShowHardware" href="javascript:void(0);">查看</a></s:if>
								<s:elseif test="#t.type==202">[软件]&nbsp;<span><s:property value="#t.title"/></span>&emsp;<a class="ShowSoftware" href="javascript:void(0);">查看</a></s:elseif>
								<s:elseif test="#t.type==501">[资料]&nbsp;<span><s:property value="#t.title" /></span>&emsp;<a class="FileBrief" href="javascript:void(0);">查看</a>&emsp;<a class="Favorite" href="javascript:void(0);">收藏</a>&emsp;<a class="FileDownload" href="javascript:void(0);">下载</a></s:elseif>
								<s:elseif test="#t.type==502">[栏目]&nbsp;<span><s:property value="#t.title" /></span>&emsp;<a class="ListFiles" href="javascript:void(0);">查看</a>&emsp;<a class="ListSubscribe" href="javascript:void(0);">订阅</a>&emsp;<a class="ListDownload" href="javascript:void(0);">下载</a></s:elseif>
							</p>
						</s:iterator>
					</s:iterator>
				</s:else>
			</div>
		</div>
	</div>
	<input type="hidden" id="temp" />
	<div id="fav" class="easyui-dialog" data-options="closed:true,width:480,height:160,top:220,modal:true,title:'资料收藏',iconCls:'icon-root-favorites'" style="padding:1em; overflow:hidden">
		<h5>选择收藏夹：</h5>
		<input id="fl" class="easyui-combobox" data-options="width:'100%',height:24,valueField:'nodeId',textField:'text',editable:false,required:true,url:'cloud/load-children?nodeId=-2&nodeType=-2',missingMessage:'必须选择一个收藏夹'" />
	</div>
	<iframe name="dlerr" style="display:none"></iframe>
	<form id="dl_file" action="cloud/download" method="post" target="dlerr"><input type="hidden" id="dl_file_id" name="id"/></form>
	<form id="dl_list" action="cloud/download-list" method="post" target="dlerr"><input type="hidden" id="dl_list_id" name="id"/><input type="hidden" id="dl_list_type" name="type"/></form>
	<%@ include file="../html/footer.jsp"%>
</body>
</html>