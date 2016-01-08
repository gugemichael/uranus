package com.uranus.lang.configuration;

/**
 * Operate definition
 * 
 * @author Michael xixuan.lx
 *
 */
public class ConfigureOption {
	
	/**
	 * policy on parsing 
	 * 
	 * @author Michael xixuan.lx
	 *
	 */
	public static enum ConfigureParsePolicy {
		/* DISCARD, ABORT, */ EXCEPTION
	}
	
	ConfigureParsePolicy policy;
	
	public ConfigureOption() {
		
	}
	
	public ConfigureOption setParsePolicy(ConfigureParsePolicy policy) {
		this.policy = policy;
		return this;
	}
	
	public ConfigureParsePolicy getParsePolicy() {
		return policy;
	}
}
