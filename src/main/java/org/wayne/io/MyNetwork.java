package org.wayne.io;

import org.wayne.main.MyBasic;
import org.wayne.misc.Utils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static java.nio.charset.StandardCharsets.UTF_8;

public class MyNetwork implements MyBasic {
    Set<ExecutorService> setExecutor = new HashSet<>();
    ServerSocket serverSocket = null;
    ExecutorService fixedThreadPool;
    Utils utils = new Utils();
    boolean closeClient = false;
    final int numThreads;
    int portnum;
    AtomicBoolean stop = new AtomicBoolean(false);

    public MyNetwork() { this(8888); }
    public MyNetwork(int portNum) {
        this(portNum, 4);
    }

    public MyNetwork(int portnum, int numThreads) {
        this.portnum = portnum;
        this.numThreads = numThreads;
        init();
    }

    public static void p(String f, Object ...o) {
        System.out.printf(f, o);
    }

    @Override
    public void shutdown() {
        setExecutor.stream().forEach(x -> shutdownExecutorService(x));
        cmdCloseSocket();
    }

    void shutdownExecutorService(ExecutorService x) {
        x.shutdown();
        try {
            int ctr = 0;
            while(!x.awaitTermination(2, TimeUnit.SECONDS)) {
                p("shutdown attempted\n");
                ctr++;
                if(ctr > 2) {
                    x.shutdownNow();
                    break;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    void cmdCloseSocket() {
        if(serverSocket == null) return;
        try {
            serverSocket.close();
            serverSocket = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init() {
        fixedThreadPool = Executors.newFixedThreadPool(numThreads,
            new ThreadFactory() {
                AtomicInteger ctr = new AtomicInteger(0);
                @Override public Thread newThread(Runnable r) {
                    return new Thread(r, String.format("group-1-thread-%02d", ctr.getAndIncrement()));
                }
            }
        );
        setExecutor.add(fixedThreadPool);
    }

    public void cmdSetCloseClient(boolean closeClient) {
        this.closeClient = closeClient;
    }

    public void cmdStartListenThreadPool() {
        if(serverSocket != null) return;
        try {
            p("starting server at port " + portnum + "\n");
            serverSocket = new ServerSocket(portnum);
            cmdListenThreadPool();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cmdListenThreadPool() {
        try {
            while(!stop.get()) {
                Socket socketClient = serverSocket.accept();
                p("new client accepted: %d\n", socketClient.getPort());
                setClient(socketClient);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setClient(Socket socket) throws SocketException {
        setClientNonScalable(socket);
    }

    private void setClientNonScalable(Socket socket) throws SocketException {
        // this method limits number of connections based on number of threads.
        // this does not scale. use nio to scale.
        // with nio, one thread can handle 2k-3k sockets
        socket.setSoTimeout(60);
        fixedThreadPool.submit(new WorkerRunnable(socket, closeClient));
    }
}

class WorkerRunnable implements Runnable {
    Socket socket;
    boolean closeConnection = true;
    Utils utils = new Utils();
    public WorkerRunnable(Socket socket, boolean closeConnection) {
        this.socket = socket;
        this.closeConnection = closeConnection;
    }

    public static void p(String f, Object ...o) {
        System.out.printf(f, o);
    }

    @Override
    public void run() {
        BufferedReader is = null;
        OutputStream os = null;
        try {
            is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            os = new BufferedOutputStream(socket.getOutputStream());
            // TODO: use a countime active timer to break, but this uses new thread
            while(true) {
                String iline = is.readLine();
                if(isCloseCmd(iline)) {
                    MyNetwork.p("isCloseCmd\n");
                    os.write("close command received\n".getBytes(UTF_8));
                    os.flush();
                    break;
                }
                NetworkCmd cmd = processInput(iline);
                if(processOutput(os, cmd)) {
                    os.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        MyNetwork.p("run closing down WorkerRunnable\n");
        if(is != null) {
            try { is.close(); } catch (IOException e) { e.printStackTrace(); }
            try { os.close(); } catch (IOException e) { e.printStackTrace(); }
        }
        if(closeConnection) {
            p("closing socket port:%d\n", socket.getPort());
            try { socket.close(); } catch (IOException e) { e.printStackTrace(); }
        }
    }

    public boolean isCloseCmd(String s) {
        if("q".equals(s) || "quit".equals(s)) return true;
        if(s == null || s.length() == 0) return false;
        return false;
    }

    public NetworkCmd processInput(String s) {
        if(s == null || s.length() == 0) return null;
        String res = String.format("socket process input:%s.\n", s);
        MyNetwork.p(res);
        return parseCmd(s);
    }

    public NetworkCmd parseCmd(String s) {
        if(s == null || s.length() == 0) return null;
        LinkedList<String> args = new LinkedList<>(Arrays.asList(s.trim().split("\\s+")));
        if(args.size() < 2) return null;
        String arg = args.poll();
        NETCMD cmd = NetworkCmd.cmd(arg);
        String arg1 = args.poll();
        String arg2 = args.poll();
        String arg3 = args.poll();
        return new NetworkCmd(cmd, arg1, arg2, arg3);
    }

    public boolean processOutput(OutputStream os, NetworkCmd cmd) throws IOException {
        if(cmd == null ||
            cmd.getCmd() == NETCMD.UNK ||
            cmd.getCmd() == NETCMD.ERR) {
            return false;
        }
        os.write(cmd.getMsg().getBytes(UTF_8));
        return true;
    }
}

enum NETCMD {
    GET,
    PUT,
    DEL,
    ERR,
    UNK,
    QUIT
}

class NetworkCmd {
    NETCMD cmd = NETCMD.UNK;
    String k;
    String v;
    String msg;
    public NetworkCmd(NETCMD cmd, String k, String v, String msg) {
        this.cmd = cmd;
        this.k = k;
        this.v = v;
        this.msg = msg;
    }
    public static NETCMD cmd(String s) {
        if(s == null || s.length() == 0) return NETCMD.UNK;
        switch(s) {
            case "get": return NETCMD.GET;
            case "put": return NETCMD.PUT;
            case "del": return NETCMD.DEL;
            default: return NETCMD.ERR;
        }
    }
    public NETCMD getCmd() { return cmd; }
    public String getK() { return k; }
    public String getV() { return v; }
    public String getMsg() { return msg; }
}

class MyNetworkHashMap extends MyNetwork implements MyBasic {
    ConcurrentHashMap<String, Object> mapObj = new ConcurrentHashMap<>();
    ConcurrentHashMap<String, String> mapStr = new ConcurrentHashMap<>();

    public MyNetworkHashMap() { this(8889); }
    public MyNetworkHashMap(int portnum) {
        this(portnum,4);
    }
    public MyNetworkHashMap(int portnum, int maxThreads) {
        super(portnum, maxThreads);
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void setClient(Socket socket) {
        fixedThreadPool.submit(new WorkerRunnableForHashMap(socket, closeClient, mapStr));
    }

}

class WorkerRunnableForHashMap extends WorkerRunnable {
    ConcurrentHashMap<String, String> map = null;

    public WorkerRunnableForHashMap(Socket socket, boolean closeConnection, ConcurrentHashMap<String, String> mapStr) {
        super(socket, closeConnection);
        this.map = mapStr;
    }

    @Override
    public NetworkCmd processInput(String s) {
        String res = String.format("MyNetworkHashMap processInput:%s.\n", s);
        MyNetworkHashMap.p(res);
        return parseCmd(s);
    }

    @Override
    public boolean processOutput(OutputStream os, NetworkCmd cmd) throws IOException {
        if(cmd == null) return false;
        switch(cmd.getCmd()) {
            case GET: return processGet(os, cmd);
            case PUT: return processPut(os, cmd);
        }
        return false;
    }

    private boolean processGet(OutputStream os, NetworkCmd cmd) throws IOException {
        p("processGet\n");
        try {
            Thread.sleep(utils.getInt(10,1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String v = map.get(cmd.getK());
        if(v != null) {
            os.write(v.getBytes(UTF_8));
            os.write("\n".getBytes(UTF_8));
        } else {
            os.write("null\n".getBytes(UTF_8));
        }
        return true;
    }

    private boolean processPut(OutputStream os, NetworkCmd cmd) throws IOException {
        p("processPut\n");
        try {
            Thread.sleep(utils.getInt(10,1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        map.put(cmd.getK(), cmd.getV());
        os.write("ok\n".getBytes(UTF_8));
        return true;
    }
}
