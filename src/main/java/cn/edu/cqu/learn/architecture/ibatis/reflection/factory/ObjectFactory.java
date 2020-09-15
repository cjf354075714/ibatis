package cn.edu.cqu.learn.architecture.ibatis.reflection.factory;

import java.util.List;
import java.util.Properties;

/**
 * Mybatis 中，用于构造业务对象的工厂，比较简单
 */
public interface ObjectFactory {

    /**
     * 设置工厂的属性，一般来说我们用不到
     * @param properties 属性
     */
    default void setProperties(Properties properties) {

    }

    /**
     * 根据传入的类型，创建一个业务对象
     * @param type 类型
     * @param <T> 模板类型
     * @return 业务对象
     */
    <T> T create(Class<T> type);

    /**
     * 根据传入的类型，找到合适的构造函数，然后去创建业务对象
     * @param type 类型
     * @param constructorArgTypes 业务对象构造函数所需要的参数的类型
     * @param constructorArgs 构造函数的参数
     * @param <T> 模板
     * @return 业务对象
     */
    <T> T create(Class<T> type, List<Class<?>> constructorArgTypes, List<Object> constructorArgs);

    /**
     * 返回当前业务对象是否是集合类型的子类
     * @param type 类型
     * @param <T> 模板方法
     * @return 是否是集合类型
     */
    <T> boolean isCollection(Class<T> type);

}
