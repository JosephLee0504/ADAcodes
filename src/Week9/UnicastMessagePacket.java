package Week9;

/**
   A class that represents a string message packet
   @see UnicastRouter.java
*/
import java.net.InetAddress;

@SuppressWarnings("serial")
public class UnicastMessagePacket implements UnicastPacket
{
   private InetAddress source, destination;
   private String message;
   
   public UnicastMessagePacket(InetAddress source,
      InetAddress destination, String message)
   {  this.source = source;
      this.destination = destination;
      this.message = message;
   }
   
   public InetAddress getSource()
   {  return source;
   }
   
   public InetAddress getDestination()
   {  return destination;
   }
   
   public String getMessage()
   {  return message;
   }

   public String toString()
   {  return "Message packet from " + source + " to " + destination;
   }
}
