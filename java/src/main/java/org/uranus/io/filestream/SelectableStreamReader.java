package org.uranus.io.filestream;

import java.io.BufferedReader;

public interface SelectableStreamReader extends SelectableReader
{
	public BufferedReader getReader();
}
