package cn.edu.cqu.learn.architecture.ibatis.result;

import cn.edu.cqu.learn.architecture.ibatis.mapping.MappedStatement;

import java.util.LinkedList;
import java.util.List;

/**
 * 不知道有啥用
 */
public class BatchResult {

    private final MappedStatement mappedStatement;
    private final String sql;
    private final List<Object> parameterObjects;

    private int[] updateCounts;

    public BatchResult(MappedStatement mappedStatement, String sql) {
        this.mappedStatement = mappedStatement;
        this.sql = sql;
        this.parameterObjects = new LinkedList<>();
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
