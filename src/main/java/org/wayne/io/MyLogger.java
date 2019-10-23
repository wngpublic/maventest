package org.wayne.io;

import org.wayne.main.MyBasic;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class MyLogger implements MyBasic {
    @Override
    public void shutdown() {
        if(fos == null) return;
        try {
            debug = false;
            p("closing MyLogger bw\n");
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            bw = null;
        }
        try {
            p("closing MyLogger fos\n");
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            fos = null;
        }
    }

    @Override
    public void init() {
        try {
            if(fos != null) return;
            p("open MyLogger fos, bw\n");
            fos = new FileOutputStream(filename,true);
            bw = new BufferedWriter(new PrintWriter(new OutputStreamWriter(fos)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private boolean debug = false;
    private int level = 0;
    private FileOutputStream fos = null;
    private BufferedWriter bw = null;
    private static final String filename = "debug.log";
    public static final MyLogger instance = new MyLogger();

    private MyLogger() {
        p("MyLogger constructor\n");
        init();
    }

    public void testSetDebug() { debug = true; }
    public void testUnsetDebug() { debug = false; }
    public void setLevel(int level) { this.level = level; }
    public void p(String f, Object ...o) {
        String s = String.format(f,o);
        System.out.printf(s);
        if(debug) {
            try {
                //System.out.println("debug write to file");
                bw.write(s);
                bw.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            //System.out.println("debug write off");
        }
    }
}
