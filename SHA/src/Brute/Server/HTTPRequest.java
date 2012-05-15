/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Brute.Server;

import java.util.StringTokenizer;

/**
 *
 * @author lucian
 */
enum Method
{

    GET, POST, UNHANDLED
};

enum Response
{

    OK, BAD_REQUEST, NOT_FOUND
};

class HTTPRequest
{

    private String url;
    private Method method;
    private String content;

    public HTTPRequest(String header)
    {
        StringTokenizer strTok = new StringTokenizer(header, "\r\n");
        content = "";
        url = "";

        method = Method.UNHANDLED;

        if (strTok.hasMoreTokens())
        {
            String tmp = strTok.nextToken();
            if (tmp.matches("GET .* HTTP/1.1"))
            {
                method = Method.GET;
            }
            if (tmp.matches("POST .* HTTP/1.1"))
            {
                method = Method.POST;
            }
            tmp = tmp.replace("GET", "");
            tmp = tmp.replace("POST", "");
            tmp = tmp.replace("HTTP/1.1", "");
            tmp = tmp.replace(" ", "");
            url = tmp;
        }
        if (method == Method.POST)
        {
            int contentLength = 0;
            while (strTok.hasMoreTokens())
            {
                String tmp = strTok.nextToken();
                if (tmp.matches("Content-[lL]ength: .*"))
                {
                    tmp = tmp.replace("Content-length: ", "");
                     tmp = tmp.replace("Content-Length: ", "");
                    tmp = tmp.replace(" ", "");
                    contentLength = Integer.parseInt(tmp);
                    break;
                }
            }
            content = header.substring(header.length() - contentLength  );
        }

    }

    public HTTPRequest(String url, Method method, String content)
    {
        this.url = url;
        this.method = method;
        this.content = content;
    }

    public Method getMethod()
    {
        return method;
    }

    public void setMethod(Method method)
    {
        this.method = method;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }
}
