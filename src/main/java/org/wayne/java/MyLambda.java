package org.wayne.java;

import org.wayne.main.MyBasic;
import org.wayne.misc.Utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;

import java.util.concurrent.Executors;

public class MyLambda implements MyBasic {
    /* cannot have inner static variable, so put it here. */
    public static final double PI = 3.14;
    public static int CTR = 0;
    Utils utils = new Utils();
    Random rand = new Random();
    ExecutorService pool = Executors.newFixedThreadPool(4);

    public static void p(String f, Object ...o) {
        System.out.printf(f,o);
    }
    public static void pl(String f, Object ...o) {
        p(f + "\n", o);
    }

    int [] aFibonacci = {0,1};
    Stream<Integer> fibonacci = Stream.generate(() -> {
        int ret = aFibonacci[1];
        int fib3 = aFibonacci[0]+aFibonacci[1];
        aFibonacci[0] = aFibonacci[1];
        aFibonacci[1] = fib3;
        return ret;
    });

    @Override
    public void shutdown() {

    }

    @Override
    public void init() {

    }

    public interface CheckShape {
        boolean test(Shape s);
    }

    public int add(int x, int y) { return x + y; }
    public int squared(int x) { return x * x; }
    public String concat(String s1, String s2) { return s1 + s2; }
    public String concatSpace(String s1, String s2) { return s1 + s2; }
    public interface ShapeOperator {
        Shape copy(Shape s);
        Shape reduce();
        Shape expand();
    }
    public static abstract class Shape {
        int h;
        int w;
        int d;
        int r;
        String name;
        double area = 0;
        public void h(int h) { this.h = h; area(); }
        public void w(int w) { this.w = w; area(); }
        public void d(int d) { this.d = d; area(); }
        public void r(int r) { this.r = r; area(); }
        public void name(String name) { this.name = name; }
        public int h() { return h; }
        public int w() { return w; }
        public int d() { return d; }
        public int r() { return r; }
        public String name() { return name; }
        public double getArea() { return area; }
        public abstract void area();
    }
    public static class Square extends Shape {
        /* if inner class is static, then you can have inner static variable. */
        static int ctr = 0;
        public Square(int w) {
            this.name = "square" + Integer.toString(CTR++);
            this.w = w;
            area();
        }
        public void area() { this.area = w*w; }
    }
    public class Rectangle extends Shape {
        public Rectangle(int h, int w) {
            this.name = "rectangle" + Integer.toString(CTR++);
            this.h = h;
            this.w = w;
            area();
        }
        public void area() { this.area = h*w; }
    }
    public class Circle extends Shape {
        public Circle(int r) {
            this.name = "circle" + Integer.toString(CTR++);
            this.r = r;
            area();
        }
        public void area() { this.area = PI*r*r; }
    }
    public class Triangle extends Shape {
        public Triangle(int h, int w) {
            this.name = "triangle" + Integer.toString(CTR++);
            this.h = h;
            this.w = w;
            area();
        }
        public void area() { this.area = 0.5*h*w; }
    }
    public List<Shape> getMatch1(List<Shape> l, CheckShape c) {
        List<Shape> results = new ArrayList<>();
        for(Shape s: l) {
            if(c.test(s)) {
                results.add(s);
            }
        }
        return results;
    }

    public void testLambda1() {
        List<Shape> l = new ArrayList<>();
        int numEachType = 5;
        int [] w = {1,2,3,4,5};
        IntStream.range(0, numEachType).forEach(i -> {
            l.add(new Square(i+1));
            l.add(new Rectangle(i+1,i+1+1));
            l.add(new Circle(i+1));
            l.add(new Triangle(i+1,i+1+1));
        });
        /*
        for(int i = 0; i < numEachType; i++) {
            l.add(new Square(i+1));
            l.add(new Rectangle(i+1,i+1+1));
            l.add(new Circle(i+1));
            l.add(new Triangle(i+1,i+1+1));
        }
        */
        List<Shape> res1 = getMatch1(l, s -> s instanceof Square);
        List<Shape> res2 = getMatch1(l, s -> s.getArea() > 10.0);

        p("print all instances\n");
        l.stream().forEach(s -> p("area %7.2f for %s\n", s.getArea(), s.name()));

        p("print instances of square\n");
        res1.stream().forEach(s -> p("area %7.2f for %s\n", s.getArea(), s.name()));

        p("print area > 10.0\n");
        res2.stream().forEach(s -> p("area %7.2f for %s\n", s.getArea(), s.name()));

        p("print filter of square\n");
        l.stream()
                .filter(s -> s instanceof Square)
                .forEach(s -> p("area %7.2f for %s\n", s.getArea(), s.name()));

        p("print filter of square names and area > 10\n");
        l.stream()
                .filter(s -> s instanceof Square)
                .filter(s -> s.getArea() > 10.0)
                .forEach(s -> p("area %7.2f for %s\n", s.getArea(), s.name()));

    }

    public void testLambda2() {
        Stream<Integer> stream1 = Stream.iterate(1, x -> x + 1);
        System.out.println(stream1.limit(5).collect(Collectors.toList()));

        Function<Integer, Stream<Integer>> f1 = (x) -> Stream.iterate(1, y -> x + y + 1);   // y = x + y + 1
        Function<Integer, Stream<Integer>> f2 = (x) -> Stream.iterate(1, y -> y + 1);       // y = y + 1
        Function<Integer, Stream<Integer>> f3 = (x) -> Stream.iterate(1, y -> x + 1);       // y = x + 1
        Stream<Integer> stream2 = f1.apply(10).limit(5);
        System.out.println(stream2.collect(Collectors.toList()));
        System.out.println(f1.apply(10).limit(5).collect(Collectors.toList()));
        System.out.println(f2.apply(10).limit(5).collect(Collectors.toList()));
        System.out.println(f3.apply(10).limit(5).collect(Collectors.toList()));

        Function<Integer, Function<Integer,Integer>> f4 = x -> y -> x + y;
        Function<Integer, Integer> f6 = x -> x + x;
        Function<Integer, Function<Integer,Integer>> f7 = x -> f6;

        p("f4 %d\n", f4.apply(5).apply(3));
        p("f6 %d\n", f6.apply(5));
        p("f7 %d\n", f7.apply(5).apply(3));

        p("finished testLambda2\n");
    }

    public interface MyTriFunction<A,B,C,D> { D apply(A a, B b, C c); }

    public void testMapReduce1() {
        // https://stackoverflow.com/questions/23620360/how-to-map-to-multiple-elements-with-java-8-streams
        /*
        Given List<MultiDataPoint> produce List<DataSet>
        class MultiDataPoint {
            Integer vint;
            Map<String,Double> map;
        }
        class DataSet {
            String key;
            List<DataPoint> dataPoints;
        }
        class DataPoint {
            Integer vint;
            Double vdouble;
        }
         */

    }

    public String testFunctionParams(Function<Integer,Integer> f, int v, Integer v1) {
        return String.format("%d\n", f.apply(v));
    }

    public void testFunctionalComposition1() {
        List<ObjectGenericNested> list0 = org.wayne.java.Objects.createListObjectGenericNested(20);

        Map<Long,ObjectGenericNested> map1 = list0
                .stream().collect(Collectors.toMap(ObjectGenericNested::id, Function.identity()));

        Map<Long,ObjectGenericNested> map2 = list0
                .stream().collect(Collectors.toMap(ObjectGenericNested::id, x->x));

        BiFunction<Long,List<ObjectGenericNested>,List<Long>> bf1 = (v,list)
                -> list.stream().filter(o->o.vi() < v).map(o->o.id()).collect(Collectors.toList());

        BiFunction<List<Integer>,Map<Integer,ObjectGenericNested>,List<String>> bf2 =
            (list,map) -> list.stream().map(o->map.get(o).name()).collect(Collectors.toList());

        Function<List<Integer>,List<Integer>> f1 =
                (list) -> list.stream().map(id->map1.get(id).vi()).collect(Collectors.toList());

        Function<Long,Long> f2 = x -> map1.get(x).obj().id();

        Function<List<Long>,List<Long>> f3 =
                l -> l.stream().map(x->map1.get(x).obj().id()).collect(Collectors.toList());

        Function<List<Long>,List<Long>> f4 =
                l -> l.stream().map(f2).collect(Collectors.toList());

        Function<Collection<ObjectGenericNested>,Collection<Long>> f5 =
                l -> l.stream().map(x -> x.id()).collect(Collectors.toList());

        Function<Long,Long> f6 = x -> x + x;

        BiFunction<Integer,Integer,Integer> f7 = (x,y) -> x+y;

        Function<Collection<ObjectGenericNested>,Collection<Long>> f8 =
                l -> l.stream().map(x -> x.id()).map(f6).collect(Collectors.toList());

        Function<Collection<ObjectGenericNested>,Collection<Long>> f9 =
                l -> l.stream().map(ObjectGenericNested::id).map(f6).collect(Collectors.toList());

        Function<Collection<ObjectGenericNested>,Collection<Long>> f10 =
                l -> l.stream().map(ObjectGenericNested::id).map(f6).collect(Collectors.toList());

        MyTriFunction<MyLambda,Integer,Integer,Integer> fTri1 = (a,b,c) -> a.add(b,c);

        MyTriFunction<MyLambda,String,String,String> fTri2 = MyLambda::concat;

        {
            Collection<Long> res0 = f8.apply(list0);
            res0.stream().forEach(x -> p("%d ", x));
            p("\n");
            for(Long x: res0) {
                p("%d ", x);
            }
            p("\n");
            if(res0 instanceof List) {
                p("Collection is instanceof list\n");
            }
        }

        {
            p("cat + hat = %s\n", fTri2.apply(this,"cat","hat"));
        }

    }

    public void testSupplier1() {
        fibonacci.limit(10).forEach(x -> p("%d\n",x));
    }

    public void testFunction() {
        /* apply and default. */
        Function<Integer,Integer> addApplyOnly = x -> x + x;
        Function<Integer,String> addApplyStr = x -> "i2s number is " + Integer.toString(x);
        Function<String,Integer> addApplyInt = x -> 10 + Integer.parseInt(x);

        p("functionTest\n");
        p("add number is %d\n", addApplyOnly.apply(10));
        p("%s\n", addApplyStr.apply(10));
        p("s2i number is %d\n", addApplyInt.apply("10"));

        Function<String,String> removeUpperCase = x -> {
            StringBuilder sb = new StringBuilder();
            for(char c: x.toCharArray()) {
                if(Character.isLowerCase(c)) sb.append(c);
            }
            return sb.toString();
        };
        Function<String,String> toLowerCase = x -> x.toLowerCase();

        String msg1 = "This IsA Sentence";
        p("original sentence: %s\n", msg1);
        p("removeUpperCase first toLowercase then apply: %s\n",
                removeUpperCase.compose(toLowerCase).apply(msg1));
        p("toLowerCase first removeUpperCase then apply: %s\n",
                toLowerCase.compose(removeUpperCase).apply(msg1));

        Function<Integer,Integer> times2 = x -> x * 2;
        Function<Integer,Integer> square = x -> x * x;
        p("functional composition ordering\n");
        // 4*4=16 then 16*2 = 32
        p("times2.compose(square).apply(4) = %d\n",
                times2.compose(square).apply(4));
        // 4*2=8  then 8*8  = 64
        p("times2.andThen(square).apply(4) = %d\n",
                times2.andThen(square).apply(4));
    }

    public void testBiFunction() {
        BiFunction<Integer,Integer,Integer> bf1 = (x,y) -> x+y;
        BiFunction<Integer,String,Integer> bf2 = (x,y) -> x+Integer.parseInt(y);
        BiFunction<Integer,Integer,Double> bf3 = (x,y) -> {
            int a = x * y;
            return 0.5 * a;
        };
        p("bifunctionTest\n");
        p("add is %d\n", bf1.apply(10,20));
        p("add is %d\n", bf2.apply(10,"30"));
        p("half is %5.2f\n", bf3.apply(3,5));
    }

    public void testStream1() {
        List<Integer> listI = Arrays.asList(0,1,2,3,4,5,6,7,8,9);

        List<Integer> plus1 = listI.stream().map(x -> x+10).collect(Collectors.toList());
        plus1.stream().forEach(x -> p("%d ",x));
        p("\n");

        listI.stream().map(x -> x+10).forEach(x -> p("%d ", x));
        p("\n");

        List<ObjectGeneric> list0 = org.wayne.java.Objects.createListObjectGeneric(20);
        p("original ObjectGeneric list:\n");
        list0.stream().forEach(x -> x.p());
        p("filter out all false\n");
        list0.stream().filter(x -> x.is1()).forEach(x -> x.p());
        p("filter out all false and under 10\n");
        list0.stream().filter(x -> x.is1() && x.id() >= 10).forEach(x->x.p());

        p("reverse sorting by id function\n");
        Function<List<ObjectGeneric>, List<ObjectGeneric>> listReverse =
                list->list
                        .stream()
                        .sorted((x,y) -> x.id().compareTo(y.id()))
                        .collect(Collectors.toList());
    }

    public void testStream2() {
        List<ObjectGenericNested> list0 = org.wayne.java.Objects.createListObjectGenericNested(10);
        p("original ObjectGeneric list:\n");
        list0.stream().forEach(x -> x.p());
        p("filter out all false\n");
        list0.stream().filter(x -> x.is1()).forEach(x -> x.p());
        p("filter out all false and odd\n");
        list0.stream().filter(x -> x.is1() && x.id() % 2 == 1).forEach(x->x.p());

        // functional composition example, getObjObj uses getObj
        Function<ObjectGenericNested,ObjectGenericNested> getObj = x -> x.obj();
        Function<ObjectGenericNested,Long> getId = x -> x.id();
        Function<ObjectGenericNested,ObjectGenericNested>
                getObjObj = getObj.andThen(getObj);

        p("filter out all false and odd, and print their child\n");
        list0.stream()
                .filter(x->x.is1() && x.id() % 2 == 1)
                .map(getObj)
                .forEach(x->x.p());

        p("filter out all false and odd, and print their child's child\n");
        list0.stream()
                .filter(x->x.is1() && x.id() % 2 == 1)
                .map(getObjObj)
                .forEach(x->x.p());

    }

    public void testStream3() {
        List<ObjectGenericNested> list0 = org.wayne.java.Objects.createListObjectGenericNested(20);
        p("original ObjectGeneric list:\n");
        list0.stream().forEach(x -> x.p());

        p("filter out odd ids and transform to return list of names\n");
        List<Long> list1 = list0
                .stream()
                .filter(x->x.id()%2==0)
                .map(x->x.id())
                .collect(Collectors.toList());
        list1.forEach(x->p("%d\n", x));
    }

    public void testStreamFilterMap() {
        Map<Integer,Integer> map = new HashMap<>();
        for(int i = 0; i < 10; i++) {
            map.put(i, i+100);
        }
        Map<Integer,Integer> mapRes = map
                .entrySet()
                .stream()
                .filter(e -> e.getValue() % 2 == 0)
                .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
        mapRes
                .entrySet()
                .stream()
                .forEach(e -> p("k:%d v:%d\n", e.getKey(), e.getValue()));
    }

    public void testFunctionComposition1() {
        /*
        functions are method that receives one value and returns another.

        bifunctions receive two values and returns one value.

        operators are functions that receive and return same value type.
            operators are extensions of functions, use apply.

        primitive types use IntFunction,LongFunction,etc.

        suppliers do not take argument, have get() method, for lazy init.

        consumers receive and returns nothing, eg forEach.

        predicate is function that receives value and returns boolean, eg
            predicate is used in filter(predicate).

         */
        BinaryOperator<Integer> binOpAdd = (a,b) -> a + b;
        BinaryOperator<Integer> binOpSub = (a,b) -> a - b;
        BinaryOperator<Integer> binOpXor = (a,b) -> a ^ b;
        BinaryOperator<Integer> binOpMul = (a,b) -> a * b;
        BinaryOperator<Integer> binOpOr  = (a,b) -> a | b;
        BinaryOperator<Integer> binOpAnd = (a,b) -> a & b;
        BiFunction<Integer,Integer,Integer> fOpAdd = (a,b) -> a + b;
        BiFunction<Integer,Integer,Integer> fOpSub = (a,b) -> a - b;
        BiFunction<Integer,Integer,Integer> fOpXor = (a,b) -> a ^ b;
        BiFunction<Integer,Integer,Integer> fOpMul = (a,b) -> a * b;
        BiFunction<Integer,Integer,Integer> fOpOr  = (a,b) -> a | b;
        BiFunction<Integer,Integer,Integer> fOpAnd = (a,b) -> a & b;

        //BinaryOperator<BiFunction<Integer,Integer,Integer>> binOpFunc1 = (a,b) -> x -> a.apply(b.apply(x));

        //Function<Integer,Integer> f1 = binOpFunc1.apply(binOpAdd,binOpMul);

        Integer res = binOpAdd.apply(3,4);
        p("3+4 = %d\n",res);

    }

    public void testStreamFlatMap() {
        // flatmap flattens several collections
        List<Integer> list1 = Arrays.asList(0,1,2,3,4);
        List<Integer> list2 = Arrays.asList(10,11,12,13,14);
        List<Integer> list3 = Arrays.asList(20,21,22,23,24);

        Stream.of(1,2,3).map(x -> x + 1).forEach(x-> p("%d ",x));
        p("\n");

        Stream.of(list1,list2,list3).flatMap(x->x.stream()).forEach(x->p("%d ",x));
        p("\n");

        List<List<Integer>> listl = Arrays.asList(list1,list2,list3);
        listl.stream().flatMap(x->x.stream()).forEach(x->p("%d ",x));
        p("\n");
    }

    public void testStream4() {
        List<Integer> list = Arrays.asList(0,1,2,3,4,5,6,7,8,9);
        boolean isAnyMatch08 = list.stream().anyMatch(i -> i == 8);
        boolean isAnyMatch12 = list.stream().anyMatch(i -> i == 12);
        p("testStream match 8:%s 12:%s\n", isAnyMatch08, isAnyMatch12);
    }

    public void test() {
        //p("lambda\n"); testLambda1();
        //p("function\n"); testFunction();
        //p("bifunction\n"); testBiFunction();
        //p("streams1\n"); testStreams1();
        //p("streams2\n"); testStream2();
        //p("fibonacciSupplier\n"); testSupplier1();
        //p("streams3\n"); testStream3();
        //p("testFunctionalComposition1\n"); testFunctionalComposition1();
        //p("testStreamFlatMap\n"); testStreamFlatMap();
        testLambda2();
    }

    public static void main(String [] args) {
        MyLambda t = new MyLambda();
        t.test();
    }
}
