package com.jeans.tinyitsm.service.portal.impl;

import org.springframework.stereotype.Service;

import com.jeans.tinyitsm.service.portal.TaskService;

@Service
public class TaskServiceImpl implements TaskService {

	@Override
	public long checkNews(long userId) {
		// TODO 检查还没有完成的待办任务
		return -1;
	}

}
