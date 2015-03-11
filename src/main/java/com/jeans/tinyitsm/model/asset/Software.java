package com.jeans.tinyitsm.model.asset;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.apache.struts2.json.annotations.JSON;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

@Entity
@Table(name = "assets_software")
@PrimaryKeyJoinColumn(name = "id")
@Indexed
public class Software extends Asset {

	private byte softwareType;
	private String license;
	private Date expiredTime;

	@Column(nullable = false, name = "software_type")
	public byte getSoftwareType() {
		return softwareType;
	}

	public void setSoftwareType(byte softwareType) {
		this.softwareType = softwareType;
	}

	@Column(nullable = false, length = 64)
	@Field
	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	@Column(name = "expired_time")
	@JSON(format = "yyyy-MM-dd HH:mm:ss")
	public Date getExpiredTime() {
		return expiredTime;
	}

	public void setExpiredTime(Date expiredTime) {
		this.expiredTime = expiredTime;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((expiredTime == null) ? 0 : expiredTime.hashCode());
		result = prime * result + ((license == null) ? 0 : license.hashCode());
		result = prime * result + softwareType;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Software other = (Software) obj;
		if (expiredTime == null) {
			if (other.expiredTime != null)
				return false;
		} else if (!expiredTime.equals(other.expiredTime))
			return false;
		if (license == null) {
			if (other.license != null)
				return false;
		} else if (!license.equals(other.license))
			return false;
		if (softwareType != other.softwareType)
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Software [softwareType=").append(softwareType).append(", license=").append(license).append(", expiredTime=").append(expiredTime)
				.append(", Asset.toString()=").append(super.toString()).append("]");
		return builder.toString();
	}
}
