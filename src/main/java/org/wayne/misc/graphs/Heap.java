package org.wayne.misc.graphs;

public class Heap {
    /**
     * Fibonacci heap is collection of min trees, 
     * where each root is in a circular doubly linked list.
     * There is a pointer to min root at any time.
     * 
     * Operations are 
     * insert O(1)
     * find min O(1)
     * delete min O(logn)
     * decrease key O(1)
     * merge O(1)
     * 
     * This is better than regular heap performance.
     * 
     * Each node has up to 4 pointers: lc, rc, prv, nxt
     */
    public static class FibonacciHeap {
        
    }
}
