package com.zmdx.enjoyshow.utils.threadpool;


import java.util.Queue;
import java.util.concurrent.Executor;

/**
 * A thread pool that automatically adjusts its size depending on specified settings / conditions
 */
public class DynamicThreadPool implements Executor {
    private static int poolCount = 0;
    private int poolID = 0;

    private volatile boolean running = true;

    private static final boolean LOGV = false;
    private static final boolean LOGVV = LOGV && false;

    /**
     * Queued work
     */
    private final Queue<Runnable> queue;

    /**
     * Minimum number of threads
     */
    private final int minThreads;

    /**
     * Maximum number of threads
     */
    private final int maxThreads;

    /**
     * Threshold of queued work to exceed before starting more threads
     */
    private final int threshold;

    private final int priority;

    /**
     * Track threads
     */
    private final Thread[] threads;

    /**
     * Number of threads
     */
    private int threadCount;

    /**
     * Create thread pool
     * 
     * @param q
     */
    public DynamicThreadPool(Queue<Runnable> q) {
        this(q, 0, 2);
    }

    /**
     * Create thread pool with specified min and max threads
     * 
     * @param q
     * @param min
     * @param max
     */
    public DynamicThreadPool(Queue<Runnable> q, int min, int max) {
        this(q, min, max, 50);
    }

    public DynamicThreadPool(Queue<Runnable> q, int min, int max, int threshold) {
        this(q, min, max, threshold, (Thread.NORM_PRIORITY + Thread.MIN_PRIORITY) / 2);
    }

    /**
     * Create thread pool with specified min and max threads
     * 
     * @param q
     * @param min
     * @param max
     * @param threshold - queued work to exceed before starting threads up to max
     */
    public DynamicThreadPool(Queue<Runnable> q, int min, int max, int threshold, int priority) {
        queue = q;
        minThreads = min;
        maxThreads = max;
        this.threshold = threshold;
        this.priority = priority;

        poolID = poolCount;
        poolCount++;

        threads = new Thread[maxThreads];

        // start initial number of threads
        for (int i = 0; i < minThreads; i++) {
            threads[i] = new Worker(poolID, i, this.priority);
            threads[i].start();

        }

        threadCount = minThreads;
    }

    /**
     * Get number of active threads
     * 
     * @return
     */
    public int activeThreads() {
        int active = 0;

        for (int i = 0; i < threads.length; i++) {
            if (threads[i] != null && threads[i].isAlive())
                active++;
        }

        return active;
    }

    /**
     * Shutdown threads and flag pool as not running
     */
    public void shutdown() {

        running = false;

        // empty queue and notify all waiting threads
        synchronized (queue) {
            queue.clear();
            queue.notifyAll();
        }
    }

    /**
     * Execute an asynchronous task
     * 
     * @param r
     */
    public void execute(Runnable r) {
        if (r == null) {
            return;
        }

        synchronized (queue) {
            if (threadCount < maxThreads) {
                // start a thread if none running or threshold exceeded
                if (threadCount == 0 || queue.size() > threshold) {
                    threads[threadCount] = new Worker(poolID, threadCount, priority);
                    threads[threadCount].start();

                    threadCount++;
                }
            }

            queue.add(r);
            queue.notify();
        }
    }

    /**
     * Inner class as worker thread to run queued tasks
     */
    private class Worker extends Thread {
        /*
         * Track position of thread in array
         */
        private final int index;
        private final int tag;
        private final int priority;

        public Worker(int tag, int index, int priority) {
            super("worker-" + tag + "-" + priority + "-" + index);
            this.index = index;
            this.tag = tag;
            this.priority = priority;
        }

        public void run() {
            try {
                Thread.currentThread().setPriority(priority);
            } catch (Throwable e) {
            }

            while (running) {
                Runnable r = null;

                synchronized (queue) {
                    if (!queue.isEmpty()) {
                        r = (Runnable) queue.poll();
                    }
                    // queue is empty, so kill active threads over minimum
                    else if (threadCount > minThreads) {
                        // eliminate reference to dying thread so it can be GC'd
                        threads[index] = threads[threadCount - 1];
                        threads[threadCount - 1] = null;
                        threadCount--;

                        // notify other threads waiting so they can terminate / process work
                        queue.notify();

                        break;
                    }
                    // minimum threads active, so just wait for work
                    else {
                        try {
                            // if (LOGV) LOG.logI("Thread number " + index + " waiting for work");

                            queue.wait();

                            // if (LOGV) LOG.logI("Thread number " + index + " awakened");
                        } catch (InterruptedException e) {
                        }
                    }
                }

                if (r != null) {

                    try {
                        // run schedule work
                        r.run();
                    } catch (Throwable t) {
                        // prevent thread leakage
                    }
                }
            }

        }
    }
}
