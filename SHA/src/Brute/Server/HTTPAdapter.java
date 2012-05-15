/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Brute.Server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

    private String url;

    public HTTPAdapter(String url)
    {
        this.url = url;
    }

    public HTTPAdapter ()
    {
        url=null;
    }

    private String makeHeader(long contentLength, String mime, String responseState)
    {
        String header = "HTTP/1.1 " + responseState + "\n";

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
        header = header + "Connection: close\n" + "Server: BruteForcer\n\n";


        return header;

    }

    public byte[] getResponse(byte[] content, String mime, String responseState) throws IOException
    {
        if (content==null) return  make404();
        String header = makeHeader( content.length, mime,  responseState);
        byte[] headerBytes = header.getBytes();
        int headerLen = header.length();

        byte[] response = new byte[content.length + headerLen];
        for (int i = 0; i < headerLen + content.length; i++)
        {
            response[i] = i < headerLen ? headerBytes[i] : content[i - headerLen];
        }
        return response;
    }

    public String getMime ()
    {
        File file = new File(url.equals("/")?"index.html":url);
        String mime = "text/plain";
        String ext = file.getName().substring(file.getName().lastIndexOf(".")==-1?0:file.getName().lastIndexOf("."));
        if(ext.equalsIgnoreCase(".html")) mime = "text/html";
        if(ext.equalsIgnoreCase(".htm"))  mime = "text/html";
        if(ext.equalsIgnoreCase(".gif"))  mime = "image/gif";
        if(ext.equalsIgnoreCase(".jpg"))  mime = "image/jpeg";
        if(ext.equalsIgnoreCase(".jpeg")) mime = "image/jpeg";
        if(ext.equalsIgnoreCase(".bmp"))  mime = "image/x-xbitmap";
        if(ext.equalsIgnoreCase(".png"))  mime = "image/png";
        if(ext.equalsIgnoreCase(".css"))  mime = "text/css";

        return mime;

    }
    public byte[] getContent (String url) throws IOException 
    {
        byte[] content = null;
        if (url.equals("/")) url ="index.html";
        if (url.charAt(0)=='/') url = url.substring(1);
        File file = new File (url);
        if (file.exists() && file.isFile() && file.canRead())
        {
            content = new byte [(int)file.length()];
            try
            {
                InputStream  isr = new FileInputStream (file);
                int read = isr.read(content);
                if (read<content.length) throw new IOException("Loss of data");
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }

        }       

        return content;
    }
    private byte [] make404 () throws IOException
    {
        String header = "HTTP/1.1 " + "404 Not Found" + "\n";
        String content = new String(getContent("404.html"));
        // дата создания в GMT
        DateFormat df = DateFormat.getTimeInstance();
        df.setTimeZone(TimeZone.getTimeZone("GMT"));

        // время последней модификации файла в GMT
        header = header + "Last-Modified: " + df.format(new Date()) + "\n";


        // длина файла
        header = header + "Content-Length: " + content.length() + "\n";

        // строка с MIME кодировкой
        header = header + "Content-Type: " + "text/html" + "\n";

        // остальные заголовки
        header = header + "Connection: close\n" + "Server: BruteForcer\n\n";
        header+=content;

        return header.getBytes();
    }
    public byte [] badRequest () throws IOException
    {
        String header = "HTTP/1.1 " + "404 Bad Request" + "\n";
        String content = new String(getContent("400.html"));
        // дата создания в GMT
        DateFormat df = DateFormat.getTimeInstance();
        df.setTimeZone(TimeZone.getTimeZone("GMT"));

        // время последней модификации файла в GMT
        header = header + "Last-Modified: " + df.format(new Date()) + "\n";


        // длина файла
        header = header + "Content-Length: " + content.length() + "\n";

        // строка с MIME кодировкой
        header = header + "Content-Type: " + "text/html" + "\n";

        // остальные заголовки
        header = header + "Connection: close\n" + "Server: BruteForcer\n\n";
        header+=content;

        return header.getBytes();
    }
}


