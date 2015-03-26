package com.jeans.tinyitsm.event;

import com.jeans.tinyitsm.event.itsm.Event;
import com.jeans.tinyitsm.model.it.Project;

public class ProjectEvent implements Event<ProjectEventType> {

	private ProjectEventType type;
	private Project target;

	@Override
	public ProjectEventType getType() {
		return type;
	}

	public void setType(ProjectEventType type) {
		this.type = type;
	}

	@Override
	public Project getTarget() {
		return target;
	}

	public void setTarget(Project target) {
		this.target = target;
	}

	@Override
	public String getMessage() {
		return null;
	}
}
