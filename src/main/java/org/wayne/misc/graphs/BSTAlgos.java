package org.wayne.misc.graphs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.wayne.misc.Utils;


public class BSTAlgos extends BST {
    public BSTAlgos() {
        super();
    }
    public BSTAlgos(BST bst) {
        super();
        setTree(bst);
    }
    /**
     * Given a, find right child's leftmost child, which is d here.
     * Return null only if a or b is null.
     * 
     *        a
     *              b
     *            c
     *          d
     */
    public BSTNode findRightLeftmost(BSTNode n) {
        if(n == null || n.r() == null) {
            return null;
        }
        BSTNode c = n.r();
        while(c.l() != null) {
            c = c.l();
        }
        return c;
    }
    
    /**
     * Given a, find left child's rightmost child, which is d here.
     * return null only if a or b is null.
     * 
     *            a
     *      b
     *        c
     *          d
     */
    public BSTNode findLeftRightmode(BSTNode n) {
        if(n == null || n.l() == null) {
            return null;
        }
        BSTNode c = n.l();
        while(c.r() != null) {
            c = c.r();
        }
        return c;
    }
    
    /**
     * Given value v, look from root for next largest node.
     */
    public BSTNode findNextLargestNode(int v) {
        class Container {
            BSTNode n = null;
            int v = 0;
        }
        class CInternal {
            BSTNode findNextLargest(BSTNode n, Container container) {
                if(n == null) {
                    return null;
                }
                if(n.v() == container.v) {
                    // 2 cases: next largest is rc->leftmost or a parent. 
                    container.n = n;
                    if(n.r() != null) {
                        BSTNode c = n.r();
                        while(c.l() != null) {
                            c = c.l();
                        }
                        return c;
                    }
                }
                else if(n.v() < container.v) {
                    BSTNode c = findNextLargest(n.r(), container);
                    if( c == null && 
                        container.n != null && 
                        n.v() > container.v) 
                    {
                        return n;
                    }
                }
                else {
                    BSTNode c = findNextLargest(n.l(), container);
                    if( c == null &&
                        container.n != null &&
                        n.v() > container.v)
                    {
                        return n;
                    }
                }
                return null;
            }
            BSTNode findNextLargestIterative(BSTNode nIn, int v) {
                BSTNode n = nIn;
                LinkedList<BSTNode> stack = new LinkedList<>();
                while(n != null) {
                    if(n.v() == v) {
                        if(n.r() != null) {
                            n = n.r();
                            while(n.l() != null) {
                                n = n.l();
                            }
                            return n;
                        }
                        else {
                            while(stack.size() != 0) {
                                n = stack.pop();
                                if(n.v() > v) {
                                    return n;
                                }
                            }
                        }
                    }
                    else if(n.v() < v) {
                        stack.push(n);
                        n = n.r();
                    }
                    else {
                        stack.push(n);
                        n = n.l();
                    }
                }
                return null;
            }
            BSTNode findNextLargest(BSTNode n, int v, int methodtype) {
                if(methodtype == 0) {
                    Container container = new Container();
                    container.v = v;
                    return findNextLargest(n, container);
                }
                else {
                    return findNextLargestIterative(n, v);
                }
            }
        }
        CInternal cinternal = new CInternal();
        return cinternal.findNextLargest(r, v, 0);
    }
    
    /**
     * Given node v, look from node for next largest node. This
     * assumes parent access.
     */
    public BSTNode findNextLargestNode(BSTNode n) {
        class CInternal {
            BSTNode findNextLargestIterative(BSTNode n) {
                if(n == null || n.r() == null && n.p() == null) {
                    return null;
                }
                if(n.r() != null) {
                    BSTNode r = n.r();
                    while(r.l() != null) {
                        r = r.l();
                    }
                    return r;
                }
                else {
                    BSTNode p = n.p();
                    while(p != null) {
                        if(p.v() > n.v()) {
                            return p;
                        }
                        p = p.p();
                    }
                }
                return null;
            }
            BSTNode findNextLargestRecursive(BSTNode n) {
                if(n == null || n.r() == null && n.p() == null) {
                    return null;
                }
                if(n.r() != null) {
                    return findRightLeftmost(n);
                }
                BSTNode p = n.p();
                while(p != null) {
                    if(p.v() > n.v()) {
                        return p;
                    }
                    p = p.p();
                }
                return null;
            }
            BSTNode findNextLargest(BSTNode n, int methodtype) {
                // this assumes no access to root ptr
                if(!enParentPointer) {
                    p("enParentPtr == null\n");
                    return null;
                }
                if(methodtype == 0) {
                    return findNextLargestRecursive(n);
                }
                return findNextLargestIterative(n);
            }
        }
        CInternal internal = new CInternal();
        BSTNode c = internal.findNextLargest(n, 0);
        return c;
    }
    
    /**
     * given list of unique integers, generate random BST.
     */
    public static BST generateRandomBST(
        List<Integer> l, 
        boolean enParentPointer,
        boolean enShuffle)
    {
        // validate input set for uniqueness
        {
            Set<Integer> set = new HashSet<>();
            for(Integer i: l) {
                if(set.contains(i)) {
                    p("duplicate detected in generateRandomBST\n");
                    return null;
                }
                set.add(i);
            }
        }
        final List<Integer> linput;

        if(enShuffle) {
            linput = new ArrayList<>(l);
            Utils u = new Utils();
            u.shuffle(linput);
        } else {
            linput = l;
        }

        BST bst = new BST(enParentPointer);
        for(Integer i: linput) {
            bst.addNode(i);
        }
        return bst;
    }
}
