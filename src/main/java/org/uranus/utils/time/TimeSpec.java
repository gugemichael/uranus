package org.uranus.utils.time;

public class TimeSpec
{
	public static long unixtimeStamp() {
		return System.currentTimeMillis() / 1000L;
	}
}
