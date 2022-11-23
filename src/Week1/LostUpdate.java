/**
   A class that demonstrates the lost update problem in concurrency
   by creating two threads that concurrently try to increment x
   each a total of ITERATIONS times.
   Sometimes the final value of x is not 2*ITERATIONS
   @author Andrew Ensor
*/
package Week1;

public class LostUpdate implements Runnable
{
   private int x;
   private static final int ITERATIONS = 20000000; // multiple of 10
   
   public LostUpdate()
   {  x = 0;
   }
   
   // repeatedly increment the value of x 
   public void run()
   {  System.out.println("Thread " + Thread.currentThread()
         + " started with x = " + x);
      int loopIterations = ITERATIONS/10;
      for (int i=0; i<loopIterations; i++)
      {  x++; x++; x++; x++; x++; x++; x++; x++; x++; x++;
      }
      System.out.println("Thread " + Thread.currentThread()
         + " finishing with x = " + x); 
   }
   
   public static void main(String[] args)
   {  // create two concurrent threads
      LostUpdate lostUpdate = new LostUpdate();
      Thread threadA = new Thread(lostUpdate);
      Thread threadB = new Thread(lostUpdate);
      threadA.start();
      threadB.start();
      try
      {  // wait for both threads to finish
         threadA.join();
         threadB.join();
      }
      catch (InterruptedException e)
      {  System.out.println("Interrupted " + e);
      }
      System.out.println("The final value of x is " + lostUpdate.x);
   }
}
