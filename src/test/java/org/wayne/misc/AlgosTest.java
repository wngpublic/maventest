package org.wayne.misc;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import lombok.Builder;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;
import org.wayne.misc.Algos.GNode;

import junit.framework.TestCase;
import static org.wayne.misc.Algos.ListNode;
import static org.wayne.misc.Algos.MapRandom;
import static org.wayne.misc.Algos.LRUCache;
import static org.wayne.misc.Algos.Interval;
import static org.wayne.misc.Algos.TreeNode;
import static org.wayne.misc.Algos.MinStack;

public class AlgosTest extends TestCase {
    Algos test = new Algos();
    Utils u = new Utils();
    Random r = new Random();
    TestUtils testUtils = new TestUtils();
    AlgosMisc algosMisc = new AlgosMisc();
    static void p(String f, Object ...o) {
        System.out.printf(f, o);
    }
    ListNode listMaker(List<Integer> l) {
        ListNode h = null;
        ListNode c = null;
        for(int i = 0; i < l.size(); i++) {
            ListNode n = new ListNode(l.get(i));
            if(h == null) {
                h = n;
                c = n;
            } else {
                c.next = n;
                c = n;
            }
        }
        return h;
    }
    void printListNode(ListNode h, boolean newline) {
        if(h == null) {
            p("null ");
        } else {
            for(ListNode c = h; c != null; c = c.next) {
                p("%2d ", c.val);
            }
        }
        if(newline) {
            p("\n");
        }
    }
    public void testRemoveNthNodeFromEndOfList() {
        p("run %s\n", new Object() {}.getClass().getEnclosingMethod().getName());
        {
            int n = 1;
            ListNode c = listMaker(Arrays.asList(1));
            printListNode(c, false);
            p(": choose %2d\n", n);
            ListNode answer = test.removeNthNodeFromEndOfList(c, n);
            printListNode(answer, true);
        }
        {
            int n = 2;
            ListNode c = listMaker(Arrays.asList(2,3,4,5));
            printListNode(c, false);
            p(": choose %2d\n", n);
            ListNode answer = test.removeNthNodeFromEndOfList(c, n);
            printListNode(answer, true);
        }
        {
            int n = 0;
            ListNode c = listMaker(Arrays.asList(2,3,4,5));
            printListNode(c, false);
            p(": choose %2d\n", n);
            ListNode answer = test.removeNthNodeFromEndOfList(c, n);
            printListNode(answer, true);
        }
        {
            int n = 4;
            ListNode c = listMaker(Arrays.asList(2,3,4,5));
            printListNode(c, false);
            p(": choose %2d\n", n);
            ListNode answer = test.removeNthNodeFromEndOfList(c, n);
            printListNode(answer, true);
        }
        {
            int n = 1;
            ListNode c = listMaker(Arrays.asList(1,2));
            printListNode(c, false);
            p(": choose %2d\n", n);
            ListNode answer = test.removeNthNodeFromEndOfList(c, n);
            printListNode(answer, true);
        }
    }
    public void badtestSearchForRange() {
        p("run %s\n", new Object() {}.getClass().getEnclosingMethod().getName());
        {
            int [] a = {5,7,8,10};
            int target = 8;
            int [] res = test.searchForRange(a, target);
            p("res = %d %d\n", res[0],res[1]);
            assertEquals(res[0], 2);
            assertEquals(res[1], 2);
        }
        {
            int [] a = {5,6,7,8,10};
            int target = 8;
            int [] res = test.searchForRange(a, target);
            p("res = %d %d\n", res[0],res[1]);
            assertEquals(res[0], 3);
            assertEquals(res[1], 3);
        }
        {
            int [] a = {5,7,7,8,8,10};
            int target = 8;
            int [] res = test.searchForRange(a, target);
            p("res = %d %d\n", res[0],res[1]);
            assertEquals(res[0], 3);
            assertEquals(res[1], 4);
        }
    }
    public void testBinsearch() {
        p("run %s\n", new Object() {}.getClass().getEnclosingMethod().getName());
        {
            int [] a = {1,2,3,4,5,6};
            u.printArray(a);
            int min = a[0];
            int max = a[a.length-1];
            int idx;
            for(int i = 0; i <= 7; i++) {
                idx = test.binsearch(a, i, true);
                p("binsearch iterative %d = %2d\n", i, idx);
                int exp = (i < min || i > max) ? -1 : (i-1);
                assertEquals(idx, exp);
            }
            for(int i = 0; i <= 7; i++) {
                idx = test.binsearch(a, i, false);
                p("binsearch recursive %d = %2d\n", i, idx);
                int exp = (i < min || i > max) ? -1 : (i-1);
                assertEquals(idx, exp);
            }
        }
        {
            int [] a = {1,2,3,4,5};
            u.printArray(a);
            int min = a[0];
            int max = a[a.length-1];
            int idx;
            for(int i = 0; i <= 6; i++) {
                idx = test.binsearch(a, i, true);
                p("binsearch iterative %d = %2d\n", i, idx);
                int exp = (i < min || i > max) ? -1 : (i-1);
                assertEquals(idx, exp);
            }
            for(int i = 0; i <= 6; i++) {
                idx = test.binsearch(a, i, false);
                p("binsearch recursive %d = %2d\n", i, idx);
                int exp = (i < min || i > max) ? -1 : (i-1);
                assertEquals(idx, exp);
            }
        }
    }
    public void badtestFirstMissingPositiveSingle() {
        p("run %s\n", new Object() {}.getClass().getEnclosingMethod().getName());
        {
            int [] a = {4,1,2,3};
            int v = test.firstMissingPositive(a);
            u.printArray(a);
            p("v = %2d\n", v);
            assertEquals(5, v);
        }
    }
    public void badtestFirstMissingPositive() {
        p("run %s\n", new Object() {}.getClass().getEnclosingMethod().getName());
        {
            int [] a = {1,2,3,4,5};
            int v = test.firstMissingPositive(a);
            u.printArray(a);
            p("v = %2d\n", v);
            assertEquals(6, v);
        }
        {
            int [] a = {3,4,-1,1};
            int v = test.firstMissingPositive(a);
            u.printArray(a);
            p("v = %2d\n", v);
            assertEquals(2, v);
        }
        {
            int [] a = {2,3,4,5,6,7};
            int v = test.firstMissingPositive(a);
            u.printArray(a);
            p("v = %2d\n", v);
            assertEquals(1, v);
        }
        {
            int [] a = {3,4,5,6,7};
            int v = test.firstMissingPositive(a);
            u.printArray(a);
            p("v = %2d\n", v);
            assertEquals(1, v);
        }
        {
            int [] a = {1,2,3,5};
            int v = test.firstMissingPositive(a);
            u.printArray(a);
            p("v = %2d\n", v);
            assertEquals(4, v);
        }
        {
            int [] a = {4,1,2,3};
            int v = test.firstMissingPositive(a);
            u.printArray(a);
            p("v = %2d\n", v);
            assertEquals(5, v);
        }
        {
            int [] a = {1,2,3,4,5,7};
            int v = test.firstMissingPositive(a);
            u.printArray(a);
            p("v = %2d\n", v);
            assertEquals(6, v);
        }
        {
            int [] a = {1,1000};
            int v = test.firstMissingPositive(a);
            u.printArray(a);
            p("v = %2d\n", v);
            assertEquals(2, v);
        }
        {
            int [] a = {3,1,4,10,13,2,14,1,11,20,5};
            int v = test.firstMissingPositive(a);
            u.printArray(a);
            p("v = %2d\n", v);
            assertEquals(6, v);
        }
        {
            int [] a = {3,1,-3,4,4,-7,10,13,2,14,1,-1,-1,1,13,-2,-5,4,3,4,11,20,11,5};
            int v = test.firstMissingPositive(a);
            u.printArray(a);
            p("v = %2d\n", v);
            assertEquals(6, v);
        }
    }

    public void testMapRandom() {
        p("run %s\n", new Object() {}.getClass().getEnclosingMethod().getName());
        Integer k;
        {
            MapRandom map = new MapRandom();
            map.put(10, "a");
            map.put(11, "b");
            map.put(12, "c");
            map.put(13, "d");
            map.put(14, "e");
            map.delete(11);
            map.delete(13);
            assertEquals(map.get(10), "a");
            assertEquals(map.get(11), null);
            assertEquals(map.get(12), "c");
            assertEquals(map.get(13), null);
            assertEquals(map.get(14), "e");
            for(int i = 0; i < 10; i++) {
                k = map.randKey();
                switch(k) {
                case 10:
                case 12:
                case 14: break;
                default: assertNotNull(k);
                }
            }
            map.put(15, "f");
            map.put(16, "g");
            map.put(17, "h");
            map.put(18, "i");
            map.delete(10);
            map.delete(15);

            map.put(19, "j");
            map.delete(19);

            assertEquals(map.get(10), null);
            assertEquals(map.get(11), null);
            assertEquals(map.get(12), "c");
            assertEquals(map.get(13), null);
            assertEquals(map.get(14), "e");
            assertEquals(map.get(15), null);
            assertEquals(map.get(16), "g");
            assertEquals(map.get(17), "h");
            assertEquals(map.get(18), "i");
            assertEquals(map.get(19), null);
            for(int i = 0; i < 10; i++) {
                k = map.randKey();
                switch(k) {
                case 12:
                case 14:
                case 16:
                case 17:
                case 18:
                break;
                default: assertNotNull(k);
                }
            }
        }
        p("testMapRandom passed\n");
    }
    public void testStringToInt() {
        int v;
        v = test.stringToInt("1234");
        assertEquals(1234, v);
        v = test.stringToInt("-1234");
        assertEquals(-1234, v);
        v = test.stringToInt("-1");
        assertEquals(-1, v);
        v = test.stringToInt("+1234");
        assertEquals(1234, v);
        v = test.stringToInt("+1");
        assertEquals(1, v);
        v = test.stringToInt("   1234");
        assertEquals(1234, v);
        v = test.stringToInt("   1234blah");
        assertEquals(1234, v);
        v = test.stringToInt("2147483648");
        assertEquals(0x7fff_ffff, v);
        v = test.stringToInt("-2147483647");
        assertEquals(-2147483647, v);
        v = test.stringToInt("-2147483648");
        assertEquals(-2147483648, v);
        v = test.stringToInt(" b11228552307");
        assertEquals(0, v);
        p("pass testStringToInt\n");
    }
    public void testLRUCache() {
        {
            LRUCache lru = new LRUCache(2);
            int v;
            lru.put(1,1);
            lru.put(2,2);
            v = lru.get(1);
            assertEquals(1, v);
            lru.put(3,3);
            v = lru.get(2);
            assertEquals(-1, v);
            lru.put(4,4);
            v = lru.get(1);
            assertEquals(-1, v);
            v = lru.get(3);
            assertEquals(3, v);
            v = lru.get(4);
            assertEquals(4, v);
        }
    }
    ListNode buildList(List<Integer> l) {
        ListNode h = null;
        ListNode c = null;
        for(int i = 0; i < l.size(); i++) {
            ListNode n = new ListNode(l.get(i));
            if(h == null) {
                h = n;
            } else {
                c.next = n;
            }
            c = n;
        }
        return h;
    }
    public void testSwapPairs() {
        {
            p("-------------\n");
            ListNode h = buildList(Arrays.asList(1));
            printListNode(h, true);
            h = test.swapPairs(h);
            printListNode(h, true);
        }
        {
            p("-------------\n");
            ListNode h = buildList(Arrays.asList(1,2));
            printListNode(h, true);
            h = test.swapPairs(h);
            printListNode(h, true);
        }
        {
            p("-------------\n");
            ListNode h = buildList(Arrays.asList(1,2,3));
            printListNode(h, true);
            h = test.swapPairs(h);
            printListNode(h, true);
        }
        {
            p("-------------\n");
            ListNode h = buildList(Arrays.asList(1,2,3,4));
            printListNode(h, true);
            h = test.swapPairs(h);
            printListNode(h, true);
        }
        {
            p("-------------\n");
            ListNode h = buildList(Arrays.asList(1,2,3,4,5));
            printListNode(h, true);
            h = test.swapPairs(h);
            printListNode(h, true);
        }
        {
            p("-------------\n");
            ListNode h = buildList(Arrays.asList(1,2,3,4,5,6));
            printListNode(h, true);
            h = test.swapPairs(h);
            printListNode(h, true);
        }
    }
    public void testReverseKGroupSingle() {
        int i = 3;
        p("-------------\n");
        p("k=%d\n", i);
        ListNode h = buildList(Arrays.asList(1,2,3,4,5,6,7,8,9));
        printListNode(h, true);
        h = test.reverseKGroup(h,i);
        // 1 2 3 4 5 6 7 8 9
        // 3 2 1 6 5 4 9 8 7
        printListNode(h, true);
    }
    public void testReverseKGroup() {
        {
            p("-------------\n");
            p("k=1\n");
            ListNode h = buildList(Arrays.asList(1));
            printListNode(h, true);
            h = test.reverseKGroup(h,1);
            printListNode(h, true);
        }
        {
            p("-------------\n");
            p("k=2\n");
            ListNode h = buildList(Arrays.asList(1,2));
            printListNode(h, true);
            h = test.reverseKGroup(h,2);
            printListNode(h, true);
        }
        {
            p("-------------\n");
            p("k=3\n");
            ListNode h = buildList(Arrays.asList(1,2));
            printListNode(h, true);
            h = test.reverseKGroup(h,3);
            printListNode(h, true);
        }
        {
            p("-------------\n");
            p("k=2\n");
            ListNode h = buildList(Arrays.asList(1,2,3));
            printListNode(h, true);
            h = test.reverseKGroup(h,2);
            printListNode(h, true);
        }
        {
            p("-------------\n");
            p("k=3\n");
            ListNode h = buildList(Arrays.asList(1,2,3));
            printListNode(h, true);
            h = test.reverseKGroup(h,3);
            printListNode(h, true);
        }
        {
            p("-------------\n");
            p("k=4\n");
            ListNode h = buildList(Arrays.asList(1,2,3,4));
            printListNode(h, true);
            h = test.reverseKGroup(h,4);
            printListNode(h, true);
        }
        for(int i = 1; i <= 6; i++) {
            p("-------------\n");
            p("k=%d\n", i);
            ListNode h = buildList(Arrays.asList(1,2,3,4,5,6));
            printListNode(h, true);
            h = test.reverseKGroup(h,i);
            printListNode(h, true);
        }
        for(int i = 1; i <= 9; i++) {
            p("-------------\n");
            p("k=%d\n", i);
            ListNode h = buildList(Arrays.asList(1,2,3,4,5,6,7,8,9));
            printListNode(h, true);
            h = test.reverseKGroup(h,i);
            printListNode(h, true);
        }
    }
    public void badtestMediaOfTwoSortedArrays() {
        int method = 4;
        {
            int [] a1 = {1,3,5,7,9,11,13};
            int [] a2 = {2,4,6,8,10,12,14,16};
            double m = test.medianOfTwoSortedArrays(a1, a2, method);
            p("median %f\n", m);
        }
        {
            int [] a1 = {1,3,5,7,9,11,13};
            int [] a2 = {2,4,6,8,10,12,14};
            double m = test.medianOfTwoSortedArrays(a1, a2, method);
            p("median %f\n", m);
        }
        {
            int [] a1 = {1,3,5,7,9,11,13};
            int [] a2 = {14,16,18,20,22,24};
            double m = test.medianOfTwoSortedArrays(a1, a2, method);
            p("median %f\n", m);
        }
        {
            int [] a1 = {1,3,5,7,9,11,13};
            int [] a2 = {14,16,18,20,22,24,26};
            double m = test.medianOfTwoSortedArrays(a1, a2, method);
            p("median %f\n", m);
        }
    }
    public void testReconstructQByHeight() {
        {
            int [][] a = {{7,0},{4,4},{7,1},{5,0},{6,1},{5,2}};
            int [][] r = test.reconstructQByHeight(a);
            u.printMatrix(a);
            u.print("------------------\n");
            u.printMatrix(r);
        }
        u.print("------------------\n");
        {
            int [][] a = {{7,0},{4,4},{7,1},{5,0},{6,1},{7,2},{5,2}};
            int [][] r = test.reconstructQByHeight(a);
            u.printMatrix(a);
            u.print("------------------\n");
            u.printMatrix(r);
        }
    }
    public void testJump2() {
        {
            int [] a = {2,3,1,1,4};
            int v = test.jump2(a);
            assertEquals(2, v);
        }
        {
            int [] a = {2,3,0,1,4};
            int v = test.jump2(a);
            assertEquals(2, v);
        }
        {
            int [] a = {1,1,1,1};
            int v = test.jump2(a);
            assertEquals(3, v);
        }
    }
    public void testValidParenthesis() {
        {
            String s = "{}()[]";
            boolean res = test.validParenthesis(s);
            assertEquals(true, res);
        }
        {
            String s = "{([]{}[()])}";
            boolean res = test.validParenthesis(s);
            assertEquals(true, res);
        }
        {
            String s = "({([]{}[()])}";
            boolean res = test.validParenthesis(s);
            assertEquals(false, res);
        }
    }
    public void testLetterCombinationsPhone() {
        {
            String s = "23";
            List<String> l = test.letterCombinationsPhone(s);
            for(String sres: l) {
                p("%s\n", sres);
            }
        }
    }
    public void testGenerateParenthesis() {
        {
            List<String> l = test.generateParenthesis(3);
            for(String sres: l) {
                p("%s\n", sres);
            }
        }
    }
    public void testSearchRange() {
        {
            int [] a = {5,7,7,8,8,10};
            int [] r = test.searchRange(a, 8);
            assertEquals(3, r[0]);
            assertEquals(4, r[1]);
        }
        {
            int [] a = {1,2,3};
            int [] r = test.searchRange(a, 2);
            assertEquals(1, r[0]);
            assertEquals(1, r[1]);
        }
        {
            int [] a = {2,2,2};
            int [] r = test.searchRange(a, 2);
            assertEquals(0, r[0]);
            assertEquals(2, r[1]);
        }
    }
    public void testBinsearchIterDuplicates() {
        int LEFT = 1;
        int RIGHT = 2;
        int idx;
        {
            int [] a = {5,5,6,6,6,6,7,8,8};
            idx = test.binsearchIterDuplicates(a, 5, LEFT);
            assertEquals(0, idx);
            idx = test.binsearchIterDuplicates(a, 5, RIGHT);
            assertEquals(1, idx);
            idx = test.binsearchIterDuplicates(a, 6, LEFT);
            assertEquals(2, idx);
            idx = test.binsearchIterDuplicates(a, 6, RIGHT);
            assertEquals(5, idx);
            idx = test.binsearchIterDuplicates(a, 7, LEFT);
            assertEquals(6, idx);
            idx = test.binsearchIterDuplicates(a, 7, RIGHT);
            assertEquals(6, idx);
            idx = test.binsearchIterDuplicates(a, 8, LEFT);
            assertEquals(7, idx);
            idx = test.binsearchIterDuplicates(a, 8, RIGHT);
            assertEquals(8, idx);
        }
    }
    public void testSerialGeneratorI() {
        {
            Algos.SerialGeneratorI gen = new Algos.SerialGeneratorI();

        }
    }
    public void badtestRandomGeneratorI() {
        {
            int max = 30;
            Algos.RandomGeneratorI gen = new Algos.RandomGeneratorI(max);
            List<Integer> l = new ArrayList<>();
            Set<Integer> set = new HashSet<>();
            int szbuf = 20;
            int numiterations = 10;
            for(int i = 0; i < numiterations; i++) {
                for(int j = 0; j < szbuf; j++) {
                    int v = gen.nextId();
                    assertEquals(false, set.contains(v));
                    set.add(v);
                    l.add(v);
                }
                u.shuffle(l);
                for(int j = 0; j < szbuf; j++) {
                    int v = l.get(j);
                    boolean res = gen.delete(v);
                    assertEquals(res, true);
                }
            }

        }
    }
    public void testSortColors() {
        {
            int [] a = {0,1};
            test.sortColors(a);
            u.printArray(a);
            p("----------\n");
        }
        {
            int [] a = {0,2};
            test.sortColors(a);
            u.printArray(a);
            p("----------\n");
        }
        {
            int [] a = {1,0};
            test.sortColors(a);
            u.printArray(a);
            p("----------\n");
        }
        {
            int [] a = {2,1};
            test.sortColors(a);
            u.printArray(a);
            p("----------\n");
        }
        {
            int [] a = {2,0};
            test.sortColors(a);
            u.printArray(a);
            p("----------\n");
        }
        {
            int [] a = {2,1,0,2,1,0};
            test.sortColors(a);
            u.printArray(a);
            p("----------\n");
        }
        {
            int [] a = {1,2,0};
            test.sortColors(a);
            u.printArray(a);
            p("----------\n");
        }
    }
    public void testMinWindow() {
        {
            String ss = "bdab";
            String st = "ab";
            String r = test.minWindow(ss, st);
            assertEquals("ab", r);
        }
    }
    public void testRemoveDuplicates() {
        p("run %s\n", new Object() {}.getClass().getEnclosingMethod().getName());
        {
            int [] a = {1,1,2};
            int r = test.removeDuplicates(a);
            assertEquals(2,r);
        }
        {
            int [] a = {1,1,1,2};
            int r = test.removeDuplicates(a);
            assertEquals(2,r);
        }
        {
            int [] a = {1,1,2,2,2};
            int r = test.removeDuplicates(a);
            assertEquals(2,r);
        }
        {
            int [] a = {1,1,2,2,2,3,3};
            int r = test.removeDuplicates(a);
            assertEquals(3,r);
        }
        {
            int [] a = {1,1,2,3,3};
            int r = test.removeDuplicates(a);
            assertEquals(3,r);
        }
        {
            int [] a = {1};
            int r = test.removeDuplicates(a);
            assertEquals(1,r);
        }
        {
            int [] a = {1,2};
            int r = test.removeDuplicates(a);
            assertEquals(2,r);
        }
        {
            int [] a = {1,2,3};
            int r = test.removeDuplicates(a);
            assertEquals(3,r);
        }
        p("passed remove duplicates\n");
    }
    public void testRegex1() {
        p("run %s\n", new Object() {}.getClass().getEnclosingMethod().getName());
        // regex no *, no escape
        {
            String s = "aaa";
            String p = "aa";
            boolean b = test.regexMatch(s, p, 1);
            assertEquals(b, false);
        }
        {
            String s = "aaa";
            String p = "aaa";
            boolean b = test.regexMatch(s, p, 1);
            assertEquals(b, true);
        }
        {
            String s = "aaabb";
            String p = "aabb";
            boolean b = test.regexMatch(s, p, 1);
            assertEquals(b, false);
        }
        {
            String s = "ababa";
            String p = "ababa";
            boolean b = test.regexMatch(s, p, 1);
            assertEquals(b, true);
        }
        {
            String s = "aaa";
            String p = "aa.";
            boolean b = test.regexMatch(s, p, 1);
            assertEquals(b, true);
        }
        {
            String s = "aaa";
            String p = "a.a";
            boolean b = test.regexMatch(s, p, 1);
            assertEquals(b, true);
        }
        {
            String s = "aaa";
            String p = ".aa";
            boolean b = test.regexMatch(s, p, 1);
            assertEquals(b, true);
        }
        {
            String s = "aaa";
            String p = "aa..";
            boolean b = test.regexMatch(s, p, 1);
            assertEquals(b, false);
        }
    }
    public void testRegex2() {
        // regex no *, has escape
    }
    public void testRegex3() {
        p("run %s\n", new Object() {}.getClass().getEnclosingMethod().getName());
        // regex with * support
        {
            String s = "aaa";
            String p = "aa";
            boolean b = test.regexMatch(s, p, 3);
            assertEquals(b, false);
        }
        {
            String s = "aaa";
            String p = "aaa";
            boolean b = test.regexMatch(s, p, 3);
            assertEquals(b, true);
        }
        {
            String s = "aaabb";
            String p = "aabb";
            boolean b = test.regexMatch(s, p, 3);
            assertEquals(b, false);
        }
        {
            String s = "ababa";
            String p = "ababa";
            boolean b = test.regexMatch(s, p, 3);
            assertEquals(b, true);
        }
        {
            String s = "aaa";
            String p = "aa.";
            boolean b = test.regexMatch(s, p, 3);
            assertEquals(b, true);
        }
        {
            String s = "aaa";
            String p = "a.a";
            boolean b = test.regexMatch(s, p, 3);
            assertEquals(b, true);
        }
        {
            String s = "aaa";
            String p = ".aa";
            boolean b = test.regexMatch(s, p, 3);
            assertEquals(b, true);
        }
        {
            String s = "aaa";
            String p = "aa..";
            boolean b = test.regexMatch(s, p, 3);
            assertEquals(b, false);
        }
        {
            String s = "aaa";
            String p = "a*";
            boolean b = test.regexMatch(s, p, 3);
            assertEquals(b, true);
        }
        {
            String s = "aaa";
            String p = ".*a";
            boolean b = test.regexMatch(s, p, 3);
            assertEquals(b, true);
        }
        {
            String s = "aaa";
            String p = "a.*a";
            boolean b = test.regexMatch(s, p, 3);
            assertEquals(b, true);
        }
        {
            String s = "aaa";
            String p = ".*";
            boolean b = test.regexMatch(s, p, 3);
            assertEquals(b, true);
        }
        {
            String s = "abcaa";
            String p = "ab.*aa";
            boolean b = test.regexMatch(s, p, 3);
            assertEquals(b, true);
        }
        {
            String s = "abcaa";
            String p = "abc*aa";
            boolean b = test.regexMatch(s, p, 3);
            assertEquals(b, true);
        }
        {
            String s = "abcdaa";
            String p = "abc*aa";
            boolean b = test.regexMatch(s, p, 3);
            assertEquals(b, false);
        }
        {
            String s = "abcccaa";
            String p = "abc*aa";
            boolean b = test.regexMatch(s, p, 3);
            assertEquals(b, true);
        }
        {
            String s = "abaa";
            String p = "abc*aa";
            boolean b = test.regexMatch(s, p, 3);
            assertEquals(b, true);
        }
        {
            String s = "abdaa";
            String p = "abc*aa";
            boolean b = test.regexMatch(s, p, 3);
            assertEquals(b, false);
        }
        {
            String s = "aab";
            String p = "c*a*b";
            boolean b = test.regexMatch(s, p, 3);
            assertEquals(b, true);
        }
        {
            String s = "a";
            String p = "ab*";
            boolean b = test.regexMatch(s, p, 3);
            assertEquals(b, true);
        }
        {
            String s = "aaaaab";
            String p = "a*a*a*b";
            boolean b = test.regexMatch(s, p, 3);
            assertEquals(b, true);
        }
    }
    public void testReverseInteger() {
        p("run %s\n", new Object() {}.getClass().getEnclosingMethod().getName());
        {
            int v = 123;
            int r = test.reverseInteger(v);
            assertEquals(321, r);
        }
        {
            int v = -123;
            int r = test.reverseInteger(v);
            assertEquals(-321, r);
        }
        {
            int v = 120;
            int r = test.reverseInteger(v);
            assertEquals(21, r);
        }
        {
            int v = 123456789;
            int r = test.reverseInteger(v);
            assertEquals(987654321, r);
        }
        {
            int v = 1534236469;
            int r = test.reverseInteger(v);
            assertEquals(0, r);
            //assertEquals(9646324351, r);
        }
        {
            int v = -1534236469;
            int r = test.reverseInteger(v);
            assertEquals(0, r);
            //assertEquals(2147483645, r);
        }
        {
            int v = -2147483648;
            int r = test.reverseInteger(v);
            assertEquals(0, r);
            // 126087180
        }
    }
    public void testSearchInRotatedSortedArray() {
        p("run %s\n", new Object() {}.getClass().getEnclosingMethod().getName());
        {
            int [] a = {0,1,2,3,4,5,6,7,8,9};
            int r = test.searchInRotatedSortedArray(a, 4);
            assertEquals(4, r);
        }
        {
            int [] a = {0,1,2,3,4,5,6,7,8,9};
            int r = test.searchInRotatedSortedArray(a, 3);
            assertEquals(3, r);
        }
        {
            int [] a = {0,1,2,3,4,5,6,7,8,9};
            int r = test.searchInRotatedSortedArray(a, 30);
            assertEquals(-1, r);
        }
        {
            int [] a = {0,1,2,3,4,5,6,7,8,9};
            int r = test.searchInRotatedSortedArray(a, 9);
            assertEquals(9, r);
        }
        {
            int [] a = {0,1,2,3,4,5,6,7,8,9};
            int r = test.searchInRotatedSortedArray(a, 0);
            assertEquals(0, r);
        }
        {
            int [] a = {4,5,6,7,8,9,0,1,2,3};
            int r = test.searchInRotatedSortedArray(a, 3);
            assertEquals(9, r);
        }
        {
            int [] a = {4,5,6,7,8,9,0,1,2,3};
            int r = test.searchInRotatedSortedArray(a, 5);
            assertEquals(1, r);
        }
        {
            int [] a = {4,5,6,7,8,9,0,1,2,3};
            int r = test.searchInRotatedSortedArray(a, 10);
            assertEquals(r,-1);
        }
        {
            int [] a = {4,5,6,7,8,9,0,1,2,3};
            int r = test.searchInRotatedSortedArray(a, 4);
            assertEquals(0, r);
        }
        {
            int [] a = {4,5,6,7,8,9,0,1,2,3};
            int r = test.searchInRotatedSortedArray(a, 0);
            assertEquals(6, r);
        }
        {
            int [] a = {2,3,4,5,6,7,8,9,0,1};
            int r = test.searchInRotatedSortedArray(a, 0);
            assertEquals(8, r);
        }
        {
            int [] a = {2,3,4,5,6,7,8,9,0,1};
            int r = test.searchInRotatedSortedArray(a, 10);
            assertEquals(r,-1);
        }
    }
    public void testSearchRotate() {
        p("run %s\n", new Object() {}.getClass().getEnclosingMethod().getName());
        {
            int [] a = new int[9];
            for(int i = 0; i < 9; i++) {
                int v = 0;
                for(int j = i; j < 9; j++) {
                    a[j] = v++;
                }
                for(int j = 0; j < i; j++) {
                    a[j] = v++;
                }
                int r = test.searchRotateIdx(a);
                u.printArray(a);
                p("r = %d\n", r);
                assertEquals(r, i);
            }
        }
        {
            int [] a = new int[10];
            for(int i = 0; i < 10; i++) {
                int v = 0;
                for(int j = i; j < 10; j++) {
                    a[j] = v++;
                }
                for(int j = 0; j < i; j++) {
                    a[j] = v++;
                }
                int r = test.searchRotateIdx(a);
                u.printArray(a);
                p("r = %d\n", r);
                assertEquals(r, i);
            }
        }
        p("pass\n");
    }
    public void testMaxSubArray() {
        p("run %s\n", new Object() {}.getClass().getEnclosingMethod().getName());
        {
            int [] a = {2,3,4,5};
            int r = test.maxSubArray(a);
            assertEquals(r, 14);
        }
        {
            int [] a = {-2};
            int r = test.maxSubArray(a);
            assertEquals(r, -2);
        }
        {
            int [] a = {10,-2,3};
            int r = test.maxSubArray(a);
            assertEquals(r, 11);
        }
        {
            int [] a = {10,-2,10};
            int r = test.maxSubArray(a);
            assertEquals(r, 18);
        }
        {
            int [] a = {5,-6,7};
            int r = test.maxSubArray(a);
            assertEquals(r, 7);
        }
        {
            int [] a = {-2,5,-2,7};
            int r = test.maxSubArray(a);
            assertEquals(r, 10);
        }
    }
    public void testCombinationSum() {
        {

        }
    }
    public void testQsortNoDup() {
        p("run %s\n", new Object() {}.getClass().getEnclosingMethod().getName());
        p("run %s\n", new Object() {}.getClass().getEnclosingMethod().getName());
        {
            u.print("qsort case 1\n");
            int [] aref = {1,2,3,4,5,6};
            int [] acop = {5,6,4,1,2,3};
            u.printArray(acop);
            test.qsort(acop, false);
            u.printArray(acop);
            for(int i = 0; i < aref.length; i++) {
                assertEquals(aref[i], acop[i]);
            }
        }
        {
            u.print("qsort case 2\n");
            int [] aref = {1,2,3,4,5,6,7,8,9,10};
            int [] acop = {7,9,5,6,10,4,1,8,2,3};
            //int [] acop = {2,9,6,1,3,10,5,7,4,8};
            //u.shuffle(acop);
            u.printArray(acop);
            test.qsort(acop, false);
            u.printArray(acop);
            for(int i = 0; i < aref.length; i++) {
                assertEquals(aref[i], acop[i]);
            }
        }
        for(int idx = 0; idx < 3; idx++)
        {
            u.print("qsort variable %d\n", idx);
            int [] aref = {1,2,3,4,5,6,7,8,9,10};
            int [] acop = {1,2,3,4,5,6,7,8,9,10};
            u.shuffle(acop);
            u.printArray(acop);
            test.qsort(acop, false);
            u.printArray(acop);
            for(int i = 0; i < aref.length; i++) {
                assertEquals(aref[i], acop[i]);
            }
        }
        u.print("pass\n");
    }
    public void badtestQsortDup() {
        p("run %s\n", new Object() {}.getClass().getEnclosingMethod().getName());
        {
            u.print("qsort case 1\n");
            int [] aref = {1,2,2,3,4,5,5,5,6,6};
            int [] acop = {5,6,4,2,1,6,2,5,3,5};
            u.printArray(acop);
            test.qsort(acop, true);
            u.printArray(acop);
            for(int i = 0; i < aref.length; i++) {
                assertEquals(aref[i], acop[i]);
            }
        }
        {
            u.print("qsort case 1\n");
            int [] aref = {1,2,2,3,4,5,5,5,6,6};
            int [] acop = {5,6,4,2,2,6,5,3,5,1};
            u.printArray(acop);
            test.qsort(acop, true);
            u.printArray(acop);
            for(int i = 0; i < aref.length; i++) {
                assertEquals(aref[i], acop[i]);
            }
        }
        {
            u.print("qsort case 2\n");
            int [] aref = {1,2,3,4,5,6,7,8,9,10};
            int [] acop = {7,9,5,6,10,4,1,8,2,3};
            u.printArray(acop);
            test.qsort(acop, true);
            u.printArray(acop);
            for(int i = 0; i < aref.length; i++) {
                assertEquals(aref[i], acop[i]);
            }
        }
        for(int idx = 0; idx < 3; idx++)
        {
            int [] aref = {1,1,2,2,2,3,4,4,5,5,6,7,7,7,8,9,10,10,10};
            int [] acop = {1,1,2,2,2,3,4,4,5,5,6,7,7,7,8,9,10,10,10};
            u.shuffle(acop);
            test.qsort(acop, true);
            u.printArray(acop);
            for(int i = 0; i < aref.length; i++) {
                assertEquals(aref[i], acop[i]);
            }
        }
    }
    public void testRomanToIntSingle() {
        p("run %s\n", new Object() {}.getClass().getEnclosingMethod().getName());
        {
            String s = "III";
            int v = test.romanToInt(s, 4);
            assertEquals(v, 3);
        }
        {
            String s = "IV";
            int v = test.romanToInt(s, 4);
            assertEquals(v, 4);
        }
        {
            String s = "X";
            int v = test.romanToInt(s, 4);
            assertEquals(v, 10);
        }
        {
            String s = "IX";
            int v = test.romanToInt(s, 4);
            assertEquals(v, 9);
        }
        {
            String s = "XC";
            int v = test.romanToInt(s, 4);
            assertEquals(v, 90);
        }
        {
            String s = "CMXCIX";
            int v = test.romanToInt(s, 4);
            assertEquals(v, 999);
        }
        {
            String s = "MMMCMXCIX";
            int v = test.romanToInt(s, 4);
            assertEquals(v, 3999);
        }
        {
            String s = "IIX";
            int v = test.romanToInt(s, 4);
            assertEquals(v, 10);
        }
    }
    public void badtestRomanToInt() {
        p("run %s\n", new Object() {}.getClass().getEnclosingMethod().getName());
        {
            String s = "IX";
            int v1, v;
            v1 = test.romanToInt(s, 1);
            assertEquals(v1, 10);
            v = test.romanToInt(s, 2);
            assertEquals(v1, v);
            v = test.romanToInt(s, 3);
            assertEquals(v1, v);
            v = test.romanToInt(s, 4);
            assertEquals(v1, v);
        }
        {
            String s = "";
            int v1, v;
            v1 = test.romanToInt(s, 1);
            assertEquals(v1, 10);
            v = test.romanToInt(s, 2);
            assertEquals(v1, v);
            v = test.romanToInt(s, 3);
            assertEquals(v1, v);
            v = test.romanToInt(s, 4);
            assertEquals(v1, v);
        }
        {
            String s = "";
            int v1, v;
            v1 = test.romanToInt(s, 1);
            assertEquals(v1, 10);
            v = test.romanToInt(s, 2);
            assertEquals(v1, v);
            v = test.romanToInt(s, 3);
            assertEquals(v1, v);
            v = test.romanToInt(s, 4);
            assertEquals(v1, v);
        }
        {
            String s = "";
            int v1, v;
            v1 = test.romanToInt(s, 1);
            assertEquals(v1, 10);
            v = test.romanToInt(s, 2);
            assertEquals(v1, v);
            v = test.romanToInt(s, 3);
            assertEquals(v1, v);
            v = test.romanToInt(s, 4);
            assertEquals(v1, v);
        }
        {
            String s = "";
            int v1, v;
            v1 = test.romanToInt(s, 1);
            assertEquals(v1, 10);
            v = test.romanToInt(s, 2);
            assertEquals(v1, v);
            v = test.romanToInt(s, 3);
            assertEquals(v1, v);
            v = test.romanToInt(s, 4);
            assertEquals(v1, v);
        }
    }
    public void testIntToRoman() {

    }
    public void testRotateMatrix() {
        p("run %s\n", new Object() {}.getClass().getEnclosingMethod().getName());
        {
            int [][] a = {
                    {0, 1, 2},
                    {7, 8, 3},
                    {6, 5, 4}
            };
            u.printMatrix(a);
            u.p("\n");
            test.rotateMatrix(a);
            u.printMatrix(a);
            u.p("----------------\n");
        }
        {
            int [][] a = {
                    { 0, 1, 2, 3},
                    {11,12,13, 4},
                    {10,15,14, 5},
                    { 9, 8, 7, 6}
            };
            u.printMatrix(a);
            u.p("\n");
            test.rotateMatrix(a);
            u.printMatrix(a);
            u.p("----------------\n");
        }
        {
            int [][] a = {
                    { 0, 1, 2, 3, 4},
                    {15,16,17,18, 5},
                    {14,23,24,19, 6},
                    {13,22,21,20, 7},
                    {12,11,10, 9, 8},
            };
            u.printMatrix(a);
            u.p("\n");
            test.rotateMatrix(a);
            u.printMatrix(a);
            u.p("----------------\n");
        }
        {
            int [][] a = {
                    { 0, 1, 2, 3, 4, 5},
                    {19,20,21,22,23, 6},
                    {18,31,32,33,24, 7},
                    {17,30,35,34,25, 8},
                    {16,29,28,27,26, 9},
                    {15,14,13,12,11,10}
            };
            u.printMatrix(a);
            u.p("\n");
            test.rotateMatrix(a);
            u.printMatrix(a);
            u.p("----------------\n");
        }
    }
    public void testSubsets() {
        p("run %s\n", new Object() {}.getClass().getEnclosingMethod().getName());
        {
            int [] a = {1,2,3};
            List<List<Integer>> ll = test.subsets(a);
            for(List<Integer> l: ll) {
                u.printListInt(l, false);
            }
        }
    }
    public void testMergeIntervals() {
        p("run %s\n", new Object() {}.getClass().getEnclosingMethod().getName());
        {
            List<Interval> list = new ArrayList<>();
            list.add(new Interval(16,17));
            list.add(new Interval(10,13));
            list.add(new Interval(15,19));
            list.add(new Interval(6,8));
            list.add(new Interval(11,12));
            list.add(new Interval(2,10));
            list.add(new Interval(4,5));
            p("BEFORE\n");
            for(Interval v: list) {
                p("s=%2d e=%2d\n", v.start, v.end);
            }
            test.qsort(list);
            p("AFTER\n");
            for(Interval v: list) {
                p("s=%2d e=%2d\n", v.start, v.end);
            }
        }
        {
            List<Interval> list = new ArrayList<>();
            list.add(new Interval(16,17));
            list.add(new Interval(10,13));
            list.add(new Interval(15,19));
            list.add(new Interval(6,8));
            list.add(new Interval(11,12));
            list.add(new Interval(2,10));
            list.add(new Interval(4,5));
            List<Interval> l = test.merge(list);
            p("AFTER\n");
            for(Interval v: l) {
                p("s=%2d e=%2d\n", v.start, v.end);
            }
        }
        {
            List<Interval> list = new ArrayList<>();
            list.add(new Interval(1,4));
            list.add(new Interval(1,4));
            p("BEFORE\n");
            for(Interval v: list) {
                p("s=%2d e=%2d\n", v.start, v.end);
            }
            List<Interval> l = test.merge(list);
            p("AFTER\n");
            for(Interval v: l) {
                p("s=%2d e=%2d\n", v.start, v.end);
            }
        }
        {
            List<Interval> list = new ArrayList<>();
            list.add(new Interval(1,4));
            list.add(new Interval(0,4));
            p("BEFORE\n");
            for(Interval v: list) {
                p("s=%2d e=%2d\n", v.start, v.end);
            }
            List<Interval> l = test.merge(list);
            p("AFTER\n");
            for(Interval v: l) {
                p("s=%2d e=%2d\n", v.start, v.end);
            }
        }
    }
    public void testLongestConsecutiveSequence() {
        p("run %s\n", new Object() {}.getClass().getEnclosingMethod().getName());
        {
            /*
             * 0 1 2 3 4 5 6 7 8
             */
            int [] a = {0,3,7,2,5,8,4,6,0,1};
            int r = test.longestConsecutiveSequence(a);
            assertEquals(9, r);
        }
    }
    public void testUniquePaths() {
        p("run %s\n", new Object() {}.getClass().getEnclosingMethod().getName());
        {
            int v;
            v = test.uniquePaths(2, 2, 1);
            assertEquals(2, v);
            v = test.uniquePaths(2, 2, 2);
            assertEquals(2, v);
        }
        {
            int v;
            v = test.uniquePaths(4, 4, 1);
            assertEquals(20, v);
            v = test.uniquePaths(4, 4, 2);
            assertEquals(20, v);
        }
        {
            int v;
            v = test.uniquePaths(5, 5, 1);
            assertEquals(70, v);
            v = test.uniquePaths(5, 5, 2);
            assertEquals(70, v);
        }
    }
    public void badtestEditDIstance() {
        p("run %s\n", new Object() {}.getClass().getEnclosingMethod().getName());
        {
            /*
             *      e a t
             *    s 1 2 3
             *    e 1 2 3
             *    a 2 1 2
             *
             *      s e a
             *    s 0 1 2
             *    e 1 0 1
             *    a 2 1 0
             *
             *      t o p
             *    s 1 2 3
             *    e 2 2
             *    a 3
             */
            String s1 = "eat";
            String s2 = "sea";
            int r = test.editDistance(s1, s2);
            assertEquals(2, r);
        }
        {
            String s1 = "pneumonoultramicroscopicsilicovolcanoconiosis";
            String s2 = "ultramicroscopically";
            int r = test.editDistance(s1, s2);
            assertEquals(r, 27);
        }
    }
    public void testNumIslands() {
        p("run %s\n", new Object() {}.getClass().getEnclosingMethod().getName());
        {
            byte [][] g = {
                    {1,1,0,1,0},
                    {1,1,0,1,0},
                    {0,0,1,0,0},
                    {0,0,0,1,1}
            };
            int r = test.numIslands(g);
            assertEquals(r, 4);
        }
        {
            byte [][] g = {
                    {1,1,1,1,0},
                    {1,1,0,1,0},
                    {1,1,0,0,0},
                    {0,0,0,0,0}
            };
            int r = test.numIslands(g);
            assertEquals(r, 1);
        }
    }
    public void testMaxrate() {
        p("run %s\n", new Object() {}.getClass().getEnclosingMethod().getName());
        {
            int [] a = {5,-1,-2,-3,-4,-5};
            int r = test.maxrate(a);
            assertEquals(r, -6);
        }
        {
            int [] a = {5,9,-1,-3,4,5};
            int r = test.maxrate(a);
            assertEquals(r, 17);
        }
    }
    public void testSortRomanNames() {
        p("run %s\n", new Object() {}.getClass().getEnclosingMethod().getName());
        String [] aref = {
                "Abc I",
                "Abc II",
                "Abc III",
                "Abc IV",
                "Abc V",
                "Abc VI",
                "Abc VII",
                "Abc VIII",
                "Abc IX",
                "Abc X",
                "Abc XI",
                "Abc XIV",
                "Abc XV",
                "Abc XIX",
                "Abc XXIV",
                "Abc XLVIII",
                "Abc XLIX",
                "Abc L"
                };
        String [] acopy1 = aref.clone();
        String [] acopy2 = new String[aref.length];
        System.arraycopy(aref, 0, acopy2, 0, aref.length);
        u.shuffle(acopy1);
        {
            String [] a1 = test.sortRomanNames(acopy1);
            for(int i = 0; i < aref.length; i++) {
                assertEquals(aref[i], a1[i]);
            }
        }
    }
    public void testToArray() {
        p("run %s\n", new Object() {}.getClass().getEnclosingMethod().getName());
        String [] a = {"str1","str2","str3","str4"};
        List<String> l = Arrays.asList(a);
        {
            String [] a2 = test.toArray(l, 1);
            assertEquals(a2.length, a.length);
            for(int i = 0; i < a.length; i++) {
                assertEquals(a[i], a2[i]);
            }
        }
        {
            String [] a2 = test.toArray(l, 2);
            assertEquals(a2.length, a.length);
            for(int i = 0; i < a.length; i++) {
                assertEquals(a[i], a2[i]);
            }
        }
    }
    public void testMaxProductSubarray() {
        p("run %s\n", new Object() {}.getClass().getEnclosingMethod().getName());
        /*
         * -3
         * -3 0 3
         * -3 3
         * -3 3 4
         * -3 3 4 -5
         * -3 -2 3
         * 3 -2 -3
         * 3 -1  3
         */
        {
            int [] a = {-4,-3,-2};
            int r = test.maxProductSubarray(a);
            assertEquals(12,r);
        }
        {
            int [] a = {-3};
            int r = test.maxProductSubarray(a);
            assertEquals(-3,r);
        }
        {
            int [] a = {-3,0,3};
            int r = test.maxProductSubarray(a);
            assertEquals(3,r);
        }
        {
            int [] a = {-3,3};
            int r = test.maxProductSubarray(a);
            assertEquals(3,r);
        }
        {
            int [] a = {-3,-2,3};
            int r = test.maxProductSubarray(a);
            assertEquals(18,r);
        }
        {
            int [] a = {-3,3,4};
            int r = test.maxProductSubarray(a);
            assertEquals(12,r);
        }
        {
            int [] a = {-3,3,4,-2};
            int r = test.maxProductSubarray(a);
            assertEquals(72,r);
        }
        {
            int [] a = {2,-5,-2,-4,3};
            int r = test.maxProductSubarray(a);
            assertEquals(24,r);
        }
        {
            int [] a = {6,3,-10,0,2};
            int r = test.maxProductSubarray(a);
            assertEquals(18,r);
        }
    }
    public void testLowestCommonAncestor() {
        p("run %s\n", new Object() {}.getClass().getEnclosingMethod().getName());
        {
            TreeNode r = new TreeNode(1);
            TreeNode n = new TreeNode(2);
            r.right = n;
            TreeNode res = test.lowestCommonAncestor(r, r, n);
            assertEquals(r, res);
        }
        {
            TreeNode r = new TreeNode(1);
            TreeNode n1 = new TreeNode(2);
            r.right = n1;
            TreeNode n2 = new TreeNode(3);
            r.left = n2;
            TreeNode res = test.lowestCommonAncestor(r, n1, n2);
            assertEquals(r, res);
        }
    }
    public void testLowestCommonAncestorBST() {
        p("run %s\n", new Object() {}.getClass().getEnclosingMethod().getName());
        {
            TreeNode r = new TreeNode(1);
            TreeNode n = new TreeNode(2);
            r.right = n;
            TreeNode res = test.lowestCommonAncestor(r, r, n);
            assertEquals(r, res);
        }
        {
            TreeNode r = new TreeNode(1);
            TreeNode n1 = new TreeNode(2);
            r.right = n1;
            TreeNode n2 = new TreeNode(3);
            r.left = n2;
            TreeNode res = test.lowestCommonAncestor(r, n1, n2);
            assertEquals(r, res);
        }
    }
    public void testContainerMostWater() {

    }
    public void testCoinChange() {
        {
            int [] c = {1,5,10};
            int res = test.coinChange(c, 20);
            assertEquals(2, res);
        }
        {
            int [] c = {1,2,5};
            int res = test.coinChange(c, 100);
            assertEquals(20, res);
        }
        {
            int [] c = {1,2,5};
            int res = test.coinChange(c, 14);
            assertEquals(4, res);
        }
        {
            int [] c = {186,419,83,408};
            int res = test.coinChange(c, 6249);
            assertEquals(20, res);
        }
    }
    public void testMaxSquare() {
        {
            char [][] m = {
                    {'1','0','1','0','0'},
                    {'1','0','1','0','1'},
                    {'1','1','1','1','1'},
                    {'1','0','0','1','0'}
            };
            int r = test.maxSquare(m);
            assertEquals(r, 1);
        }
        {
            char [][] m = {
                    {'0','0','0'},
                    {'0','0','0'},
                    {'1','1','1'}
            };
            int r = test.maxSquare(m);
            assertEquals(r, 1);
        }
        {
            char [][] m = {
                    {'1','0','1','0','0'},
                    {'1','0','1','1','1'},
                    {'1','1','1','1','1'},
                    {'1','0','0','1','0'}
            };
            int r = test.maxSquare(m);
            assertEquals(r, 4);
        }
        {
            char [][] m = {
                    {'0','0','1','0'},
                    {'1','1','1','1'},
                    {'1','1','1','1'},
                    {'1','1','1','0'},
                    {'1','1','0','0'},
                    {'1','1','1','1'},
                    {'1','1','1','0'}
            };
            int r = test.maxSquare(m);
            assertEquals(r, 9);
        }
    }
    public void testTrappingRainWater() {
        p("run %s\n", new Object() {}.getClass().getEnclosingMethod().getName());
        /*
         *                 .
         *     .           .     .
         *   . .     .     .   . .
         *   . . .   . .   . . . .
         * . . . . . . . . . . . .
         * -------------------------
         * 1 3 4 2 1 3 2 1 5 2 3 4
         */
        {
            int [] a = {1,3,4,2,1,3,2,1,5,2,3,4};
            int area = test.trapRainwater(a, 3);
            assertEquals(14, area);
        }
        /*
         *
         *                         .   .
         *                 .       .   .
         *     .           .     . .   . .
         *   . .     .     .   . . .   . .     .
         *   . . .   . .   . . . . . . . . .   .
         * . . . . . . . . . . . . . . . . . . .
         * ------------------------------------
         * 1 3 4 2 1 3 2 1 5 2 3 4 6 2 6 4 2 1 3
         */
        {
            int [] a = {1,3,4,2,1,3,2,1,5,2,3,4,6,2,6,4,2,1,3};
            int area = test.trapRainwater(a, 3);
            assertEquals(24, area);
        }
    }
    public void testMinStack() {
        p("run %s\n", new Object() {}.getClass().getEnclosingMethod().getName());
        {
            /*
             * 8,5,4,5,7,8,3,6
             *                      list1           min     list2
             * push(8)              8               8       8
             * push(5)              5,8             5       5,8
             * push(4)              4,5,8           4       0,5,8
             * push(5)              5,4,5,8         4       1,0,5,8
             * push(7)              7,5,4,5,8       4       3,1,0,5,8
             * push(8)              8,7,5,4,5,8     4       4,3,1,0,5,8
             * getMin() = 4
             * top() = 8
             * pop() = 8            7,5,4,5,8
             * push(3)              3,7,5,4,5,8     3       -1,4,3,1,4,5,8
             * push(6)              6,3,7,5,4,5,8   3
             * getMin() = 3
             * pop() = 6            3,7,5,4,5,8     3
             * pop() = 3            7,5,4,5,8       4
             * getMin() = 4
             * pop() = 7            5,4,5,8         4
             * pop() = 5            4,5,8           4
             * pop() = 4            5,8             5
             * getMin() = 5
             * pop() = 5            8               8
             * pop() = 8            -               0
             *
             */
            int [] a = {8,5,4,5,7,8,3,6};
            MinStack.MinStackC1 minstack = new MinStack.MinStackC1();
            int i = 0;
            minstack.push(a[i++]);  // 8
            minstack.push(a[i++]);  // 5
            minstack.push(a[i++]);  // 4
            minstack.push(a[i++]);  // 5
            minstack.push(a[i++]);  // 7
            minstack.push(a[i++]);  // 8
            int r;
            r = minstack.getMin();
            assertEquals(r, 4);
            r = minstack.top();
            assertEquals(r, 8);
            r = minstack.pop();
            assertEquals(r, 8);
            minstack.push(a[i++]);  // 3
            minstack.push(a[i++]);  // 6
            r = minstack.getMin();
            assertEquals(r, 3);
            r = minstack.pop();
            assertEquals(r, 6);
            r = minstack.pop();
            assertEquals(r, 3);
            r = minstack.getMin();
            assertEquals(r, 4);
            r = minstack.pop();
            assertEquals(r, 7);
            r = minstack.pop();
            assertEquals(r, 5);
            r = minstack.pop();
            assertEquals(r, 4);
            r = minstack.getMin();
            assertEquals(r, 5);
            r = minstack.pop();
            assertEquals(r, 5);
            r = minstack.pop();
            assertEquals(r, 8);
            r = minstack.top();
            assertEquals(r, 0);
            p("pass 1\n");
        }
        {
            /*
             * 8,5,4,5,7,8,3,6
             *                      list1           min     list2
             * push(8)              8               8       8
             * push(5)              5,8             5       5,8
             * push(4)              4,5,8           4       0,5,8
             * push(5)              5,4,5,8         4       1,0,5,8
             * push(7)              7,5,4,5,8       4       3,1,0,5,8
             * push(8)              8,7,5,4,5,8     4       4,3,1,0,5,8
             * getMin() = 4
             * top() = 8
             * pop() = 8            7,5,4,5,8
             * push(3)              3,7,5,4,5,8     3       -1,4,3,1,4,5,8
             * push(6)              6,3,7,5,4,5,8   3
             * getMin() = 3
             * pop() = 6            3,7,5,4,5,8     3
             * pop() = 3            7,5,4,5,8       4
             * getMin() = 4
             * pop() = 7            5,4,5,8         4
             * pop() = 5            4,5,8           4
             * pop() = 4            5,8             5
             * getMin() = 5
             * pop() = 5            8               8
             * pop() = 8            -               0
             *
             */
            int [] a = {8,5,4,5,7,8,3,6};
            MinStack.MinStackC1 minstack = new MinStack.MinStackC1();
            int i = 0;
            minstack.push(a[i++]);  // 8
            minstack.push(a[i++]);  // 5
            minstack.push(a[i++]);  // 4
            minstack.push(a[i++]);  // 5
            minstack.push(a[i++]);  // 7
            minstack.push(a[i++]);  // 8
            int r;
            r = minstack.getMin();
            assertEquals(r, 4);
            r = minstack.top();
            assertEquals(r, 8);
            r = minstack.pop();
            assertEquals(r, 8);
            minstack.push(a[i++]);  // 3
            minstack.push(a[i++]);  // 6
            r = minstack.getMin();
            assertEquals(r, 3);
            r = minstack.pop();
            assertEquals(r, 6);
            r = minstack.pop();
            assertEquals(r, 3);
            r = minstack.getMin();
            assertEquals(r, 4);
            r = minstack.pop();
            assertEquals(r, 7);
            r = minstack.pop();
            assertEquals(r, 5);
            r = minstack.pop();
            assertEquals(r, 4);
            r = minstack.getMin();
            assertEquals(r, 5);
            r = minstack.pop();
            assertEquals(r, 5);
            r = minstack.pop();
            assertEquals(r, 8);
            r = minstack.top();
            assertEquals(r, 0);
            p("pass 2\n");
        }
    }
    public void testMinStack2() {
        {
            /*
             * 8,5,4,5,7,8,3,6
             *                      list1           min     list2
             * push(8)              8               8       8
             * push(5)              5,8             5       5,8
             * push(4)              4,5,8           4       0,5,8
             * push(5)              5,4,5,8         4       1,0,5,8
             * push(7)              7,5,4,5,8       4       3,1,0,5,8
             * push(8)              8,7,5,4,5,8     4       4,3,1,0,5,8
             * getMin() = 4
             * top() = 8
             * pop() = 8            7,5,4,5,8
             * push(3)              3,7,5,4,5,8     3       -1,4,3,1,4,5,8
             * push(6)              6,3,7,5,4,5,8   3
             * getMin() = 3
             * pop() = 6            3,7,5,4,5,8     3
             * pop() = 3            7,5,4,5,8       4
             * getMin() = 4
             * pop() = 7            5,4,5,8         4
             * pop() = 5            4,5,8           4
             * pop() = 4            5,8             5
             * getMin() = 5
             * pop() = 5            8               8
             * pop() = 8            -               0
             *
             */
            int [] a = {8,5,4,5,7,8,3,6};
            MinStack.MinStackC2 minstack = new MinStack.MinStackC2();
            int i = 0;
            minstack.push(a[i++]);  // 8
            minstack.push(a[i++]);  // 5
            minstack.push(a[i++]);  // 4
            minstack.push(a[i++]);  // 5
            minstack.push(a[i++]);  // 7
            minstack.push(a[i++]);  // 8
            int r;
            r = minstack.getMin();
            assertEquals(r, 4);
            r = minstack.top();
            assertEquals(r, 8);
            r = minstack.pop();
            assertEquals(r, 8);
            minstack.push(a[i++]);  // 3
            minstack.push(a[i++]);  // 6
            r = minstack.getMin();
            assertEquals(r, 3);
            r = minstack.pop();
            assertEquals(r, 6);
            r = minstack.pop();
            assertEquals(r, 3);
            r = minstack.getMin();
            assertEquals(r, 4);
            r = minstack.pop();
            assertEquals(r, 7);
            r = minstack.pop();
            assertEquals(r, 5);
            r = minstack.pop();
            assertEquals(r, 4);
            r = minstack.getMin();
            assertEquals(r, 5);
            r = minstack.pop();
            assertEquals(r, 5);
            r = minstack.pop();
            assertEquals(r, 8);
            r = minstack.top();
            assertEquals(r, 0);
            p("pass 2\n");
        }
    }
    public void testMinStack3() {
        p("run %s\n", new Object() {}.getClass().getEnclosingMethod().getName());
        {
            /*
             * 8,5,4,5,7,8,3,6
             *                      list1           min     list2
             * push(8)              8               8       8
             * push(5)              5,8             5       5,8
             * push(4)              4,5,8           4       0,5,8
             * push(5)              5,4,5,8         4       1,0,5,8
             * push(7)              7,5,4,5,8       4       3,1,0,5,8
             * push(8)              8,7,5,4,5,8     4       4,3,1,0,5,8
             * getMin() = 4
             * top() = 8
             * pop() = 8            7,5,4,5,8
             * push(3)              3,7,5,4,5,8     3       -1,4,3,1,4,5,8
             * push(6)              6,3,7,5,4,5,8   3
             * getMin() = 3
             * pop() = 6            3,7,5,4,5,8     3
             * pop() = 3            7,5,4,5,8       4
             * getMin() = 4
             * pop() = 7            5,4,5,8         4
             * pop() = 5            4,5,8           4
             * pop() = 4            5,8             5
             * getMin() = 5
             * pop() = 5            8               8
             * pop() = 8            -               0
             *
             */
            int [] a = {8,5,4,4,5,7,8,3,6,3};
            MinStack.MinStackC2 minstack = new MinStack.MinStackC2();
            int i = 0;
            minstack.push(a[i++]);  // 8
            minstack.push(a[i++]);  // 5
            minstack.push(a[i++]);  // 4
            minstack.push(a[i++]);  // 4
            minstack.push(a[i++]);  // 5
            minstack.push(a[i++]);  // 7
            minstack.push(a[i++]);  // 8
            int r;
            r = minstack.getMin();
            assertEquals(r, 4);
            r = minstack.top();
            assertEquals(r, 8);
            r = minstack.pop();
            assertEquals(r, 8);
            minstack.push(a[i++]);  // 3
            minstack.push(a[i++]);  // 6
            minstack.push(a[i++]);  // 3
            r = minstack.getMin();
            assertEquals(r, 3);
            r = minstack.pop();
            assertEquals(r, 3);
            r = minstack.pop();
            assertEquals(r, 6);
            r = minstack.pop();
            assertEquals(r, 3);
            r = minstack.getMin();
            assertEquals(r, 4);
            r = minstack.pop();
            assertEquals(r, 7);
            r = minstack.pop();
            assertEquals(r, 5);
            r = minstack.pop();
            assertEquals(r, 4);
            r = minstack.getMin();
            assertEquals(r, 4);
            r = minstack.pop();
            assertEquals(r, 4);
            r = minstack.getMin();
            assertEquals(r, 5);
            r = minstack.pop();
            assertEquals(r, 5);
            r = minstack.pop();
            assertEquals(r, 8);
            r = minstack.top();
            assertEquals(r, 0);
            p("pass 3\n");
        }
        /*
         * ["MinStack",
         * "push 2147483646",
         * "push 2147483646",
         * "push 2147483647",
         * "top",
         * "pop",
         * "getMin",
         * "pop",
         * "getMin",
         * "pop",
         * "push 2147483647",
         * "top",
         * "getMin",
         * "push -2147483648",
         * "top",
         * "getMin",
         * "pop",
         * "getMin"]
         */
        {
            MinStack.MinStackC2 minstack = new MinStack.MinStackC2();
            int v;
            minstack.push(2147483646);
            minstack.push(2147483646);
            minstack.push(2147483647);
            v = minstack.top();
            p("%d\n", v);
            v = minstack.pop();
            p("%d\n", v);
            v = minstack.getMin();
            p("%d\n", v);
            v = minstack.pop();
            p("%d\n", v);
            minstack.push(2147483647);
            v = minstack.top();
            p("%d\n", v);
            v = minstack.getMin();
            p("min  %d\n", v);
            minstack.push(-2147483648);
            p("push -2147483648\n");
            v = minstack.top();
            p("top  %d\n", v);
            v = minstack.getMin();
            p("min  %d\n", v);
            v = minstack.pop();
            p("pop  %d\n", v);
            v = minstack.getMin();
            p("min  %d\n", v);
        }
    }
    public void testMinMaxInt() {
        int min = Integer.MIN_VALUE;
        int min1 = -2147483648;
        int max = Integer.MAX_VALUE;
        int max1 = 2147483647;
        p("min:%12d 0x%x %12d\n", min, min, min1);
        p("max:%12d 0x%x %12d\n", max, max, max1);
    }
    public void testPrintIntegers() {
        test.printIntegers(0, 127);
    }
    public void testDecodeString() {
        p("run %s\n", new Object() {}.getClass().getEnclosingMethod().getName());
        {
            String si = "3[a]2[bc]";
            String so = test.decodeString(si);
            assertEquals(so, "aaabcbc");
        }
        {
            String si = "3[a2[c]]";
            String so = test.decodeString(si);
            assertEquals(so, "accaccacc");
        }
        {
            String si = "2[abc]3[cd]ef";
            String so = test.decodeString(si);
            assertEquals(so, "abcabccdcdcdef");
        }
        {
            String si = "a10[ab]3[cd]ef";
            String so = test.decodeString(si);
            assertEquals(so, "aababababababababababcdcdcdef");
        }
    }
    public void testArraysSort() {
        int [] a = {10,8,3,6,4,1,1,3,8,7};
        u.printArray(a);
        Arrays.sort(a);
        u.printArray(a);
    }
    public void testQuicksort() {
        p("run %s\n", new Object() {}.getClass().getEnclosingMethod().getName());
        int [] a1ref   = {02,04,07,10,10,14,14,14,17,19};
        int [] a1dup   = {17,07,10,14,04,10,14,19,02,14};
        int [] a2ref   = {02,04,07,10,10,14,14,14,17,19,19};
        int [] a2dup   = {19,17,07,10,14,04,10,14,19,02,14};
        {

        }
        {

        }
        {

        }
        {

        }
    }
    public void testTrapRainwater() {
        {
            /*
             *
             *
             * 6|
             * 5|
             * 4|                            *
             * 3|                         *  *                             *
             * 2|          *              *  *     *                       *
             * 1|    *     *  *     *  *  *  *     *        *     *  *     *
             * 0+-----------------------------------------------------------
             *  |00 01 02 03 04 05 06 07 08 09 10 11 12 13 14 15 16 17 18 19
             *  +-----------------------------------------------------------
             *  | 0  1  0  2  1  0  1  1  3  4  0  2  0  0  1  0  1  1  0  3
             *  +-----------------------------------------------------------
             *          1     1  2  1  1        3  1  3  3  2  3  2  2  3
             *
             *       1     2              3
             *       |  1  |  1  2  1  1  |  4                             3
             *                               |                             |
             *                               +  3  1  3  3  2  3  2  2  3
             *  ------------------------------------------------------------
             *          1     1  2  1  1        3  1  3  3  2  3  2  2  3    = 28
             *
             */
            int [] a = {0,1,0,2,1,0,1,1,3,4,0,2,0,0,1,0,1,1,0,3};
            int r = test.trapRainwater(a, 2);
            assertEquals(28, r);
        }
    }
    public void testGetHistogram() {
        p("run %s\n", new Object() {}.getClass().getEnclosingMethod().getName());
        {
            int [] a = {0,1,0,2,1,0,1,1,3,4,0,2,0,0,1,0,1,1,0,3};
            test.getHistogram(a, true);
        }
    }
    public void badtestAreaLargestRectangleHistogram() {
        p("run %s\n", new Object() {}.getClass().getEnclosingMethod().getName());
        /*
         * 6|
         * 5|
         * 4|          *
         * 3|    *     *     *
         * 2|    *  *  *  *  *
         * 1| *  *  *  *  *  *
         * 0+-----------------
         *  |00 01 02 03 04 05
         *  +-----------------
         *
         *    0  1  2  3
         *       0  0
         *
         */
        {
            int [] a = {1,3,2,4,3,5};
            int area = test.areaLargestRectangeHistogram(a, 4);
            assertEquals(25, area);
        }

        /*
         * 6|
         * 5|                *
         * 4|          *     *
         * 3|    *     *  *  *
         * 2|    *  *  *  *  *
         * 1| *  *  *  *  *  *
         * 0+-----------------
         *  |00 01 02 03 04 05
         *  +-----------------
         *  +-----------------
         *
         *    0  1  2
         *       0  0
         *
         */
        {
            int [] a = {1,3,2,4,3,5};
            int area = test.areaLargestRectangeHistogram(a, 4);
            assertEquals(25, area);
        }
        /*
         * 6|
         * 5| *
         * 4| *     *
         * 3| *  *  *     *
         * 2| *  *  *  *  *
         * 1| *  *  *  *  *  *
         * 0+-----------------
         *  |00 01 02 03 04 05
         *  +-----------------
         *  +-----------------
         *
         */
        {
            int [] a = {5,3,4,2,3,1};
            int area = test.areaLargestRectangeHistogram(a, 4);
            assertEquals(25, area);
        }
        /*
         * 6|
         * 5| *
         * 4| *     *                       *
         * 3| *  *  *                 *     *  *
         * 2| *  *  *  *           *  *  *  *  *
         * 1| *  *  *  *  *     *  *  *  *  *  *
         * 0+-----------------------------------
         *  |00 01 02 03 04 05 06 07 08 09 10 11
         *  +-----------------------------------
         *  +-----------------------------------
         *
         */
        {
            int [] a = {5,3,4,2,1,0,1,2,3,2,4,3};
            int area = test.areaLargestRectangeHistogram(a, 4);
            assertEquals(25, area);
        }
        /*
         * 6|
         * 5|
         * 4|                            *  *
         * 3|    *                       *  *        *  *  *           *
         * 2|    *  *  *              *  *  *     *  *  *  *           *
         * 1| *  *  *  *  *  *  *     *  *  *  *  *  *  *  *     *  *  *
         * 0+-----------------------------------------------------------
         *  |00 01 02 03 04 05 06 07 08 09 10 11 12 13 14 15 16 17 18 19
         *  +-----------------------------------------------------------
         *  | 1  3  2  2  1  1  1     2  4  4  1  2  3  3  3     1  1  3
         *  +-----------------------------------------------------------
         *  a -  -
         *  q-----------------------------------------------------------
         *    1  3  1
         *       1
         *
         *
         *
         */
        {
            int [] a = {1,3,2,2,1,1,1,0,2,4,4,1,2,3,3,3,0,1,1,3};
            int area = test.areaLargestRectangeHistogram(a, 3);
            assertEquals(9, area);
        }
        /*
         * 6|                      *  *
         * 5|                   *  *  *  *  *
         * 4|          *        *  *  *  *  *
         * 3|    *     *  *     *  *  *  *  *  *     *  *  *           *
         * 2|    *  *  *  *     *  *  *  *  *  *  *  *  *  *           *
         * 1| *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *  *
         * 0+-----------------------------------------------------------
         *  |00 01 02 03 04 05 06 07 08 09 10 11 12 13 14 15 16 17 18 19
         *  +-----------------------------------------------------------
         *  | 1  3  2  4  3  1  5  6  6  5  5  3  2  3  3  3  1  1  1  3
         *  +-----------------------------------------------------------
         *
         */
        {
            int [] a = {1,3,2,4,3,1,5,6,6,5,5,3,2,3,3,3,1,1,1,3};
            int area = test.areaLargestRectangeHistogram(a, 3);
            assertEquals(25, area);
        }
    }
    public void testLongestIncreasingSubsequence() {
        p("run %s\n", new Object() {}.getClass().getEnclosingMethod().getName());
        {
            int [] a = {10,9,2,5,3,7,101,18};
            int r = test.longestIncreasingSubsequence(a);
            assertEquals(4, r);
        }
        {
            int [] a = {2,2};
            int r = test.longestIncreasingSubsequence(a);
            assertEquals(1, r);
        }
    }
    public void testClimbStairs() {
        p("run %s\n", new Object() {}.getClass().getEnclosingMethod().getName());
        {
            int r;
            r = test.climbStairs(3);
            assertEquals(3, r);
            r = test.climbStairs(4);
            assertEquals(5, r);
            r = test.climbStairs(5);
            assertEquals(8, r);
            r = test.climbStairs(6);
            assertEquals(13, r);
            r = test.climbStairs(7);
            assertEquals(21, r);
        }
    }
    public void testBuildTreePreAndIn() {
        p("run %s\n", new Object() {}.getClass().getEnclosingMethod().getName());
        {
            int [] pre = {50, 25, 20, 30, 75, 70, 80};
            int [] in =  {20, 25, 30, 50, 70, 75, 80};
            TreeNode n = test.buildTreePreAndIn(pre, in);
            assertEquals(50, n.val);
        }
        {
            int [] pre = {50, 25, 20, 10, 30, 27, 33, 32, 75, 70, 65, 72, 73, 80};
            int [] in =  {10, 20, 25, 27, 30, 32, 33, 50, 65, 70, 72, 73, 75, 80};
            TreeNode n = test.buildTreePreAndIn(pre, in);
            assertEquals(50, n.val);
        }
    }
    public void testBuildTreePostAndIn() {
        p("run %s\n", new Object() {}.getClass().getEnclosingMethod().getName());
        {
            /*
             *                      50
             *              25              75
             *          20      30       70    80
             */
            //int [] pre = {50, 25, 20, 30, 75, 70, 80};
            int [] in =  {20, 25, 30, 50, 70, 75, 80};
            int [] pst = {20, 30, 25, 70, 80, 75, 50};
            TreeNode n = test.buildTreePostAndIn(pst, in);
            assertEquals(50, n.val);
        }
    }
    public void testReverseList() {
        p("run %s\n", new Object() {}.getClass().getEnclosingMethod().getName());
        {
            ListNode h = new ListNode(1);
            ListNode n = h;
            n.next = new ListNode(2);
            n = n.next;
            n.next = new ListNode(3);
            n = n.next;
            n.next = new ListNode(4);
            n = n.next;
            n.next = new ListNode(5);
            test.print(h);
            h = test.reverseList(h);
            test.print(h);
        }
    }
    public void testIsLinkedPalindrome() {
        p("run %s\n", new Object() {}.getClass().getEnclosingMethod().getName());
        {
            int [] a = {1};
            ListNode h = test.makeListNode(a);
            boolean r = test.isLinkedPalindrome(h);
            assertEquals(true, r);
        }
        {
            int [] a = {1,1};
            ListNode h = test.makeListNode(a);
            boolean r = test.isLinkedPalindrome(h);
            assertEquals(true, r);
        }
        {
            int [] a = {1,2};
            ListNode h = test.makeListNode(a);
            boolean r = test.isLinkedPalindrome(h);
            assertEquals(false, r);
        }
        {
            int [] a = {1,2,1};
            ListNode h = test.makeListNode(a);
            boolean r = test.isLinkedPalindrome(h);
            assertEquals(true, r);
        }
        {
            int [] a = {1,2,3,4,5};
            ListNode h = test.makeListNode(a);
            boolean r = test.isLinkedPalindrome(h);
            assertEquals(false, r);
        }
        {
            int [] a = {1,2,3,3,2,1};
            ListNode h = test.makeListNode(a);
            boolean r = test.isLinkedPalindrome(h);
            assertEquals(true, r);
        }
        {
            int [] a = {1,2,3,2,1};
            ListNode h = test.makeListNode(a);
            boolean r = test.isLinkedPalindrome(h);
            assertEquals(true, r);
        }
        {
            int [] a = {1,2,3,3,4,5};
            ListNode h = test.makeListNode(a);
            boolean r = test.isLinkedPalindrome(h);
            assertEquals(false, r);
        }
    }
    public void testGreaterTree() {
        p("run %s\n", new Object() {}.getClass().getEnclosingMethod().getName());
        {
            TreeNode r = new TreeNode(25);
            TreeNode n = null;
            r.right = new TreeNode(35);
            n = r.right;
            n.left = new TreeNode(30);
            n.right = new TreeNode(40);
            r.left = new TreeNode(15);
            n = r.left;
            n.left = new TreeNode(10);
            n.right = new TreeNode(20);
            r = test.convertGreaterTree(r);
            assertEquals(130, r.val);
        }
    }
    public void testProductOfArrayExceptSelf() {
        p("run %s\n", new Object() {}.getClass().getEnclosingMethod().getName());
        {
            int [] a = {3};
            int [] r = test.productOfArrayExceptSelf(a);
            u.printArray(a);
            u.printArray(r);
        }
        p("------------------\n");
        {
            int [] a = {3,2};
            int [] r = test.productOfArrayExceptSelf(a);
            u.printArray(a);
            u.printArray(r);
        }
        p("------------------\n");
        {
            int [] a = {3,2,4};
            int [] r = test.productOfArrayExceptSelf(a);
            u.printArray(a);
            u.printArray(r);
        }
        p("------------------\n");
        {
            int [] a = {3,2,4,5,6,3,2};
            int [] r = test.productOfArrayExceptSelf(a);
            u.printArray(a);
            u.printArray(r);
        }
    }
    public void testTopKFrequent() {
        p("run %s\n", new Object() {}.getClass().getEnclosingMethod().getName());
        {
            int [] b = {1,1,1,2,2,2,2,3,3,3,3,4,4,4,4,6,6,7,7,8};
            int [] a = {1,2,3,1,2,2,3,1,2,3,4,4,6,7,6,7,8,4,4,3};
            List<Integer> l = test.topKFrequent(a, 2);
            u.printArray(a);
            p("------------------\n");
            u.printArray(l);
        }
    }
    public void testPriorityQueue() {
        p("run %s\n", new Object() {}.getClass().getEnclosingMethod().getName());
        p("min queue\n");
        {
            int [] a = {8,4,7,1,3,2,6,9,5};
            PriorityQueue<Integer> pq = new PriorityQueue<>();
            for(int i = 0; i < a.length; i++) {
                pq.add(a[i]);
            }
            while(pq.size() != 0) {
                int v = pq.poll();
                p("%2d ", v);
            }
            p("\n");
        }
        p("now reverse order max queue\n");
        {
            int [] a = {8,4,7,1,3,2,6,9,5};
            PriorityQueue<Integer> pq = new PriorityQueue<>(Collections.reverseOrder());
            for(int i = 0; i < a.length; i++) {
                pq.add(a[i]);
            }
            while(pq.size() != 0) {
                int v = pq.poll();
                p("%2d ", v);
            }
            p("\n");
        }
    }
    public void testBinsearchTypes() {
        p("run %s\n", new Object() {}.getClass().getEnclosingMethod().getName());
        {
            int [] a = {1,3,5,7,9};
            int sz = a.length;
            for(int i = 0; i < sz; i++) {
                int v = a[i];
                int idx = test.binsearcheq(a, v, 0, sz-1);
                assertEquals(idx, i);
            }
        }
        {
            int [] a = {1,3,5,7,9,11};
            int sz = a.length;
            for(int i = 0; i < sz; i++) {
                int v = a[i];
                int idx = test.binsearcheq(a, v, 0, sz-1);
                assertEquals(idx, i);
            }
        }
        {
            int [] a = {1,3,5,7,9,11,13};
            int sz = a.length;
            for(int i = 0; i < sz; i++) {
                int v = a[i];
                int idx = test.binsearcheq(a, v, 0, sz-1);
                assertEquals(idx, i);
            }
        }
        {
            int [] a = {1,3,5,7,9};
            int sz = a.length;
            int v = 0;
            int idx;
            idx = test.binsearchlt(a, v, 0, sz-1);
            assertEquals(-1, idx);
            for(int i = 0; i < sz; i++) {
                v = a[i]+1;
                idx = test.binsearchlt(a, v, 0, sz-1);
                assertEquals(i, idx);
            }
        }        {
            int [] a = {1,3,5,7,9,11};
            int sz = a.length;
            int v = 0;
            int idx;
            idx = test.binsearchlt(a, v, 0, sz-1);
            assertEquals(-1, idx);
            for(int i = 0; i < sz; i++) {
                v = a[i]+1;
                idx = test.binsearchlt(a, v, 0, sz-1);
                assertEquals(i, idx);
            }
        }
        {
            int [] a = {1,3,5,7,9,11,13};
            int sz = a.length;
            int v = 0;
            int idx;
            idx = test.binsearchlt(a, v, 0, sz-1);
            assertEquals(-1, idx);
            for(int i = 0; i < sz; i++) {
                v = a[i]+1;
                idx = test.binsearchlt(a, v, 0, sz-1);
                assertEquals(i, idx);
            }
        }
        {
            int [] a = {1,3,5,7,9};
            int sz = a.length;
            int v = 12;
            int idx;
            idx = test.binsearchgt(a, v, 0, sz-1);
            assertEquals(sz, idx);
            for(int i = 0; i < sz; i++) {
                v = a[i]-1;
                idx = test.binsearchgt(a, v, 0, sz-1);
                assertEquals(i, idx);
            }
        }        {
            int [] a = {1,3,5,7,9,11};
            int sz = a.length;
            int v = 12;
            int idx;
            idx = test.binsearchgt(a, v, 0, sz-1);
            assertEquals(sz, idx);
            for(int i = 0; i < sz; i++) {
                v = a[i]-1;
                idx = test.binsearchgt(a, v, 0, sz-1);
                assertEquals(i, idx);
            }
        }
        {
            int [] a = {1,3,5,7,9,11,13};
            int sz = a.length;
            int v = 0;
            int idx;
            idx = test.binsearchgt(a, v, 0, sz-1);
            assertEquals(0, idx);
            for(int i = 0; i < sz; i++) {
                v = a[i]-1;
                idx = test.binsearchgt(a, v, 0, sz-1);
                assertEquals(i, idx);
            }
        }
    }
    public void testMergeKSortedLists1() {
        p("run %s\n", new Object() {}.getClass().getEnclosingMethod().getName());
        ListNode [] l = new ListNode[3];
        int ctr = 0;
        {
            ListNode n = new ListNode(10);
            ListNode h = n;
            n.next = new ListNode(20);
            n = n.next;
            n.next = new ListNode(30);
            n = n.next;
            n.next = new ListNode(40);
            n = n.next;
            l[ctr++] = h;
        }
        {
            ListNode n = new ListNode(15);
            ListNode h = n;
            n.next = new ListNode(25);
            n = n.next;
            n.next = new ListNode(35);
            n = n.next;
            n.next = new ListNode(45);
            n = n.next;
            l[ctr++] = h;
        }
        {
            ListNode n = new ListNode(11);
            ListNode h = n;
            n.next = new ListNode(21);
            n = n.next;
            n.next = new ListNode(31);
            n = n.next;
            n.next = new ListNode(41);
            n = n.next;
            l[ctr++] = h;
        }
        ListNode h = test.mergeKSortedLists(l);
        assertNotNull(h);
        assertEquals(10, h.val);
    }
    public void testMergeKSortedLists2() {
        p("run %s\n", new Object() {}.getClass().getEnclosingMethod().getName());
        ListNode [] l = new ListNode[2];
        int ctr = 0;
        {
            ListNode n = new ListNode(1);
            ListNode h = n;
            n.next = new ListNode(1);
            n = n.next;
            n.next = new ListNode(2);
            l[ctr++] = h;
        }
        {
            ListNode n = new ListNode(1);
            ListNode h = n;
            n.next = new ListNode(2);
            n = n.next;
            n.next = new ListNode(2);
            l[ctr++] = h;
        }
        ListNode h = test.mergeKSortedLists(l);
        assertNotNull(h);
        assertEquals(1, h.val);
    }
    public void testFindAllAnagramsInString() {
        p("run %s\n", new Object() {}.getClass().getEnclosingMethod().getName());
        {
            String s = "cbaebabacd";
            String p = "abc";
            List<Integer> l = test.findAllAnagramsInString(s, p);
            assertEquals(2, l.size());
        }
        {
            String s = "aaaaaaabaaaaaaa";
            String p = "aaaaaaa";
            List<Integer> l = test.findAllAnagramsInString(s, p);
            assertEquals(2, l.size());
        }
    }
    public void testPathSumSubarray() {
        p("run %s\n", new Object() {}.getClass().getEnclosingMethod().getName());
        {
            int [] a = {1,2,3,2,3,1,1,4,2,3,4,1,2,2,1};
            //              1 2 3   4 5   6   7   8 9
            int sum = 5;
            int result = test.pathSumSubarray(a, sum);
            assertEquals(9, result);
        }
        {
            int [] a = {1,2,3,-1,-2,3,5,2,-5,-1,6,-1,2,3};
            //              1  2    3 4    5    6  7   8
            int sum = 5;
            int result = test.pathSumSubarray(a, sum);
            assertEquals(8, result);
        }
    }
    public void testSearch2DMatrix() {
        p("run %s\n", new Object() {}.getClass().getEnclosingMethod().getName());
        {
            int [][] a = {
                       {1,   4,  7, 11, 15},
                       {2,   5,  8, 12, 19},
                       {3,   6,  9, 16, 22},
                       {10, 13, 14, 17, 24},
                       {18, 21, 23, 26, 30}
            };
            boolean result = test.search2DMatrix(a, 5);
            assertEquals(true, result);
            result = test.search2DMatrix(a, 16);
            assertEquals(true, result);
        }
    }
    public void testPartialEqualSubsetSum() {
        p("run %s\n", new Object() {}.getClass().getEnclosingMethod().getName());
        {
            int [] a = {1,5,11,5};
            boolean res = test.partialEqualSubsetSum(a);
            assertEquals(true, res);
        }
        {
            int [] a = {1,5,3,4,2,2,5};
            boolean res = test.partialEqualSubsetSum(a);
            assertEquals(true, res);
        }
        {
            int [] a = {1,5,3,4,6,5};
            boolean res = test.partialEqualSubsetSum(a);
            assertEquals(true, res);
        }
        {
            int [] a = {1,5,5,4,6,5};
            boolean res = test.partialEqualSubsetSum(a);
            assertEquals(false, res);
        }
    }
    public void testMinMax() {
        p("run %s\n", new Object() {}.getClass().getEnclosingMethod().getName());
        int min = Integer.MIN_VALUE;
        int max = Integer.MAX_VALUE;
        int min1 = -2147483648;
        int max1 = 2147483647;
        p("min  %d 0x%x\n", min, min);
        p("max  %d 0x%x\n", max, max);
        p("min1 %d 0x%x\n", min1, min1);
        p("max1 %d 0x%x\n", max1, max1);
    }
    public void testFlattenBinaryTreeLinkedList() {
        p("run %s\n", new Object() {}.getClass().getEnclosingMethod().getName());
        int type = 5;
        {
            TreeNode r = new TreeNode(1);
            r.left = new TreeNode(2);
            p("expecting 1 2\n");
            test.flattenBinaryTreeLinkedList(r,type);
        }
        {
            TreeNode r = new TreeNode(1);
            r.left = new TreeNode(2);
            r.left.left = new TreeNode(3);
            r.left.right = new TreeNode(4);
            r.right = new TreeNode(5);
            r.right.right = new TreeNode(6);
            p("expecting 1 2 3 4 5 6\n");
            test.flattenBinaryTreeLinkedList(r,type);
        }
        {
            TreeNode r = new TreeNode(1);
            r.left = new TreeNode(2);
            r.left.right = new TreeNode(3);
            r.right = new TreeNode(4);
            r.right.left = new TreeNode(5);
            r.right.right = new TreeNode(6);
            p("expecting 1 2 3 4 5 6\n");
            test.flattenBinaryTreeLinkedList(r,type);
        }
        {
            TreeNode r = new TreeNode(1);
            r.left = new TreeNode(2);
            r.right = new TreeNode(4);
            r.left.left = new TreeNode(3);
            r.left.left.left = new TreeNode(5);
            p("expecting 1 2 3 5 4\n");
            test.flattenBinaryTreeLinkedList(r,type);
        }
        {
            TreeNode r = new TreeNode(15);
            r.left = new TreeNode(25);
            r.left.left = new TreeNode(35);
            r.left.right = new TreeNode(50);
            r.right = new TreeNode(65);
            r.right.left = new TreeNode(75);
            r.right.right = new TreeNode(85);
            p("expecting 15 25 35 50 65 75 85\n");
            test.flattenBinaryTreeLinkedList(r, type);
        }
    }
    public void testTraverseBSTIO1() {
        p("run %s\n", new Object() {}.getClass().getEnclosingMethod().getName());
        {
            TreeNode r = new TreeNode(50);
            r.left = new TreeNode(25);
            r.left.left = new TreeNode(15);
            r.left.right = new TreeNode(35);
            r.right = new TreeNode(75);
            r.right.left = new TreeNode(65);
            r.right.right = new TreeNode(85);
            test.traverseBST(r, 8);
            p("\nexpected\n15 25 35 50 65 75 85\n");
            p("---------\n");
        }
        {
            TreeNode r = new TreeNode(50);
            r.right = new TreeNode(75);
            r.right.left = new TreeNode(65);
            r.right.right = new TreeNode(85);
            test.traverseBST(r, 8);
            p("\nexpected\n50 65 75 85\n");
            p("---------\n");
        }
        {
            TreeNode r = new TreeNode(50);
            r.left = new TreeNode(25);
            r.left.left = new TreeNode(15);
            r.left.right = new TreeNode(35);
            r.right = new TreeNode(75);
            r.right.left = new TreeNode(65);
            test.traverseBST(r, 8);
            p("\nexpected\n15 25 35 50 65 75\n");
            p("---------\n");
        }
        {
            TreeNode r = new TreeNode(50);
            r.left = new TreeNode(25);
            r.left.left = new TreeNode(15);
            r.left.right = new TreeNode(35);
            test.traverseBST(r, 8);
            p("\nexpected\n15 25 35 50\n");
            p("---------\n");
        }
        {
            TreeNode r = new TreeNode(50);
            r.left = new TreeNode(25);
            r.left.right = new TreeNode(35);
            test.traverseBST(r, 8);
            p("\nexpected\n25 35 50\n");
            p("---------\n");
        }
    }
    public void testTraverseBSTIO2() {
        p("run %s\n", new Object() {}.getClass().getEnclosingMethod().getName());
        {
            TreeNode r = new TreeNode(50);
            r.left = new TreeNode(25);
            r.left.left = new TreeNode(15);
            r.left.right = new TreeNode(35);
            r.right = new TreeNode(75);
            r.right.left = new TreeNode(65);
            r.right.right = new TreeNode(85);
            test.traverseBST(r, 9);
            p("\nexpected\n15 25 35 50 65 75 85\n");
            p("---------\n");
        }
        {
            TreeNode r = new TreeNode(50);
            r.right = new TreeNode(75);
            r.right.left = new TreeNode(65);
            r.right.right = new TreeNode(85);
            test.traverseBST(r, 9);
            p("\nexpected\n50 65 75 85\n");
            p("---------\n");
        }
        {
            TreeNode r = new TreeNode(50);
            r.left = new TreeNode(25);
            r.left.left = new TreeNode(15);
            r.left.right = new TreeNode(35);
            r.right = new TreeNode(75);
            r.right.left = new TreeNode(65);
            test.traverseBST(r, 9);
            p("\nexpected\n15 25 35 50 65 75\n");
            p("---------\n");
        }
        {
            TreeNode r = new TreeNode(50);
            r.left = new TreeNode(25);
            r.left.left = new TreeNode(15);
            r.left.right = new TreeNode(35);
            test.traverseBST(r, 9);
            p("\nexpected\n15 25 35 50\n");
            p("---------\n");
        }
        {
            TreeNode r = new TreeNode(50);
            r.left = new TreeNode(25);
            r.left.right = new TreeNode(35);
            test.traverseBST(r, 9);
            p("\nexpected\n25 35 50\n");
            p("---------\n");
        }
    }
    public void badtestTraverseBST() {
        p("run %s\n", new Object() {}.getClass().getEnclosingMethod().getName());
        int type = 3;
        {
            TreeNode r = new TreeNode(15);
            r.left = new TreeNode(25);
            r.left.left = new TreeNode(35);
            r.left.right = new TreeNode(50);
            r.right = new TreeNode(65);
            r.right.left = new TreeNode(75);
            r.right.right = new TreeNode(85);
        }
        {
            TreeNode r = new TreeNode(1);
            r.left = new TreeNode(2);
            r.left.right = new TreeNode(3);
            r.right = new TreeNode(4);
            r.right.left = new TreeNode(5);
            r.right.right = new TreeNode(6);
        }
        /*
         *
         *                      15
         *              25                60
         *          30      45        65      80
         *
         */
        {
            TreeNode r = new TreeNode(15);
            r.left = new TreeNode(25);
            r.left.left = new TreeNode(30);
            r.left.right = new TreeNode(45);
            r.right = new TreeNode(60);
            r.right.left = new TreeNode(65);
            r.right.right = new TreeNode(80);
            test.traverseBST(r, 3);
            p("\n");
            test.traverseBST(r, 4);
            p("\n");
            test.traverseBST(r, 5);
            p("\n");
        }
        /*
         *                      15
         *              25                60
         *          30      45        65      80
         *        35  40  50  55    70  75  85  90
         *
         */
        {
            TreeNode r = new TreeNode(15);
            r.left = new TreeNode(25);
            r.left.left = new TreeNode(30);
            r.left.left.left = new TreeNode(35);
            r.left.left.right = new TreeNode(40);
            r.left.right = new TreeNode(45);
            r.left.right.left = new TreeNode(50);
            r.left.right.right = new TreeNode(55);
            r.right = new TreeNode(60);
            r.right.left = new TreeNode(65);
            r.right.left.left = new TreeNode(70);
            r.right.left.right = new TreeNode(75);
            r.right.right = new TreeNode(80);
            r.right.right.left = new TreeNode(85);
            r.right.right.right = new TreeNode(90);
            test.traverseBST(r, type);
            p("\n");

        }
        {

        }
        {

        }
    }
    public void testColumnToInt() {
        p("run %s\n", new Object() {}.getClass().getEnclosingMethod().getName());
        int r;
        {
            r = test.columnToInt("a");
            assertEquals(1, r);
            r = test.columnToInt("z");
            assertEquals(26, r);
            r = test.columnToInt("aa");
            assertEquals(27, r);
            r = test.columnToInt("az");
            assertEquals(52, r);
            r = test.columnToInt("ba");
            assertEquals(53, r);
            r = test.columnToInt("zz");
            assertEquals(702, r);
            r = test.columnToInt("aaa");
            assertEquals(703, r);
            r = test.columnToInt("cba");    // 1 + 26*2 + 26*26*3 = 4109
            assertEquals(2081, r);
        }
    }
    public void testGenerateGraphs() {
        p("run %s\n", new Object() {}.getClass().getEnclosingMethod().getName());
        {
            List<GNode> l = test.generateGraph(true, false, 3, 10, 3);
            test.printGraph(l);
        }
        p("------------------\n");
        {
            List<GNode> l = test.generateGraph(true, false, 3, 10, 2);
            test.printGraph(l);
        }
        p("------------------\n");
        {
            List<GNode> l = test.generateGraph(true, true, 3, 10, 3);
            test.printGraph(l);
        }
        p("------------------\n");
        {
            List<GNode> l = test.generateGraph(true, true, 3, 10, 2);
            test.printGraph(l);
        }
        p("------------------\n");
    }

    /**
     * implement basic calculator to evaluate simple string.
     * ( ) + - non negative ints and empty spaces
     * assume given expression always valid.
     */
    @Test
    public void testBasicCalculator() {

    }


    @Test
    public void testIntegerToEnglish() {
        IntegerToEnglish i2e = new IntegerToEnglish();
        for(int i = 0; i < 100; i++) {
            String r = i2e.i2ev2(i);
            p("%s\n", r);
        }
        p("%s\n", i2e.i2ev2(800));
        p("%s\n", i2e.i2ev2(902));
        p("%s\n", i2e.i2ev2(999));
        p("%s\n", i2e.i2ev2(1010));
        p("%s\n", i2e.i2ev2(8201));
        p("%s\n", i2e.i2ev2(82010));
        p("%s\n", i2e.i2ev2(820888));
        p("%s\n", i2e.i2ev2(3820888));
        p("%s\n", i2e.i2ev2(888888888));
        p("%s\n", i2e.i2ev2(1888888888));
        p("pass\n");
    }

    @Test
    public void testMedianFromDataStream() {

    }


    @Test
    public void testIntersectionTwoSortedArrays() {
        int l1 [] = { 1, 3, 7, 9,10,14,18,19};
        int l2 [] = { 0, 2, 3, 7,10,11,12,14,16,19,22};
        List<Integer> result = new ArrayList<>();
        for(int i = 0, j = 0; i < l1.length && j < l2.length;) {
            if     (l1[i] < l2[j]) { i++; }
            else if(l1[i] > l2[j]) { j++; }
            else                   { result.add(l1[i]); i++; j++; }
        }
        StringBuilder sb = new StringBuilder();
        result.stream().forEach(x -> sb.append(String.format("%d ", x)));
        p("%s\n", sb.toString());
    }

    @Test
    public void testScheduleSlabs() {
        // N items in X interval.
        // if N >= X then all good. but if N < X then create slabs.
        // once all items done, then truncate tail ptr.
        {
            //int [] v = {1,1,1,2,2,2,2};
            //String r = Schedule.scheduleSlabs(v,3);
            //p(r + "\n");
        }
        {
            p("1 apart: %s\n",Schedule.scheduleSlabs("1112222",1) );
            p("2 apart: %s\n",Schedule.scheduleSlabs("1112222",2));
            p("2 apart: %s\n",Schedule.scheduleSlabs("1212122",2));
            p("3 apart: %s\n",Schedule.scheduleSlabs("1112222",3));
            p("3 apart: %s\n",Schedule.scheduleSlabs("1212122",3));
            p("2 apart: %s\n",Schedule.scheduleSlabs("11122222",2));
            p("3 apart: %s\n",Schedule.scheduleSlabs("11122222",3));
            p("3 apart: %s\n",Schedule.scheduleSlabs("111222222",3));
            p("4 apart: %s\n",Schedule.scheduleSlabs("111222222",4));
            p("3 apart: %s\n",Schedule.scheduleSlabs("11122222233444",3));
            p("2 apart: %s\n",Schedule.scheduleSlabs("11122222233444",2));
            p("3 apart: %s\n",Schedule.scheduleSlabs("1112222223344455555",3));
            p("2 apart: %s\n",Schedule.scheduleSlabs("1112222223344455555",2));
            p("1 apart: %s\n",Schedule.scheduleSlabs("11122222223344455555",1));
        }
    }
    @Test
    public void testMedianTwoSortedArrays() {
    }

    @Test
    public void testInterleavingString() {
        long t1, t2;
        InterleaveStrings test = new InterleaveStrings();
        for(int i = 0; i < 10; i++) {
            String s1 = TestUtils.generateRandomString("abcd",200);
            String s2 = TestUtils.generateRandomString("abcc",200);
            String s3 = TestUtils.generateInterleavedString(s1, s2);
            p("s1:%s\n",s1);
            p("s2:%s\n",s2);
            p("s3:%s\n",s3);
            t1 = System.currentTimeMillis();
            assertTrue( test.isInterleavedString(s1, s2, s3, 1));
            t2 = System.currentTimeMillis() - t1;
            p("v1 time completion: %d\n", t2);

            t1 = System.currentTimeMillis();
            assertTrue( test.isInterleavedString(s1, s2, s3, 2));
            t2 = System.currentTimeMillis() - t1;
            p("v2 time completion: %d\n", t2);

            t1 = System.currentTimeMillis();
            assertTrue( test.isInterleavedString(s1, s2, s3, 3));
            t2 = System.currentTimeMillis() - t1;
            p("v3 time completion: %d\n", t2);
        }
    }
    @Test
    public void testInterleavedFuzziness() {
        // given String s1, s2, is it a subset interleave of String s3?
        // s3 may have more characters than s1 and s2,
        // but s1 and s2 must be interleaved.
    }
    @Test
    public void testEditDistance() {
        String s1,s2;
        int res;
        AtomicInteger ctr = new AtomicInteger();
        EditDistance editDistance = new EditDistance();

        int version = 1;
        if(version == 0) {
            s1 = "abcde";
            s2 = "bcdef";

            res = editDistance.editDistance(s1,s2,ctr,1);
            assert res == 2;
            int ctr1 = ctr.get();

            res = editDistance.editDistance(s1,s2,ctr,2);
            assert res == 2;
            int ctr2 = ctr.get();

            p("editDistance ctr1:%d ctr2:%d diff:%d\n",ctr1,ctr2,Math.abs(ctr1-ctr2));
        }
        if(version == 1) {
            List<TestUtils.ERRTYPE> errTypes =
                Arrays.asList(TestUtils.ERRTYPE.ADD_CHAR,TestUtils.ERRTYPE.DEL_CHAR,TestUtils.ERRTYPE.MOD_CHAR);
            int sizeStr = 8;
            for(int expErr = 0; expErr < sizeStr; expErr++) {
                for(int j = 0; j < errTypes.size(); j++) {
                    for(int k = 0; k < 1000; k++) {
                        s1 = TestUtils.generateRandomString("abcd",sizeStr);
                        s2 = TestUtils.getCharError("abcd",s1,expErr, TestUtils.ERRTYPE.ADD_CHAR);
                        res = editDistance.editDistance(s1,s2,ctr,1);
                        if(res != expErr) {
                            p("ERR: s1:%s, s2:%s, res:%d version:1\n",s1,s2,res);
                            assert res == expErr;
                        }
                        int ctr1 = ctr.get();

                        res = editDistance.editDistance(s1,s2,ctr,2);
                        if(res != expErr) {
                            p("ERR: s1:%s, s2:%s, res:%d version:2\n",s1,s2,res);
                            assert res == expErr;
                        }
                        int ctr2 = ctr.get();

                        int diff = Math.abs(ctr1-ctr2);
                        if(diff > 100) {
                            p("editDistance ctr1:%4d ctr2:%4d diff:%d\n",ctr1,ctr2,Math.abs(ctr1-ctr2));
                        }
                    }
                }
            }
        }
    }
    @Test
    public void testMinSubsetUnordered() {
        MiscProblems t = new MiscProblems();
        {
            Set<Integer> set = new HashSet<>(Arrays.asList(2,4,6));
            List<Integer> sequence = Arrays.asList(1,2,3,4,5,6,7,1,2,3,4,6,6,4);
            //                                       x   x   x     x   x x x x
            //                                     0   2   4   6   8   A   C
            Pair<Integer,Integer> pair = t.getMinSubsetUnordered(set, sequence);
            assert pair.v1 == 8 && pair.v2 == 11;
        }
    }
    @Test
    public void testBinarySearch() {
        MiscProblems t = new MiscProblems();
        {
            List<List<Integer>> list = new ArrayList<>();
            //                     0 1 2 3 4 5  6  7  8  9  10
            list.add(Arrays.asList(0,2,4,6,8,10,12,14,16,18,20));
            //                     0 1 2 3 4 5  6  7  8  9  10 11
            list.add(Arrays.asList(0,2,4,6,8,10,12,14,16,18,20,22));
            Integer res;
            for(List<Integer> li: list) {
                for(int i = 0; i < li.size(); i++) {
                    res = t.binarySearchRecursive(li, li.get(i));
                    assert res == i;
                    res = t.binarySearchRecursive(li, li.get(i)+1);
                    assert res == null;
                    res = t.binarySearchIterative(li, li.get(i));
                    assert res == i;
                    res = t.binarySearchIterative(li, li.get(i)+1);
                    assert res == null;
                }
            }
        }
        p("pass testBinarySearch\n");
    }
    @Test
    public void testMergeKSortedLists() {
        MiscProblems t = new MiscProblems();
        {
            List<List<Integer>> ll = new ArrayList<>();
            List<Integer> exp = new ArrayList<>();
            for(int i = 0; i < 5; i++) {
                List<Integer> l = new ArrayList<>();
                ll.add(l);
                for(int j = 0; j < 5; j++) {
                    int v = i*10+j;
                    l.add(v);
                    exp.add(v);
                }
            }
            List<Integer> res = t.mergeKSortedLinkedLists(ll);
            assert exp.equals(res);
        }
    }
    @Test
    public void testGenerateStringWithPadding() {
        String s1 = "abccdd";
        String padding = "0123456789";
        String res;
        Set<Character> set = new HashSet<>();
        for(int i = 0; i < s1.length(); i++) set.add(s1.charAt(i));
        for(int i = s1.length(); i < 100; i++) {
            res = TestUtils.generateStringWithPadding(s1,padding, i);
            assert res.length() == i;
            int ctr = 0;
            for(int j = 0; j < res.length(); j++) {
                char c = res.charAt(j);
                if(set.contains(c)) {
                    assert ctr < s1.length() && s1.charAt(ctr) == c;
                    ctr++;
                }
            }
            assert ctr == s1.length();
        }
    }
    @Test
    public void testMinSubsetOrdered() {
        MiscProblems t = new MiscProblems();
        {
            //                                     0 0 0 0 0 0 0 0 0 0 1 1 1 1 1 1 1 1 1
            //                                     0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 9
            //                                     | |   | |     | | | |   | | | |     |
            List<Integer> sequence = Arrays.asList(2,4,4,2,4,4,4,6,2,2,4,4,6,2,2,4,4,4,6);
            List<Integer> minList = Arrays.asList(2,4,6);
            Pair<Integer,Integer> pair = t.getMinSubsetOrdered(minList, sequence);
            assert pair != null && pair.v1 == 9 && pair.v2 == 12;
        }
        {
            //                                     0 0 0 0 0 0 0 0 0 0 1 1 1 1 1 1 1 1 1 2 2 2 2
            //                                     0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 9 0 1 2 3
            //                                     | | |   | |     | | | |   | | | |
            List<Integer> sequence = Arrays.asList(2,2,4,4,2,4,4,4,6,2,2,4,4,6,2,4,6,2,2,4,4,4,6);
            List<Integer> minList = Arrays.asList(2,4,6);
            Pair<Integer,Integer> pair = t.getMinSubsetOrdered(minList, sequence);
            assert pair != null && pair.v1 == 14 && pair.v2 == 16;
        }
    }
    @Test
    public void testDictToJson() {
        {
            // {"m1":{"m1.m1":{"m1.m1.s2":"v1.1.2","m1.m1.s1":"v1.1.1"},"m1.s1":"v1.1"},"s1":"v1"}
            Map<String,Object> map = new HashMap<>();
            Map<String,Object> m;
            map.put("s1","v1");
            map.put("m1",new HashMap<>());
            m = (Map)map.get("m1");
            m.put("m1.s1","v1.1");
            m.put("m1.m1",new HashMap<>());
            m = (Map)map.get("m1");
            m = (Map)m.get("m1.m1");
            m.put("m1.m1.s1","v1.1.1");
            m.put("m1.m1.s2","v1.1.2");
            String result = algosMisc.dictToJsonStringKVOnly(map);
            p("%s\n",result);
        }
        {
            // {"m1":{"m1.l1":["m1.l1.s1","m1.l1.s2"],"m1.m1":{"m1.m1.s2":"v1.1.2","m1.m1.s1":"v1.1.1"},"m1.s1":"v1.1"},"s1":"v1"}
            /*
             * {
             *   "m1": {
             *     "m1.l1": [
             *       "m1.l1.s1",
             *       "m1.l1.s2"
             *     ],
             *     "m1.m1": {
             *       "m1.m1.s2": "v1.1.2",
             *       "m1.m1.s1": "v1.1.1"
             *     },
             *     "m1.s1": "v1.1"
             *   },
             *   "s1": "v1"
             * }
             */
            Map<String,Object> map = new HashMap<>();
            Map<String,Object> m;
            map.put("s1","v1");
            map.put("m1",new HashMap<>());
            m = (Map)map.get("m1");
            m.put("m1.s1","v1.1");
            m.put("m1.m1",new HashMap<>());
            m.put("m1.l1",Arrays.asList("m1.l1.s1","m1.l1.s2","m1.l1.s3"));
            m = (Map)map.get("m1");
            m = (Map)m.get("m1.m1");
            m.put("m1.m1.s1","v1.1.1");
            m.put("m1.m1.s2","v1.1.2");
            String result = algosMisc.dictToJsonString(map);
            p("%s\n",result);
        }
    }
    @Test
    public void testBinarySearch2DArray() {
        {
            //List<String> list = Stream.of(1,2,3,4,5,6,7,8,9,10).map(x -> String.format("%s", x)).collect(Collectors.toList()); // 10 < 9 for string
            List<String> list = Stream.of(1,2,3,4,5,6,7,8).map(x -> String.format("%s", x)).collect(Collectors.toList());
            int result;
            for(int i = 0; i <= 9; i++) {
                result = algosMisc.binarySearch(list, String.format("%d",i));
                if(i == 0) {
                    assert result == -1;
                } else if(i >= 9) {
                    assert result == -1;
                } else {
                    assert result == (i-1);
                }
            }
        }
        {
            List<String> list = Stream.of(1,2,3,4,5,6,7,8,9).map(x -> String.format("%s", x)).collect(Collectors.toList());
            int result;
            for(int i = 0; i <= 10; i++) {
                result = algosMisc.binarySearch(list, String.format("%d",i));
                if(i == 0) {
                    assert result == -1;
                } else if(i >= 10) {
                    assert result == -1;
                } else {
                    assert result == (i-1);
                }
            }
        }
        {
            List<List<String>> ll = new ArrayList<>();
            for(int i = 0; i < 10; i++) {
                List<String> l = new ArrayList<>();
                ll.add(l);
                for(int j = 0; j < 10; j++) {
                    l.add(String.format("(%d,%d)",i,j));
                }
            }
            for(int i = 0; i < 10; i++) {
                for(int j = 0; j < 10; j++) {
                    Pair<Integer,Integer> pair = algosMisc.binarySearch2DArray(ll, String.format("(%d,%d)",i,j));
                    assert pair.v1 == i && pair.v2 == j;
                }
            }
        }
    }
    @Test
    public void testStringCompare() {
        /*
         * 5.compareto(6) == -1
         * 5.compareto(5) == 0
         * 5.compareto(4) == 1
         */
        String s1 = "v1";
        String s2 = "v2";
        String s3 = "v1";
        int result;
        result = s1.compareTo(s2);  // v1 < v2 == -1
        assert result == -1;
        result = s2.compareTo(s1);  // v2 < v1 == 1
        assert result == 1;
        result = s3.compareTo(s1);  // v1 < v1 == 0
        assert result == 0;
    }
}

class AlgosMisc {
    /*
     * in 2d array, all values are sorted. return the x,y index.
     */
    Pair<Integer,Integer> binarySearch2DArray(List<List<String>> llist, String k) {
        int max = llist.size() - 1;
        return binarySearch2DArray(llist, k, 0, max);
    }
    private Pair<Integer,Integer> binarySearch2DArray(List<List<String>> llist, String k, int l, int r) {
        // 5.compareto(6) == -1   5.compareto(5) == 0    5.compareto(4) == 1
        if(r < l) return null;
        int m = (l + r + 1)/2;
        List<String> list  = llist.get(m);
        int cmp = k.compareTo(list.get(0));
        if(cmp < 0){
            return binarySearch2DArray(llist, k, l, m-1);
        }
        List<String> list2 = ((m+1) < llist.size()) ? llist.get(m+1) : null;
        int cmp2 = list2 == null ? -1 : k.compareTo(list2.get(0));
        if(cmp2 < 0) {
            int y = binarySearch(list, k, 0, list.size()-1);
            return (y < 0) ? null : new Pair<Integer,Integer>(m,y);
        }
        return binarySearch2DArray(llist, k, m+1, r);
    }
    int binarySearch(List<String> list, String k) {
        int max = list.size() - 1;
        return binarySearch(list, k, 0, max);
    }
    private int binarySearch(List<String> list, String k, int l, int r) {
        // 5.compareto(6) == -1   5.compareto(5) == 0    5.compareto(4) == 1
        if(r < l) return -1;
        int m = (l + r + 1)/2;
        int cmp = k.compareTo(list.get(m));
        if(cmp == 0)        return m;
        else if(cmp < 0)    return binarySearch(list, k, l, m-1);
        else                return binarySearch(list, k, m+1, r);
    }
    String dictToJsonStringKVOnly(Map<String,Object> map) {
        StringBuilder sb = new StringBuilder();
        dictToJsonStringKVOnly(map, sb);
        return sb.toString();
    }
    void dictToJsonStringKVOnly(Map<String,Object> map, StringBuilder sb) {
        if(map == null || map.isEmpty()) return;
        sb.append("{");
        int ctr = 0;
        for(Map.Entry<String,Object> e: map.entrySet()) {
            if(ctr != 0) sb.append(",");
            String k = e.getKey();
            Object v = e.getValue();
            sb.append(String.format("\"%s\":", k));
            if(v instanceof Map) {
                dictToJsonStringKVOnly((Map<String,Object>)v, sb);
            } else {
                sb.append(String.format("\"%s\"", (String)v));
            }
            ctr++;
        }
        sb.append("}");
    }
    String dictToJsonString(Map<String,Object> map) {
        StringBuilder sb = new StringBuilder();
        dictToJsonString(map, sb);
        return sb.toString();
    }
    private void dictToJsonString(Map<String,Object> map, StringBuilder sb) {
        if(map == null || map.isEmpty()) return;
        sb.append("{");
        int ctr = 0;
        for(Map.Entry<String,Object> e: map.entrySet()) {
            if(ctr != 0) sb.append(",");
            String k = e.getKey();
            Object v = e.getValue();
            sb.append(String.format("\"%s\":", k));
            if(v instanceof Map) {
                dictToJsonString((Map)v, sb);
            } else if(v instanceof List) {
                dictToJsonStringProcessList((List)v, sb);
            } else {
                sb.append(String.format("\"%s\"", (String)v));
            }
            ctr++;
        }
        sb.append("}");
    }
    private void dictToJsonStringProcessList(List<Object> l, StringBuilder sb) {
        int ctr = 0;
        sb.append("[");
        for(Object o: l) {
            if(ctr != 0) sb.append(",");
            if(o instanceof String) {
                sb.append(String.format("\"%s\"", (String)o));
            } else if(o instanceof Map) {
                Map<String,Object> m = (Map)o;
                dictToJsonString(m, sb);
            } else if(o instanceof List) {
                dictToJsonStringProcessList((List)o, sb);
            }
            ctr++;
        }
        sb.append("]");
    }
}

class InterleaveStrings {
    boolean isInterleavedString(String s1, String s2, String s3, int version) {
        int l1 = s1 == null ? 0 : s1.length();
        int l2 = s2 == null ? 0 : s2.length();
        int l3 = s3 == null ? 0 : s3.length();
        if((l1+l2) != l3) return false;
        Map<String, Boolean> map = new HashMap<>();
        if(version == 1) return testInterleavingStringv1(0, 0, 0, s1, s2, s3, map);
        if(version == 2) return testInterleavingStringv2(0, 0, 0, s1, s2, s3, map);
        if(version == 3) return testInterleavingStringv3(0, 0, 0, s1, s2, s3, map);
        return testInterleavingStringv1(0, 0, 0, s1, s2, s3, map);
    }
    boolean testInterleavingStringv1(
            int i1, int i2, int i3,
            String s1, String s2, String s3,
            Map<String,Boolean> map) {
        if(i1 == s1.length() && i2 == s2.length() && i3 == s3.length()) return true;
        if(i1 == s1.length()) return s2.substring(i2).equals(s3.substring(i3));
        if(i2 == s2.length()) return s1.substring(i1).equals(s3.substring(i3));
        String k = String.format("%d,%d,%d",i1,i2,i3);
        if(map.containsKey(k)) return map.get(k);
        if(s1.charAt(i1) == s3.charAt(i3)){
            if(testInterleavingStringv1(i1+1,i2,i3+1,s1,s2,s3,map)) return true;
        }
        if(s2.charAt(i2) == s3.charAt(i3)){
            if(testInterleavingStringv1(i1,i2+1,i3+1,s1,s2,s3,map)) return true;
        }
        map.put(k,false);
        return false;
    }
    boolean testInterleavingStringv2(
            int i1, int i2, int i3,
            String s1, String s2, String s3,
            Map<String,Boolean> map) {
        if(i1 == s1.length() && i2 == s2.length() && i3 == s3.length()) return true;
        if(i1 == s1.length()) return s2.substring(i2).equals(s3.substring(i3));
        if(i2 == s2.length()) return s1.substring(i1).equals(s3.substring(i3));
        String k = String.format("%d,%d,%d",i1,i2,i3);
        if(map.containsKey(k)) return map.get(k);
        if((s1.charAt(i1) == s3.charAt(i3)) && (s2.charAt(i2) == s3.charAt(i3))) {
            return  testInterleavingStringv2(i1+1,i2,i3+1,s1,s2,s3,map) ||
                    testInterleavingStringv2(i1,i2+1,i3+1,s1,s2,s3,map);
        }
        if(s1.charAt(i1) == s3.charAt(i3)) return testInterleavingStringv2(i1+1,i2,i3+1,s1,s2,s3,map);
        if(s2.charAt(i2) == s3.charAt(i3)) return testInterleavingStringv2(i1,i2+1,i3+1,s1,s2,s3,map);
        map.put(k,false);
        return false;
    }
    boolean testInterleavingStringv3(
            int i1, int i2, int i3,
            String s1, String s2, String s3,
            Map<String,Boolean> map) {
        if(i1 == s1.length() && i2 == s2.length() && i3 == s3.length()) return true;
        if(i1 == s1.length()) return s2.substring(i2).equals(s3.substring(i3));
        if(i2 == s2.length()) return s1.substring(i1).equals(s3.substring(i3));
        if(s1.charAt(i1) == s3.charAt(i3)){
            if(testInterleavingStringv3(i1+1,i2,i3+1,s1,s2,s3,map)) return true;
        }
        if(s2.charAt(i2) == s3.charAt(i3)){
            if(testInterleavingStringv3(i1,i2+1,i3+1,s1,s2,s3,map)) return true;
        }
        return false;
    }
    boolean isInterleavedStringV4(int i, int j, int k, String s1, String s2, String s3, Map<String,Boolean> map, AtomicInteger ctr) {
        if(i == s1.length() && j == s2.length() && k == s3.length()) return true;
        String key = String.format("%d,%d,%d",i,j,k);
        if(map.containsKey(key)) return map.get(key);
        if(i < s1.length() && s1.charAt(i) == s3.charAt(k) && isInterleavedStringV4(i+1,j,k+1,s1,s2,s3,map,ctr)) return true;
        if(j < s2.length() && s2.charAt(j) == s3.charAt(k) && isInterleavedStringV4(i,j+1,k+1,s1,s2,s3,map,ctr)) return true;
        map.put(key,false);
        return false;
    }
    boolean isInterleavedStringWithAddError(int i, int j, int k, String s1, String s2, String s3, int ctrErr, Map<String,Boolean> map, AtomicInteger ctr) {
        ctr.incrementAndGet();
        if(i == s1.length() && j == s2.length() && k == s3.length()) return true;
        if(k == s3.length()) return ctrErr <= 1;
        String key = String.format("%d,%d,%d",i,j,k);
        if(map.containsKey(key)) return map.get(key);
        if(i < s1.length() && s1.charAt(i) == s3.charAt(k) && isInterleavedStringWithAddError(i+1,j,k+1,s1,s2,s3,ctrErr,map,ctr)) return true;
        if(j < s2.length() && s2.charAt(j) == s3.charAt(k) && isInterleavedStringWithAddError(i,j+1,k+1,s1,s2,s3,ctrErr,map,ctr)) return true;
        if(isInterleavedStringWithAddError(i,j,k+1,s1,s2,s3,ctrErr+1,map,ctr)) return true;
        map.put(key,false);
        return false;
    }
    boolean isInterleavedStringWithOneError(int i, int j, int k, String s1, String s2, String s3, int ctrErr, Map<String,Boolean> map, AtomicInteger ctr) {
        ctr.incrementAndGet();
        if(i == s1.length() && j == s2.length() && k == s3.length()) return true;
        if(k == s3.length()) return ctrErr <= 1;
        String key = String.format("%d,%d,%d",i,j,k);
        if(map.containsKey(key)) return map.get(key);
        if(i < s1.length() && s1.charAt(i) == s3.charAt(k) && isInterleavedStringWithOneError(i+1,j,k+1,s1,s2,s3,ctrErr,map,ctr)) return true;
        if(j < s2.length() && s2.charAt(j) == s3.charAt(k) && isInterleavedStringWithOneError(i,j+1,k+1,s1,s2,s3,ctrErr,map,ctr)) return true;
        if(isInterleavedStringWithOneError(i+1,j,k,s1,s2,s3,ctrErr+1,map,ctr)) return true;
        if(isInterleavedStringWithOneError(i,j+1,k,s1,s2,s3,ctrErr+1,map,ctr)) return true;
        if(isInterleavedStringWithOneError(i,j,k+1,s1,s2,s3,ctrErr+1,map,ctr)) return true;
        map.put(key,false);
        return false;
    }
}

class TestUtils {
    public enum ERRTYPE {
        ADD_CHAR,
        DEL_CHAR,
        MOD_CHAR
    }
    static Random r = new Random();
    static void p(String f, Object ...o) {
        System.out.printf(f, o);
    }
    static int maxShuffle = 5;
    /*
     * given s1, interleave it with paddingChars to produce length len string
     *
     * choose len - s1 positions. s1[0] is 0, s1[last] is last.
     * choose s1-2 indexes between 1 and len-1
     */
    static String generateStringWithPadding(String s1, String paddingChars, int len) {
        StringBuilder sb = new StringBuilder();
        List<Integer> indices = new ArrayList<>();
        indices.add(0);
        List<Integer> shuffled = IntStream.range(1,len-1).boxed().collect(Collectors.toList());
        shuffleIntegers(shuffled);
        indices.addAll(shuffled.subList(0,s1.length()-2));
        indices.add(len-1);
        Collections.sort(indices);
        int szPadding = paddingChars.length();
        for(int i = 0, j = 0; i < len; i++) {
            int idx = indices.get(j);
            sb.append((idx == i) ? s1.charAt(j++) : paddingChars.charAt(r.nextInt(szPadding)));
        }
        return sb.toString();
    }
    static String generateInterleavedString(String s1, String s2){
        StringBuilder sb = new StringBuilder();
        for(int i = 0, j = 0; ; ) {
            if(i < s1.length() && j < s2.length()) {
                if(r.nextBoolean()) {
                    sb.append(s1.charAt(i++));
                } else {
                    sb.append(s2.charAt(j++));
                }
            }
            else if(i < s1.length()) {
                sb.append(s1.charAt(i++));
            }
            else if(j < s2.length()) {
                sb.append(s2.charAt(j++));
            } else {
                break;
            }
        }
        return sb.toString();
    }
    static String generateRandomString(String charset, int length) {
        StringBuilder sb = new StringBuilder();
        int max = charset.length();
        for(int i = 0; i < length; i++) {
            sb.append(charset.charAt(r.nextInt(max)));
        }
        return sb.toString();
    }
    /*
     * get random char that is not equal to c
     */
    static char getRandomChar(String charset, char c) {
        for(int i = 0; i < charset.length(); i++) {
            char ret = charset.charAt(r.nextInt(charset.length()));
            if(ret != c) return ret;
            if(charset.charAt(i) != c) return charset.charAt(i);
        }
        return '\0';
    }
    static char getRandomChar(String charset) {
        for(int i = 0; i < charset.length(); i++) {
            char ret = charset.charAt(r.nextInt(charset.length()));
        }
        return '\0';
    }
    static void shuffle(int []a) {
        for(int i = 0; i < maxShuffle; i++) {
            for(int j = 0; j < a.length; j++) {
                int idx = r.nextInt(a.length);
                int v = a[idx];
                a[idx] = a[j];
                a[j] = v;
            }
        }
    }
    static void shuffle(char []a) {
        for(int i = 0; i < maxShuffle; i++) {
            for(int j = 0; j < a.length; j++) {
                int idx = r.nextInt(a.length);
                char v = a[idx];
                a[idx] = a[j];
                a[j] = v;
            }
        }
    }
    static void shuffleIntegers(List<Integer> l) {
        for(int i = 0; i < maxShuffle; i++) {
            for(int j = 0; j < l.size(); j++) {
                int idx = r.nextInt(l.size());
                Integer o = l.get(idx);
                l.set(idx, l.get(j));
                l.set(j, o);
            }
        }
    }
    static void shuffle(List<Object> l) {
        for(int i = 0; i < maxShuffle; i++) {
            for(int j = 0; j < l.size(); j++) {
                int idx = r.nextInt(l.size());
                Object o = l.get(idx);
                l.set(idx, l.get(j));
                l.set(j, o);
            }
        }
    }
    static String getCharError(String charset, String s, int numErrs, ERRTYPE errType) {
        int [] a = new int[s.length()];
        for(int i = 0; i < s.length(); i++) {
            a[i] = i;
        }
        shuffle(a);
        /*
         *   0 1 2 3 4 5  original and index (in this case)
         *   2 3 2 5 4 0  new
         */
        Set<Integer> set = new HashSet<>();
        for(int i = 0; i < numErrs; i++) {
            set.add(a[i]);
        }
        StringBuilder sb = new StringBuilder();
        switch(errType) {
            case ADD_CHAR: {
                for(int i = 0; i < s.length(); i++) {
                    sb.append(s.charAt(i));
                    if(set.contains(i))
                        sb.append(charset.charAt(r.nextInt(charset.length())));
                }
                break;
            }
            case DEL_CHAR: {
                for(int i = 0; i < s.length(); i++) {
                    if(!set.contains(i))
                        sb.append(s.charAt(i));
                }
                break;
            }
            case MOD_CHAR: {
                for(int i = 0; i < s.length(); i++) {
                    sb.append(set.contains(i) ?
                        getRandomChar(charset, s.charAt(i)) :
                        s.charAt(i));
                }
                break;
            }
        }
        return sb.toString();
    }
}

class EditDistance {
    /**
     * measures the distance between two strings. there are several implementations
     */
    int editDistance(String s1, String s2, AtomicInteger ctr, int version) {
        ctr.set(0);
        Map<String,Integer> map = new HashMap<>();
        if(version == 1) return editDistanceV1(s1,s2,0,0,ctr,0);
        if(version == 2) return editDistanceV2(s1,s2,0,0,ctr,0, map);
        return editDistanceV2(s1,s2,0,0,ctr,0, map);
    }
    int editDistanceV1(String s1, String s2, int i, int j, AtomicInteger ctr, int distance) {
        ctr.incrementAndGet();
        if(i == s1.length() && j == s2.length()){
            return distance;
        }
        if(i < s1.length() && j < s2.length() && s1.charAt(i) == s2.charAt(j)) {
            return editDistanceV1(s1,s2,i+1,j+1,ctr,distance);
        }
        int min = Integer.MAX_VALUE;
        if(i < s1.length()){
            min = editDistanceV1(s1,s2,i+1,j,ctr,distance)+1;
        }
        if(j < s2.length()){
            int tmp = editDistanceV1(s1,s2,i,j+1,ctr,distance)+1;
            min = min < tmp ? min : tmp;
        }
        return min;
    }
    int editDistanceV2(String s1, String s2, int i, int j, AtomicInteger ctr, int distance, Map<String,Integer> map) {
        ctr.incrementAndGet();
        if(i == s1.length() && j == s2.length()){
            return distance;
        }
        String key = String.format("%d,%d",i,j);
        if(map.containsKey(key)) return map.get(key);
        if(i < s1.length() && j < s2.length() && s1.charAt(i) == s2.charAt(j)) {
            return editDistanceV2(s1,s2,i+1,j+1,ctr,distance,map);
        }
        int min = Integer.MAX_VALUE;
        if(i < s1.length()){
            min = editDistanceV2(s1,s2,i+1,j,ctr,distance,map)+1;
        }
        if(j < s2.length()){
            int tmp = editDistanceV2(s1,s2,i,j+1,ctr,distance,map)+1;
            min = min < tmp ? min : tmp;
        }
        map.put(key,min);
        return min;
    }
    int editDistanceV3(String s1, String s2, AtomicInteger ctr, Map<String,Integer> map) {
        int min = Integer.MAX_VALUE;
        for(int i = 0; i < s1.length(); i++) {
            for(int j = 0; j < s2.length(); j++) {
                String key = String.format("%d,%d",i,j);
                map.put(key, 0);
            }
        }
        return min;
    }
}

class MiscProblems {
    Random r = new Random();
    AtomicInteger aictr = new AtomicInteger(0);
    /*
     * given list of coordinates representing rectangle, can they all form a perfect
     * rectangle, without overlap?
     */
    public boolean isPerfectRectangle(int [][] rectangles) {
        Integer xMin=null,xMax=null,yMin=null,yMax=null;
        return false;
    }
    /*
     * given list of horizontal lines, meaning x1 and x2, do they all overlap?
     */
    public boolean isContiguousLineOverlap(int [][] lines) {
        for(int i = 0; i < lines.length; i++) {
            int x1 = lines[i][0];
            int x2 = lines[i][1];
        }
        return false;
    }
    /*
     * given list of horizontal lines, do they all form contiguous, without overlap?
     */
    public boolean isContiguousLineNoOverlap(int [][] lines) {
        Map<Integer,Set<Integer>> map = new HashMap<>();
        map.put(0, new HashSet<>());
        map.put(1, new HashSet<>());
        for(int i = 0; i < lines.length; i++) {
            map.get(0).add(lines[i][0]);
            map.get(1).add(lines[i][1]);
        }
        Set<Integer> setToRemove = new HashSet<>();
        for(Integer x2: map.get(1)) {
            if(map.get(0).contains(x2+1)) {
                setToRemove.add(x2);
                map.get(0).remove(x2+1);
            }
        }
        map.get(1).removeAll(setToRemove);
        return (map.get(0).size() == 1 && map.get(1).size() == 1);
    }
    /*
     * given s1 and s2, find minimum window in S that contains all characters in s2 in O(n)
     * return empty string if no substring exists.
     */
    public String minimumWindowSubstring(String s1, String s2) {
        return null;
    }
    public int containerWithMostWater() {
        return 0;
    }
    public List<Integer> mergeKSortedLinkedLists(List<List<Integer>> ll) {
        List<Integer> sorted = new ArrayList<>();
        PriorityQueue<List<Integer>> pq = new PriorityQueue<>((x,y)->x.get(0)-y.get(0));
        AtomicInteger totalSize = new AtomicInteger(0);
        ll.stream().forEach(l -> {
            pq.add(l);
            totalSize.addAndGet(l.size());
        });
        for(int i = 0; i < totalSize.get(); i++) {
            List<Integer> l = pq.poll();
            sorted.add(l.remove(0));
            if(l.size() != 0)
                pq.add(l);
        }
        return sorted;
    }
    /*
     * given a minSet of numbers, find in sequence the smallest subset that has all of minSet, unordered.
     */
    public Pair<Integer,Integer> getMinSubsetUnordered(Set<Integer> minSet, List<Integer> sequence) {
        Map<Integer,Integer> map = new HashMap<>();
        LinkedList<Integer> idx = new LinkedList<>();
        int min=0,max=sequence.size();
        for(int i = 0; i < sequence.size(); i++) {
            Integer v = sequence.get(i);
            if(!minSet.contains(v)) continue;
            map.put(v, (map.get(v) == null) ? 1 : map.get(v) + 1);
            idx.add(i);
            while(map.size() == minSet.size()){
                Integer lastV = sequence.get(idx.peekFirst());
                if(map.get(lastV) == 1) break;
                idx.pollFirst();
                map.put(v, map.get(lastV) - 1);
                int j = idx.peekFirst();
                if((i - j) < (max - min)) {
                    min = j;
                    max = i;
                }
            }
        }
        return new Pair<>(min,max);
    }
    /*
     * given minList of numbers, find in sequence the smallest subset that has all of minSet, ordered minList
     *
     * seems like this is is O(minList.size()*sequence.size())
     *
     * map = map of list of indices in minList
     * mapTrackers =    map of list of list of trackers. key is index of minList.
     *                  each list of tracker is possible shortest subset.
     *                  there may be multiple lists of trackers that are waiting for the next index match.
     *                  after sequence finished iterating, select the smallest sized subset.
     */
    public Pair<Integer,Integer> getMinSubsetOrdered(List<Integer> minList, List<Integer> sequence) {
        Map<Integer,List<Integer>> map = new HashMap<>();
        List<Queue<List<Integer>>> listTrackers = new ArrayList<>();
        for(int i = 0; i < minList.size(); i++) {
            Integer v = minList.get(i);
            if(!map.containsKey(v)) {
                map.put(v, new ArrayList<>(Arrays.asList(i)));
            } else {
                map.get(v).add(i);
            }
            listTrackers.add(new LinkedList<>());
        }
        for(int i = 0; i < sequence.size(); i++) {
            Integer v = sequence.get(i);
            if(!map.containsKey(v)) continue;
            for(Integer idxOfMinList: map.get(v)) {
                if(idxOfMinList == 0) {
                    listTrackers.get(idxOfMinList).add(new LinkedList<>(Arrays.asList(i)));
                } else {
                    Queue<List<Integer>> ll = listTrackers.get(idxOfMinList-1);
                    if(ll.size() == 0) continue;
                    for(List<Integer> l: ll){
                        l.add(i);
                    }
                    listTrackers.get(idxOfMinList).addAll(ll);
                    listTrackers.get(idxOfMinList-1).clear();
                }
            }
        }
        Queue<List<Integer>> pq = new PriorityQueue<>((x,y)->((x.get(x.size()-1)-x.get(0))-(y.get(y.size()-1)-y.get(0))));
        pq.addAll(listTrackers.get(minList.size()-1));
        List<Integer> l = pq.poll();
        Pair<Integer,Integer> pair = (l == null) ? null : new Pair<Integer,Integer>(l.get(0),l.get(l.size()-1));
        return pair;
    }
    void p(String f, Object ...o) {
        System.out.printf(f, o);
    }
    private void p(List<Integer> l) {
        if(CollectionUtils.isEmpty(l)) return;
        StringBuilder sb = new StringBuilder();
        for(Integer i: l) {
            sb.append(String.format("%2d ", i));
        }
        sb.append("\n");
        p(sb.toString());
    }
    public void deleteDuplicatesInSortedArray(List<Integer> li, List<Integer> lo) {
        for(int i = 0; i < li.size(); i++) {
            if(i == 0 || li.get(i-1) != li.get(i))
                lo.add(li.get(i));
        }
    }
    /*
     *  0 1 2 3 4 5
     *  |         |         (0+5)/2=2
     *  |   |               (0+2)/2=1
     *  | |                 (0+1)/2=0
     *    | |               (1+2)/2=1
     */
    public Integer binarySearchIterative(List<Integer> list, int v) {
        if(CollectionUtils.isEmpty(list)) return null;
        int l = 0, r = list.size()-1, m;
        while(l <= r) {
            m = (l+r)/2;
            int cv = list.get(m);
            if      (cv == v) return m;
            else if (cv < v) l = m+1;
            else    r = m-1;
        }
        return null;
    }
    public Integer binarySearchRecursive(List<Integer> l, int v) {
        return (CollectionUtils.isEmpty(l)) ? null : binarySearchRecursive(l, v, 0, l.size()-1);
    }
    private Integer binarySearchRecursive(List<Integer> list, int v, int l, int r) {
        if(l > r || l < 0 || r >= list.size()) return null;
        int m = (l+r)/2;
        int cv = list.get(m);
        if      (cv == v)   return m;
        else if (cv < v)    return binarySearchRecursive(list, v, m+1, r);
        else                return binarySearchRecursive(list, v, l,m-1);
    }
    public List<String> computeLongestCommonSubsequence(List<String> s1, List<String> s2) {
        List<String> result = new ArrayList<>();
        return result;
    }
    /*
     * Compute the min diff.
     */
    public List<DiffEntry> computeDiff(List<String> s1, List<String> s2) {
        List<DiffEntry> result = new ArrayList<>();
        return result;
    }
    /*
     * Compute multiple alternative diffs
     */
    public List<List<DiffEntry>> computeDiffMultiAlternatives(List<String> s1, List<String> s2) {
        List<List<DiffEntry>> result = new ArrayList<>();
        return result;
    }
    public List<Integer> longestCommonSubsequence(String s1, String s2) {
        List<Integer> result = new ArrayList<>();
        return result;
    }
    public List<Integer> longestCommonSequence(String s1, String s2) {
        List<Integer> result = new ArrayList<>();
        return result;
    }
}

@Data
@Builder
class DiffEntry {
    int idxL;
    int idxR;
    String stringL;
    String stringR;
}



