package com.cgi.chatroom.service;

import com.cgi.chatroom.model.Message;

import com.cgi.service.chatroom.type.v1.MessageType;

public class ChatroomServiceTestFixture {

    public static final long MESSAGE_ID = 99l;
    public static final String USER_AGENT = "UserAgent";
    public static final String SENDER = "Sender";
    public static final String CONTENT = "Content";

    public static Message createDefaultMessage(int i) {

        Message m = new Message();
        m.setContent(CONTENT + i);
        return m;
    }

    public static MessageType createDefaultMessageType(int i) {
        MessageType mt = new MessageType();
        mt.setContent(CONTENT + i);
        return mt;
    }
}
