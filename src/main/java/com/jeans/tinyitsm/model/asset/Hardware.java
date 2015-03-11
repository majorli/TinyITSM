package com.jeans.tinyitsm.model.asset;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

@Entity
@Table(name = "assets_hardware")
@PrimaryKeyJoinColumn(name = "id")
@Indexed
public class Hardware extends Asset {
	private String sn;
	private String configuration;
	private byte warranty;
	private String location;
	private String ip;
	private byte importance;
	private long ownerId;
	private String code;
	private String financialCode;

	@Column(nullable = false, length = 64)
	@Field
	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	@Column(nullable = false, length = 255)
	public String getConfiguration() {
		return configuration;
	}

	public void setConfiguration(String configuration) {
		this.configuration = configuration;
	}

	@Column(nullable = false)
	public byte getWarranty() {
		return warranty;
	}

	public void setWarranty(byte warranty) {
		this.warranty = warranty;
	}

	@Column(nullable = false, length = 255)
	@Field
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@Column(nullable = false, length = 64)
	@Field
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@Column(nullable = false)
	public byte getImportance() {
		return importance;
	}

	public void setImportance(byte importance) {
		this.importance = importance;
	}

	@Column(nullable = false, name = "owner_id")
	public long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(long ownerId) {
		this.ownerId = ownerId;
	}

	@Column(nullable = false, length = 32)
	@Field
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(nullable = false, name = "financial_code", length = 32)
	@Field
	public String getFinancialCode() {
		return financialCode;
	}

	public void setFinancialCode(String financialCode) {
		this.financialCode = financialCode;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((configuration == null) ? 0 : configuration.hashCode());
		result = prime * result + ((financialCode == null) ? 0 : financialCode.hashCode());
		result = prime * result + importance;
		result = prime * result + ((ip == null) ? 0 : ip.hashCode());
		result = prime * result + ((location == null) ? 0 : location.hashCode());
		result = prime * result + (int) (ownerId ^ (ownerId >>> 32));
		result = prime * result + ((sn == null) ? 0 : sn.hashCode());
		result = prime * result + warranty;
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
		Hardware other = (Hardware) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (configuration == null) {
			if (other.configuration != null)
				return false;
		} else if (!configuration.equals(other.configuration))
			return false;
		if (financialCode == null) {
			if (other.financialCode != null)
				return false;
		} else if (!financialCode.equals(other.financialCode))
			return false;
		if (importance != other.importance)
			return false;
		if (ip == null) {
			if (other.ip != null)
				return false;
		} else if (!ip.equals(other.ip))
			return false;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (ownerId != other.ownerId)
			return false;
		if (sn == null) {
			if (other.sn != null)
				return false;
		} else if (!sn.equals(other.sn))
			return false;
		if (warranty != other.warranty)
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Hardware [sn=").append(sn).append(", configuration=").append(configuration).append(", warranty=").append(warranty)
				.append(", location=").append(location).append(", ip=").append(ip).append(", importance=").append(importance).append(", ownerId=")
				.append(ownerId).append(", code=").append(code).append(", financialCode=").append(financialCode).append(", Asset.toString()=")
				.append(super.toString()).append("]");
		return builder.toString();
	}
}
