package org.wayne.java.package2;

import org.wayne.java.package1.ObjectOne;

import java.util.List;

public class ObjectThree extends ObjectOne {
    public ObjectThree(String name, Integer id) {
        super(name, id);
    }
    public void printStrings() {
        List<String> l = getStrings(); // can access separate package's protected method
    }
}
