package com.jeans.tinyitsm.action.project;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.jeans.tinyitsm.action.BaseAction;
import com.jeans.tinyitsm.model.it.Project;
import com.jeans.tinyitsm.model.view.MenuItem;
import com.jeans.tinyitsm.model.view.ProjectItem;
import com.jeans.tinyitsm.service.it.ProjectService;

public class ProjectAction extends BaseAction<ProjectItem> {

	private ProjectService prjService;

	@Autowired
	public void setPrjService(ProjectService prjService) {
		this.prjService = prjService;
	}

	private List<MenuItem> items;

	public List<MenuItem> getItems() {
		return items;
	}

	public void setItems(List<MenuItem> items) {
		this.items = items;
	}

	@Action(value = "prj-list", results = { @Result(type = "json", params = { "root", "items" }) })
	public String loadProjects() throws Exception {
		List<Project> projects = prjService.getProjects(getCurrentCompanyId());
		items = new ArrayList<MenuItem>();
		for (Project project : projects) {
			items.add(new MenuItem(Long.toString(project.getId()), project.getName()));
		}
		return SUCCESS;
	}
}
