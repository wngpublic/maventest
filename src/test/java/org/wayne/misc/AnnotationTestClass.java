package org.wayne.misc;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class AnnotationTestClass {
    @AnnotationRuntimeMethodTest
    public void testMethod1() {
        int a = 0;
        if(a == 0) {
            return;
        }
    }

    @AnnotationRuntimeTest
    @AnnotationRuntimeMethodTest
    public void testMethod2() {
        int a = 0;
        if(a == 0) {
            return;
        }
    }

    @AnnotationSourceTest
    public void testMethod3() {
        int a = 0;
        if(a == 0) {
            return;
        }
    }

    @AnnotationSourceTest
    @AnnotationClassTest
    public void testMethod4() {
        int a = 0;
        if(a == 0) {
            return;
        }
    }

    @AnnotationRuntimeMethodTest
    @AnnotationSourceTest
    @AnnotationClassTest
    public void testMethod5() {
        int a = 0;
        if(a == 0) {
            return;
        }
    }

    @AnnotationClassTest
    public void testMethod6() {
        int a = 0;
        if(a == 0) {
            return;
        }
    }

    @AnnotationSourceTest
    @AnnotationClassTest
    public void testMethod7() {
        int a = 0;
        if(a == 0) {
            return;
        }
    }

    @AnnotationRuntimeTest
    @AnnotationSourceTest
    public void testMethod8() {
        int a = 0;
        if(a == 0) {
            return;
        }
    }

    @AnnotationRuntimeTest
    @AnnotationClassTest
    public void testMethod9() {
        int a = 0;
        if(a == 0) {
            return;
        }
    }

    @AnnotationRuntimeTest
    @AnnotationSourceTest
    public void testMethodA() {
        int a = 0;
        if(a == 0) {
            return;
        }
    }

    public Map<String, Set<String>> getRuntimeReferenceCanonicalNamesMap() {
        HashMap<String, Set<String>> map = new HashMap<>();
        map.put("testMethod1", new HashSet<>(Arrays.asList(
            AnnotationRuntimeMethodTest.class.getCanonicalName()
        )));
        map.put("testMethod2", new HashSet<>(Arrays.asList(
            AnnotationRuntimeMethodTest.class.getCanonicalName(),
            AnnotationRuntimeTest.class.getCanonicalName()
        )));
        map.put("testMethod3", new HashSet<>(Arrays.asList(
        )));
        map.put("testMethod4", new HashSet<>(Arrays.asList(
        )));
        map.put("testMethod5", new HashSet<>(Arrays.asList(
            AnnotationRuntimeMethodTest.class.getCanonicalName()
        )));
        map.put("testMethod6", new HashSet<>(Arrays.asList(
        )));
        map.put("testMethod7", new HashSet<>(Arrays.asList(
        )));
        map.put("testMethod8", new HashSet<>(Arrays.asList(
            AnnotationRuntimeTest.class.getCanonicalName()
        )));
        map.put("testMethod9", new HashSet<>(Arrays.asList(
            AnnotationRuntimeTest.class.getCanonicalName()
        )));
        map.put("testMethodA", new HashSet<>(Arrays.asList(
            AnnotationRuntimeTest.class.getCanonicalName()
        )));
        return map;
    }

    public Map<String, Set<Class<? extends Annotation>>> getRuntimeReferenceMap() {
        HashMap<String, Set<Class<? extends Annotation>>> map = new HashMap<>();
        map.put("testMethod1", new HashSet<>(Arrays.asList(
            AnnotationRuntimeMethodTest.class
        )));
        map.put("testMethod2", new HashSet<>(Arrays.asList(
            AnnotationRuntimeMethodTest.class,
            AnnotationRuntimeTest.class
        )));
        map.put("testMethod3", new HashSet<>(Arrays.asList(
        )));
        map.put("testMethod4", new HashSet<>(Arrays.asList(
        )));
        map.put("testMethod5", new HashSet<>(Arrays.asList(
            AnnotationRuntimeMethodTest.class
        )));
        map.put("testMethod6", new HashSet<>(Arrays.asList(
        )));
        map.put("testMethod7", new HashSet<>(Arrays.asList(
        )));
        map.put("testMethod8", new HashSet<>(Arrays.asList(
            AnnotationRuntimeTest.class
        )));
        map.put("testMethod9", new HashSet<>(Arrays.asList(
            AnnotationRuntimeTest.class
        )));
        map.put("testMethodA", new HashSet<>(Arrays.asList(
            AnnotationRuntimeTest.class
        )));
        return map;
    }

    public Map<String, Set<Class<? extends Annotation>>> getReferenceMap() {
        HashMap<String, Set<Class<? extends Annotation>>> map = new HashMap<>();
        map.put("testMethod1", new HashSet<>(Arrays.asList(
            AnnotationRuntimeMethodTest.class
        )));
        map.put("testMethod2", new HashSet<>(Arrays.asList(
            AnnotationRuntimeMethodTest.class,
            AnnotationRuntimeTest.class
        )));
        map.put("testMethod3", new HashSet<>(Arrays.asList(
            AnnotationSourceTest.class
        )));
        map.put("testMethod4", new HashSet<>(Arrays.asList(
            AnnotationSourceTest.class,
            AnnotationClassTest.class
        )));
        map.put("testMethod5", new HashSet<>(Arrays.asList(
            AnnotationRuntimeMethodTest.class,
            AnnotationSourceTest.class,
            AnnotationClassTest.class
        )));
        map.put("testMethod6", new HashSet<>(Arrays.asList(
            AnnotationClassTest.class
        )));
        map.put("testMethod7", new HashSet<>(Arrays.asList(
            AnnotationSourceTest.class,
            AnnotationClassTest.class
        )));
        map.put("testMethod8", new HashSet<>(Arrays.asList(
            AnnotationRuntimeTest.class,
            AnnotationSourceTest.class
        )));
        map.put("testMethod9", new HashSet<>(Arrays.asList(
            AnnotationRuntimeTest.class,
            AnnotationClassTest.class
        )));
        map.put("testMethodA", new HashSet<>(Arrays.asList(
            AnnotationRuntimeTest.class,
            AnnotationSourceTest.class
        )));
        return map;
    }
}
