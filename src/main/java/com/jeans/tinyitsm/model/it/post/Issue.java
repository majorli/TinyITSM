package com.jeans.tinyitsm.model.it.post;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

import com.jeans.tinyitsm.model.hr.Employee;
import com.jeans.tinyitsm.model.it.IssueList;
import com.jeans.tinyitsm.service.it.enums.IssueType;

@Entity
@Table(name = "issues")
@PrimaryKeyJoinColumn(name = "id")
@Indexed
public class Issue extends Post {

	private String title;
	private IssueList list;
	private IssueType type;
	private List<IssueReply> replies = new ArrayList<IssueReply>();
	private boolean closed;
	private Employee closer;
	private Date closeTime;

	@Column(nullable = false, length = 255)
	@Field
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@ManyToOne
	@JoinColumn(nullable = false, name = "issue_list_id", foreignKey = @ForeignKey(name = "FK_ISSUE_LIST"))
	public IssueList getList() {
		return list;
	}

	public void setList(IssueList list) {
		this.list = list;
	}

	@Enumerated(EnumType.ORDINAL)
	@Column(nullable = false)
	public IssueType getType() {
		return type;
	}

	public void setType(IssueType type) {
		this.type = type;
	}

	@OneToMany(mappedBy = "issue")
	public List<IssueReply> getReplies() {
		return replies;
	}

	public void setReplies(List<IssueReply> replies) {
		this.replies = replies;
	}

	@Column(nullable = false)
	public boolean isClosed() {
		return closed;
	}

	public void setClosed(boolean closed) {
		this.closed = closed;
	}

	@ManyToOne
	@JoinColumn(name = "closer_id", foreignKey = @ForeignKey(name = "FK_ISSUE_CLOSER"))
	public Employee getCloser() {
		return closer;
	}

	public void setCloser(Employee closer) {
		this.closer = closer;
	}

	@Column(name = "close_time")
	public Date getCloseTime() {
		return closeTime;
	}

	public void setCloseTime(Date closeTime) {
		this.closeTime = closeTime;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((closeTime == null) ? 0 : closeTime.hashCode());
		result = prime * result + (closed ? 1231 : 1237);
		result = prime * result + ((closer == null) ? 0 : closer.hashCode());
		result = prime * result + ((list == null) ? 0 : list.hashCode());
		result = prime * result + ((replies == null) ? 0 : replies.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Issue other = (Issue) obj;
		if (closeTime == null) {
			if (other.closeTime != null)
				return false;
		} else if (!closeTime.equals(other.closeTime))
			return false;
		if (closed != other.closed)
			return false;
		if (closer == null) {
			if (other.closer != null)
				return false;
		} else if (!closer.equals(other.closer))
			return false;
		if (list == null) {
			if (other.list != null)
				return false;
		} else if (!list.equals(other.list))
			return false;
		if (replies == null) {
			if (other.replies != null)
				return false;
		} else if (!replies.equals(other.replies))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Issue [title=").append(title).append(", list=").append(list.getName()).append(", type=").append(type.getTitle()).append(", closed=")
				.append(closed).append(", closer=").append(closer.getName()).append(", closeTime=").append(closeTime).append("]");
		return builder.toString();
	}
}
