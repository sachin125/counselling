package com.inn.counselling.utils;

import java.lang.reflect.Field;

import org.hibernate.Criteria;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.internal.SessionImpl;
import org.hibernate.loader.OuterJoinLoader;
import org.hibernate.loader.criteria.CriteriaLoader;
import org.hibernate.persister.entity.OuterJoinLoadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class CriteriaUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(CriteriaUtils.class);
	
    public static void logSQLFromCriteria(final Criteria criteria) {
    	LOGGER.debug("print logger for query:::::::::::::::::::::::: ");
        final CriteriaImpl c = (CriteriaImpl) criteria;
        final SessionImpl s = (SessionImpl) c.getSession();
        final SessionFactoryImplementor factory = s.getSessionFactory();
        final String[] implementors = factory.getImplementors(c.getEntityOrClassName());
        final CriteriaLoader loader = new CriteriaLoader((OuterJoinLoadable)
                factory.getEntityPersister(implementors[0]), factory, c, implementors[0],
                s.getLoadQueryInfluencers());
        try {
            final Field f = OuterJoinLoader.class.getDeclaredField("sql");
            f.setAccessible(true);
            LOGGER.debug((String) f.get(loader));
        } catch (final NoSuchFieldException | IllegalAccessException e) {
        	LOGGER.error(e.getMessage(), e);
        }
    }

}