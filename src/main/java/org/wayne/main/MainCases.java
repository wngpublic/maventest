package org.wayne.main;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.wayne.algos.MyAlgos;
import org.wayne.db.MySQLClasses;
import org.wayne.distributed.Java8Stuff;
import org.wayne.distributed.MyThreads;
import org.wayne.external.MyExternalLibsJackson30;
import org.wayne.external.MyHttpClient;
import org.wayne.external.MyRedisClient;
import org.wayne.io.MyNIO;
import org.wayne.io.MyNetwork;
import org.wayne.io.logging.package1.LoggerCounterBase;
import org.wayne.io.logging.package1.subpackage1.LoggerCounterType1;
import org.wayne.io.logging.package1.subpackage1.LoggerCounterType2;
import org.wayne.io.logging.package1.subpackage2.LoggerCounterType3;
import org.wayne.io.logging.package1.subpackage2.LoggerCounterType4;
import org.wayne.java.GarbageCollection;
import org.wayne.java.MyCallable;
import org.wayne.java.MyLambda;
import org.wayne.java.MyPerformance;
import org.wayne.java.MySyntax;
import org.wayne.misc.Utils;

public class MainCases {
    Logger logger = LogManager.getLogger(getClass().getEnclosingClass());

    Utils u = new Utils();
    List<Method> methods = new ArrayList<>();
    Map<String, Method> map = new HashMap<>();
    
    public MainCases() {
        init();
        getMethods();
    }

    public void startCommandProcessor(String [] args) {
        List<Class> listClasses = Arrays.asList(
            MyCallable.class,
            MyLambda.class,
            MySyntax.class,
            MyPerformance.class,
            MyExternalLibsJackson30.class,
            MyNetwork.class,
            MyNIO.class,
            MySQLClasses.class,
            MyAlgos.class,
            MyThreads.class,
            MyHttpClient.class,
            MyRedisClient.class,
            Java8Stuff.class,
            Utils.class,
            MainSimulations.class,
            LoggerCounterBase.class,
            LoggerCounterType1.class,
            LoggerCounterType2.class,
            LoggerCounterType3.class,
            LoggerCounterType4.class
        );
        CommandProcessor commandProcessor = new CommandProcessor(listClasses);
        /*
         * shutdown hook has to be called BEFORE thread start!
         * if called AFTER, then a cancel does not initiate shutdownhook!
         * How to register a thread to a shutdown hook though?
         *
         * shutdown hooks apply to daemon threads, which immediately get
         * terminated. The shutdown hook should have a register for each
         * thread. Upon shutdown invocation, call each registered thread's
         * stop method, and then this hook can either way or sleep. The
         * wait is until all threads signal with a field that it is done.
         */
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                System.out.printf("\nshutdown hook start\n");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.printf("\nshutdown hook end\n");
            }
        });
        commandProcessor.start(args);
        //new CommandProcessor(listClasses).start(args);
    }

    private void registerShutdownHook(Thread t) {

    }

    public void start(String [] args) {
        long timebegin = System.nanoTime();
        try {
            if(args.length > 0) {
                test(args);
            }
            else {
                printMethods();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        long timeend = System.nanoTime();
        long timediff = timeend - timebegin;
        long timediffmilli = timediff / 1000000;
        p("\n");
        p("time elapse millis:%d\n", timediffmilli);
        
    }
    
    private void test(String [] args) throws 
        IllegalAccessException, 
        IllegalArgumentException, 
        InvocationTargetException 
    {
        String arg0 = args[0];
        
        if(arg0.matches("^\\d+$")) {
            Integer i = Integer.parseInt(args[0]);
            if(i >= methods.size()) {
                printMethods();
            } else {
                Method m = methods.get(i);
                m.invoke(this);
            }
        }
        else {
            if(map.containsKey(arg0)) {
                Method m = map.get(arg0);
                m.invoke(this);
            } else {
                printMethods();
            }
        }
    }

    private static void p(String f, Object ...o) {
        System.out.printf(f, o);
    }
    
    private void getMethods() {
        Method [] tmpM = this.getClass().getMethods();
        
        // sort the list
        Map<String, Method> treemap = new TreeMap<>();
        
        for(int i = 0; i < tmpM.length; i++) {
            Method method = tmpM[i];
            String methodName = method.getName();
            
            if(tmpM[i].getName().matches("^t\\d+")) {
                if(tmpM[i].getModifiers() == Modifier.PUBLIC) {
                    treemap.put(methodName, method);
                    map.put(methodName, method);
                }
            }
        }
        
        for(Map.Entry<String, Method> kv: treemap.entrySet()) {
            Method m = kv.getValue();
            methods.add(m);
        }

    }

    private void printMethods() {
        for(int i = 0; i < methods.size(); i++) {
            p("%3d: %s\n", i, methods.get(i).getName());
        }
    }
    
    private void init() {
        
    }

    public void t16() {
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

}

