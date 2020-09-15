package cn.edu.cqu.learn.architecture.ibatis.transaction.factory;


import cn.edu.cqu.learn.architecture.ibatis.transaction.Transaction;
import cn.edu.cqu.learn.architecture.ibatis.transaction.TransactionIsolationLevel;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Properties;

/**
 * 事务的构建工厂
 */
public interface TransactionFactory {

    default void setProperties(Properties properties){}

    Transaction newTransaction(Connection connection);

    Transaction newTransaction(DataSource dataSource, TransactionIsolationLevel level, boolean autoCommit);
}
