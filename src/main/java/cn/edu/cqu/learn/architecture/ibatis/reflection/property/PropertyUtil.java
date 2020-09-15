package cn.edu.cqu.learn.architecture.ibatis.reflection.property;

import cn.edu.cqu.learn.architecture.ibatis.reflection.Reflector;

import java.lang.reflect.Field;
import java.util.Locale;

public final class PropertyUtil {

    /**
     * 两个业务对象之间的属性复制
     * @param type 业务对象的类型
     * @param sourceBean 被复制的对象，也就是说，它的属性是设置好的
     * @param destinationBean 属性设置的对象，也就是它的属性是空的，是别人复制给它的
     */
    public static void copyBeanProperties(
            Class<?> type, Object sourceBean, Object destinationBean
    ) {
        // 首先指向自己
        Class<?> parent = type;
        while ( null != parent ) {
            // 拿到自己的属性数组
            final Field[] fields = parent.getDeclaredFields();
            for ( Field field : fields ) {
                try {
                    try {
                        // 这就是反射的厉害之处
                        // User user1 = new User();
                        // User user2 = new User();
                        // user1.setId("1");
                        // Field[] fields = User.class.getDeclaredField();
                        // 首先，这个 user1 和 user2 肯定是同一个 Class 的实例
                        // 那么 Field 的方法，对于两个 Object 的调用，只是返回结果不同，但是是可以调用的
                        // 所以，你看 filed.get(user1)，将返回 user1 的 id
                        field.set(destinationBean, field.get(sourceBean));
                    } catch ( IllegalAccessException illegalAccessException ) {
                        if ( Reflector.canControlMemberAccessible() ) {
                            field.setAccessible(true);
                            field.set(destinationBean, field.get(sourceBean));
                        } else {
                            throw illegalAccessException;
                        }
                    }
                } catch ( Exception e ) {
                    // 谁谁的属性，没有正确设置成功
                }
            }
            // 现在，当前类型的属性已经设置完毕了，那还有自己的父类型
            parent = parent.getSuperclass();
        }
    }

    public static String methodToProperty(String name) {
        if ( name.startsWith("is") ) {
            // 如果是 is 开头，就把 is 减去
            name = name.substring(2);
        } else if ( name.startsWith("get") || name.startsWith("set") ) {
            name = name.substring(3);
        } else {
            throw new RuntimeException("当前属性获取错误，没有以 is、get、set 开头");
        }
        // 传入的是 getId，返回的是 id，所以需要
        if (name.length() == 1 || (name.length() > 1 && !Character.isUpperCase(name.charAt(1)))) {
            name = name.substring(0, 1).toLowerCase(Locale.ENGLISH) + name.substring(1);
        }
        return name;
    }

    /**
     * 判断某个方法名是否代表了一个属性
     * @param name 方法名
     * @return 是否是一个属性
     */
    public static boolean isProperty(String name) {
        return isGetter(name) || isSetter(name);
    }

    /**
     * 判断一个方法名是否是 get 方法
     * @param name 方法名
     * @return 是否是 get 方法
     */
    public static boolean isGetter(String name) {
        return (name.startsWith("get") && name.length() > 3) || (name.startsWith("is") && name.length() > 2);
    }

    /**
     * 判断一个方法名是否是 set 方法
     * @param name 方法名
     * @return 是否是 set 方法
     */
    public static boolean isSetter(String name) {
        return name.startsWith("set") && name.length() > 3;
    }
}
