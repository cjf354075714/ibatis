package cn.edu.cqu.learn.architecture.ibatis.transaction;

import java.sql.Connection;

/**
 * 代表着数据库连接的事务隔离级别的枚举类
 */
public enum TransactionIsolationLevel {

    NONE(Connection.TRANSACTION_NONE),
    READ_COMMITTED(Connection.TRANSACTION_READ_COMMITTED),
    READ_UNCOMMITTED(Connection.TRANSACTION_READ_UNCOMMITTED),
    REPEATABLE_READ(Connection.TRANSACTION_REPEATABLE_READ),
    SERIALIZABLE(Connection.TRANSACTION_SERIALIZABLE);

    private final int level;

    public int getLevel() {
        return level;
    }

    TransactionIsolationLevel(int level) {
        this.level = level;
    }
}
