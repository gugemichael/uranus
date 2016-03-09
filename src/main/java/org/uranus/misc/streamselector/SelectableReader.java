package org.uranus.misc.streamselector;


public interface SelectableReader
{
	/**
	 *  callback and return the ready selectable , choose THE ONE
	 */
    void readyCallback(String lines);
}
