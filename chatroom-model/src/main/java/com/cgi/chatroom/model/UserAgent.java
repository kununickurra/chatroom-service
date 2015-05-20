package com.cgi.chatroom.model;

/**
 * Allowed user agents domain Enum.
 */

public enum UserAgent {

    WebApp,
    MobileApp,
    SoapUI,
    MonitoringClient;

    /**
     * Check if a user agent given as string is defined in the Enumeration.
     * @param agent as String
     * @return <code>true</code> if the User agent exists in the Enum of <code>false</code> idf not.
     */
    public static boolean exists(String agent) {
        for (UserAgent u : UserAgent.values()) {
            if (u.name().equals(agent)) {
                return true;
            }
        }
        return false;
    }

}
