package com.cgi.chatroom.dao;

import com.cgi.chatroom.dao.spec.ChatroomDao;
import com.cgi.chatroom.model.Message;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * Implementation of the ChatroomDAO.
 */
@Component
public class ChatroomDaoImpl extends AbstractDaoImpl<Message, Long> implements ChatroomDao {

    private static final Logger LOG = LoggerFactory.getLogger(ChatroomDaoImpl.class);

    @Override
    public List<Message> findAllMessagesCreatedAfter(Date date) {
        LOG.info("Running findAllMessagesCreatedAfter with date {} ", date);
        Query query = getCurrentSession()
                .createQuery("from Message where creationDate > :creationDate order by creationDate asc");
        query.setParameter("creationDate", date);
        return (List<Message>) query.list();
    }
}
