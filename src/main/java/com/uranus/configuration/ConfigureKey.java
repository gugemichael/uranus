package com.uranus.configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Configure Key annotation
 * 
 * annotate the Class member field which is desired
 * to be assigned config value
 * 
 * @author Michael xixuan.lx
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface ConfigureKey {
	
	/**
	 * config key name
	 */
	public String key() default "";
	
	/**
	 * is required config key (optional)
	 * 
	 * we will skip the not found key if annotation with required=false
	 */
	public boolean required() default true;
}
