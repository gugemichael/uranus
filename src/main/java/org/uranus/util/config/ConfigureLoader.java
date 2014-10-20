package org.uranus.util.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Properties;
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
public interface ConfigureLoader<E>
{
	/**
	 * parse java properties style liked conf string
	 * @throws IOException 
	 * @throws IllegalAccessException 
	 */
	public boolean parse(Class<E> c, String conf) throws UnknownFormatConversionException, IOException, IllegalAccessException;
	
	/**
	 * parse java properties style liked conf from file
	 */
	public boolean parse(Class<E> c, File conf) throws UnknownFormatConversionException, IOException, IllegalAccessException;

}


/**
 * 	Parsing Style :
 * 
 * 		String ==> "abs"
 * 		short/int/long ==> 1000
 * 		float/double ==> 10.01
 * 		boolean ==> true,false
 * 
 */
abstract class AbstractConfigureLoader<E> implements ConfigureLoader<E>
{
	private final ConfigureOptional option;
	
	public AbstractConfigureLoader() {
		this.option = ConfigureOptional.DISCARD;
	}
	
	public AbstractConfigureLoader(ConfigureOptional option) {
		this.option = option;
	}

	/**
	 * parse java properties style liked conf string
	 */
	@Override
	public boolean parse(Class<E> c, String conf) throws IllegalAccessException, UnknownFormatConversionException, IOException {
		Properties prop = new Properties();
		String v = null;
		long number;
			prop.load(new StringReader(conf));
			int count = prop.size(), match = 0;
			for (Field x : c.getDeclaredFields()) {
				// must static
				if ((x.getModifiers() & Modifier.STATIC) == 0 || (x.getModifiers() & Modifier.PUBLIC) == 0)
					continue;
				if (x.getAnnotation(ConfigureKey.class) != null) {
					v = prop.getProperty(x.getAnnotation(ConfigureKey.class).value());
					if (v == null) {
						if (option == ConfigureOptional.DISCARD)
							continue;
						else if (option == ConfigureOptional.ABORT)
							return false;
						else if (option == ConfigureOptional.EXCEPTION)
							throw new IllegalAccessException(x.getName());
					}
				} else 
					continue;

				Class<?> type = x.getType();

				if (type == int.class || type == short.class || type == long.class) {
					try {
						number = readNumber(v);
						if (type == int.class)
							x.setInt(null, (int) number);
						else if (type == short.class)
							x.setShort(null, (short) number);
						else if (type == long.class)
							x.setLong(null, number);
					} catch (NumberFormatException e) {
						throw new UnknownFormatConversionException(x.getName());
					}
				} else if (type == boolean.class) {
					try {					
						x.setBoolean(null, readBoolean(v));
					} catch (UnknownFormatConversionException e) {
						throw e;
					}
				} else if (type == float.class || type == double.class) {
					try {
						if (type == float.class)
							x.setFloat(null, Float.parseFloat(v));
						else if (type == double.class)
							x.setDouble(null, Double.parseDouble(v));
					} catch(NumberFormatException e) {
						throw new UnknownFormatConversionException(x.getName());
					}
				} else if (type == String.class)
					x.set(null, v);
				else
					throw new UnknownFormatConversionException(x.getName());

				match++;
			}
			
			System.out.println(match + "" + count);
			return match == count;
	}

	/**
	 * parse java properties style liked conf from file
	 */
	@Override
	public boolean parse(Class<E> c, File conf) throws UnknownFormatConversionException, IOException, IllegalAccessException{
		if (!conf.exists())
			return false;
		StringBuffer buffer = new StringBuffer();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(conf));
			// read file content
			while(true) {
				String line = reader.readLine();
				if (line == null)
					break;
				else
					buffer.append(line);
			}
			return parse(c, buffer.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return false;
	}

	/**
	 *	support "true","false" defaultly 
	 */
	protected abstract boolean readBoolean(String v) throws UnknownFormatConversionException ;

	protected abstract long readNumber(String v) throws NumberFormatException;
}


