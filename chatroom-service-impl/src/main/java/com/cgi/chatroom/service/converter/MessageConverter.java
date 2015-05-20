package com.cgi.chatroom.service.converter;

import com.cgi.chatroom.model.Message;
import com.cgi.chatroom.model.UserAgent;

import com.cgi.service.chatroom.request.dto.v1.HeaderDTO;
import com.cgi.service.chatroom.type.v1.MessageOriginType;
import com.cgi.service.chatroom.type.v1.MessageType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Class used to convert and create {@link MessageType} and {@link Message}
 */
@Component
public class MessageConverter {

    @Autowired
    private DateConverter dateConverter;

    /**
     * Converts a {@link Message} to a {@link MessageType}
     *
     * @param message to be converted
     * @return converted messageType to {@link MessageType}
     */
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

    /**
     * Create a {@link Message}
     *
     * @param sender       Sender of the message
     * @param content      Content of the message
     * @param header       {@link HeaderDTO} object
     * @param creationDate Creation date of the message
     * @return a message instance
     */
    public Message toMessage(String sender, String content, HeaderDTO header, Date creationDate) {
        Message message = new Message();
        message.setContent(content);
        message.setCreationDate(creationDate);
        message.setSenderNickname(sender);
        message.setSendDate(dateConverter.toDate(header.getOrigin().getSentDate()));
        message.setSenderUserAgent(UserAgent.valueOf(header.getOrigin().getUserAgent()));
        message.setSenderId(header.getUserID());
        return message;
    }
}
