package com.exadel.education.networking.nio;

/**
 * Simple protocol to send strings. Delimiter \r\n\r\n is added to the end of every string.
 * <p/>
 * Examples:
 * 123 -> 123\r\n\r\n
 * <p/>
 * User: Sergey
 * Date: 3/24/12
 */
public final class MyProtocol {
    private static final String DELIMITER = "\r\n\r\n";

    private MyProtocol() {
    }

    public static String encodeMessage(String message) {
        return message + DELIMITER;
    }

    public static boolean canDecode(String message) {
        return (message != null) && message.endsWith(DELIMITER);
    }

    public static String decodeMessage(String message) {
        return message.substring(0, message.length() - DELIMITER.length());
    }

}
