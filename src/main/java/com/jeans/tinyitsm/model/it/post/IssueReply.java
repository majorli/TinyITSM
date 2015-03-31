package com.jeans.tinyitsm.model.it.post;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;


@Entity
@Table(name = "issue_replies")
@PrimaryKeyJoinColumn(name = "id")
public class IssueReply extends Post {

	private Issue issue;
	private int listOrder;

	@ManyToOne
	@JoinColumn(nullable = false, name = "issue_id", foreignKey = @ForeignKey(name = "FK_ISSUE_REPLY"))
	public Issue getIssue() {
		return issue;
	}

	public void setIssue(Issue issue) {
		this.issue = issue;
	}

	@Column(nullable = false, name = "list_order")
	public int getListOrder() {
		return listOrder;
	}

	public void setListOrder(int listOrder) {
		this.listOrder = listOrder;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("IssueReply [issue=").append(issue.getTitle()).append(", listOrder=").append(listOrder).append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((issue == null) ? 0 : issue.hashCode());
		result = prime * result + listOrder;
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
		IssueReply other = (IssueReply) obj;
		if (issue == null) {
			if (other.issue != null)
				return false;
		} else if (!issue.equals(other.issue))
			return false;
		if (listOrder != other.listOrder)
			return false;
		return true;
	}
}
