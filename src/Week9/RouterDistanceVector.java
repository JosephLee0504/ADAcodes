package Week9;

/**
   A class that represents a distance vector held by a unicast router
   Note that the maps used in this class have been synchronized
   and so are suitable for concurrent thread usage
   @see UnicastRouter.java
*/
import java.io.Serializable;
import java.net.InetAddress;
import java.util.Set;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings("serial")
public class RouterDistanceVector implements Serializable
{
   private InetAddress source;
   private Map<InetAddress,Double> distanceVector; //shortest distance
   private Map<InetAddress,InetAddress> pathVector;//shortest direction
   
   public RouterDistanceVector(InetAddress source)
   {  this.source = source;
      distanceVector = new LinkedHashMap<InetAddress, Double>();
      pathVector = new LinkedHashMap<InetAddress, InetAddress>();
      distanceVector.put(source, 0.0);
      pathVector.put(source, source); // path of length 0
   }
   
   // adds or updates a router as an adjacent router in the network
   // unless it already has a shorter path
   public synchronized void addAdjacentRouter(InetAddress router,
      double distance)
   {  Double shortestDistance = distanceVector.get(router);
      if (shortestDistance == null || 
         shortestDistance.doubleValue() > distance)
      {  distanceVector.put(router, distance);
         pathVector.put(router, router);
      }
   }
   
   // handles updating distance vector when an adjacent router
   // has been disconnected
   public synchronized void removeRouter(InetAddress router)
   {  distanceVector.remove(router);
      pathVector.remove(router);
      // remove any network paths that are via this router
      for (InetAddress destinationRouter : distanceVector.keySet())
      {  InetAddress shortestRouter=pathVector.get(destinationRouter);
         if (shortestRouter.equals(router))
         {  distanceVector.remove(destinationRouter);
            pathVector.remove(destinationRouter);
         }
      }
   }
   
   // returns the distance to the given destination or
   // Double.POSITIVE_INFINITY if not known
   public synchronized double getDistanceToDestination
      (InetAddress destination)
   {  Double distance = distanceVector.get(destination);
      if (distance == null) // no known path to the destination
         return Double.POSITIVE_INFINITY;
      else
         return distance.doubleValue();
   }
   
   // returns next router in shortest path to destination or null if
   // the destination is unknown
   public synchronized InetAddress getNextRouterToDestination
      (InetAddress destination)
   {  return pathVector.get(destination);
   }
   
   // relaxes the edges in this distance vector based on those in
   // the parameter
   public synchronized void relaxDistanceVector
      (RouterDistanceVector rdv)
   {  // find distance of shortest path to the other router
      Double distanceToRouter = distanceVector.get(rdv.source);
      if (distanceToRouter != null)
      {  // check every path of the other router
         for (InetAddress destinationRouter : 
            rdv.distanceVector.keySet())
         {  // check whether to relax distance to router
            double distanceViaRouter = distanceToRouter.doubleValue() 
               + rdv.distanceVector.get(destinationRouter);
            Double originalDistance
               = distanceVector.get(destinationRouter);
            if (originalDistance == null ||
               distanceViaRouter < originalDistance.doubleValue())
            {  // relax distance to router
               distanceVector.put(destinationRouter,
                  distanceViaRouter);
               pathVector.put(destinationRouter,
                  pathVector.get(rdv.source));
            }
         }
      }
      // else no path known from source to rdv.source
   }
   
   // returns an unmodifiable set of all the known routers on network
   public synchronized Set<InetAddress> getAllRouters()
   {  return Collections.unmodifiableSet(distanceVector.keySet());
   }
}
