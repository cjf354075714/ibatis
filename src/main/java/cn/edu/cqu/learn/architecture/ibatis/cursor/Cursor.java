package cn.edu.cqu.learn.architecture.ibatis.cursor;


/**
 * SQL 查询的光标？有啥用？
 */
public interface Cursor<T> extends Cloneable, Iterable<T> {
    boolean isOpen();
    boolean isConsumed();
    int getCurrentIndex();
}
