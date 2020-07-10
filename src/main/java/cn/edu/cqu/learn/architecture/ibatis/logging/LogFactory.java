package cn.edu.cqu.learn.architecture.ibatis.logging;

import java.lang.reflect.Constructor;

public class LogFactory {

    public static final String MARKER = "MYBATIS";

    private static Constructor<? extends Log> logConstructor;

    static {

    }

    private LogFactory() {
    }


}
