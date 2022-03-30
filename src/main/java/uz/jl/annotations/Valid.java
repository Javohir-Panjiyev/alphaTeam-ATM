package uz.jl.annotations;

import javax.lang.model.element.NestingKind;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Elmurodov Javohir, Mon 5:03 PM. 12/6/2021
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Valid {
    String reg() default "";
}
