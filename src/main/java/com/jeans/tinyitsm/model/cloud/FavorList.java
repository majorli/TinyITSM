package com.jeans.tinyitsm.model.cloud;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.struts2.json.annotations.JSON;

import com.jeans.tinyitsm.model.portal.User;

@Entity
@Table(name = "favor_lists")
public class FavorList implements Serializable {

	private long id;
	private String name;
	private User owner;
	private List<Favorite> favors = new ArrayList<Favorite>();

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@JSON(serialize = false)
	@ManyToOne
	@JoinColumn(nullable = false, name = "owner_id", foreignKey = @ForeignKey(name = "FK_FAVOR_LIST_OWNER"))
	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	@JSON(serialize = false)
	@OneToMany(mappedBy = "list")
	public List<Favorite> getFavors() {
		return favors;
	}

	public void setFavors(List<Favorite> favors) {
		this.favors = favors;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((owner == null) ? 0 : owner.hashCode());
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
		FavorList other = (FavorList) obj;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (owner == null) {
			if (other.owner != null)
				return false;
		} else if (!owner.equals(other.owner))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FavorList [id=").append(id).append(", name=").append(name).append(", owner=").append(owner.getUsername()).append("]");
		return builder.toString();
	}
}
