package cn.edu.cqu.learn.architecture.ibatis.reflection.factory.impl;

import cn.edu.cqu.learn.architecture.ibatis.reflection.Reflector;
import cn.edu.cqu.learn.architecture.ibatis.reflection.factory.ReflectorFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 默认的反射工厂，也就是说，通过 Class 对象，去获得 Reflector 对象
 * 当然，这些对象之间是有映射关系的，我们可以通过缓存来保存起来
 */
public class DefaultReflectorFactory implements ReflectorFactory {

    private boolean classCacheEnabled = true;

    private final ConcurrentMap<Class<?>, Reflector> reflectorConcurrentMap = new ConcurrentHashMap<>();

    public DefaultReflectorFactory() {

    }

    @Override
    public boolean isClassCacheEnabled() {
        return classCacheEnabled;
    }

    @Override
    public void setClassCacheEnabled(boolean classCacheEnabled) {
        this.classCacheEnabled = classCacheEnabled;
    }

    /**
     * 如果允许缓存，且缓存 Map 中是没有这个键值对的，那我们就去创建一个
     * 自然，多个线程同时调用同一个对象的 Reflector 的对象，有可能就会出现不必要的重复
     * 就需要使用线程安全的 Map
     * @param type 类型
     * @return Reflector
     */
    @Override
    public Reflector findForClass(Class<?> type) {
        if (classCacheEnabled) {
            return reflectorConcurrentMap.computeIfAbsent(type, Reflector::new);
        } else {
            return new Reflector(type);
        }
    }
}
