package me.com.butterflydemo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by xulu on 2017/5/26.
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ButterFly {
    int id();
    String click() default "";
}
