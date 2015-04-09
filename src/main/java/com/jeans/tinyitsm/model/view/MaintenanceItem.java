package com.jeans.tinyitsm.model.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.jeans.tinyitsm.model.it.ITSystem;
import com.jeans.tinyitsm.model.it.Maintenance;

public class MaintenanceItem implements Serializable {

	private Maintenance mtn;

	public MaintenanceItem() {
		this.mtn = null;
	}

	public MaintenanceItem(Maintenance mtn) {
		this.mtn = mtn;
	}

	public Long getId() {
		return null == mtn ? null : mtn.getId();
	}

	public String getName() {
		return null == mtn ? null : mtn.getName();
	}

	public String getOwner() {
		return null == mtn ? null : mtn.getOwner().getAlias();
	}

	public String getSystems() {
		if (null == mtn) {
			return null;
		} else {
			List<String> names = new ArrayList<String>();
			Set<ITSystem> systems = mtn.getSystems();
			for (ITSystem system : systems) {
				names.add(system.getAlias());
			}
			return StringUtils.join(names, ",");
		}
	}

	public String getProvider() {
		return null == mtn ? null : mtn.getProvider();
	}

	public String getTeam() {
		return null == mtn ? null : mtn.getMaintainTeam().getName();
	}
}
