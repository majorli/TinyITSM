package com.jeans.tinyitsm.model.it.post;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

import com.jeans.tinyitsm.model.hr.Employee;

@Entity
@Table(name = "posts")
@Inheritance(strategy = InheritanceType.JOINED)
@Indexed
public class Post implements Serializable {

	private long id;
	private Employee publisher;
	private Date publishTime;
	private Date lastUpdateTime;
	private String text;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@ManyToOne
	@JoinColumn(nullable = false, name = "publisher_id", foreignKey = @ForeignKey(name = "FK_POST_PUBLISHER"))
	public Employee getPublisher() {
		return publisher;
	}

	public void setPublisher(Employee publisher) {
		this.publisher = publisher;
	}

	@Column(nullable = false, name = "publish_time")
	public Date getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(Date publishTime) {
		this.publishTime = publishTime;
	}

	@Column(nullable = false, name = "last_update_time")
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	@Lob
	@Column(columnDefinition = "TEXT", nullable = false)
	@Field
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((lastUpdateTime == null) ? 0 : lastUpdateTime.hashCode());
		result = prime * result + ((publishTime == null) ? 0 : publishTime.hashCode());
		result = prime * result + ((publisher == null) ? 0 : publisher.hashCode());
		result = prime * result + ((text == null) ? 0 : text.hashCode());
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
		Post other = (Post) obj;
		if (id != other.id)
			return false;
		if (lastUpdateTime == null) {
			if (other.lastUpdateTime != null)
				return false;
		} else if (!lastUpdateTime.equals(other.lastUpdateTime))
			return false;
		if (publishTime == null) {
			if (other.publishTime != null)
				return false;
		} else if (!publishTime.equals(other.publishTime))
			return false;
		if (publisher == null) {
			if (other.publisher != null)
				return false;
		} else if (!publisher.equals(other.publisher))
			return false;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Post [id=").append(id).append(", publisher=").append(publisher.getName()).append(", publishTime=").append(publishTime)
				.append(", lastUpdateTime=").append(lastUpdateTime).append("]");
		return builder.toString();
	}
}
