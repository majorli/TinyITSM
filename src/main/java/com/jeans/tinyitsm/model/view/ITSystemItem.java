package com.jeans.tinyitsm.model.view;

import java.io.Serializable;
import java.util.Date;

import org.apache.struts2.json.annotations.JSON;

import com.jeans.tinyitsm.model.it.ITSystem;

public class ITSystemItem implements Serializable {

	private ITSystem system;

	public ITSystemItem() {
		this.system = null;
	}

	public ITSystemItem(ITSystem system) {
		this.system = system;
	}

	public Long getId() {
		return null == system ? null : system.getId();
	}

	public String getName() {
		return null == system ? null : system.getName();
	}

	public String getAlias() {
		return null == system ? null : system.getAlias();
	}

	public String getType() {
		return null == system ? null : system.getType().getTitle();
	}

	public String getModelOrVersion() {
		return null == system ? null : system.getModelOrVersion();
	}

	public String getBrief() {
		return null == system ? null : system.getBrief();
	}

	public String getSecurityLevel() {
		if (null == system) {
			return null;
		} else {
			String[] level = new String[] { "未定级", "一级", "二级", "三级" };
			byte l = system.getSecurityLevel();
			if (l <= 0 || l > 3) {
				return "未定级";
			} else {
				return level[l] + "，备案编号：" + system.getSecurityCode();
			}
		}
	}

	public String getUserBrief() {
		return null == system ? null : system.getUsersBrief();
	}

	public String getProvider() {
		return null == system ? null : system.getProvider();
	}

	public String getOwner() {
		return null == system ? null : system.getOwner().getAlias();
	}

	public String getScope() {
		return null == system ? null : system.getScope().getTitle();
	}

	public String getDeploy() {
		return null == system ? null : system.getDeploy().getTitle();
	}

	public Integer getBranches() {
		return null == system ? null : system.getBranches().size();
	}

	public String getStage() {
		return null == system ? null : system.getStage().getTitle();
	}

	@JSON(format = "yyyy年MM月dd日")
	public Date getConstructedTime() {
		return null == system ? null : system.getConstructedTime();
	}

	public Integer getFreeMaintainMonths() {
		return null == system ? null : system.getFreeMaintainMonths();
	}

	@JSON(format = "yyyy年MM月dd日")
	public Date getAbandonedTime() {
		return null == system ? null : system.getAbandonedTime();
	}
}
