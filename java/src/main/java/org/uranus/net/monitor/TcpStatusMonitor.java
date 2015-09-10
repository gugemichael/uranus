package org.uranus.net.monitor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Stats Center , fetch summary internal system info with tcp
 * 
 * @author Michael xixuan.lx
 *
 */
public class TcpStatusMonitor implements Runnable {

	/**
	 * clients map
	 * 
	 */
	private static ConcurrentMap<SocketChannel, TcpStatusConn> clients = new ConcurrentHashMap<SocketChannel, TcpStatusConn>();

	/**
	 * socket handler
	 * 
	 */
	private int port;

	private ServerSocket serverSocket;
	private ServerSocketChannel serverChannel;
	private Selector selector;

	private TcpStatusProcessor processor = TcpStatusProcessor.NO_OP;

	/**
	 * runnable flag
	 */
	private volatile boolean run = true;

	public TcpStatusMonitor setProcessor(TcpStatusProcessor processor) {
		if (processor != null)
			this.processor = processor;
		return this;
	}

	public void listen(int port) throws IOException {

		this.port = port;

		// checking this port is available, throws
		// IOException if already bind
		new ServerSocket(port).close();

		new Thread(this).start();
	}

	/**
	 * destory socket server
	 */
	public void shutdown() {
		run = false;
		try {
			if (serverSocket != null)
				serverSocket.close();
			if (serverChannel != null)
				serverChannel.close();
			clients.clear();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void handle(SelectionKey selectionKey) throws IOException {
		ServerSocketChannel server = null;
		SocketChannel clientSocket = null;
		int count = 0;
		if (selectionKey.isAcceptable()) {
			// new conn
			server = (ServerSocketChannel) selectionKey.channel();
			clientSocket = server.accept();
			clientSocket.configureBlocking(false);
			clientSocket.register(selector, SelectionKey.OP_READ);
			TcpStatusConn c = new TcpStatusConn(clientSocket.socket().getInetAddress().getHostAddress(), clientSocket.socket().getPort());
			clients.put(clientSocket, c);
			processor.acceptNewClient(c);
		} else if (selectionKey.isReadable()) {
			// read
			clientSocket = (SocketChannel) selectionKey.channel();
			TcpStatusConn conn = clients.get(clientSocket);
			if (conn != null) {
				ByteBuffer buffer = ByteBuffer.allocate(TcpStatusConn.BUFFER_SIZE);
				while ((count = clientSocket.read(buffer)) > 0)
					;
				if (count >= 0) {
					processor.process(conn, buffer.array());
					clientSocket.register(selector, SelectionKey.OP_WRITE);
				} else
					shutdownSocket(clientSocket);
			}
		} else if (selectionKey.isWritable()) {
			clientSocket = (SocketChannel) selectionKey.channel();
			TcpStatusConn c = clients.get(clientSocket);
			clientSocket.write(ByteBuffer.wrap(c.getData()));
			// for short connection
			if (c.isExit() || !c.isKeepalive())
				shutdownSocket(clientSocket);
			else
				clientSocket.register(selector, SelectionKey.OP_READ);
		}
	}

	@Override
	public void run() {

		try {
			serverChannel = ServerSocketChannel.open();
			// nonblocking
			serverChannel.configureBlocking(false);
			serverSocket = serverChannel.socket();
			serverSocket.setReuseAddress(true);
			serverSocket.bind(new InetSocketAddress("0.0.0.0", port));
			selector = Selector.open();
			serverChannel.register(selector, SelectionKey.OP_ACCEPT);
		} catch (Exception ex) {
			ex.printStackTrace();
			return;
		}

		while (run) {
			try {
				while (run) {
					selector.select();
					Set<SelectionKey> selectionKeys = selector.selectedKeys();
					Iterator<SelectionKey> iterator = selectionKeys.iterator();
					while (iterator.hasNext()) {
						SelectionKey selectionKey = iterator.next();
						iterator.remove();
						try {
							handle(selectionKey);
						} catch (Exception ex) {

						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			yield(3000);
		}
	}

	/**
	 * close client socket
	 * 
	 * @param channel
	 *            , client socket
	 */
	private void shutdownSocket(SocketChannel channel) throws IOException {
		channel.close();
		TcpStatusConn c = clients.remove(channel);
		processor.acceptNewClient(c);
	}

	/**
	 * jvm yield for specified time
	 * 
	 */
	private void yield(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
		}
	}
}
