package com.jeans.tinyitsm.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.SearchFactory;
import org.hibernate.search.query.dsl.PhraseMatchingContext;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jeans.tinyitsm.dao.FtsDao;

@SuppressWarnings("unchecked")
@Repository
public class FtsDaoImpl<T> implements FtsDao<T> {

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

	/**
	 * 获取由当前Hibernate事务session包裹生成的FullTextSession
	 * 
	 * @return
	 */
	public FullTextSession getFullTextSession() {
		return Search.getFullTextSession(getCurrentSession());
	}

	@Override
	public void reIndex() throws InterruptedException {
		getFullTextSession().createIndexer().startAndWait();
	}

	@Override
	public List<T> query(Class<T> c, String[] fields, String keyword) {
		FullTextSession fullTextSession = getFullTextSession();
		SearchFactory searchFactory = fullTextSession.getSearchFactory();
		QueryBuilder queryBuilder = searchFactory.buildQueryBuilder().forEntity(c).get();
		Query query = fullTextSession.createFullTextQuery(queryBuilder.keyword().onFields(fields).matching(keyword).createQuery(), c);
		List<T> results = query.list();
		return results;
	}
	
	@Override
	public List<T> query(Class<T> c, String[] fields, String keyword, String scopeField, long scopeId) {
		FullTextSession fullTextSession = getFullTextSession();
		SearchFactory searchFactory = fullTextSession.getSearchFactory();
		QueryBuilder queryBuilder = searchFactory.buildQueryBuilder().forEntity(c).get();
		org.apache.lucene.search.Query fuzzyQuery = queryBuilder.keyword().onFields(fields).matching(keyword).createQuery();
		org.apache.lucene.search.Query scopeQuery = queryBuilder.range().onField(scopeField).from(scopeId).to(scopeId).createQuery();
		Query query = fullTextSession.createFullTextQuery(
				queryBuilder.bool().must(scopeQuery).must(fuzzyQuery).createQuery(), c);
		List<T> results = query.list();
		return results;
	}

	@Override
	public List<T> query(Class<T> c, String[] fuzzyFields, String[] exactFields, String keyword, String scopeField, long scopeId) {
		FullTextSession fullTextSession = getFullTextSession();
		SearchFactory searchFactory = fullTextSession.getSearchFactory();
		QueryBuilder queryBuilder = searchFactory.buildQueryBuilder().forEntity(c).get();
		org.apache.lucene.search.Query fuzzyQuery = queryBuilder.keyword().onFields(fuzzyFields).matching(keyword).createQuery();
		PhraseMatchingContext exactContext = queryBuilder.phrase().onField(exactFields[0]);
		for (int i = 1; i < exactFields.length; i++) {
			exactContext = exactContext.andField(exactFields[i]);
		}
		org.apache.lucene.search.Query exactQuery = exactContext.sentence(keyword).createQuery();
		org.apache.lucene.search.Query scopeQuery = queryBuilder.range().onField(scopeField).from(scopeId).to(scopeId).createQuery();
		Query query = fullTextSession.createFullTextQuery(
				queryBuilder.bool().must(scopeQuery).must(queryBuilder.bool().should(exactQuery).should(fuzzyQuery).createQuery()).createQuery(), c);
		List<T> results = query.list();
		return results;
	}
}
