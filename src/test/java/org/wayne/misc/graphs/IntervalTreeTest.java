package org.wayne.misc.graphs;

public class IntervalTreeTest {

public static class IntervalTreeSetup {
    
    IntervalTree tree = new IntervalTree();
    
    void p(String f, Object ...o) {
        System.out.printf(f, o);
    }
    
    public void testAdd() {
        p("testAdd\n");
        tree.addRange(11,15);
        tree.printTree("add 11,15");
        tree.addVal(17);
        tree.printTree("add 17");
        tree.addVal(19);
        tree.printTree("add 19");
        tree.addVal(18);
        tree.printTree("add 18");
        tree.addRange(21, 25);
        tree.printTree("add 21,25");
        tree.addRange(13, 16);
        tree.printTree("add 13,16");
        tree.addRange(4, 5);
        tree.printTree("add 4,15");
        tree.addRange(2, 5);
        tree.printTree("add 2,5");
        tree.addRange(6, 7);
        tree.printTree("add 6,7");
        tree.addRange(8, 10);
        tree.printTree("add 8,10");
        tree.addRange(10, 30);
        tree.printTree("add 10,30");
    }
}

}
