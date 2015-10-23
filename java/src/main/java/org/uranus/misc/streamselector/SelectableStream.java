package org.uranus.misc.streamselector;

import java.io.BufferedReader;

public interface SelectableStream extends Selectable
{
	public BufferedReader getReader();
}
