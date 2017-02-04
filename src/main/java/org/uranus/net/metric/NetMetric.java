package org.uranus.net.metric;

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
import java.util.concurrent.TimeUnit;

/**
 * Tcp network reportor , fetch summary internal system info from tcp conn
 *
 * @author Michael xixuan.lx
 */
public class NetMetric {

    /**
     * clients map
     */
    private static ConcurrentMap<SocketChannel, MetricClientConnection> clients = new ConcurrentHashMap<SocketChannel, MetricClientConnection>();

    /**
     * socket handler
     */
    private int port;

    private ServerSocket serverSocket;
    private ServerSocketChannel serverChannel;
    private Selector selector;

    private MetricProcessor processor = MetricProcessor.NO_OP;

    /**
     * runnable flag
     */
    private volatile boolean run = true;

    public NetMetric setProcessor(MetricProcessor processor) {
        if (processor != null)
            this.processor = processor;
        return this;
    }

    public void listen(int port) throws IOException {
        this.port = port;

        // TRY to bind port ! and check if this port
        // is available, throw IOException if already bind
        new ServerSocket(port).close();

        new Thread(new Runnable() {
            @Override
            public void run() {
                NetMetric.this.run();
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
        ServerSocketChannel server;
        SocketChannel clientSocket = null;
        int count;
        MetricClientConnection client = null;
        if (selectionKey.isAcceptable()) {
            // new conn
            server = (ServerSocketChannel) selectionKey.channel();
            clientSocket = server.accept();
            clientSocket.configureBlocking(false);
            clientSocket.register(selector, SelectionKey.OP_READ);
            client = new MetricClientConnection(clientSocket.socket().getInetAddress().getHostAddress(), Integer.toString(clientSocket.socket().getPort()));
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
            while (clientSocket.write(client.getSendBuffer()) != 0)
                ;
            client.getSendBuffer().clear();
            clientSocket.register(selector, SelectionKey.OP_READ);
        }

		/*
         * call close() and no more data write to socket client
		 */
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
                                ex.printStackTrace();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                TimeUnit.MILLISECONDS.sleep(3000);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * close client socket
     *
     * @param channel , client socket
     */
    private void shutdownSocket(SocketChannel channel) throws IOException {

        // unregister all event
        if (channel.isRegistered())
            channel.keyFor(selector).cancel();

        channel.close();

        // remove associated Conn client
        MetricClientConnection c = clients.remove(channel);

        processor.destoryClient(c);
    }
}
