package com.cgi.chatroom.dao;

import com.cgi.chatroom.dao.spec.ChatroomDao;
import com.cgi.chatroom.model.Message;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * DBUnit test class for the ChatroomDao
 * Test only the operation defined by ChatroomDao but not method inherited from superclass.
 */
public class ChatroomDaoTest extends AbstractDbUnitTest {

    @Autowired
    private ChatroomDao chatroomDao;

    @Test
    public void shouldFindAllMessagesCreatedAfterAGivenDateOrderedByCreationDateAscending() throws Exception {
        // Given
        // When
        List<Message> messages = chatroomDao.findAllMessagesCreatedAfter(getSendDate());
        // Then
        assertThat(messages.size(), is(2));
        assertThat(messages.get(0).getId(), is(3l));
        assertThat(messages.get(1).getId(), is(1l));
    }

    public Date getSendDate() {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse("2015-04-30 04:00:00.045");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}