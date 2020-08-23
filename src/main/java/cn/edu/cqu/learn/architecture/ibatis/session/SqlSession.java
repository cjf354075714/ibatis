package cn.edu.cqu.learn.architecture.ibatis.session;

import cn.edu.cqu.learn.architecture.ibatis.core.Configuration;
import cn.edu.cqu.learn.architecture.ibatis.cursor.Cursor;
import cn.edu.cqu.learn.architecture.ibatis.page.RowBounds;
import cn.edu.cqu.learn.architecture.ibatis.result.BatchResult;
import cn.edu.cqu.learn.architecture.ibatis.result.ResultHandler;

import java.io.Closeable;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 * Mybatis 中执行 SQL 的唯一对外接口
 */
public interface SqlSession extends Closeable {

    /**
     * 查询单个业务数据，返回一个业务实体
     * @param statement SQL 语句，因该是类似于 select * from id = 1;
     * @param <T> 表示自己是一个模板方法
     * @return 返回一个业务类实例
     */
    <T> T selectOne(String statement);

    /**
     * 带参数的单个业务实体查询
     * @param statement 查询 SQL
     * @param parameter 参数，有可能是任意类型
     * @param <T> 模板方法
     * @return 一个业务实体
     */
    <T> T selectOne(String statement, Object parameter);

    /**
     * 批量查询，返回一个业务对象的 List 集合
     * @param statement 查询 SQL
     * @param <E> 模板方法
     * @return List 集合
     */
    <E> List<E> selectList(String statement);

    /**
     * 带参数的批量查询
     * @param statement SQL
     * @param parameter 参数，可以是任何类型的查询参数
     * @param <E> 模板方法
     * @return List 集合
     */
    <E> List<E> selectList(String statement, Object parameter);

    /**
     * 带参数、分页类的查询
     * @param statement SQL
     * @param parameter 参数
     * @param rowBounds 分页类
     * @param <E> 模板方法
     * @return 返回一个业务对象的 List 集合
     */
    <E> List<E> selectList(String statement, Object parameter, RowBounds rowBounds);

    /**
     * 我不知道返回的这个 Map 有啥用，应该是业务对象的 Key Value
     * 的映射集合
     * @param statement SQL
     * @param mapKey 查询传入的参数
     * @param <K> 模板方法
     * @param <V> 模板
     * @return 业务对象的 Map 类型
     */
    <K, V> Map<K, V> selectMap(String statement, String mapKey);

    /**
     * 不知道这个 parameter 和 mapKey 有啥用
     * @param statement SQL
     * @param parameter 参数
     * @param mapKey mapKey
     * @param <K> 模板
     * @param <V> 模板
     * @return 业务对象的 Map 类型
     */
    <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey);

    /**
     * 带分页查询的 selectMap
     * @param statement SQL
     * @param parameter 参数
     * @param mapKey mapKey
     * @param rowBounds 分页
     * @param <K> 模板
     * @param <V> 模板
     * @return 业务对象的 Map 类型
     */
    <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey, RowBounds rowBounds);

    /**
     * 根据 SQL 去查询出游标，我现在不知道这个游标有啥用
     * 是大量数据的时候，我不一次性查询出来，我在内存里
     * 存上这个查询的引用，然后一个一个的往下移动
     * @param statement SQL
     * @param <T> 业务对象
     * @return 包含业务对象的游标
     */
    <T> Cursor<T> selectCursor(String statement);

    /**
     * 带参数的游标查询
     * @param statement SQL
     * @param parameter 参数
     * @param <T> 业务对象
     * @return 游标
     */
    <T> Cursor<T> selectCursor(String statement, Object parameter);

    /**
     * 带参数，带分页的游标查询
     * @param statement SQL
     * @param parameter 参数
     * @param rowBounds 分页参数
     * @param <T> 业务对象
     * @return 游标
     */
    <T> Cursor<T> selectCursor(String statement, Object parameter, RowBounds rowBounds);

    /**
     * 带结果处理器的简单查询，注意，返回的结果将在这个
     * 结果处理器中
     * @param statement SQL
     * @param handler 结果处理器
     * @param <T> 模板
     */
    <T> void select(String statement, ResultHandler<T> handler);

    /**
     * 带结果处理器的简单查询，注意，返回的结果将在这个
     * 结果处理器中
     * @param statement SQL
     * @param parameter 参数
     * @param handler 结果处理器
     * @param <T> 模板
     */
    <T> void select(String statement, Object parameter, ResultHandler<T> handler);

    /**
     * 带结果处理器和分页的简单查询，注意，返回的结果将在这个
     * 结果处理器中
     * @param statement SQL
     * @param parameter 参数
     * @param rowBounds 分页
     * @param handler 结果处理器
     * @param <T> 模板
     */
    <T> void select(String statement, Object parameter, RowBounds rowBounds, ResultHandler<T> handler);

    /**
     * 简单插入
     * @param statement SQL
     * @return 插入成功条数
     */
    int insert(String statement);

    /**
     * 带参数的插入
     * @param statement SQL
     * @param parameter 参数
     * @return 插入成功条数
     */
    int insert(String statement, Object parameter);

    /**
     * 简单更新
     * @param statement SQL
     * @return 更新成功条数
     */
    int update(String statement);

    /**
     * 带参数的更新
     * @param statement SQL
     * @param parameter 参数
     * @return 更新成功条数
     */
    int update(String statement, Object parameter);

    /**
     * 简单删除
     * @param statement SQL
     * @return 删除条数
     */
    int delete(String statement);

    /**
     * 带参数的删除
     * @param statement SQL
     * @param parameter 参数
     * @return 删除条数
     */
    int delete(String statement, Object parameter);

    /**
     * 提交
     */
    void commit();

    /**
     * 是否强制提交
     * @param force 强制提交
     */
    void commit(boolean force);

    /**
     * 回滚
     */
    void rollback();

    /**
     * 强制回滚
     * @param force 强制回滚
     */
    void rollback(boolean force);

    /**
     * 执行所有的 Mapper 的节点 SQL
     * 并返回执行的结果
     * @return 执行结果的集合
     */
    List<BatchResult> flushStatements();

    /**
     * 清理 sqlSession 级别的缓存
     */
    void clearCache();

    Configuration getConfiguration();

    /**
     * 现在还是不能理解这个 Mapper 有什么用
     * @param type 类型
     * @param <T> 模板
     * @return Mapper
     */
    <T> T getMapper(Class<T> type);

    Connection getConnection();

    @Override
    void close();
}
