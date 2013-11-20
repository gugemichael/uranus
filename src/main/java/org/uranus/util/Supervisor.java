package org.uranus.util;

import org.apache.log4j.Logger;

public abstract class Supervisor
{
	private static Logger logger = Logger.getLogger(Supervisor.class);

	private String threadName;
	private int idle;

	public abstract void runningGuard() throws Exception;

	public Supervisor(String name, int sleep) {
		this.threadName = name;
		this.idle = sleep;
	}
	
	private void loop() {
		while (true) {
			try {
				while (true) {
					logger.info("[Supervisor] Startup with name [" + threadName + "]");
					this.runningGuard();
					Thread.sleep(idle);
				}
			} catch (Exception ex) {
				logger.error("[Supervisor] Procedure Crash , name[" + threadName + "] reason : " + ex.getMessage() + ", Respawn !");
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

	public void startupBackground() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				loop();
			}
		}).start();
	}
}
