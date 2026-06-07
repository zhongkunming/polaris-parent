package com.github.polaris.thread;

import cn.hutool.core.util.NumberUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

@Slf4j
public class DelegatingThreadExecutor implements ExecutorService {

    private final ExecutorService delegate;

    private final String poolName;

    private final Integer priority;

    public DelegatingThreadExecutor(ExecutorService delegate, String poolName, int priority) {
        this.delegate = delegate;
        this.poolName = poolName;
        this.priority = NumberUtil.max(Thread.MIN_PRIORITY, Math.min(Thread.MAX_PRIORITY, priority));
    }

    // 核心：执行Runnable任务，包装为PriorityTask
    @Override
    public void execute(Runnable command) {
        PriorityTask<Void> priorityTask = new PriorityTask<>(command, poolName, priority);
        delegate.execute(priorityTask);
    }

    // 包装Callable任务，适配submit(Callable)
    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return delegate.submit(wrapTask(task));
    }

    // 包装Runnable任务，适配submit(Runnable)
    @Override
    public Future<?> submit(Runnable task) {
        return delegate.submit(wrapTask(task));
    }

    // 包装Runnable任务，适配submit(Runnable, T)
    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        return delegate.submit(wrapTask(task, result), result);
    }

    // 包装Callable任务为PriorityTask
    private <T> Callable<T> wrapTask(Callable<T> task) {
        return new PriorityTask<>(task, poolName, priority);
    }

    // 包装Runnable任务为PriorityTask（无返回值）
    private Runnable wrapTask(Runnable task) {
        return new PriorityTask<>(task, poolName, priority);
    }

    // 包装Runnable任务为PriorityTask（带返回值）
    private <T> Runnable wrapTask(Runnable task, T result) {
        return new PriorityTask<>(task, poolName, priority, result);
    }

    // 以下方法均直接委托给共享线程池，无需修改（业务线程池不独立关闭）
    @Override
    public void shutdown() {
        // 业务线程池不关闭共享线程池，仅做标记
        log.info("业务线程池{}发起关闭请求%n", poolName);
    }

    @Override
    public List<Runnable> shutdownNow() {
        log.info("业务线程池{}发起强制关闭请求%n", poolName);
        return Collections.emptyList();
    }

    @Override
    public boolean isShutdown() {
        return delegate.isShutdown();
    }

    @Override
    public boolean isTerminated() {
        return delegate.isTerminated();
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return delegate.awaitTermination(timeout, unit);
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        List<Callable<T>> wrappedTasks = wrapAllTasks(tasks);
        return delegate.invokeAll(wrappedTasks);
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
        List<Callable<T>> wrappedTasks = wrapAllTasks(tasks);
        return delegate.invokeAll(wrappedTasks, timeout, unit);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        List<Callable<T>> wrappedTasks = wrapAllTasks(tasks);
        return delegate.invokeAny(wrappedTasks);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        List<Callable<T>> wrappedTasks = wrapAllTasks(tasks);
        return delegate.invokeAny(wrappedTasks, timeout, unit);
    }

    // 批量包装任务为PriorityTask
    private <T> List<Callable<T>> wrapAllTasks(Collection<? extends Callable<T>> tasks) {
        List<Callable<T>> wrapped = new ArrayList<>(tasks.size());
        for (Callable<T> task : tasks) {
            wrapped.add(wrapTask(task));
        }
        return wrapped;
    }
}
