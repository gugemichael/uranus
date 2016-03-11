package org.uranus.configuration.parser;

import java.io.IOException;
import java.io.StringReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Default parser ! Java porperties liked standard parse engine
 * 
 * @author Michael xixuan.lx
 * 
 */

public class KeyValueParser implements ConfigureParser {

	@Override
	public Map<String, String> parse(String content) {

		// without throw NullPointerException
		if (content == null)
			return null;

		try {
			Properties props = new Properties();
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

}