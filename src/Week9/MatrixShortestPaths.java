package Week9;

/**
   A class that uses matrix "multiplication" to solve the all-pairs
   shortest path problem in O(n^3\log_2 n)
   @author Andrew Ensor
*/

public class MatrixShortestPaths
{
   private int n; // number of vertices in graph
   private int k; // paths of length at most k
   private int[][] d; // length of shortest paths of max k edges
   private static final int INFINITY = Integer.MAX_VALUE;
   
   // create shortest paths matrix d_1 presuming weights is nxn matrix
   public MatrixShortestPaths(int[][] weights)
   {  this.n = weights.length;
      k = 1;
      d = weights;
   }
   
   // create a shortest paths matrix d=sp.d*sp.d where least weight
   // path of at most k edges from vertex i to j is sp.d[i][j]
   public MatrixShortestPaths(MatrixShortestPaths sp)
   {  this.n = sp.n;
      this.k = sp.k*2;
      d = new int[n][n];
      // calculate d = sp.d*sp.d as a matrix "multiplication"
      for (int i=0; i<n; i++)
      {  for (int j=0; j<n; j++)
         {  // "multiply" row i of sp.d by column j of sp.d
            int min = INFINITY;
            for (int x=0; x<n; x++)
            {  if (sp.d[i][x]!=INFINITY && sp.d[x][j]!=INFINITY)
               {  int sum = sp.d[i][x] + sp.d[x][j];
                  if (sum < min)
                     min = sum;
               }
            }
            d[i][j] = min; // the "product" of row i with column j
         }
      }
   }
   
   public String toString()
   {  String output = "Matrix for k=" + k + "\n";
      for (int i=0; i<n; i++)
      {  for (int j=0; j<n; j++)
         {  if (d[i][j] != INFINITY)
               output += ("\t" + d[i][j]);
            else
               output += "\tinfin";
         }
         output += "\n";
      }
      return output;
   }
   
   public static void main(String[] args)
   {  int[][] weights = {
         {0, 2, 15, INFINITY, INFINITY, INFINITY},
         {INFINITY, 0, 9, 11, 5, INFINITY},
         {INFINITY, -1, 0, 3, 6, INFINITY},
         {INFINITY, INFINITY, INFINITY, 0, 5, 2},
         {INFINITY, INFINITY, -2, INFINITY, 0, 7},
         {INFINITY, INFINITY, INFINITY, 1, INFINITY, 0}};
      MatrixShortestPaths sp = new MatrixShortestPaths(weights);
      while (sp.k<sp.n-1)
      {  sp = new MatrixShortestPaths(sp);
         System.out.println(sp);
      }
   }
}
