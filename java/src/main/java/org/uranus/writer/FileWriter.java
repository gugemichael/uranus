package org.uranus.writer;

import java.io.IOException;


public interface FileWriter extends Writer{
	
	enum WriteMode {
		APPEND, TRUNCATE
	}
	
	/**
	 * open file with full path name and mode
	 * 
	 * @param fileName, full path name
	 * @param mode, {@link WriteMode} 
	 * 
	 * @return true if open success
	 */
	public boolean open(String fileName, WriteMode mode) throws IOException;
	
	/**
	 * write with content string type
	 * 
	 * @param content
	 */
	public void writeLine(String content);
}
