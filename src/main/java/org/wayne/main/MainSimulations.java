package org.wayne.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.wayne.io.logging.EnumLogLevel;
import org.wayne.io.logging.package1.LoggerCounterBase;
import org.wayne.io.logging.package1.subpackage1.LoggerCounterType1;
import org.wayne.io.logging.package1.subpackage1.LoggerCounterType2;
import org.wayne.io.logging.package1.subpackage2.LoggerCounterType3;
import org.wayne.io.logging.package1.subpackage2.LoggerCounterType4;
import org.wayne.io.logging.package1.subpackage2.LoggerCounterType5;

public class MainSimulations implements MyBasic {
    Logger logger = LogManager.getLogger(getClass().getEnclosingClass());

    @Override
    public void shutdown() {

    }

    @Override
    public void init() {

    }

    public void testLoggerCounterBaseDEBUG() {
        LoggerCounterBase logger = new LoggerCounterBase();
        try {
            logger.count(0,10, 100, EnumLogLevel.DEBUG);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void testLoggerCounterType1INFO() {
        LoggerCounterBase logger = new LoggerCounterType1();
        try {
            logger.count(0,10, 100, EnumLogLevel.DEBUG);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void testLoggerAppCounterType2INFO() {
        LoggerCounterBase logger = new LoggerCounterType2();
        try {
            logger.count(0,10, 100, EnumLogLevel.DEBUG);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void testLoggerAppCounterType3INFO() {
        LoggerCounterBase logger = new LoggerCounterType3();
        try {
            logger.count(0,10, 100, EnumLogLevel.DEBUG);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void testLoggerAppCounterType4INFO() {
        LoggerCounterBase logger = new LoggerCounterType4();
        try {
            logger.count(0,10, 100, EnumLogLevel.DEBUG);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void testLoggerAppCounterType5INFO() {
        LoggerCounterBase logger = new LoggerCounterType5();
        try {
            logger.count(0,10, 100, EnumLogLevel.DEBUG);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
