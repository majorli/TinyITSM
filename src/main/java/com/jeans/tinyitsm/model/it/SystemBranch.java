package com.jeans.tinyitsm.model.it;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.jeans.tinyitsm.model.asset.Asset;
import com.jeans.tinyitsm.model.hr.Employee;
import com.jeans.tinyitsm.model.hr.Organization;
import com.jeans.tinyitsm.service.it.enums.SystemStage;

@Entity
@Table(name = "system_branches")
public class SystemBranch implements Serializable {

	private long id;
	private System system;
	private Organization company;
	private Employee administrator;
	private Set<Asset> components = new HashSet<Asset>();
	private BigDecimal cost;
	private Date constructedTime;
	private Date abandonedTime;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@ManyToOne
	@JoinColumn(nullable = false, name = "system_id", foreignKey = @ForeignKey(name = "FK_SYS_BRANCH"))
	public System getSystem() {
		return system;
	}

	public void setSystem(System system) {
		this.system = system;
	}

	@ManyToOne
	@JoinColumn(nullable = false, name = "company_id", foreignKey = @ForeignKey(name = "FK_BRANCH_COMPANY"))
	public Organization getCompany() {
		return company;
	}

	public void setCompany(Organization company) {
		this.company = company;
	}

	@ManyToOne
	@JoinColumn(nullable = false, name = "admin_id", foreignKey = @ForeignKey(name = "FK_BRANCH_ADMIN"))
	public Employee getAdministrator() {
		return administrator;
	}

	public void setAdministrator(Employee administrator) {
		this.administrator = administrator;
	}

	@ManyToMany
	@JoinTable(name = "sysbranches_assets", joinColumns = { @JoinColumn(nullable = false, name = "branch_id", referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(nullable = false, name = "asset_id", referencedColumnName = "id") })
	public Set<Asset> getComponents() {
		return components;
	}

	public void setComponents(Set<Asset> components) {
		this.components = components;
	}

	public void addComponent(Asset component) {
		if (null != component) {
			this.components.add(component);
		}
	}

	public void removeComponent(Asset component) {
		this.components.remove(component);
	}

	@Column(nullable = false)
	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	@Column(nullable = true, name = "constructed_time")
	public Date getConstructedTime() {
		return constructedTime;
	}

	public void setConstructedTime(Date constructedTime) {
		this.constructedTime = constructedTime;
	}

	@Column(nullable = true, name = "abandoned_time")
	public Date getAbandonedTime() {
		return abandonedTime;
	}

	public void setAbandonedTime(Date abandonedTime) {
		this.abandonedTime = abandonedTime;
	}

	public SystemStage getStage() {
		if (null == this.constructedTime) {
			return SystemStage.Constructing;
		} else if (null == this.abandonedTime) {
			return SystemStage.Operating;
		} else {
			return SystemStage.Abandoned;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((abandonedTime == null) ? 0 : abandonedTime.hashCode());
		result = prime * result + ((administrator == null) ? 0 : administrator.hashCode());
		result = prime * result + ((company == null) ? 0 : company.hashCode());
		result = prime * result + ((constructedTime == null) ? 0 : constructedTime.hashCode());
		result = prime * result + ((cost == null) ? 0 : cost.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((system == null) ? 0 : system.hashCode());
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
		SystemBranch other = (SystemBranch) obj;
		if (abandonedTime == null) {
			if (other.abandonedTime != null)
				return false;
		} else if (!abandonedTime.equals(other.abandonedTime))
			return false;
		if (administrator == null) {
			if (other.administrator != null)
				return false;
		} else if (!administrator.equals(other.administrator))
			return false;
		if (company == null) {
			if (other.company != null)
				return false;
		} else if (!company.equals(other.company))
			return false;
		if (constructedTime == null) {
			if (other.constructedTime != null)
				return false;
		} else if (!constructedTime.equals(other.constructedTime))
			return false;
		if (cost == null) {
			if (other.cost != null)
				return false;
		} else if (!cost.equals(other.cost))
			return false;
		if (id != other.id)
			return false;
		if (system == null) {
			if (other.system != null)
				return false;
		} else if (!system.equals(other.system))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SystemBranch [id=").append(id).append(", system=").append(system.getAlias()).append(", company=").append(company.getAlias())
				.append(", administrator=").append(administrator.getName()).append(", cost=").append(cost).append(", constructedTime=").append(constructedTime)
				.append(", abandonedTime=").append(abandonedTime).append("]");
		return builder.toString();
	}
}
