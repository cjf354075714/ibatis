package cn.edu.cqu.learn.architecture.ibatis.result;

public interface ResultHandler<T> {
    void handleResult(ResultContext<? extends T> resultContext);
}
