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
	 */
	public void acceptNewClient(TcpStatusConn client);

	/**
	 * Call on incoming packet, write data in ConnClient
	 * 
	 * @param c , current connection context
	 * @param buffer , read buffer
	 */
	public void process(TcpStatusConn client);

	/**
	 * Call on close client socket
	 * 
	 */
	public void destoryClient(TcpStatusConn client);

	/**
	 * Process without any operation
	 */
	public static TcpStatusProcessor NO_OP = new TcpStatusProcessor() {
		
		@Override
		public void acceptNewClient(TcpStatusConn c) {
			c.close();
		}

		@Override
		public void process(TcpStatusConn c) {
			c.close();
		}

		@Override
		public void destoryClient(TcpStatusConn c) {
		}
	};
}
