package org.uranus.net.metric;

/**
 * Net reportor Connection Handler
 * 
 * @author Michael xixuan.lx
 *
 */
public interface MetricProcessor {

	/**
	 * Call on accept incoming new client socket
	 * 
	 * @param client
	 */
    void acceptNewClient(MetricClientConnection client);

	/**
	 * Call on incoming packet, write data in ConnClient
	 * 
	 * @param c , current connection context
	 * @param buffer , read buffer
	 */
    void process(MetricClientConnection client);

	/**
	 * Call on close client socket
	 * 
	 */
    void destoryClient(MetricClientConnection client);

	
	/**
	 * Process without any operation
	 */
    MetricProcessor NO_OP = new MetricProcessor() {
		
		@Override
		public void acceptNewClient(MetricClientConnection c) {
			c.close();
		}

		@Override
		public void process(MetricClientConnection c) {
		}

		@Override
		public void destoryClient(MetricClientConnection c) {
		}
	};
}
