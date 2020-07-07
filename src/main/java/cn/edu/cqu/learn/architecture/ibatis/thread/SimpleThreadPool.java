package cn.edu.cqu.learn.architecture.ibatis.thread;

import java.util.concurrent.*;

/**
 * 自定义线程池，好像细节才能让我去实现
 */
public class SimpleThreadPool extends ThreadPoolExecutor {

    /**
     * @param corePoolSize 核心线程数量，一直存在
     * @param maximumPoolSize 最大线程数量
     * @param keepAliveTime 任务干完了之后，还存在多久
     * @param unit 时间单位
     * @param workQueue 工作队列，就是待完成的任务，是否可以用 QAS 去抽象这个队列
     * @param threadFactory 构建线程的工厂
     * @param handler 如果执行器没了，拒绝策略
     */
    private SimpleThreadPool(
            int corePoolSize,
            int maximumPoolSize,
            long keepAliveTime,
            TimeUnit unit,
            BlockingQueue<Runnable> workQueue,
            ThreadFactory threadFactory,
            RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    private static class RejectedHandle implements RejectedExecutionHandler {

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            
        }
    }
}
