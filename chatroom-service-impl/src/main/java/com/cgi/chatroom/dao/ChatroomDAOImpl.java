package com.cgi.chatroom.dao;

import com.cgi.chatroom.domain.Message;
import org.hibernate.Query;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * Implementation of the ChatroomDAO.
 */
@Component
public class ChatroomDAOImpl extends AbstractDaoImpl<Message, Long> implements ChatroomDAO {

    @Override
    public List<Message> findAllMessagesCreatedAfter(Date date) {
        Query query = getCurrentSession().createQuery(
                "from Message where creationDate > :creationDate order by creationDate asc");
        query.setParameter("creationDate", date);
        return (List<Message>) query.list();
    }
}
