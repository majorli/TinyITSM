package com.jeans.tinyitsm.model.asset;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.json.annotations.JSON;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.NumericField;

import com.jeans.tinyitsm.service.asset.AssetConstants;

/**
 * 全文检索字段: 带有*号标记的为精确检索字段<br>
 * Asset: fullName:String(包含name, vendor, modelOrVersion); comment:String<br>
 * Hardware: *sn:String, location:String, *ip:String, *code:String, *financialCode:String<br>
 * Software: *license<br>
 * 过滤字段：companyId:long<br>
 * 
 * @author Majorli
 *
 */
@Entity
@Table(name = "assets")
@Inheritance(strategy = InheritanceType.JOINED)
@Indexed
public class Asset implements Serializable {

	private long id;
	private long companyId;
	private byte type;
	private byte catalog;
	private String name;
	private String vendor;
	private String modelOrVersion;
	private String assetUsage;
	private Date purchaseTime;
	private int quantity;
	private BigDecimal cost;
	private byte state;
	private String comment;

	public static Asset createAsset(byte type) {
		if (type == AssetConstants.HARDWARE_ASSET) {
			return new Hardware();
		} else if (type == AssetConstants.SOFTWARE_ASSET) {
			return new Software();
		} else {
			return null;
		}
	}

	public static Asset createAsset(Map<String, Object> properties, long companyId, byte type) {
		Asset asset = Asset.createAsset(type);
		if (null != asset) {
			asset.setCompanyId(companyId);
			asset.setType(type);
			asset.setCatalog((byte) properties.get("catalog"));
			asset.setName((String) properties.get("name"));
			asset.setVendor((String) properties.get("vendor"));
			asset.setModelOrVersion((String) properties.get("modelOrVersion"));
			asset.setAssetUsage((String) properties.get("assetUsage"));
			asset.setPurchaseTime((Date) properties.get("purchaseTime"));
			int quantity = (int) properties.get("quantity");
			asset.setQuantity(quantity);
			asset.setCost((BigDecimal) properties.get("cost"));
			asset.setState((byte) properties.get("state"));
			asset.setComment((String) properties.get("comment"));
			if (asset instanceof Hardware) {
				((Hardware) asset).setSn((String) properties.get("sn"));
				((Hardware) asset).setConfiguration((String) properties.get("configuration"));
				((Hardware) asset).setWarranty((byte) properties.get("warranty"));
				((Hardware) asset).setLocation((String) properties.get("location"));
				((Hardware) asset).setIp((String) properties.get("ip"));
				((Hardware) asset).setImportance((byte) properties.get("importance"));
				((Hardware) asset).setOwnerId((long) properties.get("ownerId"));
				String code = (String) properties.get("code");
				if (quantity > 1) {
					code += "#1~" + quantity;
				}
				((Hardware) asset).setCode(code);
				((Hardware) asset).setFinancialCode((String) properties.get("financialCode"));
			} else if (asset instanceof Software) {
				((Software) asset).setSoftwareType((byte) properties.get("softwareType"));
				((Software) asset).setLicense((String) properties.get("license"));
				((Software) asset).setExpiredTime((Date) properties.get("expiredTime"));
			}
		}
		return asset;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(nullable = false, name = "company_id")
	@Field
	@NumericField
	public long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}

	@Column(nullable = false)
	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	@Column(nullable = false)
	public byte getCatalog() {
		return catalog;
	}

	public void setCatalog(byte catalog) {
		this.catalog = catalog;
	}

	@Column(nullable = false, length = 32)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(nullable = false, length = 32)
	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	@Column(nullable = false, name = "model_or_version", length = 64)
	public String getModelOrVersion() {
		return modelOrVersion;
	}

	public void setModelOrVersion(String modelOrVersion) {
		this.modelOrVersion = modelOrVersion;
	}

	@Column(nullable = false, name = "asset_usage", length = 255)
	public String getAssetUsage() {
		return assetUsage;
	}

	public void setAssetUsage(String assetUsage) {
		this.assetUsage = assetUsage;
	}

	@JSON(format = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "purchase_time")
	public Date getPurchaseTime() {
		return purchaseTime;
	}

	public void setPurchaseTime(Date purchaseTime) {
		this.purchaseTime = purchaseTime;
	}

	@Column(nullable = false)
	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@Column(nullable = false)
	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	@Column(nullable = false)
	public byte getState() {
		return state;
	}

	public void setState(byte state) {
		this.state = state;
	}

	@Column(nullable = false, length = 255)
	@Field
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Transient
	@Field
	public String getFullName() {
		if (StringUtils.isBlank(name)) {
			return "";
		} else {
			StringBuilder fn = new StringBuilder(name);
			if (type == AssetConstants.SOFTWARE_ASSET) {
				if (!StringUtils.isBlank(vendor)) {
					fn.insert(0, " ").insert(0, vendor);
				}
				if (!StringUtils.isBlank(modelOrVersion)) {
					fn.append(" ").append(modelOrVersion);
				}
			} else {
				if (!StringUtils.isBlank(vendor)) {
					fn.append(" ").append(vendor);
				}
				if (!StringUtils.isBlank(modelOrVersion)) {
					fn.append(" ").append(modelOrVersion);
				}
			}
			return fn.toString();
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((assetUsage == null) ? 0 : assetUsage.hashCode());
		result = prime * result + catalog;
		result = prime * result + ((comment == null) ? 0 : comment.hashCode());
		result = prime * result + (int) (companyId ^ (companyId >>> 32));
		result = prime * result + ((cost == null) ? 0 : cost.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((modelOrVersion == null) ? 0 : modelOrVersion.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((purchaseTime == null) ? 0 : purchaseTime.hashCode());
		result = prime * result + quantity;
		result = prime * result + state;
		result = prime * result + type;
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
		Asset other = (Asset) obj;
		if (assetUsage == null) {
			if (other.assetUsage != null)
				return false;
		} else if (!assetUsage.equals(other.assetUsage))
			return false;
		if (catalog != other.catalog)
			return false;
		if (comment == null) {
			if (other.comment != null)
				return false;
		} else if (!comment.equals(other.comment))
			return false;
		if (companyId != other.companyId)
			return false;
		if (cost == null) {
			if (other.cost != null)
				return false;
		} else if (!cost.equals(other.cost))
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
		if (purchaseTime == null) {
			if (other.purchaseTime != null)
				return false;
		} else if (!purchaseTime.equals(other.purchaseTime))
			return false;
		if (quantity != other.quantity)
			return false;
		if (state != other.state)
			return false;
		if (type != other.type)
			return false;
		if (vendor == null) {
			if (other.vendor != null)
				return false;
		} else if (!vendor.equals(other.vendor))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Asset [id=").append(id).append(", companyId=").append(companyId).append(", type=").append(type).append(", catalog=").append(catalog)
				.append(", name=").append(name).append(", vendor=").append(vendor).append(", modelOrVersion=").append(modelOrVersion).append("]");
		return builder.toString();
	}
}
