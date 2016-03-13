package org.uranus.configuration.loader;

import org.uranus.configuration.ConfigureOption;

import java.util.UnknownFormatConversionException;

/**
 * Only a HINT custom loader
 * 
 * @author Michael xixuan.lx
 *
 */
public class CustomizeConfigureLoader extends AbstractConfigureLoader
{
	public CustomizeConfigureLoader() {
		super();
	}

	public CustomizeConfigureLoader(ConfigureOption option) {
		super(option);
	}

	@Override
	protected Boolean readBoolean(String key, String value) throws UnknownFormatConversionException {
		throw new NumberFormatException("readBoolean method should be implement");
	}
	
	@Override
	protected Double readNumber(String key, String value) throws UnknownFormatConversionException {
		throw new NumberFormatException("readNumber method should be implement");
	}

	@Override
	protected String readString(String key, String value) throws NumberFormatException {
		throw new NumberFormatException("readString method should be implement");
	}

	@Override
	protected Object readObject(String key, String value) throws NumberFormatException {
		throw new NumberFormatException("readObject method should be implement");
	}
}
