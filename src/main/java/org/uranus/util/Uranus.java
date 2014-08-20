package org.uranus.util;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.log4j.Logger;

public class Uranus {
	
	private static final Logger logger = Logger.getLogger(Uranus.class);
	
	static long l = 0;

		static AA TRUE = new AA(true);
		static AA FALSE = new AA(false);
		
		static AA done = FALSE;
		
//		static Statistics stat = new Statistics();
		
	public static void main(String[] args) throws FileNotFoundException,
	InterruptedException, IOException {
		
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run()  {
				while (!done.is()) {
//				while (stat.getSum() == 0) {
					;
				}
			}
		});

		Thread t2 = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(2000L);
//					for (int i = 0; i != 100; i++) {
//						stat.incrSum();
//						System.out.println("set sum " + stat.getSum());
//					}
					done = TRUE;
					System.out.println("set b true");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		t1.setDaemon(true);
		t2.setDaemon(true);
		t1.start();
		t2.start();
		
		
		t1.join();
		t2.join();

		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>\n");

		System.out.println("Complete !!!");
	}

}

class AA {

	private boolean b ;
	
	public AA(boolean b) {
		this.b = b;
	}
	
	public boolean is() {return b;}
	
	public String show() { return String.valueOf(b); } 
}

class Statistics {
	private int sum;
	private int total;
	private long diff;
	private String record;
	public void incrSum() {
		this.sum++;
	}
	public int getSum() {
		return sum;
	}
	public void setSum(int sum) {
		this.sum = sum;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public long getDiff() {
		return diff;
	}
	public void setDiff(long diff) {
		this.diff = diff;
	}
	public String getRecord() {
		return record;
	}
	public void setRecord(String record) {
		this.record = record;
	}
}
