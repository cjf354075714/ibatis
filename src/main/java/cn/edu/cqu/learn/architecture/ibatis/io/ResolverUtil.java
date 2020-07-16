package cn.edu.cqu.learn.architecture.ibatis.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    // 已经匹配上的集合
    private Set<Class<? extends T>> matches = new HashSet<>();

    private ClassLoader classLoader;

    public Set<Class<? extends T>> getMatches() {
        return matches;
    }

    public void setMatches(Set<Class<? extends T>> matches) {
        this.matches = matches;
    }

    public ClassLoader getClassLoader() {
        return classLoader == null ? Thread.currentThread().getContextClassLoader() : classLoader;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public ResolverUtil<T> findImplementations(Class<?> parent, String... packageNames) {
        if (packageNames == null) {
            return this;
        }

        Test test = new IsA(parent);
        for (String pkg : packageNames) {
            find(test, pkg);
        }

        return this;
    }


    // 给定一系列的文件名和一个注解，找到这个路径下，所有拥有这个注解的类对象
    public ResolverUtil<T> findAnnotated(
            Class<? extends Annotation> annotation, String... packageNames
    ) {
        if ( null == packageNames ) {
            return this;
        }
        Test test = new AnnotatedWith(annotation);
        for (String index : packageNames) {
            find(test, index);
        }
        return this;
    }

    // 给定一个文件路径，然后，去找到这个路径下的所有文件
    // 如果文件是 class 文件，则去实例化，并添加到集合中
    public ResolverUtil<T> find(Test test, String packageName) {
        String path = getPackagePath(packageName);
        try {
            List<String> children = VFS.getInstance().list(path);
            for (String child : children) {
                if (child.endsWith(".class")) {
                    addIfMatching(test, child);
                }
            }
        } catch (IOException ioException) {
            SLF4J.error("阅读包失败：{}", packageName, ioException);
        }
        return this;
    }

    protected String getPackagePath(String packageName) {
        return packageName == null ? null : packageName.replace('.', '/');
    }


    // 传入一个类的名字，并构建一个 Class 对象，如果匹配上了，则添加进集合中
    protected void addIfMatching(Test test, String fqn) {
        try {
            // 将 xxx.xxx.xxx 转化为 xxx/xxx/xxx
            String externalName = fqn.substring(0, fqn.indexOf('.')).replace('/', '.');
            ClassLoader loader = getClassLoader();
            Class<?> type = loader.loadClass(externalName);
            if (test.matches(type)) {
                matches.add((Class<T>) type);
            }
        } catch (Throwable t) {
            SLF4J.warn("加载类 {} 错误：{}", fqn ,t.getMessage());
        }
    }

    // 实现了这接口的类，就是去执行过滤的实际类
    // 现在之后父类过滤和接口过滤
    public interface Test {

        boolean matches(Class<?> type);
    }

    public static class IsA implements Test {

        // 构造函数去初始化赋值，用于判断，传入的这个类，是否是这个类的子类
        private Class<?> parent;

        public IsA(Class<?> parent) {
            this.parent = parent;
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
