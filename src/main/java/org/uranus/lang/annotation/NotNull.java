package org.uranus.lang.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * The annotated element must not be {@code null}. Accepts any type.
 *
 * @author Emmanuel Bernard
 */
@Target({LOCAL_VARIABLE, METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER,})
@Retention(RUNTIME)
@Documented
public @interface NotNull {

    String message() default "{javax.validation.constraints.NotNull.message}";

    Class<?>[] groups() default {};

    Class<? extends Void>[] payload() default {};

    /**
     * Defines several {@link NotNull} annotations on the same element.
     */
    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
    @Retention(RUNTIME)
    @Documented
    @interface List {

        NotNull[] value();
    }
}