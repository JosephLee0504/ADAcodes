/**
   A class that represents a philosopher for the Dining Philosophers
   problem (remove sleep and try/catch to make problem more evident)
   @see DiningPhilosophersSolution.java
*/
package Week1;

import java.util.Random;

public class Philosopher implements Runnable
{
   private int idNumber;
   private Resource chopsticks;
   private boolean stopRequested;
   private Random generator;

   public Philosopher(int idNumber, Resource chopsticks)
   {  this.idNumber = idNumber;
      this.chopsticks = chopsticks;
      stopRequested = false;
      generator = new Random();
   }

   public void run()
   {  while (!stopRequested)
      {  try
         {  System.out.println("Philosopher "+idNumber+" is thinking");
            Thread.sleep(generator.nextInt(1000));
            System.out.println("Philosopher "+idNumber+" is hungry");
            chopsticks.acquire(idNumber);
            System.out.println("Philosopher "+idNumber+" is eating");
            Thread.sleep(generator.nextInt(1000));
            chopsticks.release(idNumber);
         }
         catch (InterruptedException e)
         {  System.out.println("Interruption: " + e);
         }
      }
   }

   public void requestStop()
   {  stopRequested = true;
   }
}
