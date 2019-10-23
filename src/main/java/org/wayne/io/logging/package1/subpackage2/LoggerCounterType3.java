package org.wayne.io.logging.package1.subpackage2;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.wayne.io.logging.EnumLogLevel;
import org.wayne.io.logging.package1.LoggerCounterBase;

public class LoggerCounterType3 extends LoggerCounterBase {
    Logger logger = LogManager.getLogger(getClass().getEnclosingClass());
    Logger logger3 = LogManager.getLogger("loggertype3");

    @Override
    public void count(int beg, int end, long intervalMillis, EnumLogLevel enumLogLevel) throws InterruptedException {
        super.count(beg, end, intervalMillis, enumLogLevel);
        logger.debug(String.format("id:%2d count: %d %d %d %s", id(), beg, end, intervalMillis, enumLogLevel.name()));
        logger.warn(String.format("id:%2d count: %d %d %d %s", id(), beg, end, intervalMillis, enumLogLevel.name()));
        logger.error(String.format("id:%2d count: %d %d %d %s", id(), beg, end, intervalMillis, enumLogLevel.name()));
        logger3.debug(String.format("id:%2d count: %d %d %d %s", id(), beg, end, intervalMillis, enumLogLevel.name()));
        logger3.warn(String.format("id:%2d count: %d %d %d %s", id(), beg, end, intervalMillis, enumLogLevel.name()));
        logger3.error(String.format("id:%2d count: %d %d %d %s", id(), beg, end, intervalMillis, enumLogLevel.name()));
    }
}
