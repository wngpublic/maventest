package org.wayne.misc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class Algos {
    AlgosHelper helper = new AlgosHelper();
    Utils u = new Utils();
    public void p(String f, Object ...o) {
        System.out.printf(f, o);
    }
    public String longestPalindrome(String s) {
        int sz = s.length();
        if(sz == 1) return s;
        String sret = null;
        int max = 0;
        for(int i = 0; i < sz; i++) {
            int sz1 = helper.palindromeHelper(s, i, i+1);
            int sz2 = helper.palindromeHelper(s, i, i+2);
            if(sz1 > max || sz2 > max) {
                if(sz1 > sz2) {
                    int idxs = i - sz1/2;
                    sret = s.substring(idxs, idxs+sz1);
                    max = sz1;
                } else {
                    int idxs = i - sz2/2 + 1;
                    sret = s.substring(idxs, idxs+sz2);
                    max = sz2;
                }
            }
        }
        return sret;
    }

    public static class ListNode {
        int val;
        ListNode next;
        ListNode(int x) {
            val = x;
        }
    }

    /**
     * given linked list, remove nth node from end of list and return its head.
     *
     * eg   1 2 3 4 5 and n = 2
     *      1 2 3 5 is output
     *
     *
     *      maintain 2 pointers.
     *      ptr1 = i
     *      ptr2 = i + n
     *
     *      once ptr2 == end, then ptr1 == n.
     *
     *      p   p
     *      1   2
     *        1   2
     *          1   2
     *      ---------
     *      1 2 3 4 5
     *
     *      p p
     *      1 2
     *      ---
     *      1
     *
     *      p1  p2
     *      1
     *
     *      p1  p2
     *      1   2
     *
     *                      if n == 0, then 1,2,3
     *      p1  p2          if n == 1, then 1,2
     *      p2              if n == 2, then 1,3
     *                      if n == 3, then 2,3
     *
     *      n == 1
     *      1   2   3
     *      p1  p2
     *          p1  p2      p1.next = (p1.next == null) ? null : p1.next.next
     *
     *      n == 2
     *      1   2   3
     *      p1      p2      p1.next = (p1.next == null) ? null : p1.next.next
     *
     *      n == 3
     *      1   2   3
     *      p1          p2  if(p1 == head) { return p1.next }
     *
     *  n = 2       then 1,2,4
     *  1 2 3 4
     *  a   b
     *    a   b
     *
     *  n = 3       then 1,3,4
     *  1 2 3 4
     *  a     b
     *
     *  n = 1
     *  1
     *  a b
     *
     *  n = 1       then 1
     *  1 2
     *  a b         p1.next = (p1.next == null) ? null : p1.next.next
     */
    public ListNode removeNthNodeFromEndOfList(ListNode head, int n) {
        ListNode ptr1 = head;
        ListNode ptr2 = head;

        if(n == 0) {
            return head;
        }

        // ptr2 jumps ahead of ptr1 by n
        for(int i = 0; i < n && ptr2 != null; i++) {
            ptr2 = ptr2.next;
        }

        // go til end of list
        while(ptr2 != null && ptr2.next != null) {
            ptr1 = ptr1.next;
            ptr2 = ptr2.next;
        }
        if(ptr1 == head && ptr2 == null) {
            return ptr1.next;
        }
        ptr1.next = (ptr1.next == null) ? null : ptr1.next.next;
        return head;
    }

    /**
     * given array of ints sorted in ascending order,
     * find starting and ending position of given target.
     * runtime must be O(logn)
     *
     * if target not found, return [-1,-1]
     *
     * eg       [5,7,7,8,8,10] target = 8
     * return         [3,4]
     */
    public int [] searchForRange(int [] a, int target) {
        class Internal {
            int [] searchForRange(int [] a, int target) {
                int [] ret = new int[2];
                /*
                int idx = binsearch(a, target);
                if(idx == -1) {
                    ret[0] = -1;
                    ret[1] = -1;
                    return ret;
                }
                */
                ret[0] = leftedge(a, target);
                if(ret[0] == -1) {
                    ret[1] = -1;
                    return ret;
                }
                ret[1] = rightedge(a, target);
                return ret;
            }
            /**
             * left edge index that still equals target
             */
            int leftedge(int [] a, int target) {
                int idxs = 0;
                int idxe = a.length - 1;
                while(idxs <= idxe) {
                    int idxm = idxs + (idxe - idxs)/2;
                    int v = a[idxm];
                    if(v < target) {
                        if((idxm+1) <= idxe && a[idxm+1] == target) {
                            return (idxm+1);
                        }
                        idxs = idxm + 1;
                    }
                    else {
                        if(v == target) {
                            if((idxm-1) >= idxs && a[idxm-1] != target) {
                                return idxm;
                            }
                        }
                        idxe = idxm - 1;
                    }
                }
                return -1;
            }
            /**
             * right edge index that still equals target
             */
            int rightedge(int [] a, int target) {
                int idxs = 0;
                int idxe = a.length - 1;
                while(idxs <= idxe) {
                    int idxm = idxs + (idxe - idxs)/2;
                    int v = a[idxm];
                    if(v > target) {
                        if((idxm-1) >= idxs && a[idxm-1] == target) {
                            return (idxm-1);
                        }
                        idxe = idxm - 1;
                    }
                    else {
                        if(v == target) {
                            if((idxm+1) <= idxe && a[idxm+1] != target) {
                                return idxm;
                            }
                        }
                        idxs = idxm + 1;
                    }
                }
                return -1;
            }
        }
        Internal internal = new Internal();
        return internal.searchForRange(a, target);
    }

    /**
     * given unsorted int array, find first missing positive number.
     * runtime must be O(n). There can be duplicates.
     *
     * eg       1,2,3       return 3
     *          3,4,-1,1    return 2
     *
     * 1+2+3+4+5 = 15 = n(n+1)/2 = 5*(6)/2 = 30/2 = 15
     * 1+2+3+4+5+6 = 21 = n(n+1)/2 = 6*(7)/2 = 42/2 = 21
     * 3+4+5+6+7 = 25 = n(n+1)/2 = 6*(7)/2 = 42/2 = 21
     *
     * iterate through list to find min > 0 && max > 0 and sum
     *
     * use this equation to find 1:min-1 = a
     * use this equation to find 1:max   = b
     * then expected value = b - a - sum = c
     * if(min > 1) then missing positive is 1
     * if(c == 0) then missing positive is max+1
     * if(c != 0) then missing positive is c
     *
     */
    public int firstMissingPositive(int [] a) {
        class Internal {
            int fmp1(int [] a) {
                int sum = 0;
                int min = 0;
                int max = 0;
                int numPositive = 0;
                Set<Integer> set = new HashSet<>();
                for(int i = 0; i < a.length; i++) {
                    if(a[i] <= 0) {
                        continue;
                    }
                    if(set.contains(a[i])) {
                        continue;
                    }
                    set.add(a[i]);
                    numPositive++;
                    min = (min == 0 || min > a[i]) ? a[i] : min;
                    max = (max == 0 || max < a[i]) ? a[i] : max;
                    sum += a[i];
                }
                if(min > 1) {
                    return 1;
                }
                int sum1 = (min-1)*(min-1+1)/2;
                int sum2 = (max)*(max+1)/2;
                int sum3 = sum2 - sum1;
                if(sum3 == sum) {
                    return max+1;
                }
                int diff = sum3-sum;
                return diff;
            }
            int fmp2(int [] a) {
                class Segment {
                    int min;
                    int max;
                    public Segment(int min, int max) {
                        this.min = min;
                        this.max = max;
                    }
                }
                if(a == null || a.length == 0) {
                    return 1;
                }
                int min = 0;
                int max = 0;
                Map<Integer, Segment> map = new HashMap<>();
                for(int i = 0; i < a.length; i++) {
                    int v = a[i];
                    if(v <= 0 || map.containsKey(v)) {
                        continue;
                    }
                    if(!map.containsKey(v-1) && !map.containsKey(v+1)) {
                        Segment seg = new Segment(v,v);
                        map.put(v, seg);
                    }
                    else if(map.containsKey(v-1) && map.containsKey(v+1)) {
                        Segment segl = map.get(v-1);
                        Segment segr = map.get(v+1);
                        segl.max = segr.max;
                        segr.min = segl.min;
                    }
                    else if(map.containsKey(v-1)) {
                        Segment seg = map.get(v-1);
                        map.put(v, seg);
                        seg.max++;
                    }
                    else if(map.containsKey(v+1)) {
                        Segment seg = map.get(v+1);
                        map.put(v, seg);
                        seg.min--;
                    }
                    min = ((min == 0) || (min > v)) ? v : min;
                    max = ((max == 0) || (max < v)) ? v : max;
                }
                Segment seg = map.get(min);
                if(seg == null) {
                    return 1;
                }
                if(seg.min > 1) {
                    return 1;
                }
                return seg.max + 1;
            }
            int fmp3(int [] a) {
                if(a == null) {
                    return 1;
                }
                Set<Integer> set = new HashSet<>();
                Integer min = null;
                Integer max = null;
                for(int i = 0; i < a.length; i++) {
                    int v = a[i];
                    if(v <= 0 || set.contains(v)) {
                        continue;
                    }
                    min = (min == null || min > v) ? v : min;
                    max = (max == null || max < v) ? v : max;
                    set.add(v);
                }
                if(min == null || min > 1) {
                    return 1;
                }
                int ctr = 1;
                while(set.contains(ctr)) {
                    ctr++;
                }
                return ctr;
            }
            /**
             * idx      0 1 2 3 4
             * v        4 1 2 5 4
             *
             *          5     4         i = 0, swap = a[4-1] = a[3]
             *          4       5              v =
             *            1
             *          1 4
             *              2
             *            2 4
             *          1 2 4 4 5
             *
             */
            int fmp4(int [] a) {
                if(a == null) {
                    return 1;
                }
                Integer min = null;
                Integer max = null;
                int sz = a.length;
                //u.printArray(a);
                for(int i = 0; i < a.length; i++) {
                    int v = a[i];
                    if(v <= 0) {
                        continue;
                    }
                    min = (min == null || min > v) ? v : min;
                    max = (max == null || max < v) ? v : max;
                    if(v > sz) {
                        continue;
                    }
                    int swap = a[v-1];
                    while(a[v-1] != v) {

                    }
                    while(swap < sz && swap >= 0 && a[swap-1] != swap) {
                        swap = a[v-1];
                        a[v-1] = v;
                        a[i] = swap;
                        v = a[i];
                        //u.printArray(a);
                    }
                }
                if(min == null || min > 1) {
                    return 1;
                }
                //u.printArray(a);
                for(int i = 0; i < sz; i++) {
                    if(a[i] != (i+1)) {
                        return (i+1);
                    }
                }
                return sz+1;
            }
            int fmp(int [] a) {
                int type = 4;
                switch(type) {
                case 1: return fmp1(a);
                case 2: return fmp2(a);
                case 3: return fmp3(a);
                case 4: return fmp4(a);
                default: return fmp2(a);
                }
            }
        }
        Internal internal = new Internal();
        return internal.fmp(a);
    }

    /**
     * returns -1 if not found
     */
    public int binsearch(int [] a, int target, boolean isiterative) {
        if(isiterative) {
            return binsearchiterative(a, target);
        }
        return binsearchrecursive(a, target, 0, a.length-1);
    }

    public int binsearchiterative(int [] a, int target) {
        int idxs = 0;
        int idxe = a.length - 1;
        int idxm;
        while(idxs <= idxe) {
            idxm = idxs + (idxe - idxs)/2;
            int v = a[idxm];
            if(v == target) {
                return idxm;
            }
            if(v < target) {
                idxs = idxm + 1;
            }
            else {
                idxe = idxm - 1;
            }
        }
        return -1;
    }
    public int binsearchIterDuplicates(int [] a, int target, int edgeType) {
        /*
         * edgeType: 0 = any, 1 = left edge, 2 = right edge
         */
        for(int idxs = 0, idxe = a.length-1; idxs <= idxe; ) {
            int idxm = (idxs+idxe)/2;
            int v = a[idxm];
            if(edgeType == 1) { // left edge
                if(v == target) {
                    if(idxm == 0 || (a[idxm-1] < target)) {
                        return idxm;
                    }
                    idxe = idxm-1;
                }
                else if(v < target) {
                    idxs = idxm+1;
                }
                else {
                    idxe = idxm-1;
                }
            }
            else if(edgeType == 2) { // right edge
                if(v == target) {
                    if(idxm == (a.length-1) || (a[idxm+1] > target)) {
                        return idxm;
                    }
                    idxs = idxm+1;
                }
                else if(v < target) {
                    idxs = idxm+1;
                }
                else {
                    idxe = idxm-1;
                }
            }
            else {
                if(v == target) {
                    return idxm;
                }
                else if(v < target) {
                    idxs = idxm+1;
                }
                else {
                    idxe = idxm-1;
                }
            }
        }
        return -1;
    }
    public int binsearchrecursive(int [] a, int target, int idxs, int idxe) {
        if(idxs > idxe) {
            return -1;
        }
        int idxm = idxs + (idxe - idxs)/2;
        int v = a[idxm];
        if(v == target) {
            return idxm;
        }
        if(v < target) {
            return binsearchrecursive(a, target, idxm+1, idxe);
        }
        return binsearchrecursive(a, target, idxs, idxm-1);
    }

    /**
     * have a hashmap that gets randkey in constant time, and other
     * stuff in constant time too.
     */
    public static class MapRandom {
        HashMap<Integer, String> map = new HashMap<>();
        HashMap<Integer, Integer> mapIdxKey = new HashMap<>();
        HashMap<Integer, Integer> mapKeyIdx = new HashMap<>();
        Random rand = new Random();
        int ctr = 0;

        public void put(Integer k, String v) {
            if(map.containsKey(k)) {
                return;
            }
            map.put(k, v);
            mapIdxKey.put(ctr, k);
            mapKeyIdx.put(k, ctr);
            ctr++;
        }
        public void delete(Integer k) {
            if(!map.containsKey(k)) {
                return;
            }
            /*
             * take the idx of k
             * take last idx and its key
             * remove k
             * put last key into idx
             */
            Integer lastIdx = ctr-1;
            Integer lastKey = mapIdxKey.get(lastIdx);
            Integer keyIdx  = mapKeyIdx.get(k);
            mapKeyIdx.remove(k);
            mapIdxKey.remove(keyIdx);
            map.remove(k);

            // now set the lastkey to the deleted index, unless self
            if(k != lastKey) {
                mapKeyIdx.put(lastKey, keyIdx);
                mapIdxKey.put(keyIdx, lastKey);
            }
            ctr--;
        }
        public Integer randKey() {
            int idx = rand.nextInt(map.size());
            return mapIdxKey.get(idx);
        }
        public String get(Integer k) {
            return map.get(k);
        }
    }

    /**
     * given array of non negative integers, positioned at idx == 0.
     * each element represents max jump length at that position.
     * goal is to reach last idx in min number of jumps. return min
     * number of jumps.
     *
     * eg:
     * 2 3 1 1 4  min jumps is 2
     * ---                          1
     * -----                        1
     *   ---                        2
     *   -----                      2
     *   -------                    2   done
     *     ---                      3
     *       ---                    4
     *
     * eg
     * 3 3 1 4 1 1 2 1
     * --+                          1
     * ----+                        1
     * ------+                      1
     *   --+                        2
     *   ----+                      2
     *   ------+                    2
     *     --+                      2|3
     *       --+                    2|3
     *       ----+                  2|3
     *       ------+                2|3
     *       --------+              2|3 done @ 2
     *         --+
     *           --+
     *             --+
     */
    public int jumpGame(int [] a) {
        return 0;
    }

    /**
     * convert string to integer
     * this is only for int range, -/+
     * there may be preceding whitespace
     * after integer characters, there may be non int chars.
     * as soon as non int char after int char, ignore rest.
     * if out of range, return either min or max:
     *
     * min = -2147483648 =
     * max =  2147483647 =
     *
     * 0x7fff_ffff  = 2147483647
     * 0x8000_0000  =
     * 0xffff_ffff  = -1
     *
     */
    public int stringToInt(String s) {
        int sign = 1;
        int state = 0;
        long intval = 0;
        for(int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if(state == 0) {
                if(c == '-') {
                    sign = -1;
                    state = 1;
                }
                else if(c == '+') {
                    sign = 1;
                    state = 1;
                }
                else if(c >= '0' && c <= '9') {
                    intval = c - 48;
                    state = 1;
                }
                else if(c == ' ') {

                }
                else {
                    return 0;
                }
            }
            else if(state == 1) {
                if(c >= '0' && c <= '9') {
                   int tmp = c - 48;
                   intval = intval * 10 + tmp;
                   if(sign == 1 &&  intval >= 0x7fff_ffff) {
                       return 0x7fff_ffff;
                   }
                   if(sign == -1 && intval >= 2147483648L) {
                       return -2147483648;
                   }
                   if(sign == -1 && intval >= 0x7fff_ffff) {
                       return 0x8000_0001;
                   }
                } else {
                    break;
                }
            }
        }
        intval = intval * sign;
        return (int)intval;
    }
    /**
     * LRUCache cache = new LRUCache( 2  );
     * cache.put(1, 1);
     * cache.put(2, 2);
     * cache.get(1);       // returns 1
     * cache.put(3, 3);    // evicts key 2
     * cache.get(2);       // returns -1 (not found)
     * cache.put(4, 4);    // evicts key 1
     * cache.get(1);       // returns -1 (not found)
     * cache.get(3);       // returns 3
     * cache.get(4);       // returns 4
     *
     */
    public static class LRUCache {
        class Node {
            int k;
            int v;
            Node p;
            Node n;
            public Node(int k, int v) {
                this.k = k;
                this.v = v;
            }
        }
        int cap = 1;
        Node h = null;
        Node t = null;
        HashMap<Integer, Node> map = new HashMap<>();
        public LRUCache(int capacity) {
            this.cap = capacity;
        }
        public int get(int key) {
            Node n = map.get(key);
            if(n == null) {
                return -1;
            }
            if(n != h) {
                Node prv = n.p;
                Node nxt = n.n;
                prv.n = nxt;
                if(nxt != null) {
                    nxt.p = prv;
                }
                h.p = n;
                n.n = h;
                n.p = null;
                h = n;
                if(n == t) {
                    t = prv;
                }
            }
            return n.v;
        }
        public void put(int key, int value) {
            int v = get(key);
            if(v != -1) {
                // already put into front of q, so overwrite if needed
                Node nexisting = map.get(key);
                nexisting.v = value;
                return;
            }
            Node n = new Node(key, value);
            n.v = value;

            if(map.size() >= cap) {
                map.remove(t.k);
                if(h == t) {
                    h = null;
                    t = null;
                } else {
                    t.p.n = null;
                    t = t.p;
                }
            }
            map.put(key, n);
            if(h == null) {
                h = n;
                t = n;
            } else {
                h.p = n;
                n.n = h;
                h = n;
            }
        }
    }
    public int [] twoSum(int [] nums, int target) {
        int [] a = new int[2];
        HashMap<Integer, Integer> map = new HashMap<>();
        for(int i = 0; i < nums.length; i++) {
            int d = target - nums[i];
            Integer idx = map.get(d);
            if(idx != null) {
                a[0] = i;
                a[1] = idx;
                return a;
            }
            map.put(nums[i], i);
        }
        return a;
    }
    /**
     * given linked list, swap every two adjacent nodes and
     * return its head;
     *
     * 1,2,3,4 => 2,1,4,3
     *
     * use constant space. you may not modify the value,
     * only the pointers.
     *
     * 1,2,3,4,5,6  n1  n1.n    n2  n2.n    np  np.n    nh
     *              1   2       2   3       n   n       n
     *              1   3       2   1       1   3       2
     * 2,1,3,4,5,6  3   4       4   5       1   3       2
     *              3   5       4   3       3   5       2
     * 2,1,4,3,5,6  5   6       6   n       3   5       2
     *              5   n       6   5       5   n       2
     *
     */
    public ListNode swapPairs(ListNode head) {
        if(head == null) {
            return null;
        }
        if(head.next == null) {
            return head;
        }
        ListNode n1 = head;
        ListNode n2 = head.next;
        ListNode np = null;     // previous pair
        ListNode nh = null;     // head

        while(n2 != null) {
            ListNode nn = n2.next;

            // swap the two, eg n1.n=3.n=5, n2.n=4.n=3
            n1.next = n2.next;
            n2.next = n1;

            // if prv exist, then np=1, np.n=1.n=4
            if(np != null) {
                np.next = n2;
            }
            // update previous ptr
            np = n1;
            if(nh == null) {
                nh = n2;
            }

            n1 = nn;
            n2 = (nn == null) ? null : nn.next;
        }
        return nh;
    }

    public void swap(int [] a, int i, int j) {
        int t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    /**
     * given linked list, reverse nodes of linked list k
     * at a time and return its modified list.
     *
     * 1,2,3,4,5
     * for k = 1, 1,2,3,4,5
     * for k = 2, 2,1,3,4,5
     * for k = 3, 3,2,1,4,5
     * for k = 4, 4,3,2,1,5
     * for k = 5, 5,4,3,2,1
     */
    public ListNode reverseK(ListNode head, int k) {
        if(head == null) {
            return head;
        }
        // check if k is out of bounds
        {
            int ctr = 0;
            ListNode n = head;
            while(n != null) {
                n = n.next;
                ctr++;
            }
            if(k > ctr) {
                return head;
            }
        }
        ListNode h = head;
        ListNode t = head;
        ListNode c = head;
        ListNode n = head.next;
        for(int i = 1; i < k && n != null; i++) {
            c = n;
            if(n != null) {
                n = n.next;
            }
            t.next = c.next;
            c.next = h;
            h = c;
        }
        return h;
    }
    /**
     * given linked list, reverse nodes of linked list k
     * at a time and return its modified list.
     *
     * 1,2,3,4,5,6
     * for k = 1, 1,2,3,4,5,6
     * for k = 2, 2,1,4,3,6,5
     * for k = 3, 3,2,1,6,5,4
     * for k = 4, 4,3,2,1,5,6
     * for k = 5, 5,4,3,2,1,6
     */

    /*
     * num sets: eg
     *
     * k    sz      numiter
     * 3    3       1
     *      4       1
     *      6       2
     *      7       2
     *
     *  1,2,3,4,5,6,7,8,9
     *
     *  k = 3
     *  i   t   t   h   h   c   c   n   T       list                desc
     *          n       n       n
     * -------------------------------------------------------------------
     *  -   1   2   1   2   1   2   2   -       1,2,3,4,5,6,7,8,9
     *  1                                                           i++
     *                      2                                       c = n
     *                              3                               n = n.n
     *          3                                                   t.n = n
     *                          1                                   c.n = h
     *              2   1                                           h = c
     *                                          h t n
     *  1   1   3   2   1   2   1   3           2,1,3,4,5,6,7,8,9
     *  2                                                           i++
     *                      3                                       c = n
     *                              4                               n = n.n
     *          4                                                   t.n = n
     *                          2                                   c.n = h
     *              3   2                                           h = c
     *  2   1   4   3   2   3   2   4
     *                                          h   t n
     *                                          3 2 1 4 5 6 7 8 9
     *  3
     *
     *                                  -                           if(T != null)
     *                                                                  T.n = h
     *                                  1                           T = t
     *      4       4                                               t = n, h = n
     *                              5                               n = n.n
     *  1                                                           i++
     *                      5   6                                   c = n
     *                              6                               n = n.n
     *          6                                                   t.n = n
     *                          4                                   c.n = h
     *              5   4                                           h = c
     *                                              T h t n
     *  1   4   6   5   4   5   4   6   1       3 2 1 5 4 6 7 8 9
     *  2                                                           i++
     *                      6                                       c = n
     *                              7                               n = n.n
     *          7                                                   t.n = n
     *                          5                                   c.n = h
     *              6   5                                           h = c
     *                                              T h   t n
     *  2   4   7   6   5   6   5   7   1       3 2 1 6 5 4 7 8 9
     *  3
     *                                                              if(T != null)
     *                                                                  T.n = h
     *                                  4                           T = t
     *      7       7                                               t = n, h = n
     *                              8                               n = n.n
     *  1                                                           i++
     *                      8   9                                   c = n
     *                              9                               n = n.n
     *          9                                                   t.n = n
     *                          7                                   c.n = h
     *              8   7                                           h = c
     *                                                    T h t
     *  1   7   9   8   7   8   7   9   4       3 2 1 6 5 4 8 7 9
     *
     *
     */

    public ListNode reverseKGroup(ListNode head, int k) {
        if(head == null || k <= 1) {
            return head;
        }
        // check if k is out of bounds
        int sz = 0;
        for(ListNode n = head; n != null; n = n.next, sz++);
        if(k > sz) {
            return head;
        }
        int numIter = sz / k;
        ListNode tailLast = null;
        ListNode hglobal = null;
        ListNode h = head;
        ListNode t = head;
        ListNode n = head.next;
        for(int j = 0; j < numIter; j++) {
            for(int i = 1; i < k && n != null; i++) {
                ListNode c = n;
                n = (n == null) ? null : n.next;
                t.next = n;
                c.next = h;
                h = c;
            }
            if(tailLast != null) {
                tailLast.next = h;
            }
            tailLast = t;
            hglobal = (hglobal == null) ? h : hglobal;
            t = n;
            h = n;
            n = (n == null) ? null : n.next;
        }
        return hglobal;
    }
    /**
     * find the median of two sorted arrays.
     *
     *      0 1 2 3  4  5
     *  a1: 1,3,5,7, 9
     *  a2: 4,5,6,9,10,11
     *
     *  i1l i1r i1m i1v i2l i2r i2m i2v sum1l   sum2l    suml   sumr
     *  ----------------------------------------------------------------------
     *  0   4           0   5
     *          2   5
     *                  0   5   2   6
     *                  0   1
     *                  0   1   0   4
     *                  1   1
     *                  1   1   1   5
     *                                  3       2       5       7
     *  now is it i1l+1 or i2l+1? you have to do both
     *  or        i1l-1 or i2l-1?
     *  3   4   3   7
     *                  0   5   2   6   4       3       7
     *
     *                  3   5
     *                  3   5   4   10
     *                  3   4
     *                  3   4   3   9
     *
     *  0 1 2 3
     *  3/2 = 1,2
     *  0 1 2
     *  2/2 = 1
     */
    public double medianOfTwoSortedArrays(int [] a1, int [] a2, int method) {
        class C {
            double m1(int [] a1, int [] a2) {
                int [] as = (a1.length < a2.length) ? a1 : a2;
                int [] al = (a1.length < a2.length) ? a2 : a1;
                int szs = as.length;
                int szl = al.length;
                int sz = szs + szl;
                boolean isEven = sz % 2 == 0;
                int szSplit = (isEven) ? (sz/2) : (sz/2 + 1);
                double median = 0.0;
                // special cases where neither list overlaps
                // can still use general algos. so no need to write it,
                // even though run time is better.
                if(as[szs-1] < al[0] || al[szl-1] < as[0]) {
                    if(isEven) {
                        int v1 = al[szSplit];
                        int v2 = ((szSplit+1) < szl) ? al[szSplit+1] : as[0];
                        median = (v1+v2)/2.0;
                        return median;
                    }
                    else {
                        return al[szSplit];
                    }
                }

                if(isEven) {

                }
                else {

                }
                return median;
            }
            double m3(int [] a1, int [] a2) {
                int sz1 = a1.length;
                int sz2 = a2.length;
                int szA = sz1+sz2;
                int szH = szA/2;
                if(sz1 == 0 && sz2 == 0) {
                    return 0;
                }
                if(sz1 == 0) {
                    if(sz2 % 2 == 0) {
                        return (a2[sz2/2] + a2[1+sz2/2])/2.0;
                    }
                    return a2[sz2/2]/2.0;
                }
                if(sz2 == 0) {
                    if(sz1 % 2 == 0) {
                        return (a1[sz1/2] + a1[1+sz1/2])/2.0;
                    }
                    return a1[sz1/2]/2.0;
                }
                int idxs1 = 0;
                int idxe1 = sz1 - 1;
                while(idxs1 <= idxe1) {
                    int idxm1 = (idxs1+idxe1)/2;
                    int v1 = a1[idxm1];
                    int idxs2 = 0;
                    int idxe2 = sz2 - 1;
                    // where in a2 is a2[idxm2-1] <= v1 <= a2[idxm2+1] ?
                    while(idxs2 <= idxe2) {
                        int idxm2 = (idxs2+idxe2)/2;
                        int v2 = a2[idxm2];
                        //if()
                    }
                }
                return 0.0;
            }
            double m2(int [] a1, int [] a2) {
                /**
                 * remove the corner cases.
                 */
                int sz1 = a1.length;
                int sz2 = a2.length;
                int szA = sz1+sz2;
                int szH = szA/2;
                if(sz1 == 0 && sz2 == 0) {
                    return 0;
                }
                if(sz1 == 0) {
                    if(sz2 % 2 == 0) {
                        return (a2[sz2/2] + a2[1+sz2/2])/2.0;
                    }
                    return a2[sz2/2]/2.0;
                }
                if(sz2 == 0) {
                    if(sz1 % 2 == 0) {
                        return (a1[sz1/2] + a1[1+sz1/2])/2.0;
                    }
                    return a1[sz1/2]/2.0;
                }
                if(a1[sz1-1] < a2[0]) {
                    // all a1 vals smaller than all a2 vals
                    if(szA % 2 == 1) {
                        // sz1 == sz2 is invalid for odd number
                        if(sz1 > sz2) {
                            return a1[szH];
                        }
                        return a2[sz2-szH];
                    }
                    else {
                        // even
                        if(sz1 > sz2) {
                            return (a1[szH]+a1[szH+1])/2;
                        }
                        return (a2[sz2-szH]+a2[sz2-szH+1])/2;
                    }
                }
                if(a2[sz2-1] < a1[0]) {
                    // all a2 vals smaller than all a1 vals
                }
                return m1(a1, a2);
            }
            double m4(int [] a1, int [] a2) {
                int sz1 = a1.length;
                int sz2 = a2.length;
                int sz = sz1+sz2;
                boolean isodd = sz % 2 == 1;
                double median = 0.0;
                if(isodd) {
                    int middle = sz/2;
                    if(a1[0] > a2[sz2-1]) {
                        if(sz1 > middle) {
                            return a1[sz1-middle];
                        } else { // if(sz2 > middle) {
                            return a2[middle];
                        }
                    }
                    else if(a1[sz1-1] < a2[0]) {
                        if(sz1 > middle) {
                            return a1[middle];
                        } else { // if(sz2 > middle)
                            return a2[sz2-middle];
                        }
                    }
                    int idx1l = 0;
                    int idx1r = sz1-1;
                    int idx2l = 0;
                    int idx2r = sz2-1;
                    // find middle index
                    while(idx1l <= idx1r && idx2l <= idx2r) {
                        int idx1m = (idx1l+idx1r)/2;
                        int m1 = a1[idx1m];
                        // find a2[idx2m] < m1 < a2[idx2m+1] and
                        // get its index. if idx1m+idx2m == middle, then found
                        int idx2m = (idx2l+idx2r)/2;

                    }

                } else {
                    int middleL = sz/2;
                    int middleR = middleL+1;
                    if(a1[0] > a2[sz2-1]) {

                    }
                    else if(a1[sz1-1] < a2[0]) {

                    }

                }
                return median;
            }
            double m(int [] a1, int [] a2, int type) {
                switch(type) {
                case 1: return m1(a1, a2);
                case 2: return m2(a1, a2);
                case 3: return m3(a1, a2);
                case 4: return m4(a1, a2);
                }
                return m1(a1,a2);
            }
        }
        C c = new C();
        return c.m(a1, a2, method);
    }
    /**
     * given array of non negative integers, and you are positioned at 0,
     * reach the last index in minimum number of jumps. each value in array
     * represents max jump value
     *
     * use dp
     */
    public int jump2(int [] nums) {
        class Internal {
            public int j2(int [] nums) {
                int sz = nums.length;
                int [] a = new int[sz];
                for(int i = 0; i < sz; i++) {
                    int v = nums[i];
                    for(int j = 0; j < v; j++) {
                        int idx = i + j + 1;
                        if(idx >= sz || (a[idx] < (a[i]+1) && a[idx] != 0)) {
                            break;
                        }
                        a[idx] = a[i]+1;
                    }
                }
                return a[sz-1];
            }
            public int j4(int [] nums) {
                u.printArray(nums);
                int sz = nums.length;
                int [] a = new int[sz];
                LinkedList<Integer> lidx = new LinkedList<>();
                lidx.add(0);
                while(lidx.size() != 0) {
                    int i = lidx.poll();
                    int v = nums[i];
                    for(int j = 0; j < v; j++) {
                        int idx = i + j + 1;
                        if(idx >= sz || (a[idx] < (a[i]+1) && a[idx] != 0)) {
                            continue;
                        }
                        a[idx] = a[i]+1;
                        lidx.add(idx);
                    }
                }
                u.printArray(a);
                return a[sz-1];
            }
            public int j5(int [] nums) {
                int sz = nums.length;
                int [] a = new int[sz];
                LinkedList<Integer> lidx = new LinkedList<>();
                lidx.add(0);
                while(lidx.size() != 0) {
                    int i = lidx.poll();
                    int v = nums[i];
                    int max = 0;
                    for(int j = 0; j < v; j++) {
                        int idx = i + j + 1;
                        if(idx >= sz || (a[idx] < (a[i]+1) && a[idx] != 0)) {
                            continue;
                        }
                        a[idx] = a[i]+1;
                        max = (idx > max) ? idx : max;
                    }
                    if(max != 0) {
                        lidx.add(max);
                    }
                }
                u.printArray(a);
                return a[sz-1];
            }
            public int j6(int [] nums) {
                int sz = nums.length;
                if(sz < 2) {
                    return 0;
                }
                int ctr = 0;
                for(int idxbeg = 0, idxend = 1, idxmax = 0;
                        idxbeg < sz;) {
                    ctr++;
                    for(int j = idxbeg;
                            j < sz && j < idxend;
                            j++)
                    {
                        if(j == (sz-1)) {
                            return ctr;
                        }
                        int v = nums[j];
                        idxmax = (idxmax > (v+j)) ? idxmax : (v+j);
                        if(idxmax >= (sz-1)) {
                            return ctr;
                        }
                    }
                    idxbeg = idxend;
                    idxend = idxmax+1;
                }
                return ctr;
            }
            void j3helper(int [] nums, int [] a, int i) {
                int v = nums[i];
                for(int j = 0; j < v; j++) {
                    int idx = i + j + 1;

                    if(idx >= a.length ||
                        (a[idx] < (a[i]+1) && a[idx] != 0)
                    ) {
                        return;
                    }
                    a[idx] = a[i]+1;
                    j3helper(nums, a, idx);
                }
            }
            public int j3(int [] nums) {
                int sz = nums.length;
                int [] a = new int[sz];
                j3helper(nums, a, 0);
                return a[sz-1];
            }
            public int j(int [] nums) {
                int type = 6;
                switch(type) {
                case 2: return j2(nums);
                case 3: return j3(nums);
                case 4: return j4(nums);
                case 5: return j5(nums);
                case 6: return j6(nums);
                default: return j3(nums);
                }
            }
        }
        Internal internal = new Internal();
        return internal.j(nums);
    }
    /**
     * Given n pairs of parenthesis, generate all combinations of well
     * formed parenthesis.
     *
     * eg 3
     *  ((()))
     *  (()())
     *  (())()
     *  ()(())
     *  ()()()
     */
    public List<String> generateParenthesis(int n) {
        class Internal {

            List<String> gp(int n) {
                List<String> list = new ArrayList<>();
                char [] a = new char[n*2];
                gp(n, list, a, 0, 0, 0);
                return list;
            }
            void gp(int n, List<String> l, char [] a, int idx, int ctrO, int ctrC) {
                if(idx == a.length) {
                    String s = new String(a);
                    l.add(s);
                    return;
                }
                if(idx == 0) {
                    a[idx] = '(';
                    gp(n, l, a, idx+1, ctrO+1, ctrC);
                }
                else if(ctrO >= n) {
                    a[idx] = ')';
                    gp(n, l, a, idx+1, ctrO, ctrC+1);
                }
                else {
                    a[idx] = '(';
                    gp(n, l, a, idx+1, ctrO+1, ctrC);
                    if(ctrO > ctrC) {
                        a[idx] = ')';
                        gp(n, l, a, idx+1, ctrO, ctrC+1);
                    }
                }
            }
        }
        Internal internal = new Internal();
        return internal.gp(n);
    }
    public boolean validParenthesis(String s) {
        class Internal {
            /**
             *  0   1   2   3   4   5   6   7   8   9   10  11
             *  {   (   [   ]   {   }   [   (   )   ]   )   }
             *
             *  i   c   ip  cp  v1
             *  0   {   0   {   -1
             *  1   (   1   (   -1
             *  2   [   2   [   -1
             *  3   ]   1   (   -1
             *  4   {   4   {   1
             *  5   }   4   {   1
             *          1   (   -1
             *  6   [   6   [   1
             *  7   (   7   (   6,1
             *  8   )   2   (
             *  9   ]   1   (
             *  10  )   0   (
             *  11  }   -1  -
             *
             *  you can do in place, but seems like you still need an index stack
             */
            boolean vpInPlace(String s) {
                if(s.length() % 2 != 0) {
                    return false;
                }
                LinkedList<Integer> ll = new LinkedList<>();
                for(int i = 0; i < s.length(); i++) {
                    char c = s.charAt(i);
                    if(c == '(' || c == '[' || c == '{') {
                        ll.push(i);
                    }
                    else if(c == ')' || c == ']' || c == '}') {
                        if(ll.size() == 0) {
                            return false;
                        }
                        int idx = ll.pop();
                        char cp = s.charAt(idx);
                        if( cp == '(' && c != ')' ||
                            cp == '{' && c != '}' ||
                            cp == '[' && c != ']') {
                            return false;
                        }
                    }
                }
                return ll.size() == 0;
            }
            boolean vpLL(String s) {
                if(s.length() % 2 != 0) {
                    return false;
                }
                LinkedList<Character> ll = new LinkedList<>();
                for(int i = 0; i < s.length(); i++) {
                    char c = s.charAt(i);
                    if(c == '(' || c == '[' || c == '{') {
                        ll.push(c);
                    }
                    else if(c == ')' || c == ']' || c == '}') {
                        if(ll.size() == 0) {
                            return false;
                        }
                        char cpop = ll.pop();
                        if( cpop == '(' && c != ')' ||
                            cpop == '{' && c != '}' ||
                            cpop == '[' && c != ']') {
                            return false;
                        }
                    }
                    else {
                        return false;
                    }
                }
                return (ll.size() == 0);
            }
            boolean vp(String s) {
                int type = 1;
                switch(type) {
                case 1: return vpInPlace(s);
                default: return vpLL(s);
                }
            }
        }
        Internal internal = new Internal();
        return internal.vp(s);
    }
    /**
     * given digit string, return all possible combinations
     * that number can represent.
     */
    public List<String> letterCombinationsPhone(String s) {
        class Internal {
            Map<Character,String> map;
            public Internal() {
                map = new HashMap<>();
                map.put('2', "abc");
                map.put('3', "def");
                map.put('4', "ghi");
                map.put('5', "jkl");
                map.put('6', "mno");
                map.put('7', "pqrs");
                map.put('8', "tuv");
                map.put('9', "wxyz");
            }
            List<String> lcpRecurse(String s) {
                char [] ac = new char[s.length()];
                int idx = 0;
                for(int i = 0; i < s.length(); i++) {
                    char c = s.charAt(i);
                    String v = map.get(c);
                    if(v == null) {
                        continue;
                    }
                    ac[idx++] = c;
                }
                String sfiltered = new String(ac, 0, idx);
                List<String> l = new ArrayList<>();
                char [] buf = new char[sfiltered.length()];
                lcpr(s, 0, buf, 0, l);
                return l;
            }
            void lcpr(String s, int idxs, char [] buf, int idxbuf, List<String> l) {
                if(idxbuf == buf.length) {
                    String snew = new String(buf);
                    l.add(snew);
                    return;
                }
                if(idxs >= s.length()) {
                    return;
                }
                String mapval = map.get(s.charAt(idxs));
                for(int i = 0; i < mapval.length(); i++) {
                    buf[idxbuf] = mapval.charAt(i);
                    lcpr(s, idxs+1, buf, idxbuf+1, l);
                }
            }
            List<String> lcp(String s) {
                int type = 0;
                switch(type) {
                case 0: return lcpRecurse(s);
                default: return lcpRecurse(s);
                }
            }
        }
        Internal internal = new Internal();
        return internal.lcp(s);

    }
    /**
     * given array sorted in ascending order, and target, find
     * starting and ending position of target in the array. if
     * none, then return [-1,1].
     */
    public int [] searchRange(int [] a, int target) {
        class Internal {
            int [] search1(int [] a, int target) {
                int [] ret = new int[2];
                int idxs = 0;
                int idxe = a.length-1;
                // search left side
                {
                    while(idxs <= idxe) {
                        // idxs + (idxe-idxs)/2
                        // eg 10+(20-10)/2 = 10+10/2 = 10+5=15
                        //    (10+20)/2 == 15 == 10+(20-10)/2
                        int idxm = idxs+(idxe-idxs)/2;
                        int v = a[idxm];
                        if(v == target) {
                            if( (idxm == 0) ||
                                ((idxm-1) >= 0 && a[idxm-1] < target)
                            ) {
                                ret[0] = idxm;
                                idxs = idxm;
                                idxe = a.length-1;
                                break;
                            }
                            else {
                                idxe = idxm-1;
                            }
                        }
                        else if(v < target) {
                            if( (idxm+1) < a.length && a[idxm+1] == target) {
                                ret[0] = idxm+1;
                                idxs = idxm+1;
                                idxe = a.length-1;
                                break;
                            }
                            else {
                                idxs = idxm+1;
                            }
                        }
                        else {
                            idxe = idxm-1;
                        }
                    }
                    if(ret[0] == -1) {
                        return ret;
                    }
                }
                // search right side
                {
                    while(idxs <= idxe) {
                        // idxs+(idxe-idxs) = idxe which is wrong
                        int idxm = idxs+(idxe-idxs)/2;
                        int v = a[idxm];
                        if(v == target) {
                            if( (idxm == (a.length-1)) ||
                                ((idxm+1) < a.length-1 && a[idxm+1] > target)) {
                                ret[1] = idxm;
                                return ret;
                            }
                            else {
                                idxs = idxm+1;
                            }
                        }
                        else if(v > target) {
                            if( (idxm-1) >= 0 && a[idxm-1] == target) {
                                ret[1] = idxm-1;
                                return ret;
                            }
                            else {
                                idxe = idxm-1;
                            }
                        }
                        else {
                            idxs = idxm + 1;
                        }
                    }
                }
                ret[0] = -1;
                ret[1] = -1;
                return ret;
            }
            int [] search2(int [] a, int target) {
                int [] ret = {-1,-1};
                ret[0] = search2helper(a, target, true);
                ret[1] = search2helper(a, target, false);
                return ret;
            }
            int search2helper(int [] a, int target, boolean isleft) {
                int idxs = 0;
                int idxe = a.length-1;
                while(idxs <= idxe) {
                    int idxm = (idxs+idxe)/2; // idxs+(idxe-idxs)/2 is same
                    int v = a[idxm];
                    if(v == target) {
                        if(isleft) {
                            if(idxm == 0 || a[idxm-1] < target) {
                                return idxm;
                            }
                            idxe = idxm-1;
                        }
                        else { // right
                            if(idxm == (a.length-1) || a[idxm+1] > target) {
                                return idxm;
                            }
                            idxs = idxm+1;
                        }
                    }
                    else if(v < target) {
                        idxs = idxm+1;
                    }
                    else {
                        idxe = idxm-1;
                    }
                }
                return -1;
            }
            int [] search(int [] a, int target) {
                int type = 1;
                switch(type) {
                case 1: return search1(a, target);
                case 2: return search2(a, target);
                default: return search2(a, target);
                }
            }
        }
        Internal internal = new Internal();
        return internal.search(a, target);
    }
    /**
     * in rotated sorted array, find target index. no duplicates. -1 if not found.
     */
    int searchInRotatedSortedArray(int [] a, int target) {
        class Internal {
            /**
             * brute
             */
            int search0(int [] a, int target) {
                int prv = a[0];
                int idxb = -1;
                int ret = a[0];
                boolean found = false;
                for(int i = 0; i < a.length; i++) {
                    int v = a[i];
                    if(v == target) {
                        found = true;
                        ret = i;
                    }
                    if(v < prv) {
                        idxb = i;
                    }
                    prv = v;
                }
                if(idxb != -1) {

                }
                if(found) {
                    return ret;
                }
                return -1;
            }
            /**
             * search for rotate point, then do binary search
             * within that smaller range.
             */
            int search1(int [] a, int target) {
                if(a == null || a.length == 0) {
                    return -1;
                }
                int idxs = 0;
                int idxe = a.length-1;
                int idxm = 0;
                // only if its rotated
                if(a[idxe] < a[idxs]) {
                    while(idxs <= idxe) {
                        idxm = (idxs+idxe)/2;
                        int v = a[idxm];
                        if(v == target) {
                            return idxm;
                        }
                        if((idxm-1) >= idxs && a[idxm] < a[idxm-1]) {
                            idxs = idxm;
                            break;
                        }
                        if(v > a[idxe]) {
                            idxs = idxm+1;
                        }
                        else {
                            idxe = idxm-1;
                        }
                    }
                    if(target < a[0]) {
                        idxe = a.length-1;
                    }
                    else {
                        idxe = idxs-1;
                        idxs = 0;
                    }
                }
                while(idxs <= idxe) {
                   idxm = (idxs+idxe)/2;
                   int v = a[idxm];
                   if(v == target) {
                       return idxm;
                   }
                   else if(v < target) {
                       idxs = idxm+1;
                   }
                   else {
                       idxe = idxm-1;
                   }
                }
                return -1;
            }
            /**
             * this is a leet solution, much better
             */
            int searchLeetSolution(int [] a, int target) {
                int idxs = 0;
                int idxe = a.length-1;
                while(idxs < idxe) {
                    int idxm = (idxs+idxe)/2;
                    if(a[idxm] > a[idxe]) {
                        idxs = idxm+1;
                    }
                    else {
                        idxe = idxm;
                    }
                }
                int rot = idxs;
                idxs = 0;
                idxe = a.length-1;
                while(idxs <= idxe) {
                    int idxm = (idxs+idxe)/2;
                    int realmid = (idxm+rot)%a.length;
                    if(a[realmid] == target) {
                        return realmid;
                    }
                    if(a[realmid] < target) {
                        idxs = idxm+1;
                    }
                    else {
                        idxe = idxm-1;
                    }
                }
                return -1;
            }
            int search(int [] a, int target) {
                return search1(a, target);
            }
        }
        Internal internal = new Internal();
        return internal.search(a, target);
    }
    int searchInSortedArrayDuplicates(int [] a, int target) {
        class Internal {
            int search1(int [] a, int target) {
                return 0;
            }
            int search(int [] a, int target) {
                return 0;
            }
        }
        Internal internal = new Internal();
        return internal.search(a, target);
    }
    public static class SerialGeneratorI {
        int id = 0;
        int size = 0;
        List<Integer> lused = new ArrayList<>();
        LinkedList<Integer> llnext = new LinkedList<>();
        public int nextId() {
            if(llnext.size() != 0) {
                Integer id = llnext.pop();
                if(lused.get(id) != null) {
                    return -1;
                }
                lused.set(id, id);
                size++;
                return id;
            }
            int ret = id;
            id++;
            size++;
            return ret;
        }
        public boolean delete(int id) {
            if(id > lused.size()) {
                return false;
            }
            if(lused.get(id) == null) {
                return false;
            }
            lused.set(id, null);
            llnext.push(id);
            size--;
            return true;
        }
        public List<Integer> get() {
            List<Integer> l = new ArrayList<>();
            for(int i = 0; i < lused.size(); i++) {
                Integer v = lused.get(i);
                if(v != null) {
                    l.add(v);
                }
            }
            return l;
        }
        public int size() {
            return size;
        }
    }
    public static class SerialGeneratorII {
        class Range {
            int min; // inclusive
            int max; // inclusive
            public Range() {
                this(0, 0);
            }
            public Range(int min, int max) {
                this.min = min;
                this.max = max;
            }
        }
        int size = 0;
        LinkedList<Range> lrange = new LinkedList<>();
        // create a new item
        public int nextId() {
            int ret = 0;
            if(lrange.size() == 0) {
                Range range = new Range();
                ret = range.max;
                lrange.add(range);
            }
            else if(lrange.size() == 1){
                Range range = lrange.get(0);
                ret = ++range.max;
            }
            else {
                Range range = lrange.get(0);
                if(range.min == 0) {
                    ret = ++range.max;
                    Range range2 = lrange.get(1);
                    if((range.max+1) == range2.min) {
                        range.max = range2.max;
                        lrange.remove(1);
                    }
                }
                else {
                    ret = --range.min;
                }
            }
            size++;
            return ret;
        }
        public boolean exists(int id) {
            return true;
        }
        // delete item
        public boolean delete(int id) {
            if(lrange.size() == 0) {
                return false;
            }
            if(lrange.size() == 1) {
                Range r = lrange.get(0);
                if(r.min < id || id > r.max) {
                    return false;
                }
                else if(r.min == id) {
                    r.min++;
                }
                else if(r.max == id) {
                    r.max--;
                }
                else {
                    Range rnew = new Range(id+1, r.max);
                    r.max = id-1;
                    lrange.add(1, rnew);
                }
            }
            else {

            }
            size--;
            return true;
        }
        // get all active items in list
        public List<Integer> get() {
            List<Integer> l = new ArrayList<>();
            for(int i = 0; i < lrange.size(); i++) {
                Range range = lrange.get(i);
                for(int j = range.min; j <= range.max; j++) {
                    l.add(j);
                }
            }
            return l;
        }
        public int size() {
            return size;
        }
    }
    public int cmp(List<Integer> l1, List<Integer> l2) {
        if(l1 == null && l2 == null) {
            return 0;
        }
        else if(l1 != null && l2 == null || l1 == null && l2 != null) {
            return 2;
        }
        else if(l1.size() > l2.size()) {
            return 1;
        }
        else if(l1.size() < l2.size()) {
            return -1;
        }
        else if(l1.size() != l2.size()) {
            return 3;
        }
        for(int i = 0; i < l1.size(); i++) {
            if(l1.get(i) != l2.get(i)) {
                return -1;
            }
        }
        return 0;
    }
    public static class RandomGeneratorI {
        int id = 0;
        int size = 0;
        Random r = new Random();
        final int max;
        public RandomGeneratorI() {
            this(100);
        }
        public RandomGeneratorI(int max) {
            this.max = max;
        }
        private int rand() {
            return r.nextInt(max);
        }
        public int nextId() {
            return 0;
        }
        public boolean delete(int id) {
            return false;
        }
        public int size() {
            return size;
        }
    }

    /**
     * sort colors 0,1,2, in that order.
     * 2 1 0 2 1 0
     * 0         2
     *   0 1
     *       1 2
     * 0 0 1 1 2 2
     */
    public void sortColors(int [] a) {
        class Int {
            void sort1(int [] a) {
                int sz = a.length;
                int idx0 = 0;
                int idx2 = sz-1;
                for(int i = 0; i <= idx2 && idx0 < idx2; ) {
                    if      (a[i] == 0) {
                        swap(a, i, idx0++);
                    }
                    else if (a[i] == 2) {
                        swap(a, i, idx2--);
                    }
                    if(a[i] == 1 || i < idx0) {
                        i++;
                    }
                }
            }
            void swap(int [] a, int i, int j) {
                int v = a[i];
                a[i] = a[j];
                a[j] = v;
            }
            void sort2(int [] a) {
                int sz = a.length;
                int idx0 = 0, idx1 = 0, idx2 = 0;
                for(int i = 0; i < sz; i++) {
                    if(a[i] == 0) {
                        a[idx0++] = 0;
                        a[idx1++] = 1;
                        a[idx2++] = 2;
                    }
                    else if(a[i] == 1) {
                        a[idx1++] = 1;
                        a[idx2++] = 2;
                    }
                    else {
                        a[idx2++] = 2;
                    }
                }
            }
        }
        Int t = new Int();
        t.sort1(a);
    }
    /**
     * min window substring. given string s and string t,
     * find min window in s that has all characters in t.
     * t may have repetition.
     *
     * eg   s = ADOBECODEBANC
     *      t = ABC
     *      answer is BANC
     */
    public String minWindow(String s, String t) {
        class Obj {
            char c;
            int idx;
            Obj l;
            Obj r;
            public Obj(char c, int idx) {
                this.c = c;
                this.idx = idx;
            }
        }
        Map<Character, Obj> map = new HashMap<>();
        Set<Character> set = new HashSet<>();
        int min = -1;
        int max = -1;
        for(int i = 0; i < t.length(); i++) {
            set.add(t.charAt(i));
        }
        Obj head = null;
        Obj tail = null;
        int szlist = 0;
        int mindiff = -1;
        for(int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if(!set.contains(c)) {
                continue;
            }
            Obj o = map.get(c);
            if(o == null) {
                o = new Obj(c, i);
                if(head == null) {
                    head = o;
                    tail = o;
                }
                else {
                    tail.r = o;
                    o.l = tail;
                    tail = o;
                }
                map.put(c, o);
                szlist++;
            }
            else {
                o.idx = i;
                if(o == head && o == tail) {
                    // do nothing, since only 1 element
                }
                else if(o == head) {
                    head = head.r;
                    head.l = null;
                    tail.r = o;
                    o.l = tail;
                    o.r = null;
                    tail = o;
                }
                else if(o == tail) {
                    // do nothing
                }
                else {
                    // somewhere in middle. move to tail.
                    o.l.r = o.r;
                    o.r.l = o.l;
                    tail.r = o;
                    o.l = tail;
                    o.r = null;
                    tail = o;
                }
            }
            if(szlist == t.length()) {
                int diff = tail.idx - head.idx + 1;
                if(mindiff == -1 || mindiff > diff) {
                    mindiff = diff;
                    min = head.idx;
                    max = tail.idx;
                }
            }
        }
        if(min == -1 || max == -1) {
            return "";
        }
        String substring = s.substring(min, max+1);
        return substring;
    }
    /**
     * min window substring. given string s and string t,
     * find min window in s that has all characters in t.
     * t may have repetition.
     *
     * eg   s = ADOBECODEBANC
     *      t = ABC
     *      answer is BANC
     */
    public String minWindowWithDuplicates(String s, String t) {
        class Obj {
            char c;
            int idx;
            Obj l;
            Obj r;
            public Obj(char c, int idx) {
                this.c = c;
                this.idx = idx;
            }
        }
        Map<Character, Obj> map = new HashMap<>();
        Map<Character, Integer> mapCount = new HashMap<>();
        Set<Character> set = new HashSet<>();
        int min = -1;
        int max = -1;
        for(int i = 0; i < t.length(); i++) {
            set.add(t.charAt(i));
            Integer ctr = mapCount.get(t.charAt(i));
            if(ctr == null) {
                mapCount.put(t.charAt(i), 1);
            } else {
                mapCount.put(t.charAt(i), ctr + 1);
            }
        }
        Obj head = null;
        Obj tail = null;
        int szlist = 0;
        int mindiff = -1;
        for(int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if(!set.contains(c)) {
                continue;
            }
            Obj o = map.get(c);
            if(o == null) {
                o = new Obj(c, i);
                if(head == null) {
                    head = o;
                    tail = o;
                }
                else {
                    tail.r = o;
                    o.l = tail;
                    tail = o;
                }
                map.put(c, o);
                szlist++;
            }
            else {
                o.idx = i;
                if(o == head && o == tail) {
                    // do nothing, since only 1 element
                }
                else if(o == head) {
                    head = head.r;
                    head.l = null;
                    tail.r = o;
                    o.l = tail;
                    o.r = null;
                    tail = o;
                }
                else if(o == tail) {
                    // do nothing
                }
                else {
                    // somewhere in middle. move to tail.
                    o.l.r = o.r;
                    o.r.l = o.l;
                    tail.r = o;
                    o.l = tail;
                    o.r = null;
                    tail = o;
                }
            }
            if(szlist == t.length()) {
                int diff = tail.idx - head.idx + 1;
                if(mindiff == -1 || mindiff > diff) {
                    mindiff = diff;
                    min = head.idx;
                    max = tail.idx;
                }
            }
        }
        if(min == -1 || max == -1) {
            return "";
        }
        String substring = s.substring(min, max+1);
        return substring;
    }    /**
     * remove duplicates in place, and return the number of unique.
     *
     *
     *      i   v   idx     sz
     *      0   1   0       2
     *      1   2
     *      1   2
     *      2
     *
     */
    public int removeDuplicates(int [] a) {
        class Int {
            int rd1(int [] a) {
                int sz = a.length;
                if(sz == 0) {
                    return 0;
                }
                int idx = 1;
                int prv = 0;
                for(int i = 1; i < sz; ) {
                    if(a[i] == a[idx]) {
                        i++;
                    }
                    if(a[i] != a[idx]) {

                    }
                }
                return idx;
            }
            int rd2(int [] a) {
                int sz = a.length;
                int idx = 0;
                for(int i = 0; i < sz; ) {
                    int v = a[i];
                    if(v != a[idx]) {
                        i++;
                        idx++;
                    }
                    else {
                        // duplicate
                        while(i < sz && a[++i] == a[idx]);
                        if(i < sz) {
                            a[idx++] = a[i];
                        }
                    }
                }
                return idx;
            }
            int rd3(int [] a) {
                int sz = a.length;
                if(sz == 0) {
                    return 0;
                }
                int idx = 0;
                int prv = a[0];
                for(int i = 0; i < sz; ) {
                    int v = a[i];
                    idx++;
                    if(v != prv) {
                        a[idx] = v;
                    }
                    else {
                        while(prv == v) {
                            i++;
                            if(i >= sz) {
                                break;
                            }
                            v = a[i];
                        }
                        if(i < sz) {
                            a[idx] = v;
                        }
                    }
                    prv = v;
                }
                return idx;
            }
            int rd4(int [] a) {
                int sz = a.length;
                if(sz == 0) {
                    return 0;
                }
                int idx = 0;
                for(int j = 1; j < sz; j++) {
                    if(a[j] != a[idx]) {
                        a[++idx] = a[j];
                    }
                }
                return (idx+1);
            }
            int rd(int [] a) {
                int type = 3;
                if(type == 1) {
                    return rd1(a);
                }
                if(type == 2) {
                    return rd2(a);
                }
                if(type == 3) {
                    return rd3(a);
                }
                if(type == 4) {
                    return rd4(a);
                }
                return rd1(a);
            }
        }
        Int t = new Int();
        return t.rd(a);
    }
    /**
     * given array of integers, find 3 integers that is closest to target.
     * each list has exactly one solution.
     */
    public int threeSumClosest(int [] a, int target) {
        class Int {

        }
        return 0;
    }
    /**
     * '.' matches any single
     * '*' matches zero or more of preceding element
     * \.
     * \*
     * match should cover ENTIRE input string, not partial
     *
     * aa,a         false
     * aa,aa        true
     * aaa,aa       false
     * aa,a*        true
     * aa,.*        true
     * ab,.*        true
     * aab,c*a*b*   true
     * @param pat
     * @return
     */
    public boolean regexMatch(String s, String pat, int type) {
        /**
         * type = 1 no * matching, no escape matching
         * type = 2 no * matching, escape matching
         * type = 3 * matching, no escape matching
         * type = 4 * matching, escape matching
         */
        class Class {
            /**
             * no * matching, no escape matching
             */
            boolean rem1(String s, int idxs, String p, int idxp) {
                if(idxs == s.length() && idxp == p.length()) {
                    return true;
                }
                if(idxs != s.length() && idxp == p.length() ||
                   idxs == s.length() && idxp != p.length()) {
                    return false;
                }
                char cp = p.charAt(idxp);
                char cs = s.charAt(idxs);
                if(cp == '*') {
                    return false; // this method doesnt process *
                }
                if(cp == '.' || cp == cs) {
                    return rem1(s, idxs+1, p, idxp+1);
                }
                return false;
            }
            /**
             * no * matching, escape matching
             */
            boolean rem2(String s, int idxs, String p, int idxp) {
                if(idxs == s.length() && idxp == p.length()) {
                    return true;
                }
                if(idxs != s.length() && idxp == p.length() ||
                   idxs == s.length() && idxp != p.length()) {
                    return false;
                }
                char cp = p.charAt(idxp);
                char cs = s.charAt(idxs);
                if(cp == '*') {
                    if(idxp == 0 || idxs == 0) {
                        return false;
                    }
                    char cpp = p.charAt(idxp-1);
                    if(cpp == '\\' && cs == '*') {
                        return rem2(s, idxs+1, p, idxp+1);
                    }
                    return false;
                }
                else if(cp == '.') {
                    char cpp = (idxp-1) >= 0 ? p.charAt(idxp-1) : 0;
                    if(cpp != '\\') {
                        return rem2(s, idxs+1, p, idxp+1);
                    }
                    if(cpp == '\\' && cs == '.') {
                        return rem2(s, idxs+1, p, idxp+1);
                    }
                    return false;
                }
                else if(cp == cs) {
                    return rem2(s, idxs+1, p, idxp+1);
                }
                return false;
            }
            /**
             * no escape matching
             */
            boolean rem3(String s, int idxs, String p, int idxp) {
                if(idxs == s.length() && idxp == p.length()) {
                    return true;
                }
                if(idxs != s.length() && idxp == p.length()) {
                    return false;
                }
                if(idxs == s.length() && idxp != p.length()) {
                    char cpn = (idxp+1) < p.length() ? p.charAt(idxp+1) : 0;
                    if(cpn == '*') {
                        return rem3(s, idxs, p, idxp+2);
                    }
                    return false;
                }
                char cp = p.charAt(idxp);
                char cs = s.charAt(idxs);
                char cpn = (idxp+1) < p.length() ? p.charAt(idxp+1) : 0;

                if(cpn == '*') {
                    if(cp == '.' || cp == cs) {
                        boolean b = rem3(s, idxs+1, p, idxp);
                        if(b) {
                            return true;
                        }
                        b = rem3(s, idxs, p, idxp+2);
                        if(b) {
                            return true;
                        }
                        //b = rem3(s, idxs+1, p, idxp+2);
                        return false;
                    }
                    return rem3(s, idxs, p, idxp+2);
                }
                else {
                    if(cp == '.' || cp == cs) {
                        return rem3(s, idxs+1, p, idxp+1);
                    }
                    return false;
                }
            }
            /**
             * escape matching
             */
            boolean rem4(String s, int idxs, String p, int idxp) {
                return false;
            }
            boolean rem11(String s, int idxs, String p, int idxp) {
                if(idxs == s.length() && idxp == p.length()) {
                    return true;
                }
                if(idxs != s.length() && idxp == p.length() ||
                   idxs == s.length() && idxp != p.length()) {
                    return false;
                }
                char cp = p.charAt(idxp);
                char cs = s.charAt(idxs);
                char cpn = ((idxp+1) < p.length()) ? p.charAt(idxp+1) : 0;

                if(cp == '.') {
                }
                else if(cp == '*') {
                }
                else if(cp != cs) {
                    return false;
                }
                else {
                    return rem11(s, idxs+1, p, idxp+1);
                }
                return false;
            }
            /**
             * type = 1 no * matching, no escape matching
             * type = 2 no * matching, escape matching
             * type = 3 * matching, no escape matching
             * type = 4 * matching, escape matching
             */
            boolean rem(String s, String p, int type) {
                if(s == null && p == null) {
                    return true;
                }
                if(s != null && p == null) {
                    return false;
                }
                if(s == null && p != null) {
                    return false;
                }
                switch(type) {
                case 1: return rem1(s, 0, p, 0);
                case 2: return rem2(s, 0, p, 0);
                case 3: return rem3(s, 0, p, 0);
                case 4: return rem4(s, 0, p, 0);
                default: return rem1(s, 0, p, 0);
                }
            }
            /**
             * this is from leet as reference solution. close enough
             */
            public boolean isMatch(String text, String pattern) {
                if (pattern.isEmpty()) return text.isEmpty();
                boolean first_match = (!text.isEmpty() &&
                    (pattern.charAt(0) == text.charAt(0) || pattern.charAt(0) == '.'));

                if (pattern.length() >= 2 && pattern.charAt(1) == '*'){
                    return (isMatch(text, pattern.substring(2)) ||
                            (first_match && isMatch(text.substring(1), pattern)));
                } else {
                    return first_match && isMatch(text.substring(1), pattern.substring(1));
                }
            }
            /**
             * this is from leet as DP reference solution top bottom.
             */
            public boolean isMatchDPTopDn(String text, String pattern) {
                byte [][] memo = new byte[text.length() + 1][pattern.length() + 1];
                for(int i = 0; i < memo.length; i++) {
                    for(int j = 0; j < memo[0].length; j++) {
                        memo[i][j] = -1;
                    }
                }
                return dpTopDn(0, 0, text, pattern, memo);
            }

            public boolean dpTopDn(int i, int j, String text, String pattern, byte [][] memo) {
                if (memo[i][j] != -1) {
                    return memo[i][j] == 1;
                }
                boolean ans;
                if (j == pattern.length()){
                    ans = i == text.length();
                } else{
                    boolean first_match = (i < text.length() &&
                                           (pattern.charAt(j) == text.charAt(i) ||
                                            pattern.charAt(j) == '.'));

                    if (j + 1 < pattern.length() && pattern.charAt(j+1) == '*'){
                        ans = (dpTopDn(i, j+2, text, pattern, memo) ||
                               first_match && dpTopDn(i+1, j, text, pattern, memo));
                    } else {
                        ans = first_match && dpTopDn(i+1, j+1, text, pattern, memo);
                    }
                }
                memo[i][j] = (byte) (ans ? 1 : 0);
                return ans;
            }
            /**
             * this is from leet as DP reference solution bottom up.
             */
            public boolean isMatchDPBU(String text, String pattern) {
                boolean[][] dp = new boolean[text.length() + 1][pattern.length() + 1];
                dp[text.length()][pattern.length()] = true;

                for (int i = text.length(); i >= 0; i--){
                    for (int j = pattern.length() - 1; j >= 0; j--){
                        boolean first_match = (i < text.length() &&
                                               (pattern.charAt(j) == text.charAt(i) ||
                                                pattern.charAt(j) == '.'));
                        if (j + 1 < pattern.length() && pattern.charAt(j+1) == '*'){
                            dp[i][j] = dp[i][j+2] || first_match && dp[i+1][j];
                        } else {
                            dp[i][j] = first_match && dp[i+1][j+1];
                        }
                    }
                }
                return dp[0][0];
            }
        }
        Class c = new Class();
        return c.rem(s, pat, type);
    }
    /**
     * given signed int, reverse it
     *
     * 123      321
     * -123     -321
     * 120      21
     */
    int reverseInteger(int x) {
        long ret = 0;
        int sign = x < 0 ? -1 : 1;
        int v = (sign == 1) ? x : -x;
        while(v != 0) {
            int d = v % 10;
            ret = ret * 10 + d;
            v = (v-d) / 10;
        }
        if(x > 0 && ret > 0x7fff_ffff) {
            return 0;
        }
        if(x < 0 && (ret < -1 || ret > 0x7fff_ffff)) {
            return 0;
        }
        return (int)(ret*sign);
    }
    /**
     * return the idx of rotated sorted array
     *
     * eg   0,1,2 = 0
     *      1,2,0 = 2
     *      2,0,1 = 1
     */
    public int searchRotateIdx(int [] a) {
        int idxs = 0;
        int idxe = a.length-1;
        while(idxs <= idxe) { // && a[idxs] > a[idxe]) {
            int idxm = (idxs+idxe)/2;
            int v = a[idxm];
            //if((idxm-1) > idxs && a[idxm-1] < a[idxm]) {
            //    idxs = idxm-1;
            //}
            //if((idxm+1) <= idxe && a[idxm+1] < a[idxm]) {
            //    return (idxm+1);
            //}
            if((idxm-1) >= idxs && a[idxm-1] > a[idxm]) {
                return idxm;
            }
            if(v > a[idxe]) {
                idxs = idxm+1;
            }
            else {
                idxe = idxm-1;
            }
        }
        return idxs;
    }
    /**
     * find contiguous subarray which has largest sum.
     */
    public int maxSubArray(int [] a) {
        class Int {
            int m0(int [] a) {
                int [] asum = new int[a.length];
                int max = a[0];
                for(int i = 0; i < a.length; i++) {
                    int sum = 0;
                    for(int j = i; j < a.length; j++) {
                        sum += a[j];
                        asum[i] = (asum[i] > sum) ? asum[i] : sum;
                    }
                    max = (max > asum[i]) ? max : asum[i];
                }
                return max;
            }
            int m1(int [] a) {
                if(a == null || a.length == 0) {
                    return 0;
                }
                int max = a[0];
                int sumSubarray = a[0];
                int prv = a[0];
                for(int i = 1; i < a.length; i++) {
                    if(a[i] < 0) {
                        // i think this should be if(prv < 0) {....
                        if(a[i] > prv) { // prv < a[i] < 0
                            sumSubarray = a[i];
                        }
                        else {           // a[i] <= prv < 0 || a[i] < 0 < prv
                            sumSubarray += a[i];
                        }
                    }
                    else {
                        sumSubarray = (prv < 0) ? a[i] : a[i] + sumSubarray;
                    }
                    prv = sumSubarray;
                    max = (max > sumSubarray) ? max : sumSubarray;
                }
                return max;
            }
            int m2(int [] a) {
                int [] aleftright = new int[a.length];
                int [] arightleft = new int[a.length];
                int [] asum       = new int[a.length];
                int max = a[0];
                for(int i = 0; i < a.length; i++) {
                    aleftright[i] += a[i];
                }
                for(int i = a.length-1; i >= 0; i--) {
                    arightleft[i] += a[i];
                }
                /*
                 * 0 1 2 3 4 5 6 7 8 9
                 */
                return max;
            }
            int m3(int [] a) {
                if(a == null || a.length == 0) return 0;
                int max = 0;
                int sum = 0;
                for(int i = 0; i < a.length; i++) {
                    sum = (sum < 0) ? a[i] : sum + a[i];
                    max = max > sum ? max : sum;
                }
                return max;
            }
        }
        Int t = new Int();
        int brute = t.m0(a);
        int m1 = t.m3(a);
        if(brute != m1) {
            u.p("maxSubArray mismatch %d %d\n", brute, m1);
        }
        return t.m1(a);
    }
    /**
     * combo sum. given list of unique numbers, and a target sum,
     * find all unique combinations in list that equal sum.
     */
    public List<List<Integer>> combinationSum(int [] a, int target) {
        class C {
            int ctr = 0;
            void csum1(List<List<Integer>> ll, int [] a, int target, int idx, LinkedList<Integer> l) {
                ctr++;
                // get sum
                {
                    int sum = 0;
                    for(Integer v: l) {
                        sum += v;
                    }
                    if(sum == target) {
                        List<Integer> lnew = new ArrayList<>(l);
                        ll.add(lnew);
                        return;
                    }
                    if(sum > target) {
                        return;
                    }
                }
                // overflow check
                if(idx >= a.length) {
                    return;
                }
                for(int i = idx; i < a.length; i++) {
                    int v = a[i];
                    l.push(v);
                    // you can duplicate this unique number to equal target
                    csum1(ll, a, target, i, l);
                    l.pop();
                }
            }
            List<List<Integer>> csum(int [] a, int target) {
                List<List<Integer>> ll = new ArrayList<>();
                LinkedList<Integer> l = new LinkedList<>();
                csum1(ll, a, target, 0, l);
                return ll;
            }
        }
        C c = new C();
        return c.csum(a, target);
    }
    /**
     * input a has duplicates, but combination sum cannot use these duplicates.
     * i guess sort it first to skip over duplicates?
     */
    public List<List<Integer>> combinationSumUnique(int [] a, int target) {
        class C {
            int ctr = 0;
            void csum1(List<List<Integer>> ll, int [] a, int target, int idx, LinkedList<Integer> l) {
                ctr++;
                // get sum
                {
                    int sum = 0;
                    for(Integer v: l) {
                        sum += v;
                    }
                    if(sum == target) {
                        List<Integer> lnew = new ArrayList<>(l);
                        ll.add(lnew);
                        return;
                    }
                    if(sum > target) {
                        return;
                    }
                }
                // overflow check
                if(idx >= a.length) {
                    return;
                }
                for(int i = idx; i < a.length; i++) {
                    int v = a[i];
                    l.push(v);
                    // you can duplicate this unique number to equal target
                    csum1(ll, a, target, i, l);
                    l.pop();
                }
            }
            void qsort(int [] a, int idxs, int idxe) {
                int idxm = partition(a, idxs, idxe);
                qsort(a, idxs, idxm);
                qsort(a, idxm+1, idxe);
            }
            int partition(int [] a, int idxs, int idxe) {
                int idxm = (idxs+idxe)/2;
                int v = a[idxm];
                return idxm;
            }
            List<List<Integer>> csum(int [] a, int target) {
                List<List<Integer>> ll = new ArrayList<>();
                LinkedList<Integer> l = new LinkedList<>();
                csum1(ll, a, target, 0, l);
                return ll;
            }
        }
        C c = new C();
        return c.csum(a, target);
    }
    public void qsort(int [] a, boolean hasDuplicates) {
        class C {
            int ctr = 0;
            void qsortNoDup(int [] a, int idxs, int idxe) {
                ctr++;
                //if(ctr > 100) {
                //    return;
                //}
                if(idxs >= idxe) {
                    return;
                }
                //u.printArray(a);
                int idxm = partitionNoDup(a, idxs, idxe);
                //u.printArray(a);
                if(idxm == -1) {
                    return;
                }
                // exclude idxm, which is already in place
                qsortNoDup(a, idxs, idxm-1);
                qsortNoDup(a, idxm+1, idxe);
            }
            int partitionNoDup(int [] a, int idxs, int idxe) {
                if(idxs > idxe) {
                    return -1;
                }
                int idxm = (idxs+idxe)/2;
                int v = a[idxm];
                while(true) {
                    //while(idxs < idxe && a[idxs] < v) not needed
                    while(a[idxs] < v) {
                        idxs++;
                    }
                    //while(idxe > idxs && a[idxe] > v) not needed
                    while(a[idxe] > v) {
                        idxe--;
                    }
                    if(idxs >= idxe) {
                        break;
                    }
                    swap(a, idxs, idxe);
                }
                return idxs;
            }
            void swap(int [] a, int i, int j) {
                int v = a[i];
                a[i] = a[j];
                a[j] = v;
            }
            void qsortDup(int [] a, int idxs, int idxe) {
                ctr++;
                if(ctr > 100) {
                    return;
                }
                if(idxs >= idxe) {
                    return;
                }
                int idxm = partitionDup(a, idxs, idxe);
                if(idxm == -1) {
                    return;
                }
                // exclude idm, which is already in place
                qsortDup(a, idxs, idxm-1);
                qsortDup(a, idxm+1, idxe);
            }
            int partitionDup(int [] a, int idxs, int idxe) {
                if(idxs > idxe) {
                    return -1;
                }
                int idxm = (idxs+idxe)/2;
                int v = a[idxm];
                while(true) {
                    while(a[idxs] < v) {
                        idxs++;
                    }
                    while(a[idxe] > v) {
                        idxe--;
                    }
                    if(idxs >= idxe) {
                        break;
                    }
                    swap(a, idxs, idxe);
                    idxs++;
                    idxe--;
                }
                return idxs;
            }
            void qsort(int [] a, boolean hasDuplicates) {
                boolean runLibrary = false;
                ctr = 0;
                if(hasDuplicates) {
                    qsortDup(a, 0, a.length-1);
                } else {
                    qsortNoDup(a, 0, a.length-1);
                }
                if(runLibrary) {
                    qsortlibrary(a, hasDuplicates);
                }
            }
            void qsortlibrary(int [] a, boolean hasDuplicates) {
                List<Integer> l = new ArrayList<>();
                for(int i = 0; i < a.length; i++) {
                    l.add(a[i]);
                }
                Collections.sort(l);
                for(int i = 0; i < a.length; i++) {
                    a[i] = l.get(i);
                }
            }
        }
        C c = new C();
        c.qsort(a, hasDuplicates);
    }
    /**
     * given roman numeral, convert to integer.
     * input guaranteed in range 1:3999
     *
     * I            1
     * II           2
     * III          3
     * IV           4
     * V            5
     * VI           6
     * VII          7
     * VIII         8
     * IX           9
     * X            10
     * XV           15
     * XIX          19
     * XX           20
     * XL           40
     * L            50
     * XC           90
     * XCIX         99
     * C            100
     * CCC          300
     * CD           400
     * D            500
     * DC           600
     * DCCC         800
     * CM           900
     * CMXCIX       999
     * M            1000
     * MMMCMXCIX    3999
     *
     */
    public int romanToInt(String s, int type) {
        class C {
            int romanToIntForwardMap(String s) {
                Map<Character, Integer> map = new HashMap<>();
                map.put('i', 1);
                map.put('v', 5);
                map.put('x', 10);
                map.put('l', 50);
                map.put('c', 100);
                map.put('d', 500);
                map.put('m', 1000);
                int sum = 0;
                int prv = 0;
                String slc = s.toLowerCase();
                for(int i = 0; i < slc.length(); i++) {
                    Character c = slc.charAt(i);
                    Integer v = map.get(c);
                    if(v == null) {
                        return -1;
                    }
                }
                return -1;
            }
            int romanToIntBackwardMap(String s) {
                Map<Character, Integer> map = new HashMap<>();
                map.put('i', 1);
                map.put('v', 5);
                map.put('x', 10);
                map.put('l', 50);
                map.put('c', 100);
                map.put('d', 500);
                map.put('m', 1000);
                int sum = 0;
                int prv = 0;
                String slc = s.toLowerCase();
                for(int i = slc.length()-1; i >= 0; i--) {
                    Character c = slc.charAt(i);
                    Integer v = map.get(c);
                    if(v == null) {
                        return -1;
                    }
                    sum = (prv > v) ? (sum-v) : (sum+v);
                    prv = v;
                }
                return sum;
            }
            int romanToIntForward(String s) {
                int sum = 0;
                int subgroup = 0;
                String slc = s.toLowerCase();
                for(int i = 0; i < slc.length(); i++) {
                    char c = slc.charAt(i);
                    switch(c) {
                    case 'i':
                        subgroup += 1;
                        break;
                    case 'v':
                        if(subgroup == 1) {
                            sum += 4;
                            subgroup = 0;
                        }
                        else if(subgroup != 0) {
                            return -1;
                        }
                        else {
                            sum += 5;
                        }
                        break;
                    case 'x':
                        if(subgroup == 1) {
                            sum += 9;
                            subgroup = 0;
                        }
                        else if(subgroup != 0) {
                            return -1;
                        }
                        else {
                            subgroup += 10;
                        }
                        break;
                    case 'l':
                        if(subgroup == 10) {
                            sum += 40;
                            subgroup = 0;
                        }
                        else if(subgroup != 0) {
                            return -1;
                        }
                        else {
                            sum += 50;
                        }
                        break;
                    case 'c':
                        if(subgroup == 10) {
                            sum += 90;
                            subgroup = 0;
                        }
                        else if(subgroup != 0) {
                            return -1;
                        }
                        else {
                            subgroup += 100;
                        }
                        break;
                    case 'd':
                        if(subgroup == 100 ) {
                            sum += 400;
                            subgroup = 0;
                        }
                        else if(subgroup != 0) {
                            return -1;
                        }
                        else {
                            sum += 500;
                        }
                        break;
                    case 'm':
                        if(subgroup == 100) {
                            sum += 900;
                            subgroup = 0;
                        }
                        else {
                            subgroup += 1000;
                        }
                        break;
                    default:
                        p("unknown char %s\n", c);
                        return -1;
                    }
                }
                sum += subgroup;
                subgroup = 0;
                return sum;
            }
            int romanToIntBackward(String s) {
                int sum = 0;
                int subgroup = 0;
                String slc = s.toLowerCase();
                for(int i = slc.length()-1; i >= 0; i--) {
                    char c = slc.charAt(i);
                    switch(c) {
                    case 'i':
                        break;
                    case 'v':
                        break;
                    case 'x':
                        break;
                    case 'l':
                        break;
                    case 'c':
                        break;
                    case 'd':
                        break;
                    case 'm':
                        break;
                    default:
                        p("unknown char %s\n", c);
                        return -1;
                    }
                }
                sum += subgroup;
                subgroup = 0;
                return sum;
            }
            int f(String s, int type) {
                switch(type){
                case 1: return romanToIntForward(s);
                case 2: return romanToIntForwardMap(s);
                case 3: return romanToIntBackward(s);
                case 4: return romanToIntBackwardMap(s);
                default: return romanToIntForward(s);
                }
            }

        }
        C c = new C();
        return c.f(s, type);

    }
    /**
     * integer to roman in range 1:3999
     *
     *
     * I            1
     * II           2
     * III          3
     * IV           4
     * V            5
     * VI           6
     * VII          7
     * VIII         8
     * IX           9
     * X            10
     * XV           15
     * XIX          19
     * XX           20
     * XL           40
     * L            50
     * XC           90
     * XCI          91
     * XCII         92
     * XCIV         94
     * XCV          95
     * XCVII        97
     * XCVIII       98
     * XCIX         99
     * C            100
     * CCC          300
     * CD           400
     * D            500
     * DC           600
     * DCCC         800
     * CM           900
     * CMXCIX       999
     * M            1000
     * MMMCMXCIX    3999
     */
    public String intToRoman(int v, int type) {
        class C {
            String i2r2(int v) {
                int [] av    = {  1,   4,  5,   9, 10,  40, 50,  90,100, 400,500, 900,1000};
                String [] as = {"I","IV","V","IX","X","XL","L","XC","C","CD","D","CM", "M"};
                StringBuilder sb = new StringBuilder();
                for(int i = av.length; i >= 0; i--) {

                }
                return null;
            }
            String i2r3(int v) {
                int [] av    = {1000, 900,500, 400,100,  90, 50, 40,  10,   9,  5,   4,   1};
                String [] as = { "M","CM","D","CD","C","XC","L","XL","X","IX","V","IV", "I"};
                StringBuilder sb = new StringBuilder();
                for(int i = av.length; i >= 0; i--) {

                }
                return null;
            }
            String i2r1(int v) {
                Map<Integer, Character> map = new HashMap<>();
                map.put(1,'i');
                map.put(5,'v');
                map.put(10,'x');
                map.put(50,'l');
                map.put(100,'c');
                map.put(500,'d');
                map.put(1000,'m');
                StringBuilder sb = new StringBuilder();
                return null;
            }
            String i2r(int v, int type) {
                switch(type) {
                case 1: return i2r1(v);
                case 2: return i2r2(v);
                default: return i2r1(v);
                }
            }
        }
        C t = new C();
        return t.i2r(v, type);
    }
    /**
     * given nxn matrix, rotate 90 degrees (clockwise).
     *
     * 00 01 02         06 07 00
     * 07 08 03         05 08 01
     * 06 05 04         04 03 02
     *
     * 00 01 02 03      09 10 11 00
     * 11 12 13 04      08 15 12 01
     * 10 15 14 05      07 14 13 02
     * 09 08 07 06      06 05 04 03
     *
     * 09 10 11 00
     * 08 15 12 01
     * 07 14 13 02
     * 06 05 04 03
     *
     * 1,1 12
     *
     *   0  1  2  3  4  5
     *  19 20 21 22 23  6
     *  18 31 32 33 24  7
     *  17 30 35 34 25  8
     *  16 29 28 27 26  9
     *  15 14 13 12 11 10
     *
     *  15 16 17 18 19  0
     *  14 29 30 31 20  1
     *  13 28 35 32 21  2
     *  12 27 34 33 22  3
     *  11 26 25 24 23  4
     *  10  9  8  7  6  5
     *
     */
    public void rotateMatrix(int [][] a) {
        int max = a.length-1;
        int half = max/2;
        int idxmax = 0;
        for(int i = 0; i <= half; i++) {
            for(int j = i; j < (max-idxmax); j++) {
                int v           = a[i][j];
                a[i][j]         = a[max-j][i];
                a[max-j][i]     = a[max-i][max-j];
                a[max-i][max-j] = a[j][max-i];
                a[j][max-i]     = v;
            }
            idxmax++;
        }
    }

    /**
     * given set of distinct integers, return all possible subsets.
     * @param nums
     * @return
     */
    public List<List<Integer>> subsets(int [] nums) {
        class C {
            /**
             *  1 2 3
             *  -----
             *  1
             *  1 2
             *  1 2 3
             *  1   3
             *    2
             *    2 3
             *      3
             */
            void subsets1(List<List<Integer>> ll, int [] nums, int idx, LinkedList<Integer> l) {
                {
                    List<Integer> lnew = new ArrayList<>(l);
                    ll.add(lnew);
                }
                if(idx >= nums.length) {
                    return;
                }
                for(int i = idx; i < nums.length; i++) {
                    Integer v = nums[i];
                    l.push(v);
                    subsets1(ll, nums, i+1, l);
                    l.pop();
                }
            }
            List<List<Integer>> subsets(int [] nums, int type) {
                List<List<Integer>> ll = new ArrayList<>();
                LinkedList<Integer> l = new LinkedList<>();
                subsets1(ll, nums, 0, l);
                return ll;
            }
        }
        C test = new C();
        return test.subsets(nums, 1);
    }
    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode l;
        TreeNode r;
        TreeNode(int x) { val = x; }
    }
    public boolean isValidBST(TreeNode root) {
        class Ptr {
            TreeNode n = null;
        }
        class C {
            public boolean valid(TreeNode n, Ptr ptr) {
                if(n == null) {
                    return true;
                }
                if(!valid(n.left, ptr)) {
                    return false;
                }
                if(ptr.n != null && ptr.n.val >= n.val) {
                    return false;
                }
                ptr.n = n;
                if(!valid(n.right, ptr)) {
                    return false;
                }
                return true;
            }
            boolean isValidBST(TreeNode root) {
                Ptr ptr = new Ptr();
                return valid(root, ptr);
            }
        }
        C test = new C();
        return test.isValidBST(root);
    }
    public static class Interval {
        int start;
        int end;
        Interval() {
            start = 0;
            end = 0;
        }
        Interval(int s, int e) {
            start = s;
            end = e;
        }
    }
    public List<Interval> merge(List<Interval> intervals) {
        class C {
            List<Interval> merge(List<Interval> intervals) {
                LinkedList<Interval> l = new LinkedList<>();
                intervals.sort((i1, i2) -> Integer.compare(i1.start, i2.start));
                for(int i = 0; i < intervals.size(); i++) {
                    Interval v = intervals.get(i);
                    if(l.peekLast() != null && l.getLast().end >= v.start) {
                        l.getLast().end = (l.getLast().end > v.end) ? l.getLast().end : v.end;
                    } else {
                        l.add(v);
                    }
                }
                return l;
            }
        }
        C test = new C();
        return test.merge(intervals);
    }
    public void qsort(List<Interval> l) {
        class C {
            int ctr = 0;
            void qsort(List<Interval> l, int idxs, int idxe) {
                ctr++;
                if(ctr > 100) {
                    return;
                }
                if(idxs > idxe) {
                    return;
                }
                int idxm = partition(l, idxs, idxe);
                if(idxm == -1) {
                    return;
                }
                qsort(l, idxs, idxm-1);
                qsort(l, idxm+1, idxe);
            }
            int partition(List<Interval> l, int idxs, int idxe) {
                if(idxs > idxe) {
                    return -1;
                }
                int idxm = (idxs+idxe)/2;
                Interval v = l.get(idxm);
                while(true) {
                    while(l.get(idxs).start < v.start) {
                        idxs++;
                    }
                    while(idxe >= idxs && l.get(idxe).start > v.start) {
                        idxe--;
                    }
                    if(idxs >= idxe) {
                        break;
                    }
                    Interval swap = l.get(idxs);
                    l.set(idxs, l.get(idxe));
                    l.set(idxe, swap);
                }
                return idxs;
            }
        }
        C c = new C();
        c.qsort(l, 0, l.size()-1);
    }
    /**
     * given array of prices, find max profit with at most
     * one transaction (one buy and one sell).
     * The sell > buy, else do not do transaction.
     */
    public int maxProfit(int [] a) {
        return 0;
    }
    /**
     * given list of strings, group anagrams together.
     */
    public List<List<String>> groupAnagrams(String [] as) {
        List<List<String>> ll = new ArrayList<>();
        Map<String, List<String>> map = new HashMap<>();
        for(int i = 0; i < as.length; i++) {
            String s = as[i];
            char [] ac = s.toCharArray();
            Arrays.sort(ac);
            String s1 = new String(ac);
            List<String> l = map.get(s1);
            if(l == null) {
                l = new ArrayList<>();
                map.put(s1, l);
            }
            l.add(s);
        }
        for(Map.Entry<String, List<String>> kv: map.entrySet()) {
            ll.add(kv.getValue());
        }
        return ll;
    }
    /**
     * given mxn matrix, starting at 0,0, how many ways to go to
     * [m-1,n-1]?
     *
     * This is a DP problem (at least the way I think of it)
     *
     * 02 01
     * 01 01
     *
     * 06 03 01
     * 03 02 01
     * 01 01 01
     *
     * 20 10 04 01
     * 10 06 03 01
     * 04 03 02 01
     * 01 01 01 01
     *
     * 1    2    3    4    5    6    7    8    9    10
     * .   |.   |.   |.   |.   |.   |.   |.   |.   |.   |
     * .   |.   |.   |.   |..  |..  |..  |... |... |....|
     * .   |..  |... |....| ...| .. | .  |  ..|  . |   .|
     * ....| ...|  ..|   .|   .|  ..| ...|   .|  ..|   .|
     *
     * 11   12   13   14   15   16   17   18   19   20
     * ..  |..  |..  |..  |..  |..  |... |... |... |....|
     *  .  | .  | .  | .. | .. | ...|  ..|  . |  . |   .|
     *  .  | .. | ...|  . |  ..|   .|   .|  ..|  . |   .|
     *  ...|  ..|   .|  ..|   .|   .|   .|   .|  ..|   .|
     *
     * 70 35 15 05 01
     * 35 20 10 04 01
     * 15 10 06 03 01
     * 05 04 03 02 01
     * 01 01 01 01 01
     *
     */
    public int uniquePaths(int m, int n, int type) {
        class C {
            /*
             * this is leet solution
             */
            int uniquePaths(int m, int n) {
                int N = n + m - 2;// how much steps we need to do
                int k = m - 1; // number of steps that need to go down
                double res = 1;
                // here we calculate the total possible path number
                // Combination(N, k) = n! / (k!(n - k)!)
                // reduce the numerator and denominator and get
                // C = ( (n - k + 1) * (n - k + 2) * ... * n ) / k!
                for (int i = 1; i <= k; i++)
                    res = res * (N - k + i) / i;
                return (int)res;
            }
            /*
             * this is leet solution
             */
            public int uniquePaths3(int m, int n) {
                if(m == 1 || n == 1)
                    return 1;
                m--;
                n--;
                if(m < n) {              // Swap, so that m is the bigger number
                    m = m + n;
                    n = m - n;
                    m = m - n;
                }
                long res = 1;
                int j = 1;
                for(int i = m+1; i <= m+n; i++, j++){       // Instead of taking factorial, keep on multiply & divide
                    res *= i;
                    res /= j;
                }

                return (int)res;
            }
            /**
             * this is my solution
             * @param m
             * @param n
             * @return
             */
            int uniquePaths2(int m, int n) {
                int [][] aa = new int[m][n];
                for(int i = m-1; i >= 0; i--) {
                    for(int j = n-1; j >= 0; j--) {
                        if      (i == (m-1)) {
                            aa[i][j] = 1;
                        }
                        else if (j == (n-1)) {
                            aa[i][j] = 1;
                        }
                        else {
                            aa[i][j] = aa[i+1][j] + aa[i][j+1];
                        }
                    }
                }
                return aa[0][0];
            }
            int uniquePathDFS(int m, int n) {
                AtomicInteger ai = new AtomicInteger(0);
                uniquePathDFS(ai, m, n, 0, 0);
                return ai.get();
            }
            void uniquePathDFS(AtomicInteger ai, int m, int n, int i, int j) {
                if(i >= m || j >= n) {
                    return;
                }
                if(i == (m-1) && j == (n-1)) {
                    ai.incrementAndGet();
                    return;
                }
                uniquePathDFS(ai, m, n, i+1, j);
                uniquePathDFS(ai, m, n, i, j+1);
            }
        }
        C c = new C();
        switch(type) {
        case 1: return c.uniquePaths2(m,n);
        case 2: return c.uniquePathDFS(m,n);
        default: return c.uniquePaths2(m, n);
        }
    }
    /**
     * find total number of unique paths in grid, where
     * there are obstacles. if a[i][j] == 1, then obstacle, else none.
     * you can only go right or down.
     *
     * 0 0 0 0
     * 0 1 0 1
     * 0 0 0 0
     * 1 0 0 0
     *
     * 0 0 0
     * 0 0 0
     * 1 0 0
     *
     * .. |.. |
     *  . | ..|
     *  ..|  .|
     *
     * maybe this is also DP
     * but bottom up issue is it doesnt account for blocks. eg:
     *
     * 0 0 0 0
     * 0 0 0 1
     * 0 1 0 0
     * 0 0 0 0
     *
     * actually it does account for blocks.
     *
     *
     */
    public int uniquePathsWithObstacles(int [][] grid, int type) {
        class C {
            int dp1(int [][] grid) {
                int m = grid.length;
                int n = grid[0].length;
                int [][] a =  new int[m][n];
                for(int i = m-1; i >= 0; i--) {
                    for(int j = n-1; j >= 0; j--) {
                        if(i == (m-1) && j == (n-1)) {
                            a[i][j] = (grid[i][j] == 1) ? 0 : 1;
                        }
                        else if (i == (m-1)) {
                            a[i][j] =   (grid[i][j] == 1) ? 0 :
                                        (a[i][j+1] == 0) ? 0 : 1;
                        }
                        else if (j == (n-1)) {
                            a[i][j] =   (grid[i][j] == 1) ? 0 :
                                (a[i+1][j] == 0) ? 0 : 1;
                        }
                        else {
                            a[i][j] = (grid[i][j] == 1) ? 0 : a[i+1][j] + a[i][j+1];
                        }
                    }
                }
                return a[0][0];
            }
            int upDfs(int [][] grid) {
                AtomicInteger ai = new AtomicInteger(0);
                upDfs(grid, 0, 0, ai);
                return ai.get();
            }
            void upDfs(int [][] grid, int i, int j, AtomicInteger ai) {
                if(i >= grid.length || j >= grid[0].length || grid[i][j] == 1) {
                    return;
                }
                if(i == (grid.length-1) && j == (grid[0].length-1)) {
                    ai.incrementAndGet();
                    return;
                }
                upDfs(grid, i+1, j, ai);
                upDfs(grid, i, j+1, ai);
            }
        }
        C c = new C();
        switch(type) {
        case 1: return c.dp1(grid);
        default: return c.dp1(grid);
        }
    }
    /**
     * given array of non negative integers, you are positioned at 0.
     * each element represents max jump forward. is last index reachable?
     * @param a
     * @return
     */
    public boolean jumpGame2(int [] a) {
        return false;
    }
    public boolean isSymmetric(TreeNode root) {
        if(root == null) {
            return true;
        }
        class C {
            boolean is(TreeNode n1, TreeNode n2) {
                if(n1 == null && n2 == null) {
                    return true;
                }
                if(n1 == null && n2 != null || n1 != null && n2 == null) {
                    return false;
                }
                if(n1.val != n2.val) {
                    return false;
                }
                if(!is(n1.left, n2.right)) {
                    return false;
                }
                if(!is(n1.right, n2.left)) {
                    return false;
                }
                return true;
            }

        }
        C c = new C();
        return c.is(root.left, root.right);
    }
    /**
     * given 2D board of char, find if word exists in board.
     * word can be constructed by moving u,d,l,r. The same letter
     * cannot be used more than once.
     */
    public boolean wordSearch(char [][] board, String word) {
        class C {
            boolean e(char [][] board, boolean [][] ab, String w, int idx, int i, int j) {
                if(i < 0 || i >= board.length || j < 0 || j >= board[0].length) {
                    return false;
                }
                char c = w.charAt(idx);
                if(board[i][j] != c || ab[i][j] == true) {
                    return false;
                }
                if(idx == w.length()-1) {
                    return true;
                }
                ab[i][j] = true;
                boolean b;
                b = e(board, ab, w, idx+1, i-1, j)   ||
                    e(board, ab, w, idx+1, i+1, j)   ||
                    e(board, ab, w, idx+1, i, j-1) ||
                    e(board, ab, w, idx+1, i, j+1);
                ab[i][j] = false;
                return b;
            }
            public boolean exist(char[][] board, String word) {
                boolean [][] ab = new boolean[board.length][board[0].length];
                for(int i = 0; i < board.length; i++) {
                    for(int j = 0; j < board[0].length; j++) {
                        if(e(board, ab, word, 0, i, j)) {
                            return true;
                        }
                    }
                }
                return false;
            }
        }
        C c = new C();
        return c.exist(board, word);
    }
    public List<Integer> inorderTraversal(TreeNode r) {
        class C {
            void traverse(TreeNode n, List<Integer> l) {
                if(n == null) {
                    return;
                }
                traverse(n.left, l);
                l.add(n.val);
                traverse(n.right, l);
            }
            void traverseiter(TreeNode r, List<Integer> l) {
                /*
                *               50
                *       25              75
                *   15      35      65      85
                *
                *   25,50
                *
                */
                class State {
                    TreeNode n;
                    int state = 0; // 0 = left, 1 = right
                    public State(TreeNode n) {
                        this.n = n;
                    }
                }
                if(r == null) {
                    return;
                }
                LinkedList<State> ll = new LinkedList<>();
                ll.push(new State(r));
                while(ll.size() != 0) {
                    State s = ll.peek();
                    if(s.state == 0) {
                        s.state = 1;
                        if(s.n.left != null) {
                            ll.push(new State(s.n.left));
                        }
                        else {
                            ll.pop();
                            l.add(s.n.val);
                            if(s.n.right != null) {
                                ll.push(new State(s.n.right));
                            }
                        }
                    }
                    else {
                        ll.pop();
                        l.add(s.n.val);
                        if(s.n.right != null) {
                            ll.push(new State(s.n.right));
                        }
                    }
                }
            }
            public List<Integer> inorderTraversal(TreeNode root) {
                List<Integer> l = new ArrayList<>();
                traverseiter(root, l);
                return l;
            }
        }
        C c = new C();
        return c.inorderTraversal(r);
    }
    public List<List<Integer>> levelOrder(TreeNode root) {
        class C {
            List<List<Integer>> lo(TreeNode root) {
                List<List<Integer>> ll = new ArrayList<>();
                LinkedList<TreeNode> l1 = new LinkedList<>();
                LinkedList<TreeNode> l2 = new LinkedList<>();
                if(root == null) {
                    return ll;
                }
                l1.add(root);
                while(l1.size() != 0 || l2.size() != 0) {
                    List<Integer> li = new ArrayList<>();
                    while(l1.size() != 0) {
                        TreeNode n = l1.pop();
                        if(n.left != null) l2.add(n.left);
                        if(n.right != null) l2.add(n.right);
                        li.add(n.val);
                    }
                    if(li.size() != 0) {
                        ll.add(li);
                        li = new ArrayList<>();
                    }
                    while(l2.size() != 0) {
                        TreeNode n = l2.pop();
                        if(n.left != null) l1.add(n.left);
                        if(n.right != null) l1.add(n.right);
                        li.add(n.val);
                    }
                    if(li.size() != 0) {
                        ll.add(li);
                    }
                }
                return ll;
            }
            /**
             * this is from leet solution.
             */
            public List<List<Integer>> levelOrder(TreeNode root) {
                Queue<TreeNode> queue = new LinkedList<TreeNode>();
                List<List<Integer>> wrapList = new LinkedList<List<Integer>>();

                if(root == null) return wrapList;

                queue.offer(root);
                while(!queue.isEmpty()){
                    int levelNum = queue.size();
                    List<Integer> subList = new LinkedList<Integer>();
                    for(int i=0; i<levelNum; i++) {
                        if(queue.peek().left != null) queue.offer(queue.peek().left);
                        if(queue.peek().right != null) queue.offer(queue.peek().right);
                        subList.add(queue.poll().val);
                    }
                    wrapList.add(subList);
                }
                return wrapList;
            }
            /**
             * this is from leet solution
             */
            public List<List<Integer>> levelOrderDFS(TreeNode root) {
                List<List<Integer>> res = new ArrayList<List<Integer>>();
                levelHelper(res, root, 0);
                return res;
            }

            public void levelHelper(List<List<Integer>> res, TreeNode root, int height) {
                if (root == null) return;
                if (height >= res.size()) {
                    res.add(new LinkedList<Integer>());
                }
                res.get(height).add(root.val);
                levelHelper(res, root.left, height+1);
                levelHelper(res, root.right, height+1);
            }
        }
        C c = new C();
        return c.lo(root);
    }
    /**
     * given unsorted array of ints, find length of longest
     * consecutive elements sequence.
     * elements may not be unique.
     *
     * runtime should be O(n)
     *
     * eg
     * [100,4,200,1,3,2] = 4 because [1,2,3,4]
     */
    public int longestConsecutiveSequence(int [] nums) {
        class O {
            int min;
            int max;
            int v;
            public O(int v) {
                min = v;
                max = v;
                this.v = v;
            }
        }
        class C {
            int lcs(int [] a) {
                Map<Integer,O> map = new HashMap<>();
                int max = 0;
                for(int i = 0; i < a.length; i++) {
                    int v = a[i];
                    if(!map.containsKey(v)) {
                        O o = new O(v);
                        map.put(v,o);
                        O ol = map.get(v-1);
                        O or = map.get(v+1);
                        int diff = 1;
                        O otmp;
                        if(ol != null && or != null) {
                            // update left's max and right's min
                            otmp = map.get(ol.min);
                            otmp.max = or.max;
                            ol.max = or.max;

                            otmp = map.get(or.max);
                            otmp.min = ol.min;
                            or.min = ol.min;

                            diff = or.max-ol.min+1;
                        }
                        else if(ol != null) {
                            // update left's max
                            otmp = map.get(ol.min);
                            otmp.max = o.v;

                            ol.max = o.v;
                            o.min = ol.min;
                            diff = o.max-ol.min+1;
                        }
                        else if(or != null) {
                            // update right's min
                            otmp = map.get(or.max);
                            otmp.min = o.v;

                            o.max = or.max;
                            or.min = o.v;
                            diff = or.max-o.min+1;
                        }
                        max = (max > diff) ? max : diff;
                    }
                }
                return max;
            }
            /**
             * leet solution
             */
            public int longestConsecutive(int[] nums) {
                Set<Integer> num_set = new HashSet<Integer>();
                for (int num : nums) {
                    num_set.add(num);
                }

                int longestStreak = 0;

                for (int num : num_set) {
                    if (!num_set.contains(num-1)) {
                        int currentNum = num;
                        int currentStreak = 1;

                        while (num_set.contains(currentNum+1)) {
                            currentNum += 1;
                            currentStreak += 1;
                        }

                        longestStreak = Math.max(longestStreak, currentStreak);
                    }
                }

                return longestStreak;
            }
        }
        C c = new C();
        return c.lcs(nums);
    }
    public List<List<Integer>> permute(int[] nums) {
        class C {
            boolean [] ab;
            public List<List<Integer>> permute(int[] nums) {
                List<List<Integer>> ll = new ArrayList<>();
                LinkedList<Integer> l = new LinkedList<>();
                ab = new boolean[nums.length];
                permute(nums, ll, l);
                return ll;
            }
            void permute(int[] nums, List<List<Integer>> ll, LinkedList<Integer> l) {
                if(l.size() == nums.length) {
                    List<Integer> newlist = new ArrayList<>(l);
                    ll.add(newlist);
                    return;
                }
                for(int j = 0; j < nums.length; j++) {
                    if(ab[j]) {
                        continue;
                    }
                    ab[j] = true;
                    int v = nums[j];
                    l.push(v);
                    permute(nums, ll, l);
                    l.pop();
                    ab[j] = false;
                }
            }

        }
        C c = new C();
        return c.permute(nums);
    }
    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        ListNode h = null;
        ListNode n = null;
        ListNode c = null;
        while(true) {
            if(l1 == null && l2 == null) {
                break;
            }
            if(l1 != null && l2 != null) {
                if(l1.val <= l2.val) {
                    c = l1;
                    l1 = l1.next;
                }
                else {
                    c = l2;
                    l2 = l2.next;
                }
            }
            else if(l1 != null) {
                c = l1;
                l1 = l1.next;
            }
            else {
                c = l2;
                l2 = l2.next;
            }
            if(h == null) {
                h = c;
                n = c;
            }
            else {
                n.next = c;
                n = c;
            }
        }
        return h;
    }
    public int maxProfitStock(int[] prices) {
        if(prices.length == 0) {
            return 0;
        }
        int maxcur = 0;
        int prv = prices[0];
        int max = 0;

        for(int i = 1; i < prices.length; i++) {
            int cur = prices[i];
            int diff = cur - prv;
            maxcur = (maxcur + diff) < 0 ? 0 : (maxcur + diff);
            max = maxcur > max ? maxcur : max;
            prv = cur;
        }
        return max;
    }
    /**
     * maximum profit for unlimited buy and sell. but rule is
     * you must sell before you buy again.
     */
    public int maxProfitStock2(int[] prices) {
        return 0;
    }
    public int editDistance(String word1, String word2) {
        class C {
            int ed1(String word1, String word2) {
                int sz1 = word1.length();
                int sz2 = word2.length();
                if(sz1 == 0) {
                    return sz2;
                }
                if(sz2 == 0) {
                    return sz1;
                }
                int [][]a = new int[sz1+1][sz2+1];
                for(int i = 0; i <= sz1; i++) {
                    a[i][0] = i;
                }
                for(int i = 0; i <= sz2; i++) {
                    a[0][i] = i;
                }
                for(int i = 0; i < sz1; i++) {
                    for(int j = 0; j < sz2; j++) {
                        int cost = 0;
                        if(word1.charAt(i) != word2.charAt(j)) {
                            cost = 1;
                        }
                        int costIns = a[i+1][j] + 1;
                        int costDel = a[i][j+1] + 1;
                        int costSub = a[i][j] + cost;
                        int min = costIns;
                        min = (min < costDel) ? min : costDel;
                        min = (min < costSub) ? min : costSub;
                        a[i+1][j+1] = min;
                    }
                }
                return a[sz1][sz2];
             }
        }
        // FIXME: why doesnt this work???
        int sz1 = word1.length();
        int sz2 = word2.length();
        if(sz1 == 0 && sz2 == 0) {
            return 0;
        }
        else if(sz1 == 0) {
            return sz2;
        }
        else if(sz2 == 0) {
            return sz1;
        }
        int [][]a = new int[sz1][sz2];
        for(int i = 0; i < sz1; i++) {
            for(int j = 0; j < sz2; j++) {
                int cost = word1.charAt(i) != word2.charAt(j) ? 1 : 0;
                if(i == 0 && j == 0) {
                    a[i][j] = cost;
                }
                else if(i == 0) {
                    a[i][j] = a[i][j-1] + cost;
                }
                else if(j == 0) {
                    a[i][j] = a[i-1][j] + cost;
                }
                else {
                    int costIns = a[i][j-1] + 1;
                    int costDel = a[i-1][j] + 1;
                    int costSub = a[i-1][j-1] + cost;
                    a[i][j] = (costIns < costDel) ?
                        ((costIns < costSub) ? costIns : costSub) :
                        ((costDel < costSub) ? costDel : costSub);
                }
            }
        }
        return a[sz1-1][sz2-1];
    }
    public boolean canJump(int[] nums) {
        class C {
            boolean jump1(int [] nums) {
                if(nums.length <= 1) {
                    return true;
                }
                int max = 0;
                int i = 0;
                for(; i < nums.length; ) {
                    int cur = nums[i];
                    if(cur != 0) {
                        max = (i+cur) > max ? i+cur : max;
                    }
                    if(max >= (nums.length-1)) {
                        return true;
                    }
                    if(cur > 0 || i < max) {
                        i++;
                    } else {
                        break;
                    }
                }
                return false;
            }
            boolean jump2(int [] nums) {
                if(nums.length <= 1) {
                    return true;
                }
                int max = 0;
                for(int i = 0; i < nums.length; ) {
                    if(nums[i] != 0) {
                        max = (i+nums[i]) > max ? (i+nums[i]) : max;
                        i++;
                    }
                    else if(i < max) {
                        i++;
                    }
                    else {
                        break;
                    }
                }
                return (max >= (nums.length-1)) ? true : false;
            }
            /*
             * leet solution 1: backtracking
             * UNACCEPTED because cpu O(2^N)
             */
            public boolean canJumpFromPosition(int position, int[] nums) {
                if (position == nums.length - 1) {
                    return true;
                }

                int furthestJump = Math.min(position + nums[position], nums.length - 1);
                for (int nextPosition = position + 1; nextPosition <= furthestJump; nextPosition++) {
                    if (canJumpFromPosition(nextPosition, nums)) {
                        return true;
                    }
                }

                return false;
            }

            public boolean canJumpBacktrace(int[] nums) {
                return canJumpFromPosition(0, nums);
            }
            /*
             * leet solution 2: greedy
             */
            public boolean canJumpGreedy(int[] nums) {
                int lastPos = nums.length - 1;
                for (int i = nums.length - 1; i >= 0; i--) {
                    if (i + nums[i] >= lastPos) {
                        lastPos = i;
                    }
                }
                return lastPos == 0;
            }
            /*
             * leet solution 3: DP
             * UNACCEPTED because cpu O(n^2)
             */
            int [] memo;
            int GOOD = 0;
            int BAD = 1;
            int UNKNOWN = 2;
            public boolean canJumpFromPositionDP(int position, int[] nums) {
                if (memo[position] != UNKNOWN) {
                    return memo[position] == GOOD ? true : false;
                }

                int furthestJump = Math.min(position + nums[position], nums.length - 1);
                for (int nextPosition = position + 1; nextPosition <= furthestJump; nextPosition++) {
                    if (canJumpFromPosition(nextPosition, nums)) {
                        memo[position] = GOOD;
                        return true;
                    }
                }

                memo[position] = BAD;
                return false;
            }

            public boolean canJumpDP(int[] nums) {
                memo = new int[nums.length];
                for (int i = 0; i < memo.length; i++) {
                    memo[i] = UNKNOWN;
                }
                memo[memo.length - 1] = GOOD;
                return canJumpFromPositionDP(0, nums);
            }
            /*
             * leet user solution
             */
            public boolean canJumpUser(int A[], int n) {
                int i = 0;
                for (int reach = 0; i < n && i <= reach; ++i)
                    reach = (i+A[i]) < reach ? reach : (i+A[i]);
                return i == n;
            }
        }
        C c = new C();
        return c.jump1(nums);
    }
    /*
     * Given an array nums, there is a sliding window of size k which is moving from the very left of the array to the very right. You can only see the k numbers in the window. Each time the sliding window moves right by one position.
     * For example,
     * Given nums = [1,3,-1,-3,5,3,6,7], and k = 3.
     *
     * Window position                Max
     * ---------------               -----
     * [1  3  -1] -3  5  3  6  7       3
     *  1 [3  -1  -3] 5  3  6  7       3
     *  1  3 [-1  -3  5] 3  6  7       5
     *  1  3  -1 [-3  5  3] 6  7       5
     *  1  3  -1  -3 [5  3  6] 7       6
     *  1  3  -1  -3  5 [3  6  7]      7
     * Therefore, return the max sliding window as [3,3,5,5,6,7].
     *
     * 7 7 9 8 6 7 5 5 6 3 6 5 8 7
     * |       |                        9       9,8,7,6
     *   |       |                      9       9,8,7,7
     *     |       |                    9       9,8,7,6
     *       |       |                  8       8,7,6,5
     *         |       |                7
     *           |       |              6
     *             |       |            6
     *               |       |          6
     *                 |       |        8
     *                   |       |      8
     */
    public int [] maxSlidingWindow(int [] a, int k) {
        class C {
            int [] slide(int []a, int k, int type) {
                switch(type) {
                case 1: return slide1(a, k);
                default: return slide1(a, k);
                }
            }
            int [] slide1(int []a, int k) {
                int [] res = new int[a.length-k+1];
                if(a.length == 0) {
                    return res;
                }
                int [] idxmax = new int[3];
                int [] window = new int[k];
                for(int i = 0; i < k; i++) {
                    window[i] = a[i];
                    if(a[i] > a[idxmax[0]]) {
                        idxmax[0] = i;
                    }
                    else if(a[i] > a[idxmax[1]]) {
                        idxmax[1] = i;
                    }
                    if(a[idxmax[0]] < a[idxmax[1]]) {
                        int swapidx = idxmax[0];
                        idxmax[0] = idxmax[1];
                        idxmax[1] = swapidx;
                    }
                }

                for(int idxs = k, idxe = 0;
                        idxs < a.length;
                        idxe++, idxs++)
                {
                    int v = a[idxs];
                    if(idxmax[0] < idxe) {

                    }


                }
                return res;
            }
        }
        C c = new C();
        return c.slide(a, k, 1);
    }
    /**
     * given a list of [][x1,x2,y] building coordinates that
     * overlap, return a list of coordinates that trace the edges
     * of overlap.
     *
     * eg:
     *
     *
     * 15
     * 14
     * 13          +--------+
     * 12          |        |
     * 11          |        |
     * 10          |  +--------+
     * 09          |  |        |           +--------+
     * 08          |  |        |           |        |
     * 07       +--------+     |           |        |
     * 06       |        |     |     +--------+     |
     * 05       |        |     |     |        |     |
     * 04       |        |     |     |        |     |
     * 03       |        |     |     |        |     |
     * 02       |        |     |     |        |     |
     * 01       |        |     |     |        |     |
     * 00       |        |     |     |        |     |
     *   00 01 02 03 04 05 06 07 08 09 10 11 12 13 14 15
     *
     *   input list:
     *
     *   [02,05,07],
     *   [03,06,13],
     *   [04,07,10],
     *   [09,12,06],
     *   [11,14,09]
     *
     * 15
     * 14
     * 13          *--------*
     * 12          |        |
     * 11          |        |
     * 10          |        +--+
     * 09          |           |           *--------+
     * 08          |           |           |        |
     * 07       *--+           |           |        |
     * 06       |              |     *-----+        |
     * 05       |              |     |              |
     * 04       |              |     |              |
     * 03       |              |     |              |
     * 02       |              |     |              |
     * 01       |              |     |              |
     * 00       |              *-----+              *
     *   00 01 02 03 04 05 06 07 08 09 10 11 12 13 14 15
     *
     *   output list:
     *   [02,07],
     *   [03,13],
     *   [06,13],
     *   [07,00],
     *   [09,06],
     *   [11,09],
     *   [14,00]
     *
     * @param buildings
     * @return
     */
    public List<int[]> getSkyline(int [][] buildings) {
        List<int[]> l = new ArrayList<>();
        return l;
    }
    public int numIslands(byte [][] grid) {
        class C {
            int szr = 0;
            int szc = 0;
            public int numIslands(byte[][] grid, int type) {
                switch(type) {
                case 1: return numIslandsDfs(grid);
                default: return numIslandsBfs(grid);
                }
            }
            public int numIslandsBfs(byte[][] g) {
                class RC {
                    int r;
                    int c;
                    RC(int r, int c) {
                        this.r = r;
                        this.c = c;
                    }
                }
                LinkedList<RC> ll = new LinkedList<>();
                szr = g.length;
                if(szr == 0) {
                    return 0;
                }
                szc = g[0].length;
                int ctr = 0;
                byte [][] a = new byte[szr][szc];
                for(int i = 0; i < szr; i++) {
                    for(int j = 0; j < szc; j++) {
                        if(g[i][j] == 0 || a[i][j] == 1) {
                            continue;
                        }
                        a[i][j] = 1;
                        ll.add(new RC(i,j));
                        while(ll.size() != 0) {
                            RC rc = ll.poll();
                            int r = rc.r;
                            int c = rc.c;
                            if((r-1) >= 0 && a[r-1][c] == 0 && g[r-1][c] == 1) {
                                a[r-1][c] = 1;
                                ll.push(new RC(r-1,c));
                            }
                            if((r+1) < szr && a[r+1][c] == 0 && g[r+1][c] == 1) {
                                a[r+1][c] = 1;
                                ll.push(new RC(r+1,c));
                            }
                            if((c-1) >= 0 && a[r][c-1] == 0 && g[r][c-1] == 1) {
                                a[r][c-1] = 1;
                                ll.push(new RC(r,c-1));
                            }
                            if((c+1) < szc && a[r][c+1] == 0 && g[r][c+1] == 1) {
                                a[r][c+1] = 1;
                                ll.push(new RC(r,c+1));
                            }
                        }
                        ctr++;
                    }
                }
                return ctr;
            }
            public int numIslandsDfs(byte[][] grid) {
                szr = grid.length;
                if(szr == 0) {
                    return 0;
                }
                szc = grid[0].length;
                int ctr = 0;
                byte [][] a = new byte[szr][szc];
                for(int i = 0; i < szr; i++) {
                    for(int j = 0; j < szc; j++) {
                        if(grid[i][j] == 0 || a[i][j] == 1) {
                            continue;
                        }
                        dfs(grid, a, i, j);
                        ctr++;
                    }
                }
                return ctr;
            }
            void dfs(byte [][] grid, byte [][] a, int i, int j) {
                if(i < 0 || i >= szr || j < 0 || j >= szc || a[i][j] == 1) {
                    return;
                }
                a[i][j] = 1;
                if(grid[i][j] == 0) {
                    return;
                }
                dfs(grid, a, i-1,j);
                dfs(grid, a, i+1,j);
                dfs(grid, a, i,j-1);
                dfs(grid, a, i,j+1);
            }
        }
        C c = new C();
        return c.numIslands(grid, 2);
    }
    public TreeNode serdes(TreeNode r) {
        class C {
            String serialize(TreeNode r) {
                return null;
            }
            TreeNode deserialize(String s) {
                return null;
            }
        }
        C c = new C();
        String s = c.serialize(r);
        TreeNode n = c.deserialize(s);
        return n;
    }
    int maxrate(int [] ratings) {
        /*
         *      -1 -2 -3 -4 -5
         *      --------------
         *      -1 -2 -4 -6 -9
         *
         *
         */
        if(ratings.length == 0 || ratings[0] == 0) {
            return 0;
        }
        int sz = ratings[0];
        if(sz == 1) {
            return (ratings[0] > 0) ? ratings[0] : 0;
        }
        int [] max = new int[sz];
        for(int i = 0; i < sz; i++) {
            int v = ratings[i+1];
            if(i == 0) {
                max[i] = v;
            } else if(i == 1) {
                max[i] = (v > (v+max[0])) ? v : v+max[0];
            } else {
                max[i] = (v+max[i-1]) > (v+max[i-2]) ? (v+max[i-1]) : (v+max[i-2]);
            }
        }
        int best = (max[sz-1] > max[sz-2]) ? max[sz-1] : max[sz-2];
        return best;
    }
    String [] sortRomanNames(String [] a) {
        class C {
            String roman2intString(String s) {
                int sum = 0;
                int prv = 0;
                for(int i = s.length()-1; i >= 0; i--) {
                    char c = s.charAt(i);
                    int cur = 0;
                    switch(c) {
                    case 'I': cur = 1; break;
                    case 'V': cur = 5; break;
                    case 'X': cur = 10; break;
                    case 'L': cur = 50; break;
                    default: return null;
                    }
                    sum = (prv > cur) ? (sum-cur) : (sum+cur);
                    prv = cur;
                }
                String ret = String.format("%02d",sum);
                return ret;
            }
            void sort(String [] a) {

            }
            void sort(List<String> l) {
                Collections.sort(l);
            }
        }
        C c = new C();
        Map<String,String> map = new HashMap<>();
        List<String> l = new ArrayList<>();
        for(int i = 0; i < a.length; i++) {
            String [] pair = a[i].split("\\s+");
            String newname = pair[0] + c.roman2intString(pair[1]);
            map.put(newname, a[i]);
            l.add(newname);
        }
        /*
         * this sort does 1,10,11,2,21,22,3,..., not
         * 1,2,3,10,11,21,22.
         */
        c.sort(l);
        String [] res = new String[a.length];
        for(int i = 0; i < l.size(); i++) {
            res[i] = map.get(l.get(i));
        }
        return res;
    }
    String [] toArray(List<String> l, int type) {
        class C {
            String [] toarray1(List<String> l) {
                String [] a = new String[l.size()];
                l.toArray(a);
                return a;
            }
            String [] toarray2(List<String> l) {
                return (String [])l.toArray();
            }
            String [] toarray3(List<String> l) {
                String [] a = (String [])l.toArray().clone();
                return a;
            }
            String [] toarray4(List<String> l) {
                String [] a = new String[l.size()];
                l.toArray(a);
                String [] copy = a.clone();
                return copy;
            }
            String [] toarray5(List<String> l) {
                String [] a = new String[l.size()];
                l.toArray(a);
                String [] copy = new String[a.length];
                System.arraycopy(a, 0, copy, 0, a.length);
                return copy;
            }
            String [] toarray6(List<String> l) {
                String [] a = new String[l.size()];
                l.toArray(a);
                String [] copy = Arrays.copyOf(a, a.length);
                return copy;
            }
            String [] toarray(List<String> l, int type) {
                switch(type) {
                case 1: return toarray1(l);
                case 2: return toarray2(l);
                case 3: return toarray3(l);
                case 4: return toarray4(l);
                case 5: return toarray5(l);
                case 6: return toarray6(l);
                }
                return toarray1(l);
            }
        }
        C c = new C();
        return c.toarray(l, type);
    }
    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        class C {
            ListNode intersect1(ListNode headA, ListNode headB) {
                int sz0 = 0;
                int sz1 = 0;
                {
                    ListNode n = headA;
                    while(n != null) {
                        sz0++;
                        n = n.next;
                    }
                    n = headB;
                    while(n != null) {
                        sz1++;
                        n = n.next;
                    }
                }
                /*
                 * A A A A A
                 *           AB AB AB
                 *     B B B
                 *
                 * 5 - 3 = 2 for A.
                 */
                ListNode a = headA;
                ListNode b = headB;
                if(sz0 > sz1) {
                    int diff = sz0 - sz1;
                    for(int i = 0; i < diff; i++) {
                        a = a.next;
                    }
                } else {
                    int diff = sz1 - sz0;
                    for(int i = 0; i < diff; i++) {
                        b = b.next;
                    }
                }
                while(a != null && b != null) {
                    if(a == b) {
                        return a;
                    }
                    a = a.next;
                    b = b.next;
                }
                return null;
            }
            ListNode intersect2(ListNode headA, ListNode headB) {
                int sz0 = 0;
                int sz1 = 0;
                {
                    ListNode n = headA;
                    while(n != null) {
                        sz0++;
                        n = n.next;
                    }
                    n = headB;
                    while(n != null) {
                        sz1++;
                        n = n.next;
                    }
                }
                /*
                 * A A A A A
                 *           AB AB AB
                 *     B B B
                 *
                 * 5 - 3 = 2 for A.
                 */
                if(sz0 > sz1) {
                    int diff = sz0 - sz1;
                    ListNode n = headA;
                    ListNode m = headB;
                    for(int i = 0; i < diff; i++) {
                        n = n.next;
                    }
                    while(n != null && m != null) {
                        if(n == m) {
                            return n;
                        }
                        n = n.next;
                        m = m.next;
                    }
                } else {
                    int diff = sz1 - sz0;
                    ListNode n = headB;
                    ListNode m = headA;
                    for(int i = 0; i < diff; i++) {
                        n = n.next;
                    }
                    while(n != null && m != null) {
                        if(n == m) {
                            return n;
                        }
                        n = n.next;
                        m = m.next;
                    }
                }
                return null;
            }
            ListNode intersect(ListNode headA, ListNode headB, int type) {
                switch(type) {
                case 1: return intersect1(headA, headB);
                case 2: return intersect2(headA, headB);
                }
                return intersect1(headA, headB);
            }
        }
        C c = new C();
        return c.intersect(headA, headB, 1);
    }
    /**
     * given n non negative int where each represents point in
     * coordinate space, find the vertical lines drawn such that the two
     * endpoints traps the most water.
     *
     */
    public int containerMostWater(int [] h) {
        if(h.length == 0) {
            return 0;
        }
        int area = 0;
        int idxs = 0;
        int idxe = h.length-1;
        while(idxs < idxe) {
            int w = idxe-idxs+1;
            int hmax = (h[idxe] < h[idxs]) ? h[idxe] : h[idxs];
            int atmp = w*hmax;
            area = (area < atmp) ? atmp : area;
            if(h[idxe] < h[idxs]) {
                idxe--;
            } else {
                idxs++;
            }
        }
        return area;
    }
    public int houserobber(int [] nums) {
        if(nums.length == 0) {
            return 0;
        }
        if(nums.length == 1) {
            return nums[0];
        }
        int ret = 0;
        int [] max = new int[nums.length];
        for(int i = 0; i < nums.length; i++) {
            if(i == 0 || i == 1) {
                max[i] = nums[i];
            }
            else if(i == 2) {
                max[i] = nums[i] + nums[0];
            }
            else {
                max[i] = nums[i] + ((max[i-2] < max[i-3]) ? max[i-3] : max[i-2]);
            }
            ret = (ret > max[i]) ? ret : max[i];
        }
        return ret;
    }
    public int maxProductSubarray(int[] nums) {
        class C {
            int mp1(int [] nums) {
                if(nums.length == 0) {
                    return 0;
                }
                if(nums.length == 1) {
                    return nums[0];
                }
                int min = nums[0];
                int max = nums[0];
                int prd = 0;
                int prv = 0;
                int v = 0;
                for(int i = 0; i < nums.length; i++) {
                    v = nums[i];
                    {
                        int max1 = max;
                        int max2 = v * prv;
                        int max3 = v * prd;
                        int max4 = v;
                        int maxtmp1 = (max1 < max2) ? max2 : max1;
                        int maxtmp2 = (max3 < max4) ? max4 : max3;
                        max = (maxtmp1 < maxtmp2) ? maxtmp2 : maxtmp1;
                    }
                    if(prv == 0) {
                        prd = v;
                    } else {
                        prd = v * prd;
                    }
                    min = (prd < min) ? prd : min;
                    prv = v;
                }
                return (max > v) ? max : v;
            }
            /**
             *  6 3 -3 0 -2 4 6
             *  6                   6       6       6
             *  6 3                 18      3       3
             *  6 3 -3              -54     18      -54
             *  6 3 -3 0            0       18      -54
             *  6 3 -3 0 -2         -2      -2      0
             *  6 3 -3 0 -2 4       -8      -8      4
             *  6 3 -3 0 -2 4 6     -48     -48     24
             *
             */
            int mp2(int [] nums) {
                int max = nums[0];
                int tmin = nums[0];
                int tmax = nums[0];
                for(int i = 1; i < nums.length; i++) {
                    if(nums[i] < 0) {
                        int t = tmin;
                        tmin = tmax;
                        tmax = t;
                    }
                    int t1 = tmin * nums[i];
                    int t2 = tmax * nums[i];
                    tmin = (t1 < nums[i]) ? t1 : nums[i];
                    tmax = (t2 > nums[i]) ? t2 : nums[i];
                    if(tmin < tmax) {
                        max = (max > tmax) ? max : tmax;
                    } else {
                        max = (max > tmin) ? max : tmin;
                    }
                }
                return max;
            }
        }
        C c = new C();
        return c.mp2(nums);
    }
    public TreeNode constructBinTreePreOrderInOrder(int [] preorder, int [] inorder) {
        return null;
    }
    public boolean wordBreak(String s, List<String> wordDict) {
        return false;
    }
    /**
     *               .
     * .             .
     * . .         . .
     * . .     . . . .
     * . . . . . . . .
     * ---------------
     * 3 4 2 1 2 2 3 5
     *
     *
     * .
     * . .               .
     * . .     . .   . . .
     * . . . . . . . . . .
     * -------------------
     * 3 4 2 1 2 2 1 2 2 3
     *
     * .                 .
     * . .               .
     * . .     . .   . . .
     * . . . . . . . . . .
     * -------------------
     * 3 4 2 1 2 2 1 2 2 4
     *
     * .
     * . .     .
     * . .     . .   . .
     * . . . . . . . . . .
     * -------------------
     * 3 4 2 1 3 2 1 2 2 1
     *
     *                     .
     *     .     .         .
     *   . .     . .   . . .
     * . . . . . . . . . . .
     * ---------------------
     * 1 2 3 1 1 3 2 1 2 4 4
     *
     *
     *                   .
     *     .             .   .
     *   . .     .       .   .
     *   . . .   . .   . . . .
     * . . . . . . . . . . . .
     * ------------------------
     * 1 3 4 2 1 3 2 1 2 5 2 4
     *
     *
     */
    public int trapRainwater(int [] h, int t) {
        class C {
            int trapBrute(int [] a) {
                LinkedList<Integer> ll = new LinkedList<>();
                int area = 0;
                int prvh = 0;
                for(int i = 0; i < a.length; i++) {
                    int h = a[i];

                    prvh = h;
                }
                return area;
            }
            /**
             * this is leet solution.
             */
            int trapLeet1(int [] a) {
                int sz = a.length;
                if(sz == 0) {
                    return 0;
                }
                int l = 0;
                int r = sz-1;
                int area = 0;
                int lmax = 0;
                int rmax = 0;
                while(l <= r) {
                    int vl = a[l];
                    int vr = a[r];
                    lmax = (lmax < vl) ? vl : lmax;
                    rmax = (rmax < vr) ? vr : rmax;
                    if(lmax < rmax) {
                        area += (lmax - vl);
                        l++;
                    } else {
                        area += (rmax - vr);
                        r--;
                    }
                }
                return area;
            }
            /**
             *
             *                 .
             *     .           .     .
             *   . .     .     .   . .
             *   . . .   . .   . . . .
             * . . . . . . . . . . . .
             * -------------------------
             * 1 3 4 2 1 3 2 1 5 2 3 4
             *
             *
             *                                  l   r   vl  vr  lmax rmax area
             * |                     |          0   11  1   4   1    4
             *   |                              1   11  3   4   3    4
             *     |                            2   11  4   4   4    4
             *       |                          3   11  2   4   4    4    4-2=2
             *         |                        4   11  1   4   4    4    4-1 + prv = 3+2
             *           |                      5   11  3   4   4    4    4-3 + prv = 1+5
             *             |                    6   11  2   4   4    4    4-2 + prv = 2+6
             *               |                  7   11  1   4   4    4    4-1 + prv = 3+8
             *                 |                8   11  5   4   5    4    11
             *                     |            8   10  5   3   5    4    4-3 + prv = 1+11
             *                   |              8   9   5   2   5    4    4-2 + prv = 2+12
             *                 |
             *
             */
            int trap2(int [] a) {
                if(a == null || a.length <= 1) {
                    return 0;
                }
                int sz = a.length;
                int lmax = 0;
                int rmax = 0;
                int area = 0;
                for(int l = 0, r = sz-1; l < r; ) {
                    int vl = a[l];
                    int vr = a[r];
                    if(vl < vr) {
                        area += (lmax > vl) ? (lmax - vl) : 0;
                        lmax = (lmax > vl) ? lmax : vl;
                        l++;
                    } else {
                        area += (rmax > vr) ? (rmax - vr) : 0;
                        rmax = (rmax > vr) ? rmax : vr;
                        r--;
                    }
                }
                return area;
            }
            int trap(int [] h, int t) {
                switch(t) {
                case 1: return trapBrute(h);
                case 2: return trapLeet1(h);
                case 3: return trap2(h);
                }
                return 0;
            }
        }
        C c = new C();
        return c.trap(h, t);
    }
    /**
     * print something like this
     *
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
     *
     */
    public List<String> getHistogram(int [] a, boolean doPrint) {
        LinkedList<StringBuilder> ll = new LinkedList<>();
        List<String> l = new ArrayList<>();
        int szcol = a.length;
        StringBuilder rowZero = new StringBuilder();
        StringBuilder rowLine = new StringBuilder();
        StringBuilder rowVal = new StringBuilder();
        StringBuilder rowIdx = new StringBuilder();
        rowZero.append("00+");
        rowLine.append("  +");
        rowVal.append("  |");
        rowIdx.append("  |");
        int maxh = 0;
        for(int i = 0; i < szcol; i++) {
            int h = a[i];
            // if h bigger than max, add the missing rows and populate
            // up to point with idx|spaces.
            int szrow = ll.size();
            if(h > maxh) {
                int diff = h - maxh;
                for(int j = 0; j < diff; j++) {
                    StringBuilder sb = new StringBuilder();
                    int idx = szrow + j + 1;
                    sb.append(String.format("%02d|",idx));
                    for(int k = 0; k < i; k++) {
                        sb.append((k == 0) ? "  " : "   ");
                    }
                    ll.addFirst(sb);
                }
                maxh = h;
            }
            // now populate the rightmost with the height.
            szrow = ll.size();
            int currow = 0;
            for(StringBuilder sb: ll) {
                boolean isPop = (szrow-currow) > h ? false : true;
                if(isPop) {
                    sb.append((i == 0) ? " *" : "  *");
                } else {
                    sb.append((i == 0) ? "  " : "   ");
                }
                currow++;
            }
            rowZero.append("---");
            rowLine.append("---");
            rowVal.append(String.format("%2d ", h));
            rowIdx.append(String.format("%02d ", i));
        }
        StringBuilder topRow = new StringBuilder();
        topRow.append(String.format("%02d|", maxh+1));
        ll.addFirst(topRow);
        ll.addLast(rowZero);
        ll.addLast(rowIdx);
        ll.addLast(rowLine);
        ll.addLast(rowVal);
        ll.addLast(rowLine);
        for(StringBuilder sb: ll) {
            String s = sb.toString();
            if(doPrint) {
                p(s+"\n");
            }
            l.add(s);
        }
        return l;
    }
    public int maxRectangleMatrix(int [][] m) {
        return 0;
    }
    /**
     * support push(x), pop(), top(), getMin() in constant time
     *
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
    public static class MinStack {
        public static class MinStackC1 {
            LinkedList<Integer> ll = new LinkedList<>();
            LinkedList<Integer> llmin = new LinkedList<>();
            public MinStackC1() {
            }
            public void push(int x) {
                ll.push(x);
                if(llmin.size() == 0 || x <= llmin.peek()) {
                     llmin.push(x);
                }
            }
            public int pop() {
                if(ll.size() != 0) {
                    int v = ll.pop();
                    if(v == llmin.peek()) {
                        llmin.pop();
                    }
                    return v;
                }
                return 0;
            }
            public int top() {
                if(ll.size() == 0) {
                    return 0;
                }
                return ll.peek();
            }
            public int getMin() {
                if(ll.size() == 0) {
                    return 0;
                }
                if(llmin.size() == 0) {
                    return 0;
                }
                return llmin.peek();
            }
        }
        public static class MinStackC2 {
            /*
             * 4,7,2,6,9,8,7    min     v           ll
             *
             * push 7           7       7-7         0
             * push 8           7                   1,0
             * push 9           7                   2,1,0
             * push 6           6                   -1,2,1,0
             * push 2           2                   -4,-1,2,1,0
             * push 7           2                   5,-4,-1,2,1,0
             * push 4           2                   2,5,-4,-1,2,1,0
             *
             * pop 4            2       2+2=4       2,5,-4,-1,2,1,0
             *                  2                   5,-4,-1,2,1,0
             * pop 7            2       2+5=7       5,-4,-1,2,1,0
             *                  2                   -4,-1,2,1,0
             * pop 2            2       2           -4,-1,2,1,0
             *                  2+4=6               -1,2,1,0
             * pop 6            6       6           -1,2,1,0
             *                  6+1=7               2,1,0
             * pop 9            7       7+2=9       2,1,0
             *                                      1,0
             * pop 8            7       7+1         1,0
             *                                      0
             * pop 7            7       7           0
             *                                      -
             *
             */
            LinkedList<Long> ll = new LinkedList<>();
            long min = 0;
            public MinStackC2() {
            }
            public void push(int x) {
                long v;
                if(ll.size() == 0) {
                    v = 0;
                    min = x;
                }
                else if(x < min) {
                    v = x - min;
                    min = x;
                }
                else {
                    v = x - min;
                }
                ll.push(v);
            }
            public int pop() {
                if(ll.size() == 0) {
                    return 0;
                }
                long x = ll.pop();
                long v;
                if(x <= 0) {
                    v = (int)min;
                    min = min - x;
                } else  {
                    v = min + x;
                }
                return (int)v;
            }
            public int top() {
                if(ll.size() == 0) {
                    return 0;
                }
                long x = ll.peek();
                long v;
                if(x <= 0) {
                    v = min;
                } else {
                    v = min + x;
                }
                return (int)v;
            }
            public int getMin() {
                return (int)min;
            }
        }

    }
    public TreeNode lowestCommonAncestor(TreeNode r, TreeNode p, TreeNode q) {
        class C {
            TreeNode lca2(TreeNode r, TreeNode p) {
                if(r == null) {
                    return null;
                }
                if(r == p) {
                    return r;
                }
                TreeNode nl = lca2(r.left, p);
                TreeNode nr = lca2(r.right, p);
                if(nl != null) {
                    return nl;
                }
                return nr;
            }
            TreeNode lca(TreeNode r, TreeNode p, TreeNode q) {
                if(r == null) {
                    return null;
                }
                if(r == p) {
                    return r;
                }
                if(r == q) {
                    return q;
                }
                TreeNode nl = lca(r.left, p, q);
                TreeNode nr = lca(r.right, p, q);
                if(nl != null && nr != null) {
                    return r;
                }
                if(nl != null) {
                    return nl;
                }
                return nr;
            }
        }
        C c = new C();
        return c.lca(r, p, q);
    }
    public TreeNode lowestCommonAncestorBST(TreeNode r, TreeNode p, TreeNode q) {
        class C {
            TreeNode lca(TreeNode r, TreeNode p) {
                if(r == null) {
                    return null;
                }
                if      (r == p) {
                    return r;
                }
                else if (p.val < r.val) {
                    return lca(r.left, p);
                }
                return lca(r.right, p);
            }
            TreeNode lca2(TreeNode r, TreeNode p, TreeNode q) {
                if(r == null) {
                    return null;
                }
                TreeNode rp = null;
                TreeNode rq = null;
                if      (r == p) {
                    rp = r;
                    rq = lca(r, q);
                }
                else if (r == q) {
                    rp = lca(r, p);
                    rq = r;
                }
                else {
                    rp = lca(r, p);
                    rq = lca(r, q);
                }
                if(rp != null && rq != null) {
                    return r;
                }
                else if(rq != null) {
                    return rq;
                }
                return rp;
            }
            TreeNode lca(TreeNode r, TreeNode p, TreeNode q) {
                if(r == null) {
                    return null;
                }
                if      (p.val < r.val && q.val < r.val) {
                    return lca(r.left, p, q);
                }
                else if (p.val > r.val && q.val > r.val) {
                    return lca(r.right, p, q);
                }
                else if (p.val < r.val && q.val > r.val) {
                    return r;
                }
                else if (p.val > r.val && q.val < r.val) {
                    return r;
                }
                else if (r == p && q.val < r.val) {
                    return r;
                }
                else if (r == p && q.val > r.val) {
                    return r;
                }
                else if (r == q && p.val < r.val) {
                    return r;
                }
                else if (r == q && r.val > r.val) {
                    return r;
                }
                return null;
            }
        }
        C c = new C();
        return c.lca(r, p, q);
    }
    /**
     * You are given coins of different denominations and a total amount of
     * money amount. Write a function to compute the fewest number of coins
     * that you need to make up that amount. If that amount of money cannot
     * be made up by any combination of the coins, return -1. coins are
     * infinite.
     */
    public int coinChange(int [] coins, int amount) {
        class C {
            List<Integer> lbest = new ArrayList<>();
            LinkedList<Integer> ll = new LinkedList<>();
            int cc1(int [] c, int a, int ctr) {
                if(a == 0) {
                    return ctr;
                }
                int minctr = -1;
                int ctrtmp = 0;
                boolean found = false;
                for(int i = c.length-1; i >= 0; i--) {
                    if(c[i] <= a) {
                        ll.push(c[i]);
                        ctrtmp = cc1(c, a-c[i], ctr+1);
                        if(ctrtmp >= 0) {
                            found = true;
                            if(minctr == -1 || minctr > ctrtmp) {
                                minctr = ctrtmp;
                                lbest = new ArrayList<>(ll);
                                //u.printArray(lbest);
                            }
                            break;
                        }
                        ll.pop();
                    }
                }
                if(!found) {
                    return -1;
                }
                return minctr;
            }
            int cc2(int [] c, int a) {
                int sz = c.length;
                if(sz == 0) {
                    return 0;
                }
                int [] min = new int[a+1];
                /*
                 * for each value, what is the min number of coins?
                 * if not possible for change, return -1.
                 */
                for(int i = 0; i <= a; i++) {
                    min[i] = -1;
                    for(int j = sz-1; j >= 0; j--) {
                        int coin = c[j];
                        int diff = i - coin;
                        if(diff == 0) {
                            min[i] = 1;
                        } else if(diff > 0) {
                            if(min[diff] != -1) {
                                int total = min[diff] + 1;
                                min[i] = (min[i] != -1 && min[i] < total) ? min[i] : total;
                            }
                        }
                    }
                }
                return min[a];
            }
            /**
             * leet solution.
             */
            int cc3(int [] coins, int amount) {
                if(amount<1) return 0;
                int[] dp = new int[amount+1];
                int sum = 0;

                while(++sum<=amount) {
                    int min = -1;
                    for(int coin : coins) {
                        if(sum >= coin && dp[sum-coin]!=-1) {
                            int temp = dp[sum-coin]+1;
                            min = min<0 ? temp : (temp < min ? temp : min);
                        }
                    }
                    dp[sum] = min;
                }
                return dp[amount];
            }
            /**
             * leet solution.
             */
            int cc4(int [] coins, int amount) {
                if(amount==0)
                    return 0;
                int n = amount+1;
                for(int coin : coins) {
                    int curr = 0;
                    if (amount >= coin) {
                        int next = coinChange(coins, amount-coin);
                        if(next >= 0)
                            curr = 1+next;
                    }
                    if(curr > 0)
                        n = Math.min(n,curr);
                }
                int finalCount = (n==amount+1) ? -1 : n;
                return finalCount;
            }
        }
        C c = new C();
        java.util.Arrays.sort(coins);
        return c.cc2(coins, amount);
    }
    /**
     * find max square (only nxn) in matrix
     *
     * 1 0 1 0 0
     * 1 1 1 1 1
     * 0 1 1 1 1
     * 1 1 1 1 0
     *
     * returns 9
     */
    public int maxSquare(char [][] matrix) {
        class C {
            int maxsquare(char [][] m) {
                int area = 0;
                int maxw = 0;
                int maxr = m.length;
                if(maxr == 0) {
                    return 0;
                }
                int maxc = m[0].length;
                for(int i = 0; i < maxr; i++) {
                    for(int j = 0; j < maxc; j++) {
                        if(m[i][j] == '1') {
                            int szr = i;
                            for(; szr < maxr && m[szr][j] == '1'; szr++);
                            szr = szr - i;
                            int szc = j;
                            for(; szc < maxc && m[i][szc] == '1'; szc++);
                            szc = szc - j;
                            int w = (szr < szc) ? szr : szc;
                            int areamaybe = w * w;
                            if(areamaybe <= area) {
                                continue;
                            }
                            // now do actual area checking
                            int minr = 0;
                            int minc = w;
                            boolean stop = false;
                            for(szr = i; szr < (i+w) && !stop; szr++) {
                                minr++;
                                int curc = 0;
                                for(szc = j; szc < (j+w) && !stop; szc++) {
                                    curc++;
                                    if(m[szr][szc] == '0') {
                                        minc = ((curc-1) < minc) ? (curc-1) : minc;
                                        if(minc < maxw) {
                                            stop = true;
                                        }
                                    }
                                }
                                int minw = (minc < minr) ? minc : minr;
                                int areacur = minw * minw;
                                area = (area < areacur) ? areacur : area;
                            }
                        }
                    }
                }
                return area;
            }
        }
        C c = new C();
        return c.maxsquare(matrix);
    }
    public void printIntegers(int beg, int end) {
            p(" number   binary   hex      num 1s\n");
            p("|--------|--------|--------|--------|\n");

        for(int i = beg; i <= end; i++) {
            int num1 = 0;
            for(int j = 0; j < 32; j++) {
                int b = (i >> j) & 1;
                if(b == 1) {
                    num1++;
                }
            }
            p(" %8d|%8s|0x%06x|%d\n", i, Integer.toBinaryString(i), i, num1);
        }
    }
    /**
     * s = "3[a]2[bc]",     return "aaabcbc".
     * s = "3[a2[c]]",      return "accaccacc".
     * s = "2[abc]3[cd]ef", return "abcabccdcdcdef".
     * s = "a10[ab]3[cd]ef", return "aababababababababababcdcdcdef".
     *
     *
     */
    public String decodeString(String s) {
        class C {
            int idx = 0;
            String decode(String s) {
                StringBuilder sb = new StringBuilder();
                int n = 0;
                char cprv = 0;
                while(idx < s.length()) {
                    char c = s.charAt(idx);
                    idx++;
                    if(c >= '0' && c <= '9') {
                        int tmpn = c - '0';
                        if(cprv >= '0' && c <= '9') {
                            n = n*10 + tmpn;
                        } else {
                            n = tmpn;
                        }
                    } else if(c == '[') {
                        String substr = decode(s);
                        for(int i = 0; i < n; i++) {
                            sb.append(substr);
                        }
                        n = 0;
                    } else if(c == ']') {
                        return sb.toString();
                    } else {
                        sb.append(c);
                    }
                    cprv = c;
                }
                return sb.toString();
            }
        }
        C c = new C();
        return c.decode(s);
    }
    /**
     * quicksort with duplicates example
     *
     * 00 01 02 03 04 05 06 07 08 09
     * -----------------------------
     * 02,04,07,10,10,14,14,14,17,19        original
     *
     * 17,07,10,14,04,10,14,19,02,14        shuffled
     *
     * b                           e        partition 00:09
     *              m                   mid = 9/2 = 4, value of 4
     * |                       |        swap
     * 02                      17
     *    |        |                    swap
     *    04       07
     * 02 04 10 14 07 10 14 19 17 14    return idx 1
     *
     * b  m  m+1                  e         partition 00:01  done
     *       b                    e         partition 02:09
     *                m                 mid = (9+2)/2 = 5, value of 10
     *       |           |              swap equal with m+1 and shift m
     *       14          10
     *                   m
     *       |                    |     swap, but identical swap
     *       14                   14    move e to left to force forward
     *                                  progress if same
     *       |           |              swap
     *       10          14             no better than before. this algorithm
     *                                  does not work.
     *
     * NEW ALGORITHM HANDLING DUPLICATES.
     * The trick is to force moving indices. the above algos did not move
     * indices after swap.
     *
     * 00 01 02 03 04 05 06 07 08 09
     * -----------------------------
     * 02,04,07,10,10,14,14,14,17,19        original
     *
     * 17,07,10,14,04,10,14,19,02,14        shuffled
     *
     * @param a
     */
    public void quicksort(int [] a, boolean handleDuplicates) {
        class CNoDup {
            int maxctr = 0;
            void sort(int [] a) {
                sort(a, 0, a.length);
            }
            void sort(int [] a, int lo, int hi) {
                {
                    if(maxctr > 1000) {
                        return;
                    }
                    maxctr++;
                }
                if(lo < hi) {
                    int md = partition(a, lo, hi);
                    sort(a, lo, md-1);
                    sort(a, hi, md+1);
                }
            }
            int partition(int [] a, int lo, int hi) {
                if(lo >= hi) {
                    return -1;
                }
                int ilo = lo;
                int ihi = hi;
                int md = (lo+hi)/2;
                int pivot = a[md];
                while(true) {
                    while(a[lo] < pivot) ilo++;
                    while(a[hi] > pivot) ihi--;
                    if(ilo < ihi) {
                        int tmp = a[ilo];
                        a[ilo] = a[ihi];
                        a[ihi] = tmp;
                    } else {
                        return ihi;
                    }
                }
            }
        }
        class CDup {
            int maxctr = 0;
            void sort(int [] a) {
                sort(a, 0, a.length);
            }
            void sort(int [] a, int lo, int hi) {
                {
                    if(maxctr > 1000) {
                        return;
                    }
                    maxctr++;
                }
                if(lo < hi) {
                    int md = partition(a, lo, hi);
                    sort(a, lo, md-1);
                    sort(a, hi, md+1);
                }
            }
            int partition(int [] a, int lo, int hi) {
                if(lo >= hi) {
                    return -1;
                }
                int ilo = lo-1;     // this is the difference, preinc/predec
                int ihi = hi+1;
                int md = (lo+hi)/2;
                int pivot = a[md];
                while(true) {
                    while(a[++ilo] < pivot);
                    while(a[--ihi] > pivot);
                    if(ilo >= ihi) {
                        return ihi;
                    } else {
                        int tmp = a[ilo];
                        a[ilo] = a[ihi];
                        a[ihi] = tmp;
                    }
                }
            }
        }
        if(handleDuplicates) {
            CDup c = new CDup();
            c.sort(a);
        } else {
            CNoDup c = new CNoDup();
            c.sort(a);
        }
    }
    /**
     * find area of largest rectangle in histogram:
     *
     * ------------------------------
     * case 1: 2 3 4 5 6
     *
     *  07
     *  06          .
     *  05        . .
     *  04      . . .
     *  03    . . . .
     *  02  . . . . .
     *  01  . . . . .
     *      0 1 2 3 4
     *
     *
     * ------------------------------
     *  case 2: 6 5 4 3 2 1
     *
     *  06  .
     *  05  . .
     *  04  . . .
     *  03  . . . .
     *  02  . . . . .
     *  01  . . . . . .
     *      0 1 2 3 4 5
     *
     * ------------------------------
     *  case 3: 3 2 5 3 0 4 3 3 2
     *
     *  07
     *  06
     *  05      .
     *  04      .     .
     *  03  .   . .   . . .
     *  02  . . . .   . . . .
     *  01  . . . .   . . . .
     *      0 1 2 3 4 5 6 7 8
     *
     * ------------------------------
     *  case 4: 3 2 5 3 1 4 3 3 2 1
     *
     *  07
     *  06
     *  05      .
     *  04      .     .
     *  03  .   . .   . . .
     *  02  . . . .   . . . .
     *  01  . . . . . . . . . .
     *      0 1 2 3 4 5 6 7 8 9
     *
     *  case 5: 2 1 5 6 2 3
     *
     *  07
     *  06        .
     *  05      . .
     *  04      . .
     *  03      . .   .
     *  02  .   . . . .
     *  01  . . . . . .
     *      0 1 2 3 4 5
     *
     */
    public int areaLargestRectangeHistogram(int [] h, int type) {
        class C {
            int largest(int [] h) {
                return -1;
            }
            int largestBrute(int [] a) {
                int area = 0;
                for(int i = 0; i < a.length; i++) {
                    int tmp = a[i];
                    for(int j = i-1; j >= 0; j--)
                        if(a[j] >= a[i])
                            tmp += a[i];
                        else
                            break;
                    for(int j = i+1; j < a.length; j++)
                        if(a[j] >= a[i])
                            tmp += a[i];
                        else
                            break;
                    area = area > tmp ? area : tmp;
                }
                return area;
            }
            int largestStack(int [] a) {
                int area = 0;
                LinkedList<Integer> ll = new LinkedList<>();
                for(int i = 0; i < a.length; i++) {

                }
                return area;
            }
            int largestLeet(int [] a) {
                int sz = a.length;
                if(sz == 0) {
                    return 0;
                }
                LinkedList<Integer> ll = new LinkedList<>();
                int area = 0;
                /*
                 * keep pushing if cur height > height in stack.
                 * else if cur height <= height in stack
                 *      pop
                 *      calculate the area from cur to height in stack
                 *      backtrack
                 */
                for(int i = 0; i <= sz; i++) {
                    int h = (i == sz) ? 0 : a[i];
                    int idxPeek = (ll.size() == 0) ? 0 : ll.peek();
                    if(ll.size() == 0 || h >= a[idxPeek]) {
                        ll.push(i);
                    } else {
                        ll.pop();
                        int curarea = 0;
                        if(ll.size() == 0) {
                            curarea = a[idxPeek] * i;
                        } else {
                            int idxPeekAgain = ll.peek();
                            curarea = a[idxPeek] * (i - 1 - a[idxPeekAgain]);
                        }
                        area = (area < curarea) ? curarea : area;
                        i--;
                    }
                }
                return area;
            }
        }
        C c = new C();
        switch(type) {
        case 1: return c.largest(h);
        case 2: return c.largestLeet(h);
        case 3: return c.largestBrute(h);
        case 4: return c.largestStack(h);
        }
        return c.largest(h);
    }
    /**
     * given n steps, you can step 1 or 2. how many distinct
     * ways to climb n steps?
     *
     *  overlapping problem?
     *
     *  1   1   1
     *
     *  2   2   1 1
     *          2
     *
     *  3   3   1 1 1
     *          2   1
     *          1 2
     *
     *  4   5   1 1 1 1
     *          2   1 1
     *          1 2   1
     *          1 1 2
     *          2   2
     *
     *  5   8   1 1 1 1 1
     *          2   1 1 1
     *          1 2   1 1
     *          1 1 2   1
     *          1 1 1 2
     *          2   2   1
     *          2   1 2
     *          1 2   2
     *
     *  6   13  1 1 1 1 1 1
     *          2   1 1 1 1
     *          1 2   1 1 1
     *          1 1 2   1 1
     *          1 1 1 2   1
     *          1 1 1 1 2
     *          2   2   1 1
     *          2   1 2   1
     *          2   1 1 2
     *          1 2   2   1
     *          1 2   1 2
     *          1 1 2   2
     *          2   2   2
     *
     *  7   21  1 1 1 1 1 1 1
     *          2   1 1 1 1 1
     *          1 2   1 1 1 1
     *          1 1 2   1 1 1
     *          1 1 1 2   1 1
     *          1 1 1 1 2   1
     *          1 1 1 1 1 2
     *          2   2   1 1 1
     *          2   1 2   1 1
     *          2   1 1 2   1
     *          2   1 1 1 2
     *          1 2   2   1 1
     *          1 2   1 2   1
     *          1 2   1 1 2
     *          1 1 2   2   1
     *          1 1 2   1 2
     *          1 1 1 2   2
     *          2   2   2   1
     *          1 2   2   2
     *          2   1 2   2
     *          2   2   1 2
     *
     *  this is fibonacci:
     *  1 1 2 3 5 8 13 21 34 55 89 ...
     *
     *
     */
    public int climbStairs(int n) {
        int sum = 1;
        int prv = 1;
        int prvprv = 0;
        for(int i = 1; i <= n; i++) {
            sum = prvprv + prv;
            prvprv = prv;
            prv = sum;
        }
        return sum;
    }
    /**
     *                  50
     *          25              75
     *       20    30        70    80
     *
     *       pre: 50 25 20 30 75 70 80
     *       in:  20 25 30 50 70 75 80
     *
     *                          50
     *              25                          75
     *       20         30               70          80
     *     10        27    33        65      72
     *                   32                    73
     *
     *       pre: 50 25 20 10 30 27 33 32 75 70 65 72 73 80
     *       in:  10 20 25 27 30 32 33 50 65 70 72 73 75 80
     *
     */
    public TreeNode buildTreePreAndIn(int [] pre, int [] in) {
        class C {
            int idxp = 0;
            TreeNode build(int [] pre, int [] in, int idxis, int idxie) {
                if(idxp >= pre.length) {
                    return null;
                }
                if(idxis > idxie || idxis < 0 || idxie >= in.length) {
                    return null;
                }
                TreeNode n = new TreeNode(pre[idxp]);
                for(int i = idxis; i <= idxie; i++) {
                    if(in[i] == pre[idxp]) {
                        idxp++;
                        n.left = build(pre, in, idxis, i-1);
                        n.right = build(pre, in, i+1, idxie);
                        break;
                    }
                }

                return n;
            }
        }
        C c = new C();
        TreeNode n = c.build(pre, in, 0, in.length-1);
        return n;
    }
    /**
     *                  50
     *          25              75
     *       20    30        70    80
     *
     *       pre: 50 25 20 30 75 70 80
     *       in:  20 25 30 50 70 75 80
     *       pst: 20 30 25 70 80 75 50
     *
     *                          50
     *              25                          75
     *       20         30               70          80
     *     10        27    33        65      72
     *                   32                    73
     *
     *       pre: 50 25 20 10 30 27 33 32 75 70 65 72 73 80
     *       in:  10 20 25 27 30 32 33 50 65 70 72 73 75 80
     *
     */
    public TreeNode buildTreePostAndIn(int [] post, int [] in) {
        class C {
            int idxp;
            int find(int [] io, int idxs, int idxe, int v) {
                for(int i = idxs; i <= idxe; i++)
                    if(io[i] == v) return i;
                return -1;
            }
            TreeNode bt(int [] io, int [] po, int idxs, int idxe) {
                if(idxp < 0 || idxs > idxe) return null;
                TreeNode n = new TreeNode(po[idxp--]);
                int idxm = find(io, idxs, idxe, n.val);
                n.right = bt(io, po, idxm+1, idxe);
                n.left = bt(io, po, idxs, idxm-1);
                return n;
            }
            TreeNode bt(int [] io, int [] po) {
                idxp = po.length-1;
                return bt(io, po, 0, io.length-1);
            }
        }
        if(in.length == 0 || post.length == 0) return null;
        C c = new C();
        return c.bt(in, post);
    }
    /**
     * position of most significant bit.
     *
     * 1            1
     * 11           3
     * 111          7
     * 1111         15
     * 11111        31
     * 111111       63
     * 1111111      127
     * 11111111     255
     *
     *
     * @param x
     * @return
     */
    public int msb(int x) {
        return 0;
    }
    public int majorityElement(int[] nums) {
        int number = 0;
        int cnt = 0;
        for(int i = 0; i < nums.length; i++) {
            if(cnt == 0) {
                number = nums[i];
                cnt++;
            } else {
                cnt += (number == nums[i]) ? 1 : -1;
            }
        }
        return number;
    }
    /**
     * how many configurations of a bst can you make with n nodes?
     *
     *
     * 2        .        .
     *        .            .
     *
     * 3        .        .
     *        .            .
     *      .                .
     *
     *          .        .
     *        .            .
     *          .        .
     *
     *          .
     *        .   .
     *
     * 4       .         .         .       .
     *        .         .         .       . .
     *       .         .         . .     .
     *      .           .
     *
     *
     */
    public int uniqueBST(int n) {
        class C {
            int ubst1(int n) {
                int [] a = new int[n];
                for(int i = 0; i < n; i++) {
                    a[i] = 0;
                }
                AtomicInteger ai = new AtomicInteger(0);
                ubst1(a, ai);
                return ai.get();
            }
            void ubst1(int [] a, AtomicInteger ai) {
                for(int i = 0; i < a.length; i++) {
                    a[i] += 1;
                }
            }
        }
        C c = new C();
        return c.ubst1(n);
    }
    /**
     * reverse a linked list
     *
     * a->b->c->d->e        prv cur nxt     op
     *                                      cur.n = prv
     *                                      prv = cur
     *                                      cur = nxt
     *                                      nxt = nxt.n
     *                      -   a   b       a.n = -,
     *                      a   b   c       b.n = a
     */
    public ListNode reverseList(ListNode h) {
        ListNode prv = null;
        ListNode cur = h;
        ListNode nxt = (cur == null) ? null : cur.next;
        while(cur != null) {
            cur.next = prv;
            prv = cur;
            cur = nxt;
            nxt = (nxt == null) ? null : nxt.next;
        }
        return prv;
    }
    public void print(ListNode h) {
        ListNode n = h;
        while(n != null) {
            p("%d ", n.val);
            n = n.next;
        }
        p("\n");
    }
    /**
     * given non negative int n, for 0 <= i <= n, calculate number of 1 in
     * each binary i value and return as array.
     * eg
     *
     * n = 5,   [0,1,1,2,1,2]
     *
     * 0000
     * 0001
     * 0010
     * 0011
     * 0100
     * 0101
     * 0110
     * 0111
     * 1000
     * 1001
     * 1010
     * 1011
     *
     * Can this be done in O(n) in single pass?
     */
    public int [] countBits(int n) {
        int [] ret = new int[n];
        return ret;
    }
    public int diameterBinaryTree(TreeNode root) {
        class C {
            public int diameterOfBinaryTree(TreeNode root) {
                if(root == null) {
                    return 0;
                }
                int maxtmpl = diameterOfBinaryTree(root.left);
                int maxtmpr = diameterOfBinaryTree(root.right);
                int l = len(root.left);
                int r = len(root.right);
                int d = l+r;
                int maxtmp = (maxtmpl < maxtmpr) ? maxtmpr : maxtmpl;
                maxtmp = (maxtmp < d) ? d : maxtmp;
                return maxtmp;
            }
            int len(TreeNode n) {
                if(n == null) return 0;
                int l = 1 + len(n.left);
                int r = 1 + len(n.right);
                return (l < r) ? r : l;
            }
        }
        C c = new C();
        return c.diameterOfBinaryTree(root);
    }
    public boolean isSubtree(TreeNode s, TreeNode t) {
        class C {
            boolean issubtree(TreeNode s, TreeNode t) {
                if(s == null && t == null) return true;
                if(s == null || t == null) return false;
                if(s.val == t.val) {
                    if(isequal(s, t)) return true;
                }
                if(issubtree(s.left, t)) return true;
                if(issubtree(s.right, t)) return true;
                return false;
            }
            boolean isequal(TreeNode s, TreeNode t) {
                if(s == null && t == null) return true;
                if(s == null || t == null) return false;
                if(s.val != t.val) return false;
                if(!isequal(s.left, t.left)) return false;
                if(!isequal(s.right, t.right)) return false;
                return true;
            }
        }
        C c = new C();
        return c.issubtree(s, t);
    }
    /**
     * 1->2->3->4->5
     *
     *
     *
     * 1->2->3->4->5->6
     *
     *
     *
     */
    public boolean isLinkedPalindrome(ListNode h) {
        class C {
            /**
             * broken
             */
            boolean islinked1(ListNode h) {
                if(h == null) return true;
                ListNode np = null;
                ListNode ns = h;
                ListNode nsn = null;
                ListNode nf = h;
                int ctr = 0;

                while(nf != null) {
                    if(nf == h) {
                        nf = nf.next;
                        nsn = ns.next;
                        ns.next = np;
                        ctr = (nf == null) ? 1 : 2;
                    } else {
                        nf = nf.next;
                        ns = nsn;
                        nsn = nsn.next;
                        ns.next = np;
                        ctr++;
                        if(nf == null)  break;
                        nf = nf.next;
                        ctr++;
                    }
                    np = ns;
                }
                if(ctr % 2 == 1) {
                    ns = ns.next;
                }
                ListNode nl = ns;
                ListNode nr = nsn;
                while(nl != null && nr != null) {
                    if(nl.val != nr.val) return false;
                    nl = nl.next;
                    nr = nr.next;
                }
                return true;
            }
            /**
             * broken
             */
            boolean islinked2(ListNode h) {
                if(h == null) return true;
                ListNode np = null;
                ListNode ns = null;
                ListNode nsn = null;
                ListNode nf = h;
                int ctr = 0;

                while(nf != null) {
                    nf = nf.next;
                    ctr++;
                    if(ns == null) {
                        ns = h;
                        nsn = h.next;
                        ns.next = np;
                    } else {
                        ns = nsn;
                        nsn = ns.next;
                        ns.next = np;
                        if(nf == null) break;
                        nf = nf.next;
                        ctr++;
                    }
                    np = ns;
                }
                if(ctr % 2 == 1) {
                    ns = ns.next;
                }
                ListNode nl = ns;
                ListNode nr = nsn;
                while(nl != null && nr != null) {
                    if(nl.val != nr.val) return false;
                    nl = nl.next;
                    nr = nr.next;
                }
                return true;
            }
            /**
             * broken
             */
            boolean islinked3(ListNode h) {
                if(h == null) return true;
                ListNode np = null;
                ListNode ns = null;
                ListNode nsn = null;
                ListNode nf = h;
                int ctr = 0;
                while(nf != null) {
                    if(ns == null) {
                        ns = h;
                        nsn = h.next;
                    } else {
                        nf = nf.next;
                        ns = nsn;
                        nsn = ns.next;
                    }
                    ctr++;
                    if(nf != null) {
                        nf = nf.next;
                        ctr++;
                    }
                    ns.next = np;
                    np = ns;
                    if(nf == null) {
                        ctr--;
                        break;
                    }
                }
                if(ctr % 2 == 1) {
                    ns = ns.next;
                }
                ListNode nl = ns;
                ListNode nr = nsn;
                while(nl != null && nr != null) {
                    if(nl.val != nr.val) return false;
                    nl = nl.next;
                    nr = nr.next;
                }
                return true;
            }
            /**
             * only this one works
             * @param h
             * @return
             */
            boolean islinked4(ListNode h) {
                if(h == null) return true;
                ListNode np = null;
                ListNode ns = null;
                ListNode nsn = null;
                ListNode nf = h;
                int ctr = 1;
                while(nf != null) {
                    nf = nf.next;
                    if(nf == null) break;
                    ctr++;
                    if(ns == null) {
                        ns = h;
                        nsn = h.next;
                        ns.next = np;
                    } else {
                        ns = nsn;
                        nsn = nsn.next;
                        ns.next = np;
                    }
                    np = ns;
                    nf = nf.next;
                    if(nf == null) break;
                    ctr++;
                }
                if(ctr == 1) {
                    return true;
                }
                ListNode nl = ns;
                ListNode nr = (ctr % 2 == 0) ? nsn : nsn.next;
                while(nl != null && nr != null) {
                    if(nl.val != nr.val) return false;
                    nl = nl.next;
                    nr = nr.next;
                }
                return true;

            }
        }
        C c = new C();
        return c.islinked4(h);
    }
    public ListNode makeListNode(int [] a) {
        ListNode h = null;
        ListNode n = null;
        for(int i = 0; i < a.length; i++) {
            if(i == 0) {
                h = new ListNode(a[i]);
                n = h;
            } else {
                n.next = new ListNode(a[i]);
                n = n.next;
            }
        }
        return h;
    }
    /**
     *          25
     *      15      35
     *    10  20  30  40
     *
     *    40                            40
     *    35+40                         75
     *    30+35+40                      105
     *    25+30+35+40                   130
     *    20+25+30+35+40                150
     *    15+20+25+30+35+40             165
     *    10+15+20+25+30+35+40          175
     *
     *              130
     *      165             75
     *  175     150     105     40
     */
    public TreeNode convertGreaterTree(TreeNode r) {
        class C {
            int sum = 0;
            TreeNode convert(TreeNode n) {
                if(n == null) return null;
                n.right = convert(n.right);
                n.val += sum;
                sum = n.val;
                n.left = convert(n.left);
                return n;
            }
        }
        C c = new C();
        return c.convert(r);
    }
    /**
     *      3
     *
     *      3   2
     *
     *      3   2   4
     *      ----------
     *      3   6   24
     *      24  8   4
     *      ----------
     *      8   12  6
     *
     *      3       2       4       5       6       3       2
     *      -------------------------------------------------------
     *      3       6       24      120     720     2160    4320
     *      4320    1440    720     180     36      6       2
     *      -------------------------------------------------------
     *      1440    2160    1080    864     720     1440    2160
     *      -------------------------------------------------------
     *
     *      first populate left to right with product.
     *      then from right to left, rewrite r[i] = r[i-1]*a[i+1]
     *      handle corners as 1.
     */
    public int [] productOfArrayExceptSelf(int [] a) {
        int sz = a.length;
        int [] r = new int[sz];
        if(sz == 0 || sz == 1) {
            return r;
        }
        for(int i = 0; i < sz; i++) {
            int prv = (i == 0) ? 1 : r[i-1];
            r[i] = a[i] * prv;
        }
        int curprod = 1;
        for(int i = sz-1; i >= 0; i--) {
            int va = (i == (sz-1)) ? 1 : a[i+1];
            curprod = curprod * va;
            int vr = (i == 0) ? 1 : r[i-1];
            r[i] = curprod * vr;
        }
        return r;
    }
    /**
     * given string s and non empty string p, find all start indices
     * of p's anagrams in s. order of output does not matter.
     *
     * s = cbaebabacd  p = abc
     * [0,6]
     * 0 = cba, 6 = bac
     *
     * s = abab  p = ab
     * [0,1,2]
     * 0 = ab
     * 1 = ba
     * 2 = ab
     *
     */
    public List<Integer> findAllAnagramsInString(String s, String p) {
        class C {
            List<Integer> findAll1(String s, String p) {
                List<Integer> l = new ArrayList<>();
                HashMap<Character, Integer> map = new HashMap<>();
                for(char c: p.toCharArray()) {
                    int v = (map.get(c) == null) ? 1 : (1 + map.get(c));
                    map.put(c, v);
                }
                HashMap<Character, Integer> m = new HashMap<>();
                for(int h = 0, ctr = 0, t = 0; h < s.length(); h++) {
                    char c = s.charAt(h);
                    if((h-t+1) > p.length()) {
                        int v = (m.get(s.charAt(t)) == null) ? 0 : m.get(s.charAt(t));
                        if(v != 0) {
                            m.put(s.charAt(t), --v);
                            ctr--;
                        }
                        t++;
                    }
                    if(map.get(c) == null) continue;
                    int v = (m.get(c) == null) ? 1 : m.get(c) + 1;
                    ctr++;
                    m.put(c, v);
                    if(ctr == p.length()) {
                        boolean match = true;
                        for(Map.Entry<Character, Integer> kv: map.entrySet()) {
                            //if(m.get(kv.getKey()) == null || m.get(kv.getKey()) != kv.getValue()) match = false;
                            if(m.get(kv.getKey()) == null) match = false;
                            if(m.get(kv.getKey()).intValue() != kv.getValue().intValue()) match = false;
                        }
                        if(match) l.add(t);
                    }
                }
                return l;
            }
            List<Integer> findAll2(String s, String p) {
                List<Integer> l = new ArrayList<>();
                int cnt = 0;
                HashMap<Character, Integer> map = new HashMap<>();
                // populate map with p
                for(int i = 0; i < p.length(); i++) {
                    Character c = p.charAt(i);
                    Integer v = map.get(p.charAt(i));
                    if(v == null) {
                        map.put(c, 1);
                    } else {
                        map.put(c, v+1);
                    }
                }
                int ptrTail = 0;
                int szP = p.length();
                HashMap<Character, Integer> mapCnt = new HashMap<>();
                for(int i = 0; i < s.length(); i++) {
                    int len = i - ptrTail + 1;
                    if(len >= szP) {
                        Character charTail = s.charAt(ptrTail);
                        Integer v = mapCnt.get(charTail);
                        if(v != null) {
                            v = v - 1;
                            if(v < map.get(charTail)) {
                                cnt--;
                            }
                            mapCnt.put(charTail, v - 1);
                        }
                    }
                    Character c = s.charAt(i);
                    if(map.get(c) != null) {
                        Integer v = mapCnt.get(c);
                        if(v == null) {
                            mapCnt.put(c, 1);
                            cnt++;
                        } else {
                            v = v+1;
                            if(v == map.get(c)) {
                                cnt++;
                                if(cnt == szP) {
                                    l.add(ptrTail);
                                }
                            }
                            mapCnt.put(c, v);
                        }
                    }
                    ptrTail++;
                }
                return l;
            }
        }
        C c = new C();
        return c.findAll1(s, p);
    }
    /**
     *      0    1  2   3   4   5   6   7
     *      -------------------------------
     *      10   9  2   5   3   7   101 18
     *      -------------------------------
     *
     *   i      v       a
     *   0      10      10
     *   1      9       9
     *   2      2       2
     *   3      5       2 5
     *   4      3       2 3
     *   5      7       2 3 7
     *   6      101     2 3 7 101
     *   7      18      2 3 4 18
     */
    public int longestIncreasingSubsequence(int [] a) {
        class C {
            int lisN2(int [] a) {
                int r = 0;
                int sz = a.length;
                for(int i = 0; i < sz; i++) {

                }
                return r;
            }
            int lisNlogN(int [] a) {
                int idx = -1;
                int sz = a.length;
                int [] ar = new int[sz];
                int max = 0;
                for(int i = 0; i < sz; i++) {
                    int v = a[i];
                    if(i == 0) {
                        ar[++idx] = v;
                        max++;
                    } else {
                        int idxlt = binsearchlt(ar, v, 0, idx);
                        if(idxlt == idx) {
                            ar[++idx] = v;
                            max++;
                        } else {
                            ar[idxlt+1] = v;
                        }
                    }
                }
                return max;
            }
            /**
             * returns index of r where r[idx] < v < r[idx+1], or
             * last item if r[idx+1] does not exist.
             *
             * 0 1 2 3 4
             * 1 3 5 7 9    v = 2, expect idx == 0
             * |       |    (0+4)/2=2=>5, v < a[2]
             * | |          (0+1)/2=0=>1, a[0] < v
             *                              v < a[0+1]? yes
             *              v = 4, expect idx == 1
             * |       |    (0+4)/2=2=>5, v < a[2]
             * | |          (0+1)/2=0=>1, a[0] < v
             *                              v < a[0+1]? no
             *   ||         (1+1)/2=1=>3, a[1] < v
             */
            int binsearchlt(int [] a, int v, int idxs, int idxe) {
                if(a.length == 0 || v < a[0]) {
                    return -1;
                }
                while(idxs <= idxe) {
                    int idxm = (idxs+idxe)/2;
                    int va = a[idxm];
                    if(v == va) {
                        idxe = idxm - 1;
                    }
                    else if(va < v) {
                        if(idxm < idxe && v < a[idxm+1]) {
                            return idxm;
                        }
                        idxs = idxm+1;
                    }
                    else {
                        idxe = idxm-1;
                    }
                }
                return idxe;
            }
        }
        C c = new C();
        return c.lisNlogN(a);
    }
    /**
     * return idx where a[idx] == v. -1 if not exist.
     */
    public int binsearcheq(int [] a, int v, int idxs, int idxe) {
        while(idxs <= idxe) {
            int idxm = (idxs+idxe)/2;
            int va = a[idxm];
            if(v == va)
                return idxm;
            else if(v < va)
                idxe = idxm-1;
            else
                idxs = idxm+1;
        }
        return -1;
    }
    /**
     * return idx where its value is greater than v
     * return idx where a[idx-1] < v < a[idx].
     *
     * return 0 if v < a[0]
     * if a[idx] == v, return idx+1 where v < a[idx]
     * return idxe+1 if a[idxe] < v
     */
    public int binsearchgt(int [] a, int v, int idxs, int idxe) {
        class C {
            /*
             *
             * 0    1   2   3   4
             * ------------------
             * 1    3   5   7   9    cmp 8
             * |                |
             *          5           5 < 8, 8 < a[2+1]?
             *              |   |
             *              7       7 < 8, 8 < a[3+1]? yes->return
             *
             *                      cmp 2
             * |                |
             *          5           5 > 2
             * |    |
             * 1                    1 < 2, 2 < a[1+1]? yes->return
             */
            int gt1(int [] a, int v, int idxs, int idxe) {
                if(a.length == 0) {
                    return -1;
                }
                if(a[a.length-1] < v) {
                    return idxe+1;
                }
                while(idxs <= idxe) {
                    int idxm = (idxs+idxe)/2;
                    int va = a[idxm];
                    if(va <= v) {
                        if(idxm <= idxe && v < a[idxm+1]) {
                            return idxm+1;
                        }
                        idxs = idxm+1;
                    } else {
                        idxe = idxm-1;
                    }
                }
                return 0;
            }
            int gt2(int [] a, int v, int is, int ie) {
                if(a.length == 0) {
                    return -1;
                }
                if(a[a.length-1] < v) {
                    return idxe+1;
                }
                int idxs = is;
                int idxe = ie;
                while(idxs <= idxe) {
                    int idxm = (idxs+idxe)/2;
                    int va = a[idxm];
                    if(v <= va) {
                        if(idxm > is && a[idxm-1] < v) {
                            return idxm;
                        }
                        idxe = idxm-1;
                    } else {
                        idxs = idxm+1;
                    }
                }
                return 0;
            }
        }
        C c = new C();
        int v1 = c.gt1(a, v, idxs, idxe);
        int v2 = c.gt2(a, v, idxs, idxe);
        if(v1 == v2) {
            return v1;
        }
        return -2;
    }
    /**
     * return idx where its value is less than v
     * return idx where a[idx] < v < a[idx+1];
     * if a[idx] == v, return idx-1, so a[idx] < v <= a[idx+1]
     * returns idxe if a[idxe] < v
     * returns -1 if     v < a[0]
     *
     *  0   1   2   3   4   5
     *  -----------------------
     *  1   3   5   7   9   11
     *                          v = 4
     *  |                   |   (0+5)/2=2=>5    4 < 5
     *  |   |                   (0+1)/2=0=>1    4 > 1
     *      ||                  (1+1)/2=1=>3    4 > 3, fall out return idxe
     *
     *                          v = 8
     *  |                   |   (0+5)/2=2=>5    8 > 5
     *
     *
     */
    public int binsearchlt(int [] a, int v, int idxs, int idxe) {
        if(a.length == 0 || v < a[0]) {
            return -1;
        }
        while(idxs <= idxe) {
            int idxm = (idxs+idxe)/2;
            int va = a[idxm];
            if(v >= va) {
                if(idxm < idxe && a[idxm+1] > v) {
                    return idxm;
                }
                idxs = idxm+1;
            }
            else {
                idxe = idxm-1;
            }
        }
        return idxe;
    }
    /**
     * given array of ints, find the top k frequent elements.
     * runtime must be less than nlogn.
     *
     * implement a queue constrained by k items and keep
     * track of smallest element into back of queue. remove it
     * if there is a bigger item.
     *
     * or a min heap of size k.
     */
    public List<Integer> topKFrequent(int [] a, int k) {
        class Pair implements Comparable<Pair> {
            int v = 0;
            int cnt = 0;
            Pair(int v) {
                this.v = v;
                cnt = 1;
            }
            public int compareTo(Pair p) {
                if(cnt < p.cnt) {
                    return -1;
                }
                else if(cnt == p.cnt) {
                    return 0;
                }
                return 1;
            }
        }
        class C {
            List<Integer>  basic(int [] a, int k) {
                Map<Integer, Pair> map = new HashMap<>();
                LinkedList<Pair> ll = new LinkedList<>();
                for(int i = 0; i < a.length; i++) {
                    int v = a[i];
                    Pair pair = map.get(v);
                    if(pair == null) {
                        pair = new Pair(v);
                        map.put(v, pair);
                    } else {
                        pair.cnt++;
                    }
                    if(ll.size() < k) {
                        ll.add(pair);
                    } else {
                        while(ll.size() != 0) {
                            Pair pairTail = ll.peekLast();
                            if(pairTail.cnt < pair.cnt) {
                                ll.pollLast();
                            } else {
                                ll.addLast(pair);
                                break;
                            }
                        }
                    }
                }
                List<Integer> l = new ArrayList<>();
                for(Pair pair: ll) {
                    l.add(pair.v);
                }
                return l;
            }
            List<Integer> pq(int [] a, int k) {
                // max queue looks like PriorityQueue<>(Collections.reverseOrder())
                // or change the Comparable logic
                PriorityQueue<Pair> pq = new PriorityQueue<>();
                Map<Integer, Pair> map = new HashMap<>();
                for(int i = 0; i < a.length; i++) {
                    int v = a[i];
                    Pair pair = map.get(v);
                    if(pair == null) {
                        pair = new Pair(v);
                        map.put(v, pair);
                    } else {
                        pair.cnt++;
                    }
                    while(pq.size() != 0) {
                        Pair pairpq = pq.peek();
                        if(pairpq.compareTo(pair) == -1) {
                            pq.poll();
                        } else {
                            break;
                        }
                    }
                    if(pq.size() < k) {
                        // the problem is that pair is modified
                        // while it's already in priority queue,
                        // so priority queue doesn't reorder.

                        // cannot use this implementation
                        pq.add(pair);
                    }
                }
                List<Integer> l = new ArrayList<>();
                while(pq.size() != 0) {
                    l.add(pq.poll().v);
                }
                return l;
            }
            List<Integer> pq2(int [] a, int k) {
                // this is min queue
                PriorityQueue<Pair> pq1 = new PriorityQueue<>(
                    new Comparator<Pair>() {
                        public int compare(Pair a, Pair b) {
                            return (a.cnt - b.cnt);
                        }
                    }
                );
                List<Integer> l = new ArrayList<>();
                return l;
            }

        }
        C c = new C();
        return c.pq(a, k);
    }
    public ListNode mergeKSortedLists(ListNode [] l) {
        PriorityQueue<ListNode> pq = new PriorityQueue<>(
                new Comparator<ListNode>() {
                    public int compare(ListNode a, ListNode b) {
                        int diff = a.val - b.val;
                        if(diff < 0)
                            return -1;
                        if(diff > 0)
                            return 1;
                        return 0;
                    }
                }
        );
        ListNode h = null;
        ListNode n = null;
        for(ListNode ln: l) {
            if(ln != null) {
                pq.add(ln);
            }
        }
        while(pq.size() != 0) {
            ListNode ln = pq.poll();
            ListNode lp = null;
            if(pq.size() != 0) {
                lp = pq.peek();
                while(ln.val <= lp.val) {
                    if(h == null) {
                        h = ln;
                        n = ln;
                    } else {
                        n.next = ln;
                        n = ln;
                    }
                    ln = ln.next;
                    if(ln == null) {
                        break;
                    }
                }
                if(ln != null) {
                    pq.add(ln);
                }
            } else {
                if(h == null) {
                    h = ln;
                } else {
                    n.next = ln;
                }
                break;
            }
        }
        return h;
    }
    /**
     * a has n+1 numbers, each number is [1:n]. find the one duplicate,
     * which may appear many times.
     *
     * 1+2+3    = 6     = (3^2+3)/2 = 12/2
     * 1+2+3+4  = 10    = (4^2+4)/2 = 20/2
     *
     *
     * 1+2+3+4+4+4  = 18    = (4^2+4)/2 = 20/2 = 10
     * (18-10)/2 = 4
     *
     */
    int findDuplicate(int [] a) {
        class C {
            int dup1(int [] a) {
                int sz = a.length;
                int min = Integer.MAX_VALUE;
                int max = Integer.MIN_VALUE;
                int sum = 0;
                for(int i = 0; i < sz; i++) {
                    int v = a[i];
                    min = (min < v) ? min : v;
                    max = (max > v) ? max : v;
                    sum += v;
                }
                int expsum = (max*max + max)/2;
                int sumdiff = sum - expsum;
                int dup = sumdiff/(sz-max);
                return dup;
            }
            /**
             *  n = 7
             *
             *  1 3 5 5 5 5 7
             *  5 3 7 5 1 5 5
             *  s       f
             *
             *
             *  5 3 4 1 3 2 6
             *  s         f
             *          f s
             *    f s
             *    f     s
             *    f   s
             *    fs
             *
             *  5 3 4 1 6 2 4
             *  s         f
             *
             *  5 3 1 2 6 2 4
             *  s         f
             *    f       s
             *      fs
             *
             *
             *
             *
             *
             */
            int dup2(int [] a) {
                int sz = a.length;
                int sum = 0;
                int dup = 0;
                return dup;
            }
        }
        C c = new C();
        return c.dup1(a);
    }
    /**
     * in Binary tree, find the total number of paths that equal sum.
     * start does not have to be root, and end does not have to be leaf,
     * but all must go down.
     *
     */
    public int pathSumBST(TreeNode r, int sum) {
        class C {
            int sumBST(TreeNode r, int sum) {
                if(r == null) return 0;
                int sumNow = (r.val == sum || (sum - r.val == 0)) ? 1 : 0;
                int sumLless = sumBST(r.left, sum - r.val);
                int sumRless = sumBST(r.right, sum - r.val);
                int sumLsame = sumBST(r.left, sum);
                int sumRsame = sumBST(r.right, sum);
                return sumNow + sumLless + sumRless + sumLsame + sumRsame;
            }
        }
        C c = new C();
        int ctr = c.sumBST(r, sum);
        return ctr;
    }
    /**
     * in an array, find number of subarrays that equal sum.
     * there can be negative values.
     *
     * eg:
     *
     *  idx 0  1  2  3  4  5  6  7  8  9 10 11 12 13 14
     *      -------------------------------------------
     *  val 1  2  3  2  3  1  1  4  2  3  4  1  2  2  1, and sum = 5
     *  sum 1  3  6  8 11 12 13 17 19 22 26 27 29 31 32
     *      -------------------------------------------
     *  ctr       1  2  3     4  5     6     7     8  9
     *
     */
    public int pathSumSubarray(int [] a, int sum) {
        int ctr = 0;
        Set<Integer> set = new HashSet<>();
        int cursum = 0;
        for(int i = 0; i < a.length; i++) {
            int v = a[i];
            cursum += v;
            set.add(cursum);
            if(v == sum || cursum == sum) {
                ctr++;
            }
            else if(set.contains(cursum - sum)) {
                ctr++;
            }
        }
        return ctr;
    }
    /**
     * detect the cycle of a linked list:
     *
     * case 1:  odd head, odd cycle
     *          1->2->3->4->5->6->7->8
     *                      ^        |
     *                      |        v
     *                      +--------+
     *
     * case 2:  even head, odd cycle
     *          1->2->3->4->5->6->7->8->9
     *                      ^           |
     *                      |           v
     *                      +-----------+
     *
     * case 3:  odd head, even cycle
     *          1->2->3->4->5->6->7->8->9
     *                         ^        |
     *                         |        v
     *                         +--------+
     *
     * case 4:  even head, even cycle
     *          1->2->3->4->5->6->7->8->9->10
     *                         ^           |
     *                         |           v
     *                         +-----------+
     *
     * case 5:
     *          1->2->3->4->5->6->7->8->9->10->11
     *                         ^               |
     *                         |               v
     *                         +---------------+
     *
     * case 6:
     *          1->2->3->4->5->6->7->8->9->10->11->12
     *                            ^                |
     *                            |                v
     *                            +----------------+
     *
     *      c1          c2          c3          c4          c5          c6
     *      s   f       s   f       s   f       s   f       s   f       s   f
     *      1   2       1   2       1   2       1   2       1   2       1   2
     *      2   4       2   4       2   4       2   4       2   4       2   4
     *      3   6       3   6       3   6       3   6       3   6       3   6
     *      4   8       4   8       4   8       4   8       4   8       4   8
     *      5   6       5   5       5   6       5   10      5   10      5   10
     *      6   8                   6   8       6   7       6   6       6   12
     *      7   6                   7   6       7   9       7   8       7   8
     *      8   8                   8   8       8   6       8   10      8   10
     *                                          9   8       9   6       9   12
     *                                          10  10      10  8       10  8
     *                                                      11  10      11  10
     *                                                      6   6       12  12
     *
     *
     *  c1  1->2->3->4->5
     *  c2  1->2->3->4->5->6
     *  c3  1->2->3->4->5->6->7
     *  c4  1->2->3->4->5->6->7->8
     *  c5  1->2->3->4->5->6->7->8->9
     *  c6  1->2->3->4->5->6->7->8->9->10->11
     *
     *  c1      c2      c3      c4      c5      c6
     *  s   f   s   f   s   f   s   f   s   f   s   f
     *  1   2   1   2   1   2   1   2   1   2   1   2
     *  2   4   2   4   2   4   2   4   2   4   2   4
     *  3   1   3   6   3   6   3   6   3   6   3   6
     *  4   3   4   2   4   1   4   8   4   8   4   8
     *  5   5   5   4   5   3   5   2   5   1   5   10
     *          6   6   6   5   6   4   6   3   6   1
     *                  7   7   7   6   7   5   7   3
     *                          8   8   8   7   8   5
     *                                  9   9   9   7
     *                                          10  9
     *                                          11  11
     *
     *  c1      c2      c3      c4      c5      c6
     *  s   f   s   f   s   f   s   f   s   f   s   f
     *  1   1   1   1   1   1   1   1   1   1   1   1
     *  2   3   2   3   2   3   2   3   2   3   2   3
     *  3   5   3   5   3   5   3   5   3   5   3   5
     *  4   2   4   1   4   7   4   7   4   7   4   7
     *  5   4   5   3   5   2   5   1   5   9   5   9
     *  1   1   6   5   6   4   6   3   6   2   6   11
     *          1   1   7   6   7   5   7   4   7   2
     *                  1   1   8   7   8   6   8   4
     *                          1   1   9   8   9   6
     *                                  1   1   10  8
     *                                          11  10
     *                                          1   1
     */
    public ListNode linkedListCycle(ListNode h) {
        return h;
    }
    /**
     * given matrix, find target. the matrix is ascending, left to right, and up to down.
     * so you do not have to iterate through everything.
     *
     * [
     * [1,   4,  7, 11, 15],
     * [2,   5,  8, 12, 19],
     * [3,   6,  9, 16, 22],
     * [10, 13, 14, 17, 24],
     * [18, 21, 23, 26, 30]
     * ]
     */
    public boolean search2DMatrix(int [][] a, int target) {
        int szrow = a.length;
        if(szrow == 0) return false;
        int szcol = a[0].length;
        if(szcol == 0) return false;
        for(int i = 0; i < szrow; i++) {
            if(a[i][0] > target) {
                szrow = i;
            }
            for(int j = 0; j < szcol; j++) {
                if(a[i][j] == target)
                    return true;
                if(a[i][j] > target) {
                    szcol = j;
                }
            }
        }
        return false;
    }
    /**
     * given non empty array of only positive integers, find if array can
     * be partitioned into two subsets such that sum of elements in both subsets
     * is equal.
     *
     * {1,5,11,5} => {1,5,5} and {11} == true
     * {1,2,3,5} => false
     *
     * {1,5,3,4,6,5}
     *  1 5 3 4 6 5
     *  1 3 4 5 5 6
     *  1 3
     *  1   4
     *  1     5
     *  1         6
     *    3 4
     *    3   5
     *    3       6
     *      4 5
     *      4     6
     *        5   6
     *
     *
     */
    public boolean partialEqualSubsetSum(int [] a) {
        class C {
            /* subset sum is NP complete. */
            boolean f(int [] a) {
                int sum = 0;
                int sz = a.length;
                for(int i = 0; i < sz; i++) {
                    int v = a[i];
                    sum += v;
                }
                if(sum % 2 == 1) return false;
                int split = sum/2;
                boolean res = subsetRecursive(a, 0, 0, split);
                return res;
            }
            boolean subsetRecursive(int [] a, int idx, int cursum, int target) {
                for(int i = idx; i < a.length; i++) {
                    int v = a[i];
                    cursum = cursum + v;
                    if(cursum == target || v == target) return true;
                    if(cursum > target) return false;
                    boolean res = subsetRecursive(a, i+1, cursum, target);
                    if(res) return true;
                    cursum = cursum - v;
                }
                return false;
            }
            boolean subsetIterative(int [] a, int idx, int cursum, int target) {
                boolean [] ab = new boolean[a.length];
                for(int i = idx; i < a.length; i++) {
                    for(int j = i+1; j < a.length; j++) {

                    }
                }
                return false;
            }
        }
        C c = new C();
        return c.f(a);
    }
    public List<Integer> findDisappearedNumbers(int [] nums) {
        class C {
            public List<Integer> findDisappearedNumbers(int[] nums) {
                List<Integer> l = new ArrayList<>();
                for(int i = 0; i < nums.length; ) {
                    int v = nums[i];
                    int idx = v-1;
                    if(v == (i+1) || v == 0) {
                        i++;
                        continue;
                    }
                    if(i != idx && nums[i] == nums[idx]) {
                        nums[i] = 0;
                        i++;
                        continue;
                    }
                    swap(nums, i, idx);
                }
                for(int i = 0; i < nums.length; i++)
                    if(nums[i] == 0)
                        l.add(i+1);
                return l;
            }
            public void swap(int [] a, int i, int j) {
                int tmp = a[i];
                a[i] = a[j];
                a[j] = tmp;
            }
        }
        C c = new C();
        return c.findDisappearedNumbers(nums);
    }
    /**
     * reconstruct queue by height. (h,k), h is height, k is
     * number of ppl in front of this one whose height >= itself.
     *
     * in   [[7,0], [4,4], [7,1], [5,0], [6,1], [5,2]]
     *
     * out  [[5,0], [7,0], [5,2], [6,1], [4,4], [7,1]]
     *
     * in   [[7,0], [4,4], [7,1], [5,0], [6,1], [7,2], [5,2]]
     * sort   7,0 7,1 7,2 6,1 5,0 5,2 4,4
     * final  5,0 7,0 5,2 6,1 4,4 7,1 7,2
     *
     *        7,0
     *        7,0 7,1
     *        7,0 7,1 7,2
     *        7,0 6,1 7,1 7,2
     *        5,0 7,0 6,1 7,1 7,2
     *        5,0 7,0 5,2 6,1 4,4 7,1 7,2
     *
     *
     *
     */
    public int [][] reconstructQByHeight(int [][] a) {
        class C {
            int [][] height1(int [][] a) {
                if(a == null) return null;
                int sz = a.length;
                int [][] r = new int[sz][2];

                LinkedList<int []> ll = new LinkedList<>();
                for(int i = 0; i < sz; i++) {
                    if(ll.size() == 0) {
                        ll.add(a[i]);
                    } else {
                        int hcur = a[i][0];
                        int numf = a[i][1];
                        int ill = 0;
                        int ctr = 0;
                        boolean inserted = false;
                        for(int [] a1: ll) {
                            int hlst = a1[0];
                            int numl = a1[1];
                            if(hcur <= hlst) {
                                if(numf == ctr) {
                                    ll.add(ill, a[i]);
                                    inserted = true;
                                    break;
                                } else if(numf == (ctr+1)) {
                                    ll.add(ill+1, a[i]);
                                    inserted = true;
                                    break;
                                }
                                ctr++;
                            } else {
                                if(numl == ctr) {
                                    ll.add(ill, a[i]);
                                    inserted = true;
                                    break;
                                }
                            }
                            ill++;
                        }
                        if(!inserted) {
                            ll.add(a[i]);
                        }
                    }
                }
                int i = 0;
                for(int [] a1 : ll) {
                    r[i++] = a1;
                }
                return r;
            }
            int [][] height2(int [][] a) {
                if(a == null) return null;
                int sz = a.length;
                HashMap<Integer, List<Integer>> map = new HashMap<>();
                for(int i = 0; i < sz; i++) {
                    List<Integer> l = map.get(a[i][0]);
                    if(l == null) {
                        l = new ArrayList<>();
                        map.put(a[i][0], l);
                    }
                    l.add(a[i][1]);
                }
                LinkedList<int []> ll = new LinkedList<>();
                List<Integer> keys = new ArrayList<>(map.keySet());
                Collections.sort(keys, Collections.reverseOrder());
                for(Integer key: keys) {
                    List<Integer> l = map.get(key);
                    Collections.sort(l);
                    for(Integer i: l) {
                        int [] v = {key,i};
                        if(ll.size() == 0) {
                            ll.add(v);
                        } else {
                            for(int j = 0, numGT = 0; j < ll.size(); j++) {
                                int [] vl = ll.get(j);
                                if(vl[0] >= key) numGT++;
                                if(numGT == i) {
                                    ll.add(j+1, v);
                                    break;
                                }
                                else if(numGT > i) {
                                    ll.add(j, v);
                                    break;
                                }
                            }
                        }
                    }
                }
                return ll.toArray(new int[sz][2]);
            }
        }
        C c = new C();
        return c.height2(a);
    }
    /**
     * returns all palindromic substrings:
     *
     * abc => a,b,c
     * aaa => a,a,a,aa,aa,aaa
     */
    public int palindromicSubstrings(String s) {
        class C {
            List<String> palindromes(String s) {
                List<String> l = new ArrayList<>();
                for(int i = 0; i < s.length(); i++) {
                    l.add(s.substring(i, i+1));
                    for(int j = i-1, k = i+1; j >= 0 && k < s.length(); j--, k++) {
                        if(s.charAt(j) != s.charAt(k)) break;
                        l.add(s.substring(j,k+1));
                    }
                    for(int j = i, k = i+1; j >= 0 && k < s.length(); j--,k++) {
                        if(s.charAt(j) != s.charAt(k)) break;
                        l.add(s.substring(j,k+1));
                    }
                }
                return l;
            }
        }
        C c = new C();
        List<String> l = c.palindromes(s);
        return l.size();
    }
    /**
     * flatten binary tree to linked list:
     *
     *              1
     *          2       5
     *        3   4       6
     *
     *        1->2->3->4->5->6
     *
     *                  15
     *          25              65
     *       35    50        75    85
     *
     *       15 25 35 50 65 75 85
     *
     *       n   p   l   r   nl  nr  tmp  node
     *       15  -   25  65  -   25  65
     *       25  15  35  50  15  35  50
     *       35  25  -   -   25  -   -
     *       25  15  15  50
     *       50  35
     *       15  -
     *       65  50
     *
     *
     *                      15
     *              25                60
     *          30      45        65      80
     *        35  40  50  55    70  75  85  90
     *
     *
     *
     *
     *                         1
     *                  2
     *               3     5
     *            4
     *
     *
     *
     * @param r
     */
    public void flattenBinaryTreeLinkedList(TreeNode r, int type) {
        class C {
            /* BROKEN */
            TreeNode flatten(TreeNode n, TreeNode p) {
                if(n == null) return null;
                if(p != null) {
                    p.right = n;
                }
                TreeNode prevright = n.right;
                TreeNode pl = flatten(n.left, n);
                pl = pl == null ? n : pl;
                n.left = p;
                TreeNode pr = flatten(prevright, pl);
                pr = pr == null ? n : pr;
                return pr;
            }
            /* BROKEN */
            TreeNode flatten4(TreeNode n, TreeNode p) {
                if(n == null) return null;
                if(p != null) {
                    p.right = n;
                }
                TreeNode prevright = n.right;
                TreeNode pl = flatten4(n.left, n);
                pl = pl == null ? n : pl;
                n.left = null;
                TreeNode pr = flatten4(prevright, pl);
                pr = pr == null ? n : pr;
                return pr;
            }
            /* BROKEN */
            TreeNode flatten2(TreeNode n, TreeNode p) {
                if(n == null) return null;
                if(p != null) {
                    p.right = n;
                }
                if(n.left == null && n.right == null) return n;
                else if(n.left == null && n.right != null) return flatten2(n.right, n);
                else return flatten2(n.left, n);
            }
            /* BROKEN */
            TreeNode flatten3(TreeNode n, TreeNode p) {
                if(n == null) return null;
                if(p != null) {
                    p.right = n;
                }
                if(n.left == null && n.right == null) return n;
                TreeNode prevright = n.right;
                if(n.left != null) {
                    return flatten3(n.right, flatten3(n.left, n));
                }
                return flatten3(prevright, n);
            }
            TreeNode flatten5(TreeNode n, TreeNode p) {
                if(n == null) return null;
                TreeNode nr = n.right;
                TreeNode l = flatten5(n.left, n);
                TreeNode r = flatten5(nr, (l == null) ? n : l);
                if(p != null) {
                    p.right = n;
                }
                n.left = null;
                return (r != null) ? r : ((l != null) ? l : n);
            }
            void flattenleet1(TreeNode r) {
                TreeNode n = r;
                while(n != null) {
                    if(n.left != null) {
                        TreeNode pre = n.left;
                        while(pre.right != null) {
                            pre = pre.right;
                        }
                        pre.right = n.right;
                        n.right = n.left;
                        n.left = null;
                    }
                    n = n.right;
                }
            }
            void flattenleet2(TreeNode r) {
                if(r == null) return;
                flattenleet2(r.right);
                flattenleet2(r.left);
                r.right = leet2;
                r.left = null;
                leet2 = r;
            }
            void flattenleet3(TreeNode n) {
                if(n == null) return;
                TreeNode l = n.left;
                TreeNode r = n.right;
                n.left = null;
                flattenleet3(l);
                flattenleet3(r);
                n.right = l;
                TreeNode cur = n;
                while(cur.right != null)
                    cur = cur.right;
                cur.right = r;
            }
            TreeNode leet2 = null;
            void printFlatten(TreeNode n) {
                while(n != null) {
                    p("%2d ", n.val);
                    n = n.right;
                }
                p("\n");
            }
        }
        C c = new C();
        switch(type) {
        case 1: c.flatten(r, null); break;
        case 2: c.flatten2(r, null); break;
        case 3: c.flatten3(r, null); break;
        case 4: c.flatten4(r, null); break;
        case 5: c.flatten5(r, null); break;
        case 7: c.flattenleet1(r); break;
        case 8: c.flattenleet2(r); break;
        case 9: c.flattenleet3(r); break;
        default: c.flattenleet2(r); break;
        }
        c.printFlatten(r);
    }
    /**
     *
     *                      15
     *              25                60
     *          30      45        65      80
     *        35  40  50  55    70  75  85  90
     *
     *                      15
     *              25                60
     *          30      45        65      80
     *
     */
    public void traverseBST(TreeNode n, int type) {
        class C {
            void preorder(TreeNode n) {
                if(n == null) return;
                p("%2d ", n.val);
                preorder(n.left);
                preorder(n.right);
            }
            void preorderiterative(TreeNode r) {
                if(r == null) return;
                LinkedList<TreeNode> ll = new LinkedList<>();
                TreeNode n = r;
                while(n != null) {
                    p("%2d ", n.val);
                    if(n.left != null) {
                        ll.push(n);
                        n = n.left;
                    }
                    else {
                        if(ll.size() != 0) {
                            n = ll.pop();
                            if(n.right != null) {
                                n = n.right;
                                ll.push(n);
                            }
                        }
                    }
                }
            }
            void preorderiter(TreeNode n) {
                LinkedList<TreeNode> ll = new LinkedList<>();
                while(n != null) {
                    p("%2d ", n.val);
                    if(n.right != null) ll.push(n.right);
                    n = n.left;
                    if(n == null && ll.size() != 0) n = ll.pop();
                }
            }
            void postorder(TreeNode n) {
                if(n == null) return;
                postorder(n.left);
                postorder(n.right);
                p("%2d ", n.val);
            }
            void postorderIterOneStack(TreeNode n) {
                LinkedList<TreeNode> ll = new LinkedList<>();
                while(n != null) {
                    p("%2d ", n.val);

                }
            }
            void postorderIterTwoStack(TreeNode n) {

            }
            void inorder(TreeNode n) {
                if(n == null) return;
                inorder(n.left);
                p("%2d ", n.val);
                inorder(n.right);
            }
            /**
             *
             *                      15
             *              25                60
             *          30      45        65      80
             *        35  40  50  55    70  75  85  90
             *
             *                      50
             *              25                75
             *          15      35        65      85
             *
             *          50,25,15
             *          50,25
             *          50,35
             *          50
             *          75,65
             *          75
             *          85
             *
             *                      50
             *                                75
             *                            65      85
             *
             */
            void inorderIter1(TreeNode r) {
                if(r == null) return;
                TreeNode n = r;
                LinkedList<TreeNode> ll = new LinkedList<>();
                ll.push(n);
                while(ll.size() != 0) {
                    n = ll.pop();
                    while(n.left != null) {
                        ll.push(n);
                        n = n.left;
                    }
                    p("%2d ", n.val);
                    if(n.right != null) {
                        n = n.right;
                        if(n != null) {
                            ll.push(n);
                        }
                    } else if(ll.size() != 0){
                        n = ll.pop();
                        p("%2d ", n.val);
                        if(n.right != null) {
                            n = n.right;
                            if(n != null)
                                ll.push(n);
                        }
                    }
                }
            }
            void inorderIter2(TreeNode r) {
                if(r == null) return;
                TreeNode n = r;
                LinkedList<TreeNode> ll = new LinkedList<>();
                while(ll.size() != 0 || n != null) {
                    if(n != null) {
                        ll.push(n);
                        n = n.left;
                    } else if(ll.size() != 0){
                        n = ll.pop();
                        p("%2d ", n.val);
                        n = n.right;
                    } else {
                        break;
                    }
                }
            }
            void inorderIter3(TreeNode r) {
                if(r == null) return;
                TreeNode n = r;
                LinkedList<TreeNode> ll = new LinkedList<>();
                while(ll.size() != 0 || n != null) {
                    if(n.left != null) {
                        ll.push(n);
                        n = n.left;
                    } else {
                        p("%2d ", n.val);
                        if(ll.size() == 0) {
                            break;
                        }
                        n = ll.pop();
                        if(n.right != null) {
                            n = n.right;
                        }
                    }
                }
            }
        }
        C c = new C();
        switch(type) {
        case 1: c.preorderiterative(n); break;
        case 2: c.preorder(n); break;
        case 3: c.preorderiter(n); break;
        case 4: c.postorder(n); break;
        case 5: c.postorderIterOneStack(n); break;
        case 6: c.postorderIterTwoStack(n); break;
        case 7: c.inorder(n); break;
        case 8: c.inorderIter1(n); break;
        case 9: c.inorderIter2(n); break;
        default: c.preorder(n); break;
        }
    }
    /** convert column name to int
     *
     * a    1
     * z    26
     * aa   27
     * az   52
     * ba   53
     *
     *
     *
     * CBA          E       C       A
     *              30      28      26
     *                      26
     *                      52
     *                      78
     *
     *
     * A 1
     * B 2
     * C 3
     * D 4
     * E 5
     *              D   C   B
     *              38  6   5
     *                  12
     *                  18
     *                  24
     *                  32
     *
     *
     */
    public int columnToInt(String s) {
        int sum = 0;
        int pow = 1;
        for(int i = s.length()-1; i >= 0; i--) {
            char c = s.charAt(i);
            int v = c-'a'+1;
            pow = (i == (s.length()-1)) ? 1 : (pow * 26);
            v = v * pow;
            sum += v;
        }
        return sum;
    }
    public static class GEdge {
        public int w;
        public int idDst;
        public GEdge(int idDst, int w) {
            this.w = w;
            this.idDst = idDst;
        }
    }
    public static class GNode {
        List<GEdge> outEdges = new ArrayList<>();
        public int id;
        public GNode(int id) {
            this.id = id;
        }
        public void addOutEdge(int id, int w) {
            GEdge e = new GEdge(id, w);
            outEdges.add(e);
        }
    }
    public List<GNode> generateGraph(
            boolean isAcyclic,
            boolean isRandomWeight,
            int numNodes,
            int maxEdges)
    {
        return generateGraph(isAcyclic, isRandomWeight, -1, numNodes, maxEdges);
    }
    /**
     *
     * @param isAcyclic
     * @param isRandomWeight
     * @param numSources number of nodes with no incoming
     * @param numNodes
     * @param maxEdges
     * @return
     */
    public List<GNode> generateGraph(
            boolean isAcyclic,
            boolean isRandomWeight,
            int numSources,
            int numNodes,
            int maxEdges)
    {
        List<GNode> list = new ArrayList<>();
        Random r = new Random();
        Utils u = new Utils();
        List<Integer> listNonVisited = new ArrayList<>();
        int idxNV = 0;
        if(numSources != -1) {
            for(int i = numSources; i < numNodes; i++) {
                listNonVisited.add(i);
            }
        }
        u.shuffle(listNonVisited);
        for(int i = 0; i < numNodes; i++) {
            int numEdges = ((numNodes-i) < maxEdges) ? r.nextInt(maxEdges/2): 1 + r.nextInt(maxEdges);
            GNode n = new GNode(i);
            if(i < numSources && idxNV < listNonVisited.size()) {
                for(int j = 0; j < numEdges && idxNV < listNonVisited.size(); j++) {
                    int w = (isRandomWeight) ? 1 + r.nextInt(5) : 1;
                    n.addOutEdge(listNonVisited.get(idxNV++), w);
                }
            } else {
                List<Integer> availableNodes = new ArrayList<>();
                for(int j = (isAcyclic) ? i+1 : 0; j < numNodes; j++) {
                    availableNodes.add(j);
                }
                u.shuffle(availableNodes);
                for(int j = 0; j < numEdges && j < availableNodes.size(); j++) {
                    int w = (isRandomWeight) ? 1 + r.nextInt(5) : 1;
                    n.addOutEdge(availableNodes.get(j), w);
                }
            }
            list.add(n);
        }
        return list;
    }
    public void printGraph(List<GNode> l) {
        for(GNode n: l) {
            p("Node id:%2d numEdges:%2d\n", n.id, n.outEdges.size());
            for(GEdge e: n.outEdges) {
                p("    edge id:%2d w:%2d\n", e.idDst, e.w);
            }
        }
    }
    public List<GNode> djikstra(List<GNode> l, int idsrc, int iddst) {
        List<GNode> list = new ArrayList<>();
        return list;
    }
    public List<Integer> topologicalSort(List<GNode> l) {
        class C {
            Map<Integer, GNode> m;
            C(Map<Integer, GNode> m) {
                this.m = m;
            }
            void dfs(GNode n, List<Integer> list) {
                list.add(n.id);
                for(GEdge e: n.outEdges)
                    dfs(m.get(e.idDst), list);
            }
        }
        List<Integer> list = new ArrayList<>();
        Map<Integer, GNode> m = list2map(l);
        if(!isAcyclic(m)) {
            return list;
        }
        List<GNode> sources = getSources(m);
        C c = new C(m);
        for(GNode n: sources) {
            c.dfs(n, list);
        }
        return list;
    }
    public Map<Integer, GNode> list2map(List<GNode> l) {
        Map<Integer, GNode> m = new HashMap<>();
        for(GNode n: l) {
            m.put(n.id, n);
        }
        return m;
    }
    public boolean dfs(GNode n, Set<Integer> set, Map<Integer, GNode> m) {
        if(n == null)
            return true;
        if(set.contains(n.id))
            return false;
        set.add(n.id);
        for(GEdge e: n.outEdges)
            if(!dfs(m.get(e.idDst), set, m))
                return false;
        return true;
    }
    public boolean isAcyclic(Map<Integer, GNode> m) {
        List<GNode> sources = getSources(m);
        for(GNode n: sources) {
            Set<Integer> set = new HashSet<>();
            if(!dfs(n, set, m))
                return false;
        }
        return true;
    }
    /**
     * a source is a node with no incoming edges
     */
    public List<GNode> getSources(List<GNode> l) {
        List<GNode> list = new ArrayList<>();
        Map<Integer, GNode> map = new HashMap<>();
        for(GNode n: l) {
            map.put(n.id, n);
        }
        for(GNode n: l) {
            for(GEdge e: n.outEdges) {
                map.remove(e.idDst);
            }
        }
        for(Map.Entry<Integer, GNode> kv: map.entrySet()) {
            list.add(kv.getValue());
        }
        return list;
    }
    public List<GNode> getSources(Map<Integer, GNode> m) {
        List<GNode> list = new ArrayList<>();
        Map<Integer, GNode> map = new HashMap<>();
        for(GNode n: m.values()) {
            map.put(n.id, n);
        }
        for(GNode n: m.values()) {
            for(GEdge e: n.outEdges) {
                map.remove(e.idDst);
            }
        }
        for(Map.Entry<Integer, GNode> kv: map.entrySet()) {
            list.add(kv.getValue());
        }
        return list;
    }
    public static class NodeIT {
        public int beg;
        public int end;
        public int max;
        public NodeIT l = null;
        public NodeIT r = null;
        public NodeIT(int beg, int end) {
            this.beg = beg;
            this.end = end;
        }
    }
    /**
     * interval tree adds intervals to tree.
     * then query
     *   how many nodes intersect a value? return list
     *   how many nodes intersect a range? return list
     *
     *
     */
    public static class IntervalTree {
        NodeIT root = null;
        public void put(int beg, int end) {
            NodeIT n = new NodeIT(beg, end);
            if(root == null) {
                n.max = end;
                root = n;
                return;
            }
            NodeIT p = root;
            int max = (p.max > end) ? p.max : end;
            while(p != null) {
                p.max = max;
                if     (p.beg <= beg) {
                    if(p.r == null) {
                        p.r = n;
                        return;
                    }
                    p = p.r;
                }
                else if(p.beg > beg) {
                    if(p.l == null) {
                        p.l = n;
                        return;
                    }
                    p = p.l;
                }
            }
        }
        public List<NodeIT> get(int v) {
            List<NodeIT> l = new ArrayList<>();
            get(v, v, root, l);
            return l;
        }
        public List<NodeIT> get(int beg, int end) {
            List<NodeIT> l = new ArrayList<>();
            get(beg, end, root, l);
            return l;
        }
        /**
         *                  n       n
         *                  .       .
         *                  b       e
         *          b e                         1
         *          b           e               2
         *          b                   e       3
         *                      b e             4
         *                      b       e       5
         *                              b e     6
         */
        void get(int b, int e, NodeIT n, List<NodeIT> l) {
            if(n == null) return;
            if(n.beg <= b && b <= n.end || n.beg <= e && e <= n.end)
                l.add(n);
            if     (n.beg <= b)
                get(b,e,n.r,l);
            else
                get(b,e,n.l,l);
        }
    }
    /**
     * Heap aka PriorityQueue
     */
    public static class Heap {
        final boolean isMin;
        int [] l = null;
        int max = 8;
        int size = 0;
        public Heap(boolean isMinHeap) {
            this.isMin = isMinHeap;
            l = new int[max];
        }
        /**
         * add item always adds to back of list, then swim up.
         */
        public void add(int v) {
            increaseHeap();
            l[size++] = v;
            rise();
        }
        public Integer peek() {
            if(size == 0) return null;
            return l[0];
        }
        public int size() {
            return size;
        }
        /**
         * pop always removes from top of list, then moves last item
         * top top, and sinks down.
         */
        public Integer pop() {
            if(size == 0) return null;
            int v = l[0];
            l[0] = l[--size];
            sink();
            return v;
        }
        /**
         * sink is used for pop, where top element is popped, then
         * last element is put on top's place, and allowed to sink.
         *
         * sink until parent is greater than v, by swapping.
         *
         * if minheap, then swap downward the min of left or right.
         * if maxheap, then swap downward the max of left or right.
         *
         *
         * 0 1 2 3 4 5 6 7 8 9
         *
         *
         *                       0
         *               1               2
         *           3       4       5       6
         *         7   8   9   10 11  12  13   14
         *
         *
         */
        void sink() {
            if(size == 0) return;
            int idx = 0;
            int v = l[0];
            while(idx < size) {
                int idxLC = 2 * idx + 1;
                int idxRC = 2 * idx + 2;
                int idxswap = -1;
                if(idxLC >= size && idxRC >= size) return;
                if(isMin) {
                    idxswap = (idxRC < size) ?
                            (l[idxLC] < l[idxRC] ?
                                    v > l[idxLC] ? idxLC : -1 :
                                    v > l[idxRC] ? idxRC : -1) :
                            (v > l[idxLC] ? idxLC : -1);
                } else {
                    idxswap = (idxRC < size) ?
                            (l[idxLC] > l[idxRC] ?
                                    v < l[idxLC] ? idxLC : -1 :
                                    v < l[idxRC] ? idxRC : -1) :
                            (v < l[idxLC] ? idxLC : -1);
                }
                if(idxswap == -1) return;
                swap(idx, idxswap, l);
                idx = idxswap;
            }
        }
        /**
         * rise is used for add, where add is put to bottom of heap,
         * then keep rising via swaps.
         *
         * if minheap, then swap upward until parent is less than itself.
         * if maxheap, then swap upward until parent is greater than itself.
         *
         * 0 1 2 3 4 5 6 7 8 9
         *
         *
         *                       0
         *               1               2
         *           3       4       5       6
         *         7   8   9   10 11  12  13   14
         *
         */
        void rise() {
            if(size <= 1) return;
            int idx = size-1;
            int v = l[idx];
            while(idx > 0) {
                int idxP = (idx-1)/2;
                if((isMin && l[idxP] < v) || (!isMin && l[idxP] > v)) {
                    swap(idx, idxP, l);
                    idx = idxP;
                }
                else return;
            }
        }
        void increaseHeap() {
            if(size >= max) {
                max = max * 2;
                int [] t = new int[max];
                for(int i = 0; i < size; i++) {
                    t[i] = l[i];
                }
                l = t;
            }
        }
        void swap(int i, int j, int [] a) {
            int v = a[i];
            a[i] = a[j];
            a[j] = v;
        }
    }
    public static class SortedIntervalList {
        public SortedIntervalList(List<NodeIT> l) {

        }
    }
    public static class IntegerList implements Iterable<Integer> {
        Integer [] a;
        int max = 10;
        int index = 0;
        public IntegerList() {
            a = new Integer[max];
        }
        public void add(Integer v) {
            if(index >= max) {
                max = max * 2;
                Integer [] t = new Integer[max];
                for(int i = 0; i < index; i++)
                    t[i] = a[i];
                a = t;
            }
            a[index++] = v;
        }
        public Iterator<Integer> iterator() {
            return new Iterator<Integer>() {
                int idx = 0;
                public Integer next() {
                    if(!hasNext()) return null;
                    Integer v = a[idx++];
                    return v;
                }
                public boolean hasNext() {
                    return idx < index;
                }
            };
        }
    }
    public static double medianTwoSortedArrays(int [] l1, int [] l2) {
        int cnt = 0;
        int size = l1.length + l2.length;
        int target = size/2;
        int lastUpdated = 0;
        double result = 0.0;
        for(int i=0,j=0;;) {
            if        (l1[i] < l2[j]) {
                if(i < l1.length){
                    i++;
                    lastUpdated = 0;
                } else {
                    j++;
                    lastUpdated = 1;
                }
                cnt++;
            } else if (l1[i] > l2[j]) {
                if(j < l2.length) {
                    j++;
                    lastUpdated = 1;
                } else {
                    i++;
                    lastUpdated = 0;
                }
                cnt++;
            } else {
                if(i < l1.length) {
                    i++;
                    cnt++;
                    lastUpdated = 0;
                }
                if(j < l2.length) {
                    j++;
                    cnt++;
                    lastUpdated = 1;
                }
            }
            if(cnt == target) {
                if(size % 2 == 1) {
                    result = (lastUpdated == 0) ? l1[i] : l2[j];
                } else {
                    if(lastUpdated == 0) {
                        result = l1[i];
                    } else {
                        result = l2[j];
                    }
                }
                break;
            }
        }
        return result;
    }
}

class Schedule {
    private static String scheduleSlabs1(int [] varray, int interval) {
        Map<Integer,Integer> map = new HashMap<>();
        List<Integer> deleteNodes = new ArrayList<>();

        for(int i = 0; i < varray.length; i++){
            int v = varray[i];
            if(map.containsKey(v)) map.put(v, map.get(v)+1);
            else map.put(v, 1);
        }

        StringBuffer result = new StringBuffer();

        StringBuffer subset = new StringBuffer();

        while(map.size() != 0) {
            int cnt = 0;
            subset.setLength(0);
            deleteNodes.clear();
            for(Integer k: map.keySet()) {
                subset.append(String.valueOf(k));
                int decval = map.get(k) - 1;
                map.put(k, decval);
                if(decval == 0) deleteNodes.add(k);
                cnt++;
                if(cnt == interval) {
                    result.append(subset.toString());
                    subset.setLength(0);
                    cnt = 0;
                }
            }
            if(deleteNodes.size() != 0) {
                for(Integer k: deleteNodes) map.remove(k);
            }
            if(cnt != 0) {
                if(map.size() != 0) {
                    while(cnt != interval) {
                        subset.append('-');
                        cnt++;
                    }
                }
                result.append(subset.toString());
            }
        }

        return result.toString();
    }
    private static String scheduleSlabs2(int [] varray, int interval) {
        Map<Integer,IntPair> mapPairs = new HashMap<>();
        LinkedList<IntPair> listPairs = new LinkedList<>();
        List<IntPair> deleteNodes = new ArrayList<>();

        for(int i = 0; i < varray.length; i++){
            int k = varray[i];
            if(mapPairs.containsKey(k)) {
                mapPairs.get(k).v2++;
            } else {
                IntPair intPair = new IntPair(k, 1);
                listPairs.add(intPair);
                mapPairs.put(k, intPair);
            }
        }

        StringBuffer result = new StringBuffer();

        StringBuffer subset = new StringBuffer();

        while(listPairs.size() != 0) {
            subset.setLength(0);
            deleteNodes.clear();
            for(IntPair intPair: listPairs) {

                if(listPairs.size() < interval && result.length() > 0) {
                    int numPrevPositions = 0;
                    for(int i = result.length()-1; i >= 0; i--) {
                        numPrevPositions++;
                        if(result.charAt(i) == String.valueOf(intPair.v1).charAt(0)) {
                            break;
                        }
                        if(numPrevPositions > interval) break;
                    }
                    while(numPrevPositions > interval) {
                        subset.append('-');
                        numPrevPositions--;
                    }
                }

                subset.append(String.valueOf(intPair.v1));
                intPair.v2--;
                if(intPair.v2 == 0) deleteNodes.add(intPair);
                if(subset.length() == interval) {
                    result.append(subset.toString());
                    subset.setLength(0);
                }
            }
            if(deleteNodes.size() != 0) {
                for(IntPair intPair: deleteNodes) {
                    listPairs.remove(intPair);
                }
            }
            if(subset.length() != 0) {
                if(listPairs.size() != 0) {
                    while(subset.length() != interval) {
                        subset.append('-');
                    }
                }
                result.append(subset.toString());
            }
        }

        return result.toString();
    }

    private static String scheduleSlabs2(String s, int interval) {
        Map<Character, CharIntPair> map = new HashMap<>();
        LinkedList<CharIntPair> listPairs = new LinkedList<>();
        List<CharIntPair> deleteNodes = new ArrayList<>();

        for(int i = 0; i < s.length(); i++){
            char k = s.charAt(i);
            if(map.containsKey(k)) {
                map.get(k).v2++;
            } else {
                CharIntPair pair = new CharIntPair(k, 1);
                listPairs.add(pair);
                map.put(k, pair);
            }
        }

        StringBuffer result = new StringBuffer();
        StringBuffer subset = new StringBuffer();

        while(listPairs.size() != 0) {
            subset.setLength(0);
            deleteNodes.clear();
            if(listPairs.size() <= interval && result.length() > 0) {
                int numPrevPositions = 0;
                for(int i = result.length()-1;
                    i >= 0 && numPrevPositions < interval && result.charAt(i) != listPairs.get(0).v1;
                    i--)
                    numPrevPositions++;
                while(numPrevPositions < interval) {
                    subset.append('-');
                    numPrevPositions++;
                }
            }
            for(CharIntPair pair: listPairs) {
                subset.append(pair.v1);
                pair.v2--;
                if(pair.v2 == 0){
                    deleteNodes.add(pair);
                }
                if(subset.length() == interval) {
                    result.append(subset.toString());
                    subset.setLength(0);
                }
            }
            for(CharIntPair pair: deleteNodes) {
                listPairs.remove(pair);
            }
            if(subset.length() != 0) {
                result.append(subset.toString());
            }
        }

        return result.toString();
    }
    private static String scheduleSlabs3(String s, int interval) {
        Map<Character, Pair<Character,Integer>> map = new HashMap<>();
        LinkedList<Pair<Character,Integer>> listPairs = new LinkedList<>();
        List<Pair<Character,Integer>> deleteNodes = new ArrayList<>();

        for(int i = 0; i < s.length(); i++){
            char k = s.charAt(i);
            if(map.containsKey(k)) {
                map.get(k).v2++;
            } else {
                Pair<Character,Integer> pair = new Pair<>(k, 1);
                listPairs.add(pair);
                map.put(k, pair);
            }
        }

        StringBuffer result = new StringBuffer();
        StringBuffer subset = new StringBuffer();

        while(listPairs.size() != 0) {
            subset.setLength(0);
            deleteNodes.clear();
            if(listPairs.size() <= interval && result.length() > 0) {
                int numPrevPositions = 0;
                for(int i = result.length()-1;
                    i >= 0 && numPrevPositions < interval && result.charAt(i) != listPairs.get(0).v1;
                    i--)
                    numPrevPositions++;
                while(numPrevPositions < interval) {
                    subset.append('-');
                    numPrevPositions++;
                }
            }
            for(Pair<Character,Integer> pair: listPairs) {
                subset.append(pair.v1);
                pair.v2--;
                if(pair.v2 == 0){
                    deleteNodes.add(pair);
                }
                if(subset.length() == interval) {
                    result.append(subset.toString());
                    subset.setLength(0);
                }
            }
            for(Pair<Character,Integer> pair: deleteNodes) {
                listPairs.remove(pair);
            }
            if(subset.length() != 0) {
                result.append(subset.toString());
            }
        }

        return result.toString();
    }
    public static String scheduleSlabs(int [] varray, int interval) {
        return scheduleSlabs2(varray, interval);
    }

    public static String scheduleSlabs(String s, int interval) {
        return scheduleSlabs2(s, interval);
    }

}

class AlgosHelper {
    public int palindromeHelper(String s, int i, int j) {
        if(i < 0 || j >= s.length()) {
            return 0;
        }
        // x y
        // x y x
        int ctr = j-i-1;
        while(i >= 0 && j <= s.length()) {
            if(s.charAt(i) == s.charAt(j)) {
                ctr += 2;
            }
            else {
                return ctr;
            }
        }
        return ctr;
    }
}

/**
 * https://leetcode.com/problems/integer-to-english-words/
 * convert non negative integer to english word rep (first letter is upper case)
 *
 *
 * 00 - 13 maps uniquely
 * 14 - 19 maps X + teen
 * 20-90
 * 21 - 29 maps twenty + 1-9
 * 100
 * 1_000 - 9_000
 * 10_000 - 90_000
 * 100_000 - 900_000
 * 1_000_000 - 9_000_000
 * 1_000_000_000
 */
class IntegerToEnglish {
    Map<Integer,String> map = new HashMap<>();
    Map<Integer,String> mapCount = new HashMap<>();
    IntegerToEnglish() {
        init();
    }
    void init() {
        //map.put(0,"zero");
        map.put(1,"one");
        map.put(2,"two");
        map.put(3,"three");
        map.put(4,"four");
        map.put(5,"five");
        map.put(6,"six");
        map.put(7,"seven");
        map.put(8,"eight");
        map.put(9,"nine");
        map.put(10,"ten");
        map.put(11,"eleven");
        map.put(12,"twelve");
        map.put(13,"thirteen");
        map.put(14,"fourteen");
        map.put(15,"fifteen");
        map.put(20,"twenty");
        map.put(30,"thirty");
        map.put(100,"hundred");
        map.put(1000,"thousand");
        map.put(1000000,"million");
        map.put(1000000000,"billion");
        mapCount.put(2,"hundred");
        mapCount.put(1,"");
        mapCount.put(0,"");
    }
    public String i2ev1(int idata) {
        int v = Integer.MAX_VALUE;
        int modv = 1_000_000_000;
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while(modv != 1) {
            int r = idata % modv;
            int m = (idata - r) / modv;
            if(m == 0) continue;
            String s = map.get(m);
            if(s == null) continue;
            sb.append(String.format("%s %s ", s, mapCount.get(i)));
            modv = modv / 10;
            i = ++i % 3;
        }
        return sb.toString();
    }
    static void p(String f, Object ...o) {
        System.out.printf(f, o);
    }
    public String i2ev2(int idata) {
        p("%d : ", idata);
        int moddata = idata;
        if(idata == 0) return "zero";
        List<String> l = new ArrayList<>();
        int i = 0;
        while(true) {
            int r1000 = moddata % 1000;
            moddata = (moddata - r1000) / 1000;
            int r100 = r1000 % 100;
            int q = (r1000 - r100) / 100;
            String result = helperSub100(q, l);
            if(q != 0) { result = map.get(q) + " hundred"; }
            result = (result != null) ?  (result + " " + helperSub100(r100, l)) : (helperSub100(r100, l));
            if(i == 1) {
                if(result != null) result += " thousand";
            } else if(i == 2) {
                if(result != null) result += " million";
            } else if(i == 3) {
                if(result != null) result += " billion";
            }
            if(result != null) l.add(0, result);
            i++;
            if(i == 4 || moddata == 0) break;
        }
        StringBuilder sb = new StringBuilder();
        for(String s: l) {
            sb.append(String.format("%s ", s));
        }
        return sb.toString();
    }
    private String helperSub100(int r, List<String> l) {
        String result = null;
        if       (r <= 15) {
            result = map.get(r);
        } else if(r <= 19) {
            int r1 = r - 10;
            result = map.get(r1) + ((r1 == 8) ? "een" : "teen");
        } else if(r < 40) {
            int r1 = r % 10;
            int q = r - r1;
            result = (r1 == 0) ? map.get(q) : map.get(q) + " " + map.get(r1);
        } else {
            int r1 = r % 10;
            int q = (r - r1)/10;
            result = map.get(q) + ((q == 8) ? "y" : "ty");
            if(r1 != 0) result = result + " " + map.get(r1);
        }
        return result;
    }

}

class IntPair {
    public int v1;
    public int v2;
    public IntPair(int v1, int v2) {
        this.v1 = v1;
        this.v2 = v2;
    }
}

class CharIntPair {
    public char v1;
    public int v2;
    public CharIntPair(char v1, int v2) {
        this.v1 = v1;
        this.v2 = v2;
    }

}

class Pair<X,Y> {
    public X v1;
    public Y v2;
    public Pair(X v1, Y v2) {
        this.v1 = v1;
        this.v2 = v2;
    }
}

class IntNodeSkipList {

}

