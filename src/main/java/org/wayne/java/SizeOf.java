package org.wayne.java;

import java.lang.instrument.Instrumentation;

public class SizeOf {
    private static Instrumentation instrumentation;
    public static void premain(String args, Instrumentation inst) {
        instrumentation = inst;
    }
    public static long sizeof(Object o) {
        return instrumentation.getObjectSize(o);
    }

}
