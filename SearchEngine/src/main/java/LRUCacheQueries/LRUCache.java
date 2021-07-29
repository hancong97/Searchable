package LRUCacheQueries;


import java.util.HashMap;

public class LRUCache<T> {


    private class Node<T> {
    
        private String key;
        private T value;
        
        Node prev;
        
        Node next;
        public Node (String key, T value) {
            this.key = key;
            
            this.value = value;
            prev = null;
            
            next = null;
        }
    }

    private int capacity;
    
    private HashMap<String, Node> map;
    private Node head;
    
    private Node tail;


    public LRUCache(int capacity) {
        this.capacity = capacity;
        
        map = new HashMap<String, Node>();
        head = new Node<T>(null, null);
        
        tail = new Node<T>(null, null);
        head.next = tail;
        tail.prev = head;
    }



    public T get(String key) {
    
        if (!map.containsKey(key)) {
            return null;
        }
        Node current = map.get(key);
        remove(current);
        
        add(current);
        return (T) map.get(key).value;
    }



    public void put(String key, T value) {
        if (get(key) != null) {
        
            Node current = map.get(key);
            
            current.value = value;
            
        } else {
            if (map.size() == capacity) {
            
            
                Node lfu = head.next;
                map.remove(lfu.key);
                
                
                remove(lfu);
                
                
            }
            Node newNode = new Node(key, value);
            add(newNode);
            
            
            
            map.put(key, newNode);
        }
    }






    private void remove(Node current) {
    
    
        current.prev.next = current.next;
        
        current.next.prev = current.prev;
    }

    private void add(Node current) {
        tail.prev.next = current;
        
        current.prev = tail.prev;
        current.next = tail;
        
        tail.prev = current;
    }
}
