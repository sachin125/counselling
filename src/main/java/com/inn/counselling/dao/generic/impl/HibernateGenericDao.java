package com.inn.counselling.dao.generic.impl;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Collections;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

import org.apache.cxf.jaxrs.ext.search.SearchCondition;
import org.apache.cxf.jaxrs.ext.search.SearchContext;
import org.apache.cxf.jaxrs.ext.search.jpa.JPATypedQueryVisitor;
import org.apache.cxf.jaxrs.utils.InjectionUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.internal.impl.AbstractAuditQuery;
import org.hibernate.proxy.HibernateProxyHelper;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.inn.counselling.dao.generic.IGenericDao;
import com.inn.counselling.dao.criteria.IQueryCriteriaBuilder;
import com.inn.counselling.dao.criteria.IQueryExecutor;
import com.inn.counselling.model.Users;
import com.inn.counselling.utils.QueryObject.SortOrder;
import com.inn.counselling.utils.ClassUtils;

/**
 * Represents the implementation of DAO using JPA Hibernate. Provides the basic 
 * generic implementation for all CRUD operation.
 *
 * @param <Pk> represents primary key class.
 * @param <Entity> represents the entity class.
 */
public class HibernateGenericDao<Pk, Entity> extends JPABaseDao<Pk, Entity>implements IGenericDao<Pk, Entity>{

	private static final Logger logger = LoggerFactory.getLogger(HibernateGenericDao.class);	

	public static final String IDENTIFIER_METHOD="getPrimaryKeyIdentifier";
	public static final String USERNAME_METHOD="getUsername";
	public static final String MODIFCATIONTIME_METHOD="getModifiedTime";
	public static final String MODIFIEDBY_METHOD  ="last_modifier";
	public static final String CHANGES = "changes";
	public static final String CHANGED_BY = "changed_by";
	public static final String CHANGED_ON = "changed_on";
	public static final String GET = "get";
	public static final String SET = "set";
	public static final String CREATED_TIME = "CreatedTime";
	public static final String MODIFIED_TIME = "ModifiedTime";
	public static final String CREATOR = "Creator";
	public static final String LAST_MODIFIER = "LastModifier";
	public static final String DOMAIN = "Domain";
	public static final String DESC ="desc";
	public static final String ADMIN = "admin";

	
	protected HibernateGenericDao(Class<Entity> type) {
		super(type);
	}
	
	@Override
	@Autowired(required = true)
	@Qualifier("hibernateCriteriaBuilder")
	public void setCriteriaBuilder(IQueryCriteriaBuilder<Entity> criteriaBuilder) {
		super.setCriteriaBuilder(criteriaBuilder);
	}

	@Override
	@Autowired(required = true)
	@Qualifier("hibernateQueryExecutor")
	public void setQueryExecutor(IQueryExecutor<Entity> queryExecutor) {
		super.setQueryExecutor(queryExecutor);
	}

	/**
	 * Returns the List of Entities that match the search criteria specified
	 * through the Example. Searches all Entities that match the properties set
	 * in the Example entity.
	 * 
	 * @param refEntity Example Element to search for.
	 * @return List of entities that match the search criteria specified through
	 * all properties set in the Example.
	 * @throws Exception 
	 */
	@Override
	public List<Entity> findByExample(Entity refEntity, String[] excludeProperty) throws Exception {
		logger.info("performing findByExample using entity {} excludedProperty{}",refEntity, excludeProperty);
		Session session = (Session) getEntityManager().getDelegate();
		Criteria criteria = session.createCriteria(getType());
		addSimpleFieldCriteria(refEntity, criteria);
		List<Entity> result = criteria.list();
		return result;
	}
	
	
	public List<Entity> findByExampleWithOrderByAndLimit(Entity refEntity, String[] excludeProperty,long llimit,long ulimit) throws Exception {
		logger.info("performing findByExample using entity {} excludedProperty{}",refEntity, excludeProperty);
		Session session = (Session) getEntityManager().getDelegate();
		Criteria criteria = session.createCriteria(getType());
		addSimpleFieldCriteria(refEntity, criteria);
		List<Entity> result = criteria.list();
		return result;
	}

	protected void addSimpleFieldCriteria(Object entityObject, Criteria criteria) throws Exception {
		if (entityObject == null) {
			return;
		}

		if (criteria == null) {
			// This is parent entity. Start criteria building
			Session session = (Session) getEntityManager().getDelegate();
			criteria = session.createCriteria(entityObject.getClass());
		}
		Field entityClassFields[] = entityObject.getClass().getDeclaredFields();
		try {
			Object simpleObject = entityObject.getClass().newInstance();
			if (entityClassFields != null) {
				for (Field embeddedField : entityClassFields) {
					embeddedField.setAccessible(true);
					if (embeddedField.get(entityObject) != null) {
						if (ClassUtils.isUserDefined(embeddedField.getType())){
							addEmbeddedFieldCriteria(embeddedField, entityObject,criteria);
						}else if (ClassUtils.isSimpleType(embeddedField.getType())) {
							logger.debug("field [" + embeddedField.getName()+ "] will be added to criteria of this class");
							// As field is of primitive type add to example
							embeddedField.set(simpleObject, embeddedField.get(entityObject));
						}
					} else {
						logger.debug("field ["+ embeddedField.getName()+ "] will be not be added to criteria as it's value is null");
					}
				}
				Example example = Example.create(simpleObject);
				criteria.add(example);
			}
		} catch (Exception e) {
			logger.error("Error Inside  @class :HibernateGenericDao @Method :addSimpleFieldCriteria()"+e.getMessage());
			throw e;
		}
	}

	protected void addEmbeddedFieldCriteria(Field field, Object entityObject,Criteria parentCriteria) throws IllegalArgumentException, IllegalAccessException, Exception {
		if (parentCriteria == null) {
			// This is parent entity. Start criteria building
			Session session = (Session) getEntityManager().getDelegate();
			parentCriteria = session.createCriteria(field.getDeclaringClass());
		}

		Criteria criteria = parentCriteria.createCriteria(field.getName());
		if (ClassUtils.isUserDefined(field.getType()) && field.get(entityObject) != null) {
			logger.debug("field [" + field.getName() + "] is candidate for criteria building");
			addSimpleFieldCriteria(field.get(entityObject), criteria);
		}
	}

	public List<Entity> search(SearchContext ctx, Integer maxLimit,Integer minLimit, String orderby) throws Exception {
		SearchCondition<Entity> searchCondition = ctx.getCondition(getType());
		if (searchCondition != null) {
			return getResultsForSearchCondition(searchCondition,maxLimit,minLimit,orderby);
		} else {
			if (orderby != null) {
					//return findAllWithPagination(minLimit, maxLimit,orderby,SortOrder.DESC);
					return findAllWithPagination(minLimit, maxLimit);
			} else {
				return findAllWithPagination(minLimit, maxLimit);
			}
		}
	}


	private CriteriaQuery<Entity> getCriteriaQuery(String orderBy,CriteriaBuilder criteriaBuilder,
			CriteriaQuery<Entity> criteriaQuery,Root<Entity> root) { 
		if(!orderBy.isEmpty()){
			List<Order> multipleOrderByList=new ArrayList<Order>();
			// Condition for apply order by on two attributes
			String[] commaseparateds=orderBy.split(",");
			for(String commaseparated:commaseparateds) {
				String[] slashseparateds = commaseparated.split("-");
				String orderType = "desc";
				if(slashseparateds.length>1) {
					orderType = slashseparateds[1];				
				}
				if(slashseparateds.length>0) {
					String firstPart = slashseparateds[0];
					String[] dotSeparateds = firstPart.split("\\.");
					for(String dotSeparated:dotSeparateds){
//						root.get(dotSeparated);
						if(orderType.equalsIgnoreCase("asc")) {
							multipleOrderByList.add(criteriaBuilder.asc(root));
						}else {
							multipleOrderByList.add(criteriaBuilder.desc(root));
						}
					}
					/*if(orderType.equalsIgnoreCase("asc")) {
						multipleOrderByList.add(criteriaBuilder.asc(root));
					}else {
						multipleOrderByList.add(criteriaBuilder.desc(root));
					}*/
				}
			}
			System.out.println("multipleOrderByList :: "+multipleOrderByList);
			criteriaQuery.orderBy(multipleOrderByList);
		}
		return criteriaQuery;
	}
	private List<Entity> getResultsForSearchCondition(SearchCondition<Entity> sc,Integer ulimit,Integer llimit,String orderby) {
		
		JPATypedQueryVisitor<Entity> visitor = new JPATypedQueryVisitor<Entity>(getEntityManager(), getType());
		sc.accept(visitor);
		visitor.visit(sc);
		TypedQuery<Entity> typedQuery = visitor.getTypedQuery();
/*		
 		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
 		CriteriaQuery<Entity> criteriaQuery = visitor.getCriteriaQuery();
		Root<Entity> root = criteriaQuery.from(getType());
		criteriaQuery = criteriaQuery.select(root);
		criteriaQuery = this.getCriteriaQuery(orderby, criteriaBuilder,criteriaQuery,root);
*/		
		llimit = llimit==null?0:llimit;
		ulimit = ulimit==null?0:ulimit;
		int maxResults=0;
		if(ulimit >= 0) {
			maxResults = ulimit - llimit + 1;
		}
		typedQuery.setMaxResults(maxResults);
		if (llimit >= 0) {
			typedQuery.setFirstResult(llimit.intValue());
		}
/*		List<Entity> searchList = getEntityManager().createQuery(criteriaQuery)
				.setFirstResult(llimit).setMaxResults(maxResults).getResultList();
*/		
		List<Entity> searchList = typedQuery.getResultList();
		return searchList;
	} 

	public long searchCount(SearchContext ctx) throws Exception {
		logger.info("HibernateGenericDao-search method start ");
		long recordCounts = 0;
		SearchCondition<Entity> sc = ctx.getCondition(getType());
		if (sc != null) {
			recordCounts = getResultsCountForSearchCondition(sc);
		}else {
			return super.findAllRecordCount();		
		}
		return recordCounts;
	}

	private long getResultsCountForSearchCondition(SearchCondition<Entity> sc) {
		logger.info("HibernateGenericDao-getResultsCountForSearchCondition start");
		JPATypedQueryVisitor<Entity> visitor = new JPATypedQueryVisitor<Entity>(
				getEntityManager(), getType());
		visitor.visit(sc);
		TypedQuery<Entity> typedQuery = null;
		typedQuery =visitor.getTypedQuery();
		logger.info("HibernateGenericDao-getResultsCountForSearchCondition end");
		return typedQuery.getResultList().size();

	}

	public List<JSONObject> findAudit(Pk pk) throws Exception{
		logger.info("Geting History for entity " + getClass() + "   ");
		try {
			AuditReader reader = AuditReaderFactory.get(getEntityManager());
			List<Number> revisions = reader.getRevisions(getType(), pk);
			Class objClass = getType();
			Method[] methods = objClass.getMethods();
			List<String> methodList = new ArrayList<String>();
			Method modifyByGetMethod = null;
			Method modifiedTimeMethod = null;

			for (Method method : methods) {
				methodList.add(method.getName());
				if (method.getName().replaceFirst(GET, "").equalsIgnoreCase(LAST_MODIFIER)) {
					modifyByGetMethod = method;
				}
				if (method.getName().replaceFirst(GET, "").equalsIgnoreCase(MODIFIED_TIME)) {
					modifiedTimeMethod = method;
				}
			}
			List<JSONObject> historyList = new ArrayList<JSONObject>();
			String changes = null;
			String changedBy = null;
			String changedTime = null;
			changes = "Created with ";
			AbstractAuditQuery queryNew1 = null;

			queryNew1 = (AbstractAuditQuery) reader.createQuery().forEntitiesAtRevision(getType(), revisions.get(0)).add(AuditEntity.id().eq(pk));
			Object objectNew1 = queryNew1.getSingleResult();

			for (Method method : methods) {
				String name = method.getName();
				if (name.startsWith(GET) && !name.endsWith(CREATED_TIME) && !name.endsWith(MODIFIED_TIME) 
						&& !name.endsWith(CREATOR)&& !name.contains(LAST_MODIFIER) && !name.contains(DOMAIN)
						&& methodList.contains(name.replaceFirst(GET, SET))) {

					if (method.invoke(objectNew1) != null) {
						if (!method.invoke(objectNew1).getClass().getCanonicalName().contains("com.inn")
								|| method.invoke(objectNew1).getClass().isEnum()) {

							changes += "<br/>" + name.replaceFirst(GET, "")+ " : "+ method.invoke(objectNew1).toString()+" ";

							if (modifiedTimeMethod.invoke(objectNew1) != null) {
								changedTime = ""+ modifiedTimeMethod.invoke(objectNew1);
							} else {
								changedTime = new Date().toString();
							}
							Method getUsernameMethod = Users.class.getDeclaredMethod(USERNAME_METHOD);
							if (modifyByGetMethod.invoke(objectNew1) != null) {
								changedBy = ""+ getUsernameMethod.invoke(modifyByGetMethod.invoke(objectNew1));
							} else {
								changedBy = ADMIN;
							}
						} else {

							Class fkClass = HibernateProxyHelper.getClassWithoutInitializingProxy(method.invoke(objectNew1));
							Method getIdentifier = fkClass.getDeclaredMethod(IDENTIFIER_METHOD);
							changes += "<br/>"+ name.replaceFirst(GET, "")+ " : "+ (String) getIdentifier.invoke(method.invoke(objectNew1))+" ";
							Method getUsernameMethod = Users.class.getDeclaredMethod(USERNAME_METHOD);
							if (modifyByGetMethod.invoke(objectNew1) != null) {
								changedBy = ""+ getUsernameMethod.invoke(modifyByGetMethod.invoke(objectNew1));
							} else {
								changedBy = ADMIN;
							}
							if (modifiedTimeMethod.invoke(objectNew1) != null) {
								changedTime = ""+ modifiedTimeMethod.invoke(objectNew1);
							} else {
								changedTime = new Date().toString();
							}
						}
					}
				}
			}
			JSONObject change = new JSONObject();
			try {
				change.put(CHANGES, changes);
				change.put(CHANGED_BY, changedBy);
				change.put(CHANGED_ON, changedTime);
				changes = null;
				changedBy = null;
				changedTime = null;
			} catch (JSONException e) {
				logger.error("Unable to create Json object for history  "+ e.getMessage());
			}
			for (int i = revisions.size() - 1; i > 0; i--) {
				AbstractAuditQuery queryNew = null;
				AbstractAuditQuery queryOld = null;
				try {
					queryNew = (AbstractAuditQuery) reader.createQuery().forEntitiesAtRevision(getType(), revisions.get(i)).add(AuditEntity.id().eq(pk));
				} catch (Exception e) {
					logger.error("Error Inside  @class :HibernateGenericDao @Method :findAudit()"+e.getMessage());
				}
				try {
					queryOld = (AbstractAuditQuery) reader.createQuery().forEntitiesAtRevision(getType(),revisions.get(i - 1)).add(AuditEntity.id().eq(pk));
				} catch (Exception e) {
					logger.error("Error Inside  @class :HibernateGenericDao @Method :findAudit()"+e.getMessage());
				}
				Object objectOld = null;
				if (queryOld != null) {
					objectOld = queryOld.getSingleResult();
				}
				Object objectNew = queryNew.getSingleResult();

				long k = 0;
				if (objectOld != null) {
					for (Method method : methods) {
						String name = method.getName();
						if (name.startsWith(GET)
								&& !name.endsWith(CREATED_TIME)
								&& !name.endsWith(CREATOR)
								&& !name.contains(LAST_MODIFIER)
								&& !name.endsWith(MODIFIED_TIME)
								&& !name.contains(DOMAIN)
								&& methodList.contains(name.replaceFirst(GET,
										SET))) {

							try {

								if (method.invoke(objectNew) != null
										&& method.invoke(objectOld) != null) {
									if (!method.invoke(objectNew).equals(
											method.invoke(objectOld))) {
										if (!method.invoke(objectNew).getClass().getCanonicalName().contains("com.inn")
												|| method.invoke(objectNew).getClass().isEnum()) {
											if (k == 0) {
												changes = "changed  "+ name.replaceFirst(GET, "")+ "  from  "+ method.invoke(objectOld).toString()
														+ "   to   "+ method.invoke(objectNew).toString();
											} else {
												changes += " <br/>changed  "+ name.replaceFirst(GET, "")+ "  from  "+ method.invoke(objectOld).toString()+ "   to   "+ method.invoke(objectNew).toString();
											}

											changedTime = ""+ modifiedTimeMethod.invoke(objectNew);
											Method getUsernameMethod = Users.class.getDeclaredMethod(USERNAME_METHOD);
											changedBy = ""+ getUsernameMethod.invoke(modifyByGetMethod.invoke(objectNew));
											k = 1;
										} else {

											Class fkClass = HibernateProxyHelper.getClassWithoutInitializingProxy(method.invoke(objectOld));
											Method getIdentifier = fkClass.getDeclaredMethod(IDENTIFIER_METHOD);

											String changeOLD = null;

											changeOLD = (String) getIdentifier.invoke(method.invoke(objectOld));

											String changeNEW = null;

											changeNEW = (String) getIdentifier.invoke(method.invoke(objectNew));

											if (!changeOLD.equals(changeNEW)) {
												if (k == 0) {
													changes = "changed "+ name.replaceFirst(GET, "")+ " From   "+ changeOLD+ " to  "+ changeNEW;
												} else {
													changes += "<br/> changed "+ name.replaceFirst(GET, "")+ " From  "+ changeOLD+ " to  "+ changeNEW;
												}
												Method getUsernameMethod = Users.class.getDeclaredMethod(USERNAME_METHOD);
												changedBy = ""+ getUsernameMethod.invoke(modifyByGetMethod.invoke(objectNew));

												changedTime = ""+ modifiedTimeMethod.invoke(objectNew);
												k = 1;
											}
										}
									}
								} else if (method.invoke(objectNew) != null
										&& method.invoke(objectOld) == null) {

									if (!method.invoke(objectNew).getClass().getCanonicalName().contains("com.inn")|| method.invoke(objectNew).getClass().isEnum()) {
										if (k == 0) {
											changes = "changed  "+ name.replaceFirst(GET, "")+ "  from  Null   to   "+ method.invoke(objectNew).toString();
										} else {
											changes += " <br/>changed  "+ name.replaceFirst(GET, "")+ "  from  Null   to   "+ method.invoke(objectNew).toString();
										}

										changedTime = ""+ modifiedTimeMethod.invoke(objectNew);

										Method getUsernameMethod = Users.class.getDeclaredMethod(USERNAME_METHOD);
										if (modifyByGetMethod.invoke(objectNew) != null) {
											changedBy = ""+ getUsernameMethod.invoke(modifyByGetMethod.invoke(objectNew));
										}
										k = 1;
									} else {

										Class fkClass = HibernateProxyHelper.getClassWithoutInitializingProxy(method.invoke(objectNew));
										Method getIdentifier = fkClass.getDeclaredMethod(IDENTIFIER_METHOD);
										String changeNEW = (String) getIdentifier.invoke(method.invoke(objectNew));
										Method getUsernameMethod = Users.class.getDeclaredMethod(USERNAME_METHOD);
										changedBy = ""+ getUsernameMethod.invoke(modifyByGetMethod.invoke(objectNew));

										changedTime = ""+ modifiedTimeMethod.invoke(objectNew);

										if (k == 0) {
											changes = "changed  "+ name.replaceFirst(GET, "")+ " From    Null  to  "+ changeNEW;
										} else {
											changes += "<br/>changed  "+ name.replaceFirst(GET, "")+ " From  Null to  "+ changeNEW;
										}

										k = 1;
									}

								} else if (method.invoke(objectNew) == null
										&& method.invoke(objectOld) != null) {

									if (!method.invoke(objectOld).getClass().getCanonicalName().contains("com.inn")|| method.invoke(objectOld).getClass().isEnum()) {
										if (k == 0) {
											changes = "changed  "+ name.replaceFirst(GET, "")+ "  from  "+ method.invoke(objectOld).toString()+ "  to  Null";
										} else {
											changes += " <br/> "+ name.replaceFirst(GET, "")+ "  from  "+ method.invoke(objectOld).toString()+ "  to  Null";
										}

										changedTime = ""+ modifiedTimeMethod.invoke(objectOld);

										Method getUsernameMethod = Users.class.getDeclaredMethod(USERNAME_METHOD);
										if(modifyByGetMethod.invoke(objectOld)!=null){
											changedBy = ""+ getUsernameMethod.invoke(modifyByGetMethod.invoke(objectOld));
										}
										else{
											changedBy = "";
										}
										k = 1;

									} else {

										Class fkClass = HibernateProxyHelper.getClassWithoutInitializingProxy(method.invoke(objectOld));
										Method getIdentifier = fkClass.getDeclaredMethod(IDENTIFIER_METHOD);
										String changeOLD = null;
										changeOLD = (String) getIdentifier.invoke(method.invoke(objectOld));
										Method getUsernameMethod = Users.class.getDeclaredMethod(USERNAME_METHOD);
										changedBy = ""+ getUsernameMethod.invoke(modifyByGetMethod.invoke(objectOld));

										changedTime = ""+ modifiedTimeMethod.invoke(objectOld);
										if (k == 0) {
											changes = "changed  "+ name.replaceFirst(GET, "")+ " From  " + changeOLD+ " to Null";
										} else {
											changes += "<br/> changed  "+ name.replaceFirst(GET, "")+ " From " + changeOLD+ " to Null";
										}
										k = 1;
									}

								}

							} catch (IllegalAccessException e) {
								logger.error("Illegal exception Found in hibernate generic Dao Method For History"+ e.getMessage());
							} catch (InvocationTargetException e) {
								logger.error("InvocationTargetException Found in hibernate generic Dao Method For History"+ e.getMessage());
							} catch (SecurityException e) {
								logger.error("Security   Exception  in hiberante generic dao"+ e.getMessage());
							} catch (NoSuchMethodException e) {
								logger.error("Error Inside  @class :HibernateGenericDao @Method :findAudit()"+e.getMessage());
							}
						}
					}

				}
				if (changes != null && changedTime != null && changedBy != null) {
					JSONObject change1 = new JSONObject();
					try {
						change1.put(CHANGES, changes);
						change1.put(CHANGED_BY, changedBy);
						change1.put(CHANGED_ON, changedTime);
						historyList.add(change1);
						changes = null;
						changedBy = null;
						changedTime = null;
					} catch (JSONException e) {
						logger.error("Unable to create Json object for history  "+ e.getMessage());
					}
				}
			}
			historyList.add(change);
			return historyList;

		} catch (Exception e) {
			logger.error("Error Inside  @class :HibernateGenericDao @Method :findAudit()"+e.getMessage());
		}
		return null;
	}


}
