import java.util.*;
import java.io.*;
// write own hashtable w/ separate chaining.. meaning each item in hashtable is linked list

public class HashTableSC {
  /* Fields */
  private int nel;         // number of elements
  private final double loadfactor = 0.75;
  private int capacity;    // total size of hashtable
  private LinkedList<Word>[] lists;
  
  /* Constructors */
  public HashTableSC() {
    this(4); 
  }
  
  @SuppressWarnings("unchecked")
  public HashTableSC (int capacity) {
    lists = (LinkedList<Word>[]) new LinkedList[capacity];   // create array of lists
    for (int i = 0; i < capacity; i++)                       // instantiate each element
      lists[i] = (LinkedList<Word>) new LinkedList();
    this.capacity = capacity;                                // to fill capacity
  }
  
  
  /** Methods */
  
  /* Getter method: capacity of table */
  public int getSize() {
    return capacity;
  }
  
  /* Returns number of elements occupying the hashtable */
  public int numEl() {
    return nel;
  }
  
  /* Returns array of lists */
  public LinkedList<Word>[] getLists() {
    return lists;
  } 
  
  /**
   * Rehashes the table, doubling its size while avoiding multiples of 31
   * I realize this may be inefficient, but I could not figure out another 
   *   way such that it would work with my design.
   * 
   * @return HashTableSC, returns new table. 
   */
  @SuppressWarnings("unchecked")
  private HashTableSC rehash() {
    int tempCap = capacity * 2;                    // doubles to avoid multiples of 31
    HashTableSC tempht = new HashTableSC(tempCap);      
    LinkedList<Word>[] templists = (LinkedList<Word>[]) new LinkedList[tempCap];
    for (int i = 0; i < tempCap; i++) {            // for each line in hashtable
      templists[i] = new LinkedList<Word>();          // create new array list per index
    }
    for (int i = 0; i < capacity; i++) {           // for every line in old hashtable
      for (Word werd : lists[i])                      // for each word in each list
        tempht.insertWFreq(werd.getString(), werd.getFrequency()); 
                                                         // insert into new HT 
    }
    capacity = tempCap;                            // reset member variables for next rehash
    lists = templists;
    return tempht;                                 // return a new hashtable
  }
  
  /** 
   * This method is called when inserting an item the current table. 
   * It uses a helper method
   * 
   * @param  Word, Int
   * @return HashTableSC
   */
  public HashTableSC insertWFreq(String word, int freq) {
    return insert(new Word(word,freq));   
  }
  
  /**
   * Note: the .equals for Word has been modified and in turn parameters of contains and indexOf.
   * This is the helper method to the insert with frequency method above.
   * 
   * @param  Word
   * @return HashTableSC
   */
  private HashTableSC insert(Word word) {
    LinkedList<Word> hashedList = getLists()[myhash(word.getString())];
    if (hashedList.contains(word)) {           // if the list in the specified index contains word (.equals)
      int index = hashedList.indexOf(word);        // find the index of the word (creating new oboject for .equals
      hashedList.get(index).incrementCount();      // increment frequency of word
                                                   // the hashtable already contains this word @ given index
    } else if (numEl() > (capacity * loadfactor)) {
      HashTableSC temphc = new HashTableSC();      // the hashtable needs to be rehashed 
      temphc = this.rehash();
      incrementNEL();
      return temphc.insert(word);                  // call insert again
    } else {
      lists[myhash(word.getString())].add(word);   // everything is in check to add new entry
      incrementNEL();
    }
    return this;
  }
  
  public void incrementNEL() {
    nel++;
  }
  
  /** Way to implement my hash function. Uses helper method */
  public int myhash(String word) {
    return hash(word, capacity);
  }
  
  /* Helper method to myhash() */
  private int hash(String s, int capacity) {     
    int hashVal = s.hashCode() % capacity;
    return Math.abs(hashVal);     
  }
  
  /** Checks if element is in hashtable */
  public boolean contains(Word word) {
    LinkedList<Word> whichList = lists[myhash(word.getString())];
    return whichList.contains(word) ;
  }
  
  /** Calculates the average chain length of each hashtable spot */
  public double averageChain() {
    int tChainLength = 0;
    int i;
    for (i = 0; i < this.getSize(); i++) {
      tChainLength = tChainLength + this.lists[i].size();
    } return (double)tChainLength / i;
  }
  
  /** 
   * This is a test that one can use to verify certain hashtable functions. For personal use, please do not grade.
   */
  public static void main(String args[]) {
    HashTableSC ht = new HashTableSC(4);
    ht = ht.insertWFreq("ishaan".toLowerCase(),1);
    ht = ht.insertWFreq("malia",1);
    ht = ht.insertWFreq("test",1);
    ht = ht.insertWFreq("test",1);
    ht = ht.insertWFreq("test",1);
    ht = ht.insertWFreq("test",1);
    System.out.println(ht.myhash("test"));
    System.out.println(ht.getLists()[2].get(0).getFrequency());
    ht = ht.insertWFreq("love",1);
    ht = ht.insertWFreq("piddidly",1);
    ht = ht.insertWFreq("doo",1);
    String  word = null;
    Integer freq = 0;
    try {
      BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
      for (int i = 0; i < ht.getSize(); i++) {           // for every line in old hashtable
        for (Word werd : ht.getLists()[i]) {               // for each word in each list
          word = werd.getString();                           // print to output.txt
          freq = werd.getFrequency();
          writer.write("(" + word + " " + freq + ")", 0, 3 + 
                       word.length() + freq.toString().length());
          writer.flush();
          writer.newLine();
        }
      } writer.close();
    } catch (IOException e) {
      System.err.format("IOException: %s%n", e);
    }
  }   
}