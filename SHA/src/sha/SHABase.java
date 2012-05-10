/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sha;

/**
 *
 * @author lucian
 */
public class SHABase
{
    protected final static String ALLOWED_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    protected final static int MAX_MSG_SIZE = 6;
    public final static long TOTAL=(int) Math.pow((double) ALLOWED_CHARS.length(), (double)MAX_MSG_SIZE);
}
