package org.wayne.io.logging.package1.subpackage2;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.wayne.io.logging.EnumLogLevel;
import org.wayne.io.logging.package1.LoggerCounterBase;

public class LoggerCounterType4 extends LoggerCounterBase {
    Logger logger = LogManager.getLogger(getClass().getEnclosingClass());
    Logger logger4 = LogManager.getLogger("loggertype4");

    @Override
    public void count(int beg, int end, long intervalMillis, EnumLogLevel enumLogLevel) throws InterruptedException {
        super.count(beg, end, intervalMillis, enumLogLevel);
        logger.debug(String.format("id:%2d count: %d %d %d %s", id(), beg, end, intervalMillis, enumLogLevel.name()));
        logger4.debug(String.format("id:%2d count: %d %d %d %s", id(), beg, end, intervalMillis, enumLogLevel.name()));
    }
}
