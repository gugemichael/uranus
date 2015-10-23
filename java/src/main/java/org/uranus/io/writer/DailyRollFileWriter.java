package org.uranus.io.writer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.concurrent.locks.ReentrantLock;

public class DailyRollFileWriter extends GenericFileWriter {
	
	// rotate file name carry with daytime
	private static final String ROTATE_FILE_PATTERN = "%s.%d-%02d-%02d";
	// rotate file name if conflict with default daytime rotate file, carry with autonumber
	private static final String ROTATE_FILE_CONFLICT_PATTERN = "%s.%d";
	// rotate locking
	@SuppressWarnings("unused")
	private ReentrantLock lock = new ReentrantLock();
	// file timestamp
	private volatile Time lastest;
	
	public DailyRollFileWriter(boolean buffered) {
		super(buffered);
	}

	@Override
	public void write(byte[] content, int offset, int count) {
		
		Time current = Time.getTime();
		
		super.write(content, offset, count);
		
		if (current.diffSecond(lastest)) {
			// roll file if necessary
			checkRotate(current);
		}
	}

	/**
	 * rotate and split in synchronize
	 * 
	 * @return
	 */
	protected synchronized void checkRotate(Time current) {
		
		try {
			if (lastest != null) {
				
				// check if daytime changed
				if (current.diffMiniute(lastest)) {
					
					// start rotate file
					
					/**
					 * 1. first of all, rename the file to "file.yyyyy-mm-dd"
					 *     parallel write will be lead to this file, so the previos 
					 *     file pointer is still valid 
					 */
					archive(lastest);
					
					/**
					 * 2. snapshot the current file output stream, we close it
					 *     after we open new output stream
					 * 
					 */
					FileOutputStream close = super.out;
					
					/**
					 * 3. open new stream ! all operations will effect the new
					 *     output stream, and we can close the previos *snapshot*
					 *     safe
					 */
					open(getFileName(), getFileWriteMode());
					
					/**
					 * 4. safe close
					 */
					close.flush();
					close.close();
				} 
			}
			
			lastest = current;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void archive(Time time) {
		String newName = String.format(ROTATE_FILE_PATTERN, getFileName() , time.getYear(), time.getMonth(), time.getDay());
		File archive = new File(newName);
		File current = new File(getFileName());
		if (archive.exists()) {
			int sequence = 1, max = Short.MAX_VALUE;
			File newFile;
			while (sequence++ != max) {
				newFile = new File(String.format(ROTATE_FILE_CONFLICT_PATTERN, newName, sequence));
				if (!newFile.exists() && current.renameTo(newFile))
					break;
			}
		} else {
			current.renameTo(archive);
		}

	}

}

/**
 * Time represent
 * 
 * @author Michael
 *
 */
class Time {
	private int year;
	private int month;
	private int day;
	private int hour;
	private int minute;
	private int second;
	private int microSecond;

	private Time() {
	}

	public int getYear() {
		return year;
	}

	public Time setYear(int year) {
		this.year = year;
		return this;
	}

	public int getMonth() {
		return month;
	}

	public Time setMonth(int month) {
		this.month = month;
		return this;
	}

	public int getDay() {
		return day;
	}

	public Time setDay(int day) {
		this.day = day;
		return this;
	}

	public int getHour() {
		return hour;
	}

	public Time setHour(int hour) {
		this.hour = hour;
		return this;
	}

	public int getMinute() {
		return minute;
	}

	public Time setMinute(int minute) {
		this.minute = minute;
		return this;
	}

	public int getSecond() {
		return second;
	}

	public Time setSecond(int second) {
		this.second = second;
		return this;
	}

	public int getMicroSecond() {
		return microSecond;
	}

	public Time setMicroSecond(int microSecond) {
		this.microSecond = microSecond;
		return this;
	}

	public boolean diffDay(Time t) {
		return t == null || t.year != year || t.month != month || t.day != day;
	}
	
	public boolean diffMiniute(Time t) {
		return t == null || t.year != year || t.month != month || t.day != day || t.hour != hour || t.minute != minute;
	}
	
	public boolean diffSecond(Time t) {
		return t == null || t.year != year || t.month != month || t.day != day || t.hour != hour || t.minute != minute || t.second != second ;
	}

	@Override
	public String toString() {
		return String.format("%d-%d-%d %d:%d:%d.%d", year, month, day, hour, minute, second, microSecond);
	}

	public static Time getTime() {
		Calendar calender = Calendar.getInstance();
		return new Time().setYear(calender.get(Calendar.YEAR)).setMonth(calender.get(Calendar.MONTH) + 1)
				.setDay(calender.get(Calendar.DAY_OF_MONTH)).setHour(calender.get(Calendar.HOUR_OF_DAY))
				.setMinute(calender.get(Calendar.MINUTE)).setSecond(calender.get(Calendar.SECOND))
				.setMicroSecond(calender.get(Calendar.MILLISECOND));
	}
}
