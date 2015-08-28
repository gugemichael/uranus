package org.uranus.thread;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class Hypervisor
{
	private static final int DEFAULT_IDLE_TIME = 3000;

	/**
	 * Thread AutoIncrement ID
	 */
	private static AtomicInteger id = new AtomicInteger();

	/**
	 * daemon thread , block or not
	 */
	private boolean background = false;
	
	/**
	 * Time slice waiting on sub-running crash or stop
	 */
	private int suspend = DEFAULT_IDLE_TIME;
	
	/**
	 * represent current thread, if background is true 
	 */
	private Thread current;

	private HypervisorPolicy policy = HypervisorPolicy.RUN_LOOP;
	
	
	public Hypervisor() {
	}

	public Hypervisor backgroud() {
		this.background = true;
		return this;
	}
	
	public Hypervisor suspendTime(int suspend) {
		this.suspend = suspend;
		return this;
	}
	
	public Hypervisor policy(HypervisorPolicy policy) {
		this.policy = policy;
		return this;
	}

	public boolean isBackground() {
		return background;
	}
	
	public int getSuspendTime() {
		return suspend;
	}
	
	public HypervisorPolicy getPolicy() {
		return policy;
	}
	
	
	/**
	 * start running
	 */
	public void startup() {
		if (!background) 
			loop();
		else {
			current = new Thread(new Runnable() {
				@Override
				public void run() {
					loop();
				}
			}, "Hypervisor-Thread-" + id.getAndIncrement());
			current.setDaemon(background ? false : true);
			current.start();
		}
	}
	
	/**
	 * wait until thread has exited
	 * 
	 * @throws InterruptedException
	 */
	public void join() throws InterruptedException {
		if (background && current != null)
			current.join();
	}

	
	
	
	
	private void loop() {

		prepare();

		// keep running 
		while (true) {
			try {
				while (true) {
					runningGuard();
					Thread.sleep(suspend);
				}
			} catch (Exception ex) {
				onException(ex);
			} finally {
				if (policy == HypervisorPolicy.RUN_ONCE)
					break;
				try {
					Thread.sleep(suspend);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Do initial job before running 
	 */
	protected void prepare() { }

	/**
	 * Pass exception  
	 */
	protected void onException(Exception ex) { } 

	/**
	 * Actual working here !
	 */
	protected abstract void runningGuard() throws Exception;


	


}
