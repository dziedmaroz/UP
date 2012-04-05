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
        double [][] A = new double [16][16];
        double [][] B = new double [16][16];
        double [][] C = new double [16][16];

        for (int i=0;i<16;i++)
        {
            for (int j=0;j<16;j++)
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
        MulMatrix mul = new MulMatrix(A, B, C, 0, 0, 16);
        mul.mul();
        for (int i=0;i<16;i++)
        {
            for (int j=0;j<16;j++)
            {
                System.out.printf("%8.2f", B[i][j]);
            }
            System.out.println();
        }
    }

}
