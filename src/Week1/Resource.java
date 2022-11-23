/**
   Interface representing a resource that can be acquired and released
   @see DiningPhilosophersSolution.java
*/
package Week1;

public interface Resource
{
   // blocks until the resource is available for thread with given id
   public void acquire(int id);
   // releases resource that is held by thread with given id
   public void release(int id);
}
