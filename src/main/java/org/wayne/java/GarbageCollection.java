package org.wayne.java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReentrantLock;

import org.wayne.misc.Utils;

/**
 * This is not a GC algorithm. GarbageCollection just instances
 * n number of threads, and continuously produces new objects
 * for GC to collect, forever.  Use JVM tuning to see performance.
 * 
 * Use a thread pool and for dispatching.
 *
 */
public class GarbageCollection {
    Utils utils = new Utils();
    int numThreads;
    int sleepInterval;
    int sleepPeriod;
    int jobGenerationInterval;
    ExecutorService executorService;
    GCCallback callback;
    LinkedList<Future<Integer>> listFuture = new LinkedList<>();
    ReentrantLock lock = new ReentrantLock();


    public GarbageCollection(
        int numThreads,
        int sleepIntervalMS,
        int sleepPeriodMS,
        int jobGenerationIntervalMS
    ) {
        this.numThreads = numThreads;
        this.sleepInterval = sleepIntervalMS;
        this.sleepPeriod = sleepPeriodMS;
        this.jobGenerationInterval = jobGenerationIntervalMS;
        executorService = Executors.newFixedThreadPool(numThreads);
        callback = new GCCallback();
    }
    
    private void runGCLoop() {
        int numLoops = 10000;
        int numBytesAllocatePerLoop = 10000;
        Runnable runnable = new GCConsumer(callback, listFuture, lock);
        Thread thread = new Thread(runnable);
        thread.start();
        
        for(;;) {
            List<Callable<Integer>> list = new ArrayList<>();
            for(int i = 0; i < 2*numThreads; i++) {
                GCWorkerThread worker = new GCWorkerThread(
                    callback,
                    numLoops, 
                    numBytesAllocatePerLoop, 
                    sleepInterval, 
                    sleepPeriod);
                list.add(worker);
            }
            for(Callable<Integer> callable: list) {
                lock.lock();
                Future<Integer> future = executorService.submit(callable);
                listFuture.add(future);
                lock.unlock();
            }
            if(jobGenerationInterval != 0) {
                try {
                    Thread.sleep(jobGenerationInterval);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    class HeapContainer {
        public final Integer id;
        public final List<Integer> list = new ArrayList<>();
        public final Map<Integer, HeapContainer> map = new HashMap<>();
        final Utils u = new Utils();
        public HeapContainer(Integer id) {
            this.id = id;
        }
        public void setListSize(int numElements) {
            if(list.size() > numElements) {
                return;
            }
            for(int i = 0; i < numElements; i++) {
                Integer v = new Integer(id);
                list.add(v);
            }
        }
    }
    
    private void runHeapLoop() {
        //int availableCPU = Runtime.getRuntime().availableProcessors();
        HashMap<Integer, HeapContainer> mapo = new HashMap<>();
        HashMap<Integer, HeapContainer> mape = new HashMap<>();
        int id = 0;
        int sizeList = 10_000_000;
        int pctRemoveStuff = 70;
        long sleepTime = 100;
        HeapContainer hcHeadE = null;
        HeapContainer hcHeadO = null;
        HeapContainer hcPrevE = null;
        HeapContainer hcPrevO = null;
        for(;;) {
            HeapContainer hc = new HeapContainer(id);
            hc.setListSize(sizeList);
            
            /*
             * add odd Id to odd map, populate hcHeadO, and chain container
             * to previous. 
             * 
             * a 
             * b c d e f
             * c d e f
             * d e f
             * e f
             * f
             */
            if(id % 2 == 0) {
                mape.put(id, hc);
                if(hcHeadE == null) {
                    hcHeadE = hc;
                }
                if(hcPrevE != null) {
                    hcPrevE.map.put(id, hc);
                }
                hcPrevE = hc;
            } else {
                mapo.put(id, hc);
                if(hcHeadO == null) {
                    hcHeadO = hc;
                }
                if(hcPrevO != null) {
                    hcPrevO.map.put(id, hc);
                }
                hcPrevO = hc;
            }
            
            
            for(int i = 0; i < mape.size(); i++) {
                
            }
            for(int i = 0; i < mapo.size(); i++) {
                
            }
            
            id++;
            long freemem = Runtime.getRuntime().freeMemory();
            long totalmem = Runtime.getRuntime().totalMemory();
            double pct = (double)(totalmem - freemem)/(double)totalmem;
            if(pct >= pctRemoveStuff) {
                Set<Integer> setKeys = mapo.keySet();
                for(Integer key: setKeys) {
                    mapo.remove(key);
                }
            }
            try {
                Thread.sleep(sleepTime);
            } catch(InterruptedException e) {
                
            }
        }
    }

    /**
     * runHeap periodically creates a tree. All trees with even IDs
     * are stored. When heap gets to designated percentage, then free
     * up all the IDs. 
     */
    public void runHeap() {
        try {
            runHeapLoop();
        } finally {
            executorService.shutdown();
        }
    }
    
    public void runGC() {
        try {
            runGCLoop();
        } finally {
            executorService.shutdown();
        }
    }
}

class GCConsumer implements Runnable {
    LinkedList<Future<Integer>> ll;
    ReentrantLock lock;
    GCCallback callback;
    
    public GCConsumer(
        GCCallback callback,
        LinkedList<Future<Integer>> ll,
        ReentrantLock lock)
    {
        this.callback = callback;
        this.ll = ll;
        this.lock = lock;
    }

    @Override
    public void run() {
        while(true) {
            lock.lock();
            Future<Integer> future = ll.poll();
            lock.unlock();
            if(future != null) {
                try {
                    Integer v = future.get();
                    callback.consumed(v);
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

class GCCallback {
    public void callback(int value) {
        
    }
    public void consumed(int value) {
        
    }
}

class GCWorkerThread implements Callable<Integer> {
    int numLoops;
    int numBytesAllocatePerLoop;
    int sleepInterval;
    int sleepPeriod;
    GCCallback callback;
    
    public GCWorkerThread(
        GCCallback callback,
        int numLoops,
        int numBytesAllocatePerLoop,
        int sleepInterval,
        int sleepPeriod) 
    {
        this.callback = callback;
        this.numLoops = numLoops;
        this.numBytesAllocatePerLoop = numBytesAllocatePerLoop;
        this.sleepInterval = sleepInterval;
        this.sleepPeriod = sleepPeriod;
    }

    @Override
    public Integer call() {
        return processCommand();
    }
    
    Integer processCommand() {
        for(int i = 0; i < numLoops; i++) { 
            List<Integer> list = new ArrayList<>();
            for(int j = 0; j < numBytesAllocatePerLoop; j += 4) {
                Integer intval = new Integer(j);
                list.add(intval);
            }
            if(sleepInterval != 0 && sleepPeriod != 0) {
                try {
                    Thread.sleep(sleepPeriod);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        callback.callback(numLoops);
        return numLoops;
    }
}

