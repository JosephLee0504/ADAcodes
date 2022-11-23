package Week9;

/**
   A class which contains Dijkstra's algorithm for solving the
   single-source shortest path problem in a directed or undirected
   weighted graph that implements GraphADT interface and whose
   vertices hold elements of generic type E that has suitable hashing
   function (note all edges presumed to have non-negative weight)
   @author Andrew Ensor
*/
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class ShortestPathDijkstra<E>
{
   private GraphADT<E> graph;
   private Map<Edge<E>,Integer> weights;
   
   public ShortestPathDijkstra(GraphADT<E> graph,
      Map<Edge<E>,Integer> weights)
   {  this.graph = graph;
      this.weights = weights;
   }
   
   @SuppressWarnings("deprecation")
   public Set<Edge<E>> getShortestPathsTree(Vertex<E> source)
   {  final Map<Vertex<E>,Integer> shortestPathEstimates
         = new HashMap<Vertex<E>,Integer>();
      Map<Vertex<E>,Edge<E>> leastEdges
         = new HashMap<Vertex<E>,Edge<E>>();
      // create a min priority queue to hold each vertex
      Comparator<Vertex<E>> comparator = new Comparator<Vertex<E>>()
         {  public int compare(Vertex<E> u, Vertex<E> v)
            {  return shortestPathEstimates.get(u)-
                  shortestPathEstimates.get(v);
            }
         };
      PriorityQueue<Vertex<E>> queue = new PriorityQueue<Vertex<E>>
         (graph.vertexSet().size(), comparator);
      for (Vertex<E> vertex : graph.vertexSet())
      {  leastEdges.put(vertex, null);
         if (vertex!=source)
         {  shortestPathEstimates.put(vertex, Integer.MAX_VALUE);
            queue.add(vertex);
         }
         else
            shortestPathEstimates.put(source, new Integer(0));
      }
      // create set to hold vertices whose final shortest paths known
      Set<Vertex<E>> knownSPVertices = new HashSet<Vertex<E>>();
      knownSPVertices.add(source);
      // start building the shortest paths tree
      Set<Edge<E>> spt = new HashSet<Edge<E>>();
      Vertex<E> addedVertex = source;
      // process each element of the queue
      while (queue.size()>0)
      {  // relax edges incident to addedVertex
         for (Edge<E> edge : addedVertex.incidentEdges())
         {  Vertex<E> v = edge.oppositeVertex(addedVertex);
            int newEstimate = shortestPathEstimates.get(addedVertex)
               + weights.get(edge);
            if (!knownSPVertices.contains(v) &&
               newEstimate < shortestPathEstimates.get(v))
            {  // find the adjacent vertex in the queue
               // note an iterator is used so queue can be modified
               Iterator<Vertex<E>> iterator = queue.iterator();
               boolean found = false; 
               while (!found && iterator.hasNext())
                  found = (iterator.next() == v);
               if (found) // should always be found
               {  // remove v from queue so gets resorted after change
                  iterator.remove();
                  shortestPathEstimates.put(v, newEstimate);
                  queue.add(v);
                  leastEdges.put(v, edge);
               }
            }
         }
         // priority queue now has vertex with smallest pe at head
         addedVertex = queue.poll();
         knownSPVertices.add(addedVertex);
         spt.add(leastEdges.get(addedVertex));
      }
      return spt;
   }

   public static void main(String[] args)
   {  GraphADT<String> graph = new AdjacencyListGraph<String>
         (GraphADT.GraphType.DIRECTED);
      Vertex<String> a = graph.addVertex("a");
      Vertex<String> b = graph.addVertex("b");
      Vertex<String> c = graph.addVertex("c");
      Vertex<String> d = graph.addVertex("d");
      Vertex<String> e = graph.addVertex("e");
      Vertex<String> f = graph.addVertex("f");
      Edge<String> ab = graph.addEdge(a, b);
      Edge<String> ac = graph.addEdge(a, c);
      Edge<String> bc = graph.addEdge(b, c);
      Edge<String> bd = graph.addEdge(b, d);
      Edge<String> be = graph.addEdge(b, e);
      Edge<String> cb = graph.addEdge(c, b);
      Edge<String> cd = graph.addEdge(c, d);
      Edge<String> ce = graph.addEdge(c, e);
      Edge<String> de = graph.addEdge(d, e);
      Edge<String> df = graph.addEdge(d, f);
      Edge<String> ec = graph.addEdge(e, c);
      Edge<String> ef = graph.addEdge(e, f);
      Edge<String> fd = graph.addEdge(f, d);
      Map<Edge<String>,Integer> weights = new HashMap<Edge<String>,Integer>();
      weights.put(ab, 2);
      weights.put(ac, 15);
      weights.put(bc, 9);
      weights.put(bd, 11);
      weights.put(be, 5);
      weights.put(cb, 1);
      weights.put(cd, 3);
      weights.put(ce, 6);
      weights.put(de, 5);
      weights.put(df, 2);
      weights.put(ec, 2);
      weights.put(ef, 7);
      weights.put(fd, 1);
      System.out.println("Example Graph:\n" + graph);
      ShortestPathDijkstra<String> dijkstra
         = new ShortestPathDijkstra<String>(graph, weights);
      System.out.println("Finding shortest paths tree");
      System.out.print("Edges:");
      for (Edge<String> edge : dijkstra.getShortestPathsTree(a))
         System.out.print(" "+edge);
      System.out.println();
   }
}
