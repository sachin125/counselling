package com.inn.counselling.dao.generic.impl;

import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inn.counselling.dao.criteria.impl.JPACriteriaBuilder;
import com.inn.counselling.dao.criteria.impl.JPAQueryExecutor;
import com.inn.counselling.dao.generic.AbstractBaseDao;
import com.inn.counselling.dao.generic.IGenericDao;
import com.inn.counselling.utils.QueryObject;

/**
 * Abstract reusable JPA partial implementation of {@link IGenericDao}
 * that provides automatic handling of {@link EntityManager} within a 
 * container
 */
public class JPABaseDao<Pk, Entity> extends AbstractBaseDao<Pk, Entity>{

	private static final Logger logger = LoggerFactory.getLogger(JPABaseDao.class);
	
	protected JPABaseDao(Class<Entity> type) {
		super(type);
		this.setCriteriaBuilder(new JPACriteriaBuilder<Entity>());
		this.setQueryExecutor(new JPAQueryExecutor<Entity>());
	}

	@PersistenceContext(name = "DEFAULT", type = PersistenceContextType.TRANSACTION)
	private EntityManager entityManager;
	
	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		logger.trace("JPABaseDao: setEntityManager: entityManager :"+entityManager);
		this.entityManager = entityManager;
	}
	
	public EntityManager getEntityManager() {
		return this.entityManager;
	}
	
	@Override
	public Entity create(Entity entity) throws Exception{
		if(entity==null){
			throw new NullPointerException("Can't Create null Entity "+getType().getName());
		}
		return entityManager.merge(entity);
	}

	@Override
	public Entity update(Entity entity)throws Exception{
		if(entity==null){
			throw new NullPointerException("Can't Update null Entity "+getType().getName());
		}
		return entityManager.merge(entity);
	}

	@Override
	public boolean delete(Entity entity)throws Exception{
		if(entity==null){
			throw new NullPointerException("Can't delete null Entity "+getType().getName());
		}
		entityManager.remove(entityManager.contains(entity)? entity: entityManager.merge(entity));
		return true;
	}

	@Override
	public boolean deleteByPk(Pk entityPk)throws Exception{
		if(entityPk==null){
			throw new NullPointerException("Can't delete Entity "+getType().getName()+"since specified primary key is null");
		}
		Entity anEntity = this.findByPk(entityPk);
		return this.delete(anEntity);
	}

	@Override
	public Entity findByPk(Pk entityPk)throws Exception{
		if(entityPk==null){
			logger.warn("Cannot find entity["+getType().getName()+"] with null primary key");
			return null;
		}
		org.hibernate.Session session = (Session) getEntityManager().getDelegate();
		
		logger.trace("inside @jpaBaseDao findByPk session: "+session);
		Entity entity = getEntityManager().find(getType(), entityPk);
		logger.trace("inside @jpaBaseDao findByPk session: "+session);
		logger.trace("inside @jpaBaseDao findByPk entity: "+entity);
		return entity;
	}

	@Override
	public boolean contains(Entity entity)throws Exception{
		if(entity == null){
			logger.warn("Illegal argument null for entity["+getType().getName()+"] contains method ");
			return false;
		}
		return entityManager.contains(entity);
	}

	@Override
	public List<Entity> findAll()throws Exception{
		return this.find(new QueryObject());
	}

	public List<Entity> findAllWithPagination(long lowerLimit,long upperLimit) throws Exception{
		QueryObject queryObject=new QueryObject();
		queryObject.setPaginationLowerLimit(lowerLimit);
		queryObject.setPaginationUpperLimit(upperLimit);
		return this.find(queryObject);
	}
	public List<Entity> findAllWithPagination(long lowerLimit,long upperLimit,String order,QueryObject.SortOrder sort) throws Exception{
		HashMap<String, QueryObject.SortOrder> orderMap=new HashMap<String, QueryObject.SortOrder>();
		orderMap.put(order,sort);
		QueryObject queryObject=new QueryObject();
		queryObject.setPaginationLowerLimit(lowerLimit);
		queryObject.setPaginationUpperLimit(upperLimit);
		queryObject.setOrderByMode(orderMap);
		return this.find(queryObject);
	}
	
	public long findAllRecordCount()throws Exception{
		return this.find(new QueryObject()).size();
	}
}
