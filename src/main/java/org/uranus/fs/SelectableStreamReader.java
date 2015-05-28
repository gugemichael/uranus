package org.uranus.fs;

import java.io.BufferedReader;

public interface SelectableStreamReader extends SelectableReader
{
	public BufferedReader getReader();
}
