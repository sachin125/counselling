package com.inn.counselling.service.generic;

import java.util.List;

import org.apache.cxf.jaxrs.ext.search.SearchContext;
import org.json.JSONObject;

import com.inn.counselling.dao.generic.IGenericDao;
import com.inn.counselling.utils.AdvanceSearchResult;
import com.inn.counselling.utils.QueryObject;
/**
 * This class represents an abstract service which provides a higher level interface 
 * to not only process business objects, but to get access to them in the first place. 
 * A business object may be created from different databases (and different DAO's), it 
 * could be decorated with information made from an HTTP request. 
 * This may have certain business logic that converts several data objects into a single, 
 * robust, business object.
 * 
 * For now since we are providing the CRUD implementation this abstract implementation
 * delegates responsibility to DAO which further interact with persistence store to complete
 * the operation.

 * @param <Pk> represents entity primary key type.
 * @param <Entity> represents the JPA entity type to be processed.
 */
public abstract class AbstractService<Pk, Entity> implements IGenericService<Pk, Entity>{
	/**
	 * represents the data access object which provides implementation
	 * of data persistence and retrieval from the store. 
	 */
	private IGenericDao<Pk, Entity> dao;
	/**
	 * @return returns the resepective data access object instance.
	 */
	public IGenericDao<Pk, Entity> getDao() {
		return dao;
	}
	/**
	 * Concrete service class must override this to autowire the specific data access
	 * object instance with this service.
	 * @param dao
	 */
	public void setDao(IGenericDao<Pk, Entity> dao) {
		this.dao = dao;
	}
	/**
	 * @return returns the filtered entities after performing advance search.
	 * 
	 * @param queryObject represents the query.
	 */
	@Override
	public AdvanceSearchResult<Entity> advanceSearch(QueryObject queryObject) throws Exception{
		List<Entity> results = dao.find(queryObject);
		AdvanceSearchResult<Entity> asr = new AdvanceSearchResult<Entity>();
		asr.setLowerBound(queryObject.getPaginationLowerLimit());
		asr.setUpperBound(queryObject.getPaginationUpperLimit());
		asr.setResults(results);
		asr.setTotalRecords(dao.count(queryObject));
		return asr;
	}

	/**
	 * performs a search based on the values in the entity. All the values provided
	 * in the entity are compared using equal operator. This doesn't support regular
	 * expression based search.
	 * 
	 * @param entity represents the entity instance.
	 * @return returns the list of entities filtered after performing search.
	 */
	public List<Entity> search(Entity entity) throws Exception{
		String[] exclude = {};
		return dao.findByExample(entity, exclude);

	}
	/**
	 * searches the entity based on the primary key.
	 * @param primaryKey represents the primary key of the entity.
	 * @return returns the entity found in the persistence store.
	 */
	@Override
	public Entity findByPk(Pk entityPk) throws Exception{
		return dao.findByPk(entityPk);
	}
	/**
	 * @return returns all the entities stored in the persistence store.
	 */
	@Override
	public List<Entity> findAll() throws Exception{
		return dao.findAll();
	}
	/**
	 * creates the provided entity.
	 * @param anEntity entity to create.
	 */
	@Override
	public Entity create(Entity entity) throws Exception {
		return dao.create(entity);
	}
	/**
	 * updates the provided entity.
	 * @param anEntity entity to update.
	 */
	@Override
	public Entity update(Entity entity) throws Exception{
		return dao.update(entity);
	}
	/**
	 * removes the provided entity.
	 * @param anEntity entity to remove. 
	 * NOTE: PK must be populated within the provided entity.
	 */
	@Override
	public boolean delete(Entity entity) throws Exception{
		return dao.delete(entity);
	}
	/**
	 * removes the provided entity.
	 * @param primaryKey entity's primary key. 
	 * @return 
	 * 
	 */
	@Override
	public boolean deleteByPk(Pk entityPk) throws Exception{
		return dao.deleteByPk(entityPk);
	}

	public List<Entity> search(SearchContext ctx, Integer maxLimit,Integer minLimit, String orderby) throws Exception{
		return dao.search(ctx, maxLimit, minLimit, orderby);
	}

	@Override
	public List<JSONObject> findAudit(Pk entityPk) throws Exception{
		return dao.findAudit(entityPk);
	}

	@Override
	public Long count(QueryObject queryObject) throws Exception{
		return dao.count(queryObject);
	}

	@Override
	public boolean contains(Entity entity) throws Exception{
		return dao.contains(entity);
	}

	@Override
	public List<Entity> find(QueryObject queryObject) throws Exception{
		return dao.find(queryObject);
	}

	public long searchCount(SearchContext ctx) throws Exception{
		return dao.searchCount(ctx);
	}

	@Override
	public List<Entity> findByExample(Entity refEntity, String[] excludeProperty) throws Exception{
		return dao.findByExample(refEntity, excludeProperty);
	}
}
