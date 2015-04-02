package com.jeans.tinyitsm.model.it;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.jeans.tinyitsm.model.it.gantt.Phase;

@Entity
@Table(name = "project_plans")
public class ProjectPlan implements Serializable {

	private long id;
	private Project project;
	private List<Phase> phases = new ArrayList<Phase>();

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@OneToOne(mappedBy = "plan")
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	@OneToMany(mappedBy = "plan")
	@OrderBy("phaseOrder")
	public List<Phase> getPhases() {
		return phases;
	}

	public void setPhases(List<Phase> phases) {
		this.phases = phases;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((project == null) ? 0 : project.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProjectPlan other = (ProjectPlan) obj;
		if (id != other.id)
			return false;
		if (project == null) {
			if (other.project != null)
				return false;
		} else if (!project.equals(other.project))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ProjectPlan [id=").append(id).append(", project=").append(project.getName()).append("]");
		return builder.toString();
	}
}
