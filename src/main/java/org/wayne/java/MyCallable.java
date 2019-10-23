package org.wayne.java;

//import javafx.util.Callback;
import org.wayne.main.MyBasic;
import org.wayne.misc.Utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class MyCallable implements MyBasic {

    Utils utils = new Utils();

    final static int poolSize = 3;

    BlockingQueueRegistered<Runnable> queueRegistered = new BlockingQueueRegistered<>(10);

    BlockingQueue<Runnable> queueThreadPool = new LinkedBlockingDeque<>(10);

    ThreadFactory threadFactory1 = new MyThreadFactory("myGroupNameABC");
    ThreadFactory threadFactory2 = new MyThreadFactory("myGroupNameDEF");
    ThreadFactory threadFactory3 = new MyThreadFactory("myGroupNameGHI");

    ThreadPoolExecutor threadPoolExecutor = new
        ThreadPoolExecutor(poolSize,8,5,TimeUnit.SECONDS,
        queueThreadPool, new ThreadPoolExecutor.AbortPolicy());

    ExecutorService fixedThreadPool = Executors.newFixedThreadPool(poolSize);

    ExecutorService fixedThreadPoolFactory = Executors.newFixedThreadPool(poolSize, threadFactory1);

    ExecutorService fixedThreadPoolGroup1 = Executors.newFixedThreadPool(poolSize,
        new ThreadFactory() {
            AtomicInteger ctr = new AtomicInteger(0);
            @Override public Thread newThread(Runnable r) {
                return new Thread(r, String.format("group-1-thread-%02d", ctr.getAndIncrement()));
            }
        }
    );

    ExecutorService fixedThreadPoolGroup2 = Executors.newFixedThreadPool(poolSize,
        new ThreadFactory() {
            AtomicInteger ctr = new AtomicInteger(0);
            @Override public Thread newThread(Runnable r) {
                return new Thread(r, String.format("group-2-thread-%02d", ctr.getAndIncrement()));
            }
        }
    );


    ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(poolSize, threadFactory2);

    Set<ExecutorService> setExecutor = new HashSet<>();

    public static void p(String f, Object ...o) {
        System.out.printf(f,o);
    }

    public static void pl(String f, Object ...o) {
        p(f + "\n", o);
    }

    public MyCallable() {
        setExecutor.add(threadPoolExecutor);
        setExecutor.add(fixedThreadPool);
        setExecutor.add(scheduledExecutor);
        setExecutor.add(fixedThreadPoolFactory);
        setExecutor.add(fixedThreadPoolGroup1);
        setExecutor.add(fixedThreadPoolGroup2);
    }

    public void shutdown() {
        setExecutor.stream().forEach(x -> { shutdownExecutorService(x); });
    }

    void shutdownExecutorService(ExecutorService x) {
        x.shutdown();
        try {
            int ctr = 0;
            while(!x.awaitTermination(2, TimeUnit.SECONDS)) {
                p("shutdown attempted\n");
                ctr++;
                if(ctr > 2) {
                    x.shutdownNow();
                    break;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init() {

    }

    public Future<String> futureScheduledMethod(int id, String msg, int delayInterval) {
        return scheduledExecutor.schedule(() -> {
            try { Thread.sleep(utils.getInt(100,1000)); } catch(InterruptedException e) { }
            p("done with futureScheduledMethod\n");
            return String.format("Obj%d %s", id, msg);
        }, delayInterval,TimeUnit.MILLISECONDS);
    }

    public void runrunrun() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        p("2: run run run\n");
    }

    public void testThread1() {
        new Thread(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            p("1: thread run\n");
        }).start();

        new Thread(() -> runrunrun()).start();

        Runnable runnable = () -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            p("3: runnable runner\n");
        };

        new Thread(runnable).start();

        int v = 0;
        AtomicInteger aint = new AtomicInteger(0);

        Thread t = new Thread(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            aint.incrementAndGet();
            //v++; // cant modify primitive. why??
        });

        try {
            t.start();
            t.join();
            p("joined variable:%d\n", aint.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    void testCompletableFuture1() throws ExecutionException, InterruptedException {

        ListenerOne<String> listener1 = new ListenerOne<String>() {
            public String notify(String msg) {
                return msg + " + listenerOne";
            }
        };

        ListenerOne<String> listener2 = new ListenerOne<String>() {
            public String notify(String msg) {
                return msg + " + listenerTwo";
            }
        };

        ListenerOne<String> listener3 = new ListenerOne<String>() {
            public String notify(String msg) {
                return msg + " + listenerThree";
            }
        };

        CompletableFuture<String> cf1 = CompletableFuture
                .supplyAsync(() -> "hello world")
                .thenApply(x -> listener1.notify(x))
                .thenApply(x -> listener2.notify(x))
                .thenApply(x -> listener3.notify(x));
        p("completablefuture: %s\n", cf1.get());

        CompletableFuture<String> cf2 = CompletableFuture
                .supplyAsync(() -> "hello world")
                .thenApply(x -> x + "+1")
                .thenApply(x -> x + "+2");
        p("completablefuture: %s\n", cf2.get());

        Supplier<String> supplier1 = new Supplier<String>() {
            @Override public String get() { return "hello supplier"; }
        };
        CompletableFuture<String> cf3 = CompletableFuture
                .supplyAsync(supplier1)
                .thenApplyAsync(x -> x + "+1")
                .thenApply(x -> x + "+2");
        p("completablefuture: %s\n", cf3.get());
        p("completablefuture: %s\n", cf3.get()); // contrast with stateful supplier

        Supplier<String> supplier2 = new Supplier<String>() {
            int ctr = 0;
            @Override public String get() {
                return String.format("hello supplier %d: ", ctr++);
            }
        };

        // supplier with counter
        for(int i = 0; i < 3; i++) {
            p("supplier get call = %s\n", supplier2.get());
        }

        // supplier called only once, so same result
        CompletableFuture<String> cf4 = CompletableFuture
                .supplyAsync(supplier2)
                .thenApply(x -> x + "+1")
                .thenApplyAsync(x -> x + "+2");
        p("completablefuture1: %s\n", cf4.get());
        p("completablefuture1: %s\n", cf4.get());

        // supplier called again, so different result
        // also async and sync mixture with threadPool
        CompletableFuture<String> cf5 = CompletableFuture
            .supplyAsync(supplier2, fixedThreadPool)
            .thenApplyAsync(x -> x + "+1", fixedThreadPool)
            .thenApplyAsync(x -> x + "+2")
            .thenApply(x -> x + "+3");
        p("completablefuture3: %s\n", cf5.get());
        p("completablefuture4: %s\n", cf5.get());

        p("test completablefuture thenaccept\n");
        CompletableFuture
            .supplyAsync(supplier2)
            .thenApply(x -> listener1.notify(x))
            .thenApply(x -> listener2.notify(x))
            .thenAccept(x -> p("print:%s\n", x));

        p("passed testCompleteableFuture2\n");
    }

    void testCompletableFuture2() throws ExecutionException, InterruptedException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");

        // combines completablefuture composition
        CompletableFuture<Integer> cf1 = CompletableFuture.supplyAsync(()->10).thenApply(x->x+1);
        CompletableFuture<Integer> cf2 = CompletableFuture.supplyAsync(()->20).thenApply(x->x+2);
        CompletableFuture<Integer> cf3 = cf1.thenCombine(cf2, (x,y)->x*10+y*100);
        p("cf3 = %d\n", cf3.get());

        // runAsync starts running at runAsync, not CompletableFuture.get()
        CompletableFuture<Void> cf4 = CompletableFuture
                .runAsync(() -> {
                    Integer sum = 0;
                    for(int i = 0; i < 10000000; i++) {
                        sum += 1;
                        md.update(sum.byteValue());
                    }
                    p("runAsync cf4 done, sum = %d\n", sum);
                }, fixedThreadPool)
                .exceptionally(e -> { e.printStackTrace(); return null; }); // null for Void

        CompletableFuture<Void> cf5 = CompletableFuture.runAsync(() -> {
            int sum = 0;
            for(int i = 0; i < 10; i++) sum += 10;
            p("runAsync cf5 done, sum = %d\n", sum);
        }).exceptionally(e -> { e.printStackTrace(); return null; });

        CompletableFuture<Void> cf6 = CompletableFuture
                .runAsync(() -> {
                    int sum = 0;
                    for(int i = 0; i < 100; i++) sum += 10;
                    p("runAsync cf6 part1 done, sum = %d\n", sum);
                })
                .runAsync(() -> {
                    int sum = 0;
                    for(int i = 0; i < 1; i++) sum += 10;
                    p("runAsync cf6 part2 done, sum = %d\n", sum);
                }, threadPoolExecutor)
                .thenRunAsync(() -> {
                    int sum = 0;
                    for(int i = 0; i < 100; i++) sum += 10;
                    p("runAsync cf6 part3 done, sum = %d\n", sum);
                })
                .exceptionally(e -> { e.printStackTrace(); return null; });

        cf5.get();  // does not get, but blocks here, same with join
        cf4.join();

        //CompletableFuture<Integer> cf7 = future.

        p("passed testCompletableFuture2\n");
    }

    void testCompletableFuture3() throws ExecutionException, InterruptedException {
        Function<String,String> f1 = (x) -> {
            try {
                Thread.sleep(utils.getInt(100,500));
            } catch (InterruptedException e) {
            }
            p("f1 done from sleep\n");
            return x;
        };
        Callable<String> callable1 = () -> f1.apply("return msg from callable1");
        CompletableFuture<String> cf1 = CompletableFuture.supplyAsync(() ->
            f1.apply("return msg from callable1"));
        p("testCompletableFuture3 cf1 call done. waiting for get\n");
        String s1 = cf1.get();
        p("testCompletableFuture3 got msg from cf1: %s\n", s1);
    }

    void testCompletableFutureErrorHandle() {
        Supplier<Integer> supplier = new Supplier<Integer>() {
            int ctr = 0;
            @Override
            public Integer get() {
                //int ret = ctr;
                //ctr ^= utils.getInt(0,0xffffffff);
                try {
                    Thread.sleep(utils.getInt(10,50));
                    p("done testCompletableFutureErrorHandle Supplier get %d\n", ctr);
                } catch (InterruptedException e) {}
                return ctr++;
            }
        };
        CompletableFuture<Integer> f1 = CompletableFuture.supplyAsync(supplier);
        CompletableFuture<Integer> f2 = CompletableFuture.supplyAsync(supplier);
        p("done testCompletableFutureErrorHandle, did not wait for supplyAsync\n");
    }

    void testCallable1() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ExecutionException, InterruptedException {
        /*
         * callable is old school method of submitting to executor,
         * getting future back, and calling blocking get on future.
         * it is the same as using runnable, but callable returns something.
         * use completablefuture for both, instead of runnable or callable.
         *
         * completablefutures can use supplier instead of call to get value.
         */
        // use service.execute or service.submit
        //threadPoolExecutor.
        //fixedThreadPool.

        Runnable runnable1 = () -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
            p("done with runnable1\n");
        };

        Runnable runnable2 = new Runnable() {
            String msg;
            int sleepTime;

            public void setMsg(String msg, int sleepTime) {
                this.msg = msg;
                this.sleepTime = sleepTime;
            }

            public void run() {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                }
                p("done with runnable2\n");
            }
        };

        FutureTask<String> future1 = new FutureTask<String>(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
            p("done with futuretask1\n");
            return "done with futuretask1";
        });

        Callable<String> callable1 = () -> {
            try {
                Thread.sleep(utils.getInt(100,500));
            } catch (InterruptedException e) {
            }
            p("done with callable1\n");
            return "return msg from callable1";
        };

        Callable<String> callable2 = new Callable<String>(){
            String msg = "defaultMsg for Callable";
            int sleepTime = utils.getInt(100,500);
            public void setMsg(String msg, int sleepTime) { this.msg = msg; this.sleepTime = sleepTime; }
            public String getMsg() { return msg; }
            @Override public String call() {
                try { Thread.sleep(sleepTime); } catch(InterruptedException e) { }
                p("done with callable2\n");
                return msg;
            }
        };
        //callable2.setMsg("Callable2 message set", utils.getInt(100,500));
        Future<?> f1 = fixedThreadPoolGroup1.submit(runnable1);
        fixedThreadPoolGroup1.submit(runnable2);
        Future<?> f2 = fixedThreadPoolGroup1.submit(future1);
        Future<String> f3 = fixedThreadPoolGroup1.submit(callable2);
        Future<String> f4 = fixedThreadPoolGroup1.submit(callable1);
        p("testCallable waiting for f1 and f2\n");
        String s1 = (String)f1.get();
        String s2 = (String)f2.get();
        String s3 = f3.get();
        String s4 = f4.get();
        p("testCallable get s1:%s s2:%s s3:%s s4:%s\n", s1, s2, s3, s4);
    }

    public void testCallable2() throws Exception {

        Callable<String> c1 = new Callable<String>() {
            String msg = "default";
            int sleepTime;
            public void setMsg(String msg, int sleepTime) { this.msg = msg; this.sleepTime = sleepTime; }
            public String getMsg() { return msg; }
            @Override public String call() {
                try { Thread.sleep(sleepTime); } catch(InterruptedException e) { }
                p("done with callable1 instance\n");
                return msg;
            }
        };

        int delayInterval = 100;

        Map<Future<?>, Callable<String>> map = new HashMap<>();

        // anonymous inner method does not work like below. use reflection
        //callable1.setMsg("Callable1 message",100); // does not work
        c1.getClass()
                .getMethod("setMsg", String.class, int.class)
                .invoke(c1,"Callable1 message", utils.getInt(100,500));
        map.put(scheduledExecutor.schedule(c1,delayInterval,TimeUnit.MILLISECONDS), c1);

        Callable<String> c2 = () -> {
            try { Thread.sleep(200); } catch (Exception e) { }
            p("done with callable2 instance\n");
            return "donec2msg";
        };
        Callable<String> c3 = () -> {
            Thread.sleep(200); // no need to catch exception if throw out
            p("done with callable2 instance\n");
            return "donec2msg";
        };
        map.put(scheduledExecutor.schedule(c2,delayInterval,TimeUnit.MILLISECONDS), c2);

        for(int i = 0; i < 10; i++) {
            CallableObject c = new CallableObject(i,String.format("Obj%d msg", i), utils.getInt(100,1000));
            map.put(scheduledExecutor.schedule(c,delayInterval,TimeUnit.MILLISECONDS), c);
        }

        map.keySet().forEach(f -> {
            try {
                p("get %s\n", f.get(500,TimeUnit.MILLISECONDS));
            }
            catch(TimeoutException e) {
                p("TimeoutException occurred: %s\n", e.getMessage());
                f.cancel(true); // or just let it finish
            }
            catch (Exception e) { e.printStackTrace(); }
        });

        p("done testCallable2 pass\n");
    }

    public void testReflectionCallable1() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        Callable<String> callable1 = new Callable<String>() {
            String msg = "default";
            int sleepTime;
            public void setMsg(String msg, int sleepTime) { this.msg = msg; this.sleepTime = sleepTime; }
            public String getMsg() { return msg; }
            @Override public String call() {
                try { Thread.sleep(sleepTime); } catch(InterruptedException e) { }
                p("done with callable1");
                return msg;
            }
        };

        // anonymous inner method does not work like below. use reflection
        //callable1.setMsg("Callable1 message",100); // does not work

        // getMethod(methodName, methodArg1, methodArg2, ...)
        // invoke(objectInstance, arg1, arg2, ...)
        Method method1 = callable1.getClass().getMethod("setMsg", String.class, int.class);
        method1.invoke(callable1,"Callable1 message", 100);

        p("msg val = %s\n", callable1.getClass().getMethod("getMsg").invoke(callable1,null));
    }

    void testTask1() {

    }

    void testParallelVsSerialStream() {
        // create 1M obj, filter by criteria.
        int numObj = 100;
        List<ObjectGeneric> list = new ArrayList<>();

        IntStream.range(0,numObj).forEach(i -> {
            ObjectGeneric obj = new ObjectGeneric(i,utils.getInt(0,numObj),utils.getBool());
            list.add(obj);
        });
        long times, timee, timed;

        {
            AtomicInteger ctr1 = new AtomicInteger(0);
            times = System.currentTimeMillis();
            IntStream.range(0, 10).forEach(i ->
                list.stream()
                .filter(o -> {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return o.vi() % 2 == 0;
                } )
                .map(o -> ctr1.getAndAdd(1))
                .reduce(0, (a, b) -> a + b)
            );
            timee = System.currentTimeMillis();
            timed = timee - times;
            p("time diff serial = %d ms\n", timed);
        }
        {
            AtomicInteger ctr2 = new AtomicInteger(0);
            times = System.currentTimeMillis();
            Future<?> future = fixedThreadPoolGroup1.submit(
                () -> IntStream
                .range(0, 10)
                .parallel()
                .forEach(i ->
                    list.stream()
                    .filter(o -> {
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return o.vi() % 2 == 0;
                    } )
                    .map(o -> ctr2.getAndAdd(1))
                    .reduce(0, (a, b) -> a + b)
                )
            );
            try {
                future.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
            timee = System.currentTimeMillis();
            timed = timee - times;
            p("time diff parallel = %d ms\n", timed);
        }
        {
            AtomicInteger ctr2 = new AtomicInteger(0);
            times = System.currentTimeMillis();
            Future<?> future = fixedThreadPoolGroup1.submit(
                () -> IntStream
                .range(0, 10)
                .parallel()
                .forEach(i ->
                    list.parallelStream()
                    .filter(o -> {
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return o.vi() % 2 == 0;
                    } )
                    .map(o -> ctr2.getAndAdd(1))
                    .reduce(0, (a, b) -> a + b)
                    )
            );
            try {
                future.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
            timee = System.currentTimeMillis();
            timed = timee - times;
            p("time diff parallel = %d ms\n", timed);
        }
    }

    public void testParallelStream1() {
        {
            // non thread safe with parallel can cause issue, eg NPE
            int numObj = 30;
            List<ObjectGeneric> list = new ArrayList<>();
            p("parallel generate with ArrayList, may fault NPE!\n");
            try {
                Future<?> f = fixedThreadPoolGroup1.submit(() -> IntStream
                        .range(0,numObj)
                        .parallel()
                        .forEach(i -> {
                            list.add(new ObjectGeneric(i,utils.getInt(0,numObj),utils.getBool()));
                        })
                );
                f.get();
                p("size of list:%d\n", list.size());
                list.parallelStream().forEach(o -> p("%2d ", o.id()));
                p("\n");
            } catch (Exception e) {
                e.printStackTrace ();
            }
        }
        {
            // use ConcurrentLinkedQueue, ArrayBlockingQueue, LinkedBlockingDequeue
            int numObj = 50;
            Queue<ObjectGeneric> queue = new ConcurrentLinkedQueue<>();
            p("\n\n");
            p("parallel generate with ConcurrentLinkedQueue\n");
            try {
                Future<?> f = fixedThreadPoolGroup1.submit(() -> IntStream
                        .range(0,numObj)
                        .parallel()
                        .forEach(i -> {
                            queue.add(new ObjectGeneric(i,utils.getInt(0,numObj),utils.getBool()));
                        })
                );
                f.get();

                p("size of queue:%d\n", queue.size());

                f = fixedThreadPoolGroup1.submit(() -> queue.parallelStream().forEach(o -> p("%2d ", o.id())));
                f.get();
                p("\n");

                queue.stream().forEach(o -> p("%2d ", o.id()));
                p("\n");
            } catch (Exception e) {
                e.printStackTrace ();
            }
        }
        long times, timee, timed;
        p("testParallelStream1 done\n");
    }
    public void testExecutorCompletionService() {
        /*
        ExecutorCompletionService is good for taking finished tasks from executor pool.
        This eliminates the need for keeping track of futures externally.
        Uses ExecutorCompletionService blocking take() to retrieve futures instead, which
        blocks for ANY completed future. This is in contrast to externally keeping
        future and doing blocking get on a collection of futures.

        You cannot submit arbitrary tasks to CompletionService. It is typed.
         */
        ExecutorCompletionService<Integer> executorCompletionService =
            new ExecutorCompletionService<Integer>(fixedThreadPoolGroup2);
        int numItems = 30;
        IntStream
            .range(0,numItems)
            .parallel()
            .forEach(i -> executorCompletionService
                .submit(() -> {
                    try { Thread.sleep(utils.getInt(200,900)); } catch(Exception e) { }
                    return i;
                })
            );
        try {
            // must keep track of how many submitted, hangs if overcount
            for(int i = 0; i < numItems; i++) {
                Future<Integer> f = executorCompletionService.take();
                p("%2d ", f.get());
            }
            p("\n");
        } catch(Exception e) { }
        p("done with completionservice\n");
    }

    public void testCompletableFutureStream() {
        List<Integer> list1 = Stream.iterate(0,x -> x + 1).limit(10).collect(Collectors.toList());
        Function<Integer,String> f1 = x -> String.format("This is %2d", x);
        Function<String,String>  f2 = x -> String.format("%s Add Some Junk Here", x);

        /*
         * stream ->
         * map that produces completablefuture ->
         * map that produces completablefuture ->
         * for each completablefuture
         *   thenAccept to printer
         */
        list1
            .stream()
            .map(x -> CompletableFuture.supplyAsync(() -> {
                try { Thread.sleep(100); } catch (InterruptedException e) { }
                return f1.apply(x);
            }))
            .map(x -> {
                try { Thread.sleep(100); } catch (InterruptedException e) { }
                return x.thenApply(f2);
            })
            .forEach(x -> x.thenAccept(y -> p("%s\n", y)));

        p("more of same\n");
        list1
            .stream()
            .map(x -> CompletableFuture.supplyAsync(() -> {
                try { Thread.sleep(100); } catch (InterruptedException e) { }
                return f1.apply(x);
            }))
            .map(x -> {
                try { Thread.sleep(100); } catch (InterruptedException e) { }
                return x.thenApply(f2::apply);
            })
            .forEach(x -> { try { p("%s\n", x.get()); } catch (Exception e) { } });

        p("done testCompletableFutureStream\n");
    }

    public void testInvokeAllCallablesEachLoop() {
        int sz = 5;
        int numLoops = 3;
        List<List<Future<String>>> llString = new ArrayList<>();

        for(int i = 0; i < numLoops; i++) {
            List<Callable<String>> listCallable = new ArrayList<>();
            for(int j = 0; j < sz; j++) {
                listCallable.add(new CallableObject());
            }
            try {
                List<Future<String>> listFuture = fixedThreadPoolGroup1.invokeAll(listCallable);
                llString.add(listFuture);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        long timeout = 50l;
        for(List<Future<String>> lf: llString) {
            p("beg list future\n");
            List<String> res = lf.stream().map(f -> subInvokeAllCallables(f, timeout)).collect(Collectors.toList());
            res.stream().forEach(s -> p("%s\n", s));
            p("end list future\n");
        }
    }

    public void testSubmitCallablesSeparateLoop() {
        int sz = 5;
        int numLoops = 3;
        List<List<Future<String>>> llString = new ArrayList<>();

        for(int i = 0; i < numLoops; i++) {
            List<Future<String>> listFuture = new ArrayList<>();
            for(int j = 0; j < sz; j++) {
                listFuture.add(fixedThreadPoolGroup1.submit(new CallableObject(500)));
            }
            llString.add(listFuture);
        }
        long timeout = 500l;
        for(List<Future<String>> lf: llString) {
            p("beg list future\n");
            List<String> res = lf.stream().map(f -> subInvokeAllCallables(f, timeout)).collect(Collectors.toList());
            res.stream().forEach(s -> p("%s\n", s));
            p("end list future\n");
        }
    }

    public void testSubmitCallablesCallbackWhenFinished(int numLoops) {
        int sz = 5;
        ConcurrentLinkedDeque<String> list = new ConcurrentLinkedDeque<>();
        Callback1 callback = new Callback1(list, sz*numLoops, numLoops-1, this);

        for(int i = 0; i < numLoops; i++) {
            for(int j = 0; j < sz; j++) {
                fixedThreadPoolGroup1.submit(new CallableObjectCallback(500, callback));
            }
        }
    }

    private String subInvokeAllCallables(Future<String> f, long timeout) {
        String s = null;
        try {
            s = f.get(timeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            return "timeoutexception";
        }
        return s;
    }

    public void test() {
        try {
            testCompletableFutureStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

interface ListenerOne<E> {
    public E notify(E e);
}

class BlockingQueueRegistered<E> extends LinkedBlockingDeque<E> {
    public BlockingQueueRegistered(int capacity) {
        super(capacity);
    }
    @Override public boolean add(E e) {
        boolean res = super.add(e);
        notifyAll();
        return res;
    }
    @Override public E poll() {
        E e = super.poll();
        notifyAll();
        return e;
    }
}

// class CallableObject<String> implements Callable<String> seems to be wrong. why?
class CallableObject implements Callable<String> {
    private static Utils utils = new Utils();
    private static AtomicInteger ctr = new AtomicInteger(0);
    String msg = "default";
    int id;
    int sleepTime;
    public CallableObject(int maxSleep) {
        this(ctr.getAndIncrement(), "defaultmsg", utils.getInt(1,maxSleep));
    }
    public CallableObject() {
        this(ctr.getAndIncrement(), "defaultmsg", utils.getInt(1,200));
    }
    public CallableObject(String msg) {
        this(ctr.getAndIncrement(), msg, utils.getInt(1,200));
    }
    public CallableObject(int id, String msg, int sleepTime) {
        this.id = id;
        this.msg = msg;
        this.sleepTime = sleepTime;
    }
    @Override public String call() throws Exception {
        try { Thread.sleep(sleepTime); } catch(InterruptedException e) { }
        return String.format("id:%-3d sleep:%-3d %s", id, sleepTime, msg);
    }
}

class CallableObjectCallback extends CallableObject {
    org.wayne.java.Callback callback;
    public CallableObjectCallback(int maxSleep, org.wayne.java.Callback callback) {
        super(maxSleep);
        this.callback = callback;
    }
    @Override public String call() throws Exception {
        String res = super.call();
        callback.setMsg(res);
        return res;
    }
}

class CallableObjectType2 implements Callable<MyContainer<?>> {
    private static Utils utils = new Utils();
    private static AtomicInteger ctr = new AtomicInteger(0);
    String msg = "default";
    int id;
    int sleepTime;
    MyContainer<?> container;

    public CallableObjectType2(MyContainer<?> container, int sleepTime) {
        this.id = ctr.getAndIncrement();
        this.msg = "CallableObjectType2 " + sleepTime;
        this.container = container;
        this.sleepTime = sleepTime;
    }

    public CallableObjectType2(MyContainer<?> container) {
        this(container, utils.getInt(1,200));
    }

    @Override
    public MyContainer<?> call() throws Exception {
        try { Thread.sleep(sleepTime); } catch(InterruptedException e) { }
        return container;
    }
}

class ScheduledList extends LinkedList<Callable<?>> {
    ScheduledExecutorService scheduledService;
    int delayInterval;
    public ScheduledList(ScheduledExecutorService scheduledService, int delayInterval) {
        super();
        this.scheduledService = scheduledService;
        this.delayInterval = delayInterval;
    }
    public Future<?> addCallable(Callable<?> c) {
        super.add(c);
        return scheduledService.schedule(c,delayInterval,TimeUnit.MILLISECONDS);
    }
}

class MyThreadFactory implements ThreadFactory {
    String groupName;
    final AtomicInteger ctr = new AtomicInteger(0);

    public MyThreadFactory(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r, String.format("group-%s-thread-%02d", groupName, ctr.getAndIncrement()));
        return t;
    }
}

class Callback1 implements org.wayne.java.Callback<Collection<String>> {
    ConcurrentLinkedDeque<String> list;
    AtomicInteger ctr = new AtomicInteger(0);
    String msg;
    int max;
    int numLoops;
    MyCallable mycallable;
    public Callback1(ConcurrentLinkedDeque<String> list, int max, int numLoops, MyCallable mycallable) {
        this.list = list;
        this.max = max;
        this.numLoops = numLoops;
        this.mycallable = mycallable;
    }

    @Override
    public Collection<String> get() {
        return list;
    }

    @Override
    public synchronized void setMsg(String msg) {
        this.msg = msg;
        list.add(msg);
        ctr.getAndIncrement();
        if(ctr.get() == max) {
            complete();
        }
    }

    @Override
    public String getMsg() {
        return msg;
    }

    @Override
    public void complete() {
        MyCallable.p("beg list future\n");
        List<String> res = list.stream().collect(Collectors.toList());
        res.stream().forEach(s -> MyCallable.p("%s\n", s));
        MyCallable.p("end list future\n");
        mycallable.testSubmitCallablesCallbackWhenFinished(numLoops);
    }
}

