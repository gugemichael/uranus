package com.uranus.lang.configuration.parser;

import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Default parser ! Java porperties liked standard parse engine
 * 
 * @author Michael xixuan.lx
 * 
 */
public class KeyValueVariableParser extends KeyValueParser {

	// generic variable regex pattern
	private static final Pattern pattern = Pattern.compile("\\$\\{([a-z0-9-._^@%&)]+)\\}");

	@Override
	public Map<String, String> parse(String content) {

		// use super parse method
		Map<String, String> target = super.parse(content);
		
		Iterator<String> iter = target.keySet().iterator();

		while (iter.hasNext()) {
			String key = iter.next();
			String value = target.get(key);
			Matcher matcher = pattern.matcher(value);
			while (matcher.find())
				target.put(key, value = value.replace(matcher.group(), target.get(matcher.group(1))));
		}

		return target;
	}

}