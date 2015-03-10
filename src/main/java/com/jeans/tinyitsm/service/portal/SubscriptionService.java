package com.jeans.tinyitsm.service.portal;

import java.util.List;

import com.jeans.tinyitsm.model.portal.User;
import com.jeans.tinyitsm.model.view.CloudNoti;

public interface SubscriptionService {

	/**
	 * 检查某用户的未读订阅消息，包括推送消息
	 * 
	 * @param userId
	 *            用户Id
	 * @return
	 */
	public long checkNews(long userId);

	/**
	 * 加载最新的订阅消息直到第id条的前一条，如果id<=0则加载当前最新的10条，加载过的消息自动设置为非新消息
	 * 
	 * @param id
	 * @param user
	 * @return
	 */
	public List<CloudNoti> refresh(long id, User user);

	/**
	 * 加载从第id条之后开始的10条订阅消息，如果id<=0则加载当前最新的10条，加载过的消息自动设置为非新消息
	 * 
	 * @param id
	 * @param user
	 * @return
	 */
	public List<CloudNoti> more(long id, User user);
}
