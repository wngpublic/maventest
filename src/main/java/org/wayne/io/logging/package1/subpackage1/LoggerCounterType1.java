package org.wayne.io.logging.package1.subpackage1;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.wayne.io.logging.EnumLogLevel;
import org.wayne.io.logging.package1.LoggerCounterBase;

public class LoggerCounterType1 extends LoggerCounterBase {
    Logger logger1 = LogManager.getLogger("loggertype1");

    @Override
    public void count(int beg, int end, long intervalMillis, EnumLogLevel enumLogLevel) throws InterruptedException {
        super.count(beg, end, intervalMillis, enumLogLevel);
        logger1.debug(String.format("id:%2d count: %d %d %d %s", id(), beg, end, intervalMillis, enumLogLevel.name()));
    }
}