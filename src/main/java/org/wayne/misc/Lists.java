package org.wayne.misc;

import java.util.ArrayList;
import java.util.List;

public class Lists {
    static class Node {
        Node n;
        Node p;
        Integer k;
        Integer id;
        String msg;
        String objType;
        Object v;
    }

    static class SkipNode {
        public List<SkipNode> l = new ArrayList<>();
        public SkipNode next = null;
        public Integer k;
        public SkipNode(Integer k) {
            this.k = k;
        }
    }

    static class SkipList {
        SkipNode head = null;
        SkipNode tail = new SkipNode(null);
        int size = 0;
        
        public SkipNode addKey(int k) {
            SkipNode n = new SkipNode(k);
            size++;
            if(head == null) {
                n.next = tail;
                head = n;
                return n;
            }
            SkipNode p = null;
            SkipNode c = head;
            while(c != tail) {
                if(c.k < k) {
                    p = c;
                    c = c.next;
                } else {
                    if(c == head) {
                        n.next = head;
                        head = n;
                        return n;
                    }
                    break;
                }
            }
            p.next = n;
            n.next = c;
            return n;
        }

        public void addSkipKey(SkipNode n, SkipNode nodeKey) {
            
        }
        
        public SkipNode search(int k) {
            return null;
        }
        
        /**
         * how do you update the nodes in SkipNode.List<SkipNode>?
         * maybe have a hashmap(key, list)?
         */
        public void delete(int k) {
            
        }
    }
    
    public interface IList {
        public void add(Node n);
        public void delete(Node n);
        public Node get(Integer k);
        public void add(Node n, int idx);
        public void replace(Node n, int idx);
        public Node head();
        public Node tail();
    }

    static class LinkedListDouble implements IList {

        @Override
        public void add(Node n) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void delete(Node n) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public Node get(Integer k) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void add(Node n, int idx) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void replace(Node n, int idx) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public Node head() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Node tail() {
            // TODO Auto-generated method stub
            return null;
        }
        
    }
    
    static class LinkedListSingle implements IList  {

        @Override
        public void add(Node n) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void delete(Node n) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public Node get(Integer k) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void add(Node n, int idx) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void replace(Node n, int idx) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public Node head() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Node tail() {
            // TODO Auto-generated method stub
            return null;
        }
    }
    
    static class LinkedListSingleCircular implements IList  {

        @Override
        public void add(Node n) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void delete(Node n) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public Node get(Integer k) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void add(Node n, int idx) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void replace(Node n, int idx) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public Node head() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Node tail() {
            // TODO Auto-generated method stub
            return null;
        }
        
    }
    
    static class LinkedListDoubleCircular implements IList  {

        @Override
        public void add(Node n) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void delete(Node n) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public Node get(Integer k) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void add(Node n, int idx) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void replace(Node n, int idx) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public Node head() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Node tail() {
            // TODO Auto-generated method stub
            return null;
        }
        
    }
    
    static class Queue {
        
    }
}
