package cn.edu.cqu.learn.architecture.ibatis.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.List;

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

    @Override
    protected List<String> list(URL url, String forPath) throws IOException {
        return null;
    }
}
