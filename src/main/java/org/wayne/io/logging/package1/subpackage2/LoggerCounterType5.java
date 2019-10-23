package org.wayne.io.logging.package1.subpackage2;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.wayne.io.logging.EnumLogLevel;
import org.wayne.io.logging.package1.LoggerCounterBase;

import java.util.concurrent.TimeUnit;

public class LoggerCounterType5 extends LoggerCounterBase {
    Logger logger = LogManager.getLogger(getClass().getEnclosingClass());
    Logger logger5 = LogManager.getLogger("loggertype5");

    @Override
    public void count(int beg, int end, long intervalMillis, EnumLogLevel enumLogLevel) throws InterruptedException {
        for(int i = beg; i < end; i++) {
            String msg = String.format("LoggerCounterBase ID:%2d count:%10d", id(), i);
            log(msg, enumLogLevel);

            //logAllLevels(msg);
            //if(intervalMillis > 0) Thread.currentThread().sleep(intervalMillis);

            if(intervalMillis > 0) TimeUnit.MILLISECONDS.sleep(intervalMillis);
        }

        String msg = String.format("LoggerCounterBase ID:%2d done", id());
        if(intervalMillis > 0) TimeUnit.MILLISECONDS.sleep(intervalMillis);


        super.count(beg, end, intervalMillis, enumLogLevel);
        logger5.debug(String.format("id:%2d count: %d %d %d %s", id(), beg, end, intervalMillis, enumLogLevel.name()));
        logger5.warn(String.format("id:%2d count: %d %d %d %s", id(), beg, end, intervalMillis, enumLogLevel.name()));
    }
    private void log(String msg, EnumLogLevel enumLogLevel) {
        String msgFinal = String.format("%s: %s", enumLogLevel.name(), msg);
        switch(enumLogLevel) {
            case ALL:   logger5.log(Level.ALL, msgFinal); break;
            case INFO:  logger5.info(msgFinal); break;
            case WARN:  logger5.warn(msgFinal); break;
            case DEBUG: logger5.debug(msgFinal); break;
            case ERROR: logger5.error(msgFinal); break;
            case FATAL: logger5.fatal(msgFinal); break;
            case TRACE: logger5.trace(msgFinal); break;
            case OFF:
            default:
                break;
        }
    }
}
