package Week9;

/**
   A class that represents an update packet holding a distance vector
   for the given source
   @see UnicastRouter.java
*/
import java.net.InetAddress;

@SuppressWarnings("serial")
public class UnicastUpdatePacket implements UnicastPacket
{
   private InetAddress source, destination;
   private RouterDistanceVector distanceVector;
   
   public UnicastUpdatePacket(InetAddress source,
      InetAddress destination, RouterDistanceVector distanceVector)
   {  this.source = source;
      this.destination = destination;
      this.distanceVector = distanceVector;
   }
   
   public InetAddress getSource()
   {  return source;
   }
   
   public InetAddress getDestination()
   {  return destination;
   }
   
   public RouterDistanceVector getDistanceVector()
   {  return distanceVector;
   }
   
   public String toString()
   {  return "Update packet from " + source + " to " + destination;
   }
}
