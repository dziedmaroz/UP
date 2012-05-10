package com.exadel.education.networking.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Default implementation of {@link ClientConnection}
 */
public class MyClientConnection implements ClientConnection {
    //Reference to the main server class
    private final MyNioServer server;
    //Identifier of communication channel
    private final SelectionKey selectionKey;
    //Handler of requests
    private final RequestHandlerManager requestHandlerManager;

    // The buffer into which we'll read data when it's available
    private final ByteBuffer readBuffer = ByteBuffer.allocate(8192);
    //Response queue
    private final Queue<ByteBuffer> writeQueue = new ConcurrentLinkedDeque<>();

    public MyClientConnection(MyNioServer server, SelectionKey key, RequestHandlerManager requestHandlerManager) {
        this.server = server;
        this.selectionKey = key;
        this.requestHandlerManager = requestHandlerManager;
    }

    public int doRead(SocketChannel socketChannel) throws IOException {
        int numRead = socketChannel.read(this.readBuffer);

        if (numRead > 0) {
            byte[] array = readBuffer.array();
            String request = new String(array, 0, readBuffer.position());

            if (requestHandlerManager.canHandleRequest(request)) {
                requestHandlerManager.handleRequest(request, this);
            }
        }

        return numRead;
    }

    @Override
    public void write(byte[] data) {
        //TODO: split data into blocks?
        writeQueue.add(ByteBuffer.wrap(data));

        server.readyForWrite(selectionKey);
    }

    @Override
    public void write(String data) {
        write(data.getBytes());
    }

    public Queue<ByteBuffer> getWriteQueue() {
        return writeQueue;
    }

    public void onWriteComplete() {
        // Clear out our read buffer so it's ready for new data
        this.readBuffer.clear();
    }
}
