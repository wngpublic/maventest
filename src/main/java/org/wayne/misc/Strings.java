package org.wayne.misc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Strings {
	
    Utils u = new Utils();
    boolean dbg = true;
    
    public Strings(boolean dbg) {
        this.dbg = dbg;
    }
    
    public Strings() {
        this.dbg = false;
    }
    
    public void debug(boolean dbg) {
        this.dbg = dbg;
    }
    
	static void p(String f, Object ... o) {
		System.out.printf(f, o);
	}
		
	public List<String> removeInvalidParenthesis(String s) {
	    return null;
	}
	
	public List<String> split(String s, char delimiter, boolean enableEscape) {
	    List<String> list = new ArrayList<>();
	    
	    StringBuilder sb = new StringBuilder();
	    boolean isQuote = false;
	    boolean isEscape = false;
	    for(int i = 0; i < s.length(); i++) {
	        char c = s.charAt(i);
	        if(isEscape) {
	            isEscape = false;
	            sb.append(c);
	        }
	        else if(c == '\\') {
	            isEscape = true;
	        }
	        else if(c == '"') {
                sb.append(c);
	            isQuote = !isQuote;
	        }
	        else if(c == delimiter) {
	            if(!isQuote) {
	                list.add(sb.toString());
	                sb = new StringBuilder();
	            }
	            else {
	                sb.append(c);
	            }
	        }
	        else {
	            sb.append(c);
	        }
	    }
	    if(sb.length() != 0) {
	        list.add(sb.toString());
	    }
	    return list;
	}
	
    /**
     * rolling hash is used for rabin karp search of long text,
     * with O(n) on avg, unless hash function is bad and hits all time.
     */
    public static class RollingHash {
        int sizeWindow;
        int prvval;
        int curval;
        int prime;
        char prvchar;
        boolean isvalid = false;

        public RollingHash(int sizeStr) {
            sizeWindow = sizeStr;
            prvval = 0;
            curval = 0;
            prime = 31;
            prvchar = '\0';
        }

        public int computeHash(String strWindow) {
            if(strWindow.length() != sizeWindow) {
                return -1;
            }
            if(!isvalid) {
                curval = 0;
                int base = prime;
                for(int i = sizeWindow - 1; i >= 0; i--) {
                    char c = strWindow.charAt(i);
                    curval = curval * base + c * prime;
                    base = base * prime;
                    curval = curval % 0xffffffff;
                    base = base % 0xffffffff;
                }
                prvchar = strWindow.charAt(0);
                isvalid = true;
            } else {
                int base = pow(prime, sizeWindow);
                int valDelta = curval - base * prvchar;
                int c = strWindow.charAt(sizeWindow-1);
                curval = prime * (valDelta + c);
                prvchar = strWindow.charAt(0);
            }
            return curval;
        }

        int pow(int base, int pow) {
            int v = 1;
            if(pow <= 0) {
                return 1;
            }
            for(int i = 1; i <= pow; i++) {
                v = v * base;
                v = v % 0xffffffff;
            }
            return v;
        }
    }
    
    public static class KMP {
        int [] table;
        char [] a;
        /*
         * 00 01 02 03 04 05
         *  a  b  a  b  a  a
         * -1 00 00 01 02 03
         */
        private void constructTable() {
            table[0] = -1;
            table[1] = 0;
            int pos = 2;
            int cnd = 0;
            int sz = table.length;
            while(pos < sz) {
                if(a[pos-1] == a[cnd]) {
                    table[pos++] = ++cnd;
                }
                else if(cnd > 0) {
                    cnd = table[cnd];
                }
                else {
                    table[pos] = 0;
                    pos++;
                }
            }
            for(int i = 0; i < sz; i++) {
                p("%02d ", i);
            }
            p("\n");
            for(int i = 0; i < sz; i++) {
                p("%2s ", a[i]);
            }
            p("\n");
            for(int i = 0; i < sz; i++) {
                p("%02d ", table[i]);
            }
            p("\n");
        }
        public KMP(String pat) {
            a = pat.toCharArray();
            table = new int[a.length];
            constructTable();
        }
        private void printMatch(char [] astr) {
            int szs = astr.length;
            int szp = a.length;
            {
                p("\n");
                for(int i = 0; i < szp; i++) {
                    p("%02d ", i);    // print idx
                }
                p("\n");
                for(int i = 0; i < szp; i++) {
                    p("%2s ", a[i]); // print pat
                }
                p("\n");
                for(int i = 0; i < szs; i++) {
                    p("%02d ", i);  // print idx
                }
                p("\n");
                for(int i = 0; i < szs; i++) {
                    p("%2s ", astr[i]); // print str
                }
                p("\n");
            }
        }
        /*
         * 00 01 02 03 04 05
         *  a  b  a  b  a  a
         * -1 00 00 01 02 03
         */
        public int match(String str) {
            char [] astr = str.toCharArray();
            printMatch(astr);
            int szs = astr.length;
            int szp = a.length;

            int m = 0;
            int i = 0;
            while((m + i) < szs) {
                if(a[i] == astr[m+i]) {
                    if(i == (szp-1)) {
                        return m;
                    }
                    i++;
                }
                else if(table[i] > -1) {
                    m = m + i - table[i];
                    i = table[i];
                    // print pattern shifted to m+i
                    for(int k = 0; k < m; k++) {
                        p("   ");
                    }
                    for(int k = 0; k < szp; k++) {
                        p("%2s ", a[k]); // print pat
                    }
                    p("\n");
                }
                else {
                    m++;
                    i = 0;
                    // print pattern shifted to m+i
                    for(int k = 0; k < m; k++) {
                        p("   ");
                    }
                    for(int k = 0; k < szp; k++) {
                        p("%2s ", a[k]); // print pat
                    }
                    p("\n");
                }
            }
            return -1;
        } 
    }
    
    /**
     * longest common substring: 
     * DP is O(n*m). Suffix tree is O(n+m)
     */
    public int longestCommonSubstring(String s1, String s2) {
        return longestCommonSubstring(s1, s2, 1);
    }
    public int longestCommonSubstring(String s1, String s2, int type) {
        class LCS {

            String longestCommonSubstringDP1(String s1, String s2) {
                List<List<Integer>> ll = new ArrayList<>();
                int sz1 = s1.length();
                int sz2 = s2.length();
                String sret = null;
                int max = 0;
                
                for(int i = 0; i < sz1; i++) {
                    List<Integer> l = new ArrayList<>();
                    ll.add(l);
                    for(int j = 0; j < sz2; j++) {
                        if(s1.charAt(i) == s2.charAt(j)) {
                            if(i == 0 || j == 0) {
                                l.add(0);
                            } else {
                                int v = 1 + ll.get(i-1).get(j-1);
                                l.add(v);
                                if(v > max) {
                                    max = v;
                                    sret = s1.substring(i+1-max, i+1);
                                }
                            }
                        } else {
                            l.add(0);
                        }
                    }
                }
                return sret;
            }
            
            /**
             * string1  a b c d e x
             * string2  x c d e d e a
             * 
             *   a b c d e x
             * x 0 0 0 0 0 1
             * c 0 0 1 0 0 0
             * d 0 0 0 2 0 0
             * e 0 0 0 0 3 0
             * d 0 0 0 1 0 0 
             * e 0 0 0 0 2 0
             * a 1 0 0 0 0 0
             * 
             */
            String longestCommonSubstringDP2(String s1, String s2) {
                int sz1 = s1.length();
                int sz2 = s2.length();
                int [][] aa = new int[sz1][sz2];
                int max = 0;
                String sret = null;
                
                for(int i = 0; i < sz1; i++) {
                    for(int j = 0; j < sz2; j++) {
                        if(s1.charAt(i) == s2.charAt(j)) {
                            if(i == 0 || j == 0) {
                                aa[i][j] = 0;
                            } else {
                                int v = 1 +  aa[i-1][j-1];
                                aa[i][j] = v;
                                if(v > max) {
                                    max = v;
                                    sret = s1.substring(i+1-max, i+1);
                                }
                            }
                        } else {
                            aa[i][j] = 0;
                        }
                    }
                }
                
                return sret;
            }
            
            /**
             * 
             * are these equivalent?
             * 
             *     a b c d e f g
             *   c c b c c d e f  
             *     c c b c c d e f
             *       c c b c c d e f
             *         c c b c c d e f
             *           c c b c c d e f
             *             c c b c c d e f
             *               c c b c c d e f
             *                 c c b c c d e f
             *                       
             *           
             *                 a b c d e f g
             *                 c c b c c d e f
             *               c c b c c d e f
             *             c c b c c d e f   
             *           c c b c c d e f
             *         c c b c c d e f
             *       c c b c c d e f
             *     c c b c c d e f
             *   c c b c c d e f       
             *    
             * 7 x 6
             */
            String bruteLCS1(String s1, String s2) {
                int sz1 = s1.length();
                int sz2 = s2.length();
                int max = 0;
                String sret = null;
                for(int i = 0; i < sz1; i++) {
                    int maxcur = 0;
                    for(int j = 0, k = i;
                        j < sz2 && k < sz1;
                        j++, k++)
                    {
                        maxcur = (s1.charAt(k) == s2.charAt(j)) ? 
                            maxcur + 1 : 0;
                        if(max < maxcur) {
                            max = maxcur;
                            sret = s2.substring(j+1-max, j+1);
                        }
                    }
                }
                return sret;
            }
            String bruteLCS2(String s1, String s2) {
                int sz1 = s1.length();
                int sz2 = s2.length();
                int max = 0;
                String sret = null;
                for(int i = sz1-1; i >= 0; i--) {
                    int maxcur = 0;
                    for(int j = sz2-1, k = i;
                        j >= 0 && k >= 0;
                        j--, k--)
                    {
                        maxcur = (s1.charAt(k) == s2.charAt(j)) ?
                            maxcur + 1 : 0;
                        if(max < maxcur) {
                            max = maxcur;
                            sret = s2.substring(j+1-max, j+1);
                        }
                    }
                }
                return sret;
            }
        }
        LCS lcs = new LCS();
        String sret = null;
        if(type == 1) {
            sret = lcs.longestCommonSubstringDP1(s1, s2);
        }
        else if(type == 2) {
            sret = lcs.longestCommonSubstringDP2(s1, s2);
        }
        else if(type == 3) {
            sret = lcs.bruteLCS1(s1, s2);
        }
        else if(type == 4) {
            sret = lcs.bruteLCS1(s1, s2);
        }
        if(sret == null) {
            return 0;
        } else {
            return sret.length();
        }
    }
    
    public LCSContainer longestCommonSubsequence(String s1, String s2) {
        class LCS {
            /**
             * longestCommonSubsequence
             * 
             * string1  a b c d e x
             * string2  x a c d e d e
             * 
             *   a b c d e x
             * x 0 0 0 0 0 1
             * a 1 1 1 1 1 1
             * c 1 1 2 2 2 2
             * d 1 1 2 3 3 3
             * e 1 1 2 3 4 4
             * d 1 1 2 3 4 4 
             * e 1 1 2 3 4 4
             */
            private LCSContainer longestCommonSubsequenceDP(
                String s1, 
                String s2) 
            {
                int sz1 = s1.length();
                int sz2 = s2.length();
                int [][] a = new int[sz1][sz2];
                int max = 0;
                String subsequence = null;
                
                // find longest subsequence and fill matrix
                {
                    for(int i = 0; i < sz1; i++) {
                        char c1 = s1.charAt(i);
                        int iless1 = (i == 0) ? 0 : i-1;
                        for(int j = 0; j < sz2; j++) {
                            char c2 = s2.charAt(j);
                            int jless1 = (j == 0) ? 0 : j-1;
                            if(c1 == c2) {
                                a[i][j] = a[iless1][jless1] + 1;
                            }
                            else {
                                a[i][j] = u.maxint(a[i][jless1], a[iless1][j]);
                            }
                        }
                    }
                    if(dbg) {
                        p("print matrix\n");
                        u.printMatrix(a);
                    }
                }
                
                // backtrace to produce the subsequence string
                {
                    StringBuilder sb = new StringBuilder();
                    for(int i = sz1-1, j = sz1-1; i != 0 && j != 0;) {
                        int iless1 = (i == 0) ? 0 : i - 1;
                        int jless1 = (j == 0) ? 0 : j - 1;
                        int v = a[i][j];
                        int vup = a[iless1][j];
                        int vleft = a[i][jless1];
                        int vdiag = a[iless1][jless1];
                        
                        if(v == (vdiag+1)) {
                            sb.append(s1.charAt(i));
                            i = iless1;
                            j = jless1;
                        }
                        else if(v == vup) {
                            i = iless1;
                        }
                        else if(v == vleft) {
                            j = jless1;
                        }
                    }
                    subsequence = sb.reverse().toString();
                    if(dbg) {
                        p("subsequence: %s\n", subsequence);
                    }
                }
                
                LCSContainer container = new LCSContainer(max, subsequence);
                return container;
            }
            
        }
        
        LCS lcs = new LCS();
        
        int type = 1;
        if(type == 1) {
            return lcs.longestCommonSubsequenceDP(s1, s2);
        }
        return lcs.longestCommonSubsequenceDP(s1, s2);
    }
    
    public class LCSContainer {
        int max = 0;
        String subsequence;
        public LCSContainer() {
            
        }
        public LCSContainer(int max, String subsequence) {
            this.max = max;
            this.subsequence = subsequence;
        }
        public LCSContainer max(int max) {
            this.max = max;
            return this;
        }
        public LCSContainer subsequence(String subsequence) {
            this.subsequence = subsequence;
            return this;
        }
        public int max() {
            return max;
        }
        public String subsequence() {
            return subsequence;
        }
    }
    
    public int editDistance(String s1, String s2) {
        class EditDistance {
            int editDIstanceRecursive(String s1, String s2) {
                int sz1 = s1.length();
                int sz2 = s2.length();
                int [][] aa = new int[sz1][sz2];
                int max = 0;
                
                for(int i = 0; i < sz1; i++) {
                    char c1 = s1.charAt(i);
                    for(int j = 0; j < sz2; j++) {
                        char c2 = s2.charAt(j);
                        if(c1 == c2) {
                            
                        }
                    }
                }
                
                return max;
            }
            
            int editDistanceIterative(String s1, String s2) {
                return 0;
            }
            
            int editDistanceDP(String s1, String s2) {
                int sz1 = s1.length();
                int sz2 = s2.length();
                int [][] a = new int[sz1][sz2];
                
                for(int i = 0; i < sz1; i++) {
                    char c1 = s1.charAt(i);
                    for(int j = 0; j < sz2; j++) {
                        char c2 = s2.charAt(j);
                        int substitute = (c1 == c2) ? 0 : 1;
                        a[i][j] = u.minint(
                            // deletion
                            ((i == 0) ? 1 : a[i-i][j] + 1),
                            // insertion
                            ((j == 0) ? 1 : a[i][j-1] + 1),
                            // substitution
                            ((i == 0 || j == 0) ? 
                                substitute : 
                                a[i-1][j-1] + substitute)
                            );
                    }
                }
                return a[sz1-1][sz2-1];
            }
            
            int editDistanceWagnerFischer(String s1, String s2) {
                return 0;
            }        
        }
       
        int type = 1;
        EditDistance editDistance = new EditDistance();
        if(type == 1) {
            return editDistance.editDistanceDP(s1, s2);
        }
        else if(type == 2) {
            return editDistance.editDIstanceRecursive(s1, s2);
        }
        else if(type == 3) {
            return editDistance.editDistanceIterative(s1, s2);
        }
        else if(type == 4) {
            return editDistance.editDistanceWagnerFischer(s1, s2);
        }
        return editDistance.editDistanceDP(s1, s2);
    }
    
 
    /**
     * longestSubstringNonRepeat
     * 
     * given abcabcbb, abc is longest
     * given bbbbb, b is longest
     * given pwwkew, wke is longest
     * 
     * 0 1 2 3 4 5 6 7 8
     * a b c a b c d b b
     * 1 2 3 
     *       3
     *         3
     *           3
     *             4
     *               3
     *                 1
     *            
     * 0 1 2 3 4 5                
     * p w w k e w
     * 1 2 1 2 3 3
     * 
     * a b b a
     * 1 2 1 2
     * 
     * t m m z u x t
     * 1 2 1 2 3 4 5
     */
    public int longestSubstringNonRepeat(String s) {
        Map<Character, Integer> map = new HashMap<>();
        int max = 0;
        String maxstring = null;

        for(int i = 0, j = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if(map.containsKey(c)) {
                int idx = map.get(c);
                j = (j > idx) ? j : idx;
            }
            int tmpmax = i-j;
            if(tmpmax >= max) {
                max = tmpmax;
                maxstring = s.substring(i+1-max, i+1);
            }
            map.put(c, i);
        }
        p("maxstring:%s\n", maxstring);
        return max;
    }
    
    public List<String> prettyString(String sin, int width) {
        class Cost {
            public int cost(List<String> list, int width) {
                int totalCost = 0;
                for(int i = 0; i < list.size(); i++) {
                    int cost = cost(list.get(i), width);
                    totalCost += cost;
                }
                return totalCost;
            }
            public int cost(String line, int width) {
                int diff = width - line.length();
                int cost = pow(2, diff);
                return cost;
            }
            int pow(int base, int power) {
                int val = 1;
                for(int i = 0; i < power; i++) {
                    val *= base;
                }
                return val;
            }
        }
        
        class Pretty {
            public List<String> prettyString1(List<String> words, int width) {
                List<String> list = new ArrayList<>();
                Cost costObj = new Cost();
                int mincost = 0;
                boolean set = false;
                {
                    for(int i = 0; i < words.size(); i++) {
                        int szline = 0;
                        for(int j = i-1; j >= 0; j--) {
                            int szword = words.get(j).length() + 1;
                            int sztmp = szline + szword;
                            if(sztmp < width) {
                                szline = sztmp;
                            }
                            else {
                                break;
                            }
                        }
                    }
                    
                    int cost = costObj.cost(list, width);
                    
                    if(set) {
                        mincost = (cost < mincost) ? cost : mincost;
                    } else {
                        mincost = cost;
                        set = true;
                    }
                    
                }
                return list;                
            }
        }

        List<String> words = Arrays.asList(sin.split("\\s+"));

        if(dbg) {
            for(int i = 0; i < words.size(); i++) {
                p("%3d: %s\n", i, words.get(i));
            }
        }

        Pretty pretty = new Pretty();
        return pretty.prettyString1(words, width);

    }
    
    public String longestPalindrome(String s) {
        class Palindrome {
            public String manachers(String s) {
                // https://en.wikipedia.org/wiki/Longest_palindromic_substring
                if(s == null || s.length() == 0) {
                    return null;
                }
                char [] s2 = addBoundaries(s.toCharArray());
                int [] p = new int[s2.length];
                int c = 0;
                int r = 0;
                int m = 0;
                int n = 0;
                for(int i = 1; i < s2.length; n = i + 1) {
                    if(i > r) {
                        p[i] = 0;
                        m = i - 1;
                        n = i + 1;
                    }
                    else {
                        int i2 = c * 2 - i;
                        if(p[i2] < (r - i - 1)) {
                            p[i] = p[i2];
                            m = -1;
                        } else {
                            p[i] = r - i;
                            n = r + 1;
                            m = i * 2 - n;
                        }
                    }
                    while(m >= 0 && n < s2.length && s2[m] == s2[n]) {
                        p[i]++;
                        m--;
                        n++;
                    }
                    if((i + p[i]) > r) {
                        c = i;
                        r = i + p[i];
                    }
                }
                int len = 0;
                c = 0;
                for(int i = 1; i < s2.length; i++) {
                    if(len < p[i]) {
                        len = p[i];
                        c = i;
                    }
                }
                char [] ss = Arrays.copyOfRange(s2, c-len, c + len + 1);
                String retval = String.valueOf(removeBoundaries(ss));
                return retval;
            }
            
            private char [] addBoundaries(char [] cs) {
                if(cs == null || cs.length == 0) {
                    return "||".toCharArray();
                }
                
                char [] a = new char[cs.length*2 + 1];
                for(int i = 0; i < cs.length; i++) {
                    a[i*2] = '|';
                    a[i*2+1] = cs[i];
                }
                return a;
            }
            
            private char [] removeBoundaries(char [] cs) {
                if(cs == null || cs.length < 3) {
                    return "".toCharArray();
                }
                char [] a = new char[(cs.length-1)/2];
                for(int i = 0; i < a.length; i++) {
                    a[i] = cs[i*2+1];
                }
                return a;
            }
        }
        
        Palindrome t = new Palindrome();
        int type = 0;
        if(type == 0) {
            return t.manachers(s);
        }
        return null;
    }
    
    /**
     * given 2 sorted char arrays, find median of two arrays
     * 
     * basically do binary search to find where
     * (a1.leftsize + a2.leftsize) == (a1.rightsize + a2.rightsize)
     * 
     * do some optimization checks with overlaps.
     * 
     * case1:
     *    a1      a2
     *    -------
     *            -------
     * case2:
     *    a1  a2
     *    -------
     *        --------
     * case3
     *    a1  a2
     *    ---------------
     *        --------
     */
    public int medianOfMedian(int [] a1, int [] a2) {
        int sz1 = a1.length;
        int sz2 = a2.length;
        if(sz1 == 0 && sz2 == 0) {
            return 0;
        }
        else if(sz1 == 0) {
            return a2[sz2/2];
        }
        else if(sz2 == 0) {
            return a1[sz1/2];
        }
        
        int sum = sz1 + sz2;
        int half = sum/2;
        int median = 0;
        
        // non overlapping case
        {
            int i1l = a1[0];
            int i1r = a1[sz1-1];
            int i2l = a2[0];
            int i2r = a2[sz2-1];
            if(i1r < i2l || i2r < i1l) {
                int diff = sum - sz1;
                if(diff == half) {
                    median = (i1r + i2l) / 2;
                }
                else if(diff < half) {
                    diff = (half - diff);
                    median = a2[diff];
                }
                else {
                    diff = (sz1 - (diff - half));
                    median = a1[diff];
                }
                return median;
            }
        }
        // overlapping case
        int i1s = 0;
        int i1e = sz1-1;
        while(i1s < i1e) {
            int i1m = i1s + (i1e - i1s) / 2;
            int i1v = a1[i1m];
            // binary search a2
            int i2s = 0;
            int i2e = sz2-1;
            int i2m = 0;
            int i2v = a2[0];
            while(i2s < i2e) {
                i2m = i2s + (i2e - i2s) / 2;
                i2v = a2[i2m];
                if(i1v == i2v) {
                    break;
                }
                else if(i1v < i2v) {
                    // i2m-1=b < i1m=c < i2m=d
                    if((i2m-1) > i2s) {
                        if(i1v > a2[i2m-1]) {
                            break;
                        }
                    }
                    // i1m=c < i2m=f
                    i2e = i2m-1;
                }
                else {
                    if((i2m+1) < i2e) {
                        if(i1v > a2[i2m+1]) {
                            break;
                        }
                    }
                    i2s = i2m+1;
                }
            }
            int curIdxSum = i1m + i2m;
            if(curIdxSum == half || 
               curIdxSum == (half-1) || 
               curIdxSum == (half+1)) {
                median = a1[i1m];
                break;
            }
            else if(curIdxSum < half) {
                i1s = i1m+1;
            }
            else {
                i1e = i1m-1;
            }
        }
        return median;
    }
    
    /**
     * get list of list of permutations, precursor to skip gram.
     */
    public List<List<String>> getOrderedPermutations(List<String> l) {
        List<List<String>> ll = new ArrayList<>();
        return ll;
    }
    
    /**
     * n!/(n-k)!
     * 
     * a,b,c,d choose 3
     * 
     * 4!/(4-3)! = 4!/1! = 4*3*2 = 12
     * 
     * 
     * a
     *   b
     *     c    abc
     *     d    abd
     *   c
     *     b    acb
     *     d    acd
     *   d
     *     b    adb
     *     c    adc
     * b
     *   a
     *     c    bac
     *     d    bad
     *   c
     *     a    bca
     *     d    bcd
     *   d
     *     a    bda
     *     c    bdc
     * c
     *   a
     *     b
     *     d
     *   b
     *     a
     *     d
     *   d
     *     a
     *     b
     * d
     *   a
     *     b
     *     c
     *   b
     *     a
     *     c
     *   c
     *     a
     *     b
     * 
     */
    public List<List<String>> getPermutations(List<String> l, int szmax) {
        class P {
            void permutation(
                List<List<String>> ll,
                LinkedList<String> ltmp,
                List<String> l,
                boolean [] abvisited,
                int icur,
                int szmax)
            {
                if(ltmp.size() == szmax) {
                    List<String> ladd = new ArrayList<>(ltmp);
                    ll.add(ladd);
                    return;
                }
                for(int i = 0; i < l.size(); i++) {
                    if(!abvisited[i]) {
                        abvisited[i] = true;
                        String w = l.get(i);
                        ltmp.push(w);
                        permutation(ll,ltmp,l,abvisited,i,szmax);
                        ltmp.pop();
                        abvisited[i] = false;
                    }
                }
            }
        }
        List<List<String>> ll = new ArrayList<>();
        P p = new P();
        boolean [] abvisited = new boolean[l.size()];
        LinkedList<String> ltmp = new LinkedList<>();
        p.permutation(ll, ltmp, l, abvisited, 0, szmax);
        return ll;
    }
    
    public List<List<List<String>>> getPermutations(List<String> l) {

        List<List<List<String>>> lll = new ArrayList<>();
        int sz = l.size();
        for(int szmax = 1; szmax <= sz; szmax++) {
            List<List<String>> llres = getPermutations(l, szmax);
            lll.add(llres);
        }
        return lll;
    }
    
    /**
     * n!/(k!(n-k)!)
     * 
     * a,b,c,d,e choose 3
     * 
     * 5!/(3!(5-3)!) = 5*4*3*2/(3*2*2) = 5*2
     * 
     * 
     * a
     *   b
     *     c        abc
     *     d        abd
     *     e        abe
     *   c
     *     d        acd
     *     e        ace
     *   d
     *     e        ade
     *   e
     * b
     *   c
     *     d        bcd
     *     e        bce
     *   d
     *     e        bde
     * c
     *   d
     *     e        cde
     */
    public List<List<String>> getCombinations(List<String> l, int szmax) {
        class C {
            void combination(List<List<String>> ll,
                LinkedList<String> ltmp,
                List<String> l,
                int icur,
                int szmax)
            {
                if(icur > l.size()) {
                    return;
                }
                if(ltmp.size() == szmax) {
                    List<String> ladd = new ArrayList<>(ltmp);
                    ll.add(ladd);
                    return;
                }
                for(int i = icur; i < l.size(); i++) {
                    String w = l.get(i);
                    ltmp.push(w);
                    combination(ll, ltmp, l, i+1, szmax);
                    ltmp.pop();
                }
            }
        }
        C c = new C();
        List<List<String>> ll = new ArrayList<>();
        LinkedList<String> ltmp = new LinkedList<>();
        c.combination(ll, ltmp, l, 0, szmax);
        return ll;
    }
    
    public void longestRepeatedSubstring(String s) {
        
    }
    public void longestUniqueSubstring(String s) {
        
    }
    public void longestCommonSubsequence1(String s1, String s2) {
        
    }
    public void longestCommonSubsequence(List<String> s1, List<String> s2) {
        
    }
    public void diff1(String s1, String s2) {
        
    }
    public void diff1(List<String> l1, List<String> l2) {
        
    }
    
    /**
     * find all start idx of where all words in list are captured in s
     * exactly once.
     * 
     *          0    0    1    1    2
     *          0    5    0    5    0
     * eg   s = manbarfoothefoobarman
     *      w = [foo,bar]
     *      
     * this selects 3 and 12 as starting idx for concatenation of foo,bar 
     */
    public void substringWithContcatenationOfAllWords(String s, List<String> l) {
        
    }

    /**
     * given only '(' and ')', find length of longest valid (well formed) parenthesis
     * substring.
     * 
     * leetcode /problems/longest-valid-parentheses/description/
     * 
     * eg   ()(() = 2
     *      ()(()) = 6
     *      ()() = 4
     *      
     *      
     *      0 1 2 3 4
     *      ( ) ( ( )
     *      
     *      ( ) ( ( ( ) ( ) ) ( ( ( ) ) ) )
     */
    public int longestValidParenthesis(String s) {
        class Obj {
            char c;
            int off;
            public Obj(char c, int off) {
                this.c = c;
                this.off = off;
            }
        }
        class Span {
            int beg = 0;
            int end = 0;
        }
        class Internal {
            int lvp1(String s) {
                int max = 0;
                int ptr = -1;
                int szcur = 0;
                
                LinkedList<Obj> ll = new LinkedList<>();
                for(int i = 0; i < s.length(); i++) {
                    char c = s.charAt(i);
                    Obj o = new Obj(c, i);
                    if(ll.size() == 0) {
                        ll.add(o);
                    }
                    else if(c == '(') {
                        ll.add(o);
                    }
                    else {
                        Obj objPeek = ll.peek();
                        if(objPeek.c == ')') {
                            ll.add(o);
                        }
                        else {
                            // top is ( and cur is ), so pop
                            ll.pop();
                            
                            if((ptr+1) == objPeek.off) {
                                
                            } else {
                                szcur += 2;
                            }
                            ptr = i;
                        }
                    }
                }
                return max;
            }
            int lvp2(String s) {
                int max = 0;
                boolean [] ba = new boolean[s.length()];
                LinkedList<Obj> ll = new LinkedList<>();
                int cur = 0;
                for(int i = 0; i < s.length(); i++) {
                    char c = s.charAt(i);
                    Obj o = new Obj(c, i);
                    ba[i] = false;
                    if(ll.size() == 0 || c == '(') {
                        ll.push(o);
                        cur = 0;
                    } else {
                        Obj peek = ll.peek();
                        if(peek.c == ')') {
                            ll.push(o);
                            cur = 0;
                        } else {
                            int off = peek.off;
                            ba[i] = true;
                            ba[off] = true;
                            ll.pop();
                            cur += 2;
                            for(int j = off - 1; j >= 0; j--) {
                                if(ba[j]) {
                                    cur++;
                                } else {
                                    break;
                                }
                            }
                            max = (cur > max) ? cur : max;
                        }
                    }
                }
                return max;
            }
            /**
             * this approach didnt work
             * 
             *      0 1 2 3 4 5 6 7 8       
             *      ( ) ( ( ( ) ( ) )
             *      
             * i v s s s s
             *     p p c c
             *     b e b e
             * -----------------
             * 0 ( 0 0 0 0
             * 1 ) 0 0 0 1
             * 2 ( 0 1 0 1
             * 3 ( 0 1 0 1
             * 4 ( 0 1 0 1
             * 5 ) 0 1 4 5
             * 6 ( 
             * 7 ) 
             * 8 ) 
             */
            int lvp3(String s) {
                int max = 0;
                LinkedList<Obj> ll = new LinkedList<>();
                Span spanp = new Span();
                Span spanc = new Span();
                for(int i = 0; i < s.length(); i++) {
                    char c = s.charAt(i);
                    Obj o = new Obj(c, i);
                    if(ll.size() == 0) {
                        ll.push(o);
                    }
                    else if(c == '(') {
                        ll.push(o);
                        spanp.beg = spanc.beg;
                        spanp.end = spanc.end;
                    }
                    else {
                        Obj peek = ll.peek();
                        if(peek.c == ')') {
                            ll.push(o);
                            spanp.beg = spanc.beg;
                            spanp.end = spanc.end;
                        }
                        else {
                            spanc.beg = peek.off;
                            spanc.end = i;
                            if( (spanc.beg != 0) && 
                                ((spanc.beg - 1) == spanp.end)) 
                            {
                                spanc.beg = spanp.beg;
                            }
                            int len = spanc.end - spanc.beg + 1;
                            max = (len > max) ? len : max;
                        }
                    }
                }
                return max;
            }
            /**
             * their dp solution, copied
             */
            int lvp4(String s) {
                int max = 0;
                int [] a = new int[s.length()];
                for(int i = 0; i < a.length; i++) {
                    char c = s.charAt(i);
                    if(c == '(') {
                        a[i] = (i >= 1 ? a[i-2] : 0) + 2;
                    }
                    else if((i - a[i-1]) > 0 && s.charAt(i - a[i-1] - 1) == '(') {
                        a[i] = a[i-1] + ((i - a[i-1]) >= 2 ? a[i - a[i-1] - 2] : 0) + 2;
                    }
                    max = (max > a[i]) ? max : a[i];
                }
                return max;
            }
            /**
             * their solution for stack, copied
             */
            int lvp5(String s) {
                int max = 0;
                LinkedList<Integer> ll = new LinkedList<>();
                ll.push(-1);
                for(int i = 0; i < s.length(); i++) {
                    if(s.charAt(i) == '(') {
                        ll.push(i);
                    }
                    else {
                        ll.pop();
                        if(ll.size() == 0) {
                            ll.push(i);
                        }
                        else {
                            max = (max < (i - ll.peek())) ? 
                                    (i - ll.peek()) : max;
                        }
                    }
                }
                return max;
            }
            /**
             * does this stack solution work?
             */
            int lvp6(String s) {
                int max = 0;
                LinkedList<Integer> ll = new LinkedList<>();
                for(int i = 0; i < s.length(); i++) {
                    if(ll.size() == 0) {
                        ll.push(i); // this can be ( or )
                    }
                    else if(s.charAt(i) == '(') {
                        ll.push(i);
                    }
                    else {
                        // s[i] == ')'. but what is ll.pop?
                        int peek = (ll.size() == 0) ? 0 : ll.peek();
                        max = (max < (i - peek + 1)) ?
                            (i - peek + 1) : max;
                    }
                }
                return max;
            }
            int lvp(String s) {
                int typemethod = 5;
                switch(typemethod) {
                case 1: return lvp1(s);
                case 2: return lvp2(s);
                case 3: return lvp3(s);
                case 4: return lvp4(s);
                case 5: return lvp5(s);
                case 6: return lvp6(s);
                default: return lvp2(s);
                }
            }
        }
        Internal internal = new Internal();
        return internal.lvp(s);
    }
    
    public String longestPalindromicSubstring(String s) {
        class Internal {
            String lps1(String s) {
                String substring = s;
                int sz = s.length();
                int max = 0;
                for(int i = 0; i < sz; i++) {
                    if((i+1) == sz) {
                        continue;
                    }
                    // do even
                    {
                        int j = i, k = i+1;
                        int ctr = 0;
                        while(j >= 0 && k < sz) {
                            if(s.charAt(j) != s.charAt(k)) {
                                break;
                            }
                            ctr += 2;
                            j--;
                            k++;
                        }
                        if(ctr > max) {
                            substring = s.substring(j+1, k);
                            max = ctr;
                        }
                    }
                    // do odd
                    {
                        int j = i, k = i;
                        int ctr = 1;
                        while(j >= 0 && k < sz) {
                            if(s.charAt(j) != s.charAt(k)) {
                                break;
                            }
                            if(j != k) {
                                ctr += 2;
                            }
                            j--;
                            k++;
                        }
                        if(ctr > max) {
                            substring = s.substring(j+1, k);
                            max = ctr;
                        }
                    }
                }
                return substring;
            }
            String lps(String s) {
                return lps1(s);
            }
        }
        Internal internal = new Internal();
        return internal.lps(s);
    }
}
