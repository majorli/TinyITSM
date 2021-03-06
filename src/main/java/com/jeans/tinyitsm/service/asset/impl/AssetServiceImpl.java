package com.jeans.tinyitsm.service.asset.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeans.tinyitsm.dao.BaseDao;
import com.jeans.tinyitsm.model.asset.Asset;
import com.jeans.tinyitsm.model.asset.Hardware;
import com.jeans.tinyitsm.model.asset.Software;
import com.jeans.tinyitsm.model.hr.Employee;
import com.jeans.tinyitsm.model.view.AssetItem;
import com.jeans.tinyitsm.model.view.AssetValidateResult;
import com.jeans.tinyitsm.model.view.Grid;
import com.jeans.tinyitsm.model.view.HRUnit;
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
	@Transactional(readOnly = true)
	public List<AssetItem> loadAssets(long companyId, byte catalog) {
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
		}
		hql.append("and companyId = :p_companyId order by catalog, id");
		if (isHardware) {
			assets.addAll(hwDao.find(hql.toString(), params));
		} else {
			assets.addAll(swDao.find(hql.toString(), params));
		}
		for (Asset asset : assets) {
			if (asset instanceof Hardware) {
				assetItemsList.add(HardwareItem.createInstance((Hardware) asset, hrService.getUnit(asset.getCompanyId(), HRConstants.COMPANY),
						hrService.getUnit(((Hardware) asset).getOwnerId(), HRConstants.EMPLOYEE)));
			} else if (asset instanceof Software) {
				assetItemsList.add(SoftwareItem.createInstance((Software) asset, hrService.getUnit(asset.getCompanyId(), HRConstants.COMPANY)));
			}
		}
		return assetItemsList;
	}

	@Override
	@Transactional
	public Asset newAsset(Map<String, Object> properties, byte type) {
		Asset asset = Asset.createAsset(properties, (long) properties.get("companyId"), type);
		if (null != asset) {
			if (asset instanceof Hardware) {
				hwDao.save((Hardware) asset);
			} else if (asset instanceof Software) {
				swDao.save((Software) asset);
			}
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

	@Override
	@Transactional(readOnly = true)
	public List<AssetValidateResult> validate(long companyId) {
		List<AssetValidateResult> results = new ArrayList<AssetValidateResult>();

		String hql = "from Hardware where companyId = " + companyId;
		List<Hardware> assets = hwDao.find(hql);
		for (Hardware hardware : assets) {
			if (hardware.getOwnerId() == 0) {
				// 无责任人
				if (hardware.getState() == AssetConstants.IN_USE) {
					// 在用设备无责任人
					results.add(new AssetValidateResult(AssetConstants.IN_USE_ASSET_WITHOUT_OWNER, hardware.getId(), AssetConstants
							.getAssetCatalogName(hardware.getCatalog()), hardware.getFullName(), null, null));
				}
			} else {
				// 有责任人
				Employee owner = hrService.getEmployee(hardware.getOwnerId());
				if (null == owner) {
					// 责任人不存在
					results.add(new AssetValidateResult(AssetConstants.INVALID_OWNER, hardware.getId(), AssetConstants.getAssetCatalogName(hardware
							.getCatalog()), hardware.getFullName(), null, null));
				} else {
					if (owner.getDepartment().getSubRoot().getId() != hardware.getCompanyId()) {
						// 责任人和资产所属公司不一致
						results.add(new AssetValidateResult(AssetConstants.INVALID_OWNER_COMPANY, hardware.getId(), AssetConstants.getAssetCatalogName(hardware
								.getCatalog()), hardware.getFullName(), owner.getName(), owner.getDepartment().getSubRoot().getAlias()));
					} else {
						if (owner.getState() == HRConstants.FORMER && hardware.getState() == AssetConstants.IN_USE) {
							// 在用设备的责任人已经离职
							results.add(new AssetValidateResult(AssetConstants.IN_USE_ASSET_OWNED_BY_FORMER_EMPLOYEE, hardware.getId(), AssetConstants
									.getAssetCatalogName(hardware.getCatalog()), hardware.getFullName(), owner.getName(), owner.getDepartment().getSubRoot()
									.getAlias()));
						}
					}
				}
			}
		}
		return results;
	}

	@Override
	@Transactional(readOnly = true)
	public Set<Byte> checkNextStates(Set<Long> ids, byte type) {
		Set<Byte> states = new HashSet<Byte>();
		if (ids.size() == 0) {
			return states;
		}

		states.add(AssetConstants.IN_USE);
		states.add(AssetConstants.IDLE);
		states.add(AssetConstants.DISUSE);
		if (type == AssetConstants.HARDWARE_ASSET) {
			states.add(AssetConstants.FIXING);
			states.add(AssetConstants.ELIMINATED);
		}

		// 状态转移表
		Set<Byte> n_h_iu = new HashSet<Byte>();
		Set<Byte> n_h_id = new HashSet<Byte>();
		Set<Byte> n_h_fx = new HashSet<Byte>();
		Set<Byte> n_h_du = new HashSet<Byte>();
		Set<Byte> n_s_iu = new HashSet<Byte>();
		Set<Byte> n_s_id = new HashSet<Byte>();

		n_h_iu.add(AssetConstants.IN_USE);
		n_h_iu.add(AssetConstants.IDLE);
		n_h_iu.add(AssetConstants.FIXING);

		n_h_id.add(AssetConstants.IN_USE);
		n_h_id.add(AssetConstants.FIXING);
		n_h_id.add(AssetConstants.DISUSE);

		n_h_fx.add(AssetConstants.IN_USE);
		n_h_fx.add(AssetConstants.IDLE);
		n_h_fx.add(AssetConstants.DISUSE);

		n_h_du.add(AssetConstants.ELIMINATED);

		n_s_iu.add(AssetConstants.IDLE);
		n_s_iu.add(AssetConstants.DISUSE);

		n_s_id.add(AssetConstants.IN_USE);
		n_s_id.add(AssetConstants.DISUSE);

		List<Asset> assets = loadAssets(ids, type);
		if (type == AssetConstants.HARDWARE_ASSET) {
			/*
			 * 硬件类资产(状态变为在用时需要选择责任人)：
			 * 在用 -> 在用/备用/维修
			 * 备用 -> 在用/维修/淘汰
			 * 维修 -> 在用/备用/淘汰（转移为在用时默认选中原责任人，如果有的话）
			 * 淘汰 -> 报损
			 * 报损 -> null
			 */
			for (Asset asset : assets) {
				byte oldState = asset.getState();
				if (oldState == AssetConstants.ELIMINATED) {
					// 剪枝：无论什么时候遇到原状态为报损的，直接将结果集清空并跳出循环
					states.clear();
					break;
				} else {
					if (states.size() == 0) {
						// 剪枝：已经没有下一种可能状态了，直接跳出循环
						break;
					} else {
						switch (oldState) {
						case AssetConstants.IN_USE:
							states.retainAll(n_h_iu);
							break;
						case AssetConstants.IDLE:
							states.retainAll(n_h_id);
							break;
						case AssetConstants.FIXING:
							states.retainAll(n_h_fx);
							break;
						case AssetConstants.DISUSE:
							states.retainAll(n_h_du);
						}
					}
				}
			}
		} else {
			/*
			 * 软件类资产：
			 * 在用 -> 备用/淘汰
			 * 备用 -> 在用/淘汰
			 * 淘汰 -> null
			 */
			for (Asset asset : assets) {
				byte oldState = asset.getState();
				if (oldState == AssetConstants.DISUSE) {
					// 剪枝：无论什么时候遇到原状态为淘汰的，直接将结果集清空并跳出循环
					states.clear();
					break;
				} else {
					if (states.size() == 1) {
						// 剪枝：已经只剩下淘汰一种可能了，后续的资产如果不是出现原状态为淘汰的就不会再对结果有任何影响了，所以无需再做交集，直接跳到下一项资产
						continue;
					} else {
						if (oldState == AssetConstants.IN_USE) {
							states.retainAll(n_s_iu);
						} else {
							states.retainAll(n_s_id);
						}
					}
				}
			}
		}
		return states;
	}

	/**
	 * 硬件类资产如果新状态为0(IN_USE)，则需要同时设置ownerId，如果keepOldOwner == true则原来ownerId != 0的保持不变<br>
	 * 硬件类资产如果新状态不为0,则设置ownerId为0，如果keepOldOwner == true则原来ownerId != 0的保持不变<br>
	 * 软件类资产直接变更新状态即可
	 */
	@Override
	@Transactional
	public int changeState(Set<Long> ids, byte type, byte newState, long ownerId, boolean keepOldOwner) {
		int count = 0;
		if (ids.size() > 0) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("p_newState", newState);
			params.put("p_ids", ids);
			if (type == AssetConstants.SOFTWARE_ASSET) {
				String hql = "update Software set state = :p_newState where id in (:p_ids)";
				count = swDao.executeHql(hql, params);
			} else if (type == AssetConstants.HARDWARE_ASSET) {
				long newOwnerId = (newState == AssetConstants.IN_USE) ? ownerId : 0;
				if (keepOldOwner) {
					// 保留原有的ownerId
					String hql = "update Hardware set state = :p_newState where id in (:p_ids) and ownerId <> 0";
					count += hwDao.executeHql(hql, params);
					hql = "update Hardware set state = :p_newState, ownerId = :p_newOwnerId where id in (:p_ids) and ownerId = 0";
					params.put("p_newOwnerId", newOwnerId);
					count += hwDao.executeHql(hql, params);
				} else {
					String hql = "update Hardware set state = :p_newState, ownerId = :p_newOwnerId where id in (:p_ids)";
					params.put("p_newOwnerId", newOwnerId);
					count = hwDao.executeHql(hql, params);
				}
			}
		}
		return count;
	}

	@Override
	@Transactional
	public int adjust(long id, byte adjustType, long ownerId) {
		int count = 0;
		if (id > 0) {
			switch (adjustType) {
			case 0: // 调整责任人
				count = hwDao.executeHql("update Hardware set ownerId = " + ownerId + " where id = " + id);
				break;
			case 1: // 回收资产（状态设置为IDLE，责任人id设置为0）
				count = hwDao.executeHql("update Hardware set state = 3, ownerId = 0 where id = " + id);
				break;
			case 2: // 根据责任人调整所属公司
				Hardware asset = hwDao.getById(Hardware.class, id);
				Employee owner = hrService.getEmployee(asset.getOwnerId());
				if (null != owner) {
					count = hwDao.executeHql("update Hardware set companyId = " + owner.getDepartment().getSubRoot().getId() + " where id = " + id);
				}
			}
		}
		return count;
	}

	@Override
	@Transactional
	public int createNewAssets(Map<String, Object> props, long companyId) {
		byte type = (byte) props.get("type");
		int number = (int) props.get("number");
		Asset prototype = Asset.createAsset(props, companyId, type);
		Set<Serializable> ids = new HashSet<Serializable>();
		if (null != prototype) {
			if (number > 1) {
				if (prototype instanceof Hardware) {
					DecimalFormat df = new DecimalFormat("000");
					String prototypeCode = ((Hardware) prototype).getCode();
					for (int n = 1; n <= number; n++) {
						Hardware hw = new Hardware();
						BeanUtils.copyProperties(prototype, hw);
						hw.setCode(prototypeCode + "[" + df.format(n) + "]");
						ids.add(hwDao.save(hw));
					}
				} else {
					while (number-- > 0) {
						Software sw = new Software();
						BeanUtils.copyProperties(prototype, sw);
						ids.add(swDao.save(sw));
					}
				}
			} else {
				if (type == AssetConstants.HARDWARE_ASSET) {
					ids.add(hwDao.save((Hardware) prototype));
				} else {
					ids.add(swDao.save((Software) prototype));
				}
			}
			ids.remove(null);
		}
		return ids.size();
	}

	@Override
	@Transactional(readOnly = true)
	public List<HardwareItem> loadEquipmentsByOwner(HRUnit owner) {
		List<HardwareItem> list = new ArrayList<HardwareItem>();
		if (null != owner) {
			List<Hardware> assets = new ArrayList<Hardware>();
			String hql = "from Hardware where ownerId = " + owner.getId() + " order by catalog, code, id";
			assets.addAll(hwDao.find(hql));
			for (Hardware asset : assets) {
				list.add(HardwareItem.createInstance(asset, hrService.getUnit(asset.getCompanyId(), HRConstants.COMPANY), owner));
			}
		}
		return list;
	}
}
