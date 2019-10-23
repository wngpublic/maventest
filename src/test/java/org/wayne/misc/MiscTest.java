package org.wayne.misc;

import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

public class MiscTest extends TestCase {
    Utils u = new Utils();
    Misc t = new Misc();
    
    public void p(String f, Object ...o) {
        System.out.printf(f, o);
    }
    
    public void pl(Object o) {
        System.out.println(o);
    }
    
    public void testAreaSingle() {
        List<Integer> a = null;
        int res = 0;
        a = Arrays.asList(5,3,1,2,4);
        res = t.areaTrapped(a);
        p("res: %d\n", res);
        p("-------------------------------\n\n");
    }
    
    public void testArea1() {
        List<Integer> a = Arrays.asList(1,2,3,4,5,6);
        int res = t.areaTrapped(a);
        p("res: %d\n", res);
        p("-------------------------------\n\n");
        a = Arrays.asList(6,5,4,3,2,1);
        res = t.areaTrapped(a);
        p("res: %d\n", res);
        p("-------------------------------\n\n");
        a = Arrays.asList(5,3,1,2,4);
        res = t.areaTrapped(a);
        p("res: %d\n", res);
        p("-------------------------------\n\n");
        a = Arrays.asList(4,2,1,3,5);
        res = t.areaTrapped(a);
        p("res: %d\n", res);
        p("-------------------------------\n\n");
        a = Arrays.asList(3,1,0,2,4,2,1,3,2,1,3,4,5);
        res = t.areaTrapped(a);
        p("res: %d\n", res);
        p("-------------------------------\n\n");
        a = Arrays.asList(5,4,3,1,2,3,1,2,4,2,0,1,3);
        res = t.areaTrapped(a);
        p("res: %d\n", res);
        p("-------------------------------\n\n");
        a = Arrays.asList(3,5,1,0,1,1,0,2,4,4,2,2,1,3,2,1,3,3,4,5,6,5,3,1,2,1,3,2);
        res = t.areaTrapped(a);
        p("res: %d\n", res);
        p("-------------------------------\n\n");
        a = Arrays.asList(2,3,1,2,1,3,5,6,5,4,3,3,1,2,3,1,2,2,4,4,2,0,1,1,0,1,5,3);
        res = t.areaTrapped(a);
        p("res: %d\n", res);
        p("-------------------------------\n\n");
        a = Arrays.asList(2,1,3,0,2,4,4,2,2,1,3,2,1,3,1,3,2);
        res = t.areaTrapped(a);
        p("res: %d\n", res);
        p("-------------------------------\n\n");
        a = Arrays.asList(2,3,1,3,1,2,3,1,2,2,4,4,2,0,3,1,2);
        res = t.areaTrapped(a);
        p("res: %d\n", res);
        p("-------------------------------\n\n");
        a = Arrays.asList(3,1,0,3,0,2,4,4,2,2,1,3,2,1,3,3,4,5,6,5,3,1,2,1,3,2);
        res = t.areaTrapped(a);
        p("res: %d\n", res);
        p("-------------------------------\n\n");
        a = Arrays.asList(2,3,1,2,1,3,5,6,5,4,3,3,1,2,3,1,2,2,4,4,2,0,3,0,1,3);
        res = t.areaTrapped(a);
        p("res: %d\n", res);
        p("-------------------------------\n\n");
        a = Arrays.asList(3,1,0,3,0,2,4,2,2,1,3,2,1,3,3,4,5,6,5,3,1,2,1,3,2);
        res = t.areaTrapped(a);
        p("res: %d\n", res);
        p("-------------------------------\n\n");
        a = Arrays.asList(2,3,1,2,1,3,5,6,5,4,3,3,1,2,3,1,2,2,4,2,0,3,0,1,3);
        res = t.areaTrapped(a);
        p("res: %d\n", res);
        p("-------------------------------\n\n");
        a = Arrays.asList(2,1,0,3,0,2,4,4,2,2,1,3,2,1,3,3,4,5,6,5,3,1,2,1,3,2);
        res = t.areaTrapped(a);
        p("res: %d\n", res);
        p("-------------------------------\n\n");
        a = Arrays.asList(2,3,1,2,1,3,5,6,5,4,3,3,1,2,3,1,2,2,4,4,2,0,3,0,1,2);
        res = t.areaTrapped(a);
        p("res: %d\n", res);
        p("-------------------------------\n\n");
        a = Arrays.asList(2,3,1,4,1,5,2,4,1,6,5,6,5,6,1,3,1);
        res = t.areaTrapped(a);
        p("res: %d\n", res);
        p("-------------------------------\n\n");
        a = Arrays.asList(1,3,1,6,5,6,5,6,1,4,2,5,1,4,1,3,2);
        res = t.areaTrapped(a);
        p("res: %d\n", res);
        p("-------------------------------\n\n");
        a = Arrays.asList(1,3,1,6,2,4,2,4,1,4,2,5,1,4,1,3,2);
        res = t.areaTrapped(a);
        p("res: %d\n", res);
        p("-------------------------------\n\n");
        a = Arrays.asList(2,3,1,4,1,5,2,4,1,4,2,4,2,6,1,3,1);
        res = t.areaTrapped(a);
        p("res: %d\n", res);
        p("-------------------------------\n\n");
    }
    
    public void testPermutation1() {
        String s = "abcd";
        int choose = 3;
        t.permutation(s, choose);
        s = "abcde";
        t.permutation(s, choose);
    }

    public void testHistogramArea1() {
        List<Integer> l = null;
        
        l = Arrays.asList(3,2,1,2,1,2,1,2,3);
        t.maxRectangleAreaHistogram(l);
    }
    
    public void testHistogramArea2() {
        List<Integer> l = null;
        
        {
            l = Arrays.asList(3,2,1,2,1,2,1,2,3);
            t.maxRectangleAreaHistogram(l);
        }
        {
            l = Arrays.asList(3,2,1,2,0,2,1,2,3);
            t.maxRectangleAreaHistogram(l);
        }
        {
            l = Arrays.asList(2,2,1,1,2,2,1,1,2,2,1,1,2,2);
            t.maxRectangleAreaHistogram(l);
        }
        
    }
    
    public void testSimpleStackBehavior1() {
        List<Integer> l = null;
        boolean isL2R = true;
        {
            l = Arrays.asList(3,2,1,2,1,2,1,2,3);
            t.printSimpleStackBehavior(l, isL2R);
            /*
             * 04|                    | 
             * 03|                | | | |     | 
             * 02||       |     | | | | | |   | | 
             * 01|| | | | |   | | | | | | | | | | 
             * --+-------------------------------
             *  i|0 1 2 3 4 5 6 7 8 9 a b c d e f
             *  v|2 1 1 1 2 0 1 2 3 3 4 3 2 1 3 2
             * ----------------------------------
             *    
             * idx  cur calcs               max stack_idx       stack_val   
             * 0    2                       2   0               2
             * 1    1   1*(1-0+1)=2         2   1               1
             * 2    1                       2   1,2             1,1
             * 3    1                       2   1,2,3           1,1,1
             * 4    2                           1,2,3,4         1,1,1,2
             * 5    0   2                   2   1,2,3           1,1,1
             *          1*(4-3+1)=2         2   1,2             1,1
             *          1*(4-2+1)=3         3   1               1
             *          1*(4-1+1)=4         4
             *          the answer is 5 here, but how do you account for idx 0?
             * 6    1   
             * 7    2   
             * 8    3   
             * 9    3   
             * a    4   
             * b    3   
             * c    2   
             * d    1   
             * e    3   
             * f    2
             */
        }
        p("----------------------------------\n");
        {
            l = Arrays.asList(4,2,1,2,1,2,1,2,4,4,5,5,3,4,3,2,4,6,4,2);
            t.printSimpleStackBehavior(l, isL2R);
        }
    }
    public void testPrintSpiral() {
        Misc misc = new Misc();
        {
            int [][] a = {
                { 1, 2, 3, 4, 5},
                {16,17,18,19, 6},
                {15,24,25,20, 7},
                {14,23,22,21, 8},
                {13,12,11,10, 9}
            };
            List<Integer> l = misc.getSpiralMatrix(a);
            System.out.println(l);
            for(int i = 0; i < 25; i++) {
                if(l.get(i) != (i+1)) {
                    p("ERROR at index %2d\n", i);
                    return;
                }
            }
        }
        {
            int [][] a = {
                { 1, 2, 3, 4, 5},
                {14,15,16,17, 6},
                {13,20,19,18, 7},
                {12,11,10, 9, 8}
            };
            List<Integer> l = misc.getSpiralMatrix(a);
            System.out.println(l);
            for(int i = 0; i < 20; i++) {
                if(l.get(i) != (i+1)) {
                    p("ERROR at index %2d\n", i);
                    return;
                }
            }
        }
    }
}
