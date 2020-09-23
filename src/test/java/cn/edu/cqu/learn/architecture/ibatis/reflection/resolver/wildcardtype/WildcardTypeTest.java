package cn.edu.cqu.learn.architecture.ibatis.reflection.resolver.wildcardtype;

import cn.edu.cqu.learn.architecture.ibatis.utils.ReflectorUtilTest;
import jdk.internal.util.xml.impl.Input;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.List;

public class WildcardTypeTest {

    private static final Logger SLF4J;

    static {
        SLF4J = LoggerFactory.getLogger(ReflectorUtilTest.class);
    }

    // 如果我这里使用了 ？ 来作为 List 的类型，那么这个参数类型的
    // 真实类型就是一个通配符类型
    List<? super FileInputStream> wildCardType;

    // 如果我直接使用了直接的对象，那该真实类型就是一个 Class，而不是一个通配符类型
    List<InputStream> classType;


    @Test
    public void wildTest() {
        Field[] fields = this.getClass().getDeclaredFields();
        for ( Field index : fields ) {
            Type declaredType = index.getGenericType();
            if (ParameterizedType.class.isAssignableFrom(declaredType.getClass()) ) {
                // 找到了参数化类型，然后我再去拿到它的真实类型，并且判断是否是通配符类型
                ParameterizedType parameterizedType = (ParameterizedType) declaredType;
                Type[] realTypes = parameterizedType.getActualTypeArguments();
                for ( Type realType : realTypes ) {
                    // 现在去判断是否是通配符类型
                    if (WildcardType.class.isAssignableFrom(realType.getClass()) ) {
                        WildcardType wildcardType = (WildcardType) realType;
                        SLF4J.info(wildcardType.toString());
                    }
                }
            }
        }
    }
}
