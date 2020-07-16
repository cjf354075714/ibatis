package cn.edu.cqu.learn.architecture.ibatis.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;

/**
 * 这个类，是用莱加载某些包下的类而存在的
 * 比如，我要加载，xxx.xxx.xxx 包下，某个实现了某个父类的全部类，或者带有某个注解的某个类
 * @param <T>
 */
public class ResolverUtil<T> {

    private static final Logger SLF4J;

    static {
        SLF4J = LoggerFactory.getLogger(ResolverUtil.class);
    }


    // 实现了这接口的类，就是去执行过滤的实际类
    // 现在之后父类过滤和接口过滤
    public interface Test {

        boolean matches(Class<?> type);
    }

    public static class IsA implements Test {

        // 构造函数去初始化赋值，用于判断，传入的这个类，是否是这个类的子类
        private Class<?> parent;

        public IsA() {
        }

        public Class<?> getParent() {
            return parent;
        }

        public void setParent(Class<?> parent) {
            this.parent = parent;
        }

        @Override
        public boolean matches(Class<?> type) {
            return null != type && parent.isAssignableFrom(type);
        }

        @Override
        public String toString() {
            return "父类是：" + parent.getSimpleName();
        }
    }

    public static class AnnotatedWith implements Test {

        // 它首先肯定要是个注解，我才能去判断类上是不是有注解
        private Class<? extends Annotation> annotation;

        public Class<? extends Annotation> getAnnotation() {
            return annotation;
        }

        public void setAnnotation(Class<? extends Annotation> annotation) {
            this.annotation = annotation;
        }

        public AnnotatedWith(Class<? extends Annotation> annotation) {
            this.annotation = annotation;
        }

        @Override
        public boolean matches(Class<?> type) {
            return type != null && type.isAnnotationPresent(annotation);
        }
    }
}
