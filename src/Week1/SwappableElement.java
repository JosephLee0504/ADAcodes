/**
   A class that represents an element of generic type E that includes
   a method for swapping two references which can cause deadlock
   @author Andrew Ensor
*/

package Week1;

public class SwappableElement<E>
{
   private E element;
   
   public SwappableElement(E element)
   {  this.element = element;
   }
   
   public synchronized E getElement()
   {  return element;
   }
   
   public synchronized void setElement(E element)
   {  this.element = element;
   }
   
   public synchronized void swap(SwappableElement<E> other)
   {  E temp = element;
      element = other.getElement();
      other.setElement(temp);
      // put Thread.sleep(1) here to make deadlock obvious
   }
   
   public static void main(String[] args)
   {  final SwappableElement<String> a = new SwappableElement<String>("A");
      final SwappableElement<String> b = new SwappableElement<String>("B");
      Thread t1 = new Thread(new Runnable()
         {  public void run()
            {  while(true)
               {  System.out.println("Swapping a with b");
                  a.swap(b);
               }
            }
         });
      Thread t2 = new Thread(new Runnable()
         {  public void run()
            {  while(true)
               {  System.out.println("Swapping b with a");
                  b.swap(a);
               }
            }
         });
      t1.start();
      t2.start();
   }
}
