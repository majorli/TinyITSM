package com.jeans.tinyitsm.service.it;

import java.util.List;

import com.jeans.tinyitsm.model.it.Project;

public interface ProjectService {

	/**
	 * 根据id获取信息化建设项目
	 * 
	 * @param id
	 *            项目id
	 * @return
	 */
	public Project getProject(long id);

	/**
	 * 获取指定公司所属的信息化建设项目
	 * 
	 * @param companyId
	 *            公司id
	 * @return
	 */
	public List<Project> getProjects(long companyId);
}
