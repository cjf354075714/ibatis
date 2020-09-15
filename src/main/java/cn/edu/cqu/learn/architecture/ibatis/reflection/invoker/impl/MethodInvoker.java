package cn.edu.cqu.learn.architecture.ibatis.reflection.invoker.impl;

import cn.edu.cqu.learn.architecture.ibatis.reflection.Reflector;
import cn.edu.cqu.learn.architecture.ibatis.reflection.invoker.Invoker;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * 调用方法的反射实现，这是一个包装类，在 Invoker 接口下，有一些协议实现
 */
public final class MethodInvoker implements Invoker {

    private final Class<?> type;

    private final Method method;

    public MethodInvoker(Method method) {
        this.method = method;

        // 如果该方法只需要一个参数，则参数的类型就是 type
        // 如果不是，则 type 就是防范的返回结果
        if ( 1 == method.getParameterTypes().length ) {
            type = method.getParameterTypes()[0];
        } else {
            type = method.getReturnType();
        }
    }

    /**
     * 反射去调用一个方法，并返回调用的结果
     * @param target 是哪一个类去调用的反射
     * @param args 调用方法时，需要的参数
     * @return 调用的结果
     * @throws IllegalAccessException 权限不够
     * @throws InvocationTargetException 调用方法对象的异常
     */
    @Override
    public Object invoke(Object target, Object[] args) throws IllegalAccessException, InvocationTargetException {
        try {
            return method.invoke(target, args);
        } catch ( IllegalAccessException illegalAccessException) {
            if (Reflector.canControlMemberAccessible() ) {
                method.setAccessible(true);
                return method.invoke(target, args);
            } else {
                throw illegalAccessException;
            }
        }
    }

    @Override
    public Class<?> getType() {
        return type;
    }
}
