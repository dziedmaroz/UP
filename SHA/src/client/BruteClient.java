/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import sha.SHAStep;
import sha.SHAWorker;

/**
 *
 * @author lucian
 */
public class BruteClient
{
    private static final String S_NEED_TASK = "100\n";
    private static final String S_SUCSESS = "200\n";
    private SHAWorker worker;
    private String host;
    private int port;
    private Socket socket;

    private BufferedReader socketReader;
    private BufferedWriter socketWriter;
    public BruteClient(String host, int port) throws IOException
    {
        this.host = host;
        this.port = port;
        socket = new Socket(host,port);
        socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        socketWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    public void run() throws  IOException
    {
         while(true)
         {
             System.out.println("Sending NEED_TASK [100]");
             socketWriter.write(S_NEED_TASK);
             socketWriter.flush();
             System.out.println("Waiting for answer...");
             while (!socketReader.ready());
             String hash = socketReader.readLine();

             long from = Long.parseLong(socketReader.readLine());
             long to = Long.parseLong(socketReader.readLine());
             System.out.println ("Hash: "+hash+" From: "+from+" To: "+to);
             worker = new SHAWorker(new SHAStep(from, to), hash);
             worker.run();
             try
             {
                worker.join();
             }
             catch (InterruptedException e)
             {
                 e.printStackTrace();
             }
             if (worker.isSuccses())
             {
                 socketWriter.write(S_SUCSESS+hash+"\n"+worker.getMsg());
                 socketWriter.flush();
             }
         }
    }



    
}
