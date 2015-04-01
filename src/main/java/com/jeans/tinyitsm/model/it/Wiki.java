package com.jeans.tinyitsm.model.it;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.jeans.tinyitsm.model.it.post.Page;

@Entity
@Table(name = "wikies")
public class Wiki implements Serializable {

	private long id;
	private Set<System> systems = new HashSet<System>();
	private List<Page> pages = new ArrayList<Page>();

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@OneToMany(mappedBy = "wiki")
	public Set<System> getSystems() {
		return systems;
	}

	public void setSystems(Set<System> systems) {
		this.systems = systems;
	}

	public void addSystem(System system) {
		if (null != system) {
			this.systems.add(system);
		}
	}

	public void removeSystem(System system) {
		this.systems.remove(system);
	}

	@OneToMany(mappedBy = "wiki")
	public List<Page> getPages() {
		return pages;
	}

	public void setPages(List<Page> pages) {
		this.pages = pages;
	}

	public void addPage(Page page) {
		if (null != page) {
			this.pages.add(page);
		}
	}

	public void removePage(Page page) {
		this.pages.remove(page);
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
		Wiki other = (Wiki) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Wiki [id=").append(id).append(", systems=").append(systems).append("]");
		return builder.toString();
	}
}
