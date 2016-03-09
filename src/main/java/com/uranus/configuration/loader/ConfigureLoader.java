package com.uranus.configuration.loader;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.uranus.configuration.ConfigureKey;
import com.uranus.configuration.ConfigLoadException;

/**
 * 
 * Load and parse property file key-value pair configure !
 * 
 * Conf holder class supplied must specified the PUBLIC & STATIC 
 * injected member filed annotation with ConfigureKey {@link ConfigureKey}
 * 
 * @author Michael xixuan.lx
 *
 */
public interface ConfigureLoader
{
	/**
	 * Parse properties style (key=value liked) conf from Map
	 *
	 * @throws IOException
	 * @throws ConfigLoadException
	 */
    void parse(Class<?> c, Map<String, String> kv) throws ConfigLoadException;

	/**
	 * Parse properties style (key=value liked) conf from String
	 * 
	 * @throws IOException 
	 * @throws ConfigLoadException 
	 */
    void parse(Class<?> c, String conf) throws ConfigLoadException;
	
	/**
	 * Parse properties style (key=value liked) conf from File class
	 * 
	 */
    void parse(Class<?> c, File conf) throws ConfigLoadException, IOException;

}



