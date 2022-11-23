package Week9;

/**
   A class that represents a connection request from a new
   adjacent unicast router at an estimated distance
   @see UnicastRouter.java
*/
import java.net.InetAddress;

@SuppressWarnings("serial")
public class UnicastConnectPacket implements UnicastPacket
{
   private InetAddress source, destination;
   private double distance;
   
   public UnicastConnectPacket(InetAddress source,
      InetAddress destination, double distance)
   {  this.source = source;
      this.destination = destination;
      this.distance = distance;
   }
   
   public InetAddress getSource()
   {  return source;
   }
   
   public InetAddress getDestination()
   {  return destination;
   }
   
   public double getDistance()
   {  return distance;
   }
   
   public String toString()
   {  return "Connect packet from " + source + " to " + destination;
   }
}
