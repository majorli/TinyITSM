package com.jeans.tinyitsm.event;

import java.io.Serializable;
import java.util.Set;

import com.jeans.tinyitsm.event.itsm.Event;
import com.jeans.tinyitsm.model.CloudUnit;
import com.jeans.tinyitsm.model.portal.User;

public class CloudEvent implements Event<CloudEventType> {

	private CloudEventType type;
	private Serializable target;
	
	private Set<User> users;
	private int count;
	
	public CloudEvent() {}
	
	public CloudEvent(CloudEventType type, CloudUnit target, Set<User> users) {
		this.type = type;
		this.target = target;
		this.users = users;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public String getMessage() {
		if (null == target || !(target instanceof CloudUnit)) {
			return "";
		}
		StringBuilder builder = new StringBuilder("资料库事件，");
		CloudUnit t = (CloudUnit) target;
		builder.append(type.getTitle()).append("：名称=\"").append(t.getName()).append("\"，来自=\"").append(t.getOwner().getUsername()).append("\"");
		return builder.toString();
	}

	public void setType(CloudEventType type) {
		this.type = type;
	}

	@Override
	public CloudEventType getType() {
		return type;
	}

	public void setTarget(Serializable target) {
		if (target instanceof CloudUnit) {
			this.target = target;
		}
	}

	@Override
	public Serializable getTarget() {
		return target;
	}
}
