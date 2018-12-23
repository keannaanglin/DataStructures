package dictreenary;

import java.util.ArrayList;

public class Dictreenary implements DictreenaryInterface {

    // Fields
    // -----------------------------------------------------------
    TTNode root;
    
    
    // Constructor
    // -----------------------------------------------------------
    Dictreenary () {}
    
    
    // Methods
    // -----------------------------------------------------------
    
    public boolean isEmpty() {
    	return root == null;
    }
    
    public void addWord (String toAdd) {
        toAdd = normalizeWord(toAdd);
        root = TTNode.nextNode(root, toAdd);
    }
    
    public boolean hasWord (String query) {
        query = normalizeWord(query);
        return TTNode.checkNode(query, root);
    } 
    
    public String spellCheck (String query) {
        query = normalizeWord(query);
        if(hasWord(query)) {
        	return query;
        }
        for(int i = 0; i < query.length()- 1 ; i++) {
        	String weirdWord = query.substring(0,i) + query.charAt(i + 1) + query.charAt(i) + query.substring(i+2, query.length());
        	if(hasWord(weirdWord)) {
        		return weirdWord;
        	}
        }
        return null;
        
    }
    
    public ArrayList<String> getSortedWords () {
        return getSorty(root, new ArrayList<String>(), "");
    }
    
    
    // Helper Methods
    // -----------------------------------------------------------
    
    private String normalizeWord (String s) {
        // Edge case handling: empty Strings illegal
        if (s == null || s.equals("")) {
            throw new IllegalArgumentException();
        }
        return s.trim().toLowerCase();
    }
    
    /*
     * Returns:
     *   int less than 0 if c1 is alphabetically less than c2
     *   0 if c1 is equal to c2
     *   int greater than 0 if c1 is alphabetically greater than c2
     */
    private static int compareChars (char c1, char c2) {
        return Character.toLowerCase(c1) - Character.toLowerCase(c2);
    }
    
    private static ArrayList<String> getSorty(TTNode currentNode, ArrayList<String> wordList, String almostWord) {
    	if(currentNode == null) {
    		return wordList;
    	}
    	wordList = getSorty(currentNode.left, wordList, almostWord); //look left
    	if(currentNode.wordEnd) {
    		wordList.add(almostWord + currentNode.letter);
    	}
    	wordList = getSorty(currentNode.mid, wordList, almostWord + currentNode.letter); //
    	
    	
    	wordList = getSorty(currentNode.right, wordList, almostWord);//look right 
    	return wordList;
    }
    
    private void print(TTNode node) {
    	if (node == null ) { return; }
    	System.out.println(node.letter + " " + node.wordEnd);
    	print(node.left);
    	print(node.mid);
    	print(node.right);
    }
    
    
    // TTNode Internal Storage
    // -----------------------------------------------------------
    
    /*
     * Internal storage of Dictreenary words
     * as represented using a Ternary Tree with TTNodes
     */
    private static class TTNode {
        
        boolean wordEnd;
        char letter;
        TTNode left, mid, right;
        
        TTNode (char c, boolean w) {
            letter  = c;
            wordEnd = w;
        }
        static TTNode nextNode(TTNode currentNode, String toAdd) {
        	if(toAdd.length() == 0) {
        		return null;
        	}
        	if(currentNode == null) {
        		currentNode = new TTNode(toAdd.charAt(0), toAdd.length() == 1);
        	}
        	int matchValue = compareChars(toAdd.charAt(0), currentNode.letter);
        	if(matchValue < 0) { // left
        		currentNode.left = nextNode(currentNode.left, toAdd);
        	}else if(matchValue > 0) { // right
        		currentNode.right = nextNode(currentNode.right, toAdd);
        	}else { // match
        		if(toAdd.length() == 1) {
        			currentNode.wordEnd = true;
        		}else {
        			currentNode.mid = nextNode(currentNode.mid, toAdd.substring(1, toAdd.length()));
        		}        		
        	}
        	return currentNode;
        }
        static boolean checkNode(String query, TTNode currentNode) {
        	if(currentNode == null) {
        		return false;
        	}
        	int matchValue = compareChars(query.charAt(0), currentNode.letter);
        	if(matchValue < 0) { // look left
        		return checkNode(query, currentNode.left);
        		
        	} else if (matchValue > 0) { // look right
        		return checkNode(query, currentNode.right);	
        	}else { // they match !! 
        		if(query.length() == 1) {
        			return currentNode.wordEnd;
        		}
        		return checkNode(query.substring(1,  query.length()), currentNode.mid);
        	}
        }
        
    }
    
}
