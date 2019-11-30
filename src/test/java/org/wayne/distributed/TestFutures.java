package org.wayne.distributed;

import junit.framework.TestCase;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.platform.commons.util.StringUtils;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class TestFutures extends TestCase {
    public static void p(String f, Object ...o) {
        System.out.printf(f, o);
    }

    class OI implements Comparable {
        int i;
        OI(int i) { set(i); }
        int get() { return i; }
        void set(int i) { this.i = i; }
        void inc(int x) { this.i += x; }
        int getInc(int x) { return x + i; }
        OI copy() { return new OI(i); }

        @Override
        public int compareTo(Object o) {
            if(!(o instanceof OI)) return -1;
            OI oi = OI.class.cast(o);
            if(i == oi.get()) return 0;
            if(i < oi.get()) return -1;
            return 1;
        }
    }

    class OIS {
        int i;
        String s;
        OIS(int i, String s) { setI(i); setS(s); }
        void setS(String s) { this.s = s; }
        void setI(int i) { this.i = i; }
        void incI(int x) { this.i += x; }
        int getIncI(int x) { return x + i; }
        OIS copy() { return new OIS(i, s); }
        String getS() { return s; }
        int getI() { return i; }
        void sleepIncI(int x, long sleepms) { sleep(sleepms); incI(x); }
        int sleepGetI(long sleepms) { sleep(sleepms); return i; }
        String sleepGetS(long sleepms) { sleep(sleepms); return s; }
        void sleepSetI(int i, long sleepms) { sleep(sleepms); setI(i); }
        void sleepSetS(String s, long sleepms) { sleep(sleepms); setS(s); }
        void sleep(long sleepms) {
            try {
                Thread.sleep(sleepms);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    CompletableFuture<String> getCFString(OIS ois, int sleepms, ExecutorService executor) {
        return CompletableFuture.supplyAsync(() -> ois.sleepGetS(sleepms), executor);
    }

    @Before
    public void setup() {

    }

    @Test
    public void testStreamList() {
        boolean flag = false;
        int size = 10;
        {
            List<OI> listInt1 = new ArrayList<>();
            for(int i = 0; i < 10; i++) {
                OI oi = new OI(i);
                listInt1.add(oi);
            }
            assert listInt1.stream().count() == 10;
            assert listInt1.stream().count() == 10;
            Comparator<OI> comparator = Comparator.comparing(OI::get);
            OI oi;
            oi = listInt1.stream().min(comparator).get();
            assert oi.get() == 0;
            oi = listInt1.stream().max(comparator).get();
            assert oi.get() == 9;
        }
        {
            Integer [] arrayInt = {1,2,3,4,5,6,7,8,9,10};
            List<Integer> listInt = Arrays.asList(arrayInt);
            Integer res;
            res = listInt.stream().min(Comparator.comparing(Integer::valueOf)).orElse(null);
            assert res == 1;
            res = listInt.stream().max(Comparator.comparing(Integer::valueOf)).orElse(null);
            assert res == 10;
            res = Stream.of(arrayInt).min(Comparator.comparing(Integer::valueOf)).orElse(null);
            assert res == 1;
            res = Stream.of(arrayInt).max(Comparator.comparing(Integer::valueOf)).orElse(null);
            assert res == 10;
            res = listInt.stream().collect(Collectors.summingInt(x -> x));
            assert res == 55;
        }
        {
            Integer [] arrayInt2 = {0,1,2,3,4,5,6,7,8,9};
            List<Integer> listInt2 = Arrays.asList(arrayInt2);
            Stream<Integer> streamInt2 = Stream.of(arrayInt2);

            OI [] arrayOI2 = new OI[size];
            for(int i = 0; i < size; i++) {
                arrayOI2[i] = new OI(i);
            }
            List<OI> listOI2 = Arrays.asList(arrayOI2);
            Stream<OI> streamOI2 = Stream.of(arrayOI2);

            {
                assert listInt2.stream().count() == size;
                assert listInt2.stream().count() == size;
                assert streamOI2.count() == size;
                flag = false;
                try {
                    long count = streamOI2.count();
                    p("%d\n",count);
                } catch(IllegalStateException e) {
                    flag = true;
                }
                assert flag : "cannot operate on stream more than once!";
            }

            // call copy and collect to a new list, and compare with forEach operation
            List<OI> listOICopy = listOI2.stream().map(oi -> oi.copy()).collect(Collectors.toList());
            listOI2.stream().forEach(oi -> oi.inc(100));
            for(int i = 0; i < size; i++) {
                assert (listOICopy.get(i).get() + 100) == (listOI2.get(i).get());
            }

            listOI2 = listOICopy;   // listOI2 now has 0:9
            listOICopy = listOI2.stream().map(oi -> new OI(oi.getInc(200))).collect(Collectors.toList());
            listOICopy.stream().forEach(oi -> {
                assert oi.get() >= 200;
                assert oi.get() < 210;
            });
            listOICopy.stream().forEach(oi -> oi.inc(-200));
            listOICopy.stream().forEach(oi -> { assert oi.get() >= 0 && oi.get() < 10; });

            // filter for only even numbers
            List<OI> listOIres;
            listOIres = listOICopy.stream().filter(oi -> oi.get() % 2 == 0).collect(Collectors.toList());
            assert listOIres.stream().count() == 5;
            listOIres.stream().forEach(oi -> { assert oi.get() % 2 == 0; });

            listOIres = listOICopy.stream()
                .filter(oi -> oi.get() >= 5)
                .filter(oi -> oi.get() % 2 == 0)
                .collect(Collectors.toList());
            assert listOIres.stream().count() == 2;

            listOIres.stream().forEach(oi -> { assert oi.get() % 2 == 0; });    // equivalent
            assert listOIres.stream().allMatch((oi) -> oi.get() % 2 == 0);      // equivalent

            OI res;
            // listOICopy = {0:9} now
            res = listOICopy.stream().filter(oi -> oi.get() == 5).findFirst().orElse(null);
            assert res != null && res.get() == 5;
            res = listOICopy.stream().filter(oi -> oi.get() % 3 == 0 && oi.get() != 0).findFirst().orElse(null);
            assert res != null && res.get() == 3;
            res = listOICopy.stream().filter(oi -> oi.get() % 13 == 0 && oi.get() != 0).findFirst().orElse(null);
            assert res == null;
        }
        {
            List<Integer> list = Arrays.asList(8,4,9,5,3,1,2,7,0,6);
            List<Integer> sorted = list.stream().sorted().map(x -> x).collect(Collectors.toList());
            for(int i = 0; i < list.size(); i++) {
                assert sorted.get(i) == i;
            }
            list = Arrays.asList(2,2,2,2,2,2);
            assert list.stream().allMatch(x -> x == 2);
            list = Arrays.asList(2,2,2,1,2);
            assert !list.stream().allMatch(x -> x == 2);
            assert list.stream().anyMatch(x -> x == 1);
            assert !list.stream().anyMatch(x -> x == 3);

            p("flatmap vs map\n");
            {
                List<List<Integer>> listlist = new ArrayList<>();
                Set<Integer> set = new HashSet<>();
                for(int i = 0; i < 10; i++) {
                    List<Integer> l = new ArrayList<>();
                    listlist.add(l);
                    for(int j = 0; j < 10; j++) {
                        int v = i * 10 + j;
                        l.add(v);
                        set.add(v);
                    }
                }

                List<Integer> res = listlist.stream().flatMap(Collection::stream).filter((x) -> x != 0 && x % 10 == 0).collect(Collectors.toList());
                assert res.size() == 9;
                for(int i = 0; i < res.size(); i++) {
                    assert res.get(i) == (i + 1) * 10;
                }
                res = listlist.stream().flatMap(Collection::stream).collect(Collectors.toList());
                assert res.size() == 100;
                assert set.containsAll(res);
                res.clear();

                //listlist.forEach(x -> res.addAll(x)); // this gives error saying res should be final, so use ::
                listlist.forEach(res::addAll);
                assert res.size() == 100;
                assert set.containsAll(res);

                res = listlist.stream().flatMap(l -> l.stream()).collect(Collectors.toList());
                assert res.size() == 100;
                assert set.containsAll(res);
            }
        }
        {
            p("limiting infinite streams\n");
            Stream<Integer> infiniteStream = Stream.iterate(0, i -> i + 1);
            List<Integer> collect1 = infiniteStream.limit(10).collect(Collectors.toList());
            assert collect1.size() == 10;

            flag = false;
            try {
                List<Integer> collect2 = infiniteStream.limit(10).collect(Collectors.toList());
                assert collect2.size() == 10;
            } catch(IllegalStateException e) {
                flag = true;
            }
            assert flag : "cannot operate on stream more than once";
            Random r = new Random();

            p("generate limited streams\n");
            infiniteStream = Stream.generate(() -> r.nextInt(100));
            collect1 = infiniteStream.limit(20).collect(Collectors.toList());
            assert collect1.size() == 20;
        }

        p("pass testStreamList\n");

    }

    @Test
    public void testStreamSpeed() {
        boolean flag = false;
        int size = 50;
        List<OIS> list = new ArrayList<>();
        for(int i = 0; i < size; i++) {
            OIS ois = new OIS(i, String.format("name:%d", i));
            list.add(ois);
        }

        List<String> lists;
        lists = list.stream().map(x -> x.getS()).collect(Collectors.toList());
        assert lists.size() == size;

        long timebeg, timeend, timedif;
        {
            p("\ntry serial get with sleepers\n");
            timebeg = System.currentTimeMillis();
            lists = list.stream().map(x -> x.sleepGetS(100)).collect(Collectors.toList());
            timeend = System.currentTimeMillis();
            timedif = timeend - timebeg;
            long mintime = size * 100;
            p("serial   timediff for %d elements of sleep 100ms = %d. expected time > %d\n", size, timedif, mintime);
            assert lists.size() == size;
            assert timedif > mintime;
        }
        {
            p("\ntry parallel get with sleepers\n");
            timebeg = System.currentTimeMillis();
            lists = list.stream().parallel().map(x -> x.sleepGetS(100)).collect(Collectors.toList());
            timeend = System.currentTimeMillis();
            timedif = timeend - timebeg;
            long maxtime = size * 100;
            p("parallel timediff for %d elements of sleep 100ms = %d. expected time < %d\n", size, timedif, maxtime);
            assert lists.size() == size;
            assert timedif < maxtime;
        }
        {
            p("\ntry parallelStream get with sleepers\n");
            timebeg = System.currentTimeMillis();
            lists = list.parallelStream().map(x -> x.sleepGetS(100)).collect(Collectors.toList());
            timeend = System.currentTimeMillis();
            timedif = timeend - timebeg;
            long maxtime = size * 100;
            p("parallel timediff for %d elements of sleep 100ms = %d. expected time < %d\n", size, timedif, maxtime);
            assert lists.size() == size;
            assert timedif < maxtime;
        }
        {
            p("\ntry parallel custom thread pool of 2 with sleepers\n");
            ExecutorService executorService = Executors.newFixedThreadPool(2);
            timebeg = System.currentTimeMillis();
            executorService.submit(() -> list.stream().parallel().map(x -> x.sleepGetS(100)).collect(Collectors.toList()));
            executorService.shutdown();
            try {
                executorService.awaitTermination(20, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            timeend = System.currentTimeMillis();
            timedif = timeend - timebeg;
            long maxtime = size * 100;
            p("parallel timediff for %d elements of sleep 100ms = %d. expected time < %d\n", size, timedif, maxtime);
            assert lists.size() == size;
            assert timedif < maxtime;
        }
        {
            p("\ntry fixed thread pool invokeAll with sleepers\n");
            ExecutorService executorService = Executors.newFixedThreadPool(size);
            List<Callable<String>> listCallable = new ArrayList<>();
            Set<String> setExpectedValues = list.stream().map(x -> x.getS()).collect(Collectors.toSet());
            for(int i = 0; i < size; i++) {
                OIS ois = list.get(i);
                Callable callable = () -> {
                    return ois.sleepGetS(100);
                };
                listCallable.add(callable);
            }
            List<String> listresults = new ArrayList<>();
            List<Future<String>> listf = null;
            timebeg = System.currentTimeMillis();
            try {
                listf = executorService.invokeAll(listCallable);
                executorService.shutdown();
                executorService.awaitTermination(20, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(listf != null) {
                try {
                    for(Future<String> f: listf) {
                        listresults.add(f.get());
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
            timeend = System.currentTimeMillis();
            timedif = timeend - timebeg;
            long maxtime = size * 100;
            p("parallel timediff for %d elements of sleep 100ms = %d. expected time < %d\n", size, timedif, maxtime);
            assert listresults.size() == size;
            assert setExpectedValues.size() == size;
            assert setExpectedValues.containsAll(listresults);
            assert timedif < maxtime;
        }
        {
            p("\ntry fixed thread pool submit one by one with sleepers\n");
            ExecutorService executorService = Executors.newFixedThreadPool(size);
            Set<String> setExpectedValues = list.stream().map(x -> x.getS()).collect(Collectors.toSet());
            List<String> listresults = new ArrayList<>();
            List<Future<String>> listf = new ArrayList<>();
            timebeg = System.currentTimeMillis();
            try {
                for(int i = 0; i < size; i++) {
                    OIS ois = list.get(i);
                    Future<String> f = executorService.submit(() -> ois.sleepGetS(100));
                    listf.add(f);
                }
                executorService.shutdown();
                executorService.awaitTermination(20, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            listresults = listf.stream().map(x -> {
                try {
                    return x.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                return null;
            }).collect(Collectors.toList());
            timeend = System.currentTimeMillis();
            timedif = timeend - timebeg;
            long maxtime = size * 100;
            p("parallel timediff for %d elements of sleep 100ms = %d. expected time < %d\n", size, timedif, maxtime);
            assert listresults.size() == size;
            assert setExpectedValues.size() == size;
            assert setExpectedValues.containsAll(listresults);
            assert timedif < maxtime;
        }
        {
            p("\ntry 4 thread pool task invokeAll with sleepers\n");
            ExecutorService executorService = Executors.newFixedThreadPool(4);
            List<Callable<String>> listCallable = new ArrayList<>();
            Set<String> setExpectedValues = list.stream().map(x -> x.getS()).collect(Collectors.toSet());
            for(int i = 0; i < size; i++) {
                OIS ois = list.get(i);
                Callable callable = () -> {
                    return ois.sleepGetS(100);
                };
                listCallable.add(callable);
            }
            List<String> listresults = new ArrayList<>();
            List<Future<String>> listf = null;
            timebeg = System.currentTimeMillis();
            try {
                listf = executorService.invokeAll(listCallable);
                executorService.shutdown();
                executorService.awaitTermination(20, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(listf != null) {
                try {
                    for(Future<String> f: listf) {
                        listresults.add(f.get());
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
            timeend = System.currentTimeMillis();
            timedif = timeend - timebeg;
            long maxtime = size * 100;
            p("parallel timediff for %d elements of sleep 100ms = %d. expected time < %d\n", size, timedif, maxtime);
            assert listresults.size() == size;
            assert setExpectedValues.size() == size;
            assert setExpectedValues.containsAll(listresults);
            assert timedif < maxtime;
        }
        {
            p("\ntry completeablefuture async with sleepers\n");

        }
        p("pass testStreamSpeed\n");
    }

    @Test
    public void testCompletableFutures() {
        String res = null;
        boolean flag = false;
        int size = 50;
        List<OIS> listois = new ArrayList<>();
        for(int i = 0; i < size; i++) {
            OIS ois = new OIS(i, String.format("name:%d", i));
            listois.add(ois);
        }

        try {
            {
                Future<String> completableFuture;

                completableFuture = CompletableFuture.completedFuture("hello");
                assert "hello".equals(completableFuture.get());

                completableFuture = CompletableFuture.supplyAsync(() -> "hello");
                assert "hello".equals(completableFuture.get());

                completableFuture = CompletableFuture.supplyAsync(() -> "hello").supplyAsync(() -> "again");
                assert "again".equals(completableFuture.get());

                completableFuture = CompletableFuture.supplyAsync(() -> "hello").thenApplyAsync((x) -> x + "again");
                assert "helloagain".equals(completableFuture.get());

                completableFuture = CompletableFuture.supplyAsync(() -> "hello")
                    .thenCombine(CompletableFuture.supplyAsync(() -> "again"), (s1,s2) -> s1 + "." + s2);
                assert "hello.again".equals(completableFuture.get());
            }
            {
                List<Future<Integer>> lfi = new ArrayList<>();
                for(int i = 0; i < size; i++) {
                    OIS ois = listois.get(i);
                    Future<Integer> cf = CompletableFuture.supplyAsync(() -> ois.sleepGetI(100));
                    lfi.add(cf);
                }
                assert lfi.size() == size;
                {
                    List<Integer> l = lfi.stream().map((x) -> {
                        try {
                            return x.get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }).collect(Collectors.toList());
                }
            }
            {
                ExecutorService executorService = Executors.newFixedThreadPool(4);

                Future<String> completableFuture;

                completableFuture = CompletableFuture.completedFuture("hello");
                assert "hello".equals(completableFuture.get());

                completableFuture = CompletableFuture.supplyAsync(() -> "hello", executorService);
                assert "hello".equals(completableFuture.get());

                completableFuture = CompletableFuture.supplyAsync(() -> "hello", executorService).supplyAsync(() -> "again", executorService);
                assert "again".equals(completableFuture.get());

                completableFuture = CompletableFuture.supplyAsync(() -> "hello", executorService).thenApplyAsync((x) -> x + "again", executorService);
                assert "helloagain".equals(completableFuture.get());

                completableFuture = CompletableFuture.supplyAsync(() -> "hello", executorService)
                    .thenCombine(CompletableFuture.supplyAsync(() -> "again", executorService), (s1,s2) -> s1 + "." + s2);
                assert "hello.again".equals(completableFuture.get());

                List<CompletableFuture<String>> l1 =
                    listois.stream().map((ois) -> getCFString(ois, 100, executorService)).collect(Collectors.toList());
                CompletableFuture<Void> l2 =
                    CompletableFuture.allOf(l1.toArray(new CompletableFuture[l1.size()]));
                CompletableFuture<List<String>> l3 =
                    l2.thenApply(f -> {
                            return l1.stream().map(cf -> cf.join()).collect(Collectors.toList());
                        }
                    );
                //CompletableFuture completableFuture1 = l3.thenApply()

                executorService.shutdown();
                executorService.awaitTermination(20, TimeUnit.SECONDS);
            }
            {
            }
        }
        catch (InterruptedException e) { e.printStackTrace(); }
        catch (ExecutionException e) { e.printStackTrace(); }


        p("pass testCompletableFutures\n");
    }
    @Test
    public void testCompletableFutureBasic() throws ExecutionException, InterruptedException {
        boolean flag = false;
        long tbeg = 0;
        long tend = 0;
        long tdif = 0;
        ExecutorService es = null;
        int numthreads = 64;
        es = Executors.newFixedThreadPool(numthreads);
        {
            CompletableFuture<String> cf = new CompletableFuture<>();
            try {
                String ress = cf.get(1000,TimeUnit.MILLISECONDS);     // blocking
            }
            catch (InterruptedException e) { e.printStackTrace(); }
            catch (ExecutionException e) { e.printStackTrace(); }
            catch (TimeoutException e) { flag = true; }
            assert flag; // timeout expected because nothing to get
        }
        {
            // runAsync is for task that doesn't return value. supplyAsync returns value
            CompletableFuture<Void> fvoid = CompletableFuture.runAsync(() -> {
                // override run with lambda
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                    p("hello world\n");
                } catch(Exception e) { e.printStackTrace(); }
            });
            // get still blocks
            try {
                flag = false;
                fvoid.get();
                flag = true;
            } catch (Exception e) { throw e; }
            assert flag;

            CompletableFuture<String> fstring = CompletableFuture.supplyAsync(() -> {
                // override Supplier<String>() String get()
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch(Exception e) {
                    e.printStackTrace();
                    return null;
                }
                return "hello world";
            });
            String res = fstring.get();
            assert "hello world".equals(res);
        }
        {
            TestFuturesClassA tfca = new TestFuturesClassA();
            final String sbase = "base";
            String ress;
            CompletableFuture<String> fs;

            fs = CompletableFuture
                .supplyAsync(() -> { return tfca.supplyAppendStringNoExc(sbase,1,100); } );
            CompletableFuture<String> fs2 = fs.thenApply(v -> tfca.supplyAppendStringNoExc(v,2, 100));
            ress = fs.get();
            assert "base.1".equals(ress);
            ress = fs2.get();
            assert "base.1.2".equals(ress);

            fs = CompletableFuture
                .supplyAsync(() -> { return tfca.supplyAppendStringNoExc(sbase, 3, 100); })
                .thenApplyAsync(v -> tfca.supplyAppendStringNoExc(v, 4, 100))
                .thenApplyAsync(v -> tfca.supplyAppendStringNoExc(v, 5, 100));
            ress = fs.get();
            assert "base.3.4.5".equals(ress);
            // thenApplyAsync is async, so can be useful for long latency jobs if there are other jobs running
            // with single thread or single job in threadpool, this effect is not apparent

            fs = CompletableFuture
                .supplyAsync(() -> { return tfca.supplyAppendStringNoExc(sbase, 3, 100); })
                .thenApply(v -> tfca.supplyAppendStringNoExc(v, 4, 100))
                .thenApply(v -> tfca.supplyAppendStringNoExc(v, 5, 100));
            // thenApply is on same thread. so if it's long latency, and other threads waiting, then
            // overall latency is higher because this is synchronous

            ress = fs.get();
            assert "base.3.4.5".equals(ress);
        }
        {
            tbeg = System.currentTimeMillis();
            List<CompletableFuture<String>> lfs = new ArrayList<>();
            int max = 500;
            TestFuturesClassA tfca = new TestFuturesClassA();
            String base = "base";
            for(int i = 0; i < max; i++) {
                CompletableFuture<String> fs = CompletableFuture
                    .supplyAsync(() ->
                    { return tfca.supplyAppendStringNoExc(
                        base,
                        tfca.supplyRandomInt(10,99),
                        tfca.supplyRandomInt(100,200));
                    });
                lfs.add(fs);
            }
            // always returns Void, so do post processing
            CompletableFuture<Void> cfallof = CompletableFuture
                .allOf(lfs.toArray(new CompletableFuture[lfs.size()]));
            CompletableFuture<List<String>> cflist = cfallof
                .thenApplyAsync(future -> {
                    return lfs.stream().map(cf -> cf.join()).collect(Collectors.toList());
                });
            CompletableFuture<List<String>> cf = cflist
                .thenApplyAsync(l -> {return l.stream().map(s -> s).collect(Collectors.toList()); } )
                .handle((res,e) -> (e != null) ? Arrays.asList(e.getMessage()) : res);
            List<String> ls = null;
            try {
                ls = cf.get(60000, TimeUnit.MILLISECONDS);
                assert CollectionUtils.isNotEmpty(ls);
                assert ls.size() == max;
                Pattern pattern = Pattern.compile("^base\\.\\d{1,3}$");
                for(String s: ls) {
                    assert StringUtils.isNotBlank(s);
                    assert pattern.matcher(s).matches();
                }
            } catch (TimeoutException e) {
                e.printStackTrace();
                assert false;
            }
            tend = System.currentTimeMillis();
            tdif = tend - tbeg;
            p("no thread pool time: %d ms\n", tdif);
        }
        {
            tbeg = System.currentTimeMillis();
            List<CompletableFuture<String>> lfs = new ArrayList<>();
            int max = 500;
            TestFuturesClassA tfca = new TestFuturesClassA();
            String base = "base";
            for(int i = 0; i < max; i++) {
                CompletableFuture<String> fs = CompletableFuture
                    .supplyAsync(() ->
                    { return tfca.supplyAppendStringNoExc(
                        base,
                        tfca.supplyRandomInt(10,99),
                        tfca.supplyRandomInt(100,200));
                    }, es);
                lfs.add(fs);
            }
            // always returns Void, so do post processing
            CompletableFuture<Void> cfallof = CompletableFuture
                .allOf(lfs.toArray(new CompletableFuture[lfs.size()]));
            CompletableFuture<List<String>> cflist = cfallof
                .thenApplyAsync(future -> {
                    return lfs.stream().map(cf -> cf.join()).collect(Collectors.toList());
                }, es);
            CompletableFuture<List<String>> cf = cflist
                .thenApplyAsync(l -> {return l.stream().map(s -> s).collect(Collectors.toList()); }, es)
                .handle((res,e) -> (e != null) ? Arrays.asList(e.getMessage()) : res);
            List<String> ls = null;
            try {
                ls = cf.get(60000, TimeUnit.MILLISECONDS);
                assert CollectionUtils.isNotEmpty(ls);
                assert ls.size() == max;
                Pattern pattern = Pattern.compile("^base\\.\\d{1,3}$");
                for(String s: ls) {
                    assert StringUtils.isNotBlank(s);
                    assert pattern.matcher(s).matches();
                }
            } catch (TimeoutException e) {
                e.printStackTrace();
                assert false;
            }
            tend = System.currentTimeMillis();
            tdif = tend - tbeg;
            p("%d thread pool time: %d ms\n", numthreads, tdif);
        }
        if(es != null) {
            try {
                es.shutdown();
                es.awaitTermination(2, TimeUnit.SECONDS);
            } catch(InterruptedException e) {
                e.printStackTrace();
            } finally {
                if(!es.isTerminated()) {
                    es.shutdownNow();
                }
            }
        }
        p("pass testCompletableFutureBasic\n");
    }

    /*
     *    4 thread pool time: 75246 ms
     *    8 thread pool time: 37587 ms
     *   16 thread pool time: 18849 ms
     *   32 thread pool time: 9450 ms
     *   64 thread pool time: 4754 ms
     *  128 thread pool time: 2436 ms
     *  256 thread pool time: 1271 ms
     */
    @Test
    public void testCompletableFuture1() {
        for(int numthreads = 4; numthreads < 512; numthreads = numthreads * 2) {
            ExecutorService es = Executors.newFixedThreadPool(numthreads);

            long tbeg = System.currentTimeMillis();
            int max = 1000;
            TestFuturesClassA tfca = new TestFuturesClassA();
            String base = "base";

            List<CompletableFuture<String>> lcfs = new ArrayList<>();

            for(int i = 0; i < max; i++) {
                CompletableFuture<String> fs = CompletableFuture
                    .supplyAsync(() -> { return tfca.supplyAppendStringNoExc(base, tfca.supplyRandomInt(10,99),100); },es)
                    .thenApplyAsync((x) -> { return tfca.supplyAppendStringNoExc(x,tfca.supplyRandomInt(10,99),100); },es)
                    .thenApplyAsync((x) -> { return tfca.supplyAppendStringNoExc(x,tfca.supplyRandomInt(10,99),100); },es);
                lcfs.add(fs);
            }
            List<String> errors = new ArrayList<>();
            long timeoutmillis = (max * 500)/numthreads; // min is 300 from wait
            List<String> ls = lcfs.stream().map(cfs -> {
                try {
                    return cfs.get(timeoutmillis, TimeUnit.MILLISECONDS);
                } catch (Exception e) {
                    errors.add(e.getMessage());
                    return null;
                }
            }).collect(Collectors.toList());
            assert errors.size() == 0;
            Pattern pattern = Pattern.compile("^base\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}$");
            for(String s: ls) {
                assert StringUtils.isNotBlank(s);
                assert pattern.matcher(s).matches();
            }

            try {
                es.shutdown();
                es.awaitTermination(2, TimeUnit.SECONDS);
            } catch(InterruptedException e) {
                e.printStackTrace();
            } finally {
                if(!es.isTerminated()) {
                    es.shutdownNow();
                }
            }
            long tdif = System.currentTimeMillis() - tbeg;
            p("%3d thread pool time: %d ms\n", numthreads, tdif);
        }

        for(int numthreads = 4; numthreads < 512; numthreads = numthreads * 2) {
            ExecutorService es = Executors.newFixedThreadPool(numthreads);

            long tbeg = System.currentTimeMillis();
            int max = 1000;
            TestFuturesClassA tfca = new TestFuturesClassA();
            String base = "base";

            List<CompletableFuture<String>> lcfs = new ArrayList<>();

            for(int i = 0; i < max; i++) {
                CompletableFuture<String> fs = CompletableFuture
                    .supplyAsync(() -> { return tfca.supplyAppendStringNoExc(base, tfca.supplyRandomInt(10,99),300); },es);
                lcfs.add(fs);
            }
            List<String> errors = new ArrayList<>();
            long timeoutmillis = (max * 500)/numthreads; // min is 300 from wait
            List<String> ls = lcfs.stream().map(cfs -> {
                try {
                    return cfs.get(timeoutmillis, TimeUnit.MILLISECONDS);
                } catch (Exception e) {
                    errors.add(e.getMessage());
                    return null;
                }
            }).collect(Collectors.toList());
            assert errors.size() == 0;
            Pattern pattern = Pattern.compile("^base\\.\\d{1,3}$");
            for(String s: ls) {
                assert StringUtils.isNotBlank(s);
                assert pattern.matcher(s).matches();
            }

            try {
                es.shutdown();
                es.awaitTermination(2, TimeUnit.SECONDS);
            } catch(InterruptedException e) {
                e.printStackTrace();
            } finally {
                if(!es.isTerminated()) {
                    es.shutdownNow();
                }
            }
            long tdif = System.currentTimeMillis() - tbeg;
            p("%3d thread pool time: %d ms\n", numthreads, tdif);
        }
        p("pass testCompletableFuture1\n");
    }
    @Test
    public void testCompletableFuture2() {
        for(int numthreads = 512; numthreads < 8092; numthreads = numthreads * 2) {
            ExecutorService es = Executors.newFixedThreadPool(numthreads);

            long tbeg = System.currentTimeMillis();
            int max = 1000;
            TestFuturesClassA tfca = new TestFuturesClassA();
            String base = "base";

            List<CompletableFuture<String>> lcfs = new ArrayList<>();

            for(int i = 0; i < max; i++) {
                CompletableFuture<String> fs = CompletableFuture
                    .supplyAsync(() -> { return tfca.supplyAppendStringNoExc(base, tfca.supplyRandomInt(10,99),300); },es);
                lcfs.add(fs);
            }
            List<String> errors = new ArrayList<>();
            long timeoutmillis = (max * 500)/numthreads; // min is 300 from wait
            List<String> ls = lcfs.stream().map(cfs -> {
                try {
                    return cfs.get(timeoutmillis, TimeUnit.MILLISECONDS);
                } catch (Exception e) {
                    errors.add(e.getMessage());
                    return null;
                }
            }).collect(Collectors.toList());
            assert errors.size() == 0;
            Pattern pattern = Pattern.compile("^base\\.\\d{1,3}$");
            for(String s: ls) {
                assert StringUtils.isNotBlank(s);
                assert pattern.matcher(s).matches();
            }

            try {
                es.shutdown();
                es.awaitTermination(2, TimeUnit.SECONDS);
            } catch(InterruptedException e) {
                e.printStackTrace();
            } finally {
                if(!es.isTerminated()) {
                    es.shutdownNow();
                }
            }
            long tdif = System.currentTimeMillis() - tbeg;
            p("%3d thread pool time: %d ms\n", numthreads, tdif);
        }
        p("pass testCompletableFuture1\n");
    }
    @Test
    public void testCallableFutures() {
        boolean flag = false;
        long tbeg = 0;
        long tdif = 0;

        /*
         * max = 1000 callables with 100-200 ms wait
         *
         *    1 thread pool time: 100364 ms
         *    2 thread pool time: 50119 ms
         *    4 thread pool time: 25075 ms
         *    8 thread pool time: 12531 ms
         *   16 thread pool time: 6351 ms
         *   32 thread pool time: 3214 ms
         *   64 thread pool time: 1627 ms
         *  128 thread pool time: 860 ms
         *  256 thread pool time: 432 ms
         *
         */
        for(int numthreads = 1; numthreads < 512; numthreads = numthreads * 2) {
            ExecutorService es = Executors.newFixedThreadPool(numthreads);
            ForkJoinPool fjp = new ForkJoinPool(numthreads); // for work stealing, but not in this tc

            int max = 1000;
            int waitTimeMillis = 500;
            List<Callable<String>> lcs = new ArrayList<>();
            TestFuturesClassA tfca = new TestFuturesClassA();
            final String base = "base";
            List<String> errorList = new ArrayList<>();

            tbeg = System.currentTimeMillis();
            for(int i = 0; i < max; i++) {
                Callable<String> task = () -> {
                    return tfca.supplyAppendStringNoExc(base,tfca.supplyRandomInt(10,99),100);
                };
                lcs.add(task);
            }
            try {
                // this also works as the same as submitting to thread pool. no speed difference?
                    /*
                    List<String> ls = lfs.stream().map(f -> {
                        try {
                            return f.get(waitTimeMillis, TimeUnit.MILLISECONDS);
                        } catch (Exception e) {
                            errorList.add(e.getMessage());
                            return null;
                        }
                    }).collect(Collectors.toList());

                    // submitting parallel stream into threadpool doesn't speed up... why?
                    // maybe because threadpool needs to be forkjoin threadpool
                    Future<List<String>> fls = es.submit(
                        () -> lfs.parallelStream().map(f -> {
                            try {
                                return f.get(waitTimeMillis, TimeUnit.MILLISECONDS);
                            } catch (Exception e) {
                                errorList.add(e.getMessage());
                                return null;
                            }
                        }
                    ).collect(Collectors.toList()));

                    // forkJoinPool doesnt speed up either.. all same speed...
                    Future<List<String>> fls = fjp.submit(
                        () -> lfs.parallelStream().map(f -> {
                            try {
                                return f.get(waitTimeMillis, TimeUnit.MILLISECONDS);
                            } catch (Exception e) {
                                errorList.add(e.getMessage());
                                return null;
                            }
                        }
                    ).collect(Collectors.toList()));
                    List<String> ls = fls.get(waitTimeMillis, TimeUnit.MILLISECONDS);

                    // es and fjp with parallelStream doesnt speed up, so use most simple form..
                    List<String> ls = lfs.stream().map(f -> {
                        try {
                            return f.get(waitTimeMillis, TimeUnit.MILLISECONDS);
                        } catch (Exception e) {
                            errorList.add(e.getMessage());
                            return null;
                        }
                    }).collect(Collectors.toList());

                    */

                final List<Future<String>> lfs = es.invokeAll(lcs);
                List<String> ls = new ArrayList<>();
                for(Future<String> fs: lfs) {
                    try {
                        ls.add(fs.get(waitTimeMillis, TimeUnit.MILLISECONDS));
                    } catch (Exception e) {
                        errorList.add(e.getMessage());
                    }
                }
                assert errorList.size() == 0;
                assert CollectionUtils.isNotEmpty(ls);
                assert ls.size() == max;
                Pattern pattern = Pattern.compile("^base\\.\\d{1,3}$");
                List<Boolean> lb = ls.stream().map(string ->  pattern.matcher(string).matches()).collect(Collectors.toList());
                for(Boolean b: lb) assert b;
            } catch (Exception e) {
                e.printStackTrace();
            }
            tdif = System.currentTimeMillis() - tbeg;
            p("%3d thread pool time: %d ms\n", numthreads, tdif);

            try {
                fjp.shutdown();
                es.shutdown();
                fjp.awaitTermination(2, TimeUnit.SECONDS);
                es.awaitTermination(2, TimeUnit.SECONDS);
            } catch(InterruptedException e) {
                e.printStackTrace();
            } finally {
                if(!es.isTerminated()) {
                    es.shutdownNow();
                }
                if(!fjp.isTerminated()){
                    fjp.shutdownNow();
                }
            }

        }
        {
            List<Integer> li = IntStream.range(0,20).boxed().collect(Collectors.toList());
            assert li.size() == 20;
            long rl = li.parallelStream().filter(i -> i % 2 == 0).count();
            assert rl == 10;
            Random random = new Random();
            Stream<Integer> si = Stream.iterate(0, i -> i + 1);
            li = si.limit(10).collect(Collectors.toList());
            assert li.size() == 10;
        }
        p("pass testCallableFutures\n");
    }

    @Test
    public void testCompletableFutureExceptions() {
        TestFuturesClassA tfca = new TestFuturesClassA();
        String base = "base";
        List<String> errors = new ArrayList<>();
        List<String> errorsExpected = new ArrayList<>();
        List<CompletableFuture<String>> lcfs = new ArrayList<>();
        CompletableFuture<Void> cfv = null;
        List<String> ls = new ArrayList<>();
        boolean flag = false;
        CompletableFuture<String> cfs;
        {
            try {
                cfs = CompletableFuture.supplyAsync(() -> { return tfca.supplyAppendStringNoExc(base, 1, 20); })
                    .thenApply((x) -> { return tfca.supplyAppendStringNoExc(x, 2, 20); });
                String s = cfs.get(5, TimeUnit.MILLISECONDS);
                assert false;
            } catch(InterruptedException | ExecutionException e) {
            } catch(TimeoutException e) { flag = true; }
            assert flag;

            try {
                cfs = CompletableFuture.supplyAsync(() -> { return tfca.supplyAppendStringNoExc(base, 1, 10); })
                    .thenApply((x) -> { return tfca.supplyAppendStringNoExc(x, 2, 10); });
                String s = cfs.get(15, TimeUnit.MILLISECONDS);
                assert false;
            } catch(InterruptedException | ExecutionException e) {
            } catch(TimeoutException e) { flag = true; }
            assert flag;

            try {
                cfs = CompletableFuture.supplyAsync(() -> { return tfca.supplyAppendStringNoExc(base, 1, 10); })
                    .thenApply((x) -> { return tfca.supplyAppendStringNoExc(x, 2, 10); });
                String s = cfs.get(25, TimeUnit.MILLISECONDS);
                assert "base.1.2".equals(s);
            } catch(InterruptedException | ExecutionException e) {
            } catch(TimeoutException e) { }
            assert flag;
        }
        {
            try {
                cfs = CompletableFuture.completedFuture(tfca.supplyAppendString(base,1,10));
                ls.add(cfs.get(5, TimeUnit.MILLISECONDS));
            } catch(InterruptedException | ExecutionException | TimeoutException e) { assert false; }

            try {
                cfs = CompletableFuture.supplyAsync(() -> {
                    try {
                        return tfca.supplyAppendString(base,1,10);
                    } catch (InterruptedException e) {
                        errors.add(e.getMessage());
                        return null;
                    }
                });
                assert errors.size() == 0;
                ls.add(cfs.get(5, TimeUnit.MILLISECONDS));
                flag = false;
            } catch(InterruptedException | ExecutionException e) {
            } catch(TimeoutException e) { flag = true; }
            assert flag;

            try {
                cfs = CompletableFuture.supplyAsync(() -> { return tfca.supplyAppendStringNoExc(base,1,20); });
                assert errors.size() == 0;
                ls.add(cfs.get(5, TimeUnit.MILLISECONDS));
                flag = false;
            } catch(InterruptedException | ExecutionException e) {
            } catch(TimeoutException e) { flag = true; }
            assert flag;

            try {
                flag = false;
                cfs = CompletableFuture
                    .supplyAsync(() -> { return tfca.supplyAppendStringNoExc(base,1,10); })
                    .supplyAsync(() -> { return tfca.supplyAppendStringNoExc(base,2,10); })
                    .supplyAsync(() -> { return tfca.supplyAppendStringNoExc(base,3,10); });
                assert errors.size() == 0;
                String s = cfs.get(15, TimeUnit.MILLISECONDS);
                assert "base.3".equals(s);  // so the first two get ignored, so it's not 30ms!
                flag = true;
            } catch(InterruptedException | ExecutionException e) {
            } catch(TimeoutException e) { }
            assert flag;

            try {
                flag = false;
                cfs = CompletableFuture
                    .supplyAsync(() -> { return tfca.supplyAppendStringNoExc(base,1,10); })
                    .supplyAsync(() -> { return tfca.supplyAppendStringNoExc(base,2,10); })
                    .supplyAsync(() -> { return tfca.supplyAppendStringNoExc(base,3,10); });
                assert errors.size() == 0;
                String s = cfs.get(40, TimeUnit.MILLISECONDS);
                assert "base.3".equals(s);  // so the first two get ignored!!
                flag = true;
            } catch(InterruptedException | ExecutionException e) {
            } catch(TimeoutException e) { }
            assert flag;

            try {
                flag = false;
                cfs = CompletableFuture
                    .supplyAsync(() -> { return tfca.supplyAppendStringNoExc(base,1,10); })
                    .thenApplyAsync((x) -> { return tfca.supplyAppendStringNoExc(x,2,10); })
                    .thenApplyAsync((x) -> { return tfca.supplyAppendStringNoExc(x,3,10); });
                assert errors.size() == 0;
                String s = cfs.get(20, TimeUnit.MILLISECONDS);
                assert "base.1.2.3".equals(s);
            } catch(InterruptedException | ExecutionException e) {
            } catch(TimeoutException e) { flag = true; }
            assert flag;

            ls.clear();
            try {
                flag = false;
                cfs = CompletableFuture
                    .supplyAsync(() -> { return tfca.supplyAppendStringNoExc(base,1,10); })
                    .thenApplyAsync((x) -> { return tfca.supplyAppendStringNoExc(x,2,10); })
                    .thenApplyAsync((x) -> { return tfca.supplyAppendStringNoExc(x,3,10); });
                assert errors.size() == 0;
                String s = cfs.get(40, TimeUnit.MILLISECONDS);
                assert "base.1.2.3".equals(s);
                flag = true;
            } catch(InterruptedException | ExecutionException e) {
            } catch(TimeoutException e) { }
            assert flag;

            try {
                flag = false;
                ls.clear();
                cfv = CompletableFuture
                    .supplyAsync(() -> { return tfca.supplyAppendStringNoExc(base,1,10); })
                    .thenAcceptAsync((x) -> { tfca.consumeStringNoExc(x, ls, 100);});
                assert ls.size() == 0;  // because of time timer
                String s = ls.get(0);
                assert "base.1".equals(s); // should not have been here
            } catch(IndexOutOfBoundsException e) {
                flag = true;
            }
            assert flag;

            try {
                flag = false;
                ls.clear();
                cfv = CompletableFuture
                    .supplyAsync(() -> { return tfca.supplyAppendStringNoExc(base,1,10); })
                    .thenAcceptAsync((x) -> { tfca.consumeStringNoExc(x, ls, 0); });
                assert ls.size() == 0;  // because of timer
                String s = ls.get(0);
                assert "base.1".equals(s); // should not have been here
            } catch(IndexOutOfBoundsException e) {
                flag = true;
            }
            assert flag;
            if(cfv != null) {
                cfv.join();             // need to wait, else the append will execute.
                assert ls.size() == 1; // from previous append
            }
            ls.clear();
            try {
                flag = false;
                ls.clear();
                cfv = CompletableFuture
                    .supplyAsync(() -> { return tfca.supplyAppendStringNoExc(base,1,10); })
                    .thenAcceptAsync((x) -> { tfca.consumeStringNoExc(x, ls, 10); });
                cfv.join();                 // wait for void type...
                assert ls.size() == 1;
                String s = ls.get(0);
                assert "base.1".equals(s); // should not have been here
                flag = true;
            } catch(IndexOutOfBoundsException e) {
            }
            assert flag;

            ls.clear();
            try {
                flag = false;
                ls.clear();
                CompletableFuture
                    .supplyAsync(() -> { return tfca.supplyAppendStringNoExc(base,1,10); })
                    .thenAcceptAsync((x) -> { tfca.consumeStringNoExc(x, ls, 10); })
                    .join();    // bad practice to call join here. do it when needed. like above
                assert ls.size() == 1;
                String s = ls.get(0);
                assert "base.1".equals(s); // should not have been here
                flag = true;
            } catch(IndexOutOfBoundsException e) {
            }
            assert flag;

            ls.clear();
            try {
                flag = false;
                ls.clear();
                CompletableFuture
                    .supplyAsync(()      -> { return tfca.supplyAppendStringNoExc(base,1,10); })
                    .thenApplyAsync((x)  -> { return tfca.supplyAppendStringNoExc(x,2,10); })
                    .thenApplyAsync((x)  -> { return tfca.supplyAppendStringNoExc(x,3,10); })
                    .thenAcceptAsync((x) -> { tfca.consumeStringNoExc(x, ls, 10); })
                    .join();    // bad practice to call join here. do it when needed.
                assert ls.size() == 1;
                String s = ls.get(0);
                assert "base.1.2.3".equals(s); // should not have been here
                flag = true;
            } catch(IndexOutOfBoundsException e) {
            }
            assert flag;

            ls.clear();
            try {
                flag = false;
                ls.clear();
                cfs = CompletableFuture
                    .supplyAsync(()      -> { return tfca.supplyAppendStringNoExc(base,1,10); })
                    .thenApplyAsync((x)  -> { return tfca.throwExceptionIfEmpty(null,10); })
                    .thenApplyAsync((x)  -> { return tfca.supplyAppendStringNoExc(x,3,10); })
                    .exceptionally(t -> { errors.add(t.getMessage()); return "error"; });
                assert errors.size() == 0;
                String s = cfs.get(100, TimeUnit.MILLISECONDS);
                assert errors.size() == 1;
                assert "error".equals(s); // should not have been here
                flag = true;
            } catch(Exception e) {
            }
            assert flag;

            errors.clear();
            try {
                ls.clear();
                cfs = CompletableFuture
                    .supplyAsync(() -> { return tfca.supplyAppendStringNoExc(base,1,20); })
                    .thenApplyAsync(x -> { return tfca.appendThrowExceptionIfEmpty(x,null,20); })
                    .exceptionally(t -> { errors.add(t.getMessage()); return "error1"; })
                    .thenApplyAsync(x -> { return tfca.appendThrowExceptionIfEmpty(x,null,20); })
                    .exceptionally(t -> { errors.add(t.getMessage()); return "error2"; })
                    .thenApplyAsync(x -> { return tfca.appendThrowExceptionIfEmpty(x,"append3",20); })
                    .exceptionally(t -> { errors.add(t.getMessage()); return "error3"; });
                assert errors.size() == 0;
                String s = cfs.get(200, TimeUnit.MILLISECONDS);
                assert errors.size() == 2;
                assert "error2.append3".equals(s);
            } catch(Exception e) { assert false; }
        }

        p("pass testCompletableFutureExceptions\n");
    }

    private void startWorker() {

    }

    @Test
    public void testThreadsUncaughtExceptions() {
        Thread.setDefaultUncaughtExceptionHandler(
            (othread, othrowable) -> {
                startWorker();
            }
        );
    }

    @Test
    public void testCallableExceptions() {

    }

    @Test
    public void test() {
        long timebeg = System.currentTimeMillis();

        long timeend = System.currentTimeMillis();
    }
}

class TestFuturesClassA {
    String name;
    Random random = new Random();
    static AtomicInteger aid_ = new AtomicInteger(0);
    AtomicInteger ai = new AtomicInteger(0);
    LinkedList<Integer> lli = new LinkedList<>();
    int storedi = 0;
    String storeds = "base";
    public TestFuturesClassA() {
        this.name = String.format("id.%03d", aid_.getAndIncrement());
    }
    public void setStoreds(String s) {
        this.storeds = s;
    }
    public String getStoreds() {
        return storeds;
    }
    public void setStoredi(int i) {
        storedi = i;
    }
    public int getStoredi() {
        return storedi;
    }
    public void consumeInteger(Integer i) {
        lli.add(i);
    }
    public Integer supplyInteger() {
        return (lli.size() == 0) ? null : lli.pollFirst();
    }
    public String appendThrowExceptionIfEmpty(String msg, String append, long millis) {
        if(millis != 0) {
            sleepNoExc(millis);
        }
        int sz = msg.length();
        sz = append.length();
        return String.format("%s.%s",msg,append);
    }
    public String throwExceptionIfEmpty(String msg, long millis) {
        if(millis != 0) {
            sleepNoExc(millis);
        }
        int sz = msg.length();
        return msg;
    }
    public String throwExceptionIfEmptyThrows(String msg, long millis) throws Exception {
        if(millis != 0) {
            sleepNoExc(millis);
        }
        if(msg == null) {
            throw new Exception("exception");
        }
        return msg;
    }
    public Integer supplyRandomInt(int min, int max) {
        if(max < min)
            System.out.printf(String.format("max %d < min %d", max, min));
        int d = max - min;
        int r = random.nextInt(d) + min;
        return r;
    }
    public void consumeInteger(Integer i, long millis) throws InterruptedException {
        if(millis != 0){
            Thread.sleep(millis);
        }
        consumeInteger(i);
    }
    public Integer supplyInteger(long millis) throws InterruptedException {
        if(millis != 0){
            Thread.sleep(millis);
        }
        return supplyInteger();
    }
    public Integer supplyRandom(int min, int max, long millis) throws Exception {
        if(millis != 0){
            Thread.sleep(millis);
        }
        return supplyRandomInt(min,max);
    }
    public int increment(int i, long millis) throws InterruptedException {
        int res = i + 1;
        if(millis != 0){
            Thread.sleep(millis);
        }
        return res;
    }

    public void consumeIntegerNoExc(Integer i, long millis) {
        sleepNoExc(millis);
        consumeInteger(i);
    }
    public Integer supplyIntegerNoExc(long millis) {
        sleepNoExc(millis);
        return supplyInteger();
    }
    public Integer supplyRandomNoExc(int min, int max, long millis) {
        sleepNoExc(millis);
        int d = max - min;
        int vr = random.nextInt();
        int v = (vr % d) + min;
        return v;
    }
    public int incrementNoExc(int i, long millis) {
        int res = i + 1;
        sleepNoExc(millis);
        return res;
    }
    public String supplyAppendString(String s, int i, long millis) throws InterruptedException {
        if(millis != 0){
            Thread.sleep(millis);
        }
        return String.format("%s.%d",s,i);
    }
    public String supplyAppendStringNoExc(String s, int i, long millis) {
        sleepNoExc(millis);
        return String.format("%s.%d",s,i);
    }
    public void consumeStringNoExc(String s, List<String> ls, long millis) {
        sleepNoExc(millis);
        ls.add(s);
    }
    public String consumeStringNoExc(String s, long millis) {
        sleepNoExc(millis);
        return s;
    }
    public String supplyString() {
        return String.format("%s.%d",storeds,ai.getAndIncrement());
    }
    public String supplyString100SleepNoExc() {
        sleepNoExc(100);
        return supplyString();
    }
    public void sleepNoExc(long millis) {
        if(millis == 0) return;
        try {
            Thread.sleep(millis);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

}


