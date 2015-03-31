package com.jeans.tinyitsm.event;

import java.io.Serializable;

import com.jeans.tinyitsm.event.itsm.Event;
import com.jeans.tinyitsm.model.it.Project;

public class ProjectEvent implements Event<ProjectEventType> {

	private ProjectEventType type;
	private Serializable target;

	@Override
	public ProjectEventType getType() {
		return type;
	}

	public void setType(ProjectEventType type) {
		this.type = type;
	}

	@Override
	public Serializable getTarget() {
		return target;
	}

	public void setTarget(Serializable target) {
		if (target instanceof Project) {
			this.target = target;
		}
	}

	@Override
	public String getMessage() {
		// TODO get project event message
		return null;
	}
}
