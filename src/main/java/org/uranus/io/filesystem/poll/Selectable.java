package org.uranus.io.filesystem.poll;


public abstract interface Selectable
{
	/**
	 *  callback and return the ready selectable , choose THE ONE
	 */
	public void readyCallback();
}
