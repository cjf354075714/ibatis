package cn.edu.cqu.learn.architecture.ibatis.core;


import cn.edu.cqu.learn.architecture.ibatis.transaction.factory.TransactionFactory;

import javax.sql.DataSource;

/**
 * 核心类1：Environment
 * id、事务工厂和数据库连接池
 */
public final class Environment {

    private final String id;

    private final TransactionFactory transactionFactory;

    private final DataSource dataSource;

    public Environment(String id, TransactionFactory transactionFactory, DataSource dataSource) {
        if ( null == id ) {
            throw new RuntimeException("构建 Environment 失败：id不能为空");
        }
        if ( null == transactionFactory ) {
            throw new RuntimeException("构建 Environment 失败：transactionFactory不能为空");
        }
        if ( null == dataSource ) {
            throw new RuntimeException("构建 Environment 失败：dataSource不能为空");
        }
        this.id = id;
        this.transactionFactory = transactionFactory;
        this.dataSource = dataSource;
    }

    public static class Builder {
        private final String id;
        private TransactionFactory transactionFactory;
        private DataSource dataSource;

        public Builder(String id) {
            this.id = id;
        }

        public Builder transactionFactory(TransactionFactory transactionFactory) {
            this.transactionFactory = transactionFactory;
            return this;
        }

        public Builder dataSource(DataSource dataSource) {
            this.dataSource = dataSource;
            return this;
        }

        public String id() {
            return this.id;
        }

        public Environment build() {
            return new Environment(this.id, this.transactionFactory, this.dataSource);
        }

    }

    public String getId() {
        return this.id;
    }

    public TransactionFactory getTransactionFactory() {
        return this.transactionFactory;
    }

    public DataSource getDataSource() {
        return this.dataSource;
    }
}
