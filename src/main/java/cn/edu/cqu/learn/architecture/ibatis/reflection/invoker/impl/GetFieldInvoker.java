package cn.edu.cqu.learn.architecture.ibatis.reflection.invoker.impl;

import cn.edu.cqu.learn.architecture.ibatis.reflection.Reflector;
import cn.edu.cqu.learn.architecture.ibatis.reflection.invoker.Invoker;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * 一个对象有属性，比如 Student 类，有一个 String 类型的 id
 * 通过反射，我们可以拿到这个id
 * 那就是：
 * field.get(student) = Object id
 *
 */
public final class GetFieldInvoker implements Invoker {

    private final Field field;

    public GetFieldInvoker(Field field) {
        this.field = field;
    }

    @Override
    public Object invoke(Object target, Object[] args) throws IllegalAccessException, InvocationTargetException {
        try {
            return field.get(target);
        } catch ( IllegalAccessException illegalAccessException ) {
            if ( Reflector.canControlMemberAccessible() ) {
                field.setAccessible(true);
                return field.get(target);
            } else {
                throw illegalAccessException;
            }
        }
    }

    /**
     * 拿到字段的类型
     * @return 字段类型
     */
    @Override
    public Class<?> getType() {
        return field.getType();
    }
}
