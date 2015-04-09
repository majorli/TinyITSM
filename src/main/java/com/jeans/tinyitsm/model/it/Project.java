package com.jeans.tinyitsm.model.it;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

import com.jeans.tinyitsm.model.hr.Organization;
import com.jeans.tinyitsm.service.hr.HRConstants;
import com.jeans.tinyitsm.service.it.enums.ProjectStage;
import com.jeans.tinyitsm.service.it.enums.ProjectType;

/**
 * 信息化项目实体类
 * 
 * @author Majorli
 *
 */
@Entity
@Table(name = "projects")
@Indexed
public class Project implements Serializable {
	private long id;
	private String name;
	private Organization owner;
	private Set<ITSystem> systems = new HashSet<ITSystem>();
	private ProjectType type;
	private ProjectStage stage;
	private String constructor;
	private Repository repository;
	private Team projectTeam;
	private ProjectPlan plan;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(nullable = false, length = 64)
	@Field
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ManyToOne
	@JoinColumn(nullable = false, name = "owner_org_id", foreignKey = @ForeignKey(name = "FK_PRJ_OWNER"))
	public Organization getOwner() {
		return owner;
	}

	public void setOwner(Organization owner) {
		if (owner.getType() == HRConstants.COMPANY) {
			this.owner = owner;
		}
	}

	@OneToMany(mappedBy = "project")
	public Set<ITSystem> getSystems() {
		return systems;
	}

	public void setSystems(Set<ITSystem> systems) {
		this.systems = systems;
	}

	public void addSystem(ITSystem system) {
		if (null != system) {
			this.systems.add(system);
		}
	}

	public void removeSystem(ITSystem system) {
		this.systems.remove(system);
	}

	@Enumerated(EnumType.ORDINAL)
	@Column(nullable = false)
	public ProjectType getType() {
		return type;
	}

	public void setType(ProjectType type) {
		this.type = type;
	}

	@Enumerated(EnumType.ORDINAL)
	@Column(nullable = false)
	public ProjectStage getStage() {
		return stage;
	}

	public void setStage(ProjectStage stage) {
		this.stage = stage;
	}

	@Column(nullable = false, length = 64)
	@Field
	public String getConstructor() {
		return constructor;
	}

	public void setConstructor(String constructor) {
		this.constructor = constructor;
	}

	@OneToOne
	@JoinColumn(nullable = false, name = "repo_id", foreignKey = @ForeignKey(name = "FK_PRJ_REPO"), unique = true)
	public Repository getRepository() {
		return repository;
	}

	public void setRepository(Repository repository) {
		this.repository = repository;
	}

	@ManyToOne
	@JoinColumn(nullable = false, name = "team_id", foreignKey = @ForeignKey(name = "FK_PRJ_TEAM"))
	public Team getProjectTeam() {
		return projectTeam;
	}

	public void setProjectTeam(Team projectTeam) {
		this.projectTeam = projectTeam;
	}

	@OneToOne
	@JoinColumn(nullable = true, name = "plan_id", foreignKey = @ForeignKey(name = "FK_PRJ_PLAN"), unique = true)
	public ProjectPlan getPlan() {
		return plan;
	}

	public void setPlan(ProjectPlan plan) {
		this.plan = plan;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((constructor == null) ? 0 : constructor.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((stage == null) ? 0 : stage.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		Project other = (Project) obj;
		if (constructor == null) {
			if (other.constructor != null)
				return false;
		} else if (!constructor.equals(other.constructor))
			return false;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (stage != other.stage)
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Project [id=").append(id).append(", name=").append(name).append(", type=").append(type).append(", stage=").append(stage)
				.append(", constructor=").append(constructor).append("]");
		return builder.toString();
	}
}
