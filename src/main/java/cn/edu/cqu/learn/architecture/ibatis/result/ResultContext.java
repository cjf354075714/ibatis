package cn.edu.cqu.learn.architecture.ibatis.result;

/**
 * 在执行完 SQL 之后，会有一个结果，这就是那个结果的对象
 */
public interface ResultContext<T> {

    T getResultObject();

    int getResultCount();

    boolean isStopped();

    void stop();
}
