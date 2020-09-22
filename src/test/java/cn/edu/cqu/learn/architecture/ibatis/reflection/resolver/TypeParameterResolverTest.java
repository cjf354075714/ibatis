package cn.edu.cqu.learn.architecture.ibatis.reflection.resolver;

import cn.edu.cqu.learn.architecture.ibatis.utils.ReflectorUtilTest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class TypeParameterResolverTest {

    private static final Logger SLF4J;

    static {
        SLF4J = LoggerFactory.getLogger(ReflectorUtilTest.class);
    }


    private String simpleString;
    private List simpleList;
    private List<Object> objectList;
    private Map<String, Object> stringObjectMap;
    private List<Map<String, Object>> mapList;

    @Test
    public void typeTest() {
        Field[] fields = TypeParameterResolverTest.class.getDeclaredFields();
        for ( Field index : fields ) {
            Type type = index.getGenericType();
            if (ParameterizedType.class.isAssignableFrom(type.getClass()) ) {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                Type rowType = parameterizedType.getRawType();
                SLF4J.info(rowType.toString());
            }
        }
    }

}
