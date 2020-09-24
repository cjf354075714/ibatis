package cn.edu.cqu.learn.architecture.ibatis.reflection.resolver.genericarraytype;

import cn.edu.cqu.learn.architecture.ibatis.utils.ReflectorUtilTest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class GenericArrayTypeTest<T, K> {

    private static final Logger SLF4J;

    static {
        SLF4J = LoggerFactory.getLogger(ReflectorUtilTest.class);
    }

    private T[] types;

    private List<T> tList;

    private T type;

    private List<? extends InputStream> inputStreamsList;

    private List<? extends InputStream>[] inputStreamArr;

    private Map<T, K>[][] map;

    @Test
     public void arrayTypeTest() {
         // 首先，我拿到当前类的所有字段
        Field[] fields = this.getClass().getDeclaredFields();
        for ( Field index : fields ) {
            Type declaredType = index.getGenericType();
            SLF4J.info(declaredType.toString());
        }
     }

}
