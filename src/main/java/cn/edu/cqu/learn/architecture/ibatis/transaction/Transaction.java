package cn.edu.cqu.learn.architecture.ibatis.transaction;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Mybatis 中 对事务的封装
 */
public interface Transaction {

    Connection getConnection() throws SQLException;

    void commit() throws SQLException;

    void rollback() throws SQLException;

    void close() throws SQLException;

    Integer getTimeout() throws SQLException;

}
