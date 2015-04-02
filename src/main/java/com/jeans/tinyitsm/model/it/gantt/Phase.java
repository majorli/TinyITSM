package com.jeans.tinyitsm.model.it.gantt;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

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
import javax.persistence.Transient;

import com.jeans.tinyitsm.model.hr.Employee;
import com.jeans.tinyitsm.model.it.ProjectPlan;
import com.jeans.tinyitsm.model.portal.Task;
import com.jeans.tinyitsm.service.it.enums.PhaseState;
import com.jeans.tinyitsm.service.it.enums.ProjectStage;

@Entity
@Table(name = "gantt_phases")
public class Phase implements Serializable {

	private long id;
	private ProjectPlan plan;
	private String name;
	private String description;
	private byte phaseLevel;
	private int phaseOrder;
	private Employee operator;
	private Date startTime;
	private Date finishTime;
	private ProjectStage nextStage;
	private Milestone milestone;
	private Date startedTime;
	private Date finishedTime;
	private Task task;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@ManyToOne
	@JoinColumn(nullable = false, name = "plan_id", foreignKey = @ForeignKey(name = "FK_PHASE_PLAN"))
	public ProjectPlan getPlan() {
		return plan;
	}

	public void setPlan(ProjectPlan plan) {
		this.plan = plan;
	}

	@Column(nullable = false, length = 64)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(nullable = false, length = 255)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(nullable = false, name = "phase_level")
	public byte getPhaseLevel() {
		return phaseLevel;
	}

	public void setPhaseLevel(byte phaseLevel) {
		this.phaseLevel = phaseLevel;
	}

	@Column(nullable = false, name = "phase_order")
	public int getPhaseOrder() {
		return phaseOrder;
	}

	public void setPhaseOrder(int phaseOrder) {
		this.phaseOrder = phaseOrder;
	}

	@ManyToOne
	@JoinColumn(nullable = false, name = "operator_id", foreignKey = @ForeignKey(name = "FK_PHASE_OPER"))
	public Employee getOperator() {
		return operator;
	}

	public void setOperator(Employee operator) {
		this.operator = operator;
	}

	@Column(nullable = false, name = "date_to_start")
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@Column(nullable = false, name = "date_to_finish")
	public Date getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}

	@Enumerated(EnumType.ORDINAL)
	@Column(nullable = true, name = "next_stage")
	public ProjectStage getNextStage() {
		return nextStage;
	}

	public void setNextStage(ProjectStage nextStage) {
		this.nextStage = nextStage;
	}

	@OneToOne
	@JoinColumn(nullable = true, name = "ms_id", foreignKey = @ForeignKey(name = "FK_PHASE_MS"))
	public Milestone getMilestone() {
		return milestone;
	}

	public void setMilestone(Milestone milestone) {
		this.milestone = milestone;
	}

	@Column(nullable = true, name = "started_date")
	public Date getStartedTime() {
		return startedTime;
	}

	public void setStartedTime(Date startedTime) {
		this.startedTime = startedTime;
	}

	@Column(nullable = true, name = "finished_date")
	public Date getFinishedTime() {
		return finishedTime;
	}

	public void setFinishedTime(Date finishedTime) {
		this.finishedTime = finishedTime;
	}

	@OneToOne
	@JoinColumn(nullable = true, name = "task_id", foreignKey = @ForeignKey(name = "FK_PHASE_TASK"))
	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	@Transient
	public PhaseState getState() {
		PhaseState state = null;
		Calendar today = Calendar.getInstance(), start = Calendar.getInstance(), finish = Calendar.getInstance();
		today.setTime(new Date());
		start.setTime(this.startTime);
		finish.setTime(this.finishTime);
		if (null == this.startedTime) {
			// has not been started yet
			if (today.after(start)) {
				state = PhaseState.OverdueToStart;
			} else {
				state = PhaseState.WaitingForStart;
			}
		} else {
			// has been started
			if (null == this.finishedTime) {
				// but has not been finished
				if (today.after(finish)) {
					state = PhaseState.OverdueToFinish;
				} else {
					state = PhaseState.Started;
				}
			} else {
				// has been finished
				state = PhaseState.Finished;
			}
		}
		return state;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((finishTime == null) ? 0 : finishTime.hashCode());
		result = prime * result + ((finishedTime == null) ? 0 : finishedTime.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((milestone == null) ? 0 : milestone.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((nextStage == null) ? 0 : nextStage.hashCode());
		result = prime * result + ((operator == null) ? 0 : operator.hashCode());
		result = prime * result + phaseLevel;
		result = prime * result + phaseOrder;
		result = prime * result + ((startTime == null) ? 0 : startTime.hashCode());
		result = prime * result + ((startedTime == null) ? 0 : startedTime.hashCode());
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
		Phase other = (Phase) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (finishTime == null) {
			if (other.finishTime != null)
				return false;
		} else if (!finishTime.equals(other.finishTime))
			return false;
		if (finishedTime == null) {
			if (other.finishedTime != null)
				return false;
		} else if (!finishedTime.equals(other.finishedTime))
			return false;
		if (id != other.id)
			return false;
		if (milestone == null) {
			if (other.milestone != null)
				return false;
		} else if (!milestone.equals(other.milestone))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (nextStage != other.nextStage)
			return false;
		if (operator == null) {
			if (other.operator != null)
				return false;
		} else if (!operator.equals(other.operator))
			return false;
		if (phaseLevel != other.phaseLevel)
			return false;
		if (phaseOrder != other.phaseOrder)
			return false;
		if (startTime == null) {
			if (other.startTime != null)
				return false;
		} else if (!startTime.equals(other.startTime))
			return false;
		if (startedTime == null) {
			if (other.startedTime != null)
				return false;
		} else if (!startedTime.equals(other.startedTime))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Phase [id=").append(id).append(", name=").append(name).append(", description=").append(description).append(", startedTime=")
				.append(startedTime).append(", finishedTime=").append(finishedTime).append("]");
		return builder.toString();
	}
}
