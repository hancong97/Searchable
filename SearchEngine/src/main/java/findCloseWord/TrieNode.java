package findCloseWord;


import java.util.HashMap;

/**
 * The TrieNode class represents a node in the Trie Tree.
 */
class TrieNode {
    char c;
    HashMap<Character, TrieNode> children = new HashMap<Character, TrieNode>();
    boolean isLeaf;
    


    public TrieNode() {}
 



    public TrieNode(char c){
        this.c = c;
    }
}
