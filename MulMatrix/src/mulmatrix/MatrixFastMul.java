/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mulmatrix;

import java.util.concurrent.CyclicBarrier;

/**
 *
 * @author lucian
 */
class MatrixFastMul
{
    MatrixBase matrix_;

    protected  MatrixFastMul(MatrixBase matrix_)
    {
        this.matrix_ = matrix_;
    }

    public MatrixFastMul(double [][] matrix)
    {
        this.matrix_ = new MatrixBase(matrix_);
    }

    public MatrixFastMul sum (MatrixFastMul matr)
    {
        return new MatrixFastMul(matrix_.sum(matr.matrix_));
    }


}
