package com.inn.counselling.dao.criteria.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inn.counselling.dao.annotation.CriteriaExecutor;
import com.inn.counselling.dao.criteria.IQueryExecutor;

@CriteriaExecutor
public class JPAQueryExecutor<Entity> implements IQueryExecutor<Entity> {

	private static final Logger logger = LoggerFactory.getLogger(JPAQueryExecutor.class);
	
	public JPAQueryExecutor() {
		super();
	}

	public List<Entity> select(Object criteria) {
		logger.debug("Entry inside @class JPAQueryExecutor @method select @param criteria");
		return null;
	}

	public int count(Object criteria) {
		logger.debug("Entry inside @class JPAQueryExecutor @method count @param count");
		return 0;
	}

}
