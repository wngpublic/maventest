package org.wayne.misc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.DigestException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

public class SearchSort {

    Utils u = new Utils();

    public SearchSort() {
    }

    public static void p(String f, Object ...o) {
        System.out.printf(f, o);
    }
    
    /**
     * medianOfTwoSortedArrays
     * 
     * m = length of string 1
     * n = length of string 2
     * O(log(m+n))
     * 
     * Example 1:
     * nums1 = [1, 3]
     * nums2 = [2]
     * The median is 2.0
     * 
     * Example 2:
     * nums1 = [1, 2]
     * nums2 = [3, 4]
     * The median is (2 + 3)/2 = 2.5
     * 
     * case 1: s1 > s2 || s1 < s2
     * case 2: s1 == s2
     * case 3: s1 =~ s2
     * 
     * case 1:
     *      s1      2  3  4  5  6
     *      s2      7  8  9 10 11 
     * 
     * case 2:
     *      s1      2  3  4  5  6
     *      s2      2  3  4  5  6
     *      
     * case 3:
     *      s1      2  3  4  5  6
     *      s2      4  6  8 10 12
     *      
     *      brute force is sort and find median
     *      00 01 02 03 04 05 06 07 08 09
     *       2  3  4  5  6  4  6  8 10 12
     *       2  3  4  4  5  6  6  8 10 12
     *      
     *      (idx4 + idx5) / 2 => (5 + 6) / 2 = 5.5
     *      this is O(m+n)
     *      
     *      how to do in O(log(m+n)) ?
     *      s1[(0+4)/2] = s1[2] = 4
     *      s2[(0+4)/2] = s2[2] = 8
     *      
     * case 3:
     *      s1      2  3  4  5  6
     *      s2      1  3  5  7  9
     *      
     *      brute force is sort and find median
     *      00 01 02 03 04 05 06 07 08 09
     *       2  3  4  5  6  1  3  5  7  9
     *       1  2  3  3  4  5  5  6  7  9
     * 
     * case 3:
     *      s1      2  3  4  5  9
     *      s2      5  6  7  8  
     *      
     *      brute force is sort and find median
     *      00 01 02 03 04 05 06 07 08
     *       2  3  4  5  9  5  6  7  8
     *       2  3  4  5  5  6  7  8  9
     *      
     * case 3:
     *      s1      1  2  3  4  9
     *      s2      4  5  6  7  8  
     *      
     *      brute force is sort and find median
     *      00 01 02 03 04 05 06 07 08 09
     *       1  2  3  4  9  4  5  6  7  8
     *       1  2  3  4  4  5  6  7  8  9
     *       
     * case 3:
     *      s1      1  1  1  3  4  5
     *      s2      1  2  2  3  3  4
     *      
     *      brute force is sort and find median
     *      00 01 02 03 04 05 06 07 08 09 10 11
     *       1  1  1  3  4  5  1  2  2  3  3  4
     *       1  1  1  1  2  2  3  3  3  4  4  5
     *       
     *      s1[(0+5)/2] = s1[2] = 1
     *      s2[(0+5)/2] = s2[2] = 2
     *      
     * observation: if you do binary search of first string, 
     * doing median first, then search for the same value or 
     * value right before current value in string 2, then 
     * you know how many elements precede. the goal is to 
     * do binary search until the number of elements that 
     * precede a value == the number of elements that come
     * after a value. 
     *       
     */
    public double medianOfTwoSortedArrays(int [] a1, int [] a2) {
        return 0;
    }
    
    public double medianofTwoArrays(int [] a1, int [] a2) {
        return 0;
    }
    
    public static <E extends Comparable<?>> void sort(List<E> l) {
        
    }
    
    public void sort(List<Integer> l, int sortType) {

        SortTypes internal = new SortTypes();
        int numways = 3;
        int maxidx = 10;
        
        // preprocessing for characteristics
        int min = l.get(0);
        int max = l.get(0);
        int sz = 0;
        {
            sz = l.size();
            for(int i = 0; i < sz; i++) {
                int v = l.get(i);
                min = (min < v) ? min : v;
                max = (max > v) ? max : v;
            }
        }
        
        switch(sortType) {
        case 0: internal.mergesort(l); break;
        case 1: internal.quicksort(l); break;
        case 2: internal.heapsort(l); break;
        case 3: internal.bucketsort(l, min, max); break;
        case 4: internal.radixsort(l); break;
        case 5: internal.partialquicksort(l, maxidx); break;
        case 6: internal.insertionsort(l); break;
        case 7: internal.distributesort(l, numways); break;
        default: internal.quicksort(l); break; 
        }
    }
    
    /**
     * given array of integers, find all unique triplets that give sum of 0.
     * you cannot choose the same number more than once. 
     * 
     *    0 1 2 3 4 5 
     *    0 0 0 0 0 0 
     *  
     *  1 0 1 2
     *  2 0 1   3
     *  3 0 1     4
     *  4 0 1       5
     *  5 0   2 3
     *  6 0   2   4
     *  7 0   2     5
     *  8 0     3 4
     *  9 0     3   5
     * 10 0       4 5
     * 11   1 2 3
     * 12   1 2   4
     * 13   1 2     5
     * 14   1   3 4
     * 15   1   3   5
     * 16     2 3 4
     * 17     2 3   5
     * 18       3 4 5
     */
    public List<List<Integer>> threeSumUniqueCombinations(int [] a) {
        int type = 0;
        if(type == 0) {
            return threeSumUniqueCombinationUnsorted(a);
        }
        else if(type == 1) {
            return threeSumUniqueCombinationsSorted(a);
        }
        else if(type == 2) {
            return threeSumPositionalCombinations(a);
        }
        return null;
    }
    
    private List<List<Integer>> threeSumPositionalCombinations(int [] a) {
        List<List<Integer>> ll = new ArrayList<>();
        Map<Integer, List<Integer>> map = new HashMap<>();
        for(int i = 0; i < a.length; i++) {
            if(!map.containsKey(a[i])) {
                map.put(a[i], new ArrayList<>());
            }
            List<Integer> l = map.get(a[i]);
            l.add(i);
        }
        
        for(int i = 0; i < a.length; i++) {
            int v0 = a[i];
            for(int j = i+1; j < a.length; j++) {
                int v1 = a[j];
                int sum2 = v0 + v1;
                int v2 = sum2 * -1;
                List<Integer> l = map.get(v2);
                int idx = binarySearchGTValue(l, j);
                if(idx == -1) {
                    continue;
                }
                for(; idx < l.size(); idx++) {
                    ll.add(Arrays.asList(v0,v1,v2));
                }
            }
        }
        return ll;        
    }
    
    private List<List<Integer>> threeSumUniqueCombinationUnsorted(int [] a) {
        class Triplet {
            final int v0;
            final int v1;
            final int v2;
            List<Integer> l = null; 
            Integer hashcode = null;

            List<Integer> get() {
                if(l == null) {
                    l = Arrays.asList(v0,v1,v2);
                }
                return l;
            }
            
            public Triplet(int v0, int v1, int v2) {
                this.v0 = v0;
                this.v1 = v1;
                this.v2 = v2;
            }
            
            public int v0() { return v0; }
            public int v1() { return v1; }
            public int v2() { return v2; }
            public boolean equals(Triplet t) {
                if(t == null) {
                    return false;
                }
                int thashcode = t.hashCode();
                if(thashcode != hashCode()) {
                    return false;
                }
                return (t.v0() == v0 && t.v1() == v1 && t.v2() == v2);
            }
            
            public int hashCode() {
                if(hashcode == null) {
                    hashcode = Objects.hashCode(get());
                }
                return hashcode;
            }
        }
        
        List<List<Integer>> ll = new ArrayList<>();
        Map<Integer, Integer> map = new HashMap<>();
        Map<Integer, Triplet> mapTriplet = new HashMap<>();
        
        for(int i = 0; i < a.length; i++) {
            if(!map.containsKey(a[i])) {
                map.put(a[i], 1);
            } else {
                map.put(a[i], map.get(a[i]) + 1);
            }
        }
        
        for(int i = 0; i < a.length; i++) { 
            int v0 = a[i];
            for(int j = i + 1; j < a.length; j++) {
                int v1 = a[j];
                int sum = v0 + v1;
                int v2 = sum * -1;
                if(!map.containsKey(v2)) {
                    continue;
                }
                int maxcount = map.get(v2);
                int count = 1;
                if(v0 == v2) {
                    count++;
                }
                if(v1 == v2) {
                    count++;
                }
                if(count > maxcount) {
                    continue;
                }
                int v0s = v0;
                int v1s = v1;
                int v2s = v2;
                {
                    // sorting
                    if(v0s > v1s) {
                        int tmp = v0s;
                        v0s = v1s;
                        v1s = tmp;
                    }
                    if(v0s > v2s) {
                        int tmp = v0s;
                        v0s = v2s;
                        v2s = tmp;
                    }
                    if(v1s > v2s) {
                        int tmp = v1s;
                        v1s = v2s;
                        v2s = tmp;
                    }
                }
                Triplet triplet = new Triplet(v0s,v1s,v2s);
                int hashcode = triplet.hashCode();
                if(mapTriplet.containsKey(hashcode)) {
                    continue;
                }
                mapTriplet.put(hashcode, triplet);
                ll.add(triplet.get());
            }
        }
        return ll;
    }
    
    private List<List<Integer>> threeSumUniqueCombinationUnsorted1(int [] a) {
        class Triplet {
            final int v0;
            final int v1;
            final int v2;
            List<Integer> l = null; 
            Integer hashcode = null;

            List<Integer> get() {
                if(l == null) {
                    l = Arrays.asList(v0,v1,v2);
                }
                return l;
            }
            
            public Triplet(int v0, int v1, int v2) {
                this.v0 = v0;
                this.v1 = v1;
                this.v2 = v2;
            }
            
            public int v0() { return v0; }
            public int v1() { return v1; }
            public int v2() { return v2; }
            public String toString() {
                return String.format("(%d,%d,%d)", v0,v1,v2);
            }
            public boolean equals(Triplet t) {
                if(t == null) {
                    return false;
                }
                int thashcode = t.hashCode();
                if(thashcode != hashCode()) {
                    return false;
                }
                return (t.v0() == v0 && t.v1() == v1 && t.v2() == v2);
            }
            
            public int hashCode() {
                if(hashcode == null) {
                    hashcode = Objects.hashCode(get());
                }
                return hashcode;
            }
        }
        
        List<List<Integer>> ll = new ArrayList<>();
        Map<Integer, Integer> map = new HashMap<>();
        Set<Triplet> set = new HashSet<>();
        Map<Integer, Triplet> mapTriplet = new HashMap<>();
        
        for(int i = 0; i < a.length; i++) {
            if(!map.containsKey(a[i])) {
                map.put(a[i], 1);
            } else {
                map.put(a[i], map.get(a[i]) + 1);
            }
            //p("%d ", a[i]);
        }
        //p("\n");
        
        for(int i = 0; i < a.length; i++) { 
            int v0 = a[i];
            for(int j = i + 1; j < a.length; j++) {
                int v1 = a[j];
                int sum = v0 + v1;
                int v2 = sum * -1;
                //p("i=%2d,j=%2d v0:%2d v1:%2d v2:%2d\n", i, j, v0, v1, v2);
                if(!map.containsKey(v2)) {
                    continue;
                }
                int maxcount = map.get(v2);
                int count = 1;
                if(v0 == v2) {
                    count++;
                }
                if(v1 == v2) {
                    count++;
                }
                if(count > maxcount) {
                    //p("val %2d count %d > maxcount %d\n", v2, count, maxcount);
                    continue;
                }
                int v0s = v0;
                int v1s = v1;
                int v2s = v2;
                {
                    // sorting
                    if(v0s > v1s) {
                        int tmp = v0s;
                        v0s = v1s;
                        v1s = tmp;
                    }
                    if(v0s > v2s) {
                        int tmp = v0s;
                        v0s = v2s;
                        v2s = tmp;
                    }
                    if(v1s > v2s) {
                        int tmp = v1s;
                        v1s = v2s;
                        v2s = tmp;
                    }
                }
                Triplet triplet = new Triplet(v0s,v1s,v2s);
                if(set.contains(triplet)) {
                    continue;
                }
                int hashcode = triplet.hashCode();
                if(mapTriplet.containsKey(hashcode)) {
                    continue;
                }
                Triplet tripletEntry = mapTriplet.get(hashcode);
                if(tripletEntry != null) {
                    if(!tripletEntry.equals(triplet)) {
                        //p("hash collision: unequal entry: " + tripletEntry.toString() + " triplet:" + triplet.toString() + "\n");
                    }
                    else {
                        //p("hash collision:   equal entry: " + tripletEntry.toString() + " triplet:" + triplet.toString() + "\n");
                    }
                    continue;
                }
                //p("new hash entry: " + triplet.toString() + "\n");
                mapTriplet.put(hashcode, triplet);
                set.add(triplet);
                ll.add(triplet.get());
            }
        }
        return ll;
    }
    private List<List<Integer>> threeSumUniqueCombinationsSorted(int [] a) {
        List<List<Integer>> ll = new ArrayList<>();
        List<Integer> lsorted = new ArrayList<>();
        Map<Integer, List<Integer>> map = new HashMap<>();

        for(int i = 0; i < a.length; i++) {
            lsorted.add(a[i]);
        }
        
        Collections.sort(lsorted);

        for(int i = 0; i < lsorted.size(); i++) {
            if(!map.containsKey(lsorted.get(i))) {
                map.put(lsorted.get(i), new ArrayList<>());
            }
            List<Integer> l = map.get(lsorted.get(i));
            l.add(i);
        }

        for(int i = 0; i < lsorted.size(); ) {
            int v0 = lsorted.get(i);
            for(int j = i+1; j < lsorted.size(); ) {
                int v1 = lsorted.get(j);
                int sum2 = v0 + v1;
                if(sum2 > 0) {
                    break;
                }
                int v2 = sum2 * -1;
                List<Integer> l = map.get(v2);
                int idx = binarySearchGTValue(l, j);
                if(idx == -1 || idx >= l.size()) {
                    j++;
                    continue;
                }
                ll.add(Arrays.asList(v0,v1,v2));

                while(j < lsorted.size() && lsorted.get(j) == v1) {
                    j++;
                }
            }
            
            while(i < lsorted.size() && lsorted.get(i) == v0) {
                i++;
            }
        }
        return ll;
    }

    public int binarySearch(List<Integer> l, int vref) {
        if(l == null || l.size() == 0) {
            return -1;
        }
        int idxs = 0;
        int idxe = l.size() - 1;
        while(idxs <= idxe) {
            int idxm = (idxs + idxe) / 2;
            int v = l.get(idxm);
            if(v == vref) {
                return idxm;
            }
            else if(v < vref) {
                idxs = idxm + 1;
            }
            else if(v > vref) {
                idxe = idxm - 1;
            }
        }
        return -1;
    }
    
    /**
     * return index of value that is immediately greater than vref
     * within list. if none exists, return -1.
     * 
     * 1
     * 
     * 2 3
     * 
     * @param l
     * @param vref
     * @return
     */
    public int binarySearchGTValue(List<Integer> l, int vref) {
        if(l == null || l.size() == 0) {
            return -1;
        }
        int idxs = 0;
        int idxe = l.size() - 1;
        while(idxs <= idxe) {
            int idxm = (idxs + idxe) / 2;
            int v = l.get(idxm);
            if(v == vref) {
                idxs = idxm + 1;
            }
            else if(v < vref) {
                idxs = idxm + 1;
            }
            else if(v > vref) {
                // this is what we want...
                // is immediately preceding less than or equal to vref?
                // if not, then do binary search again.
                if(idxm == 0) {
                    return idxm;
                }
                int vprecede = l.get(idxm - 1);
                if(vprecede <= vref) {
                    return idxm;
                }
                idxe = idxm - 1;
            }
        }
        return -1;
    }
    
    public int binarySearchGTIndex(List<Integer> l, int idxStart) {
        if(l == null || l.size() == 0 || idxStart >= l.size()) {
            return -1;
        }
        int vref = l.get(idxStart);
        int idxs = idxStart;
        int idxe = l.size() - 1;
        while(idxs <= idxe) {
            int idxm = (idxs + idxe) / 2;
            int v = l.get(idxm);
            if(v == vref ){
                if((idxm+1) == l.size()) {
                    return -1;
                }
                return idxm + 1;
            }
            else if(v < vref) {
                idxs = idxm + 1;
            }
            else if(v > vref) {
                idxe = idxm - 1;
            }
        }
        return -1;
    }
    
    public int kselect(List<Integer> l, int choose) {
        /*
         *   0 1 2 3 4 5 6 7 8 9     c  l   r   mid midv    i   j
         *  (5,6,7,2,3,1,9,8,0,4)    3  0   9   4   3       0   0
         *   5
         *     6
         */
        class KSelect {
            public void swap(List<Integer> list, int i, int j) {
                int tmp = list.get(j);
                list.set(j, list.get(i));
                list.set(i, tmp);
            }
            public int partition(List<Integer> list, int l, int r, int pivot) {
                int vpivot = list.get(pivot);
                u.p("pivot idx: %2d val:%2d\n", pivot, vpivot);
                swap(list, pivot, r);
                int j = l;
                for(int i = l; i <= r; i++) {
                    if(list.get(i) < vpivot) {
                        swap(list, j++, i);
                    }
                }
                swap(list, r, j);
                return j;
            }
            public int select(List<Integer> list, int l, int r, int choose) {
                if(l == r) {
                    return list.get(l);
                }
                int pivotidx = (l + r) / 2;
                u.p("\n");
                u.p("before: idx:%2d l:%2d r:%2d ", pivotidx, l, r);
                u.pl(list);
                int pivot = partition(list, l, r, pivotidx);
                u.p("after:  piv:%2d l:%2d r:%2d ", pivot, l, r);
                u.pl(list);
                if(pivot == choose) {
                    return list.get(pivot);
                }
                else if(choose < pivot) {
                    return select(list, l, pivot-1, choose);
                }
                else {
                    return select(list, pivot+1, r, choose);
                }
            }
        }
        
        KSelect kselect = new KSelect();
        return kselect.select(l, 0, l.size()-1, choose);
    }
    
    public void printDiffSet(char [] a1, char [] a2) {
        
    }
    
    static class DiffSet {
        MessageDigest digest;
        public DiffSet() {
            try {
                digest = MessageDigest.getInstance("SHA-1");
            } catch(NoSuchAlgorithmException e) {
                e.printStackTrace();
                digest = null;
            }
        }
        
        void p(String f, Object ...o) {
            System.out.printf(f, o);
        }
        
        public byte [] binaryDiffContiguous(
            int idxs, int idxe,
            byte [] ba1, byte [] ba2)
        {
            boolean fixme = true;
            if(fixme) 
                return null;
            
            if(idxs >= idxe) {
                return null;
            }
            int idxm = idxs+(idxe-idxs)/2;
            int h1l = hash(idxs, idxm, ba1);
            int h2l = hash(idxs, idxm, ba2);
            int h1r = hash(idxm+1, idxe, ba1);
            int h2r = hash(idxm+1, idxe, ba2);
            
            if(h1l == h2l && h1r == h2r) {
                return null;
            }
            else if(h1l == h2l) {
                return binaryDiffContiguous(idxm+1, idxe, ba1, ba2);
            }
            else if(h1r == h2r) {
                return binaryDiffContiguous(idxs, idxm, ba1, ba2);
            }
            else {
                byte [] bal = binaryDiffContiguous(idxs, idxm, ba1, ba2);
                byte [] bar = binaryDiffContiguous(idxm+1, idxe, ba1, ba2);
                try {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    baos.write(bal);
                    baos.write(bar);
                    return baos.toByteArray();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }

        public String binaryDiffContiguousFixedLength(
                int idxs, 
                int idxe, 
                String s1, 
                String s2,
                int szMax) 
        {

            if(idxs >= idxe) {
                return null;
            }

            int idxm = idxs + (idxe - idxs) / 2;
            byte [] h1s1 = hash(idxs, idxm, s1);
            byte [] h2s1 = hash(idxm+1, idxe, s1);
            byte [] h1s2 = hash(idxs, idxm, s2);
            byte [] h2s2 = hash(idxm+1, idxe, s2);
            boolean b1 = cmp(h1s1,h1s2);
            boolean b2 = cmp(h2s1,h2s2);

            if(!b1 && !b2) {
                // must be +/- szMax of this
                idxs = ((idxm-szMax) > 0) ? (idxm-szMax) : (idxs);
                idxe = ((idxm+szMax) < idxe) ? (idxm+szMax) : (idxe);
                String sret = s1.substring(idxs, idxe+1);
                //p("returning idx [%2d:%2d] = %s\n", idxs, idxe, sret);
                return sret;
            }
            else if(b1 && !b2) {
                return binaryDiffContiguousFixedLength(idxm+1,idxe, s1, s2, szMax);
            }
            else if(!b1 && b2) {
                return binaryDiffContiguousFixedLength(idxs, idxm, s1, s2, szMax);
            }
            else {
                return null;
            }
        }
        
        public String binaryDiffContiguousVariableLength(
            int idxs, 
            int idxe, 
            String s1, 
            String s2) 
        {
            if(idxs >= idxe) {
                return null;
            }

            int idxm = idxs + (idxe - idxs) / 2;
            byte [] h1s1 = hash(idxs, idxm, s1);
            byte [] h1s2 = hash(idxs, idxm, s2);
            byte [] h2s1 = hash(idxm+1, idxe, s1);
            byte [] h2s2 = hash(idxm+1, idxe, s2);

            boolean b1 = cmp(h1s1,h1s2);
            boolean b2 = cmp(h2s1,h2s2);

            if(b1 && b2) {
                return null;
            }
            else if(b1 && !b2) {
                return binaryDiffContiguousVariableLength(idxm+1,idxe, s1, s2);
            }
            else if(!b1 && b2) {
                return binaryDiffContiguousVariableLength(idxs, idxm, s1, s2);
            }
            else {
                // find left edge and right edge and concatenate
                String retsl = binaryDiffVariableHelper(idxs, idxm, true, s1, s2);
                String retsr = binaryDiffVariableHelper(idxm+1, idxe, false, s1, s2);
                String rets = retsl + retsr; // both must be non null
                //p("return %s\n", rets);
                return rets;
            }
        }
        
        /**
         * Helper method to find either left or right hand boundary.
         * If left boundary search, then fix right boundary.
         * If right boundary search, then fix left boundary.
         * Assumed that if left search, idxe is a match and is fixed.
         * Assumed that if right search, idxs is a match and is fixed.
         * You want to find the last point where there is a match, or
         * the first point where there is a mismatch, not the last 
         * point where there is mismatch. 
         * 
         * You cannot look for the last point where there is mismatch,
         * only the first point where there is mismatch.
         * 
         * eg
         * 
         * left search     right search
         *             | |
         * --xxxxxxxxxx| |xxxxxxxxxxx---
         *       |
         *    |
         *  |
         *   |
         */
        private String binaryDiffVariableHelper(
            int idxs, int idxe, boolean isLeft, String s1, String s2) 
        {
            if(idxs >= idxe) {
                return null;
            }

            int idxm = idxs + (idxe - idxs) / 2;
            byte [] hsl1 = hash(idxs, idxm, s1);
            byte [] hsl2 = hash(idxs, idxm, s2);
            byte [] hsr1 = hash(idxm+1, idxe, s1);
            byte [] hsr2 = hash(idxm+1, idxe, s2);
            boolean bl = cmp(hsl1, hsl2);
            boolean br = cmp(hsr1, hsr2);
            
            if(isLeft) {
                // find the first left point of mismatch
                // if first half is match, look in second half.
                // if first half is mismatch, 
                //     if idxs:idxm-1 is match, then found mismatch point
                //     else binarysearch(idxs:idxm-1) + string(idxm:idxe)
                if(bl) {
                    return binaryDiffVariableHelper(idxm+1, idxe, isLeft, s1, s2);
                }
                else {
                    if(idxs == (idxm-1)) {
                        return s1.substring(idxs, idxe+1);
                    }
                    hsl1 = hash(idxs, idxm-1, s1);
                    hsl2 = hash(idxs, idxm-1, s2);
                    bl = cmp(hsl1, hsl2);
                    if(bl) {
                        return s1.substring(idxm, idxe+1);
                    }
                    String sretIdxXM = binaryDiffVariableHelper(idxs, idxm-1, isLeft, s1, s2);
                    String sretIdxME = s1.substring(idxm, idxe+1);
                    return new String(sretIdxXM + sretIdxME);
                }
            } else {
                // find first right point of mismatch.
                // if right half half is match, look in left half.
                // if left half is mismatch
                //     if idxm+1:idxe is match, then found mismatch point
                //     else string(idxs:idxm) + binarysearch(idxm+1,idxe)
                if(br) {
                    return binaryDiffVariableHelper(idxs, idxm, isLeft, s1, s2);
                }
                else {
                    if(idxe <= (idxm+2)) {
                        return s1.substring(idxs, idxe+1);
                    }
                    hsr1 = hash(idxm+2, idxe, s1);
                    hsr2 = hash(idxm+2, idxe, s2);
                    br = cmp(hsr1, hsr2);
                    if(br) {
                        return s1.substring(idxs, idxm+2);
                    }
                    String sretIdxSM = s1.substring(idxs, idxm+1);
                    String sretIdxM1X = binaryDiffVariableHelper(idxm+1, idxe, isLeft, s1, s2);
                    return new String(sretIdxSM + sretIdxM1X);
                }
            }
        }
        
        byte [] hash(int idxs, int idxe, String s) {
            byte [] ba = s.substring(idxs, idxe+1).getBytes(StandardCharsets.UTF_8);
            return digest.digest(ba);
        }
        
        int hash(int idxs, int idxe, byte [] ba) {
            int offset = idxs;
            int len = idxe - idxs + 1;
            int ret = -1;
            if(idxs >= idxe) {
                return ret;
            }
            try {
                return digest.digest(ba, offset, len);
            } catch(Exception e) {
                return ret;
            }
        }

        private boolean cmp(byte [] a1, byte [] a2) {
            if(a1.length != a2.length) {
                return false;
            }
            for(int i = 0; i < a1.length; i++) {
                if(a1[i] != a2[i]) {
                    return false;
                }
            }
            return true;
        }
        
        /**
         * Find the first occurrence of a character c in string. The
         * string will always be 2 character set.
         */
        public int findFirstIdxBinaryChange(String s, char c, int idxs, int idxe) {
            if(idxs >= idxe) {
                return -1;
            }
            int idxm = idxs + (idxe - idxs) / 2;
            char c1 = s.charAt(idxm);
            if(c == c1) {
                if(idxm == 0) {
                    return 0;
                }
                c1 = s.charAt(idxm-1);
                if(c != c1) {
                    return idxm;
                }
                return findFirstIdxBinaryChange(s, c, idxs, idxm);
            }
            return findFirstIdxBinaryChange(s, c, idxm+1, idxe);
        }

        /**
         * Find the last occurrence of a character c in string. The
         * string will always be 2 character set.
         */
        public int findLastIdxBinaryChange(String s, char c, int idxs, int idxe) {
            if(idxs >= idxe) {
                return -1;
            }
            int idxm = idxs + (idxe - idxs) / 2;
            char c1 = s.charAt(idxm);
            if(c == c1) {
                if(idxm == idxe) {
                    return idxe;
                }
                c1 = s.charAt(idxm+1);
                if(c != c1) {
                    return idxm;
                }
                return findLastIdxBinaryChange(s, c, idxm, idxe);
            }
            return findLastIdxBinaryChange(s, c, idxs, idxm-1);
        }

    }
    
    public static class NodeRange implements Comparable<Integer> {
        public Integer min;
        public Integer max;
        public Integer v;
        public String msg;
        public NodeRange(int min, int max, String msg) {
            this.min = min;
            this.max = max;
            this.msg = msg;
        }
        public Integer min() {
            return min;
        }
        public Integer max() {
            return max;
        }
        public String msg() {
            return msg;
        }
        @Override
        public int compareTo(Integer o) {
            if(min <= o && o <= max) {
                return 0;
            }
            if(min < o) {
                return -1;
            }
            return 1;
        }
        public void print() {
            System.out.printf("min:%2d max:%2d msg:%s\n", min, max, msg);
        }
    }

    public static class BinarySearch {
        public Integer binarySearchExactIterative(List<Integer> list, int v) {
            return null;
        }
        public Integer binarySearchExactRecursive(List<Integer> list, int v) {
            return null;
        }
        /**
         * find match or idx immediately preceding match, if match not found.
         */
        public Integer binarySearchIterative(List<Integer> list, int v) {
            int idxs = 0;
            int idxe = list.size() - 1;
            int idxm = 0;
            while(idxs <= idxe) {
                idxm = idxs + (idxe - idxs) / 2;
                Integer item = list.get(idxm);
                if(item == v) {
                    return idxm;
                }
                else if(v < item) { // 7 < 10
                    if(idxm > idxs) {
                        item = list.get(idxm-1);
                        if(v > item) { // 7 > 5
                            return idxm - 1;
                        }
                    }
                    idxe = idxm - 1;
                }
                else { // 7 > 5
                    if(idxm < idxe) {
                        item = list.get(idxm+1);
                        if(v < item) { // 7 < 10
                            return idxm;
                        }
                    }
                    idxs = idxm + 1;
                }
            }
            return idxm;
        }
        public Integer binarySearchRecursive(List<Integer> list, int v) {
            return binarySearchRecursive(list, v, 0, list.size()-1);
        }
        Integer binarySearchRecursive(List<Integer> list, int v, int idxs, int idxe) {
            if(idxs > idxe) {
                return -1;
            }
            int idxm = idxs + (idxe - idxs)/2;
            Integer item = list.get(idxm);
            if(v < item) { // 7 < 10
                if(idxm > idxs) {
                    item = list.get(idxm-1);
                    if(v > item) { // 7 > 5
                        return idxm-1;
                    }
                }
                return binarySearchRecursive(list, v, idxs, idxm-1);
            }
            else if(v > item) { // 7 > 5
                if(idxm < idxe) {
                    item = list.get(idxm+1);
                    if(v < item) {
                        return idxm;
                    }
                    return binarySearchRecursive(list, v, idxm+1, idxe);
                }
                else {
                    return idxm;
                }
            }
            else {
                return idxm;
            }
        }
        /**
         * find match or idx immediately preceding match, if match not found.
         */
        public NodeRange binarySearchRangeItertive(List<NodeRange> list, int v) {
            int idxs = 0;
            int idxe = list.size()-1;
            while(idxs <= idxe) {
                int idxm = idxs + (idxe - idxs)/2;
                NodeRange node = list.get(idxm);
                int cmp = node.compareTo(v);
                if(cmp == 0) {
                    return node;
                }
                else if(cmp < 0) {
                    idxs = idxm+1;
                }
                else {
                    idxe = idxm-1;
                }
            }
            return null;
        }
        public NodeRange binarySearchRangeRecursive(List<NodeRange> list, int v) {
            return binarySearchRangeRecursive(list, v, 0, list.size()-1);
        }
        NodeRange binarySearchRangeRecursive(List<NodeRange> list, int v, int idxs, int idxe) {
            if(idxs > idxe) {
                return null;
            }
            int idxm = idxs + (idxe-idxs)/2;
            NodeRange node = list.get(idxm);
            int cmp = node.compareTo(v);
            if(cmp == 0) {
                return node;
            }
            else if(cmp < 0) {
                return binarySearchRangeRecursive(list, v, idxm+1, idxe);
            }
            else {
                return binarySearchRangeRecursive(list, v, idxs, idxm-1);
            }
        }
    }
    /**
     * These sorting algos dont account for duplicates!
     */
    public static class SortTypes {
        public void mergesort(List<Integer> l) {
            /**
             * i    0 1 2 3 4 5
             * v    8 3 7 4 9 6
             *      
             *      8 3 7
             *      8
             *        3 7
             *      3 7 8
             *            4 9 6
             *            4
             *              6 9
             *            4 6 9
             *      3 4 6 7 8 9
             * ----------------------
             * i    0 1 2 3 4 5 6
             * v    8 3 7 4 9 6 1
             * 
             * 
             */
            class Internal {
                public void mergesort(List<Integer> l, int idxs, int idxe)
                {
                    List<Integer> lcopy = new ArrayList<>(l);
                    mergesort(l, lcopy, idxs, idxe);
                }
                void merge(List<Integer> lo, List<Integer> lc,
                    int idxs, int idxm, int idxe)
                {
                    // copy first then merge
                    for(int i = idxs; i < idxe; i++) {
                        lc.set(i, lo.get(i));
                    }
                    // sort back to original
                    for(int i = idxs,   // i is always index to copy
                            j = idxs,   // j is left side original
                            k = idxm+1; // k is right side original
                        i <= idxe; 
                        i++)
                    {
                        int v;
                        if(j == idxm && k == idxe) {
                            break;
                        }
                        if(j == idxm) {
                            v = lc.get(k++);
                        }
                        else if(k == idxe) {
                            v = lc.get(j++);
                        }
                        else if(lc.get(j) < lc.get(k)) {
                            v = lc.get(j++);
                        }
                        else {
                            v = lc.get(k++);
                        }
                        lo.set(i, v);
                    }
                }
                void mergesort(
                    List<Integer> lo, List<Integer> lc,
                    int idxs, int idxe)
                {
                    if(idxs > idxe) {
                        return;
                    }
                    int idxm = idxs + (idxe-idxs)/2;
                    mergesort(lo, lc, idxs, idxm);
                    mergesort(lo, lc, idxm+1, idxe);
                    merge(lo, lc, idxs, idxm, idxe);
                }
            }
            Internal internal = new Internal();
            internal.mergesort(l,  0, l.size()-1);
        }
        public void quicksortDuplicate(List<Integer> l) {
            class Internal {
                
            }
            Internal internal = new Internal();
        }
        public void quicksort(List<Integer> l) {
            class Internal {
                /**
                 * qspartition
                 * pick idxm = midpoint and value as v.
                 * sweep left to right to pick idx[l] > v
                 * sweep right to left to pick idx[r] < v
                 * swap(idx[l], idx[r]).
                 * repeat until l >= r
                 * 
                 * idx  2 3 4 5 6 7
                 * val  8 3 7 4 9 1    idxm = 2 + (7-2)/2 = 2 + 2 = 4, v = 7
                 * 
                 * idxl,v   idxr,v      swap
                 *                      .         .
                 * 2,8      7,1         1 3 7 4 9 8
                 * 
                 * 3,3      6,9
                 *                          . .
                 * 4,7      5,4         1 3 4 7 9 8
                 * 
                 * ----------------------------------------------------------
                 * 
                 * idx  2 3 4 5 6 7 8
                 * val  8 3 7 4 9 1 6  idxm = 2 + (8-2)/2 = 2 + 3 = 5, v = 4
                 * 
                 * idxl,v   idxr,v      swap
                 *                      
                 *                      8 3 7 4 9 1 6
                 *                      
                 * 2,8      8,6         .         .
                 *          7,1         1 3 7 4 9 8 6
                 * 3,3      6,9
                 *                          . .
                 * 4,7      5,4         1 3 4 7 9 8 6
                 * 
                 * 5,7      4,4
                 * 
                 */
                public int qspartition1(List<Integer> l, int idxl, int idxr) {
                    int idxm = idxl + (idxr-idxl)/2;
                    int v = l.get(idxm);
                    while (idxl < idxr) {
                        while(l.get(idxl) < v) {
                            idxl++;
                        }
                        while(l.get(idxr) > v) {
                            idxr--;
                        }
                        if(idxl >= idxr) {
                            return idxr;
                        }
                        swap(l, idxl, idxr);
                    }
                    return idxr;
                }
                public int qspartition2(List<Integer> l, int idxl, int idxr) {
                    int idxm = idxl;
                    while(true) {
                        while(l.get(idxl) < l.get(idxm)) {
                            idxl++;
                        }
                        while(l.get(idxr) > l.get(idxm)) {
                            idxr--;
                        }
                        if(idxl >= idxr) {
                            return idxr;
                        }
                        swap(l, idxl, idxr);
                    }
                }
                public int qspartition3(List<Integer> l, int idxl, int idxr) {
                    int idxm = idxl + (idxr-idxl)/2;
                    int v = l.get(idxm);
                    while(true) { 
                        while(l.get(idxl) < v) {
                            idxl++;
                        }
                        while(l.get(idxr) > v) {
                            idxr--;
                        }
                        if(idxl >= idxr) {
                            return idxr;
                        }
                        swap(l, idxl, idxr);
                    }
                }
                public int qspartition(List<Integer> l, int idxl, int idxr, int typepartition) {
                    switch(typepartition) {
                        case 1: return qspartition1(l, idxl, idxr);
                        case 2: return qspartition2(l, idxl, idxr);
                        case 3: return qspartition3(l, idxl, idxr);
                        default: return qspartition1(l, idxl, idxr);
                    }
                }
                public void qssort(List<Integer> l, int idxs, int idxe) {
                    if(idxs >= idxe) {
                        return;
                    }
                    int idx = qspartition(l, idxs, idxe, 0);
                    qssort(l, idxs, idx);
                    qssort(l, idx+1, idxe);
                }
            }
            Internal internal = new Internal();
            internal.qssort(l, 0, l.size()-1);
        }
        public void swap(List<Integer> l, int i, int j) {
            Integer vswap = l.get(i);
            l.set(i, l.get(j));
            l.set(j, vswap);
        }
        public void heapsort(List<Integer> l) {
            class Internal {
                
            }
            Internal internal = new Internal();
            List<Integer> lc = new ArrayList<>(l);
            l.clear();
            for(int i = 0; i < lc.size(); i++) {
                
            }
        }
        public void bucketsort(List<Integer> l, int min, int max) {
            int numbuckets = max - min + 1;
            int [] buckets = new int[numbuckets];
            for(int i = 0; i < l.size(); i++) {
                int v = l.get(i);
                buckets[v]++;
            }
            for(int i = 0, j = 0; i < numbuckets && j < l.size(); i++) {
                int v = buckets[i];
                if(v == 0) {
                    continue;
                }
                while(v != 0) {
                    l.set(j, i);
                    j++;
                    v--;
                }
            }
        }
        public void radixsort(List<Integer> l) {
            class Internal {
                
            }
            Internal internal = new Internal();

        }
        public void partialquicksort(List<Integer> l, int maxidx) {
            class Internal {
                
            }
            Internal internal = new Internal();

        }
        public void insertionsort(List<Integer> l) {
            class Internal {
                
            }
            Internal internal = new Internal();

        }
        public void distributesort(List<Integer> l, int numways) {
            class Internal {
                
            }
            Internal internal = new Internal();

        }
    }
    
    public static class Hash<K,V> {
        
    }

    public static class BasicHashing {
        public static class Node {
            int numkeys;
            
        }
        
        Integer numNodes;
        
        public BasicHashing(int numnodes) {
            this.numNodes = numnodes;
        }
        public void addNode(int numnodes) {
            
        }
        public void deleteNode(int nodeId) {
            
        }
        public Node getNode(int nodeId) {
            return null;
        }
        public Integer hash(Integer k) {
            return null;
        }
        
    }
    
    public static class MemBuf {
        final int minaddr;
        final int maxaddr;
        final int rsv;
        final int size;
        int used;
        final byte [] a;
        boolean active;
        final int szLink = 12;
        LinkedList<MemBuf> listMemBuf = new LinkedList<>(); // cheat for links

        public MemBuf(int minaddr, int maxaddr, int rsv, byte [] a) {
            this.minaddr = minaddr;
            this.maxaddr = maxaddr;
            this.rsv = (rsv < 4) ? 4 : rsv;
            this.a = a;
            this.size = maxaddr - minaddr - rsv + 1;
            this.used = 0;
            this.active = true;
        }
        
        public int size() {
            return size;
        }
        
        public int used() {
            return used;
        }

        /**
         * link this MemBuf to a child MemBuf
         */
        public boolean linkChild(MemBuf membuf) {
            for(MemBuf mb: listMemBuf) {
                if(mb == membuf) {
                    return false;
                }
            }
            if((used + szLink) > size) {
                return false;
            }
            used += szLink;
            listMemBuf.add(membuf);
            return true;
        }
        
        /**
         * unlink this MemBuf from a child MemBuf
         */
        public boolean unlinkChild(MemBuf membuf) {
            for(MemBuf mb: listMemBuf) {
                if(mb == membuf) {
                    used -= szLink;
                    boolean result = listMemBuf.remove(mb);
                    return result == true;
                }
            }
            return false;
        }
        
        public boolean active() {
            return active;
        }
        
        public void dealloc() throws Exception {
            if(!active) {
                throw new Exception ("MemBuf is inactive");
            }
            this.active = false;
        }
        
        public List<MemBuf> getLinks() {
            return listMemBuf;
        }
        
        public int write(byte [] data, int offset) throws Exception {
            int size = data.length;
            int idxe = offset + size;
            if(size > this.size) {
                throw new Exception("MemBuf write size out of bounds");
            }
            if(idxe > this.size) {
                throw new Exception("MemBuf write index out of bounds");
            }
            for(int i = 0; i < size; i++) {
                
            }
            return 0;
        }
        
        public byte [] read(int offset, int size) throws Exception {
            return null;
        }
    }
    /**
     * basic memory map that allows:
     * alloc
     * dealloc
     * compaction
     * garbage collect
     */
    public static class MemMapBasic {
        class MemPtr {
            int addr;
            int sz;
            MemBuf membuf;
            MemPtr p;
            MemPtr n;
        }
        final int szMinRsvMemBuf = 32;
        final int szMinMem = 1024;
        byte [] a;
        int size;
        int szMinAlloc = 1;
        int used;
        int avail;
        final static int MB = 1024 * 1024;
        final static int szMaxMem = 64 * MB;
        LinkedList<MemPtr> ll = new LinkedList<>();

        public MemMapBasic(int mapsize, int szMinAlloc) {
            if( mapsize > szMaxMem || 
                szMinAlloc < 1 || 
                szMinAlloc > szMaxMem) 
            {
                return;
            }
            this.a = new byte[mapsize];
            this.size = mapsize;
            this.szMinAlloc = szMinAlloc;
            this.avail = size;
            this.used = 0;
        }
        
        public MemBuf alloc(int size) {
            return null;
        }
        
        /**
         * Just mark MemBuf as invalid. It can be recycled later.
         */
        public boolean dealloc(MemBuf membuf) {
            return true;
        }
        
        /**
         * This moves memory to compacted area, which enables
         * better alloc.
         */
        public void compact() {
            
        }
        
        /**
         * This traverses the linked list of BlockInfo and removes
         * non active entries from the list.
         */
        public void recycle() {
            
        }
        
        public void printStats() {
            p("MemMap stats:\n");
            p("\tsize:%d minBlockSize:%d\n", size, szMinAlloc);
            p("\tused:%d available:%d\n", used, avail);
            p("\tnumBlocksAlloc:%d\n", ll.size());
        }
    }
    
    /**
     * consistent hash has following methods:
     * 
     * addNode(Node)
     * deleteNode(Node)
     * replicate(Node)
     * Node getNode(Data)
     * int hash(Data)
     * ConsistentHash(replicaFactor, numInitialNodes, ringSize)
     * 
     * Node has following:
     * Map<K,Data> map
     */
    public static class ConsistentHash1 {

        public static class Node {
            Integer id;
            Integer lo;
            Integer hi;
            Node n;
            Node p;
            Node l;
            Node r;
            Map<Integer, Object> mapPrim = new HashMap<>();
            Map<Integer, Object> mapRepl = new HashMap<>();
            public Node(Integer id) {
                this.id = id;
            }
            public Node(Integer id, Integer lo, Integer hi) {
                this.id = id;
                this.lo = lo;
                this.hi = hi;
            }
        }

        public static class Ring {
            Node root = null;
            Node head = null;
            public void addNode(Node n) {
                if(root == null) {
                    root = n;
                    head = n;
                    return;
                }
                if(n.lo <= head.lo) {
                    head = n;
                }
                addNode(root, n);
            }
            /**
             * 
             */
            void addNode(Node c, Node n) {
                
            }
            public Node getNode(Integer k) {
                return getNode(root, k);
            }
            Node getNode(Node n, Integer k) {
                if(n.lo <= k && k <= n.hi) {
                    return n;
                }
                else if(k < n.lo) {
                    return getNode(n.l, k);
                }
                else {
                    if(n.r == null) {
                        return head;
                    }
                    return getNode(n.r, k);
                }
            }
            public void deleteNode(Node n) {
                
            }
        }

        int replica;
        int numNodes;
        int sizeRing;
        int ctr = 0;
        int prime = 103; // 53,103,157,211,557,997;
        //Ring ring = new Ring();
        TreeMap ring = new TreeMap();
        public ConsistentHash1(
            int replicaFactor, 
            int numInitialNodes, 
            int sizeRing) 
        {
            this.replica = replicaFactor;
            this.numNodes = numInitialNodes;
            this.sizeRing = sizeRing;
            
            for(int i = 0; i < numNodes; i++) {
                Node node = new Node(ctr++);
            }
        }
        public Node addNode() {
            Node node = new Node(ctr++);
            for(int i = 0; i < replica; i++) {
                int hash = hash(node.id + i);
            }
            return node;
        }
        public boolean deleteNode(Node n) {
            return true;
        }
        public Node getNode(Integer k) {
            return null;
        }
        public void put(Integer k, String v) {
            
        }
        public String get(Integer k) {
            return null;
        }
        public int hash(Integer k) {
            return k % prime;
        }
    }
    
    /**
     * return index of local min.
     */
    public int localMin(int [] a) {
        int idxs = 0,
            idxe = a.length-1;
        int idxmin = 0;
        while(idxs < idxe) {
            int idxm = idxs + (idxe - idxs)/2;
            idxmin = (a[idxm] < a[idxmin]) ? idxm : idxmin;
            
            if(((idxm-1) > idxs) && ((idxm+1) < idxe)) {
                idxmin = 
                    (a[idxm-1] > a[idxmin]) ? idxmin :
                    (a[idxm-1] < a[idxm+1]) ? idxm-1 : idxm+1;
                if(a[idxm-1] < a[idxm] && a[idxm+1] < a[idxm]) {
                    return idxm;
                }
                else if(a[idxm-1] < a[idxm]) {
                    idxe = idxm-1;
                }
                else {
                    idxs = idxm+1;
                }
            }
            else if((idxm-1) <= idxs) {
                idxs = idxm+1;
            }
            else {
                idxe = idxm-1;
            }
        }
        return idxmin;
    }
    
    /**
     * find the val v in array a, where a is sorted, but rotated.
     * if val does not exist, return null.
     * 
     * eg   a = 1 2 3 4 5 6
     *      a = 3 4 5 6 1 2     is sorted and rotated
     */
    public Integer getIdxInRotatedArray(int [] a, int v) {
        return 0;
    }
    
}
