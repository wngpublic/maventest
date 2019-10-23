package org.wayne.java;

import org.wayne.java.Objects.*;
import org.wayne.java.Objects;
import org.wayne.main.MyBasic;
import org.wayne.misc.Utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static java.nio.charset.StandardCharsets.UTF_8;

public class MyPerformance implements MyBasic {

    Map<Long, ObjectComplex> mapComplexTTL = new ConcurrentHashMap<>();
    Map<Long, ObjectComplex> mapComplexPerm = new ConcurrentHashMap<>();
    Map<String, ByteBuffer> mapZipped = new ConcurrentHashMap<>();
    Map<String, String> mapStrings1 = new ConcurrentHashMap<>();
    Map<String, String> mapStrings2 = new ConcurrentHashMap<>();
    Map<String, String> mapStrings3 = new ConcurrentHashMap<>();
    AtomicInteger avgTTL1 = new AtomicInteger();
    AtomicInteger avgTTL2 = new AtomicInteger();
    Deque<String> dequeString = new ConcurrentLinkedDeque<>();
    Deque<Integer> dequeInteger = new ConcurrentLinkedDeque<>();
    BlockingQueue<Runnable> queueThreadPool = new LinkedBlockingDeque<>(10);
    static String globalStaticString1 = null;
    String globalString1 = null;
    String globalString2 = null;

    Thread daemon;
    final int poolSize;
    ExecutorService fixedThreadPoolGroup1;
    ExecutorService fixedThreadPoolGroup2;
    int intPoolSelector = 0;
    Map<Integer, ExecutorService> mapExecutor;
    Utils utils = new Utils();

    void p(String f, Object ...o) {
        System.out.printf(f, o);
    }

    @Override
    public void shutdown() {
        mapExecutor.values().stream()
            .forEach(x -> {
                x.shutdown();
                try {
                    int ctr = 0;
                    while(!x.awaitTermination(10, TimeUnit.SECONDS)) {
                        ctr++;
                        if(ctr > 2) {
                            x.shutdownNow();
                            break;
                        }
                    }
                    p("shutdown executor service for %s\n", x.toString());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
    }

    @Override
    public void init() {
        fixedThreadPoolGroup1 = Executors.newFixedThreadPool(poolSize,
            new ThreadFactory() {
                AtomicInteger ctr = new AtomicInteger(0);
                @Override public Thread newThread(Runnable r) {
                    return new Thread(r, String.format("group-1-thread-%02d", ctr.getAndIncrement()));
                }
            }
        );

        fixedThreadPoolGroup2 = Executors.newFixedThreadPool(poolSize,
            new ThreadFactory() {
                AtomicInteger ctr = new AtomicInteger(0);
                @Override public Thread newThread(Runnable r) {
                    return new Thread(r, String.format("group-2-thread-%02d", ctr.getAndIncrement()));
                }
            }
        );

        mapExecutor = new HashMap<>();
        int ctr = 0;
        mapExecutor.put(ctr++, fixedThreadPoolGroup1);
        mapExecutor.put(ctr++, fixedThreadPoolGroup2);

        initDaemonThread();
    }

    public MyPerformance() {
        this(20);
    }
    public MyPerformance(int poolSize) {
        this.poolSize = poolSize;
        init();
    }

    public void testStats() {
        p("mapComplexTTL:  %d objects. Avg ttl:%d\n", mapComplexTTL.size(), avgTTL1.get());
        p("mapComplexPerm: %d objects\n", mapComplexPerm.size());
        p("dequeString:    %d objects\n", dequeString.size());
        p("dequeInteger:   %d objects\n", dequeInteger.size());
    }

    /*
     * This thread just scans through mapComplexTTL and decrements existing items and removes items that are 0.
     */
    void initDaemonThread() {
        daemon = new Thread(() -> {
            while(true) {
                try { Thread.sleep(100); }
                catch (InterruptedException e) { e.printStackTrace(); }
                // not using stream because modifying ctr, sum, which need to be final in streams
                int ctr = 0;
                int sum = 0;
                for(Map.Entry<Long,ObjectComplex> kv: mapComplexTTL.entrySet()) {
                    if(kv.getValue().getTTL().intValue() == 0) {
                        mapComplexTTL.remove(kv.getKey());
                    } else {
                        ctr++;
                        sum += kv.getValue().getTTL().get();
                        kv.getValue().getTTL().decrementAndGet();
                    }
                }
                avgTTL1.set((ctr != 0) ? (sum/ctr) : 0);
            }
        });
        daemon.setDaemon(true);
        daemon.start();
    }

    public void testReset() {
        mapComplexTTL = new ConcurrentHashMap<>();
        mapComplexPerm = new ConcurrentHashMap<>();
        dequeString = new ConcurrentLinkedDeque<>();
        dequeInteger = new ConcurrentLinkedDeque<>();
        mapZipped = new ConcurrentHashMap<>();
        mapStrings1 = new ConcurrentHashMap<>();
        mapStrings2 = new ConcurrentHashMap<>();
        mapStrings3 = new ConcurrentHashMap<>();
    }

    public void testClear() {
        mapComplexTTL.clear();
        mapComplexPerm.clear();
        dequeString.clear();
        dequeInteger.clear();
        mapZipped.clear();
        mapStrings1.clear();
        mapStrings2.clear();
        mapStrings3.clear();
    }

    public int testGetMapTTLSize() {
        p("size MapTTL:%d\n", mapComplexTTL.size());
        return mapComplexTTL.size();
    }
    public int testGetMapPermSize() {
        p("size MapPerm:%d\n", mapComplexPerm.size());
        return mapComplexPerm.size();
    }
    public void testGenerateObjectsTTL1() {
        testGenerateObjectsTTL(200, 500, 3, 3, 500);
    }
    public void testGenerateObjectsTTL2() {
        testGenerateObjectsTTL(300, 500, 3, 3, 600);
    }
    public void testGenerateObjectsTTL3() {
        testGenerateObjectsTTL(400, 500, 3, 3, 700);
    }

    public void testGenerateObjectsTTL(int numObj, int szObj, int numLoops, int numThreads, int ttl) {
        Callable<String> c = () -> {
            IntStream.range(0, numLoops).forEach(i -> {
                try { Thread.sleep(utils.getInt(10,99)); } catch(Exception e) { }
                List<ObjectComplex> l = Objects.createListObjectComplex(numObj, szObj, 10, utils.getInt(ttl/2,ttl));
                l.stream().forEach(x -> mapComplexTTL.put(x.id(), x));
            });
            return null;
        };
        for(int i = 0; i < numThreads; i++) {
            mapExecutor.get(intPoolSelector).submit(c);
            intPoolSelector = (++intPoolSelector) % mapExecutor.size();
        }
    }
    public void testForceGC() {
        System.gc();
    }

    public void testGenerateMapString(int numEntries, int sizePerString) {
        /*
         * modify 3 maps for 3 experiments:
         * - map1 has reference values
         * - map2 has dynamic values
         * - map3 has copy of reference value
         */
        mapStrings1.clear();
        mapStrings2.clear();
        mapStrings3.clear();
        globalString1 = utils.getRandString(sizePerString);
        String keyGeneric = utils.getRandString(32);
        IntStream.range(0, numEntries).forEach(i -> {
            String key = String.format("%s:%010d", keyGeneric, i);
            String val = utils.getRandString(sizePerString);
            mapStrings1.put(key, val);
            mapStrings2.put(key, globalString1);
            mapStrings3.put(key, new String(globalString1));
        });
    }

    public void testCompress1() {
        List<String> list1  = new ArrayList<>();
        List<byte []> list2 = new ArrayList<>();
        List<String> list3  = new ArrayList<>();

        int size = 10;
        int szstr = 5000;
        IntStream.range(0, size).forEach((i) -> list1.add(utils.getRandString(szstr)));
        long tbeg;
        long diff;

        tbeg = System.currentTimeMillis();
        int sizeI = 0;
        int sizeO = 0;
        for(String s: list1) {
            byte [] ba = s.getBytes(UTF_8);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (GZIPOutputStream gzipOut = new GZIPOutputStream(baos)) {
                gzipOut.write(ba);
                byte [] baCompress = baos.toByteArray();
                list2.add(baCompress);
                sizeI += ba.length;
                sizeO += baCompress.length;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
            }
        }
        for(byte [] ba: list2) {
            try (GZIPInputStream gzipIn = new GZIPInputStream(new ByteArrayInputStream(ba))) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte [] baUncompress = new byte[4096];
                int count = 0;
                while((count = gzipIn.read(baUncompress)) != -1) {
                    baos.write(baUncompress, 0, count);
                }
                String s = new String(baos.toByteArray(), UTF_8);
                list3.add(s);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
            }
        }
        diff = System.currentTimeMillis() - tbeg;
        double ratio = (sizeI) / (sizeO * 1.0);
        p("time: %d for %d entries of %d size each. before: %d after:%d ratio:%d\n",
            diff, size, szstr, sizeI, sizeO, ratio);
    }

    public void testMapSizes() {
        class MapSize {
            public void testMapSize(
                int numhosts,
                int numpagesperhost,
                int sizevalue,
                int szminhost, int szmaxhost,
                int szminpage, int szmaxpage
            ) {

                System.gc();
                System.runFinalization();
                long memsz1 = Runtime.getRuntime().totalMemory();
                Map<String, Map<String, String>> mapsizeStringMapString = new HashMap<>();
                for(int i = 0; i < numhosts; i++) {
                    int szhost = utils.getInt(szminhost,szmaxhost);
                    String host = utils.getRandString(szhost);
                    Map<String,String> m = mapsizeStringMapString.get(host);
                    if(m == null) {
                        m = new HashMap<String,String>();
                        mapsizeStringMapString.put(host, m);
                    }
                    for(int j = 0; j < numpagesperhost; j++) {
                        int szpage = utils.getInt(szminpage,szmaxpage);
                        String page = utils.getRandString(szpage);
                        String value = utils.getRandString(sizevalue);
                        m.put(page, value);
                    }
                }
                System.gc();
                System.runFinalization();
                long memsz2 = Runtime.getRuntime().totalMemory();

                System.gc();
                System.runFinalization();
                long memsz3 = Runtime.getRuntime().totalMemory();
                Map<String, String> mapsizeString = new HashMap<>();
                for(Map.Entry<String, Map<String, String>> kv1: mapsizeStringMapString.entrySet()) {
                    String host = new String(kv1.getKey());
                    for(Map.Entry<String, String> kv: kv1.getValue().entrySet()) {
                        String page = new String(kv.getKey());
                        String value = new String(kv.getValue());
                        String hostpage = String.format("%s/%s", host, page);
                        mapsizeString.put(hostpage, value);
                    }
                }
                System.gc();
                System.runFinalization();
                long memsz4 = Runtime.getRuntime().totalMemory();

                // estimate size of map<s,map<s,s>>
                int szref = 8;
                int sizeestimate1 = 0;
                for(Map.Entry<String, Map<String, String>> kv1: mapsizeStringMapString.entrySet()) {
                    int sz = kv1.getKey().length() * 2;
                    sizeestimate1 += sz;
                    for(Map.Entry<String,String> kv: kv1.getValue().entrySet()) {
                        sz = kv.getKey().length() * 2;
                        sizeestimate1 += sz;
                        sz = kv.getValue().length() * 2;
                        sizeestimate1 += sz;
                    }
                }
                // estimate size of map<s,s>
                int sizeestimate2 = 0;
                for(Map.Entry<String,String> kv: mapsizeString.entrySet()) {
                    int sz = kv.getKey().length() * 2;
                    sizeestimate2 += sz;
                    sz = kv.getValue().length() * 2;
                    sizeestimate2 += sz;
                }
                p("size of %d hosts, %d pagesperhost: map<s,map<s,s>> = %d  map<s,s> = %d\n",
                    numhosts, numpagesperhost, sizeestimate1, sizeestimate2);
                p("runtime mem use m<s,m<s,s>>:%d   m<s,s>:%d\n",(memsz2-memsz1), (memsz4-memsz3));
                p("instrumentation mem use: m<s,m<s,s>>:%d   m<s,s>:%d\n", SizeOf.sizeof(mapsizeStringMapString), SizeOf.sizeof(mapsizeString));
                p("sizeof string 10 char string: %d\n", SizeOf.sizeof(utils.getRandString(10)));
            }
        }
        {
            int numhosts = 10000;
            int numpagesperhost = 100;
            int sizevalue = 100;
            int szminhost = 30;
            int szmaxhost = 30;
            int szminpage = 50;
            int szmaxpage = 50;

            MapSize mapsize = new MapSize();

            mapsize.testMapSize(
                numhosts,
                numpagesperhost,
                sizevalue,
                szminhost,
                szmaxhost,
                szminpage,
                szmaxpage);
        }
        {
            int numhosts = 5000;
            int numpagesperhost = 200;
            int sizevalue = 100;
            int szminhost = 30;
            int szmaxhost = 30;
            int szminpage = 50;
            int szmaxpage = 50;

            MapSize mapsize = new MapSize();

            mapsize.testMapSize(
                numhosts,
                numpagesperhost,
                sizevalue,
                szminhost,
                szmaxhost,
                szminpage,
                szmaxpage);
        }
    }

    public void testInstrumentation() {
        String s0 = utils.getRandString(5);
        String s1 = utils.getRandString(10);
        String s2 = utils.getRandString(20);
        String s3 = utils.getRandString(30);
        List<String> list = Arrays.asList(s0,s1,s2,s3);
        p("sizeof string  5 char string: %d\n", SizeOf.sizeof(s0));
        p("sizeof string 10 char string: %d\n", SizeOf.sizeof(s1));
        p("sizeof string 20 char string: %d\n", SizeOf.sizeof(s2));
        p("sizeof string 30 char string: %d\n", SizeOf.sizeof(s3));
        p("sizeof list      char string: %d\n", SizeOf.sizeof(list));
    }

    public void testCPUThreadPerformance() {

    }
}
