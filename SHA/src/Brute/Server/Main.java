package Brute.Server;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {     
        try
        {
          BruteServer bruteServer = new BruteServer ();
          bruteServer.run();
          while (true)
          {
             System.out.print(">");
             BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
             String command = br.readLine();
             if (command.equals("die")) bruteServer.shutdownServer();
          }
        }
        catch (IOException e)
        {
            System.out.println(e);
        }
        

    }

}
