package com.jeans.tinyitsm.service.portal.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeans.tinyitsm.dao.BaseDao;
import com.jeans.tinyitsm.model.portal.Rss;
import com.jeans.tinyitsm.model.portal.Subscription;
import com.jeans.tinyitsm.model.portal.User;
import com.jeans.tinyitsm.model.view.CloudNoti;
import com.jeans.tinyitsm.service.portal.SubscriptionService;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

	private BaseDao<Subscription> subDao;

	@Autowired
	public void setSubDao(BaseDao<Subscription> subDao) {
		this.subDao = subDao;
	}

	@Override
	@Transactional(readOnly = true)
	public long checkNews(long userId) {
		String hql = "from Subscription where receiver.id = " + userId + " and newRss = true";
		return subDao.count(hql);
	}

	@Override
	@Transactional
	public List<CloudNoti> refresh(long id, User user) {
		List<CloudNoti> notes = new ArrayList<CloudNoti>();
		StringBuilder builder = new StringBuilder("from Subscription where receiver = :p_receiver");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("p_receiver", user);
		List<Subscription> subs = null;
		if (id <= 0) {
			builder.append(" order by id desc");
			subs = subDao.find(builder.toString(), params, 1, 10);
		} else {
			builder.append(" and id > :p_id order by id desc");
			params.put("p_id", id);
			subs = subDao.find(builder.toString(), params);
		}
		for (Subscription sub : subs) {
			CloudNoti note = new CloudNoti();
			note.setId(sub.getId());
			note.setNewRss(sub.isNewRss());
			Rss rss = sub.getRss();
			note.setPublishTime(rss.getPublishTime());
			note.setText(rss.getText());
			note.setType(rss.getType());
			notes.add(note);
			if (sub.isNewRss()) {
				sub.setNewRss(false);
				subDao.update(sub);
			}
		}
		return notes;
	}

	@Override
	@Transactional
	public List<CloudNoti> more(long id, User user) {
		List<CloudNoti> notes = new ArrayList<CloudNoti>();
		StringBuilder builder = new StringBuilder("from Subscription where receiver = :p_receiver");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("p_receiver", user);
		List<Subscription> subs = null;
		if (id <= 0) {
			builder.append(" order by id desc");
			subs = subDao.find(builder.toString(), params, 1, 10);
		} else {
			builder.append(" and id < :p_id order by id desc");
			params.put("p_id", id);
			subs = subDao.find(builder.toString(), params, 1, 10);
		}
		for (Subscription sub : subs) {
			CloudNoti note = new CloudNoti();
			note.setId(sub.getId());
			note.setNewRss(sub.isNewRss());
			Rss rss = sub.getRss();
			note.setPublishTime(rss.getPublishTime());
			note.setText(rss.getText());
			note.setType(rss.getType());
			notes.add(note);
			if (sub.isNewRss()) {
				sub.setNewRss(false);
				subDao.update(sub);
			}
		}
		return notes;
	}
}
