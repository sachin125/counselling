package com.inn.counselling.dao.generic;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inn.counselling.dao.criteria.IQueryCriteriaBuilder;
import com.inn.counselling.dao.criteria.IQueryExecutor;
import com.inn.counselling.dao.generic.IBaseDao;
import com.inn.counselling.utils.QueryObject;

public abstract class AbstractBaseDao<Pk, Entity> implements IBaseDao<Pk, Entity> {
	
	private static final Logger logger = LoggerFactory.getLogger(AbstractBaseDao.class);
	
	private Class<Entity> type;

	private IQueryCriteriaBuilder<Entity> criteriaBuilder;
	
	private IQueryExecutor<Entity> queryExecutor;
	
	protected AbstractBaseDao(Class<Entity> type) {
		this.type = type;
	}
	
	public Class<Entity> getType() {
		return type;
	}

	public void setType(Class<Entity> type) {
		this.type = type;
	}
	
	public IQueryCriteriaBuilder<Entity> getCriteriaBuilder() {
		return criteriaBuilder;
	}

	public void setCriteriaBuilder(IQueryCriteriaBuilder<Entity> criteriaBuilder) {
		this.criteriaBuilder = criteriaBuilder;
	}

	public IQueryExecutor<Entity> getQueryExecutor() {
		return queryExecutor;
	}

	public void setQueryExecutor(IQueryExecutor<Entity> queryExecutor) {
		this.queryExecutor = queryExecutor;
	}

	public List<Entity>find(QueryObject queryObject)throws Exception{
		Object criteria = criteriaBuilder.buildCriteria(queryObject, getType());
		return queryExecutor.select(criteria);
	}

	public long count(QueryObject queryObject) throws Exception{
		queryObject.setPaginationLowerLimit(-1);
		queryObject.setPaginationUpperLimit(-1);
		Object criteria = criteriaBuilder.buildCriteria(queryObject, getType());
		return queryExecutor.count(criteria);
	}
	
	
}
