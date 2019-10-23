package org.wayne.testcases;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import junit.framework.TestCase;

import org.wayne.java.GarbageCollectionTest;
import org.wayne.misc.Misc;
import org.wayne.misc.Strings;
import org.wayne.misc.StringsTest;
import org.wayne.misc.Utils;
import org.wayne.misc.graphs.BST;
import org.wayne.misc.graphs.BSTTest;
import org.wayne.misc.graphs.IntervalTreeTest;
import org.wayne.misc.graphs.Tries;

public class Testcases extends TestCase {
	
    Utils u = new Utils();
    List<Method> methods = new ArrayList<>();
	Map<String, Method> map = new HashMap<>();
	
	BSTTest.BSTSetup setupBST = new BSTTest.BSTSetup();
	SetupMisc setupPuzzles = new SetupMisc();
	Misc puzzles = new Misc();
	
    public Testcases() {
        init();
    	getMethods();
    }

    public void start(String [] args) {
        long timebegin = System.nanoTime();
        try {
            if(args.length > 0) {
                test(args);
            }
            else {
                printMethods();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        long timeend = System.nanoTime();
        long timediff = timeend - timebegin;
        long timediffmilli = timediff / 1000000;
        p("\n");
        p("time elapse millis:%d\n", timediffmilli);
        
    }
    
    private void test(String [] args) throws 
        IllegalAccessException, 
        IllegalArgumentException, 
        InvocationTargetException 
    {
        String arg0 = args[0];
        
        if(arg0.matches("^\\d+$")) {
            Integer i = Integer.parseInt(args[0]);
            if(i >= methods.size()) {
                printMethods();
            } else {
                Method m = methods.get(i);
                m.invoke(this);
            }
        }
        else {
            if(map.containsKey(arg0)) {
                Method m = map.get(arg0);
                m.invoke(this);
            } else {
                printMethods();
            }
        }
    }

    private static void p(String f, Object ...o) {
        System.out.printf(f, o);
    }
    
    private void getMethods() {
        Method [] tmpM = this.getClass().getMethods();
        
        // sort the list
        Map<String, Method> treemap = new TreeMap<>();
        
        for(int i = 0; i < tmpM.length; i++) {
            Method method = tmpM[i];
            String methodName = method.getName();
            
            if(tmpM[i].getName().matches("^t\\d+")) {
                if(tmpM[i].getModifiers() == Modifier.PUBLIC) {
                    treemap.put(methodName, method);
                    map.put(methodName, method);
                }
            }
        }
        
        for(Map.Entry<String, Method> kv: treemap.entrySet()) {
            Method m = kv.getValue();
            methods.add(m);
        }

    }

    private void printMethods() {
        for(int i = 0; i < methods.size(); i++) {
            p("%3d: %s\n", i, methods.get(i).getName());
        }
    }
    
    private void init() {
        
    }
    
    public void t0() {
    }

    public void t1() {
        int sizewindow = 5;
    	Strings.RollingHash rollingHash = new Strings.RollingHash(sizewindow);
    	int max = 100;
    	String s = u.getRandString(max);
    	
    	int ctr = 0;
    	List<Integer> listHash = new ArrayList<>();
    	
    	while(true && ctr < max) {
    	    String substring = s.substring(ctr, ctr+sizewindow);
    	    int hash = rollingHash.computeHash(substring);
    	    listHash.add(hash);
    	    ctr++;
    	}
    }

    public void t2() {
    	BST bst = setupBST.createTree(7, BSTTest.Type.BSTBALANCED);
    	bst.printValues();
    }

    public void t3() {
        BST bst = setupBST.createTree(15, BSTTest.Type.BSTBALANCED);
        bst.print();
        
        p("\nLEVEL ORDER\n");
        
        bst.printLevelOrder();
    }

    public void t4() {
        BST bst = setupBST.generateTree1();
        bst.printValues();
        int numNodes = bst.numNodes();
        int maxWidth = bst.maxWidth();
        int maxLevel = bst.maxLevels();
        p("NUMNODES:%2d MAXWIDTH:%2d MAXLEVEL:%2d\n", 
            numNodes, maxWidth, maxLevel);
    }

    public void t5() {
        BST bst = setupBST.generateTree2();
        bst.printValues();

        p("REVERSE LEAF NODES\n");
        bst.reverseLeafNodes();
        bst.printValues();
    }

    public void t6() {
        BST bst = setupBST.generateTree2();
        bst.printValues();

        p("REVERSE NODES\n");
        bst.reverseNodes();;
        bst.printValues();
    }

    public void t7() {
        BST bst = setupBST.generateTree3();
        bst.printValues();
    }

    public void t8() {
        List<Integer> l = setupPuzzles.getList1();
        int max = puzzles.maxConsecutiveSequence(l);
        u.p("max: %2d\n", max);
    }

    public void t9() {
        u.printLine(80);
        {
            List<Integer> l = Arrays.asList(0,5,4,3,4,5,6,5,6,5,2,4,3,4,5,4);
            u.printHistogram(l);
            int area = puzzles.maxRectangleAreaHistogram(l);
            u.p("\nmax area:%2d\n\n", area);
        }
        u.printLine(80);
        {
            List<Integer> l = Arrays.asList(1,2,0,1,2,3,2,3,2,0,1,0,1,2,3);
            u.printHistogram(l);
            int area = puzzles.maxRectangleAreaHistogram(l);
            u.p("\nmax area:%2d\n\n", area);
        }
        u.printLine(80);
        {
            List<Integer> l = Arrays.asList(6,5,4,4,5,6,5,6,5,3,4,3,4,5,6);
            u.printHistogram(l);
            int area = puzzles.maxRectangleAreaHistogram(l);
            u.p("\nmax area:%2d\n\n", area);
        }
        u.printLine(80);
        {
            List<Integer> l = Arrays.asList(6,5,4,4,5,1,5,4,5,6,3,3,4,5,6);
            u.printHistogram(l);
            int area = puzzles.maxRectangleAreaHistogram(l);
            u.p("\nmax area:%2d\n\n", area);
        }
    }

    public void t10() {
        IntervalTreeTest.IntervalTreeSetup t = new IntervalTreeTest.IntervalTreeSetup();
        t.testAdd();
    }

    public void t11() {
    }

    public void t12() {
        p("\tPATTERN abcd:\n");
        for(int i = 0; i < 10; i++) {
            String s = u.getRandString("abcd", 15);            
            p("%s\n", s);
        }
        p("\tPATTERN abc:\n");
        for(int i = 0; i < 10; i++) {
            String s = u.getRandString("abc", 15);            
            p("%s\n", s);
        }
        p("\tPATTERN ab:\n");
        for(int i = 0; i < 10; i++) {
            String s = u.getRandString("ab", 15);            
            p("%s\n", s);
        }
    }

    public void t13() {
        Tries tries = new Tries();
        // tmpdict1
        // dictionary.gz
        // tmpdictionary.gz
        String subpath = "src/main/resources/";
        String filename = subpath + "tmpdict1";
        filename = subpath + "tmpdict2.gz";
        String curDir = System.getProperty("user.dir");
        p("Current dir: %s\n", curDir);
        String fullpath = curDir + "/" + filename;
        tries.loadWordsFromFile(fullpath);
        p("PRINT\n");
        tries.print();
        p("\nNOW do lookups\n");
        p("lookup ca\n");
        tries.printPrefixSet("ca");
        p("lookup ch\n");
        tries.printPrefixSet("ch");
        p("lookup c\n");
        tries.printPrefixSet("c");
    }
    
    public void t14() {
        int size = 8;
        ByteBuffer bb = ByteBuffer.allocate(size);
        String s = 
                "abc defg hij k l mno p q rs tu v w xy z123" + 
                "little bo beep lost her sheep.";
        int idxs = 0;
        int max = s.length();
        List<String> list = new ArrayList<>();
        
        while(idxs < max) {
            int remaining = max - idxs;
            int tmpsize = (remaining < size) ? remaining : size;
            int idxe = idxs + tmpsize;
            String stmp = s.substring(idxs, idxe);
            bb.put(stmp.getBytes());
            
        }
        
        for(int i = 0; i < list.size(); i++) {
            p("%2d: %s\n", i, list.get(i));
        }
    }

    public void t15() {
        StringsTest test = new StringsTest();
        test.testLongestSubstringNR1();
    }

    public void t16() {
        GarbageCollectionTest t = new GarbageCollectionTest();
        t.tGarbageCollection();
    }

    public void t17() {
    }

    public void t18() {
    }

    public void t19() {
    }

    public void t20() {
    }

    public void t21() {
    }

    public void t22() {
    }

    public void t23() {
    }
    
    public void t24() {
    }

    public void t25() {
    }

    public void t26() {
    }

    public void t27() {
    }

    public void t28() {
    }

    public void t29() {
    }
    
    public void test() {
        
    }

}

class SetupMisc {
    
    Utils u = new Utils();

    Misc puzzles = new Misc();

    List<Integer> getList1() {
        return Arrays.asList(10,4,20,3,6,2,5);
    }
    
    void tMaxConsecutiveSequence1() {
        List<Integer> l = getList1();
        int max = puzzles.maxConsecutiveSequence(l);
        u.pl(l);
        u.p("max: %2d\n", max);
    }
    
    
}

