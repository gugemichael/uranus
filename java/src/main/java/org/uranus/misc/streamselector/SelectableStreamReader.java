package org.uranus.misc.streamselector;

import java.io.BufferedReader;

public interface SelectableStreamReader extends SelectableReader
{
	public BufferedReader getReader();
}
