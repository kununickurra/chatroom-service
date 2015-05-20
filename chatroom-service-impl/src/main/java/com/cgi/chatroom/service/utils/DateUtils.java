package com.cgi.chatroom.service.utils;

import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Utility class for date handling
 */
@Component
public class DateUtils {

    /**
     * @return the current system date
     */
    public Date getSystemTime() {
        return new Date();
    }
}
