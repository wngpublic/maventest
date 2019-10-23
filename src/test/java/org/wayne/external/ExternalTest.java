package org.wayne.external;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.ByteBufferBackedOutputStream;
import lombok.Builder;
import lombok.Data;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;

import java.io.BufferedReader;
import java.io.EOFException;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.json.JSONObject;
import org.junit.Test;
import org.apache.commons.collections4.CollectionUtils;
import org.roaringbitmap.RoaringBitmap;
import org.roaringbitmap.buffer.ImmutableRoaringBitmap;
import org.roaringbitmap.buffer.MutableRoaringBitmap;
import org.wayne.misc.Utils;
import org.roaringbitmap.IntConsumer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Iterator;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.zip.CRC32;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import junit.framework.TestCase;
import sun.misc.CRC16;

public class ExternalTest extends TestCase {
    Random random = new Random();
    Random r = new Random();
    ObjectMapper objectMapper = new ObjectMapper();

    void p(String f, Object ...o) {
        System.out.printf(f, o);
    }
    public void tLogback100() {
        Logger logger = LoggerFactory.getLogger(this.getClass());
        for(int i = 0; i < 100; i++) {
            String msg = String.format("this is a log %d", i);
            logger.info(msg);
        }
    }
    public void tLogback1000() {
        Logger logger = LoggerFactory.getLogger(this.getClass());
        for(int i = 0; i < 1000; i++) {
            String msg = String.format("this is a log %d", i);
            logger.info(msg);
        }
    }
    public void tLogbackDate1000() {
        Logger logger = LoggerFactory.getLogger("LOGGERDATE");
        for(int i = 0; i < 1000; i++) {
            String msg = String.format("this is a log %d", i);
            logger.info(msg);
        }
    }
    public void tLogbackDate10000000() {
        Logger logger = LoggerFactory.getLogger("LOGGERDATE");
        for(int i = 0; i < 10_000_000; i++) {
            String msg = String.format("this is a log date file. this is a log %d", i);
            logger.info(msg);
        }
        p("DONE\n");
    }
    public void testSQLite1() {
        SQLiteTest t = new SQLiteTest();
        t.t00();
    }
    public void testCurPath() {
        p("testCurPath:\n");
        p("%s\n", System.getProperty("user.dir"));
        p("%s\n", Paths.get("").toAbsolutePath().toString());
        p("%s\n", Paths.get(".").toAbsolutePath().toString());
        p("%s\n", Paths.get(".").toAbsolutePath().normalize().toString());
        p("%s\n", Paths.get(".").toString());
        {
            String pathname = "./src/main/java/org/wayne/external/input1.json";
            Path path = Paths.get(pathname);
            p("%s exists: %s\n",pathname,path.toFile().exists());
        }
        {
            String pathname = "./src/main/java/org/wayne/external/input2.json";
            Path path = Paths.get(pathname);
            p("%s exists: %s\n",pathname,path.toFile().exists());
        }
        {
            String pathname = "./input1.json";
            Path path = Paths.get(pathname);
            p("%s exists: %s\n",pathname,path.toFile().exists());
        }
        {
            URL s = this.getClass().getResource(".");
            assert "target/test-classes/path".equals(s.toString());
            s = this.getClass().getResource("/"); // target/test-classes
            assert "target/test-classes".equals(s.toString());
        }
    }
    public void testJsonNodeFromFile() throws Exception {
        // refer to collapseComments for layout
        testCurPath();
        ObjectMapper objectMapper = new ObjectMapper();
        String pathname = "src/main/resources/input1.json";
        InputStream inputStream;
        inputStream = this.getClass().getClassLoader().getResourceAsStream(pathname);
        assertNull(inputStream);
        inputStream = this.getClass().getResourceAsStream(pathname);
        assertNull(inputStream);
        pathname = "./dir1/jsonfile2.json"; // input1.json"; // implicitly in resources
        inputStream = this.getClass().getResourceAsStream(pathname);
        assertNull(inputStream);
        inputStream = this.getClass().getClassLoader().getResourceAsStream(pathname);
        assertNotNull(inputStream);
        String sJson = IOUtils.toString(inputStream, Charset.defaultCharset());
        JsonNode node = objectMapper.readTree(sJson);
        boolean isAssertEnabled = false;
        assert isAssertEnabled = true;
        if(!isAssertEnabled) {
            throw new Exception("assertions turned off, cannot check");
        }

        assert node != null;
        assert node.at("/k1").textValue().equals("v1");
        assert node.at("/k3/0/k301").textValue().equals("v301");
        assert node.at("/k3/1/k311/0/0").textValue().equals("k31110");
        assert node.at("/k3/1/k311/0/1").textValue().equals("k31111");
        assert node.at("/k3/1/k313/k3132").textValue().equals("v3132");
        assert node.at("/k4/2").textValue().equals("v3");
        assert node.at("/k5/0/1/0/1").textValue().equals("k51211");
        assert node.at("/k5/1/0/k522/1/0").textValue().equals("k52211");
        assert node.at("/k5/1/0/k523/k5231").textValue().equals("v5231");
        assert node.at("/k5/2/1").textValue().equals("k532");
        assert node.at("/k6/k61/0").textValue().equals("k611");
        assert node.at("/k6/k62/1/1").textValue().equals("k6222");
        assert node.at("/k6/k63/0/1/k6322").textValue().equals("v6322");
        assert node.at("/k6/k63/1/1/0").textValue().equals("k6341");
        assert node.at("/k6/k64/k641/1").textValue().equals("k6412");
        assert node.at("/k6/k64/k642/k6422").textValue().equals("v6422");
        assert node.at("/k6/k64/k642/k6423").isNull();
        assert node.at("/k6/k64/k642/k6424").isMissingNode();
        assert node.at("/k6/k64/k643/0").size() == 0;
        assert node.at("/k6/k64/k643/1").size() == 2;
        assert node.at("/k6/k64/k643/1/1").textValue().equals("k64312");
        assert node.at("/k6/k64/k644").size() == 0;
        assert node.at("/k6/k64/k645").size() == 0;
        assert !node.at("/k6/k64/k644").isMissingNode();
        assert !node.at("/k6/k64/k644").isNull();
        assert !node.at("/k6/k64/k645").isMissingNode();
        assert !node.at("/k6/k64/k645").isNull();

        // iterate k7 array
        JsonNode nodeArray = node.at("/k7/0");

        {
            int ctr1 = 0;
            for(JsonNode n: nodeArray) {
                int ctr2 = 0;
                for(JsonNode a: n) {
                    String key = String.format("k70%d%d",ctr1,ctr2);
                    assert a.textValue().equals(key);
                    ctr2++;
                }
                ctr1++;
            }
            assert nodeArray.size() == 3;
        }

        // iterate k7 map
        JsonNode nodeMap   = node.at("/k7/1/0");
        {
            Iterator<Map.Entry<String, JsonNode>> it = nodeMap.fields();
            int ctr1 = 0;
            while(it.hasNext()) {
                Map.Entry<String, JsonNode> kv = it.next();
                String keyName = kv.getKey();
                p("keyname:%s\n", keyName);
                JsonNode n = kv.getValue();
                for(int i = 0; i < 2; i++) {
                    String subkey = String.format("/k%d/0/k",i);
                    assert n.at(subkey+"/0/k2").textValue().equals("v21");
                    assert n.at(subkey+"/1/k2").textValue().equals("v23");
                }
                ctr1++;

            }
        }
        p("ExternalTest.testJsonNodeFromFile passed\n");
    }
    static class SQLiteTest {
        // sqlite-jdbc-3.8.10.1.jar
        // java -classpath ".:sqlite-jdbc-3.8.10.1.jar" Tests // this works
        String dbName_;
        Connection c_;
        Utils u_;
        public SQLiteTest() {
            // if using in memory, then jdbc:sqlite:memory
            dbName_ = "jdbc:sqlite:test_sqlite_java_0.db"; // :path_to_db
            u_ = new Utils();
            try {
                c_ = null;
                c_ = DriverManager.getConnection(dbName_);
            }
            catch(Exception e) {
            }
        }
        public void t00() {
            if(c_ == null) {
                return;
            }
            Statement stmt = null;
            ResultSet rs = null;
            try {
                stmt = c_.createStatement();
                String sql =
                    "create table if not exists company (" +
                    "id int primary key not null, " +
                    "name text, " +
                    "age int, " +
                    "ts timestamp default current_timestamp" +
                    ")";
                stmt.executeUpdate(sql);
                sql = "select * from company;";
                rs = stmt.executeQuery(sql);
                int id = 0;
                int age = 10;
                String name = null;
                String ts = null;
                while(rs.next()) {
                    id      = rs.getInt("id");
                    age     = rs.getInt("age");
                    name    = rs.getString("name");
                    ts      = rs.getString("ts");
                    System.out.printf(
                        "RS: id:%3d age:%3d name:%8s ts:%s\n",
                        id, age, name, ts);
                }
                for(int i = 0; i < 10; i++) {
                    id++;
                    age     = u_.getInt(10,20);
                    name    = u_.getRandString(5);
                    sql     =
                        "insert into company (id, name, age, ts) values (" +
                        id + ",'" + name + "'," + age + "CURRENT_TIMESTAMP)";
                    stmt.executeUpdate(sql);
                }
                rs.close();
                stmt.close();
                c_.close();
            }
            catch(Exception e) {
            }
        }
        public void test() {
        }
    }

    @Test
    public void testRoaringBitmapPerf() throws IOException {
        p("testGzip\n");
        testGZip(false, false, 5000, 20);
        p("testGzip random\n");
        testGZip(true, false, 5000, 20);
        p("random\n");
        testRoaringBitmap(true, false, 5000, 20);
        p("non random\n");
        testRoaringBitmap(false, false, 5000, 20);
        p("random\n");
        testRoaringBitmap(true, true, 5000, 20);
    }

    @Test
    public void testSinglePerf() throws IOException {
        p("testgzip\n");
        testGZip(true, true, 20000, 10);
        p("testgzip\n");
        testGZip(true, false, 20000, 10);
        p("roaringbitmap\n");
        testRoaringBitmap(true, false, 20000, 10);
    }

    public void testGZip(boolean isRandom, boolean isSorted, int size, int numRuns) throws IOException {
        long timebeg;
        long timeend;
        List<String> results = new ArrayList<>();
        for(int r = 0; r < numRuns; r++) {
            Set<Integer> set = new HashSet<>();
            for(int i = 0; i < size; i++) {
                if(isRandom) {
                    set.add(random.nextInt(10000000));
                } else {
                    set.add(i*2 + 0x20000);
                }
            }
            if(isRandom) {
                while(set.size() < size) {
                    set.add(random.nextInt(10000000));
                }
            }
            List<Integer> listI = new ArrayList<>(set);

            int [] intArray = listI.stream().mapToInt(i -> i).toArray();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            for(int i = 0; i < intArray.length; i++) {
                for(int j = 0; j < 4; j++) {
                    byteArrayOutputStream.write((intArray[i] >> j * 8) & 0xff);
                }
            }
            byte [] byteArray = byteArrayOutputStream.toByteArray();

            byteArrayOutputStream = new ByteArrayOutputStream();

            timebeg = System.nanoTime();
            if(isSorted) {
                Collections.sort(listI);
            }
            GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream);
            gzipOutputStream.write(byteArray);
            gzipOutputStream.close();
            byte [] byteArrayZipped = byteArrayOutputStream.toByteArray();
            int sizeI = byteArray.length;
            int sizeO = byteArrayZipped.length;
            byteArrayOutputStream.close();
            timeend = System.nanoTime();
            long timed1 = (timeend - timebeg);


            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayZipped);
            GZIPInputStream gzipInputStream = new GZIPInputStream(byteArrayInputStream);
            byte [] byteArrayOutput = new byte[1024];
            int bytesRead = 0;
            ByteArrayOutputStream byteArrayOutputStream1 = new ByteArrayOutputStream();
            while((bytesRead = gzipInputStream.read(byteArrayOutput)) != -1) {
                byteArrayOutputStream1.write(byteArrayOutput, 0, bytesRead);
            }
            timebeg = System.nanoTime();
            timeend = System.nanoTime();
            long timed2 = (timeend - timebeg);

            byteArrayInputStream.close();

            String result =
                String.format("numEntries:%10d sizeB:%10d, out sizeB: %10d, or %.3f original. serialize time = %10d ns (%5d ms). deserialize time = %10d ns (%5d ms)\n",
                    size, sizeI, sizeO, sizeO/(double)sizeI, timed1, timed1/1_000_000, timed2, timed2/1_000_000);
            results.add(result);
            p(result);

            byte [] byteArrayFinalOutput = byteArrayOutputStream1.toByteArray();
            IntBuffer intBuffer = ByteBuffer.wrap(byteArrayFinalOutput).order(ByteOrder.LITTLE_ENDIAN).asIntBuffer();
            int [] intArrayOut = new int[intBuffer.remaining()];
            intBuffer.get(intArrayOut);
            List<Integer> listO = new ArrayList<>();
            for(int i = 0; i < intArrayOut.length; i++) {
                listO.add(intArrayOut[i]);
            }
            assert listI.size() == listO.size();
            Set<Integer> setI = new HashSet<>(listI);
            Set<Integer> setO = new HashSet<>(listO);
            assert setI.containsAll(setO);
        }
        Collections.sort(results);
        for(String result: results) {
            //p(result);
        }
    }

    public void testRoaringBitmap(boolean isRandom, boolean isSorted, int size, int numRuns) throws IOException {
        long timebeg;
        long timeend;

        List<String> results = new ArrayList<>();
        for(int r = 0; r < numRuns; r++) {
            Set<Integer> set = new HashSet<>();
            for(int i = 0; i < size; i++) {
                if(isRandom) {
                    set.add(random.nextInt(10000000));
                } else {
                    set.add(i*2);
                }
            }
            if(isRandom) {
                while(set.size() != size) {
                    set.add(random.nextInt(10000000));
                }
            }
            List<Integer> listI = new ArrayList<>(set);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
            int [] intArray = listI.stream().mapToInt(i -> i).toArray();
            int sizeI = intArray.length * 4;

            timebeg = System.nanoTime();
            if(isSorted) {
                Collections.sort(listI);
            }
            RoaringBitmap roaringBitmapIn = RoaringBitmap.bitmapOf(intArray);
            roaringBitmapIn.runOptimize();
            roaringBitmapIn.serialize(dataOutputStream);
            dataOutputStream.flush();
            byte [] byteArray = byteArrayOutputStream.toByteArray();
            int sizeO = byteArray.length;
            dataOutputStream.close();
            timeend = System.nanoTime();
            long timed1 = (timeend - timebeg);


            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
            DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);

            timebeg = System.nanoTime();
            RoaringBitmap roaringBitmapOut = new RoaringBitmap();
            roaringBitmapOut.deserialize(dataInputStream);
            timeend = System.nanoTime();
            long timed2 = (timeend - timebeg);

            dataInputStream.close();
            byteArrayInputStream.close();

            String result =
            String.format("numEntries:%10d sizeB:%10d, out sizeB: %10d, or %.3f original. serialize time = %10d ns (%5d ms). deserialize time = %10d ns (%5d ms)\n",
                size, sizeI, sizeO, sizeO/(double)sizeI, timed1, timed1/1_000_000, timed2, timed2/1_000_000);
            results.add(result);

            List<Integer> listO = new ArrayList<>();
            for(Integer i: roaringBitmapOut) {
                listO.add(i);
            }
            assert listI.size() == listO.size();
            Set<Integer> setI = new HashSet<>(listI);
            Set<Integer> setO = new HashSet<>(listO);
            assert setI.containsAll(setO);
        }
        Collections.sort(results);
        for(String result: results) {
            p(result);
        }
    }

    @Test(expected = IOException.class)
    public void testRoaringBitmapPayload() throws IOException {
        boolean isExpected = false;
        try {
            String s = "emptyPayload";
            testRoaringBitmapPayload(s);
        } catch(IOException e) {
            isExpected = true;
        }
        assert isExpected;
    }

    public void testRoaringBitmapPayload(String s) throws IOException {
        ByteBuffer byteBuffer = base64String2ByteBuffer(s);
        List<Integer> list = byteBufferRoaringBitmap2List(byteBuffer);
        assert list != null;
        if(list != null) {
            StringBuilder sb = new StringBuilder();
            for(Integer i: list) {
                sb.append(i);
                sb.append(",");
            }
            p(sb.toString());
        }
    }

    @Test
    public void testRoaringBitmap() throws IOException {
        long timebeg;
        long timeend;
        int numRuns = 5;
        List<Integer> sizes = Arrays.asList(10,20,50,100,200,500,1000,5000,10000,20000,100000,500000,1000000,10000000);
        for(int runNum = 0; runNum < numRuns; runNum++) {
            for(int size: sizes) {
                List<Integer> listI = new ArrayList<>();
                for(int i = 0; i < size; i++) {
                    listI.add(i * 2);
                }
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
                int [] intArray = listI.stream().mapToInt(i -> i).toArray();
                int sizeI = intArray.length * 4;
                //MutableRoaringBitmap mutableRoaringBitmap = MutableRoaringBitmap.bitmapOf(1,2,3,4,5,6,7,8,9,10);
                //MutableRoaringBitmap mutableRoaringBitmap = MutableRoaringBitmap.bitmapOf(intArray);  // this gives exception!
                timebeg = System.nanoTime();
                RoaringBitmap roaringBitmapIn = RoaringBitmap.bitmapOf(intArray);
                roaringBitmapIn.runOptimize();
                roaringBitmapIn.serialize(dataOutputStream);
                dataOutputStream.flush();
                byte [] byteArray = byteArrayOutputStream.toByteArray();
                int sizeO = byteArray.length;
                dataOutputStream.close();
                timeend = System.nanoTime();
                long timed1 = (timeend - timebeg);


                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
                DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);

                timebeg = System.nanoTime();
                RoaringBitmap roaringBitmapOut = new RoaringBitmap();
                roaringBitmapOut.deserialize(dataInputStream);
                timeend = System.nanoTime();
                long timed2 = (timeend - timebeg);

                dataInputStream.close();
                byteArrayInputStream.close();

                p("numEntries:%10d sizeB:%10d, roaringbitmap sizeB: %10d, or %.3f original. serialize time = %10d ns (%5d ms). deserialize time = %10d ns (%5d ms)\n",
                    size, sizeI, sizeO, sizeO/(double)sizeI, timed1, timed1/1_000_000, timed2, timed2/1_000_000);

                List<Integer> listO = new ArrayList<>();
                for(Integer i: roaringBitmapOut) {
                    listO.add(i);
                }
                assert listI.size() == listO.size();
                Set<Integer> setI = new HashSet<>(listI);
                Set<Integer> setO = new HashSet<>(listO);
                assert setI.containsAll(setO);
            }
        }
    }

    @Test
    public void testByteBuffer() throws IOException {
        List<Integer> lii = Arrays.asList(1,2,3,4,5,6);
        int [] aii = lii.stream().mapToInt(Integer::intValue).toArray();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        lii.stream().forEach(i -> { try { dos.writeInt(i); } catch (IOException e) { e.printStackTrace(); } });
        byte [] bai = baos.toByteArray();
        ByteBuffer bb = ByteBuffer.wrap(bai);
        byte [] bao = bb.array();
        assert bb.limit() == lii.size() * 4;
        bb.limit(bb.limit());
        ByteArrayInputStream bais = new ByteArrayInputStream(bao);
        DataInputStream dis = new DataInputStream(bais);
        List<Integer> lio = new ArrayList<>();
        while(dis.available() > 0) lio.add(dis.readInt());
        int [] aio = lio.stream().mapToInt(Integer::intValue).toArray();
        assert lii.size() == lio.size();
        assert lii.equals(lio);
        assert aii.length == aio.length;
        for(int i = 0; i < aii.length; i++) assert aii[i] == aio[i];
        return;
    }

    @Test
    public void testRoaringBitmapCases() throws IOException {
        {
            List<Integer> lii = Arrays.asList(1,2);
            ByteBuffer bb = list2RoaringBitmapByteBuffer(lii);
            assert bb != null && bb.limit() != 0;

            List<Integer> lio = byteBufferRoaringBitmap2List(bb);
            assert lio != null && lio.equals(lii);
        }
        {
            List<Integer> lii = Arrays.asList(1,2,3,5,7);
            ByteBuffer bb = list2RoaringBitmapByteBuffer(lii);
            assert bb != null;

            List<Integer> lio = byteBufferRoaringBitmap2List(bb);
            assert lio != null && lio.equals(lii);
        }
        {
            List<Integer> lii = Collections.EMPTY_LIST;
            ByteBuffer bb = list2RoaringBitmapByteBuffer(lii);
            assert bb != null && bb.limit() == 8;

            List<Integer> lio = byteBufferRoaringBitmap2List(bb);
            assert lio != null && lio.equals(lii);
        }
    }

    @Test
    public void testRoaringBitmapBase64() throws IOException {
        {
            List<Integer> lii = Arrays.asList(1,2);
            ByteBuffer bb = list2RoaringBitmapByteBuffer(lii);
            assert bb != null && bb.limit() != 0;
            String s = byteBuffer2Base64String(bb);

            ByteBuffer bb2 = base64String2ByteBuffer(s);
            List<Integer> lio = byteBufferRoaringBitmap2List(bb2);
            assert lio != null && lio.equals(lii);
        }
        {
            List<Integer> lii = Arrays.asList(1,2,3,5,7);
            ByteBuffer bb = list2RoaringBitmapByteBuffer(lii);
            assert bb != null && bb.limit() != 0;
            String s = byteBuffer2Base64String(bb);

            ByteBuffer bb2 = base64String2ByteBuffer(s);
            List<Integer> lio = byteBufferRoaringBitmap2List(bb2);
            assert lio != null && lio.equals(lii);
        }
        {
            List<Integer> lii = Collections.EMPTY_LIST;
            ByteBuffer bb = list2RoaringBitmapByteBuffer(lii);
            assert bb != null && bb.limit() != 0;
            String s = byteBuffer2Base64String(bb);

            ByteBuffer bb2 = base64String2ByteBuffer(s);
            List<Integer> lio = byteBufferRoaringBitmap2List(bb2);
            assert lio != null && lio.equals(lii);
        }
    }

    @Test
    public void testJackson1() throws IOException {
        List<String> list;
        boolean flag = false;

        list = objectMapper.readValue("[]", List.class);
        assert list != null && list.size() == 0;
        assert CollectionUtils.isEmpty(list);

        list = objectMapper.readValue("[1,2,3]", List.class);
        assert list != null && list.size() == 3;
        assert CollectionUtils.isNotEmpty(list);

        try {
            list = objectMapper.readValue("{}", List.class);
        } catch(Exception e) {
            flag = true;
        }
        assert flag;

        flag = false;
        try {
            list = objectMapper.readValue("", List.class);
        } catch(Exception e) {
            flag = true;
        }
        assert flag;

        list = Arrays.asList("11","22","33","44","55");
        String res = objectMapper.writeValueAsString(list);
        assert "[\"11\",\"22\",\"33\",\"44\",\"55\"]".equals(res);
        List<String> list2 = objectMapper.readValue(res, List.class);
        assert list.equals(list2);

        list = Arrays.asList();
        res = objectMapper.writeValueAsString(list);
        assert StringUtils.isNotBlank(res);
        assert "[]".equals(res);
        list = Collections.EMPTY_LIST;
        res = objectMapper.writeValueAsString(list);
        assert StringUtils.isNotBlank(res);
        assert "[]".equals(res);

        res = objectMapper.writeValueAsString(null);
        assert "null".equals(res);
        return;
    }

    @Test
    public void testCollections() {
        List<String> ls0 = Collections.EMPTY_LIST;
        List<Integer> li0 = Collections.EMPTY_LIST;
        List<String> ls1 = Collections.emptyList();
        List<Integer> li1 = Collections.emptyList();
        Set<String> ss0 = Collections.EMPTY_SET;
        Set<String> ss1 = Collections.emptySet();
        assert ls0.equals(ls1);
        assert li0.equals(li1);
        assert ls0 != null && ls0.size() == 0;
        assert ss0.equals(ss1);
        assert ss0 != null && ss0.size() == 0;
        assert CollectionUtils.isEmpty(ls0);

        li0 = Arrays.asList();
        assert li0 != null && li0.size() == 0;
        int [] ai0 = li0.stream().mapToInt(Integer::intValue).toArray();
        assert ai0 != null && ai0.length == 0;

        Map<String,Integer> msi0 = Collections.EMPTY_MAP;
        Map<String,Integer> msi1 = Collections.emptyMap();
        assert msi0.equals(msi1);
        assert msi0 != null && msi0.size() == 0;
        assert CollectionUtils.isEmpty(ss0);
        assert MapUtils.isEmpty(msi0);
        ls1 = new ArrayList<>();
        assert ls0.equals(ls1);
        return;
    }

    @Test
    public void testJsonNodeObjectNode() throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"k\":\"key1\",\"ts\":1234,");
        sb.append("\"v\":{");
        sb.append("\"version\":\"1\",");
        sb.append("\"sk1\":[\"v1\",\"v2\",\"v3\"],");
        sb.append("\"sk2\":[\"v21\",\"v22\",\"v23\",\"v24\"],");
        sb.append("\"sk3\":[],");
        sb.append("\"sk4\":null,");
        sb.append("\"sk5\":\"abcdefg\"");
        sb.append("}");
        sb.append("}");
        String s1 = sb.toString();

        {
            JsonFactory jsonFactory = new MappingJsonFactory().enable(JsonParser.Feature.AUTO_CLOSE_SOURCE);
            ByteBuffer byteBuffer = ByteBuffer.wrap(s1.getBytes(StandardCharsets.UTF_8));
            ObjectNode objectNode = null;
            try (JsonParser parser = jsonFactory.createParser(byteBuffer.array())) {
                objectNode = parser.readValueAsTree();
                JsonNode key = objectNode.get("k");
                JsonNode payload = objectNode.get("v");
                assert "key1".equals(key.textValue());
                JsonNode vs;

                vs = payload.at("/sk1");
                assert !vs.isPojo();
                assert !vs.isTextual();
                assert !vs.isValueNode();
                assert !vs.isMissingNode();
                assert !vs.isNull();
                assert !vs.isObject();
                assert vs.isArray();
                assert vs.isContainerNode();
                assert vs.textValue() == null;
                assert "".equals(vs.asText());
                assert "[\"v1\",\"v2\",\"v3\"]".equals(vs.toString());
                assert vs instanceof ArrayNode;
                if(vs instanceof ArrayNode)
                    assert Arrays.asList("v1","v2","v3").equals(objectMapper.convertValue(vs, ArrayList.class));
                if(vs instanceof ArrayNode)
                    assert "[\"v1\",\"v2\",\"v3\"]".equals(vs.toString());
                else
                    assert "1234".equals(vs.asText());
                ArrayNode an = (ArrayNode)vs;
                assert "".equals(an.asText());
                assert an.textValue() == null;
                //String vs2 = objectMapper.convertValue(vs, String.class);

                vs = payload.get("sk1");
                assert !vs.isPojo();
                assert !vs.isTextual();
                assert !vs.isValueNode();
                assert !vs.isMissingNode();
                assert !vs.isNull();
                assert !vs.isObject();
                assert vs.isArray();
                assert vs.isContainerNode();
                assert vs.textValue() == null;
                assert "".equals(vs.asText());
                assert "[\"v1\",\"v2\",\"v3\"]".equals(vs.toString());
                assert Arrays.asList("v1","v2","v3").equals(objectMapper.convertValue(vs, ArrayList.class));

                vs = payload.at("/sk5");
                assert "abcdefg".equals(vs.textValue());
                assert "\"abcdefg\"".equals(vs.toString());
                assert "abcdefg".equals(vs.asText());

                vs = payload.get("sk5");
                assert "abcdefg".equals(vs.textValue());
                assert "\"abcdefg\"".equals(vs.toString());
                assert "abcdefg".equals(vs.asText());

                vs = payload.at("/sk100");
                assert !vs.isNull();
                assert vs.isMissingNode();
                vs = payload.get("sk100");
                assert vs == null;

            } catch(IOException e) {
                e.printStackTrace();
                assert false;
            }
        }
        {
            ByteBuffer byteBuffer = ByteBuffer.wrap(s1.getBytes(StandardCharsets.UTF_8));
            JsonNode n = objectMapper.readTree(byteBuffer.array());
            JsonNode key = n.get("k");
            JsonNode payload = n.get("v");
            assert "key1".equals(key.textValue());
            JsonNode vs;

            vs = payload.at("/sk1");
            assert !vs.isPojo();
            assert !vs.isTextual();
            assert !vs.isValueNode();
            assert !vs.isMissingNode();
            assert !vs.isNull();
            assert !vs.isObject();
            assert vs.isArray();
            assert vs.isContainerNode();
            assert vs.textValue() == null;
            assert "".equals(vs.asText());
            assert "[\"v1\",\"v2\",\"v3\"]".equals(vs.toString());
            assert Arrays.asList("v1","v2","v3").equals(objectMapper.convertValue(vs, ArrayList.class));

            vs = payload.get("sk1");
            assert !vs.isPojo();
            assert !vs.isTextual();
            assert !vs.isValueNode();
            assert !vs.isMissingNode();
            assert !vs.isNull();
            assert !vs.isObject();
            assert vs.isArray();
            assert vs.isContainerNode();
            assert vs.textValue() == null;
            assert "".equals(vs.asText());
            assert "[\"v1\",\"v2\",\"v3\"]".equals(vs.toString());
            assert Arrays.asList("v1","v2","v3").equals(objectMapper.convertValue(vs, ArrayList.class));

            vs = payload.at("/sk5");
            assert "abcdefg".equals(vs.textValue());
            assert "\"abcdefg\"".equals(vs.toString());
            assert "abcdefg".equals(vs.asText());

            vs = payload.get("sk5");
            assert "abcdefg".equals(vs.textValue());
            assert "\"abcdefg\"".equals(vs.toString());
            assert "abcdefg".equals(vs.asText());

            vs = payload.at("/sk100");
            assert !vs.isNull();
            assert vs.isMissingNode();
            vs = payload.get("sk100");
            assert vs == null;

        }
        return;
    }
    @Test
    public void testMutableCollection() throws IOException {
        String s = "[\"2\",\"3\",\"4\"]";
        List<String> l = objectMapper.readValue(s, List.class);
        assert Arrays.asList("2","3","4").equals(l);
        l.add("5");
        assert l instanceof ArrayList;
        assert l instanceof List == true;
        assert l instanceof AbstractList == true;
        l = Arrays.asList("2","3","4");
        assert l instanceof List == true;
        assert l instanceof ArrayList == false;
        if(l instanceof List && l instanceof ArrayList) {
            l.add("6");
        }
        assert l instanceof AbstractList;
        try {
            l.add("6");
        } catch(Exception e) {

        }
        assert Arrays.asList("2","3","4").equals(l);
        l = new ArrayList<>(2);
        l.add("3");
        l.add("4");
        l.add("5");
        Map<String,String> map = new HashMap<>();
        map.put("1","2");
        assert StringUtils.isBlank(map.get(null));
    }

    @Test
    public void testRoaringBitmapPrintOutput() throws IOException {
        boolean isExpected = false;
        try {
            String v1 = "";
            testRoaringBitmapPayload(v1);
        } catch(EOFException e) {
            isExpected = true;
        }
        assert isExpected;
    }


    @Test
    public void testParseNumbers() {
        boolean flag = true;
        double v;
        v = Double.valueOf("1.1");
        assert v == 1.1;
        v = Double.parseDouble("1.1");
        assert v == 1.1;
        flag = false;
        try {
            v = Double.valueOf("1.2.3");
        } catch(Exception e) {
            flag = true;
        }
        assert flag;

        flag = false;
        try {
            v = Double.parseDouble("1.2.3");
        } catch(Exception e) {
            flag = true;
        }
        assert flag;
        assert NumberUtils.isNumber("1.2");
        assert !NumberUtils.isNumber("1.2.3");
        assert !NumberUtils.isNumber(null);
    }

    @Test
    public void testJsonNode() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        {
            // this will fail because you cannot use single quotes. expects double quotes.
            boolean didFail = false;
            String stringJson = "{'key1':'val1','key2':'val2','key3':'val3'}";
            try {
                JsonNode node = objectMapper.readTree(stringJson);
                assert "val1".equals(node.at("/key1").asText());
            } catch(Exception e) {
                didFail = true;
            }
            assert didFail;
        }
        {
            String stringJson = "{\"key1\":\"val1\",\"key2\":\"val2\",\"key3\":\"val3\"}";
            JsonNode node = objectMapper.readTree(stringJson);
            assert "val2".equals(node.at("/key2").asText());
        }
        {
            String stringJson =
                "{" +
                    "\"key1\":\"val1\"," +
                    "\"key2\":\"val2\"," +
                    "\"key3\":{\"key3.1\":\"val3.1\"}" +
                    "}";
            JsonNode jsonNode = objectMapper.readTree(stringJson);
            assert "val2".equals(jsonNode.at("/key2").asText());
            ObjectNode objectNode = (ObjectNode)jsonNode;
            objectNode.put("key1","value1");
            JsonNode jsonNode2 = jsonNode.at("/key3");
            objectNode.set("key2",jsonNode2);
            String v = jsonNode.get("key1").textValue();
            assert "value1".equals(v);
            v = jsonNode.get("key3").textValue();   // null if not text, else text val
            assert v == null;
            v = jsonNode.get("key3").asText();      // "" if not text, else text val
            assert v == "";
            v = jsonNode.get("key3").toString();    // literal with quotes
            assert "{\"key3.1\":\"val3.1\"}".equals(v);
            jsonNode2 = jsonNode.get("key3");
            assert jsonNode2.isContainerNode();
            v = jsonNode.get("key3").get("key3.1").textValue();
            assert "val3.1".equals(v);
            v = jsonNode.get("key3").get("key3.1").asText();
            assert "val3.1".equals(v);
            v = jsonNode.get("key3").get("key3.1").toString();
            assert "\"val3.1\"".equals(v);
        }
        {
            String path = "src/test/resources/input_test_json.json";
            JsonNode json = objectMapper.readTree(new File(path));
            assert json != null;
        }
        {
            String path = "src/test/resources/input_test_json.json";
            try(InputStream inputStream = new FileInputStream(path)) {
                JsonNode json = objectMapper.readTree(inputStream);
                assert json != null;
            }
        }
        {
            String path = "src/test/resources/input_test_json.json";
            try(InputStream inputStream = new FileInputStream(path)) {
                String value = IOUtils.toString(inputStream, Charset.defaultCharset());
                JsonNode json = objectMapper.readTree(value);
                assert json != null;
            }
        }
        {
            String path = "src/test/resources/input_test_json.json";
            InputStream inputStream = this.getClass().getResourceAsStream(path);
            assert inputStream == null;
        }
        {
            String path = "src/test/resources/input_test_json.json";
            String value = new String(Files.readAllBytes(Paths.get(path)));
            JsonNode json = objectMapper.readTree(value);
            assert json != null;
        }
        {
            // read large file with buffered reader
            String filename = "src/test/resources/input_test_json.json";
            Path path = Paths.get(filename);
            BufferedReader bufferedReader = Files.newBufferedReader(path, Charset.defaultCharset());
            String line;
            int cnt = 0;
            while((line = bufferedReader.readLine()) != null) {
                cnt++;
            }
            bufferedReader.close();
        }
        p("testJsonNode passed\n");
    }

    String getRandomString(String charset, int size) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < size; i++) {
            sb.append(charset.charAt(r.nextInt(charset.length())));
        }
        return sb.toString();
    }
    String modifyString(String s1, String charset, int modifyType) {
        StringBuilder sb = new StringBuilder();
        int idxTarget = r.nextInt(s1.length());
        if(modifyType == 0) {           // remove char
            if(idxTarget == 0) {
                sb.append(s1.substring(1));
            } else {
                sb.append(s1.substring(0,idxTarget));
                sb.append(s1.substring(idxTarget+1));
            }
        } else if(modifyType == 1){     // add char
            if(idxTarget == 0) {
                sb.append(getAnotherChar(s1.charAt(idxTarget), charset));
                sb.append(s1);
            } else {
                sb.append(s1.substring(0,idxTarget));
                sb.append(getAnotherChar(s1.charAt(idxTarget), charset));
                sb.append(s1.substring(idxTarget));
            }
        } else {                        // modify char
            if(idxTarget == 0) {
                sb.append(getAnotherChar(s1.charAt(idxTarget), charset));
                sb.append(s1.substring(1));
            } else {
                sb.append(s1.substring(0,idxTarget));
                sb.append(getAnotherChar(s1.charAt(idxTarget), charset));
                sb.append(s1.substring(idxTarget+1));
            }
        }
        return sb.toString();
    }
    char getAnotherChar(char c, String charset) {
        for(int i = 0; i < charset.length(); i++) {
            if(charset.charAt(i) != c) return charset.charAt(i);
        }
        return '0';
    }
    String getInterleavedString(String s1, String s2) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0, j = 0;;) {
            if(i < s1.length() && j < s2.length()) {
                sb.append(r.nextBoolean() ? s1.charAt(i++) : s2.charAt(j++));
            } else if(i < s1.length()) {
                sb.append(s1.substring(i));
                break;
            } else if(j < s2.length()) {
                sb.append(s2.substring(j));
                break;
            }
        }
        return sb.toString();
    }
    int getStringEditDistance(String s1, String s2) {
        AtomicInteger ctr = new AtomicInteger();
        Map<String,Integer> map = new HashMap<>();
        return getStringEditDistance(0,0,s1,s2,0,map,ctr);
    }
    int getStringEditDistance(int i, int j, String s1, String s2, int numErrs, Map<String,Integer> map, AtomicInteger ctr) {
        ctr.incrementAndGet();
        if(i == s1.length() && j == s2.length()) return numErrs;
        int min = numErrs;
        String key = String.format("%d,%d",i,j);
        if(map.containsKey(key)) return map.get(key)+numErrs;
        if(i == s1.length()) return getStringEditDistance(i,j+1,s1,s2,numErrs+1,map,ctr);
        if(j == s2.length()) return getStringEditDistance(i+1,j,s1,s2,numErrs+1,map,ctr);
        if(s1.charAt(i) == s2.charAt(j)) {
            min = getStringEditDistance(i+1,j+1,s1,s2,numErrs,map,ctr);
        } else {
            int numErr1 = getStringEditDistance(i,j+1,s1,s2,1,map,ctr);
            int numErr2 = getStringEditDistance(i+1,j,s1,s2,1,map,ctr);
            int numErr3 = getStringEditDistance(i+1,j+1,s1,s2,1,map,ctr);
            min = Collections.min(Arrays.asList(numErr1,numErr2,numErr3));
            map.put(key,min);
            min += numErrs;
        }
        return min;
    }
    boolean isInterleaved(String s1, String s2, String s3, AtomicInteger ctr) {
        Map<String, Boolean> map = new HashMap<>();
        return isInterleaved(0,0,0,s1,s2,s3,map,ctr);
    }
    boolean isInterleaved(int i, int j, int k, String s1, String s2 ,String s3, Map<String,Boolean> map, AtomicInteger ctr) {
        ctr.incrementAndGet();
        if(i == s1.length() && j == s2.length() && k == s3.length())
            return true;
        String key = String.format("%d,%d,%d",i,j,k);
        int version = 1;
        if(version == 1) {
            if(i == s1.length())
                return s2.substring(j).equals(s3.substring(k));
            if(j == s2.length())
                return s1.substring(i).equals(s3.substring(k));
            if(map.containsKey(key))
                return map.get(key);
            if(s1.charAt(i) == s3.charAt(k))
                if(isInterleaved(i+1,j,k+1,s1,s2,s3,map,ctr))
                    return true;
            if(s2.charAt(j) == s3.charAt(k))
                if(isInterleaved(i,j+1,k+1,s1,s2,s3,map,ctr))
                    return true;
        }
        if(version == 2) {
            if(map.containsKey(key))
                return map.get(key);
            if(i < s1.length())
                if(s1.charAt(i) == s3.charAt(k))
                    if(isInterleaved(i+1,j,k+1,s1,s2,s3,map,ctr))
                        return true;
            if(j < s2.length())
                if(s2.charAt(j) == s3.charAt(k))
                    if(isInterleaved(i,j+1,k+1,s1,s2,s3,map,ctr))
                        return true;
        }
        map.put(key,false);
        return false;
    }
    boolean isInterleavedWithErrors(String s1, String s2, String s3, int maxErrs, AtomicInteger ctr) {
        Map<String, Boolean> map = new HashMap<>();
        return isInterleavedWithErrors(0,0,0,s1,s2,s3,0,maxErrs,map,ctr);
    }
    boolean isInterleavedWithErrors(int i, int j, int k, String s1, String s2, String s3, int ctrErr, int maxErrs, Map<String, Boolean> map, AtomicInteger ctr) {
        ctr.incrementAndGet();
        if(i == s1.length() && j == s2.length() && k == s3.length())
            return (ctrErr <= maxErrs );
        String key = String.format("%d,%d,%d",i,j,k);
        if(map.containsKey(key))
            return map.get(key);
        if(i < s1.length())
            if(s1.charAt(i) == s3.charAt(k))
                if(isInterleavedWithErrors(i+1,j,k+1,s1,s2,s3,ctrErr,maxErrs,map,ctr));
        if(j < s2.length())
            if(s2.charAt(j) == s3.charAt(k))
                if(isInterleavedWithErrors(i,j+1,k+1,s1,s2,s3,ctrErr,maxErrs,map,ctr));
        if(isInterleavedWithErrors(i+1,j,k,s1,s2,s3,ctrErr+1,maxErrs,map,ctr)) return true;
        if(isInterleavedWithErrors(i,j+1,k,s1,s2,s3,ctrErr+1,maxErrs,map,ctr)) return true;
        if(isInterleavedWithErrors(i,j,k+1,s1,s2,s3,ctrErr+1,maxErrs,map,ctr)) return true;
        if(isInterleavedWithErrors(i+1,j,k+1,s1,s2,s3,ctrErr+1,maxErrs,map,ctr)) return true;
        if(isInterleavedWithErrors(i,j+1,k+1,s1,s2,s3,ctrErr+1,maxErrs,map,ctr)) return true;
        map.put(key, false);
        return false;
    }

    @Test
    public void testInterleavedStringsError() {
        String s1, s2, s3;
        boolean res;
        AtomicInteger ctr = new AtomicInteger(0);
        String charset = "abc";
        int numcases = 100_000;
        for(int i = 0; i < numcases; i++) {
            s1 = getRandomString(charset, 5);
            s2 = getRandomString(charset, 5);
            String mod1 = s1;
            String mod2 = s2;
            s3 = getInterleavedString(s1,s2);
            s3 = modifyString(s3,charset,1);
            try {
                res = isInterleavedWithErrors(mod1,mod2,s3,1,ctr);
                assert res;
            } catch (Exception e) {
                p("error for case %d\ns1:%s\ns2:%s\n,s3:%s\n",i,mod1,mod2,s3);
            }
        }
    }
    @Test
    public void testMaxHeap() {
        PriorityQueue<CacheNode> q1 = new PriorityQueue<>(new Comparator<CacheNode>() {
            @Override
            public int compare(CacheNode x, CacheNode y) { return y.getW().get() - x.getW().get(); }
        });
        PriorityQueue<CacheNode> q2 = new PriorityQueue<>((x,y)->y.getW().get()-x.getW().get());
        PriorityQueue<Integer> q3 = new PriorityQueue<>((x,y)->y-x);
        List<Integer> l1 = Arrays.asList(3,8,1,7,9,0,2,6,4,5);
        l1.stream().forEach(x -> q3.add(x));
        Iterator<Integer> it = q3.iterator();
        List<Integer> topN = new ArrayList<>();
        for(int i = 0; i < 3 && i < q3.size(); i++) {
            topN.add(q3.poll());
        }
        q3.addAll(topN);
        Set<Integer> expectedSet = new HashSet<>(Arrays.asList(9,8,7,6,5));
        assert expectedSet.containsAll(topN);
    }
    @Test
    public void testRegexPatternCompile() {
        String regex = "^$";
        Pattern pattern = Pattern.compile(regex);
        String v = "";
        boolean b = pattern.matcher(v).matches();
    }
    @Test
    public void testJson() throws IOException {
        {
            String sjson = "{\"k1\":\"v1\",\"k2\":\"v2\",\"k3\":{\"k3.1\":\"v3.1\"},"+
                "\"k4\":[{\"k4.1.1\":\"v4.1.1\",\"k4.1.2\":\"v4.1.2\"},{\"k4.2.1\":\"v4.2.1\",\"k4.2.2\":\"v4.2.2\"}]}";
            JsonNode jsonNode = objectMapper.readTree(sjson);
            JsonNode n;

            n = NullNode.getInstance();
            assert n.isNull();

            n = jsonNode.path("abc");
            assert n.isMissingNode();
            assert !n.isNull();
            assert !n.isTextual();
            assert n.asText().equals("");
            assert n.textValue() == null;
            assert n.toString().equals("");

            n = jsonNode.path("k1");
            assert !n.isMissingNode();
            assert !n.isNull();
            assert !n.isArray();
            assert n.isTextual();
            assert n.asText().equals("v1");
            assert n.textValue().equals("v1");
            assert n.toString().equals("\"v1\"");

            n = jsonNode.path("/k1");
            assert n.isMissingNode();
            assert !n.isNull();
            assert n.asText().equals("");
            assert n.textValue() == null;
            assert n.toString().equals("");

            n = jsonNode.path("k3");
            assert !n.isMissingNode();
            assert !n.isNull();
            assert !n.isArray();
            assert !n.isTextual();
            assert n.asText().equals("");
            assert n.textValue() == null;
            assert n.toString().equals("{\"k3.1\":\"v3.1\"}");

            n = jsonNode.path("k3").path("k3.1");
            assert !n.isMissingNode();
            assert n.asText().equals("v3.1");
            assert n.textValue().equals("v3.1");
            assert n.toString().equals("\"v3.1\"");

            n = jsonNode.path("k3").path("k3.2");
            assert n.isMissingNode();
            assert n.asText().equals("");
            assert n.textValue() == null;
            assert n.toString().equals("");

            n = jsonNode.path("k4");
            assert !n.isMissingNode();
            assert !n.isNull();
            assert n.isArray();
            assert !n.isTextual();
            assert n.asText().equals("");
            assert n.textValue() == null;
            assert n.toString().equals("[{\"k4.1.1\":\"v4.1.1\",\"k4.1.2\":\"v4.1.2\"},{\"k4.2.1\":\"v4.2.1\",\"k4.2.2\":\"v4.2.2\"}]");

            n = jsonNode.get("k4").get(1);
            assert jsonNode.path("k4").path(1).equals(n);
            assert !n.isMissingNode();
            assert !n.isNull();
            assert !n.isArray();
            assert !n.isTextual();
            assert n.toString().equals("{\"k4.2.1\":\"v4.2.1\",\"k4.2.2\":\"v4.2.2\"}");

            n = jsonNode.path("x").path("y");
            assert n.isMissingNode();
            assert !n.isNull();
            assert !n.isTextual();
            assert n.asText().equals("");
            assert n.textValue() == null;
            assert n.toString().equals("");

            JSONObject jsonObject = new JSONObject(sjson);
            n = objectMapper.readTree(jsonObject.toString());

            assert n.path("k1").asText().equals("v1");
            assert n.path("k3").toString().equals("{\"k3.1\":\"v3.1\"}");
            assert n.path("k3").path("k3.1").toString().equals("\"v3.1\"");
            assert n.path("x").textValue() == null;

            List<String> act = new ArrayList<>();
            for(Iterator<String> it = jsonNode.fieldNames(); it.hasNext(); ) {
                act.add(it.next());
            }
            Set<String> exp = new HashSet<>(Arrays.asList("k1","k2","k3","k4"));
            assert act.size() == exp.size() && exp.containsAll(act);

            act.clear();
            for(Iterator<JsonNode> it = jsonNode.elements(); it.hasNext(); ) {
                n = it.next();
                if(n.isValueNode()) {
                    act.add(n.textValue());
                }
            }
            exp = new HashSet<>(Arrays.asList("v1","v2"));
            assert act.size() == 2 && exp.containsAll(act);

            act.clear();
            for(Iterator<Map.Entry<String,JsonNode>> it = jsonNode.fields(); it.hasNext();) {
                Map.Entry<String,JsonNode> kv = it.next();
                act.add(kv.getKey());
            }
            exp = new HashSet<>(Arrays.asList("k1","k2","k3","k4"));
            assert act.size() == exp.size() && exp.containsAll(act);

            AtomicInteger ctr = new AtomicInteger(0);
            n = objectMapper.readTree(sjson);
            traverseJsonNodeCount(n, ctr);
            assert ctr.get() == 12; // root, k1, k2, k3, k3.1, k4, k4[0], k4[1], k4.1.1, k4.1.2, k4.2.1, k4.2.2
        }
    }
    void traverseJsonNodeCount(JsonNode n, AtomicInteger ctr) {
        if(n == null) return;
        ctr.incrementAndGet();
        if(n.isValueNode()) return;
        for(Iterator<JsonNode> it = n.elements(); it.hasNext(); ) {
            traverseJsonNodeCount(it.next(), ctr);
        }
    }
    private ByteBuffer list2RoaringBitmapByteBuffer(List<Integer> li) throws IOException {
        assert li != null;
        int method = 0;
        if(method == 0) return list2RoaringBitmapByteBufferViaBB(li);
        return list2RoaringBitmapByteBufferViaBAOS(li);
    }

    private ByteBuffer list2RoaringBitmapByteBufferViaBB(List<Integer> li) throws IOException {
        int [] ai = li.stream().mapToInt(Integer::intValue).toArray();
        RoaringBitmap rb = RoaringBitmap.bitmapOf(ai);
        ByteBuffer bb = ByteBuffer.allocate(rb.serializedSizeInBytes());
        rb.serialize(new DataOutputStream(new ByteBufferBackedOutputStream(bb)));
        byte [] ba = bb.array();
        ByteBuffer bbResult = ByteBuffer.wrap(ba);
        return bbResult;
    }

    private ByteBuffer list2RoaringBitmapByteBufferViaBAOS(List<Integer> li) throws IOException {
        int [] ai = li.stream().mapToInt(Integer::intValue).toArray();
        RoaringBitmap rb = RoaringBitmap.bitmapOf(ai);
        ByteBuffer bb = ByteBuffer.allocate(rb.serializedSizeInBytes());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        rb.serialize(new DataOutputStream(baos));
        byte [] ba = baos.toByteArray();
        ByteBuffer bbResult = ByteBuffer.wrap(ba);
        return bbResult;
    }

    private List<Integer> byteBufferRoaringBitmap2List(ByteBuffer bb) throws IOException {
        assert bb != null;
        int method = 1;
        if(method == 0) return byteBufferRoaringBitmap2ListViaIRB(bb);
        return byteBufferRoaringBitmap2ListViaRB(bb);
    }

    private List<Integer> byteBufferRoaringBitmap2ListViaIRB(ByteBuffer bb) {
        assert bb != null && bb.limit() != 0;
        ImmutableRoaringBitmap irb = new ImmutableRoaringBitmap(bb);
        List<Integer> l = Arrays.stream(irb.toArray()).boxed().collect(Collectors.toList());
        return l;
    }

    private List<Integer> byteBufferRoaringBitmap2ListViaRB(ByteBuffer bb) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(bb.array());
        DataInputStream dis = new DataInputStream(bais);

        RoaringBitmap rb = new RoaringBitmap();
        rb.deserialize(dis);
        List<Integer> li = new ArrayList<>();
        for(Integer i: rb) li.add(i);
        return li;
    }

    private String byteBuffer2Base64String(ByteBuffer bb) {
        assert bb != null;
        byte [] ba = bb.array();
        return Base64.getEncoder().encodeToString(ba);
    }

    private ByteBuffer base64String2ByteBuffer(String s) {
        assert s != null;
        byte [] ba = Base64.getDecoder().decode(s);
        return ByteBuffer.wrap(ba);
    }

    @Test
    public void testHash() {
        List<String> list = Arrays.asList(
            "The cat in the hat",
            "the cat in the hat",
            "The hat in the cat",
            "the hat in the cat");
        StringBuilder sb;
        ByteBuffer bb8 = ByteBuffer.allocate(8);
        ByteBuffer bb4 = ByteBuffer.allocate(4);
        {
            bb4.clear();
            bb8.clear();
            sb = new StringBuilder();
            {
                CRC32 crc32 = new CRC32();
                crc32.update(list.get(0).getBytes());
                bb8.putLong(crc32.getValue());
                byte [] bytes = bb8.array();

                for(byte b : bytes) {
                    sb.append(String.format("%02x ", b));
                }
                p("%03d bytes %s\n", bytes.length, sb.toString());
            }
            bb4.clear();
            bb8.clear();
            sb = new StringBuilder();
            {
                CRC16 crc16 = new CRC16();
                for(byte b: list.get(0).getBytes())
                    crc16.update(b);
                bb4.putInt(crc16.value);
                byte [] bytes = bb4.array();
                for(byte b : bytes) {
                    sb.append(String.format("%02x ", b));
                }
                p("%03d bytes %s\n", bytes.length, sb.toString());
            }
            sb = new StringBuilder();
            bb4.clear();
            bb8.clear();
            {
                CRC16 crc16 = new CRC16();
                for(byte b: list.get(1).getBytes())
                    crc16.update(b);
                bb4.putInt(crc16.value);
                byte [] bytes = bb4.array();
                for(byte b : bytes) {
                    sb.append(String.format("%02x ", b));
                }
                p("%03d bytes %s\n", bytes.length, sb.toString());
            }
            sb = new StringBuilder();
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(list.get(0).getBytes());
                byte [] bytes = md.digest();
                for(byte b : bytes) {
                    sb.append(String.format("%02x ", b));
                }
                p("%03d bytes %s\n", bytes.length, sb.toString());
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            sb = new StringBuilder();
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(list.get(0).getBytes());
                byte [] bytes = md.digest();
                for(byte b : bytes) {
                    sb.append(String.format("%02x ", b));
                }
                p("%03d bytes %s\n", bytes.length, sb.toString());
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            sb = new StringBuilder();
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.digest(list.get(0).getBytes());
                byte [] bytes = md.digest();
                for(byte b : bytes) {
                    sb.append(String.format("%02x ", b));
                }
                p("%03d bytes %s\n", bytes.length, sb.toString());
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            sb = new StringBuilder();
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.digest(list.get(0).getBytes());
                byte [] bytes = md.digest();
                for(byte b : bytes) {
                    sb.append(String.format("%02x ", b));
                }
                p("%03d bytes %s\n", bytes.length, sb.toString());
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            sb = new StringBuilder();
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                byte [] bytes = md.digest(list.get(0).getBytes());
                for(byte b : bytes) {
                    sb.append(String.format("%02x ", b));
                }
                p("%03d bytes %s\n", bytes.length, sb.toString());
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            sb = new StringBuilder();
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
                byte [] salt = new byte[16];
                md.update(list.get(0).getBytes());
                md.digest(salt);
                byte [] bytes = md.digest();
                for(byte b : bytes) {
                    sb.append(String.format("%02x ", b));
                }
                p("%03d bytes %s\n", bytes.length, sb.toString());
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            sb = new StringBuilder();
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-1");
                byte [] bytes = md.digest(list.get(0).getBytes());
                for(byte b : bytes) {
                    sb.append(String.format("%02x ", b));
                }
                p("%03d bytes %s\n", bytes.length, sb.toString());
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            sb = new StringBuilder();
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                byte [] bytes = md.digest(list.get(0).getBytes());
                for(byte b : bytes) {
                    sb.append(String.format("%02x ", b));
                }
                p("%03d bytes %s\n", bytes.length, sb.toString());
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            sb = new StringBuilder();
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-384");
                byte [] bytes = md.digest(list.get(0).getBytes());
                for(byte b : bytes) {
                    sb.append(String.format("%02x ", b));
                }
                p("%03d bytes %s\n", bytes.length, sb.toString());
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            sb = new StringBuilder();
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-512");
                byte [] bytes = md.digest(list.get(0).getBytes());
                for(byte b : bytes) {
                    sb.append(String.format("%02x ", b));
                }
                p("%03d bytes %s\n", bytes.length, sb.toString());
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testConcurrentSkipListMap() {
        List<Integer> l;
        ConcurrentSkipListMap<Integer,Integer> map;
        map = new ConcurrentSkipListMap<>();
        Integer v;
        NavigableSet<Integer> navigableSet;
        Integer e;

        map.clear();
        l = Arrays.asList(0,2,4,6,8,10,12,14,16,18);
        for(Integer i: l) {
            map.put(i,i);
        }
        v = map.ceilingKey(7);
        assert v == 8;
        v = map.floorKey(7);
        assert v == 6;
        navigableSet = map.navigableKeySet();
        e = 0;
        for(Integer a: navigableSet) {
            assert a == e;
            e += 2;
        }

        map.clear();
        l = Arrays.asList(10,6,8,4,2,0,14,16,18,12);
        for(Integer i: l) {
            map.put(i,i);
        }
        v = map.ceilingKey(7);
        assert v == 8;
        v = map.floorKey(7);
        assert v == 6;
        navigableSet = map.navigableKeySet();
        e = 0;
        for(Integer a: navigableSet) {
            assert a == e;
            e += 2;
        }
        p("pass\n");
    }
    @Test
    public void testTreeMap() {
        List<Integer> l;
        TreeMap<Integer,Integer> map;
        map = new TreeMap<>();
        Integer v;
        NavigableSet<Integer> navigableSet;
        Integer e;

        map.clear();
        l = Arrays.asList(0,2,4,6,8,10,12,14,16,18);
        for(Integer i: l) {
            map.put(i,i);
        }
        v = map.ceilingKey(7);
        assert v == 8;
        v = map.floorKey(7);
        assert v == 6;
        navigableSet = map.navigableKeySet();
        e = 0;
        for(Integer a: navigableSet) {
            assert a == e;
            e += 2;
        }

        map.clear();
        l = Arrays.asList(10,6,8,4,2,0,14,16,18,12);
        for(Integer i: l) {
            map.put(i,i);
        }
        v = map.ceilingKey(7);
        assert v == 8;
        v = map.ceilingKey(18);
        assert v == 18;
        v = map.ceilingKey(19);
        assert v == null;
        v = map.floorKey(7);
        assert v == 6;
        v = map.floorKey(-1);
        assert v == null;
        v = map.firstKey();
        assert v == 0;
        v = map.lastKey();
        assert v == 18;
        navigableSet = map.navigableKeySet();
        e = 0;
        for(Integer a: navigableSet) {
            assert a == e;
            e += 2;
        }
        p("pass\n");
    }
    @Test
    public void testBinarySearchTree() {
        BinarySearchTree<Integer,Integer> map = new BinarySearchTree<>();
        List<Integer> l;
        Integer v;
        Integer e;

        l = Arrays.asList(0,2,4,6,8,10,12,14,16,18);
        map.clear();
        for(Integer i: l) {
            map.put(i,i);
        }
        v = map.height();
        assert v == 10;
        v = map.heightDiff();
        assert v == -9; // right heavy by 9
        map.printPreOrder();
        map.printPostOrder();
        map.printInOrder();
        map.printLevelOrder();
        for(Integer i: l) {
            map.del(i);
        }
        assert map.size() == 0;

        map.clear();
        l = Arrays.asList(10,6,8,2,4,0,14,18,16,12);
        for(Integer i: l) {
            map.put(i,i);
        }
        v = map.height();
        assert v == 4;
        v = map.heightDiff();
        assert v == 0;
        map.printPreOrder();
        map.printPostOrder();
        map.printInOrder();
        map.printLevelOrder();
        for(Integer i: l) {
            map.del(i);
        }
        assert map.size() == 0;

        p("testBinarySearchTree pass\n");
    }
    @Test
    public void testConsistentHash1() {
        ConsistentHash1 ch;
        Map<String,String> dataIn = new HashMap<>();
        boolean doPrint = false;
        try {
            ch = new ConsistentHash1(36);
            for(int i = 0; i < 4; i++) {
                ch.addNode();
                if(doPrint) ch.printTopology();
            }
            Set<Integer> nodeIds = ch.getNodeIds();
            ch.removeNode(0,true);
            if(doPrint) ch.printTopology();
            ch.removeNode(1,true);
            if(doPrint) ch.printTopology();
            ch.removeNode(2,true);
            if(doPrint) ch.printTopology();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testConsistentHash2() {
        ConsistentHash1 ch;
        Map<String,String> dataIn = new HashMap<>();
        boolean doPrint = false;
        int numData = 10000; // 200; // 8000;
        int numSlots = 720; // 36; //720;
        int numNodesMax = 20; // 4; // 10;
        int numNodesMin = 1;
        for(int i = 0; i < numData; i++) {
            String k = String.format("k%05d",i);
            String v = String.format("v%05d",i);
            dataIn.put(k,v);
        }
        try {
            int ctr;
            ch = new ConsistentHash1(numSlots);
            for(int i = 0; i < numNodesMax; i++) {
                ch.addNode();
                if(i == 0) {
                    for(Map.Entry<String,String> entry: dataIn.entrySet()) {
                        ch.put(entry.getKey(), entry.getValue());
                    }
                }
                if(doPrint) ch.printTopology();
                // now validate
                ctr = 0;
                for(Map.Entry<String,String> entry: dataIn.entrySet()) {
                    String va = ch.get(entry.getKey());
                    if(va == null || !entry.getValue().equals(va)) {
                        p("ERROR: ctr:%03d k:%s v:%s actual value:%s\n", ctr,entry.getKey(),entry.getValue(),va);
                        assert entry.getValue().equals(va);
                    }
                    ctr++;
                }
            }
            for(int i = numNodesMax-1; i >= numNodesMin; i--) {
                ch.removeNode(i,true);
                if(doPrint) ch.printTopology();
                // now validate
                ctr = 0;
                for(Map.Entry<String,String> entry: dataIn.entrySet()) {
                    String va = ch.get(entry.getKey());
                    if(va == null || !entry.getValue().equals(va)) {
                        p("ERROR: ctr:%03d k:%s v:%s actual value:%s\n", ctr,entry.getKey(),entry.getValue(),va);
                        assert entry.getValue().equals(va);
                    }
                    ctr++;
                }
            }
            //ch.printTopology();
            //ch.printSlotsAccessDistribution();
            p("pass testConsistentHash2 numData:%d numSlots:%d numMinNodes:%d numMaxNodes:%d\n",
                numData, numSlots, numNodesMin, numNodesMax);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testBlockMap() {
        try {
            List<Pair<Integer,Integer>> freelist;
            BlockMap blockMap = new BlockMap(64);
            freelist = blockMap.getFreelist();
            assert freelist != null && freelist.size() == 1;
            LinkedList<Pair<Integer,Integer>> inUse = new LinkedList<>();
            Pair<Integer,Integer> pair;
            pair = blockMap.alloc(4);
            assert pair != null;
            freelist = blockMap.getFreelist();
            assert freelist != null && freelist.size() == 1;
            inUse.add(pair);
            pair = blockMap.alloc(32);
            assert pair != null;
            freelist = blockMap.getFreelist();
            assert freelist != null && freelist.size() == 1;
            inUse.add(pair);
            pair = inUse.pollFirst();
            assert pair.k == 0 && pair.v == 3;
            blockMap.free(pair);
            freelist = blockMap.getFreelist();
            assert freelist != null && freelist.size() == 2;
            blockMap.free(20,30);
            freelist = blockMap.getFreelist();
            assert freelist != null && freelist.size() == 3;
            pair = blockMap.alloc(32);
            assert pair == null;
            freelist = blockMap.getFreelist();
            assert freelist != null && freelist.size() == 3;
            pair = blockMap.alloc(28);
            assert pair != null && pair.k == 36 && pair.v == 63;
            freelist = blockMap.getFreelist();
            assert freelist != null && freelist.size() == 2;
            pair = blockMap.alloc(1);
            assert pair != null && pair.k == 0 && pair.v == 0;
            freelist = blockMap.getFreelist();
            assert freelist != null && freelist.size() == 2;
            pair = blockMap.alloc(11);
            assert pair != null && pair.k == 20 && pair.v == 30;
            freelist = blockMap.getFreelist();
            assert freelist != null && freelist.size() == 1;
            pair = blockMap.alloc(3);
            assert pair != null && pair.k == 1 && pair.v == 3;
            freelist = blockMap.getFreelist();
            assert freelist != null && freelist.size() == 0;
            blockMap.free(30,60);
            freelist = blockMap.getFreelist();
            assert freelist != null && freelist.size() == 1;
            blockMap.free(0,10);
            freelist = blockMap.getFreelist();
            assert freelist != null && freelist.size() == 2;
            blockMap.free(62,63);
            freelist = blockMap.getFreelist();
            assert freelist != null && freelist.size() == 3;
            blockMap.free(61,61);
            freelist = blockMap.getFreelist();
            assert freelist != null && freelist.size() == 2;
            blockMap.free(11,29);
            freelist = blockMap.getFreelist();
            assert freelist != null && freelist.size() == 1;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class RBNode<K extends Comparable, V> extends BBTNode {
    int rb = 0;
    public RBNode(K k, V v) {
        this(k,v,0);
    }
    public RBNode(K k, V v, int rb) {
        super(k,v);
        this.rb = 0;
    }
    public void printNode() {
        System.out.printf("color:%s k:%5s v:%10s lchild:%5x rchild:%5x\n",
            rb == 0 ? "RED" : "BLK",
            k.toString(),
            v.toString(),
            (l == null ? null : l.id),
            (r == null ? null : r.id));
    }
}
class BBTNode<K extends Comparable,V> {
    K k;
    V v;
    static AtomicInteger staticIdGen = new AtomicInteger();
    int id;
    BBTNode<K,V> l;
    BBTNode<K,V> r;
    BBTNode<K,V> p;
    public static void clear() {
        staticIdGen.set(0);
    }
    public BBTNode(K k, V v) {
        this.k = k;
        this.v = v;
        id = staticIdGen.getAndIncrement();
    }
    public void printNode() {
        System.out.printf("k:%5s v:%10s lchild:%5x rchild:%5x\n",
            k.toString(),
            v.toString(),
            (l == null ? null : l.id),
            (r == null ? null : r.id));
    }
}

class AVLTree<K extends Comparable,V> {
    int count = 0;
    BBTNode<K,V> root;
    AtomicInteger ctr = new AtomicInteger();
    V get(K k) {
        ctr.set(0);
        return get(k, root, ctr);
    }
    private V get(K k, BBTNode<K,V> n, AtomicInteger ctr) {
        if     (n == null) return null;
        int cmp = n.k.compareTo(k);
        if     (cmp == 0)   return n.v;
        else if(cmp < 0)    return get(k, n.l, ctr);
        else                return get(k, n.r, ctr);
    }
    int h(BBTNode n) {
        if(n == null) return 0;
        int cntL = 1 + h(n.l);
        int cntR = 1 + h(n.r);
        return (cntL >= cntR) ? cntL : cntR;
    }
    int hDiff(BBTNode n) {
        if(n == null) return 0;
        int cntL = 1 + h(n.l);
        int cntR = 1 + h(n.r);
        return cntL - cntR;
    }
    void put(K k, V v) {
        count++;
    }

    /*
     * AVL rotation rules
     *
     * LL heavy =>
     * LR heavy =>
     * RR heavy =>
     * RL heavy =>
     */
    BBTNode rotate(BBTNode n) {
        int hDiff = hDiff(n);
        return n;
    }
    BBTNode rotateForLL(BBTNode n) {
        if(n == null || n.l == null || n.l.l == null) return n;

        BBTNode l = n.l;

        n.l = l.r;
        l.r = n;

        return l;
    }
    BBTNode rotateForLR(BBTNode<K,V> n) {
        if(n == null || n.l == null || n.l.r == null) return n;

        BBTNode l = n.l;
        BBTNode lr = l.r;

        l.r = lr.l;
        l.l = lr;

        n.l = lr.r;
        lr.r = n;

        return lr;
    }
    BBTNode rotateForRR(BBTNode<K,V> n) {
        if(n == null || n.r == null || n.r.r == null) return n;

        BBTNode r = n.r;

        n.r = r.l;
        r.l = n;

        return r;
    }
    BBTNode rotateForRL(BBTNode<K,V> n) {
        if(n == null || n.r == null || n.r.l == null) return n;

        BBTNode r = n.r;
        BBTNode rl = r.l;

        r.l = rl.r;
        r.r = rl;

        n.r = rl.l;
        rl.l = n;

        return rl;
    }
    boolean delete(K k) {
        count--;
        return false;
    }
    boolean delete(BBTNode n) {
        count--;
        return false;
    }
    boolean validateIsBalanced() {
        int hDiff = hDiff(root);
        if(Math.abs(hDiff) > 1) {
            printTree();
            return false;
        }
        return true;
        //return Math.abs(hDiff) <= 1;
    }
    void printTree() {
        printTree(root);
    }
    private void printTree(BBTNode<K,V> n) {

    }
}

class BinarySearchTreeTest {

}

class BinarySearchTree<K extends Comparable, V> {
    AtomicInteger total = new AtomicInteger();
    BBTNode<K,V> root = null;
    boolean isOverwriteMode = false;
    AtomicInteger ctr = new AtomicInteger();
    public BinarySearchTree() {
        this(true);
    }
    public BinarySearchTree(boolean isOverwriteMode) {
        setIsOverwriteMode(isOverwriteMode);
    }
    void setIsOverwriteMode(boolean isOverwriteMode) {
        this.isOverwriteMode = isOverwriteMode;
    }
    boolean getIsOverwriteMode() {
        return isOverwriteMode;
    }
    public void clear() {
        root = null;
        ctr.set(0);
        total.set(0);
        BBTNode.clear();
    }
    public V get(K k) {
        ctr.set(0);
        return get(k, root, ctr);
    }
    protected V get(K k, BBTNode<K,V> n, AtomicInteger ctr) {
        if(n == null) return null;
        ctr.incrementAndGet();
        int cmp = n.k.compareTo(k);
        return (cmp == 0) ?
            n.v : (cmp > 0) ?
                get(k,n.l,ctr) :
                get(k,n.r,ctr);
    }
    public void put(K k, V v) {
        BBTNode n = new BBTNode<>(k,v);
        if(root == null) {
            root = n;
            total.incrementAndGet();
        }
        else { put(root, n); }
    }
    protected void put(BBTNode n, BBTNode c) {
        if(n == null) return;
        int cmp = n.k.compareTo(c.k);
        if(cmp == 0) {
            if(isOverwriteMode) n.v = c.v;
        }
        else if(cmp > 0) {
            if(n.l == null){
                n.l = c;
                total.incrementAndGet();
            }
            else            put(n.l,c);
        }
        else if(cmp < 0) {
            if(n.r == null){
                n.r = c;
                total.incrementAndGet();
            }
            else            put(n.r,c);
        }
    }
    public int size() {
        return total.get();
    }
    public void del(K k) {
        if(root == null) return;
        del1(null, root, k);
    }
    // assign use parent as arg in leftmost or rightmost, no return val
    protected void del1(BBTNode p, BBTNode n, K k) {
        if(n == null) return;
        int cmp = n.k.compareTo(k);
        if(cmp == 0) {
            total.decrementAndGet();
            BBTNode child = getMostR(n, n.l, true);
            if(child != null) {
                if(p == null || n == root) {
                    root = child;
                }
                child.r = n.r;
            } else {
                child = getMostL(n, n.r, true);
                if(p == null || n == root) {
                    root = child;
                }
                child.l = n.l;
            }
        }
        else if(cmp > 1) { del1(n,n.l,k); }
        else { del1(n,n.r,k); }
    }
    // no parent reference, recursive with parent for leftmost||rightmost
    protected BBTNode del2(BBTNode n, K k) {
        if(n == null) return null;
        int cmp = n.k.compareTo(k);
        if      (cmp == 0) {
            total.decrementAndGet();
            BBTNode child = getMostR(n, n.l, true);
            if(child != null) return child;
            return getMostL(n, n.r, true);
        }
        else if (cmp > 0) return del2(n.l, k);
        return del2(n.r, k);
    }
    // no parent reference, iterative leftmost||rightmost
    public void del3(BBTNode n, K k) {
        if(n == null) return;
        int cmp = n.k.compareTo(k);
        if(cmp == 0) {

        }
        else {
            if (n.l != null) {

            }
            if (n.r != null) {

            }
        }
    }
    protected BBTNode getMostL(BBTNode p, BBTNode n, boolean doDelete) {
        if(n == null) return null;
        if(!doDelete) {
            if(n.l == null) return n;
        } else {
            if(n.l == null) {
                if(n == root || p == null) { // n == root <-> p == null
                    root = n.r;
                }
                else {
                    if(p.l == n) p.l = n.r;
                    if(p.r == n) p.r = n.r;
                    n.r = null;
                }
                return n;
            }
        }
        return getMostL(n, n.l, doDelete);
    }
    protected BBTNode getMostR(BBTNode p, BBTNode n, boolean doDelete) {
        if(n == null) return null;
        if(!doDelete) {
            if(n.r == null) return n;
        } else {
            if(n.r == null) {
                if(n == root || p == null) { // n == root <-> p == null
                    root = n.l;
                }
                else {
                    if(p.l == n) p.l = n.l;
                    if(p.r == n) p.r = n.l;
                    n.l = null;
                }
                return n;
            }
        }
        return getMostR(n, n.r, doDelete);
    }
    protected BBTNode getMostL(BBTNode p, BBTNode n) {
        if(n == null) return null;
        if(n.l == null) {
            if(p == null || n == root) {
                root = n.r;
            }
            else {
                if(p.l == n) p.l = n.r;
                if(p.r == n) p.r = n.r;
            }
            return n;
        }
        return getMostL(n,n.l);
    }
    protected BBTNode getMostR(BBTNode p, BBTNode n) {
        if(n == null) return null;
        if(n.r == null) {
            if(p == null || n == root) {
                root = n.l;
            }
            else {
                if(p.l == n) p.l = n.l;
                else p.r = n.l;
            }
            return n;
        }
        return getMostL(n,n.r);
    }
    public int height() {
        return h(root);
    }
    public int heightDiff() {
        return hDiff(root);
    }
    protected int h(BBTNode<K,V> n) {
        if(n == null) return 0;
        int cntL = 1 + h(n.l);
        int cntR = 1 + h(n.r);
        return (cntL >= cntR) ? cntL : cntR;
    }
    protected int hDiff(BBTNode<K,V> n) {
        if(n == null) return 0;
        int cntL = 1 + h(n.l);
        int cntR = 1 + h(n.r);
        return cntL - cntR;
    }
    public void printPreOrder() {
        System.out.printf("PreOrder\n");
        printPreOrder(root);
    }
    public void printInOrder() {
        System.out.printf("InOrder\n");
        printInOrder(root);
    }
    public void printPostOrder() {
        System.out.printf("PostOrder\n");
        printPostOrder(root);
    }
    public void printLevelOrder() {
        System.out.printf("LevelOrder\n");
        printLevelOrder(root);
    }
    protected void printPreOrder(BBTNode n) {
        if(n == null) return;
        printPreOrder(n.l);
        n.printNode();
        printPreOrder(n.r);
    }
    protected void printInOrder(BBTNode n) {
        if(n == null) return;
        n.printNode();
        printPreOrder(n.l);
        printPreOrder(n.r);
    }
    protected void printPostOrder(BBTNode n) {
        if(n == null) return;
        printPreOrder(n.l);
        printPreOrder(n.r);
        n.printNode();
    }
    protected void printLevelOrder(BBTNode n) {
        Queue<BBTNode> q = new LinkedList<>();
        if(n != null) q.add(n);
        while(q.size() != 0) {
            BBTNode c = q.poll();
            c.printNode();
            if(c.l != null) q.add(c.l);
            if(c.r != null) q.add(c.r);
        }
    }
}

/*
 * 1. each node is R || B
 * 2. root is B
 * 3. all leaves are B
 * 4. if node is R, then both its children are B
 * 5. every path from a given node to its descendent NIL (empty leaf) contains same number of B nodes.
 *
 * denote B == 0, R == 1
 *
 * insert algos for rebalancing
 * - insert like BST
 * - if inserted N is root, color is black
 * - inserted N color is red
 * - restore properties if needed
 *   - if parent P is black
 *     N is already red, do nothing
 *   - if parent P is red
 *     if U is red
 *       do recolor/flip but no rearrange
 *                  BG                      BG
 *              RP      BU        ->    BP      BU
 *           RN   1    2  3           RN  1    2  3
 *     if U is black
 *       4 cases based on color of parent P, grandparent G, uncle U
 *           case: left red N and red P and black G black uncle
 *                   BG                      BP
 *               RP      BU        ->    RN      RG
 *             RN  1    2  3                    1   BU
 *                                                 2  3
 *           case: right red N and red P and black G and black uncle
 *                   BG                      BN
 *               RP      BU        ->    RP      RG
 *              1  RN   2  3            1           BU
 *                                                 2  3
 *           mirror cases for remainder
 *
 */
class RedBlackTree<K extends Comparable,V> {
    int count = 0;
    BBTNode<K,V> root = null;
    AtomicInteger ctr = new AtomicInteger();
    V get(K k) {
        ctr.set(0);
        return get(k, root, ctr);
    }
    private V get(K k, BBTNode<K,V> n, AtomicInteger ctr) {
        if     (n == null) return null;
        int cmp = n.k.compareTo(k);
        if     (cmp == 0)   return n.v;
        else if(cmp < 0)    return get(k, n.l, ctr);
        else                return get(k, n.r, ctr);
    }
    int h(BBTNode<K,V> n) {
        if(n == null) return 0;
        int cntL = 1 + h(n.l);
        int cntR = 1 + h(n.r);
        return (cntL >= cntR) ? cntL : cntR;
    }
    int hDiff(BBTNode<K,V> n) {
        if(n == null) return 0;
        int cntL = 1 + h(n.l);
        int cntR = 1 + h(n.r);
        return cntL - cntR;
    }
    void put(K k, V v) {
        BBTNode<K,V> n = new BBTNode(k,v);
        if(root == null){
            root = n;
        }
    }
    void put(BBTNode<K,V> n, BBTNode<K,V> newNode) {
        if(n == null) return;
        if(n.l == null) {

        }
    }


    /*
     * RedBlack rotation rules
     *
     * LL =>
     */
    void rotateLL(BBTNode<K,V> n) {

    }
    void rotateLR(BBTNode<K,V> n) {

    }
    void rotateRR(BBTNode<K,V> n) {

    }
    void rotateRL(BBTNode<K,V> n) {

    }
    boolean delete(K k) {
        return false;
    }
    boolean delete(BBTNode n) {
        return false;
    }
    void printTree() {
        printTree(root);
    }
    private void printTree(BBTNode<K,V> n) {

    }
}

/*
 * data structure that quickly finds gaps for allocation use.
 * addRange(int beg, int end)
 * delRange(int beg, int end)
 * getBlock(int size)
 * isFree(int idx)
 * print()
 *
 * initially, there is 1 block that describes beg til end of all free.
 */
class BlockMap {
    LLNode head = null;
    int maxSize = 127;
    List<Pair<Integer,Integer>> inUse = new LinkedList<>();
    public BlockMap() {
        this(128);
    }
    public BlockMap(int maxSize) {
        this.maxSize = maxSize-1;
        clear();
    }
    public void clear() {
        head = LLNode.builder().beg(0).end(maxSize).build();
    }
    public List<Pair<Integer,Integer>> getFreelist() {
        List<Pair<Integer,Integer>> ret = new ArrayList<>();
        for(LLNode t = head; t != null; t = t.getN()) {
            Pair<Integer,Integer> pair = new Pair<>(t.getBeg(),t.getEnd());
            ret.add(pair);
        }
        return ret;
    }
    /*
     * this is linear implementation, could also use treemap or skiplist
     */
    public Pair<Integer,Integer> alloc(int size) {
        for(LLNode t = head, p = null; t != null; p = t, t = t.getN()) {
            if((t.getEnd() - t.getBeg()+1) >= size) {
                Pair<Integer,Integer> pair = new Pair<>(t.getBeg(),t.getBeg()+size-1);
                if(size == (t.getEnd()-t.getBeg()+1)) {
                    if(p != null) {
                        p.setN(t.getN());
                    } else {
                        head = t.getN();
                    }
                    if(t.getN() != null) {
                        t.getN().setP(p);
                    }
                } else {
                    t.setBeg(t.getBeg()+size);
                }
                return pair;
            }
        }
        return null;
    }
    public void free(Pair<Integer,Integer> pair) throws Exception {
        free(pair.k,pair.v);
    }
    public void free(int beg, int end) throws Exception {
        if(head == null) {
            head = LLNode.builder().beg(beg).end(end).build();
            return;
        }
        for(LLNode t = head, p = null; t != null; p = t, t = t.getN()) { // ordering matters for comma!
            if(p == null) {
                if(end < t.getBeg()) {
                    if(end+1 == t.getBeg()) {
                        t.setBeg(beg);
                    } else {
                        LLNode n = LLNode.builder().beg(beg).end(end).n(t).build();
                        head = n;
                        t.setP(n);
                    }
                    break;
                }
            }
            else if(p.getEnd() < beg && end < t.getBeg()) {
                if(p.getEnd()+1 == beg && end+1 == t.getBeg()) {
                    p.setEnd(t.getEnd());
                    p.setN(t.getN());
                    if(t.getN() != null) {
                        t.getN().setP(p);
                    }
                } else if(p.getEnd()+1 == beg) {
                    p.setEnd(end);
                } else if(end+1 == t.getBeg()) {
                    t.setBeg(beg);
                } else {
                    if(p == null || p.getN() != t || t.getP() != p) {
                        throw new Exception("invalid case");
                    }
                    LLNode n = LLNode.builder().beg(beg).end(end).p(p).n(t).build();
                    p.setN(n);
                    t.setP(n);
                }
                break;
            } else if (t.getEnd() < beg && t.getN() == null) {
                if(t.getEnd()+1 == beg) {
                    t.setEnd(end);
                } else {
                    LLNode n = LLNode.builder().beg(beg).end(end).p(t).build();
                    t.setN(n);
                }
                break;
            }
        }
    }
    public boolean isFree(int idx) {
        return isFree(idx,idx);
    }
    public boolean isFree(int beg, int end) {
        for(LLNode t = head; t != null; t = t.getN()) {
            if (t.getBeg() <= beg && end <= t.getEnd()) return true;
            if (t.getEnd() < beg) return false;
        }
        return false;
    }
    public void print() {
        LLNode t = head;
        while(t != null) {
            System.out.printf("%s\n", t.sval());
            t = t.getN();
        }
    }
}

class RangeMap {

}

@Builder
@Data
class Pair<K,V> {
    K k;
    V v;
    @Override
    public String toString() {
        return String.format("k:%s,v:%s",k,v);
    }
}

@Builder
@Data
class LLNode {
    LLNode p;
    LLNode n;
    int id;
    int beg;
    int end;
    @Override
    public String toString() {
        return sval();
    }
    String sval() {
        return String.format("id:%2d begin:%3d end:%3d",id,beg,end);
    }
}
/*
 * Use balancedBST<slotid, node> to find next largest value if slotid is missing
 * Use map of node -> list<slotid>
 * Use map of slot -> node
 */
class MapSS {
    HashMap<String,String> map = new HashMap<>();
    int id;
    public MapSS(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }
    public String get(String k) {
        return map.get(k);
    }
    public void put(String k, String v) {
        map.put(k,v);
    }
    public void remove(String k) {
        map.remove(k);
    }
    public Set<String> keySet() {
        return map.keySet();
    }
    public int size() {
        return map.size();
    }
    public void clear() {
        map.clear();
    }
}

class MapCH<K extends Comparable,V> {
    HashMap<K,V> map = new HashMap<>();
    int id;
    public MapCH(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }
    public V get(String k) {
        return map.get(k);
    }
    public void put(K k, V v) {
        map.put(k,v);
    }
    public void remove(K k) {
        map.remove(k);
    }
    public Set<K> keySet() {
        return map.keySet();
    }
    public int size() {
        return map.size();
    }
    public void clear() {
        map.clear();
    }
}

class ConsistentHash1 {
    AtomicInteger nodeIdGen = new AtomicInteger(0);
    AtomicInteger numNodes = new AtomicInteger(0);
    AtomicInteger perfCtr = new AtomicInteger();
    int numSlots = 720;
    int maxNodes = 360;
    MessageDigest md;
    ByteBuffer bb32;
    CRC32 crc32 = new CRC32();
    Map<Integer,AtomicInteger> mapStatsSlotsDistribution;
    Map<String,AtomicInteger> mapKeyAccessDistribution;
    /*
     * mmap                         nodeId -> nodeKVStore
     * mapNode2Slots                nodeId -> collection slots on ring
     * mapSlot2Node                 slotId -> nodeId
     */
    Map<Integer,MapSS>              mmap = new HashMap<>();
    Map<Integer,Set<Integer>>       mapNode2Slots = new HashMap<>();
    TreeMap<Integer,Integer>        mapSlot2Node = new TreeMap<>();
    public ConsistentHash1() {
        this(720);
    }
    void p(String f, Object ...o) {
        System.out.printf(f, o);
    }
    public ConsistentHash1(int numSlots) {
        clear();
        this.numSlots = numSlots;
        this.maxNodes = numSlots/4; // to avoid the 0 slot allocation to new nodes
        bb32 = ByteBuffer.allocate(32);
        mapStatsSlotsDistribution = new TreeMap<>();
        mapKeyAccessDistribution = new TreeMap<>();
        for(int i = 0; i < numSlots; i++) {
            mapStatsSlotsDistribution.put(i, new AtomicInteger());
        }
    }
    public void clear() {
        nodeIdGen.set(0);
        numNodes.set(0);
        mmap.clear();
        mapNode2Slots.clear();
        mapSlot2Node.clear();
    }
    private int hash(String k) throws Exception {
        return hashCRC32(k);
    }
    private int hashCRC32(String k) {
        crc32.reset();
        crc32.update(k.getBytes());
        long l = crc32.getValue();
        int ret = (int) l;
        return ret;
    }
    private int hashMD5(String k) throws Exception {
        md = MessageDigest.getInstance("MD5");
        md.update(k.getBytes());
        byte [] digest = md.digest();
        bb32.clear();
        bb32.put(digest);
        bb32.flip();
        int ret = bb32.getInt();
        if(ret == 0) {
            throw new Exception("hashing not working");
        }
        return ret;
    }
    private MapSS getNode(String k) throws Exception {
        int h = hash(k);
        int slotCandidate = ((h & 0x00ff_ffff) % numSlots);
        if(mapKeyAccessDistribution.containsKey(k)) {
            mapKeyAccessDistribution.get(k).getAndIncrement();
        } else {
            mapKeyAccessDistribution.put(k, new AtomicInteger(1));
        }
        Integer slotId = mapSlot2Node.ceilingKey(slotCandidate);
        if(slotId == null) {
            slotId = mapSlot2Node.firstKey();
        }
        if(slotId == null) {
            throw new Exception("Missing slots. slotIdEqualOrNext is null");
        }
        mapStatsSlotsDistribution.get(slotId).incrementAndGet();
        Integer nodeId = mapSlot2Node.get(slotId);
        if(nodeId == null) {
            throw new Exception(String.format("NodeId is null for key: %s slot: %d\n", k, slotId));
        }
        MapSS map = mmap.get(nodeId);
        if(map == null) {
            throw new Exception(String.format("No node exists for key: %s\n", k));
        }
        return map;
    }
    public void put(String k, String v) throws Exception {
        MapSS map = getNode(k);
        map.put(k,v);
    }
    public String get(String k) throws Exception {
        MapSS map = getNode(k);
        return map.get(k);
    }
    public void remove(String k) throws Exception {
        MapSS map = getNode(k);
        map.remove(k);
    }
    public void addNode() throws Exception {
        if(maxNodes <= numNodes.get()){
            p("Exceeded maxNodes %d", maxNodes);
            return;
        }
        int dstNodeId = nodeIdGen.getAndIncrement();
        MapSS dstNode = new MapSS(dstNodeId);
        mapNode2Slots.put(dstNodeId, new HashSet<>());
        mmap.put(dstNodeId, dstNode);
        if(numNodes.get() == 0) {
            Set<Integer> slots = mapNode2Slots.get(dstNodeId);
            for(int i = 0; i < numSlots; i++) {
                slots.add(i);
                mapSlot2Node.put(i, dstNodeId);
            }
        } else {
            // should be done in parallel.
            double pctXferPerNode = (1.0*numSlots/(numNodes.get()+1))/(numSlots);
            mmap.keySet().stream().filter(srcNodeId -> srcNodeId != dstNodeId)
                .forEach(srcNodeId -> transferSlotsFromSrcToDstId(srcNodeId, dstNodeId, pctXferPerNode));
            for(Integer srcNodeId: mmap.keySet()) {
                if(srcNodeId == dstNodeId) continue;
                MapSS srcNode = mmap.get(srcNodeId);
                transferKeysFromSrcToDstNode(srcNode, dstNode, pctXferPerNode);
            }
        }
        numNodes.incrementAndGet();
    }
    protected void transferSlotsFromSrcToDstId(Integer srcNodeId, Integer dstNodeId, double pctTransfer) {
        Set<Integer> srcSlots = mapNode2Slots.get(srcNodeId);
        List<Integer> srcSlotsScrambled = new ArrayList<>(srcSlots);
        Collections.shuffle(srcSlotsScrambled);
        int numSlotsToTransfer = (int)(srcSlots.size() * pctTransfer);
        int ctr = 0;
        List<Integer> slotsTransferred = new ArrayList<>();
        for(Integer slot: srcSlotsScrambled) {
            slotsTransferred.add(slot);
            ctr++;
            if(ctr >= numSlotsToTransfer) break;
        }
        Set<Integer> dstSlots = mapNode2Slots.get(dstNodeId);
        for(Integer slot: slotsTransferred) {
            srcSlots.remove(slot);
            dstSlots.add(slot);
            mapSlot2Node.put(slot,dstNodeId);
        }
    }
    // i have to iterate through every key to see which ones need to be rehashed.
    // expected number to rehash is numKeysToTransfer. how do i do better??
    // this assumes the slots have already been remapped.
    protected void transferKeysFromSrcToDstNode(MapSS srcNode, MapSS dstNode, double pctTransfer) throws Exception {
        Set<String> keysTransferred = new HashSet<>();
        for(String key: srcNode.keySet()) {
            MapSS newNode = getNode(key);
            if(newNode == dstNode) {
                String val = srcNode.get(key);
                dstNode.put(key,val);
                keysTransferred.add(key);
            }
        }
        for(String key: keysTransferred) {
            srcNode.remove(key);
        }
    }
    public Set<Integer> getNodeIds() {
        Set<Integer> set = new HashSet<>(mmap.keySet());
        return set;
    }
    /* you can transfer data from nodes and remap. if removeGracefully is false,
     * then do not do data transfer within node. only do slot remapping.
     * if there is replica, one of them gets promoted to master.
     */
    public void removeNode(int srcNodeId, boolean removeGracefully) throws Exception {
        MapSS srcNode = mmap.get(srcNodeId);
        if(srcNode == null) return;
        List<Integer> srcSlots = new ArrayList<>(mapNode2Slots.get(srcNodeId)); // node not deleted yet
        List<Integer> nodeIds = new ArrayList<>(mmap.keySet());
        int numSlotsToTransfer = srcSlots.size();
        int numSlotsPerNode = (int)(1.0*numSlotsToTransfer / (nodeIds.size() - 1));
        int slotCount = 0;
        for(Integer dstNodeId: nodeIds) {
            if(dstNodeId == srcNodeId) continue;
            Set<Integer> dstSlots = mapNode2Slots.get(dstNodeId);
            for(int i = 0; i < numSlotsPerNode && slotCount < numSlotsToTransfer; i++, slotCount++) {
                Integer slotId = srcSlots.get(slotCount);
                mapSlot2Node.put(slotId,dstNodeId);
                dstSlots.add(slotId);
            }
        }
        if(slotCount < numSlotsToTransfer) {
            // distribute each remaining in a single fashion
            for(int idx = 0; slotCount < numSlotsToTransfer; slotCount++) {
                if(nodeIds.get(idx) == srcNodeId) idx++;
                int dstNodeId = nodeIds.get(idx);
                idx = (idx+1) % nodeIds.size();
                if(dstNodeId == srcNodeId) {
                    dstNodeId = nodeIds.get(idx);
                }
                Integer slotId = srcSlots.get(slotCount);
                mapSlot2Node.put(slotId,dstNodeId);
                Set<Integer> dstSlots = mapNode2Slots.get(dstNodeId);
                dstSlots.add(slotId);
            }
        }
        if(removeGracefully) {
            // rehash every key in nodeId to new nodes
            int numKeysProcessed = 0;
            for(String key: srcNode.keySet()) {
                MapSS dstNode = getNode(key);
                dstNode.put(key, srcNode.get(key));
                numKeysProcessed++;
            }
            if(numKeysProcessed != srcNode.size()) {
                String msg = String.format("transfer error. numkeysprocessed:%d map.size:%d\n",
                    numKeysProcessed, srcNode.size());
                throw new Exception(msg);
            }
        }
        mapNode2Slots.get(srcNodeId).clear();
        mapNode2Slots.remove(srcNodeId);
        mmap.remove(srcNodeId);
        numNodes.decrementAndGet();
    }
    public void printTopology() throws Exception {
        p("\nPrintTopology: numNodes:%2d numSlots:%4d maxSlotsAllowed:%4d\n", numNodes.get(), numSlots, maxNodes);
        Set<Integer> slotsVisited = new HashSet<>();
        int modVal = 50;
        String padVal;
        if(numSlots < 100) {
            modVal = 50;
            padVal = "%02d";
        } else if(numSlots < 1000) {
            modVal = 30;
            padVal = "%03d";
        } else {
            modVal = 20;
            padVal = "%04d";
        }
        for(Integer nodeId: mmap.keySet()) {
            Set<Integer> slotIds = mapNode2Slots.get(nodeId);
            p("    nodeId:%3d numSlots:%4d\n", nodeId, slotIds.size());
            StringBuilder sb = new StringBuilder();
            Iterator<Integer> it = slotIds.iterator();
            for(int i = 0; it.hasNext(); i++) {
                Integer slotId = it.next();
                if (slotsVisited.contains(slotId)) {
                    throw new Exception(String.format("ERROR duplicate slotId:%d",slotId));
                }
                slotsVisited.add(slotId);
                if(i > 0 && i % modVal == 0) sb.append("\n");
                sb.append(String.format(padVal,slotId));
                if(it.hasNext()) sb.append(",");
            }
            p(sb.toString());
            p("\n");
        }
        assert slotsVisited.size() == numSlots;
        printNodeDataSummary();
        //printSlotsAccessDistribution();
        //printKeyAccessDistribution();
    }
    public void printNodeDataSummary() {
        p("\nNode Details\n");
        for(Integer nodeId: mmap.keySet()) {
            MapSS map = mmap.get(nodeId);
            Set<String> keySet = map.keySet();
            p("node:%3d numKeys:%6d\n", nodeId, keySet.size());
        }

    }
    public void printSlotsAccessDistribution() {
        p("\nSlots Access Distribution\n");
        for(Map.Entry<Integer,AtomicInteger> e: mapStatsSlotsDistribution.entrySet()) {
            p("slot:%5d access:%5d\n",e.getKey(),e.getValue().get());
        }
    }
    public void printKeyAccessDistribution() {
        p("\nKey Access Distribution\n");
        for(Map.Entry<String,AtomicInteger> e: mapKeyAccessDistribution.entrySet()) {
            p("key:%10s access:%5d\n",e.getKey(),e.getValue().get());
        }
    }
}

/**
 * We are given a processed json, a JsonNode root..
 * A path is a string hierarchy representation of a key in JsonNode.
 * We would like to get the most frequently accessed path, given a key.
 *
 * populatePaths takes JsonNode and builds path:count representation
 * accessPath takes path and increments the path count
 * getMostAccessedPath takes key and returns the most accessed path
 *
 * json1 : {
 *     a: {
 *         a:aa
 *     },
 *     b:{
 *         a:ba,
 *         c:{
 *             a:bca,
 *             aa:bcaa,
 *             b:bcb
 *         }
 *     }
 * }
 *
 * paths for key a and arbitrary count are:
 * /json1/a:2
 * /json1/a/a:9
 * /json1/b/a:10
 * /json1/b/c/a:8
 *
 */
class JsonPathLookup {
    Map<String, PriorityQueue<CacheNode>> mapQ = new HashMap<>();
    Map<String, CacheNode> mapNodes = new HashMap<>();
    public void accessPath(String path) {
        CacheNode node = mapNodes.get(path);
        if(node == null) return;
        mapQ.get(node.getK()).remove(node);
        node.getW().incrementAndGet();
        mapQ.get(node.getK()).add(node);
    }
    public String getMostAccessedPath(String key) {
        return Optional.ofNullable(mapQ.get(key)).map(q -> q.peek().getV()).orElse(null);
    }
    public void populatePaths(JsonNode node) {
        traverseNodes(node, "", null);
    }
    void traverseNodes(JsonNode node, String key, String path) {
        if(node == null) return;
        String newpath = (path == null) ? "/" + key : path + "/" + key;
        if(node.isValueNode())
            addPath(key, newpath);
        for(Iterator<String> it = node.fieldNames(); it.hasNext(); ) {
            String itKey = it.next();
            traverseNodes(node.get(itKey), itKey, newpath);
        }
    }
    void addPath(String key, String path) {
        PriorityQueue<CacheNode> q = mapQ.get(key);
        if(q == null) {
            q = new PriorityQueue<>((x,y) -> y.getW().get() - x.getW().get());
            mapQ.put(key, q);
        }
        //CacheNode node = CacheNode.builder().k(key).v(path).build();
        CacheNode node = new CacheNode();
        node.setK(key);
        node.setV(path);
        q.add(node);
        mapNodes.put(path,node);
    }
}

@Data
class CacheNode {
    String k;
    String v;
    AtomicInteger w = new AtomicInteger(0);
    //public CacheNode(String k, String v) { this.k = k; this.v = v; w = new AtomicInteger(0); }
}