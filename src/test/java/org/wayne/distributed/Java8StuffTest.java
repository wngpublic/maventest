package org.wayne.distributed;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Java8StuffTest {
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
    public void test() {
        long timebeg = System.currentTimeMillis();

        long timeend = System.currentTimeMillis();
    }
}

