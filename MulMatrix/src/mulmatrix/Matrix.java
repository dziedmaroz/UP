package mulmatrix;

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

    }
    /**
     * Конструктор копирования.
     * @param obj
     */
    public Matrix (Matrix obj)
    {

            this.matrix_ = new double [obj.matrix_.length] [obj.matrix_.length];
            for (int i=0;i<obj.matrix_.length;i++)
            {
                this.matrix_[i]= Arrays.copyOf(obj.matrix_[i],obj.matrix_.length);
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
        int rStart = 0;
        int rFin = 0;
        int cStart = 0;
        int cFin = 0;
        switch (block)
        {
            case A:
                {
                    rStart = 0;
                    cStart = 0;
                    rFin = this.matrix_.length/2;
                    cFin = this.matrix_.length/2;
                    break;
                }
            case B:
                {
                    rStart = 0;
                    cStart = this.matrix_.length/2;
                    rFin = this.matrix_.length/2;
                    cFin = this.matrix_.length;
                    break;
                }
            case C:
                {
                    rStart = this.matrix_.length/2;
                    cStart = 0;
                    rFin = this.matrix_.length;
                    cFin = this.matrix_.length/2;
                    break;
                }
            case D:
                {
                    rStart = this.matrix_.length/2;
                    cStart = this.matrix_.length/2;
                    rFin = this.matrix_.length;
                    cFin = this.matrix_.length;
                    break;
                }
        }
        double[][] tmp = new double [this.matrix_.length/2] [this.matrix_.length/2];
        for (int i = rStart;i<rFin;i++)
        {
            for (int j=cStart;j<cFin;j++)
            {
                tmp[i-rStart][j-cStart] = this.matrix_[i][j];
            }
        }
        Matrix res = null;
        try
        {
            res = new Matrix(tmp);
        }
        catch (MatrixWrongDimentionsExcpetion e)
        {
            throw new UnsupportedOperationException(e);
        }
        return res;
    }
}
