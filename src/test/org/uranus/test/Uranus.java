package org.uranus.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.log4j.Logger;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

class Dev {

	private int i = 0;

	public void foo(int v) {
		i = v;
	}

}

enum AC {
	BASE_ORDER(1000),a(1001);
	int type;
	AC(int i) {
		type = i;
	}
}

class AA {
}

class BB {
	private AA a;
	public void doSomething( AA a ) {
		this.a = a;
	}
}

public class Uranus
{

	public static void main(String[] args) {

//        ThreadPoolExecutor serv =  new ThreadPoolExecutor(4,4,
//                60L, TimeUnit.SECONDS,
//                new SynchronousQueue<Runnable>());
//        serv.setRejectedExecutionHandler(new RejectedExecutionHandler() {
//			@Override
//			public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
//				try {
//					executor.getQueue().put(r);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//		});

		final BlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(1);
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				Runnable r = null;
				while((r = queue.poll()) != null) {
					System.out.println("run ");
					r.run();
				}
			}
		});
		
		for (int i = 0; i < 10; i++) {
			try {
				queue.put(new Runnable() {
					@Override
					public void run() {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						System.out.println("1");
					}
				});
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}
        
		System.out.println("DONE");
        
	}
	
	public static void aMethod() {
		AA aObj = new AA();
		BB bObj = new BB();
		bObj.doSomething(aObj);
	}


//	private static Logger logger = Logger.getLogger(Uranus.class);
	
//	public static void main(String[] args ) throws FileNotFoundException, InterruptedException , IOException, ParseException
//	{
//		
//		while(true) {
//			foo();
//		}
		
//		int x = 1001;
//		int index = (x-A.BASE_ORDER.type);
//		A expect = A.values()[index];
//		
//		System.out.println(A.valueOf("a"));
////		System.out.println(A.valueOf(A.class, ));
//		System.out.println(A.BASE_ORDER.type + A.a.ordinal());
		
//		List<String> a = new ArrayList<String>();
		
//		System.out.println(String.format("%tc",new Date()));
		
////		StreamReaderSelector<Reader> selector = new StreamReaderSelector<Reader>();
////		Reader r = new Reader("C:\\CCleaner\\ccleaner.ini");
////		selector.register(r);
////		selector.select();
//		
////		Runtime.getRuntime().addShutdownHook(new Thread() {
////			@Override
////			public void run() {
////				System.out.println("asdfasdf");
////			}
////		});
////		
////		Thread.sleep(1000);
////		System.out.println("Input");
////		Thread.sleep(1000);
////		int i = 0;
////		throw new FileNotFoundException();
////		i = i / 0;
////		List<String> noend = new ArrayList<String>();
////		while(true)
////			noend.add("123123123123123123123123123123");
//			
////		new Scanner(System.in).next();
////		return;
//		
////		class RescheduleTask {
////			public String conf;
////			public int delay;
////			public RescheduleTask(String conf,int delay) {
////				this.conf = conf;
////				this.delay = delay;
////			}
////		}
////		
////		Queue<RescheduleTask> tasks = new PriorityBlockingQueue<RescheduleTask>(1024, new Comparator<RescheduleTask>() {
////			@Override
////			public int compare(RescheduleTask r1, RescheduleTask r2) {
////				return r1.delay - r2.delay;
////			}
////		});
////		
////		tasks.add(new RescheduleTask("2", 2));
////		tasks.add(new RescheduleTask("1", 1));
////		tasks.add(new RescheduleTask("5", 5));
////		tasks.add(new RescheduleTask("11", 11));
////		
////		System.out.println(tasks.peek().conf);
////		System.out.println(tasks.peek().conf);
////
////
////		final CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault();
////		httpclient.start();
////		try {
////			final HttpGet request = new HttpGet("http://www.apache.org/");
////			final Future<HttpResponse> future = httpclient.execute(request, null);
////			try {
////				HttpResponse response;
////				response = future.get();
////				System.out.println("Response: " + response.getStatusLine());
////				System.out.println("Shutting down");
////			} catch (ExecutionException e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
////			}
////		} finally {
////			httpclient.close();
////		}
////
////		Iterable<String> instances = null;
////		Iterable<String> itr = Splitter.on(',').trimResults().omitEmptyStrings().split("1,2");
////		for (String item : instances) 
////			System.out.println(item);
//
//		
//		short s = 188;
//		s += 1;
//		System.out.println(s);
//
//		ByteBuffer buf = ByteBuffer.allocate(12);
//		buf.putChar('a');
//		buf.putInt(111);
//		buf.putShort((short)1);
//		buf.putInt(222);
//		buf.flip();
//		System.out.println(buf.getChar());
//		System.out.println(buf.getInt());
//		System.out.println(buf.getShort());
//		buf.flip();
//		buf.putInt(333);
//		buf.flip();
//		System.out.println(buf.getInt());
//		
//		System.out.println("------------------------");
//		
////		ByteBuf b = Unpooled.buffer();
////		b.writeInt(11);
////		b.writeLong(11L);
////		b.readBoolean();
////		System.out.println(b.readableBytes());
//		
//		System.out.println("------------------------");
//		
//		byte[] by = "你好".getBytes("GBK");
//		System.out.println(by.length);
//		System.out.println(new String(by,"US-ASCII"));
//		
//		System.out.println("------------------------");
//		
//		String field = "SMALL_INT";
//		System.out.println(field.substring(field.lastIndexOf("_")+1));
//		
//		System.out.println("------------------------");
//		
////		ByteBuf buffer = Unpooled.buffer();
////		buffer.writeInt(1);
////		buffer.writeByte(1);
////		buffer.readShort();
////		System.out.println(buffer.readBytes(99).readableBytes());
////		ByteBuffer bu = ByteBuffer.allocate(4);
////		bu.putInt(buffer.readInt());
////		bu.flip();
////		System.out.println(buffer.readerIndex());
////		System.out.println(buffer.readableBytes());
//		
//		
//		System.out.println("------------------------");
//
//		BitSet bs = new BitSet();
//		bs.set(120);
//		bs.set(124);
//		System.out.println(bs.isEmpty());
//		System.out.println(bs.nextSetBit(0));
//		System.out.println(bs.toString());
//		
//		long t = System.currentTimeMillis();
//		int i = 1000000000;
//		while (i-- != 0) {
//			if (i << 1234567891 != 0)
//				;
//		}
//		
//		System.out.println(" use : " + (System.currentTimeMillis() - t));
//		
//		System.out.println(1<<1234567891);
//		System.out.println(33 >> 33);
//		
//		System.out.println("||||||||||||||||||||||||");
//		System.out.println("d".getBytes("UTF-8").length);
//		
////		int loop = 1000;
////		Dev o = null;
////		while(loop-- != 0) {
////			o = new Dev();
////			o.foo(loop);
////			System.out.println("hashcode : " + o.hashCode());
////		}
//		
////		String line = "ID = 20130101asdfwef";
////		String line = "ID = 20131231170317804g4pf3x3r";
////		
////		if (line.indexOf("ID = ") != -1)
////			System.out.println((line.substring(line.indexOf("ID = ")+5)));
//		
//		class A {
//			int a;
//
//			@Override
//			public boolean equals(Object arg0) {
//				return this.hashCode() == arg0.hashCode();
//			}
//
//			@Override
//			public int hashCode() {
//				return 1;
//			}
//			
//		}
//
//		A a = new A();
//		A b = new A();
//	
//		Map<A, Long> map = new HashMap<A,Long>();
//		map.put(a, 1L);
//		map.put(b, 1L);
//		
//		System.out.println(map.size());
		
//		while(true) {
//			int i = 100000;
//			long t = System.currentTimeMillis();
//			while(i-- != 0)
//				logger.debug("[asdfasdf]asdfasdfasdfasdfasdfasdfasd asdsf asd ");
//			System.out.println("use " + (System.currentTimeMillis() - t));
//		}
//		
//		int n = 10000000;
//		double fpp = 0.000001;
//		BloomFilter<Integer> friends = BloomFilter.create(Funnels.integerFunnel(), n, fpp);
//
//		System.out.println(friends.mightContain(1));
//		long bits = (long) (-n * Math.log(fpp) / (Math.log(2) * Math.log(2)));
//		System.out.println(bits / 8);
//		System.out.println(Math.max(1, (int) Math.round(bits / n * Math.log(2))));
		

//		AtomicReference<Dev> dev = new AtomicReference<Dev>();
//		dev.compareAndSet(null, new Dev());
//		
//		dev.get().foo(1);
//		
//		System.out.println("asdf".replaceAll(".", "*"));
//		
//		Date date=new Date("20140414"); 
//		
//		DateFormat df = new SimpleDateFormat(
//                "yyyy-MM-dd HH:mm:ss");
//		System.out.println(df.format(date));
		
		
//		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>\n");
//		System.out.println("Complete !!!");	
		
//	}

	private static long s = 0L;

	public static long func() {
		return s++;	
	}
	
	public static long testNIO() throws IOException {

		int i = 3000000;
		ByteBuffer buf = ByteBuffer.allocate("1234567890".length() * i *2);
		
		while(i--!=0) {
			buf.put("1234567890".getBytes());
		}

		buf.flip();
		
		long t0 = System.currentTimeMillis();
		FileChannel fc = new FileOutputStream("c:/bigfile-nio").getChannel();
		while (buf.hasRemaining()) {
			fc.write(buf);
		}
		fc.close();
		long t1 = System.currentTimeMillis();
		return t1 - t0;
	}

	public static long testSteam() throws IOException {

		long i = 3000000L;
		String s = "1234567890";
		StringBuilder buffer = new StringBuilder();
		
		while(i--!=0L) {
			buffer.append("1234567890");
		}
		
		s= buffer.toString();
		
		long t0 = System.currentTimeMillis();

		FileWriter fw = new FileWriter("c:/bigfile-stream");
		fw.write(s);
		fw.flush();
		fw.close();
		long t1 = System.currentTimeMillis();
		return t1 - t0;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}
}
