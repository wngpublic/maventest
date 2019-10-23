package org.wayne.misc.graphs;

import java.util.concurrent.atomic.AtomicInteger;

public class BSTNode {
    private Object obj;
    private int k;
    private int v;
    private int id;
    private int h = 0;
    private COLOR color = COLOR.RED;
    private BSTNode l;
    private BSTNode r;
    private BSTNode p;
    private static AtomicInteger idCounter = new AtomicInteger(0);
    
    public enum COLOR {
        NONE,
        RED,
        BLACK
    }

    public BSTNode(int key) {
        this.id = idCounter.incrementAndGet();
        this.k = key;
    }
    
	public BSTNode(int id, int key, int v) {
		this.id = id;
		this.k = key;
		this.v = v;
		this.l = null;
		this.r = null;
	}

	public BSTNode(int id, int key, int v, COLOR color) {
        this.id = id;
        this.k = key;
        this.v = v;
        this.l = null;
        this.r = null;
        this.color = color;
    }
	
	public int h() {
	    return h;
	}
	
	public BSTNode h(int h) {
	    this.h = h;
        return this;
	}

	public Integer key() {
	    return k;
	}
	
	public BSTNode key(Integer key) {
	    this.k = key;
        return this;
	}
	
	public Integer k() {
	    return k;
	}
	
	public BSTNode k(Integer k) {
	    this.k = k;
        return this;
	}
	
	public void keyValue(Integer key, Object value) {
	    this.k = key;
	    this.obj = value;
	}
	
	public BSTNode value(Object obj) {
	    this.obj = obj;
	    return this;
	}
	
	public Object value() {
	    return obj;
	}
	
	public int id() {
		return id;
	}
	
	public int v() {
		return v;
	}
	
	public COLOR color() {
	    return color;
	}
	
	public BSTNode color(COLOR color) {
	    this.color = color;
        return this;
	}
	
	public BSTNode l(BSTNode l) {
		this.l = l;
        return this;
	}
	
	public BSTNode r(BSTNode r) {
		this.r = r;
        return this;
	}
	
	public BSTNode l(int id, int v) {
	    BSTNode n = new BSTNode(id, v, v);
	    l = n;
	    return n;
	}
	
	public BSTNode r(int id, int v) {
	    BSTNode n = new BSTNode(id, v, v);
	    r = n;
	    return n;
	}
	
	public BSTNode p(BSTNode p) {
	    this.p = p;
	    return this;
	}
	
	public BSTNode l() {
		return l;
	}
	
	public BSTNode r() {
		return r;
	}
	
	public BSTNode p() {
	    return p;
	}
	
	private void print(String f, Object ... o) {
	    System.out.printf(f, o);
	}
	public void print() {
		if(l == null && r == null) {
			System.out.printf("ID:%2d V:%6d L:%6d R:%6d ",id,v,l,r);
		}
		else if(l == null) {
			System.out.printf("ID:%2d V:%6d L:%6d R:%6d ",id,v,l,r.v());
		}
		else if(r == null) {
			System.out.printf("ID:%2d V:%6d L:%6d R:%6d ",id,v,l.v(),r);
		}
		else {
			System.out.printf("ID:%2d V:%6d L:%6d R:%6d ",id,v,l.v(),r.v());
		}
		if(p == null) {
		    print("p:%6s", "null");
		} else {
		    print("p:%6d", p.v());
		}
		print("\n");
	}
}

class BSTColor {
    public static final int NONE = 0;
    public static final int BLACK = 1;
    public static final int RED = 2;
}
