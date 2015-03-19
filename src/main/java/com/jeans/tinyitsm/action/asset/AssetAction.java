package com.jeans.tinyitsm.action.asset;

import java.math.BigDecimal;
import java.text.Collator;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.jeans.tinyitsm.action.BaseAction;
import com.jeans.tinyitsm.model.asset.Asset;
import com.jeans.tinyitsm.model.asset.Hardware;
import com.jeans.tinyitsm.model.asset.Software;
import com.jeans.tinyitsm.model.view.AssetItem;
import com.jeans.tinyitsm.model.view.AssetValidateResult;
import com.jeans.tinyitsm.model.view.Grid;
import com.jeans.tinyitsm.model.view.HardwareItem;
import com.jeans.tinyitsm.model.view.SoftwareItem;
import com.jeans.tinyitsm.service.asset.AssetConstants;
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

	/**
	 * 用于资产管理界面的属性编辑弹出窗口
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "load-props", results = { @Result(type = "json", params = { "root", "props" }) })
	public String loadProps() throws Exception {
		props = new HashMap<String, Object>();
		List<Asset> assets = assetService.loadAssets(splitIds(), type);
		for (Asset asset : assets) {
			mergeProps(asset);
		}
		return SUCCESS;
	}

	/**
	 * 对于当前资产的某一个属性：<br>
	 * 如果当前props中没有这个key，那么就用当前资产的属性置入<br>
	 * 如果当前props中有这个key而且value==null或者value和当前资产的属性相等，那么不发生任何变化，如果不等就置为null<br>
	 * 
	 * @param asset
	 */
	private void mergeProps(Asset asset) {
		merge("name", asset.getName());
		merge("vendor", asset.getVendor());
		merge("modelOrVersion", asset.getModelOrVersion());
		merge("assetUsage", asset.getAssetUsage());
		merge("purchaseTime", asset.getPurchaseTime());
		merge("quantity", asset.getQuantity());
		merge("cost", asset.getCost());
		merge("comment", asset.getComment());
		if (asset instanceof Hardware) {
			merge("code", ((Hardware) asset).getCode());
			merge("financialCode", ((Hardware) asset).getFinancialCode());
			merge("sn", ((Hardware) asset).getSn());
			merge("configuration", ((Hardware) asset).getConfiguration());
			merge("warranty", ((Hardware) asset).getWarranty());
			merge("location", ((Hardware) asset).getLocation());
			merge("ip", ((Hardware) asset).getIp());
			merge("importance", ((Hardware) asset).getImportance());
		} else if (asset instanceof Software) {
			merge("softwareType", ((Software) asset).getSoftwareType());
			merge("license", ((Software) asset).getLicense());
			merge("expiredTime", ((Software) asset).getExpiredTime());
		}
	}

	private void merge(String key, Object value) {
		if (null == value) {
			// 传入了null，只有可能是purchaseTime或者expiredTime两个时间值，均表示没有这个属性的值，直接置入null即可
			props.put(key, value);
		} else {
			if (props.containsKey(key)) {
				// 当前props里有这个key
				Object oldValue = props.get(key);
				if (null != oldValue && !value.equals(oldValue)) {
					// 原值不为null表示以前merge入的所有这个属性均为统一值
					// 如果现在的value不统一了，那么就把原来的值设置为null，以表示多个值不统一了
					props.put(key, null);
				}
				// 其他情况，即要么原值为null表示原来就已经不是统一值了，要么value和原来那个统一的值相等，都不需要对props做任何改变
			} else {
				// props里还没有这个key，直接置入即可
				props.put(key, value);
			}
		}
	}

	private int result;

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	/**
	 * 保存一到多个资产的属性编辑结果，数值型属性可能传入负数，日期型属性可能传入空串，选择型属性可能传入-99，字符串属性可能传入空串<br>
	 * 所有类型的属性传入到Map中全部是String类型，需要自己做类型转换，由于前端使用了严格的验证所以类型转换可以不考虑异常情况<br>
	 * <br>
	 * 当只编辑一项资产时： <li>数值型属性为负数时保存该属性的最小值； <li>日期型属性为空串时保存为null； <li>选择型属性为-99时保持原值不变； <li>字符串属性直接保存。<br>
	 * 当编辑多项资产时：<br> <li>数值型属性为负数时保持各项资产该属性原值不变； <li>日期型属性为空串时保持各项资产该属性原值不变； <li>选择型属性为-99时保持各项资产该属性原值不变； <li>字符串属性为空串时保持各项资产该属性原值不变。
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "save-props", results = { @Result(type = "json", params = { "root", "result" }) })
	public String saveProps() throws Exception {
		result = assetService.saveProps(splitIds(), type, transProps());
		return SUCCESS;
	}

	/**
	 * 转换前端传来的属性值Map对象，前端传来的所有属性值都是String[]类型，数组里应该有一个元素且不会等于null<br>
	 * 如果发生数据错误，直接抛出异常给action方法，再由action方法抛出给容器<br>
	 * 如果转换成功，只有时间类型的属性才有可能是null
	 * 
	 * @return
	 * @throws Exception
	 */
	private Map<String, Object> transProps() throws Exception {
		Map<String, Object> p = new HashMap<String, Object>();
		Set<String> keys = props.keySet();
		for (String key : keys) {
			String value = ((String[]) props.get(key))[0];
			if ("expiredTime".equals(key) || "purchaseTime".equals(key)) {
				p.put(key, "".equals(value) ? null : (new SimpleDateFormat("yyyy-MM-dd")).parse(value));
			} else if ("quantity".equals(key)) {
				p.put(key, Integer.parseInt(value));
			} else if ("cost".equals(key)) {
				p.put(key, BigDecimal.valueOf(Double.parseDouble(value)));
			} else if ("warranty".equals(key) || "importance".equals(key) || "softwareType".equals(key)) {
				p.put(key, Byte.parseByte(value));
			} else {
				p.put(key, value);
			}
		}
		return p;
	}

	private Map<String, String> validation;

	public Map<String, String> getValidation() {
		return validation;
	}

	public void setValidation(Map<String, String> validation) {
		this.validation = validation;
	}

	@Action(value = "asset-vali", results = { @Result(type = "json", params = { "root", "validation" }) })
	public String vali() throws Exception {
		validation = new LinkedHashMap<String, String>();
		List<AssetValidateResult> v = assetService.validate(getCurrentCompanyId());
		for (AssetValidateResult r : v) {
			StringBuilder builder = new StringBuilder("<li>");
			switch (r.getResult()) {
			case AssetConstants.INVALID_OWNER:
				builder.append("错误：").append(r.getAssetCatalogName()).append("<span>[").append(r.getAssetFullName()).append("]</span>登记的责任人不存在！");
				break;
			case AssetConstants.IN_USE_ASSET_OWNED_BY_FORMER_EMPLOYEE:
				builder.append("错误：").append(r.getAssetCatalogName()).append("<span>[").append(r.getAssetFullName()).append("]</span>使用状态为在用，但登记的责任人<span>[")
						.append(r.getOwnerName()).append("]</span>已经离职！");
				break;
			case AssetConstants.INVALID_OWNER_COMPANY:
				builder.append("错误：").append(r.getAssetCatalogName()).append("<span>[").append(r.getAssetFullName()).append("]</span>登记的责任人<span>[")
						.append(r.getOwnerName()).append("]</span>属于<span>[").append(r.getOwnerCompanyName())
						.append("]</span>，不是本公司员工！&emsp;<a href=\"javascript:void(0);\" class=\"_Chcmp\">调整所属公司</a>");
				break;
			case AssetConstants.IN_USE_ASSET_WITHOUT_OWNER:
				builder.append("疑问：").append(r.getAssetCatalogName()).append("<span>[").append(r.getAssetFullName()).append("]</span>使用状态为在用，但没有登记责任人！");
			}
			builder.append("&emsp;<a href=\"javascript:void(0);\" class=\"_Chown\">设置责任人</a>&emsp;<a href=\"javascript:void(0);\" class=\"_SetIdle\">回收该资产</a></li>");
			validation.put(r.getId().toString(), builder.toString());
		}
		return SUCCESS;
	}

	private Set<Byte> nextStates;

	public Set<Byte> getNextStates() {
		return nextStates;
	}

	public void setNextStates(Set<Byte> nextStates) {
		this.nextStates = nextStates;
	}

	/**
	 * 检查可以设置的下一种状态<br>
	 * <ul>
	 * 软件类资产：
	 * <li>在用 -> 备用/淘汰
	 * <li>备用 -> 在用/淘汰
	 * <li>淘汰 -> null <br>
	 * <br>
	 * 硬件类资产(状态变为在用时需要选择责任人)：
	 * <li>在用 -> 在用/备用/维修
	 * <li>备用 -> 在用/维修/淘汰
	 * <li>维修 -> 在用/备用/淘汰（转移为在用时默认选中原责任人，如果有的话）
	 * <li>淘汰 -> 报损
	 * <li>报损 -> null <br>
	 * <br>
	 * 多项资产一起检查时取各项交集，可能为空
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "check-state", results = { @Result(type = "json", params = { "root", "nextStates" }) })
	public String checkState() throws Exception {
		nextStates = assetService.checkNextStates(splitIds(), type);
		return SUCCESS;
	}

	private byte state; // 新资产状态
	private long owner; // 新资产责任人（硬件类）
	private boolean keep; // 是否保留原有的责任人（多个资产同时修改时）

	public byte getState() {
		return state;
	}

	public void setState(byte state) {
		this.state = state;
	}

	public long getOwner() {
		return owner;
	}

	public void setOwner(long owner) {
		this.owner = owner;
	}

	public boolean isKeep() {
		return keep;
	}

	public void setKeep(boolean keep) {
		this.keep = keep;
	}

	/**
	 * 调整资产的使用状态
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "change-state", results = { @Result(type = "json", params = { "root", "result" }) })
	public String changeState() throws Exception {
		result = assetService.changeState(splitIds(), type, state, owner, keep);
		return SUCCESS;
	}

	/**
	 * 根据校验结果调整硬件类资产数据的调整方式<br>
	 * 0=调整责任人，1=回收资产，2=根据责任人调整所属公司
	 */
	private byte adjustType;

	public byte getAdjustType() {
		return adjustType;
	}

	public void setAdjustType(byte adjustType) {
		this.adjustType = adjustType;
	}

	/**
	 * 根据校验结果调整硬件类资产数据的调整方式，每次调整一项，提交id属性<br>
	 * adjustType: 0=调整责任人(需要提交owner属性); 1=回收资产; 2=根据责任人调整所属公司
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action(value = "adjust", results = { @Result(type = "json", params = { "root", "result" }) })
	public String adjust() throws Exception {
		result = assetService.adjust(id, adjustType, owner);
		return SUCCESS;
	}
}
