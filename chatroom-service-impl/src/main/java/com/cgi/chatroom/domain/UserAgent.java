package com.cgi.chatroom.domain;

/**
 * Allowed user agents.
 */

public enum UserAgent {

    WebApp,
    MobileApp,
    SoapUI,
    MonitoringClient;

    public static boolean exists(String s) {
        for (UserAgent u : UserAgent.values()) {
            if (u.name().equals(s)) {
                return true;
            }
        }
        return false;
    }

}
