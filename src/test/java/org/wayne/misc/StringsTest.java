package org.wayne.misc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

public class StringsTest extends TestCase {

    Strings strings = new Strings();

    boolean dbg = false;
    
    private void p(String f, Object ...o) {
        System.out.printf(f, o);
    }
    
    public void testLongestSubstringNR1() {
        p("test: testLongestSubstringNR1\n");
        Map<String, Integer> map = new HashMap<>();
        
        map.put("tmmzuxt", 5);
        map.put("abcabcbb", 3);
        map.put("abcabcdbb", 4);
        
        for(Map.Entry<String, Integer> kv: map.entrySet()) {
            String s = kv.getKey();
            int expected = kv.getValue();
            int actual = strings.longestSubstringNonRepeat(s);
            p("testLongestSubstring: %s len actual:%d expected:%d\n", s, actual, expected);
        }
    }
    
    public void testLongestSubstringNR2() {
        String s;
        {
            p("----------\n");
            s = "abcabcdbef";
            p("string:%s\n", s);
            int actual = strings.longestSubstringNonRepeat(s);
            p("testLongestSubstring: len %d\n", actual);
        }
        {
            p("----------\n");
            s = "abcd";
            p("string:%s\n", s);
            int actual = strings.longestSubstringNonRepeat(s);
            p("testLongestSubstring: len %d\n", actual);
        }
        {
            p("----------\n");
            s = "aaa";
            p("string:%s\n", s);
            int actual = strings.longestSubstringNonRepeat(s);
            p("testLongestSubstring: len %d\n", actual);
        }
        {
            p("----------\n");
            s = "a";
            p("string:%s\n", s);
            int actual = strings.longestSubstringNonRepeat(s);
            p("testLongestSubstring: len %d\n", actual);
        }
        {
            p("----------\n");
            s = "abcabcdabc";
            p("string:%s\n", s);
            int actual = strings.longestSubstringNonRepeat(s);
            p("testLongestSubstring: len %d\n", actual);
        }
        {
            p("----------\n");
            s = "abcabcdcef";
            p("string:%s\n", s);
            int actual = strings.longestSubstringNonRepeat(s);
            p("testLongestSubstring: len %d\n", actual);
        }
    }

    public void testEditDistance1() {
        p("test: testEditDistance1\n");
        String s1 = "abcde";
        String s2 = "acdef";
        int min = strings.editDistance(s1, s2);
        p("edit distance: %s %s = %d\n", s1, s2, min);
    }
    
    public void testPretty1() {
        p("test: testPretty1\n");
        String s1 = 
                "a aa aaa aaa aaaa aaa aa aaaa" +
                "bbb bbbb bb bbbb bbb bb " + 
                "cc c ccc cccccc ccc cc ccc " + 
                "ddd d ddddd ddd dddd dddd " +
                "e eeeeee eeee ee eeeee eeee e";
        int width = 10;
        String s = s1;
        p("%s\n", s);
        List<String> list = strings.prettyString(s, width);
        p("\nRESULT\n");
        for(String sline: list) {
            p("%s\n", sline);
        }
    }
    
    public void testMedianOfMedian() {
        
    }
    
    /**
     * tester class for determining duplicates
     */
    class Tree {
        String s;
        int level = 0;
        HashMap<String, Tree> map = new HashMap<>();
        public Tree(int level, String s) {
            this.level = level;
            this.s = s;
        }
        public String s() {
            return s;
        }
        public int level() {
            return level;
        }
        public Tree get(String s) {
            return map.get(s);
        }
        public Tree set(String s) {
            if(!map.containsKey(s)) {
                map.put(s, new Tree(level+1, s));
            }
            return map.get(s);
        }
    }

    public void testPermutations() {
        List<String> l = Arrays.asList(
            "abcd", "bcde", "cdef", "defg", "efgh");
        Strings strings = new Strings();
        int maxlevel = 3;
        List<List<String>> lperms = strings.getPermutations(l, maxlevel);
        // validate
        {
            Tree root = new Tree(0, null);
            for(List<String> lres: lperms) {
                Tree ctree = root;
                System.out.println(lres);
                for(String s: lres) {
                    int nextlevel = ctree.level() + 1;
                    if(ctree.get(s) != null) {
                        if(nextlevel == maxlevel) {
                            p("DUPLICATE found in node %s!\n", ctree.s());
                            return;
                        }
                    }
                    ctree = ctree.set(s);
                }
            }
            p("No duplicates found. %d items\n", lperms.size());
        }
    }
    
    public void testCombinations() {
        
        List<String> l = Arrays.asList(
                "abcd", "bcde", "cdef", "defg", "efgh");
        /*
         * n!/(k!(n-k)!)
         * 
         * 5!/(3!(5-3)!) = 5!/(3!2!)
         * 5*4*3*2/3*2*2
         * 5*3*2/3
         * 5*2
         * 
         */
        Strings strings = new Strings();
        int maxlevel = 3;
        List<List<String>> lcombs = strings.getCombinations(l, maxlevel);

        // validate
        {
            Tree root = new Tree(0, null);
            for(List<String> lres: lcombs) {
                Tree ctree = root;
                System.out.println(lres);
                for(String s: lres) {
                    int nextlevel = ctree.level() + 1;
                    if(ctree.get(s) != null) {
                        if(nextlevel == maxlevel) {
                            p("DUPLICATE found in node %s!\n", ctree.s());
                            return;
                        }
                    }
                    ctree = ctree.set(s);
                }
            }
            p("No duplicates found. %d items\n", lcombs.size());
        }
    }
    
    public void badtestLongestValidParenthesis() {
        Strings t = new Strings();
        {
            String s = "()(()";
            int max = t.longestValidParenthesis(s);
            p("longestValidParenthesis: %20s = %2d. expected 2\n", s, max);
            assertEquals(max,  2);
        }
        {
            String s = "()(())";
            int max = t.longestValidParenthesis(s);
            p("longestValidParenthesis: %20s = %2d. expected 4\n", s, max);
            assertEquals(max,  4);
        }
        {
            String s = "()((()())((())))";
            int max = t.longestValidParenthesis(s);
            p("longestValidParenthesis: %20s = %2d. expected 16\n", s, max);
            assertEquals(max,  16);
        }
        {
            String s = "()(((()())((())))";
            int max = t.longestValidParenthesis(s);
            p("longestValidParenthesis: %20s = %2d. expected 14\n", s, max);
            assertEquals(max,  14);
        }
        {
            String s = "()";
            int max = t.longestValidParenthesis(s);
            p("longestValidParenthesis: %20s = %2d. expected 2\n", s, max);
            assertEquals(max,  2);
        }
        {
            String s = "(()(()()";
            int max = t.longestValidParenthesis(s);
            p("longestValidParenthesis: %20s = %2d. expected 4\n", s, max);
            assertEquals(max,  4);
        }
        {
            String s = "())(())))";
            int max = t.longestValidParenthesis(s);
            p("longestValidParenthesis: %20s = %2d. expected 4\n", s, max);
            assertEquals(max,  4);
        }
    }
    
    public void testLongestPalindromicSubstring() {
        Strings t = new Strings();
        {
            String s = "ababbab";
            String substring = t.longestPalindromicSubstring(s);
            p("longestPalindrome %s = %s\n", s, substring);
            assertEquals(substring, "babbab");
        }
        {
            String s = "ababab";
            String substring = t.longestPalindromicSubstring(s);
            p("longestPalindrome %s = %s\n", s, substring);
            assertEquals(substring, "ababa");
        }
        {
            String s = "aaaaa";
            String substring = t.longestPalindromicSubstring(s);
            p("longestPalindrome %s = %s\n", s, substring);
            assertEquals(substring, "aaaaa");
        }
        {
            String s = "aabbabbabbb";
            String substring = t.longestPalindromicSubstring(s);
            p("longestPalindrome %s = %s\n", s, substring);
            assertEquals(substring, "bbabbabb");
        }
        {
            String s = "a";
            String substring = t.longestPalindromicSubstring(s);
            p("longestPalindrome %s = %s\n", s, substring);
            assertEquals(substring, "a");
        }
    }
}
