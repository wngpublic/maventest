package org.wayne.misc;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.wayne.misc.SearchSort.NodeRange;
import org.wayne.misc.SearchSort.BinarySearch;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

public class SearchSortTest extends TestCase  {
    
    SearchSort t = new SearchSort();
    Utils u = new Utils();

    void p(String f, Object ...o) {
        System.out.printf(f, o);
    }
    void pl(Object o) {
        System.out.println(o);
    }

    public void test3Sum1() {
        p("run %s\n", new Object() {}.getClass().getEnclosingMethod().getName());
        int [] a = {-1,0,1,2,-1,-4};
        List<List<Integer>> ll = t.threeSumUniqueCombinations(a);
        for(List<Integer> l: ll) {
            System.out.println(l);
        }
    }

    public void test3Sum2() {
        p("run %s\n", new Object() {}.getClass().getEnclosingMethod().getName());
        int [] a = {3,0,3,2,-4,0,-3,2,2,0,-1,-5};
        List<List<Integer>> ll = t.threeSumUniqueCombinations(a);
        for(List<Integer> l: ll) {
            System.out.println(l);
        }
    }
    
    public void testKselect1() {
        p("run %s\n", new Object() {}.getClass().getEnclosingMethod().getName());
        List<Integer> list = null;
        int result;
        int choose = 3;
        
        u.p("---------------------------------------\n");
        list = Arrays.asList(5,3,7,6,2,1,9,8,0,4);
        result = t.kselect(list, choose);
        u.p("result: %2d choose %2d: ",  result, choose);
        u.pl(list);

        u.p("---------------------------------------\n");
        list = Arrays.asList(5,6,7,3,2,1,9,8,0,4);
        result = t.kselect(list, choose);
        u.p("result: %2d choose %2d: ",  result, choose);
        u.pl(list);

        u.p("---------------------------------------\n");
        list = Arrays.asList(5,6,7,2,3,1,9,8,0,4);
        result = t.kselect(list, choose);
        u.p("result: %2d choose %2d: ",  result, choose);
        u.pl(list);

    }
    
    public void testPrintDiffFixed() {
        p("run %s\n", new Object() {}.getClass().getEnclosingMethod().getName());

        int sz = 50;
        int idxs = 30;
        int idxe = 34;
        int szMax = 5;

        char [] a1 = u.getAryChar(sz);
        char [] a2 = Arrays.copyOf(a1, sz);
        char [] a3 = u.getDiffChars(idxs, idxe, a2, a1);
        for(int i = 0, j = idxs; j < idxe; j++, i++) {
            a2[j] = a3[i];
        }

        String s1 = new String(a1);
        String s2 = new String(a2);

        p("%s\n", s1);
        p("%s\n", s2);

        SearchSort.DiffSet diffset = new SearchSort.DiffSet();
        String sret = diffset.binaryDiffContiguousFixedLength(0, sz-1, s1, s2, szMax);
        p("fixed length: %s\n", sret);

    }

    public void testPrintDiffVariable() {
        p("run %s\n", new Object() {}.getClass().getEnclosingMethod().getName());

        int sz = 50;
        int idxs = 30;
        int idxe = 34;

        char [] a1 = u.getAryChar(sz);
        char [] a2 = Arrays.copyOf(a1, sz);
        char [] a3 = u.getDiffChars(idxs, idxe, a2, a1);
        for(int i = 0, j = idxs; j < idxe; j++, i++) {
            a2[j] = a3[i];
        }

        String s1 = new String(a1);
        String s2 = new String(a2);

        p("%s\n", s1);
        p("%s\n", s2);

        SearchSort.DiffSet diffset = new SearchSort.DiffSet();
        String sret = diffset.binaryDiffContiguousVariableLength(0,  sz-1, s1, s2);            
        p("variable length: %s\n", sret);

    }

    public void testPrintDiffSet() {
        p("run %s\n", new Object() {}.getClass().getEnclosingMethod().getName());

        int sz = 50;
        int idxs = 30;
        int idxe = 34;
        int szMax = 5;

        char [] a1 = u.getAryChar(sz);
        char [] a2 = Arrays.copyOf(a1, sz);
        char [] a3 = u.getDiffChars(idxs, idxe, a2, a1);
        for(int i = 0, j = idxs; j < idxe; j++, i++) {
            a2[j] = a3[i];
        }

        String s1 = new String(a1);
        String s2 = new String(a2);

        p("%s\n", s1);
        p("%s\n", s2);

        SearchSort.DiffSet diffset = new SearchSort.DiffSet();
        {
            String sret = diffset.binaryDiffContiguousFixedLength(0, sz-1, s1, s2, szMax);
            p("fixed length: %s\n", sret);
        }
        {
            String sret = diffset.binaryDiffContiguousVariableLength(0,  sz-1, s1, s2);            
            p("variable length: %s\n", sret);
        }

    }
    
    public void testFindIdxBinaryChange() {
        p("run %s\n", new Object() {}.getClass().getEnclosingMethod().getName());
        String s = "aaaaaabbbbbbbbbb";
        SearchSort.DiffSet diffset = new SearchSort.DiffSet();
        int idx = diffset.findFirstIdxBinaryChange(s, 'b', 0, s.length() - 1);
        p("testFindFirstIdxBinaryChange %s = %d\n", s, idx);
        idx = diffset.findLastIdxBinaryChange(s, 'a', 0, s.length() - 1);
        p("testFindLastIdxBinaryChange  %s = %d\n", s, idx);
    }
    
    public void testBinarySearch() {
        p("run %s\n", new Object() {}.getClass().getEnclosingMethod().getName());
        int sz = 20;
        List<Integer> l = new ArrayList<>();
        for(int i = 0; i < sz; i++) {
            l.add(i*5);
        }
        System.out.println(l);
        BinarySearch binsearch = new SearchSort.BinarySearch();
        List<Integer> linput = Arrays.asList(0,7,10,13,100,1000);
        {
            p("binsearchiterative:\n");
            for(int i = 0; i < linput.size(); i++) {
                Integer in = linput.get(i);
                Integer out = binsearch.binarySearchIterative(l, in);
                p("in:%2d outIdx:%2d ", in, out);
                p("outVal:%2d\n", l.get(out));
            }
        }
        {
            p("binsearchrecursive:\n");
            for(int i = 0; i < linput.size(); i++) {
                Integer in = linput.get(i);
                Integer out = binsearch.binarySearchRecursive(l, in);
                p("in:%2d outIdx:%2d ", in, out);
                p("outVal:%2d\n", l.get(out));
            }
        }
    }
    public void testBinarySearchRange() {
        p("run %s\n", new Object() {}.getClass().getEnclosingMethod().getName());
        int sz = 20;
        List<NodeRange> l = new ArrayList<>();
        /*
         * 00:04
         * 05:09
         * 10:14
         * 15:19
         */
        for(int i = 0; i < sz; i++) {
            int max = (i+1)*5-1;
            int min = max - 4;
            NodeRange node = new NodeRange(min, max, null);
            l.add(node);
            node.print();
        }
        BinarySearch binsearch = new BinarySearch();
        List<Integer> linput = Arrays.asList(0,7,10,13,33,35,36,100,1000);
        {
            p("binsearchiterative:\n");
            for(int i = 0; i < linput.size(); i++) {
                Integer in = linput.get(i);
                NodeRange out = binsearch.binarySearchRangeItertive(l, in);
                if(out == null) {
                    p("in:%2d not found\n", in);
                } else {
                    p("in:%2d outmin:%2d outmax:%2d\n", in, out.min(), out.max());
                }
            }
        }
        {
            p("binsearchrecursive:\n");
            for(int i = 0; i < linput.size(); i++) {
                Integer in = linput.get(i);
                NodeRange out = binsearch.binarySearchRangeRecursive(l, in);
                if(out == null) {
                    p("in:%2d not found\n", in);
                } else {
                    p("in:%2d outmin:%2d outmax:%2d\n", in, out.min(), out.max());
                }
            }
        }
    }
    public void testQuicksort() {
        p("run %s\n", new Object() {}.getClass().getEnclosingMethod().getName());
        List<Integer> l = Arrays.asList(1,2,3,4,5,6,7,8);
        u.shuffle(l);
        l = Arrays.asList(5,6,2,7,4,3,1,8);
        SearchSort.SortTypes sort = new SearchSort.SortTypes();
        pl(l);
        sort.quicksort(l);
        pl(l);
    }
    public void testQuicksortRandom() {
        p("run %s\n", new Object() {}.getClass().getEnclosingMethod().getName());
        int numtrials = 5;
        SearchSort.SortTypes sort = new SearchSort.SortTypes();
        for(int i = 0; i < numtrials; i++) {
            int numitems = u.getInt(10, 20);
            List<Integer> l = new ArrayList<>();
            for(int j = 0; j < numitems; j++) {
                l.add(j);
            }
            List<Integer> lcopy = new ArrayList<>(l);
            u.shuffle(lcopy);
            sort.quicksort(lcopy);
            for(int j = 0; j < numitems; j++) {
                int vref = l.get(j);
                int vres = lcopy.get(j);
                if(vref != vres) {
                    pl(l);
                    pl(lcopy);
                    p("mismatch for quicksort!\n");
                    return;
                }
            }
        }
        p("quicksort test passed\n");
    }
    public void badtestQuicksortRandomWithDuplicates() {
        p("run %s\n", new Object() {}.getClass().getEnclosingMethod().getName());
        int numtrials = 5;
        SearchSort.SortTypes sort = new SearchSort.SortTypes();
        for(int i = 0; i < numtrials; i++) {
            int numitems = u.getInt(10, 20);
            List<Integer> l = new ArrayList<>();
            int vprev = 0;
            for(int j = 0; j < numitems; j++) {
                int v = u.getBool() ? vprev : j;
                l.add(v);
                vprev = v;
            }
            List<Integer> lcopy = new ArrayList<>(l);
            u.shuffle(lcopy);
            sort.quicksort(lcopy);
            for(int j = 0; j < numitems; j++) {
                int vref = l.get(j);
                int vres = lcopy.get(j);
                if(vref != vres) {
                    pl(l);
                    pl(lcopy);
                    p("mismatch for quicksort!\n");
                    return;
                }
            }
        }
        p("quicksort test passed\n");
    }
}
