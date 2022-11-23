package Week9;

/**
   A class that solves the maximum-flow problem by the Edmonds-Karp
   algorithm for a network whose capacities are specified by a square
   array (presumed to hold non-negative values)
   @author Andrew Ensor
*/
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MaxFlowEdmondsKarp
{
   @SuppressWarnings("unused")
   private int[][] capacities;
   private int n; // number of vertices in network
   @SuppressWarnings("unused")
   private int source, sink;
   private int[][] flow; // holds the maximum flow
   
   public MaxFlowEdmondsKarp(int[][] capacities, int source, int sink)
   {  this.capacities = capacities;
      this.n = capacities.length;
      this.source = source;
      this.sink = sink;
      // create an initial flow with value 0
      flow = new int[n][n];
      // create the residual network
      int[][] residualNetwork = new int[n][n];
      for (int i=0; i<n; i++)
      {  if (capacities[i].length < n)
            throw new IllegalArgumentException
               ("Capacity must not be a ragged array");
         for (int j=0; j<n; j++)
            residualNetwork[i][j] = capacities[i][j];
      }
      boolean maxFlow = false; // maximum flow not yet found
      while (!maxFlow)
      {  // use a breadth first search to find next augmenting path
         int[] pathVertices = getShortestPath(residualNetwork,
            source, sink);
         if (pathVertices == null) // no more augmenting paths
            maxFlow = true;
         else
         {  
            // find greatest possible flow for this augmenting path
            int augmentCapacity
               = residualNetwork[source][pathVertices[1]];
            for (int i=2; i<pathVertices.length; i++)
            {  augmentCapacity = Math.min(augmentCapacity,
                  residualNetwork[pathVertices[i-1]][pathVertices[i]]);
            }
            // update the flow and the residual network
            for (int i=1; i<pathVertices.length; i++)
            {  int start = pathVertices[i-1];
               int end = pathVertices[i];
               flow[start][end] += augmentCapacity;
               flow[end][start] -= augmentCapacity;
               residualNetwork[start][end] -= augmentCapacity;
               residualNetwork[end][start] += augmentCapacity;
            }
         }
      }
   }
   
   // helper method that uses a breadth-first search to find shortest
   // length path in residual network from source to sink
   private int[] getShortestPath(int[][] network, int source, int sink)
   {  int n = network.length;
      Map<Integer, Integer> parentVertices
         = new HashMap<Integer, Integer>(); //holds parents for search
      List<Integer> visitedVertices = new ArrayList<Integer>();
      QueueADT<Integer> processingQueue = new LinkedQueue<Integer>();
      // handle the source vertex
      parentVertices.put(source, null); // root has no parent
      visitedVertices.add(source);
      processingQueue.enqueue(source);
      boolean sinkFound = false;
      while (!sinkFound && !processingQueue.isEmpty())
      {  int frontVertex = processingQueue.dequeue();
         // find all adjacent vertices that have not been visited and
         // enqueue them
         for (int i=0; i<n; i++)
         {  if (network[frontVertex][i]>0 &&
               !visitedVertices.contains(i))
            {  // visit the vertex i
               parentVertices.put(i, frontVertex);
               visitedVertices.add(i);
               processingQueue.enqueue(i);
               if (i == sink)
                  sinkFound = true;
            }
         }
      }
      if (sinkFound)
      {  // determine the path that was found from parentVertices
         List<Integer> reversePath = new ArrayList<Integer>();
         int currentVertex = sink;
         while (currentVertex != source)
         {  reversePath.add(currentVertex);
            currentVertex = parentVertices.get(currentVertex);
         }
         reversePath.add(source);
         // transfer the vertices in path to an int[] array
         int numVertices = reversePath.size();
         int[] path = new int[numVertices];
         for (int i=0; i<numVertices; i++)
         {   path[i] = reversePath.get(numVertices-1-i);
         }
         return path;
      }
      else
         return null; // no path found
   }
   
   // returns a string representation of flow and its value
   public String toString()
   {  String output = "Maximum Flow\n";
      for (int i=0; i<n; i++)
      {  for (int j=0; j<n; j++)
            output += "\t" + flow[i][j];
         output += "\n";
      }
      // calculate the value of the flow
      int value = 0;
      for (int i=0; i<n; i++)
         value += flow[source][i];
      output += "Value is " + value + "\n";
      return output;
   }
   
   public static void main(String[] args)
   {  // test the flow network given in the manual
      int[][] capacities = {
         {0, 2, 15, 0, 0, 0}, // capacity from node a
         {0, 0, 9, 11, 5, 0}, // capacity from node b
         {0, 1, 0, 3, 6, 0}, // capacity from node c
         {0, 0, 0, 0, 5, 2}, // capacity from node d
         {0, 0, 2, 0, 0, 7}, // capacity from node e
         {0, 0, 0, 1, 0, 0}}; // capacity from node f
      MaxFlowEdmondsKarp maxFlow
         = new MaxFlowEdmondsKarp(capacities, 0, 5);
      System.out.println(maxFlow);
   }
}
