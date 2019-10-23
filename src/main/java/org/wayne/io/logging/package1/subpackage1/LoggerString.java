package org.wayne.io.logging.package1.subpackage1;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggerString {
    Logger logger = LogManager.getLogger(getClass().getEnclosingClass());
}
