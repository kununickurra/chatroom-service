package com.cgi.chatroom.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Test the default CRUD implementation included in the Abstract Dao.
 * Hibernate sessionFactory can be mocked using Mockito to save us the hassle running integration tests.
 */
@RunWith(MockitoJUnitRunner.class)
public class AbstractDaoTest {

    private static final String ENTITY = "Entity";
    private static final String REQUESTED_KEY = "Key";
    private static final String RETURNED_STRING = "returned-string";

    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private Session session;

    // Empty Mock dao implementation used as AbstractDao cannot be instantiated.
    @InjectMocks
    private BaseDaoImpl abstractDao = new BaseDaoImpl();

    @Test
    public void shouldFindByIdSuccessfully() {
        // Given
        initMocks();
        given(session.get(String.class, REQUESTED_KEY)).willReturn(RETURNED_STRING);
        // When
        String returnedEntity = abstractDao.findById(REQUESTED_KEY);
        // Then
        assertThat(returnedEntity, is(RETURNED_STRING));
    }

    @Test
    public void shouldSaveOrUpdateSuccessfully() {
        // Given
        initMocks();
        // When
        abstractDao.saveOrUpdate(ENTITY);
        // Then
        verify(session).saveOrUpdate(ENTITY);
    }

    @Test
    public void shouldDeleteSuccessfully() {
        // Given
        initMocks();
        // When
        abstractDao.delete(ENTITY);
        // Then
        verify(session).delete(ENTITY);
    }

    private void initMocks() {
        given(sessionFactory.getCurrentSession()).willReturn(session);
    }

}
