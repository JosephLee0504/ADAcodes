package Week7;

// TreeTest.java
// Tests the classes BinarySearchTree, RedBlackTree,
// OrderStatisticTree, and IntervalTree.
// Andrew's two changes made to methods testSuccessor and testPredecessor
// to fix compiler error with compareTo method, and removal of
// McGraw-Hill packaging of classes

import java.util.*;
import java.io.*;

public class TreeTest
{
    public Random rand;
    public final int range = 10000;
    public boolean loggingEnabled = true;

    public PrintStream out;

    public TreeTest()
    {
    rand = new Random();
    try {
        out =  new PrintStream(new FileOutputStream("log.txt"));
    }
    catch(FileNotFoundException ex) {
        loggingEnabled = false;
        out = null;
    }
    }

    public TreeTest(String logfile) throws FileNotFoundException
    {
    out = new PrintStream(new FileOutputStream(logfile, true));
    }

    // Prints the result of a test. 
    public boolean claim(boolean b, String msg)
    {
    print("Testing " + msg + ": ");

    if (b) 
        println("Succeeded");
    else 
        println("Failed");

    return b;
    }

    // Prints the message to the output stream without a new line.
    public void log(String msg)
    {
    if (loggingEnabled)
        out.print(msg);
    }

    // Prints the message to the output stream with a new line.
    public void logln(String msg)
    {
    if (loggingEnabled)
        out.print(msg + "\n");
    }

    // Prints the message to the output stream and the console.
    public void print(String msg)
    {
    log(msg);
    System.out.print(msg);
    }

    // Prints the message to the output stream and the console.
    public void println(String msg)
    {
    log(msg + "\n");
    System.out.println(msg);
    }

    // Test the vital functions of BinarySearchTree on random data.
    public class TestBinarySearchTree
    {
    BinarySearchTree tree;
    public Integer[] array = new Integer[0];
    @SuppressWarnings("deprecation")
	final Integer exclude = new Integer(rand.nextInt(range));

    public TestBinarySearchTree()
    {
        setUp();
    }

    // Sets up a tree for testing.
    public void setUp()
    {
        tree = new BinarySearchTree();
        array = new Integer[0];
        grow(100);
    }

    // Adds nodes to the tree.
    @SuppressWarnings("deprecation")
	public void grow(int n)
    {
        int newsize;
        Integer[] temp;

        newsize = array.length + n;
        temp = new Integer[newsize];

        for (int i = 0; i < n; i++) {
        do
            temp[i] = new Integer(rand.nextInt(range));
        while (temp[i] == exclude);

        tree.insert(temp[i]);
        }

        System.arraycopy(array, 0, temp, n, array.length);
        Arrays.sort(temp);
        array = temp;
    }

    // Test BinarySearchTree.minimum().
    public boolean testMinimum()
    {
        setUp();
        @SuppressWarnings("deprecation")
		Object handle = tree.insert(new Integer(-1));
        return (tree.minimum() == handle);
    }

    // Test BinarySearchTree.maximum().
    public boolean testMaximum()
    {
        setUp();
        @SuppressWarnings("deprecation")
		Object handle = tree.insert(new Integer(10001));
        return (tree.maximum() == handle);
    }

    // Tests BinarySearchTree.successor().
    public boolean testSuccessor()
    {
        setUp();
        int i = rand.nextInt(array.length - 1);

        Object handle = tree.successor(tree.search(array[i]));
        // Andrew's modification as compareTo requires Integer parameter
        return array[i+1].equals(BinarySearchTree.dereference(handle));
        //  return array[i+1].compareTo(BinarySearchTree.dereference(handle)) == 0;
    }

    // Tests BinarySearchTree.predecessor().
    public boolean testPredecessor()
    {
        setUp();
        int i = rand.nextInt(array.length);

        Object handle = tree.predecessor(tree.search(array[i]));
        // Andrew's modification as compareTo requires Integer parameter        
        return array[i-1].equals(BinarySearchTree.dereference(handle));
        // return array[i-1].compareTo(BinarySearchTree.dereference(handle)) == 0;
    }

    // Tests search for when the search key is not in the tree.
    public boolean testFailSearch()
    {
        setUp();
        return tree.isNil(tree.search(exclude));
    }

    // Tests search for when a search key does exist.
    public boolean testSearch()
    {
        setUp();
        Integer k = exclude;
        Object handle;

        handle = tree.insert(k);
        grow(100);

        return tree.search(k) == handle;
    }

    // Test deletion.
    public boolean testDelete()
    {
        setUp();
        Object handle = tree.insert(exclude);
        grow(100);
        Object handle2 = tree.insert(exclude);
        grow(200);
        tree.delete(handle);

        return tree.search(exclude) == handle2;
    }

    // Tests all functions.
    public boolean testAll()
    {
        boolean b = true;

        b &= claim(testMinimum(), "minimum");
        b &= claim(testMaximum(), "maximum");
        b &= claim(testFailSearch(), 
          "Search for non-existent object returns null");
        b &= claim(testSearch(), 
          "Search for existing object");
        b &= claim(testDelete(), "Deletion");
        b &= claim(testSuccessor(), "successor");
        b &= claim(testPredecessor(), "predecessor");
        
        return b;
    }
    }

    public class TestRedBlackTree extends TestBinarySearchTree
    {
    public void setUp()
    {
        tree = new RedBlackTree();
        array = new Integer[0];
        grow(100);
    }

    // Tests to see if the black height property of red black
    // trees holds for this tree.
    public boolean testBlackHeight()
    {
        setUp();

        try {
        println("Black height = " + 
            ((RedBlackTree)tree).blackHeight());
        }
        catch (RedBlackTree.BlackHeightException ex) {
        return false;
        }

        return true;
    }

    public boolean testAll()
    {
        boolean b = super.testAll();
        b &= claim(testBlackHeight(), "determining black height");

        return b;
    }
    }

    public class TestOrderStatisticTree extends TestRedBlackTree
    {
    public void setUp()
    {
        tree = new OrderStatisticTree();
        array = new Integer[0];
        grow(100);
    }

    // Tests selection.
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean testSelect()
    {
        log("Entering testSelect\n");

        setUp();
        int i = rand.nextInt(array.length) + 1;
        logln("i = " + i);
        
        Comparable expected = (Comparable)array[i-1];
        logln("expected value = " + expected);

        Object handle = ((OrderStatisticTree)tree).select(i);
        Comparable found = BinarySearchTree.dereference(handle);
        logln("value found = " + found);

        logln("Leaving testSelect");
        return found.compareTo(expected) == 0;
    }

    // Tests node rank. 
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean testRank()
    {
        logln("Entering testRank()");

        setUp();

        Comparable key = (Comparable)
        array[rand.nextInt(array.length)];
        logln("key = " + key);
        
        Object handle = tree.search(key);
        int rank = ((OrderStatisticTree)tree).rank(handle);
        logln("supposed rank = " + rank);

        Comparable found = (Comparable)array[rank-1];
        logln("found = " + found);
        
        logln("Leaving testRank()");
        return key.compareTo(found) == 0;
    }

    // Check to see if the size instance variable is consistent
    // with the actual size of the tree.
    public boolean testSize()
    {
        try {
        println("Actual size = " + 
            ((OrderStatisticTree)tree).actualSize());
        return true;
        }
        catch(OrderStatisticTree.SizeException ex) {
        logln("actualSize() failed with: ");
        logln(ex.getMessage());
        return false;
        }
    }

    // Test everything.
    public boolean testAll()
    {
        boolean b = true;
        b &= super.testAll();

        setUp();
        logln(tree.toString());

        b &= claim(testSize(), "actual size");
        b &= claim(testSelect(), "selection");
        b &= claim(testRank(), "node rank");

        return b;
    }
    }

    public class TestIntervalTree
    {
    public IntervalTree tree;

    public void setUp()
    {
        tree = new IntervalTree();

        tree.insert(new Interval(0,3));
        tree.insert(new Interval(5,8));
        tree.insert(new Interval(6,10));
        tree.insert(new Interval(8,9));
        tree.insert(new Interval(15,23));
        tree.insert(new Interval(16,21));
        tree.insert(new Interval(17,19));
        tree.insert(new Interval(25,30));
        tree.insert(new Interval(19,20));
        tree.insert(new Interval(26,26));
    }

    // Search for an overlapping interval to [22,24].
    public boolean testSearch()
    {
        logln("Entering testSearch()");

        setUp();
        Interval i = new Interval(22,24);
        Interval expected = new Interval(15,23);

        Object handle = tree.search(i);
        Interval found = (Interval) BinarySearchTree.dereference(handle);
        
        logln("Found = " + found);

        logln("Leaving testSearch()");
        return (found.getLow() == expected.getLow()) &&
        (found.getHigh() == expected.getHigh());
    }

    // Search for an overlapping interval to [11,14] (doesn't exist).
    public boolean testFailSearch()
    {
        logln("Entering testFailSearch()");

        setUp();
        Interval i = new Interval(11,14);

        Object handle = tree.search(i);

        logln("Leaving testFailSearch()");
        return tree.isNil(handle);
    }

    public boolean testAll()
    {
        boolean b = true;

        b &= claim(testSearch(), "search with good key");
        b &= claim(testFailSearch(), "search with invalid key");

        return b;
    }
    }
    
    public boolean testAll()
    {
    TestBinarySearchTree tbt = new TestBinarySearchTree();
    
    println("Testing BinarySearchTree...");
    if (tbt.testAll())
        println("Success\n");
    else
        return false;


    TestRedBlackTree trbt = new TestRedBlackTree();
    println("Testing RedBlackTree...");
    if (trbt.testAll())
        println("Success\n");
    else
        return false;

    TestOrderStatisticTree tost = new TestOrderStatisticTree();
    println("Testing OrderStatisticTree...");
    if (tost.testAll())
        println("Success\n");
    else
        return false;

    println("Testing IntervalTree...");
    if (new TestIntervalTree().testAll())
        println("Success\n");
    else
        return false;

    return true;
    }

    public static void main(String[] args)
    {
    TreeTest tt = new TreeTest();
    tt.testAll();
    }
}
