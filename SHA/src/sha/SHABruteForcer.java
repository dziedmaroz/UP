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
public class SHABruteForcer extends SHABase implements Runnable, Iterable<SHAStep>, Iterator<SHAStep>
{
    protected String hash;
    protected long lastTaskEnd;
    protected long portion;
    protected int workerCount;
    protected final long  TOP =(long) Math.pow(ALLOWED_CHARS.length(), MAX_MSG_SIZE);
    public SHABruteForcer(String hash, int workerCount)
    {
        this.hash = hash;
        lastTaskEnd = 0;
        this.workerCount = workerCount;
        portion = (long) Math.pow(ALLOWED_CHARS.length(), MAX_MSG_SIZE);
        portion = (portion/workerCount)/100;
    }

    public int getWorkerCount()
    {
        return workerCount;
    }

    public void changeWorkerCount(int count)
    {
        this.workerCount+= count;
        portion = (portion/workerCount)/100;
    }

    public void run()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Iterator<SHAStep> iterator()
    {
        return this;
    }

    public boolean hasNext()
    {
        return lastTaskEnd<TOP;
    }

    public SHAStep next()
    {
       return new SHAStep(lastTaskEnd, lastTaskEnd+portion);
    }

    public void remove()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }



}
