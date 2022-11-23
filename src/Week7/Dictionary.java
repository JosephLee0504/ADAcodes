package Week7;

/**
 * Interface for dictionary data structures, defined on page 197 of
 * <i>Introduction to Algorithms</i>, Second edition.  Any object
 * inserted into a dictionary must implement the
 * <code>Comparable</code> interface.  In addition, for specific
 * implementations of the <code>Dictionary</code> interface, there may
 * be stronger restrictions on inserted objects, such as having to
 * implement the {@link DynamicSetElement} interface.
 */

public interface Dictionary
{
    /**
     * Inserts an element that implements <code>Comparable</code>.
     *
     * @param o The element to insert.
     * @return A handle to the inserted element.
     */
    @SuppressWarnings("rawtypes")
	public Object insert(Comparable o);

    /**
     * Removes an element.
     *
     * @param handle A handle to the element to remove.
     */
    public void delete(Object handle);

    /**
     * Searches for an element with a given key.  Depending on the
     * type of element inserted into the dictionary, the type of the
     * key given to this method may be the same as the type of the
     * objects inserted, or the type of the key given to this method
     * may be a different type than the type of the objects inserted
     * but still can be compared to the type of the inserted objects.
     * For example, see {@link DynamicSetElement.Helper#compareTo}.
     *
     * @param k The key being searched for.
     * @return A handle to the object found, or <code>null</code> if
     * there is no match.
     */
    @SuppressWarnings("rawtypes")
	public Object search(Comparable k);
}

// $Id: Dictionary.java,v 1.1 2003/10/14 16:56:20 thc Exp $
// $Log: Dictionary.java,v $
// Revision 1.1  2003/10/14 16:56:20  thc
// Initial revision.
//
