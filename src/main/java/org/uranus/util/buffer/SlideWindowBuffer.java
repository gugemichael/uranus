package org.uranus.buffer;

public interface SlideWindowBuffer
{
	/**
	 * Save fixed length string buffer , The nosecone will
	 * be erased over the max size 
	 */
	public void append(String buf);
	
	public void append(String buf, char segmentation);
	
	public boolean empty();
	
	public void clear();
	
	public String getString();
	
}
