package com.jeans.tinyitsm.service.portal.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeans.tinyitsm.dao.BaseDao;
import com.jeans.tinyitsm.event.HREvent;
import com.jeans.tinyitsm.event.HREventType;
import com.jeans.tinyitsm.event.itsm.Event;
import com.jeans.tinyitsm.model.portal.Notification;
import com.jeans.tinyitsm.service.portal.MessageConstants;
import com.jeans.tinyitsm.service.portal.NotificationService;

@Service
public class NotificationServiceImpl implements NotificationService {

	private BaseDao<Notification> notiDao;

	@Autowired
	public void setNotiDao(BaseDao<Notification> notiDao) {
		this.notiDao = notiDao;
	}

	@Override
	@Transactional
	public Notification publish(byte source, String text, long companyId) {
		if (companyId <= 0 || StringUtils.isBlank(text) || source < 0 || source > 8)
			return null;
		Notification noti = new Notification();
		noti.setText(StringUtils.trim(text));
		noti.setCompanyId(companyId);
		noti.setSource(source);
		noti.setPublishTime(new Date());
		notiDao.save(noti);

		return noti;
	}

	@Override
	@Transactional
	public List<Notification> publish(byte source, String text, Set<Long> companyIds) {
		List<Notification> noti = new ArrayList<Notification>();
		if (!companyIds.isEmpty()) {
			for (long companyId : companyIds) {
				Notification n = publish(source, text, companyId);
				if (null != n) {
					noti.add(n);
				}
			}
		}
		return noti;
	}

	@Override
	@Transactional
	public List<Notification> publish(HREvent e) {
		List<Notification> noti = new ArrayList<Notification>();
		HREventType t = e.getType();
		if (t != HREventType.EmployeeLeft && t != HREventType.EmployeeMoved) {
			return noti;
		}

		Notification n = new Notification();
		StringBuilder builder = new StringBuilder(e.getType().getTitle());
		builder.append(t == HREventType.EmployeeLeft ? "，以下员工已经离职：" : "，以下员工已经调离原岗位：");
		Map<Long, String> targets = e.getTargets();
		for (long id : targets.keySet())
			builder.append(targets.get(id)).append("(id=").append(id).append(") ");
		n.setText(builder.toString());
		n.setSource(MessageConstants.TINY_HR);
		n.setCompanyId(e.getCompId());
		n.setPublishTime(new Date());
		notiDao.save(n);
		noti.add(n);

		if (t == HREventType.EmployeeMoved && e.getCompId() != e.getNewCompId()) {
			Notification n2 = new Notification();
			BeanUtils.copyProperties(n, n2);
			n2.setText(StringUtils.replace(n2.getText(), "调离原岗位", "调入本公司"));
			n2.setCompanyId(e.getNewCompId());

			notiDao.save(n2);
			noti.add(n2);
		}

		return noti;
	}

	@Override
	@Transactional
	public List<Notification> publish(Event<?> e, Set<Long> companyIds) {
		List<Notification> noti = new ArrayList<Notification>();
		for (long id : companyIds) {
			Notification n = new Notification();
			n.setText(e.getMessage());
			n.setPublishTime(new Date());
			n.setCompanyId(id);
			switch (e.getClass().getSimpleName()) {
			case "CloudEvent":
				n.setSource(MessageConstants.TINY_CLOUD);
				break;
			case "AssetEvent":
				n.setSource(MessageConstants.TINY_ASSET);
				break;
			case "ProjectEvent":
				n.setSource(MessageConstants.TINY_PROJECT);
				break;
			case "MaintainEvent":
				n.setSource(MessageConstants.TINY_MAINTAIN);
				break;
			case "ServiceEvent":
				n.setSource(MessageConstants.TINY_SERVICE);
				break;
			default:
				n.setSource(MessageConstants.ADMIN);
			}
			n.setPublishTime(new Date());
			notiDao.save(n);
			noti.add(n);
		}
		return noti;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Notification> loadNew(long toId, int rows, long companyId) {
		List<Notification> ret = null;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("p_compId", companyId);
		StringBuilder builder = new StringBuilder("from Notification where companyId = :p_compId");
		if (toId > 0) {
			builder.append(" and id > :p_toId order by id desc");
			params.put("p_toId", toId);
			ret = notiDao.find(builder.toString(), params);
		} else {
			builder.append(" order by id desc");
			ret = notiDao.find(builder.toString(), params, 1, rows <= 0 ? 20 : rows);
		}
		return ret;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Notification> loadMore(long fromId, int rows, long companyId) {
		List<Notification> ret = null;
		String hql = "from Notification where companyId = :p_compId and id < :p_toId order by id desc";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("p_compId", companyId);
		params.put("p_toId", fromId);
		ret = notiDao.find(hql, params, 1, rows <= 0 ? 20 : rows);
		return ret;
	}
}
