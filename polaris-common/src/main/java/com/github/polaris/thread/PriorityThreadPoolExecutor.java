package com.github.polaris.thread;

import java.util.concurrent.*;

public class PriorityThreadPoolExecutor extends ThreadPoolExecutor {

    private static final long KEEP_ALIVE_TIME = 60L;

    // 业务线程池名称（用于标记，单例共享时可省略）
    private final String poolName;

    public PriorityThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                      BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory,
                                      RejectedExecutionHandler handler, String poolName) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
        this.poolName = poolName;
    }

    // 静态创建共享优先级线程池
    public static ExecutorService createSharedPriorityPool(String poolName) {
        int cpuCores = Runtime.getRuntime().availableProcessors();

        // 共享线程池核心参数
        // 举例 核心线程数 2c的pod下  - 4 个核心线程 8个最大线程
        int corePoolSize = cpuCores * 2;
        int maxPoolSize = cpuCores * 4;

        // 优先级阻塞队列：无界队列（可根据业务设置有界）
        BlockingQueue<Runnable> priorityQueue = new PriorityBlockingQueue<>();
        return new PriorityThreadPoolExecutor(
                corePoolSize,
                maxPoolSize,
                KEEP_ALIVE_TIME,
                TimeUnit.SECONDS,
                priorityQueue,
                new DefaultThreadFactory(poolName),
                new CallerRunsPolicy(),
                poolName
        );
    }

    /**
     * 重写newTaskFor：将Runnable任务包装为PriorityTask
     *
     * @param runnable 原始任务
     * @param value    返回值
     * @param <T>      返回值类型
     * @return 优先级任务
     */
    @SuppressWarnings("unchecked")
    @Override
    protected <T> RunnableFuture<T> newTaskFor(Runnable runnable, T value) {
        if (runnable instanceof PriorityTask) {
            PriorityTask<T> priorityTask = (PriorityTask<T>) runnable;
            return new FutureTask<T>(runnable, value) {
                @Override
                public void run() {
                    // 执行任务时动态调整线程优先级
                    int originalPriority = Thread.currentThread().getPriority();
                    try {
                        Thread.currentThread().setPriority(priorityTask.getPriority());
                        super.run();
                    } finally {
                        Thread.currentThread().setPriority(originalPriority);
                    }
                }
            };
        }
        return super.newTaskFor(runnable, value);
    }

    /**
     * 重写newTaskFor：将Callable任务包装为PriorityTask
     */
    @Override
    protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
        if (callable instanceof PriorityTask) {
            PriorityTask<T> priorityTask = (PriorityTask<T>) callable;
            return new FutureTask<T>(callable) {
                @Override
                public void run() {
                    int originalPriority = Thread.currentThread().getPriority();
                    try {
                        Thread.currentThread().setPriority(priorityTask.getPriority());
                        super.run();
                    } finally {
                        Thread.currentThread().setPriority(originalPriority);
                    }
                }
            };
        }
        return super.newTaskFor(callable);
    }
}
