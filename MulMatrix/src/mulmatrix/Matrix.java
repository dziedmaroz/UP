 
package mulmatrix;

/**
 *
 * @author lucian
 */
public class Matrix
{

    private double[][] matrix_;

    public int rowCount()
    {
        return matrix_.length;
    }

    public int columnCount()
    {
        return matrix_[0].length;
    }

    public Matrix(double[][] matrix_)
    {
        this.matrix_ = matrix_;
    }

    private Matrix(MatrixBase B)
    {
        this.matrix_ = B.toDouble();
    }

    public Matrix sum(Matrix B)
    {
        throw new UnsupportedOperationException("Unsupported yet");
    }

    public Matrix mul(Matrix X) throws MatrixWrongDimentionsExcpetion
    {
        MatrixBase A = null;
        MatrixBase B = null;
        try
        {
            A = new MatrixBase(matrix_);
            B = new MatrixBase(X.matrix_);
        } catch (MatrixWrongDimentionsExcpetion e)
        {
            System.out.println(e);
        }
        MatrixFastMul C = null;
        try
        {

            C = new MatrixFastMul(A, B);
        } catch (MatrixWrongDimentionsExcpetion e)
        {
            System.out.println(e);
        }
        return new Matrix(C.mul().toDouble());
    }
}
