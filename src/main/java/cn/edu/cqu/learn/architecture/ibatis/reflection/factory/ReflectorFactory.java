package cn.edu.cqu.learn.architecture.ibatis.reflection.factory;

import cn.edu.cqu.learn.architecture.ibatis.reflection.Reflector;

public interface ReflectorFactory {
    boolean isClassCacheEnabled();

    void setClassCacheEnabled(boolean classCacheEnabled);

    Reflector findForClass(Class<?> type);
}
