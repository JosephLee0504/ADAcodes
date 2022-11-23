package Week9;

/**
   A class which contains the breadth first search algorithm for any
   graph that implements the GraphADT interface and whose vertices
   hold elements of generic type E that has suitable hashing function
   @author Andrew Ensor
*/
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BreadthFirstSearch<E>
{
   // colours for each vertex where WHITE is unvisited, GREY is 
   // currently being processed, and BLACK is completely processed
   protected static enum Colour{WHITE, GREY, BLACK};
   protected Map<Vertex<E>, Colour> vertexColours;
   protected GraphADT<E> graph;

   // creates new searcher which has performed no breadth-first search
   public BreadthFirstSearch(GraphADT<E> graph)
   {  this.graph = graph;
      Set<Vertex<E>> vertices = graph.vertexSet();
      vertexColours = new HashMap<Vertex<E>, Colour>(vertices.size());
      for (Vertex<E> vertex : vertices)
         vertexColours.put(vertex, Colour.WHITE); 
   }
   
   // performs a breadth-first search of graph starting at given
   // vertex which is presumed to be WHITE
   public void search(Vertex<E> startVertex)
   {  if (!graph.containsVertex(startVertex))
         throw new IllegalArgumentException("vertex not in graph");
      //create queue to hold vertices not yet fully processed
      QueueADT<Vertex<E>> processingQueue
         = new LinkedQueue<Vertex<E>>();
      // handle the starting vertex
      vertexColours.put(startVertex, Colour.GREY);
      vertexDiscovered(startVertex);
      processingQueue.enqueue(startVertex);
      // repeatedly find adjacent vertices and visit them
      while (!processingQueue.isEmpty())
      {  Vertex<E> frontVertex = processingQueue.dequeue();
         // find all the adjacent vertices that have not been visited
         // and enqueue them
         for (Edge<E> incidentEdge : frontVertex.incidentEdges())
         {  Vertex<E> adjacentVertex
               = incidentEdge.oppositeVertex(frontVertex);
            if (vertexColours.get(adjacentVertex) == Colour.WHITE)
            {  edgeTraversed(incidentEdge);
               vertexColours.put(adjacentVertex, Colour.GREY);
               vertexDiscovered(adjacentVertex);
               processingQueue.enqueue(adjacentVertex);
            }
         }
         vertexColours.put(frontVertex, Colour.BLACK);
         vertexFinished(frontVertex);
      }
   }
   
   // hook method that is called whenever a vertex has been discovered
   protected void vertexDiscovered(Vertex<E> vertex)
   {  // default implementation does nothing
   }
   
   // hook method that is called whenever a vertex has been finished
   protected void vertexFinished(Vertex<E> vertex)
   {  // default implementation does nothing
   }
   
   // hook method that is called whenever a tree edge is traversed
   protected void edgeTraversed(Edge<E> edge)
   {  // default implementation does nothing
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
      final List<Vertex<String>> list = new LinkedList<Vertex<String>>();
      BreadthFirstSearch<String> searcher = new BreadthFirstSearch<String>(graph)
         {
            protected void vertexDiscovered(Vertex<String> vertex)
            {  list.add(vertex); // add vertices to list as discovered
            }
         };
      System.out.println("Performing breadth-first search from D:");
      searcher.search(d);
      System.out.println(list);
   }
}
