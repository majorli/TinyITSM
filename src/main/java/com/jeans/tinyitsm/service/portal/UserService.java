package com.jeans.tinyitsm.service.portal;

import java.util.Collection;
import java.util.List;

import com.jeans.tinyitsm.model.portal.User;

public interface UserService {

	/**
	 * 根据员工编号和公司编号获取用户
	 * 
	 * @param compId
	 * @param emplId
	 * @return
	 */
	public User getUser(long compId, long emplId);

	/**
	 * 用户登录判断
	 * 
	 * @param user
	 * @param password
	 * @return PortalConstants.LOGIN_XXX (0..3)
	 */
	public int userLogin(User user, String password);

	/**
	 * 激活用户，根据传入的user对象（瞬态、未保存）创建一条新的users表记录，创建后user对象完成密码加密和id生成
	 * 
	 * @param user
	 * @return
	 */
	public boolean activeUser(User user);

	/**
	 * 启用/注销已激活的用户，可同时操作多个
	 * 
	 * @param ids
	 * @return
	 */
	public int setAvailable(Collection<Long> ids, boolean avail);

	/**
	 * 用户改变所在公司，可同时将多个用户改变到某一公司
	 * 
	 * @param ids
	 * @param compId
	 * @return
	 */
	public int changeCompany(Collection<Long> ids, long compId);

	/**
	 * 设置用户名，管理员用户（employeeId == 0）不能设置用户名
	 * 
	 * @param id
	 * @param username
	 * @return
	 */
	public boolean rename(long id, String username);

	/**
	 * 修改密码，老密码要正确
	 * 
	 * @param id
	 *            用户Id
	 * @param oldPwd
	 * @param newPwd
	 * @return
	 */
	public boolean changePassword(long id, String oldPwd, String newPwd);

	/**
	 * 修改密码，老密码要正确管理员的emplId为0
	 * 
	 * @param emplId
	 *            用户对应的员工Id，管理员用户为0
	 * @param compId
	 *            用户对应的员工所在公司Id
	 * @param oldPwd
	 * @param newPwd
	 * @return
	 */
	public boolean changePassword(long emplId, long compId, String oldPwd, String newPwd);

	/**
	 * 改变用户的所有角色
	 * 
	 * @param id
	 * @param iter
	 * @param admin
	 * @param leader
	 * @param supervisor
	 * @param auditor
	 * @return
	 */
	public boolean updateRoles(long id, boolean iter, boolean admin, boolean leader, boolean supervisor, boolean auditor);

	/**
	 * 改变用户的特殊角色
	 * 
	 * @param id
	 * @param iter
	 * @param admin
	 * @return
	 */
	public boolean updateRoles(long id, boolean iter, boolean admin);

	/**
	 * 根据关键字在指定公司中搜索用户，关键字匹配用户Id、用户名、员工姓名，精确搜索，只返回一个结果
	 * 
	 * @param compId
	 *            公司ID
	 * @param keyword
	 *            搜索关键字
	 * @return
	 */
	public User findUser(long compId, String keyword);

	/**
	 * 获取某公司的信息化人员列表
	 * 
	 * @param compId
	 *            公司Id
	 * @return
	 */
	public List<User> getITers(long compId);
}
