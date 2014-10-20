package org.uranus.util.buffer;

public interface SlideWindowBuffer
{
	public int MAX_APPEND = 32 * 1024;
	
	/**
	 * Save string to Buffer , the extra redundant chars (more
	 * than MAX_APPEND) will be discard 
	 * 
	 */
	public void append(String buf);
	
	/**
	 * Save string to Buffer , the additional segmentation append 
	 * every append's calling  and the extra redundant chars (more
	 * than MAX_APPEND) will be discard 
	 */
	public void append(String buf, char segmentation);
	
	/**
	 * True if the buffer has no elements
	 */
	public boolean isEmpty();
	
	/**
	 * Remove all elements
	 */
	public void clear();
	
	/**
	 * String representation of the buffer 
	 */
	public String getString();
	
}
