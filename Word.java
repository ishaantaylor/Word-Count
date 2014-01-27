/** The specific entry for this implementation of HashTable */
public class Word {
 private String word;
 private int frequency;

 /** Constructors */
 public Word(String theWord) {
   this(theWord, 1);
 }
 
 public Word(String s, int f) {
   word = s;
   frequency = f;
 }
 
 /** Methods */
 public String getString() {
  return word;
 }

 public int getFrequency() {
  return frequency;
 }

 public void incrementCount() {
  frequency++;
 }
 
 /** this method overrides the objects method and compares strings of different Word objects.
   * its primarily used in the insert method to see if a list (specified by the words hashcode)
         contains the word already. By overriding this method, the contains() method of List<E>
         uses different parameters to determine t or f.
   */     
 @Override
 public boolean equals(Object o) {
   if (o instanceof Word && this instanceof Word) {
     Word ol = (Word) o;
     return this.getString().equals(ol.getString());
   }
   else if (o instanceof String && this instanceof Word)
     return this.getString().equals(o);
   else {   
     Word ol = (Word) o;
     return this.equals(ol.getString());
   }
 }
}