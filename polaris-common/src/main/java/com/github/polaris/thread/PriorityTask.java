package com.github.polaris.thread;

import lombok.Getter;

import java.util.Objects;
import java.util.concurrent.Callable;

/**
 * 优先级任务包装类：实现Comparable，定义任务排队优先级
 *
 * @param <T> 任务返回值类型
 */
@Getter
public class PriorityTask<T> implements Comparable<PriorityTask<T>>, Runnable, Callable<T> {

    // 任务优先级：数字越大，优先级越高（1-10）
    private final Integer priority;

    // 实际任务（Runnable/Callable）
    private final Object task;

    // 业务线程池名称（标记归属）
    private final String poolName;

    // 返回参数
    private final T result;

    // 构造方法：Runnable任务
    public PriorityTask(Runnable task, String poolName, Integer priority, T result) {
        this.task = Objects.requireNonNull(task, "任务不可为空");
        this.poolName = poolName;
        this.priority = priority;
        this.result = result;
    }

    public PriorityTask(Runnable task, String poolName, Integer priority) {
        this(task, poolName, priority, null);
    }

    // 构造方法：Callable任务
    public PriorityTask(Callable<T> task, String poolName, Integer priority) {
        this.task = Objects.requireNonNull(task, "任务不可为空");
        this.poolName = poolName;
        this.priority = priority;
        this.result = null;
    }

    // 核心：定义任务比较规则（按优先级降序，优先级相同按池名排序）
    @Override
    public int compareTo(PriorityTask<T> o) {
        // 降序：当前优先级 > 目标优先级，返回-1（排在前面）
        int priorityCompare = Integer.compare(o.priority, this.priority);
        if (priorityCompare != 0) {
            return priorityCompare;
        }
        // 优先级相同时，按业务池名排序（保证排序的稳定性）
        return this.poolName.compareTo(o.poolName);
    }

    @Override
    public void run() {
        if (task instanceof Runnable) {
            ((Runnable) task).run();
        } else {
            throw new UnsupportedOperationException("当前任务类型不为 Runnable 类型，无法调用run方法");
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public T call() throws Exception {
        if (task instanceof Callable) {
            return ((Callable<T>) task).call();
        } else if (task instanceof Runnable) {
            ((Runnable) task).run();
            return result;
        }
        throw new UnsupportedOperationException("任务类型不支持");
    }
}

