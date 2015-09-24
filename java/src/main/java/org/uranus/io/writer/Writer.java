package org.uranus.io.writer;

import java.io.IOException;

/**
 * Generic writer
 * 
 * @author Michael
 *
 */
public interface Writer
{
	
	/**
	 * open a underlay resource to write
	 * 
	 * @return
	 */
	public boolean open() throws IOException;
	
	/**
	 * test is opened
	 * 
	 * @return true if is already opened
	 */
	public boolean isOpen();
	
	/**
	 * write something to underlay resource 
	 * 
	 * @param content
	 */
	public void write(String content);
	
	/**
	 * close the writer
	 */
	public void close();
}
