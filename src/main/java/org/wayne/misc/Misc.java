package org.wayne.misc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Misc {

    int ctr = 0;
    boolean dbg = true;
    Utils u = new Utils();
    
    void p(String f, Object ...o) {
        System.out.printf(f, o);
    }
    
    void pl(Object ...o) {
        System.out.println(o);
    }

    void reset() {
        ctr = 0;
    }
    
    public void debug(boolean dbg) {
        this.dbg = dbg;
    }
    
    public void printSimpleStackBehavior(List<Integer> l, boolean left2right) {
        class Behavior {
            public void pl(List<Integer> l, boolean isReverse) {
                StringBuilder sb = new StringBuilder();
                if(isReverse) {
                    for(int i = l.size() - 1; i >= 0; i--) {
                        sb.append(String.format("%2d ", l.get(i)));
                    }
                } else {
                    for(int i = 0; i < l.size(); i++) {
                        sb.append(String.format("%2d ", l.get(i)));
                    }
                }
                p("%s\n", sb.toString());
            }
            public void printArray(List<Integer> l) {
                p("\n");
                int size = l.size();
                int max = 0;
                for(int i = 0; i < size; i++) {
                    max = (max < l.get(i)) ? l.get(i) : max;
                }
                List<String> list = new ArrayList<>();
                for(int i = 0; i < max; i++) {
                    StringBuilder sb = new StringBuilder();
                    int h = max - i;
                    sb.append(String.format("%02d|", h));
                    for(int j = 0; j < size; j++) {
                        if(l.get(j) >= h) {
                            sb.append(" | ");
                        } else {
                            sb.append("   ");
                        }
                    }
                    String line = sb.toString();
                    list.add(line);
                }
                for(int i = 0; i < max; i++) {
                    p("%s\n", list.get(i));
                }
                StringBuilder sb0 = new StringBuilder();
                StringBuilder sb1 = new StringBuilder();
                StringBuilder sb2 = new StringBuilder();
                for(int i = 0; i < size; i++) {
                    sb2.append("---");
                    sb0.append(String.format("%02d ", i));
                    sb1.append(String.format("%02d ", l.get(i)));
                }
                p("--+%s\n", sb2.toString());
                p("  |%s\n", sb0.toString());
                p("  |%s\n", sb1.toString());
                p("---%s\n", sb2.toString());
            }
        }
        
        /*
         * x           x
         * x x       x x x   x
         * x x x   x x x x x x x   x
         * x x x   x x x x x x x x x
         * 
         * 4 6 6 0 2 3 4
         *         8 9 6
         *               9
         *                10
         *                
         * 
         *               
         *               
         *               
         *       x   x     x
         *     x x   x x x x   
         * x   x x x x x x x    
         * x x x x x x x x x x
         * 
         */
        Behavior t = new Behavior();
        int size = l.size();
        LinkedList<Integer> ll = new LinkedList<>();
        
        t.printArray(l);

        if(left2right) {
            for(int i = 0; i < size; i++) {
                int curval = l.get(i);
                while(ll.size() != 0) {
                    int idx = ll.peek();
                    int stackval = l.get(idx);
                    if(stackval > curval) {
                        ll.pop();
                    } else {
                        break;
                    }
                }
                ll.push(i);
            }
            StringBuilder sb1 = new StringBuilder();
            StringBuilder sb2 = new StringBuilder();
            for(int i = 0; i < ll.size(); i++) {
                int idx = ll.get(i);
                int val = l.get(idx);
                sb1.append(String.format("%2d ", idx));
                sb2.append(String.format("%2d ", val));
            }
            p("%s\n", sb1.toString());
            p("%s\n", sb2.toString());
        } 
        else {
            for(int i = size-1; i >= 0; i--) {
                int curval = l.get(i);
                while(ll.size() != 0) {
                    int idx = ll.peek();
                    int stackval = l.get(idx);
                    if(stackval > curval) {
                        ll.pop();
                    } else {
                        break;
                    }
                }
                ll.push(i);
            }
            StringBuilder sb1 = new StringBuilder();
            StringBuilder sb2 = new StringBuilder();
            for(int i = 0; i < ll.size(); i++) {
                int idx = ll.get(i);
                int val = l.get(idx);
                sb1.append(String.format("%2d ", idx));
                sb2.append(String.format("%2d ", val));
            }
            p("%s\n", sb1.toString());
            p("%s\n", sb2.toString());
        }
    }
    
    public int maxRectangleAreaHistogram(List<Integer> listHeight) {
        /*
         *           x 
         *           x x
         *     x     x x 
         * x   x   x x x           x
         * x x x   x x x x     x   x
         * x x x   x x x x x   x x x  
         * x x x x x x x x x x x x x 
         * 4 3 5 1 4 7 6 3 2 1 3 2 4
         * 
         * --------------------------
         * 1 2 3 4 1 2 2 4 5  
         * 4 3 3 1 4 4 6 3 2
         * --------------------------
         * 4 6 9 9 9 9 1 1 1 
         *             2 2 6 
         * 
         * 
         * 
         */
        
        class Area {
            public void printArray(List<Integer> l) {
                p("\n");
                int size = l.size();
                int max = 0;
                for(int i = 0; i < size; i++) {
                    max = (max < l.get(i)) ? l.get(i) : max;
                }
                List<String> list = new ArrayList<>();
                for(int i = 0; i < max; i++) {
                    StringBuilder sb = new StringBuilder();
                    int h = max - i;
                    sb.append(String.format("%02d|", h));
                    for(int j = 0; j < size; j++) {
                        if(l.get(j) >= h) {
                            sb.append(" | ");
                        } else {
                            sb.append("   ");
                        }
                    }
                    String line = sb.toString();
                    list.add(line);
                }
                for(int i = 0; i < max; i++) {
                    p("%s\n", list.get(i));
                }
                StringBuilder sb0 = new StringBuilder();
                StringBuilder sb1 = new StringBuilder();
                StringBuilder sb2 = new StringBuilder();
                for(int i = 0; i < size; i++) {
                    sb2.append("---");
                    sb0.append(String.format("%02d ", i));
                    sb1.append(String.format("%02d ", l.get(i)));
                }
                p("--+%s\n", sb2.toString());
                p("  |%s\n", sb0.toString());
                p("  |%s\n", sb1.toString());
                p("---%s\n", sb2.toString());
            }
            
            int maxRectangleAreaHistogram1(List<Integer> l) {
                int size = l.size();
                int maxarea = 0;
                for(int i = 0; i < size; i++) {
                    int v = l.get(i);
                    int area = v;
                    for(int j = i-1; j >= 0; j--) {
                        int v0 = l.get(j);
                        if(v0 < v) {
                            break;
                        }
                        area += v;
                    }
                    for(int j = i+1; j < size; j++) {
                        int v0 = l.get(j);
                        if(v0 < v) {
                            break;
                        }
                        area += v;
                    }
                    if(area > maxarea) {
                        if(dbg) {
                            p("new max area: %2d old area: %2d idx:%2d\n", 
                                    area, maxarea, i);
                        }
                        maxarea = area;
                    }
                }
                if(dbg) {
                    p("Run method. area = %2d\n", maxarea);
                }
                return maxarea;
            }
            
            int maxRectangleAreaHistogram2(List<Integer> l) {
                class Node {
                    int idx;
                    int val;
                    public Node(int idx, int val) {
                        this.idx = idx;
                        this.val = val;
                    }
                }
                Map<Integer, Node> map = new HashMap<>();
                int size = l.size();
                int maxarea = 0;
                for(int i = 0; i < size; i++) {
                    int v = l.get(i);
                    Node n = new Node(i, v);
                    int area = v;
                    maxarea = (area > maxarea) ? area : maxarea;
                }
                if(dbg) {
                    p("Run method. area = %2d\n", maxarea);
                }
                return maxarea;
            }

            int maxRectangleAreaHistogram3(List<Integer> l) {
                class Node {
                    int idx;
                    int val;
                    public Node(int idx, int val) {
                        this.idx = idx;
                        this.val = val;
                    }
                    public int v() {
                        return val;
                    }
                    public int idx() {
                        return idx;
                    }
                }
                LinkedList<Node> ll = new LinkedList<>();
                int size = l.size();
                int maxarea = 0;
                for(int i = 0; i < size; i++) {
                    int v = l.get(i);
                    Node n = new Node(i, v);
                    // remove everything larger than current val,
                    // which leaves behind everything smaller or equal.
                    while(ll.size() != 0 && ll.peek().v() > v) {
                        ll.pop();
                    }
                    int area = (ll.size() == 0) ? 
                            (v*(i+1)) : 
                            (v*(i-ll.peek().idx()+1));
                    ll.push(n);
                    if(area > maxarea) {
                        if(dbg) {
                            p("new max area: %2d old area: %2d idx:%2d\n", area, maxarea, i);
                        }
                        maxarea = area;
                    }
                }
                
                if(dbg) {
                    p("remaining ll size:%2d\n", ll.size());
                    for(int i = 0; i < ll.size(); i++) {
                        Node n = ll.get(i);
                        p("Node %2d v:%2d idx:%2d\n", i, n.v(), n.idx());
                    }
                }
                
                // what remains in ll is everything smaller or equal to end size
                // so calculate remaining areas.
                while(ll.size() != 0) {
                    Node n = ll.pop();
                    while(ll.size() != 0 && ll.peek().v() == n.v()) {
                        n = ll.pop();
                    }
                    int area = (ll.size() == 0) ?
                            (n.v()*(size-n.idx())) :
                            (n.v()*(size-ll.peek().idx()-1));
                    p("area = %2d n.v:%2d n.idx:%2d ll.size == %2d\n",
                        area, n.v(), n.idx(), ll.size());
                    if(area > maxarea) {
                        if(dbg) {
                            p("new max area: %2d old area: %2d ll idx:%2d\n", 
                                    area, maxarea, n.idx());
                        }
                        maxarea = area;
                    }
                }
                if(dbg) {
                    p("Run method. area = %2d\n", maxarea);
                }
                return maxarea;
            }
            
            int maxRectangleAreaHistogram4(List<Integer> l) {
                int maxarea = 0;
                int size = l.size();

                int [] a = new int[size];
                LinkedList<Integer> llval = new LinkedList<>();
                LinkedList<Integer> llidx = new LinkedList<>();
                
                for(int i = 0; i < size; i++) {
                    int curh = l.get(i);
                    a[i] = curh;
                    
                    if(llval.peek() != null) {
                        if(llval.peek() <= curh) {
                            
                        }
                        else {
                            while(llval.peek() > curh) {
                                llval.pop();
                                int prvidx = llidx.pop();
                                int curarea = curh * (i-prvidx+1);
                                a[prvidx] = (curarea > a[prvidx]) ? curarea : a[prvidx];
                                maxarea = (maxarea > curarea) ? maxarea : curarea;
                            }
                        }
                    }
                    llval.push(curh);
                    llidx.push(i);
                    maxarea = (maxarea > curh) ? maxarea : curh;
                }
                llval.clear();
                llidx.clear();
                
                u.printArray("  |", a);

                for(int i = size-1; i >= 0; i--) {
                    int curh = l.get(i);
                    while(llval.peek() != null && llval.peek() > curh) {
                        llval.pop();
                        int prvidx = llidx.pop();
                        int curarea = curh * (prvidx-i-1);
                        a[prvidx] = (curarea > a[prvidx]) ? curarea : a[prvidx];
                        maxarea = (maxarea > curarea) ? maxarea : curarea;
                    }
                    llval.push(curh);
                    llidx.push(i);
                    maxarea = (maxarea > curh) ? maxarea : curh;
                }
                u.printArray("  |", a);
                
                return maxarea;
            }
            
            /**
             * This is a double loop that sweeps left and right from index i.
             * 
             * @param l
             * @return
             */
            int maxRectangleAreaHistogramBruteForce(List<Integer> l) {
                int maxarea = 0;
                int size = l.size();
                int idxl = 0;
                int idxr = 0;
                int idxh = 0;
                for(int i = 0; i < size; i++) {
                    int curval = l.get(i);
                    int curarea = curval;
                    int idxcurl = i;
                    int idxcurr = i;
                    // sweep left until left height less than cur height
                    for(int j = i-1; j >= 0; j--) {
                        int v = l.get(j);
                        if(v < curval) {
                            break;
                        }
                        curarea += curval;
                        idxcurl = j;
                    }
                    // sweep right until right height less than cur height
                    for(int j = i+1; j < size; j++) {
                        int v = l.get(j);
                        if(v < curval) {
                            break;
                        }
                        curarea += curval;
                        idxcurr = j;
                    }
                    if(curarea > maxarea) {
                        idxl = idxcurl;
                        idxr = idxcurr;
                        idxh = curval;
                        maxarea = curarea;
                    }
                }
                if(dbg) {
                    p("max area: idxl:%2d idxr:%2d h:%2d area:%2d\n",
                        idxl, idxr, idxh, maxarea);
                }
                return maxarea;
            }

        }
        
        Area t = new Area();

        int type = 4;
        
        t.printArray(listHeight);
        try {
            if(type == 0) {
                return t.maxRectangleAreaHistogram1(listHeight);
            }
            if(type == 1) {
                return t.maxRectangleAreaHistogram2(listHeight);
            }
            if(type == 2) {
                return t.maxRectangleAreaHistogram3(listHeight);
            }
            if(type == 4) {
                return t.maxRectangleAreaHistogram4(listHeight);
            }
            if(type == 5) {
                return t.maxRectangleAreaHistogramBruteForce(listHeight);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
    
    public int maxRectangleMatrix(char [][] a) {
        return 0;
    }
    
    public int maxSubstringWithoutRepeatingCharacters(String s) {
        return 0;
    }
    
    public int maxConsecutiveSequence(List<Integer> listSequence) {
        
        class Node {
            public Integer v = null;
            public Node other = null;
            public Integer size = 1;
            
            public Node(Integer v) {
                this.v = v;
            }
            public Node size(int size) {
                this.size = size;
                return this;
            }
            public Node other(Node other) {
                this.other = other;
                return this;
            }
            
            @SuppressWarnings("unused")
            public Integer v() {
                return v;
            }
            
            @SuppressWarnings("unused")
            public Node other() {
                return other;
            }
            
            public Integer size() {
                return size;
            }
        }
        
        int maxsize = 0;
        
        HashMap<Integer, Node> map = new HashMap<>();
        
        reset();
        
        for(Integer v: listSequence) {
            int vm1 = v - 1;
            int vp1 = v + 1;
            
            if(map.containsKey(v)) {
                continue;
            }
            
            Node nc = new Node(v);
            Node np = map.get(vm1);
            Node nn = map.get(vp1);
            
            /*
             * 4 cases:
             * 
             * no head overlap, has tail overlap
             * no tail overlap, has head overlap
             * has head and tail overlap
             * no head and no tail overlap
             * 
             */
            
            if(np == null && nn == null) {
                // do nothing
            }
            else if(np == null && nn != null) {
                nc.size(nc.size()+nn.size()).other(nn);
                nn.size(nc.size()).other(nc);
            }
            else if(np != null && nn == null) {
                nc.size(nc.size()+np.size()).other(np);
                np.size(nc.size()).other(nc);
            }
            else if(np != null && nn != null) {
                nc.size(nc.size()+nn.size()+np.size());
                nn.size(nc.size()).other(np);
                np.size(nc.size()).other(nn);
            }
            
            maxsize = (nc.size() > maxsize) ? nc.size() : maxsize;
            map.put(v,  nc);
        }
        
        return maxsize;
    }
    
    public int areaTrapped(List<Integer> list) {
        int [] a = new int[list.size()];
        for(int i = 0; i < a.length; i++) {
            a[i] = list.get(i);
        }
        return areaTrapped(a);
    }
    
    public int areaTrapped(int [] a) {
        class AreaTrapped {
            public void printArray(int [] a) {
                p("PRINT ARRAY\n");
                int size = a.length;
                int max = 0;
                for(int i = 0; i < size; i++) {
                    max = (max < a[i]) ? a[i] : max;
                }
                List<String> list = new ArrayList<>();
                for(int i = 0; i < max; i++) {
                    StringBuilder sb = new StringBuilder();
                    int h = max - i;
                    sb.append(String.format("%02d|", h));
                    for(int j = 0; j < size; j++) {
                        if(a[j] >= h) {
                            sb.append(" | ");
                        } else {
                            sb.append("   ");
                        }
                    }
                    String line = sb.toString();
                    list.add(line);
                }
                for(int i = 0; i < max; i++) {
                    p("%s\n", list.get(i));
                }
                StringBuilder sb0 = new StringBuilder();
                StringBuilder sb1 = new StringBuilder();
                StringBuilder sb2 = new StringBuilder();
                for(int i = 0; i < size; i++) {
                    sb2.append("---");
                    sb0.append(String.format("%02d ", i));
                    sb1.append(String.format("%02d ", a[i]));
                }
                p("--+%s\n", sb2.toString());
                p("  |%s\n", sb0.toString());
                p("  |%s\n", sb1.toString());
                p("---%s\n", sb2.toString());
            }
            
            public void printArrayTrapped(int [] a, int [] above) {
                p("PRINT ARRAY TRAPPED\n");
                int size = a.length;
                int max = 0;
                for(int i = 0; i < size; i++) {
                    max = (max < a[i]) ? a[i] : max;
                }
                List<String> list = new ArrayList<>();
                for(int i = 0; i < max; i++) {
                    StringBuilder sb = new StringBuilder();
                    int h = max - i;
                    sb.append(String.format("%02d|", h));
                    for(int j = 0; j < size; j++) {
                        int base = a[j];
                        int top = above[j];
                        int total = base + top;
                        if(base >= h) {
                            sb.append(" | ");
                        }
                        else if(total >= h) {
                            sb.append(" ~ ");
                        }
                        else {
                            sb.append("   ");
                        }
                    }
                    String line = sb.toString();
                    list.add(line);
                }
                for(int i = 0; i < max; i++) {
                    p("%s\n", list.get(i));
                }
                StringBuilder sb0 = new StringBuilder();
                StringBuilder sb1 = new StringBuilder();
                StringBuilder sb2 = new StringBuilder();
                for(int i = 0; i < size; i++) {
                    sb2.append("---");
                    sb0.append(String.format("%02d ", i));
                    sb1.append(String.format("%02d ", a[i]));
                }
                p("--+%s\n", sb2.toString());
                p("  |%s\n", sb0.toString());
                p("  |%s\n", sb1.toString());
                p("---%s\n", sb2.toString());
            }
            
            public int areaTrappedTwoLoop(int [] a) {
                int size = a.length;
                int [] alr = new int[size];
                int [] arl = new int[size];
                int area = 0;
                int idxLR = 0;
                int idxRL = size-1;
                int prv = 0;
                // record last drop edge left to right
                for(int i = 0; i < size; i++) {
                    int h = a[i];
                    if(h <= prv) {
                        alr[idxLR++] = h;
                    }
                    prv = h;
                }
                prv = 0;
                // record last drop edge right to left
                for(int i = size-1; i >= 0; i--) {
                    int h = a[i];
                    if(h <= prv) {
                        arl[idxRL--] = h;
                    }
                    prv = h;
                }
                return area;
            }
            
            public void findMaxLR(int [] a) {
                int type = 0;
                if(type == 0) {
                    findMaxLR2Loop(a);
                }
                else {
                    findMaxLR1Loop(a);
                }
            }
            
            public void findMaxLR2Loop(int [] a) {
                int size = a.length;
                int max = 0;
                int maxL = 0;
                int maxR = 0;
                int idxMaxL = 0;
                int idxMaxR = 0;
                int idxL = 0;
                int idxR = size - 1;
                for(; idxL < idxR; ) {
                    
                    // keep traversing right until previous > current height
                    for(int prv = a[idxL]; idxL < idxR; idxL++) {
                    //while(idxL < idxR) {
                        int h = a[idxL];
                        max = (h > max) ? h : max;
                        if(h >= maxL && prv < h) {
                            maxL = h;
                            idxMaxL = idxL;
                        }
                        if(prv >= h && maxL > maxR) {
                            break;
                        }
                        prv = h;
                    }
                    
                    // keep traversing left until previous > current height
                    for(int prv = a[idxR]; idxR >= idxL; idxR--) {
                    //while(idxR >= idxL) {
                        int h = a[idxR];
                        max = (h > max) ? h : max;
                        if(h >= maxR && prv < h) {
                            maxR = h;
                            idxMaxR = idxR;
                        }
                        if(prv >= h && maxR > maxL) {
                            break;
                        }
                        prv = h;
                    }
                }
                
                p("\nmax = %2d idxL = %2d maxL = %2d idxR = %2d maxR = %2d\n", 
                    max, idxMaxL, maxL, idxMaxR, maxR);
                
            }
            
            public void findMaxLR1Loop(int [] a) {
                int size = a.length;
                int max = 0;
                int maxL = 0;
                int maxR = 0;
                int idxMaxL = -1;
                int idxMaxR = -1;
                for(int i = 0; i < size; i++) {
                    int h = a[i];
                    max = (h > max) ? h : max;
                    if(idxMaxL < idxMaxR) {
                        
                    }
                    if(maxR == -1) {
                        
                    }
                }
                
                p("\nmax = %2d maxL = %2d maxR = %2d idxMaxL = %2d idxMaxR = %2d\n", 
                    max, maxL, maxR, idxMaxL, idxMaxR);
                
            }
            
            public int areaTrappedBrute(int [] a) {
                int area = 0;
                int size = a.length;
                int [] above = new int [size];
                for(int i = 0; i < size; i++) {
                    
                    int idxL = i;
                    int idxR = i;
                    int maxL = 0;
                    int maxR = 0;
                    int idxMaxL = i;
                    int idxMaxR = i;
                    
                    // find left edge
                    for(int prv = 0; idxL >= 0; idxL--) {
                        int h = a[idxL];
                        if(maxL <= h) {
                            maxL = h;
                            idxMaxL = idxL;
                        }
                        //if(prv > h) {
                        //    break;
                        //}
                        prv = h;
                    }
                    
                    // find right edge
                    for(int prv = 0; idxR < size; idxR++) {
                        int h = a[idxR];
                        if(maxR <= h) {
                            maxR = h;
                            idxMaxR = idxR;
                        }
                        if(maxR >= maxL) {
                            break;
                        }
                        //if(prv > h && maxR >= maxL) {
                        //    break;
                        //}
                        prv = h;
                    }
                    
                    if(idxR >= size || idxL < 0) {
                        idxR = (idxR >= size) ? size - 1: idxR;
                        idxL = (idxL < 0) ? 0 : idxL;
                        //continue;
                    }
                    
                    int minLocal = (maxR < maxL) ? maxR : maxL;
                    for(int j = idxMaxL; j <= idxMaxR; j++) {
                        int h = a[j];
                        int diff = minLocal - h;
                        above[j] = (above[j] > diff) ? above[j] : diff;
                    }
                }
                printArrayTrapped(a, above);
                return area;
            }
            
            public int areaTrappedStack(int [] a) {
                return 0;
            }
        }
        
        AreaTrapped areaTrapped = new AreaTrapped();
        //areaTrapped.printArray(a);

        int type = 1;
        if(type == 0) {
            return areaTrapped.areaTrappedTwoLoop(a);
        }
        else if(type == 1) {
            return areaTrapped.areaTrappedBrute(a);
        }
        else if(type == 2) {
            return areaTrapped.areaTrappedStack(a);
        }
        else if(type == 3) {
            areaTrapped.findMaxLR(a);
        }
        return 0;
    }
    

    /**
     * permutation
     * n!/(n-k)!
     * 
     * @param s
     * @param choose
     * @return
     */
    public List<String> permutation(String s, int choose) {
        /*
         * abcd choose 3 = (n!/(n-k)!) = (4!/(4-3)!) = 24/1
         * 
         * a
         *  b
         *   c  abc
         *   d  abd
         *  c
         *   b  acb
         *   d  acd
         *  d
         *   b  adb
         *   c  adc
         * b
         *  a
         *   c  bac
         *   d  bad
         *  c
         *   a  bca
         *   d  bcd
         *  d
         *   a  bda
         *   c  bca
         * c
         *  a
         *   b  cab
         *   d  cad
         *  b
         *   a  cba
         *   d  cbd
         *  d
         *   a  cda
         *   b  cdb
         * d
         *  a
         *   b  dab
         *   c  dac
         *  b
         *   a  dba
         *   c  dbc
         *  c
         *   a  dca
         *   b  dcb
         *       
         */
        class Perm {
            List<String> permutation(char [] input, int choose) {
                List<String> l = new ArrayList<>();
                int size = input.length;
                boolean [] used = new boolean[size];
                char [] a = new char[choose];
                perm1(input, used, 0, a, choose, l);
                
                if(dbg) {
                    u.p("-----------------------------\n");
                    int i = 0;
                    for(String s: l) {
                        u.p("%3d: %s\n", ++i, s);
                    }
                }

                return l;
            }
            
            private void perm1(
                char [] input,
                boolean [] used,
                int idx,
                char [] a,
                int choose,
                List<String> l) 
            {
                if(idx == choose) {
                    String s = new String(a);
                    l.add(s);
                    return;
                }
                for(int i = 0; i < input.length; i++) {
                    if(used[i]) {
                        continue;
                    }
                    used[i] = true;
                    char c = input[i];
                    a[idx] = c;
                    perm1(input, used, idx+1, a, choose, l);
                    used[i] = false;
                }
            }
        }
        
        char [] a = s.toCharArray();
        Perm p = new Perm();
        return p.permutation(a, choose);
    }
    
    /**
     * combination
     * n!/(k!(n-k)!)
     * 
     * @param s
     * @param choose
     * @return
     */
    public List<String> combination(String s, int choose) {
        /*
         * 
         */
        class Comb {
            List<String> combination(char [] input, int choose) {
                List<String> l = new ArrayList<>();
                return l;
            }
        }
        
        char [] a = s.toCharArray();
        Comb c = new Comb();
        return c.combination(a, choose);
    }
    
    public int maxAreaTrapped(int [] a) {
        return 0;
    }
    
    public void rotate(int [][] a, boolean rotateClockwise, boolean isPrint) {
        if(a.length != a[0].length) {
            return;
        }
        /*
         * 00 01 02 03 04
         * 05 06 07 08 09
         * 10 11 12 13 14
         * 15 16 17 18 19
         * 20 21 22 23 24
         * 
         * CLOCKWISE
         * x = a[0][1];             x                   = a[i][j];
         * a[0][1] = a[2][0];       a[i][j]             = a[sz-1-j][i];
         * a[2][0] = a[3][2];       a[sz-1-j][i]        = a[sz-1-i][sz-1-j]
         * a[3][2] = a[1][3];       a[sz-1-i][sz-1-j]   = a[j][sz-1-i]
         * a[1][3] = x;             a[j][sz-1-i]        = x
         * 
         * COUNTERCLOCKWISE
         * x = a[0][1];             x                   = a[i][j];
         * a[0][1] = a[1][4]        a[i][j]             = a[j][sz-1-i]
         * a[1][4] = a[4][3]        a[j][sz-1-i]        = a[sz-1-i][sz-1-j]
         * a[4][3] = a[3][0]        a[sz-1-i][sz-1-j]   = a[sz-1-j][i]
         * a[3][0] = x              a[sz-1-j][i]        = x
         */
        int sz = a.length;
        if(rotateClockwise) {
            for(int i = 0; i < sz/2; i++) {
                for(int j = i; j < sz - i - 1; j++) {
                    int x               = a[i][j];
                    a[i][j]             = a[sz-1-j][i];
                    a[sz-1-j][i]        = a[sz-1-i][sz-1-j];
                    a[sz-1-i][sz-1-j]   = a[j][sz-1-i];
                    a[j][sz-1-i]        = x;
                }
            }
        } else {
            for(int i = 0; i < sz/2; i++) {
                for(int j = i; j < sz - i - 1; j++) {
                    int x               = a[i][j];
                    a[i][j]             = a[j][sz-1-i];
                    a[j][sz-1-i]        = a[sz-1-i][sz-1-j];
                    a[sz-1-i][sz-1-j]   = a[sz-1-j][i];
                    a[sz-1-j][i]        = x;
                }
            }
        }
        if(isPrint) {
            if(rotateClockwise) {
                p("ROTATE CLOCKWISE\n");
            } else {
                p("ROTATE COUNTERCLOCKWISE\n");
            }
            for(int i = 0; i < sz; i++) {
                for(int j = 0; j < sz; j++) {
                    p("%02d ", a[i][j]);
                }
                p("\n");
            }
        }
    }
    
    public List<Integer> getSpiralMatrix(int [][] a) {
        class Internal {
            void printSpiral1(int [][] a, List<Integer> l) {
                int xmin = 0, xmax = a[0].length-1, ymin = 0, ymax = a.length-1;
                int dir = 0; // R=0,D=1,L=2,U=3
                
                while(xmin <= xmax && ymin <= ymax){
                    //p("dir:%2d xmin:%2d xmax:%2d ymin:%2d ymax:%2d\n",
                    //    dir, xmin, xmax, ymin, ymax);
                    switch(dir) {
                    case 0: // R
                        for(int i = xmin; i <= xmax; i++) {
                            l.add(a[ymin][i]);
                        }
                        ymin++;
                        break;
                    case 1: // D
                        for(int i = ymin; i <= ymax; i++) {
                            l.add(a[i][xmax]);
                        }
                        xmax--;
                        break;
                    case 2: // L
                        for(int i = xmax; i >= xmin; i--) {
                            l.add(a[ymax][i]);
                        }
                        ymax--;
                        break;
                    case 3: // U
                        for(int i = ymax; i >= ymin; i--) {
                            l.add(a[i][xmin]);
                        }
                        xmin++;
                        break;
                    }
                    dir = (dir + 1) % 4;
                }
            }
            void printSpiral2(int [][] a, List<Integer> l) {
                int xmin = 0, xmax = a[0].length-1, ymin = 0, ymax = a.length-1;
                int dir = 0; // R=0,D=1,L=2,U=3
                while(xmin <= xmax && ymin <= ymax){
                    if(dir == 0) {
                        for(int i = xmin; i <= xmax; i++) {
                            l.add(a[ymin][i]);
                        }
                        ymin++;
                    }
                    else if(dir == 1) {
                        for(int i = ymin; i <= ymax; i++) {
                            l.add(a[i][xmax]);
                        }
                        xmax--;
                    }
                    else if(dir == 2) {
                        for(int i = xmax; i >= xmin; i--) {
                            l.add(a[ymax][i]);
                        }
                        ymax--;
                    }
                    else if(dir == 3) {
                        for(int i = ymax; i >= ymin; i--) {
                            l.add(a[i][xmin]);
                        }
                        xmin++;
                    }
                    dir = (dir + 1) % 4;
                }
            }
            public List<Integer> printSpiral(int [][] a) {
                List<Integer> l = new ArrayList<>();
                int type = 1;
                if(type == 1) {
                    printSpiral1(a, l);
                }
                else {
                    printSpiral2(a, l);
                }
                return l;
            }
        }
        Internal internal = new Internal();
        return internal.printSpiral(a);
    }
}
