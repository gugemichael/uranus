package org.uranus.test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.apache.log4j.Logger;

public class Uranus
{
	private static Logger logger = Logger.getLogger(Uranus.class);

	public static void main(String[] args ) throws FileNotFoundException, InterruptedException , IOException
	{
//		StreamReaderSelector<Reader> selector = new StreamReaderSelector<Reader>();
//		Reader r = new Reader("C:\\CCleaner\\ccleaner.ini");
//		selector.register(r);
//		selector.select();
		
//		Runtime.getRuntime().addShutdownHook(new Thread() {
//			@Override
//			public void run() {
//				System.out.println("asdfasdf");
//			}
//		});
//		
//		Thread.sleep(1000);
//		System.out.println("Input");
//		Thread.sleep(1000);
//		int i = 0;
//		throw new FileNotFoundException();
//		i = i / 0;
//		List<String> noend = new ArrayList<String>();
//		while(true)
//			noend.add("123123123123123123123123123123");
			
//		new Scanner(System.in).next();
//		return;
		
//		class RescheduleTask {
//			public String conf;
//			public int delay;
//			public RescheduleTask(String conf,int delay) {
//				this.conf = conf;
//				this.delay = delay;
//			}
//		}
//		
//		Queue<RescheduleTask> tasks = new PriorityBlockingQueue<RescheduleTask>(1024, new Comparator<RescheduleTask>() {
//			@Override
//			public int compare(RescheduleTask r1, RescheduleTask r2) {
//				return r1.delay - r2.delay;
//			}
//		});
//		
//		tasks.add(new RescheduleTask("2", 2));
//		tasks.add(new RescheduleTask("1", 1));
//		tasks.add(new RescheduleTask("5", 5));
//		tasks.add(new RescheduleTask("11", 11));
//		
//		System.out.println(tasks.peek().conf);
//		System.out.println(tasks.peek().conf);
//
//
//		final CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault();
//		httpclient.start();
//		try {
//			final HttpGet request = new HttpGet("http://www.apache.org/");
//			final Future<HttpResponse> future = httpclient.execute(request, null);
//			try {
//				HttpResponse response;
//				response = future.get();
//				System.out.println("Response: " + response.getStatusLine());
//				System.out.println("Shutting down");
//			} catch (ExecutionException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		} finally {
//			httpclient.close();
//		}


		System.out.println("Done");
		
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
}
