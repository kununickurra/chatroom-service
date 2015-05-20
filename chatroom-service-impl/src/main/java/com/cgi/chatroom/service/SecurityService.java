package com.cgi.chatroom.service;

import com.cgi.chatroom.model.UserAgent;

import org.springframework.stereotype.Component;

/**
 * Service used to authenticate User Agents.
 */
@Component
public class SecurityService {
    /**
     * Check if a userAgent in allowed to publish a message
     *
     * @param userAgent user agent to check.
     * @return <code>true</code> if the userAgent is allowed and <code>false</code> if not.
     */
    public boolean isAllowedToPublish(String userAgent) {
        return UserAgent.exists(userAgent);
    }
}
