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
import javax.persistence.Transient;

import org.apache.struts2.json.annotations.JSON;

import com.jeans.tinyitsm.service.portal.MessageConstants;

@Entity
@Table(name = "notifications", indexes = { @Index(columnList = "publish_time", name = "IDX_TIME"), @Index(columnList = "company_id", name = "IDX_COMP") })
public class Notification implements Serializable {

	private long id;
	private byte source;
	private String text;
	private long companyId;
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
	public byte getSource() {
		return source;
	}

	public void setSource(byte source) {
		this.source = source;
	}

	@Transient
	public String getSourceName() {
		if (source < 0 || source >= MessageConstants.NOTIFICATION_SOURCE_NAMES.length)
			return "未知来源";
		else
			return MessageConstants.NOTIFICATION_SOURCE_NAMES[source];
	}

	@Column(nullable = false)
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@JSON(serialize = false)
	@Column(nullable = false, name = "company_id")
	public long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}

	@JSON(format = "yyyy-MM-dd HH:mm:ss")
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
		result = prime * result + source;
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
		Notification other = (Notification) obj;
		if (id != other.id)
			return false;
		if (publishTime == null) {
			if (other.publishTime != null)
				return false;
		} else if (!publishTime.equals(other.publishTime))
			return false;
		if (source != other.source)
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
		builder.append("Notification [id=").append(id).append(", source=").append(source).append(", text=").append(text).append(", companyId=")
				.append(companyId).append(", publishTime=").append(publishTime).append("]");
		return builder.toString();
	}
}
