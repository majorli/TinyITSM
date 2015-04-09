package com.jeans.tinyitsm.model.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.jeans.tinyitsm.model.it.ITSystem;
import com.jeans.tinyitsm.model.it.Project;

public class ProjectItem implements Serializable {

	private Project project;

	public ProjectItem() {
		this.project = null;
	}

	public ProjectItem(Project project) {
		this.project = project;
	}

	public Long getId() {
		return null == project ? null : project.getId();
	}

	public String getName() {
		return null == project ? null : project.getName();
	}

	public String getOwner() {
		return null == project ? null : project.getOwner().getAlias();
	}

	public String getSystems() {
		if (null == project) {
			return null;
		} else {
			List<String> names = new ArrayList<String>();
			Set<ITSystem> systems = project.getSystems();
			for (ITSystem system : systems) {
				names.add(system.getAlias());
			}
			return StringUtils.join(names, ",");
		}
	}

	public String getType() {
		return null == project ? null : project.getType().getTitle();
	}

	public String getStage() {
		return null == project ? null : project.getStage().getTitle();
	}

	public String getConstructor() {
		return null == project ? null : project.getConstructor();
	}

	public String getTeam() {
		return null == project ? null : project.getProjectTeam().getName();
	}
}
