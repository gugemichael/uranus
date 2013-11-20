package org.uranus.io.filesystem.poll;

import java.io.BufferedReader;

public interface SelectableStream extends Selectable
{
	public BufferedReader getReader();
}
