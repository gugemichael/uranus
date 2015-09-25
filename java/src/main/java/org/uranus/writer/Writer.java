package org.uranus.writer;


/**
 * Generic Writer
 * 
 * @author Michael
 *
 */
public interface Writer
{
	/**
	 * write content
	 * 
	 * @param content
	 */
	public void write(byte content[], int offset, int count);
	
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
}
