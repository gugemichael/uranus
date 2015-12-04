package org.uranus.net.reportor;

/**
 * Net reportor Connection Handler
 * 
 * @author Michael xixuan.lx
 *
 */
public interface NetReportorProcessor {

	/**
	 * Call on accept incoming new client socket
	 * 
	 * @param client
	 */
	public void acceptNewClient(NetReportorConn client);

	/**
	 * Call on incoming packet, write data in ConnClient
	 * 
	 * @param c , current connection context
	 * @param buffer , read buffer
	 */
	public void process(NetReportorConn client);

	/**
	 * Call on close client socket
	 * 
	 */
	public void destoryClient(NetReportorConn client);

	
	/**
	 * Process without any operation
	 */
	public static NetReportorProcessor NO_OP = new NetReportorProcessor() {
		
		@Override
		public void acceptNewClient(NetReportorConn c) {
			c.close();
		}

		@Override
		public void process(NetReportorConn c) {
		}

		@Override
		public void destoryClient(NetReportorConn c) {
		}
	};
}
