package org.uranus.io.filestream;

import java.io.BufferedReader;

public interface SelectableStream extends Selectable
{
	public BufferedReader getReader();
}
