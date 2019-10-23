package org.wayne.misc.graphs;

public class AVLTree <E> {
    BSTNode root = null;
    
    public AVLTree() {
        
    }

    private static class AVLMethods {
        /**
         * if existing key, then overwrite.
         */
        static BSTNode add(BSTNode p, BSTNode n) {
            if(p == null) {
                return n;
            }
            if(p.key() < n.key()) {
                p.r(add(p.r(), n));
                return rebalance(p);
            }
            else if(p.key() > n.key()){
                p.l(add(p.l(), n));
                return rebalance(p);
            }
            else {
                p.value(n.value());
                return p;
            }
        }

        static BSTNode rebalance(BSTNode n) {
            int hl = height(n.l());
            int hr = height(n.r());
            int hdiff = (hl < hr) ? (hr - hl) : (hl - hr);
            if(hdiff <= 1) {
                return n;
            }
            // implies h diff is greater/equal 2, so heavy side has AT LEAST height of 2.
            if(hl < hr) {
                // right heavy, rebalance either right right or right left
                BSTNode nr = n.r();
                BSTNode nrr = nr.r();
                BSTNode nrl = nr.l();
            }
            else {
                // left heavy, rebalance either left left or left right
                BSTNode nl = n.l();
                BSTNode nll = nl.l();
                BSTNode nlr = nl.r();
            }
            return null;
        }
        
        static int height(BSTNode n) {
            if(n == null) {
                return 0;
            }
            int l = height(n.l()) + 1;
            int r = height(n.r()) + 1;
            return (l > r) ? l : r;
        }
        
        static BSTNode getNode(BSTNode n, Integer key) {
            if(n == null) {
                return null;
            }
            if(n.key() < key) {
                return getNode(n.r(), key);
            }
            else if(n.key() > key) {
                return getNode(n.l(), key);
            }
            return n;
        }
    }
    
    public void add(Integer key, E value) {
        BSTNode n = new BSTNode(key).value(value).h(1);
        if(root == null) {
            root = n;
        }
        else {
            root = AVLMethods.add(root, n);
        }
    }
    
    public BSTNode getNode(Integer key) {
        return AVLMethods.getNode(root, key);
    }
    
    @SuppressWarnings("unchecked")
    public E getValue(Integer key) {
        BSTNode n = AVLMethods.getNode(root, key);
        if(n == null) {
            return null;
        }
        E e = (E)n.value();
        return e;
    }
        
    public boolean delete(Integer key) {
        return false;
    }
    
    public void printTree() {
        
    }
}
