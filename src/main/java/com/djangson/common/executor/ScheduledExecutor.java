package com.djangson.common.executor;


import com.djangson.common.constant.Constants;
import com.djangson.common.util.LoggerUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 定时（延时）任务线程池
 * @Author: wangqinjun@vichain.com
 * @Date: 2020/4/24 22:35
 */
public class ScheduledExecutor {

    // 任务延迟执行线程池
    private ScheduledThreadPoolExecutor executor;
    private static Map<Object, Map<Object, ScheduledTask>> executorTaskMap = new ConcurrentHashMap<>();

    /**
     * 构造器
     * @param executor
     */
    public ScheduledExecutor(ScheduledThreadPoolExecutor executor) {
        this.executor = executor;
        this.executor.allowCoreThreadTimeOut(true);
        this.executor.setRemoveOnCancelPolicy(true);
        executorTaskMap.put(executor.hashCode(), new ConcurrentHashMap<>());
    }

    /**
     * 添加任务到任务池
     * @param scheduledTask
     */
    public void addTask(ScheduledTask scheduledTask) {
        addTask(scheduledTask, 30);
    }

    /**
     * 添加任务到任务池
     * @param scheduledTask
     */
    public void addTask(ScheduledTask scheduledTask, long delay) {
        addTask(scheduledTask, delay, Constants.YES);
    }

    /**
     * 添加任务到任务池
     * @param scheduledTask
     */
    public ScheduledFuture addTask(ScheduledTask scheduledTask, long delay, int discardOld) {
        if (discardOld == Constants.YES) {
            scheduledTask.discardOld = discardOld;
            scheduledTask.parentHashCode = executor.hashCode();
            executorTaskMap.get(executor.hashCode()).put(scheduledTask.getHashCode(), scheduledTask);
        }
        return executor.schedule(scheduledTask, delay, TimeUnit.SECONDS);
    }

    /**
     * 移除任务池任务
     * @param scheduledTask
     */
    public boolean removeTask(ScheduledTask scheduledTask) {
        return executor.remove(scheduledTask);
    }

    /**
     * 查询剩余任务数量
     */
    public int getTaskCount() {
        return executor.getQueue().size();
    }

    /**
     * 关闭线程池
     */
    public void shutdown() {
        executorTaskMap.remove(executor.hashCode());
        this.executor.shutdown();
    }

    /**
     * 延时任务基类
     */
    public static abstract class ScheduledTask implements Runnable {

        private int discardOld;
        private int parentHashCode;

        protected abstract Object getHashCode();

        protected abstract void execute();

        protected void afterExecute() {

        }

        @Override
        public void run() {

            try {
                // 判断旧任务是否需要丢弃
                if (this.discardOld == Constants.YES) {
                    ScheduledTask scheduledTask = executorTaskMap.get(this.parentHashCode).get(this.getHashCode());
                    if (scheduledTask != this) {
                        return;
                    }
                }

                // 任务执行
                execute();

            } catch (Exception e) {
                LoggerUtil.error("线程池任务执行失败！", e);
            } finally {

                // 删除记录
                executorTaskMap.get(this.parentHashCode).remove(this.getHashCode(), this);

                this.afterExecute();
            }
        }
    }
}
