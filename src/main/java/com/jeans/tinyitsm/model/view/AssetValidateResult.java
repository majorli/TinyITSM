package com.jeans.tinyitsm.model.view;

import java.io.Serializable;

public class AssetValidateResult implements Serializable {

	private byte result;
	private Long id;
	private String assetCatalogName;
	private String assetFullName;
	private String ownerName;
	private String ownerCompanyName;

	public AssetValidateResult() {}

	public AssetValidateResult(byte result, Long id, String assetCatalogName, String assetFullName, String ownerName, String ownerCompanyName) {
		super();
		this.result = result;
		this.id = id;
		this.assetCatalogName = assetCatalogName;
		this.assetFullName = assetFullName;
		this.ownerName = ownerName;
		this.ownerCompanyName = ownerCompanyName;
	}

	public byte getResult() {
		return result;
	}

	public void setResult(byte result) {
		this.result = result;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAssetCatalogName() {
		return assetCatalogName;
	}

	public void setAssetCatalogName(String assetCatalogName) {
		this.assetCatalogName = assetCatalogName;
	}

	public String getAssetFullName() {
		return assetFullName;
	}

	public void setAssetFullName(String assetFullName) {
		this.assetFullName = assetFullName;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getOwnerCompanyName() {
		return ownerCompanyName;
	}

	public void setOwnerCompanyName(String ownerCompanyName) {
		this.ownerCompanyName = ownerCompanyName;
	}
}
