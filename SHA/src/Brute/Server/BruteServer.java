package Brute.Server;

import Logger.LogLevel;
import Logger.Logger;
import Brute.BruteBase;
import java.io.*;
import java.net.InetSocketAddress;
import java.util.*;
import java.nio.channels.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import SHA.SHABase;
import SHA.SHABruteForcer;
import SHA.SHAStep;

public class BruteServer extends BruteBase implements Runnable
{

    private static final String CONFIG = "serverconfig.properties";
    // очередь хэшей для перебора
    ArrayDeque<String> hashQueue = new ArrayDeque<String>();
    //
    Map<String, TaskContainer> taskMap = new HashMap<String, TaskContainer>();
    // текущий перебор
    SHABruteForcer bruteForcer;
    //
    Map<String, String> complete = new HashMap<String, String>();
    // ожижают заданий
    Set<SelectionKey> waiting = new HashSet<SelectionKey>();
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

    // Контейнер для тасков. 
    class TaskContainer
    {

        private String hash;
        private String result;
        private String state;
        private long startTime;
        private long endTime;

        public TaskContainer(String hash, String result, String state)
        {
            this.hash = hash;
            this.result = result;
            this.state = state;
        }

        public String getHash()
        {
            return hash;
        }

        public String getResult()
        {
            return result;
        }

        public String getState()
        {
            return state;
        }

        public void setHash(String hash)
        {
            this.hash = hash;
        }

        public void setResult(String result)
        {
            this.result = result;
        }

        public void setState(String state)
        {
            this.state = state;
        }

        public long getEndTime()
        {
            return endTime;
        }

        public void setEndTime(long end)
        {
            this.endTime = end;
        }

        public long getStartTime()
        {
            return startTime;
        }

        public void setStartTime(long start)
        {
            this.startTime = start;
        }
    }

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
        logger.addRecord("\n\n\t\t\tStarting server...", logLevel.LOW);  
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
                if (selector.isOpen() || !waiting.isEmpty())
                {
                    selector.select();
                    Set<SelectionKey> keys = selector.selectedKeys();
                    if (!waiting.isEmpty())
                    {

                        keys.addAll(waiting);
                    }
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
                                    Post post = decodeAndCheck(byteBuffer);
                                    byteBuffer.reset();
                                    processPost(post, key);
                                }
                            }
                        } else if (key.isWritable())
                        {
                            logger.addRecord("Writing to " + key.channel().toString() + "...", LogLevel.HIGH);
                            ByteBuffer byteBuffer = connections.get(key);
                            SocketChannel socketChannel = (SocketChannel) key.channel();
                            while (byteBuffer.hasRemaining())
                            {
                                socketChannel.write(byteBuffer);
                            }
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

    /**
     * Валидация и разбор запроса
     * @param byteBuffer байт буфер
     * @return объект класса Post 
     */
    private Post decodeAndCheck(ByteBuffer byteBuffer)
    {
        charBuffer.clear();
        decoder.decode(byteBuffer, charBuffer, false);
        charBuffer.flip();
        Status status = null;
        String message = null;
        if (charBuffer.toString().length() < 3)
        {
            return null;
        }
        try
        {
            logger.addRecord(charBuffer.toString(), logLevel.TALKY);
            if (CODE_NEED_TASK.substring(0, 3).equals(charBuffer.toString().substring(0, 3)))
            {
                status = Status.NEED_TASK;
                logger.addRecord("Got " + Status.NEED_TASK + " request", logLevel.MEDIUM);
                return new Post(status, message);
            }
            if (CODE_SUCCSES.substring(0, 3).equals(charBuffer.toString().substring(0, 3)))
            {
                status = Status.SUCCSESS;
                message = charBuffer.toString().substring(4, charBuffer.toString().length());
                logger.addRecord("Got " + Status.SUCCSESS + " request", logLevel.MEDIUM);
                return new Post(status, message);
            }


            if (charBuffer.toString().substring(0, charBuffer.toString().indexOf("\n")).toString().matches(".* HTTP/1.1\r"))
            {
                status = Status.HTTP;
                logger.addRecord("Got " + Status.HTTP + " request", logLevel.MEDIUM);
                message = charBuffer.toString();
                return new Post(status, message);
            }

        } catch (IOException e)
        {
            e.printStackTrace();
        }


        return null;
    }

    /**
     * Закрываем канал
     * @param key по ключу
     * @throws IOException
     */
    private void closeChannel(SelectionKey key) throws IOException
    {
        logger.addRecord("Closing "+key.channel()+" chanel", logLevel.LOW);
        httpConnections.remove(key);
        connections.remove(key);
        SocketChannel socketChannel = (SocketChannel) key.channel();
        if (socketChannel.isConnected())
        {
            socketChannel.close();
        }
        key.cancel();
    }

    /**
     * Выключить сервер
     * @throws IOException
     */
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
    /*
     * Обработать пост
     */

    private void processPost(Post post, SelectionKey key)
    {
        if (post == null)
        {
            return;
        }
        switch (post.getStatus())
        {
            // HTTP-запрос
            case HTTP:
            {
                httpConnections.add(key);
                ByteBuffer byteBuffer = connections.get(key);
                byteBuffer.clear();
                byteBuffer.position(0);
                HTTPRequest httpRequest = new HTTPRequest(post.getMessage());
                boolean badRequest = false;
                if (httpRequest.getMethod() == Method.POST)
                {
                    // получен POST запрос
                    String request = httpRequest.getContent();
                    if (request.matches("Hash=[0-9a-f]{5,40}&addHashBtn=Add")) // если валидный хэш
                    {
                        String hash = request.substring("Hash=".length(), request.indexOf('&'));
                        if (!taskMap.containsKey(hash)) // если такой хэш еще не перебирается
                        {
                            // добавляем в очередь
                            hashQueue.addLast(hash);
                            // добавляем в мэп тасков
                            taskMap.put(hash, new TaskContainer(hash, "N/A", "WAITING"));
                        } else
                        {
                            badRequest = true;
                        }
                    } else
                    {
                        badRequest = true;
                    }
                }
                HTTPAdapter httpAdapter = new HTTPAdapter(httpRequest.getUrl());
                byte[] toWrite = null;
                try
                {
                    if (badRequest)
                    {
                        toWrite = httpAdapter.badRequest();
                    } else
                    {
                        toWrite = httpAdapter.getResponse(httpAdapter.getContent(httpRequest.getUrl()), httpAdapter.getMime(), "200");
                        byte[] tmp = httpAdapter.getContent(httpRequest.getUrl());

                        if (httpRequest.getUrl().equals("/") || httpRequest.getUrl().equals("/index.html"))
                        {
                            tmp = parseStat(tmp);
                            toWrite = httpAdapter.getResponse(tmp, httpAdapter.getMime(), "200");
                        }
                    }

                } catch (IOException e)
                {
                    e.printStackTrace();
                }
                byteBuffer.limit(toWrite.length);
                byteBuffer.put(toWrite);
                byteBuffer.flip();
                key.interestOps(SelectionKey.OP_WRITE);
                break;
            }
            // Нужно задание
            case NEED_TASK:
            {
                // Выдать таск
                ByteBuffer byteBuffer = connections.get(key);
                byteBuffer.clear();
                byteBuffer.position(0);
                byte[] task = makeNewTask();
                //Если заданий нет
                if (task == null)
                {
                    //Говорим ждать
                    task = CODE_WAIT.getBytes();
                }

                //Иначе выдаем таск или ставим в ожидание
                try
                {
                    logger.addRecord("Sending new task (" + new String(task) + ") to " + key.channel().toString(), logLevel.MEDIUM);
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
                byteBuffer.limit(task.length);
                byteBuffer.put(task);
                byteBuffer.flip();
                key.interestOps(SelectionKey.OP_WRITE);

                break;
            }
            // Хэш подобран
            case SUCCSESS:
            {
                // Вывести результат
                String tmp = post.getMessage();
                StringTokenizer strTokenizer = new StringTokenizer(tmp);
                String hash = strTokenizer.nextToken();
                String message = strTokenizer.nextToken();
                try
                {
                    logger.addRecord("Succseded task. Hash: " + hash + " Message: " + message, logLevel.MEDIUM);
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
                // Снять такск из очереди
                if (bruteForcer.getHash().equals(hash))
                {
                    bruteForcer = null;
                }
                if (taskMap.containsKey(hash))
                {
                    // пометить как успешно завершенный
                    taskMap.get(hash).setState("SUCCSEDED");
                    taskMap.get(hash).setResult(message);
                    taskMap.get(hash).setEndTime(System.currentTimeMillis());
                }
                break;
            }

        }
    }

    /**
     * Распарсить страничку статистики и заменить переменные значениями
     * @param bytes
     * @return
     */
    private byte[] parseStat(byte[] bytes)
    {
        String tmp = new String(bytes);
        String tasks = "";

        Iterator<Map.Entry<String, TaskContainer>> iter = taskMap.entrySet().iterator();
        while (iter.hasNext())
        {
            Map.Entry<String, TaskContainer> entry = iter.next();
            TaskContainer task = entry.getValue();
            tasks = tasks + "<div class=\"task_entry\"><ul><li><div class=\"status\">" + task.state + ":</div></li><li><div class=\"hash\">" + task.hash + "</div></li><li><div class=\"result\">"
                    + task.getResult() + "</div></li><li>" + (task.getState().equals("SUCCSEDED") ? Long.toString(task.getEndTime() - task.getStartTime()) + " ms" : "")
                    + "</li><li> <img src=\"img/close.png\" height=20px width=20px> </li></ul></div>";
        }

        tmp = tmp.replaceAll("#clients", Long.toString(connections.size() - httpConnections.size()));
        tmp = tmp.replaceAll("#total", Long.toString(SHABase.TOTAL));
        tmp = tmp.replaceAll("#lastportion", bruteForcer == null ? "No tasks pending" : Long.toString(bruteForcer.peekLastTask()));
        tmp = tmp.replaceAll("#tasks", tasks);
        return tmp.getBytes();
    }
    /*
     * Сгенерировать новое задание
     */

    private byte[] makeNewTask()
    {
        // Если сейчас ничего не считается или отправлена последняя порция
        if (bruteForcer == null || !bruteForcer.hasNext())
        {
            // Если отправлена последняя порция
            if (bruteForcer != null)
            {
                // говорим что все таски считаются
                taskMap.get(bruteForcer.getHash()).setState("OVER");
            }
            // Если очередь хэшей не пуста
            if (!hashQueue.isEmpty())
            {
                // Добавляем таск;
                bruteForcer = new SHABruteForcer(hashQueue.removeFirst());
                taskMap.get(bruteForcer.getHash()).setState("ACTIVE");
                taskMap.get(bruteForcer.getHash()).setStartTime(System.currentTimeMillis());
                return makeNewTask(); // рекурсивный вызов, теперь пойдем по другой ветке
            }
            // так уж получилось, что выдавать совершенно нечего
            return null;
        }
        SHAStep step = bruteForcer.next();
        return (CODE_NEW_TASK + bruteForcer.getHash() + "\n" + step.toString()).getBytes();
    }
}
