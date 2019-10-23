package org.wayne.java;

import org.wayne.misc.Utils;

import org.wayne.java.MyLambda;

import javax.xml.datatype.DatatypeConfigurationException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Objects {
    static Utils utils = new Utils();
    static Random rand = new Random();
    static AtomicLong aLong = new AtomicLong(0);
    public static List<ObjectGenericNested> createListObjectGenericNested(int size) {
        List<ObjectGenericNested> l = new ArrayList<>();
        for(int i = 0; i < size; i++) {
            l.add(new ObjectGenericNested(aLong.getAndIncrement(), i+100, rand.nextBoolean(), utils.getRandString(10)));
        }
        for(int i = 0; i < size; i++) {
            l.get(i).obj(l.get((i+1)%size));
        }
        return l;
    }
    public static List<ObjectGeneric> createListObjectGeneric(int size) {
        List<ObjectGeneric> l = new ArrayList<>();
        for(int i = 0; i < size; i++) {
            l.add(new ObjectGeneric(aLong.getAndIncrement(), i+100, rand.nextBoolean(), utils.getRandString(10)));
        }
        return l;
    }
    public static List<ObjectComplex> createListObjectComplex(int numObj, int sizeObj, int maxChildren, int ttl) {
        List<ObjectComplex> l = new ArrayList<>();
        for(int i = 0; i < numObj; i++) {
            int numChildren = utils.getInt(0,maxChildren);
            List<ObjectGeneric> listChildren = createListObjectGeneric(numChildren);
            ObjectComplex objectComplex = new ObjectComplex(
                aLong.getAndIncrement(), i+100, rand.nextBoolean(), utils.getRandString(10));
            objectComplex.setTTL(ttl);
            List<String> listString = new ArrayList<>();
            IntStream.range(0, sizeObj).forEach((u) -> listString.add(utils.getRandString(100)));
            objectComplex.setListString(listString);
            l.add(objectComplex);
        }
        return l;
    }
}


class ObjectGeneric {
    long id;
    int vi;
    boolean is1;
    String name;
    public ObjectGeneric(long id, int vi, boolean is1) {
        this(id,vi,is1,"default");
    }
    public ObjectGeneric(long id, int vi, boolean is1, String name) {
        this.id = id;
        this.vi = vi;
        this.is1 = is1;
        this.name = name;
    }
    public Long id() { return id; }
    public void id(int id) { this.id = id; }
    public int vi() { return vi; }
    public void vi(int vi) { this.vi = vi; }
    public boolean is1() { return is1; }
    public void is1(boolean is1) { this.is1 = is1; }
    public void name(String name) { this.name = name; }
    public String name() { return name; }
    public void p() { MyLambda.p("%2d %2d %s %s\n", id, vi, is1, name == null ? "" : name); }
}
class ObjectGenericNested extends ObjectGeneric {
    ObjectGenericNested obj = null;
    public ObjectGenericNested(long id, int vi, boolean is1) {
        this(id,vi,is1,"default");
    }
    public ObjectGenericNested(long id, int vi, boolean is1, String name) {
        super(id,vi,is1,name);
    }
    public ObjectGenericNested(long id, int vi, boolean is1, ObjectGenericNested obj) {
        this(id,vi,is1);
        this.obj = obj;
    }
    public ObjectGenericNested obj() { return obj; }
    public void obj(ObjectGenericNested obj) { this.obj = obj; }
    public void p() {
        MyLambda.p("%2d %2d %5s %12s nest:%d\n",
                id,vi,is1,name == null ? "" : name,(obj == null) ? -1: obj.id());
    }
}
class ObjectComplex extends ObjectGeneric {
    public ObjectComplex(long id, int vi, boolean is1, String name) {
        super(id,vi,is1,name);
    }
    byte [] byteArray;
    List<String> listString;
    List<ObjectGeneric> listObject;
    AtomicInteger ttl = new AtomicInteger(0);
    public AtomicInteger getTTL() { return ttl; }
    public void setTTL(int ttl) { this.ttl.set(ttl); }
    public byte [] getByteArray() { return byteArray; }
    public void setByteArray(byte [] byteArray) { this.byteArray = byteArray; }
    public List<String> getListString() { return listString; }
    public void setListString(List<String> listString) { this.listString = listString; }
    public List<ObjectGeneric> getListObjectGeneric() { return listObject; }
    public void setListObjectGeneric(List<ObjectGeneric> listObject) { this.listObject = listObject; }
}

class StringObjectMap extends ObjectGeneric {
    Map<String,String> map = null;
    public StringObjectMap(long id, int vi, boolean is1, String name, Map<String,String> map) {
        super(id,vi,is1,name);
        this.map = map;
    }
}

class StringObject extends ObjectGeneric {
    List<String> list = null;
    public StringObject(long id, int vi, boolean is1, String name, List<String> list) {
        super(id,vi,is1,name);
        this.list = list;
    }
}

class MyRequest {
    private static AtomicLong ctr = new AtomicLong(0);
    long id;
    Class clazz;
    Object obj;
    public MyRequest(Class clazz, Object obj) {
        this.id = ctr.getAndIncrement();
        this.clazz = clazz;
        this.obj = obj;
    }
    public long getId() { return id; }
    public Class getClazz() { return clazz; }
    public Object getObj() { return obj; }
}

class MyResponse {
    private static AtomicLong ctr = new AtomicLong(0);
    long reqId;
    long rspId;
    Class clazz;
    Object obj;
    public MyResponse(long reqId, Class clazz, Object obj) {
        this.rspId = ctr.getAndIncrement();
        this.reqId = reqId;
        this.clazz = clazz;
        this.obj = obj;
    }
    public long getReqId() { return reqId; }
    public long getRspId() { return rspId; }
    public Class getClazz() { return clazz; }
    public Object getObj() { return obj; }
}

class MyContainer<T> {
    private static AtomicInteger ctr = new AtomicInteger(0);
    T object;
    int id;
    String msg;

    public MyContainer(T object) throws DatatypeConfigurationException {
        this(object, "default");
    }

    public MyContainer(T object, String msg) throws DatatypeConfigurationException {
        if (object == null)
            throw new DatatypeConfigurationException("clazz or object exception");
        this.id = ctr.getAndIncrement();
        this.object = object;
    }
    public T get() {
        return object;
    }

    public void msg(String msg) {
        this.msg = msg;
    }

    public String msg() {
        return "ID:" + id + " " + msg;
    }
}

interface Callback<T> {
    T get();
    void setMsg(String msg);
    String getMsg();
    void complete();
}

interface SerializableObject {
    Class getClazz();
    void setClazz(Class clazz);
    void setObject(Serializable serializable);
    byte [] getBytes();
    <T> T getObject();
    void setBytes(byte [] bytes);
}