/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sha;



/**
 *
 * @author lucian
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        String hash = "3c01bdbb26f358bab27f267924aa2c9a03fcfdb8";
        SHAWorker worker = new SHAWorker(new SHAStep(0, 1000000), hash);
        try
        {
            worker.join ();
        }
        catch  (InterruptedException e)
        {
            System.out.println(e);
        }
        if (worker.isSuccses())
        {
            System.out.println(worker.getMsg());
        }
        else
        {
            System.out.println("FAIL!");
        }

    }

}
