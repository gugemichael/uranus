package org.uranus.io.writer;

import java.io.IOException;

public interface FileWriter {

	enum WriteMode {
		APPEND, TRUNCATE
	}

	/**
	 * write content
	 * 
	 * @param content
	 */
	public void write(byte content[], int offset, int count);

	/**
	 * write with content string type
	 * 
	 * @param content
	 */
	public void writeLine(String content);

	/**
	 * write something to underlay resource
	 * 
	 * @param content
	 */
	public void flush();

	/**
	 * close the writer
	 */
	public void close();

	/**
	 * open file with full path name and mode
	 * 
	 * @param fileName,
	 *            full path name
	 * @param mode,
	 *            {@link WriteMode}
	 * 
	 * @return true if open success
	 */
	public boolean open(String fileName, WriteMode mode) throws IOException;

}
