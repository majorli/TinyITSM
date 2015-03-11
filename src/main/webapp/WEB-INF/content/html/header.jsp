<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="Title" id="tt">
	<span id="appTitle" class="Heiti">${initParam.appName}</span> <span id="appVersion" class="Heiti">&nbsp;-&nbsp;${initParam.subName}</span>
</div>
<div class="Header" id="hh">
	当前位置：<span id="breadcrumbs"></span>
	<div style="float: right;">
		<label>更换主题：<select id="themes" style="border: 1px inset #CCE2ED;vertical-align: -1px;">
			<optgroup label="Base">
				<option value="default">Default</option>
				<option value="gray">Gray</option>
				<option value="metro">Metro</option>
				<option value="bootstrap">Bootstrap</option>
				<option value="black">Black</option>
			</optgroup>
			<optgroup label="Metro">
				<option value="metro-blue">Metro Blue</option>
				<option value="metro-gray">Metro Gray</option>
				<option value="metro-green">Metro Green</option>
				<option value="metro-orange">Metro Orange</option>
				<option value="metro-red">Metro Red</option>
			</optgroup>
			<optgroup label="UI">
				<option value="ui-cupertino">Cupertino</option>
				<option value="ui-dark-hive">Dark Hive</option>
				<option value="ui-pepper-grinder">Pepper Grinder</option>
				<option value="ui-sunny">Sunny</option>
			</optgroup>
		</select></label>
	</div>
	<script type="text/javascript" charset="UTF-8">
		$.extend({
			"getCookie" : function(c_name) {
				if (document.cookie.length > 0) {
					c_start = document.cookie.indexOf(c_name + "=");
					if (c_start != -1) {
						c_start = c_start + c_name.length + 1;
						c_end = document.cookie.indexOf(";", c_start);
						if (c_end == -1)
							c_end = document.cookie.length;
						return unescape(document.cookie.substring(c_start, c_end));
					}
				}
				return '';
			}
		});
		$.extend({
			"setCookie" : function(c_name, value, expiredays) {
				var exdate = new Date();
				exdate.setDate(exdate.getDate() + expiredays);
				document.cookie = c_name + "=" + escape(value) + ((expiredays == null) ? "" : ";expires=" + exdate.toGMTString() + ";path=/itsm/");
			}
		});
		$.extend({
			"msgbox" : function(title, message, icon) {
				$.messager.alert(title, message, icon).window({
					"top" : "280px"
				});
			}
		});
		$.extend({
			"waitbox" : function(message) {
				if (message) {
					$.messager.progress({
						title : "请稍候",
						msg : message + "，请稍候......"
					}).window({
						top : "280px"
					});
				} else {
					$.messager.progress("close");
				}
			}
		});
		$.ajaxSetup({
			"type" : "POST",
			"dataType" : "json",
			"cache" : false,
			"error" : function() {
				$.messager.alert("提示", "访问服务器出错，系统故障。请联系维护人员。");
			}
		});
		(function() {
			var t = $.getCookie("theme");
			if (!t) {
				t = "default";
				$.setCookie("theme", t, 3650);
			}
			$("#themes").val(t);
		})();
		$("#themes").on("change", function() {
			var $easyuiTheme = $('#easyuiTheme');
			var url = $easyuiTheme.attr('href');
			var href = url.substring(0, url.indexOf('themes')) + 'themes/' + $(this).val() + '/easyui.css';
			$easyuiTheme.attr('href', href);

			var $iframe = $('iframe');
			if ($iframe.length > 0) {
				for (var i = 0; i < $iframe.length; i++) {
					var ifr = $iframe[i];
					$(ifr).contents().find('#easyuiTheme').attr('href', href);
				}
			}
			$.setCookie("theme", $(this).val(), 3650);
		});
	</script>
	<div style="clear: both;"></div>
</div>