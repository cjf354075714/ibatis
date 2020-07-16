package cn.edu.cqu.learn.architecture.ibatis.io;

import com.mysql.cj.util.TestUtils;
import jdk.management.resource.ResourceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.InvalidPathException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/**
 * Mybatis 使用的默认的 VFS
 */
public class DefaultVFS extends VFS {

    private static final Logger SLF4J;

    private static final byte[] JAR_MAGIC;

    static {
        SLF4J = LoggerFactory.getLogger(DefaultVFS.class);

        // JAR 文件协议，头四个字节是 PK34
        JAR_MAGIC = new byte[]{ 'P', 'K', 3, 4};
    }

    @Override
    public boolean isValid() {
        return true;
    }

    // 递归列出所有的文件，返回所以的 URL
    @Override
    protected List<String> list(URL url, String forPath) throws IOException {
        InputStream is = null;
        try {
            List<String> resources = new ArrayList<>();
            URL jarUrl = findJarForResource(url);
            if (jarUrl != null) {
                is = jarUrl.openStream();
                resources = listResources(new JarInputStream(is), forPath);
            } else {
                List<String> children = new ArrayList<>();
                try {
                    if (isJar(url, new byte[JAR_MAGIC.length])) {
                        is = url.openStream();
                        try (JarInputStream jarInput = new JarInputStream(is)) {
                            for (JarEntry entry; (entry = jarInput.getNextJarEntry()) != null; ) {
                                children.add(entry.getName());
                            }
                        }
                    } else {
                        is = url.openStream();
                        List<String> lines = new ArrayList<>();
                        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                            for (String line; (line = reader.readLine()) != null;) {
                                lines.add(line);
                                if (getResources(forPath + "/" + line).isEmpty()) {
                                    lines.clear();
                                    break;
                                }
                            }
                        } catch (InvalidPathException e) {
                            // #1974
                            lines.clear();
                        }
                        if (!lines.isEmpty()) {
                            children.addAll(lines);
                        }
                    }
                } catch (FileNotFoundException e) {
                    if ("file".equals(url.getProtocol())) {
                        File file = new File(url.getFile());
                        if (file.isDirectory()) {
                            children = Arrays.asList(file.list());
                        }
                    } else {
                        // No idea where the exception came from so rethrow it
                        throw e;
                    }
                }

                // The URL prefix to use when recursively listing child resources
                String prefix = url.toExternalForm();
                if (!prefix.endsWith("/")) {
                    prefix = prefix + "/";
                }

                // Iterate over immediate children, adding files and recursing into directories
                for (String child : children) {
                    String resourcePath = forPath + "/" + child;
                    resources.add(resourcePath);
                    URL childUrl = new URL(prefix + child);
                    resources.addAll(list(childUrl, resourcePath));
                }
            }

            return resources;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    // Ignore
                }
            }
        }
    }

    // 给定一个 JAR 流对象，找出这个流对象里面，所有开头为 path 的文件的名字
    protected List<String> listResources(
            JarInputStream jar, String path
    ) throws IOException {
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        if (!path.endsWith("/")) {
            path = path + "/";
        }
        List<String> resources = new ArrayList<>();
        for (JarEntry entry; (entry = jar.getNextJarEntry()) != null;) {
            if (!entry.isDirectory()) {
                StringBuilder name = new StringBuilder(entry.getName());
                if (name.charAt(0) != '/') {
                    name.insert(0, '/');
                }
                if (name.indexOf(path) == 0) {
                    SLF4J.debug("找到资源：{}", name);
                    resources.add(name.substring(1));
                }
            }
        }
        return resources;
    }

    // 通过一个 URL 去找到一个 JAR 文件，我们通过一系列手段，来保证这个返回结果
    // 要么是 JAR 文件，要么就是 null
    // 首先需要注意 URL 这个类
    // 如果 file:file://c/index.html 这样的地址也是合法的，我需要找出最底层的地址
    protected URL findJarForResource(URL url) {
        URL result = null;
        SLF4J.debug("通过 URL：{} 寻找 JAR 文件", url);
        while (true) {
            try {
                url = new URL(url.getFile());
                SLF4J.debug("内敛 URL：{}", url);
            } catch (MalformedURLException e) {
                // 你已经找到最底层，还试图去找，那么 URL 的构造函数就出异常了
                // 然后你找到这个异常之后，就没必要去再找了
                break ;
            }
        }
        // 获取这个 URL 代表着的文件的全部名称，包括扩展名
        StringBuilder jarUrl = new StringBuilder(url.toExternalForm());

        int index = jarUrl.lastIndexOf(".jar");
        if (index >= 0) {
            try {
                result = new URL(jarUrl.toString());
                if (isJar(result, new byte[JAR_MAGIC.length])) {
                    SLF4J.debug("找到 JAR 对象：{}", result);
                } else {
                    // 有可能，这个文件它是 JAR 文件，但是它的路径错了
                    // 其实这种代码，我就没必要写了，这种是 MyBatis 的测试代码
                    jarUrl.replace(0, jarUrl.length(), result.getFile());
                    File file = new File(jarUrl.toString());
                    if (!file.exists()) {
                        try {
                            file = new File(URLEncoder.encode(jarUrl.toString(), "UTF-8"));
                        } catch (UnsupportedEncodingException e) {
                            SLF4J.error("连 UTF-8 都不支持，回家洗洗睡吧，还搞什么 ORM");
                            throw new RuntimeException("回家洗洗睡吧啊，UTF-8 不支持还要什么自行车");
                        }
                    }
                    if (file.exists()) {
                        result = file.toURI().toURL();
                        if (isJar(result, new byte[JAR_MAGIC.length])) {
                            return result;
                        }
                    }
                }
            } catch (MalformedURLException e) {
                SLF4J.error("构建 JAR 对象失败：{}", e.getMessage());
            }
        } else {
            SLF4J.error("当前找到的 {} URL 对象扩展名不为 .jar", url);
            result = null;
        }
        return result;
    }

    protected boolean isJar(URL url, byte[] buffer) {
        try (InputStream inputStream = url.openStream()) {
            int readLength = inputStream.read(buffer, 0, JAR_MAGIC.length);
            return Arrays.equals(buffer, JAR_MAGIC);
        } catch (Exception ignore) {

        }
        return false;
    }
}
