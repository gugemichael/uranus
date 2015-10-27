package org.uranus.net.monitor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
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
public class TcpStatusMonitor {

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

		new Thread(new Runnable() {
			@Override
			public void run() {
				TcpStatusMonitor.this.run();
			}
		}).start();
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
		TcpStatusConn client = null;
		if (selectionKey.isAcceptable()) {
			// new conn
			server = (ServerSocketChannel) selectionKey.channel();
			clientSocket = server.accept();
			clientSocket.configureBlocking(false);
			clientSocket.register(selector, SelectionKey.OP_READ);
			client = new TcpStatusConn(clientSocket.socket().getInetAddress().getHostAddress(), Integer.toString(clientSocket.socket().getPort()));
			clients.put(clientSocket, client);
			processor.acceptNewClient(client);
		} else if (selectionKey.isReadable()) {
			// read
			clientSocket = (SocketChannel) selectionKey.channel();
			client = clients.get(clientSocket);
			if (client != null) {
				client.getRecvBuffer().clear();
				while ((count = clientSocket.read(client.getRecvBuffer())) > 0)
					;
				if (count >= 0) {
				client.getRecvBuffer().flip();
				processor.process(client);
					clientSocket.register(selector, SelectionKey.OP_WRITE);
				} else
					shutdownSocket(clientSocket);
			}
		} else if (selectionKey.isWritable()) {
			// write
			clientSocket = (SocketChannel) selectionKey.channel();
			client = clients.get(clientSocket);
			client.getSendBuffer().flip();
			while(clientSocket.write(client.getSendBuffer()) != 0)
				;
			client.getSendBuffer().clear();
			clientSocket.register(selector, SelectionKey.OP_READ);
		}
		
		if (client.isClosed() && client.getSendBuffer().position() == 0)
			shutdownSocket(clientSocket);
	}

	private void run() {

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
		
		// unregister all event
		if (channel.isRegistered())
			channel.keyFor(selector).cancel();
		
		channel.close();
		
		// remove associated Conn client
		TcpStatusConn c = clients.remove(channel);
		
		processor.destoryClient(c);
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
