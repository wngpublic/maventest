package org.wayne.java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This file creates multiple useless classes for the purpose of testing
 * out reflection. This file has the following:
 * 
 * interfaces
 * subclasses
 * super classes
 * methods
 * return types for methods
 * methods with various params
 * method modifiers
 * arrays
 * lists
 * Object instances in classes.
 * 
 * The purpose is to use reflection to extract info about these various class types.
 * These classes don't do anything other than to test out reflection mechanics.
 * 
 * List of interfaces, classes, etc.
 * 
 * InterfaceA
 * InterfaceB
 * InterfaceC
 * InterfaceAA
 *     InterfaceA
 * InterfaceAB
 *     InterfaceA
 *     InterfaceB
 * ClassBase0
 * ClassBase1
 * ClassBase00
 *     ClassBase0
 * ClassA
 *     InterfaceA
 * ClassA1
 *     ClassA
 * ClassAA
 *     InterfaceAA
 * ClassAC
 *     InterfaceA
 *     InterfaceC
 * ClassA0
 *     ClassBase0
 *     InterfaceA
 * ClassATypeA0
 *     ClassA
 * ClassATypeA1
 *     ClassATypeA0
 * ClassABType1
 *     InterfaceA
 *     InterfaceB
 * ClassABType2
 *     InterfaceA
 *     InterfaceB
 */
public class MiscReflection {
    List<TypeContainer> list = new ArrayList<>();
    HashMap<Integer, TypeContainer> map = new HashMap<>();
    private static int ctr = 0;

    // we want a singleton, so make constructor private.
    private MiscReflection() {
    }
    private static class Loader {
        private static final MiscReflection instance =
                new MiscReflection();
    }
    public static MiscReflection instance() {
        return Loader.instance;
    }

    public int getNextId() {
        return ctr++;
    }

    private void addObject(Integer id, Object obj) {
        TypeContainer container = new TypeContainer(obj);
        list.add(container);
        map.put(id, container);
    }

    public void addObj(ClassBase0 obj) {
        addObject(obj.getId(), obj);
    }

    public void addObj(ClassBase1 obj) {
        addObject(obj.getId(), obj);
    }
    
    public void addObj(ClassBase00 obj) {
        addObject(obj.getId(), obj);
    }
    
    public void addObj(ClassA obj) {
        addObject(obj.getId(), obj);
    }
    
    public void addObj(ClassA1 obj) {
        addObject(obj.getId(), obj);
    }

    public void addObj(ClassAA obj) {
        addObject(obj.getId(), obj);
    }
    
    public void addObj(ClassAC obj) {
        addObject(obj.getId(), obj);
    }
    
    public void addObj(ClassA0 obj) {
        addObject(obj.getId(), obj);
    }
    
    public void addObj(ClassATypeA0 obj) {
        addObject(obj.getId(), obj);
    }
    
    public void addObj(ClassATypeA1 obj) {
        addObject(obj.getId(), obj);
    }
    
    public void addObj(ClassABType1 obj) {
        addObject(obj.getId(), obj);
    }
    
    public void addObj(ClassABType2 obj) {
        addObject(obj.getId(), obj);
    }
 }

enum TYPE {
    Default,
    ClassBase0,
    ClassBase1,
    ClassBase00,
    ClassA,
    ClassA1,
    ClassAA,
    ClassAC,
    ClassA0,
    ClassATypeA0,
    ClassATypeA1,
    ClassABType1,
    ClassABType2
}

class TypeContainer {
    TYPE type = TYPE.Default;
    Object obj = null;
    
    public TypeContainer(TYPE type, Object obj) {
        this.type = type;
        this.obj = obj;
    }

    public TypeContainer(Object obj) {
        if     (obj instanceof ClassBase0) {
            type = TYPE.ClassBase0;
        }
        else if(obj instanceof ClassBase1) {
            type = TYPE.ClassBase1;
        }
        else if(obj instanceof ClassBase00) {
            type = TYPE.ClassBase00;
        }
        else if(obj instanceof ClassA) {
            type = TYPE.ClassA;
        }
        else if(obj instanceof ClassA1) {
            type = TYPE.ClassA1;
        }
        else if(obj instanceof ClassAA) {
            type = TYPE.ClassAA;
        }
        else if(obj instanceof ClassAC) {
            type = TYPE.ClassAC;
        }
        else if(obj instanceof ClassA0) {
            type = TYPE.ClassA0;
        }
        else if(obj instanceof ClassATypeA0) {
            type = TYPE.ClassATypeA0;
        }
        else if(obj instanceof ClassATypeA1) {
            type = TYPE.ClassATypeA1;
        }
        else if(obj instanceof ClassABType1) {
            type = TYPE.ClassABType1;
        }
        else if(obj instanceof ClassABType1) {
            type = TYPE.ClassABType1;
        }
        else {
            type = TYPE.Default;
        }
        this.obj = obj;
    }

    public TYPE getType() {
        return type;
    }
    
    public Object getObject() {
        return obj;
    }
}

interface InterfaceA {
    String getStringIfcA();
    InterfaceA setStringIfcA(String s);
    String getValueIfcA(String key);
    void setKVIfcA(String key, String value);
    HashMap<String,String> getMapIfcA();
    void appendListIfcA(List<String> l);
    List<String> getListRangeIfcA(int idxbegin, int idxend);
    public Integer getId();
}

interface InterfaceB {
    String getStringIfcB();
    InterfaceB setStringIfcB(String s);
    String getValueIfcB(String key);
    void setKVIfcB(String key, String value);
    HashMap<String,String> getMapIfcB();
    void appendListIfcB(List<String> l);
    List<String> getListRangeIfcB(int idxbegin, int idxend);
    public Integer getId();
}

interface InterfaceC {
    String getStringIfcC();
    InterfaceC setStringIfcC(String s);
    String getValueIfcC(String key);
    void setKVIfcC(String key, String value);
    HashMap<String,String> getMapIfcC();
    void appendListIfcC(List<String> l);
    List<String> getListRangeIfcC(int idxbegin, int idxend);
    public Integer getId();
}

interface InterfaceAA extends InterfaceA {
    HashMap<Integer,Integer> getMapIfcAA();
    void setKVIfcAA(Integer key, Integer val);
    Integer getValIfcAA(Integer key);
}

interface InterfaceAB extends InterfaceA, InterfaceB {
    HashMap<Integer,Integer> getMapIfcAB();
    void setKVIfcAB(Integer key, Integer val);
    Integer getValIfcAB(Integer key);
}

class ClassBase0 {
    protected final Integer id;
    HashMap<Integer, String> mapis = new HashMap<>();
    HashMap<String, Integer> mapsi = new HashMap<>();
    
    public ClassBase0(final Integer id) {
        this.id = id;
    }
    
    public Integer getId() {
        return id;
    }

    public void printAll() {
        printMapIS();
        printMapSI();
    }
    
    public void printMapIS() {
        System.out.printf("ClassBase0 id:%2d printMapIS\n", id);
        for(Map.Entry<Integer, String> kv: mapis.entrySet()) {
            System.out.printf("%8d,%8s\n", kv.getKey(), kv.getValue());
        }
    }
    
    public void printMapSI() {
        System.out.printf("ClassBase0 id:%2d printMapSI\n", id);
        for(Map.Entry<String, Integer> kv: mapsi.entrySet()) {
            System.out.printf("%8s,%8d\n", kv.getKey(), kv.getValue());
        }
    }
    
    public void addKVBase0(Integer k, String v) {
        mapis.put(k, v);
    }
    
    public void addKVBase0(String k, Integer v) {
        mapsi.put(k, v);
    }
    
    public String getValue(Integer k) {
        return mapis.get(k);
    }
    
    public Integer getValue(String k) {
        return mapsi.get(k);
    }
}

class ClassBase1 {
    protected final Integer id;
    HashMap<Integer, String> mapis = new HashMap<>();
    HashMap<String, Integer> mapsi = new HashMap<>();
    
    public ClassBase1(final Integer id) {
        this.id = id;
    }
    
    public Integer getId() {
        return id;
    }

    public void printAll() {
        printMapIS();
        printMapSI();
    }
    
    public void printMapIS() {
        System.out.printf("ClassBase1 id:%2d printMapIS\n", id);
        for(Map.Entry<Integer, String> kv: mapis.entrySet()) {
            System.out.printf("%8d,%8s\n", kv.getKey(), kv.getValue());
        }
    }
    
    public void printMapSI() {
        System.out.printf("ClassBase1 id:%2d printMapSI\n", id);
        for(Map.Entry<String, Integer> kv: mapsi.entrySet()) {
            System.out.printf("%8s,%8d\n", kv.getKey(), kv.getValue());
        }
    }
    
    public void addKVBase0(Integer k, String v) {
        mapis.put(k, v);
    }
    
    public void addKVBase0(String k, Integer v) {
        mapsi.put(k, v);
    }
    
    public String getValue(Integer k) {
        return mapis.get(k);
    }
    
    public Integer getValue(String k) {
        return mapsi.get(k);
    }
}

class ClassBase00 extends ClassBase0 {
    public ClassBase00(final Integer id) {
        super(id);
    }
    
    public void printId() {
        System.out.printf("id:%2d\n", this.id);
    }
    
    @Override
    public void printMapIS() {
        System.out.printf("ClassBase00 id:%2d printMapIS\n", id);
        for(Map.Entry<Integer, String> kv: mapis.entrySet()) {
            System.out.printf("%8d,%8s\n", kv.getKey(), kv.getValue());
        }
    }
    
    @Override
    public void printMapSI() {
        System.out.printf("ClassBase00 id:%2d printMapSI\n", id);
        for(Map.Entry<String, Integer> kv: mapsi.entrySet()) {
            System.out.printf("%8s,%8d\n", kv.getKey(), kv.getValue());
        }
    }
}

class ClassA implements InterfaceA {
    protected final Integer id;

    @Override
    public Integer getId() {
        return id;
    }

    public ClassA(final Integer id) {
        this.id = id;
    }

    @Override
    public String getStringIfcA() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public InterfaceA setStringIfcA(String s) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getValueIfcA(String key) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setKVIfcA(String key, String value) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public HashMap<String, String> getMapIfcA() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void appendListIfcA(List<String> l) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public List<String> getListRangeIfcA(int idxbegin, int idxend) {
        // TODO Auto-generated method stub
        return null;
    }
    
}

class ClassA1 extends ClassA {
    public ClassA1(final Integer id) {
        super(id);
    }
}

class ClassAA implements InterfaceAA {
    protected final Integer id;

    @Override
    public Integer getId() {
        return id;
    }
    
    public ClassAA(final Integer id) {
        this.id = id;
    }

    @Override
    public String getStringIfcA() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public InterfaceA setStringIfcA(String s) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getValueIfcA(String key) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setKVIfcA(String key, String value) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public HashMap<String, String> getMapIfcA() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void appendListIfcA(List<String> l) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public List<String> getListRangeIfcA(int idxbegin, int idxend) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HashMap<Integer, Integer> getMapIfcAA() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setKVIfcAA(Integer key, Integer val) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Integer getValIfcAA(Integer key) {
        // TODO Auto-generated method stub
        return null;
    }
    
}

class ClassAC implements InterfaceA, InterfaceC {
    protected final Integer id;

    @Override
    public Integer getId() {
        return id;
    }
    
    public ClassAC(final Integer id) {
        this.id = id;
    }

    @Override
    public String getStringIfcC() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public InterfaceC setStringIfcC(String s) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getValueIfcC(String key) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setKVIfcC(String key, String value) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public HashMap<String, String> getMapIfcC() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void appendListIfcC(List<String> l) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public List<String> getListRangeIfcC(int idxbegin, int idxend) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getStringIfcA() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public InterfaceA setStringIfcA(String s) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getValueIfcA(String key) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setKVIfcA(String key, String value) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public HashMap<String, String> getMapIfcA() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void appendListIfcA(List<String> l) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public List<String> getListRangeIfcA(int idxbegin, int idxend) {
        // TODO Auto-generated method stub
        return null;
    }
    
}

class ClassA0 extends ClassBase0 implements InterfaceA {

    public ClassA0(final Integer id) {
        super(id);
    }

    @Override
    public String getStringIfcA() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public InterfaceA setStringIfcA(String s) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getValueIfcA(String key) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setKVIfcA(String key, String value) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public HashMap<String, String> getMapIfcA() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void appendListIfcA(List<String> l) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public List<String> getListRangeIfcA(int idxbegin, int idxend) {
        // TODO Auto-generated method stub
        return null;
    }
    
}

class ClassATypeA0 extends ClassA {
    public ClassATypeA0(final Integer id) {
        super(id);
    }
}

class ClassATypeA1 extends ClassATypeA0 {
    public ClassATypeA1(final Integer id) {
        super(id);
    }
}

class ClassABType1 implements InterfaceA, InterfaceB {
    protected final Integer id;

    @Override
    public Integer getId() {
        return id;
    }
    
    public ClassABType1(final Integer id) {
        this.id = id;
    }

    @Override
    public String getStringIfcB() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public InterfaceB setStringIfcB(String s) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getValueIfcB(String key) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setKVIfcB(String key, String value) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public HashMap<String, String> getMapIfcB() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void appendListIfcB(List<String> l) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public List<String> getListRangeIfcB(int idxbegin, int idxend) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getStringIfcA() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public InterfaceA setStringIfcA(String s) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getValueIfcA(String key) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setKVIfcA(String key, String value) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public HashMap<String, String> getMapIfcA() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void appendListIfcA(List<String> l) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public List<String> getListRangeIfcA(int idxbegin, int idxend) {
        // TODO Auto-generated method stub
        return null;
    }
    
}

class ClassABType2 implements InterfaceA, InterfaceB {
    protected final Integer id;

    @Override
    public Integer getId() {
        return id;
    }
    
    public ClassABType2(final Integer id) {
        this.id = id;
    }

    @Override
    public String getStringIfcB() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public InterfaceB setStringIfcB(String s) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getValueIfcB(String key) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setKVIfcB(String key, String value) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public HashMap<String, String> getMapIfcB() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void appendListIfcB(List<String> l) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public List<String> getListRangeIfcB(int idxbegin, int idxend) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getStringIfcA() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public InterfaceA setStringIfcA(String s) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getValueIfcA(String key) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setKVIfcA(String key, String value) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public HashMap<String, String> getMapIfcA() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void appendListIfcA(List<String> l) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public List<String> getListRangeIfcA(int idxbegin, int idxend) {
        // TODO Auto-generated method stub
        return null;
    }   
}

