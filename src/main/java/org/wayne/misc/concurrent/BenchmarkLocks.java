package org.wayne.misc.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Benchmark the different lock types against number of threads
 * and number of operations.
 * 
 * The lock types are:
 * synchronized
 * ReentrantLock fair
 * ReentrantLock unfair
 * ReadWriteLock 
 * Semaphore fair
 * Semaphore unfair
 * AtomicInteger
 * 
 * Use a thread pool.
 * 
 */
public class BenchmarkLocks {
    final Integer numThreads;
    final Integer numOps;
    final Integer numBytesPerOp;
    final Integer msSleepInterval;
    final Boolean useFutures;
    final List<Runnable> listRunnable = new ArrayList<>();
    final ReentrantLock reentrantLockFair = new ReentrantLock(true);
    final ReentrantLock reentrantLockUnfair = new ReentrantLock(false);
    final Semaphore semaphoreFair = new Semaphore(1, true);
    final Semaphore semaphoreUnfair = new Semaphore(1, false);
    final ReentrantReadWriteLock reentrantReadWriteLockFair = new ReentrantReadWriteLock(true);
    final ReentrantReadWriteLock reentrantReadWriteLockUnfair = new ReentrantReadWriteLock(false);
    final AtomicInteger atomicInteger = new AtomicInteger(0);
    final ContainerCounter containerCounter;
    /*
     * For ExecutorService threadPool, you can submit a future callable/task, or
     * you can execute a thread. Its the same thing.
     */
    ExecutorService executorService;
    
    static final int BASE = 0;
    
    public static final int REENTRANTLOCKFAIR = 1 + BASE;
    public static final int REENTRANTLOCKUNFAIR = 2 + BASE;
    public static final int SEMAPHOREFAIR = 3 + BASE;
    public static final int SEMAPHOREUNFAIR = 4 + BASE;
    public static final int REENTRANTREADWRITELOCKFAIR = 5 + BASE;
    public static final int REENTRANTREADWRITELOCKUNFAIR = 6 + BASE;
    public static final int ATOMICINTEGER = 7 + BASE;
    public static final int SYNCHRONIZED = 8 + BASE;
    public static final int CONDITION = 9 + BASE;
    
    final int lockType;
    
    public BenchmarkLocks(int numThreads, int numOperations, int lockType) {
        this(numThreads, numOperations, lockType, 100, 0, false);
    }
    
    public BenchmarkLocks(
        int numThreads, 
        int numOperations,
        int lockType,
        int numBytesPerOp,
        int msSleepInterval,
        boolean useFutures) 
    {
        this.numThreads = numThreads;
        this.numOps = numOperations;
        this.lockType = lockType;
        this.numBytesPerOp = numBytesPerOp;
        this.msSleepInterval = msSleepInterval;
        this.useFutures = useFutures;
        this.containerCounter = new ContainerCounter(numOperations, numBytesPerOp);
        executorService = Executors.newFixedThreadPool(numThreads);
    }
    
    void p(String f, Object ...o) {
        System.out.printf(f, o);
    }

    void setupReentrantLockFair() {
        List<Runnable> list = new ArrayList<>();
        for(int i = 0; i < numThreads; i++) {
            ThreadReentrantLock t = 
                new ThreadReentrantLock(i, reentrantLockFair, containerCounter, msSleepInterval);
            list.add(t);
        }
        for(Runnable r: list) {
            executorService.execute(r);
        }
    }
    
    void setupReentrantLockUnfair() {
        List<Runnable> list = new ArrayList<>();
        for(int i = 0; i < numThreads; i++) {
            ThreadReentrantLock t = 
                new ThreadReentrantLock(i, reentrantLockUnfair, containerCounter, msSleepInterval);
            list.add(t);
        }
        for(Runnable r: list) {
            executorService.execute(r);
        }
    }
    
    void setupReentrantReadWriteLockFair() {
        List<Runnable> list = new ArrayList<>();
        for(int i = 0; i < numThreads; i++) {
            ThreadReentrantReadWriteLock t =
                new ThreadReentrantReadWriteLock(i, reentrantReadWriteLockFair, containerCounter, msSleepInterval);
            list.add(t);
        }
        for(Runnable r: list) {
            executorService.execute(r);
        }
    }
    
    void setupReentrantReadWriteLockUnFair() {
        List<Runnable> list = new ArrayList<>();
        for(int i = 0; i < numThreads; i++) {
            ThreadReentrantReadWriteLock t =
                new ThreadReentrantReadWriteLock(i, reentrantReadWriteLockUnfair, containerCounter, msSleepInterval);
            list.add(t);
        }
        for(Runnable r: list) {
            executorService.execute(r);
        }
    }
    
    void setupSemaphoreFair() {
        List<Runnable> list = new ArrayList<>();
        for(int i = 0; i < numThreads; i++) {
            ThreadSemaphore t =
                new ThreadSemaphore(i, semaphoreFair, containerCounter, msSleepInterval);
            list.add(t);
        }
        for(Runnable r: list) {
            executorService.execute(r);
        }
    }
    
    void setupSemaphoreUnfair() {
        List<Runnable> list = new ArrayList<>();
        for(int i = 0; i < numThreads; i++) {
            ThreadSemaphore t =
                new ThreadSemaphore(i, semaphoreUnfair, containerCounter, msSleepInterval);
            list.add(t);
        }
        for(Runnable r: list) {
            executorService.execute(r);
        }
    }

    public void startRun() {
        Long nsBeg = System.nanoTime();
        switch(lockType) {
        case REENTRANTLOCKFAIR: setupReentrantLockFair(); break;
        case REENTRANTLOCKUNFAIR: setupReentrantLockUnfair(); break;
        case REENTRANTREADWRITELOCKFAIR: setupReentrantReadWriteLockFair(); break;
        case REENTRANTREADWRITELOCKUNFAIR: setupReentrantReadWriteLockUnFair(); break;
        case SEMAPHOREFAIR: setupSemaphoreFair(); break;
        case SEMAPHOREUNFAIR: setupSemaphoreUnfair(); break;
        case ATOMICINTEGER:
        case CONDITION:
        default: break;
        }
        executorService.shutdown();
        while(!executorService.isTerminated()) {
        }
        Long nsEnd = System.nanoTime();
        
        Long nsDiff = nsEnd - nsBeg;
        Long msDiff = nsDiff/1_000_000L;
        p("DONE: threads:%-3d ops:%-9d type:%d ms:%-6d\n", 
            numThreads, numOps, lockType, msDiff);
        containerCounter.validate();
    }
    
    class AtomicCounter {
        final int numOpsMax;
        final int numBytesPerOp;
        public AtomicInteger ctr = new AtomicInteger(0);
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        private int externalCountTotal = 0;
        public AtomicCounter(int numOps, int numBytesPerOp) {
            this.numOpsMax = numOps;
            this.numBytesPerOp = numBytesPerOp;
        }
        public boolean doOp() {
            if(ctr.get() >= numOpsMax) {
                return false;
            }
            ctr.incrementAndGet();
            return true;
        }
        public boolean doOpCAS() {
            int v = ctr.get();
            if(v >= numOpsMax) {
                return false;
            }
            while(!ctr.compareAndSet(v, v+1)) {
                v = ctr.get();
            }
            return true;
        }
        public boolean doOpDoubleCAS() {
            while(!atomicBoolean.compareAndSet(false, true)) {
            }
            int v = ctr.get();
            if(v >= numOpsMax) {
                return false;
            }
            while(!ctr.compareAndSet(v, v+1)) {
                v = ctr.get();
            }
            while(!atomicBoolean.compareAndSet(true, false)) {
            }
            return true;
        }
        public int incAndGet() {
            while(true) {
                int v = ctr.get();
                int next = v+1;
                if(ctr.compareAndSet(v, next)) {
                    return next; 
                }
            }
        }
        synchronized void synchronizedAddExternalCount(int externalCount) {
            externalCountTotal += externalCount;
        }
        void p(String f, Object ...o) {
            System.out.printf(f, o);
        }
        synchronized boolean validate() {
            if(ctr.get() == numOpsMax) {
                if(ctr.get() != externalCountTotal) {
                    p("final ctr %d != %d external count\n",
                        ctr.get(), externalCountTotal);
                    return false;
                }
            }
            //p("validate OK\n");
            return true;
        }
    }
    
    class ContainerCounter {
        final int numOpsMax;
        final int numBytesPerOp;
        int ctr = 0;
        List<Integer> list = new ArrayList<>();
        ReentrantLock lock = new ReentrantLock(false);
        int externalCountTotal = 0;
        boolean doAlloc = true;

        public ContainerCounter(int numOps, int numBytesPerOp) {
            this.numOpsMax = numOps;
            this.numBytesPerOp = numBytesPerOp;
        }
        synchronized boolean synchronizedDoOp() {
            return doOp();
        }
        boolean doOp() {
            if(ctr >= numOpsMax) {
                return false;
            }
            if(doAlloc){
                boolean [] a = new boolean[numBytesPerOp];
                if(a.length == numBytesPerOp) {
                    for(int i = 0; i < numBytesPerOp; i++) {
                        a[i] = true;
                    }
                }
            }
            list.add(ctr);
            ctr++;
            return true;
        }
        boolean doLockOp() {
            try {
                lock.lock();
                return doOp();
            }
            finally {
                lock.unlock();
            }
        }
        synchronized void synchronizedAddExternalCount(int externalCount) {
            externalCountTotal += externalCount;
        }
        void addExternalCount(int externalCount) {
            try {
                lock.lock();
                externalCountTotal += externalCount;
            }
            finally {
                lock.unlock();
            }
        }
        void p(String f, Object ...o) {
            System.out.printf(f, o);
        }
        boolean validate() {
            try {
                lock.lock();
                for(int i = 0; i < list.size(); i++) {
                    if(i != list.get(i)) {
                        p("list counter not progressive at i:%d != %d\n", 
                            i, list.get(i));
                        return false;
                    }
                }
                if(ctr == numOpsMax) {
                    if(ctr != externalCountTotal) {
                        p("final ctr %d != %d external count\n",
                            ctr, externalCountTotal);
                        return false;
                    }
                }
                //p("validate OK\n");
                return true;
            }
            finally {
                lock.unlock();
            }
        }
    }
    
    class ThreadReentrantLock implements Runnable {
        final Integer id;
        final ReentrantLock lock;
        final Integer msSleepInterval;
        final ContainerCounter counter;
        int ctr = 0;
        public ThreadReentrantLock(
            Integer id, 
            ReentrantLock lock,
            ContainerCounter containerCounter,
            Integer msSleepInterval) {
            this.id = id;
            this.lock = lock;
            this.counter = containerCounter;
            this.msSleepInterval = msSleepInterval;
        }
        @Override
        public void run() {
            while(true) {
                try {
                    lock.lock();
                    if(!counter.doOp()) {
                        break;
                    }
                    ctr++;
                }
                finally {
                    lock.unlock();
                }
            }
            try {
                lock.lock();
                counter.addExternalCount(ctr);
            }
            finally {
                lock.unlock();
            }
        }
    }
    
    class ThreadReentrantReadWriteLock implements Runnable {
        final Integer id;
        final ReentrantReadWriteLock lock;
        final Integer msSleepInterval;
        final ContainerCounter counter;
        int ctr = 0;
        public ThreadReentrantReadWriteLock(
            Integer id, 
            ReentrantReadWriteLock lock,
            ContainerCounter containerCounter,
            Integer msSleepInterval) {
            this.id = id;
            this.lock = lock;
            this.counter = containerCounter;
            this.msSleepInterval = msSleepInterval;
        }
        @Override
        public void run() {
            while(true) {
                try {
                    lock.writeLock().lock();
                    if(!counter.doOp()) {
                        break;
                    }
                    ctr++;
                }
                finally {
                    lock.writeLock().unlock();
                }
            }
            try {
                lock.writeLock().lock();
                counter.addExternalCount(ctr);
            }
            finally {
                lock.writeLock().unlock();
            }
        }
    }
    
    class ThreadSemaphore implements Runnable {
        final Integer id;
        final Semaphore semaphore;
        final Integer msSleepInterval;
        final ContainerCounter counter;
        int ctr = 0;
        public ThreadSemaphore(
            Integer id, 
            Semaphore semaphore,
            ContainerCounter containerCounter,
            Integer msSleepInterval) {
            this.id = id;
            this.semaphore = semaphore;
            this.counter = containerCounter;
            this.msSleepInterval = msSleepInterval;
        }
        @Override
        public void run() {
            while(true) {
                try {
                    semaphore.acquire(1);
                    if(!counter.doOp()) {
                        break;
                    }
                    ctr++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    semaphore.release();
                }
            }
            try {
                semaphore.acquire(1);
                counter.addExternalCount(ctr);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            finally {
                semaphore.release();
            }
        }
    }
    
    class ThreadSynchronized implements Runnable {
        final Integer id;
        final Integer msSleepInterval;
        final ContainerCounter counter;
        int ctr = 0;
        public ThreadSynchronized(
            Integer id, 
            ContainerCounter containerCounter,
            Integer msSleepInterval) {
            this.id = id;
            this.counter = containerCounter;
            this.msSleepInterval = msSleepInterval;
        }
        @Override
        public void run() {
            while(true) {
                try {
                    if(!counter.synchronizedDoOp()) {
                        break;
                    }
                    ctr++;
                }
                finally {
                }
            }
            counter.synchronizedAddExternalCount(ctr);
        }
    }

    class ThreadAtomic implements Runnable {
        final Integer id;
        final Integer msSleepInterval;
        final AtomicCounter counter;
        int ctr = 0;
        public ThreadAtomic(
            Integer id, 
            AtomicCounter containerCounter,
            Integer msSleepInterval) {
            this.id = id;
            this.counter = containerCounter;
            this.msSleepInterval = msSleepInterval;
        }
        @Override
        public void run() {
            while(true) {
                try {
                    if(!counter.doOp()) {
                        break;
                    }
                    ctr++;
                }
                finally {
                }
            }
            counter.synchronizedAddExternalCount(ctr);
        }
    }
}
