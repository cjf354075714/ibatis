package cn.edu.cqu.learn.architecture.ibatis.session;

/**
 * 不知道有啥用
 * @param <T> 默认对象
 */
public interface ResultContext<T> {
    T getResultObject();

    int getResultCount();

    boolean isStopped();

    void stop();
}
