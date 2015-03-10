package com.jeans.tinyitsm.model.view;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.struts2.json.annotations.JSON;

import com.jeans.tinyitsm.model.asset.Software;
import com.jeans.tinyitsm.service.asset.AssetConstants;

public class SoftwareItem implements AssetItem {
	private long id;
	private String company;
	private String type;
	private String catalog;
	private String name;
	private String vendor;
	private String modelOrVersion;
	private String assetUsage;
	private Date purchaseTime;
	private int quantity;
	private BigDecimal cost;
	private String softwareType;
	private String license;
	private Date expiredTime;
	private String state;
	private String comment;
	private String fullName;

	public static SoftwareItem createInstance(Software software, HRUnit company) {
		SoftwareItem item = null;
		if (null != software) {
			item = new SoftwareItem();
			item.setId(software.getId());
			item.setCompany(null == company ? null : company.getAlias());
			item.setType(AssetConstants.getAssetTypeName(software.getType()));
			item.setCatalog(AssetConstants.getAssetCatalogName(software.getCatalog()));
			item.setName(software.getName());
			item.setVendor(software.getVendor());
			item.setModelOrVersion(software.getModelOrVersion());
			item.setAssetUsage(software.getAssetUsage());
			item.setPurchaseTime(software.getPurchaseTime());
			item.setQuantity(software.getQuantity());
			item.setCost(software.getCost());
			item.setSoftwareType(AssetConstants.getSoftwareTypeName(software.getSoftwareType()));
			item.setLicense(software.getLicense());
			item.setExpiredTime(software.getExpiredTime());
			item.setState(AssetConstants.getAssetStateName(software.getState()));
			item.setComment(software.getComment());
			item.setFullName(software.getFullName());
		}
		return item;
	}

	@Override
	public long getId() {
		return id;
	}

	@Override
	public void setId(long id) {
		this.id = id;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCatalog() {
		return catalog;
	}

	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public String getModelOrVersion() {
		return modelOrVersion;
	}

	public void setModelOrVersion(String modelOrVersion) {
		this.modelOrVersion = modelOrVersion;
	}

	public String getAssetUsage() {
		return assetUsage;
	}

	public void setAssetUsage(String assetUsage) {
		this.assetUsage = assetUsage;
	}

	@JSON(format = "yyyy年MM月")
	public Date getPurchaseTime() {
		return purchaseTime;
	}

	public void setPurchaseTime(Date purchaseTime) {
		this.purchaseTime = purchaseTime;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	public String getSoftwareType() {
		return softwareType;
	}

	public void setSoftwareType(String softwareType) {
		this.softwareType = softwareType;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	@JSON(format = "yyyy年MM月")
	public Date getExpiredTime() {
		return expiredTime;
	}

	public void setExpiredTime(Date expiredTime) {
		this.expiredTime = expiredTime;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public String getFullName() {
		return fullName;
	}

	@Override
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((assetUsage == null) ? 0 : assetUsage.hashCode());
		result = prime * result + ((catalog == null) ? 0 : catalog.hashCode());
		result = prime * result + ((comment == null) ? 0 : comment.hashCode());
		result = prime * result + ((company == null) ? 0 : company.hashCode());
		result = prime * result + ((cost == null) ? 0 : cost.hashCode());
		result = prime * result + ((expiredTime == null) ? 0 : expiredTime.hashCode());
		result = prime * result + ((fullName == null) ? 0 : fullName.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((license == null) ? 0 : license.hashCode());
		result = prime * result + ((modelOrVersion == null) ? 0 : modelOrVersion.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((purchaseTime == null) ? 0 : purchaseTime.hashCode());
		result = prime * result + quantity;
		result = prime * result + ((softwareType == null) ? 0 : softwareType.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((vendor == null) ? 0 : vendor.hashCode());
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
		SoftwareItem other = (SoftwareItem) obj;
		if (assetUsage == null) {
			if (other.assetUsage != null)
				return false;
		} else if (!assetUsage.equals(other.assetUsage))
			return false;
		if (catalog == null) {
			if (other.catalog != null)
				return false;
		} else if (!catalog.equals(other.catalog))
			return false;
		if (comment == null) {
			if (other.comment != null)
				return false;
		} else if (!comment.equals(other.comment))
			return false;
		if (company == null) {
			if (other.company != null)
				return false;
		} else if (!company.equals(other.company))
			return false;
		if (cost == null) {
			if (other.cost != null)
				return false;
		} else if (!cost.equals(other.cost))
			return false;
		if (expiredTime == null) {
			if (other.expiredTime != null)
				return false;
		} else if (!expiredTime.equals(other.expiredTime))
			return false;
		if (fullName == null) {
			if (other.fullName != null)
				return false;
		} else if (!fullName.equals(other.fullName))
			return false;
		if (id != other.id)
			return false;
		if (license == null) {
			if (other.license != null)
				return false;
		} else if (!license.equals(other.license))
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
		if (purchaseTime == null) {
			if (other.purchaseTime != null)
				return false;
		} else if (!purchaseTime.equals(other.purchaseTime))
			return false;
		if (quantity != other.quantity)
			return false;
		if (softwareType == null) {
			if (other.softwareType != null)
				return false;
		} else if (!softwareType.equals(other.softwareType))
			return false;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (vendor == null) {
			if (other.vendor != null)
				return false;
		} else if (!vendor.equals(other.vendor))
			return false;
		return true;
	}
}
