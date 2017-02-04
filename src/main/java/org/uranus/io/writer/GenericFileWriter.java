package org.uranus.io.writer;

import org.uranus.util.buffer.ByteArrayBuffer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;


public class GenericFileWriter implements FileWriter {

    /**
     * thread cached calender instances
     */
    private ConcurrentMap<Long, ByteArrayBuffer> arena = new ConcurrentHashMap<Long, ByteArrayBuffer>(64);

    private static final String seperator = String.format("%n");

    /**
     * file handler
     */
    private volatile String fileName;
    private volatile WriteMode mode;

    /**
     * file write handler , dont's use ${FileWriter} bufferd , we flush() right
     * now after write()
     */
    volatile FileOutputStream out;

    private final boolean buffered;

    private final AtomicLong refcount = new AtomicLong(1);
    private final AtomicBoolean isOpen = new AtomicBoolean(true);

    public GenericFileWriter(boolean buffered) {
        this.buffered = buffered;
    }

    public boolean open(String fileName) throws IOException {
        return open(fileName, WriteMode.APPEND);
    }

    /**
     * open file with FileMode
     *
     * @param fileName, name of the file full path
     * @param mode,     {@link WriteMode} APPEND or TRUNCATE
     * @return true if open success
     * @throws IOException new {@link FileOutputStream} failed
     */
    @Override
    public synchronized boolean open(String fileName, WriteMode mode) throws IOException {

        if (fileName == null || fileName.isEmpty())
            return false;

        File file = new File(fileName);
        if (!file.exists() && !file.createNewFile())
            return false;

        this.fileName = fileName;
        boolean append = ((this.mode = mode) == WriteMode.APPEND);

        this.out = new FileOutputStream(file, append);

        return true;
    }


    /**
     * write String conent
     */
    @Override
    public void writeLine(String content) {
        String line = (content + seperator);
        write(line.getBytes(), 0, line.length());
    }

    private void ensureArena() {
        if (!arena.containsKey(Thread.currentThread().getId()))
            arena.put(Thread.currentThread().getId(), new ByteArrayBuffer());
    }

    /**
     * write byte[] conent
     */
    @Override
    public void write(byte[] content, int offset, int count) {
        if (buffered) {
            ensureArena();
            ByteArrayBuffer buffer = arena.get(Thread.currentThread().getId());
            // flush directly if we have no space to reserve
            if (!buffer.ensureCapacity(count) || !isOpen.get()) {
                doWrite(buffer.getBuffer(), 0, buffer.getOffset(), false);
                buffer.clear();
            }
            buffer.append(content, offset, count);
        } else {
            doWrite(content, offset, count, false);
        }
    }

    private void doWrite(byte[] content, int offset, int count, boolean force) {
        if (content != null && count > 0 && offset >= 0) {
            try {
                if (out != null && acquireWriteStamp()) {
                    if (isOpen.get())
                        out.write(content, offset, count);
                    releaseStamp();
                } else if (force) {
                    out.write(content, offset, count);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean acquireWriteStamp() {
        return isOpen.get() && refcount.get() != 0 && refcount.incrementAndGet() > 1;
    }

    private void releaseStamp() {
        if (refcount.decrementAndGet() == 0) {
            try {
                out.flush();
                out.close();
                out = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * flush the buffered content if this writer buffered enable
     */
    @Override
    public void flush() {
        try {
            if (buffered) {
                synchronized (this) {
                    //flush all threads buffered content
                    for (ByteArrayBuffer buffer : arena.values()) {
                        if (buffer.getOffset() != 0) {
                            doWrite(buffer.getBuffer(), 0, buffer.getOffset(), true);
                            buffer.clear();
                        }
                    }
                }
            }
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        isOpen.set(false);
        // flush own thread buffer
        ByteArrayBuffer buffer = arena.get(Thread.currentThread().getId());
        if (buffer != null && buffer.getOffset() != 0) {
            doWrite(buffer.getBuffer(), 0, buffer.getOffset(), true);
            buffer.clear();
        }

        // take a look at others buffer. we flush them if only if
        // there has no writer
        if (refcount.get() == 1) {
            flush();
        }
        releaseStamp();
    }

    String getFileName() {
        return fileName;
    }

    WriteMode getFileWriteMode() {
        return mode;
    }
}

