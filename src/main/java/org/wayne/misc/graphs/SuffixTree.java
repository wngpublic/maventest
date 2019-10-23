package org.wayne.misc.graphs;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class SuffixTree {
    NaiveSuffixTree treeNaive = new NaiveSuffixTree();
    GeneralSuffixTree treeGeneral = new GeneralSuffixTree();
    UkkonenSuffixTree treeUkkonen = new UkkonenSuffixTree();
    
    public static enum Type {
        TRIE,
        NAIVE,
        GENERAL,
        UKKONEN,
        WHATEVER
    }
    
    public void buildTree(String s, Type t) {
        if(t == Type.TRIE) {
            
        }
        else if(t == Type.NAIVE) {
            
        }
        else if(t == Type.GENERAL) {
            
        }
        else if(t == Type.UKKONEN) {
            
        }
        else {
            
        }
    }
}

class WhateverTree {
    class Node {
        char [] a;
        int sz;
        Map<Character, Node> map = new HashMap<>();
    }
}

class TrieTree {

    class Node implements Iterable<Node> {

        Character c = null;
        Map<Character, Node> map = new HashMap<>();

        public Node(Character c) {
            this.c = c;
        }

        boolean addChild(Character c) {
            if(map.get(c) != null) {
                return false;
            }
            Node n = new Node(c);
            map.put(c,  n);
            return true;
        }

        Node getChild(Character c) {
            return map.get(c);
        }

        Character get() {
            return c;
        }
        
        class NodeIterator implements Iterator<Node> {

            Iterator<Map.Entry<Character, Node>> it = map.entrySet().iterator();
            
            @Override
            public boolean hasNext() {
                return it.hasNext();
            }
            
            @Override
            public Node next() {
                if(!it.hasNext()) {
                    return null;
                }
                Map.Entry<Character, Node> kv = it.next();
                return kv.getValue();
            }
        }
        
        @Override
        public Iterator<Node> iterator() {
            return new NodeIterator();
        }
        
    }

    Node root = new Node(null);
    
    public void add(String s) {
        
    }
    
    void p(String s, Object ...o) {
        System.out.printf(s, o);
    }

    /*
     * print simple indented format.
     */
    public void print() {
        print(root, "");
    }
    
    void print(Node n, String space) {
        if(n == null) {
            return;
        }

        p("%s%s\n",space,n.get());
        
        Iterator<Node> it = n.iterator();
        
        while(it.hasNext()) {
            print(it.next(), space+"    ");
        }
    }
}

class NaiveSuffixTree {
    
}

class GeneralSuffixTree {
    
}

class UkkonenSuffixTree {
    
}
