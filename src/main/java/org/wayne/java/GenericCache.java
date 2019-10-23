package org.wayne.java;

import java.util.concurrent.ConcurrentHashMap;

public class GenericCache {
    final class CacheObject {
        private String key;
        private String classname;
        private Object obj;
        public CacheObject(String key, Object obj, String classname) {
            this.key = key;
            this.classname = classname;
            this.obj = obj;
        }
        public String key() {
            return key;
        }
        public String classname() {
            return classname;
        }
        public Object object() {
            return obj;
        }
    }

    private ConcurrentHashMap<String, CacheObject> map = new ConcurrentHashMap<>();

    public <T> boolean setAttribute(String name, Object o, Class<T> clazz) {
        if(!clazz.isInstance(o)) {
            return false;
        }
        String classname = clazz.getName();
        CacheObject obj = new CacheObject(name, o, classname);
        map.put(name, obj);
        return true;
    }
    public boolean setAttribute(String name, Object o, String classname) {
        @SuppressWarnings("rawtypes")
        final Class clazz;
        try {
            clazz = Class.forName(classname);
            if(!clazz.isInstance(o)) {
                return false;
            }
            CacheObject obj = new CacheObject(name, o, classname);
            map.put(name, obj);
        } catch(ClassNotFoundException e) {
            return false;
        }
        return true;
    }
    public <T> T getAttribute(String name, Class<T> clazz) {
        CacheObject cacheObject = map.get(name);
        if(cacheObject == null) {
            return null;
        }
        String classname = clazz.getName();
        Object obj = cacheObject.object();
        if(!classname.equals(cacheObject.classname()) || clazz.isInstance(obj)) {
            return null;
        }
        return clazz.cast(obj);
    }
    public Object getAttribute(String name, String classname) {
        CacheObject cacheObject = map.get(name);
        if(cacheObject == null) {
            return null;
        }
        @SuppressWarnings("rawtypes")
        final Class clazz;
        try {
            clazz = Class.forName(classname);
            Object obj = cacheObject.object();
            if(!classname.equals(cacheObject.classname()) || clazz.isInstance(obj)) {
                return null;
            }
            return clazz.cast(obj);
        } catch(ClassNotFoundException e) {
            return null;
        }
    }
    public boolean removeAttribute(String name) {
        if(!map.containsKey(name)) {
            return false;
        }
        map.remove(name);
        return true;
    }
}
