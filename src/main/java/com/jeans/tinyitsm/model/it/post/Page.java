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
import com.jeans.tinyitsm.model.it.Wiki;
import com.jeans.tinyitsm.service.it.enums.PageType;

@Entity
@Table(name = "wiki_pages")
@PrimaryKeyJoinColumn(name = "id")
@Indexed
public class Page extends Post {

	private String title;
	private PageType type;
	private Wiki wiki;
	private List<Reply> replies = new ArrayList<Reply>();
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

	@Enumerated(EnumType.ORDINAL)
	@Column(nullable = false)
	public PageType getType() {
		return type;
	}

	public void setType(PageType type) {
		this.type = type;
	}

	@ManyToOne
	@JoinColumn(nullable = false, name = "wiki_id", foreignKey = @ForeignKey(name = "FK_PAGE_WIKI"))
	public Wiki getWiki() {
		return wiki;
	}

	public void setWiki(Wiki wiki) {
		this.wiki = wiki;
	}

	@OneToMany(mappedBy = "page")
	public List<Reply> getReplies() {
		return replies;
	}

	public void setReplies(List<Reply> replies) {
		this.replies = replies;
	}

	public void addReply(Reply reply) {
		if (null != reply) {
			this.replies.add(reply);
		}
	}

	public void removeReply(Reply reply) {
		this.replies.remove(reply);
	}

	@Column(nullable = false)
	public boolean isClosed() {
		return closed;
	}

	public void setClosed(boolean closed) {
		this.closed = closed;
	}

	@ManyToOne
	@JoinColumn(nullable = true, name = "closer_id", foreignKey = @ForeignKey(name = "FK_PAGE_CLOSER"))
	public Employee getCloser() {
		return closer;
	}

	public void setCloser(Employee closer) {
		this.closer = closer;
	}

	@Column(nullable = true, name = "close_time")
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
		Page other = (Page) obj;
		if (closeTime == null) {
			if (other.closeTime != null)
				return false;
		} else if (!closeTime.equals(other.closeTime))
			return false;
		if (closed != other.closed)
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
		builder.append("Page [title=").append(title).append(", type=").append(type).append(", closed=").append(closed).append(", closer=")
				.append(closer.getName()).append(", closeTime=").append(closeTime).append("]");
		return builder.toString();
	}
}
