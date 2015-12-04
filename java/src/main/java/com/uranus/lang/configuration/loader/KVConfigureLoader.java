package com.uranus.lang.configuration.loader;

import java.util.UnknownFormatConversionException;

import com.uranus.lang.configuration.ConfigureOption;

/**
 * Normal value parse-able configure loader 
 * 
 * @author Michael xixuan.lx
 *
 */
public class KVConfigureLoader extends AbstractConfigureLoader
{
	public KVConfigureLoader() {
		super();
	}

	public KVConfigureLoader(ConfigureOption option) {
		super(option);
	}
	
	/**
	 *	support "true","false" defaultly 
	 *
	 */
	@Override
	protected Boolean readBoolean(String key, String value) throws UnknownFormatConversionException {
		return Boolean.parseBoolean(value);
	}
	
	@Override
	protected Double readNumber(String key, String value) throws UnknownFormatConversionException {
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
