package org.uranus.net.monitor;

/**
 * Stats Center Connection Handler
 * 
 * @author Michael xixuan.lx
 *
 */
public interface TcpStatusProcessor {

	/**
	 * Call on accept incoming new client socket
	 * 
	 * @param c
	 * @return policy, FALSE will close socket directly
	 */
	public boolean acceptNewClient(TcpStatusConn c);

	/**
	 * Call on incoming packet, write data in ConnClient
	 * 
	 * @param c , current connection context
	 * @param buffer , read buffer
	 */
	public void process(TcpStatusConn c, byte[] buffer);

	/**
	 * Call on close client socket
	 * 
	 */
	public void destoryClient(TcpStatusConn c);

	/**
	 * Process without ayn operation
	 */
	public static TcpStatusProcessor NO_OP = new TcpStatusProcessor() {
		@Override
		public boolean acceptNewClient(TcpStatusConn c) {
			return false;
		}

		@Override
		public void process(TcpStatusConn c, byte[] buffer) {
			c.exit();
		}

		@Override
		public void destoryClient(TcpStatusConn c) {
		}
	};
}
