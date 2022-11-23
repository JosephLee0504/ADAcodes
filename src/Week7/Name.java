package Week7;

// Name.java
// Wrapper class to make a String look like a DynamicSetElement.

public class Name implements DynamicSetElement
{
    private String name;    // the String being stored

    // Constructor.  Stores a String.
    public Name(String name)
    {
    this.name = name;
    }

    // Stores a String.
    @SuppressWarnings("rawtypes")
	public void setKey(Comparable key)
    {
    name = (String) key;
    }

    // Returns the stored String.
    @SuppressWarnings("rawtypes")
	public Comparable getKey()
    {
    return name;
    }

    // Compares this Name's String to another, returning a negative
    // number if this Name's String is less; 0 if they are equal; a
    // positive number if this Name's String is greater.
    public int compareTo(Object e)
    {
    return Helper.compareTo(this, e);
    }

    // Returns the stored String.
    public String toString()
    {
    return name;
    }
}
