/**
   An implementation of the dining philosophers problem using blocking
   locks. Note that this implementation might cause a deadlock
   @see DiningPhilosophersSolution.java
*/
package Week1;

public class DiningPhilosophersProblem implements Resource
{
   private static final int NUM = 5; //num philosophers and chopsticks
   private BlockingLock[] chopsticks; // lock used for each chopstick

   public DiningPhilosophersProblem()
   {  chopsticks = new BlockingLock[NUM];
      for (int i=0; i<NUM; i++)
         chopsticks[i] = new BlockingLock();
   }

   public void acquire(int id)
   {  // first acquire chopstick on left
      chopsticks[id].acquireLock();
      // then acquire chopstick on right
      chopsticks[(id+1)%NUM].acquireLock();
   }

   public void release(int id)
   {  // release both chopsticks
      chopsticks[id].releaseLock();
      chopsticks[(id+1)%NUM].releaseLock();
   }

   public static void main(String[] args)
   {  DiningPhilosophersProblem resource
         = new DiningPhilosophersProblem();
      for (int i=0; i<NUM; i++)
      {  // create and start the philosophers
         Philosopher phil = new Philosopher(i, resource);
         Thread thread = new Thread(phil);
         thread.start();
      }
   }
}
