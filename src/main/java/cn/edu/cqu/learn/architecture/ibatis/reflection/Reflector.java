package cn.edu.cqu.learn.architecture.ibatis.reflection;

import cn.edu.cqu.learn.architecture.ibatis.reflection.invoker.Invoker;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public final class Reflector {

    private final Class<?> type;

    private final String[] readablePropertyNames = null;

    private final String[] writablePropertyNames = null;

    private final Map<String, Invoker> setMethods = new HashMap<>();

    private final Map<String, Invoker> getMethods = new HashMap<>();

    private final Map<String, Class<?>> setTypes = new HashMap<>();

    private final Map<String, Class<?>> getTypes = new HashMap<>();

    private Constructor<?> defaultConstructor;

    private Map<String, String> caseInsensitivePropertyMap = new HashMap<>();

    /**
     * 构造函数，传入类型
     * @param type 类型
     */
    public Reflector(Class<?> type) {
        // 记录当前类型
        this.type = type;
        // 获取当前对象的默认构造函数
        this.addDefaultConstructor(type);
    }

    /**
     * 获取传入类型的构造函数参数个数为 0 的构造方法
     * @param type 传入类型
     */
    private void addDefaultConstructor(Class<?> type) {
        Constructor<?>[] constructors = type.getConstructors();
        for ( Constructor<?> index : constructors ) {
            if ( 0 == index.getParameterCount() ) {
                this.defaultConstructor = index;
                return ;
            }
        }
        this.defaultConstructor = null;
    }

    private void addGetMethods(Class<?> type) {

    }


    public static boolean canControlMemberAccessible() {
        return false;
    }
}
