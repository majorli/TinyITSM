package com.jeans.tinyitsm.model.portal;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.struts2.json.annotations.JSON;

@Entity
@Table(name = "subscriptions", indexes = { @Index(columnList = "receiver_user_id", name = "IDX_USER") })
public class Subscription implements Serializable {

	private long id;
	private User receiver;
	private Rss rss;
	private boolean newRss;

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
	@JoinColumn(nullable = false, name = "receiver_user_id", foreignKey = @ForeignKey(name = "FK_USER"))
	public User getReceiver() {
		return receiver;
	}

	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}

	@ManyToOne
	@JoinColumn(nullable = false, name = "rss_id", foreignKey = @ForeignKey(name = "FK_RSS"))
	public Rss getRss() {
		return rss;
	}

	public void setRss(Rss rss) {
		this.rss = rss;
	}

	@Column(nullable = false, name = "new_rss")
	public boolean isNewRss() {
		return newRss;
	}

	public void setNewRss(boolean newRss) {
		this.newRss = newRss;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Subscription [id=").append(id).append(", receiver=").append(receiver.getUsername()).append(", rss=").append(rss.getText())
				.append(", newRss=").append(newRss).append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + (newRss ? 1231 : 1237);
		result = prime * result + ((receiver == null) ? 0 : receiver.hashCode());
		result = prime * result + ((rss == null) ? 0 : rss.hashCode());
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
		Subscription other = (Subscription) obj;
		if (id != other.id)
			return false;
		if (newRss != other.newRss)
			return false;
		if (receiver == null) {
			if (other.receiver != null)
				return false;
		} else if (!receiver.equals(other.receiver))
			return false;
		if (rss == null) {
			if (other.rss != null)
				return false;
		} else if (!rss.equals(other.rss))
			return false;
		return true;
	}
}
