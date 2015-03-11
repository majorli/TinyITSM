package com.jeans.tinyitsm.event;

import java.util.Map;

public interface BaseEvent {
	public String getMessage();
	public Map<Long, String> getTargets();
	public BaseEventType getType();
	public void addTarget(long id, String name);
}
