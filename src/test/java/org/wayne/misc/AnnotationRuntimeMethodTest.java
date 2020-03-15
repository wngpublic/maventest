package org.wayne.misc;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// annotations retained at source, compile, and runtime
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.CONSTRUCTOR})
public @interface AnnotationRuntimeMethodTest {
    public String v1() default "";
    public String v2() default "";
}

