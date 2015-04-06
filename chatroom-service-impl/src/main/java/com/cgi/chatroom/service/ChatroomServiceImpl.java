package com.cgi.chatroom.service;

import com.cgi.chatroom.dao.ChatroomDAO;
import com.cgi.chatroom.domain.Message;
import com.cgi.chatroom.domain.UserAgent;
import com.cgi.chatroom.service.converter.DateConverter;
import com.cgi.chatroom.service.converter.MessageConverter;
import com.cgi.service.chatroom.request.dto.v1.GetMessagesFromDateRequestDTO;
import com.cgi.service.chatroom.request.dto.v1.GetMessagesReceivedAfterThisIDRequestDTO;
import com.cgi.service.chatroom.request.dto.v1.HeaderDTO;
import com.cgi.service.chatroom.request.dto.v1.PublishRequestDTO;
import com.cgi.service.chatroom.response.dto.v1.*;
import com.cgi.service.chatroom.v1.ChatroomService;
import com.cgi.service.chatroom.v1.PublishFault;
import com.cgi.service.chatroom.type.v1.MessageType;
import org.apache.cxf.annotations.SchemaValidation;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jws.WebParam;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ChatroomService implementation
 */
@SchemaValidation
public class ChatroomServiceImpl implements ChatroomService {

    @Autowired
    private ChatroomDAO chatroomDAO;

    @Autowired
    private DateConverter dateConverter;

    @Autowired
    private MessageConverter messageConverter;

    @Override
    public GetMessagesFromDateResponseDTO getMessagesFromDate(@WebParam(partName = "parameters", name = "getMessagesFromDateRequest", targetNamespace = "http://services.cgi.com/chatroom/request/v1/") GetMessagesFromDateRequestDTO getMessagesFromDateRequestDTO) {
        Date from = getMessagesFromDateRequestDTO.getFromDate().toGregorianCalendar().getTime();
        return new GetMessagesFromDateResponseDTO(getChatroomMessagesFromDate(from));
    }

    @Override
    public PulishResponseDTO publish(@WebParam(partName = "parameters", name = "publishRequest", targetNamespace = "http://services.cgi.com/chatroom/request/v1/") PublishRequestDTO publishRequestDTO, @WebParam(partName = "header", name = "header", targetNamespace = "http://services.cgi.com/chatroom/request/v1/", header = true) HeaderDTO headerDTO) throws PublishFault {
        // Check if the agent is authorized
        if (!UserAgent.exists(headerDTO.getOrigin().getUserAgent())) {
            throw new PublishFault(null, new ChatroomSecurityExceptionDTO("Access refused", ChatroomSecurityExceptionEnumType.UNAUTHORIZED_AGENT));
        }

        Message newToPublish = messageConverter.toMessage(
                publishRequestDTO.getSender(),
                publishRequestDTO.getContent(),
                headerDTO,
                new Date());

        chatroomDAO.saveOrUpdate(newToPublish);
        return new PulishResponseDTO(messageConverter.toChatRoomMessageType(newToPublish));
    }

    @Override
    public GetMessagesReceivedAfterThisIDResponseDTO getMessagesReceivedAfterThisID(@WebParam(partName = "parameters", name = "getMessagesReceivedAfterThisIDRequest", targetNamespace = "http://services.cgi.com/chatroom/request/v1/") GetMessagesReceivedAfterThisIDRequestDTO getMessagesReceivedAfterThisIDRequestDTO) {
        long messageId = getMessagesReceivedAfterThisIDRequestDTO.getMessageID();
        Message message = chatroomDAO.findById(messageId);
        if (message == null) {
            return new GetMessagesReceivedAfterThisIDResponseDTO();
        }
        return new GetMessagesReceivedAfterThisIDResponseDTO(getChatroomMessagesFromDate(message.getCreationDate()));
    }

    private List<MessageType> getChatroomMessagesFromDate(Date date) {
        List<Message> messages = chatroomDAO.findAllMessagesCreatedAfter(date);
        List<MessageType> chatRoomMessages = new ArrayList<>();
        for (Message message : messages) {
            chatRoomMessages.add(messageConverter.toChatRoomMessageType(message));
        }
        return chatRoomMessages;
    }
}
