package com.jeans.tinyitsm.service.hr;

import java.util.List;

import com.jeans.tinyitsm.model.hr.Employee;
import com.jeans.tinyitsm.model.view.HRUnit;

/**
 * Retrieve HR data
 * 
 * @author Majorli
 *
 */
public interface HRService {

	/**
	 * get the root organization unit node, typically the headquarters
	 * 
	 * return null if there is no root unit (id==1)
	 * 
	 * @return HRUnit
	 */
	public HRUnit getRoot();

	/**
	 * get the specific unit node
	 * 
	 * if id<=0, get root(change id to 1)
	 * 
	 * return null if there is not such a node
	 * 
	 * @param id
	 * @param unitType
	 *            from HRUnit.EMPLOYEE_NODE, DEPARTMENT_NODE, COMPANY_NODE
	 * @return HRUnit
	 */
	public HRUnit getUnit(long id, byte unitType);

	/**
	 * 根据部门名称在一个指定公司内找到该部门
	 * 
	 * @param compId
	 * @param name
	 * @return
	 */
	public HRUnit getDepartmentByName(long compId, String name);

	/**
	 * 根据姓名在一个指定公司内找到员工
	 * 
	 * @param compId
	 * @param name
	 * @return
	 */
	public HRUnit getEmployeeByName(long compId, String name);

	/**
	 * organizations tree should can be serialized to json as:
	 * [
	 * {"id":id,"text":"name or alias","children":null},
	 * {"id":id,"text":"name or alias","children":
	 * [
	 * {"id":id,"text":"name or alias","children":null}
	 * ...
	 * ]
	 * ...}
	 * ]
	 * so, the data structure is: List<HRUnit>
	 * 
	 * if unit== null, get tree of the root unit
	 * if there is no root unit, return empty list
	 * if unit.getType() == employee_node, return empty list
	 * 
	 * @param rootId
	 * @param treeType
	 * @param includeRoot
	 * @return List<HRUnit>
	 */
	public List<HRUnit> getOrgTree(long id, OrgTreeType treeType, boolean includeRoot);

	/**
	 * include sub-organizations or employees according to children type
	 * 
	 * if the root unit is a department and children type is branches, return empty list;
	 * if the root unit is a department and children type is sub-organizations, return only sub-departments;
	 * <!-- if the root unit is the root company(id <= 1) and children type is sub-organizations, exclude branches; -->
	 * if the root unit is a company and children type is employees or former employees or history departments, return recursive results;
	 * other conditions, no recusive.
	 * 
	 * if unit==null or unit is employee or any other exceptions, return empty list
	 * 
	 * @param parentId
	 * @return list of children units, not tree view
	 */
	public List<HRUnit> getChildren(long id, ChildrenType type);

	/*
	 * next 6 methods are convenient methods of getChildren(id, type)
	 */
	public List<HRUnit> getOrgChildren(long id);

	public List<HRUnit> getCompChildren(long id);

	public List<HRUnit> getDeptChildren(long id);

	public List<HRUnit> getEmployees(long id);

	public List<HRUnit> getFormerEmployees(long id);

	public List<HRUnit> getHistoryDepartments(long id);

	/**
	 * create a new department or employee, type must be HRConstants.DEPARTMENT or HRConstants.EMPLOYEE, others will cause error and return null.
	 * when create new department, id is the superior unit's id; when create new employee, id is the department's id and it can't be a company id.
	 * 
	 * @param id
	 * @param type
	 * @return the new unit or null if failed.
	 */
	public HRUnit create(long id, byte type);

	/**
	 * update department(s) or employee(s), type must be HRConstants.DEPARTMENT or HRConstants.EMPLOYEE, others will cause error and return -1.
	 * 
	 * @param units
	 * @param type
	 * @return updated units' counting, or -1 if failed.
	 */
	public int update(List<HRUnit> units, byte type);

	/**
	 * move employee (id) to new department (targetId), if targetId points to a company, do nothing an return false.
	 * 
	 * @param id
	 * @param targetId
	 * @return
	 */
	public boolean employeeMove(long id, long targetId);

	/**
	 * 员工离职（离退休、离开浙江烟草商业专卖系统、死亡......）
	 * 
	 * @param id
	 * @return
	 */
	public boolean employeeLeave(long id);

	/**
	 * 部门停用
	 * 
	 * @param id
	 * @return
	 */
	public int departmentAbandon(long id);

	/**
	 * 新增员工，指定姓名
	 * 
	 * @param name
	 * @param deptId
	 * @param listOrder
	 * @return
	 */
	public HRUnit appendEmpl(String name, long deptId, short listOrder);

	/**
	 * 新增部门，指定名称、简称
	 * 
	 * @param name
	 * @param alias
	 * @param superId
	 * @param listOrder
	 * @return
	 */
	public HRUnit appendDept(String name, String alias, long superId, short listOrder);

	/**
	 * 获取指定员工的实际对象
	 * 
	 * @param id 员工id
	 * @return
	 */
	public Employee getEmployee(long id);
}
