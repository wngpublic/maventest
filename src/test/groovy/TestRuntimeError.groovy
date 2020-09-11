

class TestRuntimeError {
    def testArgs(List<String> ls1, Map<Integer,String> m1) {
        Map<Integer,String> map = new HashMap<>();
        for(int i = 0; i < 10; i++) {
            String s = String.format("%s:%02d","stringid",i);
            map.put(i,s);
        }
        Collection<String> mvals = map.values();
        Map<String,Integer> mapinv = new HashMap<>();
        for(Map.Entry<Integer,String> kv: map.entrySet()) {
            mapinv.put(kv.getValue(),kv.getKey());
        }
        for(String s: ls1) {
            if(mapinv.containsKey(s)) {
                return true;
            }
        }
        for(Map.Entry<Integer,String> kv: m1.entrySet()) {
            if(map.containsKey(kv.getKey())) {
                return true;
            }
        }
        return false;
    }
    def testArgsSimple(String s, int i) {
        String ret = String.format("%s:%02d",s,i);
        return ret;
    }
    def testException(String s, int i) {
        return doException(s,i);
    }
    def doException(String s, int i) {
        i++;
        String v = String.format("%s_%d",s,i);
        int izero = 0;
        int i2 = i;
        int i1 = i2/izero;
        i1++;
        return i1;
    }
}