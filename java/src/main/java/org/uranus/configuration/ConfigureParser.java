package org.uranus.configuration;

import java.io.IOException;
import java.io.StringReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public interface ConfigureParser {

	/**
	 * Default parser ! Java porperties liked standard parse engine
	 * 
	 */
	public static final ConfigureParser STANDARD_PARSER = new ConfigureParser() {

		@Override
		public Map<String, String> parse(String content) {

			// without throw NullPointerException
			if (content == null)
				return null;

			Properties props = new Properties();

			try {
				props.load(new StringReader(content));
				Map<String, String> result = new HashMap<String, String>();
				Enumeration<Object> iterator = props.keys();
				String key;
				while (iterator.hasMoreElements()) {
					key = (String) iterator.nextElement();
					result.put(key, props.getProperty(key));
				}
				return result;
			} catch (IOException e) {
				return null;
			}
		}

	};

	/**
	 * kv pair
	 */
	public class KeyValue {
		public String key;
		public String value;
	}

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
