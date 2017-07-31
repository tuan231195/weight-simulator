package itree.core.weightsim.util;

import org.slf4j.LoggerFactory;

public class LoggerWrapperFactory
{
    public static LoggerWrapper getLogger(Class<?> clazz)
    {
        return new LoggerWrapper(LoggerFactory.getLogger(clazz));
    }

    public static LoggerWrapper getLogger(String loggerName)
    {
        return new LoggerWrapper(LoggerFactory.getLogger(loggerName));
    }
}
