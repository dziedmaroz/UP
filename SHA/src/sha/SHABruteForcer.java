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
public class SHABruteForcer extends SHABase implements Iterable<SHAStep>, Iterator<SHAStep>, Comparable<SHABruteForcer>
{

    protected String hash;
    protected long lastTaskEnd;
    protected static final long PORTION = 10000;
    protected long top;
    protected long buildTime;

    public SHABruteForcer(String hash)
    {
        this.hash = hash;
        lastTaskEnd = -1;
        top = TOTAL;
        buildTime = System.currentTimeMillis();
    }

    public SHABruteForcer(String hash, long start, long end)
    {
        this.hash = hash;
        this.lastTaskEnd = start;
        this.top = end;
    }

    public Iterator<SHAStep> iterator()
    {
        return this;
    }

    public boolean hasNext()
    {
        return lastTaskEnd < top;
    }

    public SHAStep next()
    {
        SHAStep nxt = new SHAStep(lastTaskEnd + 1, (lastTaskEnd + PORTION)<top?(lastTaskEnd + PORTION):top);
        lastTaskEnd += PORTION;
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

    public long peekLastTask()
    {
        return lastTaskEnd;
    }

    public int compareTo(SHABruteForcer t)
    {
        if (this.hash.equals(t.hash))
        {
            return 0;
        }
        return (int) (this.buildTime - t.buildTime) % 1000;
    }

    public boolean equals(SHABruteForcer t)
    {
        return this.hash.equals(t.hash);
    }
}
