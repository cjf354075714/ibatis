package cn.edu.cqu.learn.architecture.ibatis.reflection.resolver;

import cn.edu.cqu.learn.architecture.ibatis.utils.ReflectorUtilTest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;

public class TypeParameterResolverTest {

    private static final Logger SLF4J;

    static {
        SLF4J = LoggerFactory.getLogger(ReflectorUtilTest.class);
    }


    private String simpleString;
    private List simpleList;
    private List<Object> objectList;

    @Test
    public void typeTest() {
        Field[] fields = TypeParameterResolverTest.class.getDeclaredFields();
        for ( Field index : fields ) {
            Type type = index.getGenericType();
            SLF4J.info(type.toString());
        }
    }

}
