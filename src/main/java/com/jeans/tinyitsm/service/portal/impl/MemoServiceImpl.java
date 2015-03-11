package com.jeans.tinyitsm.service.portal.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeans.tinyitsm.dao.BaseDao;
import com.jeans.tinyitsm.model.portal.Memo;
import com.jeans.tinyitsm.model.portal.User;
import com.jeans.tinyitsm.service.portal.MemoService;

@Service
public class MemoServiceImpl implements MemoService {

	private BaseDao<Memo> memoDao;

	@Autowired
	public void setMemoDao(BaseDao<Memo> memoDao) {
		this.memoDao = memoDao;
	}

	@Override
	@Transactional(readOnly = true)
	public Memo getMemo(long userId, Date memoDate) {
		String hql = "from Memo where owner.id = :p_ownerId and memoDate = :p_memoDate";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("p_ownerId", userId);
		params.put("p_memoDate", memoDate);
		Memo memo = memoDao.getByHql(hql, params);
		return memo;
	}

	@Override
	@Transactional
	public Memo newMemo(User owner, String text, Date memoDate) {
		if (StringUtils.isBlank(text))
			return null;
		String t = StringUtils.left(text, 255);
		Memo memo = new Memo();
		memo.setText(t);
		memo.setMemoDate(memoDate);
		memo.setOwner(owner);
		Serializable id = memoDao.save(memo);
		if (null != id) {
			return memo;
		} else {
			return null;
		}
	}

	@Override
	@Transactional
	public Memo updateMemo(long id, String text) {
		String t = StringUtils.isBlank(text) ? "" : StringUtils.left(text, 255);
		Memo memo = memoDao.getById(Memo.class, id);
		if (null == memo)
			return null;
		memo.setText(t);
		memoDao.update(memo);
		return memo;
	}

}
