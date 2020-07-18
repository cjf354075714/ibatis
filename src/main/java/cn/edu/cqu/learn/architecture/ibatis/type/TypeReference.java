package cn.edu.cqu.learn.architecture.ibatis.type;


import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 引用类型的类型处理器
 * 这个类很难，还要继续学习
 * @param <T> 应用的模板
 */
public abstract class TypeReference<T> {

    private final Type rawType;

    protected TypeReference() {
        rawType = getSuperclassTypeParameter(getClass());
    }

    // 这个方法真的很难
    // 但是我知道，它返回包装类的第一次层级类型
    Type getSuperclassTypeParameter(Class<?> clazz) {
        // 拿到这个类的直接带模板类型
        Type genericSuperClass = clazz.getGenericSuperclass();
        if (genericSuperClass instanceof Class) {
            // 如果是当前这个类的实际类
            if (TypeReference.class != genericSuperClass) {
                // 就继续迭代，这个继续迭代是什么意思啊
                return getSuperclassTypeParameter(clazz.getSuperclass());
            }
            // 如果当前确实是一个类，但是却不是模板引用类型，则一定是错误的
            throw new RuntimeException(getClass() + "当前类虽然是带模板的引用类型，但是缺失了类型参数");
        }

        // 我既然是参数类型，我就自我降级一等，变成参数类型
        ParameterizedType parameterizedType = (ParameterizedType) genericSuperClass;


        // 参数类型拿到自己的模板参数数组，比如 Map<String, Object> 那拿到的就是
        // String, Object 的数组
        Type[] realParameterType = parameterizedType.getActualTypeArguments();

        // 我去拿到第一个，为什么要拿到第一个
        Type result = realParameterType[0];

        // 拿到定义的类型
        if (result instanceof ParameterizedType) {
            result = ((ParameterizedType) result).getRawType();
        }
        return result;
    }

    public final Type getRawType() {
        return rawType;
    }

    @Override
    public String toString() {
        return rawType.toString();
    }
}
