package org.uranus.test;

import org.uranus.util.buffer.LinkQueueSlideWindowBuffer;
import org.uranus.util.buffer.RingSlideWindowBuffer;
import org.uranus.util.buffer.SlideWindowBuffer;

public class UranusUseCase
{
	private static SlideWindowBuffer buffer = new RingSlideWindowBuffer();

	public static void main(String[] args) {
		for (int i = 0; i < 1000000; i++)
			buffer.append(String.valueOf(i));
		System.out.println("asdf");
	}
	
}
