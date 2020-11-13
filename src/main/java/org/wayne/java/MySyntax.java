package org.wayne.java;

import org.wayne.misc.Utils;

import org.wayne.main.MyBasic;

//import javax.annotation.Nonnull;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MySyntax implements MyBasic {

    Utils utils = new Utils();
    Set<String> setString = new HashSet<>();
    Set<Integer> setInteger = new HashSet<>();
    Set<Long> setLong = new HashSet<>();
    Set<Object> setObject = new HashSet<>();
    Map<Class, Set<?>> mapAllTypes = new HashMap<>();

    @Override
    public void shutdown() {

    }

    @Override
    public void init() {
        setString.add("hello");
        setString.add("goodbye");
        setInteger.add(1);
        setInteger.add(2);
        setLong.add(100L);
        setLong.add(200L);
        setObject.add("helloObject");
        setObject.add("goodbyeObject");
        mapAllTypes.put(String.class, setString);
        mapAllTypes.put(Integer.class, setInteger);
        mapAllTypes.put(Long.class, setLong);
        mapAllTypes.put(Object.class, setObject);
    }

    public MySyntax() {
        init();
    }
    /*
     * Can I have interface in class? yes, but you cannot have inner interface.
     */
    public interface Check {
        boolean test(CheckObject o);
    }
    public interface CheckObject {

    }
    public void testConversion1() {
        List<Integer> list = Arrays.asList(0,1,2,3,4,5,6,7,8,9);
        Set<Integer> set1 = new HashSet<>(Arrays.asList(0,1,2,3,4,5,6,7,8,9));
        Set<Integer> set2 = Stream.of(0,1,2,3,4,5,6,7,8,9).collect(Collectors.toSet());
        Set<Integer> set3 = Stream.of(0,1,2,3,4,5,6,7,8,9).collect(Collectors.toCollection(HashSet::new));
        Set<Integer> set4 = new HashSet<>(list);
    }

    static void p(String f, Object ...o) {
        System.out.printf(f, o);
    }

    String f(String f, Object ...o) {
        return String.format(f, o);
    }

    public String testArgs1(String s1, String s2) {
        String res = f("testArgs1 %s %s\n", s1,s2);
        p(res);
        return res;
    }

    String testArgs2(String s1, String s2) {
        String res = f("testArgs2 %s %s\n", s1,s2);
        p(res);
        return res;
    }

    public String testArgs3(int i1, Integer i2, String s3) {
        String res = f("testArgs3 %d %d %s\n", i1,i2,s3);
        p(res);
        return res;
    }

    public void testMethodName() {
        try{
            String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
            // this.getClass().getEnclosingMethod().getName()
            p("run %s\n", new Object() {}.getClass().getEnclosingMethod().getName());
        } catch(Exception e) {
            p(e.getMessage());
        }
    }

    public String testArgs4(int id, List<Integer> lint, List<String> lstr) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("id:%d ", id));
        lint.stream().forEach(i -> sb.append(String.format("%d,", i)));
        lstr.stream().forEach(s -> sb.append(String.format("%s,", s)));
        return sb.toString();
    }

    public String testArgs5(int i1, double i2, boolean s3) {
        String res = f("testArgs5 %d %f %s\n", i1,i2,s3);
        p(res);
        return res;
    }

    public void testURL() {
        String [] sary = {
            "http://abc.com/x/y/z?what=if&it=is",
            "http://abc.com/x/y/z?",
            "http://abc.com/x/y/z?what=if&it=is#",
            "http://abc.com/x/y/z",
            "http://abc.com/x/y/z/index.html",
            "http://abc.com",
            "http://@abc.com",
            "http://abc.com/",
            "http://abc.com/index.html",
            //"bad://abc.com",
            "http://user@abc.com:80",
            "http://user@abc.com:80/x/y/z",
            "http://user@abc.com:80/x/y/z?what=if",
            "http:user@abc.com:80/x/y/z?what=if"
        };
        long tbeg = System.currentTimeMillis();
        for(String s: sary) {
            URL url = null;
            try {
                url = new URL(s);
                p("s:%-40s, protocol:%s, auth:%s, host:%s, port:%d, path:%s, query:%s, file:%s, ref:%s, tostr:%s\n",
                    s, url.getProtocol(), url.getAuthority(), url.getHost(),
                    url.getPort(), url.getPath(), url.getQuery(), url.getFile(), url.getRef(),
                    url.toString()
                );
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        long tend = System.currentTimeMillis();
        long diff = tend - tbeg;
        p("time for URL:  %d\n", diff);
    }
    public void testString() {
        {
            StringBuilder sb = new StringBuilder();
            String ref1 = "others";
            sb.append("this," + "that," + ref1 + " ");
            sb.append("and whatever");
            p("testString: %s\n", sb.toString());
        }
    }
    public void testClass1() {
        p("testClass\n");
        p("contains hello:          %s\n", setContains(String.class, "hello"));
        p("contains bad:            %s\n", setContains(String.class, "bad"));
        p("contains 100:            %s\n", setContains(Long.class, 100L));
        p("contains 111:            %s\n", setContains(Long.class, 111L));
        p("contains 1:              %s\n", setContains(Integer.class, 1));
        p("contains 30:             %s\n", setContains(Integer.class, 30));
        p("contains helloObject:    %s\n", setContains(Object.class, "helloObject"));
        p("contains badObject:      %s\n", setContains(Object.class, "badObject"));
        p("contains Boolean:        %s\n", setContains(Boolean.class, "badObject"));
    }

    <T> boolean setContains(Class<T> classType, Object k) {
        Set<T> set = getSet(classType);
        if(set == null) return false;
        return set.contains(classType.cast(k));
    }

    <T> Set<T> getSet(Class<T> classType) {
        return (Set<T>) mapAllTypes.get(classType);
    }

    public void testRegex1() {
        List<String> list = Arrays.asList(
            "http://www.abc.com",
            "ahttp://www.abc.com",
            "?http://www.abc.com",
            "http:///www.abc.com",
            "http://www.abc.com:80",
            "http://www.abc.com:80/",
            "http://wow2.com",
            "http://oww2.com/a/b",
            "http://abc.com",
            "http://ww2.com",
            "http://ww2.com/a/b",
            "http://wwwww23.com/a",
            "http://www.ww2.com",
            "http://www.ww2.com/a/b",
            "http://wwwww.wwwww23.com/a",
            "http://abc.com:80",
            "http://abc.com:80/a",
            "//www.abcd.com",
            "///www.abcd.com",
            "www.abcd.com:80",
            "www.abcd.com:80/",
            "(http://www.abc.com/a)",
            "http://www.abc.com/a",
            "http://www.abc.com/a.bc",
            "http://www.abc.com/a/b/index.html",
            "http://www.abc.com/a/b?x1=0&x2=1;x3=3&x4=4",
            "http:http//www.abc.com",
            "http://www.abc.com/http://www.def.com",
            "http://www.abc.com/http://www.def.com",
            "http://www.abc.com?http://www.def.com",
            "http://www.abc.com?x1=0&x2=1;x3=3&x4=4/http://www.def.com/a/b?y1=0&y2=1;x3=3&y4=4",
            "https://www.abc.com",
            "ahttps://www.abc.com",
            "https://www.abc.com:80",
            "http:s//www.abc.com:80/",
            "httpss://www.abc.com",
            "ahttpss://www.abc.com",
            "httpss://www.abc.com:80",
            "https://www.abc.com:80/"
        );
        Pattern pat1 = Pattern.compile("^(?:https?://)?w+\\d*\\.");

        p("pat1\n\n");
        list.stream().forEach(x -> {
            String s = pat1.matcher(x).replaceFirst("");
            p("orig:%-90s res:%s\n", x, s);
        });
        p("\n\npat2\n\n");
        Pattern pat2 = Pattern.compile("^(?:https?://)?\\w+\\d*\\.");
        list.stream().forEach(x -> {
            String s = pat2.matcher(x).replaceFirst("");
            p("orig:%-90s res:%s\n", x, s);
        });
        p("\n\npat3\n\n");
        Pattern pat3 = Pattern.compile("^(?:https?://)");
        list.stream().forEach(x -> {
            String s = pat3.matcher(x).replaceFirst("");
            p("orig:%-90s res:%s\n", x, s);
        });
    }

    public void testEnum() {
        EnumT e1 = EnumT.PLANE;
        EnumT e2 = EnumT.BOAT;
        EnumT e3 = EnumT.TRAIN;

        p("%s\n", e1.m1(EnumT.BOAT));
        p("%s\n", e1.m1Static(e1));
        p("%s\n", EnumT.m1Static(e1));
        p("%s\n", EnumT.m1Static(e2));
        p("%s\n", EnumT.m1Static(e3));
        p("%s\n", EnumT.m1StaticBasic(e1));
        p("%s\n", EnumT.m1StaticBasic(e2));
        p("size:%d\n", EnumT.values().length);
        EnumT [] values = EnumT.values();
        for(int i = 0; i < values.length; i++) {
            p("i:%2d val:%s\n", i, values[i]);
        }
    }

    public void testToString() {
        Long l1 = null;
        Long l2 = 1234L;
        Long l3 = 34L;
        p("testToString: l1:%s l2:%s l3:%s\n", stringValue(l1), stringValue(l2), stringValue(l3));
    }

    public void testlong2int() {
        long l = 1528762431802L;
        int i = (int)l;
        p("long:%d int:%d\n", l, i);
        Long lo = null;
        Integer io = null;
        l = lo;
        i = io;
        p("long:%d int:%d\n", l, i);
    }

    public void testDistVal() throws Exception {
        // test List<Integer> getInt(int min, int max, int [][] distribution, int numElements)
        {
            int [][] dist = {{10,12,10},{13,14,20},{15,17,20},{18,20,50}};
            List<Integer> l = utils.getInt(10,20,dist,1000);
            Map<Integer,Integer> map = new TreeMap<>(); // sorted map
            for(Integer i: l) {
                Integer cnt = map.get(i);
                if(cnt == null) {
                    cnt = 1;
                } else {
                    cnt += 1;
                }
                map.put(i, cnt);
            }
            for(Map.Entry<Integer,Integer> kv: map.entrySet()) {
                p("%d = %d\n", kv.getKey(), kv.getValue());
            }
        }
        {

        }
    }

    public void testArray() {
        String[] as1 = new String[]{"v1","v2"};
        String[] as2 = {"v1","v2"};
        String[] as3 = new String[2];
        as3[0] = "v1"; as3[1] = "v2";

        assert "v1".equals(as1[0]);
        assert "v1".equals(as2[0]);
        assert "v1".equals(as3[0]);
        assert "v2".equals(as1[0]);
        assert "v2".equals(as2[0]);
        assert "v2".equals(as3[0]);

        p("passed testArray\n");
    }


    public String stringValue(Long l) {
        return l == null ? null : l.toString();
    }

    public void testListAlloc() {
        List<String> l = new ArrayList<>(0);
        assert l.size() == 0;
        l.add("hello1");
        l.add("hello2");
        l.add("hello3");
        assert l.size() == 3;
        p("testListAlloc passed\n");
    }

    public void testCurPath() {
        p("testCurPath:\n");
        p("%s\n", System.getProperty("user.dir"));
        p("%s\n", Paths.get("").toAbsolutePath().toString());
        p("%s\n", Paths.get(".").toAbsolutePath().toString());
        p("%s\n", Paths.get(".").toAbsolutePath().normalize().toString());
        p("%s\n", Paths.get(".").toString());
    }

    public void testNonNull() {
        try {
            nonNullInput("hello");
            nonNullInput(null);
            nonNullInput("bye");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    //private void nonNullInput(@Nonnull String v) {
    private void nonNullInput(String v) {
        if(v == null) {
            p("not supposed to be here\n");
        } else {
            p("val is %s\n", v);
        }
        //assert v.length() >= 0;
    }
    public void testNullObjectPattern() {
        List<ISyntax1> l = new ArrayList<>();
        l.add(new CSyntax1());
        l.add(null);
        for(ISyntax1 c: l) {
            c.calling();
        }
    }

    public void testNullOrEmptyPatterns() {
        List<String> ls = null;
        boolean expected = false;
        try {
            for(String s: ls) {
                p("%s\n", s);
            }
        } catch (Exception e) { expected = true; }
        assert expected;

        expected = true;
        ls = Arrays.asList();
        try {
            for(String s: ls) {
                p("%s\n", s);
            }
        } catch (Exception e) { expected = false; }
        assert expected;
        expected = false;

        ls = Arrays.asList("string1");
        expected = true;
        try {
            for(String s: ls) {
                p("%s\n", s);
            }
        } catch (Exception e) { expected = false; }
        assert expected;
        expected = false;

        p("testNullOrEmptyPatterns passed\n");
    }

    public void testCollections() {
        boolean flag = false;
        {
            List<String> list1 = new ArrayList<>();
            List<String> list2 = null;
            List<String> list3 = Arrays.asList("hello3", "bird3");
            List<String> list4 = new ArrayList<>(Arrays.asList("hello4","bird4","food4"));
            List<String> list = new ArrayList<>();
            list.addAll(list1);
            try {
                list.addAll(list2);
            } catch(Exception e) {
                flag = true;
            }
            list.addAll(list3);
            list.addAll(list4);
            assert flag;
            assert list.size() == 5;
        }

        {
            List<Integer> list0 = new ArrayList<>();
            List<Integer> list1 = Arrays.asList(1,2);
            List<Integer> list2 = Arrays.asList(1,2,3,4);
            List<Integer> list3 = Arrays.asList(1,2);
            List<Integer> list4 = Arrays.asList(1,2,3);
            List<Integer> list5 = Arrays.asList(1,2,3,4);
            List<Integer> list6 = Arrays.asList(1,2,3,4,5);

            Set<Integer> set = new HashSet<>();
            set.addAll(list0);
            set.addAll(list1);
            set.addAll(list2);
            assert set.size() == 4;
            assert set.containsAll(list3) == true;
            assert set.containsAll(list5) == true;
            assert set.containsAll(list6) == false;

            try {
                set.addAll(null);
                flag = false;
            } catch(Exception e) {
                flag = true;
            }
            assert flag;
        }

        p("MySyntax.testCollections passed\n");
    }

    public void test() {
        //p("hello world\n");
        testCollections();
    }

    public static void main(String [] args) {
        p("MySyntax main\n");
        // both of these work
        /*
        {
            (new MySyntax()).test();
            new MySyntax().test();
        }
        */
        new MySyntax().test();
    }

}

enum EnumT {
    PLANE,
    TRAIN,
    AUTO,
    BOAT,
    OTHER;

    public String m1(EnumT e) {
        switch(e) {
            case PLANE: return "this is PLANE";
            case TRAIN: return "this is TRAIN";
            case AUTO: return "this is AUTO";
            case BOAT: return "this is BOAT";
            case OTHER: return "this is OTHER";
            default: return "this is UNKNOWN";
        }
    }
    public String m2(EnumT e1, EnumT e2) {
        switch(e1) {
            case PLANE: return "this is PLANE";
            case TRAIN: return "this is TRAIN";
            case AUTO: return "this is AUTO";
            case BOAT: return "this is BOAT";
            case OTHER: return "this is OTHER";
            default: return "this is UNKNOWN";
        }
    }
    public static String m1Static(EnumT e) {
        switch(e) {
            case PLANE: return "this is PLANE";
            case TRAIN: return "this is TRAIN";
            case AUTO: return "this is AUTO";
            case BOAT: return "this is BOAT";
            case OTHER: return "this is OTHER";
            default: return "this is UNKNOWN";
        }
    }
    public static String m1StaticBasic(EnumT e) {
        switch(e) {
            case PLANE:
            case TRAIN:
                return "this is PLANE or TRAIN";
        }
        return m1Static(e);
    }
}
interface ISyntax1 {
    void calling();
}
class CSyntax1 implements ISyntax1 {
    @Override
    public void calling() {
        System.out.printf("hello CSyntax1\n");
    }
}
class CSyntaxNull implements ISyntax1 {
    @Override
    public void calling() {
    }
}
class URLParser {
    String protocol = null;
    String auth = null;
    String host = null;
    int    port = 0;
    String path = null;
    String query = null;
    String file = null;
    String ref = null;
    boolean valid = true;
    public URLParser(String s) {
        parse(s);
    }
    void parse(String s) {
        int idxc = s.indexOf(':');
        int idxl = 0;
        if(idxc == -1) { valid = false; return; }
        protocol = s.substring(0, idxc);
        idxl = ++idxc;
        if((idxc+1) >= s.length() ||
            (s.charAt(idxc++) != '/' && s.charAt(idxc++) != '/')) {
            valid = false;
            return;
        }
        idxl += 2;
        while(idxc < s.length() && s.charAt(idxc) != '?') {
            idxc++;
        }
        path = s.substring(idxl,idxc);
        if((idxc+1) < s.length()) {
            query = s.substring(idxc+1,s.length());
        }
    }
    public String getProtocol() { return protocol; }
    public String getAuthority() { return auth; }
    public String getHost() { return host; }
    public int getPort() { return port; }
    public String getPath() { return path; }
    public String getQuery() { return query; }
    public String getFile() { return file; }
    public String getRef() { return ref; }
    public String toString() { return protocol + "://" + path + "?" + query; }
}
