package org.uranus.io.filesystem.poll;


public abstract interface SelectableReader
{
	/**
	 *  callback and return the ready selectable , choose THE ONE
	 */
	public void readyCallback(String lines);
}
