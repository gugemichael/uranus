package org.uranus.test;

import java.io.IOException;
import java.util.UnknownFormatConversionException;

import org.uranus.thread.ThreadsGroup;
import org.uranus.util.Hypervisor;
import org.uranus.util.buffer.RingSlideWindowBuffer;
import org.uranus.util.buffer.SlideWindowBuffer;
import org.uranus.util.config.ConfigureLoader;
import org.uranus.util.config.ConfigurePolicy;
import org.uranus.util.config.HumanReadableConfigureLoader;
import org.uranus.util.config.KVConfigureLoader;

public class UranusUseCase
{
	
	public static void main(String[] args) {
//		testSlideWindowBuffer();
		testConfigureLoader();
//		testThreadGroup();
//		testHypervisor();
		
		System.out.println("[ DONE ]");
	}
	
	private static void testHypervisor() {
		new Hypervisor(3,false) {
			@Override
			protected void runningGuard() throws Exception {
				int i = 10;
				while(i-- != 0) {
					System.out.println(100/(i+1));
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
					}
				}
			}
			@Override
			protected void prepare() {
				System.out.println("prepare : " + "lalalallala");
			}
			@Override
			protected void onException(Exception ex) {
				System.out.println("error : " + ex);
			}
		}.startup();
	}
	
	private static void testThreadGroup() {
		new ThreadsGroup(10, "MyThread") {
			@Override
			protected void run(int threadID) {
				int i = 10;
				while(i-- != 0) {
					System.out.println(threadID);
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
					}
				}
			}
		}.start().join();
	}
	
	private static void testConfigureLoader() {
//		ConfigureLoader conf = new KVConfigureLoader(ConfigureOptional.DISCARD);
//		ConfigureLoader conf = new HumanReadableConfigureLoader(ConfigureOptional.DISCARD);
//		try {
//			System.out.println(conf.parse(ServerConf.class, "aaa=1m\nnum=1\nname=aaasadfa\nisgood=on"));
//			System.err.println(ServerConf.aaa);
//			System.err.println(ServerConf.isGood);
//		} catch (IOException e) {
//			System.out.println(e);
//		} catch (UnknownFormatConversionException e) {
//			// TODO Auto-generated catch block
//			System.out.println(e);
//		} catch (IllegalAccessException e) {
//			// TODO Auto-generated catch block
//			System.out.println(e);
//		}
	}
	
	private static void testSlideWindowBuffer() {
		SlideWindowBuffer buffer = new RingSlideWindowBuffer();
		for (int i = 0; i < 1000000; i++)
			buffer.append(String.valueOf(i));
		System.out.println("asdf");
	}
}


