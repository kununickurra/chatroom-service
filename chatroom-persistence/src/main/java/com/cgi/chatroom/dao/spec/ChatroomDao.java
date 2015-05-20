package com.cgi.chatroom.dao.spec;

import com.cgi.chatroom.model.Message;

import java.util.Date;
import java.util.List;

/**
 * Chatroom DAO specification
 */
public interface ChatroomDao extends AbstractDao<Message, Long> {
    /**
     * Get all Messages published <b>after</b> the date received in parameter
     *
     * @param date
     * @return List of Messages <b> ordered by creation date Ascending </b> or and Empty list in case no message
     * have been published after the given date.
     */
    public List<Message> findAllMessagesCreatedAfter(Date date);
}
