package org.uranus.io.writer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicBoolean;

public class DailyRollFileWriter implements Writer {

	private static final int ARENA_SIZE = 8192;

	private final AtomicBoolean open = new AtomicBoolean(false);

	/**
	 * thread cached calender instances
	 */
	private ThreadLocal<StringBuilder> arena = new ThreadLocal<StringBuilder>() {
		@Override
		protected StringBuilder initialValue() {
			return new StringBuilder(ARENA_SIZE);
		}
	};

	/**
	 * file timestamp
	 */
	private Time timestamp;

	private final boolean buffered;

	/**
	 * file handler
	 */
	private final String fileName;
	private FileWriter writer;

	public DailyRollFileWriter(String fileName) {
		this(fileName, false);
	}

	public DailyRollFileWriter(String fileName, boolean buffered) {
		this.fileName = fileName;
		this.buffered = buffered;
	}

	/**
	 * Implemets Writer
	 */
	@Override
	public boolean open() throws IOException {
		return makefile() && open.compareAndSet(false, true);
	}

	@Override
	public boolean isOpen() {
		return open.get();
	}

	private boolean makefile() throws IOException {
		File path = new File(fileName).getParentFile();
		if (!path.exists())
			if (!(path.mkdirs() && path.exists()))
				return false;
		if (!path.canWrite())
			return false;

		// throws IOException if file is bad
		this.writer = new FileWriter(fileName);

		return true;
	}

	@Override
	public void close() {

		if (writer != null) {
			try {
				// check if there has in memory buffer
				if (arena.get().length() != 0)
					writer.write(arena.toString());
				writer.flush();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void write(String content) {

		if (!buffered) {
			doWrite(content);
		} else {
			StringBuilder buffer = arena.get();
			// write directly we have no space to reserve
			if (buffer.length() + content.length() > ARENA_SIZE) {
				doWrite(buffer.toString());
				buffer.setLength(0);
				buffer.append(content);
			} else
				buffer.append(content);
		}

	}

	private void doWrite(String content) {

		// roll file if necessary
		checkRotate();

		try {
			writer.write(content);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * only one thread invoke
	 * 
	 * @return
	 */
	public boolean checkRotate() {

		Time time = Time.getTime();
		if (timestamp != null) {
			if (time.diffDay(timestamp)) {
				close();
				rotate(timestamp);
			} else
				return true;
		}

		try {
			if (makefile()) {
				timestamp = time;
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return false;
	}

	private void rotate(Time now) {

		String newName = String.format("%s.%d-%02d-%02d", fileName, now.getYear(), now.getMonth(), now.getDay());
		File archive = new File(newName);
		File current = new File(fileName);
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
