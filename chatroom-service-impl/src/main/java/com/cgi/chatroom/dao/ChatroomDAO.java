package com.cgi.chatroom.dao;

import com.cgi.chatroom.domain.Message;

import java.util.Date;
import java.util.List;

/**
 * Chatroom DAO implementation
 */
public interface ChatroomDAO extends AbstractDao<Message, Long> {
    /**
     * Get all Messages published after the date received in parameter
     *
     * @param date
     * @return List of Messages <b> ordered by creation date Ascending </b>
     */
    public List<Message> findAllMessagesCreatedAfter(Date date);
}
