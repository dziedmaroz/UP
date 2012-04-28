/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sha;

import java.util.Iterator;

/**
 *
 * @author lucian
 */
public class SHAStep extends  SHABase implements Iterable<SHAStep>, Iterator<SHAStep>
{

   protected long end;
    protected long current;

    public SHAStep(long start, long end)
    {
        this.current = start-1;
        this.end = end;
    }

    public Iterator<SHAStep> iterator()
    {
       return this;
    }

    public boolean hasNext()
    {
        return current<end;
    }

    public SHAStep next()
    {
       current++;
       return this;
    }

    public void remove()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public byte[] getMsg ()
    {
        String msgStr = "";
        long x = current;
        while (x!=0)
        {
            msgStr+=ALLOWED_CHARS.charAt((int)((ALLOWED_CHARS.length()+x%ALLOWED_CHARS.length())-1)%ALLOWED_CHARS.length());
            x/=ALLOWED_CHARS.length();

        }
        return msgStr.getBytes();
    }
}
