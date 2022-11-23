package Week5;

/**
   A class that demonstrates how the divide-and-conquer technique can
   be used to provide an O(n^(log_2 3)) algorithm for multiplying
   (positive) integer values of arbitrary length n
   @author Andrew Ensor
*/
import java.math.BigInteger; // used to check the calculation

public class IntegerMultiplication
{
   private final int THRESHOLD = 8; // maximum total digits for mult.
   
   public IntegerMultiplication()
   {
   }
   
   // subtract integer held in string q from integer held in string p
   // where it is presumed that p>=q
   public String subtract(String p, String q)
   {  int pLength = p.length();
      int qLength = q.length();
      if (qLength>pLength)
         throw new IllegalArgumentException
            ("In subtraction p-q number q has more digits");
      else if (pLength>qLength)
         q = padWithZeros(q, pLength);
      StringBuilder difference = new StringBuilder(pLength);
      boolean carry = false;
      for (int i=pLength-1; i>=0; i--)
      {  int digitDifference = p.charAt(i)-q.charAt(i);
         if (carry)
            digitDifference--;
         carry = (digitDifference<0);
         if (carry)
            difference.insert(0, (char)(digitDifference+'9'+1));
         else
            difference.insert(0, (char)(digitDifference+'0'));
      }
      if (carry)
         throw new ArithmeticException("Negative subtraction");
      return difference.toString();
   }
   
   // add together the two positive integer values held in the strings
   public String add(String p, String q)
   {  int pLength = p.length();
      int qLength = q.length();
      // ensure that p and q have the same length
      if (pLength>qLength)
      {  q = padWithZeros(q, pLength);
         qLength = q.length();
      }
      else if (qLength>pLength)
      {  p = padWithZeros(p, qLength);
         pLength = p.length();
      }
      StringBuilder sum = new StringBuilder(pLength+1);
      boolean carry = false;
      for (int i=pLength-1; i>=0; i--)
      {  int digitSum = (p.charAt(i)-'0')+(q.charAt(i)-'0');
         if (carry)
            digitSum++;
         carry = (digitSum>9);
         if (carry)
            sum.insert(0, (char)(digitSum-10+'0'));
         else
            sum.insert(0, (char)(digitSum+'0'));
      }
      if (carry)
         sum.insert(0, '1');
      return sum.toString();
   }
   
   // helper method which pads a string to specified length by placing
   // a quantity of '0' chars at start of string
   private String padWithZeros(String s, int length)
   {  StringBuilder buffer = new StringBuilder(length);
      for (int i=s.length(); i<length; i++)
        buffer.append('0');
      buffer.append(s);
      return buffer.toString();
   }
   
   // helper method which adds a specified quantity of '0' chars
   // to the end of a string effectively multiplying the integer
   // string by 10^quantity
   private String appendZeros(String s, int quantity)
   {  StringBuilder buffer = new StringBuilder(s.length()+quantity);
      buffer.append(s);
      for (int i=0; i<quantity; i++)
        buffer.append('0');
      return buffer.toString();
   }
   
   
   // multiply the two positive integer values held in the strings
   public String multiply(String p, String q)
   {  int pLength = p.length();
      int qLength = q.length();
      String product;
      if (pLength+qLength<=THRESHOLD)
      {  // directly evaluate the multiplication as int values
         product = Integer.toString
            (Integer.parseInt(p)*Integer.parseInt(q));
      }
      else
      {  // ensure that p and q have the same length
         if (pLength>qLength)
         {  q = padWithZeros(q, pLength);
            qLength = q.length();
         }
         else if (qLength>pLength)
         {  p = padWithZeros(p, qLength);
            pLength = p.length();
         }
         // divide the integer strings m and n into two parts
         int middle = pLength/2;
         String pHigh = p.substring(0, middle);
         String pLow = p.substring(middle);
         String qHigh = q.substring(0, middle);
         String qLow = q.substring(middle);
         // perform recursive conquer with three multiplications
         String highPartProduct = multiply(pHigh, qHigh);
         String lowPartProduct = multiply(pLow, qLow);
         String mixedPart = multiply(add(pHigh,pLow),add(qHigh,qLow));
         // combine three multiplications together to get product pq
         String highPartShifted = appendZeros(highPartProduct,
            2*(pLength-middle));
         String midPartShifted = appendZeros(subtract(subtract(
            mixedPart,highPartProduct),lowPartProduct),pLength-middle);
         product = add(add(highPartShifted, midPartShifted),
            lowPartProduct);
      }
      return product;
   }

   public static void main(String[] args)
   {  IntegerMultiplication multiplier = new IntegerMultiplication();
      String p = "98765432109876543210987654321";
      String q = "2005999888777666555444333222111999";
      String product = multiplier.multiply(p, q);
      System.out.println("Product of " + p + " with " + q + " is "
         + product);
      // check the result using the BigInteger class
      BigInteger correctResult
         = (new BigInteger(p)).multiply(new BigInteger(q));
      if (correctResult.equals(new BigInteger(product)))
         System.out.println("The answer is correct");
      else
         System.out.println("The answer is NOT correct");
   }
}
