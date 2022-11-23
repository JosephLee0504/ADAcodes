package Week5;

/**
   A class that demonstrates how the divide-and-conquer technique can
   be used to find an optimum way of multiplying a chain of n
   matrices where each matrix has order p_i times p_{i+1}
   @author Andrew Ensor
*/

public class MatrixChainOrder
{
   @SuppressWarnings("unused")
   private int[] p; // orders of the matrices in the product chain
   private int[][] s; //values of k where product A_i...A_{j-1} splits
   private int[][] m; //minimum no multiplications to evaluate product
   private int n; // total number of matrices in the product

   public MatrixChainOrder(int[] p)
   {  this.p = p;
      n = p.length-1;
      m = new int[n+1][];
      m[0] = null; // m[j][i] not used for j=0
      s = new int[n+1][];
      s[0] = null; s[1] = null; // s[j][i] not used for j=0 nor j=1
      for (int j=1; j<=n; j++)
      {  int i = j-1;
         m[j] = new int[j]; // create m[j][0], ..., m[j][j-1]
         m[j][i] = 0;
         s[j] = new int[j-1]; // create s[j][0], ..., s[j][j-2]
      }
      for (int l=2; l<=n; l++) // l is number of matrices in product
      {  for (int j=l; j<=n; j++)
         {  int i = j-l;
            // find k for which m[k][i]+m[j][k]+p[i]p[k]p[j] minimized
            int indexK = i+1;
            int minM = m[indexK][i]+m[j][indexK]+p[i]*p[indexK]*p[j];
            for (int k=i+2; k<j; k++)
            {  int anotherM = m[k][i]+m[j][k]+p[i]*p[k]*p[j];
               if (anotherM<minM)
               {  indexK = k;
                  minM = anotherM;
               }
            }
            // update arrays m and s
            s[j][i] = indexK;
            m[j][i] = minM;
         }
      }
   }
   
   public String toString()
   {  return m[n][0] + " multiplications for " + getProduct(0, n);
   }
   
   // recursive helper method that returns a string representation of
   // the portion of the optimal product A_i x ... x A_{j-1} which
   // uses parentheses to show the order of multiplication
   private String getProduct(int i, int j)
   {  if (j == i+1)
         return "A["+i+"]";
      else
      {  int k = s[j][i];
         // show the product split at k
         return "(" + getProduct(i, k) + "." + getProduct(k, j) + ")"; 
      }
   }
   
   // driver main method to test the class
   public static void main(String[] args)
   {  int[] orders = {17, 5, 26, 30, 19, 7, 15, 15}; // 7 matrices
      MatrixChainOrder mco = new MatrixChainOrder(orders);
      System.out.println(mco);
   }
} 
