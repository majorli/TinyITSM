package com.jeans.tinyitsm.model.it;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.jeans.tinyitsm.model.cloud.CloudList;

@Entity
@Table(name = "repositories")
public class Repository implements Serializable {
	private long id;
	private Set<CloudList> lists = new HashSet<CloudList>();

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@OneToMany
	@JoinTable(name = "repositories_lists", joinColumns = { @JoinColumn(nullable = false, name = "repo_id", referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(nullable = false, name = "list_id", referencedColumnName = "id") })
	public Set<CloudList> getLists() {
		return lists;
	}

	public void setLists(Set<CloudList> lists) {
		this.lists = lists;
	}

	public void addList(CloudList list) {
		if (null != list) {
			this.lists.add(list);
		}
	}

	public void removeList(CloudList list) {
		this.lists.remove(list);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
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
		Repository other = (Repository) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Repository [id=").append(id).append(", lists=").append(lists).append("]");
		return builder.toString();
	}
}
