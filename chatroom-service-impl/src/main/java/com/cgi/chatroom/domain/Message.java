package com.cgi.chatroom.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Chat room Message domain class.
 */
@Entity
@Table(name = "message")
public class Message {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "content")
    @NotNull
    private String content;
    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date creationDate;
    @Column(name = "sender_id")
    @NotNull
    private String senderId;
    @Column(name = "sender_nick")
    @NotNull
    private String senderNickname;
    @Column(name = "sender_user_agent")
    @NotNull
    private UserAgent senderUserAgent;
    @Column(name = "send_date")
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date sendDate;

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderNickname() {
        return senderNickname;
    }

    public void setSenderNickname(String senderNickname) {
        this.senderNickname = senderNickname;
    }

    public UserAgent getSenderUserAgent() {
        return senderUserAgent;
    }

    public void setSenderUserAgent(UserAgent senderUserAgent) {
        this.senderUserAgent = senderUserAgent;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message)) return false;

        Message message = (Message) o;

        if (content != null ? !content.equals(message.content) : message.content != null) return false;
        if (creationDate != null ? !creationDate.equals(message.creationDate) : message.creationDate != null)
            return false;
        if (id != null ? !id.equals(message.id) : message.id != null) return false;
        if (sendDate != null ? !sendDate.equals(message.sendDate) : message.sendDate != null) return false;
        if (senderId != null ? !senderId.equals(message.senderId) : message.senderId != null) return false;
        if (senderNickname != null ? !senderNickname.equals(message.senderNickname) : message.senderNickname != null)
            return false;
        if (senderUserAgent != message.senderUserAgent) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
        result = 31 * result + (senderId != null ? senderId.hashCode() : 0);
        result = 31 * result + (senderNickname != null ? senderNickname.hashCode() : 0);
        result = 31 * result + (senderUserAgent != null ? senderUserAgent.hashCode() : 0);
        result = 31 * result + (sendDate != null ? sendDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", creationDate=" + creationDate +
                ", senderId='" + senderId + '\'' +
                ", senderNickname='" + senderNickname + '\'' +
                ", senderUserAgent=" + senderUserAgent +
                ", sendDate=" + sendDate +
                '}';
    }
}
