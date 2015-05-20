package com.cgi.chatroom.service.converter;

import com.cgi.chatroom.model.Message;
import com.cgi.chatroom.model.UserAgent;

import com.cgi.service.chatroom.request.dto.v1.HeaderDTO;
import com.cgi.service.chatroom.type.v1.MessageOriginType;
import com.cgi.service.chatroom.type.v1.MessageType;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Calendar;
import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Test class for the {@link MessageConverter} class
 */
@RunWith(MockitoJUnitRunner.class)
public class MessageConverterTest {

    private static final Long MESSAGE_ID = 10l;
    private static final UserAgent USER_AGENT = UserAgent.MobileApp;
    private static final String SENDER = "sender";
    private static final String CONTENT = "content";
    private static final String USER_ID = "userId";
    private static final Date SEND_DATE = createDate(1);
    private static final Date CREATION_DATE = createDate(2);

    @Mock
    private DateConverter dateConverter;

    @InjectMocks
    private MessageConverter testedClass = new MessageConverter();

    @Test
    public void shouldConvertToMessageTypeSuccessfully() {
        // Given
        XMLGregorianCalendar xmlGregorianCalendarCreationDate = createXMLGregorianCalendar(1);
        XMLGregorianCalendar xmlGregorianCalendarSendDate = createXMLGregorianCalendar(2);
        Message message = new Message();
        message.setContent(CONTENT);
        message.setSenderUserAgent(UserAgent.MobileApp);
        message.setSenderNickname(SENDER);
        message.setCreationDate(CREATION_DATE);
        message.setSendDate(SEND_DATE);
        message.setCreationDate(CREATION_DATE);
        message.setSenderId(USER_ID);
        message.setId(MESSAGE_ID);

        given(dateConverter.toXMLGregorianCalendar(CREATION_DATE)).willReturn(xmlGregorianCalendarCreationDate);
        given(dateConverter.toXMLGregorianCalendar(SEND_DATE)).willReturn(xmlGregorianCalendarSendDate);

        // When
        MessageType messageType = testedClass.toChatRoomMessageType(message);
        // Then
        assertThat(messageType.getContent(), is(CONTENT));
        assertThat(messageType.getFrom(), is(SENDER));
        assertThat(messageType.getMessageId(), is(MESSAGE_ID));
        assertThat(messageType.getCreationDate(), is(xmlGregorianCalendarCreationDate));
        assertThat(messageType.getOrigin(), is(notNullValue()));
        assertThat(messageType.getOrigin().getSentDate(), is(xmlGregorianCalendarSendDate));
        assertThat(messageType.getOrigin().getUserAgent(), is(USER_AGENT.name()));
    }

    @Test
    public void shouldConvertToMessageSuccessfully() {
        // Given
        XMLGregorianCalendar xmlGregorianCalendarCreationDate = createXMLGregorianCalendar(1);
        XMLGregorianCalendar xmlGregorianCalendarSendDate = createXMLGregorianCalendar(2);
        HeaderDTO headerDTO = new HeaderDTO(new MessageOriginType(USER_AGENT.name(), xmlGregorianCalendarSendDate),
                USER_ID);

        given(dateConverter.toDate(xmlGregorianCalendarSendDate)).willReturn(SEND_DATE);

        // When
        Message message = testedClass.toMessage(SENDER, CONTENT, headerDTO, CREATION_DATE);
        // Then
        // ID should not be populated
        assertThat(message.getId(), is(nullValue()));
        assertThat(message.getContent(), is(CONTENT));
        assertThat(message.getCreationDate(), is(CREATION_DATE));
        assertThat(message.getSendDate(), is(SEND_DATE));
        assertThat(message.getSenderNickname(), is(SENDER));
        assertThat(message.getSenderUserAgent(), is(USER_AGENT));
    }

    private static Date createDate(int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, -minutes);
        return calendar.getTime();
    }

    private static XMLGregorianCalendar createXMLGregorianCalendar(int minutes) {
        try {
            XMLGregorianCalendar xmlGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar();
            xmlGregorianCalendar.setMinute(minutes);
            return xmlGregorianCalendar;
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }
    }
}
