package org.uranus.utils.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UnknownFormatConversionException;

/**
 * 
 * Parse comman key-value pair which can loading with java.util.Properties
 * easily
 * 
 * String = "abs" short/int/long = 1000 float/double = 10.01 boolean =
 * true,false
 * 
 * @author Michael
 * 
 */
public abstract class AbstractConfigureLoader implements ConfigureLoader {
	/*
	 * Operation on unknown type occurred or parse value failed
	 */
	private final ConfigureOptional policy;

	private ConfigureParser PARSER = ConfigureParser.STANDARD_PARSER;

	public AbstractConfigureLoader() {
		this.policy = ConfigureOptional.DISCARD;
	}

	public AbstractConfigureLoader(ConfigureOptional policy) {
		this.policy = policy;
	}

	public AbstractConfigureLoader setConfigureParser(ConfigureParser parser) {
		if (parser != null)
			this.PARSER = parser;
		return this;
	}

	/**
	 * parse properties to class's static member field
	 * 
	 * @param clazz
	 *            target class to be injected
	 * @param conf
	 *            config string conent
	 * 
	 * @return true if success
	 * 
	 */
	@Override
	public boolean parse(Class<?> clazz, String conf) throws IllegalAccessException, UnknownFormatConversionException,
			IOException {

		if (clazz == null || conf == null || conf.isEmpty())
			return false;

		// read properties
		Map<String, String> kv = PARSER.parse(conf);
		int count = kv.size(), match = 0;

		String value = null;

		/**
		 * iterate every member field in clazz , search for someont which is
		 * matched for our Modifier, and get its ConfigureKey annotation 's key
		 * name, finally get the value from the Property. if not found we will
		 * take action with *policy*
		 * 
		 */
		for (Field field : clazz.getDeclaredFields()) {

			// must PUBLIC STATIC
			if ((field.getModifiers() & Modifier.STATIC) == 0 || (field.getModifiers() & Modifier.PUBLIC) == 0)
				continue;

			ConfigureKey annotation = null;
			String key = null;
			
			// only parse field which has annotation 
			if ((annotation = field.getAnnotation(ConfigureKey.class)) == null)
				continue;

			// only concerned with {@Link ConfigureKey}
			if ((key = annotation.value()) == null)
				continue;

			value = kv.get(key);

			if (value == null) {
				switch (policy) {
					case ABORT:
						return false;
					case DISCARD:
						continue;
					case EXCEPTION:
						throw new IllegalAccessException(String.format("config key not found %s, %s", key,
								field.getName()));
					default:
						break;
				}
			}

			// TODO : suppress *Findbug* nullptr hint info
			value = value.trim();

			Class<?> type = field.getType();

			// match class type
			if (type == int.class || type == short.class || type == long.class) { /* number */
				try {
					long number = readNumber(value);
					if (type == int.class)
						field.setInt(null, (int) number);
					else if (type == short.class)
						field.setShort(null, (short) number);
					else if (type == long.class)
						field.setLong(null, number);
				} catch (NumberFormatException e) {
					throw new UnknownFormatConversionException(key);
				}

			} else if (type == boolean.class) { /* boolean */
				try {
					field.setBoolean(null, readBoolean(value));
				} catch (UnknownFormatConversionException e) {
					// rethrow
					throw e;
				}

			} else if (type == float.class || type == double.class) { /* float */
				try {
					if (type == float.class)
						field.setFloat(null, Float.parseFloat(value));
					else if (type == double.class)
						field.setDouble(null, Double.parseDouble(value));
				} catch (NumberFormatException e) {
					throw new UnknownFormatConversionException(key);
				}

			} else if (type == String.class) /* string */
				field.set(null, value);

			else if (type == List.class) { /* List */
				field.set(null, Arrays.asList(value.split(",")));

			} else if (type == Set.class) { /* Set */
				field.set(null, new HashSet<String>(Arrays.asList(value.split(","))));

			} else if (type.isEnum()) { /* Enum */
				for (Object item : type.getEnumConstants()) 
					if (item.toString().equals(value)) {
						field.set(null, item);
						break;
					}
				
				// enum not matched
				if (field.get(null) == null)
					throw new UnknownFormatConversionException(key);

			} else
				throw new UnknownFormatConversionException(key);

			match++;
		}

		return match == count;
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public boolean parse(Class<?> clazz, File conf) throws UnknownFormatConversionException, IOException,
			IllegalAccessException {

		if (clazz == null || conf == null || !conf.exists())
			return false;

		char[] buffer = new char[4096];
		StringBuffer content = new StringBuffer();
		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new FileReader(conf));
			// read file content
			while (true) {
				int len = reader.read(buffer);
				if (len == -1)
					break;
				content.append(buffer, 0, len);
			}

			if (content.length() == 0)
				throw new IOException("config file content empty");

			return parse(clazz, content.toString());

		} finally {
			if (reader != null)
				reader.close();
		}
	}

	/**
	 * support "true","false" defaultly
	 */
	protected abstract boolean readBoolean(String v) throws UnknownFormatConversionException;

	protected abstract long readNumber(String v) throws NumberFormatException;
}
