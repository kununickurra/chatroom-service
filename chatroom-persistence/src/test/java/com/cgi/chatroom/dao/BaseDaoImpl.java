package com.cgi.chatroom.dao;

import com.cgi.chatroom.dao.spec.AbstractDao;

/**
 * Concrete class extending the AbstractDaoImpl used to test the basic operations using mocking framework
 * AbstractDaoImpl is abstract and cannot be instantiated.
 * <p/>
 * Class use a String type for both Key and Value.
 */
public class BaseDaoImpl extends AbstractDaoImpl<String, String> implements AbstractDao<String, String> {
}
