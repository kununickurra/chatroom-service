package com.cgi.chatroom.service;

import com.cgi.chatroom.dao.spec.ChatroomDao;
import com.cgi.chatroom.model.Message;
import com.cgi.chatroom.service.converter.DateConverter;
import com.cgi.chatroom.service.converter.MessageConverter;
import com.cgi.chatroom.service.utils.DateUtils;

import com.cgi.service.chatroom.request.dto.v1.GetMessagesFromDateRequestDTO;
import com.cgi.service.chatroom.request.dto.v1.GetMessagesReceivedAfterThisIDRequestDTO;
import com.cgi.service.chatroom.request.dto.v1.HeaderDTO;
import com.cgi.service.chatroom.request.dto.v1.PublishRequestDTO;
import com.cgi.service.chatroom.response.dto.v1.ChatroomSecurityExceptionDTO;
import com.cgi.service.chatroom.response.dto.v1.ChatroomSecurityExceptionEnumType;
import com.cgi.service.chatroom.response.dto.v1.GetMessagesFromDateResponseDTO;
import com.cgi.service.chatroom.response.dto.v1.GetMessagesReceivedAfterThisIDResponseDTO;
import com.cgi.service.chatroom.response.dto.v1.PulishResponseDTO;
import com.cgi.service.chatroom.type.v1.MessageType;
import com.cgi.service.chatroom.v1.ChatroomService;
import com.cgi.service.chatroom.v1.PublishFault;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jws.WebParam;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * ChatroomService implementation
 */
public class ChatroomServiceImpl implements ChatroomService {

    private static final Logger LOG = LoggerFactory.getLogger(ChatroomServiceImpl.class);

    @Autowired
    private ChatroomDao chatroomDao;

    @Autowired
    private DateConverter dateConverter;

    @Autowired
    private MessageConverter messageConverter;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private DateUtils dateUtils;

    @Override
    public GetMessagesFromDateResponseDTO getMessagesFromDate(
            @WebParam(partName = "parameters", name = "getMessagesFromDateRequest", targetNamespace = "http://services.cgi.com/chatroom/request/v1/") GetMessagesFromDateRequestDTO getMessagesFromDateRequestDTO) {
        Date from = dateConverter.toDate(getMessagesFromDateRequestDTO.getFromDate());
        LOG.info("getMessagesFromDate operation called with date {}", from);
        return new GetMessagesFromDateResponseDTO(getChatroomMessagesFromDate(from));
    }

    @Override
    public PulishResponseDTO publish(
            @WebParam(partName = "parameters", name = "publishRequest", targetNamespace = "http://services.cgi.com/chatroom/request/v1/") PublishRequestDTO publishRequestDTO,
            @WebParam(partName = "header", name = "header", targetNamespace = "http://services.cgi.com/chatroom/request/v1/", header = true) HeaderDTO headerDTO) throws
            PublishFault {
        LOG.info("publish operation called, message content [{}] send from [{}] with user agent [{}]",
                publishRequestDTO.getContent(), publishRequestDTO.getSender(), headerDTO.getOrigin().getUserAgent());
        // Check if the agent is authorized
        if (!securityService.isAllowedToPublish(headerDTO.getOrigin().getUserAgent())) {
            LOG.info("Unauthorized access attempt from user agent [{}], returning PublishFault ",
                    headerDTO.getOrigin().getUserAgent());
            throw new PublishFault(null, new ChatroomSecurityExceptionDTO("Access refused",
                    ChatroomSecurityExceptionEnumType.UNAUTHORIZED_AGENT));
        }

        Message newToPublish = messageConverter
                .toMessage(publishRequestDTO.getSender(), publishRequestDTO.getContent(), headerDTO,
                        dateUtils.getSystemTime());

        chatroomDao.saveOrUpdate(newToPublish);
        return new PulishResponseDTO(messageConverter.toChatRoomMessageType(newToPublish));
    }

    @Override
    public GetMessagesReceivedAfterThisIDResponseDTO getMessagesReceivedAfterThisID(
            @WebParam(partName = "parameters", name = "getMessagesReceivedAfterThisIDRequest", targetNamespace = "http://services.cgi.com/chatroom/request/v1/") GetMessagesReceivedAfterThisIDRequestDTO getMessagesReceivedAfterThisIDRequestDTO) {
        long messageId = getMessagesReceivedAfterThisIDRequestDTO.getMessageID();
        LOG.info("getMessagesReceivedAfterThisID operation called with ID [{}]", messageId);
        Message message = chatroomDao.findById(messageId);
        if (message == null) {
            LOG.info("getMessagesReceivedAfterThisID, no message with ID [{}] can be found, returning empty list",
                    messageId);
            return new GetMessagesReceivedAfterThisIDResponseDTO(Collections.<MessageType>emptyList());
        }
        return new GetMessagesReceivedAfterThisIDResponseDTO(getChatroomMessagesFromDate(message.getCreationDate()));
    }

    private List<MessageType> getChatroomMessagesFromDate(Date date) {
        List<Message> messages = chatroomDao.findAllMessagesCreatedAfter(date);
        List<MessageType> chatRoomMessages = new ArrayList<>();
        for (Message message : messages) {
            chatRoomMessages.add(messageConverter.toChatRoomMessageType(message));
        }
        return chatRoomMessages;
    }
}
