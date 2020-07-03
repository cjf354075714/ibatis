package cn.edu.cqu.learn.architecture.ibatis;

import cn.edu.cqu.learn.architecture.ibatis.typehandler.SimpleTypeHandler;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.SimpleExecutor;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.type.TypeHandler;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.io.InputStream;
import java.sql.Connection;

@SpringBootTest
class IBatisApplicationTests {

    @Test
    void contextLoads() {
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

    @Test
    SqlSession sqlSession() {
        return sqlSessionFactory().openSession((Connection) null);
    }

    // 这个要有个单独的学习
    @Test
    Executor executor() {
        return new SimpleExecutor(null, null);
    }

    // 这个也要单独学习，还要弄懂原理
    @Test
    TypeHandler<String> typeHandler() {
        return new SimpleTypeHandler();
    }
}
