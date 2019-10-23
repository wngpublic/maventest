package org.wayne;

import org.wayne.misc.Utils;
import org.wayne.misc.graphs.SuffixTrees;
import org.wayne.misc.graphs.Tries;

import junit.framework.TestCase;

public class TestcasesGeneric extends TestCase {
	private void p(String f, Object ...o) {
		System.out.printf(f, o);
	}

    public void test1() {
    	p("Testcases test1\n");
    }

    public void test2() {
    	p("Testcases test2\n");
    }

    public void test3() {
    	Utils utils = new Utils();
    	String s = utils.getRandString(10);
    	p("Testcases test3 %s\n", s);
    }

    public void test4() {
        Tries tries = new Tries();
        SuffixTrees suffixTrees = new SuffixTrees();
    }

    public void test5() {
        p("Testcases test5\n");
    }
    
}
