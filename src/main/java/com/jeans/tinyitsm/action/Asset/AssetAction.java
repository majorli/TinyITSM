package com.jeans.tinyitsm.action.Asset;

import java.math.BigDecimal;
import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.jeans.tinyitsm.action.BaseAction;
import com.jeans.tinyitsm.model.asset.Asset;
import com.jeans.tinyitsm.model.asset.Hardware;
import com.jeans.tinyitsm.model.asset.Software;
import com.jeans.tinyitsm.model.view.AssetItem;
import com.jeans.tinyitsm.model.view.Grid;
import com.jeans.tinyitsm.model.view.HardwareItem;
import com.jeans.tinyitsm.model.view.SoftwareItem;
import com.jeans.tinyitsm.service.asset.AssetService;
import com.jeans.tinyitsm.service.hr.HRConstants;
import com.jeans.tinyitsm.service.hr.HRService;

public class AssetAction extends BaseAction<Grid<AssetItem>> {

	private AssetService assetService;
	private HRService hrService;

	@Autowired
	public void setAssetService(AssetService assetService) {
		this.assetService = assetService;
	}

	@Autowired
	public void setHrService(HRService hrService) {
		this.hrService = hrService;
	}

	private byte type;

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	private AssetItem asset;

	public AssetItem getAsset() {
		return asset;
	}

	public void setAsset(AssetItem asset) {
		this.asset = asset;
	}

	@Action(value = "load-asset", results = { @Result(type = "json", params = { "root", "asset" }) })
	public String loadAsset() throws Exception {
		Asset a = assetService.loadAsset(id, type);
		if (null != a) {
			if (a instanceof Hardware) {
				asset = HardwareItem.createInstance((Hardware) a, hrService.getUnit(a.getCompanyId(), HRConstants.COMPANY),
						hrService.getUnit(((Hardware) a).getOwnerId(), HRConstants.EMPLOYEE));
			} else if (a instanceof Software) {
				asset = SoftwareItem.createInstance((Software) a, hrService.getUnit(a.getCompanyId(), HRConstants.COMPANY));
			}
		}
		return SUCCESS;
	}

	@Action(value = "load-assets", results = { @Result(type = "json", params = { "root", "data" }) })
	public String loadAssets() throws Exception {
		data = assetService.loadAssets(getCurrentCompanyId(), type, page, rows);
		if (null != sort && data.getRows().size() > 0) {
			Collections.sort(data.getRows(), new Comparator<AssetItem>() {

				@Override
				public int compare(AssetItem o1, AssetItem o2) {
					int ret = 0;
					boolean asc = "asc".equals(order);
					boolean isHardware = o1 instanceof HardwareItem;
					switch (sort) {
					case "code":
						ret = compString(((HardwareItem) o1).getCode(), ((HardwareItem) o2).getCode(), asc);
						break;
					case "financialCode":
						ret = compString(((HardwareItem) o1).getFinancialCode(), ((HardwareItem) o2).getFinancialCode(), asc);
						break;
					case "name":
						if (isHardware) {
							ret = compString(((HardwareItem) o1).getName(), ((HardwareItem) o2).getName(), asc);
						} else {
							ret = compString(((SoftwareItem) o1).getName(), ((SoftwareItem) o2).getName(), asc);
						}
						break;
					case "vendor":
						if (isHardware) {
							ret = compString(((HardwareItem) o1).getVendor(), ((HardwareItem) o2).getVendor(), asc);
						} else {
							ret = compString(((SoftwareItem) o1).getVendor(), ((SoftwareItem) o2).getVendor(), asc);
						}
						break;
					case "modelOrVersion":
						if (isHardware) {
							ret = compString(((HardwareItem) o1).getModelOrVersion(), ((HardwareItem) o2).getName(), asc);
						} else {
							ret = compString(((SoftwareItem) o1).getModelOrVersion(), ((SoftwareItem) o2).getModelOrVersion(), asc);
						}
						break;
					case "sn":
						ret = compString(((HardwareItem) o1).getSn(), ((HardwareItem) o2).getSn(), asc);
						break;
					case "purchaseTime":
						Date t1 = isHardware ? ((HardwareItem) o1).getPurchaseTime() : ((SoftwareItem) o1).getPurchaseTime();
						Date t2 = isHardware ? ((HardwareItem) o2).getPurchaseTime() : ((SoftwareItem) o2).getPurchaseTime();
						if (null == t1) {
							if (null == t2) {
								ret = 0;
							} else {
								ret = asc ? -1 : 1;
							}
						} else {
							if (null == t2) {
								ret = asc ? 1 : -1;
							} else {
								ret = asc ? t1.compareTo(t2) : t2.compareTo(t1);
							}
						}
						break;
					case "quantity":
						if (isHardware) {
							ret = asc ? Integer.compare(((HardwareItem) o1).getQuantity(), ((HardwareItem) o2).getQuantity()) : Integer.compare(
									((HardwareItem) o2).getQuantity(), ((HardwareItem) o1).getQuantity());
						} else {
							ret = asc ? Integer.compare(((SoftwareItem) o1).getQuantity(), ((SoftwareItem) o2).getQuantity()) : Integer.compare(
									((SoftwareItem) o2).getQuantity(), ((SoftwareItem) o1).getQuantity());
						}
						break;
					case "cost":
						BigDecimal d1 = isHardware ? ((HardwareItem) o1).getCost() : ((SoftwareItem) o1).getCost();
						BigDecimal d2 = isHardware ? ((HardwareItem) o2).getCost() : ((SoftwareItem) o2).getCost();
						if (null == d1) {
							d1 = new BigDecimal(0.0);
						}
						if (null == d2) {
							d2 = new BigDecimal(0.0);
						}
						ret = asc ? d1.compareTo(d2) : d2.compareTo(d1);
						break;
					case "state":
						if (isHardware) {
							ret = compString(((HardwareItem) o1).getState(), ((HardwareItem) o2).getState(), asc);
						} else {
							ret = compString(((SoftwareItem) o1).getState(), ((SoftwareItem) o2).getState(), asc);
						}
						break;
					case "warranty":
						ret = compString(((HardwareItem) o1).getWarranty(), ((HardwareItem) o2).getWarranty(), asc);
						break;
					case "location":
						ret = compString(((HardwareItem) o1).getLocation(), ((HardwareItem) o2).getLocation(), asc);
						break;
					case "ip":
						ret = compString(((HardwareItem) o1).getIp(), ((HardwareItem) o2).getIp(), asc);
						break;
					case "importance":
						ret = compString(((HardwareItem) o1).getImportance(), ((HardwareItem) o2).getImportance(), asc);
						break;
					case "owner":
						ret = compString(((HardwareItem) o1).getOwner(), ((HardwareItem) o2).getOwner(), asc);
						break;
					case "softwareType":
						ret = compString(((SoftwareItem) o1).getSoftwareType(), ((SoftwareItem) o2).getSoftwareType(), asc);
						break;
					case "license":
						ret = compString(((SoftwareItem) o1).getLicense(), ((SoftwareItem) o2).getLicense(), asc);
						break;
					case "expiredTime":
						Date e1 = ((SoftwareItem) o1).getPurchaseTime();
						Date e2 = ((SoftwareItem) o2).getPurchaseTime();
						if (null == e1) {
							if (null == e2) {
								ret = 0;
							} else {
								ret = asc ? -1 : 1;
							}
						} else {
							if (null == e2) {
								ret = asc ? 1 : -1;
							} else {
								ret = asc ? e1.compareTo(e2) : e2.compareTo(e1);
							}
						}
					}
					return ret;
				}

				private int compString(String s1, String s2, boolean asc) {
					if (null == s1) {
						if (null == s2) {
							return 0;
						} else {
							return asc ? -1 : 1;
						}
					} else {
						if (null == s2) {
							return asc ? 1 : -1;
						} else {
							return asc ? Collator.getInstance(java.util.Locale.CHINA).compare(s1, s2) : Collator.getInstance(java.util.Locale.CHINA).compare(
									s2, s1);
						}
					}
				}

			});
		}
		return SUCCESS;
	}

	private Map<String, Object> props;

	public Map<String, Object> getProps() {
		return props;
	}

	public void setProps(Map<String, Object> props) {
		this.props = props;
	}

	public String loadProps() throws Exception {
		props = new HashMap<String, Object>();
		List<Asset> assets = assetService.loadAssets(splitIds(), type);
		for (Asset asset : assets) {
			mergeProps(asset);
		}
		return SUCCESS;
	}

	private void mergeProps(Asset asset) {
		// TODO 合并属性
	}
}
