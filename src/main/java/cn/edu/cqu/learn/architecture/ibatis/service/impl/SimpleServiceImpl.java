package cn.edu.cqu.learn.architecture.ibatis.service.impl;

import cn.edu.cqu.learn.architecture.ibatis.service.ISimpleService;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.reflection.Reflector;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.session.defaults.DefaultSqlSession;

public class SimpleServiceImpl implements ISimpleService {


    /**
     * Class 对象是所有 JAVA 对象的类对象，其模板就是类实例的类型
     * @return Class
     * @throws Exception Exception
     */
    @Override
    public Class<?> getTypeClass() throws Exception {
        return Object.class;
    }


    /**
     * 如何去构造一个 JAVA Bean，Mybatis 通过下面的类去实现
     * 一个业务类，就对应一个 Reflector
     * @return reflector
     * @throws Exception exception
     *
     *
     * 一个 Reflector 对应一个业务类，将这个业务类的 Class 对象传入
     * Reflector，这个 Reflector 就可以通过反射，拿到所有的类信息
     * 就可以构造这个业务类了
     *
     * 在 Reflector 的构造函数中，首先拿到 Class 的构造函数，拿的是参数个数为 0 的构造函数
     * 接着，将会拿到 Reflector 中的所有方法，只会拿到非桥接方法，桥接方法是指，子类实现了模板方法，并返回具体的类型的方法
     * 获取的过程中，需要考虑到父类、接口等方法
     * Reflector 将会拿到所有的属性字段，setting、getting方法，注意要拿到父类和接口中的方法
     *
     * 实际上，这个 Reflector 类里面还有更细节的东西，我们不需要关注，等我后面更厉害了再说
     *
     *
     */
    @Override
    public Reflector getReflector() throws Exception {
        return null;
    }

    /**
     * Reflector 的构建工厂类
     * 内部是线程安全的 Map 集合实现的，key 是 Class 对象
     * Value 是 Reflector 对象，如果在 Mybatis 运行过程中
     * 需要多次使用到一个对象的 Reflector，那环迅就有意义了，所以线程安全 Map 就是这个意义
     * @return ReflectorFactory
     * @throws Exception Exception
     *
     * 当然我们可以自己去实现这个类，依靠 Mybatis 的插件实现
     */
    @Override
    public ReflectorFactory defaultReflectorFactory() throws Exception {
        return null;
    }

    /**
     * 由什么去构造我们的业务类，就由这个 ObjectFactory 的实现类去完成
     * @return ObjectFactory
     * @throws Exception Exception
     *
     * 这个类，似乎没有和 Reflector 产生什么互动
     * 默认的实现类，将通过反射的方式去构造一个业务类
     * 如果，这个业务类的构造函数有很多参数，通过传入这些参数的数组，就可以构造
     * 哇，还可以这样啊，比如：
     * String、Integer 和 Integer、String 是两种不同的构造方法
     * 你传入的参数顺序不同，他就执行不同的构造函数
     *
     * 这个ObjectFactory 和 ReflectorFactory 相似，可以自己定义
     */
    @Override
    public ObjectFactory defaultObjectFactory() throws Exception {
        return null;
    }


    /**
     * 这个类就是我们所接触的第一个 Mybatis 类，它用于构建 SqlSessionFactory
     * 我们需要传入数据库的链接信息，还可以将这些信息按照环境来区分，就相当于 Spring 的各种配置文件
     * 返回的是 DefaultSqlSessionFactory
     *
     * sqlSessionFactoryBuilder 将调用 XML 解析器，去读取我们的 XML 文件
     * 根据这个文件，将返回一个 Configuration，这个对象将包含所有的 Mybatis 配置内容
     * 比如：自定义反射器，工厂器等，然后，再将这个对象，传给 DefaultSqlSessionFactory
     * DefaultSqlSessionFactory 将根据这个 Configuration 去返回不同的 SqlSession
     * SQL 返回结果是否将通过它去解析？
     *
     * @return SqlSessionFactoryBuilder
     * @throws Exception Exception
     */
    @Override
    public SqlSessionFactoryBuilder sqlSessionFactoryBuilder() throws Exception {
        return null;
    }

    /**
     * 这个类很关键，它包含两个主要的部分：
     * 事务管理器，我现在还没理解到事务管理器的作用
     * 数据库连接池，就是这个连接池
     * @return Environment
     * @throws Exception Exception
     */
    @Override
    public Environment environment() throws Exception {
        return null;
    }

    /**
     * 由 SqlSessionFactoryBuilder 而来，用于构建 SqlSession
     * 构建 SqlSession 需要一些特殊的参数，关键点就在 Environment
     * @return SqlSessionFactory
     * @throws Exception Exception
     *
     * SqlSessionFactory 获取 SqlSession的关键步骤：
     * 首先，从 Environment 中获取事务管理器，事务管理器就分为两种：
     * JDBC 和 Managed，我们只需要关注 JDBC 链接池即可
     *
     * 然后，从 Environment 中获取 DataSource 对象，这个对象就是我们普通的 DataSource 对象
     */
    @Override
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        return null;
    }

    /**
     * 包含所有的 Mybatis 的核心配置，其来源就是 Mybatis-Config.xml
     * 但是，我没找到是在什么地方去构建 Environment 的
     * 我所关注的 DataSource 就是在 Environment 中的
     * @return Configuration
     * @throws Exception Exception
     */
    @Override
    public Configuration configuration() throws Exception {
        return null;
    }

    /**
     * 根据 Configuration 将返回不同的 SqlSession
     * @return DefaultSqlSession
     * @throws Exception Exception
     *
     *
     */
    @Override
    public DefaultSqlSession defaultSqlSession() throws Exception {
        return null;
    }
}
