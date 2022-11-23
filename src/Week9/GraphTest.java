package Week9;

/**
   A class which demonstrates how a GraphADT graph is built
*/
public class GraphTest
{
   public static void main(String[] args)
   {  GraphADT<String> graph = new AdjacencyListGraph<String>();
      Vertex<String> auckland = graph.addVertex("Auc");
      Vertex<String> wellington = graph.addVertex("Wel");
      Vertex<String> christchurch = graph.addVertex("Chr");
      Vertex<String> chatham = graph.addVertex("Cha");
      Vertex<String> fiji = graph.addVertex("Fij");
      Vertex<String> samoa = graph.addVertex("Sam");
      Vertex<String> tahiti = graph.addVertex("Tah");
      Vertex<String> brisbane = graph.addVertex("Bri");
      Vertex<String> sydney = graph.addVertex("Syd");
      Vertex<String> melbourne = graph.addVertex("Mel");
      graph.addEdge(auckland, wellington);
      graph.addEdge(auckland, christchurch);
      graph.addEdge(auckland, fiji);
      graph.addEdge(auckland, samoa);
      graph.addEdge(auckland, tahiti);
      graph.addEdge(auckland, brisbane);
      graph.addEdge(auckland, sydney);
      graph.addEdge(auckland, melbourne);
      graph.addEdge(wellington, christchurch);
      graph.addEdge(wellington, chatham);
      graph.addEdge(christchurch, melbourne);
      Edge<String> fijiSamoaEdge = graph.addEdge(fiji, samoa);
      graph.addEdge(fiji, sydney);
      graph.addEdge(brisbane, sydney);
      graph.addEdge(sydney, melbourne);
      System.out.println(graph);
      System.out.println("Graph contains " + fijiSamoaEdge + ":" 
         + graph.containsEdge(fijiSamoaEdge));
      System.out.println("Removing edge " + fijiSamoaEdge);
      graph.removeEdge(fijiSamoaEdge);
      System.out.println(graph);
      System.out.println("Graph contains " + fijiSamoaEdge + ":" 
         + graph.containsEdge(fijiSamoaEdge));
      GraphADT<Object> graph2 = new AdjacencyListGraph<Object>(graph);
      Vertex<Object> vertex = graph2.vertexSet().iterator().next();
      System.out.println("Removing vertex " + vertex);
      graph2.removeVertex(vertex);
      System.out.println(graph2);
      System.out.println("Graph contains " + vertex + ":" 
         + graph2.containsVertex(vertex));
   }
}
