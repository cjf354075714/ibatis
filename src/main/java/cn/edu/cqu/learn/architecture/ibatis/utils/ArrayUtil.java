package cn.edu.cqu.learn.architecture.ibatis.utils;

import java.lang.reflect.Array;
import java.util.Arrays;

public class ArrayUtil {

    /**
     * 返回对象的 hashCode，本质上就是逻辑中，不同的类型将返回不同的 hashCode
     * @param object 参数可能是数组或者是 null，或者是其他类型
     * @return hashCode
     */
    public static int hashCode(Object object) {
        if ( null == object ) {
            return 0;
        }
        final Class<?> clazz = object.getClass();
        if (!clazz.isArray()) {
            return object.hashCode();
        }
        // 如果是数组，则 getComponentType 将返回数组中单个元素的类型
        // 如果根上不是数组，则会返回 null
        final Class<?> componentType = clazz.getComponentType();
        if ( long.class.equals(componentType) ) {
            return Arrays.hashCode( (long[]) object );
        } else if ( int.class.equals(componentType) ) {
            return Arrays.hashCode( (int[]) object );
        } else if ( short.class.equals(componentType) ) {
            return Arrays.hashCode( (short[]) object );
        } else if ( char.class.equals(componentType) ) {
            return Arrays.hashCode( (char[]) object );
        }
        return 0;
    }
}
