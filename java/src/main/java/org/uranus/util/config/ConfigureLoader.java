package org.uranus.util.config;

import java.io.File;
import java.io.IOException;
import java.util.UnknownFormatConversionException;

/**
 * 
 * Load and parse property file key-value pair configure !
 * 
 * Conf holder class supplied must specified the PUBLIC & STATIC 
 * injected member filed annotation with ConfigureKey {@link ConfigureKey}
 * 
 * Supported key type as follows :
 * 		
 * string, boolean, int, long, double, Enum
 * 
 * @author Michael
 *
 */
public interface ConfigureLoader
{
	/**
	 * Parse properties style (key=value liked) conf from String
	 * 
	 * @throws IOException 
	 * @throws IllegalAccessException 
	 */
	public boolean parse(Class<?> c, String conf) throws UnknownFormatConversionException, IOException, IllegalAccessException;
	
	/**
	 * Parse properties style (key=value liked) conf from File class
	 * 
	 */
	public boolean parse(Class<?> c, File conf) throws UnknownFormatConversionException, IOException, IllegalAccessException;

}



