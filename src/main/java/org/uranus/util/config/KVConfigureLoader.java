package org.uranus.util.config;

import java.util.UnknownFormatConversionException;

public class KVConfigureLoader<E> extends AbstractConfigureLoader<E>
{
	public KVConfigureLoader(ConfigureOptional option) {
		super(option);
	}
	
	public KVConfigureLoader() {
	}
	
	/**
	 *	support "true","false" defaultly 
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
