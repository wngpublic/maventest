package org.wayne.io;

import org.wayne.main.MyBasic;
import org.wayne.misc.Utils;

import org.wayne.main.MyBasic;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.Pipe;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * This is a sandbox to demonstrate selector use.
 *
 * In non network case, have 4 producers and 4 consumers that pass through a single selector.
 * Each producer is a thread that has wait production times. Consumers have no wait. After
 * each of the producers is done with its transfer, deregister from selector, and register
 * with a new transfer task. The consumer threads have ByteBuffer. All of the transfer data
 * is eventually collected in a single Concurrent Deque that will be compared with all of
 * the data produced.
 *
 * In network case, do similar operation.
 */
public class MyNIO implements MyBasic {
    int poolSize = 5;

    ExecutorService fixedThreadPoolProducer = Executors.newFixedThreadPool(poolSize);
    ExecutorService fixedThreadPoolConsumer = Executors.newFixedThreadPool(poolSize);
    Utils utils = new Utils();

    public static void p(String f, Object ...o) {
        System.out.printf(f, o);
    }


    void shutdownExecutorService(ExecutorService x) {
        x.shutdown();
        try {
            shutdownLoop(x);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    void shutdownLoop(ExecutorService x) throws InterruptedException {
        int ctr = 0;
        while(!x.awaitTermination(2, TimeUnit.SECONDS)) {
            p("shutdown attempted\n");
            ctr++;
            if(ctr > 2) {
                x.shutdownNow();
                break;
            }
        }
    }

    @Override
    public void shutdown() {
        shutdownExecutorService(fixedThreadPoolProducer);
        shutdownExecutorService(fixedThreadPoolConsumer);
    }

    @Override
    public void init() {

    }

    public void testByteBuffer() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        IntStream.range(0,64).forEach(i -> baos.write(i));
        byte [] bap = baos.toByteArray();
        byte [] bac = new byte[bap.length];


        int szbb = 8;
        ByteBuffer byteBuffer = ByteBuffer.allocate(szbb);

        p("bap sz = %d\n", bap.length);
        p("bytebuffer alloc %d\n", szbb);

        int i = 0;
        for(int off = 0; i < 100 && off < 64; i++) {
            byteBuffer.clear(); // reset to 0 pos
            byteBuffer.put(bap, off, szbb);
            byteBuffer.flip();
            byteBuffer.get(bac, off, szbb);

            off += szbb;
            p("%2d ", off);
        }

        p("\n");
        p("numloops:%d\n", i);
        p("baproducer\n");
        for(i = 0; i < bap.length; i++) {
            p("%2d ", bap[i]);
        }
        p("\n");
        p("baconsumer\n");
        for(i = 0; i < bac.length; i++) {
            p("%2d ", bac[i]);
        }
        p("\n");
    }

    public void testChannel2Channel() throws IOException {
        String stringr = "fileFrom.txt";
        String stringw = "fileTo.txt";
        RandomAccessFile fileR = new RandomAccessFile(stringr, "r");
        FileChannel channelR = fileR.getChannel();
        {
            RandomAccessFile fileW = new RandomAccessFile(stringw, "w");
            FileChannel channelW = fileW.getChannel();
            channelW.transferFrom(channelR, channelR.position(), channelR.size());
        }
        {
            RandomAccessFile fileW = new RandomAccessFile(stringw, "w");
            FileChannel channelW = fileW.getChannel();
            channelR.transferTo(channelR.position(), channelR.size(), channelW);
        }
    }

    public void testByteBufferSourceSinkChannel() throws IOException {
        /*
         * you read from source channel and you write to sink channel.
         *
         * bb1 -> sink ->
         */
        List<Integer> producer = new ArrayList<>();
        List<Integer> consumer = new ArrayList<>();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        IntStream.range(0,64).forEach(i -> baos.write(i));

        ByteBuffer byteBuffer1 = ByteBuffer.allocate(baos.size());
        byteBuffer1.put(baos.toByteArray());

        ByteBuffer byteBuffer2 = ByteBuffer.allocate(8);
        ByteBuffer byteBuffer3 = ByteBuffer.allocate(16);

        Pipe pipe1 = Pipe.open();
        Pipe.SinkChannel channelSink1 = pipe1.sink();
        Pipe.SourceChannel channelSource1 = pipe1.source();

        Pipe pipe2 = Pipe.open();
        Pipe.SinkChannel channelSink2 = pipe1.sink();
        Pipe.SourceChannel channelSource2 = pipe1.source();

    }
}
