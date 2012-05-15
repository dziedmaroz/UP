/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Brute.Client;

import Brute.BruteBase;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import SHA.SHAStep;
import SHA.SHAWorker;
import java.net.ConnectException;

/**
 *
 * @author lucian
 */
public class BruteClient extends BruteBase implements Runnable
{

    private SHAWorker worker;
    private String host;
    private int port;
    private Socket socket;
    private Thread thread = new Thread(this);
    private BufferedReader socketReader;
    private BufferedWriter socketWriter;

    public BruteClient(String host, int port) throws IOException
    {
        this.host = host;
        this.port = port;
        boolean flag = true;
        socket = null;
        while (flag && socket==null)
        {
            try
            {
                socket = new Socket(host, port);
            }
            catch (ConnectException e)
            {
                System.out.println(e);
                System.out.println("Sleeping "+SLEEP_TIME_MS+" ms");
                try
                {
                    thread.sleep(SLEEP_TIME_MS);
                }
                catch (InterruptedException eThread)
                {
                    eThread.printStackTrace();
                }
            }
        }

        socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        socketWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    public void run()
    {
        while (true)
        {
            try
            {
                System.out.println("Sending NEED_TASK [100]");
                socketWriter.write(CODE_NEED_TASK);
                socketWriter.flush();
                System.out.println("Waiting for answer...");
                while (!socketReader.ready());
                String code = socketReader.readLine();
                if (code.equals(CODE_WAIT.substring(0,3)))
                {
                    try
                    {
                        System.out.println("Got WAIT code.Sleeeeping "+SLEEP_TIME_MS+" ms...");
                        thread.sleep(SLEEP_TIME_MS);
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                    continue;
                } else
                {
                    if (code.equals(CODE_NEW_TASK.substring(0, 3)))
                    {
                        System.out.println("Got NEW_TASK");
                        String hash = socketReader.readLine();
                        long from = Long.parseLong(socketReader.readLine());
                        long to = Long.parseLong(socketReader.readLine());
                        System.out.println("Hash: " + hash + " From: " + from + " To: " + to);
                        worker = new SHAWorker(new SHAStep(from, to), hash);
                        worker.run();
                        try
                        {
                            worker.join();
                        } catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                        if (worker.isSuccses())
                        {
                            socketWriter.write(CODE_SUCCSES + hash + "\n" + worker.getMsg());
                            socketWriter.flush();
                        }
                    }
                }
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }

    }
}
