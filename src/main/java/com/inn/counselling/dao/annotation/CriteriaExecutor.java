package com.inn.counselling.dao.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;

/**
 * This annotation represents a Criteria executor component.
 * By annotating any class with this, indicates that an annotated class is a "component". 
 * Such classes are considered as candidates for auto-detection when using annotation-based configuration 
 * and class path scanning. Other class-level annotations may be considered as identifying a component as well.
 * 
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
@Inherited
public @interface CriteriaExecutor {

}
