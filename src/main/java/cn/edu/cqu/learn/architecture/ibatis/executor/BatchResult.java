package cn.edu.cqu.learn.architecture.ibatis.executor;

import cn.edu.cqu.learn.architecture.ibatis.mapping.MappedStatement;

import java.util.ArrayList;
import java.util.List;

/**
 * 批量执行结果返回所需要的类
 */
public class BatchResult {
    // 因为是一个节点的执行结果，就是下面这个节点
    private final MappedStatement mappedStatement;

    // 执行的 SQL
    private final String sql;

    // 执行的参数
    private final List<Object> parameterObjects;

    // 不知道干啥的变量
    private int[] updateCounts;

    public BatchResult(MappedStatement mappedStatement, String sql) {
        super();
        this.mappedStatement = mappedStatement;
        this.sql = sql;
        // 一般的 SQL 查询，参数也不会超过 10 个
        this.parameterObjects = new ArrayList<>();
    }

    public BatchResult(MappedStatement mappedStatement, String sql, Object parameterObject) {
        this(mappedStatement, sql);
        addParameterObject(parameterObject);
    }

    public MappedStatement getMappedStatement() {
        return mappedStatement;
    }

    public String getSql() {
        return sql;
    }

    public List<Object> getParameterObjects() {
        return parameterObjects;
    }

    public int[] getUpdateCounts() {
        return updateCounts;
    }

    public void setUpdateCounts(int[] updateCounts) {
        this.updateCounts = updateCounts;
    }

    public void addParameterObject(Object parameterObject) {
        this.parameterObjects.add(parameterObject);
    }

}
