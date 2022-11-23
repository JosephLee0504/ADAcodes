package Week9;

/**
   A class which contains Prim's algorithm for finding a minimal
   spanning tree in an undirected and connected weighted graph that
   implements GraphADT interface and whose vertices hold elements of
   generic type E that has suitable hashing function
   @author Andrew Ensor
*/
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class MinimalSpanningTreePrim<E>
{
   private GraphADT<E> graph;
   private Map<Edge<E>,Integer> weights;
   
   public MinimalSpanningTreePrim(GraphADT<E> graph,
      Map<Edge<E>,Integer> weights)
   {  if (graph.getType()!=GraphADT.GraphType.UNDIRECTED)
         throw new IllegalArgumentException("Graph not undirected");
      this.graph = graph;
      this.weights = weights;
   }
   
   public Set<Edge<E>> getMinimalSpanningTree(Vertex<E> root)
   {  // create a min priority queue to hold each vertex
      Comparator<LeastCrossEdge<E>> comparator
         = new Comparator<LeastCrossEdge<E>>()
         {  public int compare(LeastCrossEdge<E> u,
               LeastCrossEdge<E> v)
            {  return u.getWeight()-v.getWeight();
            }
         };
      PriorityQueue<LeastCrossEdge<E>> queue
         = new PriorityQueue<LeastCrossEdge<E>>
         (graph.vertexSet().size(), comparator);
      for (Vertex<E> vertex : graph.vertexSet())
      {  if (vertex!=root)
            queue.add(new LeastCrossEdge<E>(vertex));
      }
      // start building the minimal spanning tree
      Set<Edge<E>> mst = new HashSet<Edge<E>>();
      Vertex<E> addedVertex = root;
      // process each element of the queue
      while (queue.size()>0)
      {  // update the least cross edges for all the adjacent vertices
         for (Edge<E> edge : addedVertex.incidentEdges())
         {  Vertex<E> adjacentVertex=edge.oppositeVertex(addedVertex);
            // find whether the adjacent vertex is in queue
            Iterator<LeastCrossEdge<E>> iterator = queue.iterator();
            LeastCrossEdge<E> lce = null;
            boolean found = false; 
            while (!found && iterator.hasNext())
            {  lce = iterator.next();
               found = (lce.vertex == adjacentVertex);
            }
            if (found && lce.getWeight()>weights.get(edge))
            {  // remove lce from queue so gets resorted after change
               iterator.remove();
               lce.edge = edge;
               queue.add(lce);
            }
         }
         // add the smallest least cross edge to minimal spanning tree
         LeastCrossEdge<E> lce = queue.poll();
         if (lce.edge==null)
            throw new NullPointerException("No edge in cross edge");
         mst.add(lce.edge);
         addedVertex = lce.vertex;
      }
      return mst;
   }
   
   // inner class that holds information about the least weight
   // cross edge (an edge that joins A with a vertex not in A)
   // to the specified vertex
   @SuppressWarnings("hiding")
   private class LeastCrossEdge<E>
   {
      public Vertex<E> vertex;
      public Edge<E> edge; // null if not a cross edge
      
      public LeastCrossEdge(Vertex<E> vertex)
      {  this.vertex = vertex;
         this.edge = null;
      }
      
      public int getWeight()
      {  if (edge!=null)
            return weights.get(edge);
         else
            return Integer.MAX_VALUE;
      }
   }

   public static void main(String[] args)
   {  GraphADT<String> graph = new AdjacencyListGraph<String>
         (GraphADT.GraphType.UNDIRECTED);
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
      Edge<String> cd = graph.addEdge(c, d);
      Edge<String> ce = graph.addEdge(c, e);
      Edge<String> de = graph.addEdge(d, e);
      Edge<String> df = graph.addEdge(d, f);
      Edge<String> ef = graph.addEdge(e, f);
      Map<Edge<String>,Integer> weights = new HashMap<Edge<String>,Integer>();
      weights.put(ab, 2);
      weights.put(ac, 15);
      weights.put(bc, 9);
      weights.put(bd, 5);
      weights.put(be, 10);
      weights.put(cd, 3);
      weights.put(ce, 6);
      weights.put(de, 5);
      weights.put(df, 12);
      weights.put(ef, 6);
      System.out.println("Example Graph:\n" + graph);
      MinimalSpanningTreePrim<String> prim
         = new MinimalSpanningTreePrim<String>(graph, weights);
      System.out.println("Finding minimal spanning tree");
      System.out.print("Edges:");
      for (Edge<String> edge : prim.getMinimalSpanningTree(a))
         System.out.print(" "+edge);
      System.out.println();
   }
}
