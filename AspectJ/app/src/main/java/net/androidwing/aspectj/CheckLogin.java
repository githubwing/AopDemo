package net.androidwing.aspectj;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by wing on 5/9/17.
 */
@Retention(RetentionPolicy.CLASS) @Target(ElementType.METHOD) public @interface CheckLogin {
}
