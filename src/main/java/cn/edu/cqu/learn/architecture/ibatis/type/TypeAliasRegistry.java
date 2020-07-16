package cn.edu.cqu.learn.architecture.ibatis.type;

import cn.edu.cqu.learn.architecture.ibatis.io.ResolverUtil;
import cn.edu.cqu.learn.architecture.ibatis.io.Resources;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.util.*;

/**
 * 类型别人的注册类
 */
public class TypeAliasRegistry {

    // 就是这个对象，存的是 String Class 键值对，就是类型别名的映射
    // 别名就是 String，类型就是 Class
    private final Map<String, Class<?>> typeAliases = new HashMap<>();

    // 注册进集合映射
    public TypeAliasRegistry() {
        registerAlias("string", String.class);

        registerAlias("byte", Byte.class);
        registerAlias("long", Long.class);
        registerAlias("short", Short.class);
        registerAlias("int", Integer.class);
        registerAlias("integer", Integer.class);
        registerAlias("double", Double.class);
        registerAlias("float", Float.class);
        registerAlias("boolean", Boolean.class);

        registerAlias("byte[]", Byte[].class);
        registerAlias("long[]", Long[].class);
        registerAlias("short[]", Short[].class);
        registerAlias("int[]", Integer[].class);
        registerAlias("integer[]", Integer[].class);
        registerAlias("double[]", Double[].class);
        registerAlias("float[]", Float[].class);
        registerAlias("boolean[]", Boolean[].class);

        registerAlias("_byte", byte.class);
        registerAlias("_long", long.class);
        registerAlias("_short", short.class);
        registerAlias("_int", int.class);
        registerAlias("_integer", int.class);
        registerAlias("_double", double.class);
        registerAlias("_float", float.class);
        registerAlias("_boolean", boolean.class);

        registerAlias("_byte[]", byte[].class);
        registerAlias("_long[]", long[].class);
        registerAlias("_short[]", short[].class);
        registerAlias("_int[]", int[].class);
        registerAlias("_integer[]", int[].class);
        registerAlias("_double[]", double[].class);
        registerAlias("_float[]", float[].class);
        registerAlias("_boolean[]", boolean[].class);

        registerAlias("date", Date.class);
        registerAlias("decimal", BigDecimal.class);
        registerAlias("bigdecimal", BigDecimal.class);
        registerAlias("biginteger", BigInteger.class);
        registerAlias("object", Object.class);

        registerAlias("date[]", Date[].class);
        registerAlias("decimal[]", BigDecimal[].class);
        registerAlias("bigdecimal[]", BigDecimal[].class);
        registerAlias("biginteger[]", BigInteger[].class);
        registerAlias("object[]", Object[].class);

        registerAlias("map", Map.class);
        registerAlias("hashmap", HashMap.class);
        registerAlias("list", List.class);
        registerAlias("arraylist", ArrayList.class);
        registerAlias("collection", Collection.class);
        registerAlias("iterator", Iterator.class);

        registerAlias("ResultSet", ResultSet.class);
    }


    // 将别名和类型进行注册，如果类型是 String
    // 调用自己的类加载器去加载这个类
    public void registerAlias(String alias, String value) {
        try {
            Class<?> valueClass = Resources.classForName(value);
            registerAlias(alias, valueClass);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("类型注册失败，找不到这个类型：" + e.getMessage());
        }
    }

    // 从集合中获取类，如果没有，则调用上下文环境，添加到集合中
    public <T> Class<T> resolveAlias(String string) {
        try {
            if (string == null) {
                return null;
            }
            String key = string.toLowerCase(Locale.ENGLISH);
            Class<T> value;
            if (typeAliases.containsKey(key)) {
                value = (Class<T>) typeAliases.get(key);
            } else {
                value = (Class<T>) Resources.classForName(string);
                registerAlias(string, value);
            }
            return value;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("找不到名字为：" + string + "的类型映射，原因是：" + e, e);
        }
    }

    // 当前类，全部注册
    public void registerAliases(String packageName) {
        registerAliases(packageName, Object.class);
    }

    // 批量注册，将某个包路径下的，指定类型的子类型，注册到集合中
    public void registerAliases(String packageName, Class<?> superType) {
        ResolverUtil<Class<?>> resolverUtil = new ResolverUtil<>();
        resolverUtil.find(new ResolverUtil.IsA(superType), packageName);
        Set<Class<? extends Class<?>>> typeSet = resolverUtil.getMatches();
        for (Class<?> type : typeSet) {
            // 匿名内部类、接口、成员变量将不会被注册进集合
            if (!type.isAnonymousClass() && !type.isInterface() && !type.isMemberClass()) {
                registerAlias(type);
            }
        }
    }

    // 将别名和类型进行注册
    public void registerAlias(String alias, Class<?> value) {
        if ( null == alias ) {
            throw new RuntimeException("类型别名不可为空");
        }
        String key = alias.toLowerCase(Locale.ENGLISH);
        if (
                typeAliases.containsKey(key) &&
                        typeAliases.get(key) != null &&
                        !typeAliases.get(key).equals(value)) {
            throw new RuntimeException("当前别名：" + alias + "已经注册了，注册类型为：" + value.getSimpleName());
        }
        typeAliases.put(key, value);
    }


    // 如果类型映射中，没有主动传递别名，就试图去获取这个类的注解，看是否
    // 有别名注解，有的话，直接使用别名注解中的值
    // 看来，注解一般都是使用反射来使用的
    public void registerAlias(Class<?> type) {
        String alias = type.getSimpleName();
        Alias aliasAnnotation = type.getAnnotation(Alias.class);
        if (null != aliasAnnotation) {
            alias = aliasAnnotation.value();
        }
        registerAlias(alias, type);
    }


    // 获取这个类型注册的对象，这个对象是不可以修改的，其实没什么意义
    public Map<String, Class<?>> getTypeAliases() {
        return Collections.unmodifiableMap(typeAliases);
    }
}
