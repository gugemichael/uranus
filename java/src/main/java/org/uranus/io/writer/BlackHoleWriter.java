package org.uranus.io.writer;

public class BlackHoleWriter implements Writer {
	@Override
	public boolean open() {
		return true;
	}

	@Override
	public boolean isOpen() {
		return false;
	}

	@Override
	public void write(String content) {

	}

	@Override
	public void close() {

	}

}
