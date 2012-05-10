package com.exadel.education.networking.nio;

/**
 * Interface that can be used to deal with client's connection
 */
public interface ClientConnection {
    /**
     * Write data to client
     *
     * @param data data to be written
     */
    void write(byte[] data);

    /**
     * Write data to client
     *
     * @param data data to be written
     */
    void write(String data);
}
