package server;

import com.sun.org.apache.bcel.internal.generic.GETFIELD;
import java.io.*;
import java.net.InetSocketAddress;
import java.util.*;
import java.nio.channels.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import sha.SHABase;
import sha.SHABruteForcer;
import sun.misc.Regexp;

/**
 *
 * @author lucian
 */
/**
 * NEED_TASK - клиент ожидает порцию задания
 * SUCSESS - клиент нашел исходное сообщение
 * GET - запрос на старницу веб-интерфейса
 * POST - добавить хэш
 * NULL - пусто
 */
public class BruteServer implements Runnable
{

    private static final String S_NEED_TASK = "100\n";
    private static final String S_SUCSESS = "200\n";
    private static int AVG_CLI_COUNT = 10;
    private static final String CONFIG = "serverconfig.properties";
    // очередь хэшей для перебора
    Queue<SHABruteForcer> bruteforcerQueue = new PriorityQueue<SHABruteForcer>();
    // текущий перебор    
    private ServerSocketChannel serverChannel;
    private Selector selector;
    private byte[] buffer = new byte[2048];
    private Charset charset = Charset.forName("UTF-8");
    private CharBuffer charBuffer = CharBuffer.allocate(2048);
    private CharsetDecoder decoder = charset.newDecoder();
    Map<SelectionKey, ByteBuffer> connections = new HashMap<SelectionKey, ByteBuffer>();
    Set<SelectionKey> httpConnections = new HashSet<SelectionKey>();
    // подобрали текущий хэш
    private boolean succses = false;
    // путь директории с логами
    private String logDir;
    // уровень логгирования
    private LogLevel logLevel;
    private Logger logger;

    public BruteServer() throws IOException
    {
        Properties prop = new Properties();
        FileInputStream fileIn = new FileInputStream(CONFIG);
        int port = 0;
        prop.load(fileIn);
        port = Integer.parseInt(prop.getProperty("port"));
        logDir = prop.getProperty("log");
        String tmp = prop.getProperty("loglevel").toUpperCase();
        logLevel = LogLevel.MEDIUM;
        if (tmp.equals("HIGH"))
        {
            logLevel = LogLevel.HIGH;
        }
        if (tmp.equals("MEDIUM"))
        {
            logLevel = LogLevel.MEDIUM;
        }
        if (tmp.equals("LOW"))
        {
            logLevel = LogLevel.LOW;
        }
        if (tmp.equals("TALKY"))
        {
            logLevel = LogLevel.TALKY;
        }
        logger = new Logger(logDir, logLevel);
        logger.addRecord("\n\n\t\t\tStarting server...", logLevel);
        serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);
        logger.addRecord("Binding port " + Integer.toString(port) + " on localhost...", LogLevel.LOW);
        serverChannel.socket().bind(new InetSocketAddress(port));
        selector = Selector.open();
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    public void run()
    {
        while (true)
        {

            try
            {
                logger.addRecord("Waiting for connection", LogLevel.MEDIUM);
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
                            logger.addRecord("Got connection from " + key.channel().toString(), LogLevel.MEDIUM);
                            ServerSocketChannel serverChanelAccept = (ServerSocketChannel) key.channel();
                            SocketChannel socketChannel = serverChanelAccept.accept();
                            socketChannel.configureBlocking(false);
                            SelectionKey selectionKeyRead = socketChannel.register(selector, SelectionKey.OP_READ);
                            ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
                            connections.put(selectionKeyRead, byteBuffer);
                        } else if (key.isReadable())
                        {
                            logger.addRecord("Reading from " + key.channel().toString() + "...", LogLevel.HIGH);
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
                                // Разбираем содержимое буфера
                                Post post = decodeAndCheck(read, byteBuffer);
                                byteBuffer.reset();
                                processPost(post, key);
                            }
                        } else if (key.isWritable())
                        {
                            logger.addRecord("Writing to " + key.channel().toString() + "...", LogLevel.HIGH);
                            ByteBuffer byteBuffer = connections.get(key);
                            SocketChannel socketChannel = (SocketChannel) key.channel();
                            socketChannel.write(byteBuffer);
                            if (byteBuffer.limit() == byteBuffer.position())
                            {
                                key.interestOps(SelectionKey.OP_READ);
                            }
                        }
                    }
                    keys.clear();
                } else
                {
                    break;
                }
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private Post decodeAndCheck(int read, ByteBuffer byteBuffer)
    {
        charBuffer.clear();
        decoder.decode(byteBuffer, charBuffer, false);
        charBuffer.flip();
        Status status = null;
        String message = null;

        try
        {
            logger.addRecord(charBuffer.toString(), logLevel.TALKY);
            if (S_NEED_TASK.equals(charBuffer.toString().substring(0, 3)))
            {
                status = Status.NEED_TASK;
                logger.addRecord("Got " + Status.NEED_TASK + " request", logLevel);
                return new Post(status, message);
            }
            if (S_SUCSESS.equals(charBuffer.toString().substring(0, 3)))
            {
                status = Status.SUCSESS;
                message = charBuffer.toString().substring(4, charBuffer.toString().length());
                logger.addRecord("Got " + Status.SUCSESS + " request", logLevel);
                return new Post(status, message);
            }


            if (charBuffer.toString().substring(0, charBuffer.toString().indexOf("\n")).toString().matches("GET .* HTTP/1.1\r"))
            {
                status = Status.GET;
                logger.addRecord("Got " + Status.GET + " request", logLevel);
                return new Post(status, message);
            }

        } catch (IOException e)
        {
            e.printStackTrace();
        }


        return null;
    }

    private void closeChannel(SelectionKey key) throws IOException
    {
        httpConnections.remove(key);
        connections.remove(key);
        SocketChannel socketChannel = (SocketChannel) key.channel();
        if (socketChannel.isConnected())
        {
            socketChannel.close();
        }
        key.cancel();
    }

    public synchronized void shutdownServer() throws IOException
    {
        logger.addRecord("Shutting down...", LogLevel.LOW);
        Set<SelectionKey> keySet = connections.keySet();
        for (SelectionKey key : keySet)
        {
            SocketChannel socketChannel = (SocketChannel) key.channel();
            if (socketChannel.isConnected())
            {
                try
                {
                    socketChannel.close();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        if (serverChannel.isOpen())
        {

            serverChannel.close();
            selector.close();


        }
    }

    private void processPost(Post post, SelectionKey key)
    {
        if (post == null)
        {
            return;
        }
        switch (post.getStatus())
        {
            case GET:
            {
                httpConnections.add(key);
                ByteBuffer byteBuffer = connections.get(key);
                byteBuffer.clear();
                byteBuffer.position(0);
                HTTPConnector sp = new HTTPConnector(connections.size() - httpConnections.size(), bruteforcerQueue.peek() == null ? 0 : bruteforcerQueue.peek().peekLastTask(), SHABase.TOTAL, bruteforcerQueue.peek() == null ? "" : bruteforcerQueue.peek().getHash(), post);
                byteBuffer.limit(sp.getPage().length());
                byteBuffer.put(sp.getPage().getBytes());
                byteBuffer.flip();
                key.interestOps(SelectionKey.OP_WRITE);
                // Выдать статистику
                break;
            }
            case NEED_TASK:
            {
                // Выдать таск
                break;
            }
            case POST:
            {
                // Добавить таск в очередь
                // Обновить страничку
                break;
            }
            case SUCSESS:
            {
                // Вывести результат
                // Снять такск из очереди
                break;
            }

        }
    }
}
