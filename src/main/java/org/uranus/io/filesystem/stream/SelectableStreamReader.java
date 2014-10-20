package org.uranus.io.filesystem.stream;

import java.io.BufferedReader;

public interface SelectableStreamReader extends SelectableReader
{
	public BufferedReader getReader();
}
