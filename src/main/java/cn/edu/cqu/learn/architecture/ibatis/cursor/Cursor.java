package cn.edu.cqu.learn.architecture.ibatis.cursor;

import java.io.Closeable;

/**
 * ResultSet 对象支持连接不释放向下迭代的查询
 * 运用的就是数据库的游标，当然一般我们不用游标
 * @see java.sql.ResultSet
 *
 * 当我们要查询的数据量很大的时候，查询就需要分页
 * 可是也许有一些情况，分页也不满足，而查询的连接也不会被释放
 * 这个时候我们就可以使用游标来查询
 *
 * 这个接口，就是对数据库游标的一个封装
 *
 * 既然是用于迭代的，那肯定是会实现迭代器
 */
public interface Cursor<T> extends Closeable, Iterable<T> {

    /**
     * 当前游标是否还可用
     * @return 是否可用
     */
    boolean isOpen();

    /**
     * 返回当前游标是否移动到了最后
     * @return 是否最后
     */
    boolean isConsumed();

    /**
     * 返回当前游标的索引，第一个索引是0
     * 如果没有索引，则返回 -1
     * @return 索引位
     */
    int getCurrentIndex();
}
