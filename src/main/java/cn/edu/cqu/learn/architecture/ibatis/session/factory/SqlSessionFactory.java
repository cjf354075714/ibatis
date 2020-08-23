package cn.edu.cqu.learn.architecture.ibatis.session.factory;

import cn.edu.cqu.learn.architecture.ibatis.core.Configuration;
import cn.edu.cqu.learn.architecture.ibatis.enums.ExecutorType;
import cn.edu.cqu.learn.architecture.ibatis.transaction.TransactionIsolationLevel;
import cn.edu.cqu.learn.architecture.ibatis.session.SqlSession;

import java.sql.Connection;

public interface  SqlSessionFactory {
    SqlSession openSession();

    SqlSession openSession(boolean autoCommit);

    SqlSession openSession(Connection connection);

    SqlSession openSession(TransactionIsolationLevel level);

    SqlSession openSession(ExecutorType execType);

    SqlSession openSession(ExecutorType execType, boolean autoCommit);

    SqlSession openSession(ExecutorType execType, TransactionIsolationLevel level);

    SqlSession openSession(ExecutorType execType, Connection connection);

    Configuration getConfiguration();
}
