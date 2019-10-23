package org.wayne.distributed;

/**
 * This is sort of like utils, but it uses a thread pool to generate data in parallel.
 * Also add some metrics to see the latencies and stats.
 * For this version, just use off the shelf components, 
 * such as ConcurrentHashMap, ConcurrentQueue, instead of reinventing.
 */
public class DataGenerator {
    int numThreads = 1;
    public DataGenerator() {
        this.numThreads = Runtime.getRuntime().availableProcessors();
        init();
    }
    public DataGenerator(int numThreads) {
        this.numThreads = numThreads;
        init();
    }
    void init() {
        
    }
}
