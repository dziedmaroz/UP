package server;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.*;
import java.nio.channels.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import sha.SHABruteForcer;
import sha.SHAStep;

/**
 *
 * @author lucian
 */
enum Status {NEED_TASK,SUCSESS};
public class BruteServer implements Runnable
{

    private static final String S_NEED_TASK = "100\n";
    private static final String S_SUCSESS = "200\n";
    private static int AVG_CLI_COUNT = 10;

    class Post
    {
        private Status status;
        private String message;

        public Post(Status status, String message)
        {
            this.status = status;
            this.message = message;
        }

        public String getMessage()
        {
            return message;
        }

        public Status getStatus()
        {
            return status;
        }
    }

    SHABruteForcer bruteForcer;
    private ServerSocketChannel serverChannel;
    private Selector selector;
    private byte[] buffer = new byte[2048];
    private Charset charset = Charset.forName("UTF-8");
    private CharBuffer charBuffer = CharBuffer.allocate(2048);
    private CharsetDecoder decoder = charset.newDecoder();
    Map<SelectionKey, ByteBuffer> connections = new HashMap<SelectionKey, ByteBuffer>();
    private boolean succses = false;

    public BruteServer(int port,String hash) throws IOException
    {
        serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);
        serverChannel.socket().bind(new InetSocketAddress(port));
        selector = Selector.open();
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        bruteForcer = new SHABruteForcer(hash, AVG_CLI_COUNT);
    }

    public void run()
    {
        while (true)
        {
            try
            {
                if (selector.isOpen())
                {
                    selector.select();
                    Set<SelectionKey> keys = selector.selectedKeys();
                    for (SelectionKey key : keys)
                    {
                        if (!key.isValid())
                        {
                            continue;
                        }
                        if (key.isAcceptable())
                        {
                            ServerSocketChannel serverChanelAccept = (ServerSocketChannel) key.channel();
                            SocketChannel socketChannel = serverChanelAccept.accept();
                            socketChannel.configureBlocking(false);
                            SelectionKey selectionKeyRead = socketChannel.register(selector, SelectionKey.OP_READ);
                            ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
                            connections.put(selectionKeyRead, byteBuffer);
                        } else if (key.isReadable())
                        {
                            SocketChannel socketChannel = (SocketChannel) key.channel();
                            int read;
                            ByteBuffer byteBuffer = connections.get(key);
                            byteBuffer.clear();
                            try
                            {
                                read = socketChannel.read(byteBuffer);
                            } catch (IOException e)
                            {
                                closeChannel(key);
                                break;
                            }
                            if (read == -1)
                            {
                                closeChannel(key);
                                break;
                            } else if (read > 0)
                            {
                                byteBuffer.flip();
                                byteBuffer.mark();
                                Post post = decodeAndCheck(read, byteBuffer);
                                if (post!=null)
                                {
                                    byteBuffer.reset();                                   
                                    switch (post.getStatus())
                                    {
                                        case NEED_TASK:
                                        {
                                            key.interestOps(SelectionKey.OP_WRITE);                                           
                                            break;
                                        }
                                        case SUCSESS:
                                        {
                                            System.out.println (post.getMessage());
                                            succses = true;
                                            for (SelectionKey skey:selector.selectedKeys())
                                            {
                                                skey.interestOps(SelectionKey.OP_WRITE);
                                            }
                                        }
                                    }
                                }
                            }
                        } else if (key.isWritable())
                        {
                            ByteBuffer byteBuffer = connections.get(key);
                            SocketChannel socketChannel = (SocketChannel) key.channel();
                            if (succses)
                            {
                                byteBuffer.clear();
                                byteBuffer.put("DIE\n".getBytes());
                                socketChannel.write(byteBuffer);
                            }
                            if (bruteForcer.hasNext())
                            {
                                SHAStep step = bruteForcer.next();
                                try
                                {
                                    byteBuffer.clear();
                                    byteBuffer.put(bruteForcer.getHash().getBytes());
                                    byteBuffer.put("\n".getBytes());
                                    byteBuffer.put(step.getBytes());
                                    int result = socketChannel.write(byteBuffer);
                                    if (result == -1)
                                    {
                                        closeChannel(key);
                                    }
                                } catch (IOException e)
                                {
                                    closeChannel(key);
                                }
                                if (byteBuffer.position() == byteBuffer.limit())
                                {
                                    key.interestOps(SelectionKey.OP_READ);
                                }
                            }
                            else
                            {
                                closeChannel(key);
                            }
                        }
                    }
                    keys.clear();
                } else break;
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private Post decodeAndCheck (int read, ByteBuffer byteBuffer)
    {
        charBuffer.clear();
        decoder.decode(byteBuffer,charBuffer,false);
        charBuffer.flip();
        Status status = null;
        String message = null;
        if (S_NEED_TASK.equals(charBuffer.toString().substring(0,3)))
        {
            status = Status.NEED_TASK;
            return new Post(status, message);
        }
        if (S_SUCSESS.equals(charBuffer.toString().substring(0,3)))
        {
            status = Status.SUCSESS;
            message = charBuffer.toString().substring(4,charBuffer.toString().length());
            return new Post(status, message);
        }

        return null;
    }


    private void closeChannel (SelectionKey key) throws IOException
    {
        connections.remove(key);
        SocketChannel socketChannel = (SocketChannel) key.channel();
        if (socketChannel.isConnected())
        {
            socketChannel.close();
        }
        key.cancel();
    }

    public  synchronized void shutdownServer ()
    {
        Set<SelectionKey> keySet = connections.keySet();
        for (SelectionKey key:keySet)
        {
            SocketChannel socketChannel = (SocketChannel) key.channel();
            if (socketChannel.isConnected())
            {
                try
                {
                    socketChannel.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        if (serverChannel.isOpen())
        {
            try
            {
                serverChannel.close();
                selector.close();
            }
            catch (IOException e)
            {

            }
                
        }
    }
}
