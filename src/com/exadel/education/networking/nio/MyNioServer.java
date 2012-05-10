package com.exadel.education.networking.nio;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.Queue;

/**
 * Simple server that utilizes NIO
 * Date: 3/23/12
 * Time: 10:45 AM
 */
public class MyNioServer {
    private final RequestHandlerManager requestHandlerManager;
    private final InetAddress address;
    private final int port;

    public MyNioServer(InetAddress address, int port, RequestHandlerManager requestHandlerManager) throws IOException {
        this.address = address;
        this.port = port;
        this.requestHandlerManager = requestHandlerManager;
    }

    private Selector createServerSelector(InetAddress address, int port) throws IOException {
        //Selector to deal with socket channel
        Selector selector = SelectorProvider.provider().openSelector();

        InetSocketAddress socketAddress = new InetSocketAddress(address, port);
        ServerSocketChannel channel = ServerSocketChannel.open();
        channel.socket().bind(socketAddress); //Bind socketAddress
        channel.configureBlocking(false);//Let it be non-blocking
        channel.register(selector, SelectionKey.OP_ACCEPT);//Subscribe for incoming connections

        return selector;
    }

    private void start() throws IOException {
        //Create selector
        try (Selector selector = createServerSelector(address, port)) {
            while (true) {
                //Wait for an event
                selector.select();

                //Get keys
                Iterator<SelectionKey> selectedKeys = selector.selectedKeys().iterator();
                while (selectedKeys.hasNext()) {
                    SelectionKey key = selectedKeys.next();
                    selectedKeys.remove();

                    if (!key.isValid()) {
                        continue;
                    }

                    // Handle key
                    if (key.isAcceptable()) {
                        accept(selector, key);
                    } else if (key.isReadable()) {
                        read(key);
                    } else if (key.isWritable()) {
                        write(key);
                    }
                }
            }
        }
    }

    private void accept(Selector selector, SelectionKey key) throws IOException {
        // For an accept to be pending the channel must be a server socket channel.
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();

        // Accept the connection and make it non-blocking
        SocketChannel socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false);

        // Register the new SocketChannel with our Selector, indicating
        // we'd like to be notified when there's data waiting to be read
        socketChannel.register(selector, SelectionKey.OP_READ);

        //Attach our object
        SelectionKey channelKey = socketChannel.keyFor(selector);
        channelKey.attach(new MyClientConnection(this, channelKey, requestHandlerManager));
    }

    private void read(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        MyClientConnection connection = (MyClientConnection) key.attachment();

        // Attempt to read off the channel
        int numRead;
        try {
            numRead = connection.doRead(socketChannel);
        } catch (IOException e) {
            // The remote forcibly closed the connection, cancel
            // the selection key and close the channel.
            key.attach(null);
            key.cancel();
            socketChannel.close();
            return;
        }

        if (numRead == -1) {
            // Remote entity shut the socket down cleanly. Do the
            // same from our end and cancel the channel.
            key.channel().close();
            key.cancel();
        }

    }

    private void write(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        MyClientConnection connection = (MyClientConnection) key.attachment();

        Queue<ByteBuffer> queue = connection.getWriteQueue();

        // Write until there's not more data ...
        while (!queue.isEmpty()) {
            ByteBuffer buf = queue.peek();
            socketChannel.write(buf);
            if (buf.remaining() > 0) {
                // ... or the socket's buffer fills up
                break;
            }
            queue.poll();
        }

        if (queue.isEmpty()) {
            connection.onWriteComplete();
            // We wrote away all data, so we're no longer interested
            // in writing on this socket. Switch back to waiting for
            // data.
            key.interestOps(SelectionKey.OP_READ);
        }
    }

    public void readyForWrite(SelectionKey key) {
        key.interestOps(SelectionKey.OP_WRITE);
        key.selector().wakeup();
    }

    public static void main(String[] args) throws IOException {
        RequestHandlerManager requestHandlerManager = new RequestHandlerManager();

        MyNioServer server = new MyNioServer(null, 9090, requestHandlerManager);
        server.start();
    }
}
