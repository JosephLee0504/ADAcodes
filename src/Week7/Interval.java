package Week7;

/**
 * Implements an interval whose endpoints are real numbers.
 */

@SuppressWarnings("rawtypes")
public class Interval implements Comparable
{
    /** Low endpoint of the interval. */
    protected final double low;

    /** High endpoint of the interval. */
    protected final double high;

    /**
     * Initializes the endpoints of the interval.
     *
     * @param low The low endpoint.
     * @param high The high endpoint.
     */
    public Interval(double low, double high)
    {
    this.low = low;
    this.high = high;
    }

    /** Returns the low endpoint of the interval. */
    public double getLow()
    {
    return low;
    }

    /** Returns the high endpoint of the interval. */
    public double getHigh()
    {
    return high;
    }

    /**
     * Returns whether this interval overlaps another interval.
     *
     * @param i The other interval.
     * @return <code>true</code> if this interval overlaps
     * <code>i</code>, <code>false</code> otherwise.
     */
    public boolean overlaps(Interval i)
    {
    return this.low <= i.high && i.low <= this.high;
    }

    /**
     * Returns the {@link String} representation of this interval in
     * the form [low, high].
     */
    public String toString()
    {
    return "[" + low + ", " + high + "]";
    }

    /**
     * Compares this interval to another, based on their low
     * endpoints.
     *
     * @param o The other <code>Interval</code> object.
     * @return A negative integer if this <code>Interval</code> is
     * less; 0 if the objects are equal; a positive integer if this
     * <code>Interval</code> is greater.
     * @throws ClassCastException if <code>o</code> is not an
     * <code>Interval</code> object.
     */
    public int compareTo(Object o)
    {
    double otherLow = ((Interval) o).low;

    if (low < otherLow)
        return -1;
    else if (low == otherLow)
        return 0;
    else
        return 1;
    }
}

// $Id: Interval.java,v 1.1 2003/10/14 16:56:20 thc Exp $
// $Log: Interval.java,v $
// Revision 1.1  2003/10/14 16:56:20  thc
// Initial revision.
//
