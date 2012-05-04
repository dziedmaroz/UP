/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sha;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author lucian
 */
public class SHAWorker implements Runnable
{
    protected SHAStep step;
    protected String hash;
    protected String msg;
    protected boolean succses;
    private Thread thread;

    public SHAWorker(SHAStep step, String hash)
    {
        this.step = step;
        this.hash = hash;
        msg = "";
        succses = false;
        thread = new Thread(this);
        thread.start();
    }

    protected  static String bytesToHex(byte[] b) {
      char hexDigit[] = {'0', '1', '2', '3', '4', '5', '6', '7',
                         '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
      StringBuffer buf = new StringBuffer();
      for (int j=0; j<b.length; j++) {
         buf.append(hexDigit[(b[j] >> 4) & 0x0f]);
         buf.append(hexDigit[b[j] & 0x0f]);
      }
      return buf.toString();
   }
    public void run()
    {
        MessageDigest digest = null;
        try
        {
            digest =   MessageDigest.getInstance("SHA-1");
        }
        catch (NoSuchAlgorithmException e)
        {
            System.out.println ("[ERR]: "+e.toString());
        }

        for (SHAStep i: step)
        {
           String hashTmp = bytesToHex(digest.digest(i.getMsg()));
           hashTmp=hashTmp.toLowerCase();
           String msgTmp = new String ( i.getMsg());       
           if (hashTmp.equals(hash))
           {
               succses=true;
               msg = msgTmp;
               break;
           }
        }
    }
    public void join () throws InterruptedException
    {
        thread.join();
    }

    public String getMsg()
    {
        return msg;
    }

    public boolean isSuccses()
    {
        return succses;
    }
    

}
