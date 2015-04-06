package com.cgi.chatroom.service.converter;

import com.cgi.chatroom.domain.Message;
import com.cgi.chatroom.domain.UserAgent;
import com.cgi.service.chatroom.request.dto.v1.HeaderDTO;
import com.cgi.service.chatroom.type.v1.MessageOriginType;
import com.cgi.service.chatroom.type.v1.MessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by tilliern on 4/04/2015.
 */
@Component
public class MessageConverter {

    @Autowired
    private DateConverter dateConverter;

    public MessageType toChatRoomMessageType(Message message) {
        MessageType chatRoomMessage = new MessageType();
        chatRoomMessage.setMessageId(message.getId());
        chatRoomMessage.setContent(message.getContent());
        chatRoomMessage.setFrom(message.getSenderNickname());
        MessageOriginType messageOrigin = new MessageOriginType();
        messageOrigin.setUserAgent(message.getSenderUserAgent().name());
        chatRoomMessage.setOrigin(messageOrigin);
        chatRoomMessage.setCreationDate(dateConverter.toXMLGregorianCalendar(message.getCreationDate()));
        messageOrigin.setSentDate(dateConverter.toXMLGregorianCalendar(message.getSendDate()));
        return chatRoomMessage;
    }

    public Message toMessage(String sender, String content, HeaderDTO header, Date creationDate) {
        Message message = new Message();
        message.setContent(content);
        message.setCreationDate(creationDate);
        message.setSenderNickname(sender);
        message.setSendDate(header.getOrigin().getSentDate().toGregorianCalendar().getTime());
        message.setSenderUserAgent(UserAgent.valueOf(header.getOrigin().getUserAgent()));
        message.setSenderId(header.getUserID());
        return message;
    }
}
