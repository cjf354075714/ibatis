package cn.edu.cqu.learn.architecture.ibatis.reflection.resolver;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

/**
 * 参数类型解析器
 * 这也太难了！
 * 本质上是 JAVA 中各种类型之间的转化，确实很难
 *
 *
 * 强大的 JAVA 类型解析器
 * 类型如何解析？有哪些类型？我们该怎么使用这些类型
 *
 * @see java.lang.reflect.Type 这个是 JAVA 中的所有类型的根接口
 * 我们通过这个接口，去访问到多种类型，它下面一共有五种类型，其中一个类，四个接口
 *
 * @see Class 这个就不用说了，就是一个具体的类型，不同的 Class 讲产生不同的类型
 *
 * @see java.lang.reflect.TypeVariable 类型变量，什么是类型变量
 * 就是说，你这个类型，是会变化的，比如 Class 它虽然是一个类型，但是它一旦指定，不可变化
 * 但是这个类型变量不同，我指定成 T，则它可以随机更改，所以类型变量就是指 T、K、V 等
 *
 * @see java.lang.reflect.ParameterizedType 参数类型，那既然你都带有了类型变量
 * 那就叫你参数化类型吧，那比如 Map<K, V>、List<T> 这种，就叫参数化类型，参数化类型
 * 有一些方法可以拿到我们的参数，这些具体的细节，请看 Extend.md
 * 那么，Map<String, Object> 这种，它的类型是参数化类型，还是 Class 呢？
 * 测试出来，发现还是参数化类型，这个是没问题的
 *
 * @see java.lang.reflect.WildcardType 表示那些有 ？ 的类型
 * 就比如，List<T> 它是一个参数化类型，那么 List<? extends Inpustream> 它就是一个通配符类型
 *
 * @see java.lang.reflect.GenericArrayType 表示参数类型或者类型变量的一个数组
 * 比如 T[] List<T>[] 等等，注意，它有一些方法，可以拿到数组中的变量类型
 *
 */
public final class TypeParameterResolver {

    private static Type resolveTypeVar(
            TypeVariable<?> typeVar, Type srcType, Class<?> declaringClass
    ) {
        Type result;
        Class<?> clazz;
        // 接下来的判断逻辑，就是根据 srcType 的类型，去给 clazz 附上不同的值
        // 如果 srcType 就是一个 Class，那么 clazz 就是一个 Class
        // 如果 srcType 是一个参数化类型，则 clazz 就是这个参数化类型的最完成包装，就是 List，Map 等
        if ( srcType instanceof Class ) {
            clazz = (Class<?>) srcType;
        } else if ( srcType instanceof ParameterizedType ) {
            ParameterizedType parameterizedType = (ParameterizedType) srcType;
            clazz = (Class<?>) parameterizedType.getRawType();
        } else {
            // 如果你不是 Class 类型，也不是参数化类型，那我就直接报错
            // 为啥，因为我就是不处理其他类型
            throw new RuntimeException("错误的 srcType 类型");
        }

        // 如果我算出来的结果，直接等于了你定义的类型
        // 这一段逻辑我没有明白，真不明白
        if ( clazz == declaringClass ) {
            Type[] bounds = typeVar.getBounds();
            if ( bounds.length > 0 ) {
                return bounds[0];
            }
            return Object.class;
        }
    }
}
