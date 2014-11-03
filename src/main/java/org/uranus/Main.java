package org.uranus;

import org.apache.log4j.Logger;
import org.uranus.util.buffer.LinkQueueSlideWindowBuffer;



public class Main
{
	private static Logger LOG = org.apache.log4j.Logger.getLogger(Main.class);

	
	public static void main(String[] args) {
		while(true) {
			new LinkQueueSlideWindowBuffer().hashCode();
		}
	}
}
