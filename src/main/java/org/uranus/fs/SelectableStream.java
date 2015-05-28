package org.uranus.fs;

import java.io.BufferedReader;

public interface SelectableStream extends Selectable
{
	public BufferedReader getReader();
}
