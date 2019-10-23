package org.wayne.java;

import junit.framework.TestCase;

public class GarbageCollectionTest extends TestCase {
    public void tGarbageCollection() {
        int numThreads = 32;
        int sleepInterval = 0;
        int sleepPeriod = 0;
        int jobGenerationInterval = 10;

        GarbageCollection garbageCollection = new GarbageCollection(
            numThreads,
            sleepInterval,
            sleepPeriod,
            jobGenerationInterval);
        garbageCollection.runGC();
    }

    public void tHeapDump() {
        
    }
    
    public void testGarbageCollection() {
        
    }
}
