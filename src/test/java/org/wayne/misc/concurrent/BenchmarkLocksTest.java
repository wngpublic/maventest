package org.wayne.misc.concurrent;

import junit.framework.TestCase;
import org.junit.Test;

public class BenchmarkLocksTest extends TestCase {
    @Test
    public void testAll() {
        test2T1MUnfair();
        test8T1MFair();
        test8T1MUnfair();
        test64T1MFair();
        test64T1MUnfair();
        test256T1MUnfair();
    }
    @Test
    public void test2T1MUnfair() {
        int numThreads = 2;
        int numOps = 1_000_000;
        tReentrantLocks(numThreads, numOps, BenchmarkLocks.REENTRANTLOCKUNFAIR);
        tReentrantLocks(numThreads, numOps, BenchmarkLocks.REENTRANTLOCKUNFAIR);
        tReentrantLocks(numThreads, numOps, BenchmarkLocks.REENTRANTLOCKUNFAIR);
    }
    @Test
    public void test8T1MFair() {
        int numThreads = 8;
        int numOps = 1_000_000;
        tReentrantLocks(numThreads, numOps, BenchmarkLocks.REENTRANTLOCKFAIR);
    }
    @Test
    public void test8T1MUnfair() {
        int numThreads = 8;
        int numOps = 1_000_000;
        tReentrantLocks(numThreads, numOps, BenchmarkLocks.REENTRANTLOCKUNFAIR);
        tReentrantLocks(numThreads, numOps, BenchmarkLocks.REENTRANTLOCKUNFAIR);
        tReentrantLocks(numThreads, numOps, BenchmarkLocks.REENTRANTLOCKUNFAIR);
    }
    @Test
    public void test64T1MFair() {
        int numThreads = 64;
        int numOps = 1_000_000;
        tReentrantLocks(numThreads, numOps, BenchmarkLocks.REENTRANTLOCKFAIR);
    }
    @Test
    public void test64T1MUnfair() {
        int numThreads = 64;
        int numOps = 1_000_000;
        tReentrantLocks(numThreads, numOps, BenchmarkLocks.REENTRANTLOCKUNFAIR);
        tReentrantLocks(numThreads, numOps, BenchmarkLocks.REENTRANTLOCKUNFAIR);
        tReentrantLocks(numThreads, numOps, BenchmarkLocks.REENTRANTLOCKUNFAIR);
    }
    @Test
    public void test256T1MFair() {
        int numThreads = 256;
        int numOps = 1_000_000;
        tReentrantLocks(numThreads, numOps, BenchmarkLocks.REENTRANTLOCKFAIR);
    }
    @Test
    public void test256T1MUnfair() {
        int numThreads = 256;
        int numOps = 1_000_000;
        tReentrantLocks(numThreads, numOps, BenchmarkLocks.REENTRANTLOCKUNFAIR);
        tReentrantLocks(numThreads, numOps, BenchmarkLocks.REENTRANTLOCKUNFAIR);
        tReentrantLocks(numThreads, numOps, BenchmarkLocks.REENTRANTLOCKUNFAIR);
    }
    private void tReentrantLocks(int numThreads, int numOps, int lockType) {
        BenchmarkLocks t = new BenchmarkLocks(numThreads, numOps, lockType);
        t.startRun();
    }
}
