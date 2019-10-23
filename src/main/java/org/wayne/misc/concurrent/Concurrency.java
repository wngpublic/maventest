package org.wayne.misc.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public class Concurrency {
    ExecutorService executorService;
    Semaphore semaphore3 = new Semaphore(3);
    Semaphore semaphore = new Semaphore(1);
    ReentrantLock reentrantLock = new ReentrantLock(false);
    ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock(false);
    Condition conditionRead = reentrantLock.newCondition();
    Condition conditionWrite = reentrantLock.newCondition();
    int numThreads;
    int msSleep;
    
    
    
}

class SConcurrency<T> {
    ExecutorService executorService = null;
    CallbackClass callbackClass = null;
    Random r = new Random();
    public static void p(String f, Object ...a) {
        System.out.printf(f, a);
    }
    public SConcurrency(int sizeThreadPool) {
        executorService = Executors.newFixedThreadPool(sizeThreadPool);
    }
    static class Multithreading {
        static class ACircularQueue<E> {
            int sz;
            List<E> l;
            ConcurrentHashMap<E, Integer> mapResults;
            Lock lock;
            Condition cnd;
            Condition cndWr;
            Condition cndRd;
            int h = -1;
            int t = 0;
            final TimeUnit tunit = TimeUnit.MILLISECONDS;
            public void print(String format, Object ... args) {
                System.out.printf(format, args);
            }
            public ACircularQueue(int size) {
                sz = size;
                l = new ArrayList<>(sz);
                for(int i = 0; i < sz; i++) {
                    l.add(null);
                }
                mapResults = new ConcurrentHashMap<>();
                lock = new ReentrantLock();
                cnd = lock.newCondition();
                cndWr = lock.newCondition();
                cndRd = lock.newCondition();
            }
            public void put(List<E> list, long expire, String id) {
                long timeRemaining = expire;
                long timePrv, timeCur;
                try {
                    lock.lock();
                    if(h == -1) {
                        h = 0;
                    }
                    for(int i = 0; i < list.size(); i++) {
                        while(((h + 1) % sz) == t) {
                            timePrv = System.currentTimeMillis();
                            cndWr.await(timeRemaining, tunit);
                            timeCur = System.currentTimeMillis();
                            timeRemaining = timeRemaining - (timeCur - timePrv);
                            if(timeRemaining <= 0) {
                                return;
                            }
                        }
                        E e = list.get(i);
                        l.set(h, e);
                        h = (h + 1) % sz;
                        cndRd.signalAll();
                    }
                } catch(Exception e) {
                } finally {
                    lock.unlock();
                }
            }
            public List<E> get(int size, long expire, String id) {
                List<E> list = new ArrayList<>();
                long timeRemaining = expire;
                long timePrv, timeCur;
                try {
                    lock.lock();
                    for(int i = 0; i < size; i++) {
                        while(h == -1 || t == h) {
                            timePrv = System.currentTimeMillis();
                            cndRd.await(timeRemaining, tunit);
                            timeCur = System.currentTimeMillis();
                            timeRemaining = timeRemaining - (timeCur - timePrv);
                            if(timeRemaining <= 0) {
                                return list;
                            }
                        }
                        E e = l.get(t);
                        t = (t + 1) % sz;
                        list.add(e);
                        cndWr.signalAll();
                    }
                } catch(Exception e) {
                } finally {
                    lock.unlock();
                }
                return list;
            }
            public void put(E e, String id) {
                try {
                    lock.lock();
                    if(h == -1) {
                        h = 0;
                    }
                    while(((h + 1) % sz) == t) {
                        cnd.await();
                    }
                    l.set(h, e);
                    h = (h + 1) % sz;
                    cnd.signalAll();
                } catch(InterruptedException ex) {
                    ex.printStackTrace();
                } catch(Exception ex) {
                    ex.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
            public E get(String id) {
                E e = null;
                try {
                    lock.lock();
                    while(h == -1 || t == h) {
                        cnd.await();
                    }
                    l.get(t);
                    t = (t + 1) % sz;
                    cnd.signalAll();
                } catch(InterruptedException ex) {
                    ex.printStackTrace();
                } catch(Exception ex) {
                    ex.printStackTrace();
                } finally {
                    lock.unlock();
                }
                return e;
            }
            public void putResult(E e) {
                mapResults.put(e, 1);
            }
            public ConcurrentHashMap<E, Integer> getResultMap() {
                return mapResults;
            }
        }

        static class ACircularQueueConsumer implements Runnable {
            ACircularQueue<Integer> q;
            String name;
            int szPerConsume;
            public ACircularQueueConsumer(
                    ACircularQueue<Integer> queue, String id, int sizePerConsume) {
                q = queue;
                name = id;
                szPerConsume = sizePerConsume;
            }
            @Override
            public void run() {
                boolean stop = false;
                while(!stop) {
                    List<Integer> l = q.get(szPerConsume, 10000, name);
                    if(l.size() == 0) {
                        stop = true;
                    }
                    for(Integer i: l) {
                        if(i == null) {
                            continue;
                        }
                        q.putResult(i);
                    }
                }
            }
        }

        static class ACircularQueueProducer implements Runnable {
            ACircularQueue<Integer> q;
            String name;
            int idxStart;
            int szProduce;
            public ACircularQueueProducer(
                    ACircularQueue<Integer> queue, 
                    String id, 
                    int startIndex, 
                    int sizeToProduce) {
                this.q = queue;
                name = id;
                idxStart = startIndex;
                szProduce = sizeToProduce;
            }
            @Override
            public void run() {
                int idxEnd = idxStart + szProduce;
                List<Integer> l = new ArrayList<>();
                for(int i = idxStart; i < idxEnd; i++) {
                    l.add(new Integer(i));
                }
                q.put(l, 10000, name);
            }
        }

        static class ASimpleSemaphore {
            boolean acquired = false;
            public ASimpleSemaphore() {
                
            }
            public void acquire() {
                try {
                    synchronized(this) {
                        while(acquired) {
                            wait();
                        }
                        acquired = true;
                        notifyAll();
                    }
                } catch(InterruptedException ex) {
                    ex.printStackTrace();
                } finally {
                    
                }
            }
            public synchronized void release() {
                try {
                    synchronized(this) {
                        while(!acquired) {
                            wait();
                        }
                        acquired = false;
                        notifyAll();
                    }
                } catch(InterruptedException ex) {
                    ex.printStackTrace(); 
                } finally {
                    
                }
            }
        }

        static class ASimpleSemaphoreUser implements Runnable {
            int numLoops;
            ASimpleSemaphore semaphore;
            String id;
            public static void print(String format, Object ... args) {
                System.out.printf(format, args);
            }
            public ASimpleSemaphoreUser(int numLoops, String id, ASimpleSemaphore semaphore) {
                this.numLoops = numLoops;
                this.semaphore = semaphore;
                this.id = id;
            }
            @Override
            public void run() {
                for(int i = 0; i < numLoops; i++) {
                    semaphore.acquire();
                    print("%s acquired %2d\n", id, i);
                    semaphore.release();
                    print("%s released %2d\n", id, i);
                }
            }
        }
    }
    public void createTasks(
            int numTasks, 
            AtomicInteger numFail, 
            AtomicInteger numPass, 
            Object caller) {
        callbackClass = new CallbackClass(numTasks, numFail, numPass, caller, this);
        List<FutureTask<T>> list = new ArrayList<>();
        for(int i = 0; i < numTasks; i++) {
            final int sleepInterval = r.nextInt(5);
            final int randNum = r.nextInt(numTasks);
            TaskCallable callable = 
                    new TaskCallable(randNum, sleepInterval, callbackClass);
            FutureTask<T> future = 
                    new FutureTask<>(callable);
            list.add(future);
        }
        for(FutureTask<T> future: list) {
            executorService.submit(future);
        }
    }
    public void callShutdown() {
        p("SHUTDOWN EXECUTORSERVICE\n");
        executorService.shutdown();
    }
    class TaskCallable implements Callable<T> {
        final CallbackClass callback;
        final int counter;
        final int sleepInterval;
        final Random r = new Random();
        @Override 
        public T call() {
            for(int i = 0; i < 100; i++) {
                try {
                    Thread.sleep(sleepInterval);
                } catch(InterruptedException e) {
                }
            }
            for(int i = 0; i < counter; i++) {
                boolean isSleep = r.nextBoolean();
                if(isSleep) {
                }
            }
            final boolean isPass = counter %2 == 0;
            callback.update(isPass);
            return null;
        }
        public TaskCallable(int num, int sleepInterval, CallbackClass callback) {
            this.callback = callback;
            this.counter = num;
            this.sleepInterval = sleepInterval;
        }
    }
    static class TestConcurrency {
        public void t00() {
            int sizeThreadPool = 100;
            int sizeNumJobs = 10000;
            final AtomicInteger numFail = new AtomicInteger(0);
            final AtomicInteger numPass = new AtomicInteger(0);
            final SConcurrency<Integer> concurrent = new SConcurrency<>(sizeThreadPool);
            concurrent.createTasks(sizeNumJobs, numFail, numPass, this);
        }
        public void printPassFail(AtomicInteger numPass, AtomicInteger numFail) {
            p("TestConcurrency NUMPASS:%d NUMFAIL:%d\n", numPass.get(), numFail.get());
        }
        public void test() {
            t00();
        }
    }
    class CallbackClass {
        final AtomicInteger numFail;
        final AtomicInteger numPass;
        final AtomicInteger numTotal;
        final int sizeStop;
        final Object caller;
        final Object callerShutdown;
        public CallbackClass(
                int sizeStop, 
                AtomicInteger numFail, 
                AtomicInteger numPass,
                Object caller,
                Object callerShutdown) {
            this.sizeStop = sizeStop;
            this.numFail = numFail;
            this.numPass = numPass;
            this.caller = caller;
            this.callerShutdown = callerShutdown;
            numTotal = new AtomicInteger(0);
        }
        public void update(boolean isPass) {
            if(isPass) {
                numPass.incrementAndGet();
            } else {
                numFail.incrementAndGet();
            }
            int total = numTotal.incrementAndGet();
            if(total >= sizeStop) {
                if(caller instanceof TestConcurrency) {
                    TestConcurrency t = (TestConcurrency)caller;
                    t.printPassFail(numPass, numFail);
                }
                if(callerShutdown instanceof SConcurrency) {
                    ((SConcurrency<?>)callerShutdown).callShutdown();
                }
            }
        }
    }
}
