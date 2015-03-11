package com.jeans.tinyitsm.service.portal.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeans.tinyitsm.dao.BaseDao;
import com.jeans.tinyitsm.model.portal.User;
import com.jeans.tinyitsm.model.view.HRUnit;
import com.jeans.tinyitsm.service.hr.HRService;
import com.jeans.tinyitsm.service.portal.PortalConstants;
import com.jeans.tinyitsm.service.portal.UserService;
import com.jeans.tinyitsm.util.EncodeUtil;

@Service
public class UserServiceImpl implements UserService {

	private BaseDao<User> userDao;
	private HRService hrService;

	@Autowired
	public void setUserDao(BaseDao<User> userDao) {
		this.userDao = userDao;
	}

	@Autowired
	public void setHrService(HRService hrService) {
		this.hrService = hrService;
	}

	@Override
	@Transactional(readOnly = true)
	public User getUser(long compId, long emplId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("p_compId", compId);
		params.put("p_emplId", emplId);
		String hql = "from User where companyId = :p_compId and employeeId = :p_emplId and available = true";
		return userDao.getUniqueResult(hql, params);
	}

	@Override
	@Transactional(readOnly = true)
	public int userLogin(User user, String password) {
		if (user == null) {
			return PortalConstants.LOGIN_USER_INACTIVE;
		} else {
			if (!user.isAvailable()) {
				return PortalConstants.LOGIN_USER_UNAVAILABLE;
			} else {
				if (EncodeUtil.encodeBySHA1(password).equals(user.getPassword())) {
					return PortalConstants.LOGIN_SUCCESS;
				} else {
					return PortalConstants.LOGIN_FAILED;
				}
			}
		}
	}

	@Override
	@Transactional
	public boolean activeUser(User user) {
		User u = getUser(user.getCompanyId(), user.getEmployeeId());
		if (u != null) {
			return false;
		} else {
			user.encodePassword();
			user.setAvailable(true);
			userDao.save(user);
			return true;
		}
	}

	@Override
	@Transactional
	public int setAvailable(Collection<Long> ids, boolean avail) {
		String hql = "update User set available = :p_avail where employeeId in (:p_ids)";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("p_avail", avail);
		params.put("p_ids", ids);
		return userDao.executeHql(hql, params);
	}

	@Override
	@Transactional
	public int changeCompany(Collection<Long> ids, long compId) {
		String hql = "update User set companyId = :p_comp where employeeId in (:p_ids)";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("p_comp", compId);
		params.put("p_ids", ids);
		return userDao.executeHql(hql, params);
	}

	@Override
	@Transactional
	public boolean rename(long id, String username) {
		String hql = "update User set username = :p_username where id = :p_id and employeeId <> 0";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("p_username", username);
		params.put("p_id", id);
		return userDao.executeHql(hql, params) == 1;
	}

	@Override
	@Transactional
	public boolean changePassword(long id, String oldPwd, String newPwd) {
		String hql = "update User set password = :p_newPwd where id = :p_id and password = :p_oldPwd";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("p_newPwd", EncodeUtil.encodeBySHA1(newPwd));
		params.put("p_id", id);
		params.put("p_oldPwd", EncodeUtil.encodeBySHA1(oldPwd));
		return userDao.executeHql(hql, params) == 1;
	}

	@Override
	@Transactional
	public boolean changePassword(long emplId, long compId, String oldPwd, String newPwd) {
		String hql = "update User set password = :p_newPwd where employeeId = :p_emplId and companyId = :p_compId and password = :p_oldPwd";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("p_newPwd", EncodeUtil.encodeBySHA1(newPwd));
		params.put("p_emplId", emplId);
		params.put("p_compId", compId);
		params.put("p_oldPwd", EncodeUtil.encodeBySHA1(oldPwd));
		return userDao.executeHql(hql, params) == 1;
	}

	@Override
	@Transactional
	public boolean updateRoles(long id, boolean iter, boolean admin, boolean leader, boolean supervisor, boolean auditor) {
		String hql = "update User set iter = :p_iter, admin = :p_admin, leader = :p_leader, supervisor = :p_supervisor, auditor = :p_auditor where id = :p_id";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("p_iter", iter);
		params.put("p_admin", admin);
		params.put("p_id", id);
		params.put("p_leader", leader);
		params.put("p_supervisor", supervisor);
		params.put("p_auditor", auditor);
		return userDao.executeHql(hql, params) == 1;
	}

	@Override
	@Transactional
	public boolean updateRoles(long id, boolean iter, boolean admin) {
		String hql = "update User set iter = :p_iter, admin = :p_admin where id = :p_id";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("p_iter", iter);
		params.put("p_admin", admin);
		params.put("p_id", id);
		return userDao.executeHql(hql, params) == 1;
	}

	@Override
	@Transactional(readOnly = true)
	public User findUser(long compId, String keyword) {
		User user = null;
		long id = 0;
		try {
			id = Long.parseLong(keyword);
			user = userDao.getById(User.class, id);
			if ((null != user) && (user.getCompanyId() != compId || user.getEmployeeId() == 0 || !user.isAvailable())) {
				user = null;
			}
		} catch (NumberFormatException e) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("p_keyword", StringUtils.trim(keyword));
			params.put("p_compId", compId);
			String hql = "from User where username = :p_keyword and companyId = :p_compId and available = true and employeeId <> 0";
			user = userDao.getByHql(hql, params);
			if (null == user) {
				HRUnit employee = hrService.getEmployeeByName(compId, keyword);
				if (null != employee) {
					hql = "from User where employeeId = " + employee.getId() + " and available = true";
					user = userDao.getByHql(hql);
				}
			}
		}
		return user;
	}

	@Override
	@Transactional(readOnly = true)
	public List<User> getITers(long compId) {
		List<User> iters = new ArrayList<User>();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("p_compId", compId);
		String hql = "from User where companyId = :p_compId and iter = true and available = true";
		iters = userDao.find(hql, params);
		return iters;
	}
}
