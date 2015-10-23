package org.uranus.misc.streamselector;


public abstract interface Selectable
{
	/**
	 *  callback and return the ready selectable , choose THE ONE
	 */
	public void readyCallback();
}
