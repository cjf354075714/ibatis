package cn.edu.cqu.learn.architecture.ibatis.session;

import java.sql.Connection;

/**
 * 用于构建 SqlSession 的工厂类
 * 实现该类的实例的生命周期同整个项目的生命周期相同
 */
public interface SqlSessionFactory {

    // 默认打开会话
    SqlSession openSession();

    // 当前会话是否自动提交
    SqlSession openSession(boolean autoCommit);

    // 使用一个 Connection 去构建
    SqlSession openSession(Connection connection);

    // 根据事务的隔离级别去获取会话
    SqlSession openSession(TransactionIsolationLevel level);

    SqlSession openSession(ExecutorType execType, boolean autoCommit);

    SqlSession openSession(ExecutorType execType, TransactionIsolationLevel level);

    SqlSession openSession(ExecutorType execType, Connection connection);

    Configuration getConfiguration();
}
