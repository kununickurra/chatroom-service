package com.cgi.chatroom.service;

import com.cgi.service.chatroom.request.dto.v1.GetMessagesFromDateRequestDTO;
import com.cgi.service.chatroom.request.dto.v1.GetMessagesReceivedAfterThisIDRequestDTO;
import com.cgi.service.chatroom.request.dto.v1.HeaderDTO;
import com.cgi.service.chatroom.request.dto.v1.PublishRequestDTO;
import com.cgi.service.chatroom.response.dto.v1.GetMessagesFromDateResponseDTO;
import com.cgi.service.chatroom.response.dto.v1.GetMessagesReceivedAfterThisIDResponseDTO;
import com.cgi.service.chatroom.response.dto.v1.PublishResponseDTO;
import com.cgi.service.chatroom.type.v1.MessageOriginType;
import com.cgi.service.chatroom.type.v1.MessageType;
import com.cgi.service.chatroom.v1.PublishFault;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.cgi.chatroom.dao.spec.ChatroomDao;
import com.cgi.chatroom.model.Message;
import com.cgi.chatroom.service.converter.DateConverter;
import com.cgi.chatroom.service.converter.MessageConverter;
import com.cgi.chatroom.service.utils.DateUtils;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Arrays;
import java.util.Date;

import static com.cgi.chatroom.service.ChatroomServiceTestFixture.CONTENT;
import static com.cgi.chatroom.service.ChatroomServiceTestFixture.MESSAGE_ID;
import static com.cgi.chatroom.service.ChatroomServiceTestFixture.SENDER;
import static com.cgi.chatroom.service.ChatroomServiceTestFixture.USER_AGENT;
import static com.cgi.chatroom.service.ChatroomServiceTestFixture.createDefaultMessage;
import static com.cgi.chatroom.service.ChatroomServiceTestFixture.createDefaultMessageType;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Test class for the ChatroomService
 */
@RunWith(MockitoJUnitRunner.class)
public class ChatroomServiceTest {

    @Mock
    private ChatroomDao chatroomDao;

    @Mock
    private DateConverter dateConverter;

    @Mock
    private MessageConverter messageConverter;

    @Mock
    private SecurityService securityService;

    @Mock
    private DateUtils dateUtils;

    @InjectMocks
    private ChatroomServiceImpl chatroomServiceImpl;

    @Test
    public void shouldGetMessagesFromDateSuccessfully() throws Exception {
        // Given
        Date date = new Date();
        Message m1 = createDefaultMessage(1);
        Message m2 = createDefaultMessage(2);
        MessageType mt1 = createDefaultMessageType(1);
        MessageType mt2 = createDefaultMessageType(2);

        XMLGregorianCalendar requestedDate = DatatypeFactory.newInstance().newXMLGregorianCalendar();
        given(dateConverter.toDate(requestedDate)).willReturn(date);
        given(chatroomDao.findAllMessagesCreatedAfter(date)).willReturn(Arrays.asList(m1, m2));
        given(messageConverter.toChatRoomMessageType(m1)).willReturn(mt1);
        given(messageConverter.toChatRoomMessageType(m2)).willReturn(mt2);

        GetMessagesFromDateRequestDTO getMessagesFromDateRequestDTO = new GetMessagesFromDateRequestDTO(requestedDate);
        // When
        GetMessagesFromDateResponseDTO responseDTO =
                chatroomServiceImpl.getMessagesFromDate(getMessagesFromDateRequestDTO);
        // Then
        assertThat(responseDTO.getMessage(), is(notNullValue()));
        assertThat(responseDTO.getMessage().size(), is(2));
        assertThat(responseDTO.getMessage().get(0), is(mt1));
        assertThat(responseDTO.getMessage().get(1), is(mt2));

    }

    @Test
    public void shouldGetMessagesReceivedAfterThisIDSuccessfully() throws Exception {
        // Given
        Long messageId = MESSAGE_ID;
        Date date = new Date();
        Message m0 = createDefaultMessage(0);
        m0.setCreationDate(date);
        Message m1 = createDefaultMessage(1);
        Message m2 = createDefaultMessage(2);
        MessageType mt1 = createDefaultMessageType(1);
        MessageType mt2 = createDefaultMessageType(2);

        initDefaultMocks(messageId, date, m0, m1, m2, mt1, mt2);

        // When
        GetMessagesReceivedAfterThisIDResponseDTO responseDTO = chatroomServiceImpl
                .getMessagesReceivedAfterThisID(new GetMessagesReceivedAfterThisIDRequestDTO(messageId));

        // Then
        assertThat(responseDTO.getMessage(), is(notNullValue()));
        assertThat(responseDTO.getMessage().size(), is(2));
        assertThat(responseDTO.getMessage().get(0), is(mt1));
        assertThat(responseDTO.getMessage().get(1), is(mt2));
    }

    @Test
    public void shouldNotGetAnyMessageWhenIdDoesNotExists() throws Exception {
        // Given
        Long messageId = 99l;
        Date date = new Date();
        Message m0 = createDefaultMessage(0);
        m0.setCreationDate(date);
        Message m1 = createDefaultMessage(1);
        Message m2 = createDefaultMessage(2);
        MessageType mt1 = createDefaultMessageType(1);
        MessageType mt2 = createDefaultMessageType(2);

        initDefaultMocks(messageId, date, m0, m1, m2, mt1, mt2);

        // When
        GetMessagesReceivedAfterThisIDResponseDTO responseDTO = chatroomServiceImpl
                .getMessagesReceivedAfterThisID(new GetMessagesReceivedAfterThisIDRequestDTO(messageId));

        // Then
        assertThat(responseDTO.getMessage(), is(notNullValue()));
        assertThat(responseDTO.getMessage().size(), is(2));
        assertThat(responseDTO.getMessage().get(0), is(mt1));
        assertThat(responseDTO.getMessage().get(1), is(mt2));
    }

    @Test
    public void shouldPublishMessageSuccessfully() throws Exception {
        // Given
        Message m1 = createDefaultMessage(1);
        MessageType mt1 = createDefaultMessageType(1);

        String userAgent = USER_AGENT;
        HeaderDTO headerDTO = new HeaderDTO(new MessageOriginType(userAgent, null), "");
        String sender = SENDER;
        String content = CONTENT;
        Date sentDate = new Date();
        Date currentDate = new Date();

        given(securityService.isAllowedToPublish(userAgent)).willReturn(true);
        given(messageConverter.toMessage(sender, content, headerDTO, sentDate)).willReturn(m1);
        given(messageConverter.toChatRoomMessageType(m1)).willReturn(mt1);
        given(dateUtils.getSystemTime()).willReturn(currentDate);

        PublishRequestDTO publishRequestDTO = new PublishRequestDTO();
        publishRequestDTO.setContent(content);
        publishRequestDTO.setSender(sender);

        //When
        PublishResponseDTO responseDTO = chatroomServiceImpl.publish(publishRequestDTO, headerDTO);
        verify(chatroomDao, times(1)).saveOrUpdate(m1);

        // Then
        assertThat(responseDTO.getMessage(), is(mt1));
    }

    @Test(expected = PublishFault.class)
    public void shouldThrowExceptionForUnauthorizedAccess() throws Exception {
        // Given
        Message m1 = createDefaultMessage(1);
        MessageType mt1 = createDefaultMessageType(1);
        String userAgent = USER_AGENT;
        HeaderDTO headerDTO = new HeaderDTO(new MessageOriginType(userAgent, null), "");
        String sender = SENDER;
        String content = CONTENT;
        Date date = new Date();

        try {

            given(securityService.isAllowedToPublish(userAgent)).willReturn(false);
            given(messageConverter.toMessage(sender, content, headerDTO, date)).willReturn(m1);
            given(messageConverter.toChatRoomMessageType(m1)).willReturn(mt1);
            given(dateUtils.getSystemTime()).willReturn(date);

            PublishRequestDTO publishRequestDTO = new PublishRequestDTO();
            publishRequestDTO.setContent(content);
            publishRequestDTO.setSender(sender);
            // When
            chatroomServiceImpl.publish(publishRequestDTO, headerDTO);

        } finally {
            // Then
            // Make sure the unauthorized message dos not get published.
            verify(chatroomDao, never()).saveOrUpdate(any(Message.class));

        }
    }

    private void initDefaultMocks(Long messageId, Date date, Message m0, Message m1, Message m2, MessageType mt1,
                                  MessageType mt2) throws DatatypeConfigurationException {
        XMLGregorianCalendar requestedDate = DatatypeFactory.newInstance().newXMLGregorianCalendar();
        given(dateConverter.toDate(requestedDate)).willReturn(date);
        given(chatroomDao.findById(messageId)).willReturn(m0);
        given(chatroomDao.findAllMessagesCreatedAfter(date)).willReturn(Arrays.asList(m1, m2));
        given(messageConverter.toChatRoomMessageType(m1)).willReturn(mt1);
        given(messageConverter.toChatRoomMessageType(m2)).willReturn(mt2);
    }

}
