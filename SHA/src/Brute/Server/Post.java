/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Brute.Server;

/**
 *
 * @author lucian
 */
/**
 * Класс контейнер для полученных данных
 */
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
