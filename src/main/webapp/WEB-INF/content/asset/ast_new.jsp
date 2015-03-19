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
<script type="text/javascript" charset="UTF-8" src="${pageContext.request.contextPath}/js/ast_new.js"></script>
<style type="text/css">
	form#newAssets div.MidLine>span {display:inline-block;width:100px;}
</style>
<title>TinyAsset - 新增资产</title>
</head>
<body>
	<%@ include file="../html/header.jsp"%>
	<div class="Container" id="cc">
		<%@ include file="../html/navi.jsp" %>
		<div id="wpad">
			<div class="easyui-panel" data-options="title:'新增信息化资产',width:'100%',height:'100%'" style="padding:2em;">
				<form id="newAssets" action="" method="post">
					<div id="p_type" class="MidLine"><span>资产类型：</span><input id="v_type" class="easyui-combobox" name="type" data-options="data:[{'id':1,'text':'硬件类资产'},{'id':3,'text':'计算机软件'}],valueField:'id',textField:'text',editable:false" /></div>
					<div id="p_catalog" class="MidLine"><span>资产类别：</span><input id="v_catalog" class="easyui-combobox" name="catalog" data-options="valueField:'id',textField:'text',editable:false" /></div>
					<div id="p_code" class="MidLine HW"><span>资产编号：</span><input id="v_code" class="easyui-textbox" name="code" data-options="prompt:'请输入资产编号',validType:'length[0,32]'" /></div>
					<div id="p_financialCode" class="MidLine HW"><span>财务资产编号：</span><input id="v_financialCode" class="easyui-textbox" name="financialCode" data-options="prompt:'请输入财务固定资产编号',validType:'length[0,32]'" /></div>
					<div id="p_name" class="MidLine"><span>资产名称：</span><input id="v_name" class="easyui-textbox" name="name" data-options="prompt:'请输入资产名称',validType:'length[0,32]'" /></div>
					<div id="p_vendor" class="MidLine"><span>厂商/制造商：</span><input id="v_vendor" class="easyui-textbox" name="vendor" data-options="prompt:'请输入资产的厂商或制造商名称',validType:'length[0,32]'" /></div>
					<div id="p_modelOrVersion" class="MidLine"><span>版本/型号：</span><input id="v_modelOrVersion" class="easyui-textbox" name="modelOrVersion" data-options="prompt:'请输入资产的版本或型号',validType:'length[0,64]'" /></div>
					<div id="p_assetUsage" class="MidLine"><span>用途及基本功能：</span><input id="v_assetUsage" class="easyui-textbox" name="assetUsage" data-options="prompt:'请输入资产的用途及基本功能',validType:'length[0,255]'" /></div>
					<div id="p_sn" class="MidLine HW"><span>序列号：</span><input id="v_sn" class="easyui-textbox" name="sn" data-options="prompt:'请输入序列号',validType:'length[0,64]'" /></div>
					<div id="p_configuration" class="MidLine HW"><span>主要配置：</span><input id="v_configuration" class="easyui-textbox" name="configuration" data-options="prompt:'请输入主要配置信息',validType:'length[0,255]'" /></div>
					<div id="p_" class="MidLine"><span>：</span><input id="v_" class="easyui-" name="" data-options="" /></div>
					<div id="p_" class="MidLine"><span>：</span><input id="v_" class="easyui-" name="" data-options="" /></div>
					<div id="p_" class="MidLine"><span>：</span><input id="v_" class="easyui-" name="" data-options="" /></div>
					<div id="p_" class="MidLine"><span>：</span><input id="v_" class="easyui-" name="" data-options="" /></div>
					<div id="p_" class="MidLine"><span>：</span><input id="v_" class="easyui-" name="" data-options="" /></div>
					<div id="p_" class="MidLine"><span>：</span><input id="v_" class="easyui-" name="" data-options="" /></div>
					<div id="p_" class="MidLine"><span>：</span><input id="v_" class="easyui-" name="" data-options="" /></div>
					<div id="p_" class="MidLine"><span>：</span><input id="v_" class="easyui-" name="" data-options="" /></div>
					<div id="p_" class="MidLine"><span>：</span><input id="v_" class="easyui-" name="" data-options="" /></div>
					<div id="p_" class="MidLine"><span>：</span><input id="v_" class="easyui-" name="" data-options="" /></div>
					<div id="p_" class="MidLine"><span>：</span><input id="v_" class="easyui-" name="" data-options="" /></div>
					<div id="p_" class="MidLine"><span>：</span><input id="v_" class="easyui-" name="" data-options="" /></div>
					<div id="p_" class="MidLine"><span>：</span><input id="v_" class="easyui-" name="" data-options="" /></div>
					<div id="p_" class="MidLine"><span>：</span><input id="v_" class="easyui-" name="" data-options="" /></div>
					<div id="p_" class="MidLine"><span>：</span><input id="v_" class="easyui-" name="" data-options="" /></div>
					<div id="p_" class="MidLine"><span>：</span><input id="v_" class="easyui-" name="" data-options="" /></div>
					<div id="p_" class="MidLine"><span>：</span><input id="v_" class="easyui-" name="" data-options="" /></div>
				</form>
			</div>
		</div>
	</div>
	<%@ include file="../html/footer.jsp"%>
</body>
</html>