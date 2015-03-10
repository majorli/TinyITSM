package com.jeans.tinyitsm.event.itsm;

import java.io.Serializable;

/**
 * 事件的基础接口：所有事件对象需实现此接口以提供基本功能
 * 
 * @author Majorli
 *
 */
public interface Event<T extends EventType> {

	/**
	 * 获取事件的描述，一般用于生成日志文本
	 * 
	 * @return
	 */
	public String getMessage();

	/**
	 * 获取事件的子类型，如事件没有子类型，返回null
	 * 
	 * @return
	 */
	public T getType();

	/**
	 * 获取产生事件的目标对象
	 * 
	 * @return
	 */
	public Serializable getTarget();
}
