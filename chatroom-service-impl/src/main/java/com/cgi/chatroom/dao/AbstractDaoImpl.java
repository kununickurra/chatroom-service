package com.cgi.chatroom.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Standard CRUD operation implemented with Hibernate
 */
@Component
public abstract class AbstractDaoImpl<T, PK extends Serializable> implements AbstractDao<T,PK> {

    @Autowired
    private SessionFactory sessionFactory;

    private Class<T> entityClass;

    protected AbstractDaoImpl() {
        Type t = getClass().getGenericSuperclass();
        ParameterizedType pt = (ParameterizedType) t;
        this.entityClass = (Class) pt.getActualTypeArguments()[0];
    }

    public Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public T findById(PK id) {
        return (T) getCurrentSession().get(entityClass, id);
    }

    @Override
    public void saveOrUpdate(T e) {
        getCurrentSession().saveOrUpdate(e);
    }

    @Override
    public void delete(T e) {
        getCurrentSession().delete(e);
    }

}