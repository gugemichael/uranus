package org.uranus.configuration.loader;

import org.uranus.configuration.ConfigureOption;

/**
 * Only declared custom loader
 * 
 * @author Michael
 *
 */
public abstract class UserDefinedConfigureLoader extends AbstractConfigureLoader
{
	public UserDefinedConfigureLoader() {
		super();
	}

	public UserDefinedConfigureLoader(ConfigureOption option) {
		super(option);
	}
}
