package org.uranus.configuration.loader;

import java.util.UnknownFormatConversionException;

import org.uranus.configuration.ConfigureOption;

/**
 * Only a HINT custom loader
 * 
 * @author Michael xixuan.lx
 *
 */
public class UserDefinedConfigureLoader extends AbstractConfigureLoader
{
	public UserDefinedConfigureLoader() {
		super();
	}

	public UserDefinedConfigureLoader(ConfigureOption option) {
		super(option);
	}

	@Override
	protected Boolean readBoolean(String key, String value) throws UnknownFormatConversionException {
		throw new NumberFormatException("this method should be implement");
	}
	
	@Override
	protected Double readNumber(String key, String value) throws UnknownFormatConversionException {
		throw new NumberFormatException("this method should be implement");
	}

	@Override
	protected String readString(String key, String value) throws NumberFormatException {
		throw new NumberFormatException("this method should be implement");
	}

	@Override
	protected Object readObject(String key, String value) throws NumberFormatException {
		throw new NumberFormatException("this method should be implement");
	}
}
