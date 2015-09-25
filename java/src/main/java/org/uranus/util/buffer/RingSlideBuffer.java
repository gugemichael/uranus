package org.uranus.util.buffer;

public class RingSlideBuffer implements SlideBuffer
{
	private static final int MAX = 512 * 1024;
	
	private int size = 0;
	private int cursor = 0;
	private char[] buffer = new char[MAX+1]; 
	
	public RingSlideBuffer() {
		
	}
	
	@Override
	public void append(String buf) {
		if (buf.length() >= MAX)
			buf = new String(buf.substring(0,MAX-1));
		int len = buf.length();
		if (cursor + len <= MAX) {
			System.arraycopy(buf.toCharArray(), 0, buffer, cursor, len);
			cursor += len;
		} else {
			// copy util end of array
			System.arraycopy(buf.toCharArray(), 0, buffer, cursor, MAX - cursor);
			// copy and overwrite head part
			System.arraycopy(buf.toCharArray(), 0, buffer, 0, len - (MAX - cursor));
			cursor = len - (MAX - cursor) ;
		}
		size += len;
	}

	@Override
	public void append(String buf, char segmentation) {
		if (buf.length() >= MAX)
			buf = new String(buf.substring(0,MAX-1));
		int len = buf.length();
		if (cursor + len + 1 <= MAX) {
			System.arraycopy(buf.toCharArray(), 0, buffer, cursor, len);
			buffer[cursor+len+1] = segmentation;
			cursor += len + 1;
		} else {
			// copy util end of array
			System.arraycopy(buf.toCharArray(), 0, buffer, cursor, MAX - cursor);
			// copy and overwrite head part
			System.arraycopy(buf.toCharArray(), 0, buffer, 0, len - (MAX - cursor));
			buffer[len-(MAX - cursor)+1] = segmentation;
			cursor = len - (MAX - cursor) + 1;
		}
		size += len;
	}

	@Override
	public long length() {
		return 0;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public void clear() {
		size = 0 ;
		cursor = 0;
	}

	@Override
	public String getString() {
		if (size <= MAX)
			return new String(buffer,0,size);
		else 
			return new StringBuilder(MAX).append(buffer,cursor,MAX - cursor).append(buffer,0,cursor).toString();
	}

}
