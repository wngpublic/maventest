package org.wayne.misc.graphs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;

/**
 * Tries are good for looking up all sets that match prefix.
 * 
 * This applies to predicting word completion, based on previously
 * existing words.
 *
 */
public class Tries {

    TrieNode root = new TrieNode(null);
    
    int numWords = 0;
    
    boolean dbg = true;
    
    boolean compressed = true;
    
    public Tries() {
    }
    
    void p(String f, Object ...o) {
        System.out.printf(f, o);
    }
    
    public void print() {
        printPrefixSet(null);
    }
    
    public void add(String s) {
        TrieNode n = root;
        for(int i = 0; i < s.length(); i++) {
            Character c = s.charAt(i);
            n = n.addChild(c);
        }
        numWords++;
        if(dbg) {
            p("%5d: add string %s\n", numWords, s);
        }
    }
    
    /**
     * loadWordsFromFile:
     * file has newline separated words.
     * file can be gzip or regular text file.
     */
    public void loadWordsFromFile(String filename) {
        
        File file = new File(filename);

        if(!file.exists()) {
            p("file %s does not exist\n", filename);
            return;
        }
        
        boolean isZipped = loadWordsFromGzip(filename);
        
        if(!isZipped) {
            BufferedReader br = null;
            try {
                br = new BufferedReader(new FileReader(file));
                String line;
                while((line = br.readLine()) != null) {
                    String s = line.trim();
                    add(s);
                }
            } catch(IOException e) {
                e.printStackTrace();
            } finally {
                if(br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    
    private boolean loadWordsFromGzip(String filename) {
        RandomAccessFile raf = null;
        boolean isZipped = false;
        File file = new File(filename);
        Path src = Paths.get(filename);
        try {
            String type = Files.probeContentType(src);
            p("Type:%s\n", type);
            raf = new RandomAccessFile(filename, "r");
            int magic = raf.read() & 0xff | ((raf.read() << 8) & 0xff00);
            if(magic == GZIPInputStream.GZIP_MAGIC) {
                isZipped = true;
            }
            raf.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(raf != null) {
                try {
                    raf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
        if(isZipped) {
            List<String> list = new ArrayList<>();
            StringBuilder sb = new StringBuilder();
            GZIPInputStream gis = null;
            try {
                gis = new GZIPInputStream(new FileInputStream(file));
                byte [] buf = new byte[16];
                int len = 0;
                
                while((len = gis.read(buf)) > 0) {
                    for(int i = 0; i < len; i++) {
                        byte b = buf[i];
                        if(b == '\n') {
                            String s = sb.toString();
                            list.add(s);
                            sb.delete(0, sb.length());
                        } else {
                            char c = (char)b;
                            sb.append(c);
                        }
                    }
                }
                if(sb.length() != 0) {
                    String s = sb.toString();
                    list.add(s);
                }
                for(String s: list) {
                    add(s);
                }
            } catch(IOException e) {
                
            } finally {
                if(gis != null) {
                    try {
                        gis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        
        return isZipped;
    }
    
    public void loadWords(List<String> list) {
        for(String s: list) {
            add(s);
        }
    }
    
    public void delete(String s) {
        
    }

    public void printPrefixSet(String prefix) {
        List<String> list = getPrefixList(prefix);
        for(int i = 0; i < list.size(); i++) {
            p("%5d: %s\n", (i+1), list.get(i));
        }
    }
    
    
    public List<String> getPrefixList(String s) {
        Set<String> set = getPrefixSet(s);
        List<String> list = new ArrayList<>(set);
        Collections.sort(list);
        return list;
    }
    
    public Set<String> getPrefixSet(String s) {
        Set<String> set = new HashSet<>();
        TrieNode n = root;
        
        if(s != null) {
            for(int i = 0; i < s.length(); i++) {
                Character c = s.charAt(i);
                n = n.getNode(c);
                if(n == null) {
                    return set;
                }
            }
        }
        
        Map<Character, TrieNode> map = n.getMap();
        String sNew = (s == null || s.length() == 0) ? "" : s;
        constructPrefixSet(sNew, map, set);
        return set;
    }
    
    void constructPrefixSet(String s, Map<Character, TrieNode> map, Set<String> set) {
        for(Map.Entry<Character, TrieNode> kv: map.entrySet()) {
            String sNew = s + kv.getKey();
            TrieNode n = kv.getValue();
            Map<Character, TrieNode> mapNew = n.getMap();
            if(mapNew.size() == 0) {
                set.add(sNew);
            }
            else {
                constructPrefixSet(sNew, mapNew, set);
            }
        }
    }
    
    public boolean exists(String s) {
        TrieNode n = root;
        for(int i = 0; i < s.length(); i++) {
            Character c = s.charAt(i);
            n = n.getNode(c);
            if(n == null) {
                return false;
            }
        }
        return true;
    }
}

// cannot have Node in this file and Node in another file.
//class Node {
class TrieNode {

    Character c = null;
    String prefix = null;
    
    Map<Character, TrieNode> map = new HashMap<>();
    
    public TrieNode() {
    }
    
    public TrieNode(Character c) {
        this.c = c;
    }
    
    public void print() {
        print(null);
    }
    
    public void print(String msg) {
        printVal(msg);
        printChildren(msg + "    ");
    }
    
    public void printChildren(String msg) {
        for(Map.Entry<Character, TrieNode> kv: map.entrySet()) {
            Character c = kv.getKey();
            
            if(msg != null) {
                System.out.printf("%s:%s\n", msg, c);
            }
            else {
                System.out.printf("%s\n", c);
            }
        }
    }
    
    public void printVal() {
        printVal(null);
    }

    public void printVal(String msg) {
        if(msg != null) {
            System.out.printf("%s:%s\n", msg, c);
        }
        else if(c == null) {
            System.out.printf("%s\n", c);
        }
        else {
            System.out.printf("%s\n", c);
        }
    }
    
    public Character val() {
        return c;
    }
    
    public TrieNode getNode(Character c) {
        return map.get(c);
    }
    
    public void addChild(Character c, TrieNode n) {
        if(map.get(c) != null) {
            return;
        }
        map.put(c, n);
    }
    
    public TrieNode addChild(Character c) {
        if(map.get(c) != null) {
            return map.get(c);
        }
        
        TrieNode n = new TrieNode(c);
        
        map.put(c, n);
        
        return n;
    }
    
    public boolean deleteChild(Character c) {
        if(map.get(c) == null) {
            return false;
        }
        
        map.remove(c);
        
        return true;
    }
    
    /**
     * getMap returns a map of Character and TrieNode.
     * @return
     */
    public Map<Character, TrieNode> getMap() {
        return Collections.unmodifiableMap(map);
    }
}