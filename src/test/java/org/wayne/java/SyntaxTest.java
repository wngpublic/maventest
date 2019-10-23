package org.wayne.java;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.min;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wayne.misc.Utils;

import junit.framework.TestCase;

import javax.annotation.Nonnull;

/**
 * This class just tries out various classes in java, and does some misc calculation tests.
 */
public class SyntaxTest extends TestCase {
    Random random = new Random();
    Logger logger;

    public SyntaxTest() {
        logger = LoggerFactory.getLogger(this.getClass());
    }
    void p(String f, Object ...o) {
        System.out.printf(f, o);
    }
    /**
     * test tree map API
     */
    @Test
    public void testTreeMap() {
        TreeMap<Integer, Integer> tree = new TreeMap<>();
        tree.put(10, 10);
        tree.put(20, 20);
        tree.put(30, 30);
        tree.put(40, 40);
        tree.put(50, 50);
        tree.put(60, 60);
        tree.put(70, 70);
        tree.put(80, 80);
        tree.put(90, 90);

        List<Integer> list = new ArrayList<>();
        list.addAll(Arrays.asList(3,13,17));
        for(Integer k: list) {
            Integer ceilingKey = tree.ceilingKey(k);
            Integer floorKey = tree.floorKey(k);
            p("key:%2d ceilingKey:%2d floorKey:%2d\n",
                    k, ceilingKey, floorKey);

        }
        SortedMap<Integer,Integer> sortedMap = tree.subMap(25, 55);
        p("sortedMap: 35,55\n");
        for(Map.Entry<Integer, Integer> kv: sortedMap.entrySet()) {
            Integer k = kv.getKey();
            Integer v = kv.getValue();
            p("sorted map: k:%2d v:%2d\n", k, v);
        }
    }

    /**
     * bucketing tests a part of consistent hashing.
     * print 20 items into hash 11, which first map to
     * 2,3,4,5,6 buckets by using a treemap.
     *
     * With hash 11 and 2 buckets, the ranges are split into:
     * 0:5  = bucket 0
     * 6:11 = bucket 1
     *
     * With 3 buckets, they get split into:
     * 0:3          = b0
     * 6:9          = b1
     * 4:5,10,11    = b2
     *
     * With hash 11 and 4 buckets, the ranges are split into:
     * 0:2          = b0
     * 6:8          = b1
     * 4:5,10       = b2
     * 3,9,11       = b3
     *
     * Then with 5 buckets, the ranges are split into:
     * 0:1          = bucket 0
     * 6:7          = bucket 1
     * 4:5          = bucket 2
     * 3,9,11       = bucket 3
     * 2,8,10       = bucket 4
     *
     * Then with 6 buckets, the ranges are split into:
     * 0:1          = b0
     * 6:7          = b1
     * 4:5          = b2
     * 3,9          = b3
     * 2,8          = b4
     * 11,10        = b5
     */
    @Test
    public void testBucketing() {
        List<Integer> list = new ArrayList<>();
        int numItems = 25;
        for(int i = 1; i <= numItems; i++) {
            int v  = i;
            list.add(v);
        }
        Utils u = new Utils();
        int hash = 11;
        int minBucket = 4;
        int maxBucket = 8;
        for(int numBuckets = minBucket; numBuckets <= maxBucket; numBuckets++) {
            TreeMap<Integer,Integer> map = new TreeMap<>();
        }
    }
    @Test
    public void testArrayConversions() throws IOException {
        int size = 10;
        boolean isRandom = false;
        boolean flag = false;
        {
            p("integer conversion\n");
            List<Integer> listI = new ArrayList<>();
            int [] arrayI = new int[size];
            byte [] arrayB = new byte[size * 4];
            for(int i = 0; i < size; i++) {
                int v = (isRandom) ? random.nextInt() : i * 2 + 0x8000000;
                listI.add(v);
                arrayI[i] = v;
                for(int j = 0; j < 4; j++) arrayB[i * 4 + j] = (byte)(v >> 0);
            }
            {
                // change List<Integer> -> int []
                int [] intArrayI = new int[listI.size()];
                for(int i = 0; i < intArrayI.length; i++) {
                    intArrayI[i] = listI.get(i);
                }
                assert listI.size() == intArrayI.length;
                for(int i = 0; i < listI.size(); i++) {
                    assert listI.get(i) == intArrayI[i];
                }
            }
            {
                // change List<Integer> -> int []
                int [] intArrayI = listI.stream().mapToInt(i->i).toArray();
                assert listI.size() == intArrayI.length;
                for(int i = 0; i < listI.size(); i++)
                    assert listI.get(i) == intArrayI[i];

                intArrayI = listI.stream().mapToInt(Integer::intValue).toArray();
                assert listI.size() == intArrayI.length;
                for(int i = 0; i < listI.size(); i++)
                    assert listI.get(i) == intArrayI[i];
            }
            {
                List<Integer> list = IntStream.of(arrayI).boxed().collect(Collectors.toList());
                assert list.size() == listI.size();
                for(int i = 0; i < listI.size(); i++) {
                    assert listI.get(i).intValue() == list.get(i).intValue();
                }
            }
            {
                // change List<Integer> to byte array and back to List<Integer>
                int [] intArrayI = listI.stream().mapToInt(i->i).toArray();
                ByteArrayOutputStream byteArrayOutputStream1 = new ByteArrayOutputStream();
                for(Integer i: listI)
                    for (int j = 0; j < 4; j++)
                        byteArrayOutputStream1.write((i >> j * 8) & 0xff);

                byte [] byteArray1 = byteArrayOutputStream1.toByteArray();
                IntBuffer intBuffer1 = ByteBuffer.wrap(byteArray1).order(ByteOrder.LITTLE_ENDIAN).asIntBuffer();
                int [] intArray1 = new int[intBuffer1.remaining()];
                intBuffer1.get(intArray1);

                ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
                for(Integer i: listI)
                    for (int j = 0; j < 4; j++)
                        byteArrayOutputStream2.write((byte)(i >> j * 8));

                byte [] byteArray2 = byteArrayOutputStream2.toByteArray();
                IntBuffer intBuffer2 = ByteBuffer.wrap(byteArray2).order(ByteOrder.LITTLE_ENDIAN).asIntBuffer();
                int [] intArray2 = new int[intBuffer2.remaining()];
                intBuffer2.get(intArray2);

                ByteArrayOutputStream byteArrayOutputStream3 = new ByteArrayOutputStream();
                DataOutputStream dataOutputStream3 = new DataOutputStream(byteArrayOutputStream3);
                for(Integer i: listI) dataOutputStream3.writeInt(i);

                byte [] byteArray3 = byteArrayOutputStream3.toByteArray();
                IntBuffer intBuffer3 = ByteBuffer.wrap(byteArray3).asIntBuffer();
                int [] intArray3 = new int[intBuffer3.remaining()];
                intBuffer3.get(intArray3);

                assert byteArray1.length == byteArray2.length;
                for(int i = 0; i < byteArray1.length; i++) assert byteArray1[i] == byteArray2[i];

                assert intArray1.length == intArrayI.length;
                for(int i = 0; i < intArray1.length; i++) assert intArray1[i] == intArrayI[i];

                assert intArray1.length == intArray2.length;
                for(int i = 0; i < intArray1.length; i++) assert intArray1[i] == intArray2[i];

                assert intArray1.length == intArray3.length;
                for(int i = 0; i < intArray1.length; i++) assert intArray1[i] == intArray3[i];

                assert intArray1.length == listI.size();
                for(int i = 0; i < intArray1.length; i++) assert intArray1[i] == listI.get(i).intValue();

                List<Integer> list0 = new ArrayList<>();
                for(int i = 0; i < intArray1.length; i++) list0.add(intArray1[i]);
                List<Integer> list1 = Arrays.stream(intArray1).boxed().collect(Collectors.toList());
                List<Integer> list2 = IntStream.of(intArray1).boxed().collect(Collectors.toList());
                assert list0.size() == listI.size();
                assert list1.size() == listI.size();
                assert list2.size() == listI.size();
                for(int i = 0; i < listI.size(); i++) {
                    assert listI.get(i).intValue() == list0.get(i).intValue();
                    assert listI.get(i).intValue() == list1.get(i).intValue();
                    assert listI.get(i).intValue() == list2.get(i).intValue();
                }
            }
            {
                // change List<Integer> to base64
            }
        }
        {
            {
                List<String> listI = Arrays.asList("To","be","or","not","to","be",",","that","is","the","question",".");
                // List<String> to byte array and back
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
                for(String s: listI) {
                    dataOutputStream.writeUTF(s);
                }
                //listI.stream().forEach(str -> dataOutputStream.writeUTF(str));
                dataOutputStream.flush();
                byte [] byteArray = byteArrayOutputStream.toByteArray();
                dataOutputStream.close();

                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
                DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);
                List<String> listO = new ArrayList<>();
                while(dataInputStream.available() > 0) {
                    String s = dataInputStream.readUTF();
                    listO.add(s);
                }

                assert listI.size() == listO.size();
                for(int i = 0; i < listI.size(); i++) assert listI.get(i).equals(listO.get(i));
            }
            {
                // writing list of strings and ints into byte array and back
                List<String> list1 = Arrays.asList("The cat in the hat", "a rock on the road", "somewhere, out there");
                List<Integer> list2 = Arrays.asList(0x1000_0000, 0x1100_0000, 0x1110_0000);
                List<String> list3 = Arrays.asList("around the world", "around the block");
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
                for(String s: list1) dataOutputStream.writeUTF(s);
                for(Integer i: list2) dataOutputStream.writeInt(i);
                for(String s: list3) dataOutputStream.writeUTF(s);
                dataOutputStream.flush();
                byte [] byteArray = byteArrayOutputStream.toByteArray();
                dataOutputStream.close();

                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
                DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);
                List<String> list1o = new ArrayList<>();
                List<Integer> list2o = new ArrayList<>();
                List<String> list3o = new ArrayList<>();
                list1o.add(dataInputStream.readUTF());
                list1o.add(dataInputStream.readUTF());
                list1o.add(dataInputStream.readUTF());
                list2o.add(dataInputStream.readInt());
                list2o.add(dataInputStream.readInt());
                list2o.add(dataInputStream.readInt());
                list3o.add(dataInputStream.readUTF());
                list3o.add(dataInputStream.readUTF());

                assert list1.size() == list1o.size();
                assert list2.size() == list2o.size();
                assert list3.size() == list3o.size();
                for(int i = 0; i < list1.size(); i++) assert list1.get(i).equals(list1o.get(i));
                for(int i = 0; i < list2.size(); i++) assert list2.get(i).equals(list2o.get(i));
                for(int i = 0; i < list3.size(); i++) assert list3.get(i).equals(list3o.get(i));
            }
            {
                // ByteBuffer conversion and base64
                List<String> list1 = Arrays.asList("The cat in the hat", "a rock on the road", "somewhere, out there", UUID.randomUUID().toString());
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
                for(String s: list1) dataOutputStream.writeUTF(s);
                dataOutputStream.flush();
                byte [] byteArray = byteArrayOutputStream.toByteArray();
                dataOutputStream.close();
                byte [] byteArrayEncoded = Base64.getEncoder().encode(byteArray);
                ByteBuffer byteBuffer = ByteBuffer.allocate(byteArrayEncoded.length);
                byteBuffer.put(byteArrayEncoded);

                byte [] byteArrayDecoded = Base64.getDecoder().decode(byteBuffer.array());
                assert byteArray.length == byteArrayDecoded.length;
                for(int i = 0; i < byteArray.length; i++) assert byteArray[i] == byteArrayDecoded[i];

                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayDecoded);
                DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);
                List<String> list1o = new ArrayList<>();
                while(dataInputStream.available() > 0) list1o.add(dataInputStream.readUTF());
                assert list1.size() == list1o.size();
                for(int i = 0; i < list1.size(); i++) assert list1.get(i).equals(list1o.get(i));
            }
            {
                // List<Integer> to Object [] array to Integer [].
                List<Integer> listI = Arrays.asList(0x100_0000,0x200_0000,0x300_0000,0x400_0000,0x500_0000,0x600_0000);
                Integer [] arrayI = listI.toArray(new Integer[listI.size()]);
                assert arrayI.length == listI.size();
                for(int i = 0; i < listI.size(); i++) assert listI.get(i).intValue() == arrayI[i].intValue();
            }
        }
        {
            // use stream map to see if null or empty get added
            Map<String,String> map = new HashMap<>();
            map.put("k1","v1");
            map.put("k2","v2");
            List<String> ak1 = Arrays.asList("k1","k2","k3","k4","k5");
            List<String> av1 = ak1.stream().map(k -> map.get(k)).filter(v -> v != null).collect(Collectors.toList());
            assert av1.size() == 2;
            assert map.get(null) == null;
            assert map.get("") == null;
        }
        p("testArrayConversions passed\n");
    }

    @Test
    public void testParseDouble() {
        List<String> list = Arrays.asList("1","1.1","1.2");
        boolean flag = false;

        try {
            double v = Double.parseDouble("1");
            assert v == 1;
            assert v == 1.0;
        } catch(Exception e) {
            assert flag;
        }
        try {
            double v = Double.parseDouble("1.1");
            assert v == 1.1;
        } catch(Exception e) {
            assert flag;
        }
        try {
            double v = Double.parseDouble("1.2.3");
            assert flag;
        } catch(NumberFormatException e) {
            flag = true;
        }
        assert flag;
        assert StringUtils.isBlank("");
        assert StringUtils.isBlank(null);
        assert !"1.1".equals(null);
        assert "1.1".equals("1.1");
        assert !"1".equals("1.0");
    }

    class GridRain {
        public int x;
        public int y;
        public int h;
        public boolean visited;
        public int visitedCtr = 0;
        public int num = 1;
        public GridRain sink;
        public GridRain(int x, int y, int h) {
            this.x = x;
            this.y = y;
            this.h = h;
            this.sink = this;
        }
        public void incVisited() {
            visited = true;
            visitedCtr++;
        }
        public boolean visited() {
            return getVisited() != 0;
        }
        public int getVisited() {
            return visitedCtr;
        }
    }

    class GridRain2 {
        public int x;
        public int y;
        public boolean visited;
        public int num = 1;
        public GridRain2(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    public void printRainfall(List<List<GridRain>> ll, String msg) {
        p("%s in:\n", msg);
        for(int i = 0; i < ll.size(); i++) {
            for(int j = 0; j < ll.get(i).size(); j++) {
                p("%2d ", ll.get(i).get(j).h);
            }
            p("\n");
        }
        p("%s out\n", msg);
        for(int i = 0; i < ll.size(); i++) {
            for(int j = 0; j < ll.get(i).size(); j++) {
                p("%2d ", ll.get(i).get(j).num);
            }
            p("\n");
        }
        p("\n");
    }
    public void testRainfallPQ1(int m[][]) {
        int maxx = m[0].length;
        int maxy = m.length;
        PriorityQueue<GridRain> q = new PriorityQueue<>(new Comparator<GridRain>() {
            @Override
            public int compare(GridRain o1, GridRain o2) {
                if(o1.h == o2.h) return 0;
                return (o1.h < o2.h) ? 1 : -1;
            }
        });
        List<List<GridRain>> ll = new ArrayList<>();
        /*
        for(int i = 0; i < maxy; i++) {
            List<GridRain> l = new ArrayList<>();
            ll.add(l);
            for(int j = 0; j < maxx; j++) {
                GridRain g = new GridRain(j,i,m[i][j]);
                q.add(g);
                l.add(g);
            }
        }
        */
        for(int i = 0; i < maxy; i++) {
            List<GridRain> l = new ArrayList<>();
            ll.add(l);
            for(int j = 0; j < maxx; j++) {
                GridRain g = new GridRain(j,i,m[i][j]);
                l.add(g);
            }
        }
        for(int i = maxy-1; i >= 0; i--) {
            for(int j = 0; j < maxx; j++) {
                q.add(ll.get(i).get(j));
            }
        }


        while(q.size() != 0) {
            GridRain g = q.poll();
            GridRain min = g;
            /*
            if(g.y > 0          && ll.get(g.y-1).get(g.x).h <= g.h && ll.get(g.y-1).get(g.x).h <= min.h)    min = ll.get(g.y-1).get(g.x);
            if(g.x < (maxx-1)   && ll.get(g.y).get(g.x+1).h <= g.h && ll.get(g.y).get(g.x+1).h <= min.h)    min = ll.get(g.y).get(g.x+1);
            if(g.y < (maxy-1)   && ll.get(g.y+1).get(g.x).h <= g.h && ll.get(g.y+1).get(g.x).h <= min.h)    min = ll.get(g.y+1).get(g.x);
            if(g.x > 0          && ll.get(g.y).get(g.x-1).h <= g.h && ll.get(g.y).get(g.x-1).h <= min.h)    min = ll.get(g.y).get(g.x-1);
            */

            if(g.y > 0          && !ll.get(g.y-1).get(g.x).visited && ll.get(g.y-1).get(g.x).h <= g.h && ll.get(g.y-1).get(g.x).h <= min.h)    min = ll.get(g.y-1).get(g.x);
            if(g.x < (maxx-1)   && !ll.get(g.y).get(g.x+1).visited && ll.get(g.y).get(g.x+1).h <= g.h && ll.get(g.y).get(g.x+1).h <= min.h)    min = ll.get(g.y).get(g.x+1);
            if(g.y < (maxy-1)   && !ll.get(g.y+1).get(g.x).visited && ll.get(g.y+1).get(g.x).h <= g.h && ll.get(g.y+1).get(g.x).h <= min.h)    min = ll.get(g.y+1).get(g.x);
            if(g.x > 0          && !ll.get(g.y).get(g.x-1).visited && ll.get(g.y).get(g.x-1).h <= g.h && ll.get(g.y).get(g.x-1).h <= min.h)    min = ll.get(g.y).get(g.x-1);

            /*
            if(g.y > 0          && !ll.get(g.y-1).get(g.x).visited && ll.get(g.y-1).get(g.x).h <= g.h && ll.get(g.y-1).get(g.x).h <= min.h)    min = ll.get(g.y-1).get(g.x);
            else if(g.x < (maxx-1)   && !ll.get(g.y).get(g.x+1).visited && ll.get(g.y).get(g.x+1).h <= g.h && ll.get(g.y).get(g.x+1).h <= min.h)    min = ll.get(g.y).get(g.x+1);
            else if(g.y < (maxy-1)   && !ll.get(g.y+1).get(g.x).visited && ll.get(g.y+1).get(g.x).h <= g.h && ll.get(g.y+1).get(g.x).h <= min.h)    min = ll.get(g.y+1).get(g.x);
            else if(g.x > 0          && !ll.get(g.y).get(g.x-1).visited && ll.get(g.y).get(g.x-1).h <= g.h && ll.get(g.y).get(g.x-1).h <= min.h)    min = ll.get(g.y).get(g.x-1);
            */

            if(min != g) {
                min.num += g.num;
                g.num = 0;
            }
            g.visited = true;
        }
        printRainfall(ll, "max");
    }
    public void testRainfallPQ(int m[][]) {
        int maxx = m[0].length;
        int maxy = m.length;
        PriorityQueue<GridRain> q = new PriorityQueue<>(new Comparator<GridRain>() {
            @Override
            public int compare(GridRain o1, GridRain o2) {
                if(o1.h == o2.h) return 0;
                return (o1.h < o2.h) ? 1 : -1;
            }
        });
        List<List<GridRain>> ll = new ArrayList<>();
        for(int i = 0; i < maxy; i++) {
            List<GridRain> l = new ArrayList<>();
            ll.add(l);
            for(int j = 0; j < maxx; j++) {
                GridRain g = new GridRain(j,i,m[i][j]);
                q.add(g);
                l.add(g);
            }
        }
        while(q.size() != 0) {
            GridRain g = q.poll();
            GridRain min = g;

            if(g.y > 0          && !ll.get(g.y-1).get(g.x).visited && ll.get(g.y-1).get(g.x).h <= g.h && ll.get(g.y-1).get(g.x).h <= min.h)    min = ll.get(g.y-1).get(g.x);
            if(g.x < (maxx-1)   && !ll.get(g.y).get(g.x+1).visited && ll.get(g.y).get(g.x+1).h <= g.h && ll.get(g.y).get(g.x+1).h <= min.h)    min = ll.get(g.y).get(g.x+1);
            if(g.y < (maxy-1)   && !ll.get(g.y+1).get(g.x).visited && ll.get(g.y+1).get(g.x).h <= g.h && ll.get(g.y+1).get(g.x).h <= min.h)    min = ll.get(g.y+1).get(g.x);
            if(g.x > 0          && !ll.get(g.y).get(g.x-1).visited && ll.get(g.y).get(g.x-1).h <= g.h && ll.get(g.y).get(g.x-1).h <= min.h)    min = ll.get(g.y).get(g.x-1);

            if(min != g) {
                min.num += g.num;
                g.num = 0;
            }
            g.visited = true;
        }
        printRainfall(ll, "max");
    }
    public void testRainfallPQ2(int m[][]) {
        int maxx = m[0].length;
        int maxy = m.length;
        List<List<GridRain>> ll = new ArrayList<>();
        PriorityQueue<GridRain> q = new PriorityQueue<>(new Comparator<GridRain>() {
            @Override
            public int compare(GridRain o1, GridRain o2) {
                if(o1.h == o2.h) return 0;
                if(o1.h < o2.h) return 1;
                return -1;
            }
        });
        for(int i = 0; i < m.length; i++) {
            List<GridRain> l = new ArrayList<>();
            ll.add(l);
            for(int j = 0; j < m[i].length; j++) {
                GridRain g = new GridRain(j,i,m[i][j]);
                q.add(g);
                l.add(g);
            }
        }
        {
            while(q.size() != 0) {
                GridRain g = q.poll();
                // check u r d l for min has not been visited yet.
                GridRain min = null;
                int minh = g.h;
                if(g.y > 0          && !ll.get(g.y-1).get(g.x).visited && ll.get(g.y-1).get(g.x).h <= g.h && ll.get(g.y-1).get(g.x).h <= minh)    min = ll.get(g.y-1).get(g.x);
                if(g.x < (maxx-1)   && !ll.get(g.y).get(g.x+1).visited && ll.get(g.y).get(g.x+1).h <= g.h && ll.get(g.y).get(g.x+1).h <= minh)    min = ll.get(g.y).get(g.x+1);
                if(g.y < (maxy-1)   && !ll.get(g.y+1).get(g.x).visited && ll.get(g.y+1).get(g.x).h <= g.h && ll.get(g.y+1).get(g.x).h <= minh)    min = ll.get(g.y+1).get(g.x);
                if(g.x > 0          && !ll.get(g.y).get(g.x-1).visited && ll.get(g.y).get(g.x-1).h <= g.h && ll.get(g.y).get(g.x-1).h <= minh)    min = ll.get(g.y).get(g.x-1);
                if(g == ll.get(0).get(2)) {
                    boolean isTrue = true;
                }
                if(min != null) {
                    min.num += g.num;
                    g.num = 0;
                }
                g.visited = true;
            }
        }
        for(int i = 0; i < m.length; i++) {
            for(int j = 0; j < m[i].length; j++) {
                GridRain g = ll.get(i).get(j);
                g.visited = false;
                q.add(g);
            }
        }
        {
            while(q.size() != 0) {
                GridRain g = q.poll();
                // check u r d l for min has not been visited yet.
                GridRain min = g;
                if(g.y > 0          && !ll.get(g.y-1).get(g.x).visited && ll.get(g.y-1).get(g.x).h <= g.h && ll.get(g.y-1).get(g.x).h <= min.h)    min = ll.get(g.y-1).get(g.x);
                if(g.x < (maxx-1)   && !ll.get(g.y).get(g.x+1).visited && ll.get(g.y).get(g.x+1).h <= g.h && ll.get(g.y).get(g.x+1).h <= min.h)    min = ll.get(g.y).get(g.x+1);
                if(g.y < (maxy-1)   && !ll.get(g.y+1).get(g.x).visited && ll.get(g.y+1).get(g.x).h <= g.h && ll.get(g.y+1).get(g.x).h <= min.h)    min = ll.get(g.y+1).get(g.x);
                if(g.x > 0          && !ll.get(g.y).get(g.x-1).visited && ll.get(g.y).get(g.x-1).h <= g.h && ll.get(g.y).get(g.x-1).h <= min.h)    min = ll.get(g.y).get(g.x-1);
                if(min != g) {
                    min.num += g.num;
                    g.num = 0;
                }
                g.visited = true;
            }
        }

        printRainfall(ll, "max2");
    }
    public void testRainfallBFSN(int m[][]) {
        int maxx = m[0].length;
        int maxy = m.length;
        List<List<GridRain>> ll = new ArrayList<>();
        LinkedList<GridRain> q = new LinkedList<>();

        for(int i = 0; i < m.length; i++) {
            List<GridRain> l = new ArrayList<>();
            ll.add(l);
            for(int j = 0; j < m[i].length; j++) {
                GridRain g = new GridRain(j,i,m[i][j]);
                l.add(g);
            }
        }

        /*
        for all bigger than min, add to min. assume anything bigger than itself is ok, does not have to be the the biggest.
         */
        q.add(ll.get(0).get(0));
        GridRain min = ll.get(0).get(0);
        while(q.size() != 0) {
            GridRain g = q.pollFirst();
            g.visited = true;
            if(g.y > 0) {
                GridRain o = ll.get(g.y-1).get(g.x);
                if(o.h >= g.h && o.h >= min.h) {
                    o.num += g.num; g.num = 0;
                }
                if(!o.visited) q.add(o);
            }
            if(g.x < (maxx-1)) {
                GridRain o = ll.get(g.y).get(g.x+1);
                if(o.h <= g.h && o.h <= min.h) {
                    o.num += g.num; g.num = 0;
                }
                if(!o.visited) q.add(o);
            }
            if(g.y < (maxy-1)) {
                GridRain o = ll.get(g.y+1).get(g.x);
                if(o.h <= g.h && o.h <= min.h) {
                    o.num += g.num; g.num = 0;
                }
                if(!o.visited) q.add(o);
            }
            if(g.x > 0) {
                GridRain o = ll.get(g.y).get(g.x-1);
                if(o.h <= g.h && o.h <= min.h) {
                    o.num += g.num; g.num = 0;
                }
                if(!o.visited) q.add(o);
            }
        }
        printRainfall(ll, "bfs");
    }

    public void testRainfallBFS(int m[][]) {
        int maxx = m[0].length;
        int maxy = m.length;
        List<List<GridRain>> ll = new ArrayList<>();
        LinkedList<GridRain> q = new LinkedList<>();

        for(int i = 0; i < m.length; i++) {
            List<GridRain> l = new ArrayList<>();
            ll.add(l);
            for(int j = 0; j < m[i].length; j++) {
                GridRain g = new GridRain(j,i,m[i][j]);
                l.add(g);
            }
        }

        /*
        for all bigger than min, add to min. assume anything bigger than itself is ok, does not have to be the the biggest.
         */
        q.add(ll.get(0).get(0));
        while(q.size() != 0) {
            GridRain g = q.pollFirst();
            g.visited = true;
            if(g.y > 0) {
                GridRain o = ll.get(g.y-1).get(g.x);
                if(o.h <= g.h) {
                    o.num += g.num; g.num = 0;
                }
                if(!o.visited) q.add(o);
            }
            if(g.x < (maxx-1)) {
                GridRain o = ll.get(g.y).get(g.x+1);
                if(o.h <= g.h) {
                    o.num += g.num; g.num = 0;
                }
                if(!o.visited) q.add(o);
            }
            if(g.y < (maxy-1)) {
                GridRain o = ll.get(g.y+1).get(g.x);
                if(o.h <= g.h) {
                    o.num += g.num; g.num = 0;
                }
                if(!o.visited) q.add(o);
            }
            if(g.x > 0) {
                GridRain o = ll.get(g.y).get(g.x-1);
                if(o.h <= g.h) {
                    o.num += g.num; g.num = 0;
                }
                if(!o.visited) q.add(o);
            }
        }
        printRainfall(ll, "bfs");
    }
    public GridRain testRainfallDFSDP(GridRain g, List<List<GridRain>> ll) {
        int maxx = ll.get(0).size();
        int maxy = ll.size();
        g.visited = true;
        GridRain min = g;
        if(g.y > 0) {
            GridRain o = ll.get(g.y-1).get(g.x);    // U
            if(o.h <= min.h) min = (o.visited) ? o.sink : o;
        }
        if(g.x < (maxx-1)) {
            GridRain o = ll.get(g.y).get(g.x+1);    // R
            if(o.h <= min.h) min = (o.visited) ? o.sink : o;
        }
        if(g.y < (maxy-1)) {
            GridRain o = ll.get(g.y+1).get(g.x);    // D
            if(o.h <= min.h) min = (o.visited) ? o.sink : o;
        }
        if(g.x > 0) {
            GridRain o = ll.get(g.y).get(g.x-1);    // L
            if(o.h <= min.h) min = (o.visited) ? o.sink : o;
        }
        if(min != g) {
            min.num += g.num;
            g.num = 0;
            if(!min.visited) {
                min = testRainfallDFSDP(min, ll);
            }
        }
        g.sink = min;
        return min;
    }


    public void testRFDPDFSH2(GridRain g, GridRain o) {
        if(o.sink.h <= g.h) {
            g.sink = o.sink;
        }
        if(g != g.sink) {
            g.sink.num += g.num;
            g.num = 0;
        }
    }

    // this chooses first smallest, not smallest among a set
    public void testRFDPDFSH1(GridRain g, GridRain o) {
        if(o.sink.h < g.h) {
            g.sink = o.sink;
        }
        if(g != g.sink) {
            g.sink.num += g.num;
            g.num = 0;
        }
    }

    public boolean testRFDPDFSH1(GridRain g, GridRain o, List<List<GridRain>> ll) {
        if(o.h < g.h) {
            if(!o.visited()) {
                testRFDPDFS(o, ll);
            }
            testRFDPDFSH1(g, o);
            return true;
        }
        return false;
    }

    public boolean testRFDPDFSH2(GridRain g, GridRain o, List<List<GridRain>> ll) {
        if(o.h <= g.h) {
            if(!o.visited()) {
                testRFDPDFS(o, ll);
            }
            testRFDPDFSH2(g, o);
            return true;
        }
        return false;
    }

    public void testRFDPDFS(GridRain g, List<List<GridRain>> ll) {
        int maxx = ll.get(0).size();
        int maxy = ll.size();
        g.incVisited();
        if(g.y > 0) {
            GridRain o = ll.get(g.y-1).get(g.x);    // U
            if(testRFDPDFSH1(g, o, ll)) return;
        }
        if(g.x < (maxx-1)) {
            GridRain o = ll.get(g.y).get(g.x+1);    // R
            if(testRFDPDFSH1(g, o, ll)) return;
        }
        if(g.y < (maxy-1)) {
            GridRain o = ll.get(g.y+1).get(g.x);    // D
            if(testRFDPDFSH1(g, o, ll)) return;
        }
        if(g.x > 0) {
            GridRain o = ll.get(g.y).get(g.x-1);    // L
            if(testRFDPDFSH1(g, o, ll)) return;
        }

        // if nothing smaller, then look for equal height
        if(g.y > 0) {
            GridRain o = ll.get(g.y-1).get(g.x);    // U
            if(testRFDPDFSH2(g, o, ll)) return;
        }
        if(g.x < (maxx-1)) {
            GridRain o = ll.get(g.y).get(g.x+1);    // R
            if(testRFDPDFSH2(g, o, ll)) return;
        }
        if(g.y < (maxy-1)) {
            GridRain o = ll.get(g.y+1).get(g.x);    // D
            if(testRFDPDFSH2(g, o, ll)) return;
        }
        if(g.x > 0) {
            GridRain o = ll.get(g.y).get(g.x-1);    // L
            if(testRFDPDFSH2(g, o, ll)) return;
        }
    }

    public void testRainfallDPDFS(int m[][]) {
        int maxx = m[0].length;
        int maxy = m.length;
        List<List<GridRain>> ll = new ArrayList<>();

        for(int i = 0; i < m.length; i++) {
            List<GridRain> l = new ArrayList<>();
            ll.add(l);
            for(int j = 0; j < m[i].length; j++) {
                l.add(new GridRain(j,i,m[i][j]));
            }
        }
        // start bottom left
        for(int i = maxy-1; i >= 0; i--) {
            for(int j = 0; j < maxx; j++) {
                if(!ll.get(i).get(j).visited) {
                    GridRain g = ll.get(i).get(j);
                    testRFDPDFS(g, ll);
                }
            }
        }

        printRainfall(ll, "dpdfs");
        validateRainfallDPDFS(ll);

    }
    public void validateRainfallDPDFS(List<List<GridRain>> ll) {
        for(int i = 0; i < ll.size(); i++) {
            List<GridRain> l = ll.get(i);
            for(int j = 0; j < l.size(); j++) {
                assert l.get(j).getVisited() == 1;
            }
        }
    }
    public void testRFBFDFS(int m[][]) {
        int maxx = m[0].length;
        int maxy = m.length;
        List<List<GridRain>> ll = new ArrayList<>();
        PriorityQueue<GridRain> q = new PriorityQueue<>(new Comparator<GridRain>() {
            @Override
            public int compare(GridRain o1, GridRain o2) {
                if(o1.h == o2.h) return 0;
                return (o1.h < o2.h) ? 1 : -1;
            }
        });

        for(int i = 0; i < m.length; i++) {
            List<GridRain> l = new ArrayList<>();
            ll.add(l);
            for(int j = 0; j < m[i].length; j++) {
                GridRain g = new GridRain(j,i,m[i][j]);
                l.add(g);
                q.add(g);
            }
        }

        while(q.size() != 0){
            GridRain g = q.poll();
            // for each g, find the sink by doing dfs and dp.
            if(!g.visited) {
                g.sink = testRainfallDFSDP(g, ll);
            }
        }
        printRainfall(ll, "dfs1");
    }
    public GridRain testRFDFSH1(GridRain g, GridRain s, List<List<GridRain>> ll) {
        int maxx = ll.get(0).size();
        int maxy = ll.size();
        g.visited = true;
        GridRain newSink = null;
        // evaluate where the sink is, U R D L by finding min
        if(g.y > 0) {
            GridRain o = ll.get(g.y-1).get(g.x);    // U
            if(o.h >= g.h) {
                if(s.h < g.h) {
                    GridRain sinkNew = testRFDFSH1(o, s, ll);
                } else {
                    GridRain sinkNew = testRFDFSH1(o, g, ll);
                }
                if(s.h < g.h) {

                } else {

                }
            }
            GridRain sink = testRFDFSH1(g, o, ll);
        }
        if(g.x < (maxx-1)) {
            GridRain o = ll.get(g.y).get(g.x+1);    // R
            testRainfallDFSHelperUR(g, o, ll);
        }
        if(g.y < (maxy-1)) {
            GridRain o = ll.get(g.y+1).get(g.x);    // D
            testRainfallDFSHelperDL(g, o, ll);
        }
        if(g.x > 0) {
            GridRain o = ll.get(g.y).get(g.x-1);    // L
            testRainfallDFSHelperDL(g, o, ll);
        }

        if(newSink != null) {
            if(newSink.h < g.h) return (newSink.h < s.h) ? newSink : s;
            else                return (g.h < newSink.h) ? g : newSink;
        } else {
            return (g.h < s.h) ? g : s;
        }
    }

    public void testRFDFS(int m[][]) {
        int maxx = m[0].length;
        int maxy = m.length;
        List<List<GridRain>> ll = new ArrayList<>();

        for(int i = 0; i < m.length; i++) {
            List<GridRain> l = new ArrayList<>();
            ll.add(l);
            for(int j = 0; j < m[i].length; j++) {
                GridRain g = new GridRain(j,i,m[i][j]);
                l.add(g);
            }
        }

        for(int i = maxy-1; i >= 0; i--) {
            for(int j = 0; j < maxx; j++) {
                if(!ll.get(i).get(j).visited) {
                    GridRain g = ll.get(i).get(j);
                    testRFDFSH1(g, g, ll);
                }
            }
        }

        printRainfall(ll, "dfs");
    }
    public void testRainfallDFSHelperUR(GridRain g, GridRain o, List<List<GridRain>> ll) {
        if(o.h <= g.h) {
            o.num += g.num;
            g.num = 0;
        } else {
            g.num += o.num;
            o.num = 0;
        }
        if(!o.visited) {
            testRainfallDFSUD(o, ll);
        }
    }

    public void testRainfallDFSHelperDL(GridRain g, GridRain o, List<List<GridRain>> ll) {
        if(o.h < g.h) {
            o.num += g.num;
            g.num = 0;
        } else {
            g.num += o.num;
            o.num = 0;
        }
        if(!o.visited) {
            testRainfallDFSUD(o, ll);
        }
    }

    public void testRainfallDFSUD(GridRain g, List<List<GridRain>> ll) {
        int maxx = ll.get(0).size();
        int maxy = ll.size();
        g.visited = true;
        if(g.y > 0) {
            GridRain o = ll.get(g.y-1).get(g.x);    // U
            testRainfallDFSHelperUR(g, o, ll);
        }
        if(g.x < (maxx-1)) {
            GridRain o = ll.get(g.y).get(g.x+1);    // R
            testRainfallDFSHelperUR(g, o, ll);
        }
        if(g.y < (maxy-1)) {
            GridRain o = ll.get(g.y+1).get(g.x);    // D
            testRainfallDFSHelperDL(g, o, ll);
        }
        if(g.x > 0) {
            GridRain o = ll.get(g.y).get(g.x-1);    // L
            testRainfallDFSHelperDL(g, o, ll);
        }
    }

    public void testRainfallDFS(GridRain g, List<List<GridRain>> ll) {
        int maxx = ll.get(0).size();
        int maxy = ll.size();
        //if(g.y > 0          && !ll.get(g.y-1).get(g.x).visited && ll.get(g.y-1).get(g.x).h >= g.h)    min = ll.get(g.y-1).get(g.x);
        //if(g.x < (maxx-1)   && !ll.get(g.y).get(g.x+1).visited && ll.get(g.y).get(g.x+1).h >= g.h)    min = ll.get(g.y).get(g.x+1);
        if(g.x < (maxx-1) && !ll.get(g.y).get(g.x+1).visited && ll.get(g.y).get(g.x+1).h <= g.h){
            ll.get(g.y).get(g.x+1).num += g.num;
            g.num = 0;
            testRainfallDFS(ll.get(g.y).get(g.x+1), ll);
        }
        if(g.y > 0   && !ll.get(g.y-1).get(g.x).visited && ll.get(g.y-1).get(g.x).h <= g.h) {
            ll.get(g.y-1).get(g.x).num += g.num;
            g.num = 0;
            testRainfallDFS(ll.get(g.y-1).get(g.x), ll);
        }
        g.visited = true;
    }

    public void testRainfallDFS(int m[][]) {
        int maxx = m[0].length;
        int maxy = m.length;
        List<List<GridRain>> ll = new ArrayList<>();
        LinkedList<GridRain> q = new LinkedList<>();

        for(int i = 0; i < m.length; i++) {
            List<GridRain> l = new ArrayList<>();
            ll.add(l);
            for(int j = 0; j < m[i].length; j++) {
                GridRain g = new GridRain(j,i,m[i][j]);
                l.add(g);
            }
        }

        for(int i = maxy-1; i >= 0; i--) {
            //for(int j = maxx-1; j >= 0; j--) {
            for(int j = 0; j < maxx; j++) {
                if(!ll.get(i).get(j).visited) {
                    testRainfallDFSUD(ll.get(i).get(j), ll);
                    //testRainfallDFS(ll.get(i).get(j), ll);
                }
            }
        }

        printRainfall(ll, "dfs");
    }

    @Test
    public void testRainfallSingle() {
        {
            int m[][]  = {
                {2,2,2},
                {2,3,2},
                {2,2,2},
            };
            testRainfallDPDFS(m);
        }

        {
            int m[][]  = {
                {2,2,2},
                {2,1,2},
                {2,2,2},
            };
            testRainfallDPDFS(m);
        }
        {
            int m[][]  = {
                {1,2,3},
                {4,5,6}
            };
            testRainfallDPDFS(m);
        }
        {
            int m[][]  = {
                {1,2,1},
                {5,5,5}
            };
            testRainfallDPDFS(m);
        }
        {
            int m[][]  = {
                {10,11,12,13,15},
                { 9, 3, 2, 9,14},
                { 8, 4, 1, 8,15},
                { 7, 5, 6, 7,11},
                { 8, 7, 8, 9,10}
            };
            testRainfallDPDFS(m);
        }
        {
            int m[][]  = {
                {2,2,2},
                {2,2,2},
                {2,2,2},
            };
            testRainfallDPDFS(m);
        }
        {
            int m[][]  = {
                {2,2,2},
                {2,2,2},
                {1,2,1},
            };
            testRainfallDPDFS(m);
        }
        {
            int m[][]  = {
                {1,5,1},
                {3,2,4},
                {1,0,5},
            };
            testRainfallDPDFS(m);
        }
        {
            int m[][]  = {
                {10,11,12,13,10},
                { 9, 3, 2,11,14},
                { 8, 4,12, 8,15},
                { 7,11, 6, 7,11},
                {10, 7, 8, 9,10}
            };
            testRainfallDPDFS(m);
        }
        {
            int m[][]  = {
                {10,11, 8,13,14},
                { 9, 3, 2,14,14},
                { 8, 4,12, 8,15},
                { 7,11, 7, 6, 5},
                {10, 9, 8, 7, 8}
            };
            testRainfallDPDFS(m);
        }

    }

    @Test
    public void testRainfall() {
        {
            PriorityQueue<GridRain> q = new PriorityQueue<>(new Comparator<GridRain>() {
                @Override
                public int compare(GridRain o1, GridRain o2) {
                    if(o1.h == o2.h) return 0;
                    if(o1.h < o2.h) return 1;
                    return -1;
                }
            });
            q.add(new GridRain(0,0,3));
            q.add(new GridRain(0,0,2));
            q.add(new GridRain(0,0,1));
            q.add(new GridRain(0,0,2));
            List<GridRain> l = new ArrayList<>();
            while(q.size() != 0)
                l.add(q.poll());
            assert l.get(0).h == 3;
            assert l.get(1).h == 2;
            assert l.get(2).h == 2;
            assert l.get(3).h == 1;
        }

        {
            int m[][]  = {
                {1,2,3},
                {4,5,6}
            };
            testRainfallPQ(m);
            testRainfallDFS(m);
        }
        {
            int m[][]  = {
                {1,2,1},
                {5,5,5}
            };
            testRainfallPQ(m);
            testRainfallDFS(m);
        }
        {
            int m[][]  = {
                {2,2,2},
                {2,1,2},
                {2,2,2},
            };
            testRainfallPQ(m);
            testRainfallDFS(m);
        }
        {
            int m[][]  = {
                {2,2,2},
                {2,3,2},
                {2,2,2},
            };
            testRainfallPQ(m);
            testRainfallDFS(m);
        }
        {
            int m[][]  = {
                {2,2,2},
                {2,2,2},
                {2,2,2},
            };
            testRainfallPQ(m);
            testRainfallDFS(m);
        }
        {
            int m[][]  = {
                {4,3,2,3},
                {1,2,3,4},
                {2,3,4,5}
            };
            testRainfallPQ(m);
            testRainfallDFS(m);
        }
        {
            int m[][]  = {
                {4,3,2,3},
                {1,2,3,1},
                {2,3,4,5}
            };
            testRainfallPQ(m);
            testRainfallDFS(m);
        }
        {
            int m[][]  = {
                {4,3,4,3,2},
                {1,2,5,2,1},
                {2,3,4,5,3},
                {3,4,5,6,4}
            };
            testRainfallPQ(m);
            testRainfallDFS(m);
        }
        {
            int m[][]  = {
                {10,11,12,13,15},
                { 9, 3, 2, 9,14},
                { 8, 4, 1, 8,15},
                { 7, 5, 6, 7,11},
                { 8, 7, 8, 9,10}
            };
            testRainfallPQ(m);
            testRainfallDFS(m);
        }
    }

    void testI(Integer i) {
        i = 3;
        assert i == 3;
    }

    @Test
    public void testInt() {
        Integer i = 4;
        testI(i);
        assert i == 4;

        Integer j = 100;
        i = 100;
        assert i == j;
        assert i == 100;
        assert j == 100;

        // this direct comparison only works til -128/128
        i = 10000;
        j = 10000;
        assert i != j;
        assert i == 10000;
        assert j == 10000;
        assert i.intValue() == 10000;
        assert j.intValue() == 10000;
        assert i.intValue() == j.intValue();

        boolean flag = false;
        i = 0;
        j = 0;
        for(int iv = 0; iv < 10000; iv++, i++, j++) {
            if(i != j) {
                flag = true;
                break;
            }
        }
        assert i.intValue() == j.intValue() && i.intValue() == 128;
        p("testInt passed\n");
    }

    void printI(Integer i) {
        p("integer %d\n", i);
    }
    void printII(Integer i1, Integer i2) {
        p("biconsumer %d,%d\n", i1, i2);
    }
    void consumerThrowException(Integer i) throws Exception {
        throw new Exception(String.format("consumer exception %d", i));
    }
    void printI(Integer i, Consumer<Integer> consumer) {
        consumer.accept(i);
    }
    void printSimpleI(Integer i) {
        p("printSimpleInteger %d\n", i);
    }
    @Test
    public void testConsumerProducerLamba() {
        Consumer<Long> consumerLamba = (Long t) -> System.out.printf("consumer %d\n", t);
        consumerLamba.accept(new Long(5));
        consumerLamba.accept(new Long(10));
        p("testConsumerProducerLambda pass\n");
        Consumer<Integer> consumerILambda = (t) -> { printI(t + t); };
        consumerILambda.accept(2);
        consumerILambda.accept(5);
        BiConsumer<Integer,Integer> consumerIILambda = (i, j) -> { printII(i,j); };
        consumerIILambda.accept(2,3);
        consumerIILambda.accept(4,5);
        Consumer<Integer> consumerIException = (t) -> {
            try {
                consumerThrowException(t);
            } catch (Exception e) {
                p("Exceptions must be wrapped in accept: %s\n", e.getMessage());
            }
        };
        consumerIException.accept(2);
        Consumer<Integer> consumerISimple = (t) -> { printSimpleI(t); };
        printI(3,consumerISimple);
        printI(7,consumerISimple);

    }
    @Test
    public void testException() {
        /**
         * a RuntimeException throwable cannot be cast to IOException, get ClassCastException
         */
        {
            int result = 0;
            try {
                try {
                    try {
                        throw new RuntimeException("runtime exception");
                    } catch(Exception e) {
                        throw new WrappedRuntimeException(e);
                    }
                } catch(WrappedRuntimeException e) {
                    throw (IOException)e.throwable();
                }
            } catch(Exception e) {
                if      (e instanceof IOException) result = 1;
                else if (e instanceof ClassCastException) result = 2;
            }
            assert result == 2;
        }
        {
            int result = 0;
            try {
                try {
                    try {
                        throw new IOException("runtime exception");
                    } catch(Exception e) {
                        throw new WrappedRuntimeException(e);
                    }
                } catch(WrappedRuntimeException e) {
                    throw (IOException)e.throwable();
                }
            } catch(Exception e) {
                if      (e instanceof IOException) result = 1;
                else if (e instanceof ClassCastException) result = 2;
            }
            assert result == 1;
        }
        {
            // callable has internal throw/catch/rethrow and outer has catch

            ExecutorService executorService = Executors.newFixedThreadPool(1);
            Future<Integer> future = executorService.submit(() -> {
                try {
                    throw new RuntimeException("runtime_exception_1");
                } catch(Exception e) {
                    p("caught inner exception: %s\n", e.getMessage());
                    throw e;
                }
            });

            Integer i = null;
            try {
                i = future.get();
            } catch(Exception e) {
                p("caught outer exception: %s\n", e.getMessage());
            }

            p("calling shutdown\n");
            executorService.shutdown();

            while(!executorService.isShutdown()) {
                try {
                    p("awaiting termination\n");
                    executorService.awaitTermination(10, TimeUnit.SECONDS);
                } catch(InterruptedException e) {
                    logger.info(e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }
    @Test
    public void testSetList() {
        Set<Integer> set = new HashSet<>();
        List<Integer> list1 = Arrays.asList(1,2,3,4,5,5,5);
        List<Integer> list3 = new ArrayList<>(set);
        assert list3.size() == 0;
        set.addAll(list1);
        List<Integer> list2 = new ArrayList<>(set);
        assert list2.size() == 5;
        assert list3.size() == 0;
        assert set.containsAll(list2);
    }
    @Test
    public void testSetListPerformance() {
        Random r = new Random();
        int numRuns = 10000;
        long timeAccum=0;
        long timeAvg = 0;


        timeAccum=0;
        for(int i = 0; i < numRuns; i++) {
            List<Integer> l1 = new ArrayList<>();
            List<Integer> l2 = new ArrayList<>();
            for(int j = 0; j < 2000; j++) l1.add(j);
            for(int j = 0; j < 400; j++) l2.add(j);
            long timebegin = System.nanoTime();
            List<Integer> l3 = new ArrayList<>();
            l3.addAll(l1);
            l3.addAll(l2);
            assert l3.size() == 2400;
            long timeend = System.nanoTime();
            long timediff = timeend - timebegin;
            //long timediffmilli = timediff / 1000000;
            timeAccum+=timediff;
        }
        timeAvg = timeAccum/numRuns;
        p("\ntime elapse nano list:%d\n", timeAvg);


        timeAccum=0;
        for(int i = 0; i < numRuns; i++) {
            List<Integer> l1 = new ArrayList<>();
            List<Integer> l2 = new ArrayList<>();
            for(int j = 0; j < 2000; j++) l1.add(j);
            for(int j = 0; j < 400; j++) l2.add(j);
            long timebegin = System.nanoTime();

            Set<Integer> set = new HashSet<>();
            set.addAll(l1);
            set.addAll(l2);
            List<Integer> l3 = new ArrayList<>(set);
            assert set.size() == 2000;
            long timeend = System.nanoTime();
            long timediff = timeend - timebegin;
            //long timediffmilli = timediff / 1000000;
            timeAccum+=timediff;
        }
        timeAvg = timeAccum/numRuns;
        p("\ntime elapse nano set:%d\n", timeAvg);

        timeAccum=0;
        for(int i = 0; i < numRuns; i++) {
            List<Integer> l1 = new ArrayList<>();
            List<Integer> l2 = new ArrayList<>();
            for(int j = 0; j < 2000; j++) l1.add(j);
            for(int j = 0; j < 400; j++) l2.add(j);
            long timebegin = System.nanoTime();
            List<Integer> l3 = new ArrayList<>();
            l3.addAll(l1);
            l3.addAll(l2);
            assert l3.size() == 2400;
            long timeend = System.nanoTime();
            long timediff = timeend - timebegin;
            //long timediffmilli = timediff / 1000000;
            timeAccum+=timediff;
        }
        timeAvg = timeAccum/numRuns;
        p("\ntime elapse nano list:%d\n", timeAvg);


        timeAccum=0;
        for(int i = 0; i < numRuns; i++) {
            List<Integer> l1 = new ArrayList<>();
            List<Integer> l2 = new ArrayList<>();
            for(int j = 0; j < 2000; j++) l1.add(j);
            for(int j = 0; j < 400; j++) l2.add(j);
            long timebegin = System.nanoTime();

            Set<Integer> set = new HashSet<>();
            set.addAll(l1);
            set.addAll(l2);
            List<Integer> l3 = new ArrayList<>(set);
            assert set.size() == 2000;
            long timeend = System.nanoTime();
            long timediff = timeend - timebegin;
            //long timediffmilli = timediff / 1000000;
            timeAccum+=timediff;
        }
        timeAvg = timeAccum/numRuns;
        p("\ntime elapse nano set:%d\n", timeAvg);

    }

    @Test
    public void testClassname() {
        Class<?> clazz = getClass();
        Class<?> enclosingClazz = getClass().getEnclosingClass();
        Optional<Class<?>> opt1 = Optional.ofNullable(clazz);
        Optional<Class<?>> opt2 = Optional.ofNullable(enclosingClazz);
        opt1.ifPresent(x -> p("clazz %s\n", x.getName()));
        opt2.ifPresent(x -> p("enclosingclazz %s\n", x.getName()));
        assert clazz != null;
        assert enclosingClazz == null;
    }

    @Test
    public void testString() {
        String s1;
        List<String> a1;

        s1 = "abc,aaa bbb & ccc,aba";
        a1 = Arrays.asList(s1.split(","));
        assert a1.size() == 3;

        s1 = "abc";
        a1 = Arrays.asList(s1.split(","));
        assert a1.size() == 1;

        s1 = "";
        assert StringUtils.isEmpty(s1);
        a1 = Arrays.asList();
        assert a1.size() == 0;

        s1 = null;
        assert StringUtils.isEmpty(s1);
        a1 = Arrays.asList();
        assert a1.size() == 0;

        a1 = StringUtils.isEmpty(s1) ? Collections.emptyList() : Arrays.asList(s1.split(","));
        assert a1.size() == 0;

        s1 = "abc,aaa bbb & ccc,aba";
        a1 = StringUtils.isEmpty(s1) ? Collections.emptyList() : Arrays.asList(s1.split(","));
        assert a1.size() == 3;

        boolean expected = false;
        // can you add to Arrays.asList? nope
        try {
            a1 = Arrays.asList("aaa abb","bbb");
            a1.add("ccc");
            assert false;
        } catch(UnsupportedOperationException e) {
            expected = true;
        }
        assert expected;

        // but this is ok
        try {
            a1 = new ArrayList<>(Arrays.asList("aaa abb","bbb"));
            a1.add("ccc");
            a1 = new ArrayList<>(Arrays.asList());
            a1.add("ccc");
        } catch(UnsupportedOperationException e) {
            assert false;
        }
        assert expected;

        List<String> lists = null;
        int ctr = 0;
        try {
            for(String s: lists) {
                ctr++;
            }
            expected = false;
        } catch(Exception e) {
            expected = true;
        }
        assert expected;
        assert ctr == 0;

        lists = new ArrayList<>();
        ctr = 0;
        try {
            for(String s: lists) {
                ctr++;
            }
            expected = true;
        } catch(Exception e) {
            expected = false;
        }
        assert expected;
        assert ctr == 0;

        lists = new ArrayList<>();
        List<String> list1 = null;
        List<String> list2 = new ArrayList<>();
        List<String> list3 = Arrays.asList("v1","v2","v3","v4");
        try {
            lists.addAll(list1);
            expected = false;
        } catch(NullPointerException e) {
            expected = true;
        }
        assert expected;
        lists.addAll(list2);
        lists.addAll(list3);
        assert lists.size() == 4;

        Integer i;
        String s;
        try {
            i = null;
            s = String.valueOf(i);
            assert s != null && "null".equals(s);
            expected = true;
        } catch(NullPointerException e) {
            expected = false;
        }
        assert expected;

        try {
            i = null;
            s = Optional.ofNullable(i).map(x -> String.valueOf(x)).orElse(null);
            assert s == null;
            expected = true;
        } catch(NullPointerException e) {
            expected = false;
        }
        assert expected;

        p("\n----------passed testString-------\n");
    }
    @Test
    public void testInitializations() {
        Integer i;
        //assert i == null; // must initialize, else get error!
        i = null;
        assert i == null; // must initialize, else get error!
    }
}
