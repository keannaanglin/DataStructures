package forneymonegerie;

public class Forneymonegerie implements ForneymonegerieInterface {

    // Fields
    // ----------------------------------------------------------
    private ForneymonType[] collection;
    private int size;
    private int typeSize;
    private static final int START_SIZE = 16;


    // Constructor
    // ----------------------------------------------------------
    Forneymonegerie () {
      collection = new ForneymonType[START_SIZE];
      size = 0;
      typeSize = 0;
    }


    // Methods
    // ----------------------------------------------------------
    public boolean empty () {
        return size == 0;
    }

    public int size () {
        return size;
    }

    public int typeSize () {
      return typeSize;
    }

    public boolean collect (String toAdd) {
        int index = findString(toAdd);
        if(index >= 0){
          collection[index].count++;
          size++;
          return false;
          
        } else {
        	if (needToGrow()) { collection = grow(collection); }
            collection[typeSize] = new ForneymonType(toAdd, 1);
            typeSize++;
            size++;
            return true;
        }
    }

    public boolean release (String toRemove) {
      int index = findString(toRemove);
      if(collection[index].count > 1){
        collection[index].count--;
        size--;
        return true;
      } else if (collection[index].count == 1) {
    	  for (int i = index; i < typeSize - 1; i++) {
    		  collection[i] = collection[i +1];
    	  }
    	  typeSize--;
    	  size--;
        return true;
       
      }else {
    	  return false;
      }
    }

    public void releaseType (String toNuke) {
       int index = findString(toNuke);
       size = size - collection[index].count;
       for(int i = index; i < typeSize -1; i++) {
    	   collection[i] = collection[i + 1];
       }
       typeSize--;
    }

    public int countType (String toCount) {
        int index = findString(toCount);
        if(index == -1) {
    		return 0;
    	}
    	return collection[index].count;
    }

    public boolean contains (String toCheck) {
    	return findString(toCheck) != -1;
    }

    public String nth (int n) {    	
    	int nthNumber = 0;
    	if(n < 0 || n > size) {
    		throw new IllegalArgumentException();
    	}
    	for(int i = 0; i < typeSize; i++) {
    		for(int j = 0; j < collection[i].count; j++) {
    			if(n == nthNumber) {
    				return collection[i].type;
    			}
    			nthNumber++;
    		}
    	}
    	return "";
    }

    public String rarestType () {
    	if(typeSize == 0) {
    		return null;
    	}
    	int rarestForneymonTypeNumber = collection[0].count;
    	int indexOfRarestForneymonTypeNumber = 0;
    	for(int i = 0; i < typeSize; i++) {
    		if(collection[i].count <= rarestForneymonTypeNumber) {
    			rarestForneymonTypeNumber = collection[i].count;
    			indexOfRarestForneymonTypeNumber = i;
    		}
    	}
    	return collection[indexOfRarestForneymonTypeNumber].type;
    }

    public Forneymonegerie clone() {
    	Forneymonegerie clone = new Forneymonegerie();
    	while(collection.length >= clone.collection.length) {
    		clone.collection = grow(clone.collection);
    	}
    	for(int i = 0; i < typeSize; i++) {
    		clone.collection[i] = new ForneymonType(collection[i].type, collection[i].count);
    	}
    	clone.size = size;
    	clone.typeSize = typeSize;
    	return clone;
    }

    public void trade (Forneymonegerie other) {
    	ForneymonType[] tempCollection = collection;
    	int tempSize = size;
    	int tempTypeSize = typeSize;
    	collection = other.collection;
    	size = other.size;
    	typeSize = other.typeSize;
    	other.collection = tempCollection;
    	other.size = tempSize;
    	other.typeSize = tempTypeSize;
    }
    
    public String toString() {
    	String returnString = "[ ";
    	for(int i = 0; i < typeSize; i++) {
    		returnString += "\"" + collection[i].type + ": " + collection[i].count + "\"";
    		if(i != typeSize -1) {
    			returnString += ", ";
    		}
    	}
    	returnString += " ]";
    	return returnString;
    }

    // Static methods
    // ----------------------------------------------------------
    public static Forneymonegerie diffMon (Forneymonegerie y1, Forneymonegerie y2) {
       Forneymonegerie diffMonegerie = new Forneymonegerie();
       boolean weFoundIt = false; //need to fix tomorrow//
       for(int i = 0; i < y1.typeSize; i++) {
    	   for(int j = 0; j < y2.typeSize; j++) {
    		   if(y1.collection[i].type.equals(y2.collection[j].type)) {
    			   weFoundIt = true;
    			   if(y1.collection[i].count > y2.collection[j].count) {
    				   int counter = y2.collection[j].count;
    				   while (counter < y1.collection[i].count) {
    					   diffMonegerie.collect(y1.collection[i].type);
    					   counter++;
    				   }
    			   }
    		   }
    		   
    	   }
    	   if(!weFoundIt) {
    		   int counter = 0;
    		   while (counter < y1.collection[i].count) {
    			   diffMonegerie.collect(y1.collection[i].type);
    			   counter++;
    		   }
    	   }
    	   weFoundIt = false;
       }
       return diffMonegerie;
    }

    public static boolean sameCollection (Forneymonegerie y1, Forneymonegerie y2) {
    	if(y1.typeSize != y2.typeSize) {
    		return false;
    	}
    	boolean weFoundIt = false;
    	for(int i =0; i < y1.typeSize; i++) {
    		weFoundIt = false;
    		for(int j = 0; j < y2.typeSize; j++) {
    			if(y1.collection[i].type.equals(y2.collection[j].type) && y1.collection[i].count == y2.collection[j].count) {
    				weFoundIt = true;
    			}
    			
    			
    		}
    		if(!weFoundIt) {
    			return false;
    		}
    	}
    	return true;
    }


    // Private helper methods
    // ----------------------------------------------------------

    // TODO: Add yours here!

    public int findString(String uniqueString) {
      for(int i = 0; i < typeSize; i++) {
        if(uniqueString.equals(collection[i].type)) {
          return i;
        }
      }
      return -1;
    }
    
    public void checkAndGrow() {
    	if(typeSize == collection.length) {
    		ForneymonType[] newCollection = new ForneymonType[collection.length + 50];
    		for(int i = 0; i < collection.length; i++) {
    			newCollection[i] = collection[i];
    		}
    		collection = newCollection;
    	}
    }
    public boolean needToGrow() {
    	return typeSize == collection.length; 
    }
    
    public ForneymonType[] grow(ForneymonType[] collectionToGrow) {
    	ForneymonType[] newCollection = new ForneymonType[collectionToGrow.length + 50];
		for(int i = 0; i < collectionToGrow.length; i++) {
			newCollection[i] = collectionToGrow[i];
		}
		return newCollection;
    }

    // Private Classes
    // ----------------------------------------------------------
    private class ForneymonType {
        String type;
        int count;

        ForneymonType (String t, int c) {
            type = t;
            count = c;
        }
    }

}
