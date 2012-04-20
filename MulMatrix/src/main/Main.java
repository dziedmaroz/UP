/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package main;
import mulmatrix.*;



/**
 *
 * @author lucian
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        final int matrSize = 16;
        double [][] A = new double [matrSize][matrSize];
        double [][] B = new double [matrSize][matrSize];
        double [][] C = new double [matrSize][matrSize];

        for (int i=0;i<matrSize;i++)
        {
            for (int j=0;j<matrSize;j++)
            {
                if (i==j)
                {
                    A[i][j]=1;
                }
                B[i][j]=i+j;
                System.out.printf ("%8.2f",B[i][j]);
            }
            System.out.println();
        }
        Matrix matrA = new Matrix(A);
        Matrix matrB = new Matrix(B);
        Matrix matrC = null;
        try
        {
            matrC = matrA.mul(matrB);
            for (int i=0;i<matrSize;i++)
            {
                for (int j=0;j<matrSize;j++)
                {
                    System.out.printf("%8.2f", matrC.cell(i, j));
                }
                System.out.println();
            }
        }
        catch (MatrixWrongDimentionException e)
        {
            System.out.println(e);
        }
        
       
    }

}
