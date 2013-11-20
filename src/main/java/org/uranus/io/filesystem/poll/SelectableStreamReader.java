package org.uranus.io.filesystem.poll;

import java.io.BufferedReader;

public interface SelectableStreamReader extends SelectableReader
{
	public BufferedReader getReader();
}
