package org.wayne.java.package2;

import org.wayne.java.package1.ObjectOne;
import java.util.List;

public class ObjectTwo {
    org.wayne.java.package1.ObjectOne o1;
    public void setObjectOne(ObjectOne o1) {
        this.o1 = o1;
    }
    public List<String> getObjectOneStrings() {
        //return o1.getStrings(); // cannot access separate package's protected method
        return o1.getStringsPublic();
    }
    protected void addSelfStringToObjectOne() {
        if(o1 != null) {
            o1.addString("package2.ObjectTwo");
        }
    }
}

