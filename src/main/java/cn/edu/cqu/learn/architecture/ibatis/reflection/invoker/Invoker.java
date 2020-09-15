package cn.edu.cqu.learn.architecture.ibatis.reflection.invoker;

import java.lang.reflect.InvocationTargetException;

/**
 * 不知道会干些什么，后面补上
 */
public interface Invoker {
    Object invoke(Object target, Object[] args) throws IllegalAccessException, InvocationTargetException;

    Class<?> getType();
}
