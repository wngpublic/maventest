package org.wayne.java.package3;

public class ObjectMultiInterface implements Interface0, Interface1 {
    @Override
    public String hello(String s) {
        return String.format("Hello %s", s);
    }

    @Override
    public String helloInterface1(String s) {
        return String.format("helloInterface1 %s", s);
    }

    @Override
    public String helloInterface0(String s) {
        return String.format("helloInterface1 %s", s);
    }
}
