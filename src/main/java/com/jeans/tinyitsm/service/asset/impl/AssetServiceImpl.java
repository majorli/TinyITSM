package com.jeans.tinyitsm.service.asset.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
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

	@Override
	@Transactional
	public int saveProps(Set<Long> ids, byte type, Map<String, Object> props) {
		if (ids.isEmpty()) {
			return 0;
		}
		if (ids.size() == 1) {
			Asset asset = loadAsset(ids.iterator().next(), type);
			if (null == asset) {
				return 0;
			}
			/*
			 * 只编辑一项资产时：
			 * 数值型属性为负数时保存该属性的最小值；
			 * 日期型属性为null时保存为null；
			 * 选择型属性为-99时保持原值不变；
			 * 字符串属性直接保存。
			 */
			asset.setName((String) props.get("name"));
			asset.setVendor((String) props.get("vendor"));
			asset.setModelOrVersion((String) props.get("modelOrVersion"));
			asset.setAssetUsage((String) props.get("assetUsage"));
			asset.setPurchaseTime((Date) props.get("purchaseTime"));
			int q = (int) props.get("quantity");
			asset.setQuantity(q < 0 ? 1 : q);
			BigDecimal c = (BigDecimal) props.get("cost");
			asset.setCost(c.compareTo(BigDecimal.ZERO) == -1 ? BigDecimal.ZERO : c);
			asset.setComment((String) props.get("comment"));
			if (asset instanceof Hardware) {
				((Hardware) asset).setCode((String) props.get("code"));
				((Hardware) asset).setFinancialCode((String) props.get("financialCode"));
				((Hardware) asset).setSn((String) props.get("sn"));
				((Hardware) asset).setConfiguration((String) props.get("configuration"));
				byte w = (byte) props.get("warranty");
				if (w >= -1 && w <= 1) {
					((Hardware) asset).setWarranty(w);
				}
				((Hardware) asset).setLocation((String) props.get("location"));
				((Hardware) asset).setIp((String) props.get("ip"));
				byte i = (byte) props.get("importance");
				if (i >= 0 && i <= 2) {
					((Hardware) asset).setImportance(i);
				}
				hwDao.update((Hardware) asset);
				return 1;
			} else if (asset instanceof Software) {
				byte s = (byte) props.get("softwareType");
				if (s >= 0 && s <= 6) {
					((Software) asset).setSoftwareType(s);
				}
				((Software) asset).setLicense((String) props.get("license"));
				((Software) asset).setExpiredTime((Date) props.get("expiredTime"));
				swDao.update((Software) asset);
				return 1;
			} else {
				return 0;
			}
		} else {
			List<Asset> assets = loadAssets(ids, type);
			int count = assets.size();
			for (Asset asset : assets) {
				/*
				 * 编辑多项资产时：
				 * 数值型属性为负数时保持各项资产该属性原值不变；
				 * 日期型属性为null时保持各项资产该属性原值不变；
				 * 选择型属性为-99时保持各项资产该属性原值不变；
				 * 字符串属性为空串时保持各项资产该属性原值不变。
				 */
				String v = (String) props.get("name");
				if (!StringUtils.isEmpty(v)) {
					asset.setName(v);
				}
				v = (String) props.get("vendor");
				if (!StringUtils.isEmpty(v)) {
					asset.setVendor(v);
				}
				v = (String) props.get("modelOrVersion");
				if (!StringUtils.isEmpty(v)) {
					asset.setModelOrVersion(v);
				}
				v = (String) props.get("assetUsage");
				if (!StringUtils.isEmpty(v)) {
					asset.setAssetUsage(v);
				}
				Date d = (Date) props.get("purchaseTime");
				if (null != d) {
					asset.setPurchaseTime(d);
				}
				int q = (int) props.get("quantity");
				if (q >= 0) {
					asset.setQuantity(q);
				}
				BigDecimal c = (BigDecimal) props.get("cost");
				if (c.compareTo(BigDecimal.ZERO) != -1) {
					asset.setCost(c);
				}
				v = (String) props.get("comment");
				if (!StringUtils.isEmpty(v)) {
					asset.setComment(v);
				}
				if (asset instanceof Hardware) {
					v = (String) props.get("sn");
					if (!StringUtils.isEmpty(v)) {
						((Hardware) asset).setSn(v);
					}
					v = (String) props.get("configuration");
					if (!StringUtils.isEmpty(v)) {
						((Hardware) asset).setConfiguration(v);
					}
					byte w = (byte) props.get("warranty");
					if (w >= -1 && w <= 1) {
						((Hardware) asset).setWarranty(w);
					}
					v = (String) props.get("location");
					if (!StringUtils.isEmpty(v)) {
						((Hardware) asset).setLocation(v);
					}
					v = (String) props.get("ip");
					if (!StringUtils.isEmpty(v)) {
						((Hardware) asset).setIp(v);
					}
					byte i = (byte) props.get("importance");
					if (i >= 0 && i <= 2) {
						((Hardware) asset).setImportance(i);
					}
					hwDao.update((Hardware) asset);
				} else if (asset instanceof Software) {
					byte s = (byte) props.get("softwareType");
					if (s >= 0 && s <= 6) {
						((Software) asset).setSoftwareType(s);
					}
					v = (String) props.get("license");
					if (!StringUtils.isEmpty(v)) {
						((Software) asset).setLicense(v);
					}
					d = (Date) props.get("expiredTime");
					if (null != d) {
						((Software) asset).setExpiredTime(d);
					}
					swDao.update((Software) asset);
				} else {
					count--;
				}
			}
			return count;
		}
	}
}
