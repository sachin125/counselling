package com.inn.counselling.dao.generic;

import java.util.List;

import org.apache.cxf.jaxrs.ext.search.SearchContext;
import org.json.JSONObject;

/**
 * <b>Data Access Object (DAO) Pattern</b>: Abstracts from any direct type of 
 * database or persistence mechanism. Provides specific operations without 
 * exposing details of the database.
 * <br/><br/>
 * <b>Responsibility</b> : Complete Generic Data Access Object definition 
 * including: 
 * <br/>
 * <br/>
 * <ul>
 * <li>Creating a new record in the underlying persistent storage</li> 
 * <li>Reading existing records from the underlying persistence storage</li> 
 * <li>Update an existing record in the underlying persistent storage</li> 
 * <li>Delete an existing record in the underlying persistent storage</li> 
 * <li>Delete all records from the underlying persistent storage</li> 
 * </ul>
 * 
 * @param <Pk> Primary key type
 * @param <Entity> Entity type
 * 
 */
public interface IGenericDao<Pk, Entity> extends IBaseDao<Pk, Entity>{

	/**
	 * Returns the List of Entities that match the search criteria specified 
	 * through the Example. Searches all Entities that match the properties set 
	 * in the Example entity.
	 * 
	 * @param refEntity Example Element to search for.
	 * @return List of entities that match the search criteria specified 
	 *         through all properties set in the Example.
	 * @throws Exception 
	 */
	List<Entity> findByExample(Entity refEntity, String[] excludeProperty) throws Exception;

	/**
	 * search on entity History 
	 * @param PrimaryKey
	 * @throws Exception 
	 */
	List<JSONObject> findAudit(Pk pk) throws Exception;

	List<Entity> search(SearchContext ctx, Integer maxLimit, Integer minLimit, String orderby) throws Exception;

	long searchCount(SearchContext ctx)  throws Exception;

}

