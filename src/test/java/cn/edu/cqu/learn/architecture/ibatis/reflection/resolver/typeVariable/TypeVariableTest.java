package cn.edu.cqu.learn.architecture.ibatis.reflection.resolver.typeVariable;

import cn.edu.cqu.learn.architecture.ibatis.utils.ReflectorUtilTest;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.net.Socket;

public class TypeVariableTest {

    private static final Logger SLF4J;

    static {
        SLF4J = LoggerFactory.getLogger(ReflectorUtilTest.class);
    }

    @Test
    public void fieldTypeTest() throws NoSuchFieldException {
        Field key = TypeVariableEntity.class.getDeclaredField("value");
        Type type = key.getGenericType();
        if (TypeVariable.class.isAssignableFrom(type.getClass()) ) {
            // 但是我觉得没有必要去实现 GenericDeclaration 这个接口
            // 你像 Map 这些都没有去实现
            TypeVariable typeVariable = (TypeVariable) type;
            // 获取当前这个类型变量的上级，也就是最高的类型，如果没有规定，则是 Object
            Type[] bounds = typeVariable.getBounds();

            for ( Type index : bounds ) {
                SLF4J.info(index.toString());
            }
        }
    }

    private static class TypeVariableEntity<K extends InputStream, V> implements GenericDeclaration {
        // 字段
        K key;
        // 字段
        V value;

        @Override
        public TypeVariable<?>[] getTypeParameters() {
            return new TypeVariable[0];
        }

        @Override
        public <T extends Annotation> T getAnnotation(@NotNull Class<T> annotationClass) {
            return null;
        }

        @Override
        public Annotation[] getAnnotations() {
            return new Annotation[0];
        }

        @Override
        public Annotation[] getDeclaredAnnotations() {
            return new Annotation[0];
        }
    }
}
