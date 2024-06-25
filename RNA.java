import java.util.*;
import java.lang.*;
import java.io.*;

public class RNA {
   
   public static void main(String[] args) throws FileNotFoundException {
      
      // ArrayList to store test cases
      ArrayList<String> array = new ArrayList<String>();
      Scanner input = new Scanner(new File("testcases.txt"));
      String S;
      int n = 0;
      
      // Reading test cases from file
      while (input.hasNextLine()) {
         array.add(input.nextLine());
         n++;
      }
      
      // Process each test case
      for (int i = 0; i < n; i++)
         Nussinov(array.get(i));
      

   }
   
   //algorithm implementation
   private static void Nussinov(String S) {
      int n = S.length();
      int[][] OPT = new int[n][n];
      int temp = 0;  // To store the largest result in the t loop
      
      System.out.println(S);  // Print the input sequence
      
      double startTime = System.nanoTime();  // Start time for timing
      
      //algorithm DP approach
      for (int j = 5; j < n; j++)
         for (int i = j - 5; i >= 0; i--) {
            for (int t = j - 5; t >= i; t--)
               if (S.charAt(j) + S.charAt(t) == 138 || S.charAt(j) + S.charAt(t) == 150)
                  if (i == t)  // If j pairs with i
                     temp = Math.max(temp, 1 + OPT[t + 1][j - 1]);
                  else
                     temp = Math.max(temp, OPT[i][t - 1] + 1 + OPT[t + 1][j - 1]);
            
            OPT[i][j] = Math.max(OPT[i][j - 1], temp);
            temp = 0;
         }
      
      // Traceback
      char[] se = new char[n];
      for (int i = 0; i < n; i++)
         se[i] = '.';
      
      System.out.println(Traceback(OPT, 0, n - 1, se));  // Print the secondary structure
      
      double endTime = System.nanoTime();  // End time for timing
      
      // Print results
      System.out.print("Length = " + n);
      System.out.print(", Pairs = " + OPT[0][n - 1]);
      System.out.println(", Time = " + (endTime - startTime) / 1000000000 + " sec");
      
      // Print matrix (optional)
      if (n <= 25)
         for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++)
               System.out.print(OPT[i][j] + " ");
            System.out.println();
         }
      
      System.out.println();
   }

   // Traceback to reconstruct the secondary structure
   private static char[] Traceback(int[][] OPT, int left, int right, char[] se) {
      if (right - left < 5) return se;
      
      // Find the j that is part of a base pair
      for (int j = right; j >= left + 5; j--)
         if (OPT[left][j] > OPT[left][j - 1]) {
            se[j] = ')';
            
            // Find the t that pairs with j
            int t;
            for (t = j - 5; t > left; t--)
               if (t == left && OPT[t][j] == 1 + OPT[t + 1][j])
                  break;
               else if (OPT[left][j] == 1 + OPT[left][t - 1] + OPT[t + 1][j])
                  break;
            
            se[t] = '(';
            
            // Call the subroutines recursively
            Traceback(OPT, left, t - 1, se);
            Traceback(OPT, t + 1, j - 1, se);
            break;
         }
      
      return se;
   }
   
   // Generate a random RNA sequence of given size
   private static String randomS(int size) {
      String input = "ACGU";
      char[] output = new char[size];
      for (int i = 0; i < size; i++)
         output[i] = input.charAt((int)(Math.random() * 4));
      return new String(output);
   }
}
