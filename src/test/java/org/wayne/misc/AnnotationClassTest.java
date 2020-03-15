package org.wayne.misc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// annotations retained at source, compile, ignored in runtime
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD,ElementType.TYPE,ElementType.CONSTRUCTOR})
public @interface AnnotationClassTest {
    public String v1() default "";
    public String v2() default "";
}
