<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
		<div id="navi">
			<div id="userPanel">
				<input id="emplId" type="hidden" value="${sessionScope.userInfo.user.employeeId}" />
				<input id="emplIsIter" type="hidden" value="${sessionScope.userInfo.user.iter}" />
				<div class="TinyLine">用户名：${sessionScope.userInfo.user.username}</div>
				<div class="TinyLine">公&emsp;司：${sessionScope.userInfo.company.alias}</div>
				<div class="TinyLine">员&emsp;工：${sessionScope.userInfo.employee.name}</div>
				<div class="TinyLine">
					IT人员：
					<c:choose>
						<c:when test="${sessionScope.userInfo.user.iter}">是</c:when>
						<c:otherwise>否</c:otherwise>
					</c:choose>
				</div>
				<div class="TinyLine">
					管理员：
					<c:choose>
						<c:when test="${sessionScope.userInfo.user.admin}">是</c:when>
						<c:otherwise>否</c:otherwise>
					</c:choose>
				</div>
				<div class="TinyLine">
					<a id="userInfo" href="javascript:void(0);">用户设置</a><a id="logout" href="javascript:void(0);">退出登录</a>
				</div>
			</div>
			<div id="naviPanel" class="easyui-accordion" style="margin-top:1em;">
				<div title="菜单" data-options="iconCls:'icon-tip'" style="overflow:auto;padding:10px;">
					<ul id="mainMenuTree">
						<c:forEach items="${sessionScope.userInfo.menu}" var="m">
							<li id="${m.id}">
								<span>${m.text}</span>
								<ul>
									<c:forEach items="${m.children}" var="i">
										<li id="${i.id}">
											<span>${i.text}</span>
										</li>
									</c:forEach>
								</ul>
							</li>
						</c:forEach>
					</ul>
				</div>
				<div title="搜索" data-options="iconCls:'icon-search'" style="overflow:auto;padding:10px;">
					<input id="fullTextSearch" style="width: 100%" />
					<div id="searchCatalog">
						<div data-options="name:'SAPMD'">全部</div>
						<div data-options="name:'S'">服务</div>
						<div data-options="name:'A'">资产</div>
						<div data-options="name:'P'">项目</div>
						<div data-options="name:'M'">运维</div>
						<div data-options="name:'D'">资料</div>
					</div>
					<h1>全文检索</h1>
					<p>检索关键字长度不能太短，中文关键字最少2个汉字，英文关键字最少3个字母，否则将检索不到结果。</p>
					<p>英文根据英语词法断词，不能搜索一个单词中的部分字母。</p>
				</div>
				<div title="帮助" data-options="iconCls:'icon-help'" style="overflow:auto;padding:10px;"></div>
				<div title="关于" data-options="iconCls:'icon-logo'" style="overflow:auto;padding:10px;"></div>
			</div>
		</div>
		<script type="text/javascript" charset="UTF-8">
			$.extend({
				"initNavi" : function(p) {
					$("#userPanel").css("padding", "10px").panel({
						"title" : "当前用户",
						"width" : "100%"
					});
					$("#userInfo").linkbutton({
						"iconCls" : "icon-man"
					}).css("margin-right", "1em").on("click", function() {
						if ($("#emplId").val() == 0) {
							$.messager.alert("消息", "'管理员'是系统内置用户，不能修改信息。", "info").window({
								"top" : "280px"
							});
						} else {
							window.location.replace("func?code=USR_SET");
						}
					});
					$("#logout").linkbutton({
						"iconCls" : "icon-clear"
					}).on("click", function() {
						$.messager.confirm("确认", "你确定要登出系统吗？", function(r) {
							if (r)
								window.location.replace("func?code=LOGOUT");
						}).window({
							"top" : "280px"
						});
					});
					$("#naviPanel").accordion({
						"width" : "100%",
						"height" : ($("#navi").innerHeight() - $("#userPanel").outerHeight() - 40) + "px",
						"selected" : 0
					});
					$("#mainMenuTree").tree({
						"animate" : true,
						"onClick" : function(node) {
							if (node.children == null)
								window.location.replace("func?code=" + node.id);
						}
					});
					
					$("#fullTextSearch").searchbox({
						"searcher" : function(value, cata) {
							if ($("#emplIsIter").val() == "true") {
								window.location.replace("fts?catalog=" + cata + "&keyword=" + value);
							} else {
								$.messager.alert("消息", "只有信息化人员可以进行全文检索。", "info").window({
									"top" : "280px"
								});
							}
						},
						"menu" : "#searchCatalog",
						"prompt" : "请输入关键字"
					});
					$("#naviPanel").accordion("getPanel", 2).panel("refresh", "docs/help_" + p + ".html");
					$("#naviPanel").accordion("getPanel", 3).panel("refresh", "docs/about.html");
				}
			});
		</script>