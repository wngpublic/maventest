package org.wayne.misc;

import junit.framework.TestCase;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AnnotationReflectionTest extends TestCase {
    @Test
    public void testAnnotationTestClassAnnotations() {
        AnnotationTestClass annotationTestClass = new AnnotationTestClass();
        Map<String, Set<Class<? extends Annotation>>> mapAllReference = annotationTestClass.getReferenceMap();
        Map<String, Set<Class<? extends Annotation>>> mapRuntimeReference = annotationTestClass.getRuntimeReferenceMap();


        // sanity check
        {
            Set<Class<? extends Annotation>> setAnnotation = mapAllReference.get("testMethod5");
            assertTrue(setAnnotation.contains(AnnotationRuntimeMethodTest.class));
            assertTrue(setAnnotation.contains(AnnotationSourceTest.class));
            assertTrue(setAnnotation.contains(AnnotationClassTest.class));
            Set<Class<? extends Annotation>> setRuntimeAnnotation = mapRuntimeReference.get("testMethod5");
            assertTrue(setRuntimeAnnotation.contains(AnnotationRuntimeMethodTest.class));
            assertTrue(!setRuntimeAnnotation.contains(AnnotationSourceTest.class));
            assertTrue(!setRuntimeAnnotation.contains(AnnotationClassTest.class));
        }

        Class clazz = annotationTestClass.getClass();
        Method[] methods = clazz.getMethods();
        Map<String, List<String>> map = new HashMap<>();
        for(Method method: methods) {
            String methodName = method.getName();
            if(methodName.indexOf("test") == 0) {
                // gets annotations declared in this class and inherited
                Annotation[] annotations = method.getAnnotations();

                // gets annotations declared in this class, not inherited
                Annotation[] annotationsDeclared = method.getDeclaredAnnotations();

                Annotation[][] annotationsParameter = method.getParameterAnnotations();

                Set<Class<? extends Annotation>> setRuntimeAnnotation = mapRuntimeReference.get(methodName);

                assertTrue(setRuntimeAnnotation != null);
                assertTrue(setRuntimeAnnotation.size() == annotations.length);

                for(Annotation annotation: annotations) {
                    String annotationString = annotation.toString();
                    Class clazzAnnotation = annotation.annotationType();
                    String annotationName = clazz.getName();
                    if(annotationName == null) {
                        continue;
                    }
                }
                List<Annotation> annotationList = new ArrayList<>();
                List<Annotation> list1 = Arrays.asList(annotations);
                List<Annotation> list2 = Arrays.asList(annotationsDeclared);
            }
            else {
                continue;
            }
        }
    }
}
