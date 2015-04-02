package com.jeans.tinyitsm.model.portal;

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
import javax.persistence.Table;
import javax.persistence.Transient;

import com.jeans.tinyitsm.model.hr.Employee;
import com.jeans.tinyitsm.service.portal.TaskState;
import com.jeans.tinyitsm.service.portal.TaskType;

@Entity
@Table(name = "tasks")
public class Task implements Serializable {

	private long id;
	private TaskType type;
	private String description;
	private Date dueDate;
	private Date remindDate;
	private Date completionDate;
	private Employee Owner;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Enumerated(EnumType.ORDINAL)
	@Column(nullable = false)
	public TaskType getType() {
		return type;
	}

	public void setType(TaskType type) {
		this.type = type;
	}

	@Column(nullable = false, length = 255)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(nullable = false, name = "due_date")
	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	@Column(nullable = true, name = "remind_date")
	public Date getRemindDate() {
		return remindDate;
	}

	public void setRemindDate(Date remindDate) {
		this.remindDate = remindDate;
	}

	@Column(nullable = true, name = "completion_date")
	public Date getCompletionDate() {
		return completionDate;
	}

	public void setCompletionDate(Date completionDate) {
		this.completionDate = completionDate;
	}

	@ManyToOne
	@JoinColumn(nullable = false, name = "owner_id", foreignKey = @ForeignKey(name = "FK_TASK_OWNER"))
	public Employee getOwner() {
		return Owner;
	}

	public void setOwner(Employee owner) {
		Owner = owner;
	}

	@Transient
	public TaskState getState() {
		if (null == this.completionDate) {
			// not completed
			Calendar today = Calendar.getInstance(), due = Calendar.getInstance(), remind = Calendar.getInstance();
			today.setTime(new Date());
			due.setTime(this.dueDate);
			if (null == this.remindDate) {
				remind = null;
			} else {
				remind.setTime(this.remindDate);
			}
			if (today.after(due)) {
				return TaskState.Overdue;
			} else {
				if (null != remind && today.after(remind)) {
					return TaskState.Reminding;
				} else {
					return TaskState.Normal;
				}
			}
		} else {
			return TaskState.Completed;
		}
	}

	@Transient
	public boolean isCompleted() {
		return null != this.completionDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Owner == null) ? 0 : Owner.hashCode());
		result = prime * result + ((completionDate == null) ? 0 : completionDate.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((dueDate == null) ? 0 : dueDate.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((remindDate == null) ? 0 : remindDate.hashCode());
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
		Task other = (Task) obj;
		if (Owner == null) {
			if (other.Owner != null)
				return false;
		} else if (!Owner.equals(other.Owner))
			return false;
		if (completionDate == null) {
			if (other.completionDate != null)
				return false;
		} else if (!completionDate.equals(other.completionDate))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (dueDate == null) {
			if (other.dueDate != null)
				return false;
		} else if (!dueDate.equals(other.dueDate))
			return false;
		if (id != other.id)
			return false;
		if (remindDate == null) {
			if (other.remindDate != null)
				return false;
		} else if (!remindDate.equals(other.remindDate))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Task [id=").append(id).append(", type=").append(type.getTitle()).append(", description=").append(description).append(", dueDate=")
				.append(dueDate).append(", remindDate=").append(remindDate).append(", completionDate=").append(completionDate).append(", Owner=")
				.append(Owner.getName()).append("]");
		return builder.toString();
	}
}
