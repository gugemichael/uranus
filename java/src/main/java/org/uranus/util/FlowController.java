package org.uranus.util;

import java.security.AccessControlException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public interface FlowController {

	public enum FlowControllerType {
		QPS, FLOW_FUNNEL;
	}

	public interface FlowControllerPolicy {
		
		public FlowControllerPolicy BLOCK = new FlowControllerPolicy() {
			@Override
			public void action(long spend, long left) {
				try {
					TimeUnit.MILLISECONDS.sleep(left);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}; 
		
		public FlowControllerPolicy EXCEPTION = new FlowControllerPolicy() {
			@Override
			public void action(long spend, long left) {
				throw new AccessControlException("qps flow control max");
			}
		}; 
		
		public void action(long spend, long left);
	}

	/**
	 * entry this flow check point once request
	 * 
	 * @return
	 */
	public boolean control(int number);
	
	/**
	 * entry this flow check point many request
	 * 
	 * @return
	 */
	public boolean control();

	/**
	 * 
	 */
	public FlowController setCheckPoint(Object control) throws IllegalArgumentException;

	/**
	 * 
	 */
	public FlowController setControlPolicy(FlowControllerPolicy policy) throws IllegalArgumentException;
}

class QPSFlowController implements FlowController {

	/**
	 * counter for qps
	 */
	private AtomicInteger counter = new AtomicInteger(0);
	
	private AtomicLong timestamp = new AtomicLong(0L);

	FlowControllerPolicy policy = FlowControllerPolicy.BLOCK;

	private volatile int MAX = 0;
	
	@Override
	public boolean control() {
		return control(1);
	}

	@Override
	public boolean control(int number) {
		
		if (MAX <= 0)
			return true;
		
		long current = System.currentTimeMillis();
		if (current <= (timestamp.get() + 1000L)) {
			if (counter.getAndAdd(number) >= MAX) {
				
				// new timestamp and clear counter
				long moment = timestamp.get(); 
				long spend = current - moment, left = (moment + 1000L) - current;
				
				/**
				 *  last action , may throw exception !
				 */
				policy.action(spend, left);
				
				timestamp.set(System.currentTimeMillis());
				counter.set(0);
				
				return false;
			}
		} else {
			timestamp.set(current);
			counter.set(1);
		}

		return true;
	}

	@Override
	public FlowController setCheckPoint(Object control) throws IllegalArgumentException {
		if (control instanceof Number)
			MAX = ((Number) control).intValue();
		else
			throw new IllegalArgumentException("QPS flow controller control() args should be numbers");
		return this;
	}

	@Override
	public FlowController setControlPolicy(FlowControllerPolicy policy) throws IllegalArgumentException {
		this.policy = policy;
		return this;
	}

}
