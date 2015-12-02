package org.uranus.configuration;

/**
 * Operate method definition
 * 
 * @author Michael
 *
 */
public class ConfigureOption {
	
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
