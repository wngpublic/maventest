package org.wayne.io.logging.package1;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.wayne.io.logging.EnumLogLevel;
import org.wayne.main.MyBasic;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class LoggerCounterBase implements MyBasic {
    Logger logger = LogManager.getLogger(getClass().getEnclosingClass());
    public static final AtomicInteger idIncrementer = new AtomicInteger();
    int id;
    boolean stop = false;

    public LoggerCounterBase() {
        id = idIncrementer.getAndIncrement();
    }

    @Override
    public void shutdown() {

    }

    @Override
    public void init() {

    }

    public void stop() {
        stop = true;
    }

    protected int id() {
        return id;
    }
    public void count(int beg, int end, long intervalMillis, EnumLogLevel enumLogLevel) throws InterruptedException {

        for(int i = beg; i < end; i++) {
            String msg = String.format("LoggerCounterBase ID:%2d count:%10d", id, i);
            log(msg, enumLogLevel);

            //logAllLevels(msg);
            //if(intervalMillis > 0) Thread.currentThread().sleep(intervalMillis);

            if(intervalMillis > 0) TimeUnit.MILLISECONDS.sleep(intervalMillis);
            if(stop) break;
        }

        String msg = String.format("LoggerCounterBase ID:%2d done", id);
        if(intervalMillis > 0) TimeUnit.MILLISECONDS.sleep(intervalMillis);
    }

    public void logAllLevels(String msg) {
        logger.log(Level.ALL,   String.format("%s: %s", Level.ALL.name(),   msg));
        logger.log(Level.INFO,  String.format("%s: %s", Level.INFO.name(),  msg));
        logger.log(Level.WARN,  String.format("%s: %s", Level.WARN.name(),  msg));
        logger.log(Level.DEBUG, String.format("%s: %s", Level.DEBUG.name(), msg));
        logger.log(Level.ERROR, String.format("%s: %s", Level.ERROR.name(), msg));
        logger.log(Level.FATAL, String.format("%s: %s", Level.FATAL.name(), msg));
        logger.log(Level.TRACE, String.format("%s: %s", Level.TRACE.name(), msg));
        logger.info(String.format("%s: %s", msg));
        logger.warn(String.format("%s: %s", msg));
        logger.debug(String.format("%s: %s", msg));
        logger.error(String.format("%s: %s", msg));
        logger.fatal(String.format("%s: %s", msg));
        logger.trace(String.format("%s: %s", msg));
    }

    void logLevelAndExplicit(String msg, EnumLogLevel enumLogLevel) {
        String msgFinal = String.format("%s: %s", enumLogLevel.name(), msg);
        switch(enumLogLevel) {
            case ALL:   logger.log(Level.ALL, msgFinal);
                break;
            case INFO:  logger.info(msgFinal);
                break;
            case WARN:  logger.warn(msgFinal);
                break;
            case DEBUG: logger.debug(msgFinal);
                break;
            case ERROR: logger.error(msgFinal);
                break;
            case FATAL: logger.fatal(msgFinal);
                break;
            case TRACE: logger.trace(msgFinal);
                break;
            case OFF:
            default:
                break;
        }
    }

    void log(String msg, EnumLogLevel enumLogLevel) {
        String msgFinal = String.format("%s: %s", enumLogLevel.name(), msg);
        switch(enumLogLevel) {
            case ALL:   logger.log(Level.ALL, msgFinal); break;
            case INFO:  logger.info(msgFinal); break;
            case WARN:  logger.warn(msgFinal); break;
            case DEBUG: logger.debug(msgFinal); break;
            case ERROR: logger.error(msgFinal); break;
            case FATAL: logger.fatal(msgFinal); break;
            case TRACE: logger.trace(msgFinal); break;
            case OFF:
            default:
                break;
        }
    }
}
