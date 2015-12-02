package org.uranus.configuration.loader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UnknownFormatConversionException;

import org.uranus.configuration.ConfigLoadException;
import org.uranus.configuration.ConfigureKey;
import org.uranus.configuration.ConfigureOption;
import org.uranus.configuration.ConfigureParser;
import org.uranus.configuration.GenericStruct;
import org.uranus.configuration.ConfigLoadException.ExceptionCode;
import org.uranus.configuration.ConfigureOption.ConfigureParsePolicy;

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
	private final ConfigureOption option;

	private ConfigureParser PARSER = ConfigureParser.STANDARD_PARSER;

	public AbstractConfigureLoader() {
		
		this(
				new ConfigureOption().setParsePolicy(ConfigureParsePolicy.EXCEPTION)
		);
		
	}

	public AbstractConfigureLoader(ConfigureOption policy) {
		this.option = policy;
	}

	public AbstractConfigureLoader setConfigureParser(ConfigureParser parser) {
		if (parser != null)
			this.PARSER = parser;
		return this;
	}
	
	@SuppressWarnings("unchecked")
	private void resolvMember(Field member, Class<?> type, final String key, final String value) throws IllegalAccessException, ConfigLoadException {
		
		if (type == int.class || type == short.class || type == long.class || type == float.class || type == double.class) {			 /* number */
			Double number = readNumber(key, value);
			if (type == int.class)
				member.setInt(null, number.intValue());
			else if (type == short.class)
				member.setShort(null, number.shortValue());
			else if (type == long.class)
				member.setLong(null, number.longValue());
			else if (type == float.class)
				member.setFloat(null, number.floatValue());
			else if (type == double.class)
				member.setDouble(null, number.doubleValue());
			
		} else if (type == boolean.class) { 							/* boolean */
			member.setBoolean(null, readBoolean(key, value).booleanValue());

		} else if (type == String.class) {								/* string */
			member.set(null, readString(key, value));

		} else if (type == List.class || type == Set.class) { 		/* List and Set */
			
			String[] values = value.split(",");
			
			// generic type
			Type generic = ((ParameterizedType) member.getGenericType()).getActualTypeArguments()[0];
			
			@SuppressWarnings("rawtypes")
			List list = GenericStruct.makeGenericList(generic);
			
			// TODO : merge with *GenericStruct*
			for (String v : values) {
				if (generic.equals(String.class))
					list.add(readString(key, v));
				else if (generic.equals(Short.class))
					list.add(readNumber(key, v).shortValue());
				else if (generic.equals(Integer.class))
					list.add(readNumber(key, v).intValue());
				else if (generic.equals(Long.class))
					list.add(readNumber(key, v).longValue());
				else if (generic.equals(Float.class))
					list.add(readNumber(key, v).floatValue());
				else if (generic.equals(Double.class))
					list.add(readNumber(key, v).doubleValue());
				else if (generic.equals(Byte.class))
					list.add(readBoolean(key, v));
				else 
					list.add(readObject(key, v));
			}
			
			if (type == Set.class)
				member.set(null, new HashSet<Object>(list));
			else
				member.set(null, list);

		} else if (type.isEnum()) { 										/* enum */
			
			for (Object item : type.getEnumConstants())
				if (item.toString().equals(value)) {
					member.set(null, item);
					break;
				}

			// enum not matched
			if (member.get(null) == null)
				throw new ConfigLoadException(ExceptionCode.CONFIG_KEY_PARSE_FAILED, String.format("enum type %s parse key %s failed", type.getSimpleName(), key));

		} else {																	/* Object */
			
			member.set(null, readObject(key, value));
		}
	}

	/**
	 * Parse properties style (key=value liked) conf from Map
	 *
	 * @throws IOException
	 * @throws IllegalAccessException
	 */
	public void parse(Class<?> clazz, Map<String, String> kv) throws ConfigLoadException {
		if (clazz == null || kv == null || kv.isEmpty())
			throw new ConfigLoadException(ExceptionCode.CONFIG_ARGUMENT_INVALID, "key value map or clazz is invalid");

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


			// only annotated with ConfigureKey
			ConfigureKey annotation = field.getAnnotation(ConfigureKey.class);
			if (annotation == null)
				continue;
			
			final String annotationKey = annotation.key();

			// only concerned with {@Link ConfigureKey}
			if (annotationKey == null)
				continue;

			value = kv.get(annotationKey);

			if (value == null) {

				// skip only when not required and in kvMap, with nullValue
				if (!annotation.required())
					continue;

				// TODO : not beautiful !
				switch (option.getParsePolicy()) {
					case EXCEPTION:
						throw new ConfigLoadException(ExceptionCode.CONFIG_KEY_NOT_FOUND, String.format("config key not found %s, %s", annotationKey, field.getName()));
					default:
						break;
				}
			}

			// TODO : suppress *Findbug* nullptr hint info
			value = value.trim();

			Class<?> type = field.getType();

			try {
				resolvMember(field, type, annotationKey, value);
			} catch (NumberFormatException e) {
				throw new ConfigLoadException(ExceptionCode.CONFIG_KEY_PARSE_FAILED, String.format("convert annotation key %s failed", annotationKey));
			} catch (IllegalArgumentException e) {
				throw new ConfigLoadException(ExceptionCode.CONFIG_KEY_PARSE_FAILED, String.format("set annotation key %s failed", annotationKey));
			} catch (IllegalAccessException e) {
				throw new ConfigLoadException(ExceptionCode.CONFIG_KEY_ACCESS_INVALID, String.format("access annotation key %s failed", annotationKey));
			}
		}
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
	public void parse(Class<?> clazz, String conf) throws ConfigLoadException {

		if (clazz == null || conf == null || conf.isEmpty())
			throw new ConfigLoadException(ExceptionCode.CONFIG_ARGUMENT_INVALID, "config string or clazz is invalid");

		// read properties
		Map<String, String> kv = PARSER.parse(conf);
		
		parse(clazz, kv);
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public void parse(Class<?> clazz, File conf) throws ConfigLoadException, IOException {

		if (clazz == null || conf == null || !conf.exists() || !conf.canRead())
			throw new ConfigLoadException(ExceptionCode.CONFIG_ARGUMENT_INVALID, "config file or clazz is invalid");

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

			parse(clazz, content.toString());

		} finally {
			if (reader != null)
				reader.close();
		}
	}

	/**
	 * support "true","false" defaultly
	 */
	protected abstract Boolean readBoolean(String key, String value) throws UnknownFormatConversionException;

	protected abstract Double readNumber(String key, String value) throws NumberFormatException;
	
	protected abstract String readString(String key, String value) throws NumberFormatException;
	
	protected abstract Object readObject(String key, String value) throws NumberFormatException;
}
