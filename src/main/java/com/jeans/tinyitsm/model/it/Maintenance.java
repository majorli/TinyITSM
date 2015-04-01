package com.jeans.tinyitsm.model.it;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

@Entity
@Table(name = "maintenance")
@Indexed
public class Maintenance implements Serializable {

	private long id;
	private String name;
	private Set<System> systems = new HashSet<System>();
	private String provider;
	private Repository repository;
	private Team maintainTeam;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(nullable = false, length = 64)
	@Field
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@OneToMany(mappedBy = "maintenance")
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

	@Column(nullable = false, length = 64)
	@Field
	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	@OneToOne
	@JoinColumn(nullable = false, name = "repo_id", foreignKey = @ForeignKey(name = "FK_MTN_REPO"), unique = true)
	public Repository getRepository() {
		return repository;
	}

	public void setRepository(Repository repository) {
		this.repository = repository;
	}

	@ManyToOne
	@JoinColumn(nullable = false, name = "team_id", foreignKey = @ForeignKey(name = "FK_MTN_TEAM"))
	public Team getMaintainTeam() {
		return maintainTeam;
	}

	public void setMaintainTeam(Team maintainTeam) {
		this.maintainTeam = maintainTeam;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((provider == null) ? 0 : provider.hashCode());
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
		Maintenance other = (Maintenance) obj;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (provider == null) {
			if (other.provider != null)
				return false;
		} else if (!provider.equals(other.provider))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Maintenance [id=").append(id).append(", name=").append(name).append(", provider=").append(provider).append("]");
		return builder.toString();
	}
}
