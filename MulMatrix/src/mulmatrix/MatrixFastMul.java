package mulmatrix;

import java.util.concurrent.*;
class Globals
{
    static ForkJoinPool fjPool = new ForkJoinPool();
}
class MatrixFastMul extends RecursiveTask<MatrixBase>
{

    private MatrixBase A, B;

    @Override
    protected MatrixBase compute()
    {
        if (A.getSize() == 1 && B.getSize() == 1)
        {
            MatrixBase res = null;
            try
            {
                double[][] tmp = new double[1][1];
                tmp[0][0] = A.toDouble()[0][0] * B.toDouble()[0][0];
                res = new MatrixBase(tmp);
            } catch (MatrixWrongDimentionsExcpetion e)
            {
                System.out.println(e);
                System.out.println("MatrixFastMul: returning null!");
            }
            return res;
        } else
        {
            MatrixFastMul P1 = null;
            MatrixFastMul P2 = null;
            MatrixFastMul P3 = null;
            MatrixFastMul P4 = null;
            MatrixFastMul P5 = null;
            MatrixFastMul P6 = null;
            MatrixFastMul P7 = null;

            MatrixBase P1res = null;
            MatrixBase P2res = null;
            MatrixBase P3res = null;
            MatrixBase P4res = null;
            MatrixBase P5res = null;
            MatrixBase P6res = null;
            MatrixBase P7res = null;


            try
            {
                //                                   (A11 + A22)                                   *   (B11+B22)
                P1 = new MatrixFastMul(A.getBlock(Blocks.A).sum(A.getBlock(Blocks.D)), B.getBlock(Blocks.A).sum(B.getBlock(Blocks.D)));
                //                                   (A21 + A22)                                    * B11
                P2 = new MatrixFastMul(A.getBlock(Blocks.C).sum(A.getBlock(Blocks.D)), B.getBlock(Blocks.A));
                //                                   A11                     B12 - B22
                P3 = new MatrixFastMul(A.getBlock(Blocks.A), B.getBlock(Blocks.B).sum(B.getBlock(Blocks.D).neg()));
                //                                      A22              *   (B21-B11)
                P4 = new MatrixFastMul(A.getBlock(Blocks.D), B.getBlock(Blocks.C).sum(B.getBlock(Blocks.A).neg()));
                //                                      (A11+A22)        *   B22
                P5 = new MatrixFastMul(A.getBlock(Blocks.A).sum(A.getBlock(Blocks.D)), B.getBlock(Blocks.D));
                //                                      (A21-A11)     *  (B11+B12)
                P6 = new MatrixFastMul(A.getBlock(Blocks.C).sum(A.getBlock(Blocks.A).neg()), B.getBlock(Blocks.A).sum(B.getBlock(Blocks.B)));
                //                                      (A21-A11)    *   (B21+B22)
                P7 = new MatrixFastMul(A.getBlock(Blocks.C).sum(A.getBlock(Blocks.D).neg()), B.getBlock(Blocks.C).sum(B.getBlock(Blocks.D)));

                
                P2.fork();
                P3.fork();
                P4.fork();
                P5.fork();
                P6.fork();
                P7.fork();


            } catch (MatrixWrongDimentionsExcpetion e)
            {
                System.out.println(e);
            }

            MatrixBase C = null;
            try
            {
                P1res = P1.compute();
                P2res = P2.join();
                P3res = P3.join();
                P4res = P4.join();
                P5res = P5.join();
                P6res = P6.join();
                P7res = P7.join();
                C = new MatrixBase(
                        // P1 + P4 - P5 + P7
                        P1res.sum(P4res.sum(P5res.neg().sum(P7res))),
                        // P3 + P5
                        P3res.sum(P5res),
                        // P2 + P4
                        P2res.sum(P4res),
                        // P1 - P2 + P3 + P6
                        P1res.sum(P2res.neg().sum(P3res.sum(P6res))));
            } catch (MatrixWrongDimentionsExcpetion e)
            {
                System.out.println(e);
            }
            return C;
        }
    }

    public MatrixFastMul(MatrixBase A, MatrixBase B) throws MatrixWrongDimentionsExcpetion
    {
        if (A.getSize() != B.getSize())
        {
            throw new MatrixWrongDimentionsExcpetion("MatrixFastMul says: Sizes do not match");
        }
        this.A = A;
        this.B = B;
    }

    public MatrixBase mul () throws MatrixWrongDimentionsExcpetion
    {
        return Globals.fjPool.invoke(new MatrixFastMul(A, B));
    }
}
