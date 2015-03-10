package com.jeans.tinyitsm.dao.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hibernate.NaturalIdLoadAccess;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jeans.tinyitsm.dao.BaseDao;

@SuppressWarnings("unchecked")
@Repository
public class BaseDaoImpl<T> implements BaseDao<T> {

	private SessionFactory sessionFactory;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/**
	 * 获得当前事务的session
	 * 
	 * @return org.hibernate.Session
	 */
	public Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	public Serializable save(T o) {
		if (o != null) {
			return getCurrentSession().save(o);
		}
		return null;
	}

	@Override
	public T getById(Class<T> c, Serializable id) {
		return (T) getCurrentSession().byId(c).load(id);
	}

	@Override
	public T getByNaturalId(Class<T> c, Map<String, Object> naturalIds) {
		NaturalIdLoadAccess access = getCurrentSession().byNaturalId(c);
		for (String key : naturalIds.keySet())
			access.using(key, naturalIds.get(key));
		return (T) access.load();
	}
	
	@Override
	public T getByHql(String hql) {
		Query q = getCurrentSession().createQuery(hql);
		List<T> l = q.list();
		if (l != null && l.size() > 0) {
			return l.get(0);
		}
		return null;
	}

	@Override
	public T getByHql(String hql, Map<String, Object> params) {
		Query q = getCurrentSession().createQuery(hql);
		if (params != null && !params.isEmpty()) {
			for (String key : params.keySet()) {
				Object p = params.get(key);
				if (p instanceof Collection)
					q.setParameterList(key, (Collection<?>) p);
				else
					q.setParameter(key, params.get(key));
			}
		}
		List<T> l = q.list();
		if (l != null && l.size() > 0) {
			return l.get(0);
		}
		return null;
	}
	
	@Override
	public T getUniqueResult(String hql){
		return (T) getCurrentSession().createQuery(hql).uniqueResult();
	}

	@Override
	public T getUniqueResult(String hql, Map<String, Object> params){
		Query q = getCurrentSession().createQuery(hql);
		if (params != null && !params.isEmpty()) {
			for (String key : params.keySet()) {
				Object p = params.get(key);
				if (p instanceof Collection)
					q.setParameterList(key, (Collection<?>) p);
				else
					q.setParameter(key, params.get(key));
			}
		}
		return (T) q.uniqueResult();
	}
	
	@Override
	public void delete(T o) {
		if (o != null) {
			getCurrentSession().delete(o);
		}
	}

	@Override
	public void update(T o) {
		if (o != null) {
			getCurrentSession().update(o);
		}
	}

	@Override
	public void saveOrUpdate(T o) {
		if (o != null) {
			getCurrentSession().saveOrUpdate(o);
		}
	}

	@Override
	public List<T> find(String hql) {
		Query q = getCurrentSession().createQuery(hql);
		return q.list();
	}

	@Override
	public List<T> find(String hql, Map<String, Object> params) {
		Query q = getCurrentSession().createQuery(hql);
		if (params != null && !params.isEmpty()) {
			for (String key : params.keySet()) {
				Object p = params.get(key);
				if (p instanceof Collection)
					q.setParameterList(key, (Collection<?>) p);
				else
					q.setParameter(key, params.get(key));
			}
		}
		return q.list();
	}

	@Override
	public List<T> find(String hql, Map<String, Object> params, int page, int rows) {
		Query q = getCurrentSession().createQuery(hql);
		if (params != null && !params.isEmpty()) {
			for (String key : params.keySet()) {
				Object p = params.get(key);
				if (p instanceof Collection)
					q.setParameterList(key, (Collection<?>) p);
				else
					q.setParameter(key, params.get(key));
			}
		}
		return q.setFirstResult((page - 1) * rows).setMaxResults(rows).list();
	}

	@Override
	public List<T> find(String hql, int page, int rows) {
		Query q = getCurrentSession().createQuery(hql);
		return q.setFirstResult((page - 1) * rows).setMaxResults(rows).list();
	}

	@Override
	public Long count(String hql) {
		Query q = getCurrentSession().createQuery("select count(*) " + hql);
		return (Long) q.uniqueResult();
	}

	@Override
	public Long count(String hql, Map<String, Object> params) {
		Query q = getCurrentSession().createQuery("select count(*) " + hql);
		if (params != null && !params.isEmpty()) {
			for (String key : params.keySet()) {
				Object p = params.get(key);
				if (p instanceof Collection)
					q.setParameterList(key, (Collection<?>) p);
				else
					q.setParameter(key, params.get(key));
			}
		}
		return (Long) q.uniqueResult();
	}

	@Override
	public int executeHql(String hql) {
		Query q = getCurrentSession().createQuery(hql);
		return q.executeUpdate();
	}

	@Override
	public int executeHql(String hql, Map<String, Object> params) {
		Query q = getCurrentSession().createQuery(hql);
		if (params != null && !params.isEmpty()) {
			for (String key : params.keySet()) {
				Object p = params.get(key);
				if (p instanceof Collection)
					q.setParameterList(key, (Collection<?>) p);
				else
					q.setParameter(key, params.get(key));
			}
		}
		return q.executeUpdate();
	}

	@Override
	public int executeSql(String sql) {
		SQLQuery q = getCurrentSession().createSQLQuery(sql);
		return q.executeUpdate();
	}

	@Override
	public int executeSql(String sql, Map<String, Object> params) {
		SQLQuery q = getCurrentSession().createSQLQuery(sql);
		if (params != null && !params.isEmpty()) {
			for (String key : params.keySet()) {
				Object p = params.get(key);
				if (p instanceof Collection)
					q.setParameterList(key, (Collection<?>) p);
				else
					q.setParameter(key, params.get(key));
			}
		}
		return q.executeUpdate();
	}
}
