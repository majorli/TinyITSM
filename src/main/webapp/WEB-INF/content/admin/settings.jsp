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
<script type="text/javascript" charset="UTF-8" src="${pageContext.request.contextPath}/js/settings.js"></script>
<title>TinyITSM - 系统控制台</title>
</head>
<body>
<!-- 
	1、用户管理：设置特殊角色(iter, admin)，设置审核人标签；
	2、设置是否使用外部HR接口，是否使用外部CI接口；
	3、批量导入HR数据，批量导入CI数据，获取HR数据模板文件；
-->
	<%@ include file="../html/header.jsp"%>
	<div class="Container" id="cc">
		<%@ include file="../html/navi.jsp" %>
		<div id="wpad">
			<div id="syscon">
				<h4>用户角色设置</h4>
				<div class="MidLine">
					<span style="vertical-align:-2px;">搜索本公司用户：</span><input id="user" class="easyui-searchbox" style="width:300px;height:30px;" />
				</div>
				<div class="MidLine">
					<span style="margin-right:4px;">用户：</span>
					<span id="username" style="font-weight:bold;color:red;"></span>
					<span style="margin-left:4px;">对应员工：</span>
					<span id="employee" style="font-weight:bold;color:red;"></span>
					<span style="margin-left:4px;">所在部门：</span>
					<span id="department" style="font-weight:bold;color:red;"></span>
				</div>
				<div id="roles" class="MidLine">
					<span>特权：</span>
					<input type="hidden" id="userid" value="" />
					<label><input id="iter" type="checkbox" />信息化人员</label>
					<label><input id="admin" type="checkbox" />本系统管理员</label>
					<label><input id="leader" type="checkbox" />公司领导</label>
					<label><input id="supervisor" type="checkbox" />部门负责人</label>
					<label><input id="auditor" type="checkbox" />审核人</label>
				</div>
				<div class="Line">
					<a id="setRoles" class="easyui-linkbutton" data-options="iconCls:'icon-saved'" style="padding:2px 8px;">保存设置</a>
					<a id="cancelRoles" class="easyui-linkbutton" data-options="iconCls:'icon-multiple'" style="padding:2px 8px;">清空重填</a>
				</div>
				<h4>变更管理员密码</h4>
				<div class="MidLine">
					<span style="vertical-align:-2px;">旧密码：</span><input id="pwd" type="password" style="width:300px;height:30px;" />
				</div>
				<div class="MidLine">
					<span style="vertical-align:-2px;">新密码：</span><input id="pswd" type="password" style="width:300px;height:30px;" />
				</div>
				<div class="MidLine">
					<span style="vertical-align:-2px;">确&emsp;认：</span><input id="repswd" type="password" style="width:300px;height:30px;" />
				</div>
				<div class="Line">
					<a id="resetAdminPassword" class="easyui-linkbutton" data-options="iconCls:'icon-saved'" style="padding:2px 8px;">变更密码</a>
					<a id="cancelAdminPwd" class="easyui-linkbutton" data-options="iconCls:'icon-multiple'" style="padding:2px 8px;">清空重填</a>
				</div>
				<h4>批量数据导入</h4>
				<p style="color:red">注意：<br />
				1）批量导入Excel文档中的数据必须严格遵照系统提供的文档模板，错误的数据或数据格式都可能导致严重的后果。<br />
				2）每个公司的管理员只能导入本公司的数据，不能跨公司导入数据，即使是所辖的分公司。<br />
				3）批量导入的数据只会以新增的方式添加入数据库，不会删除或覆盖原有数据，因此需仔细确认没有与已有数据重复的记录存在。<br />
				4）下载批量导入数据的标准模板文件：
				<a href="${pageContext.request.contextPath}/docs/HR数据批量导入模板.xlsx" id="hrtemp">HR数据模板</a>，
				<a href="${pageContext.request.contextPath}/docs/CI数据批量导入模板.xlsx" id="citemp">CI数据模板</a>
				</p>
				<div style="margin-bottom:4px;">导入HR数据：</div>
				<form id="hrfile" action="admin/hr-import" method="post" enctype="multipart/form-data">
					<input id="hr" class="easyui-filebox" name="data" style="width:320px;height:30px;" data-options="buttonText:'选取文件'" />
					<a id="hrsubmit" style="padding: 2px 8px;" class="easyui-linkbutton" data-options="iconCls:'icon-saved'">导入</a>
					<a id="hrreset" style="padding: 2px 8px;" class="easyui-linkbutton" data-options="iconCls:'icon-multiple'">清除</a>
				</form>
				<div style="margin-top:1em;margin-bottom:4px;">导入CI数据：</div>
				<form id="cifile" action="admin/ci-import" method="post" enctype="multipart/form-data">
					<input id="ci" class="easyui-filebox" name="data" style="width:320px;height:30px;" data-options="buttonText:'选取文件'" />
					<a id="cisubmit" style="padding: 2px 8px;" class="easyui-linkbutton" data-options="iconCls:'icon-saved'">导入</a>
					<a id="cireset" style="padding: 2px 8px;" class="easyui-linkbutton" data-options="iconCls:'icon-multiple'">清除</a>
				</form>
				<h4>初始化全文检索索引</h4>
				<p style="color:red">注意：<br />
				1）全文检索索引应该只在系统重新部署后初始化建立一次，系统运行过程中重建索引可能引起未知问题，请慎重。<br />
				2）如果确定原有的索引表已经完全损坏，请系统管理员从文件系统中彻底删除索引表根目录后再重新初始化。
				</p>
				<a id="reIndex" class="easyui-linkbutton" data-options="iconCls:'icon-settings'" style="padding:2px 8px;">初始化索引表</a>
				<h4>发布管理员公告</h4>
				<p style="color:red">注意：管理员公告的可见范围为本公司及下属公司，但不能向上级公司或平级公司发布管理员公告。
				</p>
				<div class="Line">
					<label>公告范围：<input id="adminNotiScope" class="easyui-combotree" data-options="valueField:'id',textField:'text',url:'hr/get-companies',prompt:'请选择公告可见范围',cascadeCheck:false,required:true,missingMessage:'必须至少选择一家公司',height:30,width:200" multiple="multiple" /></label>
					<label>公告内容：<input type="text" id="adminNotiText" class="easyui-textbox" data-options="validType:'length[5,255]',required:true,missingMessage:'公告内容必须输入',prompt:'请输入公告内容，长度5至255个字',height:30,width:500" /></label>
					<a id="adminNoti" class="easyui-linkbutton" data-options="iconCls:'icon-saved'" style="padding: 2px 8px;">发布</a>
				</div>
				<div style="height:1em;"></div>
			</div>
		</div>
	</div>
	<%@ include file="../html/footer.jsp"%>
</body>
</html>