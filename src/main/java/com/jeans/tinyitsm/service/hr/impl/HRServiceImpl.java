package com.jeans.tinyitsm.service.hr.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeans.tinyitsm.HREventObserver;
import com.jeans.tinyitsm.dao.BaseDao;
import com.jeans.tinyitsm.event.HREvent;
import com.jeans.tinyitsm.event.HREventType;
import com.jeans.tinyitsm.model.hr.Employee;
import com.jeans.tinyitsm.model.hr.Organization;
import com.jeans.tinyitsm.model.view.HRUnit;
import com.jeans.tinyitsm.service.hr.ChildrenType;
import com.jeans.tinyitsm.service.hr.HRConstants;
import com.jeans.tinyitsm.service.hr.HRService;
import com.jeans.tinyitsm.service.hr.OrgTreeType;

@Service
public class HRServiceImpl implements HRService {

	private BaseDao<Employee> emplDao;

	private BaseDao<Organization> deptDao;

	@Autowired
	public void setEmplDao(BaseDao<Employee> emplDao) {
		this.emplDao = emplDao;
	}

	@Autowired
	public void setDeptDao(BaseDao<Organization> deptDao) {
		this.deptDao = deptDao;
	}

	@Override
	public HRUnit getRoot() {
		return getUnit(1L, HRConstants.COMPANY);
	}

	@Override
	@Transactional(readOnly = true)
	public HRUnit getUnit(long id, byte unitType) {
		if (id <= 0 && unitType != HRConstants.EMPLOYEE)
			id = 1;
		HRUnit u = null;
		switch (unitType) {
		case HRConstants.EMPLOYEE:
			Employee e = emplDao.getById(Employee.class, id);
			if (e != null) {
				u = new HRUnit(e.getId(), e.getName(), e.getDepartment().getAlias(), unitType, e.getListOrder(), null);
			}
			break;
		case HRConstants.DEPARTMENT:
		case HRConstants.COMPANY:
		default:
			Organization o = deptDao.getById(Organization.class, id);
			if (o != null) {
				u = new HRUnit(o.getId(), o.getName(), o.getAlias(), unitType, o.getListOrder(), null);
			}
		}
		return u;
	}

	@Override
	@Transactional(readOnly = true)
	public HRUnit getDepartmentByName(long compId, String name) {
		if (null == getUnit(compId, HRConstants.COMPANY))
			return null;
		HRUnit d = null;
		Organization o = deptDao.getByHql("from Organization where subRoot.id = " + compId + " and name = '" + name + "' and state = 0 and type = 1");
		if (null != o) {
			d = new HRUnit(o.getId(), o.getName(), o.getAlias(), o.getType(), o.getListOrder(), null);
		}
		return d;
	}
	
	@Override
	@Transactional(readOnly = true)
	public HRUnit getEmployeeByName(long compId, String name) {
		if (null == getUnit(compId, HRConstants.COMPANY))
			return null;
		HRUnit e = null;
		Employee emp = emplDao.getByHql("from Employee where department.subRoot.id = " + compId + " and name = '" + name + "' and state < 2");
		if (null != emp) {
			e = new HRUnit(emp.getId(), emp.getName(), emp.getDepartment().getAlias(), HRConstants.EMPLOYEE, emp.getListOrder(), null);
		}
		return e;
	}

	@Override
	@Transactional(readOnly = true)
	public List<HRUnit> getOrgTree(long id, OrgTreeType treeType, boolean includeRoot) {
		Organization o = (id <= 0) ? deptDao.getById(Organization.class, 1L) : deptDao.getById(Organization.class, id);
		if (null == o)
			o = deptDao.getById(Organization.class, 1L);
		HRUnit root = generateTree(o, treeType);
		if (includeRoot) {
			List<HRUnit> tree = new ArrayList<HRUnit>();
			tree.add(root);
			return tree;
		} else {
			return root.getChildren();
		}
	}

	private HRUnit generateTree(Organization root, OrgTreeType treeType) {
		HRUnit tree = new HRUnit(root.getId(), root.getName(), root.getAlias(), root.getType(), root.getListOrder(), null);
		List<HRUnit> children = new ArrayList<HRUnit>();
		Iterator<Organization> it = root.getInferiors().iterator();
		while (it.hasNext()) {
			Organization c = it.next();
			if (treeType == OrgTreeType.DepartmentsTree && c.getType() == HRConstants.COMPANY)
				continue;
			if (treeType == OrgTreeType.BranchesTree && c.getType() == HRConstants.DEPARTMENT)
				continue;
			if (treeType == OrgTreeType.FullTree && root.getId() <= 1 && c.getType() == HRConstants.COMPANY)
				continue;
			if (c.getState() == HRConstants.HISTORY)
				continue;
			children.add(generateTree(c, treeType));
		}
		tree.setChildren(children);
		return tree;
	}

	@Override
	@Transactional(readOnly = true)
	public List<HRUnit> getChildren(long id, ChildrenType type) {
		Organization p = (id <= 0) ? deptDao.getById(Organization.class, 1L) : deptDao.getById(Organization.class, id);
		if (type == ChildrenType.Branches && p.getType() == HRConstants.DEPARTMENT) {
			return new ArrayList<HRUnit>();
		}
		List<HRUnit> n = new ArrayList<HRUnit>();

		switch (type) {
		case Departments:
		case HistoryDepartments: {
			List<Organization> inf = null;
			if (type == ChildrenType.HistoryDepartments && p.getType() == HRConstants.COMPANY) {
				String hql = "from Organization where subRoot.id = :p_subRoot and type = 1 and state = 1 order by superior, listOrder";
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("p_subRoot", id);
				inf = deptDao.find(hql, params);
			} else {
				inf = p.getInferiors();
			}
			Iterator<Organization> it = inf.iterator();
			while (it.hasNext()) {
				Organization c = it.next();
				if (c.getType() == HRConstants.COMPANY)
					continue;
				if (type == ChildrenType.Departments && c.getState() == HRConstants.HISTORY)
					continue;
				if (type == ChildrenType.HistoryDepartments && c.getState() == HRConstants.NORMAL)
					continue;
				n.add(new HRUnit(c.getId(), c.getName(), c.getAlias(), HRConstants.DEPARTMENT, c.getListOrder(), null));
			}
			break;
		}
		case Branches:
		case SubOrganizations: {
			List<Organization> inf = null;
			inf = p.getInferiors();
			Iterator<Organization> it = inf.iterator();
			while (it.hasNext()) {
				Organization c = it.next();
				if (type == ChildrenType.Branches && c.getType() == HRConstants.DEPARTMENT)
					continue;
				/*
				 * if (type == ChildrenType.SubOrganizations && p.getId() <= 1 && c.getType() == HRConstants.COMPANY)
				 * continue;
				 */
				if (c.getState() == HRConstants.HISTORY)
					continue;
				n.add(new HRUnit(c.getId(), c.getName(), c.getAlias(), c.getType(), c.getListOrder(), null));
			}
			break;
		}
		case Employees:
		case FormerEmployees: {
			List<Employee> emp = null;
			if (p.getType() == HRConstants.DEPARTMENT) {
				emp = p.getEmployees();
			} else {
				String hql = "from Employee where department in (select id from Organization where subRoot.id = :p_subRoot and type = 1) order by department, state, listOrder";
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("p_subRoot", id);
				emp = emplDao.find(hql, params);
			}
			Iterator<Employee> it = emp.iterator();
			while (it.hasNext()) {
				Employee e = it.next();
				if (type == ChildrenType.Employees && e.getState() == HRConstants.FORMER)
					continue;
				if (type == ChildrenType.FormerEmployees && e.getState() != HRConstants.FORMER)
					continue;
				n.add(new HRUnit(e.getId(), e.getName(), e.getDepartment().getAlias(), HRConstants.EMPLOYEE, e.getListOrder(), null));
			}
		}
		default:
		}

		return n;
	}

	@Override
	public List<HRUnit> getOrgChildren(long id) {
		return getChildren(id, ChildrenType.SubOrganizations);
	}

	@Override
	public List<HRUnit> getCompChildren(long id) {
		return getChildren(id, ChildrenType.Branches);
	}

	@Override
	public List<HRUnit> getDeptChildren(long id) {
		return getChildren(id, ChildrenType.Departments);
	}

	@Override
	public List<HRUnit> getEmployees(long id) {
		return getChildren(id, ChildrenType.Employees);
	}

	@Override
	public List<HRUnit> getFormerEmployees(long id) {
		return getChildren(id, ChildrenType.FormerEmployees);
	}

	@Override
	public List<HRUnit> getHistoryDepartments(long id) {
		return getChildren(id, ChildrenType.HistoryDepartments);
	}

	@Override
	@Transactional
	public HRUnit create(long id, byte type) {
		if (type == HRConstants.DEPARTMENT)
			return createDept(null, null, id, (short) 127);
		else if (type == HRConstants.EMPLOYEE)
			return createEmpl(null, id, (short) 127);
		else
			return null;
	}

	private HRUnit createDept(String name, String alias, long superiorId, short listOrder) {
		if (superiorId <= 0)
			return null;

		Organization sup = deptDao.getById(Organization.class, superiorId);
		if (sup == null || sup.getState() == HRConstants.HISTORY)
			return null;

		Organization dept = new Organization();
		dept.setName((null == name) ? "新部门" : name);
		dept.setAlias((null == alias) ? "新部门" : alias);
		dept.setState(HRConstants.NORMAL);
		dept.setType(HRConstants.DEPARTMENT);
		dept.setListOrder((listOrder <= 0) ? (short) 1 : listOrder);
		dept.setSuperior(sup);
		if (sup.getType() == HRConstants.COMPANY)
			dept.setSubRoot(sup);
		else
			dept.setSubRoot(sup.getSubRoot());
		long id = (long) deptDao.save(dept);

		HREvent e = new HREvent();
		e.setCompId(dept.getSubRoot().getId());
		e.setType(HREventType.NewDepartmentCreated);
		e.addTarget(id, dept.getName());
		HREventObserver.fire(e);

		return new HRUnit(id, dept.getName(), dept.getAlias(), dept.getType(), dept.getListOrder(), null);
	}

	private HRUnit createEmpl(String name, long departmentId, short listOrder) {
		if (departmentId <= 0)
			return null;

		Organization dep = deptDao.getById(Organization.class, departmentId);
		if (dep == null || dep.getType() == HRConstants.COMPANY || dep.getState() == HRConstants.HISTORY)
			return null;

		Employee empl = new Employee();
		empl.setName((null == name) ? "新员工" : name);
		empl.setDepartment(dep);
		empl.setListOrder((listOrder <= 0) ? (short) 1 : listOrder);
		empl.setState(HRConstants.NORMAL);
		long id = (long) emplDao.save(empl);

		HREvent e = new HREvent();
		e.setCompId(dep.getSubRoot().getId());
		e.setType(HREventType.NewEmployeeCreated);
		e.addTarget(id, empl.getName());
		HREventObserver.fire(e);

		return new HRUnit(id, empl.getName(), empl.getDepartment().getAlias(), HRConstants.EMPLOYEE, empl.getListOrder(), null);
	}

	@Override
	@Transactional
	public int update(List<HRUnit> units, byte type) {
		if (type == HRConstants.EMPLOYEE)
			return updateEmpl(units);
		else if (type == HRConstants.DEPARTMENT)
			return updateDept(units);
		else
			return -1;
	}

	private int updateDept(List<HRUnit> updates) {
		if (null == updates)
			return 0;
		String hql = "update Organization set name = :p_name, alias = :p_alias, listOrder = :p_listOrder where id = :p_id";
		int count = 0;
		Map<String, Object> params = new HashMap<String, Object>();
		Iterator<HRUnit> it = updates.iterator();
		while (it.hasNext()) {
			HRUnit n = it.next();
			params.put("p_name", n.getName());
			params.put("p_alias", n.getAlias());
			params.put("p_listOrder", n.getListOrder());
			params.put("p_id", n.getId());
			count += deptDao.executeHql(hql, params);
		}
		return count;
	}

	private int updateEmpl(List<HRUnit> empls) {
		if (null == empls)
			return 0;
		String hql = "update Employee set name = :p_name, listOrder = :p_listOrder where id = :p_id";
		int count = 0;
		Map<String, Object> params = new HashMap<String, Object>();
		Iterator<HRUnit> it = empls.iterator();
		while (it.hasNext()) {
			HRUnit n = it.next();
			params.put("p_name", n.getName());
			params.put("p_listOrder", n.getListOrder());
			params.put("p_id", n.getId());
			count += emplDao.executeHql(hql, params);
		}
		return count;
	}

	@Override
	@Transactional
	public boolean employeeMove(long id, long targetId) {
		Employee empl = emplDao.getById(Employee.class, id);
		Organization newDept = deptDao.getById(Organization.class, targetId);
		if (null == empl || null == newDept || newDept.getState() == HRConstants.HISTORY || newDept.getType() == HRConstants.COMPANY
				|| empl.getState() == HRConstants.FORMER || empl.getDepartment().getId() == targetId)
			return false;
		
		Long oldCompId = empl.getDepartment().getSubRoot().getId();
		empl.setDepartment(newDept);
		emplDao.update(empl);
		Long newCompId = newDept.getSubRoot().getId();
		
		HREvent e = new HREvent();
		e.setCompId(oldCompId);
		e.setNewCompId(newCompId);
		e.setType(HREventType.EmployeeMoved);
		e.addTarget(id, empl.getName());
		HREventObserver.fire(e);
		
		return true;
	}

	@Override
	@Transactional
	public boolean employeeLeave(long id) {
		Employee empl = emplDao.getById(Employee.class, id);
		if (null == empl || empl.getState() == HRConstants.FORMER)
			return false;
		
		empl.setState(HRConstants.FORMER);
		emplDao.update(empl);

		HREvent e = new HREvent();
		e.setCompId(empl.getDepartment().getSubRoot().getId());
		e.setType(HREventType.EmployeeLeft);
		e.addTarget(id, empl.getName());
		HREventObserver.fire(e);

		return true;
	}

	@Override
	@Transactional
	public int departmentAbandon(long id) {
		if (id <= 0)
			return 0;

		int count = 0;
		Organization dept = deptDao.getById(Organization.class, id);
		if (null == dept || dept.getState() == HRConstants.HISTORY)
			return -1;
		
		List<Long> subIds = new ArrayList<Long>(), empIds = new ArrayList<Long>();
		abandonCheck(subIds, empIds, dept);
		if (empIds.size() > 0)
			return -1;

		String hql = "update Organization set state = 1 where id in (:p_ids)";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("p_ids", subIds);
		count = deptDao.executeHql(hql, params);
		
		if (count > 0) {
			HREvent e = new HREvent();
			e.setCompId(dept.getSubRoot().getId());
			e.setType(HREventType.DepartmentAbandoned);
			e.addTarget(dept.getId(), dept.getName());
			HREventObserver.fire(e);
		}
		
		return count;
	}

	private void abandonCheck(List<Long> subIds, List<Long> empIds, Organization dept) {
		if (dept.getState() == HRConstants.NORMAL)
			subIds.add(dept.getId());

		Iterator<Employee> empIt = dept.getEmployees().iterator();
		while (empIt.hasNext()) {
			Employee e = empIt.next();
			if (e.getState() == HRConstants.NORMAL)
				empIds.add(e.getId());
		}

		Iterator<Organization> subIt = dept.getInferiors().iterator();
		while (subIt.hasNext()) {
			Organization o = subIt.next();
			abandonCheck(subIds, empIds, o);
		}
			
	}

	@Override
	@Transactional
	public HRUnit appendEmpl(String name, long deptId, short listOrder) {
		return createEmpl(name, deptId, listOrder);
	}

	@Override
	@Transactional
	public HRUnit appendDept(String name, String alias, long superId, short listOrder) {
		return createDept(name, alias, superId, listOrder);
	}
}
