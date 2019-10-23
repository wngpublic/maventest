package org.wayne;

import org.wayne.misc.Utils;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase
{
	private void p(String f, Object ...o) {
		System.out.printf(f, o);
	}
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }
    
    public void test1() {
    	p("AppTest test1\n");
    }

    public void test2() {
    	p("AppTest test2\n");
    }

    public void test3() {
    	Utils utils = new Utils();
    	String s = utils.getRandString(10);
    	p("AppTest test3 %s\n", s);
    }
}
