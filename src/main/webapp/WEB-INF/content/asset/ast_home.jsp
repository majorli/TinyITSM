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
	div.TinyLine>span {display:inline-block;width:100px;}
	div.TinyLine>input {width:480px;}
</style>
<script type="text/javascript" charset="UTF-8" src="${pageContext.request.contextPath}/js/ast_home.js"></script>
<title>TinyAsset - 资产管理</title>
</head>
<body>
	<%@ include file="../html/header.jsp"%>
	<div class="Container" id="cc">
		<%@ include file="../html/navi.jsp" %>
		<div id="wpad">
			<div class="easyui-layout" data-options="width:'100%',height:'100%'">
				<div data-options="region:'north',title:'信息化资产管理',height:64,collapsible:false" style="padding:6px;overflow:hidden;">
					<span style="vertical-align:-2px;">硬件类别：</span><select id="hwCatalog" class="easyui-combobox" data-options="editable:false,width:120"></select>
					<span style="padding-left:2px;vertical-align:-2px;">软件类别：</span><select id="swCatalog" class="easyui-combobox" data-options="editable:false,width:120"></select>
					<a id="exportAssets" class="easyui-menubutton" data-options="iconCls:'icon-table',menu:'#exportMenu'">导出Excel</a>
					<a id="editProperties" class="easyui-linkbutton" data-options="iconCls:'icon-table-lightning',plain:true">数据校验</a>
					<a id="editProperties" class="easyui-linkbutton" data-options="iconCls:'icon-table-edit',plain:true">编辑属性</a>
					<a id="changeState" class="easyui-linkbutton" data-options="iconCls:'icon-vcard-edit',plain:true">调整状态</a>
					<a id="appendAsset" class="easyui-linkbutton" data-options="iconCls:'icon-vcard-add',plain:true">新增资产</a>
				</div>
				<div id="exportMenu">
					<div id="_all">导出全部IT资产</div>
					<div id="_hard">导出硬件类资产</div>
					<div id="_soft">导出软件类资产</div>
				</div>
				<div data-options="region:'center'">
					<div id="tabs" class="easyui-tabs" data-options="fit:true,border:false">
						<div title="硬件类资产" style="padding-top:1px"><table id="hardware"></table></div>
						<div title="软件类资产" style="padding:1px"><table id="software"></table></div>
						<div title="资产校验表" style="padding:1px"></div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div id="aProps" class="easyui-dialog" data-options="closed:true,width:650,height:380,top:220,modal:true,title:'资产属性',iconCls:'icon-table-edit'" style="padding:1em;">
		<h5 style="color:red">提示：编辑多项资产时，不统一的属性不显示原值，提交空值保持原值不变，提交空格清除原值。</h5>
		<div id="p_code" class="TinyLine HW"><span>资产编号：</span><input id="v_code"  class="easyui-textbox" data-options="validType:'length[0,32]',height:24" /></div>
		<div id="p_financialCode" class="TinyLine HW"><span>财务资产编号：</span><input id="v_financialCode"  class="easyui-textbox" data-options="validType:'length[0,32]',height:24" /></div>
		<div id="p_name" class="TinyLine"><span>名称：</span><input id="v_name"  class="easyui-textbox" data-options="validType:'length[0,32]',height:24" /></div>
		<div id="p_vendor" class="TinyLine"><span>厂商/制造商：</span><input id="v_vendor"  class="easyui-textbox" data-options="validType:'length[0,32]',height:24" /></div>
		<div id="p_modelOrVersion" class="TinyLine"><span>版本/型号：</span><input id="v_modelOrVersion"  class="easyui-textbox" data-options="validType:'length[0,64]',height:24" /></div>
		<div id="p_assetUsage" class="TinyLine"><span>用途及基本功能：</span><input id="v_assetUsage"  class="easyui-textbox" data-options="validType:'length[0,255]',height:24" /></div>
		<div id="p_sn" class="TinyLine HW"><span>序列号：</span><input id="v_sn"  class="easyui-textbox" data-options="validType:'length[0,64]',height:24" /></div>
		<div id="p_configuration" class="TinyLine HW"><span>主要配置：</span><input id="v_configuration"  class="easyui-textbox" data-options="validType:'length[0,255]',height:24" /></div>
		<div id="p_purchaseTime" class="TinyLine"><span>采购时间：</span><input id="v_purchaseTime" type="text" class="easyui-datebox" data-options="height:24" /></div>
		<div id="p_quantity" class="TinyLine"><span>数量：</span><input id="v_quantity" class="easyui-numberspinner" data-options="min:1,height:24" /></div>
		<div id="p_cost" class="TinyLine"><span>原值：</span><input id="v_cost" class="easyui-numberspinner" data-options="min:0,precision:2,groupSeparator:',',prefix:'￥',height:24" /></div>
		<div id="p_warranty" class="TinyLine HW"><span>保修状态：</span><input id="v_warranty" class="easyui-combobox" data-options="valueField:'id',textField:'text',data:[{id:0,text:'在保'},{id:1,text:'续保'},{id:-1,text:'过保'}],height:24" /></div>
		<div id="p_location" class="TinyLine HW"><span>物理位置：</span><input id="v_location"  class="easyui-textbox" data-options="validType:'length[0,255]',height:24" /></div>
		<div id="p_ip" class="TinyLine HW"><span>网络地址：</span><input id="v_ip" class="easyui-textbox" data-options="validType:'length[0,64]',height:24" /></div>
		<div id="p_importance" class="TinyLine HW"><span>重要程度：</span><input id="v_importance" class="easyui-combobox" data-options="valueField:'id',textField:'text',data:[{id:0,text:'普通'},{id:1,text:'重要'},{id:2,text:'关键'}],height:24" /></div>
		<div id="p_softwareType" class="TinyLine SW"><span>软件类型：</span><input id="v_softwareType" class="easyui-combobox" data-options="valueField:'id',textField:'text',data:[{id:0,text:'商品软件'},{id:1,text:'自由/开源软件'},{id:2,text:'免费软件'},{id:3,text:'试用软件'},{id:4,text:'定制开发软件'},{id:5,text:'自主研发软件'},{id:6,text:'其他类型软件'}],height:24" /></div>
		<div id="p_license" class="TinyLine SW"><span>许可证：</span><input id="v_license" class="easyui-textbox" data-options="validType:'length[0,64]',height:24" /></div>
		<div id="p_expiredTime" class="TinyLine SW"><span>许可期限：</span><input id="v_expiredTime" type="text" class="easyui-datebox" data-options="height:24" /></div>
		<div id="p_comment" class="TinyLine"><span>备注：</span><input id="v_comment" class="easyui-textbox" data-options="validType:'length[0,255]',height:24" /></div>
	</div>
	<iframe name="dlerr" style="display:none"></iframe>
	<form id="exportForm" action="asset/export-assets" method="post" target="dlerr"><input type="hidden" id="exportType" name="type"/></form>
	<%@ include file="../html/footer.jsp"%>
</body>
</html>