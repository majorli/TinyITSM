package com.jeans.tinyitsm.service.it.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeans.tinyitsm.dao.BaseDao;
import com.jeans.tinyitsm.model.hr.Organization;
import com.jeans.tinyitsm.model.it.Maintenance;
import com.jeans.tinyitsm.service.hr.HRService;
import com.jeans.tinyitsm.service.it.MaintenanceService;

@Service
public class MaintenanceServiceImpl implements MaintenanceService {

	private BaseDao<Maintenance> mtnDao;

	private HRService hrService;

	@Autowired
	public void setMtnDao(BaseDao<Maintenance> mtnDao) {
		this.mtnDao = mtnDao;
	}

	@Autowired
	public void setHrService(HRService hrService) {
		this.hrService = hrService;
	}

	@Override
	@Transactional(readOnly = true)
	public Maintenance getMaintenance(long id) {
		return mtnDao.getById(Maintenance.class, id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Maintenance> getMaintenances(long companyId) {
		List<Maintenance> mtns = new ArrayList<Maintenance>();
		Organization company = hrService.getCompany(companyId);
		if (null != company) {
			String hql = "from Maintenance where owner = :p_owner";
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("p_owner", company);
			mtns = mtnDao.find(hql, params);
		}
		return mtns;
	}

}
