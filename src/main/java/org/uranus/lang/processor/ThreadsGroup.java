package org.uranus.lang.processor;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Group run method startup with Java Thread
 */
public abstract class ThreadsGroup {

    // all threads elements
    private final transient Thread[] threads;

    public ThreadsGroup(int count) {
        this(count, ThreadsGroup.class.getSimpleName());
    }

    public ThreadsGroup(int count, String name) {
        threads = new Thread[count];
        AtomicInteger index = new AtomicInteger(0);
        for (; index.get() < count; index.incrementAndGet()) {
            final int id = index.get();
            threads[index.get()] = new Thread(new Runnable() {
                public void run() {
                    ThreadsGroup.this.run(id);
                }
            }, String.format("%s-%d", name, id));
        }
    }

    /**
     * User defined run
     */
    protected abstract void run(int id);

    public ThreadsGroup setDeamon(boolean deamon) {
        for (Thread t : threads)
            t.setDaemon(deamon);
        return this;
    }

    public ThreadsGroup start() {
        for (Thread t : threads)
            t.start();
        return this;
    }

    public void join() {
        for (Thread t : threads) {
            while (true) {
                try {
                    t.join();
                    break;
                } catch (InterruptedException e) {
                }
            }
        }
    }
}
