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
<script type="text/javascript" charset="UTF-8" src="${pageContext.request.contextPath}/js/sys_new.js"></script>
<style type="text/css">
	form#newSystem label {display:inline-block;width:100px;}
	form#newSystem label.R {margin-left:3em;}
	div.MidLine>span>input, div.MidLine>input {width:320px;}
</style>
<title>TinyProject - 新增信息系统</title>
</head>
<body>
	<%@ include file="../html/header.jsp"%>
	<div class="Container" id="cc">
		<%@ include file="../html/navi.jsp" %>
		<div id="wpad">
			<div class="easyui-panel" data-options="title:'新增信息化资产',width:'100%',height:'100%'" style="padding:2em;">
				<form action="" method="post" id="newSystem">
					<h4>系统基本信息</h4>
					<div class="MidLine">
						<label>系统类别：</label><input id="v_type" class="easyui-combobox" data-options="data:[{'id':0,'text':'基础设施'}, {'id':1,'text':'信息网络系统'}, {'id':2,'text':'信息安全系统'}, {'id':3,'text':'核心平台'}, {'id':4,'text':'虚拟化/云系统'}, {'id':5,'text':'硬件平台'}, {'id':6,'text':'软件平台'}, {'id':7,'text':'数据分析系统'}, {'id':8,'text':'移动应用'}, {'id':9,'text':'业务应用系统'}, {'id':10,'text':'作业类系统'}, {'id':11,'text':'信息化工具'}, {'id':12,'text':'桌面办公系统'}, {'id':13,'text':'其他信息系统'}],valueField:'id',textField:'text',editable:false,height:30" />
						<label class="R">系统状态：</label><input id="v_stage" class="easyui-combobox" data-options="data:[{'id':0,'text':'建成（系统已经建成并完成最终验收）'},{'id':1,'text':'在建（尚未完成项目建设和验收）'}],valueField:'id',textField:'text',editable:false,height:30" />
					</div>
					<div class="MidLine">
						<label>系统名称：</label><input id="v_name" class="easyui-textbox" data-options="prompt:'请输入系统名称',validType:'length[0,64]',required:true,missingMessage:'系统名称必须填写',height:30" />
						<label class="R">系统简称：</label><input id="v_alias" class="easyui-textbox" data-options="prompt:'请输入系统简称',validType:'length[0,16]',height:30" />
					</div>
					<div class="MidLine">
						<label>型号/版本：</label><input id="v_modelOrVersion" class="easyui-textbox" data-options="prompt:'请输入系统的型号或版本号',validType:'length[0,64]',height:30" />
						<label class="R">功能与用途：</label><input id="v_brief" class="easyui-textbox" data-options="prompt:'请输入系统功能与用途简介',validType:'length[0,255]',height:30" />
					</div>
					<div class="MidLine">
						<label>等级保护级别：</label><input id="v_securityLevel" class="easyui-combobox" data-options="data:[{'id':0,'text':'未定级'}, {'id':1,'text':'一级'}, {'id':2,'text':'二级'}, {'id':3,'text':'三级'}],valueField:'id',textField:'text',editable:false,height:30" />
						<label class="R">等保备案号：</label><input id="v_securityCode" class="easyui-textbox" data-options="prompt:'请输入等保备案编号',disabled:true,validType:'length[0,32]',height:30" />
					</div>
					<div class="MidLine">
						<label>系统使用者：</label><input id="v_userBrief" class="easyui-textbox" data-options="prompt:'请输入主要用户群体的描述',validType:'length[0,255]',height:30" />
						<label class="R">系统供应商：</label><input id="v_provider" class="easyui-textbox" data-options="prompt:'请输入系统的供应商/开发商/集成商',validType:'length[0,64]',height:30" />
					</div>
					<div class="MidLine">
						<label>系统使用范围：</label><input id="v_scope" class="easyui-combobox" data-options="url:'project/system-scopes',valueField:'id',textField:'text',value:0,editable:false,height:30" />
						<span id="d_companiesInScope"><label class="R">指定使用范围：</label><input id="v_companiesInScope" class="easyui-combotree" data-options="url:'hr/get-branches-tree',valueField:'id',textField:'text',editable:false,cascadeCheck:false,height:30" multiple="multiple" /></span>
					</div>
					<div class="MidLine">
						<label>部署方式：</label><input id="v_deploy" class="easyui-combobox" data-options="data:[{'id':0,'text':'国家局集中'}, {'id':1,'text':'国省两级分布'}, {'id':2,'text':'国省市三级分布'}, {'id':3,'text':'省集中'}, {'id':4,'text':'省市两级分布'}, {'id':5,'text':'市集中'}, {'id':6,'text':'行业外'}, {'id':7,'text':'其他部署方式'}],valueField:'id',textField:'text',value:3,editable:false,height:30" />
						<label class="R">下属系统分支：</label><input id="v_branches" class="easyui-combobox" data-options="url:'project/potential-branches',valueField:'id',textField:'text',editable:false,multiple:true,height:30" />
					</div>
					<div class="MidLine">
						<label>系统建成时间：</label><input id="v_constructedTime" class="easyui-datebox" data-options="height:30" />
						<label class="R">免费运维期(月)：</label><input id="v_freeMaintainMonths" class="easyui-numberspinner" data-options="min:0,height:30" />
					</div>
					<h4>系统关联信息</h4>
					<div class="MidLine">
						<label>系统建设项目：</label><input id="v_prjOptions" class="easyui-combobox" data-options="data:[{'id':0,'text':'不关联系统建设项目'},{'id':1,'text':'创建一个新的建设项目'},{'id':2,'text':'合并入原有的建设项目'}],valueField:'id',textField:'text',value:0,editable:false,height:30" />
						<span id="d_newPrj"><label class="R">建设项目名称：</label><input id="v_newPrjName" class="easyui-textbox" data-options="prompt:'请输入新建的系统建设项目名称',validType:'length[0,64]',height:30" /></span>
						<span id="d_oldPrj"><label class="R">原有建设项目：</label><input id="v_oldPrjs" class="easyui-combobox" data-options="url:'project/prj-list',valueField:'id',textField:'text',editable:false,height:30" /></span>
					</div>
					<div class="MidLine">
						<label>系统运维项目：</label><input id="v_mtnOptions" class="easyui-combobox" data-options="data:[{'id':0,'text':'不关联系统运维项目'},{'id':1,'text':'创建一个新的运维项目'},{'id':2,'text':'合并入原有的运维项目'}],valueField:'id',textField:'text',value:0,editable:false,height:30" />
						<span id="d_newMtn"><label class="R">运维项目名称：</label><input id="v_newMtnName" class="easyui-textbox" data-options="prompt:'请输入新建的系统运维项目名称',validType:'length[0,64]',height:30" /></span>
						<span id="d_oldMtn"><label class="R">原有运维项目：</label><input id="v_oldMtns" class="easyui-combobox" data-options="url:'maintain/mtn-list',valueField:'id',textField:'text',editable:false,height:30" /></span>
					</div>
					<div class="MidLine">
						<label>系统讨论组：</label><input id="v_wikiOptions" class="easyui-combobox" data-options="data:[{'id':0,'text':'创建一个新的讨论组'},{'id':1,'text':'加入原有的讨论组'}],valueField:'id',textField:'text',value:0,editable:false,height:30" />
						<span id="d_newWiki"><label class="R">讨论组名称：</label><input id="v_newWikiName" class="easyui-textbox" data-options="prompt:'请输入新建的系统讨论组名称',validType:'length[0,64]',height:30" /></span>
						<span id="d_wikies"><label class="R">原有讨论组：</label><input id="v_wikies" class="easyui-combobox" data-options="url:'maintain/wiki-list',valueField:'id',textField:'text',editable:false,height:30" /></span>
					</div>
					<div style="margin-top:2em;">
						<a id="f_submit" class="easyui-linkbutton" data-options="iconCls:'icon-saved'" style="padding:2px 6px;">提交</a>
						<a id="f_reset" class="easyui-linkbutton" data-options="iconCls:'icon-forbid'" style="padding:2px 6px;">重填</a>
					</div>
				</form>
			</div>
		</div>
	</div>
	<%@ include file="../html/footer.jsp"%>
</body>
</html>