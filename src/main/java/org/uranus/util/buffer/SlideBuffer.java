package org.uranus.util.buffer;

public interface SlideBuffer
{
	int MAX_APPEND = 32 * 1024;
	
	/**
	 * Save string to Buffer , the extra redundant chars (more
	 * than MAX_APPEND) will be discard 
	 * 
	 */
    void append(String buf);
	
	/**
	 * Save string to Buffer , the additional segmentation append 
	 * every append's calling  and the extra redundant chars (more
	 * than MAX_APPEND) will be discard 
	 */
    void append(String buf, char segmentation);
	
	/**
	 * True if the buffer has no elements
	 */
    boolean isEmpty();
	
	/**
	 * content length
	 */
    long length();
	
	/**
	 * Remove all elements
	 */
    void clear();
	
	/**
	 * String representation of the buffer 
	 */
    String getString();
	
}
