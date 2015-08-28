package org.uranus.utils.config;

import java.io.File;
import java.io.IOException;
import java.util.UnknownFormatConversionException;

/**
 * 
 * Parse properties kv paire configure !
 * 
 * E must has the same PUBLIC & STATIC class
 * member for save them , wrong type class
 * member will be ignore
 * 
 * we support :
 * 		
 * 		String / boolean / long / double / Enum
 * 
 * 
 * @author Michael
 *
 */
public interface ConfigureLoader
{
	/**
	 * parse java properties style liked conf string
	 * @throws IOException 
	 * @throws IllegalAccessException 
	 */
	public boolean parse(Class<?> c, String conf) throws UnknownFormatConversionException, IOException, IllegalAccessException;
	
	/**
	 * parse java properties style liked conf from file
	 */
	public boolean parse(Class<?> c, File conf) throws UnknownFormatConversionException, IOException, IllegalAccessException;

}



