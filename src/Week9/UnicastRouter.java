package Week9;

/**
   A class that represents a router for performing unicast routing
   across a network built from unicast routers
   @author Andrew Ensor
*/

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Scanner; // Java 1.5 equivalent of cs1.Keyboard
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class UnicastRouter implements Runnable
{
   private int routerPort; // port on local machine used by server
   private InetAddress routerAddress; // IP address on local machine
   private ServerSocket serverSocket;
   private boolean stopRequested;
   private RouterDistanceVector distanceVector;
   private QueueADT<UnicastPacket> packetQueue; // packets to process
   private Thread processingThread; // handles the queue processing
   private Map<InetAddress, RouterConnection> adjacentRouters; //synch
   private RouterDisplay routerDisplay; // display for router output
   
   public UnicastRouter(int routerPort)
   {  this.routerPort = routerPort;
      // create the server socket
      serverSocket = null;
      try
      {  routerAddress = InetAddress.getLocalHost();
         serverSocket = new ServerSocket(routerPort);
         serverSocket.setSoTimeout(200); // timeout for socket accept
      }
      catch (UnknownHostException e)
      {  displayError("Can't get local host: " + e);
         System.exit(-1);
      }
      catch (IOException e)
      {  displayError("Server can't listen on port: " + e);
         System.exit(-1);
      }
      stopRequested = false;
      // create the distance vector, packet queue, and adjacent routers
      distanceVector = new RouterDistanceVector(routerAddress);
      packetQueue = new LinkedQueue<UnicastPacket>();
      processingThread = new Thread(new PacketProcessor());
      adjacentRouters 
         = new ConcurrentHashMap<InetAddress, RouterConnection>();
   }
   
   // continually listens for connection requests from new adjacent
   // routers
   public void run()
   {  displayText("Router started at " + routerAddress +
         " on port " + routerPort);
      displayAdjacentRouters();
      displayNetwork();
      processingThread.start();
      while (!stopRequested)
      {  try
         {  // block until the next client requests a connection
            // or the server timeout is reached
            Socket socket = serverSocket.accept();
            InetAddress newRouter = socket.getInetAddress();
            displayText("Connection made with " + newRouter);
            RouterConnection connection
               = new RouterConnection(this, socket);
            // add the connection to the adjacentRouter map
            adjacentRouters.put(newRouter, connection);
            displayText("Connection made with " + newRouter);
            displayAdjacentRouters();
            // note that newRouter is not added to distanceVector
            // until UnicastConnectPacket received from newRouter
            // since we don't yet know the distance from newRouter
            Thread.sleep(50); // give other threads a chance
         }
         catch (SocketTimeoutException e)
         {} // ignore the timeout and pass around while loop again
         catch (InterruptedException e)
         {} // ignore the interruption
         catch (IOException e)
         {  displayError("Can't accept client connection: "+e);
            stopRequested = true;
         }
      }
      displayText("Router finishing");
      try
      {  serverSocket.close();
      }
      catch (IOException e)
      {  displayError("Can't close server: " + e);
      }
   }
   
   public void requestStop()
   {  // send a UnicastDisconnectPacket to all other routers on network
      for (InetAddress knownRouter : distanceVector.getAllRouters())
      {  UnicastDisconnectPacket packet = new UnicastDisconnectPacket
            (routerAddress, knownRouter);
         processPacket(packet);
      }
      // obtain monitor for queue in case another thread is
      // currently mofiying the queue
      synchronized(packetQueue) // code block synchronized on queue
      {  stopRequested = true;
         packetQueue.notifyAll(); // notify waiting packet processor
      }
      // wait for the PacketProcessor thread to die which indicates
      // that it has finished sending the disconnect packets
      // and its queue is empty
      try
      {  processingThread.join();
      }
      catch (InterruptedException e)
      {} // don't bother waiting any longer for it to die
      // terminate all the router connections
      for (InetAddress adjacentRouter : adjacentRouters.keySet())
      {  RouterConnection connection = adjacentRouters.get(adjacentRouter);
         connection.requestDisconnect();
      }
      adjacentRouters.clear();
      displayAdjacentRouters();
   }
   
   // connect to the specified IP address and port given the distance
   // between this router and newRouter
   public void connectToIP(String ipAddress, int port,
      double distance)
      throws IOException
   {  // make a connection to newRouter on specified port
      Socket socket = new Socket(ipAddress, port);
      RouterConnection connection = new RouterConnection(this, socket);
      connection.setDistance(distance);
      InetAddress newRouter = connection.getAdjacentRouter();
      displayText("Connection made with " + newRouter);
      // add the connection to the adjacentRouter map
      adjacentRouters.put(newRouter, connection);
      displayAdjacentRouters();
      // update the distance vector for newRouter
      distanceVector.addAdjacentRouter(newRouter, distance);
      displayNetwork();
      // send a connect package with distance to newRouter
      // so it is aware of the distance from this router
      UnicastConnectPacket connectPacket
         = new UnicastConnectPacket(routerAddress, newRouter, distance);
      processPacket(connectPacket);
   }
   
   public void processPacket(UnicastPacket packet)
   {  if (!stopRequested)
      {  synchronized(packetQueue) // code block synchronized on queue
         {  packetQueue.enqueue(packet);
            // notify waiting packet processor of new packet
            packetQueue.notifyAll();
         }
      }
      else // trying to stop router so ignore the packet
         displayText("Packet ignored from " + packet.getSource());
   }
   
   public void setDisplay(RouterDisplay routerDisplay)
   {  this.routerDisplay = routerDisplay;
   }
   
   public void displayText(String text)
   {  if (routerDisplay != null)
         routerDisplay.displayText(text+"\n");
      else // default to System.out
         System.out.println(text);
   }
   
   public void displayError(String error)
   {  System.err.println(error);
   }
   
   public void displayNetwork()
   {  if (routerDisplay != null)
         routerDisplay.displayNetwork(distanceVector);
   }
   
   public void displayAdjacentRouters()
   {  if (routerDisplay != null)
         routerDisplay.displayAdjacentRouters(adjacentRouters);
   }
   
   public int getRouterPort()
   {  return routerPort;
   }
   
   public InetAddress getRouterAddress()
   {  return routerAddress;
   }
   
   public RouterDistanceVector getDistanceVector()
   {  return distanceVector;
   }
   
   // inner class that processes the queue of packets
   private class PacketProcessor implements Runnable
   {
      // repeatedly tries to process the elements in the queue
      // note that this method continues even once stopRequested
      // until queue is empty
      public void run()
      {  while (!(stopRequested && packetQueue.isEmpty()))
            processQueue();
      }
      
      // wait until there is a packet at front of queue
      public void processQueue()
      {  UnicastPacket packet = null;
         synchronized(packetQueue) // code block synchronized on queue
         {  while (!stopRequested && packetQueue.isEmpty())
            {  try
               {   //wait for notification that queue not empty
                  packetQueue.wait();
                  Thread.sleep(50); // give other threads a chance
               }
               catch (InterruptedException e)
               {} // ignore interruption
            }
            if (!packetQueue.isEmpty())
               packet = packetQueue.dequeue();
         }
         if (packet != null)
            process(packet);
      }
      
      private void process(UnicastPacket packet)
      {  InetAddress source = packet.getSource();
         InetAddress destination = packet.getDestination();
         // check whether packet should just be forwarded
         if (!destination.equals(routerAddress))
         {  // forward packet via shortest distance router
            InetAddress nextRouter
               = distanceVector.getNextRouterToDestination(destination);
            RouterConnection connection = null;
            if (nextRouter != null)
               connection = adjacentRouters.get(nextRouter);
            if (connection == null)
            {  // send back an undeliverable message
               UnicastMessagePacket returnPacket
                  = new UnicastMessagePacket(routerAddress, source,
                  "Unable to deliver packet to " + destination);
               processPacket(returnPacket);
            }
            else
            {  connection.forward(packet);
               displayText("Forwarding packet from " + source + " to "
                  + destination);
            }
         }
         // else this router is the intended destination of the packet
         else if (packet instanceof UnicastMessagePacket)
         {  // output the message
            displayText("Message received from " + source + ":\n" +
               ((UnicastMessagePacket)packet).getMessage());
         }
         else if (packet instanceof UnicastUpdatePacket)
         {  distanceVector.relaxDistanceVector(
               ((UnicastUpdatePacket)packet).getDistanceVector());
            displayText("Update received from " + source);
            displayNetwork();
         }
         else if (packet instanceof UnicastDisconnectPacket)
         {  // check whether this disconnection is from an adjacent router
            RouterConnection currentConnection
               = adjacentRouters.get(source);
            if (currentConnection != null)
            {  currentConnection.requestDisconnect();
               adjacentRouters.remove(source);
               displayAdjacentRouters();
            }
            // update the distance vector
            distanceVector.removeRouter(source);
            // re-add the adjacent routers to distance vector in case
            // some were removed from the distance vector
            for (RouterConnection connection : adjacentRouters.values())
            {  distanceVector.addAdjacentRouter(connection.getAdjacentRouter(),
                  connection.getDistance());
            }
            displayText("Disconnect received from " + source);
            displayNetwork();
         }
         else if (packet instanceof UnicastConnectPacket)
         {  double distance = ((UnicastConnectPacket)packet).getDistance();
            // check whether this connection is from an adjacent router
            RouterConnection connection = adjacentRouters.get(source);
            if (connection != null)
            {  // update the distance for this connection
               connection.setDistance(distance);
               displayAdjacentRouters();
               // update the distance vector
               distanceVector.removeRouter(source);
               distanceVector.addAdjacentRouter(source, distance);
               displayText("Connect received from " + source +
                  " with distance " + distance);
               displayNetwork();
            }
            else
               displayText("Received connect packet from " + source
                  + " but not connected to this router");
         }
         else
            throw new IllegalStateException("Unknown packet type");
      }
   }

   public static void main(String[] args)
   {  // get the port number for the router server from the keyboard
      @SuppressWarnings("resource")
	  Scanner in = new Scanner(System.in);
      System.out.print("Please enter port for this router server: ");
      int port = in.nextInt();
      final UnicastRouter router = new UnicastRouter(port);
      // prepare the display for the router
      RouterControlPanel routerPanel = new RouterControlPanel(router);
      router.setDisplay(routerPanel);
      // prepare a frame in which to place the router display
      JFrame frame = new JFrame("Unicast Router");
      frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      frame.getContentPane().add(routerPanel);
      frame.pack();
      frame.addWindowListener(new WindowAdapter()
         {  public void windowClosing(WindowEvent e)
            {  router.requestStop();
            }
         });
      // position the frame in the middle of the screen
      Toolkit tk = Toolkit.getDefaultToolkit();
      Dimension screenDimension = tk.getScreenSize();
      Dimension frameDimension = frame.getSize();
      frame.setLocation((screenDimension.width-frameDimension.width)/2,
         (screenDimension.height-frameDimension.height)/2);
      frame.setVisible(true);
      frame.toFront(); // make this frame the front most frame
      routerPanel.requestFocusInWindow();
      // run this router in the main thread
      router.run();
   }
}
