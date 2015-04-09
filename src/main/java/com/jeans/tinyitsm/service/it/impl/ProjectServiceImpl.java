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
import com.jeans.tinyitsm.model.it.Project;
import com.jeans.tinyitsm.service.hr.HRService;
import com.jeans.tinyitsm.service.it.ProjectService;

@Service
public class ProjectServiceImpl implements ProjectService {

	private BaseDao<Project> prjDao;

	private HRService hrService;

	@Autowired
	public void setPrjDao(BaseDao<Project> prjDao) {
		this.prjDao = prjDao;
	}

	@Autowired
	public void setHrService(HRService hrService) {
		this.hrService = hrService;
	}

	@Override
	@Transactional(readOnly = true)
	public Project getProject(long id) {
		return prjDao.getById(Project.class, id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Project> getProjects(long companyId) {
		List<Project> p = new ArrayList<Project>();
		Organization c = hrService.getCompany(companyId);
		if (null != c) {
			String hql = "from Project where owner = :p_owner";
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("p_owner", c);
			p = prjDao.find(hql, params);
		}
		return p;
	}

}
