/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package client;

import java.io.IOException;

public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        BruteClient bClient;
        try
        {
            bClient = new BruteClient("localhost",8080);
            bClient.run();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}