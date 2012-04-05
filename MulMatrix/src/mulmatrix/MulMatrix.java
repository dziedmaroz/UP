/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mulmatrix;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/**
 *
 * @author lucian
 */
public class MulMatrix extends RecursiveAction {
    private double [][] A;
    private double [][] B;
    private double [][] C;
    private int x;
    private int y;
    private int blockSize;
    private ForkJoinPool fjPool;

    public MulMatrix(double[][] A, double[][] B, double[][] C, int x, int y, int blockSize)
    {
        this.A = A;
        this.B = B;
        this.C = C;
        this.x = x;
        this.y = y;
        this.blockSize = blockSize;
        fjPool = new ForkJoinPool();
    }

    @Override
    protected void compute()
    {
        if (blockSize == 2)
        {
            C[y][x] = A[y][x]*B[y][x]+A[y][x+1]*B[y+1][x];
            C[y][x+1] = A[y][x]*B[y][x+1]+A[y][x+1]*B[y+1][x+1];
            C[y+1][x] = A[y+1][x]*B[y][x]+A[y+1][x+1]*B[y+1][x];
            C[y+1][x+1] = A[y+1][x]*B[y][x+1]+A[y+1][x+1]*B[y+1][x+1];
        }
        else
        {
            MulMatrix C11 = new MulMatrix(A,B,C,x,y,blockSize/2);
            MulMatrix C12 = new MulMatrix(A, B, C, x+blockSize/2, y, blockSize/2);
            MulMatrix C21 = new MulMatrix(A, B, C, x, y+blockSize/2, blockSize/2);
            MulMatrix C22 = new MulMatrix(A, B, C, x+blockSize/2, y+blockSize/2, blockSize/2);

             
            C12.fork();
            C21.fork();
            C22.fork();

            C11.compute();
            C12.join();
            C21.join();
            C22.join();
        }
    }
    public void mul ()
    {
        long timeStart = System.currentTimeMillis();
        System.out.println ("[LOG]: Mul START @ " + timeStart);
        fjPool.invoke(this);
        long timeEnd = System.currentTimeMillis();
        System.out.println ("[LOG]: Mul END @ " + timeEnd+ " \n [LOG]:"+ (timeEnd-timeStart)+ " ms total");
        //return C;
    }
}
