package org.wayne.java.package1;

import java.util.ArrayList;
import java.util.List;

public class ObjectTwo {
    ObjectOne o1;
    public void setObjectOne(ObjectOne o1) {
        this.o1 = o1;
    }
    public List<String> getObjectOneStrings() {
        List<String> l = new ArrayList<>();
        List<String> l2 = o1.getStrings();
        return l;
    }
}
