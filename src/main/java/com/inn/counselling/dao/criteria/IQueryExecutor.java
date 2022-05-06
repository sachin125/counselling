package com.inn.counselling.dao.criteria;

import java.util.List;

/**
 * Represents the query executor service interface. This implementing class shall be
 * responsible for providing the custom way to generate query and execute it.
 * 
 * @param <Entity> represents the JPA entity class.
 */
public interface IQueryExecutor<Entity> {
	/**
	 * execute the select query and generate the result in form of list.
	 * @param criteria represents the query object.
	 * @return returns the list of entities filtered after applying criteria
	 * generated using query object.
	 */
	List<Entity> select(Object criteria);

	/**
	 * execute the select query using the respective query object and returns the 
	 * count of entities filtered after applying criteria.
	 * @param criteria represents the query object.
	 * @return returns the count of entities filtered after applying criteria.
	 */
	int count(Object criteria);
}
