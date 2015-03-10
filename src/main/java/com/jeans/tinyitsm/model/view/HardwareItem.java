package com.jeans.tinyitsm.model.view;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.struts2.json.annotations.JSON;

import com.jeans.tinyitsm.model.asset.Hardware;
import com.jeans.tinyitsm.service.asset.AssetConstants;

public class HardwareItem implements AssetItem {
	private long id;
	private String code;
	private String financialCode;
	private String company;
	private String type;
	private String catalog;
	private String name;
	private String vendor;
	private String modelOrVersion;
	private String assetUsage;
	private String sn;
	private String configuration;
	private Date purchaseTime;
	private int quantity;
	private BigDecimal cost;
	private String state;
	private String warranty;
	private String location;
	private String ip;
	private String importance;
	private String owner;
	private String comment;
	private String fullName;

	public static HardwareItem createInstance(Hardware hardware, HRUnit company, HRUnit employee) {
		HardwareItem item = null;
		if (null != hardware) {
			item = new HardwareItem();
			item.setId(hardware.getId());
			item.setCode(hardware.getCode());
			item.setFinancialCode(hardware.getFinancialCode());
			item.setCompany(null == company ? null : company.getAlias());
			item.setType(AssetConstants.getAssetTypeName(hardware.getType()));
			item.setCatalog(AssetConstants.getAssetCatalogName(hardware.getCatalog()));
			item.setName(hardware.getName());
			item.setVendor(hardware.getVendor());
			item.setModelOrVersion(hardware.getModelOrVersion());
			item.setAssetUsage(hardware.getAssetUsage());
			item.setSn(hardware.getSn());
			item.setConfiguration(hardware.getConfiguration());
			item.setPurchaseTime(hardware.getPurchaseTime());
			item.setQuantity(hardware.getQuantity());
			item.setCost(hardware.getCost());
			item.setState(AssetConstants.getAssetStateName(hardware.getState()));
			item.setWarranty(AssetConstants.getHardwareWarrantyName(hardware.getWarranty()));
			item.setLocation(hardware.getLocation());
			item.setIp(hardware.getIp());
			item.setImportance(AssetConstants.getHardwareImportanceName(hardware.getImportance()));
			item.setOwner(null == employee ? null : employee.getName());
			item.setComment(hardware.getComment());
			item.setFullName(hardware.getFullName());
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getFinancialCode() {
		return financialCode;
	}

	public void setFinancialCode(String financialCode) {
		this.financialCode = financialCode;
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

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getConfiguration() {
		return configuration;
	}

	public void setConfiguration(String configuration) {
		this.configuration = configuration;
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

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getWarranty() {
		return warranty;
	}

	public void setWarranty(String warranty) {
		this.warranty = warranty;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getImportance() {
		return importance;
	}

	public void setImportance(String importance) {
		this.importance = importance;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
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
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((comment == null) ? 0 : comment.hashCode());
		result = prime * result + ((company == null) ? 0 : company.hashCode());
		result = prime * result + ((configuration == null) ? 0 : configuration.hashCode());
		result = prime * result + ((cost == null) ? 0 : cost.hashCode());
		result = prime * result + ((financialCode == null) ? 0 : financialCode.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((importance == null) ? 0 : importance.hashCode());
		result = prime * result + ((ip == null) ? 0 : ip.hashCode());
		result = prime * result + ((location == null) ? 0 : location.hashCode());
		result = prime * result + ((modelOrVersion == null) ? 0 : modelOrVersion.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((owner == null) ? 0 : owner.hashCode());
		result = prime * result + ((purchaseTime == null) ? 0 : purchaseTime.hashCode());
		result = prime * result + quantity;
		result = prime * result + ((sn == null) ? 0 : sn.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((vendor == null) ? 0 : vendor.hashCode());
		result = prime * result + ((warranty == null) ? 0 : warranty.hashCode());
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
		HardwareItem other = (HardwareItem) obj;
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
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
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
		if (configuration == null) {
			if (other.configuration != null)
				return false;
		} else if (!configuration.equals(other.configuration))
			return false;
		if (cost == null) {
			if (other.cost != null)
				return false;
		} else if (!cost.equals(other.cost))
			return false;
		if (financialCode == null) {
			if (other.financialCode != null)
				return false;
		} else if (!financialCode.equals(other.financialCode))
			return false;
		if (id != other.id)
			return false;
		if (importance == null) {
			if (other.importance != null)
				return false;
		} else if (!importance.equals(other.importance))
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
		if (purchaseTime == null) {
			if (other.purchaseTime != null)
				return false;
		} else if (!purchaseTime.equals(other.purchaseTime))
			return false;
		if (quantity != other.quantity)
			return false;
		if (sn == null) {
			if (other.sn != null)
				return false;
		} else if (!sn.equals(other.sn))
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
		if (warranty == null) {
			if (other.warranty != null)
				return false;
		} else if (!warranty.equals(other.warranty))
			return false;
		return true;
	}

}
