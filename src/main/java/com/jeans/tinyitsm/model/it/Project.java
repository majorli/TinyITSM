package com.jeans.tinyitsm.model.it;

import java.io.Serializable;

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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

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
	private System system;
	private ProjectType type;
	private ProjectStage stage;
	private String constructor;
	private Wiki wiki;
	private Repository repository;
	private IssueList issueList;
	private Team ProjectTeam;
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

	@OneToOne
	@JoinColumn(nullable = false, name = "system_id", foreignKey = @ForeignKey(name = "FK_PRJ_SYS"), unique = true)
	public System getSystem() {
		return system;
	}

	public void setSystem(System system) {
		this.system = system;
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
	@JoinColumn(nullable = false, name = "wiki_id", foreignKey = @ForeignKey(name = "FK_PRJ_WIKI"), unique = true)
	public Wiki getWiki() {
		return wiki;
	}

	public void setWiki(Wiki wiki) {
		this.wiki = wiki;
	}

	@OneToOne
	@JoinColumn(nullable = false, name = "repo_id", foreignKey = @ForeignKey(name = "FK_PRJ_REPO"), unique = true)
	public Repository getRepository() {
		return repository;
	}

	public void setRepository(Repository repository) {
		this.repository = repository;
	}

	@OneToOne
	@JoinColumn(nullable = false, name = "issue_list_id", foreignKey = @ForeignKey(name = "FK_PRJ_ISS_LIST"), unique = true)
	public IssueList getIssueList() {
		return issueList;
	}

	public void setIssueList(IssueList issueList) {
		this.issueList = issueList;
	}

	@ManyToOne
	@JoinColumn(nullable = false, name = "team_id", foreignKey = @ForeignKey(name = "FK_PRJ_TEAM"))
	public Team getProjectTeam() {
		return ProjectTeam;
	}

	public void setProjectTeam(Team projectTeam) {
		ProjectTeam = projectTeam;
	}

	@OneToOne
	@JoinColumn(name = "plan_id", foreignKey = @ForeignKey(name = "FK_PRJ_PLAN"))
	public ProjectPlan getPlan() {
		return plan;
	}

	public void setPlan(ProjectPlan plan) {
		this.plan = plan;
	}
}
