package com.inn.counselling.dao.criteria.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.inn.counselling.dao.criteria.IQueryCriteriaBuilder;
import com.inn.counselling.utils.QueryObject;

@com.inn.counselling.dao.annotation.CriteriaBuilder
public class JPACriteriaBuilder<Entity> implements IQueryCriteriaBuilder<Entity> {

	private static final Logger logger = LoggerFactory.getLogger(HibernateCriteriaBuilder.class);

	@PersistenceContext(name = "DEFAULT" )
	private EntityManager entityManager;
	
	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	@Override
	public Object buildCriteria (QueryObject queryObject, Class<Entity> type){
		logger.debug("Entry inside @class JPACriteriaBuilder @method applyPagination @param queryObject "+queryObject+" type: "+type);
		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Entity> criteriaQuery = criteriaBuilder.createQuery(type);
		Root<Entity> root = criteriaQuery.from(type);
		criteriaQuery.select(root);
		addWhereClause(criteriaQuery, queryObject);
		
		return null;
	}

	private void addWhereClause(CriteriaQuery<Entity> cQuery, QueryObject queryObject) {
	}

}
