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
public class SHABruteForcer extends SHABase implements  Iterable<SHAStep>, Iterator<SHAStep>,Comparable<SHABruteForcer>
{
    protected String hash;
    protected long lastTaskEnd;
    protected long portion;
    protected int workerCount;
    protected long  top ;
    protected long buildTime;
    public SHABruteForcer(String hash, int workerCount)
    {
        this.hash = hash;
        lastTaskEnd = 0;
        this.workerCount = workerCount;
        top =(long) Math.pow(ALLOWED_CHARS.length(), MAX_MSG_SIZE);
        portion = (long) Math.pow(ALLOWED_CHARS.length(), MAX_MSG_SIZE);
        portion = (portion/workerCount)/100;
        buildTime = System.currentTimeMillis();
    }

    public SHABruteForcer(String hash, long start, long end)
    {
        this.hash = hash;
        this.lastTaskEnd = start;
        this.top = end;
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

    public Iterator<SHAStep> iterator()
    {
        return this;
    }

    public boolean hasNext()
    {
        return lastTaskEnd<top;
    }

    public SHAStep next()
    {
       SHAStep nxt = new SHAStep(lastTaskEnd+1, lastTaskEnd+portion);
       lastTaskEnd+=portion;
       return nxt;
    }

    public void remove()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getHash()
    {
        return hash;
    }

    public long peekLastTask ()
    {
        return lastTaskEnd;
    }

    public int compareTo(SHABruteForcer t)
    {
        if (this.hash.equals(t.hash)) return 0;
        return (int) (this.buildTime-t.buildTime)%1000;
    }


    public boolean equals(SHABruteForcer t)
    {
        return this.hash.equals(t.hash);
    }


}
