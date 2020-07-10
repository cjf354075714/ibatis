package cn.edu.cqu.learn.architecture.ibatis.io;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Properties;


/**
 * 这个类，就是用来将类加载器加载资源文件的
 * 返回的结果有很多中
 * Reader 流对象
 * File 对象
 * Properties 对象
 * In 流对象 等
 */
public class Resources {

    private final static ClassLoaderWrapper CLASS_LOADER_WRAPPER;

    private static Charset charset;

    static {
        CLASS_LOADER_WRAPPER = new ClassLoaderWrapper();
    }

    Resources() {

    }

    // 返回类加载器的包装类中的默认加载器
    // 肯定为 null 啊
    public static ClassLoader getDefaultClassLoader() {
        return CLASS_LOADER_WRAPPER.defaultClassLoader;
    }

    // 这个 set 方法，我好像看到了 JS 的写法
    public static void setDefaultClassLoader(ClassLoader defaultClassLoader) {
        CLASS_LOADER_WRAPPER.defaultClassLoader = defaultClassLoader;
    }

    public static URL getResourceURL(String resource) throws IOException {
        return getResourceURL(null, resource);
    }

    public static Properties getResourceAsProperties(String resource) throws IOException {
        Properties props = new Properties();
        try (InputStream in = getResourceAsStream(resource)) {
            props.load(in);
        }
        return props;
    }

    public static Properties getResourceAsProperties(ClassLoader loader, String resource) throws IOException {
        Properties props = new Properties();
        try (InputStream in = getResourceAsStream(loader, resource)) {
            props.load(in);
        }
        return props;
    }

    public static Reader getResourceAsReader(String resource) throws IOException {
        Reader reader;
        if (charset == null) {
            reader = new InputStreamReader(getResourceAsStream(resource));
        } else {
            reader = new InputStreamReader(getResourceAsStream(resource), charset);
        }
        return reader;
    }

    public static Reader getResourceAsReader(ClassLoader loader, String resource) throws IOException {
        Reader reader;
        if (charset == null) {
            reader = new InputStreamReader(getResourceAsStream(loader, resource));
        } else {
            reader = new InputStreamReader(getResourceAsStream(loader, resource), charset);
        }
        return reader;
    }

    public static File getResourceAsFile(String resource) throws IOException {
        return new File(getResourceURL(resource).getFile());
    }

    public static File getResourceAsFile(ClassLoader loader, String resource) throws IOException {
        return new File(getResourceURL(loader, resource).getFile());
    }

    public static InputStream getUrlAsStream(String urlString) throws IOException {
        URL url = new URL(urlString);
        URLConnection conn = url.openConnection();
        return conn.getInputStream();
    }

    public static Reader getUrlAsReader(String urlString) throws IOException {
        Reader reader;
        if (charset == null) {
            reader = new InputStreamReader(getUrlAsStream(urlString));
        } else {
            reader = new InputStreamReader(getUrlAsStream(urlString), charset);
        }
        return reader;
    }

    public static Properties getUrlAsProperties(String urlString) throws IOException {
        Properties props = new Properties();
        try (InputStream in = getUrlAsStream(urlString)) {
            props.load(in);
        }
        return props;
    }

    public static Class<?> classForName(String className) throws ClassNotFoundException {
        return CLASS_LOADER_WRAPPER.classForName(className);
    }

    public static Charset getCharset() {
        return charset;
    }

    public static void setCharset(Charset charset) {
        Resources.charset = charset;
    }

    public static InputStream getResourceAsStream(String resource) throws IOException {
        return getResourceAsStream(null, resource);
    }

    // 传入一个类加载器，并且试图返回一个资源在类加载器下的对象映射
    public static URL getResourceURL(ClassLoader loader, String resource) throws IOException {
        URL url = CLASS_LOADER_WRAPPER.getResourceAsURL(resource, loader);
        if (null == url) {
            throw new IOException("找不到当前路径下的资源：" + resource);
        }
        return url;
    }

    public static InputStream getResourceAsStream(ClassLoader loader, String resource) throws IOException {
        InputStream in = CLASS_LOADER_WRAPPER.getResourceAsStream(resource, loader);
        if (in == null) {
            throw new IOException("找不到当前路径下的资源：" + resource);
        }
        return in;
    }
}
