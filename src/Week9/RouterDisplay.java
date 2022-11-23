package Week9;

/**
   An interface that represents a display for the UnicastRouter
   @see UnicastRouter.java
*/
import java.net.InetAddress;
import java.util.Map;

public interface RouterDisplay
{
   // notification to display the string text
   public void displayText(String text);
   // notification to display updated adjacent routers
   public void displayAdjacentRouters
      (Map<InetAddress, RouterConnection> adjacentRouters);
   // notification to display updated network routers
   public void displayNetwork(RouterDistanceVector distanceVector);
}
