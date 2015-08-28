package org.uranus.io.filestream;


public abstract interface SelectableReader
{
	/**
	 *  callback and return the ready selectable , choose THE ONE
	 */
	public void readyCallback(String lines);
}
