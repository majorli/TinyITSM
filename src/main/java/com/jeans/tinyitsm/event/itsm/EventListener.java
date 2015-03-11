package com.jeans.tinyitsm.event.itsm;

/**
 * 事件监听器基础接口
 * 
 * @author Majorli
 *
 */
public interface EventListener<T extends Event<? extends EventType>> {

	/**
	 * 触发一次事件
	 * 
	 * @param event
	 *            事件对象
	 */
	public void fired(T event);
}
