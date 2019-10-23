package org.wayne.misc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

public class StructuresTest extends TestCase {
    Utils u = new Utils();
    Structures t = new Structures();
    
    public void testLIS1() {
        List<Integer> l = null;
        l = Arrays.asList(20,22,10,25,21,27,28);
        t.longestIncreasingSubsequence(l);
        u.p("\n");

        l = Arrays.asList(20,26,10,15,25,21,27,28);
        t.longestIncreasingSubsequence(l);
        u.p("\n");

        l = Arrays.asList(10,12,24,26,13,15,25,21,30,33,27,29,31);
        t.longestIncreasingSubsequence(l);
        u.p("\n");

        l = Arrays.asList(10,12,24,26,13,15,25,21,30,6,27,29,8);
        t.longestIncreasingSubsequence(l);
        u.p("\n");

        //                 1  2 1 2  3  4  5  4  5  6  6  7 3 4  8
        l = Arrays.asList(10,12,1,3,13,40,45,15,18,60,47,48,4,6,50);
        t.longestIncreasingSubsequence(l);
        u.p("\n");

        //                 1  1  2  2  3  4  2, 3  4  5  6
        l = Arrays.asList(80,20,40,30,50,70,25,30,35,50,55);
        t.longestIncreasingSubsequence(l);
        u.p("\n");

        l = Arrays.asList(80,81,82,83,84,40,41,42,43,85,20,21,22,44,45,46);
        t.longestIncreasingSubsequence(l);
        u.p("\n");

        l = Arrays.asList(80,81,82,83,84,40,41,42,43,85,20,21,22,44);
        t.longestIncreasingSubsequence(l);
        u.p("\n");

        l = Arrays.asList(80,81,82,83,40,41,42,43,84,20,21,22,44,85,45,46);
        t.longestIncreasingSubsequence(l);
        u.p("\n");

        l = Arrays.asList(80,81,82,83,40,41,42,43,84,20,21,22,44,85,45);
        t.longestIncreasingSubsequence(l);
        u.p("\n");
    }
    
    public void testLISSingle() {
        List<Integer> l = null;

        l = Arrays.asList(80,20,40,30,50,70,25,30,35,50,55);
        t.longestIncreasingSubsequence(l);
        u.p("\n");
    }
    
    public void testMemoryAllocator() {
        int maxMemSize = 256;
        int blockSize = 4;

        Structures.MemoryAllocator memmap = new Structures.MemoryAllocator(
            maxMemSize, blockSize, Structures.MemoryAllocator.MemType.BITMAP);

        Structures.MemoryAllocator memlist = new Structures.MemoryAllocator(
            maxMemSize, blockSize, Structures.MemoryAllocator.MemType.LIST);
        
        List<Structures.MemBlock> listBlocks = new ArrayList<>();
    }
    
}
