package Week7;

/**
   A class that implements a disjoint set collection using a linked
   list for each set, where each node has a link to the next node in
   the list and a link back to the representative at the head
   @author Andrew Ensor
*/
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LinkedDisjointSets<E> implements DisjointSetsADT<E>
{
   private List<Node<E>> repNodes; // heads for each linked list
   private Map<Node<E>,Integer> setSizes;//each repNode gives set size
   private Map<E, Node<E>> elementMap; // map of elements to locators
   
   public LinkedDisjointSets()
   {  repNodes = new ArrayList<Node<E>>();
      setSizes = new HashMap<Node<E>, Integer>();
      elementMap = new HashMap<E, Node<E>>();
   }
   
   @SuppressWarnings("deprecation")
   public E makeSet(E x)
   {  if (elementMap.containsKey(x))
         throw new IllegalArgumentException("element already used");
      Node<E> node = new Node<E>(x);
      node.repNode = node; // rep of the new set is x itself  
      repNodes.add(node); // add the head of the new set to the list
      setSizes.put(node, new Integer(1));
      elementMap.put(x, node); // add the new element to the map
      return x;
   }
   
   public E union(E x, E y)
   {  Node<E> nodeX = elementMap.get(x);
      Node<E> nodeY = elementMap.get(y);
      if (nodeX == null && nodeY == null)
         return null; // neither element is in any set
      if (nodeX == null)
         return nodeY.repNode.x; // x was not in any set
      else if (nodeY == null)
         return nodeX.repNode.x; // y was not in any set
      Node<E> repX = nodeX.repNode;
      Node<E> repY = nodeY.repNode;
      if (repX == repY)
         return repX.x; // same set
      else // add the smaller set to the larger set for efficiency
      {  int sizeX = setSizes.get(repX).intValue();
         int sizeY = setSizes.get(repY).intValue();
         if (sizeX < sizeY)
            return link(repY, repX); // add set with x to set with y
         else
            return link(repX, repY); // add set with y to set with x
      }
   }
   
   // helper method adds second non-empty set to first where each set
   // is specified by the node of its representative
   @SuppressWarnings("deprecation")
   private E link(Node<E> repX, Node<E> repY)
   {  // insert nodes of second set into first immediately after repX
      Node<E> nodeY = repY;
      Node<E> previousY = null;
      do
      {  nodeY.repNode = repX;
         previousY = nodeY;
         nodeY = nodeY.next;
      }
      while (nodeY != null);
      // link second set into the first set
      previousY.next = repX.next;
      repX.next = repY;
      // update the map of set sizes and list of repNodes
      int sizeX = setSizes.get(repX).intValue();
      int sizeY = setSizes.get(repY).intValue();
      setSizes.put(repX, new Integer(sizeX+sizeY));
      setSizes.remove(repY);
      repNodes.remove(repY); // setY no longer exists
      return repX.x;
   }
   
   public E findSet(E x) 
   {  Node<E> node = elementMap.get(x);
      if (node == null)
         return null; // element not in any set
      else // element is in a set
         return node.repNode.x; // return representative of the set
   }
   
   public String toString()
   {  String output = "Sets: ";
      for (Node<E> repNode : repNodes)
      {  Node<E> node = repNode;
         output += "{";
         while (node != null)
         {  output += node.x.toString();
            node = node.next;
            if (node != null)
               output += ",";
         }
         output += "}(size="+setSizes.get(repNode)+") ";
      }
      return output+"\n";
   }
   
   // inner class that represents a node in one of the linked lists
   @SuppressWarnings("hiding")
   private class Node<E>
   {
      public E x; // element held in the node
      public Node<E> next; // link to next node in list
      public Node<E> repNode; // link to head of the list
      
      public Node(E x)
      {  this.x = x;
         next = null;
         repNode = null;
      }
   }
   
   @SuppressWarnings("deprecation")
   public static void main(String[] args)
   {
      DisjointSetsADT<Character> sets = new LinkedDisjointSets<Character>();
      System.out.println("Creating singleton sets for a,b,c,d,e,f,g");
      Character a = sets.makeSet(new Character('a'));
      Character b = sets.makeSet(new Character('b'));
      @SuppressWarnings("unused")
	  Character c = sets.makeSet(new Character('c'));
      Character d = sets.makeSet(new Character('d'));
      Character e = sets.makeSet(new Character('e'));
      Character f = sets.makeSet(new Character('f'));
      Character g = sets.makeSet(new Character('g'));
      System.out.println(sets);
      System.out.println("Union {a} with {e}, {b} with {d}, {f} with {g}");
      @SuppressWarnings("unused")
	  Character ae = sets.union(a, e);
      Character bd = sets.union(b, d);
      Character fg = sets.union(f, g);
      System.out.println(sets);
      System.out.println("Union {b,d} with {f,g}");
      sets.union(bd, fg);
      System.out.println(sets);      
   }
}
