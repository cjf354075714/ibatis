package cn.edu.cqu.learn.architecture.ibatis.reflection.factory.impl;

import cn.edu.cqu.learn.architecture.ibatis.reflection.Reflector;
import cn.edu.cqu.learn.architecture.ibatis.reflection.factory.ObjectFactory;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.*;

/**
 * 默认的业务对象构造工厂
 * 注意，如果传入一个简单的集合接口，我需要去找到该接口的实现类，因为接口是不能去实例化的
 */
public class DefaultObjectFactory implements ObjectFactory, Serializable {

    private static final long serialVersionUID = -8855120656740914948L;

    @SuppressWarnings("unchecked")
    @Override
    public <T> T create(Class<T> type) {
        Class<T> impClass = (Class<T>) resolveInterface(type);
        try {
            Constructor<T> constructor = impClass.getDeclaredConstructor();
            try {
                return (T) constructor.newInstance();
                // 如果是访问异常，则去设置好构造函数的权限，前提是去判断，能不能获取到成员变量的访问
            } catch ( IllegalAccessException illegalAccessException ) {
                // 安全系统允许你去访问 Class 的方法或这变量
                if ( Reflector.canControlMemberAccessible() ) {
                    constructor.setAccessible(true);
                    return (T) constructor.newInstance();
                } else {
                    throw illegalAccessException;
                }
            }
        } catch (Exception e) {
            // 打印出异常
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T create(Class<T> type, List<Class<?>> constructorArgTypes, List<Object> constructorArgs) {
        // 首先去找到拥有当前参数的构造函数
        // 我们这个函数，就是去将接口转化为具体的实例的，所以 Class<?> 转化为 Class<T> 是可以的
        Class<T> impClass = (Class<T>) resolveInterface(type);
        try {
            Constructor<T> constructor = impClass.getDeclaredConstructor(constructorArgTypes.toArray(new Class[0]));
            Object[] args = constructorArgs.toArray(new Object[0]);
            try {
                return (T) constructor.newInstance(args);
                // 如果是访问异常，则去设置好构造函数的权限，前提是去判断，能不能获取到成员变量的访问
            } catch ( IllegalAccessException illegalAccessException ) {
                // 安全系统允许你去访问 Class 的方法或这变量
                if ( Reflector.canControlMemberAccessible() ) {
                    constructor.setAccessible(true);
                    return (T) constructor.newInstance(args);
                } else {
                    throw illegalAccessException;
                }
            }
        } catch (Exception e) {
            // 打印出异常
        }
        return null;
    }

    /**
     * 如果 type 能够转化为 Collection 对象，则表示传入的 type 是一个 Collection 接口的实例
     * @param type 类型
     * @param <T> 模板
     * @return 是否
     */
    @Override
    public <T> boolean isCollection(Class<T> type) {
        return Collection.class.isAssignableFrom(type);
    }

    private Class<?> resolveInterface(Class<?> interfaceType) {
        Class<?> result = null;
        if ( List.class == interfaceType || Collection.class == interfaceType || Iterable.class == interfaceType ) {
            result = LinkedList.class;
        } else if ( Map.class == interfaceType ) {
            result = HashMap.class;
            // 如果是排序集合，则返回 TreeSet 对象类，显然它要写在 Set 前面，不然，就返回 HashSet 了
        } else if ( SortedSet.class == interfaceType ) {
            result = TreeSet.class;
        } else if ( Set.class == interfaceType ) {
            result = HashSet.class;
        } else {
            // 如果不是接口，则该是什么就是什么
            result = interfaceType;
        }
        return result;
    }
}
