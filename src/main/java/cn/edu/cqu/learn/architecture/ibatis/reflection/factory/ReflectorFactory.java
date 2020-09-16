package cn.edu.cqu.learn.architecture.ibatis.reflection.factory;

import cn.edu.cqu.learn.architecture.ibatis.reflection.Reflector;

/**
 * 现在我不知道这个反射工厂有什么用
 * 还是看实现吧
 * @see cn.edu.cqu.learn.architecture.ibatis.reflection.factory.impl.DefaultReflectorFactory
 *
 * 这个工厂，表示，去获取某个 Class 的包装对象 Reflector
 * 我们可以通过缓存的方式去记录下 Class 和 Reflector
 * @see Reflector
 */
public interface ReflectorFactory {

    /**
     * 是否支持缓存
     * @return 是否支持缓存
     */
    boolean isClassCacheEnabled();

    /**
     * 设置是否支持缓存
     * @param classCacheEnabled 缓存的标志位
     */
    void setClassCacheEnabled(boolean classCacheEnabled);

    /**
     * 通过 type 去找到自己的包装类 Reflector
     * @param type 类型
     * @return Reflector
     */
    Reflector findForClass(Class<?> type);
}
