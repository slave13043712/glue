package org.aakimov.glue.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that can be used to automatically generate immutable data class for the target interface.
 *
 * Target interface must contatin only getter methods that are not generic and do not throw any exceptions.
 *
 * @author aakimov
 */
@Retention(value = RetentionPolicy.CLASS)
@Target(value = {ElementType.TYPE})
public @interface ImmutableImplementation {
}
