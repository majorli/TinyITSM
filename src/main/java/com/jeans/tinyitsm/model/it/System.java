package com.jeans.tinyitsm.model.it;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

import com.jeans.tinyitsm.model.hr.Organization;
import com.jeans.tinyitsm.service.hr.HRConstants;
import com.jeans.tinyitsm.service.it.enums.SystemScope;
import com.jeans.tinyitsm.service.it.enums.SystemStage;
import com.jeans.tinyitsm.service.it.enums.SystemType;

@Entity
@Table(name = "systems")
@Indexed
public class System implements Serializable {

	private long id;
	private String name;
	private String alias;
	private SystemType type;
	private String modelOrVersion;
	private String brief;
	private byte securityLevel;
	private String securityCode;
	private String usersBrief;
	private String provider;
	private Organization owner;
	private SystemScope scope;
	private SystemScope deploy;
	private Date constructedTime;
	private Date abandonedTime;
	private int freeMaintainMonths;
	private Project project;
	private Maintenance maintenance;
	private Set<SystemBranch> branches = new HashSet<SystemBranch>();
	private Wiki wiki;

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

	@Column(nullable = false, length = 16)
	@Field
	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	@Enumerated(EnumType.ORDINAL)
	@Column(nullable = false)
	public SystemType getType() {
		return type;
	}

	public void setType(SystemType type) {
		this.type = type;
	}

	@Column(nullable = false, name = "model_or_version", length = 64)
	@Field
	public String getModelOrVersion() {
		return modelOrVersion;
	}

	public void setModelOrVersion(String modelOrVersion) {
		this.modelOrVersion = modelOrVersion;
	}

	@Column(nullable = false, length = 255)
	@Field
	public String getBrief() {
		return brief;
	}

	public void setBrief(String brief) {
		this.brief = brief;
	}

	@Column(nullable = false, name = "sec_lvl")
	public byte getSecurityLevel() {
		return securityLevel;
	}

	public void setSecurityLevel(byte securityLevel) {
		this.securityLevel = securityLevel;
	}

	@Column(nullable = false, name = "sec_code", length = 32)
	public String getSecurityCode() {
		return securityCode;
	}

	public void setSecurityCode(String securityCode) {
		this.securityCode = securityCode;
	}

	@Column(nullable = false, name = "users_brief", length = 255)
	public String getUsersBrief() {
		return usersBrief;
	}

	public void setUsersBrief(String usersBrief) {
		this.usersBrief = usersBrief;
	}

	@Column(nullable = false, length = 64)
	@Field
	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	@ManyToOne
	@JoinColumn(nullable = false, name = "owner_org_id", foreignKey = @ForeignKey(name = "FK_SYS_OWNER"))
	public Organization getOwner() {
		return owner;
	}

	public void setOwner(Organization owner) {
		if (owner.getType() == HRConstants.COMPANY) {
			this.owner = owner;
		}
	}

	@Enumerated(EnumType.ORDINAL)
	@Column(nullable = false)
	public SystemScope getScope() {
		return scope;
	}

	public void setScope(SystemScope scope) {
		this.scope = scope;
	}

	@Enumerated(EnumType.ORDINAL)
	@Column(nullable = false)
	public SystemScope getDeploy() {
		return deploy;
	}

	public void setDeploy(SystemScope deploy) {
		if (deploy == SystemScope.Inferiors) {
			this.deploy = SystemScope.DirectInferiors;
		} else if (deploy == SystemScope.PrivateAndInferiors) {
			this.deploy = SystemScope.PrivateAndDirectInferiors;
		} else {
			this.deploy = deploy;
		}
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

	@Column(nullable = false, name = "free_mt_months")
	public int getFreeMaintainMonths() {
		return freeMaintainMonths;
	}

	public void setFreeMaintainMonths(int freeMaintainMonths) {
		this.freeMaintainMonths = freeMaintainMonths;
	}

	@ManyToOne
	@JoinColumn(nullable = true, name = "project_id", foreignKey = @ForeignKey(name = "FK_SYS_PRJ"))
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	@ManyToOne
	@JoinColumn(nullable = true, name = "maintenance_id", foreignKey = @ForeignKey(name = "FK_SYS_MTN"))
	public Maintenance getMaintenance() {
		return maintenance;
	}

	public void setMaintenance(Maintenance maintenance) {
		this.maintenance = maintenance;
	}

	@OneToMany(mappedBy = "system")
	public Set<SystemBranch> getBranches() {
		return branches;
	}

	public void setBranches(Set<SystemBranch> branches) {
		this.branches = branches;
	}

	public void addBranch(SystemBranch branch) {
		if (null != branch) {
			this.branches.add(branch);
		}
	}

	public void removeBranch(SystemBranch branch) {
		this.branches.remove(branch);
	}

	/**
	 * 一个信息系统可以没有Wiki，一个Wiki可以包含多个信息系统
	 * 
	 * @return
	 */
	@ManyToOne
	@JoinColumn(nullable = true, name = "wiki_id", foreignKey = @ForeignKey(name = "FK_SYS_WIKI"))
	public Wiki getWiki() {
		return wiki;
	}

	public void setWiki(Wiki wiki) {
		this.wiki = wiki;
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
		result = prime * result + ((alias == null) ? 0 : alias.hashCode());
		result = prime * result + ((brief == null) ? 0 : brief.hashCode());
		result = prime * result + ((constructedTime == null) ? 0 : constructedTime.hashCode());
		result = prime * result + ((deploy == null) ? 0 : deploy.hashCode());
		result = prime * result + freeMaintainMonths;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((modelOrVersion == null) ? 0 : modelOrVersion.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((owner == null) ? 0 : owner.hashCode());
		result = prime * result + ((provider == null) ? 0 : provider.hashCode());
		result = prime * result + ((scope == null) ? 0 : scope.hashCode());
		result = prime * result + ((securityCode == null) ? 0 : securityCode.hashCode());
		result = prime * result + securityLevel;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((usersBrief == null) ? 0 : usersBrief.hashCode());
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
		System other = (System) obj;
		if (abandonedTime == null) {
			if (other.abandonedTime != null)
				return false;
		} else if (!abandonedTime.equals(other.abandonedTime))
			return false;
		if (alias == null) {
			if (other.alias != null)
				return false;
		} else if (!alias.equals(other.alias))
			return false;
		if (brief == null) {
			if (other.brief != null)
				return false;
		} else if (!brief.equals(other.brief))
			return false;
		if (constructedTime == null) {
			if (other.constructedTime != null)
				return false;
		} else if (!constructedTime.equals(other.constructedTime))
			return false;
		if (deploy != other.deploy)
			return false;
		if (freeMaintainMonths != other.freeMaintainMonths)
			return false;
		if (id != other.id)
			return false;
		if (modelOrVersion == null) {
			if (other.modelOrVersion != null)
				return false;
		} else if (!modelOrVersion.equals(other.modelOrVersion))
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
		if (provider == null) {
			if (other.provider != null)
				return false;
		} else if (!provider.equals(other.provider))
			return false;
		if (scope != other.scope)
			return false;
		if (securityCode == null) {
			if (other.securityCode != null)
				return false;
		} else if (!securityCode.equals(other.securityCode))
			return false;
		if (securityLevel != other.securityLevel)
			return false;
		if (type != other.type)
			return false;
		if (usersBrief == null) {
			if (other.usersBrief != null)
				return false;
		} else if (!usersBrief.equals(other.usersBrief))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("System [id=").append(id).append(", name=").append(name).append(", alias=").append(alias).append(", type=").append(type)
				.append(", modelOrVersion=").append(modelOrVersion).append(", brief=").append(brief).append(", securityLevel=").append(securityLevel)
				.append(", securityCode=").append(securityCode).append(", usersBrief=").append(usersBrief).append(", provider=").append(provider)
				.append(", owner=").append(owner.getAlias()).append(", scope=").append(scope).append(", deploy=").append(deploy).append(", constructedTime=")
				.append(constructedTime).append(", abandonedTime=").append(abandonedTime).append(", freeMaintainMonths=").append(freeMaintainMonths)
				.append("]");
		return builder.toString();
	}
}
