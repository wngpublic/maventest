package org.wayne.testcases;

//import org.junit.Test;

import junit.framework.TestCase;

/**
 * class name MUST end in Test, not Tests,
 * else test does not run.
 * 
 */
public class UnitTestsTest extends TestCase
{
    Testcases t = new Testcases();

    private void p(String f, Object ...o) {
        System.out.printf(f, o);
    }

    // all tests must be test*, not t0
    
    public void test0() {
        p("UnitTests t0\n");
        t.t0();
    }

    public void test1() {
        p("UnitTests t1\n");
    }

    public void test2() {
        p("UnitTests t2\n");
        t.t2();
    }

    public void test3() {
        p("UnitTests t3\n");
        t.t3();
    }

    public void test4() {
        p("UnitTests t4\n");
        t.t4();
    }

    public void test5() {
        p("UnitTests t5\n");
        t.t5();
    }
}
