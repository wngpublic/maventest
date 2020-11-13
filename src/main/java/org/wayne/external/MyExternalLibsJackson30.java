package org.wayne.external;

//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.wayne.main.MyBasic;
import org.wayne.misc.Utils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class MyExternalLibsJackson30 implements MyBasic {
    Utils utils = new Utils();
    JsonFactory jsonFactory = new MappingJsonFactory();
    public MyExternalLibsJackson30() {
        jsonFactory.enable(JsonParser.Feature.AUTO_CLOSE_SOURCE);
    }

    static void p(String f, Object ...o) {
        System.out.printf(f, o);
    }
    public void testLRU() {
        Cache<Integer,Integer> cache = CacheBuilder.newBuilder().maximumSize(10).recordStats().build();

        p("put 0-10\n");
        IntStream.range(0,11).forEach(i -> cache.put(i,i));
        IntStream.range(0,11).forEach(i -> { p("getIfPresent:%d -> %d\n", i, cache.getIfPresent(i)); });

        p("put 10-15\n");
        for(int i = 10; i < 16; i++) {
            cache.put(i,i);
        }
        p("stats:%s\n", cache.stats());
        //IntStream.range(10, 16).forEach(i -> cache.put(i,i));
        IntStream.range(0,16).forEach(i -> { p("getIfPresent:%d -> %d\n", i, cache.getIfPresent(i)); });

        p("access 5-9\n");
        IntStream.range(5,10).forEach(i -> { p("getIfPresent:%d -> %d\n", i, cache.getIfPresent(i)); });

        p("put 16-20\n");
        for(int i = 16; i < 21; i++) {
            cache.put(i,i);
        }
        p("stats:%s\n", cache.stats());
        IntStream.range(0,21).forEach(i -> { p("getIfPresent:%d -> %d\n", i, cache.getIfPresent(i)); });

        p("put 30-50\n");
        IntStream.range(30, 51).forEach(i -> cache.put(i,i));
        IntStream.range(0,51).forEach(i -> { p("getIfPresent:%d -> %d\n", i, cache.getIfPresent(i)); });
    }
    public void testLRULastWrite() {
        Cache<Integer,Integer> cache = CacheBuilder.newBuilder().expireAfterWrite(60, TimeUnit.SECONDS).maximumSize(10).recordStats().build();

        p("put 0-10\n");
        IntStream.range(0,11).forEach(i -> cache.put(i,i));
        IntStream.range(0,11).forEach(i -> { p("getIfPresent:%d -> %d\n", i, cache.getIfPresent(i)); });

        p("put 10-15\n");
        for(int i = 10; i < 16; i++) {
            cache.put(i,i);
        }
        //IntStream.range(10, 16).forEach(i -> cache.put(i,i));
        IntStream.range(0,16).forEach(i -> { p("getIfPresent:%d -> %d\n", i, cache.getIfPresent(i)); });

        p("access 5-9\n");
        IntStream.range(5,10).forEach(i -> { p("getIfPresent:%d -> %d\n", i, cache.getIfPresent(i)); });

        p("put 16-20\n");
        for(int i = 16; i < 21; i++) {
            cache.put(i,i);
        }
        IntStream.range(0,21).forEach(i -> { p("getIfPresent:%d -> %d\n", i, cache.getIfPresent(i)); });

        p("put 30-50\n");
        IntStream.range(30, 51).forEach(i -> cache.put(i,i));
        IntStream.range(0,51).forEach(i -> { p("getIfPresent:%d -> %d\n", i, cache.getIfPresent(i)); });
    }
    public void testLRULastAccess() {
        Cache<Integer,Integer> cache = CacheBuilder.newBuilder().expireAfterAccess(60, TimeUnit.SECONDS).maximumSize(10).recordStats().build();

        p("put 0-10\n");
        IntStream.range(0,11).forEach(i -> cache.put(i,i));
        IntStream.range(0,11).forEach(i -> { p("getIfPresent:%d -> %d\n", i, cache.getIfPresent(i)); });

        p("put 10-15\n");
        for(int i = 10; i < 16; i++) {
            cache.put(i,i);
        }
        //IntStream.range(10, 16).forEach(i -> cache.put(i,i));
        IntStream.range(0,16).forEach(i -> { p("getIfPresent:%d -> %d\n", i, cache.getIfPresent(i)); });

        p("access 5-9\n");
        IntStream.range(5,10).forEach(i -> { p("getIfPresent:%d -> %d\n", i, cache.getIfPresent(i)); });

        p("put 16-20\n");
        for(int i = 16; i < 21; i++) {
            cache.put(i,i);
        }
        IntStream.range(0,21).forEach(i -> { p("getIfPresent:%d -> %d\n", i, cache.getIfPresent(i)); });

        p("put 30-50\n");
        IntStream.range(30, 51).forEach(i -> cache.put(i,i));
        IntStream.range(30,51).forEach(i -> { p("getIfPresent:%d -> %d\n", i, cache.getIfPresent(i)); });
    }

    public void testCacheStatsEnabled(int maxSize) {
        Cache<Integer,String> cache = CacheBuilder.newBuilder().maximumSize(maxSize).recordStats().build();
        IntStream.range(0,maxSize*2).forEach(i -> cache.put(i, utils.getRandString(16)));
        int cntHit = 0;
        int cntMiss = 0;
        for(int i = 0; i < maxSize; i++) {
            int key = utils.getInt(0,10000000);
            if(cache.getIfPresent(key) != null) {
                cntHit++;
            } else {
                cntMiss++;
            }
        }
        p("stats:%s\n", cache.stats());
        p("hit:%d miss:%d\n", cntHit, cntMiss);
        p("cache stats: size:%d stats:%d\n", cache.size(), cache.stats().evictionCount());
    }

    public void testCacheStatsDisabled(int maxSize) {
        Cache<Integer,String> cache = CacheBuilder.newBuilder().maximumSize(maxSize).build();
        IntStream.range(0,maxSize*2).forEach(i -> cache.put(i, utils.getRandString(16)));
        int cntHit = 0;
        int cntMiss = 0;
        for(int i = 0; i < maxSize; i++) {
            int key = utils.getInt(0,10000000);
            if(cache.getIfPresent(key) != null) {
                cntHit++;
            } else {
                cntMiss++;
            }
        }
        p("stats:%s\n", cache.stats());
        p("hit:%d miss:%d\n", cntHit, cntMiss);
        p("cache stats: size:%d stats:%d\n", cache.size(), cache.stats().evictionCount());
    }

    public void testCacheStatsEnabledBenchmark() {
        int maxSize = 1000000;
        p("begin stats enabled loop 5 times\n");
        for(int i = 0; i < 5; i++) {
            testCacheStatsEnabled(maxSize);
        }
    }
    public void testCacheStatsDisabledBenchmark() {
        int maxSize = 1000000;
        p("begin stats disabled loop 5 times\n");
        for(int i = 0; i < 5; i++) {
            testCacheStatsDisabled(maxSize);
        }
    }
    public void testJSON1() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"aa0\":"); // list
        sb.append("[");
            sb.append("\"listval_aa0_0\",");
            sb.append("\"listval_aa0_1\",");
            sb.append("\"listval_aa0_2\"");
        sb.append("],");
        sb.append("\"aa1\":"); // map
        sb.append("{");
            sb.append("\"mapkey_aa1_0\":\"mapval_aa1_0\",");
            sb.append("\"mapkey_aa1_1\":\"mapval_aa1_1\",");
            sb.append("\"mapkey_aa1_2\":\"mapval_aa1_2\"");
        sb.append("},");
        sb.append("\"aa2\":"); // map of list
        sb.append("{");
            sb.append("\"maplist_aa2_0\":");
            sb.append("[");
                sb.append("\"listval_aa2_0_0\",");
                sb.append("\"listval_aa2_0_1\"");
            sb.append("],");
            sb.append("\"maplist_aa2_1\":");
            sb.append("[");
                sb.append("\"listval_aa2_1_0\",");
                sb.append("\"listval_aa2_1_1\"");
            sb.append("]");
        sb.append("}");
        sb.append("}");
        String strJson = sb.toString();
        p("jsonStr = %s\n", strJson);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNodeP = objectMapper.readTree(strJson);
            JsonNode jsonNodeC = jsonNodeP.get("aa0");
            String str0 = jsonNodeP.asText();
            String str1 = jsonNodeC.asText();

            p("-------------------\n");
            p("original: %s\n",strJson);
            p("-------------------\n");
            p("mod: %s\n",str0);
            p("-------------------\n");
            str0 = jsonNodeP.toString();
            p("mod: %s\n",str0);
            p("-------------------\n");

            List<String> list1 = objectMapper.readerFor(new TypeReference<List<String>>(){}).readValue(jsonNodeC);
            p("print List of String\n");
            list1.stream().forEach(x -> p("%s\n", x));

            jsonNodeC = jsonNodeP.get("aa1");
            Map<String,String> map1 = objectMapper.readerFor(new TypeReference<Map<String,String>>(){}).readValue(jsonNodeC);
            p("print Map of String\n");
            map1.entrySet().stream().forEach(kv -> p("k:%s v:%s\n", kv.getKey(), kv.getValue()));

            jsonNodeC = jsonNodeP.get("aa2");
            Map<String,JsonNode> map2 = objectMapper.readerFor(new TypeReference<Map<String,JsonNode>>(){}).readValue(jsonNodeC);
            p("print map of list\n");
            for(Map.Entry<String,JsonNode> kv1: map2.entrySet()) {
                jsonNodeC = kv1.getValue();
                List<String> list2 = objectMapper.readerFor(new TypeReference<List<String>>(){}).readValue(jsonNodeC);
                p("print List of String\n");
                list2.stream().forEach(x -> p("%s\n", x));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void testJSON2() {
        String jsonObject1Str = "{\"s2\":\"strings2\",\"s1\":\"strings1\",\"i1\":1,\"i2\":2}";
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JSONObject1 jsonObject1 = objectMapper.readValue(jsonObject1Str, JSONObject1.class);
            p("%s\n", jsonObject1.toString());
            String obj2str = objectMapper.writeValueAsString(jsonObject1);
            p("str obj:%s\n", obj2str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void testJSON3() {
        ObjectMapper objectMapper = new ObjectMapper();
        /*
         * [
         *    { aa0 : [ lv0_0, lv0_1, lv0_2 ]},
         *    { aa1 : {
         *             k1_0: v1_0,
         *             k1_1: v1_1,
         *             k1_2: v1_2
         *          }
         *    },
         *    {
         *      aa2 : [
         *             {k2_0: [ l2_00, l2_01]},
         *             {k2_1: [ l2_10, l2_11]}
         *          ]
         *    },
         *    { aa3 : 3 }
         * ]
         */
        {
            ArrayNode root = objectMapper.createArrayNode();
            ArrayNode aa0 = objectMapper.createArrayNode();
            aa0.add("lv00").add("lv01").add("lv02");
            ObjectNode on0 = objectMapper.createObjectNode().putPOJO("aa0", aa0);
            root.add(on0);

            ObjectNode aa1 = objectMapper.createObjectNode();
            aa1.put("k10","v10").put("k11","v11").put("k12","v12");
            ObjectNode on1 = objectMapper.createObjectNode().putPOJO("aa1", aa1);
            root.add(on1);

            ArrayNode aa2 = objectMapper.createArrayNode();
            ObjectNode onk20 = objectMapper.createObjectNode()
                .putPOJO("k20", objectMapper.createArrayNode().add("lv200").add("lv201"));
            ObjectNode onk21 = objectMapper.createObjectNode()
                .putPOJO("k21", objectMapper.createArrayNode().add("lv210").add("lv211"));
            root.add(objectMapper.createObjectNode().putPOJO("aa2", aa2.add(onk20).add(onk21)));

            root.add(objectMapper.createObjectNode().put("aa3", 3));

            try {
                String s1 = objectMapper.writeValueAsString(root);
                p("s1:%s\n", s1);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        /*
         * {
         *    aa0 : [ lv00, lv01, lv02 ],
         *    aa1 : {
         *             k10: v10,
         *             k11: v11,
         *             k12: v12
         *          },
         *    aa2 : {
         *             k2_0: [ l200, l201],
         *             k2_1: [ l210, l211]
         *          },
         *    aa3 : 3
         * }
         */
        {
            ObjectNode root = objectMapper.createObjectNode();
            ArrayNode aa0 = objectMapper.createArrayNode().add("lv00").add("lv01").add("lv02");
            root.putPOJO("aa0", aa0);

            ObjectNode aa1 = objectMapper.createObjectNode()
                .put("k10","v10")
                .put("k11","v11")
                .put("k12","v12");
            root.putPOJO("aa1", aa1);

            ObjectNode aa2 = objectMapper.createObjectNode();
            ArrayNode onk20 = objectMapper.createArrayNode().add("lv200").add("lv201");
            ArrayNode onk21 = objectMapper.createArrayNode().add("lv210").add("lv211");
            aa2.putPOJO("k20", onk20);
            aa2.putPOJO("k21", onk21);
            root.putPOJO("aa2", aa2);
            root.put("aa3", 3);

            try {
                String s1 = objectMapper.writeValueAsString(root);
                p("s1:%s\n", s1);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }
    public void testJSON4() {
        /*
         * Hierachy for querying and inserting.
         *
         *  company
         *    name
         *    address
         *    departments
         *      directors
         *      executive
         *      manufacturing
         *      r&d
         *        groupWidget1
         *        groupWidget2
         *        groupWidget3
         *          groupWidget3.1
         *          groupWidget3.2
         *      legal
         *        counsel
         *        deptGroupWidget1
         *        deptGroupWidget2
         *      sales&marketing
         *      finance
         *        audit
         *        accounting
         *        revenue
         *        tax
         *      hr
         *      field&facilities
         *        operations
         *          office1
         *          office2
         *        inventory
         *          office1
         *          office2
         *    employee hieararchy
         *      directors
         *      executive
         *        head
         *      manufacturing
         *      r&d
         *        groupWidget1
         *          head
         *            namefirst
         *            namelast
         *            deptname
         *            groupWidget1.1
         *              head
         *                sub1
         *                sub2
         *            groupWidget1.2
         *              headR&D
         *                sub1
         *                sub2
         *              headQA
         *                sub1
         *                sub2
         *        groupWidget2
         *        groupWidget3
         *        ...
         *        research
         *        systems
         *        operations
         *      legal
         *        counsel
         *        deptR&D
         *      sales&marketing
         *      finance
         *        audit
         *        accounting
         *        revenue
         *        tax
         *      hr
         *      field&facilities
         *        operations
         *          office1
         *            head
         *              sub1
         *              sub2
         *                sub2.1
         *                sub2.2
         *                sub2.3
         *                sub2.4
         *              sub3
         *                sub3.1
         *          office2
         *            head
         *              sub1
         *                sub1.1
         *                sub1.2
         *                sub1.3
         *              sub2
         *                sub2.1
         *              sub3
         *                sub3.1
         *    customers
         *    products
         *      services
         *        marketing
         *          advertisements
         *          campaigns
         *        financial
         *          tax
         *          advice
         *          transactions
         *          equity
         *          retirement
         *          real estate
         *          savings
         *        food
         *          on premise
         *          delivery
         *        entertainment
         *          on premise
         *          delivery
         *        electronic
         *          routing
         *          storage
         *          processing
         *        social
         *          athletic
         *          games
         *          entertainment
         *          misc
         *        education
         *          profit
         *            primary
         *            secondary
         *            tertiary
         *          non profit
         *        transportation
         *          deliveries and shipments
         *        raw materials processing
         *          lumber
         *          stone
         *          chemicals
         *      goods
         *        food
         *          fruits
         *          canned
         *          dried
         *          meats
         *          vegetables
         *          processed
         *          desserts
         *        electronic
         *        mechanical
         *        real estate
         *        raw materials
         *        toys
         *      government
         *        audit
         *        tax
         *        labor
         *
         *
         */
        ObjectMapper om = new ObjectMapper();
        ObjectNode root = om.createObjectNode();
        root.put("name", "company");
        ObjectNode on;
        {
            /*
             *    departments
             *      directors
             *      executive
             *      manufacturing
             *        plant1
             *        plant2
             *      r&d
             *        groupWidget1
             *        groupWidget2
             *        groupWidget3
             *          groupWidget3.1
             *          groupWidget3.2
             *      legal
             *        counsel
             *        deptGroupWidget1
             *        deptGroupWidget2
             *      sales&marketing
             *      finance
             *        audit
             *        accounting
             *        revenue
             *        tax
             *      hr
             *      field&facilities
             *        operations
             *          office1
             *          office2
             *        inventory
             *          office1
             *          office2
             */
            on = om.createObjectNode();
            root.putPOJO("departments", on);
            on.put("directors","1");
            on.put("executive","2");
            on.putPOJO("manufacturing",
                om.createObjectNode()
                    .put("plant1","3.1")
                    .put("plant2","3.2"));
            on.putPOJO("r&d",
                om.createObjectNode()
                .put("groupWidget1","4.1")
                .put("groupWidget2","4.2")
                .putPOJO("groupWidget3",
                    om.createObjectNode()
                    .put("groupWidget3.1","4.3.1")
                    .put("groupWidget3.2","4.3.2")
                )
            );
            on.putPOJO("legal",
                om.createObjectNode()
                .put("counsel","5.1")
                .put("deptGroupWidget","5.2")
            );
            on.putPOJO("finance",
                om.createObjectNode()
                    .put("audit","6.1")
                    .put("accounting","6.2")
                    .put("revenue","6.3")
                    .put("tax","6.4")
            );
        }
        root.putPOJO("address", om.createObjectNode());
        ArrayNode arrayNode = om.createArrayNode();
        for(int i = 0; i < 3; i++) {
            arrayNode.add(String.format("data_%d",i));
        }
        root.putPOJO("employees", arrayNode);
        root.putPOJO("customers", om.createObjectNode());
        root.putPOJO("products", om.createObjectNode());
        JsonNode jn = root.get("departments");
        try {
            String s1;
            s1 = om.writeValueAsString(root);
            p("\n");
            p("%s\n", s1);
            s1 = om.writeValueAsString(jn);
            p("\n");
            p("%s\n", s1);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    /*
     * schema generation for random sql data, used for query practice.
     *
     * the goal of this dataset is to create a model where most sql queries can be exercised.
     * this includes inner joins, outer joins, filtering over multiple indices that can expose
     * performance differences among single indexed, multi indexed, multi indexed and non indexed.
     * filter over ranges, sparse filtering. the max join support would be 3 schemas. also
     * support basic emulation of partitioned tables.
     *
     * I am creating a basic model of companies and inventory and customers, since it is a
     * good starting point to emulate reality, and it enables sql modeling of complex tasks.
     *
     * table.entities # list of all people and companies, people can have multi accounts and addresses
     *
     * table.employees
     *
     * table.employees.reports
     *
     * table.inventories    # inventories for companies
     *
     * table.items          # item code
     *
     * table.transactions   # transactions history for companies, debit/credit
     *
     * table.companies.job
     *
     * table.jobCategories
     *
     * table.companies.structure # table of dept names
     *
     * table.companyCustomerAccounts: # list of all accounts for all companies
     *
     * table.countryCode # list of all relevant countryCodes
     *
     * table.addresses # list of all company and customer addresses
     *
     */
    public void testGenerateSQLData1() {



    }

    final static Map<String,Set<String>> gmap1 = ImmutableMap.<String,Set<String>>builder()
        .put("t1", Collections.emptySet())
        .put("t2",Collections.emptySet())
        .put("t3",ImmutableSet.of("v1","v2","v3"))
        //.put("t4",null) // this causes runtime error, i guess is checked
        .put("t4",Collections.emptySet())
        .build();
    public void testGuavaMap1() {
        p("testGuavaMap1\n");
        if(gmap1.containsKey("xx")) {
            p("xx exists\n");
        } else {
            p("xx does not exist\n");
        }
        Set<String> set1 = gmap1.get("t1");
        if(set1.size() == 0) {
            p("t1 set is 0\n");
        }
        set1 = gmap1.get("t4");
        if(set1 == null) {
            p("t4 set is null\n");
        }
        set1 = gmap1.get("t3");
        if(set1.size() == 3) {
            p("t3 map size is 3\n");
            p("get v2: %s\n", set1.contains("v2"));
            p("get v4: %s\n", set1.contains("v4"));
        }
    }
    public void testGuavaMap2() {
        p("testGuavaMap2\n");
        Map<String,Set<String>> gmap1 = ImmutableMap.<String,Set<String>>builder()
            .put("l1", Collections.emptySet())
            .put("l2",Collections.emptySet())
            .put("l3",ImmutableSet.of("v1","v2","v3"))
            //.put("t4",null) // this causes runtime error, i guess is checked
            .put("l4",Collections.emptySet())
            .build();
        if(gmap1.containsKey("xx")) {
            p("xx exists\n");
        } else {
            p("xx does not exist\n");
        }
        p("--------test local\n");
        Set<String> set1 = gmap1.get("l1");
        if(set1.size() == 0) {
            p("l1 set is 0\n");
        }
        set1 = gmap1.get("l4");
        if(set1 == null) {
            p("l4 set is null\n");
        }
        set1 = gmap1.get("l3");
        if(set1.size() == 3) {
            p("l3 map size is 3\n");
            p("get v2: %s\n", set1.contains("v2"));
            p("get v4: %s\n", set1.contains("v4"));
        }
        p("--------test this\n");
        set1 = this.gmap1.get("t1");
        if(set1.size() == 0) {
            p("t1 set is 0\n");
        }
        set1 = this.gmap1.get("t4");
        if(set1 == null) {
            p("t4 set is null\n");
        }
        set1 = this.gmap1.get("t3");
        if(set1.size() == 3) {
            p("t3 map size is 3\n");
            p("get v2: %s\n", set1.contains("v2"));
            p("get v4: %s\n", set1.contains("v4"));
        }
        p("--------test non this\n");
        set1 = gmap1.get("t1");
        if(set1 == null) {
            p("t1 set is null\n");
        }
        set1 = gmap1.get("t4");
        if(set1 == null) {
            p("t4 set is null\n");
        }
        set1 = gmap1.get("t3");
        if(set1 == null) {
            p("t3 map is null\n");
        }

    }

    public void testDefaultOptional() {
        OptionalInt.of(10).orElse(1);
        Optional.ofNullable(null).orElse(10);
        //String s =
    }

    public void testJsonNode() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        {
            // this will fail because you cannot use single quotes. expects double quotes.
            boolean didFail = false;
            String stringJson = "{'key1':'val1','key2':'val2','key3':'val3'}";
            try {
                JsonNode node = objectMapper.readTree(stringJson);
                assert "val1".equals(node.at("/key1").asText());
            } catch(Exception e) {
                didFail = true;
            }
            assert didFail;
        }
        {
            String stringJson = "{\"key1\":\"val1\",\"key2\":\"val2\",\"key3\":\"val3\"}";
            JsonNode node = objectMapper.readTree(stringJson);
            assert "val2".equals(node.at("/key2").asText());
        }
        {
            String stringJson =
                "{" +
                    "\"key1\":\"val1\"," +
                    "\"key2\":\"val2\"," +
                    "\"key3\":\"val3\"" +
                    "}";
            JsonNode node = objectMapper.readTree(stringJson);
            assert "val2".equals(node.at("/key2").asText());
        }
        p("testJsonNode passed\n");
    }

    public void collapseComments() {
        /*
        {
  "k1":"v1",
  "k2":"v2",
  "k3":[
    {"k300":"v300", "k301":"v301", "k302":"v302"},
    {
      "k310":[
        {"k31011":"v31011", "k31012":"v31012"},
        {"k31021":"v31021", "k31022":"v31022"}
      ],
      "k311":[
        ["k31110", "k31111"],
        ["k31120", "k31121"]
      ],
      "k312":["k3120", "k3121"],
      "k313":{"k3131":"v3131", "k3132":"v3132"}
    },
    [
      {"k3210":"v3210", "k3211":"v3211"},
      {"k3220":"v3220", "k3221":"v3221"}
    ],
    [
      ["k3310","k3311"],
      ["k3320","k3321"]
    ],
    ["k341","k342"],
    "k35"
  ],
  "k4":[1,2,3,4,5],
  "k5":[
    [
      ["k5111", "k5112", "k5113"],
      [
        ["k51210","k51211"],
        ["k51220","k51221"]
      ]
    ],
    [
      {
        "k521":["k5210","k5211"],
        "k522":[
          ["k52201","k52202"],
          ["k52211","k52212"]
        ],
        "k523":{"k5231":"v5231","k5232":"v5232"}
      }
    ],
    ["k531","k532","k533"]
  ],
  "k6":{
    "k61":["k611","k612","k613"],
    "k62":[
      ["k6211","k6212"],
      ["k6221","k6222"]
    ],
    "k63":[
      [
        {"k6311":"v6311", "k6312":"v6312"},
        {"k6321":"v6321", "k6322":"v6322"}
      ],
      [
        ["k6331","k6332"],
        ["k6341","k6342"]
      ]
    ],
    "k64":{
      "k641":["k6411","k6412"],
      "k642":{"k6421":"v6421", "k6422":"v6422","k6423":null},
      "k643":[
        [],
        ["k64311","k64312"],
        ["k64321","k64322"]
      ],
      "k644":{},
      "k645":[]
    },
    "k65":{"k651":"v651", "k652":"v652"}
  }
}
         */
    }

    public void testJsonNodeFromFile() throws Exception {
        // refer to collapseComments for layout
        boolean isAssertEnabled = false;
        assert isAssertEnabled = true;
        if(!isAssertEnabled) {
            throw new Exception("assertions turned off, cannot check");
        }

        ObjectMapper objectMapper = new ObjectMapper();
        String pathname = "src/main/java/org/wayne/external/input1.json";
        pathname = "input_test_json.json";
        String sJson = IOUtils.toString(getClass().getClassLoader().getResourceAsStream(pathname), Charset.defaultCharset());
        JsonNode node = objectMapper.readTree(sJson);

        {
            try (JsonParser parser = jsonFactory.createParser(this.getClass().getResourceAsStream(pathname))) {
                node = parser.readValueAsTree();
            }
        }

        JsonNode root = node;

        assert node != null;
        assert node.at("/k1").textValue().equals("v1");
        assert node.at("/k3/0/k301").textValue().equals("v301");
        assert node.at("/k3/1/k311/0/0").textValue().equals("k31110");
        assert node.at("/k3/1/k311/0/1").textValue().equals("k31111");
        assert node.at("/k3/1/k313/k3132").textValue().equals("v3132");
        assert node.at("/k4/2").textValue().equals("v3");
        assert node.at("/k5/0/1/0/1").textValue().equals("k51211");
        assert node.at("/k5/1/0/k522/1/0").textValue().equals("k52211");
        assert node.at("/k5/1/0/k523/k5231").textValue().equals("v5231");
        assert node.at("/k5/2/1").textValue().equals("k532");
        assert node.at("/k6/k61/0").textValue().equals("k611");
        assert node.at("/k6/k62/1/1").textValue().equals("k6222");
        assert node.at("/k6/k63/0/1/k6322").textValue().equals("v6322");
        assert node.at("/k6/k63/1/1/0").textValue().equals("k6341");
        assert node.at("/k6/k64/k641/1").textValue().equals("k6412");
        assert node.at("/k6/k64/k642/k6422").textValue().equals("v6422");
        assert node.at("/k6/k64/k642/k6423").isNull();
        assert node.at("/k6/k64/k642/k6424").isMissingNode();
        assert node.at("/k6/k64/k643/0").size() == 0;
        assert node.at("/k6/k64/k643/1").size() == 2;
        assert node.at("/k6/k64/k643/1/1").textValue().equals("k64312");
        assert node.at("/k6/k64/k644").size() == 0;
        assert node.at("/k6/k64/k645").size() == 0;
        assert !node.at("/k6/k64/k644").isMissingNode();
        assert !node.at("/k6/k64/k644").isNull();
        assert !node.at("/k6/k64/k645").isMissingNode();
        assert !node.at("/k6/k64/k645").isNull();

        // iterate k7 array
        JsonNode nodeArray = node.at("/k7/0");

        {
            int ctr1 = 0;
            for(JsonNode n: nodeArray) {
                int ctr2 = 0;
                for(JsonNode a: n) {
                    String key = String.format("k70%d%d",ctr1,ctr2);
                    assert a.textValue().equals(key);
                    ctr2++;
                }
                ctr1++;
            }
            assert nodeArray.size() == 3;
        }

        // iterate k7 map
        JsonNode nodeMap   = node.at("/k7/1/0");
        {
            Iterator<Map.Entry<String, JsonNode>> it = nodeMap.fields();
            int ctr1 = 0;
            while(it.hasNext()) {
                Map.Entry<String, JsonNode> kv = it.next();
                String keyName = kv.getKey();
                assert !StringUtils.isEmpty(keyName);
                //p("keyname:%s\n", keyName);
                JsonNode n = kv.getValue();
                for(int i = 0; i < 2; i++) {
                    String subkey = String.format("/k%d/0/k",i);
                    assert n.at(subkey+"/0/k2").textValue().equals("v21");
                    assert n.at(subkey+"/1/k2").textValue().equals("v23");
                }
                ctr1++;

            }
        }

        assert node.get("k7") != null;
        boolean flag = true;
        try {
            assert node.get("k8") == null;
            assert node.at("/k8").isMissingNode();
            assert !node.at("/k8").isNull();
            flag = node.at("/k8").isNull();
            JsonNode ntmp = node.at("/k8");
            assert ntmp.isMissingNode();
        } catch(Exception e) {
            flag = true;
        }
        assert !flag;

        flag = true;
        try {
            JsonNode n;
            boolean b;
            int i;
            JsonNode ntmp;

            n = node.at("/k10/k10_1"); // "k10_1":{},
            assert !n.isMissingNode();
            assert !n.isNull();
            assert !n.isValueNode();
            assert !n.isPojo();
            assert n.isContainerNode();
            assert !n.isArray();
            assert n.size() == 0;
            assert node.get("k10/k10_1") == null;
            assert node.get("/k10/k10_1") == null;

            ntmp = node.get("k10");
            assert ntmp.get("k10_1") != null; // "k10_1":{},
            assert !ntmp.get("k10_1").isNull();
            assert !ntmp.get("k10_1").isMissingNode();
            assert ntmp.get("k10_1").size() == 0;
            assert ntmp.get("k10/k10_5") == null;
            assert ntmp.get("/k10/k10_5") == null;

            assert ntmp.get("k10_2") != null; // "k10_2":[],
            assert !ntmp.get("k10_2").isNull();
            assert !ntmp.get("k10_2").isMissingNode();
            assert ntmp.get("k10_2").size() == 0;
            n = ntmp.get("k10_2");
            assert !n.isMissingNode();
            assert !n.isNull();
            assert !n.isValueNode();
            assert !n.isPojo();
            assert n.isContainerNode();
            assert n.isArray();

            assert ntmp.get("k10_3") != null; // "k10_3":null,
            assert ntmp.get("k10_3").isNull();
            assert !ntmp.get("k10_3").isMissingNode();
            assert ntmp.get("k10_3").size() == 0;
            n = ntmp.get("k10_3");
            assert !n.isMissingNode();
            assert n.isNull();
            assert n.isValueNode();
            assert !n.isPojo();
            assert !n.isContainerNode();
            assert !n.isArray();

            assert ntmp.get("k10_4") != null; // "k10_4":["v1","v2"],
            assert !ntmp.get("k10_4").isNull();
            assert !ntmp.get("k10_4").isMissingNode();
            assert ntmp.get("k10_4").size() == 2;
            n = ntmp.get("k10_4");
            assert !n.isMissingNode();
            assert !n.isNull();
            assert !n.isValueNode();
            assert !n.isPojo();
            assert n.isContainerNode();
            assert n.isArray();

            assert ntmp.get("k10_5") != null; // "k10_5":{"k10_5_0":"v10_5_0","k10_5_1":"v10_5_1"}
            assert !ntmp.get("k10_5").isNull();
            assert !ntmp.get("k10_5").isMissingNode();
            assert ntmp.get("k10_5").size() == 3;
            assert !ntmp.get("k10_5").isPojo();
            assert ntmp.get("k10_5").isContainerNode();
            assert !ntmp.get("k10_5").isArray();
            assert !ntmp.get("k10_5").isValueNode();

            assert ntmp.get("kxxx") == null;
        } catch(Exception e) {
            flag = false;
        }
        assert flag;

        try {
            /*
            "k103":{
                "k103.0":["v103.0.0","v103.0.1","v103.0.2"],
                "k103.1":["v103.1.0","v103.1.1","v103.1.2"],
                "k103.2":["v103.0.0","v103.0.1","v103.0.2"],
                "k103.3":["v103.0.0","v103.0.1","v103.3.2"],
                "k103.4":["v103.0.0","v103.0.1","v103.0.2","v103.4.3"]
            }
            */
            JsonNode n = node.get("k103");
            assert n != null;
            Map<String, Object> map = objectMapper.convertValue(n, Map.class);
            assert map != null;
            List<String> list0 = objectMapper.convertValue(map.get("k103.0"), List.class);
            Set<String> set0 = objectMapper.convertValue(map.get("k103.0"), Set.class);
            assert list0 != null;
            assert set0 != null;
            assert list0.size() == 3;
            assert set0.size() == 3;
            Set<String> set1 = objectMapper.convertValue(map.get("k103.1"), Set.class);
            Set<String> set2 = objectMapper.convertValue(map.get("k103.2"), Set.class);
            Set<String> set3 = objectMapper.convertValue(map.get("k103.3"), Set.class);
            Set<String> set4 = objectMapper.convertValue(map.get("k103.4"), Set.class);

            assert set1 != null;
            assert set2 != null;
            assert set3 != null;
            assert set4 != null;
            assert set0.size() == set2.size();
            assert set0.size() != set4.size();
            assert set4.size() == 4;

            for(String s: set0) {
                if(!set2.contains(s)) {
                    assert false : s;
                }
            }
            for(String s: set0) {
                if(set1.contains(s)) {
                    assert false : s;
                }
            }
            Map<String,Object> map2 = objectMapper.convertValue(map.get("k103.5"), Map.class);
            assert map2 != null;
            String smap2 = objectMapper.writeValueAsString(map2);
            try {
                flag = false;
                Map<String,Object> map3 = objectMapper.convertValue(smap2, Map.class);
                assert false;
            } catch(Exception e) {
                flag = true;
            }
            assert flag;
            Map<String,Object> map3 = objectMapper.readValue(smap2, Map.class);
            String smap3 = objectMapper.writeValueAsString(map3);
            assert smap2.equals(smap3);

            flag = true;
        } catch(Exception e) {
            flag = false;
        }
        assert flag;
        try {
            JsonNode n;
            Map<String,Object> map;
            SerializedPOJO serializedPOJO;

            n = node.get("k106"); // k106 has EXACT data of POJO
            map = objectMapper.convertValue(n, Map.class);
            serializedPOJO = objectMapper.convertValue(map, SerializedPOJO.class);
            assert serializedPOJO != null;
        } catch(Exception e) {
            flag = false;
        }
        assert flag;

        try {
            JsonNode n;
            Map<String,Object> map;
            SerializedPOJO serializedPOJO;

            n = node.get("k105"); // k105 has LESS data than POJO
            map = objectMapper.convertValue(n, Map.class);
            serializedPOJO = objectMapper.convertValue(map, SerializedPOJO.class);
            assert serializedPOJO != null;
            assert serializedPOJO.getK104_1() == null;
            assert serializedPOJO.getK104_0() != null;
        } catch(Exception e) {
            flag = false;
        }
        assert flag;

        try {
            JsonNode n;
            Map<String,Object> map;
            SerializedPOJO serializedPOJO;
            SerializedPOJOIgnorable serializedPOJOIgnorable;

            n = node.get("k104"); // k104 has MORE data than POJO
            map = objectMapper.convertValue(n, Map.class);
            try {
                flag = false;
                serializedPOJO = objectMapper.convertValue(map, SerializedPOJO.class);
                assert serializedPOJO == null;
                assert false;
            } catch(Exception e) {
                flag = true;
            }
            assert flag;

            try {
                flag = true;
                serializedPOJOIgnorable = objectMapper.convertValue(map, SerializedPOJOIgnorable.class);
                assert serializedPOJOIgnorable != null;
                assert serializedPOJOIgnorable.getK104_0() != null;
                assert serializedPOJOIgnorable.getK104_1() != null;
                assert serializedPOJOIgnorable.getK104_5() != null;
            } catch(Exception e) {
                flag = false;
            }
            assert flag;

            n = node.get("k107");
            map = objectMapper.convertValue(n, Map.class);
            try {
                flag = true;
                serializedPOJOIgnorable = objectMapper.convertValue(map, SerializedPOJOIgnorable.class);
                assert serializedPOJOIgnorable != null;
                assert serializedPOJOIgnorable.getK104_0() == null;
                assert serializedPOJOIgnorable.getK104_1() == null;
                assert serializedPOJOIgnorable.getK104_5() == null;
            } catch(Exception e) {
                flag = false;
            }
            assert flag;
        } catch(Exception e) {
            flag = false;
        }
        assert flag;


        {
            JsonNode n = node.get("k100");
            Map<String, Object> map = objectMapper.convertValue(n, Map.class);
            assert map != null;
            assert map.size() == 2;
            Map<String, String> map1 = objectMapper.convertValue(map.get("k100.0"), Map.class);
            assert map1 != null;
            assert map1.size() == 2;
            assert map1.get("k100.0.0") != null && map1.get("k100.0.0").equals("v100.0.0");
        }

        {
            JsonNode n = node.get("k101");
            Map<String, Object> map = objectMapper.convertValue(n, Map.class);
            assert map != null;
            assert map.size() == 2;
            List<String> list = objectMapper.convertValue(map.get("k101.1"), List.class);
            assert list != null;
            assert list.size() == 3;
            assert list.get(0).equals("v101.1.0");
        }

        {
            JsonNode n = node.get("k102");
            Map<String, Object> map = objectMapper.convertValue(n, Map.class);
            assert map != null;
            assert map.size() == 3;
            List<String> list = objectMapper.convertValue(map.get("k102.2"), List.class);
            assert list == null;
        }

        {
            String json2Str = node.asText();
            assert json2Str != null && json2Str.length() == 0;
            assert StringUtils.isEmpty(json2Str);
            assert StringUtils.isBlank(json2Str);
            json2Str = node.textValue();
            assert json2Str == null;
            json2Str = node.toString();
            assert json2Str != null && json2Str.length() != 0;
            assert StringUtils.isNotEmpty(json2Str);
            json2Str = node.get("k4").asText();
            assert json2Str != null && json2Str.length() == 0;
            json2Str = node.get("k4").textValue();
            assert json2Str == null;
            json2Str = node.get("k4").toString();
            assert json2Str != null && json2Str.length() != 0;

            JsonNode jsonnode = objectMapper.readTree(node.toString());
            assert jsonnode != null;

            C1 c1 = new C1();
            c1.setS1("somestring");
            c1.setI1(123);
            c1.setAs1(Arrays.asList("hello1","hello2","hello3"));
            c1.setSs1(new HashSet<>(Arrays.asList("hello1","hello2","hello3","hello3")));

            String sc1 = objectMapper.writeValueAsString(c1);
            C1 c1c = objectMapper.readValue(sc1, C1.class);
            assert c1c != null;

            C3 c3 = new C3();
            c3.setS1("somestring");
            c3.setI1(123);
            c3.setAs1(Arrays.asList("hello1","hello2","hello3"));
            c3.setSs1(new HashSet<>(Arrays.asList("hello1","hello2","hello3","hello3")));
            c3.setS2("someother");
            c3.setI2(234);
            String sc3 = objectMapper.writeValueAsString(c3);
            C3 c3c = objectMapper.readValue(sc3, C3.class);
            assert c3c != null;

            C1 c1_1 = new C1();
            c1_1.setS1("somestring_1");
            c1_1.setI1(1233);
            c1_1.setAs1(Arrays.asList("hello1_1","hello2_1","hello3_1"));
            c1_1.setSs1(new HashSet<>(Arrays.asList("hello1_1","hello2_1","hello3_1","hello3")));

            C2 c2 = new C2();
            c2.setS1("somestring");
            c2.setI1(123);
            c2.setC1_1(c1_1);
            c2.setAs1(Arrays.asList("hello1","hello2","hello3"));
            c2.setAc1(Arrays.asList(c1,c3));
            String sc2 = objectMapper.writeValueAsString(c2);
            C2 c2c = deserializeC2(objectMapper, sc2);
            assert c2c != null;

            JsonNode nodec = objectMapper.readTree(sc2);
            assert nodec != null;
            JsonNode nodectree = objectMapper.valueToTree(sc2);
            assert nodectree != null;
            String snodec = nodec.toString();           // this doesn't add backslash to each "
            String snodectree = nodectree.toString();   // this adds backslash to each "
            String snodej = objectMapper.writeValueAsString(nodec);
            assert snodej != null && snodej.equals(snodec);
            assert snodectree != null && !snodectree.equals(snodec);
            JsonNode subnode = nodec.get("ac1");
            assert subnode.isArray();
            JsonNode subnodeidx = subnode.get(0);
            C1 c1FromJson = objectMapper.treeToValue(subnodeidx, C1.class);
            assert c1FromJson != null;
            assert c1FromJson.getI1() != null && c1FromJson.getI1().intValue() == 123;

            List<C1> ac1 = new ArrayList<>();
            ac1.add(c1);
            ac1.add(c1_1);
            ac1.add(c3);
            List<C1> ac2 = new ArrayList<>();
            ac2.add(c1);
            ac2.add(c1_1);
            String sac1 = objectMapper.writeValueAsString(ac1);
            String sac2 = objectMapper.writeValueAsString(ac2);
            List<C1> ac1_1 = objectMapper.readValue(sac1, List.class);  // this converts to HashMap, not C1
            assert ac1_1.size() == 3;
            assert ac1_1.get(0) instanceof Map;
            List<C1> ac2_1 = objectMapper.readValue(sac2, new TypeReference<List<C1>>(){});
            assert ac2_1.size() == 2;
            assert ac2_1.get(0) instanceof C1;
            assert ac2_1.get(1) instanceof C1;
            // this one can fail because sac1 has extended type, which has undefined fields.
            // fix with FAIL_ON_UNKNOWN_PROPERTIES
            boolean failSeen = false;
            try {
                List<C1> ac1_2 = objectMapper.readValue(sac1, new TypeReference<List<C1>>(){});
            } catch(Exception e) {
                failSeen = true;
            }
            assert failSeen;
            failSeen = true;
            try {
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                List<C1> ac1_2 = objectMapper.readValue(sac1, new TypeReference<List<C1>>(){});
                failSeen = false;
                assert true;
            } catch(Exception e) {
                failSeen = true;
            }
            assert !failSeen;

            if(!testJsonMissingFields()) {
                assert false;
            }
        }

        {
            String v = "[\"1\",\"2\",\"3\"] ";
            try {
                flag = true;
                List<String> list = objectMapper.convertValue(v, List.class);
                assert false;
            } catch(Exception e) {
                flag = true;
            }
            assert flag;
            try {
                flag = true;
                List<String> list = objectMapper.readValue(v, List.class);
                assert list != null && list.size() == 3;
            } catch(Exception e) {
                flag = false;
            }
            assert flag;
        }
        {
            String v1 = "[\"1\",\"2\",\"3\"] ";
            String v2 = "[\"1\",\"2\",\"3\"] ";
            String v3 = "{\"list0\":[\"1\",\"2\",\"3\"],\"list1\": [\"1\",\"2\",\"3\"]}";
            try {
                flag = true;
                SerializedPOJO1 pojo = objectMapper.readValue(v3, SerializedPOJO1.class);
                assert SerializedPOJO1.isFullyPopulated(pojo);
                assert SerializedPOJO1.isPopulated(pojo);
                flag = true;
            } catch(Exception e) {
                flag = false;
            }
            assert flag;
        }
        {
            // parse a Map<String,List<String>>
            String v1 = "{\"list0\":[\"1\",\"2\",\"3\"],\"list1\": [\"1\",\"2\",\"3\"]}";
            try {
                flag = false;
                Map<String,List<String>> map = objectMapper.convertValue(v1, Map.class);
                flag = false;
            } catch(Exception e) {
                flag = true;
            }
            assert flag;
            try {
                flag = false;
                Map<String,String> map = objectMapper.convertValue(v1, Map.class);
                flag = false;
            } catch(Exception e) {
                flag = true;
            }
            assert flag;
            try {
                flag = false;
                Map<String,List<String>> map = objectMapper.readValue(v1, Map.class);
                flag = true;
            } catch(Exception e) {
                flag = false;
            }
            assert flag;
            try {
                flag = false;
                Map<String,String> map = objectMapper.readValue(v1, Map.class);
                flag = true;
            } catch(Exception e) {
                flag = false;
            }
            assert flag;
        }
        p("MyExternalLibsJackson30.testJsonNodeFromFile passed\n");
    }

    //  tests effects of POJO format and non POJO format
    public boolean testJsonMissingFields() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        C1 c1 = new C1();
        c1.setS1("somestring");
        c1.setI1(123);
        c1.setAs1(Arrays.asList("hello1","hello2","hello3"));
        c1.setSs1(new HashSet<>(Arrays.asList("hello1","hello2","hello3","hello3")));
        String sc1 = objectMapper.writeValueAsString(c1);
        C1 c1c = objectMapper.readValue(sc1, C1.class);
        assert c1c != null;

        C4 c4 = new C4();
        c4.setS1("somestring");
        c4.setI1(123);
        c4.setAs1(Arrays.asList("hello1","hello2","hello3"));
        c4.setSs1(new HashSet<>(Arrays.asList("hello1","hello2","hello3","hello3")));
        c4.setInteger2(222);
        String sc4 = objectMapper.writeValueAsString(c4);
        C4 c4c = objectMapper.readValue(sc4, C4.class);
        assert c4c != null;
        assert c4.getInteger2() == 222;
        assert c4c.getInteger2() == 222;

        C3 c3 = new C3();
        c3.setS1("somestring");
        c3.setI1(123);
        c3.setAs1(Arrays.asList("hello1","hello2","hello3"));
        c3.setSs1(new HashSet<>(Arrays.asList("hello1","hello2","hello3","hello3")));
        c3.setS2("someother");
        c3.setI2(234);
        String sc3 = objectMapper.writeValueAsString(c3);
        C3 c3c = objectMapper.readValue(sc3, C3.class);
        assert c3c != null;
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        C1 c3asc1 = objectMapper.readValue(sc3, C1.class);
        assert c3asc1 != null;
        C3 c1asc3 = objectMapper.readValue(sc1, C3.class);
        assert c1asc3 != null;
        assert c1asc3.getI2() == null;
        assert c1asc3.getS2() == null;
        assert c1asc3.getI1() == 123;
        assert c1asc3.getS1().equals("somestring");
        return true;
    }

    protected C2 deserializeC2(ObjectMapper objectMapper, String s) {
        // seems like I cannot readValue for list of objects? it causes exception
        //C2 c2c = objectMapper.readValue(sc2, C2.class);
        C2 c2 = new C2();
        try {
            JsonNode node = objectMapper.readTree(s);
            c2.setS1(node.get("s1").textValue());
            c2.setI1(node.get("i1").intValue());
            c2.setC1(objectMapper.convertValue(node.get("c1"), C1.class));
            //c2.setC1_1(objectMapper.convertValue(node.get("c1_1"), C1.class));
            c2.setC1_1(objectMapperWrapperConvertValue(objectMapper, node.get("c1_1"), C1.class));
            JsonNode nodeAS1 = node.get("as1");
            assert nodeAS1.isArray();
            JsonNode nodeAC1 = node.get("ac1");
            assert nodeAC1.isArray();
            c2.setAs1(objectMapper.convertValue(node.get("as1"), List.class));
            c2.setAc1(objectMapper.convertValue(node.get("ac1"), List.class));
            //TypeReference not needed...
            //c2.setAc1(objectMapper.convertValue(node.get("ac1"), new TypeReference<List<C1>>() {}));
            return c2;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private <T> T objectMapperWrapperConvertValue(ObjectMapper objectMapper, Object o, Class<T> clazz) {
        return objectMapper.convertValue(o, clazz);
    }

    protected void deserializeC2String(ObjectMapper objectMapper, String s) {
        {
            //c2.setAs1(Arrays.asList(objectMapper.treeToValue(node.get("as1"), List.class)));
            //TypeReference<List<String>> typeref = new TypeReference<List<String>>() {};
            //JsonNode as1 = node.get("as1");
            // this is for reading string json, not JsonNode
            //List<String> l1 = objectMapper.readValue(as1, List.class);
            //List<String> l2 = objectMapper.readValue(as1, typeref);
            //List<String> l3 = objectMapper.convertValue(as1, List.class);
        }
    }

    public void testJSONObject() {
        String sJson = "{\"k1\":\"v1\",\"k2\":\"v2\",\"k3\":[{\"k31\":\"v31\"},{\"k32\":\"v32\"}],\"k4\":{\"k40\":\"v40\",\"k41\":\"v41\"}}";
        JSONObject json = new JSONObject(sJson);
        String s = json.getString("k1");
        assert s.equals("v1");
        assert !json.has("foo");
        json.remove("k1");
        assert !json.has("k1");
        JSONObject o = json.getJSONObject("k4");
        assert o != null;
        s = o.getString("k40");
        assert s != null;
        json.getJSONObject("k4").remove("k40");
        assert !json.getJSONObject("k4").has("k40");
        p("passed testJSONObject");
    }

    public void testDiffJson(String json1, String json2) {

    }

    public void testDiffText(String text1, String text2) {

    }

    public void testDiffXML(String xml1, String xml2) {

    }

    public void testCurPath() {
        p("testCurPath:\n");
        p("%s\n", System.getProperty("user.dir"));
        p("%s\n", Paths.get("").toAbsolutePath().toString());
        p("%s\n", Paths.get(".").toAbsolutePath().toString());
        p("%s\n", Paths.get(".").toAbsolutePath().normalize().toString());
        p("%s\n", Paths.get(".").toString());
        {
            String pathname = "./src/main/java/org/wayne/external/input1.json";
            Path path = Paths.get(pathname);
            p("%s exists: %s\n",pathname,path.toFile().exists());
        }
        {
            String pathname = "./src/main/java/org/wayne/external/input2.json";
            Path path = Paths.get(pathname);
            p("%s exists: %s\n",pathname,path.toFile().exists());
        }
    }

    @Override
    public void shutdown() {

    }

    @Override
    public void init() {

    }

    public static void main(String [] args) {
        MyExternalLibsJackson30 t = new MyExternalLibsJackson30();
        try {
            t.testJSONObject();
            //t.testJsonNodeFromFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class C1 {
    String s1;
    Integer i1;
    List<String> as1;
    Set<String> ss1;
    public String getS1() { return s1;}
    public void setS1(String s1) { this.s1 = s1; }
    public Integer getI1() { return i1;}
    public void setI1(Integer i1) { this.i1 = i1; }
    public List<String> getAs1() { return as1; }
    public void setAs1(List<String> as1) { this.as1 = as1; }
    public void setSs1(Set<String> ss1) { this.ss1 = ss1; }
    public Set<String> getSs1() { return ss1; }
    @Override
    public boolean equals(Object o) {
        if(o == this) return true;
        if(!(o instanceof C1)) return false;
        C1 c1 = (C1)o;
        //if(s1)
        return true;
    }
    @Override
    public int hashCode() {
        return Objects.hash(s1,i1,as1,ss1);
    }
}

class C2 {
    String s1;
    Integer i1;
    List<String> as1;
    C1 c1;
    C1 c1_1;
    List<C1> ac1;
    public String getS1() { return s1;}
    public void setS1(String s1) { this.s1 = s1; }
    public Integer getI1() { return i1;}
    public void setI1(Integer i1) { this.i1 = i1; }
    public List<String> getAs1() { return as1; }
    public void setAs1(List<String> as1) { this.as1 = as1; }
    public void setC1(C1 c1) { this.c1 = c1; }
    public C1 getC1() { return c1; }
    public void setC1_1(C1 c1_1) { this.c1_1 = c1_1; }
    public C1 getC1_1() { return c1_1; }
    public void setAc1(List<C1> ac1) { this.ac1 = ac1; }
    public List<C1> getAc1() { return ac1; }
}

class C3 extends C1 {
    String s2;
    Integer i2;
    public String getS2() { return s2;}
    public void setS2(String s2) { this.s2 = s2; }
    public Integer getI2() { return i2;}
    public void setI2(Integer i2) { this.i2 = i2; }
}

class C4 {
    String s1;
    Integer i1;
    List<String> as1;
    Set<String> ss1;
    Integer i2;
    public String getS1() { return s1;}
    public void setS1(String s1) { this.s1 = s1; }
    public Integer getI1() { return i1;}
    public void setI1(Integer i1) { this.i1 = i1; }
    public void setInteger2(Integer i2) { this.i2 = i2; }
    public Integer getInteger2() { return i2; }
    public List<String> getAs1() { return as1; }
    public void setAs1(List<String> as1) { this.as1 = as1; }
    public void setSs1(Set<String> ss1) { this.ss1 = ss1; }
    public Set<String> getSs1() { return ss1; }
}

class C5 {
    String s;
    C3 c3;
    Map<String, C4> mapC4;

    public String getS() {
        return s;
    }

    public C3 getC3() {
        return c3;
    }

    public Map<String, C4> getMapC4() {
        return mapC4;
    }

    public void setS(String s) {
        this.s = s;
    }

    public void setC3(C3 c3) {
        this.c3 = c3;
    }

    public void setMapC4(Map<String, C4> mapC4) {
        this.mapC4 = mapC4;
    }
}

class SerializedPOJO {
    List<String> k104_0;
    List<String> k104_1;
    Map<String,Object> k104_5;
    public void setK104_0(List<String> k104_0) {
        this.k104_0 = k104_0;
    }
    public List<String> getK104_0() {
        return k104_0;
    }
    public void setK104_1(List<String> k104_1) {
        this.k104_1 = k104_1;
    }
    public List<String> getK104_1() {
        return k104_1;
    }
    public void setK104_5(Map<String,Object> k104_5) {
        this.k104_5 = k104_5;
    }
    public Map<String,Object> getK104_5() {
        return k104_5;
    }
}

//@JsonIgnoreProperties(ignoreUnknown = true)
class SerializedPOJOIgnorable {
    List<String> k104_0;
    List<String> k104_1;
    Map<String,Object> k104_5;
    public void setK104_0(List<String> k104_0) {
        this.k104_0 = k104_0;
    }
    public List<String> getK104_0() {
        return k104_0;
    }
    public void setK104_1(List<String> k104_1) {
        this.k104_1 = k104_1;
    }
    public List<String> getK104_1() {
        return k104_1;
    }
    public void setK104_5(Map<String,Object> k104_5) {
        this.k104_5 = k104_5;
    }
    public Map<String,Object> getK104_5() {
        return k104_5;
    }
}

class SerializedPOJO1 {
    List<String> list0;
    List<String> list1;
    public void setList0(List<String> list0) {
        this.list0 = list0;
    }
    public List<String> getList0() {
        return list0;
    }
    public void setList1(List<String> list1) {
        this.list1 = list1;
    }
    public List<String> getList1() {
        return list1;
    }
    // a POJO can have static methods
    public static boolean isPopulated(SerializedPOJO1 pojo) {
        return (pojo.getList0() != null || pojo.getList1() != null);
    }
    public static boolean isFullyPopulated(SerializedPOJO1 pojo) {
        return (pojo.getList0() != null && pojo.getList1() != null);
    }
}

//@JsonIgnoreProperties(ignoreUnknown = true)
class SerializedPOJO1Ignorable {
    List<String> list0;
    List<String> list1;
    public void setList0(List<String> list0) {
        this.list0 = list0;
    }
    public List<String> getList0() {
        return list0;
    }
    public void setList1(List<String> list1) {
        this.list1 = list1;
    }
    public List<String> getList1() {
        return list1;
    }
}