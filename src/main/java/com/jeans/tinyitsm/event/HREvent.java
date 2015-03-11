package com.jeans.tinyitsm.event;

import java.util.LinkedHashMap;
import java.util.Map;

public class HREvent implements BaseEvent {

	private Map<Long, String> targets;
	private long compId;
	private HREventType type;
	private long newCompId;

	@Override
	public Map<Long, String> getTargets() {
		return targets;
	}

	public void setTargets(Map<Long, String> targets) {
		this.targets = targets;
	}

	public long getCompId() {
		return compId;
	}

	public void setCompId(long compId) {
		this.compId = compId;
	}

	@Override
	public HREventType getType() {
		return type;
	}

	public void setType(HREventType type) {
		this.type = type;
	}
	
	@Override
	public void addTarget(long id, String name) {
		if (null == targets)
			targets = new LinkedHashMap<Long, String>();
		targets.put(id, name);
	}

	/**
	 * only useful when move employee
	 */
	public long getNewCompId() {
		return newCompId;
	}

	public void setNewCompId(long newCompId) {
		this.newCompId = newCompId;
	}

	@Override
	public String getMessage() {
		StringBuilder builder = new StringBuilder("HR事件，");
		builder.append(type.getTitle()).append("：变更对象=").append(targets).append("，公司编号=").append(compId);
		if (type == HREventType.EmployeeMoved && compId != newCompId)
			builder.append("，新公司编号=").append(newCompId);
		return builder.toString();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("HREvent [getMessage()=").append(getMessage()).append("]");
		return builder.toString();
	}
}
