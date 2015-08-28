package org.uranus.utils.time;

public interface TimeSpec {
	
	public long timestamp();

	public TimeSpec UNIX = new TimeSpec() {

		@Override
		public long timestamp() {
			return System.currentTimeMillis() / 1000L;
		}
	};
}
