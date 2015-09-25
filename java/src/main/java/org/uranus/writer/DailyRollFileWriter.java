package org.uranus.writer;

import java.io.File;
import java.io.IOException;

import java.util.Calendar;

public class DailyRollFileWriter extends GenericFileWriter {

	/**
	 * file timestamp
	 */
	private Time lastest;

	public DailyRollFileWriter(boolean buffered) {
		super(buffered);
	}

	@Override
	public void write(byte[] content, int offset, int count) {
		
		// roll file if necessary
		checkRotate();

		super.write(content, offset, count);
	}

	/**
	 * only one thread invoke
	 * 
	 * @return
	 */
	protected void checkRotate() {

		Time current = Time.getTime();

		try {
			if (lastest == null) {
				// first time to open file
				lastest = current;
			} else {
				// check day passment
				if (current.diffDay(lastest)) {
					close();
					archive(lastest);
					open(getFileName(), getFileWriteMode());
				} else
					lastest = current;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void archive(Time time) {
		String newName = String.format("%s.%d-%02d-%02d", getFileName() , time.getYear(), time.getMonth(), time.getDay());
		File archive = new File(newName);
		File current = new File(getFileName());
		if (archive.exists()) {
			int sequence = 0, max = 256;
			while (sequence++ != max && !current.renameTo(new File(String.format("%s.%d", newName, sequence))))
				;
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
