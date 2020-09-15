package cn.edu.cqu.learn.architecture.ibatis.session.factory.impl;


import cn.edu.cqu.learn.architecture.ibatis.core.Configuration;
import cn.edu.cqu.learn.architecture.ibatis.core.Environment;
import cn.edu.cqu.learn.architecture.ibatis.executor.enums.ExecutorType;
import cn.edu.cqu.learn.architecture.ibatis.session.SqlSession;
import cn.edu.cqu.learn.architecture.ibatis.session.factory.SqlSessionFactory;
import cn.edu.cqu.learn.architecture.ibatis.transaction.TransactionIsolationLevel;
import cn.edu.cqu.learn.architecture.ibatis.transaction.factory.TransactionFactory;
import cn.edu.cqu.learn.architecture.ibatis.transaction.factory.impl.JdbcTransactionFactory;

import java.sql.Connection;

/**
 * 默认的 SqlSession 构造工厂，它返回的是 SqlSession 的默认实现类
 * 根据配置来返回是那一个实现类
 */
public class DefaultSqlSessionFactory implements SqlSessionFactory {

    private final Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public SqlSession openSession() {
        return null;
    }

    @Override
    public SqlSession openSession(boolean autoCommit) {
        return null;
    }

    @Override
    public SqlSession openSession(Connection connection) {
        return null;
    }

    @Override
    public SqlSession openSession(TransactionIsolationLevel level) {
        return null;
    }

    @Override
    public SqlSession openSession(ExecutorType execType) {
        return null;
    }

    @Override
    public SqlSession openSession(ExecutorType execType, boolean autoCommit) {
        return null;
    }

    @Override
    public SqlSession openSession(ExecutorType execType, TransactionIsolationLevel level) {
        return null;
    }

    @Override
    public SqlSession openSession(ExecutorType execType, Connection connection) {
        return null;
    }

    @Override
    public Configuration getConfiguration() {
        return null;
    }



    /**
     * Mybatis 的配置文件中 <environments /> 节点里面有很多个
     * environment 配置，每个配置都代表这不同的数据库连接
     * 在正常的解析过程中，会正常的解析出是什么类型的 transactionFactory
     * 如果我这里发现，没有事务工厂，那就返回一个 jdbc 类型的事务工厂
     * 在我们使用的过程中，一般都是配置成 jdbc 事务工厂
     * 所以我这里没有写完全部的事务工厂
     * @param environment 环境对象
     * @return 事务工厂
     */
    private TransactionFactory getTransactionFactoryFromEnvironment(Environment environment) {
        if (null == environment || null == environment.getTransactionFactory()) {
            return new JdbcTransactionFactory();
        }
        return environment.getTransactionFactory();
    }
}
