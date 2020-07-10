package cn.edu.cqu.learn.architecture.ibatis.session;

import java.sql.Connection;

/**
 * 数据库事务隔离级别
 */
public enum TransactionIsolationLevel {
    // 不支持事务
    NONE(Connection.TRANSACTION_NONE),

    // 读已提交，自己设置成这个级别，就能读到别人的还没有提交的数据
    READ_UNCOMMITTED(Connection.TRANSACTION_READ_UNCOMMITTED),

    // 读已提交，只能读取到别人已经提交的数据
    READ_COMMITTED(Connection.TRANSACTION_READ_COMMITTED),

    // 可重复度
    REPEATABLE_READ(Connection.TRANSACTION_REPEATABLE_READ),

    // 串行化，保证数据安全
    SERIALIZABLE(Connection.TRANSACTION_SERIALIZABLE);

    private final int level;

    TransactionIsolationLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}
