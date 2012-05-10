/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.File;
import java.text.DateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 *
 * @author fpm.arhangel
 */
public class HTTPConnector {

    private int connetcions;
    private long lastTask;
    private long total;
    private String hash;
    private Post post;

    public HTTPConnector(int connetcions, long lastTask, long total, String hash, Post post)
    {
        this.connetcions = connetcions;
        this.lastTask = lastTask;
        this.total = total;
        this.hash = hash;
        this.post = post;
    }



    public String getPage() {
        // первая строка ответа
        // первая строка ответа
        String content =
        "<html><head><title>SHA BruteForcer</title></head><body>Clients: "+connetcions+"<br>LastTask: "+lastTask+"<br>Total: "+total+"<br>Hash: "+hash+"</body></html>";
        String  mime = "text/html";
        String response = "HTTP/1.1 200 OK\n";

        // дата создания в GMT
        DateFormat df = DateFormat.getTimeInstance();
        df.setTimeZone(TimeZone.getTimeZone("GMT"));

        // время последней модификации файла в GMT
        response = response + "Last-Modified: " + df.format(new Date()) + "\n";


        // длина файла
        response = response + "Content-Length: " + content.length() + "\n";

        // строка с MIME кодировкой
        response = response + "Content-Type: " + mime + "\n";

        // остальные заголовки
        response = response + "Connection: close\n" + "Server: BruteForce\n\n";
        response +=content+"\r\n";
        return response;
    }
    
}
