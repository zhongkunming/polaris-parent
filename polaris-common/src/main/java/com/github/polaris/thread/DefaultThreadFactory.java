package com.github.polaris.thread;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class DefaultThreadFactory implements ThreadFactory {

    // 线程池内线程计数器
    private final AtomicInteger threadNum = new AtomicInteger(1);

    // 业务线程池名称（标记线程归属）
    private final String poolName;

    // 线程优先级
    private final int priority;

    public DefaultThreadFactory(String poolName) {
        this(poolName, Thread.NORM_PRIORITY); // 默认优先级5
    }

    public DefaultThreadFactory(String poolName, int priority) {
        this.poolName = poolName;
        // 校验优先级范围（1-10），避免非法值
        this.priority = NumberUtil.max(Thread.MIN_PRIORITY, Math.min(Thread.MAX_PRIORITY, priority));
    }

    @Override
    public Thread newThread(@NonNull Runnable runnable) {
        Thread thread = new Thread(runnable, String.format("%s-worker-%d", poolName, threadNum.getAndIncrement()));
        thread.setDaemon(false); // 非守护线程，确保任务执行完成
        thread.setPriority(this.priority); // 设置统一优先级
        // 可选：设置线程未捕获异常处理器，避免线程异常退出无感知
        thread.setUncaughtExceptionHandler((t, e) -> log.error(StrUtil.format("线程{}执行异常", t.getName()), e));
        return thread;
    }
}


