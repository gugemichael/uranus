package org.uranus.test;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import org.uranus.net.monitor.TcpStatusMonitor;
import org.uranus.net.monitor.TcpStatusReportProcessor;

public class TcpStatusMonitorTest {
	
	public static class Counter {
		public static AtomicInteger c1 = new AtomicInteger();
		public static AtomicInteger c2 = new AtomicInteger();
		public static AtomicInteger c3 = new AtomicInteger();
	}

	public static void main(String[] args) throws IOException {

		TcpStatusMonitor monitor = new TcpStatusMonitor();
		monitor.setProcessor(new TcpStatusReportProcessor(Counter.class));
//		monitor.setProcessor(new TcpStatusProcessor() {
//			@Override
//			public void process(TcpStatusConn c) {
//
//				String command = new String(c.getRecvBuffer().array(), 0, c.getRecvBuffer().limit()).trim();
//				c.write(new StringBuilder("\n").append(command).reverse().toString().getBytes());
//				c.close();
//			}
//
//			@Override
//			public void destoryClient(TcpStatusConn c) {
//				System.out.println("destory " + c);
//			}
//
//			@Override
//			public void acceptNewClient(TcpStatusConn c) {
//				System.out.println("accept " + c);
//			}
//		});

		System.out.println("listen on 6789");

		monitor.listen(6789);

	}

}
