package com.jeans.tinyitsm.model.view;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class PropertyGridRow implements Serializable {

	private String name;
	private String value;
	private String group;
	private Object editor;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public Object getEditor() {
		return editor;
	}

	public void setEditor(Object editor) {
		this.editor = editor;
	}
	
	public PropertyGridRow() {}
	
	public PropertyGridRow(String name, String value, String group, Object editor) {
		this.name = name;
		this.value = value;
		this.group = group;
		this.editor = editor;
	}

	/**
	 * 获取属性编辑器的工厂方法<br>
	 * 简单编辑器包括Text, Datebox, Numberbox, Checkbox, Email, Url, 不需要提供后三个参数(全部为null即可)<br>
	 * 范围编辑器包括RangedText和RangedNumber, 需要提供range参数，range参数为至少两个值的int[]，第一个值表示下限，第二个值表示上限<br>
	 * 自定义编辑器(UserDefined)根据最后两个参数type和options生成任何用户自定义的编辑器<br>
	 * 空编辑器返回null值，表示不能编辑
	 * 
	 * @param editType
	 *            由PropertyGridEditor定义好的编辑类型
	 * @param range
	 *            范围编辑器需要的范围参数，指定数据上下限，对于RangedText表示最小和最大长度，对于RangedNumber表示最小值和最大值
	 * @param type
	 *            自定义编辑器的编辑器类型名称
	 * @param options
	 *            自定义编辑器的编辑器参数
	 * @return
	 */
	public static Object createEditor(PropertyGridEditor editType, int[] range, String type, Map<String, Object> options) {
		Object ed = null;
		Map<String, Object> e = new HashMap<String, Object>();
		Map<String, Object> o = new HashMap<String, Object>();
		switch (editType) {
		case Text:
			ed = "text";
			break;
		case Datebox:
			ed = "datebox";
			break;
		case Numberbox:
			ed = "numberbox";
			break;
		case Checkbox:
			e.put("type", "checkbox");
			o.put("on", true);
			o.put("off", false);
			e.put("options", o);
			ed = e;
		case Email:
			e.put("type", "textbox");
			o.put("validType", "email");
			e.put("options", o);
			ed = e;
			break;
		case Url:
			e.put("type", "textbox");
			o.put("validType", "url");
			e.put("options", o);
			ed = e;
			break;
		case RangedText:
			e.put("type", "textbox");
			o.put("validType", "length[" + range[0] + "," + range[1] + "]");
			e.put("options", o);
			ed = e;
			break;
		case RangedNumber:
			e.put("type", "numberbox");
			o.put("min", range[0]);
			o.put("max", range[1]);
			e.put("options", o);
			ed = e;
			break;
		case Null:
			ed = null;
			break;
		case UserDefined:
			e.put("type", type);
			e.put("options", options);
			ed = e;
		default:
		}
		return ed;
	}
}
