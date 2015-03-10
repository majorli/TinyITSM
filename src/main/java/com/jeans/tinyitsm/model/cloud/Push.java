package com.jeans.tinyitsm.model.cloud;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.apache.struts2.json.annotations.JSON;

import com.jeans.tinyitsm.model.portal.User;

@Entity
@Table(name = "pushes")
public class Push implements Serializable {

	private long id;
	private byte type;
	private long unitId;
	private Set<User> users = new LinkedHashSet<User>();

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(nullable = false)
	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	@Column(nullable = false, name = "unit_id")
	public long getUnitId() {
		return unitId;
	}

	public void setUnitId(long unitId) {
		this.unitId = unitId;
	}

	@JSON(serialize = false)
	@ManyToMany
	@JoinTable(name = "pushes_users", joinColumns = { @JoinColumn(nullable = false, name = "push_id", referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(nullable = false, name = "user_id", referencedColumnName = "id") })
	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + type;
		result = prime * result + (int) (unitId ^ (unitId >>> 32));
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
		Push other = (Push) obj;
		if (id != other.id)
			return false;
		if (type != other.type)
			return false;
		if (unitId != other.unitId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Push [id=").append(id).append(", type=").append(type).append(", unitId=").append(unitId).append("]");
		return builder.toString();
	}
}
