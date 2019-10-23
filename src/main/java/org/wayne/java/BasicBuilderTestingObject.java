package org.wayne.java;

import java.util.function.Consumer;

public class BasicBuilderTestingObject {
    int a;
    int b;
    int c;
    int d;
    int e;
    private BasicBuilderTestingObject(
        int a, int b, int c, int d, int e
    ) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
    }
    public int a() { return a; }
    public int b() { return b; }
    public int c() { return c; }
    public int d() { return d; }
    public int e() { return e; }

    static class BasicBuilderTestingObjectOldBuilder {
        int a;
        int b;
        int c;
        int d;
        int e;
        public BasicBuilderTestingObjectOldBuilder a(int a) { this.a = a; return this; }
        public BasicBuilderTestingObjectOldBuilder b(int b) { this.b = b; return this; }
        public BasicBuilderTestingObjectOldBuilder c(int c) { this.c = c; return this; }
        public BasicBuilderTestingObjectOldBuilder d(int d) { this.d = d; return this; }
        public BasicBuilderTestingObjectOldBuilder e(int e) { this.e = e; return this; }
        public BasicBuilderTestingObject build() {
            return new BasicBuilderTestingObject(a,b,c,d,e);
        }
    }
    // if no static, then get "is not enclosing class" error, and
    // have to do new BasicBuilderTestingObject().new BasicBuilderTestingObjectNewBuilder()
    static class BasicBuilderTestingObjectNewBuilder {
        int a; // using with, a cannot be private
        int b;
        int c;
        int d;
        int e;
        public BasicBuilderTestingObjectNewBuilder with(Consumer<BasicBuilderTestingObjectNewBuilder> consumer) {
            consumer.accept(this);
            return this;
        }
        public BasicBuilderTestingObject build() {
            return new BasicBuilderTestingObject(a,b,c,d,e);
        }
    }
}
