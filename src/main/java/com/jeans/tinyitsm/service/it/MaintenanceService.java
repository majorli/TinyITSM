package com.jeans.tinyitsm.service.it;

import java.util.List;

import com.jeans.tinyitsm.model.it.Maintenance;

public interface MaintenanceService {

	/**
	 * 获取指定id的运维项目
	 * 
	 * @param id
	 *            运维项目id
	 * @return
	 */
	public Maintenance getMaintenance(long id);

	/**
	 * 获取指定公司主管的运维项目
	 * 
	 * @param companyId
	 *            主管公司id
	 * @return
	 */
	public List<Maintenance> getMaintenances(long companyId);
}
