package cn.edu.cqu.learn.architecture.ibatis.executor;

import cn.edu.cqu.learn.architecture.ibatis.mapping.MappedStatement;
import cn.edu.cqu.learn.architecture.ibatis.page.RowBounds;
import cn.edu.cqu.learn.architecture.ibatis.result.ResultHandler;

import java.sql.SQLException;
import java.util.List;

/**
 * 这才是真正的执行器
 */
public interface Executor {

    // 这个模板就没有办法了嘛
    ResultHandler NO_RESULT_HANDLER = null;

    /**
     * 执行更新
     * @param ms 映射对象
     * @param parameter 参数
     * @return 执行成功的条数
     * @throws SQLException 异常
     */
    int update(MappedStatement ms, Object parameter) throws SQLException;

    <E, T> List<E> query(MappedStatement mappedStatement, Object parameter,
                         RowBounds rowBounds, ResultHandler<T> resultHandler);
}
