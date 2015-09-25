package org.uranus.util.time;

public interface TimeSpec {
	
	public TimeSpec UNIX = new TimeSpec() {
		@Override
		public long timestamp() {
			return System.currentTimeMillis() / 1000L;
		}
	};
	
	public long timestamp();
}
