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
<script type="text/javascript" charset="UTF-8" src="${pageContext.request.contextPath}/js/tinyhr.js"></script>
<title>TinyHR - 内置微型人力资源子系统[人员与组织机构设置]</title>
</head>
<body>
	<%@ include file="../html/header.jsp"%>
	<div class="Container" id="cc">
		<%@ include file="../html/navi.jsp" %>
		<div id="wpad">
			<div id="hrPanel" class="easyui-layout" data-options="height:'100%'">
				<div id="orgTree" data-options="region:'west',title:'组织机构树',split:true" style="width:250px;padding:4px;">
					<div class="easyui-layout" data-options="fit:true">
						<div id="orgTreeTools" data-options="region:'north',border:false,height:'34px'">
							<div class="easyui-panel" style="padding-bottom:2px;margin-bottom:4px;border-top:none;border-left:none;border-right:none;">
								<a id="orgTreeReload" href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-reload'">刷新</a>
								<a href="javascript:void(0)" class="easyui-menubutton" data-options="menu:'#mmOrgTreeView',iconCls:'icon-settings'">视图设置</a>
							</div>
							<div id="mmOrgTreeView" style="width:120px;">
								<div id="showName">显示全称</div>
								<div id="showAlias">显示简称</div>
								<div class="menu-sep"></div>
								<div id="expandAll">全部展开</div>
								<div id="collapseAll">全部折叠</div>
							</div>
						</div>
						<div id="orgTreeList" data-options="region:'center',border:false">
							<ul id="tree"></ul>
						</div>
					</div>
				</div>
				<div id="nodeDetail" data-options="region:'center',title:'机构与人员属性'" style="padding:4px;">
					<div class="easyui-layout" data-options="fit:true">
						<div id="nodeDetailTools" data-options="region:'north',border:false,height:'34px'">
							<div class="easyui-panel" style="padding-bottom:2px;margin-bottom:4px;border-top:none;border-left:none;border-right:none;">
								<a id="nodeDetailEdit" href="javascript:void(0)" class="easyui-splitbutton" data-options="menu:'#mmNodeDetailEdit',iconCls:'icon-edit'">编辑</a>
								<a id="nodeDetailSave" href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-save'">保存</a>
								<a id="nodeDetailCancel" href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-undo'">撤销</a>
							</div>
							<div id="mmNodeDetailEdit" style="width:120px;">
								<div id="newDept" data-options="iconCls:'icon-plus'">部门新增</div>
								<div id="abandonDept" data-options="iconCls:'icon-forbid'">部门停用</div>
								<div class="menu-sep"></div>
								<div id="newEmpl" data-options="iconCls:'icon-add-employee'">人员新增</div>
								<div id="moveEmpl">人员调动</div>
								<div id="leaveEmpl" data-options="iconCls:'icon-remove-employee'">人员离职</div>
							</div>
						</div>
						<div id="nodeDetails" data-options="region:'center',border:false">
							<div id="nodeDetailTabs" class="easyui-tabs" data-options="fit:true,border:false">
								<div title="下属部门" data-options="iconCls:'icon-department'" style="padding-top:1px"><table id="dept"></table></div>
								<div title="在职员工" data-options="iconCls:'icon-employee'" style="padding-top:1px"><table id="empl"></table></div>
								<div title="已停用部门" data-options="iconCls:'icon-forbid'" style="padding-top:1px;"><table id="hisDept"></table></div>
								<div title="已离职员工" data-options="iconCls:'icon-remove-employee'" style="padding-top:1px;"><table id="fmrEmpl"></table></div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<%@ include file="../html/footer.jsp"%>
	<div id="move">
		<h5>请选择调往的目的公司和部门</h5>
		<div class="Line"><label>调往公司：<input id="targetComp" /></label></div>
		<div class="Line"><label>调往部门：<input id="targetDept" /></label></div>
	</div>
	<input type="hidden" id="extraHR" value="${sessionScope.extraHR}" />
</body>
</html>