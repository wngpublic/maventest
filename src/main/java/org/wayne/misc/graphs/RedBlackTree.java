package org.wayne.misc.graphs;

import java.util.LinkedList;
import java.util.List;

import org.wayne.misc.Obj;

/**
 * RedBlackTree:
 * 
 * Each node has left and right child.
 * 
 * root is always black
 * 
 * the children of a red node are black
 * 
 * for each node with at least one null child, the number of black nodes from
 * root to null child is the same.
 * 
 * there are no two adjacent red nodes (no node-parent node-child is red-red) 
 *
 */
public class RedBlackTree extends BST {
    final static BSTNode.COLOR NONE = BSTNode.COLOR.NONE;
    final static BSTNode.COLOR RED = BSTNode.COLOR.RED;
    final static BSTNode.COLOR BLACK = BSTNode.COLOR.BLACK;
    private static class ObjBSTNode {
        private BSTNode n;
        BSTNode get() {
            return n;
        }
        void set(BSTNode n) {
            this.n = n;
        }
    }
    private static class Methods {
        /**
         * if key exists, just overwrite for now.
         * @param p
         * @param n
         * @return
         */
        static BSTNode put(BSTNode p, BSTNode n) {
            if(p == null) {
                return n;
            }
            else if(p.id() < n.id()) {
                p.r(put(p.r(), n));
                return rotate(p);
            }
            else if(p.id() > n.id()) {
                p.l(put(p.l(), n));
                return rotate(p);
            }
            p.value(n.value());
            return p;
        }

        static BSTNode rotate(BSTNode n) {
            if(n == null || n.l() == null && n.r() == null) {
                return n;
            }
            BSTNode nc = n;
            BSTNode nl = n.l();
            BSTNode nr = n.r();
            BSTNode.COLOR colorL = (nl == null) ? NONE : nl.color();
            BSTNode.COLOR colorR = (nr == null) ? NONE : nr.color();
            
            if(n.color() != colorL && n.color() != colorR) {
                return n;
            }

            if(colorL == RED && colorR == RED) {
                n.color(BLACK);
            }
            else if(colorL == RED) {
                BSTNode nll = nl.l();
                BSTNode nlr = nl.r();
                BSTNode.COLOR colorLL = (nll == null) ? NONE : nll.color();
                BSTNode.COLOR colorLR = (nlr == null) ? NONE : nlr.color();
                
                if(colorLL == RED && colorLR == RED) {
                    nl.color(BLACK);
                }
                else if(colorLL == RED) {
                    // L == RED && LL == RED -> L == RED && R == RED
                    n.l(nl.r());
                    nl.r(n);
                    nc = nl;
                }
                else if(colorLR == RED) {
                    // L == RED && LR == RED -> L == RED && R == RED
                    nl.r(null);
                    nlr.l(nl);
                    nlr.r(n);
                    n.l(null);
                    nc = nlr;
                }
            }
            else if(colorR == RED) {
                BSTNode nrr = nr.r();
                BSTNode nrl = nr.l();
                BSTNode.COLOR colorRR = (nrr == null) ? NONE : nrr.color();
                BSTNode.COLOR colorRL = (nrl == null) ? NONE : nrl.color();
                
                if(colorRR == RED && colorRL == RED) {
                    nr.color(BLACK);
                }
                else if(colorRR == RED) {
                    // R == RED && RR == RED -> L == RED && R == RED
                    n.r(nr.l());
                    nr.l(n);
                    nc = nr;
                }
                else if(colorRL == RED) {
                    // R == RED && RL == RED -> L == RED && R == RED
                    nr.l(null);
                    nrl.r(nr);
                    nrl.l(n);
                    n.r(null);
                    nc = nrl;
                }
            }
            return nc;
        }
        
        /**
         *                  50
         *          25               75
         *     15       35      65        85
         *   10  20   30  40  60  70   80    90
         * 
         * 33
         * 18
         * 27
         */
        static BSTNode get(BSTNode n, Integer k) {
            if(n == null) {
                return null;
            }
            if(n.key() < k) {
                return get(n.r(), k);
            }
            else if(n.key() > k) {
                return get(n.l(), k);
            }
            else {
                return n;
            }
        }
        
        /**
         * return node that is equal to or less than k.
         */
        static BSTNode getFloor(ObjBSTNode objNode, BSTNode n, Integer k) {
            if(n == null) {
                return objNode.get();
            }
            if(n.key() == k) {
                return n;
            }
            else if(n.key() < k) {
                if(objNode.get() == null || objNode.get().key() < n.key()) {
                    objNode.set(n);
                }
                return getFloor(objNode, n.r(), k);
            }
            else if(n.key() > k) {
                return getFloor(objNode, n.l(), k);
            }
            return objNode.get();
        }
        
        /**
         * return node that is equal to or greater than k.
         */
        static BSTNode getCeil(ObjBSTNode objNode, BSTNode n, Integer k) {
            if(n == null) {
                return objNode.get();
            }
            if(n.key() == k) {
                return n;
            }
            else if(n.key() < k) {
                return getCeil(objNode, n.r(), k);
            }
            else if(n.key() > k) {
                if(objNode.get() == null || objNode.get().key() > n.key()) {
                    objNode.set(n);
                }
                return getCeil(objNode, n.l(), k);
            }
            return objNode.get();
        }
        
        static boolean isBalancedTree(
            int curHeight,
            Obj.ObjInteger objMin, 
            Obj.ObjInteger objMax,
            BSTNode n) 
        {
            if(n == null) {
                if(objMin.v() == null || objMax.v() == null) {
                    objMin.v(curHeight);
                    objMax.v(curHeight);
                    return true;
                }
                else if(curHeight < objMin.v()) {
                    objMin.v(curHeight);
                }
                else if(curHeight > objMax.v()) {
                    objMax.v(curHeight);
                }
                if((objMax.v() - objMin.v()) > 1) {
                    return false;
                }
                return true;
            }
            boolean vl = isBalancedTree(curHeight+1,objMin,objMax,n.l());
            boolean vr = isBalancedTree(curHeight+1,objMin,objMax,n.r());
            return (vl && vr);
        }
        
        /**
         * 
         *                  50
         *          25               75
         *     15       35      65        85
         *   10  20   30  40  60  70   80    90
         * 
         */
        static boolean delete(BSTNode n, Integer k) {
            return false;
        }
        
        /**
         * construct in order list.
         * 
         *                  50
         *          25               75
         *     15       35      65        85
         *   10  20   30  40  60  70   80    90
         * 
         * 20,60
         *               20    30 35 40 50 60
         *      50,25,15,20
         *      50,25,      35,30
         *      50,25,      40
         *      75 65 60
         * 35,65
         */
        static void getRange(
            BSTNode nLo, 
            BSTNode nHi, 
            BSTNode n, 
            List<BSTNode> list) 
        {
            if(n == null) {
                return;
            }
            if(n.key() > nLo.key()) {
                getRange(nLo, nHi, n.l(), list);
            }
            if(n.key() >= nLo.key() && n.key() <= nHi.key()) {
                list.add(n);
            }
            if(n.key() < nHi.key()) {
                getRange(nLo, nHi, n.r(), list);
            }
        }
    }
    
    public void put(Integer k, Integer v) {
        if(r == null) {
            BSTNode n = new BSTNode(k, k, v, BSTNode.COLOR.BLACK);
            r = n;
        }
        else {
            BSTNode n = new BSTNode(k, k, v, BSTNode.COLOR.RED);
            r = Methods.put(r, n);
        }
    }
    
    /**
     * get first K <= k
     */
    public BSTNode getFloor(Integer k) {
        ObjBSTNode objNode = new ObjBSTNode();
        BSTNode n = Methods.getFloor(objNode, r, k);
        return n;
    }
    
    /**
     * get first K >= k
     */
    public BSTNode getCeiling(Integer k) {
        ObjBSTNode objNode = new ObjBSTNode();
        BSTNode n = Methods.getCeil(objNode, r, k);
        return n;
    }
    
    public BSTNode get(Integer k) {
        return Methods.get(r, k);
    }
        
    public Boolean delete(Integer k) {
        return Methods.delete(r, k);
    }
    
    public List<BSTNode> getRange(Integer keyLo, Integer keyHi) {
        LinkedList<BSTNode> list = new LinkedList<>();
        ObjBSTNode objMin = new ObjBSTNode();
        ObjBSTNode objMax = new ObjBSTNode();
        BSTNode nLo = Methods.getFloor(objMin, r, keyLo);
        BSTNode nHi = Methods.getCeil(objMax, r, keyHi);

        Methods.getRange(nLo, nHi, r, list);
        return list;
    }
    
    public Integer height() {
        Obj.ObjInteger objMin = new Obj.ObjInteger();
        Obj.ObjInteger objMax = new Obj.ObjInteger();
        Methods.isBalancedTree(0, objMin, objMax, r);
        return objMax.v();
    }
    
    /**
     * balanced is defined by max height is within 1 of min height
     */
    public boolean isBalancedTree() {
        return Methods.isBalancedTree(
            0, 
            new Obj.ObjInteger(), 
            new Obj.ObjInteger(), 
            r);
    }
}
