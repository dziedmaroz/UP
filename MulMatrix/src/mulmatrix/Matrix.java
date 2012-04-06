/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mulmatrix;

/**
 *
 * @author lucian
 */
public class Matrix {
     private double [][] matrix;

    public Matrix(double[][] matrix)
    {
        this.matrix = matrix;
    }

    private int max (int x, int y)
    {
        return x>y?x:y;
    }

    public Matrix mul (Matrix M) throws MatrixWrongDimentionException
    {
        int thisM = this.matrix.length;
        int thisN = this.matrix[0].length;

        int mM = M.matrix.length;
        int mN = M.matrix[0].length;

        if (mM != thisN ) throw new MatrixWrongDimentionException();

        int mSize = max (max(thisM,thisN),max(mM,mN));

        mSize = (int) Math.pow(2, Math.ceil(Math.log(mSize)/Math.log(2)));
        

        double [][] A = new double [mSize][mSize];
        double [][] B = new double [mSize][mSize];
        double [][] C = new double [mSize][mSize];

        for (int i=0;i<thisM;i++)
        {
            for (int j=0;j<thisN;j++)
            {
                A[i][j] = matrix[i][j];
            }
        }

        for (int i=0;i<mM;i++)
        {
            for  (int j=0;j<mN;j++)
            {
                B[i][j] = M.matrix [i][j];
            }
        }
        MatrixBlock blockA = new MatrixBlock(A, 0, 0, mSize);
        MatrixBlock blockB = new MatrixBlock(B, 0, 0, mSize);
        MatrixBlock blockC = new MatrixBlock(C, 0, 0, mSize);
        MulMatrix mulMatr = new MulMatrix(blockA, blockB, blockC,mSize);
        mulMatr.mul();
        double [][] res = new double [thisM][mN];
        for (int i=0;i<thisM;i++)
        {
            for (int j=0;j<mN;j++)
            {
                res [i][j] = C[i][j];
            }
        }
        return new Matrix(res);
    }
    public double cell (int y, int x) throws MatrixWrongDimentionException
    {
        if (y>=matrix.length || x > matrix[0].length) throw new MatrixWrongDimentionException ();
        return matrix[y][x];
    }
}
