/**
   An implementation of the dining philosophers problem using a
   monitor. Note that this implementation does not cause deadlock
   but may cause starvation of a thread
   @author Andrew Ensor
*/
package Week1;

public class DiningPhilosophersSolution implements Resource
{
   private static final int NUM = 5; //num philosophers and chopsticks
   private enum State{THINKING, HUNGRY, EATING};
   private State[] philState;

   public DiningPhilosophersSolution()
   {  philState = new State[NUM];
      for (int i=0; i<NUM; i++)
         philState[i] = State.THINKING;
   }

   // helper method that return the id of philosopher to left
   private int left(int id)
   {  return (id-1+NUM)%NUM;
   }

   // helper method that return the id of philosopher to right
   private int right(int id)
   {  return (id+1)%NUM;
   }

   // return whether philosopher with specified id can eat
   private boolean canEat(int id)
   {  return (philState[left(id)] != State.EATING)
         && (philState[id] == State.HUNGRY)
         && (philState[right(id)] != State.EATING);
   }

   public synchronized void acquire(int id)
   {  philState[id] = State.HUNGRY;
      while (!canEat(id))
      {  try
         {  wait(); // make thread release monitor and wait for notify
         }
         catch (InterruptedException e)
         {  // do nothing
         }
      }
      philState[id] = State.EATING;
   }

   public synchronized void release(int id)
   {  philState[id] = State.THINKING;
      notifyAll(); // notify all waiting threads
   }

   public static void main(String[] args)
   {  DiningPhilosophersSolution resource
         = new DiningPhilosophersSolution();
      for (int i=0; i<NUM; i++)
      {  // create and start the philosophers
         Philosopher phil = new Philosopher(i, resource);
         Thread thread = new Thread(phil);
         thread.start();
      }
   }
}
