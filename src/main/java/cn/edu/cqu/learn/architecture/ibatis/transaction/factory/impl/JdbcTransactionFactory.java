package cn.edu.cqu.learn.architecture.ibatis.transaction.factory.impl;

import cn.edu.cqu.learn.architecture.ibatis.transaction.Transaction;
import cn.edu.cqu.learn.architecture.ibatis.transaction.TransactionIsolationLevel;
import cn.edu.cqu.learn.architecture.ibatis.transaction.factory.TransactionFactory;
import cn.edu.cqu.learn.architecture.ibatis.transaction.impl.JdbcTransaction;

import javax.sql.DataSource;
import java.sql.Connection;

public class JdbcTransactionFactory implements TransactionFactory {

    @Override
    public Transaction newTransaction(Connection connection) {
        return new JdbcTransaction(connection);
    }

    @Override
    public Transaction newTransaction(DataSource dataSource, TransactionIsolationLevel level, boolean autoCommit) {
        return new JdbcTransaction(dataSource, level, autoCommit);
    }
}
