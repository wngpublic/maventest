package org.wayne.distributed;

import org.wayne.main.MyBasic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Java8Stuff implements MyBasic {

    public static void p(String f, Object ...o) {
        System.out.printf(f, o);
    }

    @Override
    public void shutdown() {

    }

    @Override
    public void init() {

    }

    class OI {
        int i;
        OI(int i) { set(i); }
        int get() { return i; }
        void set(int i) { this.i = i; }
        void inc(int x) { this.i += x; }
        OI copy() { return new OI(i); }
    }

    public void testStreamList() {
        {
            List<OI> listInt1 = new ArrayList<>();
            for(int i = 0; i < 10; i++) {
                OI oi = new OI(i);
                listInt1.add(oi);
            }

            Integer [] arrayInt2 = {0,1,2,3,4,5,6,7,8,9};
            List<Integer> listInt2 = Arrays.asList(arrayInt2);
            Stream<Integer> streamInt2 = Stream.of(arrayInt2);

            OI [] arrayOI2 = new OI[10];
            for(int i = 0; i < 10; i++) {
                arrayOI2[i] = new OI(i);
            }
            List<OI> listOI2 = Arrays.asList(arrayOI2);
            Stream<OI> streamOI2 = Stream.of(arrayOI2);

            //streamOI2.forEach(x -> x.inc(100));
            long count = streamOI2.count();
            p("%d\n",count);
            count = streamOI2.count();
            p("%d\n",count);
            //assert streamOI2.count() == 10;

            //listInt1.stream().forEach()
        }
    }

}
