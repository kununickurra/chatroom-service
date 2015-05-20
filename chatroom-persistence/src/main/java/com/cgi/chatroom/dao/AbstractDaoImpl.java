package com.cgi.chatroom.dao;

import com.cgi.chatroom.dao.spec.AbstractDao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Standard CRUD operation implemented with Hibernate
 */
@Component
public abstract class AbstractDaoImpl<T, PK extends Serializable> implements AbstractDao<T, PK> {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SessionFactory sessionFactory;

    private Class<T> entityClass;

    protected AbstractDaoImpl() {
        Type t = getClass().getGenericSuperclass();
        ParameterizedType pt = (ParameterizedType) t;
        this.entityClass = (Class) pt.getActualTypeArguments()[0];
    }

    @Override
    public T findById(PK id) {
        LOG.info("Running findById with ID {} ", id);
        return (T) getCurrentSession().get(entityClass, id);
    }

    @Override
    public void saveOrUpdate(T entity) {
        LOG.info("Running saveOrUpdate for entity {} ", entity);
        getCurrentSession().saveOrUpdate(entity);
    }

    @Override
    public void delete(T entity) {
        LOG.info("Running delete for entity {} ", entity);
        getCurrentSession().delete(entity);
    }

    /**
     * @return the current Hibernate session from the session factory
     */
    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

}