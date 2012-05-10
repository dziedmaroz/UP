
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import server.BruteServer;
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        String hash = "9865d483bc5a94f2e30056fc256ed3066af54d0";
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
