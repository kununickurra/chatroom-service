package com.cgi.chatroom.dao;

import java.io.Serializable;

/**
 * Abstract superclass for DAOs
 * <p>
 * T: Entity, PK: Primary key
 * </p>
 */
public interface AbstractDao<T, PK extends Serializable> {

    T findById(PK id);

    void saveOrUpdate(T e);

    void delete(T e);

}
