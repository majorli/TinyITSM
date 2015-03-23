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
	form#newAssets label {display:inline-block;width:100px;}
	form#newAssets label.R {margin-left:3em;}
	div.MidLine>input {width:320px;}
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
					<div class="MidLine">
						<label>资产类型：</label><input id="v_type" class="easyui-combobox" data-options="data:[{'id':1,'text':'硬件类资产'},{'id':3,'text':'计算机软件'}],valueField:'id',textField:'text',editable:false" />
						<label class="R">资产类别：</label><input id="v_catalog" class="easyui-combobox" data-options="valueField:'id',textField:'text',editable:false,required:true,missingMessage:'资产类别必须选择'" />
					</div>
					<div class="MidLine HW">
						<label>资产编号：</label><input id="v_code" class="easyui-textbox" data-options="prompt:'请输入资产编号',validType:'length[0,32]'" />
						<label class="R">财务资产编号：</label><input id="v_financialCode" class="easyui-textbox" data-options="prompt:'请输入财务固定资产编号',validType:'length[0,32]'" />
					</div>
					<div class="MidLine">
						<label>资产名称：</label><input id="v_name" class="easyui-textbox" data-options="prompt:'请输入资产名称',validType:'length[0,32]'" />
						<label class="R">厂商/制造商：</label><input id="v_vendor" class="easyui-textbox" data-options="prompt:'请输入资产的厂商或制造商名称',validType:'length[0,32]'" />
					</div>
					<div class="MidLine">
						<label>版本/型号：</label><input id="v_modelOrVersion" class="easyui-textbox" data-options="prompt:'请输入资产的版本或型号',validType:'length[0,64]'" />
						<label class="R">用途及基本功能：</label><input id="v_assetUsage" class="easyui-textbox" data-options="prompt:'请输入资产的用途及基本功能',validType:'length[0,255]'" />
					</div>
					<div class="MidLine HW">
						<label>序列号：</label><input id="v_sn" class="easyui-textbox" data-options="prompt:'请输入序列号',validType:'length[0,64]'" />
						<label class="R">主要配置：</label><input id="v_configuration" class="easyui-textbox" data-options="prompt:'请输入主要配置信息',validType:'length[0,255]'" />
					</div>
					<div class="MidLine">
						<label>数量：</label><input id="v_quantity" class="easyui-numberspinner" data-options="min:1" />
						<label class="R">原值：</label><input id="v_cost" class="easyui-numberspinner" data-options="min:0,precision:2,groupSeparator:',',prefix:'￥'" />
					</div>
					<div class="MidLine">
						<label>采购时间：</label><input id="v_purchaseTime" class="easyui-datebox" />
						<label class="R">目前使用情况：</label><input id="v_state" class="easyui-combobox" data-options="data:[{'id':0,'text':'在用'},{'id':3,'text':'闲置/备用'}],valueField:'id',textField:'text',editable:false,required:true,missingMessage:'使用情况必须选择'" />
					</div>
					<div class="MidLine HW"><label>保修状态：</label><input id="v_warranty" class="easyui-combobox" data-options="data:[{'id':0,'text':'在保'},{'id':1,'text':'续保'},{'id':-1,'text':'过保'}],valueField:'id',textField:'text',editable:false" /></div>
					<div class="MidLine HW"><label>物理位置：</label><input id="v_location" class="easyui-textbox" data-options="validType:'length[0,255]'" /></div>
					<div class="MidLine HW"><label>网络地址：</label><input id="v_ip" class="easyui-textbox" data-options="validType:'length[0,64]'" /></div>
					<div class="MidLine HW"><label>重要程度：</label><input id="v_importance" class="easyui-combobox" data-options="data:[{'id':0,'text':'普通'},{'id':1,'text':'重要'},{'id':2,'text':'关键'}],valueField:'id',textField:'text',editable:false" /></div>
					<div class="MidLine HW"><label>责任部门：</label><input id="v_dept" class="easyui-combotree" data-options="valueField:'id',textField:'text',editable:false,url:'hr/get-curr-depts'" /></div>
					<div class="MidLine HW"><label>责任人：</label><input id="v_empl" class="easyui-combobox" data-options="valueField:'id',textField:'name',editable:false" /></div>
					<div class="MidLine SW"><label>软件类型：</label><input id="v_softwareType" class="easyui-combobox" data-options="valueField:'id',textField:'text',data:[{id:0,text:'商品软件'},{id:1,text:'自由/开源软件'},{id:2,text:'免费软件'},{id:3,text:'试用软件'},{id:4,text:'定制开发软件'},{id:5,text:'自主研发软件'},{id:6,text:'其他类型软件'}],editable:false" /></div>
					<div class="MidLine SW"><label>许可证：</label><input id="v_license" class="easyui-textbox" data-options="validType:'length[0,64]'" /></div>
					<div class="MidLine SW"><label>许可期限：</label><input id="v_expiredTime" class="easyui-datebox" /></div>
					<div class="MidLine"><label>备注：</label><input id="v_comment" class="easyui-textbox" data-options="validType:'length[0,255]'" /></div>
					<div class="MidLine" style="color:red;">提示：提交时可以保存多条相同记录，硬件类资产自动在资产编号后添加标识号[xxx]，至多100条。</div>
					<div class="MidLine"><label>提交记录数量：</label><input id="v_number" class="easyui-numberspinner" data-options="min:1,max:100,suffix:'条',width:80" />
						<a id="f_submit" href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-saved'" style="padding:2px 6px;margin-left:1em;">提交</a>
						<a id="f_reset" href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-forbid'" style="padding:2px 6px;margin-left:1em;">重填</a>
					</div>
				</form>
			</div>
		</div>
	</div>
	<%@ include file="../html/footer.jsp"%>
</body>
</html>