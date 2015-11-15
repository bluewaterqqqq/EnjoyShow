package com.zmdx.enjoyshow.utils.threadpool;

/**
 * Copyright (c) Tapas Mobile.  All Rights Reserved.
 *
 * @author Qiongzhu Wan <qiongzhu@dianxinos.com / qiongzhu@gmail.com>
 * @version 1.0
 */


import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import com.zmdx.enjoyshow.common.ESConfig;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class ThreadPool {
    private static DynamicThreadPool sDynamicPool = null;
    private static DynamicThreadPool sDynamicCriticalPool = null;

    private static ScheduledThreadPoolExecutor scheduledPool = new ScheduledThreadPoolExecutor(1);

    private static Handler sUiHandler = null;
    private static HandlerThread sHandlerThread = null;
    private static Handler sWorkerHandler = null;

    public static int CPU_CORES = 1;
    public static String PROCESS_NAME = "(unknown)";

    public static void runOnPool(Runnable r) {
        if (ESConfig.DEBUG)
            sDynamicPool.execute(new ShowExceptionRunnable(r));
        else
            sDynamicPool.execute(r);
    }

    public static Executor getPoolExecutor() {
        return sDynamicPool;
    }

    public static void runOnUi(Runnable r) {
        if (sUiHandler == null) {
            return;
        }
        sUiHandler.post(r);
    }

    public static void postOnUiDelayed(Runnable r, int delay) {
        if (sUiHandler == null) {
            return;
        }
        if (ESConfig.DEBUG)
            sUiHandler.postDelayed(r, delay);
        else
            sUiHandler.postDelayed(r, delay);
    }

    public static void runCriticalTask(Runnable r) {
        if (ESConfig.DEBUG) {
            // LOG.logD("[CRITICAL TASK SUBMITED: " + r, new Exception());
            sDynamicCriticalPool.execute(new ShowExceptionRunnable(r));
        } else {
            // LOG.logD("[CRITICAL TASK SUBMITED: " + r);
            sDynamicCriticalPool.execute(r);
        }
    }

    public static void runOnWorker(Runnable r) {
        if (ESConfig.DEBUG)
            sWorkerHandler.post(new ShowExceptionRunnable(r));
        else
            sWorkerHandler.post(r);
    }

    public static void postOnWorkerDelayed(Runnable r, int delay) {
        if (ESConfig.DEBUG)
            sWorkerHandler.postDelayed(new ShowExceptionRunnable(r), delay);
        else
            sWorkerHandler.postDelayed(r, delay);
    }

    public static Looper getWorkerLooper() {
        return sHandlerThread.getLooper();
    }

    public static Handler getWorkerHandler() {
        return sWorkerHandler;
    }

    /**
     * Schedule a runnable running at fixed rate
     */
    public static ScheduledFuture<?> schedule(Runnable r, long initialDelay, long period, TimeUnit unit) {
        if (ESConfig.DEBUG)
            return scheduledPool.scheduleAtFixedRate(new ShowExceptionRunnable(r), initialDelay, period, TimeUnit.SECONDS);
        else
            return scheduledPool.scheduleAtFixedRate(r, initialDelay, period, TimeUnit.SECONDS);
    }

    public static void startup() {
        ensureUiThread();

        // standard pool runner
        final int minThreads = Math.max(2, CPU_CORES);
        final int maxThreads = CPU_CORES * 4 + 2;
        sDynamicPool = new DynamicThreadPool(new LinkedBlockingQueue<Runnable>(), minThreads, maxThreads,0,
                Thread.MIN_PRIORITY + 1);

        // critical pool runner
        final int minThreadsForCritical = Math.max(2, CPU_CORES);
        final int maxThreadsForCritical = lowPhysicalMemoryDevices() ? CPU_CORES + 1 : CPU_CORES * 2;
        sDynamicCriticalPool = new DynamicThreadPool(new LinkedBlockingQueue<Runnable>(), minThreadsForCritical,
                maxThreadsForCritical, 0, Thread.NORM_PRIORITY - 1);

        // ui thread runner
        sUiHandler = new Handler();

        // handler based thread runner
        sHandlerThread = new HandlerThread("internal");
        sHandlerThread.setPriority(Thread.NORM_PRIORITY - 1);
        sHandlerThread.start();
        sWorkerHandler = new Handler(sHandlerThread.getLooper());
    }

    public static void shutdown() {
        sHandlerThread.quit();
    }

    public static void ensureUiThread() {
        if (!isUiThread()) {
            throw new IllegalStateException("ensureUiThread: thread check failed");
        }
    }

    public static void ensureNonUiThread() {
        if (isUiThread()) {
            throw new IllegalStateException("ensureNonUiThread: thread check failed");
        }
    }

    public static boolean isUiThread() {
        final Looper myLooper = Looper.myLooper();
        final Looper mainLooper = Looper.getMainLooper(); // never null
    
        return mainLooper.equals(myLooper);
    }

    static int memoryMB = -1;
    public static boolean lowPhysicalMemoryDevices() {
        if (memoryMB == -1) {
            memoryMB = (int) (getPhysicalMemoryKBs() / 1024);
        }
        return (memoryMB < 300);
    }

    static long sPhysicalMemory = 0L;

    public static Long getPhysicalMemoryKBs() {
        // read /proc/meminfo to find MemTotal 'MemTotal: 711480 kB'
        // This operation would complete in fixed time

        if (sPhysicalMemory == 0L) {
            final String PATTERN = "MemTotal:";

            InputStream inStream = null;
            InputStreamReader inReader = null;
            BufferedReader inBuffer = null;

            try {
                inStream = new FileInputStream("/proc/meminfo");
                inReader = new InputStreamReader(inStream);
                inBuffer = new BufferedReader(inReader);

                String s;
                while ((s = inBuffer.readLine()) != null && s.length() > 0) {
                    if (s.startsWith(PATTERN)) {
                        String memKBs = s.substring(PATTERN.length()).trim();
                        memKBs = memKBs.substring(0, memKBs.indexOf(' '));
                        sPhysicalMemory = Long.parseLong(memKBs);
                        break;
                    }
                }
            } catch (Exception e) {
            } finally {
                silentlyClose(inStream);
                silentlyClose(inReader);
                silentlyClose(inBuffer);
            }
        }

        return sPhysicalMemory;
    }

    private static void silentlyClose(Closeable c) {
        if (c != null) {
            try {
                c.close();
            } catch (Throwable e) {
            }
        }
    }
}
