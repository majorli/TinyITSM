package com.jeans.tinyitsm.model.cloud;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.struts2.json.annotations.JSON;

import com.jeans.tinyitsm.model.portal.User;

@Entity
@Table(name = "subscribes")
public class Subscribe implements Serializable {

	private long id;
	private User subscriber;
	private CloudList list;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@JSON(serialize = false)
	@ManyToOne
	@JoinColumn(nullable = false, name = "subscriber_id", foreignKey = @ForeignKey(name = "FK_SUBSCRIBER"))
	public User getSubscriber() {
		return subscriber;
	}

	public void setSubscriber(User subscriber) {
		this.subscriber = subscriber;
	}

	@JSON(serialize = false)
	@ManyToOne
	@JoinColumn(nullable = false, name = "list_id", foreignKey = @ForeignKey(name = "FK_SUBSCRIBED_LIST"))
	public CloudList getList() {
		return list;
	}

	public void setList(CloudList list) {
		this.list = list;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((list == null) ? 0 : list.hashCode());
		result = prime * result + ((subscriber == null) ? 0 : subscriber.hashCode());
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
		Subscribe other = (Subscribe) obj;
		if (id != other.id)
			return false;
		if (list == null) {
			if (other.list != null)
				return false;
		} else if (!list.equals(other.list))
			return false;
		if (subscriber == null) {
			if (other.subscriber != null)
				return false;
		} else if (!subscriber.equals(other.subscriber))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Subscribe [id=").append(id).append(", subscriber=").append(subscriber.getUsername()).append(", list=").append(list.getName())
				.append("]");
		return builder.toString();
	}
}
