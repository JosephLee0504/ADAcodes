package Week9;

/**
   A class that represents a disconnection request from an
   adjacent unicast router
   @see UnicastRouter.java
*/
import java.net.InetAddress;

@SuppressWarnings("serial")
public class UnicastDisconnectPacket implements UnicastPacket
{
   private InetAddress source, destination;
   
   public UnicastDisconnectPacket(InetAddress source,
      InetAddress destination)
   {  this.source = source;
      this.destination = destination;
   }
   
   public InetAddress getSource()
   {  return source;
   }
   
   public InetAddress getDestination()
   {  return destination;
   }
   
   public String toString()
   {  return "Disconnect packet from " + source + " to " + destination;
   }
}
