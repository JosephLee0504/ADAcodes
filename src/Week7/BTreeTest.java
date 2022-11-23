package Week7;

// BTreeTest.java
// Tests the BTree class.  Creates a B-tree by inserting several
// elements and then deleting them.  Prints out the entire tree after
// several of the operations.

public class BTreeTest
{
    public static void main(String[] args)
    {
    BTree tree = new BTree(2);
    System.out.println("Adding F,S,Q,K,C,L,H,T,V,W,M,R,N,P");
    tree.insert(new Name("F"));
    tree.insert(new Name("S"));
    tree.insert(new Name("Q"));
    tree.insert(new Name("K"));
    tree.insert(new Name("C"));
    tree.insert(new Name("L"));
    tree.insert(new Name("H"));
    tree.insert(new Name("T"));
    tree.insert(new Name("V"));
    tree.insert(new Name("W"));
    tree.insert(new Name("M"));
    tree.insert(new Name("R"));
    tree.insert(new Name("N"));
    tree.insert(new Name("P"));
    System.out.println(tree);
    System.out.println("Deleting P,S,K");
    tree.delete(new Name("P"));
    tree.delete(new Name("S"));
    tree.delete(new Name("K"));
    System.out.println(tree);
   }
}
