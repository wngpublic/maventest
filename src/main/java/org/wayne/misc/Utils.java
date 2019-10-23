package org.wayne.misc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * table of methods:
 * 
 * int [][]             copy(int [][])
 * List<List<Integer>>  copy(List<List<Integer>>)
 * void                 copyArray(int [], int [])
 * void                 copyArray(int [], int [], int, int)
 * void                 copyList(List<Integer>, List<Integer>)
 * char []              getAryChar(int)
 * int []               getAryInt(int, int, int)
 * boolean              getBool()
 * getCharset()
 * getDiffChar()
 * getInt(int, int)
 * getIntListNormal(int, int, int)
 * getIntNormal(double, int, int, int)
 * getIntNormal(int, int)
 * getIntNotInList<List<Integer>, int, int)
 * getListInt(int, int, int)
 * getPrimesInRange(int)
 * getRandomMatrix(int, int, int, int)
 * getRandString(int)
 * getRandString(String, int)
 * shuffle(char [])
 * swap(List<Integer>, int, int)
 * 
 */
public class Utils extends UtilsGenerate {

}

class UtilsGenerate extends UtilsStreaming {
    
}

class UtilsStreaming extends UtilsData {

}

class UtilsData extends UtilsBasic {
    public List<Integer> getIntListNormal(int size, int min, int max) {
        /*
        * double * median = 1    -> -5:5
        * double * median = 10   -> -50:50
        * double * median = 100  -> -500:500
        * 
        */
        List<Integer> list = new ArrayList<>();
        double range = (max - min + 1)/10.0;
        int median = (min + max) / 2;
        for(int i = 0; i < size; i++) {
            int v = getIntNormal(range, median, min, max);
            list.add(v);
        }
        return list;
    }
    /**
    * normal distribution of 0 is -10:10
    * 10 distribution is -50:50
    * 2 distribution is -10:10
    */
    public int getIntNormal(int min, int max) {
        double range = (max - min + 1)/10.0;
        int median = (min + max) / 2;
        return getIntNormal(range, median, min, max);
    }
    
    public int getIntNormal(double range, int median, int min, int max) {
        /*
        * double * 1    -> -5:5
        * double * 10   -> -50:50
        * double * 100  -> -500:500
        */
        boolean found = false;
        for(int i = 0; i < 1000 && !found; i++) {
            int v = (int)(rand.nextGaussian() * range) + median;
            if(min <= v && v <= max) {
                return v;
            }
        }
        return median;
    }

    
}

class UtilsBasic {

    char [] charset;
    Random rand = new Random();

    public void print(String f, Object ...a) {
        System.out.printf(f, a);
    }

    public void p(String f, Object ...a) {
        print(f, a);
    }
    
    public void pl(Object o) {
        System.out.println(o);
    }

    public UtilsBasic() {
        reset();
    }
    
    public int [][] copy(int [][] a) {
        int numRows = a.length;
        int numCols = a[0].length;
        int [][] o = new int[numRows][numCols];
        for(int i = 0; i < numRows; i++) {
            for(int j = 0; j < numCols; j++) {
                o[i][j] = a[i][j];
            }
        }
        return o;
    }
    
    public void copyList(List<Integer> src, List<Integer> dst) {
        dst.clear();
        for(Integer i: src)
            dst.add(i);
    }
    
    public List<List<Integer>> copy(List<List<Integer>> list) {
        int numRows = list.size();
        List<List<Integer>> o = new ArrayList<>();
        for(int i = 0; i < numRows; i++) {
            List<Integer> row = new ArrayList<>(list.get(i));
            o.add(row);
        }
        return o;
    }
    
    public int [][] 
    initMatrixArray(int numRows, int numCols, int val) {
        int [][] a = new int[numRows][numCols];
        for(int i = 0; i < numRows; i++) {
            for(int j = 0; j < numCols; j++) {
                a[i][j] = val;
            }
        }
        return a;
    }
    
    public void printMatrix(int [][] a) {
        p("PRINT MATRIX\n");
        int szr = a.length;
        int szc = a[0].length;
        for(int i = 0; i < szr; i++) {
            for(int j = 0; j < szc; j++) {
                p("%2d ", a[i][j]);
            }
            p("\n");
        }
    }
    
    public void printMatrix(List<List<Integer>> list) {
        p("PRINT MATRIX\n");
        int numRows = list.size();
        for(int i = 0; i < numRows; i++) {
            List<Integer> row = list.get(i);
            for(int j = 0; j < row.size(); j++) {
                p("%3d ", row.get(j));
            }
            p("\n");
        }
    }
    
    public List<Integer>
    initList(int numCols, int val) {
        List<Integer> list = new ArrayList<>();
        for(int i = 0; i < numCols; i++)
            list.add(val);
        return list;
    }
    
    public List<List<Integer>> 
    initMatrixList(int numRows, int numCols, int val) {
        List<List<Integer>> list = new ArrayList<>();
        for(int i = 0; i < numRows; i++) {
            List<Integer> row = initList(numCols, val);
            list.add(row);
        }
        return list;
    }
    
    public List<List<Integer>> 
    getRandomMatrix(int numRows, int numCols, int min, int max) {
        List<List<Integer>> list = new ArrayList<>(numRows);
        int diff = max - min + 1;
        for(int i = 0; i < numRows; i++) {
            List<Integer> cols = new ArrayList<>(numCols);
            for(int j = 0; j < numCols; j++) {
                int v = rand.nextInt(diff) + min;
                cols.add(v);
            }
            list.add(cols);
        }
        return list;
    }
    
    public List<List<Integer>>
    matrixToList(int [][] a) {
        List<List<Integer>> list = new ArrayList<>();
        int numRows = a.length;
        int numCols = a[0].length;
        for(int i = 0; i < numRows; i++) {
            List<Integer> row = new ArrayList<>();
            for(int j = 0; j < numCols; j++) {
                row.add(a[i][j]);
            }
            list.add(row);
        }
        return list;
    }
    
    public int [][]
    listToMatrix(List<List<Integer>> list) {
        int numRows = list.size();
        int numCols = list.get(0).size();
        int [][] a = new int[numRows][numCols];
        for(int i = 0; i < numRows; i++){ 
            List<Integer> row = list.get(i);
            for(int j = 0; j < numCols; j++) {
                a[i][j] = row.get(j);
            }
        }
        return a;
    }
    
    public String getRandString(String stringSet, int sizeStr) {
        StringBuilder sb = new StringBuilder();
        int sizeSet = stringSet.length();
        for(int j = 0; j < sizeStr; j++) {
            int idx = rand.nextInt(sizeSet);
            sb.append(stringSet.charAt(idx));
        }
        String word = sb.toString();
        return word;
    }
    
    public String getRandString(int sizeStr) {
        StringBuilder sb = new StringBuilder();
        int sizeSet = charset.length;
        for(int i = 0; i < sizeStr; i++) {
            int idx = rand.nextInt(sizeSet);
            sb.append(charset[idx]);
        }
        String word = sb.toString();
        return word;
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    Comparable min(Comparable ...c) {
        if(c.length == 0) {
            return null;
        }
        Comparable min = c[0];
        for(Comparable v: c) {
            if(min.compareTo(v) > 0) {
                min = v;
            }
        }
        return min;
    }
    
    @SuppressWarnings({ "rawtypes" })
    public void printMinInts(Comparable ... c) {
        Comparable min = min(c);
        if(min == null) {
            p("null\n");
            return;
        }
        p("min:%2d; ", min);
        for(Comparable v: c) {
            p("%d ", v);
        }
        p("\n");
    }
    
    public int minint(int ... vargs) {
        int sz = vargs.length;
        if(sz == 0) {
            return -1;
        }
        int min = vargs[0];
        for(int i = 1; i < sz; i++) {
            int v = vargs[i];
            min = (min < v) ? min : v;
        }
        return min;
    }
    
    public int maxint(int ... vargs) {
        int sz = vargs.length;
        if(sz == 0) {
            return -1;
        }
        int max = vargs[0];
        for(int i = 1; i < sz; i++) {
            int v = vargs[i];
            max = (max > v) ? max : v;
        }
        return max;
    }
    
    public Integer getIntNotInList(List<Integer> l, int min, int max) {
        // gets a value that is within min:max and not in l, null if not found.
        Set<Integer> set = new HashSet<>();
        for(Integer v: l) {
            set.add(v);
        }
        for(int i = min; i < max; i++) {
            if(!set.contains(i)) {
                return i;
            }
        }
        return null;
    }
    
    public void setCharset(String s) {
        charset = s.toCharArray();
    }
    
    char [] getCharset() {
        return charset;
    }
    
    public void reset() {
        charset = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    }
    
    public void shuffle(char [] a, int numShuffles) {
        for(int i = 0; i < numShuffles; i++) {
            shuffle(a);
        }
    }
    
    public void shuffle(int [] a, int numShuffles) {
        for(int i = 0; i < numShuffles; i++) {
            shuffle(a);
        }
    }
    
    public void shuffle(char [] a) {
        for(int i = a.length; i > 0; i--) {
            int idx = rand.nextInt(i);
            char c = a[idx];
            a[idx] = a[i-1];
            a[i-1] = c;
        }
    }
    
    public void shuffle(int [] a) {
        for(int i = a.length; i > 0; i--) {
            int idx = rand.nextInt(i);
            int c = a[idx];
            a[idx] = a[i-1];
            a[i-1] = c;
        }        
    }
    
    public void shuffle(Object [] a) {
        for(int i = a.length; i > 0; i--) {
            int idx = rand.nextInt(i);
            Object c = a[idx];
            a[idx] = a[i-1];
            a[i-1] = c;
        }
    }
    
    /**
     * return [min,max]
     */
    public int getInt(int min, int max) {
        int range = max - min + 1;
        return rand.nextInt(range) + min;
    }

    public long getLong(long min, long max) {
        long range = max - min + 1;
        return (rand.nextLong() % range) + min;
    }

    /*
     * getInt distribution
     * int min
     * int max
     * int [][] distribution
     *     array of 3 values, [0]=min,[1]=max,[2]=weight
     *     the array must have cumulative weight, [2], = 100, else error and return 0
     *     min and max range are inclusive, and next index value must be greater than prev,
     *     and may have gaps between array indices.
     *
     *     version1 must be continuous and not have gaps.
     *     version2 does not have to be continuous
     *
     * example1 for non continuous (not implemented yet):
     *
     * min=10
     * max=90
     * distribution:    [0][0]  = 10    // 10 inclusive
     *                  [0][1]  = 20    // 20 inclusive
     *                  [0][2]  = 25    // 25% distribution
     *                  [1][0]  = 21    // min inclusive
     *                  [1][1]  = 21    // max inclusive
     *                  [1][2]  = 50    // 50% distribution
     *                  [2][0]  = 22
     *                  [2][1]  = 29
     *                  [2][2]  = 10    // 10%, total distribution is 85% now.
     *                  [3][0]  = 30
     *                  [3][1]  = 31
     *                  [3][2]  = 15    // 15%, so total distribution is 100 now, ok
     */
    public List<Integer> getInt(int min, int max, int [][] distribution, int numElements) throws Exception {
        return getIntDistributionV1(min, max, distribution, numElements);
    }

    public List<Integer> getInt(int min, int max, List<List<Integer>> distribution, int numElements) throws Exception {
        int [][] dist = new int[distribution.size()][];
        for(int i = 0; i < distribution.size(); i++) {
            List<Integer> l = distribution.get(i);
            dist[i] = new int[l.size()];
            for(int j = 0; j < l.size(); j++) {
                dist[i][j] = l.get(j);
            }
        }
        return getIntDistributionV1(min, max, dist, numElements);
    }

    private List<Integer> getIntDistributionV1(int min, int max, int [][] dist, int numElements) throws Exception {
        {
            // validate
            int sum = 0;
            for(int i = 0,
                prvmax = min;
                i < dist.length;
                i++)
            {
                if(dist[i][0] > dist[i][1]) {
                    throw new Exception(String.format("distval weight idx %d has min > max", i));
                };
                if(i == 0) {
                    if(dist[i][0] != min) {
                        throw new Exception(String.format("distval weight idx %d [0] != min @ 0", i));
                    }
                } else {
                    if(prvmax+1 != dist[i][0]) {
                        throw new Exception(String.format("distval weight idx %d not continuous", i));
                    }
                    if(dist[i][1] > max) {
                        throw new Exception(String.format("distval weight idx %d [1] > max", i));
                    }
                }
                prvmax = dist[i][1];
                sum += dist[i][2];
            }
            if(sum != 100) {
                for(int j = 0; j < dist.length; j++) {
                    System.out.printf("dist %2d %2d = %2d\n", dist[j][0],dist[j][1],dist[j][2]);
                }
                throw new Exception(String.format("distval weight != 100"));
            }
        }
        List<Integer> l = new ArrayList<>();
        /*
         * for numElements
         *   pick a distribution d from 0:99
         *     iterate through distribution table to find distribution index, eg
         *       if(sum(dist[0]:dist[i-1]) <= d <= dist[i])
         *         v = random number from dist[i][0]:dist[i][1],
         *         eg dist[i][0] = 10, dist[i][1] = 20, dist[i][2] = 30, so d falls into this distribution of 30,
         *         so choose a number between 10 and 20
         */
        for(int i = 0; i < numElements; i++) {
            int d = rand.nextInt(99);
            for(int j = 0, sum = 0, prv = 0; j < dist.length; j++) {
                prv = sum;
                sum += dist[j][2];
                if(prv <= d && d <= sum) {
                    if(dist[j][0] == dist[j][1]) {
                        l.add(dist[j][0]);
                        break;
                    }
                    int diffrange = dist[j][1] - dist[j][0];
                    int v = getInt(0,diffrange) + dist[j][0];
                    l.add(v);
                    break;
                }
            }
        }
        return l;
    }

    public boolean getBool() {
        int v = rand.nextInt(2);
        return v == 0;
    }
    public Random getRand() { return rand; }
    
    public int [] getAryInt(int sz, int min, int max) {
        int [] a = new int[sz];
        int range = max - min + 1;
        for(int i = 0; i < sz; i++) {
            a[i] = rand.nextInt(range) + min;
        }
        return a;
    }
    
    public List<Integer> getListInt(int sz, int min, int max) {
        List<Integer> l = new ArrayList<>();
        int range = max - min + 1;
        for(int i = 0; i < sz; i++) {
            int v = rand.nextInt(range) + min;
            l.add(v);
        }
        return l;
    }
    
    public char [] getAryChar(int sz) {
        char [] a = new char[sz];
        int range = charset.length;
        for(int i = 0; i < sz; i++) {
            a[i] = charset[rand.nextInt(range)];
        }
        return a;
    }
    
    /**
     * get a char from charset that is different from src
     */
    public char getDiffChar(char src, char [] charset) {
        int sz = charset.length;
        int idxstart = getInt(0, sz-1);
        for(int i = idxstart, ctr = 0; ctr < sz; ctr++) {
            char c = charset[i];
            if(c != src) {
                return c;
            }
            i = (i+1) % sz;
        }
        return src;
    }

    public char [] getDiffChars(char [] src, char [] charset) {
        return getDiffChars(0, src.length-1, src, charset);
    }

    public char [] getDiffChars(int idxs, int idxe, char [] src, char [] charset) {
        
        int sz = idxe - idxs + 1;
        
        char [] a = new char[sz];
        
        for(int i = 0, j = idxs; i < sz; i++, j++) {
            a[i] = getDiffChar(src[j], charset);
        }
        
        return a;
    }

    public void printArray(String msg, int [] a) {
        printArray(msg, a, true);
    }

    public void printArray(String msg, int [] a, boolean newline) {
        if(msg != null) {
            print("%s", msg);
        }
        for(int i = 0; i < a.length; i++) {
            print("%2d ", a[i]);
        }
        if(newline) {
            print("\n");
        }
    }

    public void printArray(int [] a) {
        printArray(null, a);
    }

    public void printArray(List<Integer> l) {
        for(int i = 0; i < l.size(); i++) {
            p(l.get(i) + " ");
        }
        p("\n");
    }

    public void printArray(int [] a, int idxB, int idxE) {
        for(int i = idxB; i <= idxE; i++) {
            print("%3d ", a[i]);
        }
        print("\n");
    }
    
    public void printListInt(List<Integer> l, boolean onePerLine) {
        if(onePerLine) {
            for(int i = 0; i < l.size(); i++) {
                p("%3d: %d\n", i, l.get(i));
            }
        }
        else {
            for(int i = 0; i < l.size(); i++) {
                p("%3d ", l.get(i));
            }
            p("\n");
        }
    }
    
    public void copyArray(int [] a, int [] acopy) {
        for(int i = 0; i < a.length; i++) {
            acopy[i] = a[i];
        }
    }

    public void copyArray(int [] a, int [] acopy, int idxBeg, int idxEnd) {
        for(int i = idxBeg; i <= idxEnd; i++) {
            acopy[i] = a[i];
        }
    }
    
    public void swap(int [] a, int i, int j) {
        if(i < 0 || i >= a.length || j < 0 || j >= a.length) {
            return;
        }
        int c = a[i];
        a[i] = a[j];
        a[j] = c;
    }

    public void swap(char [] a, int i, int j) {
        if(i < 0 || i >= a.length || j < 0 || j >= a.length) {
            return;
        }
        char c = a[i];
        a[i] = a[j];
        a[j] = c;
    }
    
    public void swap(List<Integer> list, int i, int j) {
        if(i < 0 || i >= list.size() || j < 0 || j >= list.size()) {
            return;
        }
        
        Integer oi = list.get(i);
        Integer oj = list.get(j);
        list.set(i, oj);
        list.set(j, oi);
    }
    
    public void shuffle(List<Integer> list) {
        if(list == null) {
            return;
        }
        int sz = list.size();
        
        for(int i = 0; i < sz; i++) {
            int r = getInt(i+1, sz);
            swap(list, i, r);
        }
    }
    
    public void printLine(int numPad) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < numPad; i++) {
            sb.append("-");
        }
        if(numPad <= 0) {
            p("\n\n");
        }
        else {
            p("\n%s\n", sb.toString());
        }
    }
    
    public void printHistogram(List<Integer> l) {
        List<StringBuilder> list = new ArrayList<>();
        int size = l.size();
        int max = 0;
        
        for(int i = 0; i < size; i++) {
            Integer v = l.get(i);
            max = (max < v) ? v : max;
        }
        for(int i = 0; i <= max; i++) {
            list.add(new StringBuilder());
        }
        
        for(int i = 0; i < size; i++) {
            int v = l.get(i);
            for(int j = 0; j <= max; j++) {
                StringBuilder sb = list.get(j);
                if(j < v) {
                    sb.append("  *");
                }
                else {
                    sb.append("   ");
                }
            }
        }
        
        for(int i = max; i >= 0; i--) {
            StringBuilder sb = list.get(i);
            String s = sb.toString();
            p("   %s\n", s);
        }
        p("    ");
        for(int i = 0; i < size; i++) {
            p("---");
        }
        p("\n");
        p("val");
        for(int i = 0; i < size; i++) {
            p("%3d", l.get(i));
        }
        p("\n");
        p("idx");
        for(int i = 0; i < size; i++) {
            p("%3d", i);
        }
        p("\n");
        
    }
    
    public int nextInt() {
        return rand.nextInt();
    }
    
    public double nextDouble() {
        return rand.nextDouble();
    }
    
    /**
     * Sieve of Eratosthenes
     */
    public List<Integer> getPrimesInRange(int max) {
        List<Integer> lres = new ArrayList<>();
        List<Integer> lval = new ArrayList<>();
        for(int i = 0; i < max; i++) {
            lval.add(i);
        }
        for(int i = 2; i < max; i++) {
            // skip if not prime (marked out below)
            if(lval.get(i) == null) {
                continue;
            }
            lres.add(i);
            // mark all multiples of this prime as null
            for(int j = i*2, k = 2; j < max; ) {
                lval.set(j, null);
                k++;
                j = i * k;
            }
        }
        return lres;
    }
    /**
     * FIXME
     * Fermat primality test.
     * 
     * N = integer to test.
     * 
     */
    public boolean isPrime(long v) {
        return false;
    }
}
