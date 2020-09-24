package cn.edu.cqu.learn.architecture.ibatis.utils;



import java.lang.reflect.Method;
import java.sql.Ref;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 这个对象，用于反射的一些操作
 * 我将列出一些情况
 */
public final class ReflectorUtil {

    /**
     * 获取当前类型下的所有方法，包括抽象方法，或者接口方法？
     * @param type 类型
     * @return 方法数组
     */
    public static Method[] getAllMethod(Class<?> type) {
        return null;
    }

    /**
     * 获取一个方法的签名字符串，这个字符串就相当于这个方法的一个表示
     * 形如：java.lang.Object#testedFun:java.lang.String,java.lang.Integer
     * @param method 方法
     * @return 返回签名字符串
     */
    public static String getMethodSignature(Method method) {
        StringBuilder result = new StringBuilder();
        // 首先拿到方法的返回类型
        // 返回类型肯定不会是 null，即使是 Void 返回类型，它也是个 Class<?>
        Class<?> resultType = method.getReturnType();
        // 返回结果 java.lang.Long#
        result.append(resultType.getName()).append('#');
        // 添加方法名
        result.append(method.getName());
        // 接着去拿到参数列表
        Class<?>[] paramsType = method.getParameterTypes();
        for ( int index = 0; index < paramsType.length; index ++) {
            // 循环拼接参数类型名称
            if ( 0 == index ) {
                result.append( ':' ).append(paramsType[index].getName());
            } else {
                result.append( ',' ).append(paramsType[index].getName());
            }
        }
        return result.toString();
    }

    /**
     * 将 methods 中的方法对象，添加到 uniqueMethods 中去
     * key 是该方法的一个描述字符串
     * 字符串的生成规则根据上面方法来：getMethodSignature(Method method);
     * 在这个方法之前，需要去了解到什么是桥接方法
     *
     * <pre>
     *     {@code
     *      public interface Father<T> {
     *          <T> T getTemplate();
     *      }
     *
     *      public Sun implements Father<String> {
     *          String getTemplate();
     *      }
     *
     *      // 写得烂的代码：
     *      Father sun = new Sun();
     *      // 写的好的代码：
     *      Father<String> sun = new Sun<>();
     *
     *      // 在 new 子类的时候，不管我们是否写了具体的模板类，编译器都会在字节码里面
     *      // 去添加一个对应模板函数的桥接函数，这个桥接函数接收 Object 类型，然后强制转化为模板类型
     *      // 最终去调用具体的模板方法
     *      // 所以，不管写不写，在运行时都会检查是否允许执行，因为强转一定会检查
     *      // 但是，如果写了，那么在编译之前，就能找到这个错误，所以还是记得都写上
     *
     *      // 其实这里面还有很多细节，现在来不及了
     *     }
     * </pre>
     *
     * Mybatis 屏蔽了桥接方法，也确实是，我没有必要去添加桥接方法
     * 因为，我直接拿到了模板方法，就没有必要去记录桥接方法了
     * 然后，如果子类实现了父类的方法，首先，返回对象是一样的，其次参数类型也是一样的
     * 所以方法签名一定是一样的，那我就不需要去多次记录，即使记录下的是父类的方法
     * 其结果任然是子类的方法，因为是一样的
     *
     *
     * @param uniqueMethods 唯一的方法 Map 集合
     * @param methods 待添加的方法数组
     */
    public static void addUniqueMethods(Map<String, Method> uniqueMethods, Method[] methods) {
        // 遍历方法数组
        for ( Method index : methods ) {
            // 我只需要不是桥接方法
            if ( !index.isBridge() ) {
                String methodSignature = ReflectorUtil.getMethodSignature(index);
                // 如果我已经记录了父类或者子类的函数，就是重载函数，我就没必要再去记录一次
                if ( !uniqueMethods.containsKey(methodSignature) ) {
                    uniqueMethods.put(methodSignature, index);
                }
            }
        }
    }

    /**
     * 获取一个 Class 对象中的所有方法，这些方法不是桥接方法，且重构方法将只会被记录一次
     * @param type 类型
     * @param uniqueMethods 用于存储这些方法的 Map，写在这里，是为了不让这个 Util 类，有太多的内存占用
     * @return 方法数组
     */
    public static Method[] getClassMethods(Class<?> type, Map<String, Method> uniqueMethods) {
        Class<?> parentClass = type;
        while ( null != parentClass && Object.class != parentClass ) {
            // 我只是拿到这个类的单纯的方法
            ReflectorUtil.addUniqueMethods(uniqueMethods, parentClass.getDeclaredMethods());
            // 但是，如果这个类，它是个抽象类，抽象类允许暂时不去实现接口中的方法
            // 且，getDeclaredMethods 方法拿不到接口中的方法，就需要先去拿到接口中的方法
            // 然后，再去拿到这些接口中的方法
            Class<?>[] interfaceClassS = type.getInterfaces();
            for ( Class<?> index : interfaceClassS ) {
                ReflectorUtil.addUniqueMethods(uniqueMethods, index.getDeclaredMethods());
            }
            // 接着就去找到自己的父类
            parentClass = parentClass.getSuperclass();
        }
        // 获取完毕之后，将 Map 中的 Value 全部拿出来，转化成数组
        Collection<Method> methodCollection = uniqueMethods.values();
        return methodCollection.toArray(new Method[0]);
    }
}
