package com.inn.counselling.dao.criteria;

import com.inn.counselling.utils.QueryObject;

/**
 * Represents the criteria builder interface. Implementing class shall be 
 * responsible for generating criteria for the provided query object based
 * upon the underlying technology. 
 * 
 *
 */
public interface IQueryCriteriaBuilder<Entity> {
	/**
	 * converts the query object into a query.
	 * @param queryObject
	 * @param entityClass
	 * @return
	 */
	Object buildCriteria(QueryObject queryObject, Class<Entity> entityClass);
}
