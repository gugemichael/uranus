package org.uranus.util.config;

import java.util.UnknownFormatConversionException;

/**
 * Normal value parse-able configure loader 
 * 
 * @author Michael
 *
 */
public class KVConfigureLoader extends AbstractConfigureLoader
{
	public KVConfigureLoader(ConfigureOptional option) {
		super(option);
	}
	
	/**
	 *	support "true","false" defaultly 
	 *
	 */
	@Override
	protected boolean readBoolean(String v) throws UnknownFormatConversionException {
		return Boolean.parseBoolean(v);
	}
	
	@Override
	protected long readNumber(String v) throws NumberFormatException {
		return Long.parseLong(v);
	}

}
