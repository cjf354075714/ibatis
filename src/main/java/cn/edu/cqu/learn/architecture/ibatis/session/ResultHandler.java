package cn.edu.cqu.learn.architecture.ibatis.session;

/**
 * 结果处理器接口
 * @param <T>
 */
public interface ResultHandler<T> {
    // 处理结果
    void handleResult(ResultContext<? extends T> resultContext);
}
