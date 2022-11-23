package Week5;

/**
   A class that performs the Huffman encoding algorithm for a string
   @author Andrew Ensor
*/
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;

public class Huffman
{
   private Map<Character, Integer> charFrequencyMap;
   @SuppressWarnings("deprecation")
   private static final Integer ONE = new Integer(1);
   
   // constructor accepts a string to use for determining frequency
   // of each letter and the characters that are valid for encoding
   @SuppressWarnings("deprecation")
   public Huffman(String characters)
   {  charFrequencyMap = new HashMap<Character, Integer>();
      for (int i=0; i<characters.length(); i++)
      {  Character character = new Character(characters.charAt(i));
         Integer frequency = charFrequencyMap.get(character);
         if (frequency == null)
            frequency = ONE;
         else
            frequency = new Integer(frequency.intValue()+1);
         charFrequencyMap.put(character, frequency);
      }
   }
   
   // builds a binary tree using the specified string which
   // should contain only valid characters
   protected HuffmanNode buildTree(String data)
   {  // create a priority queue to hold root of each binary tree
      Comparator<HuffmanNode> comparator=new Comparator<HuffmanNode>()
         {  public int compare(HuffmanNode nodeX, HuffmanNode nodeY)
            {  return nodeX.getFrequency()-nodeY.getFrequency();
            }
         };
      PriorityQueue<HuffmanNode> queue
         = new PriorityQueue<HuffmanNode>(charFrequencyMap.size(),
         comparator);
      // create a leaf node for each character key in the map
      Iterator<Character> iterator
         = charFrequencyMap.keySet().iterator();
      while (iterator.hasNext())
      {  Character character = iterator.next();
         HuffmanNode leaf = new LeafNode(character,
            charFrequencyMap.get(character));
         queue.add(leaf);
      }
      while (queue.size()>1)
      {  HuffmanNode nodeX = queue.poll();
         HuffmanNode nodeY = queue.poll();
         HuffmanNode nodeZ = new ParentNode(nodeX, nodeY);
         queue.add(nodeZ);
      }
      return queue.poll();
   }
   
   // returns a string representation of the character frequency map
   public String toString()
   {  StringBuffer output = new StringBuffer();
      Iterator<Character> iterator
         = charFrequencyMap.keySet().iterator();
      while (iterator.hasNext())
      {  Character character = iterator.next();
         int frequency = charFrequencyMap.get(character).intValue();
         output.append(character.charValue() + " has frequency " 
            + frequency + "\n");
      }
      return output.toString();
   }

   // driver main method to test the class
   public static void main(String[] args)
   {  String original ="peter piper picked a peck of pickled peppers";
      Huffman huff = new Huffman(original);
      System.out.println("Characters");
      System.out.println(huff);
      System.out.println("Resulting Huffman binary tree");
      System.out.println(huff.buildTree(original));
   }
   
   // inner class that represents a leaf in a Huffman binary tree
   private class LeafNode implements HuffmanNode
   {
      private Character character;
      private int frequency;
      
      public LeafNode(Character character, int frequency)
      {  this.character = character;
         this.frequency = frequency;
      }

      public HuffmanNode getLeftChild()
      {  return null;
      }   

      public HuffmanNode getRightChild()
      {  return null;
      }   

      public boolean isLeaf()
      {  return true;
      }   

      public int getFrequency()
      {  return frequency;
      }
      
      @SuppressWarnings("unused")
	public Character getCharacter()
      {  return character;
      }
      
      public String toString()
      {  return character.charValue() + ":" + frequency;
      }
   }
   
   // inner class represents a parent node in a Huffman binary tree
   private class ParentNode implements HuffmanNode
   {
      private HuffmanNode leftChild, rightChild;
      private int frequency;
      
      // create a parent node with specified child nodes
      public ParentNode(HuffmanNode leftChild, HuffmanNode rightChild)
      {  this.leftChild = leftChild;
         this.rightChild = rightChild;
         frequency=leftChild.getFrequency()+rightChild.getFrequency();
      }
      
      public HuffmanNode getLeftChild()
      {  return leftChild;
      }   

      public HuffmanNode getRightChild()
      {  return rightChild;
      }   

      public boolean isLeaf()
      {  return false;
      }   

      public int getFrequency()
      {  return frequency;
      }
      
      public String toString()
      {  return "[" + leftChild.toString() + ","
            + rightChild.toString() + "]";
      }
   }
}
