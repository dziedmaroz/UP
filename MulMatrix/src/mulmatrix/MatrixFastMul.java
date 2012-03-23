package mulmatrix;

import java.util.concurrent.*;

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
            } catch (MatrixWrongDimentionsExcpetion e)
            {
                System.out.println(e);
            }

            MatrixBase C = null;
            try
            {
                C = new MatrixBase(
                        // P1 + P4 - P5 + P7
                        P1.join().sum(P4.join().sum(P5.join().neg().sum(P7.join()))),
                        // P3 + P5
                        P3.join().sum(P5.join()),
                        // P2 + P4
                        P2.join().sum(P4.join()),
                        // P1 - P2 + P3 + P6
                        P1.join().sum(P2.join().neg().sum(P3.join().sum(P6.join()))));
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
}
