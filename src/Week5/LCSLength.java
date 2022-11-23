package Week5;

/**
   A class that demonstrates how the divide-and-conquer technique can
   be used to find a longest common subsequence of strings x and y
   @author Andrew Ensor
*/

public class LCSLength
{
   private enum Construction{INCLUDE, NOTX, NOTY};
   
   public LCSLength()
   {
   }
   
   // returns one longest common subsequence of x and y
   public String findLCS(String x, String y)
   {  int m = x.length();
      int n = y.length();
      int[][] c = new int[m+1][n+1];
      Construction[][] b = new Construction[m+1][n+1];
      for (int i=1; i<=m; i++)
         c[i][0] = 0;
      for (int j=0; j<=n; j++)
         c[0][j] = 0;
      for (int i=1; i<=m; i++)
      {  for (int j=1; j<=n; j++)
         {  if (x.charAt(i-1)==y.charAt(j-1))
            {  c[i][j] = c[i-1][j-1]+1;
               b[i][j] = Construction.INCLUDE;
            }
            else if (c[i-1][j]>c[i][j-1])
            {  c[i][j] = c[i-1][j];
               b[i][j] = Construction.NOTX;
            }
            else
            {  c[i][j] = c[i][j-1];
               b[i][j] = Construction.NOTY;
            }
         }
      } 
      // use the array b to determine one longest common subsequence
      // starting at index m,n of b
      return getLCSString(x, b, m, n);
   }
   
   // recursive helper method which returns the subsequence of X
   // starting at entry i,j in the array b
   private String getLCSString(String x, Construction[][] b,
      int i, int j)
   {  if (i>0 && j>0)
      {  switch (b[i][j])
         {  case INCLUDE :
            {  return getLCSString(x, b, i-1, j-1) + x.charAt(i-1);
            }
            case NOTX :
            {  return getLCSString(x, b, i-1, j);
            }
            case NOTY :
            {  return getLCSString(x, b, i, j-1);
            }
         }
      }
      return "";
   }
   
   public static void main(String[] args)
   {  // test example given in Introduction to Algorithms textbook
      String x = "amputation";
      String y = "spanking";
      LCSLength finder = new LCSLength();
      String lcs = finder.findLCS(x, y);
      System.out.println("One longest common subsequence of " + x
         + " and " + y + " is " + lcs + ".");
   }
}
