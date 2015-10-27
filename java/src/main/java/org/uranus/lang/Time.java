package org.uranus.lang;

public class Time {

	/**
	 * return current unix timestamp seconds
	 * 
	 * @return unix timestamp
	 */
	public static long getUnixStamp() {
		return System.currentTimeMillis() / 1000L;
	};
	
}
