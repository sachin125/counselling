package com.inn.counselling.service.generic;

import java.util.List;

import org.apache.cxf.jaxrs.ext.search.SearchContext;
import org.json.JSONObject;
import org.springframework.transaction.annotation.Transactional;

import com.inn.counselling.utils.AdvanceSearchResult;
import com.inn.counselling.utils.QueryObject;

/**
 * @param <Pk> represents the primary key of the entity.
 * @param <Entity> represents the entity type.
 */
@Transactional
public interface IGenericService<Pk, Entity> {
	/**
	 * @return returns the filtered entities after performing advance search.
	 * 
	 * @param queryObject represents the query.
	 */
	AdvanceSearchResult<Entity> advanceSearch(QueryObject queryObject) throws Exception;
	/**
	 * API which performs a search based on the values in the entity. All the
	 * values provided in the entity are compared using equal operator. 
	 * This doesn't support regular expression based search.
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
	Entity findByPk(Pk entityPk) throws Exception;
	/**
	 * @return returns all the entities stored in the persistence store.
	 */
	Long count(QueryObject queryObject) throws Exception;
	List<Entity> findAll()throws Exception;
	/**
	 * creates the provided entity.
	 * 
	 * @param anEntity entity to create.
	 */
	Entity create(Entity entity) throws Exception;
	/**
	 * updates the provided entity.
	 * 
	 * @param anEntity entity to update.
	 */
	Entity update(Entity entity) throws Exception;
	/**
	 * removes the entity, note: primary key must be set within the
	 * entity.
	 * 
	 * @param anEntity
	 * @return 
	 */
	boolean delete(Entity entity) throws Exception;
	/**
	 * removes the entity identified by the provided primary key.
	 * @param primaryKey
	 */
	boolean deleteByPk(Pk entityPk) throws Exception;
		/**
	 * search on entity History 
	 * @param PrimaryKey
	 */
	List<JSONObject> findAudit(Pk pk)throws Exception;
	
	
	boolean contains(Entity entity) throws Exception;

	List<Entity> find(QueryObject queryObject) throws Exception;
	
	List<Entity> findByExample(Entity refEntity, String[] excludeProperty) throws Exception;
		
	List<Entity> search(SearchContext context, Integer upperLimit, Integer lowerLimit, String orderBy) throws Exception;

	public long searchCount(SearchContext ctx)  throws Exception;
	
	
}
