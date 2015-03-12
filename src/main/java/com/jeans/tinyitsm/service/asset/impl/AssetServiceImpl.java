package com.jeans.tinyitsm.service.asset.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeans.tinyitsm.dao.BaseDao;
import com.jeans.tinyitsm.model.asset.Asset;
import com.jeans.tinyitsm.model.asset.Hardware;
import com.jeans.tinyitsm.model.asset.Software;
import com.jeans.tinyitsm.model.view.AssetItem;
import com.jeans.tinyitsm.model.view.Grid;
import com.jeans.tinyitsm.model.view.HardwareItem;
import com.jeans.tinyitsm.model.view.SoftwareItem;
import com.jeans.tinyitsm.service.asset.AssetConstants;
import com.jeans.tinyitsm.service.asset.AssetService;
import com.jeans.tinyitsm.service.hr.HRConstants;
import com.jeans.tinyitsm.service.hr.HRService;

@Service
public class AssetServiceImpl implements AssetService {

	private BaseDao<Asset> asDao;
	private BaseDao<Hardware> hwDao;
	private BaseDao<Software> swDao;

	private HRService hrService;

	@Autowired
	public void setAsDao(BaseDao<Asset> asDao) {
		this.asDao = asDao;
	}

	@Autowired
	public void setHwDao(BaseDao<Hardware> hwDao) {
		this.hwDao = hwDao;
	}

	@Autowired
	public void setSwDao(BaseDao<Software> swDao) {
		this.swDao = swDao;
	}

	@Autowired
	public void setHrService(HRService hrService) {
		this.hrService = hrService;
	}

	@Override
	@Transactional(readOnly = true)
	public Asset loadAsset(long id, byte type) {
		if (id <= 0) {
			return null;
		}
		Asset asset = null;
		if (type == AssetConstants.HARDWARE_ASSET) {
			asset = hwDao.getById(Hardware.class, id);
		} else if (type == AssetConstants.SOFTWARE_ASSET) {
			asset = swDao.getById(Software.class, id);
		} else if (type == AssetConstants.GENERIC_ASSET) {
			asset = asDao.getById(Asset.class, id);
		} else {
			asset = null;
		}
		return asset;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Asset> loadAssets(Set<Long> ids, byte type) {
		List<Asset> assets = new ArrayList<Asset>();
		if (ids.size() == 1) {
			assets.add(loadAsset(ids.iterator().next(), type));
		} else if (ids.size() > 1) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("p_ids", ids);
			if (type == AssetConstants.HARDWARE_ASSET) {
				String hql = "from Hardware where id in (:p_ids)";
				assets.addAll(hwDao.find(hql, params));
			} else if (type == AssetConstants.SOFTWARE_ASSET) {
				String hql = "from Software where id in (:p_ids)";
				assets.addAll(swDao.find(hql, params));
			}
		}
		return assets;
	}

	@Override
	@Transactional(readOnly = true)
	public Grid<AssetItem> loadAssets(long companyId, byte catalog, int page, int rows) {
		Grid<AssetItem> assetItems = new Grid<AssetItem>();
		List<AssetItem> assetItemsList = new ArrayList<AssetItem>();

		List<Asset> assets = new ArrayList<Asset>();
		StringBuilder hql = new StringBuilder("from ");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("p_companyId", companyId);
		boolean isHardware = true;
		if (catalog == AssetConstants.HARDWARE_ASSET) {
			hql.append("Hardware where type = 1 ");
		} else if (catalog == AssetConstants.SOFTWARE_ASSET) {
			hql.append("Software where type = 3 ");
			isHardware = false;
		} else if (catalog >= AssetConstants.NETWORK_EQUIPMENT && catalog <= AssetConstants.OTHER_EQUIPMENT) {
			hql.append("Hardware where catalog = :p_catalog ");
			params.put("p_catalog", catalog);
		} else if (catalog >= AssetConstants.OPERATING_SYSTEM_SOFTWARE && catalog <= AssetConstants.OTHER_SOFTWARE) {
			hql.append("Software where catalog = :p_catalog ");
			params.put("p_catalog", catalog);
			isHardware = false;
		} else {
			assetItems.setTotal(0);
			assetItems.setRows(assetItemsList);
			return assetItems;
		}
		hql.append("and companyId = :p_companyId");
		long t = 0;
		if (isHardware) {
			assets.addAll(hwDao.find(hql.toString(), params, page, rows));
			t = hwDao.count(hql.toString(), params);
		} else {
			assets.addAll(swDao.find(hql.toString(), params, page, rows));
			t = swDao.count(hql.toString(), params);
		}
		for (Asset asset : assets) {
			if (asset instanceof Hardware) {
				assetItemsList.add(HardwareItem.createInstance((Hardware) asset, hrService.getUnit(asset.getCompanyId(), HRConstants.COMPANY),
						hrService.getUnit(((Hardware) asset).getOwnerId(), HRConstants.EMPLOYEE)));
			} else if (asset instanceof Software) {
				assetItemsList.add(SoftwareItem.createInstance((Software) asset, hrService.getUnit(asset.getCompanyId(), HRConstants.COMPANY)));
			}
		}
		assetItems.setRows(assetItemsList);
		assetItems.setTotal(t);
		return assetItems;
	}

	@Override
	@Transactional
	public Asset newAsset(Map<String, Object> properties, byte type) {
		Asset asset = Asset.createAsset(type);
		asset.setCompanyId((long) properties.get("companyId"));
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
			hwDao.save((Hardware) asset);
		} else if (asset instanceof Software) {
			((Software) asset).setSoftwareType((byte) properties.get("softwareType"));
			((Software) asset).setLicense((String) properties.get("license"));
			((Software) asset).setExpiredTime((Date) properties.get("expiredTime"));
			swDao.save((Software) asset);
		} else {
			asDao.save(asset);
		}
		return asset;
	}
}
