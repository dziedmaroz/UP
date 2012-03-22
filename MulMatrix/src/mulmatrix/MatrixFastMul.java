package mulmatrix;
import java.util.concurrent.*;

 class MatrixFastMul extends RecursiveTask<MatrixBase>
{
  private MatrixBase A, B;

    @Override
    protected MatrixBase compute()  
    {
        if (A.getSize()==1 && B.getSize()==1)
        {
            MatrixBase res=null;
            try
            {
             double [][] tmp = new double[1][1];
             tmp[0][0] = A.toDouble()[0][0]*B.toDouble()[0][0];
             res = new MatrixBase(tmp);
            }
            catch (MatrixWrongDimentionsExcpetion e)
            {
                System.out.println(e);
                System.out.println("MatrixFastMul: returning null!");
            }
            return res;
        }
        else
        {
            //                                   (A11 + A22)                                   *   (B11+B22)
            MatrixFastMul P1 = new MatrixFastMul(A.getBlock(Blocks.A).sum(A.getBlock(Blocks.D)), B.getBlock(Blocks.A).sum(B.getBlock(Blocks.D)));
            //                                   (A21 + A22)                                    * B11
            MatrixFastMul P2 = new MatrixFastMul(A.getBlock(Blocks.C).sum(A.getBlock(Blocks.D)), B.getBlock(Blocks.A));
            //                                   A11                     B12 - B22
            MatrixFastMul P3 = new MatrixFastMul(A.getBlock(Blocks.A), B.getBlock(Blocks.B).sum(B.getBlock(Blocks.D).neg()));
            //                                      A22              *   (B21-B11)
            MatrixFastMul P4 = new MatrixFastMul(A.getBlock(Blocks.D), B.getBlock(Blocks.C).sum(B.getBlock(Blocks.A).neg()));
            //                                      (A11+A22)        *   B22
            MatrixFastMul P5 = new MatrixFastMul(A.getBlock(Blocks.A).sum(A.getBlock(Blocks.D)), B.getBlock(Blocks.D));
            //                                      (A21-A11)     *  (B11+B12)
            MatrixFastMul P6 = new MatrixFastMul(A.getBlock(Blocks.C).sum(A.getBlock(Blocks.A).neg()), B.getBlock(Blocks.A).sum(B.getBlock(Blocks.B)));
            //                                      (A21-A11)    *   (B21+B22)
            MatrixFastMul P7 = new MatrixFastMul(A.getBlock(Blocks.C).sum(A.getBlock(Blocks.D).neg()), B.getBlock(Blocks.C).sum(B.getBlock(Blocks.D)));

            

        }

        return null;
    }

    public MatrixFastMul(MatrixBase A, MatrixBase B)
    {
        this.A = A;
        this.B = B;
    }

     
  

}
