package mulmatrix;

import java.util.concurrent.CyclicBarrier;
import java.util.Arrays;
/**
 *
 * @author lucian
 */
enum Blocks {A,B,C,D};
/*
 *      ------------------------
 *     |           |           |
 *     |           |           |
 *     |     A     |     B     |
 *     |           |           |
 *     |           |           |
 * A =  ------------------------
 *     |           |           |
 *     |           |           |
 *     |     C     |     D     |
 *     |           |           |
 *     |           |           |
 *      ------------------------
 */


public class Matrix
{
        private double [] [] matrix_;
        private CyclicBarrier barrier_;
    /**
     * Конструктор матрицы из массива вещественных чисел. Если размеры оригинальной
     * матрицы не являются степенью двойки, матрица дозаполняется нулями.
     * @param matrix
     */
    public Matrix(double[][] matrix) throws MatrixWrongDimentionsExcpetion
    {
        if (matrix.length==0) throw new MatrixWrongDimentionsExcpetion("Matrix say's: very funny, it's just nothing");
        if (matrix.length != matrix[0].length) throw new MatrixWrongDimentionsExcpetion("Matrix say's: we are accept square matrixes only");

        matrix_ = new double [(int)Math.pow(2,Math.floor((Math.log(matrix.length)/Math.log(2)))+1)][(int)Math.pow(2,Math.floor((Math.log(matrix.length)/Math.log(2)))+1)];
        for (int i=0;i<matrix.length;i++)
        {
            System.arraycopy(matrix[i], 0, matrix_[i], 0, matrix.length);
        }
        barrier_ = null;
    }
    /**
     * Конструктор копирования. Проверяет выполняются ли над оригиналом какие-
     * нибудь действия. Если что бросает UnsupportedOperationException
     * @param obj
     */
    public Matrix (Matrix obj)
    {
        if (obj.barrier_.getNumberWaiting()==0)
        {
            this.matrix_ = Arrays.copyOf(obj.matrix_, matrix_.length);
            barrier_ = null;
        }
        else
        {
            throw new  UnsupportedOperationException ("Some actions pending...");
        }
    }

    /**
     * Складывает две матрицы и возвращает результат. Не меняет this
     * @param matr
     * @return
     */
    public Matrix sum (Matrix matr)
    {
        Matrix res = new Matrix(matr);
        if (this.matrix_.length == matr.matrix_.length && this.matrix_[0].length== matr.matrix_[0].length)
        {
            for (int i=0;i<matrix_.length;i++)
            {
                for (int j=0;j<matrix_[i].length;j++)
                {
                    res.matrix_[i][j] +=this.matrix_[i][j];
                }
            }
        }
        else
        {
            throw new UnsupportedOperationException("Wrong dimetions...");
        }

        return res;
    }

   /**
    * Возвращает блок матрицы. Блоки соответсвуют A,B,C,D как показано на рисунке
    * выше
    * @param block
    * @return
    */
    private Matrix getBlock (Blocks block)
    {
        throw new UnsupportedOperationException ("Not supported yet");
    }
}
