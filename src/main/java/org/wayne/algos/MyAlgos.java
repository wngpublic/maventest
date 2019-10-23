package org.wayne.algos;

import org.wayne.main.MyBasic;
import org.wayne.misc.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MyAlgos implements MyBasic {
    Utils utils = new Utils();

    @Override
    public void shutdown() {

    }

    @Override
    public void init() {

    }

    public static void p(String f, Object ... o) {
        System.out.printf(f,o);
    }
    public void testDistribution1() throws Exception {
        class PairNum {
            int lo;
            int hi;
            int v;

            PairNum(int lo, int hi) {
                this.lo = lo;
                this.hi = hi;
            }
        }
        class Helper {
            int category(List<PairNum> l, int v) {
                for (PairNum p : l)
                    if (v >= p.lo && v <= p.hi) return p.v;
                return 0;
            }

            Map<Integer, Integer> selectUniqueCategoriesAndVersions(Utils utils, List<PairNum> listPair, int maxRange, int numCategoriesToSelect) {
                Map<Integer, Integer> categories = new HashMap<>();
                List<Integer> listCategories = new ArrayList<>();
                for (PairNum pn : listPair) {
                    listCategories.add(pn.v);
                }
                utils.shuffle(listCategories);
                for (int i = 0; i < numCategoriesToSelect; i++) {
                    int category = listCategories.get(i);
                    int numVersions = utils.getInt(1, 5);
                    categories.put(category, numVersions);
                }
                return categories;
            }
        }
        Helper h = new Helper();

        int maxRange = 0;
        int numItemTypes = 10;

        List<PairNum> listPair = new ArrayList<>();
        for (int i = 0, beg = 0; i < numItemTypes; i++) {
            int cur = utils.getInt(2, 20) + beg;
            PairNum pair = new PairNum(beg, cur);
            pair.v = i;
            listPair.add(pair);
            maxRange = cur;
            beg = cur + 1;
        }

        int minNum = 1;
        int maxNum = numItemTypes / 2;

        int numIterations = 10;
        Map<Integer, Integer> mapDistribution = new TreeMap<>();

        for (int i = 0; i < numIterations; i++) {
            int numCategories = utils.getInt(minNum, maxNum);
            Map<Integer, Integer> categories = h.selectUniqueCategoriesAndVersions(utils, listPair, maxRange, numCategories);
            for (Map.Entry<Integer, Integer> kv : categories.entrySet()) {
                Integer count = mapDistribution.get(kv.getKey());
                if (count == null) {
                    count = 0;
                }
                count += kv.getValue();
                mapDistribution.put(kv.getKey(), count);
            }
        }
        p("Pairs Values\n");
        for(PairNum pn: listPair) {
            p("%2d:%2d = %2d\n",pn.lo,pn.hi,(pn.hi-pn.lo+1));
        }
        p("distribution:\n");
        for (Map.Entry<Integer, Integer> kv : mapDistribution.entrySet()) {
            p("%2d = %2d\n", kv.getKey(), kv.getValue());
        }
    }
}
