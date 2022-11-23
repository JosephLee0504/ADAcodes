package Week5;

/**
   An interface that represents a node in a binary tree that is used
   for the Huffman encoding algorithm
   @see Huffman.java
*/

public interface HuffmanNode
{
   // returns the left child of this node or null if none
   public HuffmanNode getLeftChild();
   
   // returns the right child of this node or null if none
   public HuffmanNode getRightChild();
   
   // returns whether this node is a leaf (which holds a character)
   public boolean isLeaf();
   
   // returns total frequency of all characters that are descendants
   public int getFrequency();
}
