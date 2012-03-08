/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mulmatrix;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 *
 * @author lucian
 */
class MulThread extends Thread
{
     private CyclicBarrier barrier_;

    public MulThread(CyclicBarrier barrier_)
    {
        this.barrier_ = barrier_;
    }

     
    @Override
    public void run()
    {
        try
        {
            barrier_.await();
        }
        catch (InterruptedException e)
        {
            System.out.print(e);
        }
        catch (BrokenBarrierException e)
        {
            System.out.print(e);
        }
    }
     
}
