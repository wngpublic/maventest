package org.wayne.java.package1;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

public class ObjectOne {
    Set<String> set = new HashSet<>();
    String name;
    Integer id;
    public ObjectOne(String name, Integer id) {
        this.name = name;
        this.id = id;
    }
    public void addString(String v) {
        set.add(v);
    }
    protected List<String> getStrings() {
        return new ArrayList<String>(set);
    }
    public List<String> getStringsPublic() {
        return getStrings();
    }
    public boolean hasString(String v) {
        return set.contains(v);
    }
}
