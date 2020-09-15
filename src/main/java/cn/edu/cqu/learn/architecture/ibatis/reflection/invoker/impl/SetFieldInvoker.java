package cn.edu.cqu.learn.architecture.ibatis.reflection.invoker.impl;

import cn.edu.cqu.learn.architecture.ibatis.reflection.Reflector;
import cn.edu.cqu.learn.architecture.ibatis.reflection.invoker.Invoker;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * 业务对象属性的 set 方法反射对象
 */
public final class SetFieldInvoker implements Invoker {

    private final Field field;

    public SetFieldInvoker(Field field) {
        this.field = field;
    }

    /**
     * set 属性的方法回调，原来还可以这样
     * @param target 是哪一个 Object 对象去调用这个 set
     * @param args 设置的参数，当然，这个参数一定是大小唯一的数组
     * @return set 方法是没有返回值的
     * @throws IllegalAccessException 访问权限异常
     * @throws InvocationTargetException 目标异常
     */
    @Override
    public Object invoke(Object target, Object[] args) throws IllegalAccessException, InvocationTargetException {
        try {
            field.set(target, args[0]);
        } catch ( IllegalAccessException illegalAccessException ) {
            if ( Reflector.canControlMemberAccessible() ) {
                field.setAccessible(true);
                field.set(target, args[0]);
            } else {
                throw illegalAccessException;
            }
        }
        return null;
    }

    @Override
    public Class<?> getType() {
        return field.getType();
    }
}
