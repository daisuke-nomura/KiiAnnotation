package com.kyaracter.kiiannotation.annotations;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface ApplicationScope {
    String name();
    boolean simplify() default true;
    boolean builder() default true;
    String suffix() default "Bucket";
}
