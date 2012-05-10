/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.TimeZone;

/**
 *
 * @author fpm.arhangel
 */




class HTTPAdapter
{

   

   
    private int connetcions;
    private long lastTask;
    private long total;
    private String hash;
    private Post post;

    public HTTPAdapter(int connetcions, long lastTask, long total, String hash, Post post)
    {
        this.connetcions = connetcions;
        this.lastTask = lastTask;
        this.total = total;
        this.hash = hash;
        this.post = post;
    }

    

    private String makeHeader(long contentLength, String mime, String responseState)
    {
        String header = "HTTP/1.1 "+responseState+"\n";

        // дата создания в GMT
        DateFormat df = DateFormat.getTimeInstance();
        df.setTimeZone(TimeZone.getTimeZone("GMT"));

        // время последней модификации файла в GMT
        header = header + "Last-Modified: " + df.format(new Date()) + "\n";


        // длина файла
        header = header + "Content-Length: " + contentLength + "\n";

        // строка с MIME кодировкой
        header = header + "Content-Type: " + mime + "\n";

        // остальные заголовки
        header = header + "Connection: close\n" + "Server: BruteForce\n\n";
        

        return header;

    }

    public byte[] getResponse(byte[] content, String mime, String responseState)
    {
       String header = makeHeader(total, mime, responseState);
       byte[] headerBytes = header.getBytes();
       int headerLen = header.length();

       byte [] response = new byte [content.length+headerLen];
       for (int i=0;i<headerLen+content.length;i++)
       {
            response[i]=i<headerLen?headerBytes[i]:content[i-headerLen];
       }
       return response;
    }
}
