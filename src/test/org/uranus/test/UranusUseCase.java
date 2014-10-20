package org.uranus.test;

import java.io.IOException;
import java.util.UnknownFormatConversionException;

import org.uranus.util.buffer.RingSlideWindowBuffer;
import org.uranus.util.buffer.SlideWindowBuffer;
import org.uranus.util.config.ConfigureLoader;
import org.uranus.util.config.ConfigureOptional;
import org.uranus.util.config.KVConfigureLoader;

public class UranusUseCase
{
	
	public static void main(String[] args) {
		//testSlideWindowBuffer();
		testConfigureLoader();
		
		System.out.println("[ DONE ]");
	}
	
	private static void testConfigureLoader() {
		ConfigureLoader<ServerConf> conf = new KVConfigureLoader<ServerConf>(ConfigureOptional.DISCARD);
		try {
			System.out.println(conf.parse(ServerConf.class, "aaa=111111\nnum=1\nname=aaasadfa\nisgood=true"));
		} catch (IOException e) {
			System.out.println(e);
		} catch (UnknownFormatConversionException e) {
			// TODO Auto-generated catch block
			System.out.println(e);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			System.out.println(e);
		}
	}
	
	private static void testSlideWindowBuffer() {
		SlideWindowBuffer buffer = new RingSlideWindowBuffer();
		for (int i = 0; i < 1000000; i++)
			buffer.append(String.valueOf(i));
		System.out.println("asdf");
	}
}


