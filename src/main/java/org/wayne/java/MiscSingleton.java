package org.wayne.java;

/**
 * There are a few ways to do singleton pattern.
 * Look at public literature for these patterns, where
 * these are copied from.
 * 
 *
 */
public class MiscSingleton {

}

/**
 * Non thread safe SingletonClassic.
 */
class SingletonClassic {
    private static SingletonClassic instance = null;
    private SingletonClassic() {
        
    }
    public static SingletonClassic instance() {
        
        if(instance == null) {
            instance = new SingletonClassic();
        }
        return instance;
    }
    public void method() {
        
    }
}

/**
 * This is not thread safe. Adding volatile for post Java 1.5 
 * can make this thread safe.
 */
class SingletonClassicNotThreadSafe {
    private static SingletonClassicNotThreadSafe instance = null;
    private SingletonClassicNotThreadSafe() {
        
    }
    public static SingletonClassicNotThreadSafe instance() {
        if(instance == null) {
            synchronized(SingletonClassicNotThreadSafe.class) {
                if(instance == null) {
                    instance = new SingletonClassicNotThreadSafe();
                }
            }
        }
        return instance;
    }
    public void method() {
        
    }
}

/**
 * This is thread safe, same as SingletonClassicNotThreadSafe, but
 * this uses volatile instance.
 */
class SingletonClassicVolatileThreadSafe {
    private static volatile SingletonClassicVolatileThreadSafe instance = null;
    private SingletonClassicVolatileThreadSafe() {
        
    }
    public static SingletonClassicVolatileThreadSafe instance() {
        if(instance == null) {
            synchronized(SingletonClassicNotThreadSafe.class) {
                if(instance == null) {
                    instance = new SingletonClassicVolatileThreadSafe();
                }
            }
        }
        return instance;
    }
    public void method() {
        
    }
}

class SingletonClassicFinalThreadSafe {
    private static final SingletonClassicFinalThreadSafe instance = new SingletonClassicFinalThreadSafe();
    private SingletonClassicFinalThreadSafe() {
        
    }
    public static SingletonClassicFinalThreadSafe instance() {
        return instance;
    }
    public void method() {
        
    }
}

class SingletonEagerThreadSafe {
    private static SingletonEagerThreadSafe instance = new SingletonEagerThreadSafe();
    private SingletonEagerThreadSafe() {
        
    }
    public static SingletonEagerThreadSafe instance() {
        return instance;
    }
    public void method() {
        
    }
}

class SingletonLazyNonThreadSafe {
    private static SingletonLazyNonThreadSafe instance = null;
    private SingletonLazyNonThreadSafe() {
        
    }
    public static SingletonLazyNonThreadSafe instance() {
        if(instance == null) {
            instance = new SingletonLazyNonThreadSafe();
        }
        return instance;
    }
    public void method() {
        
    }
}

/**
 * This is apparently most widely used because it is
 * thread safe and does not use synchronized.
 * @author wng
 *
 */
class SingletonInnerThreadSafe {
    private SingletonInnerThreadSafe() {
        
    }
    private static class Loader {
        private static final SingletonInnerThreadSafe instance = new SingletonInnerThreadSafe();
    }
    public static SingletonInnerThreadSafe instance() {
        return Loader.instance;
    }
    public void method() {
        
    }
}

/**
 * This is also a popular method, using enum.
 *
 */
enum SingletonEnumThreadSafe {
    instance;
    public void method() {
        
    }
}

