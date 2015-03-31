package com.jeans.tinyitsm.model.it;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.jeans.tinyitsm.model.hr.Employee;
import com.jeans.tinyitsm.service.it.enums.TeamType;

@Entity
@Table(name = "teams")
@Inheritance(strategy = InheritanceType.JOINED)
public class Team implements Serializable {

	private long id;
	private String name;
	private TeamType type;
	private Set<Employee> staff = new HashSet<Employee>();
	private Employee leader;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(nullable = false, length = 64)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Enumerated(EnumType.ORDINAL)
	@Column(nullable = false)
	public TeamType getType() {
		return type;
	}

	public void setType(TeamType type) {
		this.type = type;
	}

	@ManyToMany
	@JoinTable(name = "teams_staff", joinColumns = { @JoinColumn(nullable = false, name = "team_id", referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(nullable = false, name = "empl_id", referencedColumnName = "id") })
	public Set<Employee> getStaff() {
		return staff;
	}

	public void setStaff(Set<Employee> staff) {
		this.staff = staff;
	}

	public void addStaff(Employee employee) {
		this.staff.add(employee);
	}

	public void removeStaff(Employee employee) {
		this.staff.remove(employee);
		if (null != this.leader && this.leader.equals(employee)) {
			this.leader = null;
		}
	}

	@ManyToOne
	@JoinColumn(nullable = false, name = "leader_id", foreignKey = @ForeignKey(name = "FK_TEAM_LEADER"))
	public Employee getLeader() {
		return leader;
	}

	public void setLeader(Employee leader) {
		this.leader = leader;
		this.staff.add(leader);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((leader == null) ? 0 : leader.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((staff == null) ? 0 : staff.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		Team other = (Team) obj;
		if (id != other.id)
			return false;
		if (leader == null) {
			if (other.leader != null)
				return false;
		} else if (!leader.equals(other.leader))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (staff == null) {
			if (other.staff != null)
				return false;
		} else if (!staff.equals(other.staff))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Team [id=").append(id).append(", name=").append(name).append(", leader=").append(leader.getName()).append("]");
		return builder.toString();
	}
}
