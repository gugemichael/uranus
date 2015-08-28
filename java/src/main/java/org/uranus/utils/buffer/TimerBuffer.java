package org.uranus.utils.buffer;

import org.uranus.utils.time.TimeSpec;

public abstract class TimerBuffer
{

	/**
	 * Current time in unixtimestamp
	 */
	protected long time = TimeSpec.unixtimeStamp();
	
	/**
	 * Current buffer size
	 */
	protected long size = 0L;
	
	/**
	 * Max live time can hold in memory 
	 */
	protected long maxTime = 0L;
	
	/**
	 * Max buffer size can hold in memory 
	 */
	protected long maxSize = 0L;
	
	public TimerBuffer(long time, long size) {
		this.maxTime = time;
		this.maxSize = size;
	}
	
	public boolean strike() {
		return ( - time > maxTime && size!=0) || size >= maxSize;
	}
	
	abstract protected void updateSize(); 

}
