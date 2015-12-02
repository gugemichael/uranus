package org.uranus.configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface ConfigureKey {
	
	public static class Type<T> {
		
	}
	
	/**
	 * config key name
	 */
	public String key() default "";
	
	/**
	 * is required config key
	 * 
	 * we will skip the not found key if annotation with required=false
	 */
	public boolean required() default true;
}
