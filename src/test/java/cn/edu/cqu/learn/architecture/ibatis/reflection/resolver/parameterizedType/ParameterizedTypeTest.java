package cn.edu.cqu.learn.architecture.ibatis.reflection.resolver.parameterizedType;

import cn.edu.cqu.learn.architecture.ibatis.utils.ReflectorUtilTest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParameterizedTypeTest<T> {

    private static final Logger SLF4J;

    static {
        SLF4J = LoggerFactory.getLogger(ReflectorUtilTest.class);
    }

    private Map<String, Object> map;
    private Map.Entry<String, Object> entry;
    private List<T> tList;

    @Test
    public void test() {
        Field[] fields = this.getClass().getDeclaredFields();
        for ( Field index : fields ) {
            Type type = index.getGenericType();
            SLF4J.info(type.toString());
        }
    }

    @Test
    public void test1() {

    }
}
