package Week9;

/**
   A class that represents a connection between two adjacent routers
   in a network
   @see UnicastRouter.java
*/
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class RouterConnection implements Runnable
{
   private UnicastRouter router; // router with this end of connection
   private Socket socket;
   private ObjectOutputStream oos;
   private ObjectInputStream ois;
   private Double distance;
   private boolean disconnectRequest;
   
   // create a connection with an initially unknown distance
   public RouterConnection(UnicastRouter router, Socket socket)
   {  this.router = router;
      this.socket = socket;
      disconnectRequest = false;
      try
      {  socket.setSoTimeout(200); // timeout for read
         oos = new ObjectOutputStream(socket.getOutputStream());
         ois = new ObjectInputStream(socket.getInputStream());
      }
      catch (SocketException e)
      {  router.displayError("Unable to set socket timeout: " + e);
         disconnectRequest = true;
      }
      catch (IOException e)
      {  router.displayError("Unable to create streams: " + e);
         disconnectRequest = true;
      }
      distance = Double.NaN; // distance initially unknown
      // start the new router connection in its own thread
      Thread thread = new Thread(this);
      thread.start();
   }
   
   // continually listens for incoming packets on the socket
   public void run()
   {  while (!disconnectRequest)
      {  try
         {  // block until input is received on the input stream
            // or else the socket timeout is reached
            UnicastPacket packet = (UnicastPacket)ois.readObject();
            router.processPacket(packet);
            Thread.sleep(50); // give other threads a chance
         }
         catch (SocketTimeoutException e)
         {} // ignore the timeout and pass around while loop again
         catch (InterruptedException e)
         {} // ignore the interruption
         catch (ClassNotFoundException e)
         {  router.displayError("Error with object input: " + e);
         }
         catch (IOException e)
         {  router.displayError("Error while reading on socket: " + e);
            disconnectRequest = true;
         }
      }
      // clean up the stream resources
      try
      {  oos.close();
         ois.close();
         socket.close();
      }
      catch (IOException e)
      {  router.displayError("Unable to close streams: " + e);
      }
   }
   
   // forwards the packet to the router at other end of this connection
   public void forward(UnicastPacket packet)
   {  if (disconnectRequest)
         router.displayText("Connection already closing so packet ignored");
      else
      {  try
         {  oos.writeObject(packet);
            oos.flush();
         }
         catch (IOException e)
         {  router.displayError("Error while writing on socket: " + e);
         }
      }
   }
   
   // returns the distance (weight) for this connection
   public double getDistance()
   {  return distance;
   }
   
   public void setDistance(double distance)
   {  this.distance = distance;
   }
   
   // returns the adjacent router
   public InetAddress getAdjacentRouter()
   {  return socket.getInetAddress();
   }
   
   public void requestDisconnect()
   {  disconnectRequest = true;
   }
   
   // returns a string representation of the connection
   public String toString()
   {  return socket.getInetAddress() + " (port " + socket.getPort()
         + ")";
   }
}
