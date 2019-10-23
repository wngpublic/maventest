package org.wayne.misc;

import java.util.List;

import junit.framework.TestCase;

public class UtilsTest extends TestCase {
    
    Utils u = new Utils();

    public void p(String f, Object ...o) {
        System.out.printf(f, o);
    }
    public void testMinInt1() {
        int min = 0;
        min = u.minint();
        u.p("min empty = %d\n", min);
        min = u.minint(4,2,8);
        u.p("min int = %d\n", min);
    }
    
    public void testPrimeRange() {
        int max = 100_000;
        List<Integer> l = u.getPrimesInRange(max);
        for(int i = 0; i < l.size(); i++) {
            p("%d\n", l.get(i));
        }
    }
}
