$(function() {
	/**
	 * initialize page parameters
	 */
	$("#breadcrumbs").html("首页");
	Date.prototype.pattern = function(fmt) {
		var o = {
			"M+" : this.getMonth() + 1,
			"d+" : this.getDate(),
			"h+" : this.getHours() % 12 == 0 ? 12 : this.getHours() % 12,
			"H+" : this.getHours(),
			"m+" : this.getMinutes(),
			"s+" : this.getSeconds(),
			"q+" : Math.floor((this.getMonth() + 3) / 3),
			"S" : this.getMilliseconds()
		};
		var week = {
			"0" : "/u65e5",
			"1" : "/u4e00",
			"2" : "/u4e8c",
			"3" : "/u4e09",
			"4" : "/u56db",
			"5" : "/u4e94",
			"6" : "/u516d"
		};
		if (/(y+)/.test(fmt)) {
			fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
		}
		if (/(E+)/.test(fmt)) {
			fmt = fmt.replace(RegExp.$1, ((RegExp.$1.length > 1) ? (RegExp.$1.length > 2 ? "/u661f/u671f" : "/u5468") : "") + week[this.getDay() + ""]);
		}
		for ( var k in o) {
			if (new RegExp("(" + k + ")").test(fmt)) {
				fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
			}
		}
		return fmt;
	}

	var func = {
		"fId" : 0,
		"lId" : 0,
		"loadMemo" : function(date) {
			$("#dt").html(date.pattern("yyyy年MM月dd日"));
			return $.ajax({
				"url" : "portal/load-memo",
				"data" : {
					"memoDate" : date.pattern("yyyy-MM-dd")
				}
			});
		},
		"loadMemoResult" : function(data) {
			if (null == data) {
				$("#mmid").val(-1);
				$("#mm").textbox("clear");
			} else {
				$("#mmid").val(data.id);
				$("#mm").textbox("setValue", data.text);
			}
			func.showRest();
		},
		"loadNoti" : function() {
			return $.ajax({
				"url" : "portal/refr-noti",
				"data" : {
					"rows" : 15
				}
			});
		},
		"refrNoti" : function() {
			$.ajax({
				"url" : "portal/refr-noti",
				"data" : {
					"id" : func.fId
				},
				"success" : function(data) {
					if (data.length == 0)
						return;
					func.fId = data[0].id;
					$.each(data.reverse(), function(i, n) {
						$("#noti").prepend("<dt>" + n.publishTime + "</dt><dd><span class='Red'>" + n.sourceName + "：</span>" + n.text + "</dd>");
					});
					$("#noti dt:first").get(0).scrollIntoView(true);
				}
			});
		},
		"moreNoti" : function() {
			$.ajax({
				"url" : "portal/more-noti",
				"data" : {
					"id" : func.lId,
					"rows" : 15
				},
				"success" : function(data) {
					if (data.length == 0)
						return;
					func.lId = data[data.length - 1].id;
					$.each(data, function(i, n) {
						$("#noti").append("<dt>" + n.publishTime + "</dt><dd><span class='Red'>" + n.sourceName + "：</span>" + n.text + "</dd>");
					});
					$("#noti dd:last").get(0).scrollIntoView(false);
				}
			});
		},
		"loadNotiResult" : function(data) {
			$("#noti").html("");
			func.fId = data[0].id;
			func.lId = data[data.length - 1].id;
			$.each(data, function(i, n) {
				$("#noti").append("<dt>" + n.publishTime + "</dt><dd><span class='Red'>" + n.sourceName + "：</span>" + n.text + "</dd>");
			});
		},
		"titleTip" : function() {
			return $.ajax({
				"url" : "portal/title-tip"
			});
		},
		"showError" : function() {
			$.messager.alert("提示", "访问服务器出错，系统故障。请联系维护人员。");
		},
		"initPage" : function(date) {
			$.when(func.loadMemo(date), func.loadNoti(), func.titleTip()).then(function(memo, noti, tip) {
				func.loadMemoResult(memo[0]);
				func.loadNotiResult(noti[0]);
				$("#notifications").panel({
					"title" : tip[0]
				});
			}, func.showError);
		},
		"showRest" : function() {
			var r = 255 - $("#mm").textbox("getText").length
			$("#mmrest").html(r);
			if (r < 0)
				$("#mmrest").addClass("Red");
			else
				$("#mmrest").removeClass("Red");
		},
		"saveMemo" : function() {
			$.ajax({
				"url" : "portal/save-memo",
				"data" : {
					"id" : $("#mmid").val(),
					"text" : $("#mm").textbox("getText"),
					"memoDate" : $("#memoCalendar").calendar("options").current.pattern("yyyy-MM-dd")
				},
				"success" : function(data) {
					if (null == data) {
						$.msgbox("错误", "保存备忘录失败", "warning");
					} else {
						$("#mmid").val(data.id);
						$("#mm").textbox("setValue", data.text);
						func.showRest();
					}
				}
			});
		}
	};

	/**
	 * initialize navi
	 */
	$.initNavi("homepage");

	/**
	 * 备忘录
	 */
	$("#mm").textbox({
		"multiline" : true,
		"fit" : true,
		"prompt" : "备忘录正文，不超过255字，Ctrl-Enter提交保存"
	});
	$("#mm").textbox('textbox').on("keyup", function(e) {
		if (e.ctrlKey == true && e.keyCode == 13)
			func.saveMemo();
		else
			func.showRest();
	}).on("contextmenu", function(e) {
		return false;
	}).css({
		"white-space" : "pre",
		"overflow" : "auto",
		"font-family" : "consolas"
	});
	func.initPage($("#memoCalendar").calendar("options").current);
	$("#refrNoti").on("click", func.refrNoti);
	$("#moreNoti").on("click", func.moreNoti);
	$("#memoCalendar").calendar({
		"onSelect" : function(date) {
			func.loadMemo(date).then(func.loadMemoResult, func.showError);
		}
	});
});