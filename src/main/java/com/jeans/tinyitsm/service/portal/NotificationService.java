package com.jeans.tinyitsm.service.portal;

import java.util.List;

import com.jeans.tinyitsm.event.HREvent;
import com.jeans.tinyitsm.event.itsm.Event;
import com.jeans.tinyitsm.model.portal.Notification;

public interface NotificationService {
	/**
	 * 根据提供的字段值生成一条新的系统公告
	 * 
	 * @param source
	 *            MessageContants类定义的常量
	 * @param text
	 *            公告正文，255个字
	 * @param companyId
	 *            能看到公告的公司ID，0表示所有公司可见
	 * @return
	 */
	public Notification publish(byte source, String text, long companyId);

	/**
	 * 根据TinyHR事件生成公告
	 * 
	 * @param e
	 *            需要生成公告的事件
	 * @return
	 */
	public List<Notification> publish(HREvent e);

	/**
	 * 根据TinyITSM事件生成公告
	 * 
	 * @param e
	 * @return
	 */
	public List<Notification> publish(Event<?> e, List<Long> companyIds);

	/**
	 * 加载最新的系统公告，直到toId的前一条，如果toId <= 0，加载最新的rows条，默认20条
	 * 本方法用于向首页“加载最新”时发送公告列表，刷新公告
	 * 
	 * @param toId
	 *            加载到此条之前
	 * @param rows
	 *            当toId <= 0时获取的公告条数，默认20条(当rows <= 0 时取默认值)
	 * @param companyId
	 *            加载范围
	 * @return
	 */
	public List<Notification> loadNew(long toId, int rows, long companyId);

	/**
	 * 从fromId之后开始加载rows条系统公告，默认20条
	 * 本方法用于向首页“加载更多”时发送公告列表
	 * 
	 * @param fromId
	 *            从该条公告之后开始获取
	 * @param rows
	 *            获取的公告条数，默认20条(当rows <= 0 时取默认值)
	 * @param companyId
	 *            加载范围
	 * @return
	 */
	public List<Notification> loadMore(long fromId, int rows, long companyId);
}
