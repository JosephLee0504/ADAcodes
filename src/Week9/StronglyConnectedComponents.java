package Week9;

/**
   A class which contains the strongly connected components algorithm
   for a graph that implements GraphADT interface and whose vertices
   hold elements of generic type E that has suitable hashing function
   @author Andrew Ensor
*/
import java.util.HashSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class StronglyConnectedComponents<E>
{
   protected GraphADT<E> graph;
   
   // creates new searcher which has performed no depth-first search
   public StronglyConnectedComponents(GraphADT<E> graph)
   {  this.graph = graph;
   }
   
   public Set<Set<Vertex<E>>> getComponentVertices()
   {  Set<Set<Vertex<E>>> components = new HashSet<Set<Vertex<E>>>();
      // first perform depth-first searches until all vertices visited
      // and record vertices in reverse order of when they finish
      CompleteDepthFirstSearch<E> cdfsSearcher
         = new CompleteDepthFirstSearch<E>(graph);
      Stack<Vertex<E>> vertexStack = cdfsSearcher.completeSearch();
      // create a new graph of same type that is the transpose of
      // graph so has same vertices but all edges reversed         
      GraphADT<E> graphTransposed
         = new AdjacencyListGraph<E>(graph.getType());
      Map<Vertex<E>,Vertex<E>> correspOtoT
         = new HashMap<Vertex<E>,Vertex<E>>();
      Map<Vertex<E>,Vertex<E>> correspTtoO
         = new HashMap<Vertex<E>,Vertex<E>>();
      for (Vertex<E> originalVertex : graph.vertexSet())
      {  Vertex<E> newVertex =
            graphTransposed.addVertex(originalVertex.getUserObject());
         // keep track of the correspondence between vertices in
         // graph and the new vertices added to graphTransposed
         correspOtoT.put(originalVertex, newVertex);
         correspTtoO.put(newVertex, originalVertex);
      }
      for (Edge<E> originalEdge : graph.edgeSet())
      {  Vertex<E>[] endVertices = originalEdge.endVertices();
         Vertex<E> startVertex = correspOtoT.get(endVertices[1]);
         Vertex<E> endVertex = correspOtoT.get(endVertices[0]);
         graphTransposed.addEdge(startVertex, endVertex);
      }
      // next perform a depth-first search of the transposed graph
      // using start vertices in order of those in the stack
      RecordDepthFirstSearch<E> rdfsSearcher
         = new RecordDepthFirstSearch<E>(graphTransposed);
      while (!vertexStack.empty())
      {  Vertex<E> vertex = vertexStack.pop();
         Set<Vertex<E>> visitedVertices
            =rdfsSearcher.getVisitedVertices(correspOtoT.get(vertex));
         if (visitedVertices!=null && visitedVertices.size()>0)
         {  // obtain a corresponding list of the original vertices
            Set<Vertex<E>> originalVertices =new HashSet<Vertex<E>>();
            for (Vertex<E> newVertex : visitedVertices)
               originalVertices.add(correspTtoO.get(newVertex));
            components.add(originalVertices);
         }
      }
      return components;
   }
   
   // inner class that repeatedly performs depth-first search until
   // all vertices have been visited
   @SuppressWarnings("hiding")
   private class CompleteDepthFirstSearch<E> extends DepthFirstSearch<E>
   {
      private Stack<Vertex<E>> vertexStack;
      
      public CompleteDepthFirstSearch(GraphADT<E> graph)
      {  super(graph);
         vertexStack = new Stack<Vertex<E>>();      
      }
      
      // performs a complete depth-first search of graph and returns
      // a stack of the vertices of graph with most recently finished
      // on top
      public Stack<Vertex<E>> completeSearch()
      {  for (Vertex<E> vertex : graph.vertexSet())
         {  if (vertexColours.get(vertex) == Colour.WHITE)
               search(vertex);
         }
         return vertexStack;
      }
      
      // overridden DepthFirstSearch method called when vertex 
      // has finished
      protected void vertexFinished(Vertex<E> vertex)
      {  vertexStack.add(vertex);
      }
   }
   
   // inner class that returns a set of vertices of graph when
   // vertices are searched using depth-first search
   @SuppressWarnings("hiding")
   private class RecordDepthFirstSearch<E> extends DepthFirstSearch<E>
   {
      private Set<Vertex<E>> visitedVertices; //holds visited vertices
      
      public RecordDepthFirstSearch(GraphADT<E> graph)
      {  super(graph);
         // prepare the empty set of visited vertices
         visitedVertices = new HashSet<Vertex<E>>();
      }
      
      // performs depth-first search of graph starting at specified
      // vertex and returns a set of the visited vertices
      public Set<Vertex<E>> getVisitedVertices(Vertex<E> vertex)
      {  if (vertexColours.get(vertex)!=Colour.WHITE)
            return null; // vertex already visited
         else // perform a new search
         {  visitedVertices.clear();
            super.search(vertex); // will update visitedVertices
            return visitedVertices;
         }
      }
      
      // overridden DepthFirstSearch method called when vertex has
      // been discovered
      protected void vertexDiscovered(Vertex<E> vertex)
      {  visitedVertices.add(vertex);
      }
   }

   public static void main(String[] args)
   {  GraphADT<String> graph = new AdjacencyListGraph<String>
         (GraphADT.GraphType.DIRECTED);
      Vertex<String> a = graph.addVertex("A");
      Vertex<String> b = graph.addVertex("B");
      Vertex<String> c = graph.addVertex("C");
      Vertex<String> d = graph.addVertex("D");
      Vertex<String> e = graph.addVertex("E");
      Vertex<String> f = graph.addVertex("F");
      graph.addEdge(b, a);
      graph.addEdge(b, c);
      graph.addEdge(c, e);
      graph.addEdge(d, a);
      graph.addEdge(d, b);
      graph.addEdge(d, e);
      graph.addEdge(e, b);
      graph.addEdge(e, f);
      graph.addEdge(f, c);
      System.out.println("Example Graph:\n" + graph);
      StronglyConnectedComponents<String> scc
         = new StronglyConnectedComponents<String>(graph);
      System.out.println("Finding strongly-connected components");
      for (Set<Vertex<String>> component : scc.getComponentVertices())
      {  System.out.print("Component:");
         for (Vertex<String> vertex : component)
         {  System.out.print(" "+vertex);
         }
         System.out.println();
      }
   }
}
