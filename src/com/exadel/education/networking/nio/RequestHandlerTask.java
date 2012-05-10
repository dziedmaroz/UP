package com.exadel.education.networking.nio;

import java.util.Date;

/**
 * Tasks that handles client requests
 */
public class RequestHandlerTask implements Runnable {
    private final String request;
    private final ClientConnection connection;

    public RequestHandlerTask(String request, ClientConnection connection) {
        this.request = request;
        this.connection = connection;
    }

    @Override
    public void run() {
        String requestBody = MyProtocol.decodeMessage(request).trim();

        System.out.println("[" + new Date() + "] received message: " + requestBody);

        connection.write(MyProtocol.encodeMessage("Your message [" + requestBody + "] has been received"));
    }
}
