package com.rox.nio;

public class ResponseHandler {
    private byte[] response = null;

    public synchronized boolean handleResponse(byte[] rsp) {
        this.response = rsp;
        this.notify();
        return true;
    }

    public synchronized void waitForResponse() {
        while (this.response == null) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println(new String(this.response));
    }
}