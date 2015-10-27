package org.uranus.test;

import org.uranus.lang.processor.ThreadsGroup;
import org.uranus.util.FlowController;
import org.uranus.util.FlowController.FlowControllerPolicy;
import org.uranus.util.FlowController.FlowControllerType;
import org.uranus.util.FlowControllerFactory;

public class FlowControllerTest {

	public static void main(String[] args) {

		final FlowController qpsController = FlowControllerFactory
															.create(FlowControllerType.QPS)
															.setControlPolicy(FlowControllerPolicy.BLOCK)
															.setCheckPoint(new Integer(5));

		new ThreadsGroup(4) {
			@Override
			protected void run(int id) {
				long sec = System.currentTimeMillis();
				int count = 0;
				while (true) {
					try {
						Thread.sleep(50L);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
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
