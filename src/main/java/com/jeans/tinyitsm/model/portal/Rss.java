package com.jeans.tinyitsm.model.portal;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name = "rss", indexes = { @Index(name = "IDX_TIME", columnList = "publish_time") })
public class Rss implements Serializable {

	private long id;
	private String text;
	private byte type;
	private long targetId;
	private Date publishTime;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(nullable = false)
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Column(nullable = false)
	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	@Column(nullable = false, name = "target_id")
	public long getTargetId() {
		return targetId;
	}

	public void setTargetId(long targetId) {
		this.targetId = targetId;
	}

	@Column(nullable = false, name = "publish_time")
	public Date getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(Date publishTime) {
		this.publishTime = publishTime;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((publishTime == null) ? 0 : publishTime.hashCode());
		result = prime * result + (int) (targetId ^ (targetId >>> 32));
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		result = prime * result + type;
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
		Rss other = (Rss) obj;
		if (id != other.id)
			return false;
		if (publishTime == null) {
			if (other.publishTime != null)
				return false;
		} else if (!publishTime.equals(other.publishTime))
			return false;
		if (targetId != other.targetId)
			return false;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Rss [id=").append(id).append(", text=").append(text).append(", type=").append(type).append(", targetId=").append(targetId)
				.append(", publishTime=").append(publishTime).append("]");
		return builder.toString();
	}
}
