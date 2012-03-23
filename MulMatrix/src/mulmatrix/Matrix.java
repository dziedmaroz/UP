/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mulmatrix;

/**
 *
 * @author lucian
 */
public class Matrix
{
    private double [] [] matrix_ ;

    public int rowCount()
    {
        return matrix_.length;
    }

    public int columnCount()
    {
        return matrix_[0].length;
    }



}
