package org.uranus.util.config;

import java.util.HashMap;
import java.util.Map;
import java.util.UnknownFormatConversionException;

public class HumanReadableConfigureLoader extends AbstractConfigureLoader
{
	public static long RADIX = 1024L;
	
	private static final Map<Character, Long> capacityMap = new HashMap<Character, Long>() {{
		put('k', 1024L);
		put('K', get('k'));
		put('m', get('k') * RADIX);
		put('M', get('m'));
		put('g', get('m') * RADIX);
		put('G', get('g'));
		put('t', get('g') * RADIX);
		put('T', get('t'));
	}};

	public HumanReadableConfigureLoader(ConfigureOptional option) {
		super(option);
	}
	
	@Override
	protected boolean readBoolean(String v) {
		if (v.equalsIgnoreCase("true") || v.equalsIgnoreCase("on") || v.equalsIgnoreCase("yes"))
			return true;
		else if ((v.equalsIgnoreCase("false") || v.equalsIgnoreCase("off") || v.equalsIgnoreCase("no")))
			return false;
		else
			throw new UnknownFormatConversionException(String.format("value %s can't covert to boolean !", v));
	}

	@Override
	protected long readNumber(String v) {
		if (v.isEmpty())
			throw new NumberFormatException("value is empty !");
		Long flag = capacityMap.get(v.charAt(v.length()-1));
		if (flag != null && v.length() >= 2)
			return Long.parseLong(v.substring(0, v.length()-2)) * flag;
		else
			return Long.parseLong(v);
	}

}
