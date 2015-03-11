package com.jeans.tinyitsm.service.portal;

public interface TaskService {

	/**
	 * 检查用户还没有完成的待办任务，包括还没开始的和正在进行中的
	 * 
	 * @param userId
	 * @return
	 */
	public long checkNews(long userId);
}
