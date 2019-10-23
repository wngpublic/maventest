package org.wayne.java;

import junit.framework.TestCase;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.UnknownFormatConversionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.mockito.cglib.core.Local;

import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.function.Supplier;

public class Java8Test extends TestCase {
    Logger logger = LogManager.getLogger(getClass().getEnclosingClass());

    //Logger logger = Logger.getLogger(getClass().getName());
    Random r = new Random();
    void p(String f, Object ...o) {
        System.out.printf(f, o);
    }
    @Test
    public void testLambda1() {
        MyLambda t = new MyLambda();
        t.testLambda2();
    }
    @Test
    public void testFunctionalLambda() {
        // test function
        {
            Function<Integer, Integer> f1 = x -> x + x;
            Function<Integer, Function<Integer,Integer>> f2 = x -> y -> x + y;
            BiFunction<Integer, Integer, Integer> bf1 = (x,y) -> x + y;
            Function<Integer, Stream<Integer>> fs1 = (x) -> Stream.iterate(1, y -> x + y + 1);   // y = x + y + 1
            assert f1.apply(5) == 10;
            assert f1.apply(8) == 16;
            assert f2.apply(3).apply(4) == 7;
            assert f2.apply(5).apply(6) == 11;
            assert bf1.apply(3,4) == 7;
            assert bf1.apply(5,6) == 11;
            List<Integer> l = fs1.apply(10).limit(5).collect(Collectors.toList());
            assert l.equals(Arrays.asList(1,12,23,34,45));

            // test predicate's test method. used for filtering when chained.
            boolean flag = false;
            Predicate<String> isMatchName = s -> s.equals("is_match");
            Predicate<Integer> isEvenInt = x -> x % 2 == 0;
            assert !isMatchName.test("no");
            assert isMatchName.test("is_match");
            assert isEvenInt.test(2);
            assert !isEvenInt.test(3);
            try {
                flag = false;
                assert !isMatchName.test(null);
            } catch(NullPointerException e) {
                flag = true;
            }
            assert flag;

            // function interface
            List<Integer> li = Arrays.asList(1,2,3,4,5);
            List<String> ls;

            Function<Integer,String> fis = x -> String.format("%d",x); // convert int to string
            assert fis.apply(1).equals("1");
            assert fis.apply(123).equals("123");
            ls = li.stream().map(x -> fis.apply(x)).collect(Collectors.toList());
            assert Arrays.asList("1","2","3","4","5").equals(ls);

            // using filter with stream
            ls = li.stream().filter(x -> isEvenInt.test(x)).map(x -> fis.apply(x)).collect(Collectors.toList());
            assert Arrays.asList("2","4").equals(ls);
            ls = li.stream().filter(isEvenInt).map(x -> fis.apply(x)).collect(Collectors.toList());
            assert Arrays.asList("2","4").equals(ls);
        }

        // using interface, which can only have 1 method
        {
            IFCSingleAnd ifcAnd = (x,y) -> x & y;
            IFCSingleXor ifcXor = (x,y) -> x ^ y;
            assert ifcAnd.and(0x0000cccc,0x88448844) == 0x8844;
            assert ifcXor.xor(0x0000cccc,0x88448844) == 0x88444488;
        }

        // test optional
        {
            Optional<String> o0 = Optional.empty();
            String name1 = null;
            String stringMememe = "mememe";
            StringBuilder stringBuilder = new StringBuilder();
            Optional<String> o1 = null;
            boolean flag = false;
            assert o1 == null;
            try {
                o1 = Optional.of(name1);
            } catch(NullPointerException e) {
                flag = true;
            }
            assert flag;
            try {
                o1 = Optional.of(null);
                flag = false;
            } catch(NullPointerException e) {
                flag = true;
            }
            assert flag;
            assert o1 == null;
            try {
                o1 = Optional.ofNullable(null);
                flag = true;
            } catch(NullPointerException e) {
                flag = false;
            }
            assert flag;
            assert o1 != null;
            String v = o1.isPresent() ? "ispresent" : "isnotpresent";
            assert "isnotpresent".equals(v);
            o1 = Optional.ofNullable("value");
            assert o1 != null;
            v = o1.isPresent() ? "ispresent" : "isnotpresent";
            assert "ispresent".equals(v);
            v = Optional.of("something").orElse("null");
            assert "something".equals(v);
            v = Optional.ofNullable("something").orElse("null");
            assert "something".equals(v);
            String v1 = null;
            v = Optional.ofNullable(v1).orElse("null");
            assert "null".equals(v);
            //assert o1.orElse("isnull").equals("isnull");
            AtomicInteger atomicInteger = new AtomicInteger(0);
            Optional<String> o2 = Optional.of(stringMememe);
            o1 = Optional.ofNullable(null);
            atomicInteger.set(0);
            o1.ifPresent(x -> atomicInteger.set(1));
            assert atomicInteger.get() == 0;
            stringBuilder.setLength(0);
            o1.ifPresent(x -> stringBuilder.append(x));
            assert stringBuilder.length() == 0;
            o2.ifPresent(x -> stringBuilder.append(x));
            assert stringBuilder.length() == stringMememe.length();

            o2.ifPresent(x -> atomicInteger.set(1));
            assert atomicInteger.get() == 1;

            v1 = o1.orElse("EMPTY");
            assert "EMPTY".equals(v1);
            v1 = Optional.ofNullable(name1).orElse("EMPTY");
            assert "EMPTY".equals(v1);
            v1 = Optional.ofNullable(name1).orElseGet(() -> getString("blank")); // must be empty supplier
            assert "blank".equals(v1);
            v1 = Optional.ofNullable(name1).map((x) -> getString(x)).orElse("blank");
            assert "blank".equals(v1);
            v1 = Optional.ofNullable("test").map((x) -> getString(x)).orElse("blank");
            assert "test".equals(v1);

            Optional<Integer> optionalInteger;
            Integer integerVal;

            optionalInteger = Optional.of(10);
            atomicInteger.set(0);
            optionalInteger.ifPresent(x -> atomicInteger.set(x));
            assert atomicInteger.get() == 10;

            optionalInteger = Optional.ofNullable(null);
            atomicInteger.set(1);
            optionalInteger.ifPresent(x -> atomicInteger.set(x));
            assert atomicInteger.get() == 1;

            try {
                flag = false;
                optionalInteger = Optional.of(null);    // this throws exception
            } catch(NullPointerException e) {
                flag = true;
            }
            assert flag == true;

            optionalInteger = Optional.empty();
            assert optionalInteger != null;
            assert !optionalInteger.isPresent();

            optionalInteger = Optional.ofNullable(null);
            integerVal = optionalInteger.map(i -> i.intValue()).orElse(null);
            assert integerVal == null;

            optionalInteger = Optional.of(10);
            assert optionalInteger.isPresent();
            optionalInteger.ifPresent(x -> { assert x.intValue() == 10; });
            assert optionalInteger.map(x -> x.intValue() == 10 ).orElse(false);

            Optional<Converter> optionalConverter1 = Optional.ofNullable(new Converter(1));
            Optional<Converter> optionalConverter2 = Optional.ofNullable(new Converter(2));
            Optional<Converter> optionalConverterN = Optional.ofNullable(new Converter(null));
            Optional<Converter> optionalConverterNull = Optional.ofNullable(null);
            Optional<Converter> optionalConverterEmpty = Optional.empty();

            integerVal = optionalConverter1.map(x -> x.getInteger(10)).orElse(100);
            assert integerVal != null && integerVal.intValue() == 10;
            integerVal = optionalConverterNull.map(x -> x.getInteger(10)).orElse(100);
            assert integerVal != null && integerVal.intValue() == 100;

            try {
                flag = false;
                integerVal = null;
                integerVal = Optional.of(integerVal).orElse(100);
            } catch(NullPointerException e) {
                flag = true;
            }
            assert flag == true;

            integerVal = Optional.ofNullable(integerVal).orElse(100);
            assert integerVal != null && integerVal.intValue() == 100;

            try {
                flag = false;
                integerVal = null;
                integerVal = Optional.of(integerVal).map(x -> x).orElse(10);
            } catch(NullPointerException e) {
                flag = true;
            }
            assert flag == true;

            integerVal = null;
            integerVal = Optional.ofNullable(integerVal).map(x -> x).orElse(10);
            assert integerVal != null && integerVal.intValue() == 10;

            try {
                flag = false;
                integerVal = null;
                integerVal = Optional.ofNullable(integerVal).map(x -> x).orElseThrow(NullPointerException::new);
            } catch(NullPointerException e) {
                flag = true;
            }
            assert flag == true;
        }

        // test supplier
        {
            {
                // supplier cannot pass in arg.
                Random random = new Random();
                Supplier<Integer> supplierInt = () -> random.nextInt(100);
                Integer intValue;
                intValue = supplierInt.get();
                assert intValue >= 0 && intValue <= 100;
                intValue = supplierInt.get();
                assert intValue >= 0 && intValue <= 100;
            }
            {
                // pass supplier to function. but change the supplier.
                List<Integer> list1 = Arrays.asList(1,2,3,4);
                List<Integer> list2 = Arrays.asList(11,12,13,14);
                int sum1 = sum(() -> loadListInt(10,15));
                int sum2 = sum(() -> loadListInt(15,20));
                assert sum1 != sum2;
                Function<List<Integer>,Integer> fsum = this::sum;
                assert sum(() -> list1) == fsum.apply(list1);
                assert sum(() -> list2) == fsum.apply(list2);

                Supplier<List<Integer>> supplier;
                supplier = () -> loadListInt(10,15);
                Function<Supplier<List<Integer>>,Integer> fSupplierSum = this::sum;
                assert fSupplierSum.apply(() -> loadListInt(10,15)) == sum(() -> loadListInt(10,15));
                assert fSupplierSum.apply(supplier) == fSupplierSum.apply(() -> loadListInt(10,15));
            }
            {
                SupplierTestObject sto = new SupplierTestObject(3);
                Supplier<List<Integer>> supplier = () -> sto.supply();
                assert sum(supplier) == (0+1+2);
                assert sum(supplier) == (3+4+5);
                assert sum(supplier) == (6+7+8);
                sto.setCurrentCount(0);
                assert sum(supplier) == (0+1+2);
            }
        }


        // test consumer
        {
            Converter converter = new Converter(0);

            Consumer<String> consumer1 = x -> converter.setStoredString(x);
            Supplier<String> supplier1 = () -> converter.getStoredString();
            consumer1.accept("mememe");
            assert "mememe".equals(supplier1.get());
            assert "mememe".equals(converter.getStoredString());

            Set<String> set1 = new HashSet<>();
            Consumer<String> consumer2 = x -> set1.add(x);
            consumer2.accept("aa");
            consumer2.accept("bb");
            consumer2.accept("cc");
            assert set1.size() == 3 && set1.containsAll(Arrays.asList("aa","bb","cc"));
            BiConsumer<String,String> biconsumer1 = (x,y) -> p("my name is %s %s\n", x, y);
            biconsumer1.accept("first","last");
            set1.clear();
            biconsumer1 = (x,y) -> { set1.add(x); set1.add(y); };
            biconsumer1.accept("aa","bb");
            biconsumer1.accept("aa","cc");
            biconsumer1.accept("dd","ee");
            assert set1.size() == 5 && set1.containsAll(Arrays.asList("aa","bb","cc","dd","ee"));
        }

        // test supplier and consumer params
        {
            Converter converter1 = new Converter(1);
            converter1.setEnableContinuousQueue(true);

            Supplier<Integer> supplier0 = ()  -> converter1.getIntegerFromQueue();
            Consumer<Integer> consumer0 = (x) -> converter1.storeIntegerToQueue(x);

            Consumer<Supplier<Integer>> consumer1 = (x) -> converter1.setSupplierInteger(x);
            Consumer<Consumer<Integer>> consumer2 = (x) -> converter1.setConsumerInteger(x);
            Supplier<Consumer<Integer>> supplier1 = ()  -> converter1.getConsumerInteger();
            Supplier<Supplier<Integer>> supplier2 = ()  -> converter1.getSupplierInteger();

            consumer1.accept(supplier0);
            consumer2.accept(consumer0);
            Consumer<Integer> consumerOut = supplier1.get();
            Supplier<Integer> supplierOut = supplier2.get();

            consumer0.accept(10);
            Integer integerVal = supplierOut.get();
            assert integerVal == 10;
            consumerOut.accept(integerVal + 10);
            assert supplierOut.get() == 20;
        }

        // test supplier from obj and consumer to obj
        {
            Converter converter1 = new Converter(1);
            Converter converter2 = new Converter(2);
            converter1.setEnableContinuousQueue(true);
            Supplier<Integer> supplier1 = converter1::getIntegerFromQueue;
            Consumer<Integer> consumer1 = converter2::storeIntegerToQueue;

            for(int i = 0; i < 10; i++)
                assert supplier1.get() == i;

            converter1.reset();
            for(int i = 0; i < 10; i++)
                consumer1.accept(supplier1.get());
            for(int i = 0; i < 10; i++)
                assert supplier1.get() == i+10;
        }

        // flatmap vs map stream
        {
            Map<Integer,Integer> mapII = new HashMap<>();
            Map<Integer,Map<Integer,Integer>> mapMapII = new HashMap<>();
            List<Integer> listI = new ArrayList<>();
            List<List<Integer>> listListI = new ArrayList<>();

            // initialize
            {
                int factor = 4;
                for(int i = 0; i < factor; i++) {
                    mapII.put(i,i+100);
                    listI.add(i);

                    mapMapII.put(i, new HashMap<>());
                    listListI.add(new ArrayList<>());

                    for(int j = 0; j < factor; j++) {
                        int base = i*factor+j;
                        mapMapII.get(i).put(j,base+100);
                        listListI.get(i).add(base);
                    }
                }
            }

            // map of map to flatmap;  list to flatmap; list of list to flatmap
            {
                List<Integer> resultList;
                resultList = listI.stream().collect(Collectors.toList());
                assert Arrays.asList(0,1,2,3).equals(resultList);

                // map unnecessary as it is already flat and no transform needed, cannot use flatmap
                resultList = listI.stream().map(x -> x).collect(Collectors.toList());
                assert Arrays.asList(0,1,2,3).equals(resultList);

                // flatmap needed because list of list
                resultList = listListI.stream().flatMap(x -> x.stream()).collect(Collectors.toList());
                assert Arrays.asList(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15).equals(resultList);

                // map not needed because no transform, stream each subsection
                resultList = listListI.stream().map(x -> x).flatMap(x -> x.stream()).collect(Collectors.toList());
                assert Arrays.asList(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15).equals(resultList);

                // map needed because of transform
                resultList = listListI.stream().flatMap(x -> x.stream()).map(x -> x+1).collect(Collectors.toList());
                assert Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16).equals(resultList);

                resultList = mapII.values().stream().collect(Collectors.toList());
                assert Arrays.asList(100,101,102,103).equals(resultList);

                resultList = mapMapII.values().stream().flatMap(x -> x.values().stream()).collect(Collectors.toList());
                assert Arrays.asList(100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115)
                    .equals(resultList);

                // filter out odds
                Predicate<Integer> predicate = x -> x % 2 == 0;
                resultList = listListI.stream().flatMap(x -> x.stream()).filter(predicate).collect(Collectors.toList());
                assert Arrays.asList(0,2,4,6,8,10,12,14).equals(resultList);
            }
        }
        p("passed testFunctionalLambda\n");
    }

    @Test
    public void testOptional() {
        {
            Map<Integer,Integer> map = new HashMap<>();
            map.put(1,11);
            map.put(2,12);
            map.put(3,13);
            Integer i;
            Integer i1 = null;
            Integer i2 = null;
            Integer i3 = 3;
            Integer i4 = 4;
            Integer o1 = null;

            Optional<Integer> oi1 = Optional.ofNullable(null);
            boolean expected;

            expected = false;
            try {
                oi1 = Optional.of(i1);
                expected = false;
            } catch(NullPointerException e) {
                expected = true;
            }
            assert expected;

            try {
                i1 = null;
                i3 = 3;
                oi1 = Optional.ofNullable(i1);
                assert oi1.isPresent() == false;
                oi1 = Optional.ofNullable(i3);
                assert oi1.isPresent() == true;
                expected = true;
            } catch(NullPointerException e) {
                expected = false;
            }
            assert expected;

            try {
                o1 = Optional.ofNullable(i1).orElseGet(() -> 1);    // orElse is never Optional
                assert o1 == 1;
                o1 = Optional.ofNullable(i1).orElse(1);
                assert o1 == 1;
                o1 = Optional.ofNullable(map.get(10)).orElse(100);
                assert o1 == 100;
                o1 = Optional.ofNullable(map.get(1)).orElse(20); // 1->11
                assert o1 == 11;
                o1 = Optional.ofNullable(map.get(10)).orElse(map.get(2)); // 10->null,2->12
                assert o1 == 12;
                o1 = Optional.ofNullable(map.get(10)).orElseGet(() -> map.get(2)); // 10->null,2->12
                assert o1 == 12;
                expected = true;
            } catch(NullPointerException e) {
                expected = false;
            }
            assert expected;

            map.clear();
            map.put(1,2);
            map.put(2,3);
            map.put(3,4);
            map.put(4,5);
            o1 = Optional.ofNullable(map.get(2)).map(x -> map.get(x)).map(x -> map.get(x)).orElse(10);
            assert o1 == 5;
            o1 = Optional.ofNullable(map.get(3)).map(x -> map.get(x)).map(x -> map.get(x)).orElse(10);
            assert o1 == 10;
            // 4->5, 5->null, null->map.get(null)
            o1 = Optional.ofNullable(map.get(4)).map(x -> map.get(x)).map(x -> map.get(x)).orElse(10);
            assert o1 == 10;

            Map<Integer,Map<Integer,Integer>> map2 = new HashMap<>();
            Map<Integer,Map<Integer,Integer>> map3 = new HashMap<>();
            Map<Integer,Integer> map4 = new HashMap<>();

            oi1 = Optional.ofNullable(null);
            Optional<Integer> oi2 = Optional.ofNullable(null);
            Optional<Integer> oi3 = Optional.ofNullable(null);
            Optional<Integer> oi4 = Optional.ofNullable(4);
            Optional<Integer> oi5 = Optional.ofNullable(5);
            Optional<Integer> oi6 = null;

            oi6 = Stream.of(oi1,oi2,oi3,oi4,oi5).filter(x -> x.isPresent()).map(x -> x.get()).findFirst();
            assert oi6.isPresent();
            assert oi6.get() == 4;

            long long1 = Stream.of(i1,i2,i3,i4).count();
            assert long1 == 4;

            List<Integer> l1 = Arrays.asList(null,null,2,null,4,null,6);
            assert l1.size() == 7;
            List<Optional<Integer>> loi1 = l1.stream().map(x -> Optional.ofNullable(x)).collect(Collectors.toList());
            oi1 = loi1.stream().filter(x -> x.isPresent()).map(x -> x.get()).findFirst();
            assert oi1.isPresent() && oi1.get() == 2;

            l1 = Arrays.asList(null,null,null);
            loi1 = l1.stream().map(x -> Optional.ofNullable(x)).collect(Collectors.toList());
            oi1 = loi1.stream().filter(x -> x.isPresent()).map(x -> x.get()).findFirst();
            assert oi1.isPresent() == false;
            assert oi1.orElse(null) == null;
            assert oi1.orElse(1) == 1;
            //i = Optional.of(map.get(10)).orElseGet(map.get(8)).orElseGet(map.get(3));
            //i = Stream.of(10,8,3).map(x -> Optional.ofNullable(map.get(x))).filter(x -> x.isPresent()).map(x -> x.get()).findFirst();
        }
        {
            // object and optional
            LocalObject lo1 = new LocalObject(1,2);
            LocalObject lo2 = null;
            Optional<LocalObject> olo1 = Optional.ofNullable(lo1);
            Optional<LocalObject> olo2 = Optional.ofNullable(lo2);
            Integer i1 = olo1.map(o -> o.getI1()).orElse(null);
            Integer i2 = olo2.map(o -> o.getI1()).orElse(null);
            assert i1 == 1;
            assert i2 == null;

            List<LocalObject> llo = new ArrayList<>();
            llo.add(null);
            llo.add(new LocalObject(1,2));
            llo.add(null);
            llo.add(new LocalObject(3,4));
            llo.add(null);
            llo.add(new LocalObject(5,6));
            llo.add(new LocalObject(6,7));
            llo.add(null);
            llo.add(new LocalObject(7,8));


            //Optional.ofNullable(olo2).ifPresent(x -> i2 = x.get().getI1());
            //Integer i2 = Optional.ofNullable(olo2.get()).map(x -> x.getI1()).orElse(null);//Optional.ofNullable(olo2.get().getI1()).orElseGet(() ->null);
        }
        {
            Optional<Integer> oi1 = Optional.empty();
            oi1 = null;                                     // never set as null, should be Optional.empty()
        }
        {
            {
                LocalO1 lo1 = new LocalO1(new LocalO2(new LocalO3(new LocalO4(1))));
                Integer i;
                i = null;
                if(lo1.geto2() != null) {
                    if(lo1.geto2().geto3() != null) {
                        if(lo1.geto2().geto3().geto4() != null) {
                            i = lo1.geto2().geto3().geto4().geto5();
                        }
                    }
                }
                assert i == 1;

                i = Optional.ofNullable(lo1)
                    .flatMap(o -> Optional.ofNullable(o.geto2()))
                    .flatMap(o -> Optional.ofNullable(o.geto3()))
                    .flatMap(o -> Optional.ofNullable(o.geto4()))
                    .flatMap(o -> Optional.ofNullable(o.geto5()))
                    .orElse(100);
                assert i == 1;
            }
            {
                LocalO1 lo1 = new LocalO1(new LocalO2(null));
                Integer i = Optional.ofNullable(lo1)
                    .flatMap(o -> Optional.ofNullable(o.geto2()))
                    .flatMap(o -> Optional.ofNullable(o.geto3()))
                    .flatMap(o -> Optional.ofNullable(o.geto4()))
                    .flatMap(o -> Optional.ofNullable(o.geto5()))
                    .orElse(100);
                assert i == 100;
            }
            {
                LocalO4 o1 = null;
                LocalO4 o2 = null;
                LocalO4 o3 = new LocalO4(3);
                LocalO4 o4 = null;
                LocalO4 o5 = new LocalO4(5);
                Integer i = Stream
                    .of(o1,o2,o3,o4,o5)
                    .filter(o -> o != null)
                    .map(o -> o.geto5())
                    .findFirst()
                    .orElse(100);
                assert i == 3;
                i = Stream
                    .of(o1,o2,o3,o4,o5)
                    .filter(o -> o != null)
                    .findFirst()
                    .map(o -> o.geto5())
                    .orElse(100);
                assert i == 3;
                i = Stream
                    .of(o1,o2,o4)
                    .filter(o -> o != null)
                    .findFirst()
                    .map(o -> o.geto5())
                    .orElse(null);
                assert i == null;
                Optional<Integer> oi;
                oi = Stream
                    .of(o1,o2,o3,o4,o5)
                    .filter(o -> o != null)
                    .findFirst()
                    .map(o -> Optional.ofNullable(o.geto5()))
                    .orElse(Optional.ofNullable(100));
                assert oi.isPresent();
                assert oi.get() == 3;
                oi = Stream
                    .of(o1,o2,o4)
                    .filter(o -> o != null)
                    .findFirst()
                    .map(o -> Optional.ofNullable(o.geto5()))
                    .orElse(Optional.ofNullable(100));
                assert oi.isPresent();
                assert oi.get() == 100;
                Optional<LocalO4> olo;

                olo = Optional
                    .ofNullable(new LocalO2(new LocalO3(new LocalO4(1))))
                    .filter(o -> o != null)
                    .flatMap(o -> Optional.ofNullable(o.geto3()))
                    .map(o -> Optional.ofNullable(o.geto4()))
                    .orElse(Optional.ofNullable(new LocalO4(10)));
                i = (olo.isPresent()) ? olo.get().geto5() : 20;
                assert i == 1;

                i = Optional
                    .ofNullable(new LocalO2(new LocalO3(null)))
                    .flatMap(o -> Optional.ofNullable(o.geto3()))
                    .map(o -> o.geto4())
                    .map(o -> o.geto5())
                    .orElse(10);
                assert i == 10;

                i = Optional
                    .ofNullable(new LocalO2(null))
                    .map(o -> o.geto3())
                    .map(o -> o.geto4())
                    .map(o -> o.geto5())
                    .orElse(10);
                assert i == 10;

                i = Optional
                    .ofNullable(new LocalO2(new LocalO3(new LocalO4(null))))
                    .flatMap(o -> Optional.ofNullable(o.geto3()))
                    .map(o -> o.geto4())
                    .map(o -> o.geto5())
                    .orElse(10);
                assert i == 10;

                oi = Optional
                    .ofNullable(new LocalO2(new LocalO3(new LocalO4(null))))
                    .flatMap(o -> Optional.ofNullable(o.geto3()))
                    .map(o -> o.geto4())
                    .map(o -> Optional.ofNullable(o.geto5()))
                    .orElse(Optional.ofNullable(10));
                assert !oi.isPresent();

                i = Optional
                    .ofNullable(new LocalO2(new LocalO3(new LocalO4(null))))
                    .flatMap(o -> Optional.ofNullable(o.geto3()))
                    .map(o -> o.geto4())
                    .map(o -> o.geto5()) // this is null
                    .orElse(10);
                assert i == 10;

                oi = Optional
                    .ofNullable(new LocalO2(new LocalO3(new LocalO4(null))))
                    .flatMap(o -> Optional.ofNullable(o.geto3()))
                    .map(o -> o.geto4())
                    .map(o -> Optional.ofNullable(o.geto5())) // this is wrapped
                    .orElse(Optional.ofNullable(10));
                assert !oi.isPresent(); // this is ofNullable(null)

                oi = Optional
                    .ofNullable(new LocalO2(new LocalO3(new LocalO4(null))))
                    .flatMap(o -> Optional.ofNullable(o.geto3()))
                    .map(o -> o.geto4())
                    .map(o -> o.geto5())
                    .map(o -> Optional.ofNullable(o)) // never gets here because o is null
                    .orElse(Optional.ofNullable(10));
                assert oi.isPresent();
                assert oi.get() == 10;

                i = Optional
                    .ofNullable(new LocalO2(new LocalO3(new LocalO4(1))))
                    .flatMap(o -> Optional.ofNullable(o.geto3()))
                    .map(o -> o.geto4())
                    .map(o -> o.geto5())
                    .orElse(10);
                assert i == 1;

                olo = Optional
                    .ofNullable(new LocalO2(new LocalO3(null)))
                    //.filter(o -> o != null)
                    .flatMap(o -> Optional.ofNullable(o.geto3()))
                    //.flatMap(o -> o.geto4())
                    //.flatMap(o -> Optional.ofNullable(o.geto4()))
                    .map(o -> Optional.ofNullable(o.geto4()))
                    .orElse(Optional.ofNullable(new LocalO4(10))); // this is never reached!!!
                    //.orElse(Optional.ofNullable(new LocalO4(10)));
                i = (olo.isPresent()) ? olo.get().geto5() : 20;
                assert i == 20;

                olo = Optional
                    .ofNullable(new LocalO2(new LocalO3(null)))
                    .filter(o -> o != null)
                    .flatMap(o -> Optional.ofNullable(o.geto3()))
                    .map(o -> Optional.ofNullable(o.geto4()))
                    .orElse(Optional.ofNullable(null));
                i = (olo.isPresent()) ? olo.get().geto5() : 20;
                assert i == 20;

            }
            {
                Integer i1 = null;
                Integer i2 = null;
                Integer i3 = 3;
                Integer i4 = null;
                Integer i5 = 5;
                Integer i = Stream
                    .of(i1,i2,i3,i4,i5)
                    .filter(v -> v != null)
                    .findFirst()
                    .orElse(100);
                assert i == 3;
                i = Stream
                    .of(i1,i2,i4)
                    .filter(v -> v != null)
                    .findFirst()
                    .orElse(100);
                assert i == 100;
                i = Stream
                    .of(i1,i2,i4)
                    .filter(v -> v != null)
                    .findFirst()
                    .orElse(null);
                assert i == null;
            }
            {
                LocalO4 lo41 = null;
                LocalO4 lo42 = null;
                LocalO4 lo43 = new LocalO4(10);
                LocalO4 lo44 = null;
                LocalO4 lo45 = new LocalO4(30);

                LocalO3 lo31 = new LocalO3(lo41);
                LocalO3 lo32 = null;
                LocalO3 lo33 = new LocalO3(lo43);
                LocalO3 lo34 = new LocalO3(null);
                LocalO3 lo35 = new LocalO3(lo45);

                List<LocalO3> ll = Arrays.asList(lo31,lo32,lo33,lo34,lo35);

                boolean expected = false;
                List<Integer> li;
                List<LocalO3> lo3;
                List<LocalO4> lo4;

                try {
                    li = ll.stream()
                        .map(localo3 -> localo3.geto4())
                        .map(localo4 -> localo4.geto5())
                        .collect(Collectors.toList());
                    assert li.size() == 2;
                    expected = false;
                } catch(NullPointerException e) {
                    expected = true;
                }
                assert expected;

                try {
                    List<Optional<LocalO3>> loo3 = ll.stream()
                        .map(localo3 -> Optional.ofNullable(localo3))
                        .collect(Collectors.toList());
                    assert loo3.size() == 5;
                    List<Optional<LocalO4>> loo4 = loo3.stream()
                        .filter(o -> o.isPresent())
                        .map(o -> Optional.ofNullable(o.get().geto4()))
                        .collect(Collectors.toList());
                    assert loo4.size() == 4;
                    List<Integer> li5 = loo4.stream()
                        .filter(o -> o.isPresent())
                        .map(o -> o.get().geto5())
                        .collect(Collectors.toList());
                    Set<Integer> expectedValues = new HashSet<>(Arrays.asList(10,30));
                    assert li5.size() == 2;
                    assert expectedValues.containsAll(li5);


                    // combined, with error cases
                    try {
                        li5 = ll.stream()
                            .map(o -> Optional.ofNullable(o))
                            .map(o -> Optional.ofNullable(o.get().geto4())) // this will cause exception because access null
                            .filter(o -> o.isPresent())
                            .map(o -> Optional.ofNullable(o.get().geto5()))
                            .filter(o -> o.isPresent())
                            .map(o -> o.get())
                            .collect(Collectors.toList());
                        expected = false;
                    } catch(NoSuchElementException e) {
                        expected = true;
                    }
                    assert expected;

                    // correct
                    // MUST filter optionals to process, else get NoSuchElementException!
                    li5 = ll.stream()
                        .map(o -> Optional.ofNullable(o))
                        .filter(o -> o.isPresent())
                        .map(o -> Optional.ofNullable(o.get().geto4()))
                        .filter(o -> o.isPresent())
                        .map(o -> Optional.ofNullable(o.get().geto5()))
                        .filter(o -> o.isPresent())
                        .map(o -> o.get())
                        .collect(Collectors.toList());
                    assert li5.size() == 2;
                    assert expectedValues.containsAll(li5);

                    expected = true;
                } catch(NullPointerException e) {
                    expected = false;
                }
                assert expected;

                try {
                    li = ll.stream()
                        .map(localo3 -> Optional.ofNullable(localo3))
                        .map(localo3 -> localo3.get().geto4())
                        .map(localo4 -> Optional.ofNullable(localo4))
                        .map(localo4 -> localo4.get().geto5())
                        .map(i -> Optional.ofNullable(i))
                        .map(i -> i.get().intValue())
                        .collect(Collectors.toList());
                    assert li.size() == 2;
                    expected = false;
                } catch(NoSuchElementException e) {
                    expected = true;
                }
                assert expected;

                // old way of doing
                {
                    li = new ArrayList<>();
                    for(LocalO3 t: ll) {
                        if(t == null) continue;
                        LocalO4 t1 = t.geto4();
                        if(t1 == null) continue;
                        Integer t2 = t1.geto5();
                        if(t2 == null) continue;
                        li.add(t2);
                    }
                    assert li.size() == 2;
                }

                {
                    lo43 = new LocalO4(null);
                    lo33 = new LocalO3(lo43);
                    Integer i = Optional.ofNullable(lo33)
                        .map(o -> o.geto4().geto5())
                        .orElse(100);
                    assert i == 100;

                    try {
                        i = Optional
                            .ofNullable(new LocalO2(new LocalO3(null)))
                            .map(o -> o.geto3().geto4().geto5())
                            .orElse(200);
                        assert i == 200;
                        expected = false;
                    } catch(NullPointerException e) {
                        expected = true;
                    }
                    assert expected;

                    try {
                        LocalO2 t = new LocalO2(new LocalO3(null));
                        i = t != null ?
                            t.geto3() != null ?
                                t.geto3().geto4() != null ?
                                    t.geto3().geto4().geto5() :
                                    200
                                : 300
                            : 400;
                        assert i == 200;

                        i = Optional
                            .ofNullable(t)
                            .map(o -> o.geto3())
                            .map(o -> o.geto4())
                            .map(o -> o.geto5())
                            .orElse(200);
                        assert i == 200;

                        t = new LocalO2(null);
                        i = t != null ?
                            t.geto3() != null ?
                                t.geto3().geto4() != null ?
                                    t.geto3().geto4().geto5() :
                                    200
                                : 300
                            : 400;
                        assert i == 300;

                        i = Optional
                            .ofNullable(t)
                            .map(o -> o.geto3())
                            .map(o -> o.geto4())
                            .map(o -> o.geto5())
                            .orElse(200);
                        assert i == 200;

                        // but this will throw exception! so do single map unwrap at a time!
                        try {
                            i = Optional
                                .ofNullable(t)
                                .map(o -> o.geto3().geto4().geto5())
                                .orElse(200);
                            assert i == 200;
                            expected = false;
                        } catch(NullPointerException e) {
                            expected = true;
                        }
                        assert expected;

                        i = Optional
                            .ofNullable(new LocalO1(null))
                            .map(o -> o.geto2())
                            .map(o -> o.geto3())
                            .map(o -> o.geto4())
                            .map(o -> o.geto5())
                            .orElse(100);
                        assert i == 100;

                        i = Optional
                            .ofNullable(new LocalO1(new LocalO2(null)))
                            .map(o -> o.geto2())
                            .map(o -> o.geto3())
                            .map(o -> o.geto4())
                            .map(o -> o.geto5())
                            .orElse(200);
                        assert i == 200;

                        i = Optional
                            .ofNullable(new LocalO1(new LocalO2(new LocalO3(null))))
                            .map(o -> o.geto2())
                            .map(o -> o.geto3())
                            .map(o -> o.geto4())
                            .map(o -> o.geto5())
                            .orElse(300);
                        assert i == 300;

                        i = Optional
                            .ofNullable(new LocalO1(new LocalO2(new LocalO3(new LocalO4(null)))))
                            .map(o -> o.geto2())
                            .map(o -> o.geto3())
                            .map(o -> o.geto4())
                            .map(o -> o.geto5())
                            .orElse(400);
                        assert i == 400;

                        i = Optional
                            .ofNullable(new LocalO1(new LocalO2(new LocalO3(new LocalO4(20)))))
                            .map(o -> o.geto2())
                            .map(o -> o.geto3())
                            .map(o -> o.geto4())
                            .map(o -> o.geto5())
                            .orElse(500);
                        assert i == 20;

                        Integer ii;
                        ii = Stream.of(new LocalO4(null), new LocalO4(null))
                            .filter(x -> x != null)
                            .findFirst()
                            .map(x -> x.geto5())
                            .orElse(null);
                        assert ii == null;

                        try {
                            ii = Stream.of(new LocalO4(null), new LocalO4(10))
                                .filter(x -> x != null)
                                .findFirst()
                                .map(x -> x.geto5())// having no filter will fault
                                .orElse(null);
                            assert ii == 10;
                            expected = false;
                        } catch(Exception e) {
                            expected = true;
                        }
                        assert expected;

                        ii = Stream.of(new LocalO4(null), new LocalO4(10))
                            .filter(x -> x != null)
                            .findFirst()
                            .map(x -> x.geto5())
                            .filter(x -> x != null) // takes first LocalO4 and its val is null
                            .orElse(null);
                        assert ii == null;

                        // this is correct
                        ii = Stream.of(new LocalO4(null), new LocalO4(10))
                            .filter(x -> x != null)
                            .map(x -> x.geto5())
                            .filter(x -> x != null) // takes first LocalO4 and its val is null
                            .findFirst()
                            .orElse(null);
                        assert ii == 10;

                        ii = Stream.of(new LocalO4(null), new LocalO4(null))
                            .filter(x -> x != null)
                            .map(x -> x.geto5())
                            .filter(x -> x != null) // takes first LocalO4 and its val is null
                            .findFirst()
                            .orElse(10);
                        assert ii == 10;


                        ii = Stream.of(new LocalO3(null), new LocalO3(null))
                            .filter(x -> x != null)
                            .findFirst()
                            .map(x -> x.geto4())
                            .map(x -> x.geto5())
                            .orElse(null);
                        assert ii == null;

                        ii = Stream.of(new LocalO3(null), new LocalO3(new LocalO4(null)))
                            .filter(x -> x != null)
                            .findFirst()
                            .map(x -> x.geto4())
                            .map(x -> x.geto5())
                            .orElse(null);
                        assert ii == null;

                        {
                            // why does this fault at findFirst??? because optional is not allowed to have null
                            try {
                                ii = Stream.of(new LocalO3(null), new LocalO3(new LocalO4(null)))
                                    .filter(x -> x != null)
                                    .map(x -> x.geto4())
                                    .map(x -> x.geto5())
                                    .filter(x -> x != null)
                                    .findFirst()
                                    .orElse(null);
                                assert ii == null;
                                expected = false;
                            } catch (Exception e) {
                                expected = true;
                            }
                            assert expected;

                            // this one also faults!
                            try {
                                ii = Stream.of(new LocalO3(null), new LocalO3(new LocalO4(null)))
                                    .filter(x -> x != null)
                                    .map(x -> x.geto4())
                                    .map(x -> x.geto5())
                                    .findFirst()
                                    .orElse(null);
                                assert ii == null;
                                expected = false;
                            } catch (Exception e) {
                                expected = true;
                            }
                            assert expected;

                            // this one does not fault!
                            try {
                                ii = Stream.of(new LocalO3(null), new LocalO3(new LocalO4(null)))
                                    .filter(x -> x != null)
                                    .map(x -> x.geto4())
                                    .filter(x -> x != null)
                                    .map(x -> x.geto5())
                                    .filter(x -> x != null)
                                    .findFirst()
                                    .orElse(null);
                                assert ii == null;
                                expected = true;
                            } catch (Exception e) {
                                expected = false;
                            }
                            assert expected;

                            // this one does not fault!
                            try {
                                ii = Stream.of(new LocalO3(null), new LocalO3(new LocalO4(null)), new LocalO3(new LocalO4(10)))
                                    .filter(x -> x != null)
                                    .map(x -> x.geto4())
                                    .filter(x -> x != null)
                                    .map(x -> x.geto5())
                                    .filter(x -> x != null)
                                    .findFirst()
                                    .orElse(null);
                                assert ii == 10;
                                expected = true;
                            } catch (Exception e) {
                                expected = false;
                            }
                            assert expected;

                        }


                        ii = Stream.of(new LocalO3(null), new LocalO3(new LocalO4(null)), new LocalO3(new LocalO4(10)))
                            .filter(x -> x != null)
                            .findFirst()
                            .map(x -> x.geto4())// this only evaluates the first one!
                            .map(x -> x.geto5())
                            .orElse(null);
                        assert ii == null;

                        try {
                            ii = Stream.of(new LocalO3(null), new LocalO3(new LocalO4(null)), new LocalO3(new LocalO4(10)))
                                .filter(x -> x != null)
                                .map(x -> x.geto4())
                                .filter(x -> x != null)
                                .findFirst()
                                .map(x -> x.geto5()) // this evaluates only second one!
                                .orElse(null);
                            assert ii == null;
                            expected = true;
                        } catch (Exception e) {
                            expected = false;
                        }
                        assert expected;

                        try {
                            ii = Stream.of(new LocalO3(null), new LocalO3(new LocalO4(null)), new LocalO3(new LocalO4(10)))
                                .filter(x -> x != null)
                                .map(x -> x.geto4())
                                .filter(x -> x != null)
                                .map(x -> x.geto5())
                                .filter(x -> x != null)
                                .findFirst()
                                .orElse(null);
                            assert ii == 10;
                            expected = true;
                        } catch (Exception e) {
                            expected = false;
                        }
                        assert expected;

                        try {
                            ii = Stream.of((new LocalO3(null)).geto4(), (new LocalO3(null)).geto4())
                                .filter(x -> x != null) // both are null
                                .map(x -> x.geto5())
                                .findFirst()// filter returned empty, so no null??
                                .orElse(null);
                            assert ii == null;
                            expected = true;
                        } catch (Exception e) {
                            expected = false;
                        }
                        assert expected;

                        try {
                            ii = Stream.of((new LocalO3(null)).geto4(), (new LocalO3(null)).geto4())
                                .map(x -> x.geto5())
                                .findFirst()// will fault because all map returns null, so you have to filter first, in previous example
                                .orElse(null);
                            assert ii == null;
                            expected = false;
                        } catch (Exception e) {
                            expected = true;
                        }
                        assert expected;

                        try {
                            ii = Stream.of((new LocalO3(null)).geto4(), (new LocalO3(new LocalO4(null))).geto4())
                                .filter(x -> x != null)
                                .map(x -> x.geto5())
                                .findFirst()// will fault because all result is null
                                .orElse(null);
                            assert ii == null;
                            expected = false;
                        } catch (Exception e) {
                            expected = true;
                        }
                        assert expected;

                        try {
                            ii = Stream.of((new LocalO3(null)).geto4(), (new LocalO3(new LocalO4(null))).geto4())
                                .filter(x -> x != null)
                                .findFirst()
                                .map(x -> x.geto5()) // will NOT fault because there is non null element
                                .orElse(null);
                            assert ii == null;
                            expected = true;
                        } catch (Exception e) {
                            expected = false;
                        }
                        assert expected;

                        try {
                            ii = Stream.of((new LocalO3(null)).geto4(), (new LocalO3(new LocalO4(10))).geto4())
                                .filter(x -> x != null)
                                .map(x -> x.geto5())
                                .findFirst() // will NOT fault because there is non null element
                                .orElse(null);
                            assert ii == 10;
                            expected = true;
                        } catch (Exception e) {
                            expected = false;
                        }
                        assert expected;

                        try {
                            ii = Stream.of((new LocalO3(null)).geto4(), (new LocalO3(new LocalO4(10))).geto4())
                                .filter(x -> x != null)
                                .findFirst()
                                .map(x -> x.geto5())
                                .orElse(null);
                            assert ii == 10;
                            expected = true;
                        } catch (Exception e) {
                            expected = false;
                        }
                        assert expected;

                        try {
                            ii = Stream.of((new LocalO3(new LocalO4(null))).geto4(), (new LocalO3(new LocalO4(10))).geto4())
                                .filter(x -> x != null)
                                .map(x -> x.geto5())
                                .findFirst()    // this faults because first element in stream is null
                                .orElse(null);
                            assert ii == null;
                            expected = false;
                        } catch (Exception e) {
                            expected = true;
                        }
                        assert expected;

                        try {
                            ii = Stream.of((new LocalO3(new LocalO4(null))).geto4(), (new LocalO3(new LocalO4(10))).geto4())
                                .map(x -> x.geto5())
                                .filter(x -> x != null)
                                .findFirst()    // this does not fault because first element in stream is not null
                                .orElse(null);
                            assert ii == 10;
                            expected = true;
                        } catch (Exception e) {
                            expected = false;
                        }
                        assert expected;


                        try {
                            ii = Stream.of((new LocalO3(new LocalO4(20))).geto4(), (new LocalO3(new LocalO4(10))).geto4())
                                .filter(x -> x != null)
                                .map(x -> x.geto5())
                                .findFirst()    // no fault because at least 1 non null
                                .orElse(null);
                            assert ii == 20;
                            expected = true;
                        } catch (Exception e) {
                            expected = false;
                        }
                        assert expected;

                        try {
                            ii = Stream.of((new LocalO3(new LocalO4(null))).geto4(), (new LocalO3(new LocalO4(10))).geto4())
                                .filter(x -> x != null)
                                .map(x -> x.geto5())
                                .findFirst()    // fault because at first element is null
                                .orElse(null);
                            assert ii == null;
                            expected = false;
                        } catch (Exception e) {
                            expected = true;
                        }
                        assert expected;

                        try {
                            ii = Stream.of((new LocalO3(new LocalO4(null))).geto4(), (new LocalO3(new LocalO4(10))).geto4())
                                .map(x -> x.geto5())
                                .filter(x -> x != null)
                                .findFirst()    // no fault because at first element is non null
                                .orElse(null);
                            assert ii == 10;
                            expected = true;
                        } catch (Exception e) {
                            expected = false;
                        }
                        assert expected;

                        try {
                            ii = Stream.of(new LocalO3(null), new LocalO3(new LocalO4(null)), new LocalO3(new LocalO4(10)))
                                .filter(x -> x != null)
                                .map(x -> x.geto4())
                                .filter(x -> x != null)
                                .map(x -> x.geto5()) // this evaluates only second one!
                                .findFirst()    // this faults because first element x.geto5 is null, not Optional<null>. need filter to make it work!
                                .orElse(null);
                            assert ii == 10;
                            expected = false;
                        } catch (Exception e) {
                            expected = true;
                        }
                        assert expected;

                        try {
                            ii = Stream.of(new LocalO3(null), new LocalO3(new LocalO4(null)), new LocalO3(new LocalO4(10)))
                                .filter(x -> x != null)
                                .map(x -> x.geto4())
                                .filter(x -> x != null)
                                .map(x -> x.geto5())
                                .filter(x -> x != null)
                                .findFirst()    // this no faults because first element x.geto5 is not null
                                .orElse(null);
                            assert ii == 10;
                            expected = true;
                        } catch (Exception e) {
                            expected = false;
                        }
                        assert expected;


                        ii = Stream.of(new LocalO3(null), new LocalO3(new LocalO4(null)), new LocalO3(new LocalO4(10)))
                            .filter(x -> x != null)
                            .map(x -> x.geto4())
                            .filter(x -> x != null)
                            .map(x -> x.geto5())
                            .filter(x -> x != null) // this evaluates third one!
                            .findFirst()
                            .orElse(null);
                        assert ii == 10;

                        ii = Stream.of(new LocalO3(null), new LocalO3(new LocalO4(null)), new LocalO3(new LocalO4(10)), new LocalO3(new LocalO4(20)))
                            .filter(x -> x != null)
                            .map(x -> x.geto4())
                            .filter(x -> x != null)
                            .map(x -> x.geto5())
                            .filter(x -> x != null)
                            .findFirst()
                            .orElse(null);
                        assert ii == 10;

                        try {
                            ii = Stream.of(new LocalO3(null), new LocalO3(new LocalO4(null)), new LocalO3(new LocalO4(10)), new LocalO3(new LocalO4(20)))
                                .filter(x -> x != null)
                                .map(x -> x.geto4())
                                .map(x -> x.geto5())        // this will fault
                                .filter(x -> x != null)
                                .findFirst()
                                .orElse(null);
                            assert ii == 10;
                            expected = false;
                        } catch (Exception e) {
                            expected = true;
                        }
                        assert expected;

                        ii = Stream.of(new LocalO3(null), new LocalO3(new LocalO4(20)), new LocalO3(new LocalO4(10)))
                            .filter(x -> x != null)
                            .findFirst()
                            .map(x -> x.geto4())
                            .map(x -> x.geto5())
                            .orElse(null);
                        assert ii == null;

                        ii = Stream.of(new LocalO3(null), new LocalO3(new LocalO4(20)), new LocalO3(new LocalO4(10)))
                            .filter(x -> x != null)
                            .findFirst()
                            .map(x -> x.geto4())
                            .map(x -> x.geto5())
                            .filter(x -> x != null)
                            .orElse(null);
                        assert ii == null;

                        try {
                            ii = Stream.of(new LocalO3(null), new LocalO3(new LocalO4(20)), new LocalO3(new LocalO4(10)))
                                .map(x -> x.geto4())
                                .map(x -> x.geto5())
                                .filter(x -> x != null)
                                .findFirst()
                                .orElse(null);
                            assert ii == null;
                            expected = false;
                        } catch (Exception e) {
                            expected = true;
                        }
                        assert expected;

                        ii = Stream.of(new LocalO3(null), new LocalO3(new LocalO4(20)), new LocalO3(new LocalO4(10)))
                            .filter(x -> x != null)
                            .map(x -> x.geto4())
                            .filter(x -> x != null)
                            .map(x -> x.geto5())
                            .filter(x -> x != null)
                            .findFirst()
                            .orElse(null);
                        assert ii == 20;

                        ii = Stream.of(new LocalO3(null), new LocalO3(new LocalO4(null)), new LocalO3(new LocalO4(10)))
                            .filter(x -> x != null)
                            .map(x -> x.geto4())
                            .filter(x -> x != null)
                            .map(x -> x.geto5())
                            .filter(x -> x != null)
                            .findFirst()
                            .orElse(null);
                        assert ii == 10;

                        ii = Stream.of(new LocalO3(null), new LocalO3(new LocalO4(null)))
                            .filter(x -> x != null)
                            .map(x -> x.geto4())
                            .filter(x -> x != null)
                            .map(x -> x.geto5())
                            .filter(x -> x != null)
                            .findFirst()
                            .orElse(null);
                        assert ii == null;

                        ii = Optional
                            .ofNullable(new LocalO3(null))
                            .map(x -> x.geto4())
                            .map(x -> x.geto5())
                            .orElse(null);
                        assert ii == null;

                        ii = Optional
                            .ofNullable(new LocalO3(new LocalO4(null)))
                            .map(x -> x.geto4())
                            .map(x -> x.geto5())
                            .orElse(null);
                        assert ii == null;

                        ii = Optional
                            .ofNullable(new LocalO3(new LocalO4(10)))
                            .map(x -> x.geto4())
                            .map(x -> x.geto5())
                            .orElse(null);
                        assert ii == 10;

                        ii = Optional
                            .ofNullable((new LocalO3(null)).geto4())
                            .map(x -> x.geto5())
                            .orElse(null);
                        assert ii == null;

                        ii = Optional
                            .ofNullable((new LocalO3(new LocalO4(null))).geto4())
                            .map(x -> x.geto5())
                            .orElse(null);
                        assert ii == null;

                        ii = Optional
                            .ofNullable((new LocalO3(new LocalO4(10))).geto4())
                            .map(x -> x.geto5())
                            .orElse(null);
                        assert ii == 10;

                        Boolean booleanE;
                        booleanE = Optional
                            .ofNullable(new LocalO4(null))
                            .map(x -> x.geto5())
                            .map(x -> x > 10)
                            .orElse(null);
                        assert booleanE == null;

                        booleanE = Optional
                            .ofNullable(new LocalO4(15))
                            .map(x -> x.geto5())
                            .map(x -> x > 10)
                            .orElse(null);
                        assert booleanE == true;

                        booleanE = Optional
                            .ofNullable(new LocalO4(5))
                            .map(x -> x.geto5())
                            .map(x -> x > 10)
                            .orElse(null);
                        assert booleanE == false;

                        booleanE = Optional
                            .ofNullable(new LocalO3(null))
                            .map(x -> x.geto4())
                            .map(x -> x.geto5())
                            .map(x -> x > 10)
                            .orElse(null);
                        assert booleanE == null;


                        expected = true;
                    } catch(NullPointerException e) {
                        expected = false;
                    }
                    assert expected;

                    try {
                        i = Optional
                            .ofNullable(new LocalO2(new LocalO3(new LocalO4(10))))
                            .map(o -> o.geto3().geto4().geto5())
                            .orElse(300);
                        assert i == 10;
                        expected = true;
                    } catch(NullPointerException e) {
                        expected = false;
                    }
                    assert expected;

                }
            }
            {
                Integer i;
                i = Optional
                    .ofNullable(new LocalO5(1))
                    .filter(x -> x != null)
                    .flatMap(x -> Optional.ofNullable(x.add(1)))
                    .flatMap(x -> Optional.ofNullable((new LocalO6(2, new LocalO4(10))).match(x)))
                    .flatMap(x -> Optional.ofNullable(x.geto5()))
                    .orElse(100);
                assert i == 10;
                i = Optional
                    .ofNullable(new LocalO5(2))
                    .filter(x -> x != null)
                    .flatMap(x -> Optional.ofNullable(x.add(1)))
                    .flatMap(x -> Optional.ofNullable((new LocalO6(2, new LocalO4(10))).match(x)))
                    .flatMap(x -> Optional.ofNullable(x.geto5()))
                    .orElse(100);
                assert i == 100;
                Optional<Integer> oi;
            }
        }
        {
            LocalO5 o5 = new LocalO5(null);
            Optional.ofNullable((new LocalO3(null)).geto4())
                .filter(o -> o != null)
                .map(o -> o.geto5())
                .ifPresent(o -> o5.set(o));
            assert o5.get() == null;
            Optional.ofNullable((new LocalO3(new LocalO4(null))).geto4())
                .filter(o -> o != null)
                .map(o -> o.geto5())
                .ifPresent(o -> o5.set(o));
            assert o5.get() == null;
            Optional.ofNullable((new LocalO3(new LocalO4(10))).geto4())
                .filter(o -> o != null)
                .map(o -> o.geto5())
                .ifPresent(o -> o5.set(o));
            assert o5.get() == 10;
        }
        p("passed testOptional\n");
    }

    @Test
    public void testFlatMapFunction() {
        {
            Map<String,List<String>> map1 = new HashMap<>();
            map1.put("k1",Arrays.asList("v1a","v1b","v1c","v1d"));
            map1.put("k2",Arrays.asList("v2a"));
            map1.put("k3",Arrays.asList("v3a","v3b"));

            Map<String,String> map2 = new HashMap<>();
            map2.put("k1","v1");
            map2.put("k2","v2");

            Function<String,List<String>> f1 = x -> map1.get(x);

            List<String> l1 = Arrays.asList("k1","k2","k3","k4","k5");

            List<List<String>> result1 = l1.stream()
                .map(k -> f1.apply(k))
                .filter(x -> x != null)
                .collect(Collectors.toList());
            assert result1.size() == 3;

            List<String> result2 = l1.stream()
                .map(k -> f1.apply(k))
                .filter(x -> x != null)
                .flatMap(l -> l.stream())
                .collect(Collectors.toList());
            assert result2.size() == 7;

            List<List<String>> result3 = l1.stream()
                .map(k -> f1.apply(k))
                .collect(Collectors.toList());
            assert result3.size() == 5; // did not filter out null
        }
        {
            List<List<Integer>> l1 = new ArrayList<>();
            l1.add(Arrays.asList(1,2,3));
            l1.add(Arrays.asList(2,3,4,5));
            l1.add(Arrays.asList(3,4,5,6,7));
            List<Integer> l2 = l1.stream().flatMap(x -> x.stream()).collect(Collectors.toList());
            assert l1.size() == 3;
            assert l2.size() == 12;
        }
        {
            Set<Integer> set1 = new HashSet<>();
            Set<Integer> set2 = new HashSet<>();
            for(int i = 0; i < 5; i++) set1.add(i);
            for(int i = 0; i < 4; i++) set2.add(i);
            assert(set2.stream().allMatch(i -> set1.contains(i)));  // set2 is subset of set1
            assert(!set1.stream().allMatch(i -> set2.contains(i))); // set1 is not subset of set2
            assert(!set2.containsAll(set1)); // set2 is subset of set1
            assert(set1.containsAll(set2));  // set1 is superset of set2
        }
        p("passed testFlatMapFunction\n");
    }

    @Test
    public void testSum() {
        List<Integer> list = new ArrayList<>();
        for(int i = 1; i <= 10; i++) list.add(i);
        int sum = list.stream().mapToInt(x -> x).sum();
        int reduce = list.stream().mapToInt(x -> x).reduce(0, (a,b)-> a+b);
        double avg = list.stream().mapToInt(x -> x).average().getAsDouble();
        int manualsum = 0;
        for(int i: list) manualsum += i;
        assert sum == manualsum;
        assert sum == 55;
        assert reduce == 55;
        assert avg == 5.5;
    }

    @Test
    public void testStream() {
        Set<Integer> si0 = new HashSet<>();
        int max = 100;
        for(int i = 0; i < max; i++) si0.add(i);
        List<Integer> li0 = si0.stream().limit(10).collect(Collectors.toList());
        assert li0 != null && li0.size() == 10;
        assert si0.containsAll(li0);
        Set<Integer> si1 = new HashSet<>(li0);
        assert si1.size() == li0.size();

        {
            List<NewLambdaObject> listObjects = Arrays.asList(
                new NewLambdaObject(1,3,0),
                new NewLambdaObject(2,8,0),
                new NewLambdaObject(8,9,0),
                new NewLambdaObject(10,16,0),
                new NewLambdaObject(14,13,0),
                new NewLambdaObject(17,2,0),
                new NewLambdaObject(10,16,0),
                new NewLambdaObject(2,8,0),
                new NewLambdaObject(2,9,0),
                new NewLambdaObject(7,28,0),
                new NewLambdaObject(19,27,0),
                new NewLambdaObject(2,8,0),
                new NewLambdaObject(17,2,0),
                new NewLambdaObject(17,3,0),
                new NewLambdaObject(10,16,0),
                new NewLambdaObject(12,10,0),
                new NewLambdaObject(2,8,0),
                new NewLambdaObject(8,8,0),
                new NewLambdaObject(17,2,0),
                new NewLambdaObject(17,17,0),
                new NewLambdaObject(18,14,0),
                new NewLambdaObject(6,9,0),
                new NewLambdaObject(4,27,0)
            );

            Set<Integer> setIntegers = new HashSet<>(Arrays.asList(1,2,3,4,5));
            Comparator<Integer> comparator = (x,y) -> x - y;
            Integer resultInteger = listObjects.stream()
                .filter(x ->  x != listObjects.get(2) || x.a() % 2 == 0)  // keep if obj != objlist.get(2) || x.a % mod == 0
                .map(x -> x.a())
                .filter(x -> x < 14) // keep if val < threshold
                .max(comparator)
                .orElseThrow(NoSuchElementException::new);
            assert resultInteger == 12;

            NewLambdaObject resultNewLambdaObject = listObjects.stream()
                .filter(x -> x != listObjects.get(2) && x.a() % 2 == 0 && x.b() % 2 == 0)
                .max(Comparator.comparing(NewLambdaObject::a))  // equivalent
                //.max((x,y) -> x.a() - y.a())  // equivalent
                .orElse(null);
            assert resultNewLambdaObject != null && resultNewLambdaObject.a() == 18;

            // hashCode and equals must be overridden for distinct to work
            List<NewLambdaObject> resultList = listObjects.stream().distinct().collect(Collectors.toList());
            assert listObjects.size() == 23 && resultList.size() == 16;

            NewLambdaObject refObj = new NewLambdaObject(0,0,0);
            refObj.incCtr(null);
            refObj.incCtr(null);
            refObj.incCtr(null);
            assert refObj.getCtr() == 3;
            refObj.reset();

            // peek is intermediate evaluation, which is lazy evaluation, not iterate through entire stream
            listObjects.stream().peek(x -> refObj.incCtr(x));
            assert refObj.getCtr() == 0;

            refObj.reset();
            listObjects.stream().peek(x -> x.consumeEmpty(x)).forEach(x -> refObj.incCtr(x));
            assert refObj.getCtr() == 23;

            // stream is intermediate evaluation, which is lazy evaluate, not iterate through entire stream
            refObj.reset();
            resultNewLambdaObject = listObjects.stream()
                .peek(x -> refObj.incCtr(x))
                .filter(x -> x.a() > 5)
                .findFirst()
                .get();
            assert refObj.getCtr() == 3; // only iterate til reach 8
            assert resultNewLambdaObject != null && resultNewLambdaObject.a() == 8;

            resultNewLambdaObject = listObjects.stream().filter(x -> x.a() > 8).findFirst().get();
            assert resultNewLambdaObject != null && resultNewLambdaObject.a() == 10;

            // does not need peek for intermediate evaluation. operations are processed in chain 1 at a time
            refObj.reset();
            resultNewLambdaObject = listObjects.stream().map(x -> refObj.incCtr(x)).filter(x -> x.a() > 8).findFirst().get();
            assert resultNewLambdaObject != null && resultNewLambdaObject.a() == 10;
            assert refObj.getCtr() == 4;

            resultNewLambdaObject = listObjects.stream().filter(x -> x.a() > 8).findFirst().get();
            assert resultNewLambdaObject != null && resultNewLambdaObject.a() == 10;

            resultNewLambdaObject = listObjects.stream().filter(x -> x.a() > 10).findFirst().orElse(null);
            assert resultNewLambdaObject != null && resultNewLambdaObject.a() == 14;

            resultNewLambdaObject = listObjects.stream().filter(x -> x.a() > 50).findFirst().orElse(null);
            assert resultNewLambdaObject == null;

            refObj.reset();
            IntStream.range(0,5).forEach(x -> refObj.addCtr(x));
            assert refObj.getCtr() == 10;

            refObj.reset();
            IntStream.of(0,1,2,3,4).forEach(x -> refObj.addCtr(x));
            assert refObj.getCtr() == 10;

            List<Integer> listIntegers = listObjects.stream().map(x -> x.a()).collect(Collectors.toList());
            int resultInt = listIntegers.stream().reduce(0, Integer::sum);
            resultInteger = listObjects.stream().map(x -> x.a()).reduce(0, Integer::sum);
            assert resultInteger.intValue() == resultInt;
            resultInteger = listObjects.stream().map(x -> x.a()).reduce(1, Integer::sum);
            assert resultInteger == 223;

            Set<Integer> setResults = listObjects.stream().filter(x -> setIntegers.contains(x.a())).map(x -> x.a()).collect(Collectors.toSet());
        }
        {
            List<Integer> li1 = Arrays.asList(1,1,2,3,4,4,5,5,6,7);
            int [] array = li1.stream().mapToInt(x -> x).toArray(); // list to int array
            assert array != null && array.length == 10;
            Object [] arrayObject = li1.toArray(); // list to object array
            assert arrayObject != null && arrayObject.length == 10;
            List<Integer> li2 = li1.stream().distinct().collect(Collectors.toList());
            assert li1.size() == 10;
            assert li2.size() == 7;
            Long l1 = li1.stream().distinct().count();
            assert l1 == 7;
            l1 = li1.stream().count();
            assert l1 == 10;
            l1 = li1.stream().filter(x -> x % 2 == 0).count();
            assert l1 == 4;
            l1 = li1.stream().filter(x -> x % 2 == 0).distinct().count();
            assert l1 == 3;
            int sum = li1.stream().mapToInt(x -> x.intValue()).sum();
            assert sum == (1+1+2+3+4+4+5+5+6+7);
            sum = Stream.of(1,2,3,4,5).mapToInt(x -> x.intValue()).sum();
            assert sum == (1+2+3+4+5);
            List<LocalO4> l4 = Arrays.asList(new LocalO4(2), new LocalO4(3), new LocalO4(4), new LocalO4(5));
            sum = l4.stream().map(x -> x.geto5()).reduce(0, Integer::sum);
            assert sum == (2+3+4+5);
            sum = l4.stream().mapToInt(x -> x.geto5()).sum();
            assert sum == (2+3+4+5);
            List<Integer> l2 = Arrays.asList(1,2,3,4,5,6,7,8,9,10);
            sum = l2.stream().mapToInt(x -> x.intValue()).limit(3).sum();
            assert sum == (1+2+3);
            sum = l2.stream().mapToInt(x -> x.intValue()).limit(4).sum();
            assert sum == (1+2+3+4);
            sum = l2.stream().reduce(0, (total,y)->total+y);
            assert sum == (1+2+3+4+5+6+7+8+9+10);
            sum = l2.stream().reduce(100, (total,y)->total+y);
            assert sum == (100+1+2+3+4+5+6+7+8+9+10);
        }
        p("\npass streams\n");
    }

    @Test
    public void testString() {
        String s = "\"abc\"";
        String ref = "abc";
        String trimmed = s.replace("\"","");
        assert ref.equals(trimmed);
        s = "%a%b%c%%%";
        boolean expected = false;
        try {
            p(s);   // cannot print %
            expected = false;
        } catch(UnknownFormatConversionException e) {
            expected = true;
        }
        assert expected;

        String s2 = s.replaceAll("%","%%");
        assert s2.equals("%%a%%b%%c%%%%%%");
        try {
            p(s2);   // can print %% (even)
            expected = true;
        } catch(Exception e) {
            expected = false;
        }
        assert expected;

        s2 = s.replaceAll("%","\\%");
        assert s2.equals("%a%b%c%%%");
        try {
            p(s2);   // cannot print %
            expected = false;
        } catch(UnknownFormatConversionException e) {
            expected = true;
        }
        assert expected;

        s2 = s.replaceAll("%","\\\\%");
        assert s2.equals("\\%a\\%b\\%c\\%\\%\\%");
        try {
            p(s2);   // cannot print \%
            expected = false;
        } catch(UnknownFormatConversionException e) {
            expected = true;
        }
        assert expected;

        p("\npass testString\n");
    }

    private List<Integer> loadListInt(int startIdx, int endIdx) {
        List<Integer> list = new ArrayList<>();
        for(int i = startIdx; i <= endIdx; i++)
            list.add(i);
        return list;
    }

    private int sum(List<Integer> l) {
        int sum = 0;
        for(int i: l) sum += i;
        return sum;
    }

    private int sum(Supplier<List<Integer>> supplier) {
        int sum = 0;
        for(Integer i: supplier.get()) sum += i;
        return sum;
    }

    @Test
    public void testLambaSingleMethodInterface() {
        SingleMethodInterface0Arg o0 = () -> 1;
        SingleMethodInterface1Arg o1 = (i) -> i;
        SingleMethodInterface2Arg o2 = (i,j) -> i+j;
        SingleMethodInterface3Arg o3 = (i,j,k) -> i+j+k;
        for(int i = 0; i < 3; i++)
            for(int j = 0; j < 3; j++)
                for(int k = 0; k < 3; k++) {
                    assert o0.foo() == 1;
                    assert o1.foo(k) == k;
                    assert o2.foo(j,k) == (j+k);
                    assert o3.foo(i,j,k) == (i+j+k);
                }
    }

    private String getString(String s) {
        return s;
    }

    @Test
    public void testNewLambdaObjectBuilder() {
        NewLambdaObject o;
        o = new NewLambdaObject.NewLambdaObjectBuilder()
            .with(builder -> {
                builder.a = 1;
                builder.b = 2;
                builder.c = 3;
            }).build();
        assert o.a() == 1 && o.b() == 2 && o.c() == 3;
        o = new NewLambdaObject.NewLambdaObjectOldBuilder()
            .a(1)
            .b(2)
            .c(3)
            .build();
        assert o.a() == 1 && o.b() == 2 && o.c() == 3;

        BasicBuilderTestingObject b;
        b = new BasicBuilderTestingObject.BasicBuilderTestingObjectNewBuilder()
            .with(builder -> {
                builder.a = 1;  // implies a cannot be private
                builder.b = 2;
                builder.c = 3;
            }).build();
        assert o.a() == 1 && o.b() == 2 && o.c() == 3;
    }

    @Test
    public void testHashMap() {
        Map<String,String> map = new HashMap<>();
        assert null == map.get("foo");
        logger.info("testHashMap ran");
        p("pass testHashMap\n");
    }
    @Test
    public void testOptionalConsume() {
        boolean expected = false;
        {
            LocalO5 localo5 = new LocalO5(10);
            LocalO3 localo3 = null;
            try {
                expected = false;
                localo3 = null;
                Optional.ofNullable(localo3.geto4())
                    .filter(localo4 -> localo4.geto5() != null)
                    .ifPresent(localo4 -> localo5.set(localo4.geto5()));
                assert localo5.get() == 10;
            } catch(NullPointerException e) {
                expected = true;
            }
            assert expected;

            localo3 = null;
            Optional.ofNullable(localo3)
                .map(o -> o.geto4())
                .filter(localo4 -> localo4.geto5() != null)
                .ifPresent(localo4 -> localo5.set(localo4.geto5()));
            assert localo5.get() == 10;

            localo3 = new LocalO3(new LocalO4(null));
            Optional.ofNullable(localo3)
                .map(o -> o.geto4())
                .filter(localo4 -> localo4.geto5() != null)
                .ifPresent(localo4 -> localo5.set(localo4.geto5()));
            assert localo5.get() == 10;

            localo3 = new LocalO3(new LocalO4(20));
            Optional.ofNullable(localo3)
                .map(o -> o.geto4())
                .filter(localo4 -> localo4.geto5() != null)
                .ifPresent(localo4 -> localo5.set(localo4.geto5()));
            assert localo5.get() == 20;

            localo3 = new LocalO3(null);
            Optional.ofNullable(localo3)
                .map(o -> o.geto4())
                .filter(localo4 -> localo4.geto5() != null)
                .ifPresent(localo4 -> localo5.set(localo4.geto5()));
            assert localo5.get() == 20;
        }
    }
    @Test
    public void testGenerateList() {
        List<Integer> range;
        range = IntStream.range(0,10).boxed().collect(Collectors.toList());
        assert range.size() == 10;
        for(int i = 0; i < range.size(); i++) {
            assert i == range.get(i);
        }
        range = IntStream.rangeClosed(0,10).boxed().collect(Collectors.toList());
        assert range.size() == 11;
        for(int i = 0; i < range.size(); i++) {
            assert i == range.get(i);
        }
    }
}

interface SingleMethodInterface0Arg { public int foo(); }
interface SingleMethodInterface1Arg { public int foo(int i); }
interface SingleMethodInterface2Arg { public int foo(int i, int j); }
interface SingleMethodInterface3Arg { public int foo(int i, int j, int k); }

class SupplierTestObject {
    int size = 0;
    int currentCount = 0;
    List<Integer> supply() {
        List<Integer> list = new ArrayList<>();
        for(int i = 0; i < size; i++) {
            list.add(currentCount++);
        }
        return list;
    }
    public void setCurrentCount(int count) {
        this.currentCount = count;
    }
    public SupplierTestObject(int sizeList) {
        this.size = sizeList;
    }
}

class Converter {
    Integer id;
    LinkedList<Integer> ll = new LinkedList<>();
    boolean enableContinuousQueue = false;
    int inxt = 0;
    int blocksize = 3;
    String s;
    Supplier<Integer> supplierInteger;
    Consumer<Integer> consumerInteger;
    public Converter(Integer id) {
        this.id = id;
    }
    void setStoredString(String s) {
        this.s = s;
    }
    String getStoredString() {
        return this.s;
    }
    String getString(String s) {
        return s;
    }
    void setSupplierInteger(Supplier<Integer> supplierInteger) {
        this.supplierInteger = supplierInteger;
    }
    Supplier<Integer> getSupplierInteger() {
        return this.supplierInteger;
    }
    void setConsumerInteger(Consumer<Integer> consumerInteger) {
        this.consumerInteger = consumerInteger;
    }
    Consumer<Integer> getConsumerInteger() {
        return this.consumerInteger;
    }
    void setEnableContinuousQueue(boolean enableContinuousQueue) {
        this.enableContinuousQueue = enableContinuousQueue;
    }
    void storeIntegerToQueue(Integer i) {
        ll.add(i);
    }
    void reset() {
        inxt = 0;
        ll.clear();
    }
    void storeRangeToQueue(int ibeg, int iend) {
        for(int i = ibeg; i < iend; i++) {
            ll.add(i);
        }
    }
    Integer getIntegerFromQueue() {
        Integer i = ll.poll();
        if(enableContinuousQueue && i == null) {
            storeRangeToQueue(inxt, inxt + blocksize);
            inxt += blocksize;
            return ll.poll();
        }
        return i;
    }
    Integer getInteger(Integer i) {
        return i;
    }
    String getString(String s, Integer i) {
        return String.format("%s.%d.%d", s, id);
    }
    Integer getId() {
        return id;
    }
}

class LocalObject {
    private Integer i1;
    private Integer i2;
    public LocalObject(Integer i1, Integer i2){
        this.i1 = i1;
        this.i2 = i2;
    }
    public Integer getI1() { return i1; }
    public Integer getI2() { return i2; }
}

class LocalO1 {
    private LocalO2 o;
    public LocalO1(LocalO2 o) {
        this.o = o;
    }
    public LocalO2 geto2() {
        return o;
    }
}

class LocalO2 {
    private LocalO3 o;
    public LocalO2(LocalO3 o) {
        this.o = o;
    }
    public LocalO3 geto3() {
        return o;
    }
}

class LocalO3 {
    private LocalO4 o;
    public LocalO3(LocalO4 o) {
        this.o = o;
    }
    public LocalO4 geto4() {
        return o;
    }
}

class LocalO4 {
    private Integer o;
    public LocalO4(Integer o) {
        this.o = o;
    }
    public Integer geto5() {
        return o;
    }
}

class LocalO5 {
    private Integer o;
    public LocalO5(Integer o) {
        this.o = o;
    }
    public void set(Integer o) { this.o = o; }
    public Integer get() { return o; }
    public Integer add(Integer i) {
        return o + i;
    }
}

class LocalO6 {
    private Integer o;
    private LocalO4 o4;
    public LocalO6(Integer o, LocalO4 o4) {
        this.o = o;
        this.o4 = o4;
    }
    public LocalO4 match(Integer i) {
        if(i == o) return o4;
        return null;
    }
}



class NewLambdaObject {
    int a;
    int b;
    int c;
    int ctr = 0;
    public NewLambdaObject(int a) {
        this.a = a;
        this.b = 0;
        this.c = 0;
    }
    public NewLambdaObject(int a, int b) {
        this.a = a;
        this.b = b;
        this.c = 0;
    }
    public void consumeEmpty(NewLambdaObject o) {

    }
    public NewLambdaObject incCtr(NewLambdaObject o){
        ctr++;
        return o;
    }
    public void reset() {
        a = b = c = ctr = 0;
    }
    public void addCtr(int x) {
        ctr += x;
    }
    public int getCtr() {
        return ctr;
    }
    public NewLambdaObject(int a, int b, int c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }
    public int a() { return a; }
    public int b() { return b; }
    public int c() { return c; }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof NewLambdaObject) {
            NewLambdaObject o = (NewLambdaObject)obj;
            return o.a() == this.a && o.b() == this.b && o.c() == this.c;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + this.a;
        result = 31 * result + this.b;
        result = 31 * result + this.c;
        return result;
    }
    static class NewLambdaObjectOldBuilder {
        int a;
        int b;
        int c;
        public NewLambdaObjectOldBuilder a(int a) { this.a = a; return this; }
        public NewLambdaObjectOldBuilder b(int b) { this.b = b; return this; }
        public NewLambdaObjectOldBuilder c(int c) { this.c = c; return this; }
        public NewLambdaObject build() {
            return new NewLambdaObject(a,b,c);
        }
    }

    static class NewLambdaObjectBuilder {
        int a;
        int b;
        int c;
        public NewLambdaObjectBuilder with(Consumer<NewLambdaObjectBuilder> consumer) {
            consumer.accept(this);
            return this;
        }
        public NewLambdaObject build() {
            return new NewLambdaObject(a,b,c);
        }
    }
}

// single abstract method == SAM
interface IFCSingleAnd {
    abstract int and(int a, int b);
}

interface IFCSingleXor {
    abstract int xor(int a, int b);
}