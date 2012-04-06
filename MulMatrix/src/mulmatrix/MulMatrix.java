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
class MulMatrix extends RecursiveAction {
    private  MatrixBlock A;
    private MatrixBlock B;
    private MatrixBlock C;
    private int blockSize;
    private ForkJoinPool fjPool;

    public MulMatrix(MatrixBlock A, MatrixBlock B, MatrixBlock C, int blockSize)
    {
        this.A = A;
        this.B = B;
        this.C = C;
        this.blockSize = blockSize;
        fjPool = new ForkJoinPool();
    }


    @Override
    protected void compute()
    {
        if (blockSize == 2)
        {
            double A11 = A.matrix[A.blockStartY][A.blockStartX];
            double A12 = A.matrix[A.blockStartY][A.blockStartX+1];
            double A21 = A.matrix[A.blockStartY+1][A.blockStartX];
            double A22 = A.matrix[A.blockStartY+1][A.blockStartX+1];

            double B11 = B.matrix[B.blockStartY][B.blockStartX];
            double B12 = B.matrix[B.blockStartY][B.blockStartX+1];
            double B21 = B.matrix[B.blockStartY+1][B.blockStartX];
            double B22 = B.matrix[B.blockStartY+1][B.blockStartX+1];

            C.matrix[C.blockStartY][C.blockStartX] += A11*B11+A12*B21;
            C.matrix[C.blockStartY][C.blockStartX+1] += A11*B12+A12*B22;
            C.matrix[C.blockStartY+1][C.blockStartX] += A21*B11+A22*B21;
            C.matrix[C.blockStartY+1][C.blockStartX+1] += A21*B12+A22*B22;
        }
        else
        {
            MatrixBlock A11 = new MatrixBlock(A.matrix, A.blockStartX, A.blockStartY, blockSize/2);
            MatrixBlock A12 = new MatrixBlock(A.matrix, A.blockStartX+blockSize/2, A.blockStartY, blockSize/2);
            MatrixBlock A21 = new MatrixBlock(A.matrix, A.blockStartX, A.blockStartY+blockSize/2, blockSize/2);
            MatrixBlock A22 = new MatrixBlock(A.matrix, A.blockStartX+blockSize/2, A.blockStartY+blockSize/2,blockSize/2);

            MatrixBlock B11 = new MatrixBlock(B.matrix, B.blockStartX, B.blockStartY, blockSize/2);
            MatrixBlock B12 = new MatrixBlock(B.matrix, B.blockStartX+blockSize/2, B.blockStartY, blockSize/2);
            MatrixBlock B21 = new MatrixBlock(B.matrix, B.blockStartX, B.blockStartY+blockSize/2, blockSize/2);
            MatrixBlock B22 = new MatrixBlock(B.matrix, B.blockStartX+blockSize/2, B.blockStartY+blockSize/2,blockSize/2);

            MatrixBlock C11 = new MatrixBlock(C.matrix, C.blockStartX, C.blockStartY, blockSize/2);
            MatrixBlock C12 = new MatrixBlock(C.matrix, C.blockStartX+blockSize/2, C.blockStartY, blockSize/2);
            MatrixBlock C21 = new MatrixBlock(C.matrix, C.blockStartX, C.blockStartY+blockSize/2, blockSize/2);
            MatrixBlock C22 = new MatrixBlock(C.matrix, C.blockStartX+blockSize/2, C.blockStartY+blockSize/2,blockSize/2);

            MulMatrix mulC11_1 = new MulMatrix(A11, B11, C11, blockSize/2);
            MulMatrix mulC12_1 = new MulMatrix(A11, B12, C12, blockSize/2);
            MulMatrix mulC21_1 = new MulMatrix(A21, B11, C21, blockSize/2);
            MulMatrix mulC22_1 = new MulMatrix(A21, B12, C22, blockSize/2);

            MulMatrix mulC11_2 = new MulMatrix(A12, B21, C11, blockSize/2);
            MulMatrix mulC12_2 = new MulMatrix(A12, B22, C12, blockSize/2);
            MulMatrix mulC21_2 = new MulMatrix(A22, B21, C21, blockSize/2);
            MulMatrix mulC22_2 = new MulMatrix(A22, B22, C22, blockSize/2);

            mulC11_1.fork();
            mulC12_1.fork();
            mulC21_1.fork();
            mulC22_1.fork();

            mulC11_1.join();
            mulC12_1.join();
            mulC21_1.join();
            mulC22_1.join();

            mulC11_2.fork();
            mulC12_2.fork();
            mulC21_2.fork();
            mulC22_2.fork();

            mulC11_2.join();
            mulC12_2.join();
            mulC21_2.join();
            mulC22_2.join();
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
