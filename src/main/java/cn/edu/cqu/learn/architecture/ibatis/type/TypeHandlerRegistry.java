package cn.edu.cqu.learn.architecture.ibatis.type;


import java.lang.reflect.Type;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 类型处理器的注册器
 */
public class TypeHandlerRegistry {

    // 一个用于存储，JDBC 类型和类型处理器的 Map 集合
    private final Map<JdbcType, TypeHandler<?>> jdbcTypeHandlerMap
             = new EnumMap<>(JdbcType.class);

    // 类型，与 JDBC 类型的类型集合，我不知道有啥用
    private final Map<Type, Map<JdbcType, TypeHandler<?>>>
            typeHandlerMap = new ConcurrentHashMap<>();

    // 未知的类型
    private final TypeHandler<Object> unknownTypeHandler = null;

    private final Map<Class<?>, TypeHandler<?>>
            allTypeHandlersMap = new HashMap<>();
}
