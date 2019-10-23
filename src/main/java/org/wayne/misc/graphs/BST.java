package org.wayne.misc.graphs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.wayne.misc.Utils;

/**
 * List of public methods:
 * 
 * boolean          addNode(BSTNode)
 * boolean          addNode(BSTNode, BSTNode, boolean)
 * boolean          addNode(int)
 * void             addTree(BSTNode);
 * boolean          compareTree(BST);
 * boolean          compareSubTree(BSTNode, BSTNode)
 * List<Integer>    convertBSTToVal(List<BSTNode>)
 * List<BSTNode>    getLevelNodes(int)
 * BSTNode          getNode(int)
 * BSTNode          getRoot()
 * List<BSTNode>    inorder()
 * List<BSTNode>    inorderIterative()
 * boolean          isBalanced()
 * List<BSTNode>    levelorder()
 * int              maxLevels()
 * int              maxWidth()
 * int              numNodes()
 * List<BSTNode>    postorder()
 * List<BSTNode>    postorderIterative()
 * List<BSTNode>    preorder()
 * List<BSTNode>    preorderIterative()
 * void             print()
 * void             print(List<BSTNode>)
 * void             printLevelOrder()
 * void             reverseLeafNodes()
 * void             reverseNodes()
 * void             setNode(BSTNode, BSTNode, boolean)
 * 
 * @author wayneng
 *
 */
public class BST {

    BSTNode r = null;
    
    Map<Integer, BSTNode> map = new HashMap<>();
    
    Utils utils = new Utils();
    
    int idCtr = 0;
    
    boolean enParentPointer = false;
    
    boolean isIncompleteParentPointer = false;
    
    public BST() {
        
    }
    
    public BST(boolean enParentPointer) {
        this.enParentPointer = enParentPointer;
    }
    
    public BSTNode getRoot() {
        return r;
    }
    
    public boolean enParentPointer() {
        return enParentPointer;
    }

    protected static void p(String f, Object ...o) {
        System.out.printf(f, o);
    }

    public BSTNode getNode(int k) {
        if(map.containsKey(k)) {
            return map.get(k);
        }
        return null;
    }
    public void setTree(BST tree) {
        this.enParentPointer = tree.enParentPointer();
        setTree(tree.getRoot());
    }

    public void setTree(BSTNode root) {
        map.clear();
        r = null;
        setTreeInternal(root);
    }
    
    protected void setTreeInternal(BSTNode n) {
        if(n == null) {
            return;
        }
        
        if(r == null) {
            r = n;
        }
        
        if(map.containsKey(n.key())) {
            map.clear();
            r = null;
            return;
        }
        
        map.put(n.key(), n);
        
        setTreeInternal(n.l());
        setTreeInternal(n.r());
    }
    
    public boolean compareTree(BST bstOther) {
        if(bstOther == null) {
            return false;
        }
        
        BSTNode ro = bstOther.getRoot();
        
        if(ro == null) {
            return (r == null);
        }
        
        return compareSubTree(r, ro);
    }
    
    public boolean compareSubTree(BSTNode n, BSTNode o) {
        if(n == null && o != null || n != null && o == null) {
            return false;
        }
        
        if(n == null && o == null) {
            return true;
        }
        
        if(n.key() != o.key()) {
            return false;
        }
        
        boolean lcmp = compareSubTree(n.l(), o.l());
        boolean rcmp = compareSubTree(n.r(), o.r());
        
        return (lcmp && rcmp);
    }
    
    public boolean addNode(BSTNode n) {
        if(n == null) {
            return false;
        }
        
        Integer k = n.key();
        
        if(map.containsKey(k)) {
            return false;
        }
        
        map.put(k, n);
        
        if(enParentPointer) {
            BSTNode c = BSTHelper.addNodeWithParent(r,  n);
            // if already have root, and is direct child, then set parent.
            // c can be r || n || r.lc || r.rc
            if(r != null && n == c) {
                n.p(r);
            }
            r = c;
        } else {
            r = BSTHelper.addNode(r, n);
        }
        
        return true;
    }
    
    public BSTNode addNode(int val) {
        BSTNode n = new BSTNode(++idCtr, val, val);
        if(addNode(n)) {
            return n;
        }
        return null;
    }
    
    public boolean addNode(BSTNode p, BSTNode n, boolean isLeft) {
        if(n == null || p == null) {
            return false;
        }
        
        Integer k = n.key();
        
        if(!map.containsKey(k)) {
            map.put(k, n);
        }
        
        setNode(p, n, isLeft);
        
        return true;
    }
    
    public void setNode(BSTNode p, BSTNode n, boolean isLeft) {
        if(p == null || n == null) {
            return;
        }
        
        if(isLeft) {
            p.l(n);
        }
        else {
            p.r(n);
        }
    }
    
    public boolean deleteNode(BSTNode n) {
        r = BSTHelper.deleteNode(null, r, n);
        return true;
    }
    
    public int numNodes() {
        return map.size();
    }
    
    /*
     * reverseLeafNodes example:
     * 
     * example1:
     * before:
     *        20  
     *    15      25
     *  13  17  23  27 <- leaf
     * 
     * after:
     *        20  
     *    15      25
     *  27  23  17  13 <- leaf
     * 
     * example2:
     * before:
     *        20  
     *    15      25
     *      17  23     <- leaf
     * 
     * after:
     *        20  
     *    15      25
     *      23  17     <- leaf
     */
    
    public void reverseLeafNodes() {
        if(r == null) {
            return;
        }
        BSTHelper.reverseLeafNodes(r, r.l(), r, r.r());
    }
    
    public void reverseNodes() {
        if(r == null) {
            return;
        }
        BSTHelper.reverseNodes(r);
    }
    
    
    public List<BSTNode> getLevelNodes(int level) {
        /*
         * 40 N 20 60 N 10 30 50 70 N 5 15 25 35 45 55 65 75
         */
        List<BSTNode> l = new ArrayList<>();
        
        if(r == null) {
            return l;
        }
        
        LinkedList<BSTNode> ll = new LinkedList<>();
        int curlevel = 0;
        
        ll.add(r);
        
        while(ll.size() != 0 || curlevel > level) {
            BSTNode n = ll.poll();
            
            if(n == null) {
                ll.add(null);
                curlevel++;
            }
            else {
                l.add(n);
            }            
            
            if(ll.size() == 0) {
                ll.add(null);
            }
            
            BSTNode nl = n.l();
            BSTNode nr = n.r();
            
            if(nl != null) {
                ll.add(nl);
            }
            if(nr != null) {
                ll.add(nr);
            }
        }
        
        return l;
    }
    
    public int maxLevels() {
        return maxLevels(r);
    }
    
    protected int maxLevels(BSTNode n) {
        if(n == null) {
            return 0;
        }
        int l = maxLevels(n.l()) + 1;
        int r = maxLevels(n.r()) + 1;
        
        return (l < r) ? r : l;
    }
    
    public int maxWidth() {
        AtomicInteger max = new AtomicInteger(0);
        BSTHelper.maxWidth(r, max);
        return max.get();
    }
    
    public boolean isBalanced() {
        int depth = BSTHelper.isBalancedDepth(r, 0);
        return (depth == -1);
    }
    
    public List<BSTNode> preorder() {
        List<BSTNode> list = new ArrayList<>();
        BSTHelper.preorder(r, list);
        return list;
    }
    
    public List<BSTNode> preorderIterative() {
        List<BSTNode> list = new ArrayList<>();
        BSTHelper.preorderIterative(r, list);
        return list;
    }
    
    public List<BSTNode> postorder() {
        List<BSTNode> list = new ArrayList<>();
        BSTHelper.postorder(r, list);
        return list;
    }
    
    public List<BSTNode> postorderIterative() {
        List<BSTNode> list = new ArrayList<>();
        BSTHelper.postorderIterative(r, list);
        return list;
    }
    
    public List<BSTNode> inorder() {
        List<BSTNode> list = new ArrayList<>();
        BSTHelper.inorder(r, list);
        return list;
    }

    public List<BSTNode> inorderIterative() {
        List<BSTNode> list = new ArrayList<>();
        BSTHelper.inorderIterative(r, list);
        return list;
    }

    public List<BSTNode> levelorder() {
        List<BSTNode> list = new ArrayList<>();
        LinkedList<BSTNode> ll = new LinkedList<>();
        
        if(r != null) {
            ll.add(r);
            while(ll.size() != 0) {
                BSTNode n = ll.poll();
                list.add(n);
                BSTNode l = n.l();
                BSTNode r = n.r();
                if(l != null) {
                    ll.add(l);
                }
                if(r != null) {
                    ll.add(r);
                }
            }
        }
        
        return list;
    }
    
    public void printLevelOrder() {
        /*
         * 40 N 20 60 N 10 30 50 70 N 5 15 25 35 45 55 65 75 N
         */

        List<BSTNode> list = new ArrayList<>();
        LinkedList<BSTNode> ll = new LinkedList<>();
        
        if(r == null) {
            return;
        }

        int level = 1;
        ll.add(r);
        ll.add(null);
        
        while(ll.size() != 0) {
            BSTNode n = ll.poll();
            
            if(n == null) {
                p("---- LEVEL %d\n", level);
                print(list);
                list.clear();
                level++;
                
                ll.add(null);
                
                n = ll.poll();
                if(n == null) {
                    break;
                }
            }

            list.add(n);
                        
            BSTNode l = n.l();
            BSTNode r = n.r();
            if(l != null) {
                ll.add(l);
            }
            if(r != null) {
                ll.add(r);
            }
        }
        
    }
    
    public List<BSTNode> serialize() {
        return null;
    }
    
    public BST deserialize(List<BSTNode> list) {
        return null;
    }
    
    public List<Integer> convertBSTToVal(List<BSTNode> list) {
        List<Integer> l = new ArrayList<>();
        for(BSTNode n: list) {
            l.add(n.v());
        }
        return l;
    }
    
    public void print(List<BSTNode> list) {
        for(int i = 0; i < list.size(); i++) {
            BSTNode n = list.get(i);
            p("%2d: ", i);
            n.print();
        }
    }
    
    public void print() {
        if(r == null) {
            return;
        }
        List<BSTNode> listpre = preorder();
        List<BSTNode> listin  = inorder();
        p("PREORDER-----------\n");
        print(listpre);
        p("\n");
        p("INORDER------------\n");
        print(listin);
    }
    
    public void printValues() {
        if(r == null) {
            return;
        }
        List<Integer> listpre  = convertBSTToVal(preorder());
        List<Integer> listin   = convertBSTToVal(inorder());
        List<Integer> listpost = convertBSTToVal(postorder());
        p("PREORDER:  ");
        utils.printListInt(listpre, false);
        p("INORDER:   ");
        utils.printListInt(listin, false);
        p("POSTORDER: ");
        utils.printListInt(listpost, false);
    }
    
    static class BSTHelper {    
        static BSTNode addNodeWithParent(BSTNode p, BSTNode n) {
            if(p == null) {
                return n;
            }
            int vc = n.key();
            int vp = p.key();
            
            if     (vc < vp) {
                BSTNode c = addNodeWithParent(p.l(), n);
                if(c == n) {
                    n.p(p);
                }
                p.l(c);
            }
            else if(vc > vp) {
                BSTNode c = addNodeWithParent(p.r(), n);
                if(c == n) {
                    n.p(p);
                }
                p.r(c);
            }
            return p;
        }
        static BSTNode addNode(BSTNode p, BSTNode n) {
            if(p == null) {
                return n;
            }
            int vc = n.key();
            int vp = p.key();
            
            if     (vc < vp) {
                p.l(addNode(p.l(), n));
            }
            else if(vc > vp) {
                p.r(addNode(p.r(), n));
            }
            return p;
        }

        static void reverseLeafNodes(BSTNode pl, BSTNode l, BSTNode pr, BSTNode r) {

            if(pl == null || pr == null) {
                return;
            }
            
            // must be symmetrical, fully populated tree
            if(l == null && r != null || l != null && r == null) {
                return;
            }
            
            if(l == null && r == null) {
                return;
            }

            reverseLeafNodes(l, l.l(), r, r.r());
            reverseLeafNodes(l, l.r(), r, r.l());
            
            // if all leaf nodes of current are null, then l and r are leaf nodes
            if(l.l() == null && l.r() == null && r.l() == null && r.r() == null) {
                if(pl.l() == l && pr.r() == r) {
                    pl.l(r);
                    pr.r(l);
                }
                else {
                    pl.r(r);
                    pr.l(l);
                }
            }
            
        }

        static void reverseNodes(BSTNode n) {
            if(n == null) {
                return;
            }

            BSTNode l = n.l();
            BSTNode r = n.r();

            reverseNodes(l);
            reverseNodes(r);

            n.l(r);
            n.r(l);
        }

        static int maxWidth(BSTNode n, AtomicInteger max) {
            if(n == null) {
                return 0;
            }
            
            int l = maxWidth(n.l(), max);
            int r = maxWidth(n.r(), max);
            
            int w = l+r+1;
            
            if(w > max.get()) {
                max.set(w);
            }
            
            return (l < r) ? r+1 : l+1;
        }

        static void inorderIterative(BSTNode n, List<BSTNode> list) {
            /*
             *               40
             *       20              60
             *   10      30      50      70
             *  
             *  PREORDER:   40  20  10  30  60  50  70 
             *  INORDER:    10  20  30  40  50  60  70 
             *  POSTORDER:  10  30  20  50  70  60  40 
             *  
             */

            LinkedList<StackData> ll = new LinkedList<>();
            
            StackData d = new StackData(n, n.l(), n.r());
            ll.push(d);
                    
            while(ll.size() != 0) {
                d = ll.pop();
                BSTNode c = d.c();
                BSTNode l = d.l();
                BSTNode r = d.r();
                
                if(l != null) {
                    d.l(null);
                    ll.push(d);
                    d = new StackData(l, l.l(), l.r());
                    ll.push(d);
                }
                else if(c != null) {
                    list.add(c);
                    d.c(null);
                    if(!d.isNull()) {
                        ll.push(d);
                    }
                }
                else if(r != null) {
                    d = new StackData(r, r.l(), r.r());
                    ll.push(d);
                }
            }
        }

        static void inorder(BSTNode n, List<BSTNode> list) {
            if(n == null) {
                return;
            }
            inorder(n.l(), list);
            list.add(n);
            inorder(n.r(), list);
        }

        static void postorderIterative(BSTNode n, List<BSTNode> list) {
            /*
             *               40
             *       20              60
             *   10      30      50      70
             *  
             *  PREORDER:   40  20  10  30  60  50  70 
             *  INORDER:    10  20  30  40  50  60  70 
             *  POSTORDER:  10  30  20  50  70  60  40 
             *  
             */

            LinkedList<StackData> ll = new LinkedList<>();
            
            StackData d = new StackData(n, n.l(), n.r());
            ll.push(d);
                    
            while(ll.size() != 0) {
                d = ll.pop();
                BSTNode c = d.c();
                BSTNode l = d.l();
                BSTNode r = d.r();
                
                if(l != null) {
                    d.l(null);
                    ll.push(d);
                    d = new StackData(l, l.l(), l.r());
                    ll.push(d);
                }
                else if(r != null) {
                    d.r(null);
                    ll.push(d);
                    d = new StackData(r, r.l(), r.r());
                    ll.push(d);
                }
                else {
                    list.add(c);
                }
            }
        }

        static void postorder(BSTNode n, List<BSTNode> list) {
            if(n == null) {
                return;
            }
            postorder(n.l(), list);
            postorder(n.r(), list);
            list.add(n);
        }

        static void preorderIterative(BSTNode n, List<BSTNode> list) {
            /*
             *               40
             *       20              60
             *   10      30      50      70
             *  
             *  PREORDER:   40  20  10  30  60  50  70 
             *  INORDER:    10  20  30  40  50  60  70 
             *  POSTORDER:  10  30  20  50  70  60  40 
             */

            LinkedList<StackData> ll = new LinkedList<>();
            
            StackData d = new StackData(n, n.l(), n.r());
            ll.push(d);
                    
            while(ll.size() != 0) {
                d = ll.pop();
                BSTNode c = d.c();
                BSTNode l = d.l();
                BSTNode r = d.r();
                
                if(c != null) {
                    list.add(c);
                    d.c(null);
                }
                
                if(l != null) {
                    d.l(null);
                    ll.push(d);
                    d = new StackData(l, l.l(), l.r());
                    ll.push(d);
                }
                else if(r != null) {
                    d.r(null);
                    d = new StackData(r, r.l(), r.r());
                    ll.push(d);
                }
            }
        }

        static void preorder(BSTNode n, List<BSTNode> list) {
            if(n == null) {
                return;
            }
            list.add(n);
            preorder(n.l(), list);
            preorder(n.r(), list);
        }

        static int isBalancedDepth(BSTNode n, int curdepth) {
            if(n == null) {
                return curdepth;
            }
            
            int l = isBalancedDepth(n.l(), curdepth+1);
            int r = isBalancedDepth(n.r(), curdepth+1);
            
            if(l == -1 || r == -1) {
                return -1;
            }
            
            return (l == r) ? curdepth : -1;
        }
        
        static BSTNode getRightMostParent(BSTNode p, BSTNode n) {
            if(n == null) {
                return p;
            }
            if(n.r() == null) {
                return p;
            }
            return getRightMostParent(n, n.r());
        }
        
        static BSTNode getRightMostChild(BSTNode n) {
            if(n == null) {
                return null;
            }
            if(n.r() == null) {
                return n;
            }
            return getRightMostChild(n.r());
        }
        
        static BSTNode getLeftMostChild(BSTNode n) {
            if(n == null) {
                return null;
            }
            if(n.l() == null) {
                return n;
            }
            return getLeftMostChild(n.l());
        }
        
        static BSTNode getLeftMostParent(BSTNode p, BSTNode n) {
            if(n == null) {
                return p;
            }
            if(n.l() == null) {
                return p;
            }
            return getLeftMostParent(n, n.l());
        }
        
        /**
         * if 2 children get either left child rightmost or right child 
         * leftmost. choose leftmost of right child in this case.
         * ---------------------------
         *               40
         *       20              60
         *   10      30      50      70
         */
        static BSTNode deleteNode(BSTNode p, BSTNode c, BSTNode n) {
            if(c == null) {
                return c;
            }
            if     (c.key() < n.key()) {
                return deleteNode(c, c.r(), n);
            }
            else if(c.key() > n.key()) {
                return deleteNode(c, c.l(), n);
            }

            if(c.r() == null && c.l() == null) {
                return null;
            }
            else if(c.r() == null) {
                return c.l();
            }
            else if(c.l() == null) {
                return c.r();
            }
            /* 
             * get right child's left most node and left most node's
             * parent. the left most node's parent sets left child
             * as left most node's right child. left most node
             * becomes replacement node.
             */
            BSTNode nleftmostparent = c.r();
            BSTNode nleftmost = c.r().l();
            while(nleftmost != null && nleftmost.l() != null) {
                nleftmostparent = nleftmost;
                nleftmost = nleftmost.l();
            }
            if(nleftmost == null) {
                return nleftmostparent;
            }
            nleftmostparent.l(nleftmost.r());
            nleftmost.l(c.l());
            nleftmost.r(c.r());
            return nleftmost;
        }
    }
    
    static class StackData {
        public BSTNode c;
        public BSTNode l;
        public BSTNode r;
        
        public StackData(BSTNode c, BSTNode l, BSTNode r) {
            this.c = c;
            this.l = l;
            this.r = r;
        }
        
        public void c(BSTNode c) {
            this.c = c;
        }
        
        public void l(BSTNode l) {
            this.l = l;
        }
        
        public void r(BSTNode r) {
            this.r = r;
        }
        
        public BSTNode c() {
            return c;
        }
        
        public BSTNode l() {
            return l;
        }

        public BSTNode r() {
            return r;
        }
        
        public boolean isNull() {
            return (c == null && l == null && r == null);
        }
    }

}
