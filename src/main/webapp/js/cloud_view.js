$(function() {
	/**
	 * initialize page parameters and functions
	 */
	$("#breadcrumbs").html("<a href=\"javascript:window.location.replace('func?code=HOME_PAGE')\">首页</a>&nbsp;&#187;&nbsp;资料分类视图");

	/**
	 * initialize navi
	 */
	$.initNavi("cloud_view");

	/**
	 * initialize wpad
	 */
	$("#cata").tree({
		"data" : [ {
			"id" : 1,
			"text" : "全部资料",
			"iconCls" : "icon-folder-documents",
			"children" : [ {
				"id" : 10,
				"text" : "文档资料",
				"iconCls" : "icon-file-txt",
				"children" : [ {
					"id" : 101,
					"text" : "WORD文档",
					"iconCls" : "icon-file-word"
				}, {
					"id" : 102,
					"text" : "EXCEL表格",
					"iconCls" : "icon-file-excel"
				}, {
					"id" : 103,
					"text" : "PPT演示文稿",
					"iconCls" : "icon-file-ppt"
				}, {
					"id" : 104,
					"text" : "VISIO图表",
					"iconCls" : "icon-file-visio"
				}, {
					"id" : 105,
					"text" : "PDF文稿",
					"iconCls" : "icon-file-pdf"
				} ]
			}, {
				"id" : 20,
				"text" : "多媒体资料",
				"iconCls" : "icon-file-iso",
				"children" : [ {
					"id" : 201,
					"text" : "图片资料",
					"iconCls" : "icon-file-image"
				}, {
					"id" : 202,
					"text" : "音频资料",
					"iconCls" : "icon-file-music"
				}, {
					"id" : 203,
					"text" : "视频资料",
					"iconCls" : "icon-file-video"
				} ]
			}, {
				"id" : 30,
				"text" : "其他类型",
				"iconCls" : "icon-file"
			} ]
		} ],
		"onSelect" : function(node) {
			if ($("#files").datagrid("options").url == null) {
				$("#files").datagrid({
					"url" : "cloud/view-files",
					"queryParams" : {
						"docType" : node.id
					}
				}).datagrid("getPager").pagination({
					"buttons" : [ {
						"text" : "下载选中的文件",
						"handler" : function() {
							var chks = $("#files").datagrid("getChecked");
							if (chks.length == 1) {
								$("#dl_file_id").val(chks[0].id);
								$("#dl_file").form("submit");
							} else if (chks.length > 1) {
								var ids = [];
								$.each(chks, function(i, n) {
									ids.push(n.id);
								});
								$("#dl_multi_ids").val(ids.join(","));
								$("#dl_multi").form("submit");
							}
						}
					} ]
				});
			} else {
				$("#files").datagrid("load", {
					"docType" : node.id
				});
			}
		}
	});
	$("#files").datagrid(
			{
				"border" : false,
				"height" : '100%',
				"rownumbers" : true,
				"columns" : [ [
						{
							"checkbox" : true,
							"field" : "ck",
							"styler" : function(value, row, index) {
								return "height:30px;";
							}
						},
						{
							"field" : "name",
							"title" : "名称",
							"sortable" : true,
							"width" : 350,
							"formatter" : function(value, row, index) {
								var ret = "<span class='" + row.iconCls
										+ "' style='display:inline-block;height:20px;background-position:left center;padding-left:20px;'>" + value + "</span>";
								return ret;
							}
						}, {
							"field" : "list",
							"title" : "栏目",
							"sortable" : true,
							"width" : 200
						}, {
							"field" : "size",
							"title" : "大小",
							"sortable" : true,
							"width" : 80
						}, {
							"field" : "createTime",
							"title" : "创建时间",
							"sortable" : true,
							"width" : 150
						} ] ],
				"pagination" : true,
				"pageSize" : 20
			});
})