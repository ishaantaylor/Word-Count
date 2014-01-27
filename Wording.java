import java.io.*;
import java.util.*;

/** 
 * This class is the framework of a class that implements my implementation of a 
 * hashtable with separate chaining.
 * 
 * To run this program: run Wording artofwar.txt
 * 
 */
public class Wording {
  /* no fields or constructors */
  /*
   * This method takes in a text file and returns an output file labelled "output.txt" 
   * in the format:
   * (String word  int frequency)
   * ie. (test 4)
   * 
   * @param file name, type String
   */
  private static void wordCount(String file) {
    String currentToken = null;
    HashTableSC ht = new HashTableSC();
    try {
      BufferedReader reader = new BufferedReader(new FileReader(file));
      BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
      writer.write(file,0,file.length());                 // header of output file
      writer.newLine();
      writer.newLine();
      String line = null;
      while ((line = reader.readLine()) != null) {        // read line by line, efficiency
        StringTokenizer st = new StringTokenizer(line, 
                                                 "\n \\. \\, \\? \\! \\: \\; \\- \' \" \\% \\[ \\] \\( \\) \\@ \\$ \\&",
                                                 false);
                                                         // natural delimiters and most special delimiters
        while (st.hasMoreTokens()) {
          currentToken = st.nextToken();                 // grab next token
          ht = ht.insertWFreq(currentToken.toLowerCase(), 1);    
                                                         // insert into hashtable
        }
      } reader.close();
      String  word = null;
      Integer freq = 0;
      double avgChainLength = ht.averageChain();
      StringBuilder sb = new StringBuilder("");
      for (int i = 0; i < ht.getSize(); i++) {           // for every line in old hashtable
        for (Word werd : ht.getLists()[i]) {               // for each word in each list
          word = werd.getString();                           // print to output.txt
          freq = werd.getFrequency();
          sb.append("(");                                    // string builder is less expensive
          sb.append(word);                                   //  than concatenating 5 string values
          sb.append(" ");
          sb.append(freq);
          sb.append(")");
          writer.write(sb.toString(), 0, 3 + 
                       word.length() + freq.toString().length());
          writer.newLine();
          writer.flush();
          sb.delete(0, sb.toString().length());
        }
      } 
      String s = "Average Chain Length: " + avgChainLength;
      writer.newLine();
      writer.write(s, 0, s.length());
      writer.newLine();
      s = "Number of Elements: " + ht.numEl();
      writer.write(s, 0, s.length());
      writer.newLine();
      s = "Table Size: " + ht.getSize();
      writer.write(s, 0, s.length());
      writer.close();
    } catch (IOException e) {
      System.err.format("IOException: %s%n", e);
    }
  }
  
  
  public static void main(String args[]) {
    wordCount(args[0]);
  }
}