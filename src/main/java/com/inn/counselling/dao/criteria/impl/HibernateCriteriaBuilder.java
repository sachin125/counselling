package com.inn.counselling.dao.criteria.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inn.counselling.dao.annotation.CriteriaBuilder;
import com.inn.counselling.dao.criteria.IQueryCriteriaBuilder;
import com.inn.counselling.exception.SystemConfigurationException;
import com.inn.counselling.utils.QueryObject;
import com.inn.counselling.utils.QueryObject.MinMax;
import com.inn.counselling.utils.QueryObject.SearchMode;
import com.inn.counselling.utils.QueryObject.SortOrder;
import com.inn.counselling.utils.SearchFilterWrapper;

@CriteriaBuilder
public class HibernateCriteriaBuilder<Entity> implements IQueryCriteriaBuilder<Entity>{

	private static final Logger logger = LoggerFactory.getLogger(HibernateCriteriaBuilder.class);

	@PersistenceContext(name = "DEFAULT" )
	private EntityManager entityManager;
	
	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	/**
	 * converts the query object into Hibernate criteria. 
	 * @param queryObject represents the search parameters.
	 * @param entityClass represents the underlying base entity.
	 */
	@Override
	public Criteria buildCriteria(QueryObject queryObject, Class<Entity> entityClass) {
		logger.debug("Entry inside @class HibernateCriteriaBuilder @method buildCriteria @param queryObject "+queryObject+" entity: "+entityClass);
		Session session = (Session) getEntityManager().getDelegate();
		if(session == null){
			logger.error("Unable to get the session from entity manager.");
			throw new SystemConfigurationException("Unable to create session from entity manager.");
		}
		Criteria criteria = session.createCriteria(entityClass);
		applyFilters(criteria, queryObject);
		applyOrdering(criteria, queryObject);
		applyPagination(criteria, queryObject);
		return criteria;
	}
	/**
	 * applies all the filtering restrictions on the criteria object passed.
	 * @param criteria represents the Hibernate query criteria
	 * @param queryObject represents the encapsulated version of advance search.
	 */
	private void applyFilters(Criteria criteria, QueryObject queryObject) {
		logger.debug("Entry inside @class HibernateCriteriaBuilder @method applyFilters @param");
		Map<String, SearchMode> searchModes = queryObject.getFieldNameModeMapping();
		Map<String, Object> fieldValues = queryObject.getFieldNameValueMapping();
		List<SearchFilterWrapper> wrapperObj = queryObject.getSearchFilterWrapperList();
		logger.debug("inside @class HibernateCriteriaBuilder @method applyFilters Applying filters using search modes "+searchModes+" fieldValues: "+fieldValues);
		
		if(fieldValues.size() <= 0){
			return;
		}

		Iterator<SearchFilterWrapper> iterator = wrapperObj.iterator();
		while(iterator.hasNext()){
			SearchFilterWrapper filterObj = iterator.next();
			String fieldKey = filterObj.getFieldName();
			SearchMode mode = filterObj.getMode();
			Object value = filterObj.getValue();
		
			switch (mode) {
			case EQUAL:
				criteria.add(Restrictions.eq(fieldKey, value));
				break;
			case NOT_EQUAL:
				criteria.add(Restrictions.ne(fieldKey, value));
				break;
			case LIKE:
				criteria.add(Restrictions.like(fieldKey, value));
				break;
			case IS_NULL:
				criteria.add(Restrictions.isNull(fieldKey));
				break;
			case IN:
				criteria.add(Restrictions.in(fieldKey, (List)value));
				break;
			case BETWEEN:
				criteria.add(Restrictions.between(fieldKey, ((MinMax)value).getMin(),((MinMax)value).getMax()));
				break;
			case GREATER_THAN:
				criteria.add(Restrictions.gt(fieldKey, value));
				break;
			case LESS_THAN:
				criteria.add(Restrictions.lt(fieldKey, value));
				break;
			case GREATER_OR_EQUAL:
				criteria.add(Restrictions.ge(fieldKey, value));
				break;
			case LESS_OR_EQUAL:
				criteria.add(Restrictions.le(fieldKey, value));
				break;
			default:
				logger.warn("Unable to restriction in filter for mode[{}], since this is not a supported.", mode);
				break;
			}
		}

	}
	/**
	 * apply order by clause to the provided criteria.
	 * @param criteria represents the Hibernate query criteria
	 * @param queryObject represents the encapsulated version of advance search.
	 */
	private void applyOrdering(Criteria criteria, QueryObject queryObject) {
		Map<String, SortOrder> orderByMode = queryObject.getOrderByMode();
		logger.debug("Entry inside @class HibernateCriteriaBuilder @method applyOrdering @param orderByMode "+orderByMode);		
		
		if(orderByMode == null || orderByMode.size() <= 0){
			return;
		}
		else{
			for(Entry<String, SortOrder> entry : orderByMode.entrySet()){
				String key = entry.getKey();
				SortOrder order = entry.getValue();
				if(SortOrder.ASC.equals(order)){
					criteria.addOrder(Order.asc(key));
				}
				else{
					criteria.addOrder(Order.desc(key));
				}
			}
		}
	}
	/**
	 * apply paging conditions to the provided criteria object.
	 * @param criteria represents the Hibernate query criteria
	 * @param queryObject represents the encapsulated version of advance search.
	 */
	private void applyPagination(Criteria criteria, QueryObject queryObject) {
		long maxLimit = queryObject.getPaginationUpperLimit();
		long minLimit = queryObject.getPaginationLowerLimit();
		logger.debug("Entry inside @class HibernateCriteriaBuilder @method applyPagination @param maxLimit "+maxLimit+" minLimit: "+minLimit);		
		if(maxLimit >= 0){
		   criteria.setMaxResults((int)(maxLimit-minLimit+1));
		}

		if(minLimit >= 0){
			criteria.setFirstResult((int)minLimit);
		}
	}
	
}
