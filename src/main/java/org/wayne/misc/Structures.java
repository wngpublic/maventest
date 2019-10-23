package org.wayne.misc;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Structures {

    Utils u = new Utils();
    boolean dbg = true;
    
    public void debug(boolean dbg) {
        this.dbg = dbg;
    }
    
    public static class LRUCache {
        
    }
    
    public List<Integer> longestIncreasingSubsequence(List<Integer> l) {

        class LIS {
            
            int dbgctr = 0;
            
            public List<Integer> lisBrute(List<Integer> l) {
                int size = l.size();
                int [] aCount = new int[size]; // initialized as 0
                int idxmax = 0;
                int max = 0;
                dbgctr = 0;
                
                LinkedList<Integer> lret = new LinkedList<>();
                int [] ap = new int[size];
                
                for(int i = 0; i < size; i++) {
                    dbgctr++;
                    int v = l.get(i);
                    /*
                     * look for any element before it that is less than V
                     * then look at aCount to see its count, increment it for V
                     * and choose best count so far
                     */
                    int bestCount = 0;
                    int idxp = -1;
                    for(int j = 0; j < i; j++) {
                        dbgctr++;
                        int p = l.get(j);
                        if(p < v) {
                            int count = aCount[j];
                            if(count > bestCount) {
                                bestCount = count;
                                idxp = j;
                            }
                        }
                    }
                    bestCount++;
                    aCount[i] = bestCount;
                    ap[i] = idxp;
                    if(bestCount > max) {
                        max = bestCount;
                        idxmax = i;
                    }
                }
                
                // construct the values
                for(int i = idxmax; i >= 0; i = (i == 0) ? -1 : ap[i]) {
                    lret.addFirst(l.get(i));
                }
                
                if(dbg) {
                    StringBuilder sb0 = new StringBuilder();
                    StringBuilder sb1 = new StringBuilder();
                    StringBuilder sb2 = new StringBuilder();
                    StringBuilder sb3 = new StringBuilder();
                    StringBuilder sb4 = new StringBuilder();
                    
                    for(int i = 0; i < size; i++) {
                        sb0.append(String.format("%2d ", i));
                        sb1.append(String.format("%2d ", aCount[i]));
                        sb2.append(String.format("%2d ", l.get(i)));
                        sb4.append(String.format("%2d ", ap[i]));
                    }
                    
                    for(int i = 0; i < lret.size(); i++) {
                        sb3.append(String.format("%2d ", lret.get(i)));
                    }
                    
                    u.p("--------------------------------------\n");
                    u.p("acount index: %s\n", sb0.toString());
                    u.p("input  value: %s\n", sb2.toString());
                    u.p("aCount count: %s\n", sb1.toString());
                    u.p("prv    index: %s\n", sb4.toString());
                    u.p("list  values: %s\n", sb3.toString());
                }
                
                return lret;
            }
            
            public List<Integer> lisRecursive(List<Integer> l) {
                List<Integer> lret = new ArrayList<>();
                return lret;
            }

            public List<Integer> lisRecursiveWithMemory(List<Integer> l) {
                List<Integer> lret = new ArrayList<>();
                return lret;
            }

            public List<Integer> lisDouble(List<Integer> l) {
                List<Integer> lret = new ArrayList<>();
                return lret;
            }

            public List<Integer> lisOpt1(List<Integer> l) {
                int size = l.size();
                /*
                 * array to track count of individual subsequences
                 * array to track individual subsequences
                 * array to track previous idx of this sequence
                 */
                int [] aprv = new int[size];
                int [] amid = new int[size];
                int L = 0;
                
                for(int i = 0; i < aprv.length; i++) {
                    aprv[i] = -1;
                }
                for(int i = 0; i < amid.length; i++) {
                    amid[i] = -1;
                }
                
                dbgctr = 0;
                if(dbg) {
                    StringBuilder sb0 = new StringBuilder();
                    StringBuilder sb1 = new StringBuilder();
                    
                    for(int i = 0; i < size; i++) {
                        sb0.append(String.format("%2d ", i));
                        sb1.append(String.format("%2d ", l.get(i)));
                    }
                    
                    u.p("--------------------------------------------------------------\n");
                    u.p("acount index: %s\n", sb0.toString());
                    u.p("input  value: %s\n", sb1.toString());
                }
                
                for(int i = 0; i < size; i++) {
                    dbgctr++;
                    int lo = 1;
                    int hi = L;
                    int v = l.get(i);
                    
                    // binary search in l 
                    while(lo <= hi) {
                        dbgctr++;
                        int mid = (lo+hi)/2;
                        int idxmid = amid[mid];
                        int vmid = l.get(idxmid);
                        u.p("    binsearch lo:%2d hi:%2d amid[%2d]:%2d v:%2d vmid:%2d\n", lo, hi, mid, idxmid, v, vmid);
                        if(vmid < v) {
                            lo = mid+1;
                        } else {
                            hi = mid-1;
                        }
                    }
                    

                    /*
                     * pass current mid to historical tracking. 
                     * amid can always change, but aprv cannot.
                     */
                    aprv[i] = (lo == 0) ? -1 : amid[lo-1];
                    amid[lo] = i;
                    
                    if(lo > L) {
                        L = lo;
                    }

                    {
                        u.p("dbg: i:%2d L=%2d lo=%2d aprv[%2d]=%2d amid[%2d]=%2d\n",
                                i, L, lo, 
                                i, aprv[i], 
                                lo, amid[lo]);
                        StringBuilder sb0 = new StringBuilder();
                        StringBuilder sb1 = new StringBuilder();
                        StringBuilder sb2 = new StringBuilder();
                        StringBuilder sb3 = new StringBuilder();
                        for(int j = 0; j < aprv.length; j++) {
                            sb0.append(String.format("%2d ", aprv[j]));
                            sb1.append(String.format("%2d ", amid[j]));
                            sb2.append(String.format("%2d ", (aprv[j] == -1) ? -1 : l.get(aprv[j])));
                            sb3.append(String.format("%2d ", (amid[j] == -1) ? -1 : l.get(amid[j])));
                        }
                        u.p("                                                      aprv: %s     amid: %s\n", sb0.toString(), sb1.toString());
                        u.p("                                                       val: %s      val: %s\n", sb2.toString(), sb3.toString());
                    }
                }
                
                LinkedList<Integer> lret = new LinkedList<>();
                // get the largest chain, L, from traversing aprv indices
                for(int i = amid[L]; 
                    i >= 0; 
                    i = (i == 0) ? -1 : aprv[i]) {
                    lret.addFirst(l.get(i));
                }
                
                if(dbg) {
                    StringBuilder sb0 = new StringBuilder();
                    StringBuilder sb1 = new StringBuilder();
                    StringBuilder sb2 = new StringBuilder();
                    StringBuilder sb3 = new StringBuilder();
                    StringBuilder sb4 = new StringBuilder();
                    StringBuilder sb5 = new StringBuilder();
                    StringBuilder sb6 = new StringBuilder();
                    
                    for(int i = 0; i < size; i++) {
                        sb0.append(String.format("%2d ", i));
                        sb1.append(String.format("%2d ", l.get(i)));
                        sb2.append(String.format("%2d ", amid[i]));
                        sb3.append(String.format("%2d ", aprv[i]));
                        if(amid[i] == -1) {
                            sb5.append(String.format("-1 "));
                        } else {
                            sb5.append(String.format("%2d ", l.get(amid[i])));
                        }
                        if(aprv[i] == -1) {
                            sb6.append(String.format("-1 "));
                        } else {
                            sb6.append(String.format("%2d ", l.get(aprv[i])));
                        }
                    }
                    
                    for(int i = 0; i < lret.size(); i++) {
                        sb4.append(String.format("%2d ", lret.get(i)));
                    }
                    
                    u.p("--------------------------------------\n");
                    u.p("acount index: %s\n", sb0.toString());
                    u.p("input  value: %s\n", sb1.toString());
                    u.p("mid      idx: %s\n", sb2.toString());
                    u.p("amid  values: %s\n", sb5.toString());
                    u.p("prv    index: %s\n", sb3.toString());
                    u.p("prv   values: %s\n", sb6.toString());
                    u.p("list  values: %s\n", sb4.toString());
                }                
                return lret;
            }

            public List<Integer> lisOpt2(List<Integer> l) {
                /*
                int size = l.size();
                // array to track count of individual subsequences
                int [] aCount = new int[size]; // initialized as 0
                // array to track individual subsequences
                int [] aSubSequence = new int[size]; 
                int idxmax = 0;
                int max = 0;
                dbgctr = 0;
                
                LinkedList<Integer> lret = new LinkedList<>();
                int [] ap = new int[size];
                return lret;
                */
                return null;
            }
        }
        
        LIS lis = new LIS();
        int type = 2;
        if(type == 0) {
            return lis.lisBrute(l);
        }
        else if(type == 1) {
            return lis.lisDouble(l);
        }
        else if(type == 2) {
            return lis.lisOpt1(l);
        }
        else if(type == 3) {
            return lis.lisRecursive(l);
        }
        else if(type == 4) {
            return lis.lisRecursiveWithMemory(l);
        }
        else if(type == 5) {
            return lis.lisOpt2(l);
        }
        return lis.lisBrute(l);
    }

    /**
     * MemBlock is used by MemoryAllocator.
     */
    public static class MemBlock {
        final private int offset;
        final private int size;
        public MemBlock(int offset, int size) {
            this.offset = offset;
            this.size = size;
        }
        public int offset() {
            return offset;
        }
        public int size() {
            return size;
        }
    }
    
    /**
     * MemoryAllocator.
     *
     */
    public static class MemoryAllocator {
        
        class MemBitMap {
            private final int maxMemSize;
            private final int blockSize;
            private final long [] bitmap;
            int head = 0;
            int tail = 0;
            
            public MemBitMap(int maxMemSize, int blockSize) {
                this.maxMemSize = maxMemSize;
                this.blockSize = blockSize;
                
                int sizebitmap = 1 + (maxMemSize / blockSize);
                int numLongs = 1 + (sizebitmap / 64);
                bitmap = new long[numLongs];
            }

            public int maxMemSize() {
                return maxMemSize;
            }
            
            public int blockSize() {
                return blockSize;
            }
            
            public MemBlock allocate(int numBytes) {
                int numBlocks = bytesToBlocks(numBytes);
                return allocateBlock(numBlocks);
            }
            
            private int bytesToBlocks(int numBytes) {
                int mod = numBytes % blockSize;
                int numBlocks = (mod == 0) ?
                        (numBytes / blockSize) :
                        ((numBytes + blockSize) / blockSize);
                return numBlocks;
            }
            private MemBlock allocateBlock(int numBlocks) {
                return null;
            }
            
            public void deallocate(MemBlock memBlock) {
                
            }
        }
        
        class MemList {
            private final int maxMemSize;
            private final int blockSize;
            public MemList(int maxMemSize, int blockSize) {
                this.maxMemSize = maxMemSize;
                this.blockSize = blockSize;
            }
            public int maxMemSize() {
                return maxMemSize;
            }
            public int blockSize() {
                return blockSize;
            }
            public MemBlock allocate(int numBytes) {
                return null;
            }
            public void deallocate(MemBlock memBlock) {
                
            }
        }
        
        enum MemType {
            BITMAP,
            LIST
        }
        
        private final int maxMemSize;
        private final int blockSize;
        private final MemType type;
        MemBitMap memBitMap;
        MemList memList;

        public MemoryAllocator(
            int maxMemSize,
            int blockSize,
            MemType type) 
        {
            this.maxMemSize = maxMemSize;
            this.blockSize = blockSize;
            this.type = type;
            
            memBitMap = new MemBitMap(maxMemSize, blockSize);
            memList = new MemList(maxMemSize, blockSize);
        }
        
        public int maxMemSize() {
            return maxMemSize;
        }
        
        public int blockSize() {
            return blockSize;
        }
        
        public MemBlock allocate(int numBytes) {
            if(type == MemType.BITMAP) {
                return memBitMap.allocate(numBytes);
            }
            else {
                return memList.allocate(numBytes);
            }
        }
        
        public void deallocate(MemBlock memBlock) {
            if(type == MemType.BITMAP) {
                memBitMap.deallocate(memBlock);
            }
            else {
                memList.deallocate(memBlock);
            }
        }
        
    }
}
