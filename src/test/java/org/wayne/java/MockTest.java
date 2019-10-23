package org.wayne.java;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

public class MockTest {
    TesterClass tester;
    TesterInnerClass inner;
    @Before
    public void setup() {
        inner = mock(TesterInnerClass.class);
        tester = new TesterClass();
        tester.setTesterInnerClass(inner);
    }

    @Test
    public void testCaptor() throws IOException {
        ArgumentCaptor<String> argString = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> argString2 = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Integer> argInteger = ArgumentCaptor.forClass(Integer.class);
        tester.foo1("v0");
        tester.foo1("v1");
        tester.foo1("v2");
        tester.foo2(2);
        tester.foo2(3);
        tester.foo3("v1");
        verify(inner, times(3)).setData(argString.capture());
        verify(inner, times(2)).add(argInteger.capture());
        verify(inner).setData2(argString2.capture());
        List<String> list = argString.getAllValues();
        assertEquals("v0", list.get(0));
        assertEquals("v1", list.get(1));
        assertEquals("v2", list.get(2));
        String s;
        s = argString2.getValue();
        assertEquals("v1", s);
    }

    //@Test(expected = IOException.class)
    @Test
    public void testPatternMatching() throws IOException {
        inner = new TesterInnerClass();
        tester = mock(TesterClass.class);
        tester.setTesterInnerClass(inner);

        ArgumentCaptor<String> argString = ArgumentCaptor.forClass(String.class);

        // this is order specific with previous line! anyString overwrites specific rules! note foo1 vs foo3 mock!
        when(tester.foo1(anyString())).thenReturn("test.anystring");
        when(tester.foo1("test")).thenReturn("test.return");
        when(tester.foo3("test")).thenReturn("test.return");
        when(tester.foo3(anyString())).thenReturn("test.anystring");
        when(tester.foo3("test.exception")).thenThrow(new IOException("IOException for test.exception"));

        when(tester.foo1Foo3(eq(anyString()), "test3")).thenReturn("test3.anystring");
        when(tester.foo1Foo3(anyString(), anyString())).thenReturn("test.anystring");
        String result;
        result = tester.foo1("foo");
        assertEquals("test.anystring", result);
        result = tester.foo1("test");
        assertEquals("test.return", result);

        result = tester.foo3("foo2");
        assertEquals("test.anystring", result);
        result = tester.foo3("test");
        assertEquals("test.anystring", result);
        boolean flag = false;
        try {
            result = tester.foo3("test.exception");
            assert false;
        } catch(IOException e) {
            flag = true;
        }
        assert flag;

        verify(tester, times(2)).foo1(argString.capture());
        verify(tester, times(3)).foo3(argString.capture());
    }
}

class TesterClass {
    TesterInnerClass innerRef;
    int ctrFoo1 = 0;
    int ctrFoo2 = 0;
    int ctrFoo3 = 0;
    String foo1 = null;
    int foo2 = 0;
    String foo3 = null;

    public void setTesterInnerClass(TesterInnerClass inner) {
        this.innerRef = inner;
    }

    public String foo1(String s) {
        innerRef.setData(s);
        foo1 = s;
        ctrFoo1++;
        return foo1;
    }
    public int foo2(int i) {
        innerRef.add(i);
        foo2 = i;
        ctrFoo2++;
        return i;
    }
    public String foo3(String s) throws IOException {
        innerRef.setData2(s);
        foo3 = s;
        ctrFoo3++;
        return foo3;
    }
    public String foo1Foo3(String foo1, String foo3) throws IOException {
        foo1(foo1);
        return foo3(foo3);
    }
    public String foo1() {
        return foo1;
    }

    public int getCtrFoo1() {
        return ctrFoo1;
    }

    public int getCtrFoo2() {
        return ctrFoo2;
    }

    public int getCtrFoo3() {
        return ctrFoo3;
    }
}

class TesterInnerClass {
    String data = null;
    int i = 0;
    String data2 = null;
    int ctr1 = 0;
    int ctr2 = 0;
    public void setData(String s) {
        this.data = s;
        ctr1++;
    }

    public String getData() {
        return data;
    }

    public int getI() {
        return i;
    }

    public String getData2() {
        return data2;
    }

    public int getCtr1() {
        return ctr1;
    }

    public int getCtr2() {
        return ctr2;
    }

    public void add(int i) {
        this.i = i;
        ctr2++;
    }
    public void setData2(String s) {
        this.data2 = s;
    }
    public int getTotalAdd() {
        return ctr1;
    }
    public int getTotalSetData() {
        return ctr2;
    }
}