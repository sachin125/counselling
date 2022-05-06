package com.inn.counselling.rest.generic;

import java.util.List;

import org.apache.cxf.jaxrs.ext.search.SearchContext;

import com.inn.counselling.utils.AdvanceSearchResult;
import com.inn.counselling.utils.QueryObject;

/**
 * Generic rest interface.
 * 
 *
 * @param <Pk> represents the primary key of the entity.
 * @param <Entity> represents the entity type.
 */
public interface IGenericRest<Pk, Entity> {
	/**
	 * @return returns the filtered entities after performing advance search.
	 * 
	 * @param queryObject represents the query.
	 * @throws Exception 
	 */
	AdvanceSearchResult<Entity> advanceSearch(QueryObject queryObject) throws Exception;
	/**
	 * API which performs a search based on the values in the entity. All the
	 *@path  values provided in the entity are compared using equal operator. 
	 * @path This doesn't support regular expression based search.
	 * 
	 * @param entity represents the entity instance.
	 * 
	 * @return returns the list of entities filtered after performing search.
	 */
	List<Entity> search(Entity entity)throws Exception;
	/**
	 * searches the entity based on the primary key.
	 * 
	 * @param primaryKey represents the primary key of the entity.
	 * 
	 * @return returns the entity found in the persistence store.
	 */
	Entity findByPk(Long  pk) throws Exception;

	List<Entity> findAll()throws Exception;

	Entity create(Entity anEntity)throws Exception,Exception;

	Entity update(Entity anEntity)throws Exception;
	
	long count(QueryObject queryObject) throws Exception;
	
	boolean delete(Entity anEntity) throws Exception;
	
	boolean deleteByPk(Long pk) throws Exception;
	
	boolean contains(Entity anEntity) throws Exception;
	
	List<Entity> find(QueryObject queryObject) throws Exception;
	
	public List<Entity> search(Integer ulimit, Integer llimit, String orderby) throws Exception;

}
