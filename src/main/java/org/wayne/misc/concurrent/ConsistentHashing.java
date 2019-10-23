package org.wayne.misc.concurrent;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.zip.CRC32;

/**
 * ConsistentHashing:
 * 
 * Used to handle the situation that when adding a new node, pre-existing
 * nodes have to reallocate their data to the new node to match a new
 * hash. In a traditional hash, all values in a node get reallocated.
 * But consistent hashing reallocates only a portion of each node's data
 * into the new node. 
 * 
 * add a node
 * remove a node
 * disable a node
 * re-enable a node (which leads to inconsistency that needs to be handled)
 * specify replication factor
 * 
 * method for put Object from Node
 * method for get Object from Node
 * method for update Object from Node
 * method for delete Object from Node
 * 
 * Objects are put, get, update, delete from Node.
 */
public class ConsistentHashing  {

    public enum DataType {
        STRING,
        INT,
        DATAOBJECT,
        DEFAULT
    }
    
    public static class DataObject {
        public Integer id;
        public Object object;
    }
    
    public static class Data {
        DataType type;
        Integer id;
        Object objData = null;

        public Data(DataType type, Object data) {
            this.type = type;
            objData = data;
        }
        
        public DataType getType() {
            return type;
        }
        
        
    }

    public static class Node {
        Node prevNode;
        Node nextNode;
        
        Integer id;
        boolean enabled = true;
        HashMap<String, Data> mapData = new HashMap<>();

        public Node(Integer id) {
            this.id = id;
        }
        
        public Integer id() {
            return id;
        }

        public boolean enabled() {
            return enabled;
        }
        
        public void enabled(boolean enable) {
            this.enabled = enable;
        }
        
        public Data get(String key) {
            if(!enabled) {
                return null;
            }
            return mapData.get(key);
        }

        public Boolean put(String key, Data value) {
            if(!enabled) {
                return null;
            }
            mapData.put(key, value);
            return true;
        }

        public Boolean remove(String key) {
            if(!enabled) {
                return null;
            }
            if(!mapData.containsKey(key)) {
                return false;
            }
            mapData.remove(key);
            return true;
        }
        
        public Node nextNode() {
            return nextNode;
        }
        
        public Node nextNode(Node node) {
            this.nextNode = node;
            return this;
        }
        
        public Node prevNode() {
            return prevNode;
        }
        
        public Node prevNode(Node node) {
            this.prevNode = node;
            return this;
        }
    }
    
    static int ctrNodeID = 0;
    final int replicaFactor;
    final LinkedHashMap<Integer, Node> mapNode = new LinkedHashMap<>();
    final TreeMap<Integer, Node> mapTree = new TreeMap<>();
    Node head;
    Node tail;
    
    public ConsistentHashing(int replicaFactor) 
    {
        this.replicaFactor = replicaFactor;
    }
    
    public int addNode() {
        int id = ctrNodeID++;
        addNode(id);
        return id;
    }
    
    public void addNode(Integer id) {
        Node node = new Node(id);
        mapNode.put(id, node);
        mapTree.put(id,  node);
        
        if(head == null) {
            head = node;
            tail = node;
        } else {
            tail.nextNode(node);
            head.prevNode(node);
            node.prevNode(tail);
            node.nextNode(head);
            tail = node;
        }
    }
    
    public void removeNode(Integer id) {
        Node node = mapNode.get(id);
        if(node == null) {
            return;
        }
        if(head == node) {
            head = head.nextNode();
            head.prevNode(tail);
            tail.nextNode(head);
        }
        if(tail == node) {
            if(head != tail) {
                tail = tail.prevNode();
                tail.nextNode(head);
                head.prevNode(tail);
            } else {
                head = null;
                tail = null;
            }
        }
        mapNode.remove(id);
        mapTree.remove(id);
    }
    
    public boolean isNodeEnabled(Integer id) {
        Node node = mapNode.get(id);
        if(node == null) {
            return false;
        }
        return node.enabled();
    }

    public void disableNode(Integer id) {
        Node node = mapNode.get(id);
        if(node == null) {
            return;
        }
        node.enabled(false);
    }
    
    public void enableNode(Integer id) {
        Node node = mapNode.get(id);
        if(node == null) {
            return;
        }
        node.enabled(true);
    }
    
    private int hash(String key) {
        CRC32 crc32 = new CRC32();
        byte [] barray = key.getBytes(StandardCharsets.UTF_8);
        crc32.update(barray);
        int ret = (int)crc32.getValue();
        return ret;
    }
    
    public Integer getNodeId(String key) {
        int hash = hash(key);
        // get first node >= hash
        int hashCeiling = mapTree.ceilingKey(hash);
        return 0;
    }
    
    public void put(String key, String value) {
        Integer id = getNodeId(key);
        Node node = mapNode.get(id);
        Data data = new Data(DataType.STRING, value);
        node.put(key, data);
    }
    
    public void put(String key, Data data) {
        Integer id = getNodeId(key);
        Node node = mapNode.get(id);
        node.put(key, data);
    }
    
    public Data get(String key) {
        Integer id = getNodeId(key);
        Node node = mapNode.get(id);
        Data data = node.get(key);
        return data;
    }
}
