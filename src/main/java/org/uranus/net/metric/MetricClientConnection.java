package org.uranus.net.metric;

import java.nio.ByteBuffer;

/**
 * Socket connection client
 *
 * @author Michael xixuan.lx
 */
public class MetricClientConnection {

    // max buffer receive from command
    static final int BUFFER_SIZE = 128 * 1024;

    // remote address
    private String ip, port;
    // exit status
    private boolean close = false;
    // socket read write buffer
    private ByteBuffer recvBuffer = ByteBuffer.allocate(BUFFER_SIZE);
    private ByteBuffer sendBuffer = ByteBuffer.allocate(BUFFER_SIZE);

    public MetricClientConnection(String ip, String port) {
        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public boolean isClosed() {
        return close;
    }

    public void close() {
        this.close = true;
    }

    public ByteBuffer getSendBuffer() {
        return sendBuffer;
    }

    public ByteBuffer getRecvBuffer() {
        return recvBuffer;
    }

    public void write(byte[] data) {
        this.sendBuffer.put(data);
    }

    @Override
    public String toString() {
        return String.format("client %s:%s", ip, port);
    }
}
