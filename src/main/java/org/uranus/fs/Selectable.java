package org.uranus.fs;


public abstract interface Selectable
{
	/**
	 *  callback and return the ready selectable , choose THE ONE
	 */
	public void readyCallback();
}
