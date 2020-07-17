package cn.edu.cqu.learn.architecture.ibatis;

import cn.edu.cqu.learn.architecture.ibatis.type.JdbcType;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.EnumMap;
import java.util.Map;

@SpringBootTest
class IBatisApplicationTests {

    private static final Logger SLF4J;

    static {
        SLF4J = LoggerFactory.getLogger(IBatisApplicationTests.class);
    }

    @Test
    void contextLoads() {

    }

    // EnumMap 实际上就是一个 Map，只不过，它规定，这个 Map 的 key 就只能是枚举类型
    // 然后，Map 的 value 是随便的，你想写什么类型就写什么类型
    // 以后我要是遇到枚举作为 key 的情况，我就用这个类
    // 这个类线程不安全，key 的顺序按照枚举的顺序实现的
    // 细节我不去了解，后面有专门的数据结构学习
    @Test
    void enumMapTest() {
        Map<JdbcType, Object> enumMap = new EnumMap<>(JdbcType.class);
        enumMap.put(JdbcType.CHAR, "char");
        for (Map.Entry<JdbcType, Object> index : enumMap.entrySet()) {
            SLF4J.info(index.toString());
        }
    }
}
