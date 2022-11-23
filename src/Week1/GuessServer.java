/**
   A class that represents a server in a number guessing game where
   GuessClient objects connect to this GuessServer and try to guess
   a random integer value between min (incl) and max (excl)
   The game initiates with a response from the server and ends when
   the server responds with "Correct guess!"
   @author Andrew Ensor
*/
package Week1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class GuessServer
{
   private int min, max; // minimum (incl) and maximum (excl) range
   private boolean stopRequested;
   private Random generator;
   public static final int PORT = 7777; // some unused port number
   
   public GuessServer(int min, int max)
   {  this.min = min;
      this.max = max;
      stopRequested = false;
      generator = new Random();
   }
   
   // start the server if not already started and repeatedly listen
   // for client connections until stop requested
   public void startServer()
   {  stopRequested = false;
      ServerSocket serverSocket = null;
      try
      {  serverSocket = new ServerSocket(PORT);
         System.out.println("Server started at "
            + InetAddress.getLocalHost() + " on port " + PORT);
      }
      catch (IOException e)
      {  System.err.println("Server can't listen on port: " + e);
         System.exit(-1);
      }
      try
      {  while (!stopRequested)
         {  // block until the next client requests a connection
            // note that the server socket could set an accept timeout
            Socket socket = serverSocket.accept();
            System.out.println("Connection made with "
               + socket.getInetAddress());
            // start a game with this connection, note that a server
            // might typically keep a reference to each game
            GuessGame game = new GuessGame(socket,
               generator.nextInt(max-min)+min);
            Thread thread = new Thread(game);
            thread.start();
         }
         serverSocket.close();
      }
      catch (IOException e)
      {  System.err.println("Can't accept client connection: " + e);
      }
      System.out.println("Server finishing");
   }
   
   // stops server AFTER the next client connection has been made
   // (since this server socket doesn't timeout on client connections)
   public void requestStop()
   {  stopRequested = true;
   }
   
   // driver main method to test the class
   public static void main(String[] args)
   {  GuessServer server = new GuessServer(1, 100);
      server.startServer();
   }
   
   // inner class that represents a single game played across a socket
   private class GuessGame implements Runnable
   {
      private int value; // value to guess
      private Socket socket; // socket for client/server communication
      
      // constructor for a guess game to guess value across a socket
      // for client/server communication 
      public GuessGame(Socket socket, int value)
      {  this.value = value;
         this.socket = socket;
      }
      
      public void run()
      {  PrintWriter pw; // output stream to client
         BufferedReader br; // input stream from client
         try
         {  // create an autoflush output stream for the socket
            pw = new PrintWriter(socket.getOutputStream(), true);
            // create a buffered input stream for this socket
            br = new BufferedReader(new InputStreamReader(
               socket.getInputStream()));
            // play the game until value is correctly guessed
            pw.println("Guess the number between " + min + " and "
               + (max-1) + " inclusive");
            int guess = min-1;
            do
            {  String clientGuess = br.readLine();
               String response;
               if (clientGuess == null)
                  response = "Nothing entered, try again";
               else
               {  try
                  {  guess = Integer.parseInt(clientGuess);
                     if (guess < value)
                        response = "Guess too low, try again";
                     else if (guess > value)
                        response = "Guess too high, try again";
                     else
                        response = "Correct guess!";
                  }
                  catch (NumberFormatException e)
                  {  response = "Not an int value, try again";
                  }
               }
               pw.println(response);
            }
            while (guess!=value);
            pw.close();
            br.close();
            System.out.println("Closing connection with "
               + socket.getInetAddress());
            socket.close();
            }
         catch (IOException e)
         {  System.err.println("Server error with game: " + e);
         }
      }
   }
}
