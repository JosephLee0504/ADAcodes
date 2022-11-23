/**
   A class that represents a blocking (rather than spinning) lock
   where the BlockingLock instance is used as a monitor to control
   access to its acquireLock and releaseLock methods
   @author Andrew Ensor
*/
package Week1;

public class BlockingLock
{
   private boolean s; // access to s is synchronized

   public BlockingLock()
   {  s = true; // initially lock is available
   }

   public synchronized void acquireLock()
   {  while (!s) // wait for the lock available notification
      {  try
         {  wait();
         }
         catch (InterruptedException e)
         {  // ignore
         }
      }
      s = false; // lock is now unavailable for other threads
   }

   public synchronized void releaseLock()
   {  s = true; // lock is now available for other threads
      notify(); // notify one waiting thread
   }
}
