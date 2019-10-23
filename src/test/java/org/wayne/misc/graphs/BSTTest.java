package org.wayne.misc.graphs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import junit.framework.TestCase;

import org.wayne.misc.Utils;

public class BSTTest extends TestCase {
    class BSTAndList {
        BST bst;
        List<BSTNode> listNodes;
        List<Integer> listVals;
        Map<Integer, BSTNode> map;
    }
    public static void p(String f, Object ...o) {
        System.out.printf(f, o);
    }
    /**
     *               40
     *       20              60
     *   10      30      50      70
     *  5  15  25  35  45  55  65  75
     */
    public BSTAndList generateBST1() {
        BST bst = new BST(true);
        List<Integer> list = 
            Arrays.asList(
                40,
                20,10,5,
                15,
                30,25,35,
                60,70,75,
                50,45,55,
                65);
        List<BSTNode> lres = new ArrayList<>();
        HashMap<Integer, BSTNode> map = new HashMap<>();
        for(Integer i: list) {
            BSTNode n = bst.addNode(i);
            lres.add(n);
            map.put(i, n);
        }
        BSTAndList bstAndList = new BSTAndList();
        bstAndList.bst = bst;
        bstAndList.listNodes = lres;
        List<Integer> listOrdered = new ArrayList<>(list);
        Collections.sort(listOrdered);
        bstAndList.listVals = listOrdered;
        bstAndList.map = map;
        return bstAndList;
    }

    public void testFindNextLargestNodeWithRoot() {
        BSTAndList bstAndList = generateBST1();
        BST bst = bstAndList.bst;
        //bst.print();
        Map<Integer, BSTNode> map = bstAndList.map;

        BSTAlgos bstAlgos = new BSTAlgos(bst);
        List<Integer> listtest = Arrays.asList(25,30,35,40,70,75);
        for(int i = 0; i < listtest.size(); i++) 
        {
            Integer item = listtest.get(i);
            BSTNode nc = map.get(item);
            BSTNode nn = bstAlgos.findNextLargestNode(nc);
            if(nn == null) {
                p("node cur:%2d nxt:null\n", nc.v());
            } else {
                p("node cur:%2d nxt:%2d\n", nc.v(), nn.v());
            }
        }
    }
    public void testFindNextLargestNodeNoRoot() {
        
    }
    
    public static class Type {
        public static final int BSTBALANCED = 1;
        public static final int BSTRANDOM = 2;
        public static final int BSTFIXED = 3;
    }
    public static class BSTSetup {

        Utils u = new Utils();
        
        public void tBSTReverseLeafNodes() {
            int id = 0;
            BST bst = new BST();
            BSTNode n = new BSTNode(id++, 50, 50);
            bst.addNode(n);
            if(bst.getRoot() != null) {
                bst.getRoot().p();
            }
            bst.reverseLeafNodes();
        }
        
        public BST createTree(int numNodes, int type) {
            BST bst = new BST();
            switch(type) {
            case Type.BSTBALANCED:
                bstBalanced(numNodes, bst);
                break;
            case Type.BSTRANDOM:
                bstRandom(numNodes, bst);
                break;
            case Type.BSTFIXED:
                bstFixed(numNodes, bst);
                break;
            default:
                break;
            }
            
            return bst;
        }
        
        void bstBalanced(int numNodes, BST bst) {
            /*
             * 1,3,7,15,31 pattern determines if balanced. 
             * return if numNodes not balanced.
             * 
             * the equation should be 2^n - 1 = numNodes.
             * 
             * 2^n - 1 = numNodes;
             * 2^n = numNodes + 1;
             * log2(numNodes+1) = n
             * log10(numNodes+1)/log10(2) = log2(numNodes+1)
             * if(log2(numNodes+1) == integer) then OK.
             * 
             */
            
            class CVal {
                public int lo = 0;
                public int hi = 0;
                public CVal(int lo, int hi) {
                    this.lo = lo;
                    this.hi = hi;
                }
            }

            double logtop = Math.log(numNodes+1);
            double logbot = Math.log(2);
            double logval = logtop/logbot;
            if(logval != Math.floor(logval) || Double.isInfinite(logval)) {
                return;
            }
            
            int max = numNodes * 10;
            int min = 10;
            AtomicInteger id = new AtomicInteger(0);
            
            LinkedList<CVal> ll = new LinkedList<>();
            List<Integer> lvals = new ArrayList<>();
            int cnt = 0;
            int lo = min;
            int hi = max;
            
            // add root parameters to list
            {
                CVal cv = new CVal(lo, hi);
                ll.add(cv);
                cnt++;
            }
            
            // now calculate mid for each parameter set in list
            while(ll.size() != 0) {
                CVal cv = ll.poll();

                lo = cv.lo;
                hi = cv.hi;
                int mid = (lo+hi)/2;
                
                BSTNode n = new BSTNode(id.incrementAndGet(), mid, mid);
                bst.addNode(n);
                
                lvals.add(mid);

                if(cnt < numNodes) {
                    CVal cvl = new CVal(lo, mid);
                    CVal cvh = new CVal(mid, hi);
                    ll.add(cvl);
                    ll.add(cvh);
                    cnt += 2;
                }
            }
        
        }
        
        void bstBalanced(
            int lo, int hi, 
            AtomicInteger id, AtomicInteger ctr, 
            int maxNodes, BST bst) 
        {
            if(ctr.get() == maxNodes) {
                return;
            }
            
            int mid = (lo+hi)/2;
            BSTNode n = new BSTNode(id.incrementAndGet(), mid, mid);
            bst.addNode(n);
            ctr.incrementAndGet();
            
            bstBalanced(lo, mid, id, ctr, maxNodes, bst);
            bstBalanced(mid, hi, id, ctr, maxNodes, bst);
        }
        
        void bstRandom(int numNodes, BST bst) {
            int max = numNodes * 10;
            int min = 10;
            AtomicInteger id = new AtomicInteger(0);
            
            List<Integer> listvals = new ArrayList<>();
            
            for(int i = min; i <= max; i++) {
                listvals.add(i);
            }
            
            u.shuffle(listvals);
            
            LinkedList<Integer> ll = new LinkedList<>(listvals.subList(0, numNodes));
            bstRandom(bst, ll, id);
        }
        
        private void bstRandom(
            BST bst,
            LinkedList<Integer> ll,
            AtomicInteger id) {
            while(ll.size() != 0) {
                Integer v = ll.poll();
                BSTNode n = new BSTNode(id.incrementAndGet(), v, v);
                bst.addNode(n);
            }
        }
        
        void bstFixed(int numNodes, BST bst) {
        }
        
        public BST generateTree1() {
            /*
             * 10  20  21  22  23  24  25  30  31  32  33  
             * 34  35  36  37  40  50  60  70
             * 
             *                   40                             
             *           20               60                   
             *       10      30      50        70              
             *           25      35
             *         24     34   36
             *       23     33       37
             *     22     32         
             *   21     31
             */
            int id = 0;
            BSTNode r = new BSTNode(id++, 40, 40);
            {
                r   .l(id++, 20)
                    .l(id++, 10);
                
                r.l()
                    .r(id++, 30)
                    .l(id++, 25)
                    .l(id++, 24)
                    .l(id++, 23)
                    .l(id++, 22)
                    .l(id++, 21);
                
                r.l().r()
                    .r(id++, 35)
                    .l(id++, 34)
                    .l(id++, 33)
                    .l(id++, 32)
                    .l(id++, 31);
                
                r.l().r().r()
                    .r(id++, 36)
                    .r(id++, 37);

                r   .r(id++, 60)
                    .l(id++, 50);

                r.r()
                    .r(id++, 70);
            }
            BST bst = new BST();
            bst.setTree(r);
            return bst;
        }
        
        /*
         * @return
         */
        public BST generateTree2() {
            /*
             *               40
             *       20              60
             *   10      30      50      70
             *  5  15  25  35  45  55  65  75
             */
            int id = 0;
            BSTNode root = new BSTNode(id++, 40, 40);
            
            root
                .l(id++, 20)
                .l(id++, 10)
                .l(id++, 5);
            root.l().l()
                .r(id++, 15);
            root.l()
                .r(id++, 30)
                .l(id++, 25);
            root.l().r()
                .r(id++, 35);
            root
                .r(id++, 60)
                .l(id++, 50)
                .l(id++, 45);
            root.r().l()
                .r(id++, 55);
            root.r()
                .r(id++, 70)
                .l(id++, 65);
            root.r().r()
                .r(id++, 75);

            BST bst = new BST();
            bst.setTree(root);
            return bst;
        }
        
        /*
         * @return
         */
        public BST generateTree3() {
            /*
             *               40
             *       20              60
             *   10      30      50      70
             */
            int id = 0;
            BSTNode root = new BSTNode(id++, 40, 40);
            
            root
                .l(id++, 20)
                .l(id++, 10);
            root.l()
                .r(id++, 30);
            root
                .r(id++, 60)
                .l(id++, 50);
            root.r()
                .r(id++, 70);

            BST bst = new BST();
            bst.setTree(root);
            return bst;
        }

    }
}



