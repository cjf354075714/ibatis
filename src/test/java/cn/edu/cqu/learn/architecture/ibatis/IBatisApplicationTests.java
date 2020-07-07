package cn.edu.cqu.learn.architecture.ibatis;

import cn.edu.cqu.learn.architecture.ibatis.entity.BaseEntity;
import cn.edu.cqu.learn.architecture.ibatis.typehandler.SimpleTypeHandler;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.SimpleExecutor;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.reflection.ParamNameResolver;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.type.TypeHandler;
import org.junit.jupiter.api.Test;
import org.mockito.internal.stubbing.answers.ReturnsElementsOf;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.io.InputStream;
import java.sql.Connection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@SpringBootTest
class IBatisApplicationTests {

    @Test
    void contextLoads() {
        Set<String> stringSet = new HashSet<>();
        stringSet.add("cao");
        stringSet.add("jun");
        stringSet.add("feng");
        List<String> stringList = new LinkedList<>();
        stringList.add("cao");
        stringList.add("jun");
        stringList.add("feng");
        String[] strings = new String[3];
        strings[0] = "cao";
        strings[1] = "jun";
        strings[2] = "feng";
        Object result = ParamNameResolver.wrapToMapIfCollection(strings, null);
        System.out.println(result);
    }


    // 通过文件流去构建一个 SqlSessionFactory 对象

    // 比如一个节点，Environment 节点，首先是通过 XML 工具类，去读取 Environment 节点
    // 然后根据这个节点的配置去生成一个 DataSource 对象，我们不需要去关注细节
    // 只需要知道，根据配置文件，写死的，比如，配置文件写的是 POOLED
    // 对象就返回一个 PooledConnection 对象，本质上是 DataSource 的实现类
    // 构建的过程有些细节
    @Test
    SqlSessionFactory sqlSessionFactory() {
        return new SqlSessionFactoryBuilder().build((InputStream) null);
    }

    // 这个对象包含了所有的 Mybatis 的基础信息
    // 在构建 SqlSessionFactory 的时候，需要用到里面的 Environment 对象
    // 这个对象，将被传输给 SqlSession。而且，所有的 SqlSession 的将用同一个 Configuration
    @Test
    Configuration configuration() {
        return new Configuration();
    }

    // 包含 ID、DataSource、TransactionFactory
    // DataSource 对象，就是我们接触到的普通对象
    // Mybatis 会有自己的一些实现对象
    // TransactionFactory 是 Mybatis 的事务管理器工厂
    // 事务涉及到什么呢？事务的隔离级别，和是否自定提交，
    // MyBatis 根据这个特性，封装了一个事务对象，就包含三个值：Connection、隔离界别、是否自动提交
    // 就是一个包装对象
    @Test
    Environment environment() {
        return new Environment(null, null, null);
    }

    // 通过 SqlSessionFactory 打开 SqlSession 对象
    // SqlSession 是 Transaction 的包装类，此外还有一些其他对象
    // Configuration 该对象将被用作 Sql 执行之后，做一些事情
    // Executor 该对象是 Sql 的真正执行器

    // 现在，去 DEBUG，看 insert 是如何运行的

    // 这个类是 Mapper 中，里面的节点的 JAVA 对象
    // 比如 Mapper 中有一个 insert 节点，那么，就会生成一个 MapperStatement 对象
    // 一个节点，是 Mybatis 的协议实现，当然，里面的节点信息，就会自动保存在 MapperStatement 中
    // 而这个 MapperStatement 将以 Map 的结构形似，存在 Configuration 中
    // 其中，Map key 就是 id， Map value 就是 MapperStatement

    // MapperStatement 里面有一个 SqlSource，这个类是 SQL 的包装类
    // 它里面还涉及到很多类
    @Test
    MappedStatement mappedStatement() {
        return null;
    }


    // 先看 SqlSession 的 update 方法

    // sqlSession.update(id, param);
    // 一般来说，SqlSession 方法，一般都只有两个参数，第一个是 MappedStatement 的 id
    // 第二个，就是 Sql 的参数
    // 第三个，就是其他的参数

    // 1，从 SqlSession 中拿到 Configuration
    // 2，从 Configuration 中拿到 Map<String id, MappedStatement> mappedStatements
    // 3，从 mappedStatements 根据 id 拿到合适的 MappedStatement
    // 4，将传入的参数进行一次包装：
    // 4，将我们传入的 SQL 参数，解析成 Map，前提是我们传入的是一个集合类，包括数组
    // 4，我们传入什么类型的集合，key 就是 那个类型的名称，
    // 4，否者返回普通类别
    // 5，获取到 SqlSession 的执行器，这个执行器，我们可以自定义，也有 Mybatis 自己的默认执行器
    // 6，执行器，将使用 MappedStatement 和 包装好的 SQL 参数去执行 SQL 并返回
    // 7，请看执行器
    @Test
    SqlSession sqlSession() {
        return sqlSessionFactory().openSession((Connection) null);
    }

    // 这个要有个单独的学习
    // Executor 是真正的 SQL 执行器，是真的嘛？
    // 它有很多类型，我们一般都不需要去指定真正的执行者，后面我将看各个类型的执行器

    // 先看简单的 SimpleExecutor
    // 1，SqlSession.update(MappedStatement ms, Object sqlParam);
    // 2，BaseExecutor.update(MappedStatement ms, Object sqlParam);
    // 3，首先，有一个类，将用作监控一个 SQL 执行时，需要花费的 Mapper 资源
    // 3，ErrorContext 是线程安全的，用于不同线程，执行 BaseExecute 时，记录当前执行的是哪一个 Mapper
    // 4，比如，我执行的是 BaseMapper.xml，它的路径是 "classpath:mapper/BaseMapper.xml"
    // 4，那 ErrorContext 将会记录下来这个路径
    // 5，清理 BaseExecutor 的缓存记录，暂时先不看
    // 6，运行时多态，调用具体的执行器去执行会话请求
    // 6，通过 MappedStatement 获取到 Configuration
    // 6，通过 Configuration 构建一个 StatementHandler，哎呀，你一个 MappedStatement，那肯定有对应的 StatementHandler 啊
    // 7，
    @Test
    Executor executor() {
        return new SimpleExecutor(null, null);
    }

    // 这个也要单独学习，还要弄懂原理
    @Test
    TypeHandler<String> typeHandler() {
        return new SimpleTypeHandler();
    }

    // 这个类的 wrapToMapIfCollection 方法，就是用来解析我们传入的 SQL 参数
    // 如果是 collection 类，则返回 HashMap<String, Object> = {"collection": Object}
    // 如果是 List 类，则额外返回 {"collection": Object, "list": Object}
    // 如果是数组，则返回 {"array": Object}
    // 如果是普通对象，则直接返回普通对象
    @Test
    ParamNameResolver paramNameResolver() {
        return null;
    }
}
