package Week9;

/**
   An interface that represents a packet of information that can be
   sent between unicast routers in a network
   @see UnicastRouter.java
*/
import java.net.InetAddress;
import java.io.Serializable;

public interface UnicastPacket extends Serializable
{
   public InetAddress getSource();
   public InetAddress getDestination();
}
