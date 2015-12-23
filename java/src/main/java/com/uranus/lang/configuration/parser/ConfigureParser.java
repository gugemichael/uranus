package com.uranus.lang.configuration.parser;

import java.util.Map;

import javax.xml.crypto.dsig.keyinfo.KeyValue;

/**
 * Variaty of config string parse into Map key 
 * value. only {@link parse} implement
 * 
 * @author Michael xixuan.lx
 *
 */
public interface ConfigureParser {

	/**
	 * parse variety struct config into KeyValue {@link KeyValue} model
	 * 
	 * @param conent
	 *            string based configuration
	 * 
	 * @return key value pair
	 * 
	 */
	public Map<String, String> parse(String content);

}
