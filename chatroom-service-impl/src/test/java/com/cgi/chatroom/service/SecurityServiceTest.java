package com.cgi.chatroom.service;

import com.cgi.chatroom.model.UserAgent;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test class for the {@link SecurityService}
 */

public class SecurityServiceTest {

    private SecurityService testedClass = new SecurityService();

    @Test
    public void shouldAllowKnownUserAgents() {
        for (UserAgent agent : UserAgent.values()) {
            // Given - When - Then
            assertTrue(testedClass.isAllowedToPublish(agent.name()));
        }
    }

    @Test
    public void shouldRefuseUnknownUserAgents() {
        // Given - When - Then
        assertFalse(testedClass.isAllowedToPublish("_unknown_user_agent_"));
    }

}
