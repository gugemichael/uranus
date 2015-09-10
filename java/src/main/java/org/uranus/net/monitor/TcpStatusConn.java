package org.uranus.net.monitor;

/**
 * express a socket client
 * 
 * @author Michael xixuan.lx
 *
 */
public class TcpStatusConn {

	static final int BUFFER_SIZE = 128 * 1024;

	/**
	 * remote address
	 */
	private String ip;
	
	private int port;

	/**
	 * last command
	 */
	private byte[] lastCommand;

	/**
	 * keep long connection
	 */
	private boolean keepalive = false;

	/**
	 * exit status
	 */
	private boolean exit = false;

	/**
	 * socket read write buffer
	 */
	private byte[] data;
	
	

	public TcpStatusConn(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public byte[] getLastCommand() {
		return lastCommand;
	}

	public void setLastCommand(byte[] lastCommand) {
		this.lastCommand = lastCommand;
	}

	public boolean isKeepalive() {
		return keepalive;
	}

	public void keepalive() {
		this.keepalive = true;
	}

	public boolean isExit() {
		return exit;
	}

	public void exit() {
		this.exit = true;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}
}
