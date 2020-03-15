package org.wayne.misc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// annotations retained at source, ignored in compile, and runtime
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.METHOD,ElementType.TYPE,ElementType.CONSTRUCTOR})
public @interface AnnotationSourceTest {
    public String v1() default "";
    public String v2() default "";
}
