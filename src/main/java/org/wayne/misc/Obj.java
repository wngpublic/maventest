package org.wayne.misc;

public class Obj {
    public static class ObjInteger {
        private Integer v;
        public ObjInteger() {
            
        }
        public ObjInteger(Integer v) {
            this.v = v;
        }
        public Integer v() {
            return v;
        }
        public void v(Integer v) {
            this.v = v;
        }
    }
    public static class ObjDouble {
        private Double v;
        public ObjDouble() {
            
        }
        public ObjDouble(Double v) {
            this.v = v;
        }
        public Double v() {
            return v;
        }
        public void v(Double v) {
            this.v = v;
        }
    }
    public static class ObjString {
        private String v;
        public ObjString() {
            
        }
        public ObjString(String v) {
            this.v = v;
        }
        public String v() {
            return v;
        }
        public void v(String v) {
            this.v = v;
        }
    }
    public static final int INT = 1;
    public static final int DOUBLE = 2;
    public static final int STRING = 3;
    public final int type;
    private Object obj = null;
    public Obj(int type) throws Exception {
        if(type != INT && type != DOUBLE && type != STRING) {
            String msg = String.format("Obj type is invalid: %d. Must be INT, DOUBLE, STRING", type);
            throw new Exception(msg);
        }
        this.type = type;
    }
    public Obj v(String v) throws Exception {
        if(type != STRING) {
            throw new Exception(String.format("Object initialized type %d is not String type", type));
        }
        obj = v;
        return this;
    }
    public Obj v(Integer v) throws Exception {
        if(type != INT) {
            throw new Exception(String.format("Object initialized type %d is not Integer type", type));
        }
        obj = v;
        return this;
    }
    public Obj v(Double v) throws Exception {
        if(type != DOUBLE) {
            throw new Exception(String.format("Object initialized type %d is not Double type", type));
        }
        obj = v;
        return this;
    }
    public String vString() throws Exception {
        if(type != STRING) {
            throw new Exception(String.format("Object initialized type %d is not String type", type));
        }
        return (String)obj;
    }
    public Integer vInt() throws Exception {
        if(type != INT) {
            throw new Exception(String.format("Object initialized type %d is not Integer type", type));
        }
        return (Integer)obj;
    }
    public Double vDouble() throws Exception {
        if(type != DOUBLE) {
            throw new Exception(String.format("Object initialized type %d is not Double type", type));
        }
        return (Double)obj;
    }
}
