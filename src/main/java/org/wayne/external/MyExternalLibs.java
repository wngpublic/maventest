package org.wayne.external;

import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.core.json.JsonFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.NumericNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import java.io.IOException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;
import org.apache.commons.io.IOUtils;
import org.wayne.main.MyBasic;
import org.wayne.misc.Utils;
//import org.json.JSONObject;

class JSONObject1 {
    String s1;
    String s2;
    Integer i1;
    Integer i2;
    public void setS1(String s1) { this.s1 = s1; }
    public void setS2(String s2) { this.s2 = s2; }
    public void setI1(Integer i1) { this.i1 = i1; }
    public void setI2(Integer i2) { this.i2 = i2; }
    public String getS1() { return s1; }
    public String getS2() { return s2; }
    public Integer getI1() { return i1; }
    public Integer getI2() { return i2; }

    @Override
    public String toString() {
        return "JSONObject1{" +
            "s1='" + s1 + '\'' +
            ", s2='" + s2 + '\'' +
            ", i1=" + i1 +
            ", i2=" + i2 +
            '}';
    }
}
