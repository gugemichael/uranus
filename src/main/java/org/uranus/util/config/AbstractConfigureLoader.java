package org.uranus.util.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.UnknownFormatConversionException;


/**
 * 	Parsing Style :
 * 
 * 		String ==> "abs"
 * 		short/int/long ==> 1000
 * 		float/double ==> 10.01
 * 		boolean ==> true,false
 * 
 * @author Michael
 * 
 */
public abstract class AbstractConfigureLoader implements ConfigureLoader
{
	private final ConfigurePolicy option;
	
	public AbstractConfigureLoader() {
		this.option = ConfigurePolicy.DISCARD;
	}
	
	public AbstractConfigureLoader(ConfigurePolicy option) {
		this.option = option;
	}

	/**
	 * parse java properties style liked conf string
	 */
	@Override
	public boolean parse(Class<?> c, String conf) throws IllegalAccessException, UnknownFormatConversionException, IOException {
		if (c == null || conf == null) {
			if (option == ConfigurePolicy.DISCARD || option == ConfigurePolicy.ABORT)
				return false;
			else if (option == ConfigurePolicy.EXCEPTION)
				throw new IOException("inject class or config is null");
		}
		Properties prop = new Properties();
		String v = null;
		long number;
		prop.load(new StringReader(conf));
		int count = prop.size(), match = 0;
		for (Field x : c.getDeclaredFields()) {
			// must static
			if ((x.getModifiers() & Modifier.STATIC) == 0 || (x.getModifiers() & Modifier.PUBLIC) == 0)
				continue;
			if (x.getAnnotation(ConfigureKey.class) == null) 
				continue;
			
			v = prop.getProperty(x.getAnnotation(ConfigureKey.class).value());
			if (v == null) {
				if (option == ConfigurePolicy.DISCARD)
					continue;
				else if (option == ConfigurePolicy.ABORT)
					return false;
				else if (option == ConfigurePolicy.EXCEPTION)
					throw new IllegalAccessException(x.getName());
			}

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
					if (option == ConfigurePolicy.DISCARD)
						continue;
					else if (option == ConfigurePolicy.ABORT)
						return false;
					else if (option == ConfigurePolicy.EXCEPTION)
						throw new UnknownFormatConversionException(x.getName());
				}
			} else if (type == boolean.class) {
				try {					
					x.setBoolean(null, readBoolean(v));
				} catch (UnknownFormatConversionException e) {
					if (option == ConfigurePolicy.DISCARD)
						continue;
					else if (option == ConfigurePolicy.ABORT)
						return false;
					else if (option == ConfigurePolicy.EXCEPTION)
						throw e;
				}
			} else if (type == float.class || type == double.class) {
				try {
					if (type == float.class)
						x.setFloat(null, Float.parseFloat(v));
					else if (type == double.class)
						x.setDouble(null, Double.parseDouble(v));
				} catch(NumberFormatException e) {
					if (option == ConfigurePolicy.DISCARD)
						continue;
					else if (option == ConfigurePolicy.ABORT)
						return false;
					else if (option == ConfigurePolicy.EXCEPTION)
						throw new UnknownFormatConversionException(x.getName());
				}
			} else if (type == String.class)
				x.set(null, v.trim());
			else if (type == List.class)
				x.set(null, Arrays.asList(v.trim().split(",")));
			else if (type == Set.class)
				x.set(null, new HashSet<String>(Arrays.asList(v.trim().split(","))));
			else {
				if (option == ConfigurePolicy.DISCARD)
					continue;
				else if (option == ConfigurePolicy.ABORT)
					return false;
				else if (option == ConfigurePolicy.EXCEPTION)
					throw new UnknownFormatConversionException(x.getName());
			}
				

			match++;
		}

		return match == count;
	}

	/**
	 * parse java properties style liked conf from file
	 */
	@Override
	public boolean parse(Class<?> c, File conf) throws UnknownFormatConversionException, IOException, IllegalAccessException{
		if (c == null || conf == null) {
			if (option == ConfigurePolicy.DISCARD || option == ConfigurePolicy.ABORT)
				return false;
			else if (option == ConfigurePolicy.EXCEPTION)
				throw new IOException("inject class or config is null");
		}
		StringBuffer buffer = new StringBuffer();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(conf));
			// read file content
			while(true) {
				String line = reader.readLine();
				if (line == null)
					break;
				buffer.append(line).append("\n");
			}
			return parse(c, buffer.toString());
		} catch (IOException e) {
			if (option == ConfigurePolicy.DISCARD || option == ConfigurePolicy.ABORT)
				return false;
			else if (option == ConfigurePolicy.EXCEPTION)
				throw e;
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