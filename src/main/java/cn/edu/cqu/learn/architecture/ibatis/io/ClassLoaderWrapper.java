package cn.edu.cqu.learn.architecture.ibatis.io;

import java.io.InputStream;
import java.net.URL;


/**
 * 这个类，就是用于包装一些类加载器，然后，让这些类加载器
 * 去加载一些类，或者一些资源
 * 包装的类加载器有：
 * 我们指定的类加载器
 * 默认的类加载器
 * 当前线程上下文容器的类加载器
 * 当前类的类加载器
 * 系统的类加载器
 * 这些类加载器，在某些情况下，是相同的
 */
public class ClassLoaderWrapper {

    // 默认的类加载器
    ClassLoader defaultClassLoader;
    // 系统级类加载器
    // Application -> System -> Bootstrap
    // 记不得
    ClassLoader systemClassLoader;

    ClassLoaderWrapper() {
        try {
            systemClassLoader = ClassLoader.getSystemClassLoader();
        } catch (SecurityException ignore) {
            // 只有在谷歌应用引擎下，才会有这个异常
        }
    }

    // 传入一个资源的路径，去使用多个类加载器，定位到这个资源，返回一个 URL
    public URL getResourceAsURL(String resource) {
        return getResourceAsURL(resource, getClassLoaders(null));
    }

    // 使用一个特定的类加载器去寻找
    public URL getResourceAsURL(String resource, ClassLoader classLoader) {
        return getResourceAsURL(resource, getClassLoaders(classLoader));
    }

    public InputStream getResourceAsStream(String resource) {
        return getResourceAsStream(resource, getClassLoaders(null));
    }

    public InputStream getResourceAsStream(String resource, ClassLoader classLoader) {
        return getResourceAsStream(resource, getClassLoaders(classLoader));
    }

    public Class<?> classForName(String name) throws ClassNotFoundException {
        return classForName(name, getClassLoaders(null));
    }

    public Class<?> classForName(String name, ClassLoader classLoader) throws ClassNotFoundException {
        return classForName(name, getClassLoaders(classLoader));
    }

    URL getResourceAsURL(String resource, ClassLoader[] classLoaders) {
        URL result;
        for (ClassLoader index : classLoaders) {
            if (null != index) {
                // 将一个资源的地址传入，返回一个 URL 协议对象
                result = index.getResource(resource);
                if (null == result) {
                    // 有些类加载器，必须要 "/" 开头
                    result = index.getResource("/" + resource);
                }
                if (null != result) {
                    return result;
                }
            }
        }
        return null;
    }

    // 既然有 URL 的资源表示，自然有流的表示
    InputStream getResourceAsStream(String resource, ClassLoader[] classLoaders) {
        InputStream result;
        for (ClassLoader index : classLoaders) {
            if (null != index) {
                // 将一个资源的地址传入，返回一个 URL 协议对象
                result = index.getResourceAsStream(resource);
                if (null == result) {
                    // 有些类加载器，必须要 "/" 开头
                    result = index.getResourceAsStream("/" + resource);
                }
                if (null != result) {
                    return result;
                }
            }
        }
        return null;
    }

    Class<?> classForName(String className, ClassLoader[] classLoaders) throws ClassNotFoundException {
        for (ClassLoader index : classLoaders) {
            if (null != index) {
                try {
                    // 试图用这个类加载器，根据类名称去加载一个类
                    return Class.forName(className, true, index);
                } catch (ClassNotFoundException e) {
                    // 数组中去加载类，要所有的类都没加载到，才表示加载失败
                }
            }
        }
        throw new ClassNotFoundException("当前类加载器加载不到名称为：" + className + "的类");
    }

    // 返回一个类加载器数组，我不明白为什么要这样写
    // 这些类加载器，是程序运行时，涉及到的类加载器
    ClassLoader[] getClassLoaders(ClassLoader classLoader) {
        return new ClassLoader[]{
                classLoader, defaultClassLoader,
                Thread.currentThread().getContextClassLoader(),
                getClass().getClassLoader(),
                systemClassLoader
        };
    }
}
