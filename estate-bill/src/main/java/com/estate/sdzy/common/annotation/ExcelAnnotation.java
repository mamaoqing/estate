package com.estate.sdzy.common.annotation;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelAnnotation {

    String value() default "";
    String fmt() default "";
    boolean export () default true;
    boolean master() default false;
    String dist() default  "";
}
