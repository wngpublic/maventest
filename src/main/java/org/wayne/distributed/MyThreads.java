package org.wayne.distributed;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.logging.log4j.LogManager;

import org.apache.logging.log4j.Logger;
import org.wayne.main.MyBasic;
import org.wayne.misc.Utils;

import java.lang.management.ManagementFactory;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import com.sun.management.OperatingSystemMXBean;

public class MyThreads implements MyBasic {
    Logger logger = LogManager.getLogger(getClass().getEnclosingClass());
    Utils utils = new Utils();
    ExecutorService executorService;
    static String refstr = "The cat in the hat. The cat in the hat. The cat in the hat";

    @Override
    public void shutdown() {

    }

    @Override
    public void init() {

    }

    public void testScheduledExecutorFixedRate() {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        Runnable r = new Runnable() {
            @Override
            public void run() {
                long tsecc = System.currentTimeMillis()/1000;
                System.out.printf("Hello %d\n", tsecc);
                try {
                    Thread.sleep(3000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        ScheduledFuture<?> scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(r, 1L, 2L, TimeUnit.SECONDS);
    }

    public void testScheduledExecutorFixedDelay() {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        Runnable r = new Runnable() {
            @Override
            public void run() {
                long tsecc = System.currentTimeMillis()/1000;
                System.out.printf("Hello %d\n", tsecc);
                try {
                    Thread.sleep(3000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        ScheduledFuture<?> scheduledFuture = scheduledExecutorService.scheduleWithFixedDelay(r, 1L, 2L, TimeUnit.SECONDS);
    }

    public void setExecutorService(int numThreads) {
        //ThreadFactory threadFactory = Th
        this.executorService = Executors.newFixedThreadPool(numThreads);
    }

    /**
     * run SHA on each thread with curved interval (sine * time). have one daemon thread monitor the CPU.
     */
    public void testThreadsAndCpuMonitoring() throws NoSuchAlgorithmException {
        System.out.printf("starting testThreadsAndCpuMonitoring\n");

        int numThreads = 4;
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("mythread-%d").setDaemon(true).build();
        executorService = Executors.newFixedThreadPool(numThreads, threadFactory);

        AtomicBoolean atomicStop = new AtomicBoolean(false);
        long durationms = 30 * 1000;

        class SHARunner implements Runnable {
            int id = 0;
            public SHARunner(int id) {
                this.id = id;
            }
            @Override
            public void run() {
                System.out.printf("starting thread %d\n", id);
                MessageDigest digest = null;
                try {
                    digest = MessageDigest.getInstance("SHA-256");
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                    return;
                }
                long timebeg = System.currentTimeMillis();
                byte[] encodedhash = digest.digest(refstr.getBytes(StandardCharsets.UTF_8));
                long sleepms = 0;
                while(!atomicStop.get()) {
                    encodedhash = digest.digest(encodedhash);
                    try {
                        if(sleepms > 0) {
                            Thread.sleep(sleepms);
                        }
                        long timeend = System.currentTimeMillis();
                        long timedif = timeend - timebeg;
                        if(timedif > durationms) {
                            //System.out.printf("timediff %d > %d\n", timedif, durationms);
                            atomicStop.set(true);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.printf("ending thread %d\n", id);
            }
        }

        class OSCPURunner implements Runnable {
            @Override
            public void run() {
                OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean( OperatingSystemMXBean.class);
                while(!atomicStop.get()) {
                    try {
                        Thread.sleep(1000);
                        System.out.println("processCpuLoad " + osBean.getProcessCpuLoad() + "; systemCpuLoad " + osBean.getSystemCpuLoad());
                    } catch(InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        List<Future<?>> futures = new ArrayList<>();
        futures.add(executorService.submit(new OSCPURunner()));
        for(int i = 0; i < numThreads; i++) {
            futures.add(executorService.submit(new SHARunner(i)));
        }
        System.out.printf("futures has %d elements\n", futures.size());

        executorService.shutdown();
        while(!executorService.isShutdown()) {
            try {
                executorService.awaitTermination(60, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.printf("done. next\n");
    }

    /*
     * thread pool variations
     *
     * 1. schedule 5 tasks at a time. do not schedule next 5 tasks til this set is done.
     * 2. schedule at most 5 tasks actively into pool, to reduce mem and cpu footprint
     * 3. schedule tasks that can switch when blocked by long wait time.
     * 4. IO bound vs CPU bound pools have different scheduling patterns.
     */
}
