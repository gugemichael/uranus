package org.uranus.configuration.loader;

import java.util.HashMap;
import java.util.Map;
import java.util.UnknownFormatConversionException;

import org.uranus.configuration.ConfigureOption;

/**
 * Config expressed in human reading format loader 
 * 
 * @author Michael xixuan.lx
 *
 */
public class HumanReadableConfigureLoader extends AbstractConfigureLoader
{
	public static long RADIX = 1024L;
	
	private static final Map<Character, Long> capacityMap = new HashMap<Character, Long>() {
		private static final long serialVersionUID = 1387202577810325534L;
	{
		put('k', RADIX);
		put('K', get('k'));
		put('m', get('k') * RADIX);
		put('M', get('m'));
		put('g', get('m') * RADIX);
		put('G', get('g'));
		put('t', get('g') * RADIX);
		put('T', get('t'));
	}};
	
	public HumanReadableConfigureLoader() {
		super();
	}

	public HumanReadableConfigureLoader(ConfigureOption option) {
		super(option);
	}
	
	@Override
	protected Boolean readBoolean(String key, String value) throws UnknownFormatConversionException {
		if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("on") || value.equalsIgnoreCase("yes"))
			return true;
		else if ((value.equalsIgnoreCase("false") || value.equalsIgnoreCase("off") || value.equalsIgnoreCase("no")))
			return false;
		else
			throw new UnknownFormatConversionException(String.format("value %s can't covert to boolean !", value));
	}

	@Override
	protected Double readNumber(String key, String value) throws NumberFormatException {
		
		if (value.isEmpty())
			throw new NumberFormatException("value is empty !");
		
		Long flag = capacityMap.get(value.charAt(value.length() - 1));
		if (flag != null && value.length() >= 2)
			return Double.parseDouble(value.substring(0, value.length()-1)) * flag;
		else
			return Double.parseDouble(value);
	}

	@Override
	protected String readString(String key, String value) throws NumberFormatException {
		return value;
	}

	@Override
	protected Object readObject(String key, String value) throws NumberFormatException {
		throw new NumberFormatException("this loader not support object read");
	}
}
