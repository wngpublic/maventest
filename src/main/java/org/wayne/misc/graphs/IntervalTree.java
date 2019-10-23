package org.wayne.misc.graphs;

public class IntervalTree {
    
    IntervalNode root = null;
    
    public IntervalTree() {
        
    }
    
    public void reset() {
        root = null;
    }
    
    void p(String f, Object ...o) {
        System.out.printf(f, o);
    }
    
    public void printTree() {
        printTree(null);
    }
    
    public void printTree(String msg) {
        if(msg == null) {
            p("------------BEGIN PRINT TREE\n");
        } else {
            p("------------BEGIN PRINT TREE %s\n", msg);
        }
        printTreeInternal(root);
    }
    
    void printTreeInternal(IntervalNode n) {
        if(n == null) {
            return;
        }
        printTreeInternal(n.l);
        n.p();
        printTreeInternal(n.r);
    }
    
    public void addRange(Integer beg, Integer end) {
        IntervalNode n = new IntervalNode(beg, end);
        root = addVal(root, n);
    }
    
    public void addVal(Integer v) {
        IntervalNode n = new IntervalNode(v);
        root = addVal(root, n);
    }
    
    IntervalNode addVal(IntervalNode p, IntervalNode n) {
        if(p == null) {
            return n;
        }
        
        /*
         * 0 1 2 3 4 5 6 7 8 9
         *         - - -            ref
         *         - - -            n.beg <= p.beg && n.end >= p.end 
         *           -              n.beg <= p.beg && n.end >= p.end
         *       - - - - -          n.beg > p.beg  && n.end < p.end
         *     - - -   - - -        n.beg < p.beg  && n.end >= p.end
         *   - - -       - - -      n.beg > p.beg  && n.beg > p.end
         *   - -           - -      
         */
        
        
        if     (n.beg >  p.beg && n.end < p.end) {  // N in P
            
        }
        else if(n.beg <= p.beg && n.end >= p.end) { // P in N
            p.beg = n.beg;
            p.end = n.end;
            p.l = addVal(p.l, n);
            p.r = addVal(p.r, n);
        }
        else if(n.end == (p.beg - 1)) { // N contiguous right of P
            p.beg = n.beg;
            p.l = addVal(p.l, n);
        }
        else if(n.beg == (p.end + 1)) { // N contiguous left of P
            p.end = n.end;
            p.r = addVal(p.r, n);
        }
        else if(n.end < p.beg) {        // N left of P
            p.l = addVal(p.l, n);
        }
        else if(n.beg > p.end) {        // N right of P
            p.r = addVal(p.r, n);
        }
        else if(p.beg <= n.end && n.end <= p.end) { // N end part overlaps P
            p.beg = n.beg;
            p.l = addVal(p.l, n);
        }
        else if(p.beg <= n.beg && p.end <= n.end) { // N beg overlaps P
            p.end = n.end;
            p.r = addVal(p.r, n);
        }
        else {
            p("NOT SURE\n");
        }
        p.min = (p.min < n.beg) ? p.min : n.beg;
        p.max = (p.max > n.end) ? p.max : n.end;
        mergeChildren(p);

        return p;
    }
    
    IntervalNode addValSimple(IntervalNode p, IntervalNode n) {
        if(p == null) {
            return n;
        }
        
        if     (n.beg >  p.beg && n.end < p.end) {  // N in P
            
        }
        else if(n.beg <= p.beg && n.end >= p.end) { // P in N
            p.beg = n.beg;
            p.end = n.end;
            p.l = addValSimple(p.l, n);
            p.r = addValSimple(p.r, n);
        }
        else if(n.end == (p.beg - 1) || (p.beg <= n.end && n.end <= p.end)) { // N contiguous right of P
            p.beg = n.beg;
            p.l = addValSimple(p.l, n);
        }
        else if(n.beg == (p.end + 1) || (p.beg <= n.beg && p.end <= n.end)) { // N contiguous left of P
            p.end = n.end;
            p.r = addValSimple(p.r, n);
        }
        else if(n.end < p.beg) {        // N left of P
            p.l = addValSimple(p.l, n);
        }
        else if(n.beg > p.end) {        // N right of P
            p.r = addValSimple(p.r, n);
        }
        else {
            p("NOT SURE\n");
        }
        p.min = (p.min < n.beg) ? p.min : n.beg;
        p.max = (p.max > n.end) ? p.max : n.end;
        mergeChildren(p);

        return p;
    }
    
    void mergeChildren(IntervalNode p) {
        mergeChildL(p);
        mergeChildR(p);
    }
    
    void mergeChildL(IntervalNode p) {
        if(p.l == null) {
            return;
        }
        if(p.l.end >= p.beg) {
            p.beg = p.l.beg;
            p.l = p.l.l;
        }
    }
    
    void mergeChildR(IntervalNode p) {
        if(p.r == null) {
            return;
        }
        if(p.r.beg <= p.end) {
            p.end = p.r.end;
            p.r = p.r.r;
        }
    }
    
    public void deleteVal(Integer v) {
        root = deleteVal(root, v);
    }
    
    IntervalNode deleteVal(IntervalNode n, Integer v) {
        return n;
    }
}


class IntervalNode {
    public int ctr = 0;
    public int min;
    public int max;
    public int beg;
    public int end;
    public static int idstatic = 0;
    public int id;
    public IntervalNode l;
    public IntervalNode r;

    public IntervalNode(int val) {
        this(val, val);
    }

    public IntervalNode(int beg, int end) {
        this.id = idstatic++;
        this.beg = beg;
        this.end = end;
        this.min = beg;
        this.max = end;
    }
    
    public void p() {
        System.out.printf("id:%2d beg:%3d end:%3d min:%3d max:%3d lID:%2d rID:%2d\n",
            id, beg, end, min, max,
            (l == null) ? null : l.id,
            (r == null) ? null : r.id
            );
    }
    
}
