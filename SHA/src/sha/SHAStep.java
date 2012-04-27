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
    protected byte[] start;
    protected byte[] end;
    protected byte[] cur;

    public SHAStep(String start, String end)
    {
       
    }

    public Iterator<SHAStep> iterator()
    {
       return this;
    }

    public boolean hasNext()
    {
      
    }

    public SHAStep next()
    {
       
    }

    public void remove()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }



    


}
