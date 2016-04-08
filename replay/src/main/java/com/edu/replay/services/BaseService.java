package com.edu.replay.services;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BaseService {
	@Resource
	private SessionFactory sessionFactory;
	
	private Query getQuery(String queryString,Object... objs){
		Query query=sessionFactory.getCurrentSession().createQuery(queryString);
		for(int i=0;i<objs.length;i++){
			query.setParameter(i, objs[i]);
		}
		return query;
	}
	
	protected void delete(Object obj) {
		sessionFactory.getCurrentSession().delete(obj);
	}

	protected Object getFirst(String queryString, Object... objs) {
		Query query=getQuery(queryString, objs);
		return query.uniqueResult();
	}

	@SuppressWarnings("rawtypes")
	protected List find(String queryString, Object... objs) {
		Query query=getQuery(queryString, objs);
		List list = query.list();
		if (list == null)
			return new ArrayList();
		else
			return list;
	}

	@SuppressWarnings("rawtypes")
	protected List findTopN(int n, String queryString, Object... objs) {
		List list = find(queryString, objs);
		int toIndex = n > list.size() ? list.size() : n;
		return list.subList(0, toIndex);
	}

	@SuppressWarnings("unchecked")
	protected <T> T get(Class<T> entityName, Serializable id) {
		return (T) sessionFactory.getCurrentSession().get(entityName, id);
	}

	protected void save(Object obj) {
		sessionFactory.getCurrentSession().save(obj);
	}

	protected void update(Object obj) {
		sessionFactory.getCurrentSession().update(obj);
	}

	@SuppressWarnings("rawtypes")
	protected List queryForPage(int pageNo,int pageSize,String queryString,Object... objs){
		Query query=getQuery(queryString, objs);
		return query.setFirstResult((pageNo-1)*pageSize).setMaxResults(pageSize).list();
	}
}
