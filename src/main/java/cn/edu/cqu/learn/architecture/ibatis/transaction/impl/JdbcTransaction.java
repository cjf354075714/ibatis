package cn.edu.cqu.learn.architecture.ibatis.transaction.impl;

import cn.edu.cqu.learn.architecture.ibatis.transaction.Transaction;
import cn.edu.cqu.learn.architecture.ibatis.transaction.TransactionIsolationLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Connection 对象在 Mybatis 中的具体包装类
 * @see Connection
 *
 * 其实我好奇，为啥不是 PooledConnection 的包装类
 *
 * 这个对象为什么要这样写？
 * 是因为，他有两种构建方式，以 Connection 对象最为核心
 * 第一种构建方式是直接传入一个 Connection 对象
 * 第二种方式是通过传入 DataSource、Level、autoComit，本质上还是从
 * 数据库连接池中去获取对象
 */
public class JdbcTransaction implements Transaction {

    private static final Logger SLF4J;

    static {
        SLF4J = LoggerFactory.getLogger(JdbcTransaction.class);
    }

    protected Connection connection;

    protected DataSource dataSource;

    protected TransactionIsolationLevel level;

    protected boolean autoCommit;

    public JdbcTransaction(DataSource dataSource, TransactionIsolationLevel level, boolean autoCommit) {
        this.dataSource = dataSource;
        this.level = level;
        this.autoCommit = autoCommit;
    }

    public JdbcTransaction(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Connection getConnection() throws SQLException {
        if (null == connection) {
            openConnection();
        }
        return connection;
    }

    @Override
    public void commit() throws SQLException {
        if (null != connection) {
            if (!connection.getAutoCommit()) {
                // 当前连接对象还在，并且没有设置成自动提交
                SLF4J.debug("提交 Connection 对象：" + connection);
                connection.commit();
            }
        }
    }

    @Override
    public void rollback() throws SQLException {
        if (null != connection) {
            // 其实不管这个对象是不是自动提交，都可以回滚
            // 只是这里，自动提交的对象，将不会被回滚
            if (!connection.getAutoCommit()) {
                SLF4J.debug("回滚 Connection 对象：" + connection);
                connection.rollback();
            }
        }
    }

    @Override
    public void close() throws SQLException {
        if (null != connection) {
            SLF4J.debug("关闭连接对象：" + connection);
            resetAutoCommit();
            connection.close();
        }
    }

    @Override
    public Integer getTimeout() throws SQLException {
        return null;
    }

    protected void openConnection() throws SQLException {
        SLF4J.debug("从数据库连接池中去获取 Connection 对象");
        connection = dataSource.getConnection();
        if (null != level) {
            connection.setTransactionIsolation(level.getLevel());
        }
        setDesiredAutoCommit(autoCommit);
    }

    protected void setDesiredAutoCommit(boolean desiredAutoCommit) {
        try {
            if (desiredAutoCommit != connection.getAutoCommit()) {
                SLF4J.debug("给 Connection 对象设置自动提交：{}", desiredAutoCommit);
                connection.setAutoCommit(desiredAutoCommit);
            }
        } catch (SQLException e) {
            throw new RuntimeException("当前数据库驱动可能不支持自动提交：", e);
        }
    }

    /**
     * 这个是对某些数据库而言的，我们并不需要知道细节
     */
    protected void resetAutoCommit() {
        try {
            if (!connection.getAutoCommit()) {
                SLF4J.debug("重置 Connection 的自动提交：" + connection);
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            SLF4J.debug("在关闭 Connection 对象之前，重置自动提交失败：{}", e.getMessage());
        }
    }
}
