/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mulmatrix;

/**
 *
 * @author lucian
 */

class MatrixBlock {

    double [][] matrix;
    int blockStartX;
    int blockStartY;
    int blockSize;

    public MatrixBlock(double[][] matrix, int blockStartX, int blockStartY, int blockSize)
    {
        this.matrix = matrix;
        this.blockStartX = blockStartX;
        this.blockStartY = blockStartY;
        this.blockSize = blockSize;
    }
}
