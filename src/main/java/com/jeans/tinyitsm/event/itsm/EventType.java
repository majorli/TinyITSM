package com.jeans.tinyitsm.event.itsm;

/**
 * 事件子类型基础接口：一种事件可以细分为多个子类型，比如云存储事件中的新文件上传子类型等
 * 一般用一个enum来明确限定事件类型，继承本接口提供获取子类型Title的能力
 * 
 * @author Majorli
 *
 */
public interface EventType {

	/**
	 * 获取事件子类型的标题
	 * 
	 * @return
	 */
	public String getTitle();
}
