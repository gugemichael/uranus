package org.uranus.processor;


public abstract class Hypervisor {
	
	private static final int DEFAULT_IDLE_TIME = 3000;

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

	public boolean isBackground() {
		return background;
	}

	public int getSuspendTime() {
		return suspend;
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
			}, Hypervisor.class.getSimpleName());
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
			} catch (Throwable ex) {
				onException(ex);
			} finally {
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
	protected void prepare() {
		
	}

	/**
	 * Pass exception
	 */
	protected void onException(Throwable ex) {
		
	}

	/**
	 * Actual working here !
	 */
	protected abstract void runningGuard() throws Exception;

}
