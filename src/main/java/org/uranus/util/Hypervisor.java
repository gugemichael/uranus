package org.uranus.util;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class Hypervisor
{

	/**
	 * Thread AutoIncrement ID
	 */
	private static AtomicInteger id = new AtomicInteger();

	/**
	 * Time slice waiting on sub-running crash or stop
	 */
	private int idle;

	/**
	 * daemon thread , block or not
	 */
	private boolean background;

	public Hypervisor() {
		this(3);
	}

	public Hypervisor(int idle) {
		this(idle, false);
	}

	public Hypervisor(int idle, boolean backgroud) {
		this.idle = idle;
		this.background = backgroud;
	}

	/**
	 * Do initial job before running 
	 */
	protected abstract void prepare();

	/**
	 * Pass exception  
	 */
	protected abstract void onException(Exception ex);

	/**
	 * Actual working here !
	 */
	protected abstract void runningGuard() throws Exception;


	private void loop() {

		prepare();

		// keep running 
		while (true) {
			try {
				while (true) {
					runningGuard();
					Thread.sleep(idle);
				}
			} catch (Exception ex) {
				onException(ex);
			} finally {
				try {
					Thread.sleep(idle);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void startup() {
		if (!background) 
			loop();
		else {
			new Thread(new Runnable() {
				@Override
				public void run() {
					loop();
				}
			}, "Hypervisor-Thread-" + id.getAndIncrement()).start();
		}
	}
}
