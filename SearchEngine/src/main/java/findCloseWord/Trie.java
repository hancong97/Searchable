package findCloseWord;

import java.util.HashMap;
import java.util.ArrayList;


public class Trie {

    private TrieNode root;


    public Trie() {
        root = new TrieNode();
    }
 

    public void insert(String word) {
        HashMap<Character, TrieNode> children = root.children;
 
        for(int i=0; i<word.length(); i++){
            char c = word.charAt(i);
 
            TrieNode t;
            if(children.containsKey(c)){
                    t = children.get(c);
            }else{
                t = new TrieNode(c);
                children.put(c, t);
            }
 
            children = t.children;

            if(i==word.length()-1)
                t.isLeaf = true;    
        }
    }
 



    public boolean search(String word) {
        TrieNode t = searchNode(word);
 
        if(t != null && t.isLeaf) 
            return true;
        else
            return false;
    }




    public ArrayList<String> getWordsStartsWith(String prefix, int threshold) {
        ArrayList<String> words = new ArrayList<String>();
        TrieNode t = searchNode(prefix);
        if (t == null) {
            return words;
        }
        searchWordsHelper(words, t, prefix, threshold);
                return words;
    }




    private void searchWordsHelper(ArrayList<String> words, TrieNode t, String word, int threshold) {
        if (words.size() >= threshold) {
            return;
        }

        if (t.isLeaf == true) {
            words.add(word);
        }
        for (char c : t.children.keySet()) {
            searchWordsHelper(words, t.children.get(c), word + c, threshold);
        }
    }

    public boolean startsWith(String prefix) {
        if(searchNode(prefix) == null) {
            return false;
        } else {
            return true;
        }
    }


    private TrieNode searchNode(String str){
        HashMap<Character, TrieNode> children = root.children; 
        TrieNode t = null;
        
        
        for(int i=0; i<str.length(); i++){
            char c = str.charAt(i);
            
            
            if(children.containsKey(c)){
                t = children.get(c);
                children = t.children;
            }else{
                return null;
            }
        }
 
        return t;
    }

    public static void main(String args[]) {
    	Trie lexicon = new Trie();

        lexicon.insert("brake");
        lexicon.insert("service");

        System.out.println(lexicon.search("brake"));
        System.out.println(lexicon.search("bra"));
        
        
        System.out.println(lexicon.search("serve"));


        System.out.println(lexicon.startsWith("bra"));
        
        
        System.out.println(lexicon.startsWith("sea"));

        lexicon.insert("bra");
        
        
        lexicon.insert("brave");
        System.out.println(lexicon.search("bra"));
        
        
        ArrayList<String> words = lexicon.getWordsStartsWith("bra", 10);
        for (String w : words) {
            System.out.println(w);
        }
    	
    }
}