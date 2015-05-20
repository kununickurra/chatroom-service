package com.cgi.chatroom.dao.spec;

import java.io.Serializable;

/**
 * Abstract superclass for Data Access object with the main CRUD operations defined.
 * <p>
 * <code>T</code>: Entity
 * </p>
 * <p>
 * <code>PK</code>: Primary key
 * </p>
 */
public interface AbstractDao<T, PK extends Serializable> {

    /**
     * Find entity by its unique identifier.
     *
     * @param id unique identifier
     * @return the entity retrieved using it's id or <code>null</code>
     * in case no entity could be found with the given id
     */
    T findById(PK id);

    /**
     * Save or update an entity.
     *
     * @param entity the entity to save or update
     */
    void saveOrUpdate(T entity);

    /**
     * Delete an entity.
     *
     * @param entity the entity to be deleted
     */
    void delete(T entity);

}
