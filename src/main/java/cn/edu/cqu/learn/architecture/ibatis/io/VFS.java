package cn.edu.cqu.learn.architecture.ibatis.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Virtual File System 虚拟文件系统
 * 原本上，这个概念是来自于 Linux 的，就是说，这个系统对底层屏蔽了细节
 * 我们在 Linux 上访问文件的时候，只需要访问 VFS 的接口就行了
 * 基于这个概念，我们要知道，也许 Mybatis 运行时，也需要访问其他的外部资源
 * 所以，有自己的一个简单的 VFS
 */
public abstract class VFS {

    private static final Logger SLFF4J;

    // 默认的实现 VFS 的类集合
    public static final List<Class<? extends VFS>> MYBATIS_VFS;

    // 用户自己定义的实现 VFS 的集合
    public static final List<Class<? extends VFS>> CONSUMER_VFS;

    static {
        SLFF4J = LoggerFactory.getLogger(VFS.class);

        MYBATIS_VFS = new LinkedList<>();
        MYBATIS_VFS.add(DefaultVFS.class);

        CONSUMER_VFS = new LinkedList<>();
    }

    public static VFS getInstance() {
        return VFSHolder.INSTANCE;
    }

    // 用户添加自己的 VFS 实现类
    public static void addConsumerVFS(Class<? extends VFS> consumerVFS) {
        CONSUMER_VFS.add(consumerVFS);
    }

    // 根据一个路径名，返回当前路径名下的所有的资源文件的 URL 对象
    // 使用的是当前线程的类加载器去构建
    protected static List<URL> getResources(String path) throws IOException {
        return Collections.list(Thread.currentThread().getContextClassLoader().getResources(path));
    }

    public abstract boolean isValid();

    protected abstract List<String> list(URL url, String forPath) throws IOException;

    public List<String> list(String path) throws IOException {
        List<String> names = new ArrayList<>();
        List<URL> temp = getResources(path);
        for (URL url : temp) {
            names.addAll(list(url, path));
        }
        return names;
    }

    private static class VFSHolder {
        private static final VFS INSTANCE;

        static {
            INSTANCE = createVFS();
        }

        // 就是去构建一个真正可用的 VFS 类，先用用户传入的 Class 对象去构建
        // 然后才是默认的，我们一般都没有用自己的 VFS 类
        private static VFS createVFS() {
            List<Class<? extends VFS>> allAdapterVFS = new LinkedList<>();
            allAdapterVFS.addAll(CONSUMER_VFS);
            allAdapterVFS.addAll(MYBATIS_VFS);
            VFS result;
            for (Class<? extends VFS> index : allAdapterVFS) {
                try {
                    result = index.getDeclaredConstructor().newInstance();
                    if (result.isValid()) {
                        return result;
                    } else {
                        SLFF4J.error("VFS 类：{} 不可用", index.getSimpleName());
                    }
                } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                    SLFF4J.error("构建 VFS {} 类，失败：{}", index.getSimpleName(), e.getMessage());
                    return null;
                }
            }
            return null;
        }
    }
}
