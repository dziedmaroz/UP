package com.exadel.education.networking.nio;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Handler of user requests
 */
public class RequestHandlerManager {
    private ExecutorService executorService;

    public RequestHandlerManager() {
        this.executorService = Executors.newFixedThreadPool(4);
    }

    /**
     * Test if the given request can be handled
     *
     * @param request request
     * @return true if this request is valid
     */
    public boolean canHandleRequest(String request) {
        return MyProtocol.canDecode(request);
    }

    public void handleRequest(String request, ClientConnection connection) {
        if (MyProtocol.canDecode(request)) {
            executorService.submit(new RequestHandlerTask(request, connection));
        }
    }

}
