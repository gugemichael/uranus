package org.uranus.test;

import java.util.concurrent.atomic.AtomicInteger;

import org.uranus.thread.ThreadsGroup;
import org.uranus.util.FlowController;
import org.uranus.util.FlowController.FlowControllerPolicy;
import org.uranus.util.FlowController.FlowControllerType;
import org.uranus.util.FlowControllerFactory;

public class FlowControllerTest {
	
	final AtomicInteger counter = new AtomicInteger(1);

	public static void main(String[] args) {

		final FlowController qpsController = FlowControllerFactory
															.create(FlowControllerType.QPS)
															.setControlPolicy(FlowControllerPolicy.BLOCK)
															.setCheckPoint(new Integer(100000000));
		
		new ThreadsGroup(1) {
			@Override
			protected void run(int id) {
				long sec = System.currentTimeMillis();
				int count = 0;
				while (true) {
//					try {
//						Thread.sleep(50L);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
					if (qpsController.control())
						count++;
					else {
						System.out.println(String.format("thread-%d entry count %d, time %d ms", id, count, System.currentTimeMillis() - sec));
						sec = System.currentTimeMillis();
						count = 0;
					}
				}
			}
		}.start().join();
	}
}
