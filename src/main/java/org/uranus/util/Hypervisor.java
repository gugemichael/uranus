package org.uranus.util;

public abstract class Hypervisor
{

	/**
	 * Time slice waiting on sub-running crash or stop
	 */
	private int idle;

	public Hypervisor(int idle) {
		this.idle = idle;
	}

	/**
	 * Do initial job before running 
	 */
	protected void prepare() {	
	}
	
	/**
	 * Pass exception  
	 */
	protected void onException(Exception ex) {
	}
	
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

	public void startupWithBlocked() {
		loop();
	}

	public void startup(String threadName) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				loop();
			}
		}, threadName).start();
	}
}
