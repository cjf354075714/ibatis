package cn.edu.cqu.learn.architecture.ibatis;

import cn.edu.cqu.learn.architecture.ibatis.type.JdbcType;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

@SpringBootTest
class IBatisApplicationTests {

    private static final Logger SLF4J;

    static {
        SLF4J = LoggerFactory.getLogger(IBatisApplicationTests.class);
    }

    @Test
    void contextLoads() {

    }

    // EnumMap 实际上就是一个 Map，只不过，它规定，这个 Map 的 key 就只能是枚举类型
    // 然后，Map 的 value 是随便的，你想写什么类型就写什么类型
    // 以后我要是遇到枚举作为 key 的情况，我就用这个类
    // 这个类线程不安全，key 的顺序按照枚举的顺序实现的
    // 细节我不去了解，后面有专门的数据结构学习
    @Test
    void enumMapTest() {
        Map<JdbcType, Object> enumMap = new EnumMap<>(JdbcType.class);
        enumMap.put(JdbcType.CHAR, "char");
        for (Map.Entry<JdbcType, Object> index : enumMap.entrySet()) {
            SLF4J.info(index.toString());
        }
    }

    // Type 这个类，是 JAVA 语言中所有的类型的通用接口
    // 这些接口，分别是：原始类型、参数类型、数组类型、参数类型和基本类型
    // 这个接口还有很多东西
    // 我们如何理解 Type 这个接口，就是，Class 这个类的父接口
    // 对于不同的类，它的 Class 是不同的，这些 Class 可以按照分类，分成好几种
    // 所以，有几种，就有几种 Type 的接口
    // 下面就有一个现成的
    @Test
    void type() {

    }

    // 当一个类，有自己的模板类的时候，那么，他的直接通用父类，就是 ParameterizedType 的实际实现者
    // 自然，我们就可以通过这个 ParameterizedType 拿到它的具体的模板类型
    @Test
    void parameterizedType() {
        List<String> temp = new LinkedList<>();
        Class<?> tempClass = temp.getClass();

        // 如果是模板类型，则一定是参数类型
        Type type = tempClass;
        ParameterizedType parameterizedType = (ParameterizedType) type;
        Type[] types = parameterizedType.getActualTypeArguments();
        Type type1 = types[0];
        SLF4J.info("");

    }

    // class.getGenericSuperclass();
    // 上面的方法，将返回 class 这个类的直接父类，并且附带上模板类型
    // 如果，这个类在定义的时候，指定了具体类型，则返回具体的模板类型
    // 否者，返回模板类型
    @Test
    void clazz() {
        Map<String, Object> temp = new HashMap<>();
        Type type = temp.getClass().getGenericSuperclass();
        SLF4J.info(type.toString());
    }

}
