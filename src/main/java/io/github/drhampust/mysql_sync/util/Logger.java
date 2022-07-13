package io.github.drhampust.mysql_sync.util;

import org.slf4j.LoggerFactory;

import static io.github.drhampust.mysql_sync.Main.LOGGER_CONFIG;

@SuppressWarnings("unused")
public class Logger {
    private static org.slf4j.Logger logger;
    private static String prefix;
    public Logger (String modID, String prefix){
        logger = LoggerFactory.getLogger(modID);
        Logger.prefix = prefix;
    }
    public Logger (String modID){
        this(modID, "");
    }

    public void trace(String message, Object variable1){
        if (LOGGER_CONFIG.trace)
            logger.trace(prefix + message, variable1);
    }

    public void trace(String message, Object variable1, Object variable2){
        if (LOGGER_CONFIG.trace)
            logger.trace(prefix + message, variable1, variable2);
    }

    public void trace(String message, Object... variables){
        if (LOGGER_CONFIG.trace)
            logger.trace(prefix + message, variables);
    }

    public void debug(String message, Object variable1){
        if (LOGGER_CONFIG.debug)
            logger.debug(prefix + message, variable1);
    }

    public void debug(String message, Object variable1, Object variable2){
        if (LOGGER_CONFIG.debug)
            logger.debug(prefix + message, variable1, variable2);
    }

    public void debug(String message, Object... variables){
        if (LOGGER_CONFIG.debug)
            logger.debug(prefix + message, variables);
    }

    public void info(String message, Object variable1){
        if (LOGGER_CONFIG.info)
            logger.info(prefix + message, variable1);
    }

    public void info(String message, Object variable1, Object variable2){
        if (LOGGER_CONFIG.info)
            logger.info(prefix + message, variable1, variable2);
    }

    public void info(String message, Object... variables){
        if (LOGGER_CONFIG.info)
            logger.info(prefix + message, variables);
    }

    public void warn(String message, Object variable1){
        if (LOGGER_CONFIG.warning)
            logger.warn(prefix + message, variable1);
    }

    public void warn(String message, Object variable1, Object variable2){
        if (LOGGER_CONFIG.warning)
            logger.warn(prefix + message, variable1, variable2);
    }

    public void warn(String message, Object... variables){
        if (LOGGER_CONFIG.warning)
            logger.warn(prefix + message, variables);
    }

    public void error(String message, Object variable1){
            logger.error(prefix + message, variable1);
    }

    public void error(String message, Object variable1, Object variable2){
        logger.error(prefix + message, variable1, variable2);
    }

    public void error(String message, Object... variables){
        logger.error(prefix + message, variables);
    }
}
